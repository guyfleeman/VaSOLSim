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

import javafx.scene.image.Image;
import main.java.vasolsim.common.file.Question;
import main.java.vasolsim.common.file.QuestionSet;

import javax.annotation.Nullable;

/**
 * @author willstuckey
 * @date 4/2/15 <p></p>
 */
public interface ContentDistributionGovernor
{
	/**
	 * exam type
	 */
	public enum ExamType
	{
		/**
		 * data loaded from file system, and processed locally
		 */
		LOCAL,
		/**
		 * data loaded from a remote server, and processed remotely
		 */
		REMOTE
	}

	/**
	 * checks if there is an active exam. Active exams are either in-progress, or staged for start.
	 * @return
	 */
	public boolean hasActiveExam();

	/**
	 * gets the active question set. Returns null if there is no active exam.
	 * @return
	 */
	@Nullable
	public QuestionSet getActiveQuestionSet();

	/**
	 * Returns if the active question set has resources. NOTE: this method will return true if there is no active exam.
	 * It is intended to check if teh resource window node needs to be active.
	 * @return
	 */
	public boolean hasActiveResourcesForQuestionSet();

	/**
	 * gets the active resource set. Returns null if there is no active exam.
	 * @return
	 */
	@Nullable
	public Image[] getActiveResourceSet();

	/**
	 * gets the active resource. Returns null if there is no active exam.
	 * @return
	 */
	@Nullable
	public Image getActiveResource();

	/**
	 * gets the active question. Return null if there is no active exam.
	 * @return
	 */
	@Nullable
	public Question getActiveQuestion();

	/**
	 * advances the page. Effects the output of getActiveQuestionSet() and getActiveQuestion().
	 */
	public void nextPage();

	/**
	 * decrements the page. Effects the output of getActiveQuestionSet() and getActiveQuestion().
	 */
	public void previousPage();

	/**
	 * advances the resource page. Effects the output of
	 */
	public void nextResorucePage();

	/**
	 * decrements the resource page. Effects the output of getActiveResource()
	 */
	public void previousResourcePage();

	public void invokeExamComplete();
}
