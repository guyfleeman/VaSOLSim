package com.vasolsim.sclient.auth;

import javax.annotation.Nonnull;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public class DefaultRemoteUserAuthenticator implements RemoteUserAuthenticator
{
	@Nonnull
	public VSSAuthToken authenticateUser() throws VSSAuthenticationException
	{
		throw new VSSAuthenticationException("Client does not yet support remote authorization.");
	}
}
