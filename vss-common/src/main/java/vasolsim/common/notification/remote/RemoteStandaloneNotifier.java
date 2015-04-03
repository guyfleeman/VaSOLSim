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

package main.java.vasolsim.common.notification.remote;

import main.java.vasolsim.common.GenericUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

/**
 * @author willstuckey
 * @date 3/3/15 <p>Notifies the project maintainers through SMTP.</p>
 */
public class RemoteStandaloneNotifier implements RemoteNotifier
{
	/**
	 * last error message
	 */
	public static String lastErrorMessage;

	protected static RemoteStandaloneNotifier instance;

	protected static final String defaultUser   = "guyfleeman@gmail.com";   //mandrill username
	protected static final String defaultApiKey = "L963HdUaROda8vCtHi64Lg"; //mandrill API key
	protected static final String defaultTo     = "guyfleeman@gmail.com";
	protected static final String defaultFrom   = "vss@guyfleeman.github.com";
	/**
	 * default smtp address
	 */
	protected static final String defaultHost   = "smtp.mandrillapp.com";
	/**
	 * default smtp port
	 */
	protected static final String defaultPort   = "587";

	/*
	 * instance vars for custom configurations
	 */
	protected String user;
	protected String apiKey;
	protected String to;
	protected String from;
	protected String host;
	protected String port;

	/**
	 * Constructor protected for simplicity in instantiation. If you need direct access to this constructor in order to
	 * create more than one notification instance, extend this class.
	 *
	 * @param user   smtp username
	 * @param apiKey smtp API key (or password but please dont)
	 * @param to     email address for notification recipient
	 * @param from   email address for sender
	 * @param host   smtp server host
	 * @param port   smtp server port
	 */
	protected RemoteStandaloneNotifier(String user, String apiKey, String to, String from, String host, String port)
	{
		this.user = user;
		this.apiKey = apiKey;
		this.to = to;
		this.from = from;
		this.host = host;
		this.port = port;
	}

	/*
	 * create a default instnace
	 */
	static
	{
		createDefaultInstance();
	}

	/**
	 * returns the created notifier, default if custom instance not created
	 *
	 * @return current notification instance
	 */
	public static RemoteStandaloneNotifier getInstance()
	{
		return instance;
	}

	/**
	 * initializes the notification instance to the default configuration. This is called internally and does not need
	 * to be called again by the user, unless he or she wants to remove and replace a custom instance with the default
	 * instance
	 */
	public static void createDefaultInstance()
	{
		createCustomInstance(defaultUser,
		                     defaultApiKey,
		                     defaultTo,
		                     defaultFrom,
		                     defaultHost,
		                     defaultPort);
	}

	/**
	 * initializes the notification instance to a custom configuration.
	 * @param user
	 * @param apiKey
	 * @param to
	 * @param from
	 * @param host
	 * @param port
	 */
	public static void createCustomInstance(String user,
	                                        String apiKey,
	                                        String to,
	                                        String from,
	                                        String host,
	                                        String port)
	{
		RemoteStandaloneNotifier.instance = new RemoteStandaloneNotifier(user, apiKey, to, from, host, port);
	}

	/**
	 * sends a remote notification
	 * @param subject the notification subject
	 * @param body the notification body
	 * @return success
	 */
	public boolean sendRemoteNotification(String subject, String body)
	{
		return sendRemoteNotification(subject, body, null);
	}

	/**
	 * sends a remote notification
	 * @param subject the notification subject
	 * @param body the notification body
	 * @param attachments attachments
	 * @return success
	 */
	public boolean sendRemoteNotification(String subject, String body, File[] attachments)
	{
		Properties properties = System.getProperties();
		properties.setProperty("mail.transport.protocol", "smtp");
		properties.setProperty("mail.smtp.host", this.host);
		properties.setProperty("mail.smtp.port", String.valueOf(this.port));
		properties.setProperty("mail.smtp.user", this.user);

		final Session session = Session.getInstance(properties, null);
		session.setPasswordAuthentication(
				new URLName("smtp", this.host, -1, null, this.user, null),
				new PasswordAuthentication(this.user, this.apiKey));

		try
		{
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.to));

			message.setSubject(subject);
			message.setText(body);

			Transport.send(message);
		}
		catch (MessagingException e)
		{
			lastErrorMessage = GenericUtils.exceptionToString(e);
			return false;
		}

		return true;
	}
}
