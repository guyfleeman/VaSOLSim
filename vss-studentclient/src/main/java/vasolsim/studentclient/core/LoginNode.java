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

package main.java.vasolsim.studentclient.core;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import main.java.vasolsim.common.GenericUtils;
import main.java.vasolsim.common.auth.VSSAuthenticationException;
import main.java.vasolsim.common.node.DrawableParent;
import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.studentclient.StudentClient;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;

/**
 * @author willstuckey
 * @date 2/5/15 <p></p>
 */
public class LoginNode implements DrawableParent
{
	public static Logger logger = Logger.getLogger(LoginNode.class.getName());

	protected Parent loginNode;

	public LoginNode()
	{
		redrawParent(false);
	}

	public void redrawParent(boolean apply)
	{
		logger.info("displaying login");

		final HBox horizontalRoot = new HBox();
		horizontalRoot.getStyleClass().add("login-root");
		{
			final VBox left = new VBox();
			left.setPrefWidth(StudentClient.screenSize.getWidth() / 2);
			left.getStyleClass().add("login-cont");
			{
				final VBox titleRoot = new VBox();
				titleRoot.getStyleClass().add("login-title-bg");
				{
					final Label title = new Label("VSS - Student Login");
					title.getStyleClass().addAll("text", "text-large", "text-white", "text-center", "login-title");
					title.setAlignment(Pos.CENTER);

					titleRoot.getChildren().add(title);
				}

				final ToggleGroup loginGroup = new ToggleGroup();

				final VBox loginRoot = new VBox();
				loginRoot.setAlignment(Pos.CENTER);
				loginRoot.getStyleClass().add("login-bg");
				{
					final Button loginButton = new Button("Login");
					loginButton.getStyleClass().addAll("text", "text-medium", "text-white", "core-button");

					final VBox localRoot = new VBox();
					{
						final RadioButton select = new RadioButton("Local Login");
						select.setToggleGroup(loginGroup);
						select.setSelected(true);
						select.getStyleClass().addAll("text", "text-medium", "text-white");

						final VBox fields = new VBox();
						fields.getStyleClass().add("login-field-cont");
						{
							final TextField firstNameTF = new TextField();
							firstNameTF.setPromptText("first");
							firstNameTF.getStyleClass().addAll("login-field", "text", "text-small");

							final TextField lastNameTF = new TextField();
							lastNameTF.setPromptText("last");
							lastNameTF.getStyleClass().addAll("login-field", "text", "text-small");

							final TextField studentNumberTF = new TextField();
							studentNumberTF.setPromptText("student #");
							studentNumberTF.getStyleClass().addAll("login-field", "text", "text-small");

							/*
							 * field state changer
							 */
							select.selectedProperty().addListener(new ChangeListener<Boolean>()
							{
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								                    Boolean newValue)
								{
									if (oldValue && !newValue)
									{
										firstNameTF.setDisable(true);
										lastNameTF.setDisable(true);
										studentNumberTF.setDisable(true);
									}
									else if (!oldValue && newValue)
									{
										firstNameTF.setDisable(false);
										lastNameTF.setDisable(false);
										studentNumberTF.setDisable(false);

										logger.info("Login Context -> local");
										loginButton.setOnAction(new EventHandler<ActionEvent>()
										{
											@Override
											public void handle(ActionEvent event)
											{
												try
												{
													StudentClient.activeAuthorization =
															StudentClient.localUserAuthenticator.authenticateUser(
																	firstNameTF.getText(),
																	lastNameTF.getText(),
																	studentNumberTF.getText());

													advanceNode();
												}
												catch (VSSAuthenticationException e)
												{
													StudentClient.activeAuthorization = null;
													//logger.warn(GenericUtils.exceptionToString(e));
													PopupManager.showMessage(VSSAuthenticationException
															                         .lastErrorMessage);
												}
											}
										});
									}
								}
							});

							/*
							 * default handler
							 */
							loginButton.setOnAction(new EventHandler<ActionEvent>()
							{
								@Override
								public void handle(ActionEvent event)
								{
									try
									{
										StudentClient.activeAuthorization =
												StudentClient.localUserAuthenticator.authenticateUser(
														firstNameTF.getText(),
														lastNameTF.getText(),
														studentNumberTF.getText());

										advanceNode();
									}
									catch (VSSAuthenticationException e)
									{
										StudentClient.activeAuthorization = null;
										//logger.warn(GenericUtils.exceptionToString(e));
										PopupManager.showMessage(VSSAuthenticationException
												                         .lastErrorMessage);
									}
								}
							});

							fields.getChildren().addAll(firstNameTF, lastNameTF, studentNumberTF);
						}

						localRoot.getChildren().addAll(select, fields);
					}

					final VBox remoteRoot = new VBox();
					{
						final RadioButton select = new RadioButton("Remote Login");
						select.setToggleGroup(loginGroup);
						select.setSelected(false);
						select.getStyleClass().addAll("text", "text-medium", "text-white");
						select.setDisable(true);

						final VBox fields = new VBox();
						fields.getStyleClass().add("login-field-cont");
						{
							final TextField usernameTF = new TextField();
							usernameTF.setDisable(true);
							usernameTF.setPromptText("username");
							usernameTF.getStyleClass().addAll("login-field", "text", "text-small");

							final PasswordField passwordTF = new PasswordField();
							passwordTF.setDisable(true);
							passwordTF.setPromptText("password");
							passwordTF.getStyleClass().addAll("login-field", "text", "text-small");

							final TextField remoteTF = new TextField();
							remoteTF.setDisable(true);
							remoteTF.setPromptText("remote address");
							remoteTF.getStyleClass().addAll("login-field", "text", "text-small");

							/*
							 * field state changer
							 */
							select.selectedProperty().addListener(new ChangeListener<Boolean>()
							{
								@Override
								public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
								                    Boolean newValue)
								{
									if (oldValue && !newValue)
									{
										usernameTF.setDisable(true);
										passwordTF.setDisable(true);
										remoteTF.setDisable(true);
									}
									else if (!oldValue && newValue)
									{
										usernameTF.setDisable(false);
										passwordTF.setDisable(false);
										remoteTF.setDisable(false);

										logger.info("Login Context -> remote");
										loginButton.setOnAction(new EventHandler<ActionEvent>()
										{
											@Override
											public void handle(ActionEvent event)
											{
												try
												{
													StudentClient.activeAuthorization =
															StudentClient.remoteUserAuthenticator.authenticateUser(
																	usernameTF.getText(),
																	passwordTF.getText().toCharArray(),
																	remoteTF.getText());

													advanceNode();
												}
												catch (VSSAuthenticationException e)
												{
													StudentClient.activeAuthorization = null;
													logger.warn(GenericUtils.exceptionToString(e));
													PopupManager.showMessage(VSSAuthenticationException.lastErrorMessage);
												}
											}
										});
									}
								}
							});

							fields.getChildren().addAll(usernameTF, passwordTF, remoteTF);
						}

						remoteRoot.getChildren().addAll(select, fields);
					}

					loginRoot.getChildren().addAll(localRoot, remoteRoot, loginButton);
				}

				left.getChildren().addAll(titleRoot, loginRoot);
			}

			final VBox right = new VBox();
			right.setMinWidth(360);
			right.setPrefWidth(StudentClient.screenSize.getWidth() / 2);
			right.setAlignment(Pos.CENTER);
			{
				final HBox top = new HBox();
				top.setAlignment(Pos.CENTER);
				{
					final Label white = new Label("White");
					white.getStyleClass().addAll("login-color", "login-white");

					final Label lightGray = new Label("Light Gray");
					lightGray.getStyleClass().addAll("login-color", "login-lightgray");

					top.getChildren().addAll(white, lightGray);
				}

				final HBox bottom = new HBox();
				bottom.setAlignment(Pos.CENTER);
				{
					final Label darkGray = new Label("Dark Gray");
					darkGray.getStyleClass().addAll("login-color", "login-darkgray");

					final Label black = new Label("Black");
					black.getStyleClass().addAll("login-color", "login-black");

					bottom.getChildren().addAll(darkGray, black);
				}

				right.getChildren().addAll(top, bottom);
			}

			horizontalRoot.getChildren().addAll(left, right);
		}

		loginNode = horizontalRoot;
	}

	protected void advanceNode()
	{
		//StudentClient.primaryScene.setRoot(null);
		StudentClient.primaryScene.setRoot(StudentClient.examSelectorNode.getParent());
	}

	public Parent getParent()
	{
		return loginNode;
	}
}
