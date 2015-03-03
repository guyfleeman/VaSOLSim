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

package main.java.vasolsim.tclient.form;

import main.java.vasolsim.common.GenericUtils;
import main.java.vasolsim.common.VaSolSimException;
import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.file.ExamBuilder;
import main.java.vasolsim.common.node.DrawableNode;
import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.tclient.TeacherClient;
import main.java.vasolsim.tclient.core.CenterNode;
import main.java.vasolsim.tclient.tree.ExamTreeElement;
import main.java.vasolsim.tclient.tree.ExamsTreeElement;
import main.java.vasolsim.tclient.tree.QuestionSetTreeElement;
import main.java.vasolsim.tclient.tree.QuestionTreeElement;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class ExamExportNode implements DrawableNode
{
	protected static HBox examExportNode;

	public static boolean exportRaw = false;

	public ExamExportNode()
	{
		redrawNode(false);
	}

	public void redrawNode(boolean apply)
	{
		if (apply && ExamsTreeElement.exams.size() == 0)
		{
			PopupManager.showMessage("No exams have been created or imported.");
		}

		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label exportSelectionLabel = new Label("Select the exam to export:");
		exportSelectionLabel.getStyleClass().add("lbltext");
		exportSelectionLabel.setWrapText(true);

		final ComboBox<String> examSelectionBox = new ComboBox<String>();
		examSelectionBox.setPromptText("Select an exam.");
		for (ExamTreeElement exam : ExamsTreeElement.exams)
			if (exam.exam != null && exam.exam.getTestName() != null)
				examSelectionBox.getItems().add(exam.exam.getTestName());

		verticalRoot.getChildren().addAll(exportSelectionLabel,
		                                  examSelectionBox);

		HBox dir = new HBox();
		final TextField fileField = new TextField();
		fileField.setPrefWidth(2000);
		Button openFileButton = new Button("Output Directory");
		openFileButton.setMinWidth(120);
		openFileButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				DirectoryChooser exportLocation = new DirectoryChooser();
				exportLocation.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
				fileField.setText(exportLocation.showDialog(TeacherClient.stage).toString());
			}
		});
		dir.getChildren().addAll(fileField, openFileButton);

		Button exportButton = new Button("Export ->");

		if (!exportRaw)
		{
			Label infoLabel = new Label(TeacherClient.PASSWORD_DESCRIPTION_LABEL);
			infoLabel.getStyleClass().add("lbltext");
			infoLabel.setWrapText(true);

			Label passwordLabelOne = new Label(TeacherClient.PASSWORD_PROMPT_ONE);
			passwordLabelOne.getStyleClass().add("lbltext");
			final PasswordField passwordFieldOne = new PasswordField();
			passwordFieldOne.setMaxWidth(400);

			Label passwordLabelTwo = new Label(TeacherClient.PASSWORD_PROMPT_TWO);
			passwordLabelTwo.getStyleClass().add("lbltext");
			final PasswordField passwordFieldTwo = new PasswordField();
			passwordFieldTwo.setMaxWidth(400);

			verticalRoot.getChildren().addAll(infoLabel,
			                                  passwordLabelOne,
			                                  passwordFieldOne,
			                                  passwordLabelTwo,
			                                  passwordFieldTwo,
			                                  dir,
			                                  exportButton);

			exportButton.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent actionEvent)
				{
					if (passwordFieldOne.getText() == null
							|| passwordFieldOne.getText().trim().length() <= 0)
					{
						PopupManager.showMessage("Password Field 1:\n" + TeacherClient.PASSWORD_INVALID_MESSAGE,
						                         TeacherClient.PASSWORD_INVALID_TITLE);
						return;
					}

					if (passwordFieldTwo.getText() == null
							|| passwordFieldTwo.getText().trim().length() <= 0)
					{
						PopupManager.showMessage("Password Field 2 (confirmation):\n" +
								                         TeacherClient.PASSWORD_INVALID_MESSAGE,
						                         TeacherClient.PASSWORD_INVALID_TITLE);
						return;
					}

					if (!passwordFieldOne.getText().equals(passwordFieldTwo.getText()))
					{
						PopupManager.showMessage(TeacherClient.PASSWORD_NO_MATCH, TeacherClient.PASSWORD_INVALID_TITLE);
						return;
					}

					String password = passwordFieldOne.getText();
					passwordFieldOne.clear();
					passwordFieldTwo.clear();

					Exam exam = null;
					for (ExamTreeElement examTreeElement : ExamsTreeElement.exams)
						if (examTreeElement.exam.getTestName().equals(examSelectionBox.getValue()))
						{
							examTreeElement.exam.getQuestionSets().clear();
							for (QuestionSetTreeElement questionSetTreeElement : examTreeElement.qSets)
							{
								questionSetTreeElement.qSet.getQuestions().clear();
								for (QuestionTreeElement questionTreeItem : questionSetTreeElement.questions)
									questionSetTreeElement.qSet.getQuestions().add(questionTreeItem.question);

								examTreeElement.exam.getQuestionSets().add(questionSetTreeElement.qSet);
							}

							exam = examTreeElement.exam;
							break;
						}

					if (exam == null)
					{
						PopupManager.showMessage("You must select an exam to export.");
						return;
					}

					try
					{
						/*
						 * check for and remove characters that can't be in a filename
						 */
						String newTestName = GenericUtils.toValidFileName(exam.getTestName()).replaceAll(" ", "");

						/*
						 * check for reserved filenames
						 */
						for (String reservedFileName : TeacherClient.reservedSystemFileNamesList)
							if (newTestName.equalsIgnoreCase(reservedFileName))
							{
								newTestName = newTestName + " - SystemReserved";
								break;
							}

						ExamBuilder.writeExam(exam,
						                      new File(fileField.getText() + "/" + newTestName + ".vss"),
						                      password,
						                      true);
					}
					catch (VaSolSimException e)
					{
						PopupManager.showMessage(e.toString());
					}
				}
			});
		}
		else
		{
			verticalRoot.getChildren().addAll(dir,
			                                  exportButton);

			exportButton.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent actionEvent)
				{
					Exam exam = null;
					for (ExamTreeElement examTreeElement : ExamsTreeElement.exams)
						if (examTreeElement.exam.getTestName().equals(examSelectionBox.getValue()))
						{
							exam = examTreeElement.exam;
							break;
						}

					if (exam == null)
					{
						PopupManager.showMessage("You must select an exam to export.");
						return;
					}

					try
					{
						ExamBuilder.writeRaw(exam,
						                     new File(fileField.getText() + "/" +
								                              exam.getTestName().replaceAll(" ", "") + "raw.vss"),
						                     true);
					}
					catch (VaSolSimException e)
					{
						PopupManager.showMessage(e.toString());
					}
				}
			});
		}

		examExportNode = horizontalRoot;

		if (apply)
		{
			CenterNode.addScrollRoot();
			CenterNode.getScrollRoot().setContent(examExportNode);
		}
	}

	static
	{


		//TODO possibly cross these with a string change listener to prevent the out of frame popup crash loop
		/*
		passwordFieldOne.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldFocus,
			                    Boolean newFocus) {
				currentlyValidated = false;

				if (oldFocus && !newFocus)
					if (passwordFieldOne.getText() == null
							|| passwordFieldOne.getText().length() <= 0
							|| passwordFieldOne.getText().equals(""))
						PopupManager.showMessage(passwordInvalidMessage, passwordInvalidTitle);
			}
		});

		passwordFieldTwo.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldFocus,
			                    Boolean newFocus) {
				if (oldFocus && !newFocus)
				{
					currentlyValidated = false;

					if (passwordFieldTwo.getText() == null
							|| passwordFieldTwo.getText().length() <= 0
							|| passwordFieldTwo.getText().equals(""))
						PopupManager.showMessage(passwordInvalidMessage, passwordInvalidTitle);
					else if ((passwordFieldOne.getText() == null
							|| passwordFieldOne.getText().length() <= 0
							|| passwordFieldOne.getText().equals(""))
							|| !passwordFieldOne.getText().equals(passwordFieldTwo.getText()))
						PopupManager.showMessage(passwordNoMatch, passwordInvalidTitle);
					else
						currentlyValidated = true;
				}
			}
		});
		*/

	    /*
        nextButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                boolean foundError = false;

                if (passwordFieldOne.getText() == null
                        || passwordFieldOne.getText().trim().length() <= 0)
                {
                    foundError = true;
                    PopupManager.showMessage("Password Field 1:\n" + passwordInvalidMessage, passwordInvalidTitle);
                }

                if (passwordFieldTwo.getText() == null
                        || passwordFieldTwo.getText().trim().length() <= 0)
                {
                    foundError = true;
                    PopupManager.showMessage("Password Field 2 (confirmation):\n" + passwordInvalidMessage,
                            passwordInvalidTitle);
                }

                if (!foundError)
                {
                    if (!passwordFieldOne.getText().equals(passwordFieldTwo.getText()))
                        PopupManager.showMessage(passwordNoMatch, passwordInvalidTitle);
                    else
                    {
                        lastValidatedPassword = passwordFieldOne.getText();
                        currentlyValidated = true;

                        passwordFieldOne.clear();
                        passwordFieldTwo.clear();

                        if (advToStats)
                        {
                            CenterNode.getStyledRoot().getChildren().remove(examExportNode);
                            try
                            {
                                ExamStatsInfoNode.setExamBuilder(__NONUSEDREFERENCE_OldExamBuilder.getInstance(
		                                lastValidatedPassword.getBytes()));
                                CenterNode.addScrollRoot();
                                CenterNode.getScrollRoot().setContent(ExamStatsInfoNode.getNode());
                            } catch (VaSolSimException e)
                            {
                                PopupManager.showMessage(internalExceptionOnExamBuilderInstanceInit + "Error Info:\n" +
                                        e.getCause() + "\n" + e.getMessage(),
                                        internalExceptionTitle);
                            }
                        }
                    }
                }

            }
        });
        */


	}

	public Node getNode()
	{
		return examExportNode;
	}
}
