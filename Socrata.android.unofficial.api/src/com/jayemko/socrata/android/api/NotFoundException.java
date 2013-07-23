package com.jayemko.socrata.android.api;

/**
 * Resources that return a 404 throw this exception.
 */
public class NotFoundException extends RequestException
{
    public NotFoundException(String s)
    {
        super(s);
    }
}
