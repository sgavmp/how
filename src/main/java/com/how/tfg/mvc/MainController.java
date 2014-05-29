package com.how.tfg.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.service.UserService;
import com.how.tfg.modules.core.controller.BaseController;
import com.how.tfg.security.SecurityUtil;

@Controller
@SessionAttributes("user")
public class MainController {

	private UserService service;
	private List<BaseController> allApps;

	@ModelAttribute("apps")
	public List<BaseController> getAllApps() {
		return allApps;
	}

	@Autowired
	public void setAllApps(List<BaseController> allApps) {
		this.allApps = allApps;
	}

	@Autowired
	public MainController(UserService service) {
		this.service = service;
	}

	@ModelAttribute("menu")
	public String getMenuOpt() {
		return "apps";
	}

	@ModelAttribute("principal")
	public Object getUser() {
		return SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
	}

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET, params = {
			"provider", "event" })
	public String homeEvent(WebRequest request,
			@RequestParam(value = "provider") String providerId,
			@RequestParam(value = "event") String event, Model model) {
		model.addAttribute("info", "web.messages." + providerId + "." + event);
		return "home";
	}

	@RequestMapping(value = { "", "/" }, method = RequestMethod.GET)
	public String home(WebRequest request, Model model) {
		return "home";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.GET)
	public String redirectRequestToRegistrationPageFacebook() {
		return "redirect:/user/register";
	}

	@RequestMapping(value = "/auth/google", method = RequestMethod.POST)
	public String redirectRequestToRegistrationPageGoogle() {
		return "redirect:/user/register";
	}

	@RequestMapping(value = "/user/register", method = RequestMethod.GET)
	public String showRegistrationForm(WebRequest request, Model model) {
		ProviderSignInUtils providerSign = new ProviderSignInUtils();
		Connection<?> connection = providerSign
				.getConnectionFromSession(request);

		User registered = service.registerNewUserAccountOrGet(connection);

		SecurityUtil.logInUser(registered);
		providerSign.doPostSignUp(registered.getEmail(), request);

		return "redirect:/";
	}
	
	@RequestMapping(value="/state", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> getStatusOfApp(WebRequest request) {
		Map<String, String> stats = new HashMap<String,String>();
		DateTime start = DateTime.now();
		service.measureStateOfServer();
		DateTime end = DateTime.now();
		stats.put("Start", start.toString());
		stats.put("Time", new Long(end.minus(start.getMillis()).getMillis()).toString());
		stats.put("End", end.toString());
		return stats;
	}

}
