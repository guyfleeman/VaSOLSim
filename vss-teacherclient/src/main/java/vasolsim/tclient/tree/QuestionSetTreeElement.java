package main.java.vasolsim.tclient.tree;


import main.java.vasolsim.common.notification.PopupManager;
import main.java.vasolsim.common.file.QuestionSet;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.tclient.tree.TreeElement;
import main.java.vasolsim.tclient.core.CenterNode;
import main.java.vasolsim.tclient.TeacherClient;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class QuestionSetTreeElement extends TreeElement
{
	public ExamTreeElement        parent;
	public QuestionSetTreeElement instance;
	public QuestionSet                 qSet      = new QuestionSet();
	public Vector<QuestionTreeElement> questions = new Vector<QuestionTreeElement>();

	public static Logger logger = Logger.getLogger(QuestionSetTreeElement.class.getName());

	public QuestionSetTreeElement()
	{
		super(new Label("New QSet"),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToAddIcon,
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonCreateStyleClass,
				      12),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToRemoveIcon,
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonDestroyStyleClass,
				      12)
		);

		instance = this;
	}

	public void initListeners()
	{
		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("question creation invoked");
				TeacherClient.questionInitNode.parent = instance;

				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(TeacherClient.questionTypeNode.getNode());
			}
		});

		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				if (PopupManager.askYesNo("You are about to delete this QandA set. All data will be lost. Continue?"))
				{
					logger.info("question set removal invoked -> " + qSet.getName());
					parent.qSets.remove(instance);
					parent.treeElementReference.getChildren().remove(treeElementReference);
					CenterNode.getStyledRoot().getChildren().clear();
				}
			}
		});

		this.instance.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("display question set info -> " + qSet.getName());
				CenterNode.addScrollRoot();
				TeacherClient.questionSetNode.boundTreeElement = instance;
				TeacherClient.questionSetNode.qSet = instance.qSet;
				CenterNode.getScrollRoot().setContent(TeacherClient.questionSetNode.getNode());
			}
		});
	}
}
