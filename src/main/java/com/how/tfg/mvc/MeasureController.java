package com.how.tfg.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.how.tfg.data.services.ConnectionService;

@Controller
@RequestMapping("/measure")
public class MeasureController {
	
	private ConnectionService service;
	
	@Autowired
	public MeasureController(ConnectionService service) {
		super();
		this.service = service;
	}
		
	@RequestMapping({"","/"})
	public String mainMeasureOrFirstMeasure(RedirectAttributes redirectAttributes) {
		if (service.haveAnyConnection())
			return "redirect:"+getUrlFirstApp();
		redirectAttributes.addFlashAttribute("error", "web.notconnection");
		return "redirect:/";
	}
	
	private String getUrlFirstApp() {
		return "/measure/"+service.getFirstApp();
	}
}
