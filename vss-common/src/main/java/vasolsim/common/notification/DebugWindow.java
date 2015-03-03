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

package main.java.vasolsim.common.notification;

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

	@Override
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
