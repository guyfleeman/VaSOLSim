package com.gmail.guyfleeman.vasolsim.common;

/**
 * @author guyfleeman
 * @date 7/2/14
 * <p></p>
 */
public class MalformedXMLException extends VaSolSimException
{
    /**
     * @param message
     */
    public MalformedXMLException(String message)
    {
        super(message);
    }

    /**
     * @param cause
     */
    public MalformedXMLException(Throwable cause)
    {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public MalformedXMLException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
