package org.ligoj.app.plugin.mail.resource;

import org.ligoj.app.api.ServicePlugin;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * Features of mail implementations.
 */
public interface MailServicePlugin extends ServicePlugin {

	/**
	 * Send a mail using JavaMail.
	 * 
	 * @param node
	 *            Target node.
	 * @param messagePreparator
	 *            The message configuration.
	 * @return Original message. only there for cascaded messaging and test.
	 */
	MimeMessagePreparator send(String node, MimeMessagePreparator messagePreparator);

}
