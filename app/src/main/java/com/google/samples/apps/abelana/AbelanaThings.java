/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.abelana;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Base64;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Collections;


/**
 * This make take about 300ms seconds to complete, but it will only run once.
 */
public class AbelanaThings  {


    public static Storage storage;

    private static GoogleCredential credential;

    public static AbelanaUser start(Context ctx, String atok, String secret) {
        AbelanaUser user = new AbelanaUser();

        String[] part = atok.split("\\.");
        byte[] jb = Base64.decode(part[1], Base64.URL_SAFE);
        String json = new String(jb);
        byte[] half = Base64.decode(secret, Base64.URL_SAFE);
        String halfpw = new String(half);

        try {
            JSONObject pojo = new JSONObject(json);
            user.UserID = pojo.getString("UserID");
            user.Exp = pojo.getLong("Exp");
            user.Iat = pojo.getLong("Iat");
        } catch (JSONException e) {
            System.out.println("Abelana User - convert json "+e.toString());
            return null;
        }
        if( storage == null) {
            AbelanaThings at = new AbelanaThings(ctx, halfpw);
        }
        return user;
    }

    public AbelanaThings(Context ctx, String phint ) {
        final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        final HttpTransport httpTransport = new NetHttpTransport();
        Resources r = ctx.getResources();
        byte[] android, server;
        byte[] password = new byte[32];

        android = Base64.decode("vW7CmbQWdPjpdfpBU39URsjHQV50KEKoSfafHdQPSh8",
                Base64.URL_SAFE+Base64.NO_PADDING+Base64.NO_WRAP);
        server = Base64.decode(phint, Base64.URL_SAFE);

        int i = 0;
        for(byte b : android) {
            password[i] = (byte)(android[i] ^ server[i]);
            i++;
        }
        byte[] pw = Base64.encode(password, Base64.URL_SAFE+Base64.NO_PADDING+Base64.NO_WRAP);
        String pass = new String(pw);

        if (storage == null) {
            try {
                KeyStore keystore = KeyStore.getInstance("PKCS12");
                keystore.load(r.openRawResource(R.raw.abelananew), pass.toCharArray());

                 credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setServiceAccountId(r.getString(R.string.service_account))
                        .setServiceAccountScopes(
                                Collections.singleton(StorageScopes.DEVSTORAGE_FULL_CONTROL))
                        .setServiceAccountPrivateKey(
                                (PrivateKey) keystore.getKey("privatekey", pass.toCharArray()))
                        .build();

                storage = new Storage.Builder( httpTransport, jsonFactory, credential)
                        .setApplicationName(r.getString(R.string.app_name) + "/1.0")
                        .build();

            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (UnrecoverableKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("loaded");
        }
    }

    /*Creates the URL for image at the given input string, which represents a photo ID
     * or user ID
     */

    public static String getImage(String name) {
        final String Bucket = "abelana";
        DateTime soon = DateTime.now(DateTimeZone.UTC).plusMinutes(20);
        long expires = soon.getMillis()/1000;
        String stringToSign = "GET\n\n\n"
                + expires +"\n"
                + "/"+Bucket+"/"+name+".webp";

        String uri = "https://storage.googleapis.com/abelana/"+name+".webp"
                + "?GoogleAccessId="+credential.getServiceAccountId()
                +"&Expires="+expires
                +"&Signature="+Uri.encode(signData(stringToSign));

        return  uri;

    }

    public static String extractPhotoID(String url) {
        String beginTarget = "abelana/";
        String endTarget = ".webp";
        int beginIndex = url.indexOf(beginTarget) + beginTarget.length();
        int endIndex = url.indexOf(endTarget);
        if (beginIndex != -1 && endIndex != -1) {
            return url.substring(beginIndex, endIndex);
        } else {
            return null;
        }
    }

    private static Signature signer;     // Cache the Signature

    private static String signData(String data) {
        try {
            if (signer == null) {
                signer = Signature.getInstance("SHA256withRSA");
                signer.initSign(credential.getServiceAccountPrivateKey());
            }
            signer.update(data.getBytes("UTF-8"));
            byte[] rawSignature = signer.sign();
            return new String(Base64.encode(rawSignature, Base64.DEFAULT ));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

}
