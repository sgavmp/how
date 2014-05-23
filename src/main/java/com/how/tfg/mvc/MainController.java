package com.how.tfg.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.how.tfg.data.domain.User;
import com.how.tfg.data.domain.UserDetails;
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
    
    @RequestMapping(value="/", method=RequestMethod.GET, params={"provider","event"})
    public String homeEvent(WebRequest request, @RequestParam(value="provider") String providerId, @RequestParam(value="event") String event, Model model) {
        model.addAttribute("info", "web.messages." + providerId + "." + event);
        return "home";
    }
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home(WebRequest request, Model model) {
        return "home";
    }
    
    @RequestMapping(value="/apps", method=RequestMethod.GET)
    public String apps(WebRequest request, Model model) {
        return "apps";
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
        Connection<?> connection = providerSign.getConnectionFromSession(request);
        
        User registered = service.registerNewUserAccountOrGet(connection);

        SecurityUtil.logInUser(registered);
        providerSign.doPostSignUp(registered.getEmail(), request);

        return "redirect:/";
    }

}
