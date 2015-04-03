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

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public class VSSAuthenticationException extends Exception
{
	public static int lastErrorCode;
	public static String lastErrorMessage;

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
