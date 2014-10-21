package com.google.samples.apps.abelana;

public class AbelanaUser {
    public String  UserID;
    public long    Iat;
    public long    Exp;

    public boolean Expired() {
        return false;
    }
}
