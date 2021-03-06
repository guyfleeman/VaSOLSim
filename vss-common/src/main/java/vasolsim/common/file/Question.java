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

package main.java.vasolsim.common.file;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import main.java.vasolsim.common.GenericUtils;

import java.util.ArrayList;

import static main.java.vasolsim.common.GenericUtils.*;

/**
 * @author willstuckey
 * @date 7/1/14
 * <p>This class represents a question and its enclosed answer choices and data.</p>
 */
public class Question
{
	@Nonnull
	public static ArrayList<Character> charset = new ArrayList<Character>();

	private final boolean                 isLocked;
	private       boolean                 scrambleAnswers;
	private       boolean                 answerOrderMatters;
	@Nonnull
	private       String                  name;
	@Nullable
	private       String                  question;
	@Nonnull
	private       QuestionType            questionType;
	@Nonnull
	private       ArrayList<AnswerChoice> correctAnswerChoices;
	@Nonnull
	private       ArrayList<AnswerChoice> answerChoices;

	/**
	 * Default constructor. Initializes an unlocked question. Questions must be locked before they will be accepted by
	 * the viewer.
	 */
	public Question()
	{
		this("New Question",
		     null,
		     QuestionType.MULTIPLE_CHOICE,
		     new ArrayList<AnswerChoice>(),
		     new ArrayList<AnswerChoice>(),
		     false,
		     false);
	}

	/**
	 * Advanced constructor. Initializes an unlocked question. Questions must be locked before they will be accepted by
	 * the viewer.
	 *
	 * @param name                 the name of the question
	 * @param question             the text of the question
	 * @param questionType         the type of question (how the question will be rendered)
	 * @param answerChoices        the answer choices
	 * @param correctAnswerChoices the correct answer choices
	 * @param scrambleAnswers      if the order of the answers will be scrambled when the test viewer renders the
	 *                             question
	 * @param answerOrderMatters   if the order of the answers is needed for a correct answer. Only applies to certain
	 *                             question types.
	 */
	public Question(@Nonnull String name,
	                @Nullable String question,
	                @Nonnull QuestionType questionType,
	                @Nonnull ArrayList<AnswerChoice> answerChoices,
	                @Nonnull ArrayList<AnswerChoice> correctAnswerChoices,
	                boolean scrambleAnswers,
	                boolean answerOrderMatters)
	{
		this.name = name;
		this.question = question;
		this.questionType = questionType;
		this.answerChoices = answerChoices;
		this.correctAnswerChoices = correctAnswerChoices;
		this.scrambleAnswers = scrambleAnswers;
		this.answerOrderMatters = answerOrderMatters;
		this.isLocked = false;
	}

	/**
	 * Advanced constructor. Initializes a question. Questions must be locked before they will be accepted by the
	 * viewer.
	 *
	 * @param name                 the name of the question
	 * @param question             the text of the question
	 * @param questionType         the type of question (how the question will be rendered)
	 * @param answerChoices        the answer choices
	 * @param correctAnswerChoices the correct answer choices
	 * @param scrambleAnswers      if the order of the answers will be scrambled when the test viewer renders the
	 *                             question
	 * @param answerOrderMatters   if the order of the answers is needed for a correct answer. Only applies to certain
	 *                             question types.
	 * @param isLocked             denotes if the question is locked
	 */
	Question(@Nonnull String name,
	         @Nullable String question,
	         @Nonnull QuestionType questionType,
	         @Nonnull final ArrayList<AnswerChoice> answerChoices,
	         @Nonnull final ArrayList<AnswerChoice> correctAnswerChoices,
	         boolean scrambleAnswers,
	         boolean answerOrderMatters,
	         boolean isLocked)
	{
		this.name = name;
		this.question = question;
		this.questionType = questionType;
		this.answerChoices = (ArrayList<AnswerChoice>) answerChoices.clone();
		this.correctAnswerChoices = (ArrayList<AnswerChoice>) correctAnswerChoices.clone();
		this.scrambleAnswers = scrambleAnswers;
		this.answerOrderMatters = answerOrderMatters;
		this.isLocked = isLocked;
	}

	/**
	 * Initialize answer blanks based on type, used to prevent null pointers in questions that have not been
	 * initialized
	 * by the teacher client. Will only work if the question is unlocked.
	 */
	public void initializeAnswers()
	{
		if (!isLocked)
			initializeAnswers(questionType);
	}

	/**
	 * Initialize answer blanks based on type, used to prevent null pointers in questions that have not been
	 * initialized
	 * by the teacher client. Will only work if the question is unlocked.
	 *
	 * @param questionType type
	 */
	public void initializeAnswers(GenericUtils.QuestionType questionType)
	{
		if (!isLocked)
		{
			if (questionType == GenericUtils.QuestionType.TE_D_AND_D_GRAMMAR_MULTIPLE_RESPONSE)
			{
				ArrayList<AnswerChoice> answers = new ArrayList<AnswerChoice>();
				for (Character c : Question.charset)
				{
					AnswerChoice ac = new AnswerChoice();
					ac.setActive(false);
					ac.setCorrect(false);
					ac.setText(Character.toString(c));
					ac.setVisibleChoiceID(Character.toString(c));
					answers.add(ac);
				}

				AnswerChoice correctAnswerHolder = new AnswerChoice();
				correctAnswerHolder.setActive(false);
				correctAnswerHolder.setCorrect(true);
				correctAnswerHolder.setText("none");
				correctAnswerHolder.setVisibleChoiceID(GenericUtils.NO_DATA);
				answers.add(correctAnswerHolder);
				answerChoices = answers;
			}
			else
			{
				ArrayList<AnswerChoice> answers = new ArrayList<AnswerChoice>();
				for (int i = 0; i < 8; i++)
					answers.add(new AnswerChoice());
				answerChoices = answers;
			}
		}
	}

	/**
	 * Basic answer checker. Designed to give immediate correct/incorrect feedback. Analysis of choice and
	 * statistics to
	 * be computed by a more comprehensive algorithm elsewhere.
	 *
	 * @param uncheckedAnswers the students selected responses.
	 *
	 * @return if the provided answer is a correct one
	 */
	public boolean checkAnswer(ArrayList<AnswerChoice> uncheckedAnswers)
	{
		@SuppressWarnings("unchecked")
		final ArrayList<AnswerChoice> guesses = (ArrayList<AnswerChoice>) uncheckedAnswers.clone();
		@SuppressWarnings("unchecked")
		final ArrayList<AnswerChoice> correctAnswers = (ArrayList<AnswerChoice>) correctAnswerChoices.clone();

		if (guesses.size() != correctAnswers.size())
			return false;

		/*
		 * if the order matters, check directly
		 */
		if (answerOrderMatters)
		{
			for (int index = 0; index < correctAnswers.size(); index++)
				if (!(guesses.get(index).getAnswerText().equals(correctAnswers.get(index).getAnswerText())))
					return false;

			return true;
		}
		/*
		 * if the order does not matter iterate though everything looking for matched removing duplicates
		 */
		else
		{
			for (int index = 0; index < guesses.size(); index++)
			{
				boolean foundAnswer = false;
				for (int correctIndex = 0; correctIndex < correctAnswers.size(); correctIndex++)
				{
					if (guesses.get(index).getAnswerText().equals(
							correctAnswers.get(correctIndex).getAnswerText()))
					{
						foundAnswer = true;
						guesses.remove(index);
						correctAnswers.remove(correctIndex);
						break;
					}
				}

				if (!foundAnswer)
					return false;
			}

			return true;
		}
	}

	/**
	 * gets answer choices
	 * @return answer choices
	 */
	@Nonnull
	public ArrayList<AnswerChoice> getAnswerChoices()
	{
		return answerChoices;
	}

	/**
	 * set answer choices, only if the question is unlocked
	 * @param answerChoices answer choices
	 */
	public final void setAnswerChoices(@Nonnull ArrayList<AnswerChoice> answerChoices)
	{
		if (!isLocked)
			this.answerChoices = answerChoices;
	}

	/**
	 * gets if answers are being scrambled
	 * @return if scramble answers
	 */
	public boolean getScrambleAnswers()
	{
		return scrambleAnswers;
	}

	/**
	 * gets if the question is locked
	 * @return if is locked
	 */
	public final boolean isLocked()
	{
		return isLocked;
	}

	/**
	 * gets if answer order matters
	 * @return if order matters
	 */
	public boolean getAnswerOrderMatters()
	{
		return answerOrderMatters;
	}

	/**
	 * gets name
	 * @return name
	 */
	@Nonnull
	public String getName()
	{
		return name;
	}

	/**
	 * sets the name, only if the question is unlocked
	 * @param name name
	 */
	public final void setName(@Nonnull String name)
	{
		if (!isLocked)
			this.name = name;
	}

	/**
	 * gets the correct answer choices
	 * @return correct answers
	 */
	@Nonnull
	public final ArrayList<AnswerChoice> getCorrectAnswerChoices()
	{
		if (!isLocked)
		{
			return correctAnswerChoices;
		}

		return null;
	}

	/**
	 * sets the correct answer choices, only if the question is unlocked
	 * @param correctAnswerChoices correct answers
	 */
	public final void setCorrectAnswerChoices(@Nonnull ArrayList<AnswerChoice> correctAnswerChoices)
	{
		if (!isLocked)
			this.correctAnswerChoices = correctAnswerChoices;
	}

	/**
	 * gets the question text
	 * @return question text
	 */
	@Nullable
	public String getQuestion()
	{
		return question;
	}

	/**
	 * sets the question text, only if the question is unlocked
	 * @param question the question
	 */
	public final void setQuestion(@Nullable String question)
	{
		if (!isLocked)
			this.question = question;
	}

	/**
	 * gets the question type
	 * @return type
	 */
	@Nonnull
	public QuestionType getQuestionType()
	{
		return questionType;
	}

	/**
	 * sets the question type, only if the question is unlocked
	 * @param questionType type
	 */
	public final void setQuestionType(@Nonnull QuestionType questionType)
	{
		if (!isLocked)
			this.questionType = questionType;
	}
}
