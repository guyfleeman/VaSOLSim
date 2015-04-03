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

package main.java.vasolsim.tclient;

import javax.annotation.Nonnull;
import main.java.vasolsim.common.ExternalTask;
import main.java.vasolsim.common.GenericUtils;
import main.java.vasolsim.common.Preloader;
import main.java.vasolsim.common.notification.DebugWindow;
import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.tclient.core.BottomNode;
import main.java.vasolsim.tclient.core.CenterNode;
import main.java.vasolsim.tclient.core.LeftNode;
import main.java.vasolsim.tclient.core.MenuNode;
import main.java.vasolsim.tclient.form.*;
import main.java.vasolsim.tclient.tree.TreeElement;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

/**
 * @author guyfleeman
 * @date 7/15/14
 * <p>Runner for the teacher client. Initialization done in Preload.</p>
 */
public class TeacherClient extends Application
{
	///////////////////////////////////
	//  TEACHER CLIENT UI CONSTANTS  //
	@SuppressWarnings("unused")
	private static final boolean __BEGIN_TEACHER_CLIENT_UI_CONSTANTS = false;
	///////////////////////////////////

	public static final String PASSWORD_DESCRIPTION_LABEL                       = "Please provide and confirm the " +
			"exam's password prior to" +
			" " +
			"creation. This password will be used to prevent students from modifying the contents and the exam. The " +
			"students will enter this password when they go to take the exam, so please do not use your personal " +
			"information. Some good choices might be \"class period\" (e.g \"4y\") or \"your name\" (e.g. \"Karen\")" +
			"." +
			"You do not need to include the quotation marks, however symbols are valid. Students must enter the " +
			"*EXACT* same password that you enter here in order to access the exam. Once you set the exam's " +
			"password," +
			"it may not be changed for security reasons; please double check your password prior to continuing.";
	public static final String PASSWORD_PROMPT_ONE                              = "Please enter the password: ";
	public static final String PASSWORD_PROMPT_TWO                              = "Please confirm the password: ";
	public static final String CONTINUE_BUTTON_TEXT                             = "Continue";
	public static final String PASSWORD_INVALID_TITLE                           = "Invalid Password";
	public static final String PASSWORD_INVALID_MESSAGE                         = "Password cannot be of zero length" +
			" " +
			"or all " +
			"whitespace.";
	public static final String PASSWORD_NO_MATCH                                = "Passwords do not match.";
	public static final String STATS_REPORTING_INFO_LABEL_TEXT                  = "This form will take. The " +
			"information needed to " +
			"report " +
			"the answers given by students. The answers can be read and compiled into class statistics. \n\n" +
			"If you want to report statistics please check the box and enter the email address you want the " +
			"statistics to be sent. DO NOT SEND STATISTICS TO YOUR PERSONAL EMAIL; you will get a bulky email for " +
			"every student that completes the test. It is strongly recommended that you establish a gmail account " +
			"for the sole purpose of reporting statistics. Once you create the email account, you will never have " +
			"to read it again, the program will do it for you and compile the stats. When the student completes the " +
			"test, he/she will be prompted to enter his/her email address. The program will automatically send an " +
			"email from their account to the destination address you provide. \n\nIf you have any more concerns, " +
			"or need help filling out the form, please visit my youtube channel. (!!LINK COMING SOON!!)";
	public static final String STATS_REPORTING_LABEL_TEXT                       = "Check this box to enable " +
			"statistics" +
			" reporting.";
	public static final String STATS_REPORTING_CB_TEXT                          = "reporting statistics";
	public static final String STATS_DEST_ADDR_LABEL_TEXT                       = "The destination email address for" +
			" " +
			"reported " +
			"statistics.";
	public static final String STATS_VERIFY_BUTTON_TEXT                         = "Verify ->";
	public static final String STATS_SA_INFO_LABEL_TEXT                         = "If you'd prefer students not " +
			"enter" +
			" " +
			"their" +
			" " +
			"own emails, " +
			"you can report stats in \"standalone mode.\" In this mode, you will provide the address and password " +
			"of an email account, which will send the answer data for all students. DO NOT USE YOUR PERSONAL EMAIL. " +
			"The email and password will be encrypted using the industry standard for credit card data " +
			"(SHA-512 -> AES-256 -> StartTLS). ";
	public static final String STATS_SA_LABEL_TEXT                              = "Check this box to enable " +
			"standalone" +
			" " +
			"stats" +
			" reporting.";
	public static final String STATS_SACB_TEXT                                  = "reporting statistics standalone";
	public static final String STATS_SA_ADDR_LABEL_TEXT                         = "The sender email address.";
	public static final String STATS_SA_PASSWORD_LABEL_TEXT                     = "The sender email address password.";
	public static final String STATS_SASMTP_ADDR_LABEL_TEXT                     = "The SMTP address for the email " +
			"provider" +
			" " +
			"(should " +
			"auto-complete).";
	public static final String STATS_SASMTP_PORT_LABEL_TEXT                     = "The port for the SMTP address " +
			"(should " +
			"auto-complete).";
	public static final String ADDRESS_INVALID_TITLE                            = "Invalid Address";
	public static final String EMAIL_INVALID_MESSAGE                            = "Email cannot be of zero length or" +
			" " +
			"all " +
			"whitespace.";
	public static final String EMAIL_INVALID_REGEX_MESSAGE                      = "Email is not of valid form.";
	public static final String VERIFIED_TITLE                                   = "Information Verified";
	public static final String VERIFIED_MESSAGE                                 = "All required information has been" +
			" " +
			"verified.";
	public static final String ADDRESS_INVALID_MESSAGE                          = "The address cannot be of zero " +
			"length or" +
			" " +
			"all whitespace.";
	public static final String ADDRESS_INVALID_REGEX_MESSAGE                    = "The address is not of valid form.";
	public static final String PORT_INVALID_TITLE                               = "Invalid Port";
	public static final String PORT_INVALID_MESSAGE                             = "The port cannot be nothing or all" +
			" " +
			"whitespace";
	public static final String PORT_INVALID_CHARS_MESSAGE                       = "The port must be a numeric " +
			"integer.";
	public static final String PORT_INVALID_RANGE                               = "The port must be between 0 and " +
			"65536.";
	public static final String SMTP_BAD_CONFIG                                  = "The SMTP configuration was " +
			"invalid.";
	public static final String SMTP_BAD_TITLE                                   = "Invalid SMTP";
	public static final String INTERNAL_EXCEPTION_ON_EXAM_BUILDER_INSTANCE_INIT = "A problem internal to VaSOLSim " +
			"has" +
			" " +
			"occurred.\nI apologize for the inconvenience.\n\n";
	public static final String INTERNAL_EXCEPTION_TITLE                         = "Internal Exception";
	public static final String TEST_STATS_LABEL_TEXT                            = "Exam Information Overview";
	public static final String TEST_NAME_LABEL_TEXT                             = "Test Name:";
	public static final String AUTHOR_NAME_LABEL_TEXT                           = "Author Name:";
	public static final String SCHOOL_NAME_LABEL_TEXT                           = "School Name:";
	public static final String PERIOD_NAME_LABEL_TEXT                           = "Period:";
	public static final String MULTIPLE_CHOICE_TEXT                             = "multiple choice (1 answer)";
	public static final String MULTIPLE_RESPONSE_TEXT                           = "multiple response (0-8 answers)";
	public static final String TECH_ENHANCED_MULTIPLE_CHOICE_TEXT               = "multiple choice, technology " +
			"enhanced " +
			"format" +
			" " +
			"(1" +
			" " +
			"answer)";
	public static final String TECH_ENHANCED_MULTIPLE_RESPONSE_TEXT             = "multiple response, technology " +
			"enhanced " +
			"format" +
			" " +
			"(0-8 answers)";
	public static final String TECH_ENHANCED_DD_MULTIPLE_CHOICE_TEXT            = "multiple choice, drag and drop " +
			"format (1 " +
			"answer)";
	public static final String TECH_ENHANCED_DD_MULTIPLE_RESPONSE_TEXT          = "multiple response, drag and drop " +
			"format, " +
			"predefined (1-8 answers)";
	public static final String TECH_ENCHANCED_DD_GRAMMAR_TEXT                   = "grammar multiple response " +
			"(punctuation), " +
			"drag and drop " +
			"format (udf answer count)";
	public static final String TECH_ENCHANCED_VENN_DIAGRAM_TEXT                 = "venn diagram, multiple response " +
			"(avaliable in a future release)";
	public static final String QUESTION_SET_INFO_LABEL_TEXT                     = "Question Set Information Overview";
	public static final String QUESTION_SET_NAME_LABEL_TEXT                     = "Question Set Name:";
	public static final String RESOURCE_FILE_INFO_LABEL_TEXT                    = "Use this section to attach a " +
			"resource file. The " +
			"resource" +
			" " +
			"will be visible to the student as he/she works through every QandA in the section. Valid files are " +
			"text (.txt), MS Word (.docx), PDF (.pdf), and images (.jpg, .png, .gif).";
	public static final String INVALID_FILE_TYPE_MESSAGE                        = "File type not recognized: .";
	public static final String INVALID_FILE_TYPE_TITLE                          = "Invalid File Type";

	///////////////////////////////////////
	//  END TEACHER CLIENT UI CONSTANTS  //
	///////////////////////////////////////

	/*
	 * titles
	 */
	public static String preloadTitle = "VaSOLSim Teacher Client. Loading...";
	public static String title        = "Virginia Standards Of Learning Simulator (VaSOLSim) - Teacher Client";

	/*
	 * resources
	 */
	public static boolean clearPersistenceFlagged = false;
	public static String rscPathRoot                = "/rsc/";
	public static String pathToStyle                = rscPathRoot + "css/tClientStyle.css";
	public static String pathToCommonStyle          = rscPathRoot + "css/commonStyle.css";
	public static String pathToAppGlobal            = rscPathRoot + "css/appglobal.css";
	public static String pathToBackgroundImage      = rscPathRoot + "img/background.png";
	public static String pathToExamsIcon            = rscPathRoot + "img/exams.png";
	public static String pathToExamIcon             = rscPathRoot + "img/exam.png";
	public static String pathToAddIcon              = rscPathRoot + "img/add.png";
	public static String pathToRemoveIcon           = rscPathRoot + "img/remove.png";
	public static String pathToQuestionSetIcon      = rscPathRoot + "img/set.png";
	public static String pathToQuestionIcon         = rscPathRoot + "img/question.png";
	public static String pathToCorrectAnswerIcon    = rscPathRoot + "img/answerCorrect.png";
	public static String pathToIncorrectAnswerIcon  = rscPathRoot + "img/answerIncorrect.png";
	public static String pathToMasterPropertiesFile = rscPathRoot + "persistence/teacherClient.prop";

	/*
	 * rsc.style
	 */
	public static String defaultStylesheets = "commonStyle.css tClientStyle.css";

	public static String treeButtonDefaultStyleClass = "btnDefault";
	public static String treeButtonCreateStyleClass  = "btnHoverCreate";
	public static String treeButtonDestroyStyleClass = "btnHoverDestroy";

	public static String stringPaneDefaultStyleClass              = "charPaneDefaultSuper";
	public static String stringPaneDefaultSubStyleClass           = "charPaneDefault";
	public static String stringPaneDefaultSubActiveStyleClass     = "charPaneDefaultActive";
	public static String stringPaneDefaultTextStyleClass          = "charPaneDefaultText";
	public static String stringPaneDefaultSmallTextStyleClass     = "charPaneSmallText";
	public static String stringPaneDefaultVerySmallTextStyleClass = "charPaneVerySmallText";

	/*
	 * high-level containers
	 */
	public static Stage stage;
	public static Scene primaryScene;
	public static Node topNode    = new HBox();
	public static Node leftNode   = new HBox();
	public static HBox centerNode = new HBox();
	public static Node rightNode  = new HBox();
	public static Node bottomNode = new HBox();
	public static TreeItem<TreeElement> examsRoot;

	/*
	 * globally accessible nodes
	 */
	public static ExamInitNode    examInitNode;
	public static ExamNode        examNode;
	public static QuestionSetNode questionSetNode;

	public static QuestionNode     questionNode;
	public static QuestionInitNode questionInitNode;
	public static QuestionTypeNode questionTypeNode;

	public static ExamExportNode examExportNode;

	/*
	 * grammar charset
	 */
	public static ArrayList<Character> charset              = new ArrayList<Character>();
	public static String               defaultCharsetString = ". , ? ! ; : -";// /u2013 (em dash) /u2014 (en dash)

	/*
	 * logging
	 */
	public static String logFormat = "%d{ISO8601} [%t] %-5p %c %x - %m%n";
	public static Logger teacherClientLogger;
	public static DebugWindow debugWindow = new DebugWindow(false);

	/*
	 * file system compliance
	 */
	public static String illegalFileCharacters   = "/ ? < > \\ : * | ^ .";
	public static String reservedSystemFileNames = "com1 com2 com3 com4 com5 com6 com7 com 8 com9 " +
			"lpt1 lpt2 lpt3 lpt4 lpt5 lpt6 lpt7 lpt8 lpt9 " +
			"con nul prn";
	public static String[] illegalFileCharactersList;
	public static String[] reservedSystemFileNamesList;

	static
	{
		illegalFileCharactersList = illegalFileCharacters.split(" ");
		reservedSystemFileNamesList = reservedSystemFileNames.split(" ");
	}

	public TeacherClient() {}

	/**
	 * main
	 * @param args, program args: --disable-dep-check, --disable-fullspeed, --disable-caspian, <--debug | --trace>
	 */
	public static void main(String[] args)
	{
		if (!Arrays.asList(args).contains("--disable-dep-check"))
		{
			try
			{
				System.out.println("apache commons: " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/commons/lang3/exception/ExceptionUtils.class"));
				System.out.println("apache io:      " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/commons/io/FileUtils.class"));
				System.out.println("apache log4j:   " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/log4j/Logger.class"));
				System.out.println("apache pdfbox:  " + TeacherClient.class.getClassLoader().getResources(
						"org/apache/pdfbox/pdmodel.PDDocument.class"));
				System.out.println("javamail:       " + TeacherClient.class.getClassLoader().getResources(
						"javax/mail/Version.class"));
			}
			catch (Exception e)
			{
				System.out.println("error enumerating dependencies");
				throw new RuntimeException("error enumerating dependencies");
			}
		}
		else
		{
			System.out.println("skipped dependency link check");
		}

		ConsoleAppender console = new ConsoleAppender();
		console.setLayout(new PatternLayout(logFormat));
		console.setThreshold(Level.TRACE);
		console.activateOptions();
		Logger.getRootLogger().addAppender(console);

		WriterAppender debugWindowAppender = new WriterAppender(
				new PatternLayout(logFormat), TeacherClient.debugWindow);

		debugWindowAppender.setThreshold(Level.INFO);
		if (Arrays.asList(args).contains("--debug"))
			debugWindowAppender.setThreshold(Level.DEBUG);
		if (Arrays.asList(args).contains("--trace"))
			debugWindowAppender.setThreshold(Level.TRACE);

		debugWindowAppender.activateOptions();
		Logger.getRootLogger().addAppender(debugWindowAppender);

		teacherClientLogger = Logger.getLogger(TeacherClient.class.getName());
		teacherClientLogger.setLevel(Level.ALL);

		teacherClientLogger.info("starting VSS teacher client");
		teacherClientLogger.trace("init system properties");
		new JFXPanel();
		if (!Arrays.asList(args).contains("--disable-fullspeed"))
			System.setProperty("javafx.animation.fullspeed", Boolean.toString(true));
		if (!Arrays.asList(args).contains("--disable-caspian"))
			System.setProperty("javafx.userAgentStylesheetUrl", "caspian");
		teacherClientLogger.trace("launch");

		try
		{
			launch(args);
		}
		catch (Exception e)
		{
			teacherClientLogger.fatal("unhandled root exception: " + GenericUtils.exceptionToString(e));
			PopupManager.showMessage("unhandled root exception: " + GenericUtils.exceptionToString(e));
		}
	}

	/**
	 * JavaFX entry call
	 * @param primaryStage stage
	 */
	public void start(final Stage primaryStage)
	{
		TeacherClient.stage = primaryStage;
		TeacherClient.stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent windowEvent)
			{
				if (!clearPersistenceFlagged)
					System.out.println("TODO: save open data");
				else
				{
					new File(System.getProperty("user.home") + "/.vss-teacherclient").deleteOnExit();
					//vasolsimFileRoot.deleteOnExit();
				}

				Platform.exit();
			}
		});

		TeacherClient.primaryScene = new Scene(new VBox(), 960, 720);

		Preloader.stage = TeacherClient.stage;
		Preloader.preloadTitle = TeacherClient.preloadTitle;
		Preloader.load(getInitRoutine(),
		               getOnSuccessRoutine(),
		               Preloader.getDefaultOnFailHandler());
	}

	/**
	 * creates the initialization task for the preloader
	 * @return the initialization task
	 */
	@Nonnull
	public static ExternalTask<Void> getInitRoutine()
	{
		return new ExternalTask<Void>()
		{
			@Override
			protected Void call() throws Exception
			{
				try
				{
					updateMessage("Initializing core elements...");
					GenericUtils.pause();

					/*
					 * create top node, the menu bar
					 */
					TeacherClient.topNode = MenuNode.getMenuNode();

					/*
					 * create left node, will contain the tree
					 */
					TeacherClient.leftNode = LeftNode.getLeftNode();

					/*
					 * create the center node
					 */
					TeacherClient.centerNode = CenterNode.getCenterRoot();

					/*
					 * create the bottom node
					 */
					TeacherClient.bottomNode = BottomNode.getBottomNode();

					/*
					 * bind nodes to the scene root
					 */
					final BorderPane border = new BorderPane();
					border.setTop(TeacherClient.topNode);
					border.setLeft(TeacherClient.leftNode);
					border.setCenter(TeacherClient.centerNode);
					border.setRight(TeacherClient.rightNode);
					border.setBottom(TeacherClient.bottomNode);

					/*
					 * check for filesystem persistence (saved preferences and data)
					 */
					updateMessage("Checking for persistence...");
					GenericUtils.pause();

					File vasolsimFileRoot = new File(System.getProperty("user.home") + "/.vss-teacherclient");
					File rsc              = new File(vasolsimFileRoot.getAbsolutePath() + "/rsc");
					File data             = new File(vasolsimFileRoot.getAbsolutePath() + "/data");
					File img              = new File(vasolsimFileRoot.getAbsolutePath() + "/rsc/img");

					File propFile         = new File(vasolsimFileRoot.getAbsolutePath() + "/teacherClient.prop");
					File imgFile          = new File(img.getAbsolutePath() + "/background.png");

					boolean canLoadPersistence = true;
					if (!(vasolsimFileRoot.isDirectory()
							&& rsc.isDirectory()
							&& data.isDirectory()
							&& img.isDirectory()
							&& propFile.isFile()
							&& imgFile.isFile()))
					{
						updateMessage("Persistence absent. Creating...");
						GenericUtils.pause();

						vasolsimFileRoot.mkdirs();
						rsc.mkdirs();
						data.mkdirs();
						img.mkdirs();

						/*
						 * failure to initialize required resources for persistence
						 */
						if (!(vasolsimFileRoot.isDirectory()
								&& rsc.isDirectory()
								&& data.isDirectory()
								&& img.isDirectory()))
						{
							updateMessage("Error creating persistence. Attempting to recover...");
							GenericUtils.pause();
							loadDefaultResources(this);
							canLoadPersistence = false;
						}
						else
						{
							try
							{
								if (!(new File(vasolsimFileRoot.getAbsolutePath() + "/teacherClient.prop").isFile()))
									GenericUtils.exportResource(pathToMasterPropertiesFile,
									                            vasolsimFileRoot.getAbsolutePath() +
											                            "/teacherClient.prop");

								if (!(new File(rsc.getAbsolutePath() + "/commonStyle.css").isFile()))
									GenericUtils.exportResource(pathToCommonStyle,
									                            rsc.getAbsolutePath() + "/commonStyle.css");

								if (!(new File(rsc.getAbsolutePath() + "/tClientStyle.css").isFile()))
									GenericUtils.exportResource(pathToStyle,
									                            rsc.getAbsolutePath() + "/tClientStyle.css");

								if (!(new File(rsc.getAbsolutePath() + "/appglobal.css").isFile()))
									GenericUtils.exportResource(pathToAppGlobal,
									                            rsc.getAbsolutePath() + "/appglobal.css");

								if (!(new File(img.getAbsolutePath() + "/background.png")).isFile())
									GenericUtils.exportResource(pathToBackgroundImage,
									                            img.getAbsolutePath() + "/background.png");
							}
							/*
							 * failure to copy master to visible or persistent resources
							 */
							catch (Exception e)
							{
								updateMessage("Error creating persistence. Attempting to recover...");
								GenericUtils.pause();
								loadDefaultResources(this);
								canLoadPersistence = false;
							}
						}
					}

					if (canLoadPersistence)
					{
						updateMessage("Persistence found. Checking data...");
						GenericUtils.pause();

						FileInputStream fis = new FileInputStream(propFile);
						Properties properties = new Properties();
						properties.load(fis);
						fis.close();

						for (String s : properties.getProperty("charset", defaultCharsetString).split(" "))
							TeacherClient.charset.add(s.charAt(0));
						TeacherClient.charset.add(' ');

						for (String s : properties.getProperty("stylesheet", defaultStylesheets).split(" "))
							TeacherClient.primaryScene.getStylesheets().add("file:///" + rsc.getAbsolutePath() + "/" + s);
					}

					/*
					 * initialize assets that require a FX thread for initialization.
					 */
					updateMessage("Scheduling visual initialization...");
					GenericUtils.pause();

					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							TeacherClient.examInitNode = new ExamInitNode();
							TeacherClient.examNode = new ExamNode();
							TeacherClient.questionSetNode = new QuestionSetNode();

							TeacherClient.questionNode = new QuestionNode();
							TeacherClient.questionInitNode = new QuestionInitNode();
							TeacherClient.questionTypeNode = new QuestionTypeNode();

							TeacherClient.examExportNode = new ExamExportNode();

							TeacherClient.primaryScene.setRoot(border);
						}
					});

					updateProgress(1, 1);
					updateMessage("Done.");

					GenericUtils.pause(1000);
				}
				catch (Exception e)
				{
					teacherClientLogger.fatal(GenericUtils.exceptionToString(e));
				}

				return null;
			}
		};
	}

	/**
	 * creates the on success EventHandler for the preloader
	 * @return event handler
	 */
	public static EventHandler<WorkerStateEvent> getOnSuccessRoutine()
	{
		return new EventHandler<WorkerStateEvent>()
		{
			@Override
			public void handle(WorkerStateEvent workerStateEvent)
			{
				/*
				 * hide and brief pause
				 */
				TeacherClient.stage.hide();
				GenericUtils.pause();

				/*
				 * reset stage
				 */
				TeacherClient.stage.setTitle(TeacherClient.title);
				TeacherClient.stage.setScene(TeacherClient.primaryScene);

				/*
				 * size stage
				 */
				Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
				TeacherClient.stage.setWidth(bounds.getWidth());
				TeacherClient.stage.setHeight(bounds.getHeight());

				/*
				 * show
				 */
				TeacherClient.stage.show();
			}
		};
	}

	/**
	 * loads default resources in the event the persistence creation fails
	 * @param task the task to be logged to
	 */
	public static void loadDefaultResources(ExternalTask task)
	{
		/*
		 * get default rsc.style
		 */
		task.updateMessage("Loading external rsc.style...");
		GenericUtils.pause();

		System.out.println("IM HERE");
		TeacherClient.primaryScene.getStylesheets().add(
				TeacherClient.class.getResource(TeacherClient.pathToStyle).toExternalForm());
		TeacherClient.primaryScene.getStylesheets().add(
				TeacherClient.class.getResource(TeacherClient.pathToCommonStyle).toExternalForm());
		TeacherClient.primaryScene.getStylesheets().add(
				TeacherClient.class.getResource(TeacherClient.pathToAppGlobal).toExternalForm());

		/*
		 * get default charset
		 */
		task.updateMessage("Loading default charset...");
		for (String s : defaultCharsetString.split(" "))
			TeacherClient.charset.add(s.charAt(0));
		TeacherClient.charset.add(' ');
	}
}