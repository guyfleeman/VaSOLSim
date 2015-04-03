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

package main.java.vasolsim.common.notification.remote;

import java.io.File;

/**
 * @author willstuckey
 * @date 3/11/15 <p></p>
 */
public class RemoteDependentNotifier implements RemoteNotifier
{
	public boolean sendRemoteNotification(String subject, String body)
	{
		return sendRemoteNotification(subject, body, null);
	}

	public boolean sendRemoteNotification(String subject, String body, File[] attachments)
	{
		return false;
	}
}
