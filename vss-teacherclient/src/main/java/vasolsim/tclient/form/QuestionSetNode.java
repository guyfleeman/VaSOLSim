package main.java.vasolsim.tclient.form;

import main.java.vasolsim.common.node.DrawableNode;
import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.common.file.QuestionSet;
import main.java.vasolsim.tclient.TeacherClient;
import main.java.vasolsim.tclient.core.CenterNode;
import main.java.vasolsim.tclient.tree.TreeElement;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static main.java.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/28/14 <p></p>
 */
public class QuestionSetNode implements DrawableNode
{
	private String lastPath = null;
	protected Node questionSetNode;
	public QuestionSet qSet = new QuestionSet();
	public TreeElement boundTreeElement;

	public QuestionSetNode()
	{
		redrawNode(false);
	}

	public void redrawNode(boolean apply)
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label questionSetInfoLabel = new Label(TeacherClient.QUESTION_SET_INFO_LABEL_TEXT);
		questionSetInfoLabel.getStyleClass().add("lbltext");

		Label questionSetNameLabel = new Label(TeacherClient.QUESTION_SET_NAME_LABEL_TEXT);
		questionSetNameLabel.getStyleClass().add("lbltext");

		final TextField questionSetNameField = new TextField();
		questionSetNameField.setPrefWidth(400);

		Button applyNameButton = new Button("Apply");

		HBox spacer = new HBox();
		spacer.setPrefHeight(2);
		spacer.setPrefWidth(2000);
		spacer.getStyleClass().add("lblspacer");

		Label resourceFileInfoLabel = new Label(TeacherClient.RESOURCE_FILE_INFO_LABEL_TEXT);
		resourceFileInfoLabel.getStyleClass().add("lbltext");
		resourceFileInfoLabel.setWrapText(true);

		final Label resourceFileLabel = new Label(lastPath == null ? "File: none" : "File: " + lastPath);
		resourceFileLabel.getStyleClass().add("lbltext");
		resourceFileLabel.setWrapText(true);

		HBox buttonBox = new HBox();
		buttonBox.getStyleClass().add("helementspacing");

		Button loadResourceButton = new Button("Load");
		Button removeResourceButton = new Button("Remove");

		buttonBox.getChildren().addAll(loadResourceButton, removeResourceButton);

		TilePane imageContainer = new TilePane();
		imageContainer.setPrefWidth(2000);
		imageContainer.setVgap(10);
		imageContainer.setHgap(10);
		if (qSet.getResources() != null)
			for (Image i : qSet.getFxResources())
			{
				ImageView iv = new ImageView(i);
				iv.setPreserveRatio(true);
				iv.setFitWidth(150);
				iv.getStyleClass().add("pic");
				imageContainer.getChildren().add(iv);
			}
		else
		{
			Label noImg = new Label("no resource to display");
			noImg.getStyleClass().add("lbltext");
			noImg.setWrapText(true);
			imageContainer.getChildren().add(noImg);
		}

		final ProgressBar bar = new ProgressBar();
		bar.managedProperty().bind(bar.visibleProperty());
		bar.setVisible(false);
		bar.setPrefWidth(2000);

		/*
		 * add elements
		 */
		verticalRoot.getChildren().addAll(questionSetInfoLabel,
		                                  questionSetNameLabel,
		                                  questionSetNameField,
		                                  applyNameButton,
		                                  spacer,
		                                  resourceFileLabel,
		                                  bar,
		                                  imageContainer,
		                                  buttonBox);

		/*
		 * Init listeners
		 */
		applyNameButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (questionSetNameField.getText() != null
						&& questionSetNameField.getText().trim().length() > 0)
				{
					boundTreeElement.label.setText(questionSetNameField.getText());
					questionSetNameField.clear();
				}
			}
		});

		loadResourceButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				FileChooser fc = new FileChooser();
				File resource = fc.showOpenDialog(TeacherClient.stage);
				String tmpPath;
				try
				{
					tmpPath = resource.getCanonicalPath();
				}
				catch (IOException e)
				{
					tmpPath = resource.getAbsolutePath();
				}

				if (tmpPath.equals(""))
					tmpPath = lastPath;
				else
					lastPath = tmpPath;

				final String path = tmpPath;

				Task pdfRender = new Task<Void>()
				{
					@Override
					protected Void call() throws Exception
					{
						int maxProgress = getPDFPages(new File(path)) * 2;
						PDDocument doc = PDDocument.load(new File(path));
						@SuppressWarnings("unchecked")
						List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
						Iterator<PDPage> iterator = pages.iterator();
						BufferedImage[] images = new BufferedImage[pages.size()];
						for (int i = 0; iterator.hasNext(); i++)
						{
							images[i] = iterator.next().convertToImage();
							updateProgress(i, maxProgress);
						}

						doc.close();
						qSet.setResources(images);

						Image[] fxImages = new Image[images.length];
						for (int i = 0; i < images.length; i++)
						{
							fxImages[i] = convertBufferedImageToFXImage(images[i]);
							updateProgress(images.length + i, maxProgress);
						}

						qSet.setFxResources(fxImages);

						return null;
					}
				};
				bar.setVisible(true);
				bar.progressProperty().bind(pdfRender.progressProperty());
				resourceFileLabel.setText("File: " + tmpPath);
				new Thread(pdfRender).start();
				//qSet.loadPDFResource(tmpPath);
				//redrawNode(true);

				pdfRender.setOnSucceeded(new EventHandler<WorkerStateEvent>()
				{
					@Override
					public void handle(WorkerStateEvent workerStateEvent)
					{
						bar.setVisible(false);
						redrawNode(true);
					}
				});

				pdfRender.setOnFailed(new EventHandler<WorkerStateEvent>()
				{
					@Override
					public void handle(WorkerStateEvent workerStateEvent)
					{
						bar.setVisible(false);
						qSet.setResources(null);
						qSet.setFxResources(null);
						PopupManager.showMessage("PDF failed to open");
						redrawNode(false);
					}
				});
			}
		});

		removeResourceButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				qSet.removeResource();
				lastPath = null;
				redrawNode(true);
			}
		});

		questionSetNode = horizontalRoot;

		if (apply)
		{
			CenterNode.addScrollRoot();
			CenterNode.getScrollRoot().setContent(questionSetNode);
		}
	}

	public Node getNode()
	{
		return questionSetNode;
	}
}
