package com.vasolsim.sclient.auth;

import com.sun.istack.internal.NotNull;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public class DefaultRemoteUserAuthenticator implements RemoteUserAuthenticator
{
	@NotNull
	public VSSAuthToken authenticateUser() throws VSSAuthenticationException
	{
		throw new VSSAuthenticationException("Client does not yet support remote authorization.");
	}
}
