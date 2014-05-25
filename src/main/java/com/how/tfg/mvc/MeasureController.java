package com.how.tfg.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/measure")
public class MeasureController {
	
	private String url_first_measure = "/measure/trello";
		
	@RequestMapping({"","/"})
	public String mainMeasureOrFirstMeasure() {
		return "redirect:"+url_first_measure;
	}
}
