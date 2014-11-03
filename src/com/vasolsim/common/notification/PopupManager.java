package com.vasolsim.common.notification;

import javax.swing.*;

/**
 * @author guyfleeman
 * @date 6/25/14 <p></p>
 */
public class PopupManager
{
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

	public static void showMessage(String message)
	{
		showMessage(message, "Message");
	}

	public static void showMessage(String message, String title)
	{
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}
}
