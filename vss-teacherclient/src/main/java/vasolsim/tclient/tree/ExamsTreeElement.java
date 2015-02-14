package main.java.vasolsim.tclient.tree;

import main.java.vasolsim.tclient.tree.TreeElement;
import main.java.vasolsim.common.node.ImageButton;
import main.java.vasolsim.tclient.TeacherClient;
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
public class ExamsTreeElement extends TreeElement
{
	public static Vector<ExamTreeElement> exams = new Vector<ExamTreeElement>();
	public static Logger logger = Logger.getLogger(ExamsTreeElement.class.getName());

	public ExamsTreeElement()
	{
		super(new Label("Exams"),
		      new ImageButton(
				      TeacherClient.class,
				      TeacherClient.pathToAddIcon,
				      TeacherClient.treeButtonDefaultStyleClass,
				      TeacherClient.treeButtonCreateStyleClass,
				      12),
		      null);

		super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent mouseEvent)
			{
				logger.info("exam creation invoked");
				CenterNode.getStyledRoot().getChildren().clear();
				CenterNode.addScrollRoot();
				CenterNode.getScrollRoot().setContent(TeacherClient.examInitNode.getNode());
			}
		});
	}
}
