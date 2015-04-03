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

package main.java.vasolsim.studentclient.node;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import main.java.vasolsim.common.VaSolSimException;
import main.java.vasolsim.common.auth.VSSAuthToken;
import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.file.ExamBuilder;
import main.java.vasolsim.common.node.DrawableParent;
import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.studentclient.StudentClient;

import java.io.File;

/**
 * @author willstuckey
 * @date 3/11/15 <p></p>
 */
public class ExamSelectorNode implements DrawableParent
{
	protected Parent examSelector;

	public ExamSelectorNode()
	{
		redrawParent(false);
	}

	public void redrawParent(boolean apply)
	{
		if (apply
				&& (StudentClient.activeAuthorization == null
				|| StudentClient.activeAuthorization.getAuthType() == VSSAuthToken.AuthType.NONE))
		{
			PopupManager.showMessage("The exam selector was activated without a verified authorization. Exams cannot" +
					                         " " +
					                         "be given without without a verification.");

			StudentClient.primaryScene.setRoot(StudentClient.loginNode.getParent());
			return;
		}

		final VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().addAll("selector-root", "selector-cont");
		{
			final VBox header = new VBox();
			header.getStyleClass().addAll("selector-bg");
			{
				final Label title = new Label("Load an exam:");
				title.getStyleClass().addAll("text", "text-medium", "text-white");

				final HBox buttonBox = new HBox();
				buttonBox.getStyleClass().addAll("selector-btnbox");
				{
					final Button loadLocalButton = new Button("Load Local Exam");
					loadLocalButton.getStyleClass().addAll("text", "text-small", "text-white", "core-button", "selector-btn");


					final Button loadRemoteButton = new Button("Load Remote Exam");
					loadRemoteButton.getStyleClass().addAll("text", "text-small", "text-white", "core-button", "selector-btn");

					loadLocalButton.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle(ActionEvent event)
						{
							FileChooser examChooser = new FileChooser();
							examChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Documents"));
							File exam = examChooser.showOpenDialog(StudentClient.stage);
							if (exam == null)
								return;

							try
							{
								Exam loadedExam = ExamBuilder.readExam(exam, "null");

							}
							catch (VaSolSimException e)
							{
								System.out.println("read error");
							}
						}
					});

					loadRemoteButton.setOnAction(new EventHandler<ActionEvent>()
					{
						@Override
						public void handle(ActionEvent event)
						{
							PopupManager.showMessage("TODO: remote exams");
						}
					});

					buttonBox.getChildren().addAll(loadLocalButton, loadRemoteButton);
				}

				header.getChildren().addAll(title, buttonBox);
			}

			verticalRoot.getChildren().add(header);
		}

		this.examSelector = verticalRoot;
	}

	public Parent getParent()
	{
		return examSelector;
	}
}
