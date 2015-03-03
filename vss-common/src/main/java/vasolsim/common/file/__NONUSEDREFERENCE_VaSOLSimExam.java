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

import main.java.vasolsim.common.*;

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
import java.util.regex.Pattern;

/**
 * @author guyfleeman
 * @date 6/25/14 <p></p>
 */
public class __NONUSEDREFERENCE_VaSOLSimExam
{
	public static int initialQuestionArraySize;

	//root depth
	public static final String xmlRootElementName = "vssroot";

	//root+ depth
	public static final String xmlInfoElementName = "information";

	//root++ depth (information+)
	public static final String xmlTestNameElementName    = "testName";
	public static final String xmlAuthorElementName      = "author";
	public static final String xmlSchoolNameElementName  = "school";
	public static final String xmlClassPeriodElementName = "class";
	public static final String xmlDateElementName        = "date";

	//root+ depth
	public static final String xmlSecurityElementName = "sec";

	//root++ depth (sec+)
	public static final String xmlHashAlgorithmElementName                     = "hashAlgorithm";
	public static final String xmlValidationHashElementName                    = "encryptedValidationHash";
	public static final String xmlValidationPIVElementName                     = "parametricIV";
	public static final String xmlEncryptingQuestionsElementName               = "encryptingQuestions";
	public static final String xmlReportingStatisticsElementName               = "statisticsReporting";
	public static final String xmlStandaloneSMTPStatisticsReportingElementName = "statisticsStandaloneParadigm";
	public static final String xmlEncryptingStatisticsElementName              = "encryptingStatistics";
	public static final String xmlEncryptedStatisticsReportingEmailAddressElementName
	                                                                           = "encryptedStatisticsReportingEmail";
	public static final String xmlEncryptedStatisticsReportingEmailPasswordElementName
	                                                                             =
			"encryptedStatisticsReportingEmailPassword";
	public static final String xmlReportingNotificationsElementName              = "notificationReporting";
	public static final String xmlStandaloneSMTPNotificationReportingElementName = "notificationStandaloneParadigm";
	public static final String xmlEncryptedNotificationReportingEmailAddressElementName
	                                                                             =
			"encryptedNotificationReportingEmail";
	public static final String xmlEncryptedNotificationReportingEmailPasswordElementName
	                                                                             =
			"encryptedNotificationReportingEmailPassword";

	//root+ depth
	public static final String xmlQuestionSetElementName = "questionSet";

	//root++ depth (questionSet+)
	public static final String xmlQuestionSetIDElementName               = "setID";
	public static final String xmlQuestionSetUsingResourceElementName    = "usingResource";
	public static final String xmlQuestionSetResourceParadigmElementName = "resourceParadigm";
	public static final String xmlQuestionSetResourceDataElementName     = "resourceData";
	public static final String xmlQuestionGroupingElementName            = "questionGrouping";

	//root+++ depth (questionSet++, questionGrouping+)
	public static final String xmlQuestionGroupingIDElementName                  = "questionID";
	public static final String xmlQuestionGroupingQuestionElementName            = "QandA";
	public static final String xmlQuestionGroupingEncryptedAnswerHashElementName = "encryptedAnswerHash";
	public static final String xmlQuestionGroupingAnswerChoiceElementName        = "answerChoice";
	public static final String xmlQuestionGroupingIsScramblingAnswers            = "scramblingAnswers";
	public static final String xmlQuestionGroupingDoesAnswerOrderMater           = "orderMatters";

	//root++++ depth (questionSet+++, questionGrouping++, answer+)
	public static final String xmlAnswerID       = "answerID";
	public static final String xmlAnswerChoiceID = "answerChoiceID";
	public static final String xmlAnswerChoice   = "answer";

	protected static byte[] IV                                = new byte[16];
	protected static short  algorithmExpectedKeyLengthInBytes = 16;
	protected static String serviceProviderInterface          = "AES/CBC/PKCS5Padding";
	protected static String serviceProvider                   = "SunJCE";
	protected static String algorithm                         = "AES";
	protected static String charsetEncoding                   = "UTF-8";
	protected static String validEmailRegex                   = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
			"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	protected static String indentationKey                    = "{http://xml.apache.org/xslt}indent-amount";
	protected static SecureRandom secureRandom;
	protected static Pattern      validEmailPattern;

	private static final String ERROR_MESSAGE_FILE_ALREADY_EXISTS                           =
			"File already exists and overwrite not permitted.";
	private static final String ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK           =
			"File not found after internal check. Try running as admin. Is this a bug or a file permissions error?";
	private static final String ERROR_MESSAGE_COULD_NOT_CREATE_DIRS                         =
			"Could not create file directory. Do you have permission to create a directory on your machine? " +
					"(try running the jar as admin)";
	private static final String ERROR_MESSAGE_COULD_NOT_CREATE_FILE                         =
			"Could not create file. Do you have permission to create a file on your machine? " +
					"(try running the jar as admin)";
	private static final String ERROR_MESSAGE_CREATE_FILE_EXCEPTION                         =
			"File creation internal exception. Do you have permission to create a file on your machine? Could this " +
					"be" +
					" " +
					"a bug? (try running the jar as admin)";
	private static final String ERROR_MESSAGE_GENERIC_CRYPTO                                =
			"This message being shown for debugging purposes. If the problem persists, please paste the following " +
					"information into an email for the project manager.";
	private static final String ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION            = "This message being " +
			"shown for " +
			"debugging purposes. If the problem persists, please paste the following information into an email for " +
			"the " +
			"project manager. COULD NOT WRITE XML FILE. INTERNAL CONFIGURATION ERROR.";
	private static final String ERROR_MESSAGE_INTERNAL_TRANSFORMER_EXCEPTION                = "This message being " +
			"shown for " +
			"debugging " +
			"purposes. If the problem persists, please paste the following information into an email for the project" +
			" " +
			"manager. COULD NOT WRITE XML FILE. INTERNAL TRANSFORMER EXCEPTION";
	private static final String ERROR_MESSAGE_VALIDATION_KEY_NOT_PROVIDED                   = "VALIDATION KEY NOT " +
			"PROVIDED AND IS " +
			"REQUIRED";
	private static final String ERROR_MESSAGE_STANDALONE_STATS_PASSWORD_NOT_PROVIDED        = "Statistics requested " +
			"with " +
			"standalone paradigm and no password provided.";
	private static final String ERROR_MESSAGE_STANDALONE_NOTIFICATION_PASSWORD_NOT_PROVIDED = "Notification " +
			"requested" +
			" " +
			"with standalone paradigm and no password provided.";
	private static final String ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION  = "This message being " +
			"shown " +
			"for debugging purposes. If the problem persists, please paste the following information into an email " +
			"for " +
			"the project manager.";

	static
	{
		secureRandom = new SecureRandom();
		secureRandom.nextBytes(IV);

		validEmailPattern = Pattern.compile(validEmailRegex);
	}

	public String getTestName()
	{
		return testName;
	}

	public void setTestName(String testName)
	{
		this.testName = testName;
	}

	public static enum HashType
	{
		SHA256,
		SHA512
	}

	private String testName      = "undefined name";
	private String testPublisher = "undefined author";
	private String schoolName    = "undefined school";
	private String className     = "undefined class";
	private String publishDate   = "undefined date";

	private boolean  isEncryptingFullQuestions = false;
	private byte[]   validationKey             = new byte[]{};
	private byte[]   encryptedValidationHash   = new byte[]{};
	private byte[]   decryptedValidationHash   = new byte[]{};
	private HashType hashAlgorithm             = HashType.SHA256;

	private boolean isReportingStatistics                             = false;
	private boolean isReportingStatisticsUsingStandaloneEmailParadigm = false;
	private boolean isReportingStatisticsEncrypted                    = true;
	private boolean isNotifyingCompletion                             = false;
	private boolean isNotifyingCompletionUsingStandaloneEmailParadigm = false;
	private byte[]  encryptedStatisticsEmail                          = new byte[]{};
	private byte[]  encryptedStatisticsEmailPassword                  = new byte[]{};
	private byte[]  decryptedStatisticsEmailPassword                  = new byte[]{};
	private byte[]  encryptedNotificationEmail                        = new byte[]{};
	private byte[]  encryptedNotificationEmailPassword                = new byte[]{};
	private byte[]  decryptedNotificationEmailPassword                = new byte[]{};
	private String  decryptedStatisticsEmail                          = "";
	private String  decryptedNotificationEmail                        = "";

	private Cipher encryptionCipher;
	private Cipher decryptionCipher;

	private ArrayList<QuestionSet> questions = new ArrayList<QuestionSet>(initialQuestionArraySize);

	@SuppressWarnings("unused")
	private final byte __BEGIN_ENCAPSULATED_FIELDS = 0x00;

	/**
	 * Returns the publisher or author of the test.
	 *
	 * @return publisher
	 */
	public String getTestPublisher()
	{
		return testPublisher;
	}

	/**
	 * Sets the publisher or author of the test.
	 *
	 * @param testPublisher publisher
	 */
	public void setTestPublisher(String testPublisher)
	{
		this.testPublisher = testPublisher;
	}

	/**
	 * Gets the name of the school form which the test was made
	 *
	 * @return school name
	 */
	public String getSchoolName()
	{
		return schoolName;
	}

	/**
	 * Sets the school form which the test will be made
	 *
	 * @param schoolName school name
	 */
	public void setSchoolName(String schoolName)
	{
		this.schoolName = schoolName;
	}

	/**
	 * Gets the name of the class for which the test was made
	 *
	 * @return class name
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * Sets the name of the class for which the test will be made
	 *
	 * @param className class name
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * Get the date of publication
	 *
	 * @return publication date
	 */
	public String getPublishDate()
	{
		return publishDate;
	}

	/**
	 * Set the date of publication
	 *
	 * @param publishDate publication date
	 */
	public void setPublishDate(String publishDate)
	{
		this.publishDate = publishDate;
	}

	/**
	 * Directs the writer to encrypt the entire QandA, not just the correct answer, when the test is exported.
	 */
	public void fullyEncryptQuestions()
	{
		isEncryptingFullQuestions = true;
	}

	/**
	 * Directs the writer to encrypt only the answers, saving time, when the test is exported.
	 */
	public void partiallyEncryptQuestions()
	{
		isEncryptingFullQuestions = false;
	}

	/**
	 * @return if the writer is encrypting the entire QandA
	 */
	public boolean isEncryptingFullQuestions()
	{
		return isEncryptingFullQuestions;
	}

	/**
	 * Sets the key that will be used to lock and protect the file. Then generates and updates the key hash.
	 *
	 * @param key key
	 */
	public void setValidationKey(String key)
	{
		setValidationKey(key.getBytes());
	}

	/**
	 * Sets the key that will be used to lock and protect the file. Then generates and updates the key hash.
	 *
	 * @param key key
	 */
	public void setValidationKey(byte[] key)
	{
		this.validationKey = key;
		decryptedValidationHash = generateHash(validationKey, HashType.SHA256);
	}

	/**
	 * Returns the key that will be used to lock and protect the file. Will not return the key from a read file.
	 *
	 * @return key
	 */
	public byte[] getValidationKey()
	{
		return validationKey;
	}

	/**
	 * Returns the hash generated from the key with the parameters specified by the static fields.
	 *
	 * @return hash
	 */
	public byte[] getDecryptedValidationHash()
	{
		return decryptedValidationHash;
	}

	/**
	 * Returns the hash of the key encrypted by the encryption DEFAULT_ENCRYPTION_ALGORITHM initialized by the hash.
	 * This one way process locks file's sensitive data from the students who might try to poke around. This value will
	 * not be initialized until the file is written, or the crypto is forced active and called prior to writing;
	 *
	 * @return encrypted validation hash.
	 */
	public byte[] getEncryptedValidationHash()
	{
		return encryptedValidationHash;
	}

	/**
	 * Returns the DEFAULT_ENCRYPTION_ALGORITHM used to hash the key
	 *
	 * @return hash DEFAULT_ENCRYPTION_ALGORITHM
	 */
	public HashType getHashAlgorithm()
	{
		return hashAlgorithm;
	}

	/**
	 * Returns if the test will report statistics when the student completes it.
	 *
	 * @return will statistics be reported
	 */
	public boolean isReportingStatistics()
	{
		return isReportingStatistics;
	}

	/**
	 * Sets if the test will report statistics when the student completes it. If this is set to true,
	 * a statistics email
	 * address must be provided. After the test finishes, the data regarding time taken, date taken, questions
	 * answered,
	 * and correct answers will be built into an email body. An email will then be sent to the given email address so
	 * the teacher/admin side client can read all of the emails and assemble reporting statistics for the student body
	 * that took the test. This feature is included because I know it can be difficult for a teacher to get access to a
	 * server to host test data. I believe this is an easy way to compile stats without requiring teachers to have
	 * access to a reliable server. If this is set to true the student will provide his/her email at the beginning of
	 * the test and the program will automatically and securely log into the students email and send the statistics. It
	 * is strongly recommended that you not disable the encryption of the statistics when they are sent. This protects
	 * the student's email, as well as the integrity of the collected information.
	 *
	 * @param isReportingStatistics
	 */
	public void setReportingStatistics(boolean isReportingStatistics)
	{
		this.isReportingStatistics = isReportingStatistics;
	}

	/**
	 * Returns if the test will be using a standalone email paradigm for reporting test statistics.
	 *
	 * @return if the test will be using a standalone email paradigm
	 */
	public boolean isReportingStatisticsUsingStandaloneEmailParadigm()
	{
		return isReportingStatisticsUsingStandaloneEmailParadigm;
	}

	/**
	 * Sets if the test will be using a standalone email paradigm for reporting test statistics. A standalone paradigm
	 * means that the student will not have to provide any email information for the email to be sent. Instead, the
	 * password for the receiving email account must be provided. Then, then the student completes the test, the
	 * receiving email account will send an email to itself. This means however that the email's password must be sent
	 * in the file since the student is no longer providing this information.
	 * <p/>
	 * NOTE: THE STANDALONE EMAIL PARADIGM IS NOT SECURE IF THE TEST WILL BE PUBLISHED ONLINE PUBLICLY IN COMBINATION
	 * WITH THE TEST'S PASSWORD. SECURITY ISSUES ARE PREVENTED BY USING A SERVER, BUT THIS PROGRAM IS DESIGNED TO ACT
	 * INDEPENDENTLY OF SUCH. IF YOU ARE PUBLISHING THE TEST'S XML FILE ONLINE, DO NOT PUBLISH THE PASSWORD ALONGSIDE
	 * IT. The password is encrypted so it cannot be read, but a hacker who knows the tests password and can program in
	 * Java could reverse engineer the encryption process to obtain the email address and password. It is expected that
	 * this skill will be outside of the scope of high school students.
	 *
	 * @param isReportingStatisticsUsingStandaloneEmailParadigm if the test will be using a standalone email paradigm
	 *                                                          for reporting test statistics
	 */
	public void setReportingStatisticsUsingStandaloneEmailParadigm(
			boolean isReportingStatisticsUsingStandaloneEmailParadigm)
	{
		this.isReportingStatisticsUsingStandaloneEmailParadigm = isReportingStatisticsUsingStandaloneEmailParadigm;
	}

	/**
	 * Returns if the statistics being reported will be encrypted. True by default.
	 *
	 * @return if the statistics being reported will be encrypted
	 */
	public boolean isReportingStatisticsEncrypted()
	{
		return isReportingStatisticsEncrypted;
	}

	/**
	 * Sets if the statistics being reported will be encrypted. True by default.
	 *
	 * @param isReportingStatisticsEncrypted if the statistics being reported will be encrypted
	 */
	public void setReportingStatisticsEncrypted(boolean isReportingStatisticsEncrypted)
	{
		this.isReportingStatisticsEncrypted = isReportingStatisticsEncrypted;
	}

	/**
	 * Returns if notification emails will be sent.
	 *
	 * @return if notification emails will be sent.
	 */
	public boolean isNotifyingCompletion()
	{
		return isNotifyingCompletion;
	}

	/**
	 * Sets if notification emails will be sent when a student completes a test. Notifications emails are sent to the
	 * notification address, and notify the holder of that address that x student, completed x test. If this is enabled
	 * the student must provide email credentials when he/she starts the test. The email will be automatically sent
	 * form
	 * his/her account. This is not the same as reporting statistics. I assume teachers don't want a bunch of
	 * statistics
	 * bulking up their email, but they might want a log of who has/hasn't completed the test. Also,
	 * this can serve as a
	 * log if the teacher doesn't want statistics at all.
	 *
	 * @param isNotifyingCompletion if notification emails will be sent
	 */
	public void setNotifyingCompletion(boolean isNotifyingCompletion)
	{
		this.isNotifyingCompletion = isNotifyingCompletion;
	}

	/**
	 * Returns if the test will be using a standalone email paradigm for reporting test completion notification.
	 *
	 * @return if the test will be using a standalone email paradigm
	 */
	public boolean isNotifyingCompletionUsingStandaloneEmailParadigm()
	{
		return isNotifyingCompletionUsingStandaloneEmailParadigm;
	}

	/**
	 * Sets if the test will be using a standalone email paradigm for reporting test completion. A standalone paradigm
	 * means that the student will not have to provide any email information for the email to be sent. Instead, the
	 * password for the receiving email account must be provided. Then, then the student completes the test, the
	 * receiving email account will send an email to itself. This means however that the email's password must be sent
	 * in the file since the student is no longer providing this information.
	 * <p/>
	 * NOTE: THE STANDALONE EMAIL PARADIGM IS NOT SECURE IF THE TEST WILL BE PUBLISHED ONLINE PUBLICLY IN COMBINATION
	 * WITH THE TEST'S PASSWORD. SECURITY ISSUES ARE PREVENTED BY USING A SERVER, BUT THIS PROGRAM IS DESIGNED TO ACT
	 * INDEPENDENTLY OF SUCH. IF YOU ARE PUBLISHING THE TEST'S XML FILE ONLINE, DO NOT PUBLISH THE PASSWORD ALONGSIDE
	 * IT. The password is encrypted so it cannot be read, but a hacker who knows the tests password and can program in
	 * Java could reverse engineer the encryption process to obtain the email address and password. It is expected that
	 * this skill will be outside of the scope of high school students.
	 *
	 * @param isNotifyingCompletionUsingStandaloneEmailParadigm if the test will be using a standalone email paradigm
	 *                                                          for reporting test statistics
	 */
	public void setNotifyingCompletionUsingStandaloneEmailParadigm(
			boolean isNotifyingCompletionUsingStandaloneEmailParadigm)
	{
		this.isNotifyingCompletionUsingStandaloneEmailParadigm = isNotifyingCompletionUsingStandaloneEmailParadigm;
	}

	/**
	 * Gets decrypted statistics email password
	 *
	 * @return
	 */
	public byte[] getDecryptedStatisticsEmailPassword()
	{
		return decryptedStatisticsEmailPassword;
	}

	/**
	 * Sets decrypted statistics email password
	 *
	 * @param decryptedStatisticsEmailPassword
	 */
	public void setDecryptedStatisticsEmailPassword(byte[] decryptedStatisticsEmailPassword)
	{
		this.decryptedStatisticsEmailPassword = decryptedStatisticsEmailPassword;
	}

	/**
	 * Gets decrypted notification email password
	 *
	 * @return
	 */
	public byte[] getDecryptedNotificationEmailPassword()
	{
		return decryptedNotificationEmailPassword;
	}

	/**
	 * Sets decrypted notification email password
	 *
	 * @param decryptedNotificationEmailPassword
	 */
	public void setDecryptedNotificationEmailPassword(byte[] decryptedNotificationEmailPassword)
	{
		this.decryptedNotificationEmailPassword = decryptedNotificationEmailPassword;
	}

	/**
	 * Gets decrypted statistics email address
	 *
	 * @return
	 */
	public String getDecryptedStatisticsEmail()
	{
		return decryptedStatisticsEmail;
	}

	/**
	 * Sets decrypted statistics email address
	 *
	 * @param decryptedStatisticsEmail
	 */
	public void setDecryptedStatisticsEmail(String decryptedStatisticsEmail) throws VaSolSimException
	{
		if (!isValidEmail(decryptedStatisticsEmail))
			throw new VaSolSimException("Bad statistics email");

		this.decryptedStatisticsEmail = decryptedStatisticsEmail;
	}

	/**
	 * Gets decrypted statistics email address
	 *
	 * @return
	 */
	public String getDecryptedNotificationEmail()
	{
		return decryptedNotificationEmail;
	}

	/**
	 * Sets decrypted notification email address
	 *
	 * @param decryptedNotificationEmail
	 */
	public void setDecryptedNotificationEmail(String decryptedNotificationEmail) throws VaSolSimException
	{
		if (!isValidEmail(decryptedNotificationEmail))
			throw new VaSolSimException("Bad notification email");

		this.decryptedNotificationEmail = decryptedNotificationEmail;
	}

	@SuppressWarnings("unused")
	private final byte __END_ENCAPSULATED_FIELDS = 0x00;

	/**
	 * This function will initialize the internal ciphers that protect the data in the file. This process is done
	 * automatically when the write function is called. If access is needed to the encrypted versions of data for
	 * whatever reason prior to the write process, OR IF THE KEY IS UPDATED prior to the write process, this function
	 * must be called
	 *
	 * @throws VaSolSimException if the ciphers cannot be initialized, an exception will be thrown
	 */
	public void updateCryptoEngine() throws VaSolSimException
	{
		decryptionCipher = getDecryptionCipher(decryptedValidationHash);
		encryptionCipher = getEncryptionCipher(decryptedValidationHash);
	}

	/**
	 * This function uses the initialized ciphers to encrypt properties of the file. If any properties or questions are
	 * updated, this function must be called in order to have access to the encrypted data prior to the write process.
	 * This function is called by default at the start of the write function. NOTE: if the cryptographic key is
	 * updated,
	 * updateCryptoEngine() must be called first or this function will encrypt the data based off of the old key.
	 * Regardless, the write function will force everything to be updated prior to actually writing to avoid write
	 * complications.
	 */
	public void updateCryptoProperties() throws VaSolSimException
	{
		try
		{
			if (decryptedValidationHash.length > 0)
				encryptedValidationHash = encryptionCipher.doFinal(decryptedValidationHash);

			if (getDecryptedNotificationEmail() != null)
				encryptedNotificationEmail = encryptionCipher.doFinal(getDecryptedNotificationEmail().getBytes());

			if (getDecryptedNotificationEmailPassword().length > 0)
				encryptedNotificationEmailPassword = encryptionCipher.doFinal(getDecryptedNotificationEmailPassword());

			if (getDecryptedStatisticsEmail() != null)
				encryptedStatisticsEmail = encryptionCipher.doFinal(getDecryptedStatisticsEmail().getBytes());

			if (getDecryptedStatisticsEmailPassword().length > 0)
				encryptedStatisticsEmailPassword = encryptionCipher.doFinal(getDecryptedStatisticsEmailPassword());
		}
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

	}

	/**
	 * Writes a digitized version of the VaSOLSim test, represented by an thisInstance of this class,
	 * to a given file in
	 * XML format. Will not overwrite by default. Prior to writing, this function will (re)initialize teh Ciphers used
	 * to protect the file, and update all protected information accordingly.
	 *
	 * @param simFile the file on the disk to be written
	 *
	 * @return if the write operation was successful
	 *
	 * @throws VaSolSimException thrown if insufficient information to write the file is contained in VaSolSimTest
	 *                           object. Please ensure a password is provided to protect answer data and potentially
	 *                           email data if test statistics and notifications are reported.
	 */
	public boolean write(File simFile) throws VaSolSimException
	{
		return write(simFile, false);
	}

	/**
	 * Writes a digitized version of the VaSOLSim test, represented by an thisInstance of this class,
	 * to a given file in
	 * XML format. Prior to writing, this function will (re)initialize teh Ciphers used to protect the file, and update
	 * all protected information accordingly.
	 *
	 * @param simFile          the file on the disk to be written
	 * @param canOverwriteFile can this method call write over an existing file with content
	 *
	 * @return if the write operation was successful
	 *
	 * @throws VaSolSimException thrown if insufficient information to write the file is contained in VaSolSimTest
	 *                           object. Please ensure a password is provided to protect answer data and potentially
	 *                           email data if test statistics and notifications are reported.
	 */
	public boolean write(File simFile, boolean canOverwriteFile) throws VaSolSimException
	{
		if (simFile.isFile())
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
					printWriter = new PrintWriter(simFile);
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
			if (!simFile.getParentFile().isDirectory() && !simFile.getParentFile().mkdirs())
			{
				throw new VaSolSimException(ERROR_MESSAGE_COULD_NOT_CREATE_DIRS);
			}

			try
			{
				if (!simFile.createNewFile())
				{
					throw new VaSolSimException(ERROR_MESSAGE_COULD_NOT_CREATE_FILE);
				}
			}
			catch (IOException e)
			{
				throw new VaSolSimException(ERROR_MESSAGE_CREATE_FILE_EXCEPTION);
			}
		}

		//update crypto stuff
		updateCryptoEngine();
		updateCryptoProperties();

		/*
         * Check for any missing encrypted data that is required to read the test as specified
		 */
		if (encryptedValidationHash.length < 1)
			throw new VaSolSimException(ERROR_MESSAGE_VALIDATION_KEY_NOT_PROVIDED);

		/*
		 * Leave out for client side information gathering
		 */
		/*
		if (isNotifyingCompletion && encryptedNotificationEmail.length < 1)
			throw new VaSolSimException("Notification requested with no email provided!");
			*/


		if (isNotifyingCompletion
				&& isNotifyingCompletionUsingStandaloneEmailParadigm
				&& encryptedNotificationEmailPassword.length < 1)
			throw new VaSolSimException(ERROR_MESSAGE_STANDALONE_NOTIFICATION_PASSWORD_NOT_PROVIDED);

		/*
		if (isReportingStatistics && encryptedStatisticsEmail.length < 1)
			throw new VaSolSimException("Statistics requested with no email provided!");
			*/

		if (isReportingStatistics
				&& isReportingStatisticsUsingStandaloneEmailParadigm
				&& encryptedStatisticsEmailPassword.length < 1)
			throw new VaSolSimException(ERROR_MESSAGE_STANDALONE_STATS_PASSWORD_NOT_PROVIDED);

		Document solExam;
		try
		{
			solExam = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		}
		catch (ParserConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_XML_PARSER_INITIALIZATION_EXCEPTION, e);
		}

		//create document root
		Element root = solExam.createElement(xmlRootElementName);
		solExam.appendChild(root);

		//append the information sub element
		Element information = solExam.createElement(xmlInfoElementName);
		root.appendChild(information);

		Element security = solExam.createElement(xmlSecurityElementName);
		root.appendChild(security);

		/*
		 * Build information element tree
		 */
		createInformationElements(information, solExam);

		/*
		 * Build security element tree
		 */
		createSecurityElements(security, solExam);

		try
		{
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
			transformer.setOutputProperty(indentationKey, "4");

			transformer.transform(
					new DOMSource(solExam),
					new StreamResult(new FileOutputStream(simFile))
			);
		}
		catch (FileNotFoundException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_FILE_NOT_FOUND_AFTER_INTERNAL_CHECK, e);
		}
		catch (TransformerConfigurationException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_TRANSFORMER_CONFIGURATION, e);
		}
		catch (TransformerException e)
		{
			throw new VaSolSimException(ERROR_MESSAGE_INTERNAL_TRANSFORMER_EXCEPTION, e);
		}

		return true;
	}

	/**
	 * Builds the elements for the information node of the xml file. If further information is needed, please see the
	 * file called vssXMLSchema.xml
	 *
	 * @param informationParentElement the parent element for the attached information
	 * @param test                     the primary document
	 */
	protected void createInformationElements(Element informationParentElement, Document test)
	{
		appendSubNode(xmlTestNameElementName, testName, informationParentElement, test);
		appendSubNode(xmlAuthorElementName, testPublisher, informationParentElement, test);
		appendSubNode(xmlSchoolNameElementName, testPublisher, informationParentElement, test);
		appendSubNode(xmlClassPeriodElementName, className, informationParentElement, test);
		appendSubNode(xmlDateElementName, publishDate, informationParentElement, test);
	}

	/**
	 * Builds the elements for the security node of the xml file. If further information is needed, please see the file
	 * called vssXMLSchema.xml.
	 * <p/>
	 * I wrote this after two tequila sunrises and a malibu sunset and it worked first try. Get on my level.
	 *
	 * @param securityParentElement the parent element for the attached security information
	 * @param test                  the primary document
	 *
	 * @throws VaSolSimException thrown if insufficient information for the crypto is given
	 */
	protected void createSecurityElements(Element securityParentElement, Document test) throws VaSolSimException
	{
		/*
		 * Internal security
		 */
		appendSubNode(xmlHashAlgorithmElementName, hashAlgorithm.toString(), securityParentElement, test);
		appendSubNode(xmlValidationHashElementName,
		              //convertHashToString(encryptionCipher.doFinal(validationKey)),
		              convertHashToString(encryptedValidationHash),
		              securityParentElement,
		              test);
		appendSubNode(xmlValidationPIVElementName, new String(IV), securityParentElement, test);
		appendSubNode(xmlEncryptingQuestionsElementName,
		              Boolean.toString(isEncryptingFullQuestions),
		              securityParentElement,
		              test);

		/*
		 * Statistics reporting
		 */
		appendSubNode(xmlReportingStatisticsElementName,
		              Boolean.toString(isReportingStatistics),
		              securityParentElement,
		              test);
		appendSubNode(xmlStandaloneSMTPStatisticsReportingElementName,
		              Boolean.toString(isReportingStatisticsUsingStandaloneEmailParadigm),
		              securityParentElement,
		              test);
		appendSubNode(xmlEncryptingQuestionsElementName,
		              Boolean.toString(isReportingStatisticsEncrypted),
		              securityParentElement,
		              test);

		if (isReportingStatistics)
			appendSubNode(xmlEncryptedStatisticsReportingEmailAddressElementName,
			              new String(encryptedStatisticsEmail),
			              securityParentElement,
			              test);
		else
			appendSubNode(xmlEncryptedStatisticsReportingEmailAddressElementName,
			              "!!statisticsemailnotprovided!!",
			              securityParentElement,
			              test);

		if (isReportingStatisticsUsingStandaloneEmailParadigm)
			appendSubNode(xmlEncryptedStatisticsReportingEmailPasswordElementName,
			              new String(encryptedStatisticsEmailPassword),
			              securityParentElement,
			              test);
		else
			appendSubNode(xmlEncryptedStatisticsReportingEmailPasswordElementName,
			              "!!notusingstandaloneparadigm",
			              securityParentElement,
			              test);

		/*
		 * Notification reporting
		 */
		appendSubNode(xmlReportingNotificationsElementName,
		              Boolean.toString(isNotifyingCompletion),
		              securityParentElement,
		              test);
		appendSubNode(xmlStandaloneSMTPNotificationReportingElementName,
		              Boolean.toString(isNotifyingCompletionUsingStandaloneEmailParadigm),
		              securityParentElement,
		              test);

		if (isNotifyingCompletion)
			appendSubNode(xmlEncryptedNotificationReportingEmailAddressElementName,
			              new String(encryptedNotificationEmail),
			              securityParentElement,
			              test);
		else
			appendSubNode(xmlEncryptedNotificationReportingEmailAddressElementName,
			              "!!notificationemailnotprovided!!",
			              securityParentElement,
			              test);

		if (isNotifyingCompletionUsingStandaloneEmailParadigm)
			appendSubNode(xmlEncryptedNotificationReportingEmailPasswordElementName,
			              new String(encryptedNotificationEmailPassword),
			              securityParentElement,
			              test);
		else
			appendSubNode(xmlEncryptedNotificationReportingEmailPasswordElementName,
			              "!!notusingstandaloneparadigm!!",
			              securityParentElement,
			              test);
	}

	protected void createQuestionElements()
	{

	}

	protected static void appendSubNode(String elementName, String nodeData, Element parentElement, Document doc)
	{
		Element subElement = doc.createElement(elementName);
		subElement.appendChild(doc.createTextNode(nodeData));
		parentElement.appendChild(subElement);
	}

	/**
	 * Returns if a given string is a valid email regex
	 *
	 * @param email email address
	 *
	 * @return if the email address is a valid email regex
	 */
	public static boolean isValidEmail(String email)
	{
		return validEmailPattern.matcher(email).matches();
	}

	/**
	 * Returns the bytes currently being used as the parametric initialization vector used to encrypted elements of the
	 * file
	 *
	 * @return parametricIV
	 */
	public static byte[] getEncryptionParametricIV()
	{
		return IV;
	}

	/**
	 * Sets the bytes currently being used as the parametric initialization vector used to encrypted elements of the
	 * file. It is recommended the function generateNewEncryptionParametricIV() be used for the highest level of
	 * security
	 *
	 * @param encryptionParametricIV teh new parametricIV
	 */
	public static void setEncryptionParametricIV(byte[] encryptionParametricIV)
	{
		IV = encryptionParametricIV;
	}

	/**
	 * Sets the bytes currently being used as the parametric initialization vector used to encrypted elements of the
	 * file. This function uses a SecureRandom object internal to the class.
	 */
	public static void generateNewEncryptionParametricIV()
	{
		secureRandom.nextBytes(IV);
	}

	/**
	 * Reads a file from a given directory, validates it with the given key, and loads the data into a VaSOLSimExam
	 * object.
	 *
	 * @param file the file to be read
	 * @param key  the key to open the exam
	 *
	 * @return the loaded and verified exam
	 *
	 * @throws IOException           thrown if the function hits any IO issues independent of the VaSOMSim program
	 * @throws VaSolSimException     thrown if any problems occur with validating the file or any other data in the
	 *                               file
	 * @throws MalformedXMLException thrown if the XML file does not follow the schema established for VaSOLSim files.
	 *                               If you are unsure as to what this is, please see the file
	 *                               /xmlschema/cssXMLSchema.xml and consult the documentation located there and on w3.
	 */
	public static __NONUSEDREFERENCE_VaSOLSimExam read(File file, byte[] key)
			throws IOException, VaSolSimException
	{
		__NONUSEDREFERENCE_VaSOLSimExam exam = null;

		return exam;
	}

	/**
	 * Generates a hash from a password with a given key length using a Secure Hash Algorithm (SHA)
	 *
	 * @param password the password form which the hash will be generated
	 * @param length   the length in bytes of the hash, defaults to SHA-256. Valid values are SHA-1, SHA-256, SHA-512.
	 *
	 * @return hash
	 */
	protected static byte[] generateHash(byte[] password, HashType length)
	{
		String hashAlgorithm;
		switch (length)
		{
			case SHA256:
				hashAlgorithm = "SHA-256";
				break;
			case SHA512:
				hashAlgorithm = "SHA-512";
				break;
			default:
				hashAlgorithm = "SHA-256";
		}

		try
		{
			MessageDigest hash = MessageDigest.getInstance(hashAlgorithm);
			hash.update(password);
			return hash.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			return new byte[]{-1};
		}
	}

	/**
	 * Converts a byte array hash to a character representation for storage and comparison
	 *
	 * @param hash the hash to convert to plain text
	 *
	 * @return plain text representation of the hash value
	 */
	public static String convertHashToString(byte[] hash)
	{
		StringBuilder hashString = new StringBuilder();
		for (int index = 0; index < hash.length; index++)
			hashString.append(Integer.toString((hash[index] & 0xff) + 0x0100, 16).substring(1));

		return hashString.toString();
	}

	/**
	 * Creates a cipher around a 128bit AES encryption initialized to decryption mode.
	 *
	 * @param key the key for the cipher
	 *
	 * @return initialized cipher, null if there is an error. The popup manager is used to notify errors.
	 */
	protected static Cipher getDecryptionCipher(byte[] key) throws VaSolSimException
	{
		byte[] validatedKey = new byte[algorithmExpectedKeyLengthInBytes];

		/*
		 * Holy crap yes I know this is terribly insecure but if you give this function an encryption key smaller
		 * than 128 bits or of a value than the one you have set in teh static fields we have bigger problems to
		 * deal with. Also, its for school, who is actually going to read this but me haha. If you do, email me with
		 * the subject line LAWL YOUR CRYPTO IS REALLY BAD and we can share a laugh;
		 *
		 * Anyways, if the key is too short, repeat the byte sequence (copy it) until the byte array key is long
		 * enough.
		 */
		if (key.length < algorithmExpectedKeyLengthInBytes)
		{
			int invalidatedKeyIndex = 0;
			for (int index = 0; index < algorithmExpectedKeyLengthInBytes; index++)
			{
				validatedKey[index] = key[invalidatedKeyIndex++];

				if (index >= key.length)
					invalidatedKeyIndex = 0;
			}
		}
		else if (key.length > algorithmExpectedKeyLengthInBytes)
		{
			if (!(key.length == 32 || key.length == 64))
				throw new VaSolSimException(
						ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD KEY: HIGHER KEY NOT POT (256, 512)");

			if (key.length == 32)
			{
				byte[] xorKey = new byte[16];
				byte[] lowerHalf = new byte[16];
				byte[] higherHalf = new byte[16];

				System.arraycopy(key, 0, lowerHalf, 0, 16);
				System.arraycopy(key, 16, higherHalf, 0, 16);

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) lowerHalf[index] ^ (int) higherHalf[index];
					xorKey[index] = (byte) (0xff & xorBytes);
				}

				validatedKey = xorKey;
			}
			else
			{
				byte[] lowerQuarterOne = new byte[16];
				byte[] lowerQuarterTwo = new byte[16];
				byte[] higherQuarterOne = new byte[16];
				byte[] higherQuarterTwo = new byte[16];
				byte[] lowerHalf = new byte[16];
				byte[] higherHalf = new byte[16];
				byte[] xorKey = new byte[16];

				System.arraycopy(key, 0, lowerQuarterOne, 0, 16);
				System.arraycopy(key, 16, lowerQuarterTwo, 0, 16);
				System.arraycopy(key, 32, higherQuarterOne, 0, 16);
				System.arraycopy(key, 48, higherQuarterTwo, 0, 16);

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) lowerQuarterOne[index] ^ (int) lowerQuarterTwo[index];
					lowerHalf[index] = (byte) (0xff & xorBytes);
				}

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) higherQuarterOne[index] ^ (int) higherQuarterTwo[index];
					higherHalf[index] = (byte) (0xff & xorBytes);
				}

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) lowerHalf[index] ^ (int) higherHalf[index];
					xorKey[index] = (byte) (0xff & xorBytes);
				}

				validatedKey = xorKey;
			}

		}
		else if (key.length == algorithmExpectedKeyLengthInBytes)
			validatedKey = key;

		Cipher cipher = null;
		try
		{
			System.out.println(validatedKey.length);

			cipher = Cipher.getInstance(serviceProviderInterface, serviceProvider);
			cipher.init(
					Cipher.DECRYPT_MODE,
					new SecretKeySpec(validatedKey, algorithm),
					new IvParameterSpec(IV));
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

		return cipher;
	}

	/**
	 * Creates a cipher around a statically defined DEFAULT_ENCRYPTION_ALGORITHM, AES 128bit by default, initialized to
	 * encryption mode.
	 *
	 * @param key secure hash of some sort of the key
	 *
	 * @return initialized cipher, null if internal exception
	 *
	 * @throws VaSolSimException any internal cryptographic related exceptions throw this for debugging to the user
	 */
	protected static Cipher getEncryptionCipher(byte[] key) throws VaSolSimException
	{
		byte[] validatedKey = new byte[algorithmExpectedKeyLengthInBytes];

		/*
		 * Holy crap yes I know this is terribly insecure but if you give this function an encryption key smaller
		 * than 128 bits or of a value than the one you have set in teh static fields we have bigger problems to
		 * deal with. Also, its for school, who is actually going to read this but me haha. If you do, email me with
		 * the subject line LAWL YOUR CRYPTO IS REALLY BAD and we can share a laugh;
		 *
		 * Anyways, if the key is too short, repeat the byte sequence (copy it) until the byte array key is long
		 * enough.
		 */
		if (key.length < algorithmExpectedKeyLengthInBytes)
		{

			int invalidatedKeyIndex = 0;
			for (int index = 0; index < algorithmExpectedKeyLengthInBytes; index++)
			{
				validatedKey[index] = key[invalidatedKeyIndex++];

				if (index >= key.length)
					invalidatedKeyIndex = 0;
			}
		}
		else if (key.length > algorithmExpectedKeyLengthInBytes)
		{
			if (!(key.length == 32 || key.length == 64))
				throw new VaSolSimException(
						ERROR_MESSAGE_GENERIC_CRYPTO + "\n\nBAD KEY: HIGHER KEY NOT POT (256, 512)");

			if (key.length == 32)
			{
				byte[] xorKey = new byte[16];
				byte[] lowerHalf = new byte[16];
				byte[] higherHalf = new byte[16];

				System.arraycopy(key, 0, lowerHalf, 0, 16);
				System.arraycopy(key, 16, higherHalf, 0, 16);

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) lowerHalf[index] ^ (int) higherHalf[index];
					xorKey[index] = (byte) (0xff & xorBytes);
				}

				validatedKey = xorKey;
			}
			else
			{
				byte[] lowerQuarterOne = new byte[16];
				byte[] lowerQuarterTwo = new byte[16];
				byte[] higherQuarterOne = new byte[16];
				byte[] higherQuarterTwo = new byte[16];
				byte[] lowerHalf = new byte[16];
				byte[] higherHalf = new byte[16];
				byte[] xorKey = new byte[16];

				System.arraycopy(key, 0, lowerQuarterOne, 0, 16);
				System.arraycopy(key, 16, lowerQuarterTwo, 0, 16);
				System.arraycopy(key, 32, higherQuarterOne, 0, 16);
				System.arraycopy(key, 48, higherQuarterTwo, 0, 16);

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) lowerQuarterOne[index] ^ (int) lowerQuarterTwo[index];
					lowerHalf[index] = (byte) (0xff & xorBytes);
				}

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) higherQuarterOne[index] ^ (int) higherQuarterTwo[index];
					higherHalf[index] = (byte) (0xff & xorBytes);
				}

				for (int index = 0; index < 16; index++)
				{
					int xorBytes = (int) lowerHalf[index] ^ (int) higherHalf[index];
					xorKey[index] = (byte) (0xff & xorBytes);
				}

				validatedKey = xorKey;
			}
		}
		else if (key.length == algorithmExpectedKeyLengthInBytes)
			validatedKey = key;

		/*
		 * Initialize the Cipher
		 */
		Cipher cipher = null;
		try
		{
			cipher = Cipher.getInstance(serviceProviderInterface, serviceProvider);
			cipher.init(
					Cipher.ENCRYPT_MODE,
					new SecretKeySpec(validatedKey, algorithm),
					new IvParameterSpec(IV));
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

		return cipher;
	}
}
