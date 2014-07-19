package com.gmail.guyfleeman.vasolsim;

import com.gmail.guyfleeman.vasolsim.common.VaSolSimException;
import com.gmail.guyfleeman.vasolsim.common.file.Exam;
import com.gmail.guyfleeman.vasolsim.common.file.ExamBuilder;
import com.gmail.guyfleeman.vasolsim.common.struct.AnswerChoice;
import com.gmail.guyfleeman.vasolsim.common.struct.Question;
import com.gmail.guyfleeman.vasolsim.common.struct.QuestionSet;

import java.io.File;
import java.util.ArrayList;

import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 6/25/14
 * <p></p>
 */
public class VaSOLSimTestRunner
{
	public static void main(String[] args) throws Exception
	{
		docGen();
	}

	public static void smtpTest()
	{
		System.out.println(isValidEmail("guyfleeman@gmail.com"));
		System.out.println(isValidAddress("smtp.gmail.com"));
		//System.out.println(canConnectToAddress("smtp.gmail.com"));
		System.out.println(isValidSMTPConfiguration("smtp.gmail.com",
				587,
				"guyfleeman@gmail.com",
				"mechdudeman".getBytes(),
				true));
	}

	public static void docGen() throws VaSolSimException
	{
		ExamBuilder eb = ExamBuilder.getInstance("password".getBytes());
		eb.setReportingStats(true);
		eb.setStatsDestinationEmail("guyfleeman@gmail.com");
		eb.setReportingStatsStandalone(true);
		eb.setStatsSenderEmail("guyfleeman@gmail.com");
		eb.setStatsSenderEmailPassword("<mypassword>".getBytes());
		eb.setStatsSenderSMTPAddress("smtp.gmail.com");
		Exam myExam = eb.getExamFramework();

		ArrayList<QuestionSet> questionSets = new ArrayList<QuestionSet>();
		QuestionSet setOne = new QuestionSet("Question Set One", null, false);
		Question questionOne = new Question("Q1",
				"Was George Washington the first president of the United States?",
				QuestionType.MULTIPLE_CHOICE,
				null,
				null,
				true,
				false,
				false);
		AnswerChoice acOne = new AnswerChoice("A. ", "True", false);
		AnswerChoice acTwo = new AnswerChoice("B. ", "False", false);
		ArrayList<AnswerChoice> answers = new ArrayList<AnswerChoice>();
		answers.add(acOne);
		answers.add(acTwo);
		questionOne.setAnswerChoices(answers);
		ArrayList<String> encAnsHash = new ArrayList<String>();
		encAnsHash.add(convertBytesToHexString(applyCryptographicCipher("True".getBytes(), myExam.getEncryptionCipher())));
		questionOne.setCorrectAnswerEncryptedHashes(encAnsHash);

		Question questionTwo = new Question("Q1",
				"Which of the following are nouns?",
				QuestionType.MULTIPLE_CHOICE,
				null,
				null,
				true,
				false,
				false);
		AnswerChoice acThree = new AnswerChoice("A. ", "red", false);
		AnswerChoice acFour = new AnswerChoice("B. ", "apple", false);
		AnswerChoice acFive = new AnswerChoice("C. ", "family", false);
		AnswerChoice acSix = new AnswerChoice("D. ", "to run", false);
		ArrayList<AnswerChoice> answersTwo = new ArrayList<AnswerChoice>();
		answersTwo.add(acThree);
		answersTwo.add(acFour);
		answersTwo.add(acFive);
		answersTwo.add(acSix);
		questionTwo.setAnswerChoices(answersTwo);
		ArrayList<String> encAnsHashTwo = new ArrayList<String>();
		encAnsHashTwo.add(convertBytesToHexString(applyCryptographicCipher("apple".getBytes(),
				myExam.getEncryptionCipher())));
		encAnsHashTwo.add(convertBytesToHexString(applyCryptographicCipher("family".getBytes(),
				myExam.getEncryptionCipher())));
		questionTwo.setCorrectAnswerEncryptedHashes(encAnsHashTwo);

		ArrayList<Question> questions = new ArrayList<Question>();
		questions.add(questionOne);
		questions.add(questionTwo);

		setOne.setQuestions(questions);
		questionSets.add(setOne);

		myExam.setQuestionSets(questionSets);

		myExam.sendEmail("My First Email", "This is the body.", true);

		ExamBuilder.writeExamToFile(myExam, new File("/home/guyfleeman/Desktop/exam.xml"), true);
	}
}
