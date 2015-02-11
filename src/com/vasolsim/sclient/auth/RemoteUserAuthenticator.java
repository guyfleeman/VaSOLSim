package com.vasolsim.sclient.auth;

import com.sun.istack.internal.NotNull;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public interface RemoteUserAuthenticator
{
	@NotNull
	public VSSAuthToken authenticateUser() throws VSSAuthenticationException;
}
