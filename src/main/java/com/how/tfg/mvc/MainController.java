package com.how.tfg.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.repository.UserRepository;
import com.how.tfg.security.SecurityUtil;
import com.how.tfg.social.UserService;

@Controller
@SessionAttributes("user")
public class MainController {

	private UserService service;

    @Autowired
    public MainController(UserService service) {
        this.service = service;
    }

    @RequestMapping("/main")
    public String greeting(WebRequest request, @RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "hello";
    }
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String redirectRequestToRegistrationPageFacebook() {
        return "redirect:/user/register";
    }
    
    @RequestMapping(value = "/auth/google", method = RequestMethod.POST)
    public String redirectRequestToRegistrationPageGoogle() {
        return "redirect:/user/register";
    }
    
    @RequestMapping(value = "/connect/**", method = RequestMethod.GET)
    public String redirectRequestToRegistrationPageDropbox() {
        return "redirect:/";
    }
    
    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String showRegistrationForm(WebRequest request, Model model) {
    	ProviderSignInUtils providerSign = new ProviderSignInUtils();
        Connection<?> connection = providerSign.getConnectionFromSession(request);
        
        User registered = service.registerNewUserAccountOrGet(connection);

        SecurityUtil.logInUser(registered);
        providerSign.doPostSignUp(registered.getEmail(), request);

        return "redirect:/";
    }

}
