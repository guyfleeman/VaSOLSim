package com.vasolsim.common;

/**
 * @author guyfleeman
 * @date 6/27/14 <p>VaSolException is thrown whenever teh requirements or rules of teh VaSolSim program are
 * violated.</p>
 */
public class VaSolSimException extends Exception
{
	/**
	 * @param message
	 */
	public VaSolSimException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public VaSolSimException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public VaSolSimException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
