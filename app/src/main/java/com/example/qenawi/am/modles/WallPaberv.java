package com.example.qenawi.am.modles;

/**
 * Created by QEnawi on 4/7/2017.
 */

public class WallPaberv
{
// Default constructor required for calls to
    // DataSnapshot.getValue(User.class)

    public WallPaberv()
    {
    }
    public WallPaberv(String email, String link)
    {
        this.email = email;
        this.link = link;
    }
    public String getEmail() {
        return email;
    }

    public String getLink() {
        return link;
    }

    private String email;
    private String link;

}
