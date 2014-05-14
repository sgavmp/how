package com.how.tfg.social;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.social.connect.web.thymeleaf.SpringSocialDialect;
import org.thymeleaf.processor.IProcessor;

public class SpringSocialDialectExtended extends SpringSocialDialect {
	@Override
	public Set<IProcessor> getProcessors() {
		final Set<IProcessor> processors = super.getProcessors();
		processors.add(new NotConnectedAttrProcessor());
		return processors;
	}
}
