package com.gmail.guyfleeman.vasolsim.tclient.element.form;

import com.gmail.guyfleeman.vasolsim.common.file.Exam;
import com.gmail.guyfleeman.vasolsim.common.struct.QuestionSet;
import com.gmail.guyfleeman.vasolsim.tclient.element.core.CenterNode;
import com.gmail.guyfleeman.vasolsim.tclient.element.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author williamstuckey
 * @date 7/25/14
 * <p></p>
 */
public class ExamInfoNode
{
    protected static Node examInfoNode;
    protected static Exam exam;
	public static TreeElement boundTreeElement;

    static
    {
        updateNode();
    }

    private static void updateNode()
    {
        HBox horizontalRoot = new HBox();

        VBox verticalRoot = new VBox();
        verticalRoot.getStyleClass().add("borders");
        horizontalRoot.getChildren().add(verticalRoot);

        Label testStats = new Label(testStatsLabelText);
        testStats.getStyleClass().add("lbltext");

        Label testNameDispLabel = new Label("Name: " +
                (exam == null || exam.getTestName() == null || exam.getTestName().equals(NO_TEST_NAME_GIVEN)
                        ? "none"
                        : exam.getTestName()));
        testNameDispLabel.getStyleClass().add("lbltextsub");

        Label testAuthorDispLabel = new Label("Author: " +
                (exam == null || exam.getAuthorName() == null || exam.getAuthorName().equals(NO_AUTHOR_NAME_GIVEN)
                        ? "none"
                        : exam.getAuthorName()));
        testAuthorDispLabel.getStyleClass().add("lbltextsub");

        Label testSchoolDispLabel = new Label("School: " +
                (exam == null || exam.getSchoolName() == null || exam.getSchoolName().equals(NO_SCHOOL_NAME_GIVEN)
                        ? "none"
                        : exam.getSchoolName()));
        testSchoolDispLabel.getStyleClass().add("lbltextsub");

        Label testPeriodDispLabel = new Label("Period: " +
                (exam == null || exam.getPeriodName() == null || exam.getPeriodName().equals(NO_PERIOD_ID_GIVEN)
                        ? "none"
                        : exam.getPeriodName()));
        testPeriodDispLabel.getStyleClass().add("lbltextsub");

        Label spacerOne = new Label();

        Label testQSetLabel = new Label("Question Set Count: " + (exam != null && exam.getQuestionSets() != null
                ? exam.getQuestionSets().size()
                : 0));
        testQSetLabel.getStyleClass().add("lbltextsub");

        int qCount = 0;
        if (exam != null && exam.getQuestionSets() != null)
            for (QuestionSet qs : exam.getQuestionSets())
                if (qs != null && qs.getQuestions() != null)
                    qCount += qs.getQuestions().size();
        Label testQNumLabel = new Label("Question Count: " + qCount);
        testQNumLabel.getStyleClass().add("lbltextsub");

        int rscCount = 0;
        if (exam != null && exam.getQuestionSets() != null)
            for (QuestionSet qs : exam.getQuestionSets())
                if (qs.getResourceType() != ResourceType.NONE)
                    rscCount++;
        Label testRscCount = new Label("Resource Count: " + rscCount);
        testRscCount.getStyleClass().add("lbltextsub");

        HBox spacerTwo = new HBox();
        spacerTwo.getStyleClass().add("lblspacer");
        spacerTwo.setPrefHeight(1);
        spacerTwo.setPrefWidth(2000);

		/*
        Rectangle line = new Rectangle(400, 2);
		line.prefWidth(2000);
		line.prefHeight(20);
		line.getStyleClass().add("rectspacer");
		*/

        Label testNameLabel = new Label(testNameLabelText);
        testNameLabel.getStyleClass().add("lbltext");

        final TextField testNameField = new TextField();
        testNameField.setMaxWidth(400);

        Label authorNameLabel = new Label(authorNameLabelText);
        authorNameLabel.getStyleClass().add("lbltext");

        final TextField authorNameField = new TextField();
        authorNameField.setMaxWidth(400);

        Label schoolNameLabel = new Label(schoolNameLabelText);
        schoolNameLabel.getStyleClass().add("lbltext");

        final TextField schoolNameField = new TextField();
        schoolNameField.setMaxWidth(400);

        Label periodNameLabel = new Label(periodNameLabelText);
        periodNameLabel.getStyleClass().add("lbltext");

        final TextField periodNameField = new TextField();
        periodNameField.setMaxWidth(150);

        Button applyButton = new Button("Apply");

        verticalRoot.getChildren().addAll(testStats,
                testNameDispLabel,
                testAuthorDispLabel,
                testSchoolDispLabel,
                testPeriodDispLabel,
                spacerOne,
                testQSetLabel,
                testQNumLabel,
                testRscCount,
                spacerTwo,
				/* line, */
                testNameLabel,
                testNameField,
                authorNameLabel,
                authorNameField,
                schoolNameLabel,
                schoolNameField,
                periodNameLabel,
                periodNameField,
                applyButton);

		/*
		 * Listeners
		 */
        applyButton.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                if (testNameField.getText() != null && testNameField.getText().trim().length() > 0)
                {
	                exam.setTestName(testNameField.getText());
	                boundTreeElement.label.setText(testNameField.getText());
                }

                if (authorNameField.getText() != null && authorNameField.getText().trim().length() > 0)
                    exam.setAuthorName(authorNameField.getText());

                if (schoolNameField.getText() != null && schoolNameField.getText().trim().length() > 0)
                    exam.setSchoolName(schoolNameField.getText());

                if (periodNameField.getText() != null && periodNameField.getText().trim().length() > 0)
                    exam.setPeriodName(periodNameField.getText());

                updateNode();
                redrawNode();
            }
        });

        examInfoNode = horizontalRoot;
    }

    public static void redrawNode()
    {
        CenterNode.removeScrollRoot();
        CenterNode.addScrollRoot();
        CenterNode.getScrollRoot().setContent(ExamInfoNode.getExamInfoNode());
    }

    public static Exam getExam()
    {
        return exam;
    }

    public static void setExam(Exam exam)
    {
        ExamInfoNode.exam = exam;
        updateNode();
    }

    public static Node getExamInfoNode()
    {
        return examInfoNode;
    }
}
