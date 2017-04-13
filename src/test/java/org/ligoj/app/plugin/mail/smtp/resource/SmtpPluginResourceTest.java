package org.ligoj.app.plugin.mail.smtp.resource;

import java.io.IOException;
import java.util.Collections;

import javax.transaction.Transactional;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import net.sf.ehcache.CacheManager;

/**
 * Test of {@link SmtpPluginResource}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/application-context-test.xml")
@Rollback
@Transactional
public class SmtpPluginResourceTest extends AbstractAppTest {

	@Autowired
	private SmtpPluginResource resource;

	@Before
	public void prepareConfiguration() throws IOException {
		persistEntities("csv", Node.class, Parameter.class, ParameterValue.class);
	}

	@Before
	@After
	public void cleanCache() {
		CacheManager.getInstance().getCache("plugin-data").removeAll();
	}

	@Test
	public void getKey() {
		Assert.assertEquals("service:mail:smtp", resource.getKey());
	}

	@Test
	public void getMailSender() {
		final JavaMailSenderImpl mailSender = (JavaMailSenderImpl) resource.getMailSender("service:mail:smtp:local");
		Assert.assertEquals("localhost", mailSender.getHost());
		Assert.assertEquals("user", mailSender.getUsername());
		Assert.assertEquals("secret", mailSender.getPassword());
		Assert.assertEquals(25, mailSender.getPort());
		Assert.assertEquals("UTF-8", mailSender.getDefaultEncoding());
	}

	@Test
	public void send() {
		final SmtpPluginResource resource = new SmtpPluginResource();
		applicationContext.getAutowireCapableBeanFactory().autowireBean(resource);
		resource.applicationContext = Mockito.mock(ApplicationContext.class);
		final SmtpPluginResource mockPLugin = Mockito.mock(SmtpPluginResource.class);
		Mockito.when(mockPLugin.getMailSender("service:mail:smtp:local"))
				.thenReturn(Mockito.mock(JavaMailSenderImpl.class));
		Mockito.when(resource.applicationContext.getBean(SmtpPluginResource.class)).thenReturn(mockPLugin);
		final MimeMessagePreparator messagePreparator = Mockito.mock(MimeMessagePreparator.class);
		Assert.assertEquals(messagePreparator, resource.send("service:mail:smtp:local", messagePreparator));
	}

	@Test(expected = NotImplementedException.class)
	public void link() {
		resource.link(0);
	}

	@Test
	public void getVersion() throws Exception {
		Assert.assertNull(resource.getVersion(Collections.emptyMap()));
	}

	@Test
	public void getLastVersion() throws Exception {
		Assert.assertNull(resource.getLastVersion());
	}

	@Test
	public void checkStatus() throws Exception {
		Assert.assertTrue(resource.checkStatus(null, null));
	}

	@Test
	public void checkSubscriptionStatus() throws Exception {
		Assert.assertNull(resource.checkSubscriptionStatus(null, null));
	}

}
