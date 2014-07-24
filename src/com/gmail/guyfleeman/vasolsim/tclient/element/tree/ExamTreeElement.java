package com.gmail.guyfleeman.vasolsim.tclient.element.tree;

import com.gmail.guyfleeman.vasolsim.common.file.Exam;
import com.gmail.guyfleeman.vasolsim.common.notification.PopupManager;
import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.ImageButton;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
 */
public class ExamTreeElement extends TreeElement
{
	public Exam exam;

	public Vector<QuestionSetTreeElement> sets = new Vector<QuestionSetTreeElement>();

	public ExamTreeElement instance;

	public ExamTreeElement()
	{
		super(new Label("New Exam"),
				new ImageButton(
						TeacherClient.class,
						TeacherClient.pathToAddIcon,
						"btnconfirmhover",
						"btnnormal",
						12),
				new ImageButton(
						TeacherClient.class,
						TeacherClient.pathToRemoveIcon,
						"btndenyhover",
						"btnnormal",
						12)
				);

		instance = this;
	}

	public void initListeners()
	{
		super.removeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (PopupManager.askYesNo("You are about to delete this exam. All data will be lost. Continue?"))
				{
					ExamsTreeElement.exams.remove(instance);
					TeacherClient.examsRoot.getChildren().remove(objectReference);
				}
			}
		});
	}
}
