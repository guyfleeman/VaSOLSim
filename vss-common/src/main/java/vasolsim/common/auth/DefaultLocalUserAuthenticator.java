/*
 * Copyright (c) 2015.
 *
 *     This file is part of VaSOLSim.
 *
 *     VaSOLSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     VaSOLSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with VaSOLSim.  If not, see <http://www.gnu.org/licenses/>.
 */

package main.java.vasolsim.common.auth;

import javax.annotation.Nonnull;

/**
 * @author willstuckey
 * @date 3/3/15 <p></p>
 */
public class DefaultLocalUserAuthenticator implements LocalUserAuthenticator
{
	protected static DefaultLocalUserAuthenticator instance;

	private DefaultLocalUserAuthenticator() {}

	static
	{
		instance = new DefaultLocalUserAuthenticator();
	}

	@Nonnull
	public static DefaultLocalUserAuthenticator getInstance()
	{
		return instance;
	}

	public VSSAuthToken authenticateUser(String firstname, String lastname, String studentNumber) throws VSSAuthenticationException
	{
		if (firstname == null || firstname.equals(""))
		{
			VSSAuthenticationException.lastErrorMessage = "A first name must be provided";
			throw new VSSAuthenticationException("null or void first name text field");
		}

		if (lastname == null || lastname.equals(""))
		{
			VSSAuthenticationException.lastErrorMessage = "A last name must be provided";
			throw new VSSAuthenticationException("null or void last name text field");
		}

		if (studentNumber == null || studentNumber.equals(""))
		{
			VSSAuthenticationException.lastErrorMessage = "A student number must be provided";
			throw new VSSAuthenticationException("null or void student number text field");
		}

		return new VSSAuthToken(VSSAuthToken.AuthType.LOCAL);
	}
}
