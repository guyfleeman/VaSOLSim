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

package main.java.vasolsim.teacherclient.form;

import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.node.DrawableNode;
import main.java.vasolsim.common.support.notification.PopupManager;
import main.java.vasolsim.teacherclient.TeacherClient;
import main.java.vasolsim.teacherclient.core.CenterNode;
import main.java.vasolsim.teacherclient.tree.ExamTreeElement;
import main.java.vasolsim.teacherclient.tree.ExamsTreeElement;
import main.java.vasolsim.common.node.tree.TreeElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static main.java.vasolsim.common.GenericUtils.*;


/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class ExamInitNode implements DrawableNode
{
	public Exam lastGeneratedExam;

	protected Node examStatsInfoNode;

	public ExamInitNode()
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().addAll("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		/*
		 * Stats stuff
		 */
		Label infoLabel = new Label(TeacherClient.STATS_REPORTING_INFO_LABEL_TEXT);
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		Label reportingStatsLabel = new Label(TeacherClient.STATS_REPORTING_LABEL_TEXT);
		reportingStatsLabel.getStyleClass().add("lbltext");

		final CheckBox reportingStatsCB = new CheckBox(TeacherClient.STATS_REPORTING_CB_TEXT);
		reportingStatsCB.getStyleClass().add("cbtext");
		reportingStatsCB.setIndeterminate(false);

		final Label statsDestinationAddrLabel = new Label(TeacherClient.STATS_DEST_ADDR_LABEL_TEXT);
		statsDestinationAddrLabel.getStyleClass().add("lbltext");
		statsDestinationAddrLabel.setDisable(true);

		final TextField statsDestinationAddrField = new TextField();
		statsDestinationAddrField.setMaxWidth(400);
		statsDestinationAddrField.setDisable(true);

		final Button verifyDestinationEmailButton = new Button(TeacherClient.STATS_VERIFY_BUTTON_TEXT);
		verifyDestinationEmailButton.setDisable(true);

		/*
		 * Standalone ui stuff
		 */
		Label standaloneInfoLabel = new Label(TeacherClient.STATS_SA_INFO_LABEL_TEXT);
		standaloneInfoLabel.getStyleClass().add("lbltext");
		standaloneInfoLabel.setWrapText(true);

		final Label reportingStatsStandaloneLabel = new Label(TeacherClient.STATS_SA_LABEL_TEXT);
		reportingStatsStandaloneLabel.getStyleClass().add("lbltext");
		reportingStatsStandaloneLabel.setDisable(true);

		final CheckBox reportingStatsStandaloneCB = new CheckBox(TeacherClient.STATS_SACB_TEXT);
		reportingStatsStandaloneCB.getStyleClass().add("cbtext");
		reportingStatsStandaloneCB.setIndeterminate(false);
		reportingStatsStandaloneCB.setDisable(true);

		final Label senderAddrLabel = new Label(TeacherClient.STATS_SA_ADDR_LABEL_TEXT);
		senderAddrLabel.getStyleClass().add("lbltext");
		senderAddrLabel.setDisable(true);

		final TextField senderAddrField = new TextField();
		senderAddrField.setMaxWidth(400);
		senderAddrField.setDisable(true);

		final Label senderPasswordLabel = new Label(TeacherClient.STATS_SA_PASSWORD_LABEL_TEXT);
		senderPasswordLabel.getStyleClass().add("lbltext");
		senderPasswordLabel.setDisable(true);

		final PasswordField senderPasswordField = new PasswordField();
		senderPasswordField.setMaxWidth(400);
		senderPasswordField.setDisable(true);

		final Label senderSMTPAddrLabel = new Label(TeacherClient.STATS_SASMTP_ADDR_LABEL_TEXT);
		senderSMTPAddrLabel.getStyleClass().add("lbltext");
		senderSMTPAddrLabel.setDisable(true);

		final TextField senderSMTPAddrField = new TextField();
		senderSMTPAddrField.setMaxWidth(400);
		senderSMTPAddrField.setDisable(true);

		final Label senderSMTPPortLabel = new Label(TeacherClient.STATS_SASMTP_PORT_LABEL_TEXT);
		senderSMTPPortLabel.getStyleClass().add("lbltext");
		senderSMTPPortLabel.setDisable(true);

		final TextField senderSMTPPortField = new TextField();
		senderSMTPPortField.setMaxWidth(80);
		senderSMTPPortField.setDisable(true);

		final Button verifySMTPInfoButton = new Button(TeacherClient.STATS_VERIFY_BUTTON_TEXT);
		verifySMTPInfoButton.setDisable(true);

		final Button continueButton = new Button(TeacherClient.CONTINUE_BUTTON_TEXT);

		/*
		 * add everything
		 */
		verticalRoot.getChildren().addAll(infoLabel,
		                                  reportingStatsLabel,
		                                  reportingStatsCB,
		                                  statsDestinationAddrLabel,
		                                  statsDestinationAddrField,
		                                  verifyDestinationEmailButton,
		                                  standaloneInfoLabel,
		                                  reportingStatsStandaloneLabel,
		                                  reportingStatsStandaloneCB,
		                                  senderAddrLabel,
		                                  senderAddrField,
		                                  senderPasswordLabel,
		                                  senderPasswordField,
		                                  senderSMTPAddrLabel,
		                                  senderSMTPAddrField,
		                                  senderSMTPPortLabel,
		                                  senderSMTPPortField,
		                                  verifySMTPInfoButton,
		                                  continueButton);

		/*
		 * listeners
		 */
		reportingStatsCB.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
			                    Boolean newValue)
			{
				if (oldValue && !newValue)
				{
					statsDestinationAddrLabel.setDisable(true);
					statsDestinationAddrField.clear();
					statsDestinationAddrField.setDisable(true);
					verifyDestinationEmailButton.setDisable(true);

					reportingStatsStandaloneLabel.setDisable(true);
					reportingStatsStandaloneCB.setSelected(false);
					reportingStatsStandaloneCB.setDisable(true);
				}
				else if (!oldValue && newValue)
				{
					statsDestinationAddrLabel.setDisable(false);
					statsDestinationAddrField.clear();
					statsDestinationAddrField.setDisable(false);
					verifyDestinationEmailButton.setDisable(false);

					reportingStatsStandaloneLabel.setDisable(false);
					reportingStatsStandaloneCB.setDisable(false);
				}
			}
		});

		reportingStatsStandaloneCB.selectedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
			                    Boolean newValue)
			{
				if (oldValue && !newValue)
				{
					senderAddrLabel.setDisable(true);
					senderPasswordLabel.setDisable(true);
					senderSMTPAddrLabel.setDisable(true);
					senderSMTPPortLabel.setDisable(true);

					senderAddrField.clear();
					senderPasswordField.clear();
					senderSMTPAddrField.clear();
					senderSMTPPortField.clear();
					senderAddrField.setDisable(true);
					senderPasswordField.setDisable(true);
					senderSMTPAddrField.setDisable(true);
					senderSMTPPortField.setDisable(true);

					verifySMTPInfoButton.setDisable(true);
				}
				else if (!oldValue && newValue)
				{
					senderAddrLabel.setDisable(false);
					senderPasswordLabel.setDisable(false);
					senderSMTPAddrLabel.setDisable(false);
					senderSMTPPortLabel.setDisable(false);

					senderAddrField.clear();
					senderPasswordField.clear();
					senderSMTPAddrField.clear();
					senderSMTPPortField.clear();
					senderAddrField.setDisable(false);
					senderPasswordField.setDisable(false);
					senderSMTPAddrField.setDisable(false);
					senderSMTPPortField.setDisable(false);

					verifySMTPInfoButton.setDisable(false);
				}
			}
		});

		verifyDestinationEmailButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (statsDestinationAddrField.getText() == null
						|| statsDestinationAddrField.getText().trim().length() <= 0)
					PopupManager.showMessage(TeacherClient.EMAIL_INVALID_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
				else if (!isValidEmail(statsDestinationAddrField.getText()))
					PopupManager.showMessage(TeacherClient.EMAIL_INVALID_REGEX_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
				else
					PopupManager.showMessage(TeacherClient.VERIFIED_MESSAGE, TeacherClient.VERIFIED_TITLE);
			}
		});

		verifySMTPInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				boolean foundError = false;

				if (senderAddrField.getText() == null
						|| senderAddrField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(TeacherClient.EMAIL_INVALID_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
				}
				else if (!isValidEmail(senderAddrField.getText()))
				{
					foundError = true;
					PopupManager.showMessage(TeacherClient.EMAIL_INVALID_REGEX_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
				}

				if (senderPasswordField.getText() == null
						|| senderAddrField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(TeacherClient.PASSWORD_INVALID_MESSAGE, TeacherClient.PASSWORD_INVALID_TITLE);
				}

				if (senderSMTPAddrField.getText() == null
						|| senderSMTPAddrField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(TeacherClient.ADDRESS_INVALID_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
				}
				else if (!isValidAddress(senderSMTPAddrField.getText()))
				{
					foundError = true;
					PopupManager.showMessage(TeacherClient.ADDRESS_INVALID_REGEX_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
				}

				if (senderSMTPPortField.getText() == null
						|| senderSMTPPortField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(TeacherClient.PORT_INVALID_MESSAGE, TeacherClient.PORT_INVALID_TITLE);
				}
				else
				{
					try
					{
						if (!isValidPort(Integer.parseInt(senderSMTPPortField.getText())))
						{
							foundError = true;
							PopupManager.showMessage(TeacherClient.PORT_INVALID_RANGE, TeacherClient.PORT_INVALID_TITLE);
						}
					}
					catch (NumberFormatException e)
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.PORT_INVALID_CHARS_MESSAGE, TeacherClient.PORT_INVALID_TITLE);
					}
				}

				if (!foundError)
				{
					if (!isValidSMTPConfiguration(senderSMTPAddrField.getText(),
					                              Integer.parseInt(senderSMTPPortField.getText()),
					                              senderAddrField.getText(),
					                              senderPasswordField.getText().getBytes(),
					                              true))
						PopupManager.showMessage(TeacherClient.SMTP_BAD_CONFIG, TeacherClient.SMTP_BAD_TITLE);
					else
						PopupManager.showMessage(TeacherClient.VERIFIED_MESSAGE, TeacherClient.VERIFIED_TITLE);
				}
			}
		});

		continueButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				boolean foundError = false;

				if (reportingStatsCB.isSelected())
				{
					if (statsDestinationAddrField.getText() == null
							|| statsDestinationAddrField.getText().trim().length() <= 0)
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.EMAIL_INVALID_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
					}
					else if (!isValidEmail(statsDestinationAddrField.getText()))
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.EMAIL_INVALID_REGEX_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
					}
				}

				if (reportingStatsStandaloneCB.isSelected())
				{
					if (senderAddrField.getText() == null
							|| senderAddrField.getText().trim().length() <= 0)
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.EMAIL_INVALID_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
					}
					else if (!isValidEmail(senderAddrField.getText()))
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.EMAIL_INVALID_REGEX_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
					}

					if (senderPasswordField.getText() == null
							|| senderAddrField.getText().trim().length() <= 0)
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.PASSWORD_INVALID_MESSAGE, TeacherClient.PASSWORD_INVALID_TITLE);
					}

					if (senderSMTPAddrField.getText() == null
							|| senderSMTPAddrField.getText().trim().length() <= 0)
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.ADDRESS_INVALID_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
					}
					else if (!isValidAddress(senderSMTPAddrField.getText()))
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.ADDRESS_INVALID_REGEX_MESSAGE, TeacherClient.ADDRESS_INVALID_TITLE);
					}

					if (senderSMTPPortField.getText() == null
							|| senderSMTPPortField.getText().trim().length() <= 0)
					{
						foundError = true;
						PopupManager.showMessage(TeacherClient.PORT_INVALID_MESSAGE, TeacherClient.PORT_INVALID_TITLE);
					}
					else
					{
						try
						{
							if (!isValidPort(Integer.parseInt(senderSMTPPortField.getText())))
							{
								foundError = true;
								PopupManager.showMessage(TeacherClient.PORT_INVALID_RANGE, TeacherClient.PORT_INVALID_TITLE);
							}
						}
						catch (NumberFormatException e)
						{
							foundError = true;
							PopupManager.showMessage(TeacherClient.PORT_INVALID_CHARS_MESSAGE, TeacherClient.PORT_INVALID_TITLE);
						}
					}
				}

				Exam tmp = new Exam();
				if (!foundError)
				{
					if (reportingStatsStandaloneCB.isSelected() &&
							!isValidSMTPConfiguration(senderSMTPAddrField.getText(),
							                          Integer.parseInt(senderSMTPPortField.getText()),
							                          senderAddrField.getText(),
							                          senderPasswordField.getText().getBytes(),
							                          true))
					{
						PopupManager.showMessage(TeacherClient.SMTP_BAD_CONFIG, TeacherClient.SMTP_BAD_TITLE);
					}
					else
					{
						if (reportingStatsCB.isSelected())
						{
							tmp.setReportingStats(true);
							tmp.setStatsDestinationEmail(statsDestinationAddrField.getText());

							if (reportingStatsStandaloneCB.isSelected())
							{
								tmp.setReportingStatsStandalone(true);
								tmp.setStatsSenderEmail(senderAddrField.getText());
								tmp.setStatsSenderPassword(senderPasswordField.getText());
								tmp.setStatsSenderSMTPAddress(senderSMTPAddrField.getText());
								tmp.setStatsSenderSMTPPort(Integer.parseInt(senderSMTPPortField.getText()));
							}
						}

						//create ui binder
						ExamTreeElement newExam = new ExamTreeElement();

						//generate exportable exam stub
						newExam.exam = tmp;

						//create the view that will be in the visual tree
						TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
						                                               newExam,
						                                               TeacherClient.pathToExamIcon,
						                                               24);
						//reference the view to the element to it can remove itself
						newExam.treeElementReference = element;
						//initialize listeners
						newExam.initListeners();

						//add exam to tree view
						TeacherClient.examsRoot.getChildren().add(element);

						//add exam to exams object
						ExamsTreeElement.exams.add(newExam);

						//clean ui
						CenterNode.addScrollRoot();
						TeacherClient.examNode.setExam(newExam.exam);
						TeacherClient.examNode.boundTreeElement = newExam;
						CenterNode.getScrollRoot().setContent(TeacherClient.examNode.getNode());
					}
				}
			}
		});

		examStatsInfoNode = verticalRoot;
	}

	public Node getNode()
	{
		return examStatsInfoNode;
	}

	public void redrawNode(boolean apply)
	{
		if (apply)
		{
			CenterNode.addScrollRoot();
			CenterNode.getScrollRoot().setContent(getNode());
		}
	}
}
