package com.gmail.guyfleeman.vasolsim.tclient.element;

import com.gmail.guyfleeman.vasolsim.common.VaSolSimException;
import com.gmail.guyfleeman.vasolsim.common.file.ExamBuilder;
import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.ExamTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.ExamsTreeElement;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
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

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;


/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
 */
public class ExamStatsInfoNode
{
	protected static Node examStatsInfoNode;
	protected static ExamBuilder examBuilder;

	static
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		/*
		 * Stats stuff
		 */
		Label infoLabel = new Label(statsReportingInfoLabelText);
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		Label reportingStatsLabel = new Label(statsReportingLabelText);
		reportingStatsLabel.getStyleClass().add("lbltext");

		final CheckBox reportingStatsCB = new CheckBox(statsReportingCBText);
		reportingStatsCB.getStyleClass().add("cbtext");
		reportingStatsCB.setIndeterminate(false);

		final Label statsDestinationAddrLabel = new Label(statsDestAddrLabelText);
		statsDestinationAddrLabel.getStyleClass().add("lbltext");
		statsDestinationAddrLabel.setDisable(true);

		final TextField statsDestinationAddrField = new TextField();
		statsDestinationAddrField.setMaxWidth(400);
		statsDestinationAddrField.setDisable(true);

		final Button verifyDestinationEmailButton = new Button(statsVerifyButtonText);
		verifyDestinationEmailButton.setDisable(true);

		/*
		 * Standalone ui stuff
		 */
		Label standaloneInfoLabel = new Label(statsSAInfoLabelText);
		standaloneInfoLabel.getStyleClass().add("lbltext");
		standaloneInfoLabel.setWrapText(true);

		final Label reportingStatsStandaloneLabel = new Label(statsSALabelText);
		reportingStatsStandaloneLabel.getStyleClass().add("lbltext");
		reportingStatsStandaloneLabel.setDisable(true);

		final CheckBox reportingStatsStandaloneCB = new CheckBox(statsSACBText);
		reportingStatsStandaloneCB.getStyleClass().add("cbtext");
		reportingStatsStandaloneCB.setIndeterminate(false);
		reportingStatsStandaloneCB.setDisable(true);

		final Label senderAddrLabel = new Label(statsSAAddrLabelText);
		senderAddrLabel.getStyleClass().add("lbltext");
		senderAddrLabel.setDisable(true);

		final TextField senderAddrField = new TextField();
		senderAddrField.setMaxWidth(400);
		senderAddrField.setDisable(true);

		final Label senderPasswordLabel = new Label(statsSAPasswordLabelText);
		senderPasswordLabel.getStyleClass().add("lbltext");
		senderPasswordLabel.setDisable(true);

		final PasswordField senderPasswordField = new PasswordField();
		senderPasswordField.setMaxWidth(400);
		senderPasswordField.setDisable(true);

		final Label senderSMTPAddrLabel = new Label(statsSASMTPAddrLabelText);
		senderSMTPAddrLabel.getStyleClass().add("lbltext");
		senderSMTPAddrLabel.setDisable(true);

		final TextField senderSMTPAddrField = new TextField();
		senderSMTPAddrField.setMaxWidth(400);
		senderSMTPAddrField.setDisable(true);

		final Label senderSMTPPortLabel = new Label(statsSASMTPPortLabelText);
		senderSMTPPortLabel.getStyleClass().add("lbltext");
		senderSMTPPortLabel.setDisable(true);

		final TextField senderSMTPPortField = new TextField();
		senderSMTPPortField.setMaxWidth(80);
		senderSMTPPortField.setDisable(true);

		final Button verifySMTPInfoButton = new Button(statsVerifyButtonText);
		verifySMTPInfoButton.setDisable(true);

		final Button continueButton = new Button(passwordContinueButtonText);

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
		reportingStatsCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
			                    Boolean newValue) {
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

		reportingStatsStandaloneCB.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
			                    Boolean newValue) {
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

		verifyDestinationEmailButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (statsDestinationAddrField.getText() == null
						|| statsDestinationAddrField.getText().trim().length() <= 0)
					PopupManager.showMessage(emailInvalidMessage, addressInvalidTitle);
				else if (!isValidEmail(statsDestinationAddrField.getText()))
					PopupManager.showMessage(emailInvalidRegexMessage, addressInvalidTitle);
				else
					PopupManager.showMessage(verifiedMessage, verifiedTitle);
			}
		});

		verifySMTPInfoButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boolean foundError = false;

				if (senderAddrField.getText() == null
						|| senderAddrField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(emailInvalidMessage, addressInvalidTitle);
				}
				else if (!isValidEmail(senderAddrField.getText()))
				{
					foundError = true;
					PopupManager.showMessage(emailInvalidRegexMessage, addressInvalidTitle);
				}

				if (senderPasswordField.getText() == null
						|| senderAddrField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(passwordInvalidMessage, passwordInvalidTitle);
				}

				if (senderSMTPAddrField.getText() == null
						|| senderSMTPAddrField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(addressInvalidMessage, addressInvalidTitle);
				}
				else if (!isValidAddress(senderSMTPAddrField.getText()))
				{
					foundError = true;
					PopupManager.showMessage(addressInvalidRegexMessage, addressInvalidTitle);
				}

				if (senderSMTPPortField.getText() == null
						|| senderSMTPPortField.getText().trim().length() <= 0)
				{
					foundError = true;
					PopupManager.showMessage(portInvalidMessage, portInvalidTitle);
				}
				else
				{
					try
					{
						if (!isValidPort(Integer.parseInt(senderSMTPPortField.getText())))
						{
							foundError = true;
							PopupManager.showMessage(portInvalidRange,portInvalidTitle);
						}
					}
					catch (NumberFormatException e)
					{
						foundError = true;
						PopupManager.showMessage(portInvalidCharsMessage, portInvalidTitle);
					}
				}

				if (!foundError)
				{
					if (!isValidSMTPConfiguration(senderSMTPAddrField.getText(),
							Integer.parseInt(senderSMTPPortField.getText()),
							senderAddrField.getText(),
							senderPasswordField.getText().getBytes(),
							true))
						PopupManager.showMessage(smtpBadConfig, smtpBadTitle);
					else
						PopupManager.showMessage(verifiedMessage, verifiedTitle);
				}
			}
		});

		continueButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				boolean foundError = false;

				if (reportingStatsCB.isSelected()) {
					if (statsDestinationAddrField.getText() == null
							|| statsDestinationAddrField.getText().trim().length() <= 0) {
						foundError = true;
						PopupManager.showMessage(emailInvalidMessage, addressInvalidTitle);
					} else if (!isValidEmail(statsDestinationAddrField.getText())) {
						foundError = true;
						PopupManager.showMessage(emailInvalidRegexMessage, addressInvalidTitle);
					}
				}

				if (reportingStatsStandaloneCB.isSelected()) {
					if (senderAddrField.getText() == null
							|| senderAddrField.getText().trim().length() <= 0) {
						foundError = true;
						PopupManager.showMessage(emailInvalidMessage, addressInvalidTitle);
					} else if (!isValidEmail(senderAddrField.getText())) {
						foundError = true;
						PopupManager.showMessage(emailInvalidRegexMessage, addressInvalidTitle);
					}

					if (senderPasswordField.getText() == null
							|| senderAddrField.getText().trim().length() <= 0) {
						foundError = true;
						PopupManager.showMessage(passwordInvalidMessage, passwordInvalidTitle);
					}

					if (senderSMTPAddrField.getText() == null
							|| senderSMTPAddrField.getText().trim().length() <= 0) {
						foundError = true;
						PopupManager.showMessage(addressInvalidMessage, addressInvalidTitle);
					} else if (!isValidAddress(senderSMTPAddrField.getText())) {
						foundError = true;
						PopupManager.showMessage(addressInvalidRegexMessage, addressInvalidTitle);
					}

					if (senderSMTPPortField.getText() == null
							|| senderSMTPPortField.getText().trim().length() <= 0) {
						foundError = true;
						PopupManager.showMessage(portInvalidMessage, portInvalidTitle);
					} else {
						try {
							if (!isValidPort(Integer.parseInt(senderSMTPPortField.getText()))) {
								foundError = true;
								PopupManager.showMessage(portInvalidRange, portInvalidTitle);
							}
						} catch (NumberFormatException e) {
							foundError = true;
							PopupManager.showMessage(portInvalidCharsMessage, portInvalidTitle);
						}
					}
				}

				if (!foundError)
				{
					if (reportingStatsStandaloneCB.isSelected() &&
							!isValidSMTPConfiguration(senderSMTPAddrField.getText(),
									Integer.parseInt(senderSMTPPortField.getText()),
									senderAddrField.getText(),
									senderPasswordField.getText().getBytes(),
									true))
					{
						PopupManager.showMessage(smtpBadConfig, smtpBadTitle);
					}
					else
					{
						if (reportingStatsCB.isSelected())
						{
							examBuilder.setReportingStats(true);
							examBuilder.setStatsDestinationEmail(statsDestinationAddrField.getText());

							if (reportingStatsStandaloneCB.isSelected())
							{
								examBuilder.setReportingStatsStandalone(true);
								examBuilder.setStatsSenderEmail(senderAddrField.getText());
								examBuilder.setStatsSenderEmailPassword(senderPasswordField.getText().getBytes());
								examBuilder.setStatsSenderSMTPAddress(senderSMTPAddrField.getText());
								examBuilder.setStatsSenderSMTPPort(senderSMTPPortField.getText());
							}
						}

						//create ui binder
						ExamTreeElement newExam = new ExamTreeElement();
						try {
							//generate exportable exam stub
							newExam.exam = examBuilder.getExamFramework();

							//create the view that will be in the visual tree
							TreeItem<TreeElement> element = createTreeItem(TeacherClient.class,
									newExam,
									TeacherClient.pathToExamIcon,
									24);
							//reference the view to the element to it can remove itself
							newExam.objectReference = element;
							//initialize listeners
							newExam.initListeners();

							//add exam to tree view
							TeacherClient.examsRoot.getChildren().add(element);

							//add exam to exams object
							ExamsTreeElement.exams.add(newExam);

							//clean ui
							CenterNode.removeScrollRoot();
						}
						catch (VaSolSimException e)
						{
							PopupManager.showMessage(internalExceptionOnExamBuilderInstanceInit + "Error Info:\n" +
											e.getCause() + "\n" + e.getMessage(),
									internalExceptionTitle);
						}
					}
				}
			}
		});

		examStatsInfoNode = verticalRoot;
	}

	public static Node getExamStatsInfoNode() {
		return examStatsInfoNode;
	}

	public static ExamBuilder getExamBuilder() {
		return examBuilder;
	}

	public static void setExamBuilder(ExamBuilder examBuilder) {
		ExamStatsInfoNode.examBuilder = examBuilder;
	}
}
