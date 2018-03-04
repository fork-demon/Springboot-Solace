package com.example.config;

import java.util.Properties;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
public class ContextConfig {

	@Autowired
	CamelContext camelContext;

	@Value("${solace.url}")
	String solaceURL;

	@Value("${solace.vpn}")
	String solaceVPN;

	@Value("${solace.principal}")
	String principal;

	@Value("${solace.credential}")
	String credential;

	@Value("${solace.jndifactory}")
	String jndifactory;


	@Bean
	public JndiTemplate solaceJndiTemplate() {
		JndiTemplate jndiTemplate = new JndiTemplate();
		Properties jndiProps = new Properties();

		jndiProps.setProperty("java.naming.factory.initial", "com.solacesystems.jndi.SolJNDIInitialContextFactory");
		jndiProps.setProperty("java.naming.provider.url", solaceURL); 
		jndiProps.setProperty("Solace_JMS_VPN", solaceVPN);
		jndiProps.setProperty("java.naming.security.principal", principal); 
		jndiProps.setProperty("java.naming.security.credentials", credential); 
		jndiTemplate.setEnvironment(jndiProps);
		return jndiTemplate;
	}

	@Bean
	public JndiObjectFactoryBean solaceConnectionFactory() {
		JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();

		jndiObjectFactoryBean.setJndiTemplate(solaceJndiTemplate());
		jndiObjectFactoryBean.setJndiName(jndifactory);

		return jndiObjectFactoryBean;
	}
	/*
	 * This is optional as depends on use case if  caching of session and consumer 
	 * is needed. As of now default value 10 is assigned for session cache value.
	 */
	@Bean
	public CachingConnectionFactory solaceCachedConnectionFactory() {
		CachingConnectionFactory jndiObjectFactoryBean = new CachingConnectionFactory();

		jndiObjectFactoryBean.setTargetConnectionFactory((ConnectionFactory)solaceConnectionFactory().getObject());
		jndiObjectFactoryBean.setSessionCacheSize(10);

		return jndiObjectFactoryBean;
	}

	@Bean
	public JmsComponent solaceJms() {
		JmsComponent component = new JmsComponent();

		component.setConnectionFactory(solaceCachedConnectionFactory());
		return component;
	}

	public String getSolaceURL() {
		return solaceURL;
	}

	public void setSolaceURL(String solaceURL) {
		this.solaceURL = solaceURL;
	}

	public String getSolaceVPN() {
		return solaceVPN;
	}

	public void setSolaceVPN(String solaceVPN) {
		this.solaceVPN = solaceVPN;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public String getJndifactory() {
		return jndifactory;
	}

	public void setJndifactory(String jndifactory) {
		this.jndifactory = jndifactory;
	}
}
