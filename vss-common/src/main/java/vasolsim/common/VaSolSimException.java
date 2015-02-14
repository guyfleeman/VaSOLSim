package main.java.vasolsim.common;

/**
 * @author guyfleeman
 * @date 6/27/14
 * <p>VaSolException is thrown whenever VaSolSim is violated. It is also used as a wrapper to report internal exception
 * in a friendlier manner. Many crypto/sec exceptions are wrapped by this class.</p>
 */
public class VaSolSimException extends Exception
{
	/**
	 * @param message the message
	 */
	public VaSolSimException(String message)
	{
		super(message);
	}

	/**
	 * @param cause the cause
	 */
	public VaSolSimException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message the
	 * @param cause
	 */
	public VaSolSimException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
