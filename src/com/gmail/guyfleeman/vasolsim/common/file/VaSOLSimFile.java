package com.gmail.guyfleeman.vasolsim.common.file;

import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p></p>
 */
public class VaSOLSimFile
{
	private static final String ERROR_MESSAGE_FILE_ALREADY_EXISTS =
			"File already exists and overwrite not permitted.";
	private static final String ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK =
			"File not found after internal check. Try running as admin. Is this a bug or a file permissions error?";
	private static final String ERROR_MESSAGE_COULD_NOT_CREATE_DIRS =
			"Could not create file directory. Do you have permission to create a directory on your machine? " +
					"(try running the jar as admin)";
	private static final String ERROR_MESSAGE_COULD_NOT_CREATE_FILE =
			"Could not create file. Do you have permission to create a file on your machine? " +
					"(try running the jar as admin)";
	private static final String ERROR_MESSAGE_CREATE_FILE_EXCEPTION =
			"File creation internal exception. Do you have permission to create a file on your machine? Could this be " +
					"a bug? (try running the jar as admin)";

	public void read() throws IOException
	{

	}

	public boolean write(File simFile)
	{
		return write(simFile, false);
	}

	public boolean write(File simFile, boolean canOverwriteFile)
	{
		if (simFile.isFile())
		{
			if (!canOverwriteFile)
			{
				PopupManager.showMessage(ERROR_MESSAGE_FILE_ALREADY_EXISTS);
				return false;
			}
			else
			{
				PrintWriter printWriter;
				try
				{
					printWriter = new PrintWriter(simFile);
				}
				catch (FileNotFoundException e)
				{
					PopupManager.showMessage(ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK);
					return false;
				}

				printWriter.print("");
				printWriter.close();
			}
		}
		else
		{
			if (simFile.getParentFile().isDirectory() || !simFile.getParentFile().mkdirs())
			{
				PopupManager.showMessage(ERROR_MESSAGE_COULD_NOT_CREATE_DIRS);
				return false;
			}

			try
			{
				if (!simFile.createNewFile())
				{
				 	PopupManager.showMessage(ERROR_MESSAGE_COULD_NOT_CREATE_FILE);
					return false;
				}
			}
			catch (IOException e)
			{
				PopupManager.showMessage(ERROR_MESSAGE_CREATE_FILE_EXCEPTION);
				return false;
			}
		}

		//TODO write xml

		return true;
	}
}

