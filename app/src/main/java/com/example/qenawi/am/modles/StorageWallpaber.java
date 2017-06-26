package com.example.qenawi.am.modles;

/**
 * Created by QEnawi on 4/15/2017.
 */

public class StorageWallpaber
{
    public String getUri()//-><
    {
        return uri;
    }

    private  String uri;

    public String getUri_camo()
    {
        return uri_camo;
    }

    private String uri_camo;

    public StorageWallpaber(String uri,String uri_camo) {
        this.uri_camo=uri_camo;this.uri = uri;
    }
    public StorageWallpaber(){}
}
