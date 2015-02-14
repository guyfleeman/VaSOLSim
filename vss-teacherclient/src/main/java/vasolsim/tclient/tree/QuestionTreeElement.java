package main.java.vasolsim.tclient.tree;

import main.java.vasolsim.common.file.Question;
import main.java.vasolsim.tclient.tree.TreeElement;
import main.java.vasolsim.tclient.TeacherClient;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.tclient.core.CenterNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Logger;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14 <p></p>
 */
public class QuestionTreeElement extends TreeElement
{
	public QuestionSetTreeElement parent;
	public QuestionTreeElement    instance;
	public Question                  question = new Question();

	public static Logger logger = Logger.getLogger(QuestionTreeElement.class.getName());

	public QuestionTreeElement()
	{
		super(new Label("New Question"),
		      null,
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
		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("question removal invoked -> " + question.getName());
				parent.questions.remove(instance);
				parent.treeElementReference.getChildren().remove(treeElementReference);
				CenterNode.getStyledRoot().getChildren().clear();
			}
		});

		this.instance.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("display question info -> " + question.getName());
				CenterNode.addScrollRoot();

				TeacherClient.questionNode.boundTreeElement = instance;
				TeacherClient.questionNode.redrawNode(true);
			}
		});
	}
}
