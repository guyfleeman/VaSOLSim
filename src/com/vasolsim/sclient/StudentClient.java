package com.vasolsim.sclient;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

/**
 * Runner class for student user interface.
 * @author optimalpandemic
 * @date 11/3/14 <p></p>
 */
public class StudentClient extends Application
{
	public static Stage stage;
	
	@Override
	public void start(Stage primaryStage)
	{
		stage = primaryStage;
		stage.setTitle("Virginia Practice SOL");
		
		stage.show();
	}
	
	public static void main(String[] args)
	{
		new JFXPanel();
		
		// We aren't really supposed to do this
		// JavaFX is past using main() at all
		System.setProperty("javafx.userAgentStylesheetUrl", "caspian");
		launch(args);
	}
	
	//If I decide later that I hate myself
	public StudentClient()
	{
		
	}
}
