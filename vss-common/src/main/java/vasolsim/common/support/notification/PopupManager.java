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

package main.java.vasolsim.common.support.notification;

import javax.swing.*;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p>A small wrapper for generating popups and prompts.</p>
 */
public class PopupManager
{
	/**
	 * creates a yes or no prompt
	 * @param message the displayed message/question
	 * @return the boolean value of the response
	 */
	public static boolean askYesNo(String message)
	{
		return (JOptionPane.showConfirmDialog(
				null,
				message,
				"Request",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE)
				== JOptionPane.YES_OPTION);
	}

	/**
	 * shows a message prompt
	 * @param message the message
	 */
	public static void showMessage(String message)
	{
		showMessage(message, "Prompt");
	}

	/**
	 * shows a message prompt
	 * @param message the message
	 * @param title the title of the prompt
	 */
	public static void showMessage(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}
}
