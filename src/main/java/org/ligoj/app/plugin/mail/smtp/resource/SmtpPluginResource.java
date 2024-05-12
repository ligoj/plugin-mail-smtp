package org.ligoj.app.plugin.mail.smtp.resource;

import org.apache.commons.lang3.NotImplementedException;
import org.ligoj.app.plugin.mail.resource.MailResource;
import org.ligoj.app.plugin.mail.resource.MailServicePlugin;
import org.ligoj.app.resource.plugin.AbstractToolPluginResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Properties;

/**
 * Features of mail implementations.
 */
@Component
public class SmtpPluginResource extends AbstractToolPluginResource implements MailServicePlugin {

	/**
	 * Plug-in key.
	 */
	public static final String URL = MailResource.SERVICE_URL + "/smtp";

	/**
	 * Plug-in key.
	 */
	public static final String KEY = URL.replace('/', ':').substring(1);

	/**
	 * JavaMail username.
	 */
	public static final String PARAMETER_USER = KEY + ":username";

	/**
	 * JavaMail password.
	 */
	public static final String PARAMETER_PASSWORD = KEY + ":password";

	/**
	 * JavaMail hostname.
	 */
	public static final String PARAMETER_HOST = KEY + ":host";

	/**
	 * JavaMail port.
	 */
	public static final String PARAMETER_PORT = KEY + ":port";

	@Autowired
	protected ApplicationContext applicationContext;
	
	@Override
	public String getKey() {
		return KEY;
	}

	/**
	 * Return the {@link JavaMailSender} built from the given node.
	 * 
	 * @param node
	 *            The node holding the SMTP configuration.
	 * @return the {@link JavaMailSender} built from the given node.
	 */
	public JavaMailSender getMailSender(final String node) {
		final var mail = new JavaMailSenderImpl();
		final var parameters = pvResource.getNodeParameters(node);
		mail.setUsername(parameters.get(PARAMETER_USER));
		mail.setPassword(parameters.get(PARAMETER_PASSWORD));
		mail.setHost(parameters.get(PARAMETER_HOST));
		mail.setPort(Optional.ofNullable(parameters.get(PARAMETER_PORT)).map(Integer::valueOf).orElse(25));
		mail.setDefaultEncoding("UTF-8");

		final var properties = new Properties();
		properties.put("mail.smtp.auth", Boolean.TRUE);
		properties.put("mail.smtp.starttls.enable", Boolean.TRUE);
		properties.put("mail.smtp.quitwait", Boolean.FALSE);
		properties.put("mail.smtp.socketFactory.fallback", Boolean.FALSE);
		properties.put("mail.smtp.connectiontimeout",configuration.get("plugin:mail:smtp:timeout", 3000));

		mail.setJavaMailProperties(properties);
		return mail;
	}

	@Override
	public MimeMessagePreparator send(final String node, final MimeMessagePreparator messagePreparator) {
		applicationContext.getBean(SmtpPluginResource.class).getMailSender(node).send(messagePreparator);
		return messagePreparator;
	}

	@Override
	public void link(int subscription) {
		throw new NotImplementedException("");
	}

}
