package com.example.qenawi.am.modles;

/**
 * Created by QEnawi on 4/11/2017.
 */

public class Statusm
{
    public Statusm(){}

    public Statusm(String statusGif, String email)
    {
        this.statusGif = statusGif;
        this.email = email;
    }
    public String getStatusGif()
    {
        return statusGif;
    }
    public String getEmail()
    {
        return email;
    }
    private  String statusGif;
    private  String email;
}
