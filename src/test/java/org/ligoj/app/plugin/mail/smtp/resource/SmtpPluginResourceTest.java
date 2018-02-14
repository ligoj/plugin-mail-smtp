package org.ligoj.app.plugin.mail.smtp.resource;

import java.io.IOException;
import java.util.Collections;

import javax.transaction.Transactional;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ligoj.app.AbstractAppTest;
import org.ligoj.app.model.Node;
import org.ligoj.app.model.Parameter;
import org.ligoj.app.model.ParameterValue;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.sf.ehcache.CacheManager;

/**
 * Test of {@link SmtpPluginResource}
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/application-context-test.xml")
@Rollback
@Transactional
public class SmtpPluginResourceTest extends AbstractAppTest {

	@Autowired
	private SmtpPluginResource resource;

	@BeforeEach
	public void prepareConfiguration() throws IOException {
		persistEntities("csv", Node.class, Parameter.class, ParameterValue.class);
	}

	@BeforeEach
	@AfterEach
	public void cleanCache() {
		CacheManager.getInstance().getCache("plugin-data").removeAll();
	}

	@Test
	public void getKey() {
		Assertions.assertEquals("service:mail:smtp", resource.getKey());
	}

	@Test
	public void getMailSender() {
		final JavaMailSenderImpl mailSender = (JavaMailSenderImpl) resource.getMailSender("service:mail:smtp:local");
		Assertions.assertEquals("localhost", mailSender.getHost());
		Assertions.assertEquals("user", mailSender.getUsername());
		Assertions.assertEquals("secret", mailSender.getPassword());
		Assertions.assertEquals(25, mailSender.getPort());
		Assertions.assertEquals("UTF-8", mailSender.getDefaultEncoding());
	}

	@Test
	public void send() {
		final SmtpPluginResource resource = new SmtpPluginResource();
		applicationContext.getAutowireCapableBeanFactory().autowireBean(resource);
		resource.applicationContext = Mockito.mock(ApplicationContext.class);
		final SmtpPluginResource mockPLugin = Mockito.mock(SmtpPluginResource.class);
		Mockito.when(mockPLugin.getMailSender("service:mail:smtp:local")).thenReturn(Mockito.mock(JavaMailSenderImpl.class));
		Mockito.when(resource.applicationContext.getBean(SmtpPluginResource.class)).thenReturn(mockPLugin);
		final MimeMessagePreparator messagePreparator = Mockito.mock(MimeMessagePreparator.class);
		Assertions.assertEquals(messagePreparator, resource.send("service:mail:smtp:local", messagePreparator));
	}

	@Test
	public void link() {
		Assertions.assertThrows(NotImplementedException.class, () -> {
			resource.link(0);
		});
	}

	@Test
	public void getVersion() throws Exception {
		Assertions.assertNull(resource.getVersion(Collections.emptyMap()));
	}

	@Test
	public void getLastVersion() throws Exception {
		Assertions.assertNull(resource.getLastVersion());
	}

	@Test
	public void checkStatus() throws Exception {
		Assertions.assertTrue(resource.checkStatus(null, null));
	}

	@Test
	public void checkSubscriptionStatus() throws Exception {
		Assertions.assertNotNull(resource.checkSubscriptionStatus(null, null));
	}

}
