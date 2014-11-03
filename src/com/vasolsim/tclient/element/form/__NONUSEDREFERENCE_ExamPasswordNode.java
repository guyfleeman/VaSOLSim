package com.vasolsim.tclient.element.form;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class __NONUSEDREFERENCE_ExamPasswordNode
{
	public static boolean advToStats = true;

	protected static HBox examPasswordPromptNode;

	private static boolean currentlyValidated    = false;
	private static String  lastValidatedPassword = null;

	static
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label infoLabel = new Label(passwordDescriptionLabel);
		infoLabel.getStyleClass().add("lbltext");
		infoLabel.setWrapText(true);

		Label passwordLabelOne = new Label(passwordPromptOne);
		passwordLabelOne.getStyleClass().add("lbltext");
		final PasswordField passwordFieldOne = new PasswordField();
		passwordFieldOne.setMaxWidth(400);

		Label passwordLabelTwo = new Label(passwordPromptTwo);
		passwordLabelTwo.getStyleClass().add("lbltext");
		final PasswordField passwordFieldTwo = new PasswordField();
		passwordFieldTwo.setMaxWidth(400);

		Button nextButton = new Button();
		nextButton.setText(continueButtonText);

		verticalRoot.getChildren().addAll(infoLabel,
		                                  passwordLabelOne,
		                                  passwordFieldOne,
		                                  passwordLabelTwo,
		                                  passwordFieldTwo,
		                                  nextButton);

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
                            CenterNode.getStyledRoot().getChildren().remove(examPasswordPromptNode);
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

		examPasswordPromptNode = horizontalRoot;
	}

	public static boolean isCurrentlyValidated()
	{
		return currentlyValidated;
	}

	public static String getLastValidatedPassword()
	{
		return lastValidatedPassword;
	}

	public static Node getExamPasswordPromptNode()
	{
		return examPasswordPromptNode;
	}
}
