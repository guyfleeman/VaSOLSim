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

package main.java.vasolsim.teacherclient.form;

import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.file.QuestionSet;
import main.java.vasolsim.common.node.DrawableNode;
import main.java.vasolsim.teacherclient.TeacherClient;
import main.java.vasolsim.teacherclient.core.CenterNode;
import main.java.vasolsim.common.node.tree.TreeElement;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import static main.java.vasolsim.common.GenericUtils.*;

/**
 * @author williamstuckey
 * @date 7/25/14 <p></p>
 */
public class ExamNode implements DrawableNode
{
	protected Node        examInfoNode;
	protected Exam        exam;
	public    TreeElement boundTreeElement;

	public ExamNode()
	{
		redrawNode(false);
	}

	public Node getNode()
	{
		return examInfoNode;
	}

	public void redrawNode(boolean apply)
	{
		HBox horizontalRoot = new HBox();

		VBox verticalRoot = new VBox();
		verticalRoot.getStyleClass().add("borders");
		horizontalRoot.getChildren().add(verticalRoot);

		Label testStats = new Label(TeacherClient.TEST_STATS_LABEL_TEXT);
		testStats.getStyleClass().add("lbltext");

		Label testNameDispLabel = new Label(
				"Name: " + (exam == null
						            || exam.getTestName() == null
						            || exam.getTestName().equals(NO_TEST_NAME_GIVEN)
				            ? "none"
				            : exam.getTestName()));
		testNameDispLabel.getStyleClass().add("lbltextsub");

		Label testAuthorDispLabel = new Label(
				"Author: " + (exam == null
						              || exam.getAuthorName() == null
						              || exam.getAuthorName().equals(NO_AUTHOR_NAME_GIVEN)
				              ? "none"
				              : exam.getAuthorName()));
		testAuthorDispLabel.getStyleClass().add("lbltextsub");

		Label testSchoolDispLabel = new Label(
				"School: " + (exam == null
						              || exam.getSchoolName() == null
						              || exam.getSchoolName().equals(NO_SCHOOL_NAME_GIVEN)
				              ? "none"
				              : exam.getSchoolName()));
		testSchoolDispLabel.getStyleClass().add("lbltextsub");

		Label testPeriodDispLabel = new Label(
				"Period: " + (exam == null
						              || exam.getPeriodName() == null
						              || exam.getPeriodName().equals(NO_PERIOD_ID_GIVEN)
				              ? "none"
				              : exam.getPeriodName()));
		testPeriodDispLabel.getStyleClass().add("lbltextsub");

		Label spacerOne = new Label();

		Label testQSetLabel = new Label(
				"Question Set Count: " + (exam != null
						                          && exam.getQuestionSets() != null
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

		Label testNameLabel = new Label(TeacherClient.TEST_NAME_LABEL_TEXT);
		testNameLabel.getStyleClass().add("lbltext");

		final TextField testNameField = new TextField();
		testNameField.setMaxWidth(400);

		Label authorNameLabel = new Label(TeacherClient.AUTHOR_NAME_LABEL_TEXT);
		authorNameLabel.getStyleClass().add("lbltext");

		final TextField authorNameField = new TextField();
		authorNameField.setMaxWidth(400);

		Label schoolNameLabel = new Label(TeacherClient.SCHOOL_NAME_LABEL_TEXT);
		schoolNameLabel.getStyleClass().add("lbltext");

		final TextField schoolNameField = new TextField();
		schoolNameField.setMaxWidth(400);

		Label periodNameLabel = new Label(TeacherClient.PERIOD_NAME_LABEL_TEXT);
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

				redrawNode(true);
			}
		});

		examInfoNode = horizontalRoot;

		if (apply)
		{
			CenterNode.removeScrollRoot();
			CenterNode.addScrollRoot();
			CenterNode.getScrollRoot().setContent(getNode());
		}
	}

	public Exam getExam()
	{
		return exam;
	}

	public void setExam(Exam exam)
	{
		this.exam = exam;
		redrawNode(false);
	}
}
