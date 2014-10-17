package com.google.samples.apps.cloudlaunch;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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
 * Created by lesv on 10/9/14.
 * This make take about 300ms seconds to complete, but it will only run once.
 */
public class AbelanaThings  {

    public static Storage storage = null;

    private static final String MY_PASSWD = "notasecret";       // FIXME TODO
    private static GoogleCredential credential;

    public AbelanaThings(Activity activity, String phint ) {
        final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        final HttpTransport httpTransport = new NetHttpTransport();
        Resources r = activity.getResources();

        if (storage == null) {
            try {
                KeyStore keystore = KeyStore.getInstance("PKCS12");
                keystore.load(r.openRawResource(R.raw.abelana), MY_PASSWD.toCharArray());

                credential = new GoogleCredential.Builder()
                        .setTransport(httpTransport)
                        .setJsonFactory(jsonFactory)
                        .setServiceAccountId(r.getString(R.string.service_account))
                        .setServiceAccountScopes(
                                Collections.singleton(StorageScopes.DEVSTORAGE_FULL_CONTROL))
                        .setServiceAccountPrivateKey(
                                (PrivateKey) keystore.getKey("privatekey", MY_PASSWD.toCharArray()))
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
        }
    }

    public static String getImage(String name) {
        final String Bucket = "abelana";
        DateTime soon = DateTime.now(DateTimeZone.UTC).plusMinutes(3);
        long expires = soon.getMillis()/1000;
        String stringToSign = "GET\n\n\n"
                + expires +"\n"
                + "/"+Bucket+"/"+name+".webp";

        String uri = "https://storage.googleapis.com/abelana/"+name+".webp"
                + "?GoogleAccessId="+credential.getServiceAccountId()
                +"&Expires="+expires
                +"&Signature="+Uri.encode(signData(
                stringToSign));
        return  uri;

    }

    private static Signature signer = null;     // Cache the Signature

    private static String signData(String data) {
        try {
            if (signer == null) {
                signer = Signature.getInstance("SHA256withRSA");
                signer.initSign(credential.getServiceAccountPrivateKey());
            }
            signer.update(data.getBytes("UTF-8"));
            byte[] rawSignature = signer.sign();
            String encodedSignature = new String(Base64.encodeBase64(rawSignature, false), "UTF-8");
            return encodedSignature;
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
