package com.gmail.guyfleeman.vasolsim.common.struct;

import javax.crypto.Cipher;
import java.security.MessageDigest;
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
    private ArrayList<String> correctAnswerEncryptedHashes;
    private ArrayList<AnswerChoice> answerChoices;

    public Question()
    {
	    this("New Question",
	         "No Question Defined",
	         QuestionType.MULTIPLE_CHOICE,
	         new ArrayList<AnswerChoice>(),
	         new ArrayList<String>(),
	         false,
	         true,
	         false);
    }

	public Question(final String name,
                    final String question,
                    final QuestionType questionType,
                    final ArrayList<AnswerChoice> answerChoices,
                    final ArrayList<String> correctAnswerEncryptedHashes,
                    final boolean scrambleAnswers,
                    final boolean answerOrderMatters,
                    final boolean initializedFromFile)
    {
        this.name = name;
        this.question = question;
        this.questionType = questionType;
        this.answerChoices = answerChoices;
        this.correctAnswerEncryptedHashes = correctAnswerEncryptedHashes;
        this.scrambleAnswers = scrambleAnswers;
        this.answerOrderMatters = answerOrderMatters;
        this.initializedFromFile = initializedFromFile;
    }

    public boolean checkAnswer(final ArrayList<AnswerChoice> guess, Cipher encryptionCipher)
    {
        if (guess.size() != correctAnswerEncryptedHashes.size())
            return false;

        ArrayList<String> guessHashes = new ArrayList<String>();
        for (int index = 0; index < correctAnswerEncryptedHashes.size(); index++)
        {
            try
            {
                MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
                msgDigest.update(guess.get(index).getAnswerText().getBytes());
                guessHashes.set(index,
                        convertBytesToHexString(applyCryptographicCipher(msgDigest.digest(), encryptionCipher)));
            } catch (Exception e)
            {
                return false;
            }
        }

        if (answerOrderMatters)
        {
            for (int index = 0; index < guessHashes.size(); index++)
                if (!(guessHashes.get(index).equals(correctAnswerEncryptedHashes.get(index))))
                    return false;

            return true;
        } else
        {
            for (int index = 0; index < guessHashes.size(); index++)
            {
                boolean foundAnswer = false;
                for (int correctIndex = 0; correctIndex < correctAnswerEncryptedHashes.size(); correctIndex++)
                {
                    if (guessHashes.get(index).equals(correctAnswerEncryptedHashes.get(correctIndex)))
                    {
                        foundAnswer = true;
                        guessHashes.remove(index);
                        correctAnswerEncryptedHashes.remove(correctIndex);
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

    public ArrayList<String> getCorrectAnswerEncryptedHashes()
    {
        return correctAnswerEncryptedHashes;
    }

    public void setCorrectAnswerEncryptedHashes(ArrayList<String> encryptedHashes)
    {
        if (!initializedFromFile)
            this.correctAnswerEncryptedHashes = encryptedHashes;
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
