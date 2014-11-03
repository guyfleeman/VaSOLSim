package com.vasolsim.common.file;

import com.vasolsim.common.GenericUtils;
import com.vasolsim.common.VaSolSimException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.Cipher;
import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_COULD_NOT_CREATE_DIRS;
import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_COULD_NOT_CREATE_FILE;
import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_CREATE_FILE_EXCEPTION;
import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_FILE_ALREADY_EXISTS;
import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK;
import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_GENERIC_CRYPTO;
import static com.vasolsim.common.GenericUtils.ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION;
import static com.vasolsim.common.GenericUtils
		.ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION;
import static com.vasolsim.common.GenericUtils.INDENTATION_KEY;
import static com.vasolsim.common.GenericUtils.XML_ANSWER_CHOICE_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_ANSWER_CHOICE_ID_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_ANSWER_CHOICE_VISIBLE_ID_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_ANSWER_TEXT_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_AUTHOR_NAME_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_DATE_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_ENCRYPTED_VALIDATION_HASH_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_INFO_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_IS_REPORTING_STATISTICS_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_IS_REPORTING_STATISTICS_STANDALONE_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_PARAMETRIC_INITIALIZATION_VECTOR_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_PERIOD_NAME_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_ENCRYPTED_ANSWER_HASH;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_ID_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_NAME_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_REATIAN_ANSWER_ORDER_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_SCRAMBLE_ANSWERS_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_SET_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_SET_ID_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_SET_NAME_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_SET_RESOURCE_DATA_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_SET_RESOURCE_TYPE_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_QUESTION_TEXT_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_ROOT_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_SCHOOL_NAME_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_SECURITY_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_STATISTICS_DESTINATION_EMAIL_ADDRESS_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_STATISTICS_SENDER_EMAIL_ADDRESS_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_STATISTICS_SENDER_EMAIL_PASSWORD_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_STATISTICS_SENDER_SMTP_ADDRESS_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_STATISTICS_SENDER_SMTP_PORT_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.XML_TEST_NAME_ELEMENT_NAME;
import static com.vasolsim.common.GenericUtils.convertBytesToHexString;

/**
 * @author willstuckey
 * @date 10/31/14 <p></p>
 */
public class ExamBuilder
{
	/**
	 * Writes an exam to an XML file
	 *
	 * @param exam     the exam to be written
	 * @param examFile the target file
	 * @param password the passphrase locking the restricted content
	 *
	 * @return if the write was successful
	 *
	 * @throws VaSolSimException
	 */
	public static boolean writeExam(Exam exam, File examFile, String password) throws VaSolSimException
	{
		return writeExam(exam, examFile, password, false);
	}

	/**
	 * Writes an exam to an XML file
	 *
	 * @param exam      the exam to be written
	 * @param examFile  the target file
	 * @param password  the passphrase locking the restricted content
	 * @param overwrite if an existing file can be overwritten
	 *
	 * @return if the write was successful
	 *
	 * @throws VaSolSimException
	 */
	public static boolean writeExam(Exam exam, File examFile, String password,
	                                boolean overwrite) throws VaSolSimException
	{
		/*
		 * check the file creation status and handle it
		 */
		//if it exists
		if (examFile.isFile())
		{
			//can't overwrite
			if (!overwrite)
			{
				throw new VaSolSimException(ERROR_MESSAGE_FILE_ALREADY_EXISTS);
			}
			//can overwrite, clear the existing file
			else
			{
				PrintWriter printWriter;
				try
				{
					printWriter = new PrintWriter(examFile);
				}
				catch (FileNotFoundException e)
				{
					throw new VaSolSimException(ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK);
				}

				printWriter.print("");
				printWriter.close();
			}
		}
		//no file, create one
		else
		{
			if (!examFile.getParentFile().isDirectory() && !examFile.getParentFile().mkdirs())
			{
				throw new VaSolSimException(ERROR_MESSAGE_COULD_NOT_CREATE_DIRS);
			}

			try
			{
				if (!examFile.createNewFile())
				{
					throw new VaSolSimException(ERROR_MESSAGE_COULD_NOT_CREATE_FILE);
				}
			}
			catch (IOException e)
			{
				throw new VaSolSimException(ERROR_MESSAGE_CREATE_FILE_EXCEPTION);
			}
		}

		/*
		 * initialize the cryptography system
		 */
		String encryptedHash;
		Cipher encryptionCipher;
		try
		{
			//hash the password
			byte[] hash;
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			msgDigest.update(password.getBytes());
			hash = GenericUtils.validate512HashTo128Hash(msgDigest.digest());

			encryptionCipher = GenericUtils.initCrypto(hash, Cipher.ENCRYPT_MODE);

			encryptedHash = GenericUtils.convertBytesToHexString(GenericUtils.applyCryptographicCipher(
					hash, encryptionCipher));
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM\n" +
					                            e.toString() + "\n" +
					                            e.getCause() + "\n" +
					                            ExceptionUtils.getStackTrace(e),
			                            e);
		}

		/*
		 * initialize the document
		 */
		Document examDoc;
		Transformer examTransformer;
		try
		{
			examDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

			examTransformer = TransformerFactory.newInstance().newTransformer();
			examTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			examTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			examTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			examTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
			examTransformer.setOutputProperty(INDENTATION_KEY, "4");
		}
		catch (ParserConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION, e);
		}
		catch (TransformerConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION, e);
		}

		/*
		 * build exam info
		 */
		Element root = examDoc.createElement(XML_ROOT_ELEMENT_NAME);
		examDoc.appendChild(root);

		Element info = examDoc.createElement(XML_INFO_ELEMENT_NAME);
		root.appendChild(info);

		//exam info
		GenericUtils.appendSubNode(XML_TEST_NAME_ELEMENT_NAME, exam.getTestName(), info, examDoc);
		GenericUtils.appendSubNode(XML_AUTHOR_NAME_ELEMENT_NAME, exam.getAuthorName(), info, examDoc);
		GenericUtils.appendSubNode(XML_SCHOOL_NAME_ELEMENT_NAME, exam.getSchoolName(), info, examDoc);
		GenericUtils.appendSubNode(XML_PERIOD_NAME_ELEMENT_NAME, exam.getPeriodName(), info, examDoc);
		GenericUtils.appendSubNode(XML_DATE_ELEMENT_NAME, exam.getDate(), info, examDoc);


		//start security xml section
		Element security = examDoc.createElement(XML_SECURITY_ELEMENT_NAME);
		root.appendChild(security);

		GenericUtils.appendSubNode(XML_ENCRYPTED_VALIDATION_HASH_ELEMENT_NAME,
		                           encryptedHash,
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_PARAMETRIC_INITIALIZATION_VECTOR_ELEMENT_NAME,
		                           GenericUtils.convertBytesToHexString(encryptionCipher.getIV()),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_IS_REPORTING_STATISTICS_ELEMENT_NAME,
		                           Boolean.toString(exam.isReportingStats()),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_IS_REPORTING_STATISTICS_STANDALONE_ELEMENT_NAME,
		                           Boolean.toString(exam.isReportingStatsStandalone()),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_DESTINATION_EMAIL_ADDRESS_ELEMENT_NAME,
		                           GenericUtils.convertBytesToHexString(GenericUtils.applyCryptographicCipher(
				                           exam.getStatsDestinationEmail().getBytes(), encryptionCipher)),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_EMAIL_ADDRESS_ELEMENT_NAME,
		                           GenericUtils.convertBytesToHexString(GenericUtils.applyCryptographicCipher(
				                           exam.getStatsSenderEmail().getBytes(), encryptionCipher)),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_EMAIL_PASSWORD_ELEMENT_NAME,
		                           GenericUtils.convertBytesToHexString(GenericUtils.applyCryptographicCipher(
				                           exam.getStatsSenderPassword().getBytes(), encryptionCipher)),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_SMTP_ADDRESS_ELEMENT_NAME,
		                           GenericUtils.convertBytesToHexString(GenericUtils.applyCryptographicCipher(
				                           exam.getStatsSenderSMTPAddress().getBytes(), encryptionCipher)),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_SMTP_PORT_ELEMENT_NAME,
		                           GenericUtils.convertBytesToHexString(GenericUtils.applyCryptographicCipher(
				                           Integer.toString(exam.getStatsSenderSMTPPort()).getBytes(),
				                           encryptionCipher)),
		                           security,
		                           examDoc);

		ArrayList<QuestionSet> questionSets = exam.getQuestionSets();
		if (GenericUtils.verifyQuestionSetsIntegrity(questionSets))
		{
			for (int setsIndex = 0; setsIndex < questionSets.size(); setsIndex++)
			{
				QuestionSet qSet = questionSets.get(setsIndex);

				Element qSetElement = examDoc.createElement(XML_QUESTION_SET_ELEMENT_NAME);
				root.appendChild(qSetElement);

				GenericUtils.appendSubNode(XML_QUESTION_SET_ID_ELEMENT_NAME,
				                           Integer.toString(setsIndex + 1),
				                           qSetElement,
				                           examDoc);
				GenericUtils.appendSubNode(XML_QUESTION_SET_NAME_ELEMENT_NAME,
				                           (qSet.getName() == null || qSet.getName().equals(""))
				                           ? "Question Set " + (setsIndex + 1)
				                           : qSet.getName(),
				                           qSetElement,
				                           examDoc);
				GenericUtils.appendSubNode(XML_QUESTION_SET_RESOURCE_TYPE_ELEMENT_NAME,
				                           qSet.getResourceType().toString(),
				                           qSetElement,
				                           examDoc);

				if (qSet.getResources() != null)
				{
					for (BufferedImage img : qSet.getResources())
					{
						try
						{
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							ImageIO.write(img, "png", out);
							out.flush();
							GenericUtils.appendSubNode(XML_QUESTION_SET_RESOURCE_DATA_ELEMENT_NAME,
							                           convertBytesToHexString(out.toByteArray()),
							                           qSetElement,
							                           examDoc);
						}
						catch (IOException e)
						{
							throw new VaSolSimException("Error: cannot write images to byte array for transport");
						}
					}
				}

				for (int setIndex = 0; setIndex < qSet.getQuestions().size(); setIndex++)
				{
					Question question = qSet.getQuestions().get(setIndex);

					Element qElement = examDoc.createElement(XML_QUESTION_ELEMENT_NAME);
					qSetElement.appendChild(qElement);

					GenericUtils.appendSubNode(XML_QUESTION_ID_ELEMENT_NAME,
					                           Integer.toString(setIndex + 1),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_NAME_ELEMENT_NAME,
					                           (question.getName() == null || question.getName().equals(""))
					                           ? "Question " + (setIndex + 1)
					                           : question.getName(),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_TEXT_ELEMENT_NAME,
					                           question.getQuestion(),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_SCRAMBLE_ANSWERS_ELEMENT_NAME,
					                           Boolean.toString(question.getScrambleAnswers()),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_REATIAN_ANSWER_ORDER_ELEMENT_NAME,
					                           Boolean.toString(question.getAnswerOrderMatters()),
					                           qElement,
					                           examDoc);

					for (AnswerChoice answer : question.getCorrectAnswerChoices())
					{
						GenericUtils.appendSubNode(XML_QUESTION_ENCRYPTED_ANSWER_HASH,
						                           GenericUtils.convertBytesToHexString(
								                           GenericUtils.applyCryptographicCipher(
										                           answer.getAnswerText().getBytes(),
										                           encryptionCipher)),
						                           qElement,
						                           examDoc);
					}

					for (int questionIndex = 0; questionIndex < question.getAnswerChoices().size(); questionIndex++)
					{
						AnswerChoice ac = question.getAnswerChoices().get(questionIndex);

						Element acElement = examDoc.createElement(XML_ANSWER_CHOICE_ELEMENT_NAME);
						qElement.appendChild(acElement);

						GenericUtils.appendSubNode(XML_ANSWER_CHOICE_ID_ELEMENT_NAME,
						                           Integer.toString(questionIndex + 1),
						                           acElement,
						                           examDoc);
						GenericUtils.appendSubNode(XML_ANSWER_CHOICE_VISIBLE_ID_ELEMENT_NAME,
						                           ac.getVisibleChoiceID(),
						                           acElement,
						                           examDoc);
						GenericUtils.appendSubNode(XML_ANSWER_TEXT_ELEMENT_NAME,
						                           ac.getAnswerText(),
						                           acElement,
						                           examDoc);
					}
				}
			}
		}

		return true;
	}

	/**
	 * reads an exam from a file
	 *
	 * @param examFile the file to read
	 * @param password the passphrase that will unlock the file
	 *
	 * @return the initialized exam
	 *
	 * @throws VaSolSimException
	 */
	public static Exam readExam(File examFile, String password) throws VaSolSimException
	{


		return null;
	}

	/**
	 * writes an editable, unlocked exam to a file
	 *
	 * @param exam     the exam to be written
	 * @param examFile the target file
	 *
	 * @return if the write was successful
	 *
	 * @throws VaSolSimException
	 */
	public static boolean writeRaw(Exam exam, File examFile) throws VaSolSimException
	{
		return writeRaw(exam, examFile, true);
	}

	/**
	 * writes an editable, unlocked exam to a file
	 *
	 * @param exam      the exam to be written
	 * @param examFile  the target file
	 * @param overwrite if an existing file can be overwritten
	 *
	 * @return if the file write was successful
	 *
	 * @throws VaSolSimException
	 */
	public static boolean writeRaw(Exam exam, File examFile, boolean overwrite) throws VaSolSimException
	{
		/*
		 * check the file creation status and handle it
		 */
		//if it exists
		if (examFile.isFile())
		{
			//can't overwrite
			if (!overwrite)
			{
				throw new VaSolSimException(ERROR_MESSAGE_FILE_ALREADY_EXISTS);
			}
			//can overwrite, clear the existing file
			else
			{
				PrintWriter printWriter;
				try
				{
					printWriter = new PrintWriter(examFile);
				}
				catch (FileNotFoundException e)
				{
					throw new VaSolSimException(ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK);
				}

				printWriter.print("");
				printWriter.close();
			}
		}
		//no file, create one
		else
		{
			if (!examFile.getParentFile().isDirectory() && !examFile.getParentFile().mkdirs())
			{
				throw new VaSolSimException(ERROR_MESSAGE_COULD_NOT_CREATE_DIRS);
			}

			try
			{
				if (!examFile.createNewFile())
				{
					throw new VaSolSimException(ERROR_MESSAGE_COULD_NOT_CREATE_FILE);
				}
			}
			catch (IOException e)
			{
				throw new VaSolSimException(ERROR_MESSAGE_CREATE_FILE_EXCEPTION);
			}
		}

		/*
		 * initialize the document
		 */
		Document examDoc;
		Transformer examTransformer;
		try
		{
			examDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

			examTransformer = TransformerFactory.newInstance().newTransformer();
			examTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			examTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			examTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			examTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
			examTransformer.setOutputProperty(INDENTATION_KEY, "4");
		}
		catch (ParserConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION, e);
		}
		catch (TransformerConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION, e);
		}

		/*
		 * build exam info
		 */
		Element root = examDoc.createElement(XML_ROOT_ELEMENT_NAME);
		examDoc.appendChild(root);

		Element info = examDoc.createElement(XML_INFO_ELEMENT_NAME);
		root.appendChild(info);

		//exam info
		GenericUtils.appendSubNode(XML_TEST_NAME_ELEMENT_NAME, exam.getTestName(), info, examDoc);
		GenericUtils.appendSubNode(XML_AUTHOR_NAME_ELEMENT_NAME, exam.getAuthorName(), info, examDoc);
		GenericUtils.appendSubNode(XML_SCHOOL_NAME_ELEMENT_NAME, exam.getSchoolName(), info, examDoc);
		GenericUtils.appendSubNode(XML_PERIOD_NAME_ELEMENT_NAME, exam.getPeriodName(), info, examDoc);

		//start security xml section
		Element security = examDoc.createElement(XML_SECURITY_ELEMENT_NAME);
		root.appendChild(security);

		GenericUtils.appendSubNode(XML_IS_REPORTING_STATISTICS_ELEMENT_NAME,
		                           Boolean.toString(exam.isReportingStats()),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_IS_REPORTING_STATISTICS_STANDALONE_ELEMENT_NAME,
		                           Boolean.toString(exam.isReportingStatsStandalone()),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_DESTINATION_EMAIL_ADDRESS_ELEMENT_NAME,
		                           exam.getStatsDestinationEmail(),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_EMAIL_ADDRESS_ELEMENT_NAME,
		                           exam.getStatsSenderEmail(),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_SMTP_ADDRESS_ELEMENT_NAME,
		                           exam.getStatsSenderSMTPAddress(),
		                           security,
		                           examDoc);
		GenericUtils.appendSubNode(XML_STATISTICS_SENDER_SMTP_PORT_ELEMENT_NAME,
		                           Integer.toString(exam.getStatsSenderSMTPPort()),
		                           security,
		                           examDoc);

		ArrayList<QuestionSet> questionSets = exam.getQuestionSets();
		if (GenericUtils.verifyQuestionSetsIntegrity(questionSets))
		{
			for (int setsIndex = 0; setsIndex < questionSets.size(); setsIndex++)
			{
				QuestionSet qSet = questionSets.get(setsIndex);

				Element qSetElement = examDoc.createElement(XML_QUESTION_SET_ELEMENT_NAME);
				root.appendChild(qSetElement);

				GenericUtils.appendSubNode(XML_QUESTION_SET_ID_ELEMENT_NAME,
				                           Integer.toString(setsIndex + 1),
				                           qSetElement,
				                           examDoc);
				GenericUtils.appendSubNode(XML_QUESTION_SET_NAME_ELEMENT_NAME,
				                           (qSet.getName() == null || qSet.getName().equals(""))
				                           ? "Question Set " + (setsIndex + 1)
				                           : qSet.getName(),
				                           qSetElement,
				                           examDoc);
				GenericUtils.appendSubNode(XML_QUESTION_SET_RESOURCE_TYPE_ELEMENT_NAME,
				                           qSet.getResourceType().toString(),
				                           qSetElement,
				                           examDoc);

				if (qSet.getResources() != null)
				{
					for (BufferedImage img : qSet.getResources())
					{
						try
						{
							ByteArrayOutputStream out = new ByteArrayOutputStream();
							ImageIO.write(img, "png", out);
							out.flush();
							GenericUtils.appendSubNode(XML_QUESTION_SET_RESOURCE_DATA_ELEMENT_NAME,
							                           convertBytesToHexString(out.toByteArray()),
							                           qSetElement,
							                           examDoc);
						}
						catch (IOException e)
						{
							throw new VaSolSimException("Error: cannot write images to byte array for transport");
						}
					}
				}

				for (int setIndex = 0; setIndex < qSet.getQuestions().size(); setIndex++)
				{
					Question question = qSet.getQuestions().get(setIndex);

					Element qElement = examDoc.createElement(XML_QUESTION_ELEMENT_NAME);
					qSetElement.appendChild(qElement);

					GenericUtils.appendSubNode(XML_QUESTION_ID_ELEMENT_NAME,
					                           Integer.toString(setIndex + 1),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_NAME_ELEMENT_NAME,
					                           (question.getName() == null || question.getName().equals(""))
					                           ? "Question " + (setIndex + 1)
					                           : question.getName(),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_TEXT_ELEMENT_NAME,
					                           question.getQuestion(),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_SCRAMBLE_ANSWERS_ELEMENT_NAME,
					                           Boolean.toString(question.getScrambleAnswers()),
					                           qElement,
					                           examDoc);
					GenericUtils.appendSubNode(XML_QUESTION_REATIAN_ANSWER_ORDER_ELEMENT_NAME,
					                           Boolean.toString(question.getAnswerOrderMatters()),
					                           qElement,
					                           examDoc);

					for (AnswerChoice answer : question.getCorrectAnswerChoices())
					{
						GenericUtils.appendSubNode(XML_QUESTION_ENCRYPTED_ANSWER_HASH,
						                           answer.getAnswerText(),
						                           qElement,
						                           examDoc);
					}

					for (int questionIndex = 0; questionIndex < question.getAnswerChoices().size(); questionIndex++)
					{
						AnswerChoice ac = question.getAnswerChoices().get(questionIndex);

						Element acElement = examDoc.createElement(XML_ANSWER_CHOICE_ELEMENT_NAME);
						qElement.appendChild(acElement);

						GenericUtils.appendSubNode(XML_ANSWER_CHOICE_ID_ELEMENT_NAME,
						                           Integer.toString(questionIndex + 1),
						                           acElement,
						                           examDoc);
						GenericUtils.appendSubNode(XML_ANSWER_CHOICE_VISIBLE_ID_ELEMENT_NAME,
						                           ac.getVisibleChoiceID(),
						                           acElement,
						                           examDoc);
						GenericUtils.appendSubNode(XML_ANSWER_TEXT_ELEMENT_NAME,
						                           ac.getAnswerText(),
						                           acElement,
						                           examDoc);
					}
				}
			}
		}

		return true;
	}

	/**
	 * reads an editable exam from a file
	 *
	 * @param examFile the file to read
	 *
	 * @return the initialized exam
	 */
	public static Exam readRaw(File examFile)
	{
		return null;
	}
}
