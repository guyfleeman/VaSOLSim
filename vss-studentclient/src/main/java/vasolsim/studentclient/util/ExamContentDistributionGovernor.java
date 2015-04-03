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

package main.java.vasolsim.studentclient.util;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.file.Question;
import main.java.vasolsim.common.file.QuestionSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Vector;

/**
 * @author willstuckey
 * @date 3/16/15
 * <p>This class is responsible for tracking loaded exams and their progress. It will manage the active exam and
 * govern the od in which its contents will be displayed. For CAT tests, this class will interface with the remote
 * to pull questions, and push answers. It will also verify remotes loaded by ExamBuilder.</p>
 */
public class ExamContentDistributionGovernor implements ContentDistributionGovernor
{
	protected static ExamContentDistributionGovernor instance;

	@Nullable
	protected static ContentDistributionGovernor remoteContentDistributionGovernor;
	@Nonnull
	protected static final Vector<GovernedExamNode> governedExams = new Vector<>(1, 1);
	@Nullable
	protected static GovernedExamNode activeGovernedExam;

	protected static int questionSetIndex = 0;
	protected static int questionSetResourceIndex = 0;
	protected static int questionIndex = 0;

	static
	{
		ExamContentDistributionGovernor.instance = new ExamContentDistributionGovernor();
	}

	public ExamContentDistributionGovernor getInstance()
	{
		return instance;
	}

	/**
	 * returns if a remote governor is avaliable
	 * @return
	 */
	public boolean hasRemoteContentDistributionGovernor()
	{
		return ExamContentDistributionGovernor.remoteContentDistributionGovernor != null;
	}

	/**
	 * sets the remote distributor
	 * @param remoteContentDistributionGovernor
	 */
	//TODO encapsulate with AccessController to prevent plugins from swapping a valid CDG with a bad one
	//its all handled upstream anyway, but best to do this
	public void setRemoteContentDistributionGovernor(ContentDistributionGovernor remoteContentDistributionGovernor)
	{
		ExamContentDistributionGovernor.remoteContentDistributionGovernor = remoteContentDistributionGovernor;
	}

	/**
	 * gets the remote distributor
	 * @return
	 */
	public ContentDistributionGovernor getRemoteContentDistributionGovernor()
	{
		return ExamContentDistributionGovernor.remoteContentDistributionGovernor;
	}

	/**
	 * checks if there is an active exam. Active exams are either in-progress, or staged for start.
	 * @return
	 */
	public boolean hasActiveExam()
	{
		return activeGovernedExam != null;
	}

	/**
	 * gets the active question set. Returns null if there is no active exam.
	 * @return
	 */
	@Nullable
	public QuestionSet getActiveQuestionSet()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL)
				return activeGovernedExam.getExam().getQuestionSets().get(questionSetIndex);
			else
				return ExamContentDistributionGovernor.remoteContentDistributionGovernor.getActiveQuestionSet();
		}

		return null;
	}

	/**
	 * Returns if the active question set has resources. NOTE: this method will return true if there is no active exam.
	 * It is intended to check if teh resource window node needs to be active.
	 * @return
	 */
	public boolean hasActiveResourcesForQuestionSet()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL)
				return getActiveQuestionSet().hasResources();
			else
				return ExamContentDistributionGovernor.remoteContentDistributionGovernor.hasActiveResourcesForQuestionSet();
		}

		return true;
	}

	/**
	 * gets the active resource set. Returns null if there is no active exam.
	 * @return
	 */
	@Nullable
	public Image[] getActiveResourceSet()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL
					    && getActiveQuestionSet().hasResources())
				return getActiveQuestionSet().getFxResources();
			else
				return ExamContentDistributionGovernor.remoteContentDistributionGovernor.getActiveResourceSet();
		}

		return null;
	}

	/**
	 * gets the active resource. Returns null if there is no active exam.
	 * @return
	 */
	@Nullable
	public Image getActiveResource()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL
					&& hasActiveResourcesForQuestionSet())
				return getActiveResourceSet()[questionSetResourceIndex];
			else
				return ExamContentDistributionGovernor.remoteContentDistributionGovernor.getActiveResource();
		}

		return null;
	}

	/**
	 * gets the active question. Return null if there is no active exam.
	 * @return
	 */
	@Nullable
	public Question getActiveQuestion()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL)
				return getActiveQuestionSet().getQuestions().get(questionIndex);
			else
				return ExamContentDistributionGovernor.remoteContentDistributionGovernor.getActiveQuestion();
		}

		return null;
	}

	/**
	 * advances the page. Effects the output of getActiveQuestionSet() and getActiveQuestion().
	 */
	public void nextPage()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL)
			{
				//can advance question
				if (questionIndex < getActiveQuestionSet().getQuestions().size() - 1)
					questionIndex++;
					//can't advance question, but can advance to next question set
				else if (questionIndex == getActiveQuestionSet().getQuestions().size() - 1
						         && questionSetIndex < activeGovernedExam.getExam().getQuestionSets().size() - 1)
				{
					questionSetIndex++;
					questionIndex = 0;
				}
				//cannot advance question or question set (end reached)
				else if (questionIndex == getActiveQuestionSet().getQuestions().size() - 1
						         && questionIndex == activeGovernedExam.getExam().getQuestionSets().size() - 1)
					invokeExamComplete();
			}
			else
				ExamContentDistributionGovernor.remoteContentDistributionGovernor.nextPage();
		}
	}

	/**
	 * decrements the page. Effects the output of getActiveQuestionSet() and getActiveQuestion().
	 */
	public void previousPage()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL)
			{
				//can decrement question
				if (questionIndex > 0)
					questionIndex--;
				//can't decrement question, but can decrement set
				else if (questionIndex == 0
						         && questionSetIndex > 0)
				{
					questionSetIndex--;
					questionIndex = getActiveQuestionSet().getQuestions().size() - 1;
				}
			}
			else
				ExamContentDistributionGovernor.remoteContentDistributionGovernor.previousPage();
		}
	}

	/**
	 * advances the resource page. Effects the output of getActiveResource()
	 */
	public void nextResorucePage()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL
						&& getActiveQuestionSet().hasResources()
					    && questionSetResourceIndex < getActiveResourceSet().length - 1)
				questionSetResourceIndex++;
			else
				ExamContentDistributionGovernor.remoteContentDistributionGovernor.nextResorucePage();
		}
	}

	/**
	 * decrements the resource page. Effects the output of getActiveResource()
	 */
	public void previousResourcePage()
	{
		if (hasActiveExam())
		{
			if (activeGovernedExam.getExamType() == ExamType.LOCAL
						&& getActiveQuestionSet().hasResources()
						&& questionSetResourceIndex > 0)
				questionSetResourceIndex--;
			else
				ExamContentDistributionGovernor.remoteContentDistributionGovernor.previousResourcePage();
		}
	}

	/**
	 * effect undecided
	 */
	public void invokeExamComplete()
	{
		if (activeGovernedExam.getExamType() == ExamType.LOCAL)
		{

		}
		else
			ExamContentDistributionGovernor.remoteContentDistributionGovernor.invokeExamComplete();
	}

	/**
	 * @return governed exams
	 */
	public Vector<GovernedExamNode> getGovernedExams()
	{
		return governedExams;
	}

	/**
	 *
	 * @param exam
	 * @return
	 */
	public GovernedExamNode addGovernedExam(@Nonnull Exam exam, @Nonnull ExamType examType)
	{
		if (examType == ExamType.LOCAL)
		{
			if (exam.getScrambleQuestionSetOrder())
			{
				//TODO scramble set order
			}

			for (QuestionSet qSet : exam.getQuestionSets())
			{
				if (qSet.getScrambleQuestionOrder())
				{
					//TODO scramble question order
				}
			}

			for (QuestionSet qSet : exam.getQuestionSets())
			{
				for (Question q : qSet.getQuestions())
				{
					if (q.getScrambleAnswers())
					{
						//TODO scramble answer order
					}
				}
			}
		}

		GovernedExamNode governedExamNode = new GovernedExamNode(exam, examType);
		governedExams.add(governedExamNode);
		return governedExamNode;
	}

	/**
	 *
	 * @param governedExamNode
	 * @return
	 */
	@Nullable
	public GovernedExamNode setActiveGovernedExam(@Nonnull GovernedExamNode governedExamNode)
	{
		synchronized (governedExams)
		{
			int foundIndex = -1;
			for (int index = 0; index < governedExams.size(); index++)
				if (governedExamNode.equals(governedExams.get(index)))
				{
					foundIndex = index;
					break;
				}

			if (foundIndex == -1)
				return null;

			activeGovernedExam = governedExams.get(foundIndex);
			return activeGovernedExam;
		}
	}

	/**
	 *
	 * @param exam
	 * @return
	 */
	@Nullable
	public GovernedExamNode setActiveGovernedExam(@Nonnull Exam exam)
	{
		synchronized (governedExams)
		{
			int foundIndex = -1;
			for (int index = 0; index < governedExams.size(); index++)
				if (exam.equals(governedExams.get(index).getExam()))
				{
					foundIndex = index;
					break;
				}

			if (foundIndex == -1)
				return null;

			activeGovernedExam = governedExams.get(foundIndex);
			return activeGovernedExam;
		}
	}

	/**
	 *
	 * @param name test name
	 * @return
	 */
	@Nullable
	public GovernedExamNode setActiveGovernedExam(@Nonnull String name)
	{
		synchronized (governedExams)
		{
			int foundIndex = -1;
			for (int index = 0; index < governedExams.size(); index++)
				if (name.equals(governedExams.get(index).getExam().getTestName()))
				{
					foundIndex = index;
					break;
				}

			if (foundIndex == -1)
				return null;

			activeGovernedExam = governedExams.get(foundIndex);
			return activeGovernedExam;
		}
	}

	/**
	 * @return
	 */
	@Nonnull
	public Parent[] getExamUIs()
	{
		synchronized (governedExams)
		{
			Parent[] nodes = new Parent[governedExams.size()];
			for (int index = 0; index < governedExams.size(); index++)
			{
				nodes[index] = governedExams.get(index).getParent();
			}

			return nodes;
		}
	}

	/**
	 *
	 * @return
	 */
	@Nullable
	public Parent getActiveExamUI()
	{
		if (hasActiveExam())
			return activeGovernedExam.getParent();

		return null;
	}
}
