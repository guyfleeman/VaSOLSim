package com.gmail.guyfleeman.vasolsim.common.notification;

import javax.swing.*;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p></p>
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
		JOptionPane.showMessageDialog(null, message);
	}
}
