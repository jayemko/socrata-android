package com.jayemko.socrata.android.api;

public class Response
{
    public int status;
    public String body;

    public Response(int status, String body)
    {
        this.status = status;
        this.body = body;
    }
}
