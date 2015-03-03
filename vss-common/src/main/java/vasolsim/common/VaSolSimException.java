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
