package com.vasolsim.common.file;

import com.vasolsim.common.VaSolSimException;

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
public class Exam
{
	private final boolean initializedFromFile;

	private boolean reportingStats           = false;
	private boolean reportingStatsStandalone = false;
	private String  statsSenderEmail         = null;
	private String  statsSenderPassword      = null;
	private String  statsSenderSMTPAddress   = null;
	private int     statsSenderSMTPPort      = 587;
	private String  statsDestinationEmail    = null;

	private String testName   = NO_TEST_NAME_GIVEN;
	private String authorName = NO_AUTHOR_NAME_GIVEN;
	private String schoolName = NO_SCHOOL_NAME_GIVEN;
	private String periodName = NO_PERIOD_ID_GIVEN;
	private String date       = NO_DATE_GIVEN;

	private ArrayList<QuestionSet> questionSets = new ArrayList<QuestionSet>();

	public Exam(final boolean initializedFromFile)
	{
		this.initializedFromFile = initializedFromFile;
	}

	protected Exam(boolean reportingStats,
	               boolean reportingStatsStandalone,
	               final String statsDestinationEmail,
	               final String statsSenderEmail,
	               final String statsSenderPassword,
	               final String statsSenderSMTPAddress,
	               final int statsSenderSMTPPort,
	               final boolean initializedFromFile)
	{
		this.reportingStats = reportingStats;
		this.reportingStatsStandalone = reportingStatsStandalone;
		this.statsDestinationEmail = statsDestinationEmail;
		this.statsSenderEmail = statsSenderEmail;
		this.statsSenderPassword = statsSenderPassword;
		this.statsSenderSMTPAddress = statsSenderSMTPAddress;
		this.statsSenderSMTPPort = statsSenderSMTPPort;
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
			transport.connect(new String(statsSenderSMTPAddress),
			                  statsSenderSMTPPort,
			                  new String(statsSenderEmail),
			                  new String(statsSenderPassword));

			Message emailMessage = new MimeMessage(session);
			emailMessage.setFrom(new InternetAddress(new String(statsSenderEmail)));
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

	public boolean isInitializedFromFile()
	{
		return initializedFromFile;
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

	public String getStatsSenderEmail()
	{
		return statsSenderEmail;
	}

	public void setStatsSenderEmail(String statsSenderEmail)
	{
		this.statsSenderEmail = statsSenderEmail;
	}

	public String getStatsSenderPassword()
	{
		if (!initializedFromFile)
			return statsSenderPassword;

		return null;
	}

	public void setStatsSenderPassword(String statsSenderPassword)
	{
		this.statsSenderPassword = statsSenderPassword;
	}

	public String getStatsSenderSMTPAddress()
	{
		return statsSenderSMTPAddress;
	}

	public void setStatsSenderSMTPAddress(String statsSenderSMTPAddress)
	{
		this.statsSenderSMTPAddress = statsSenderSMTPAddress;
	}

	public int getStatsSenderSMTPPort()
	{
		return statsSenderSMTPPort;
	}

	public void setStatsSenderSMTPPort(int statsSenderSMTPPort)
	{
		this.statsSenderSMTPPort = statsSenderSMTPPort;
	}

	public String getStatsDestinationEmail()
	{
		return statsDestinationEmail;
	}

	public void setStatsDestinationEmail(String statsDestinationEmail)
	{
		this.statsDestinationEmail = statsDestinationEmail;
	}

	public String getTestName()
	{
		return testName;
	}

	public void setTestName(String testName)
	{
		this.testName = testName;
	}

	public String getAuthorName()
	{
		return authorName;
	}

	public void setAuthorName(String authorName)
	{
		this.authorName = authorName;
	}

	public String getSchoolName()
	{
		return schoolName;
	}

	public void setSchoolName(String schoolName)
	{
		this.schoolName = schoolName;
	}

	public String getPeriodName()
	{
		return periodName;
	}

	public void setPeriodName(String periodName)
	{
		this.periodName = periodName;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	public ArrayList<QuestionSet> getQuestionSets()
	{
		return questionSets;
	}

	public void setQuestionSets(ArrayList<QuestionSet> questionSets)
	{
		this.questionSets = questionSets;
	}
}
