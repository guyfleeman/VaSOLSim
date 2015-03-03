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

import javafx.concurrent.Task;

/**
 * @author willstuckey
 * @date 11/8/14
 * <p>Allows external logging to the task.</p>
 */
public abstract class ExternalTask<E> extends Task<E>
{
	public void updateMessage(String message)
	{
		super.updateMessage(message);
	}

	public void updateProgress(double current, double max)
	{
		super.updateProgress(current, max);
	}

	public void updateProgress(long current, long max)
	{
		super.updateProgress(current, max);
	}
}
