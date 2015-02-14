package main.java.vasolsim.sclient.auth;

import javax.annotation.Nonnull;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public interface RemoteUserAuthenticator
{
	@Nonnull
	public VSSAuthToken authenticateUser() throws VSSAuthenticationException;
}
