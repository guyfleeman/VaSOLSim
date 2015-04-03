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
import javafx.scene.layout.VBox;
import main.java.vasolsim.common.file.Exam;
import main.java.vasolsim.common.node.DrawableParent;

import javax.annotation.Nonnull;

/**
 *
 */
public class GovernedExamNode implements DrawableParent
{
	protected ContentDistributionGovernor.ExamType examType;
	@Nonnull
	protected String displayName = "Exam";
	@Nonnull
	protected String status      = "UNK";
	@Nonnull
	protected Parent governedExamUINode;
	@Nonnull
	protected Exam   exam;

	/**
	 * @param exam
	 */
	public GovernedExamNode(@Nonnull Exam exam, @Nonnull ContentDistributionGovernor.ExamType examType)
	{
		this.examType = examType;
		setExam(exam);
	}

	/**
	 * @param exam
	 */
	public void setExam(@Nonnull Exam exam)
	{
		this.exam = exam;
	}

	/**
	 * @return
	 */
	@Nonnull
	public Exam getExam()

	{
		return exam;
	}

	/**
	 * removes this governed exam node and it associated exam from the program.
	 */
	public void removeExam()
	{
		for (int index = 0; index < ExamContentDistributionGovernor.governedExams.size(); index++)
			if (ExamContentDistributionGovernor.governedExams.get(index).equals(this))
				ExamContentDistributionGovernor.governedExams.remove(index);
	}

	/**
	 * draws the UI node
	 *
	 * @param apply
	 */
	public void redrawParent(boolean apply)
	{
		this.governedExamUINode = new VBox();
	}

	/**
	 * creates the associated interface for the exam
	 *
	 * @return
	 */
	@Nonnull
	public Parent getParent()
	{
		return this.governedExamUINode;
	}

	public ContentDistributionGovernor.ExamType getExamType()
	{
		return this.examType;
	}
}
