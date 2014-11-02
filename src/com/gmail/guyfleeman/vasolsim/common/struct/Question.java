package com.gmail.guyfleeman.vasolsim.common.struct;

import java.util.ArrayList;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/1/14
 * <p></p>
 */
public class Question
{
	private final boolean initializedFromFile;
	private boolean scrambleAnswers;
	private boolean answerOrderMatters;
	private String name;
	private String question;
	private QuestionType questionType;
	private ArrayList<AnswerChoice> correctAnswerChoices;
	private ArrayList<AnswerChoice> answerChoices;

	public Question()
	{
		this("New Question",
		     "No Question Defined",
		     QuestionType.MULTIPLE_CHOICE,
		     new ArrayList<AnswerChoice>(),
		     new ArrayList<AnswerChoice>(),
		     false,
		     true,
		     false);
	}

	public Question(final String name,
	                final String question,
	                final QuestionType questionType,
	                final ArrayList<AnswerChoice> answerChoices,
	                final ArrayList<AnswerChoice> correctAnswerChoices,
	                final boolean scrambleAnswers,
	                final boolean answerOrderMatters,
	                final boolean initializedFromFile)
	{
		this.name = name;
		this.question = question;
		this.questionType = questionType;
		this.answerChoices = answerChoices;
		this.correctAnswerChoices = correctAnswerChoices;
		this.scrambleAnswers = scrambleAnswers;
		this.answerOrderMatters = answerOrderMatters;
		this.initializedFromFile = initializedFromFile;
	}

	public boolean checkAnswer(ArrayList<AnswerChoice> uncheckedAnswers)
	{
		@SuppressWarnings("unchecked")
		final ArrayList<AnswerChoice> guesses = (ArrayList<AnswerChoice>) uncheckedAnswers.clone();
		@SuppressWarnings("unchecked")
		final ArrayList<AnswerChoice> correctAnswers = (ArrayList<AnswerChoice>) correctAnswerChoices.clone();

		if (guesses.size() != correctAnswers.size())
			return false;

		if (answerOrderMatters)
		{
			for (int index = 0; index < correctAnswers.size(); index++)
				if (!(guesses.get(index).getAnswerText().equals(correctAnswers.get(index).getAnswerText())))
					return false;

			return true;
		}
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

	public ArrayList<AnswerChoice> getAnswerChoices()
	{
		return answerChoices;
	}

	public final void setAnswerChoices(ArrayList<AnswerChoice> answerChoices)
	{
		if (!initializedFromFile)
			this.answerChoices = answerChoices;
	}

	public boolean getScrambleAnswers()
	{
		return scrambleAnswers;
	}

	public boolean isInitializedFromFile()
	{
		return initializedFromFile;
	}

	public boolean getAnswerOrderMatters()
	{
		return answerOrderMatters;
	}

	public String getName()
	{
		return name;
	}

	public final void setName(final String name)
	{
		if (!initializedFromFile)
			this.name = name;
	}

	public ArrayList<AnswerChoice> getCorrectAnswerChoices()
	{
		return correctAnswerChoices;
	}

	public void setCorrectAnswerChoices(ArrayList<AnswerChoice> correctAnswerChoices)
	{
		if (!initializedFromFile)
			this.correctAnswerChoices = correctAnswerChoices;
	}

	public String getQuestion()
	{
		return question;
	}

	public final void setQuestion(final String question)
	{
		if (!initializedFromFile)
			this.question = question;
	}

	public QuestionType getQuestionType()
	{
		return questionType;
	}

	public void setQuestionType(QuestionType questionType)
	{
		this.questionType = questionType;
	}
}
