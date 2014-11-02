package com.gmail.guyfleeman.vasolsim.tclient.element.tree;

import com.gmail.guyfleeman.vasolsim.tclient.TeacherClient;
import com.gmail.guyfleeman.vasolsim.tclient.element.ImageButton;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.form.ExamStatsInfoNode;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Vector;

/**
 * @author guyfleeman
 * @date 7/23/14
 * <p></p>
 */
public class ExamsTreeElement extends TreeElement
{
    public static Vector<ExamTreeElement> exams = new Vector<ExamTreeElement>();

    public ExamsTreeElement()
    {
        super(new Label("Exams"),
                new ImageButton(
                        TeacherClient.class,
                        TeacherClient.pathToAddIcon,
                        "btnconfirmhover",
                        "btnnormal",
                        12),
                null);

        super.addButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                CenterNode.getStyledRoot().getChildren().clear();
	            CenterNode.addScrollRoot();
	            CenterNode.getScrollRoot().setContent(TeacherClient.examStatsInfoNode.getNode());
            }
        });
    }
}
