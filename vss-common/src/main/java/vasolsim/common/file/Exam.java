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
import main.java.vasolsim.common.VaSolSimException;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Properties;

import static main.java.vasolsim.common.GenericUtils.*;

/**
 * @author willstuckey
 * @date 7/14/14
 * <p>This class represents an exam and its supporting data.</p>
 */
public class Exam
{
	private final boolean isLocked;
	private boolean reportingStats           = false;
	private boolean reportingStatsStandalone = false;
	private boolean scrambleQuestionSetOrder = false;

	@Nonnull
	private String testName   = NO_TEST_NAME_GIVEN;
	@Nonnull
	private String authorName = NO_AUTHOR_NAME_GIVEN;
	@Nonnull
	private String schoolName = NO_SCHOOL_NAME_GIVEN;
	@Nonnull
	private String periodName = NO_PERIOD_ID_GIVEN;
	@Nonnull
	private String date       = NO_DATE_GIVEN;

	private int     statsSenderSMTPPort      = 587;
	@Nullable
	private String  statsDestinationEmail    = null;
	@Nullable
	private String  statsSenderEmail         = null;
	@Nullable
	private String  statsSenderPassword      = null;
	@Nullable
	private String  statsSenderSMTPAddress   = null;

	@Nonnull
	private ArrayList<QuestionSet> questionSets = new ArrayList<QuestionSet>();

	/**
	 * Default constructor. Initializes an unlocked exam. Exams must be locked before they will be accepted by the
	 * viewer.
	 */
	public Exam()
	{
		this.isLocked = false;
	}

	/**
	 * Advanced constructor. Initializes an unlocked exam. Exams must be locked before they will be accepted by the
	 * viewer.
	 * @param reportingStats if the exam will report statistics upon completion
	 * @param reportingStatsStandalone if the exam is reporting statistics, will it do so standalone
	 * @param statsDestinationEmail the email destination of the statistics. After statistics are compiled, they will be
	 *                              bundled and sent to an email address to be analyzed later. This is the destination
	 *                              of this data.
	 * @param statsSenderEmail if reporting standalone, the email account that will send the statistics to the
	 *                         destination. Can be the same as the destination. DO NOT USE A PERSONAL/PROFESSIONAL EMAIL
	 * @param statsSenderPassword if reporting standalone, the password to the email account that will send the
	 *                               statistics. DO NOT USE A PERSONAL/PROFESSIONAL EMAIL
	 * @param statsSenderSMTPAddress the SMTP server address of the sender email account
	 * @param statsSenderSMTPPort the port of the SMTP server of the sender email account
	 */
	protected Exam(boolean reportingStats,
	               boolean reportingStatsStandalone,
	               @Nullable String statsDestinationEmail,
	               @Nullable String statsSenderEmail,
	               @Nullable String statsSenderPassword,
	               @Nullable String statsSenderSMTPAddress,
	               int statsSenderSMTPPort)
	{
		this.reportingStats = reportingStats;
		this.reportingStatsStandalone = reportingStatsStandalone;
		this.statsDestinationEmail = statsDestinationEmail;
		this.statsSenderEmail = statsSenderEmail;
		this.statsSenderPassword = statsSenderPassword;
		this.statsSenderSMTPAddress = statsSenderSMTPAddress;
		this.statsSenderSMTPPort = statsSenderSMTPPort;
		this.isLocked = false;
	}

	/**
	 * Advanced constructor. Initializes an exam. Exams must be locked before they will be accepted by the
	 * viewer.
	 * @param reportingStats if the exam will report statistics upon completion
	 * @param reportingStatsStandalone if the exam is reporting statistics, will it do so standalone
	 * @param statsDestinationEmail the email destination of the statistics. After statistics are compiled, they will be
	 *                              bundled and sent to an email address to be analyzed later. This is the destination
	 *                              of this data.
	 * @param statsSenderEmail if reporting standalone, the email account that will send the statistics to the
	 *                         destination. Can be the same as the destination. DO NOT USE A PERSONAL/PROFESSIONAL EMAIL
	 * @param statsSenderPassword if reporting standalone, the password to the email account that will send the
	 *                               statistics. DO NOT USE A PERSONAL/PROFESSIONAL EMAIL
	 * @param statsSenderSMTPAddress the SMTP server address of the sender email account
	 * @param statsSenderSMTPPort the port of the SMTP server of the sender email account
	 * @param isLocked if the exam will be locked
	 */
	Exam(boolean reportingStats,
	     boolean reportingStatsStandalone,
	     @Nullable String statsDestinationEmail,
	     @Nullable String statsSenderEmail,
	     @Nullable String statsSenderPassword,
	     @Nullable String statsSenderSMTPAddress,
	     final int statsSenderSMTPPort,
	     boolean isLocked)
	{
		this.reportingStats = reportingStats;
		this.reportingStatsStandalone = reportingStatsStandalone;
		this.statsDestinationEmail = statsDestinationEmail;
		this.statsSenderEmail = statsSenderEmail;
		this.statsSenderPassword = statsSenderPassword;
		this.statsSenderSMTPAddress = statsSenderSMTPAddress;
		this.statsSenderSMTPPort = statsSenderSMTPPort;
		this.isLocked = isLocked;
	}

	/**
	 * Advanced constructor. Initializes an unlocked exam. Exams must be locked before they will be accepted by the
	 * viewer.
	 * @param exam an exam to clone. The clone will be unlocked and any locked data will be lost and replaced by the
	 *             default internal values.
	 */
	protected Exam(Exam exam)
	{
		this(exam, false);
	}

	/**
	 * Advanced constructor. Initializes an exam. Exams must be locked before they will be accepted by the viewer.
	 * @param exam an exam to clone. The clone will be unlocked and any locked data will be lost and replaced by the
	 *             default internal values.
	 * @param isLocked if the exam will be locked.
	 */
	Exam(Exam exam,
	     boolean isLocked)
	{

		/*
		 * copy creation stamp
		 */
		this.authorName = exam.getAuthorName();
		this.testName = exam.getAuthorName();
		this.schoolName = exam.getSchoolName();
		this.periodName = exam.getPeriodName();
		this.date = exam.getDate();

		/*
		 * copy stats info
		 */
		this.reportingStats = exam.isReportingStats();
		this.reportingStatsStandalone = exam.isReportingStatsStandalone();
		this.statsDestinationEmail = exam.getStatsDestinationEmail();
		this.statsSenderEmail = exam.getStatsSenderEmail();
		this.statsSenderPassword = exam.privledgedGetStatsSenderPassword();
		this.statsSenderSMTPAddress = exam.getStatsSenderSMTPAddress();
		this.statsSenderSMTPPort = exam.getStatsSenderSMTPPort();

		/*
		 * copy data
		 */
		this.scrambleQuestionSetOrder = exam.scrambleQuestionSetOrder;
		this.questionSets = exam.getQuestionSets();

		/*
		 * set lock
		 */
		this.isLocked = isLocked;
	}

	/**
	 * IMPLEMENTATION NOT FINAL
	 * @param title title
	 * @param body body
	 * @param usingStartTLS tls
	 * @return success
	 * @throws VaSolSimException
	 */
	public boolean sendEmail(@Nonnull String title,
	                         @Nonnull String body,
	                         boolean usingStartTLS) throws VaSolSimException
	{
		return sendEmail(statsDestinationEmail, title, body, usingStartTLS);
	}

	/**
	 * IMPLEMENTATION NOT FINAL
	 * @param destination destination
	 * @param title title
	 * @param body body
	 * @param usingStartTLS tls
	 * @return success
	 * @throws VaSolSimException
	 */
	public boolean sendEmail(@Nonnull String destination,
	                         @Nonnull String title,
	                         @Nonnull String body,
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
			transport.connect(statsSenderSMTPAddress,
			                  statsSenderSMTPPort,
			                  statsSenderEmail,
			                  statsSenderPassword);

			Message emailMessage = new MimeMessage(session);
			emailMessage.setFrom(new InternetAddress(statsSenderEmail));
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
		smtpProperties.put("mail.smtp.host", statsSenderSMTPAddress);
		smtpProperties.put("mail.smtp.port", Integer.toString(statsSenderSMTPPort));

		Session smtpSession = Session.getInstance(smtpProperties,
				new Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(
								new String(statsSenderEmail),
								new String(statsSenderPassword));
					}
				});

		try
		{
			Message emailMessage = new MimeMessage(smtpSession);
			emailMessage.setFrom(new InternetAddress(new String(statsSenderEmail)));
			emailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destination));
			emailMessage.setSubject(title);
			emailMessage.setOverlay(body);

			Transport.send(emailMessage);
		}
		catch (MessagingException e)
		{
			throw new VaSolSimException("EMAIL ERROR", e);
		}
		*/

		return true;
	}

	/**
	 * gets if the exam is locked
	 * @return if is locked
	 */
	public final boolean isLocked()
	{
		return isLocked;
	}

	/**
	 * gets if the exam is reporting statistics
	 * @return if is reporting
	 */
	public boolean isReportingStats()
	{
		return reportingStats;
	}

	/**
	 * sets if the exam is reporting statistics, only if the exam is unlocked
	 * @param reportingStats if is reporting
	 */
	public final void setReportingStats(boolean reportingStats)
	{
		if (!isLocked)
			this.reportingStats = reportingStats;
	}

	/**
	 * gets if the exam is reporting statistics standalone
	 * @return if is reporting standalone
	 */
	public boolean isReportingStatsStandalone()
	{
		return reportingStatsStandalone;
	}

	/**
	 * sets if the exam is reporting statistics standalone, only if the exam is unlocked
	 * @param reportingStatsStandalone if is reporting standalone
	 */
	public final void setReportingStatsStandalone(boolean reportingStatsStandalone)
	{
		if (!isLocked)
			this.reportingStatsStandalone = reportingStatsStandalone;
	}

	/**
	 * gets the destination email
	 * @return the email address
	 */
	@Nullable
	public String getStatsDestinationEmail()
	{
		return statsDestinationEmail;
	}

	/**
	 * sets the destination for sending statistics, only if the exam is unlocked
	 * @param statsDestinationEmail destination email address
	 * @return false if the exam is locked or the email doesn't pass the regex
	 */
	public final boolean setStatsDestinationEmail(@Nullable String statsDestinationEmail)
	{
		if (!isLocked && isValidEmail(statsDestinationEmail))
		{
			this.statsDestinationEmail = statsDestinationEmail;
			return true;
		}

		return false;
	}

	/**
	 * gets the stats sender email for use with standalone
	 * @return the email address
	 */
	@Nullable
	public String getStatsSenderEmail()
	{
		return statsSenderEmail;
	}

	/**
	 * sets the stats sender email, only if the exam is unlocked
	 * @param statsSenderEmail the email
	 */
	public final void setStatsSenderEmail(@Nullable String statsSenderEmail)
	{
		if (!isLocked)
			this.statsSenderEmail = statsSenderEmail;
	}

	/**
	 * gets the password for sending statistics, only if the exam is unlocked
	 * @return the password
	 */
	@Nullable
	public final String getStatsSenderPassword()
	{
		if (!isLocked)
			return statsSenderPassword;

		return null;
	}

	/**
	 * gets the password for sending statistics
	 * @return the password
	 */
	@Nullable
	final String privledgedGetStatsSenderPassword()
	{
		return statsSenderPassword;
	}

	/**
	 * sets the password for sending statistics, only if the exam is unlocked
	 * @param statsSenderPassword the password
	 */
	public final void setStatsSenderPassword(@Nullable String statsSenderPassword)
	{
		if (!isLocked)
			this.statsSenderPassword = statsSenderPassword;
	}

	/**
	 * gets the SMTP address for sending statistics
	 * @return the address
	 */
	@Nullable
	public String getStatsSenderSMTPAddress()
	{
		return statsSenderSMTPAddress;
	}

	/**
	 * sets the SMTP address for sending statistics
	 * @param statsSenderSMTPAddress the address
	 */
	public void setStatsSenderSMTPAddress(@Nullable String statsSenderSMTPAddress)
	{
		this.statsSenderSMTPAddress = statsSenderSMTPAddress;
	}

	/**
	 * gets the SMTP port
	 * @return the port
	 */
	public int getStatsSenderSMTPPort()
	{
		return statsSenderSMTPPort;
	}

	/**
	 * sets the SMTP port
	 * @param statsSenderSMTPPort the port
	 */
	public void setStatsSenderSMTPPort(int statsSenderSMTPPort)
	{
		this.statsSenderSMTPPort = statsSenderSMTPPort;
	}

	/**
	 * gets the name of the test
	 * @return test name
	 */
	@Nonnull
	public String getTestName()
	{
		return testName;
	}

	/**
	 * sets the name of the test, only if the exam is unlocked
	 * @param testName test name
	 */
	public final void setTestName(@Nonnull String testName)
	{
		if (!isLocked)
			this.testName = testName;
	}

	/**
	 * gets the author's name
	 * @return author name
	 */
	@Nonnull
	public String getAuthorName()
	{
		return authorName;
	}

	/**
	 * sets the author's name, only if the exam is unlocked
	 * @param authorName author name
	 */
	public final void setAuthorName(@Nonnull String authorName)
	{
		if (!isLocked)
			this.authorName = authorName;
	}

	/**
	 * gets the school name
	 * @return school name
	 */
	@Nonnull
	public String getSchoolName()
	{
		return schoolName;
	}

	/**
	 * sets the school name, only if the exam is unlocked
	 * @param schoolName school name
	 */
	public final void setSchoolName(@Nonnull String schoolName)
	{
		if (!isLocked)
			this.schoolName = schoolName;
	}

	/**
	 * gets the period name
	 * @return period name
	 */
	@Nonnull
	public String getPeriodName()
	{
		return periodName;
	}

	/**
	 * sets the period name, only if the exam is unlocked
	 * @param periodName period name
	 */
	public final void setPeriodName(@Nonnull String periodName)
	{
		if (!isLocked)
			this.periodName = periodName;
	}

	/**
	 * gets the date
	 * @return date
	 */
	@Nonnull
	public String getDate()
	{
		return date;
	}

	/**
	 * sets the date, only if the exam is unlocked
	 * @param date date
	 */
	public void setDate(@Nonnull String date)
	{
		if (!isLocked)
			this.date = date;
	}

	/**
	 * gets the question sets
	 * @return question sets
	 */
	@Nonnull
	public ArrayList<QuestionSet> getQuestionSets()
	{
		return questionSets;
	}

	/**
	 * sets the question sets, only if the exam is unlocked
	 * @param questionSets question sets
	 */
	public final void setQuestionSets(@Nonnull ArrayList<QuestionSet> questionSets)
	{
		if (!isLocked)
			this.questionSets = questionSets;
	}

	/**
	 * determines if the order of the question sets will be randomized when presented
	 * @return if the sets' order will be scrambled
	 */
	public boolean getScrambleQuestionSetOrder()
	{
		return scrambleQuestionSetOrder;
	}

	/**
	 * determines if the order of the question sets will be randomized when presented
	 * @param scrambleQuestionSetOrder if the sets' order will be scrambled
	 */
	public void setScrambleQuestionSetOrder(boolean scrambleQuestionSetOrder)
	{
		if (!isLocked)
			this.scrambleQuestionSetOrder = scrambleQuestionSetOrder;
	}
}
