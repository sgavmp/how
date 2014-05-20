package com.how.tfg.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.view.RedirectView;

public class ModConnectController extends ConnectController {

	public ModConnectController(
			ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
	}
	
	@Override
	protected RedirectView connectionStatusRedirect(String providerId, NativeWebRequest request) {
		String event = "connect";
		if (((ServletWebRequest) request).getRequest().getMethod().compareTo("DELETE")==0)
				event = "delete";
		RedirectView redirectView = new RedirectView("/", true);
		redirectView.addStaticAttribute("provider", providerId);
		redirectView.addStaticAttribute("event", event);
		return redirectView;
	}

}
