package com.vasolsim.common.file;

import com.vasolsim.common.VaSolSimException;

import javax.crypto.Cipher;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

import static com.vasolsim.common.GenericUtils.*;

/**
 * @author guyfleeman
 * @date 7/14/14 <p></p>
 */
public class __NONUSEDREFERENCE_OldExam
{
	private final boolean initializedFromFile;

	private byte[] encryptedValidationHash = new byte[]{};
	private byte[] parametricIV            = new byte[16];
	private Cipher encryptionCipher        = null;
	private Cipher decryptionCipher        = null;

	private boolean reportingStats                    = false;
	private boolean reportingStatsStandalone          = false;
	private byte[]  decryptedStatsSenderEmail         = new byte[]{};
	private byte[]  encryptedStatsSenderEmail         = new byte[]{};
	private byte[]  decryptedStatsSenderEmailPassword = new byte[]{};
	private byte[]  encryptedStatsSenderEmailPassword = new byte[]{};
	private byte[]  decryptedStatsSenderSMTPAddress   = new byte[]{};
	private byte[]  encryptedStatsSenderSMTPAddress   = new byte[]{};
	private byte[]  encryptedStatsSenderSMTPPort      = new byte[]{};
	private int     decryptedStatsSenderSMTPPort      = 587;
	private String  statsDestinationEmail             = null;

	private String testName   = NO_TEST_NAME_GIVEN;
	private String authorName = NO_AUTHOR_NAME_GIVEN;
	private String schoolName = NO_SCHOOL_NAME_GIVEN;
	private String periodName = NO_PERIOD_ID_GIVEN;
	private String date       = NO_DATE_GIVEN;

	private ArrayList<QuestionSet> questionSets = new ArrayList<QuestionSet>();

	protected __NONUSEDREFERENCE_OldExam(final byte[] encryptedValidationHash,
	                                     final byte[] parametricIV,
	                                     final Cipher encryptionCipher,
	                                     final Cipher decryptionCipher,
	                                     final String statsDestinationEmail,
	                                     boolean reportingStats,
	                                     boolean reportingStatsStandalone,
	                                     final byte[] encryptedStatsSenderEmail,
	                                     final byte[] encryptedStatsSenderEmailPassword,
	                                     final byte[] encryptedStatsSenderSMTPAddress,
	                                     final byte[] encryptedStatsSenderSMTPPort,
	                                     final boolean initializedFromFile)
	{
		this.encryptedValidationHash = encryptedValidationHash;
		this.parametricIV = parametricIV;
		this.encryptionCipher = encryptionCipher;
		this.decryptionCipher = decryptionCipher;

		this.reportingStats = reportingStats;
		this.reportingStatsStandalone = reportingStatsStandalone;
		this.statsDestinationEmail = statsDestinationEmail;
		this.encryptedStatsSenderEmail = encryptedStatsSenderEmail;
		this.encryptedStatsSenderEmailPassword = encryptedStatsSenderEmailPassword;
		this.encryptedStatsSenderSMTPAddress = encryptedStatsSenderSMTPAddress;
		this.encryptedStatsSenderSMTPPort = encryptedStatsSenderSMTPPort;

		this.decryptedStatsSenderEmail = applyCryptographicCipher(
				this.encryptedStatsSenderEmail, this.decryptionCipher);
		this.decryptedStatsSenderEmailPassword = applyCryptographicCipher(
				this.encryptedStatsSenderEmailPassword, this.decryptionCipher);
		this.decryptedStatsSenderSMTPAddress = applyCryptographicCipher(
				this.encryptedStatsSenderSMTPAddress, this.decryptionCipher);
		this.decryptedStatsSenderSMTPPort = Integer.parseInt(new String(applyCryptographicCipher(
				this.encryptedStatsSenderSMTPPort, this.decryptionCipher)));

		this.initializedFromFile = initializedFromFile;
	}

	public boolean sendEmail(String title,
	                         String body,
	                         boolean usingStartTLS) throws VaSolSimException
	{
		return sendEmail(statsDestinationEmail, title, body, usingStartTLS);
	}

	public boolean sendEmail(String destination,
	                         String title,
	                         String body,
	                         boolean usingStartTLS) throws VaSolSimException
	{
		Properties smtpProperties = new Properties();

		if (usingStartTLS)
			smtpProperties.put("mail.smtp.starttls.enable", "true");

		smtpProperties.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(smtpProperties, null);
		try
		{
			Transport transport = session.getTransport("smtp");
			transport.connect(new String(decryptedStatsSenderSMTPAddress),
			                  decryptedStatsSenderSMTPPort,
			                  new String(decryptedStatsSenderEmail),
			                  new String(decryptedStatsSenderEmailPassword));

			Message emailMessage = new MimeMessage(session);
			emailMessage.setFrom(new InternetAddress(new String(decryptedStatsSenderEmail)));
			emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination));
			emailMessage.setSubject(title);
			emailMessage.setText(body);

			transport.sendMessage(emailMessage, new Address[]{new InternetAddress(statsDestinationEmail)});
		}
		catch (MessagingException e)
		{
			throw new VaSolSimException("EMAIL ERROR", e);
		}


		/*
		smtpProperties.put("mail.smtp.host", decryptedStatsSenderSMTPAddress);
		smtpProperties.put("mail.smtp.port", Integer.toString(decryptedStatsSenderSMTPPort));

		Session smtpSession = Session.getInstance(smtpProperties,
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								new String(decryptedStatsSenderEmail),
								new String(decryptedStatsSenderEmailPassword));
					}
				});

		try
		{
			Message emailMessage = new MimeMessage(smtpSession);
			emailMessage.setFrom(new InternetAddress(new String(decryptedStatsSenderEmail)));
			emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination));
			emailMessage.setSubject(title);
			emailMessage.setText(body);

			Transport.send(emailMessage);
		}
		catch (MessagingException e)
		{
			throw new VaSolSimException("EMAIL ERROR", e);
		}
		*/

		return true;
	}

	public byte[] getEncryptedValidationHash()
	{
		return encryptedValidationHash;
	}

	public byte[] getParametricIV()
	{
		return parametricIV;
	}

	public Cipher getEncryptionCipher()
	{
		return encryptionCipher;
	}

	public final Cipher getDecryptionCipher()
	{
		if (!initializedFromFile)
			return decryptionCipher;

		return null;
	}

	public boolean isReportingStats()
	{
		return reportingStats;
	}

	public boolean isReportingStatsStandalone()
	{
		return reportingStatsStandalone;
	}

	public byte[] getEncryptedStatsSenderEmail()
	{
		return encryptedStatsSenderEmail;
	}

	public byte[] getEncryptedStatsSenderEmailPassword()
	{
		return encryptedStatsSenderEmailPassword;
	}

	public byte[] getDecryptedStatsSenderSMTPAddress()
	{
		return decryptedStatsSenderSMTPAddress;
	}

	public byte[] getEncryptedStatsSenderSMTPAddress()
	{
		return encryptedStatsSenderSMTPAddress;
	}

	public byte[] getEncryptedStatsSenderSMTPPort()
	{
		return encryptedStatsSenderSMTPPort;
	}

	public int getDecryptedStatsSenderSMTPPort()
	{
		return decryptedStatsSenderSMTPPort;
	}

	public String getStatsDestinationEmail()
	{
		return statsDestinationEmail;
	}

	public ArrayList<QuestionSet> getQuestionSets()
	{
		return questionSets;
	}

	public final void setQuestionSets(ArrayList<QuestionSet> questionSets)
	{
		if (!initializedFromFile)
			this.questionSets = questionSets;
	}

	public String getTestName()
	{
		return testName;
	}

	public final void setTestName(String testName)
	{
		if (!initializedFromFile)
			this.testName = testName;
	}

	public String getAuthorName()
	{
		return authorName;
	}

	public final void setAuthorName(String authorName)
	{
		if (!initializedFromFile)
			this.authorName = authorName;
	}

	public String getSchoolName()
	{
		return schoolName;
	}

	public final void setSchoolName(String schoolName)
	{
		if (!initializedFromFile)
			this.schoolName = schoolName;
	}

	public String getPeriodName()
	{
		return periodName;
	}

	public final void setPeriodName(String periodName)
	{
		if (!initializedFromFile)
			this.periodName = periodName;
	}

	public String getDate()
	{
		return date;
	}

	public final void setDate(String date)
	{
		if (!initializedFromFile)
			this.date = date;
	}
}
