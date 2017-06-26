package com.example.qenawi.am.modles;

/**
 * Created by QEnawi on 4/11/2017.
 */

public class MyPair
{
    public MyPair()
    {}


    public String getEmailBase() {
        return emailBase;
    }

    public String getEmailPair() {
        return emailPair;
    }
    private  String emailBase;
    private  String emailPair;

    public String getSecret() {
        return secret;
    }

    private String secret;
    public MyPair(String emailBase, String emailPair,String secret)
    {
        this.emailBase = emailBase;
        this.emailPair = emailPair;
        this.secret=secret;
    }
}
