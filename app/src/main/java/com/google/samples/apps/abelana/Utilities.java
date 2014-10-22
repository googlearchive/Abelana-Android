package com.google.samples.apps.abelana;

import com.google.identitytoolkit.client.GitkitClient;

import static android.util.Base64.NO_PADDING;
import static android.util.Base64.NO_WRAP;
import static android.util.Base64.URL_SAFE;
import static android.util.Base64.encodeToString;

/**
 * Created by zafir on 9/29/14.
 */
public class Utilities {
    static String getLocalID() {
        GitkitClient client = LoginActivity.client;
        return null;
    }

    static String base64Encoding(String str) {
        byte[] bytes = str.getBytes();
        return encodeToString(bytes, NO_PADDING | NO_WRAP | URL_SAFE);
    }
}
