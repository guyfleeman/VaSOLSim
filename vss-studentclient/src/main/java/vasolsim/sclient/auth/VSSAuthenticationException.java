package main.java.vasolsim.sclient.auth;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public class VSSAuthenticationException extends Exception
{
	public VSSAuthenticationException()
	{
		super();
	}

	public VSSAuthenticationException(String message)
	{
		super(message);
	}

	public VSSAuthenticationException(Throwable cause)
	{
		super(cause);
	}

	public VSSAuthenticationException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
