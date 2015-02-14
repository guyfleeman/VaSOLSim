package main.java.vasolsim.common;

/**
 * @author guyfleeman
 * @date 7/2/14
 * <p>This exception is thrown if the XML of an imported is malformed, or incomplete, to a non-recoverable point.</p>
 */
public class MalformedXMLException extends VaSolSimException
{
	/**
	 * @param message the message
	 */
	public MalformedXMLException(String message)
	{
		super(message);
	}

	/**
	 * @param cause the cause
	 */
	public MalformedXMLException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message the message
	 * @param cause the cause
	 */
	public MalformedXMLException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
