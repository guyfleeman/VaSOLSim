package com.vasolsim.common.notification;

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
