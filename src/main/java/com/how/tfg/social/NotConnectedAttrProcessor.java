package com.how.tfg.social;

import org.springframework.context.ApplicationContext;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.ClassUtils;
import org.thymeleaf.Arguments;
import org.thymeleaf.context.IContext;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.ConfigurationException;
import org.thymeleaf.processor.attr.AbstractConditionalVisibilityAttrProcessor;

public class NotConnectedAttrProcessor extends AbstractConditionalVisibilityAttrProcessor {

	private final boolean thymeleaf3Present;

	private final boolean thymeleaf4Present;

	public NotConnectedAttrProcessor() {
		super("notconnected");
		thymeleaf3Present = ClassUtils.isPresent("org.thymeleaf.spring3.context.SpringWebContext", NotConnectedAttrProcessor.class.getClassLoader());
		thymeleaf4Present = ClassUtils.isPresent("org.thymeleaf.spring4.context.SpringWebContext", NotConnectedAttrProcessor.class.getClassLoader());
	}

	@Override
	public int getPrecedence() {
		return 300;
	}

	@Override
	protected boolean isVisible(Arguments arguments, Element element, String attributeName) {
		final String providerId = element.getAttributeValue(attributeName);
		if (providerId == null || providerId.trim().equals("")) {
			return false;
		}
		ConnectionRepository connectionRepository = getConnectionRepository(arguments.getContext());
		return connectionRepository.findConnections(providerId).size() == 0;
	}

	private ConnectionRepository getConnectionRepository(final IContext context) {
		ApplicationContext applicationContext = null;
		if (thymeleaf4Present) {
			applicationContext = getSpringApplicationContextForThymeleaf4(context);
		} else if (thymeleaf3Present) {
//			applicationContext = getSpringApplicationContextForThymeleaf3(context);
		} else {
			throw new ConfigurationException("Neither Thymeleaf 3 SpringWebContext nor Thymeleaf 4 SpringWebContext is in "
					+ "the application classpath.");
		}
		ConnectionRepository connectionRepository = applicationContext.getBean(ConnectionRepository.class);
		return connectionRepository;
	}

//	private ApplicationContext getSpringApplicationContextForThymeleaf3(final IContext context) {
//		if (!(context instanceof org.thymeleaf.spring3.context.SpringWebContext)) {
//			throw new ConfigurationException(
//					"Thymeleaf execution context is not a Spring web context (implementation of " +
//							org.thymeleaf.spring3.context.SpringWebContext.class.getName() + ". Spring Social integration can only be used in " +
//					"web environements with a Spring application context.");
//		}
//		final org.thymeleaf.spring3.context.SpringWebContext springContext = (org.thymeleaf.spring3.context.SpringWebContext) context;		
//		return springContext.getApplicationContext();
//	}

	private ApplicationContext getSpringApplicationContextForThymeleaf4(final IContext context) {
		if (!(context instanceof org.thymeleaf.spring4.context.SpringWebContext)) {
			throw new ConfigurationException(
					"Thymeleaf execution context is not a Spring web context (implementation of " +
					org.thymeleaf.spring4.context.SpringWebContext.class.getName() + ". Spring Social integration can only be used in " +
					"web environements with a Spring application context.");
		}
		final org.thymeleaf.spring4.context.SpringWebContext springContext = (org.thymeleaf.spring4.context.SpringWebContext) context;		
		return springContext.getApplicationContext();
	}

}
