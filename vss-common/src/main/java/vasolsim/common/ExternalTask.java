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
