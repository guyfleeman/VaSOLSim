package com.gmail.guyfleeman.vasolsim.common.file;

import com.gmail.guyfleeman.vasolsim.common.VaSolSimException;
import com.gmail.guyfleeman.vasolsim.common.struct.AnswerChoice;
import com.gmail.guyfleeman.vasolsim.common.struct.Question;
import com.gmail.guyfleeman.vasolsim.common.struct.QuestionSet;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;


import static com.gmail.guyfleeman.vasolsim.common.GenericUtils.*;

/**
 * @author williamstuckey
 * @date 7/11/14
 * <p></p>
 */
public class ExamBuilder
{
	/**
	 * Returns a new instance of an exam factory.
	 * @param validationKey the key that will be used to validate the exam
	 * @return an ExamFactory ready for validation
	 * @throws VaSolSimException
	 */
	public static ExamBuilder getInstance(String validationKey) throws VaSolSimException
	{
		return getInstance(validationKey.getBytes());
	}

	/**
	 * Returns a new instance of an exam factory.
	 * @param key the key that will be used to validate the exam
	 * @return an ExamFactory ready for validation
	 * @throws VaSolSimException
	 */
	public static ExamBuilder getInstance(byte[] key) throws VaSolSimException
	{
		/*
		 * Generate a 512 bit (64 byte) hash from the given key.
		 */
		byte[] hash;
		try
		{
			MessageDigest msgDigest = MessageDigest.getInstance("SHA-512");
			msgDigest.update(key);
			hash = msgDigest.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_HASH_EXCEPTION, e);
		}

		return new ExamBuilder(validate512HashTo128Hash(hash));
	}

	/**
	 * Opens an exam from an exam xml file.
	 * @param examFile
	 * @return
	 */
	public static Exam getExamFromFile(File examFile, byte[] key)
	{
		return null;
	}

	public static boolean writeExamToFile(Exam exam, File examFile, boolean canOverwriteFile) throws VaSolSimException
	{
		if (examFile.isFile())
		{
			if (!canOverwriteFile)
			{
				throw new VaSolSimException(ERROR_MESSAGE_FILE_ALREADY_EXISTS);
			}
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

		Document examDoc;
		Transformer examTransformer;
		try
		{
			examDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

			examTransformer = TransformerFactory.newInstance().newTransformer();
			examTransformer.setOutputProperty(OutputKeys.INDENT,         "yes");
			examTransformer.setOutputProperty(OutputKeys.METHOD,         "xml");
			examTransformer.setOutputProperty(OutputKeys.ENCODING,       "UTF-8");
			examTransformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
			examTransformer.setOutputProperty(INDENTATION_KEY,           "4");
		}
		catch (ParserConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION, e);
		}
		catch (TransformerConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION, e);
		}

		Element root = examDoc.createElement(XML_ROOT_ELEMENT_NAME);
		examDoc.appendChild(root);

		//start information xml section
		Element info = examDoc.createElement(XML_INFO_ELEMENT_NAME);
		root.appendChild(info);

		appendSubNode(XML_TEST_NAME_ELEMENT_NAME,   exam.getTestName(),   info, examDoc);
		appendSubNode(XML_AUTHOR_NAME_ELEMENT_NAME, exam.getAuthorName(), info, examDoc);
		appendSubNode(XML_SCHOOL_NAME_ELEMENT_NAME, exam.getSchoolName(), info, examDoc);
		appendSubNode(XML_PERIOD_NAME_ELEMENT_NAME, exam.getPeriodName(), info, examDoc);
		appendSubNode(XML_DATE_ELEMENT_NAME,        exam.getDate(),       info, examDoc);


		//start security xml section
		Element security = examDoc.createElement(XML_SECURITY_ELEMENT_NAME);
		root.appendChild(security);

		appendSubNode(XML_ENCRYPTED_VALIDATION_HASH_ELEMENT_NAME,
				convertBytesToHexString(exam.getEncryptedValidationHash()),
				security,
				examDoc);
		appendSubNode(XML_PARAMETRIC_INITIALIZATION_VECTOR_ELEMENT_NAME,
				new String(exam.getParametricIV()),
				security,
				examDoc);
		appendSubNode(XML_IS_REPORTING_STATISTICS_ELEMENT_NAME,
				Boolean.toString(exam.isReportingStats()),
				security,
				examDoc);
		appendSubNode(XML_IS_REPORTING_STATISTICS_STANDALONE_ELEMENT_NAME,
				Boolean.toString(exam.isReportingStatsStandalone()),
				security,
				examDoc);
		appendSubNode(XML_STATISTICS_DESTINATION_EMAIL_ADDRESS_ELEMENT_NAME,
				exam.getStatsDestinationEmail(),
				security,
				examDoc);
		appendSubNode(XML_STATISTICS_SENDER_EMAIL_ADDRESS_ELEMENT_NAME,
				new String(exam.getEncryptedStatsSenderEmail()),
				security,
				examDoc);
		appendSubNode(XML_STATISTICS_SENDER_EMAIL_PASSWORD_ELEMENT_NAME,
				new String(exam.getEncryptedStatsSenderEmailPassword()),
				security,
				examDoc);
		appendSubNode(XML_STATISTICS_SENDER_SMTP_ADDRESS_ELEMENT_NAME,
				new String(exam.getEncryptedStatsSenderSMTPAddress()),
				security,
				examDoc);
		appendSubNode(XML_STATISTICS_SENDER_SMTP_PORT_ELEMENT_NAME,
				new String(exam.getEncryptedStatsSenderSMTPPort()),
				security,
				examDoc);

		ArrayList<QuestionSet> questionSets = exam.getQuestionSets();
		if (verifyQuestionSetsIntegrity(questionSets))
		{
			for (int setsIndex = 0; setsIndex < questionSets.size(); setsIndex++)
			{
				QuestionSet qSet = questionSets.get(setsIndex);

				Element qSetElement = examDoc.createElement(XML_QUESTION_SET_ELEMENT_NAME);
				root.appendChild(qSetElement);

				appendSubNode(XML_QUESTION_SET_ID_ELEMENT_NAME,
						Integer.toString(setsIndex + 1),
						qSetElement,
						examDoc);
				appendSubNode(XML_QUESTION_SET_NAME_ELEMENT_NAME,
						(qSet.getName() == null || qSet.getName().equals(""))
								? "Question Set " + (setsIndex + 1)
								: qSet.getName(),
						qSetElement,
						examDoc);
				appendSubNode(XML_QUESTION_SET_RESOURCE_TYPE_ELEMENT_NAME,
						qSet.getResourceType().toString(),
						qSetElement,
						examDoc);
				appendSubNode(XML_QUESTION_SET_RESOURCE_DATA_ELEMENT_NAME,
						new String(qSet.getResource()),
						qSetElement,
						examDoc);

				for (int setIndex = 0; setIndex < qSet.getQuestions().size(); setIndex++)
				{
					Question question = qSet.getQuestions().get(setIndex);

					Element qElement = examDoc.createElement(XML_QUESTION_ELEMENT_NAME);
					qSetElement.appendChild(qElement);

					appendSubNode(XML_QUESTION_ID_ELEMENT_NAME,
							Integer.toString(setIndex + 1),
							qElement,
							examDoc);
					appendSubNode(XML_QUESTION_NAME_ELEMENT_NAME,
							(question.getName() == null || question.getName().equals(""))
									? "Question " + (setIndex + 1)
									: question.getName(),
							qElement,
							examDoc);
					appendSubNode(XML_QUESTION_TEXT_ELEMENT_NAME,
							question.getQuestion(),
							qElement,
							examDoc);
					appendSubNode(XML_QUESTION_SCRAMBLE_ANSWERS_ELEMENT_NAME,
							Boolean.toString(question.getScrambleAnswers()),
							qElement,
							examDoc);
					appendSubNode(XML_QUESTION_REATIAN_ANSWER_ORDER_ELEMENT_NAME,
							Boolean.toString(question.getAnswerOrderMatters()),
							qElement,
							examDoc);

					for (String encAnsHash : question.getCorrectAnswerEncryptedHashes())
						appendSubNode(XML_QUESTION_ENCRYPTED_ANSWER_HASH,
								encAnsHash,
								qElement,
								examDoc);


					for (int questionIndex = 0; questionIndex < question.getAnswerChoices().size(); questionIndex++)
					{
						AnswerChoice ac = question.getAnswerChoices().get(questionIndex);

						Element acElement = examDoc.createElement(XML_ANSWER_CHOICE_ELEMENT_NAME);
						qElement.appendChild(acElement);

						appendSubNode(XML_ANSWER_CHOICE_ID_ELEMENT_NAME,
								Integer.toString(questionIndex + 1),
								acElement,
								examDoc);
						appendSubNode(XML_ANSWER_CHOICE_VISIBLE_ID_ELEMENT_NAME,
								ac.getVisibleChoiceID(),
								acElement,
								examDoc);
						appendSubNode(XML_ANSWER_TEXT_ELEMENT_NAME,
								ac.getAnswerText(),
								acElement,
								examDoc);
					}
				}
			}
		}

		try
		{
			examTransformer.transform(
					new DOMSource(examDoc),
					new StreamResult(new FileOutputStream(examFile))
			);
		}
		catch (FileNotFoundException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK, e);
		}
		catch (TransformerException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_TRANSFORMER_EXCEPTION, e);
		}

		return true;
	}

	public static boolean writeExamToFile(Exam exam, File examFile) throws VaSolSimException
	{
		return writeExamToFile(exam, examFile, false);
	}

	/////////////////////////////////////
	//  End Builder Instance Elements  //
	/////////////////////////////////////

	private byte[] encryptedValidationHash           = new byte[]{};
	private byte[] parametricIV                      = new byte[16];
	private Cipher encryptionCipher                  = null;
	private Cipher decryptionCipher                  = null;

	private boolean reportingStats                   = false;
	private boolean reportingStatsStandalone         = false;
	private byte[] decryptedStatsSenderEmail         = NO_SMTP.getBytes();
	@SuppressWarnings("all")
	private byte[] encryptedStatsSenderEmail         = new byte[]{};
	private byte[] decryptedStatsSenderEmailPassword = NO_SMTP.getBytes();
	@SuppressWarnings("all")
	private byte[] encryptedStatsSenderEmailPassword = new byte[]{};
	private byte[] decryptedStatsSenderSMTPAddress   = NO_SMTP.getBytes();
	@SuppressWarnings("all")
	private byte[] encryptedStatsSenderSMTPAddress   = new byte[]{};
	private byte[] decryptedStatsSenderSMTPPort      = "587".getBytes();
	@SuppressWarnings("all")
	private byte[] encryptedStatsSenderSMTPPort      = new byte[]{};
	private String statsDestinationEmail             = NO_STATS;

	protected ExamBuilder(byte[] validationHash) throws VaSolSimException
	{
		/*
		 * Initialize the Ciphers
		 */
		Cipher encryptionCipher;
		Cipher decryptionCipher;
		byte[] parametricIV = new byte[16];
		try
		{
			/*
			 * Get an IV
			 */
			new SecureRandom().nextBytes(parametricIV);

			/*
			 * Init encryption
			 */
			encryptionCipher = Cipher.getInstance(DEFAULT_SERVICE_PROVIDER_INTERFACE, DEFAULT_SERVICE_PROVIDER);
			encryptionCipher.init(
					Cipher.ENCRYPT_MODE,
					new SecretKeySpec(validationHash, DEFAULT_ENCRYPTION_ALGORITHM),
					new IvParameterSpec(parametricIV));

			decryptionCipher = Cipher.getInstance(DEFAULT_SERVICE_PROVIDER_INTERFACE, DEFAULT_SERVICE_PROVIDER);
			decryptionCipher.init(
					Cipher.DECRYPT_MODE,
					new SecretKeySpec(validationHash, DEFAULT_ENCRYPTION_ALGORITHM),
					new IvParameterSpec(parametricIV)
			);

			/*
			 * Encrypt the hash.
			 */
			this.encryptedValidationHash = encryptionCipher.doFinal(validationHash);

			/*
			 * Clear the unprotected password from memory by overwriting it. Ensures even if GC misses it the memory
			 * is useless.
			 */
			for (byte b : validationHash)
				b = (byte) 0x00;
		}
		/*
		 * Catch the MANY, MANY exceptions involved with crypto and rebrand them as VaSolSimExceptions for debugging.
		 */
		catch (IllegalBlockSizeException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nILLEGAL BLOCK SIZE\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}
		catch (BadPaddingException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PADDING\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}
		catch (NoSuchProviderException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD PROVIDER\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}
		catch (NoSuchPaddingException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nNO SUCH PADDING\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}
		catch (InvalidKeyException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD KEY\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}
		catch (InvalidAlgorithmParameterException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD ALGORITHM PARAMS\n" +
					e.toString() + "\n" +
					e.getCause() + "\n" +
					ExceptionUtils.getStackTrace(e),
					e);
		}

		this.parametricIV = parametricIV;
		this.encryptionCipher = encryptionCipher;
		this.decryptionCipher = decryptionCipher;
	}

	public Exam getExamFramework() throws VaSolSimException
	{
		/*
		 * Validate fields
		 */
		if (reportingStats)
		{
			if (!isValidEmail(statsDestinationEmail))
				throw new VaSolSimException(ERROR_MESSAGE_STATS_DESTINATION_ADDRESS_NOT_PROVIDED);

			if (reportingStatsStandalone)
			{
				if (!isValidEmail(new String(decryptedStatsSenderEmail)))
					throw new VaSolSimException(ERROR_MESSAGE_STANDALONE_STATS_ADDRESS_NOT_PROVIDED);

				if (decryptedStatsSenderEmailPassword.length < 1)
					throw new VaSolSimException(ERROR_MESSAGE_STANDALONE_STATS_PASSWORD_NOT_PROVIDED);

				if (!isValidAddress(new String(decryptedStatsSenderSMTPAddress)))
					throw new VaSolSimException(ERROR_MESSAGE_STANDALONE_STATS_SMTP_ADDRESS_NOT_PROVIDED);

				if (!isValidPort(Integer.parseInt(new String(decryptedStatsSenderSMTPPort))))
					throw new VaSolSimException(ERROR_MESSAGE_STANDALONE_STATS_SMTP_PORT_INVALID);

				if (!isValidSMTPConfiguration(
						new String(decryptedStatsSenderSMTPAddress),
						Integer.parseInt(new String(decryptedStatsSenderSMTPPort)),
						new String(decryptedStatsSenderEmail),
						decryptedStatsSenderEmailPassword,
						true))
					throw new VaSolSimException(ERROR_MESSAGE_BAD_SMTP_CREDENTIALS);
			}
		}

		/*
		 * Encrypt fields
		 */
		if (!isCipherProperlyInitialized(encryptionCipher) || !isCipherProperlyInitialized(decryptionCipher))
			throw new VaSolSimException(ERROR_MESSAGE_CIPHER_NOT_INITIALIZED_PROPERLY);

		encryptedStatsSenderEmail         = applyCryptographicCipher(
				decryptedStatsSenderEmail, encryptionCipher);
		encryptedStatsSenderEmailPassword = applyCryptographicCipher(
				decryptedStatsSenderEmailPassword, encryptionCipher);
		encryptedStatsSenderSMTPAddress   = applyCryptographicCipher(
				decryptedStatsSenderSMTPAddress, encryptionCipher);
		encryptedStatsSenderSMTPPort      = applyCryptographicCipher(
				decryptedStatsSenderSMTPPort, encryptionCipher);

		return new Exam(encryptedValidationHash,
				parametricIV,
				encryptionCipher,
				decryptionCipher,
				statsDestinationEmail,
				reportingStats,
				reportingStatsStandalone,
				encryptedStatsSenderEmail,
				encryptedStatsSenderEmailPassword,
				encryptedStatsSenderSMTPAddress,
				encryptedStatsSenderSMTPPort,
				false);
	}

	public boolean isReportingStats()
	{
		return reportingStats;
	}

	public void setReportingStats(boolean reportingStats)
	{
		this.reportingStats = reportingStats;
	}

	public boolean isReportingStatsStandalone()
	{
		return reportingStatsStandalone;
	}

	public void setReportingStatsStandalone(boolean reportingStatsStandalone)
	{
		this.reportingStatsStandalone = reportingStatsStandalone;
	}

	public String getStatsDestinationEmail()
	{
		return statsDestinationEmail;
	}

	public void setStatsDestinationEmail(String statsDestinationEmail)
	{
		if (statsDestinationEmail != null && isValidEmail(statsDestinationEmail))
			this.statsDestinationEmail = statsDestinationEmail;
	}

	public void setStatsSenderEmail(String emailAddress)
	{
		if (isValidEmail(emailAddress))
			this.decryptedStatsSenderEmail = emailAddress.getBytes();
	}

	public void setStatsSenderEmailPassword(byte[] password)
	{
		if (password.length > 0)
			this.decryptedStatsSenderEmailPassword = password;
	}

	public void setStatsSenderSMTPAddress(String address)
	{
	 	if (isValidAddress(address))
		    this.decryptedStatsSenderSMTPAddress = address.getBytes();
	}
}
