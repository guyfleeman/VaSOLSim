package com.vasolsim.common.notification;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author willstuckey
 * @date 11/12/14 <p></p>
 */
public class DebugWindow extends ByteArrayOutputStream
{
	private boolean hasBeenStaged = false;

	protected Stage debugWindow;
	protected Scene    debugScene;
	protected TextArea textArea;

	public static int maxCharacterLength = 10000;

	public DebugWindow(boolean show)
	{
		super();

		if (show)
			initialize();
	}

	public void flush() throws IOException
	{
		if (textArea != null)
		{
			super.flush();
			textArea.appendText(super.toString());
			super.reset();
		}
	}

	public void initialize()
	{
		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.textProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String s, String s2)
			{
				if (textArea.getText().length() > maxCharacterLength)
					textArea.setText(
							textArea.getText().substring(textArea.getText().length() - maxCharacterLength,
							                             maxCharacterLength));
			}
		});

		debugWindow = new Stage();
		debugScene = new Scene(textArea, 600, 380);
		debugWindow.setScene(debugScene);

		hasBeenStaged = true;
	}

	public void show()
	{
		if (!hasBeenStaged)
			initialize();

		debugWindow.show();
	}

	public void hide()
	{
		if (hasBeenStaged)
			debugWindow.hide();
	}

	public void toggle()
	{
		if (!hasBeenStaged || !debugWindow.isShowing())
			show();
		else
			hide();
	}
}
