package com.example.qenawi.am.DataBase;

import com.orm.SugarRecord;

/**
 * Created by QEnawi on 4/11/2017.
 */

public class MyDp extends SugarRecord
{
    public MyDp(){}

    public MyDp(String emailPair, String useremail) {
        this.emailPair = emailPair;
        this.useremail = useremail;
    }

    private String emailPair;

    public String getEmailPair() {
        return emailPair;
    }

    public String getUseremail() {
        return useremail;
    }

    private String useremail;

}
