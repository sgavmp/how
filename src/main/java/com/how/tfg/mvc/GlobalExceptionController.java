package com.how.tfg.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
public class GlobalExceptionController {
	
	@ExceptionHandler({Exception.class, RuntimeException.class})
	public ModelAndView handleAllException(Exception ex) {
 
		ModelAndView model = new ModelAndView("home");
		model.addObject("error", "Ha ocurrido un error");
 
		return model;
 
	}
	
	@RequestMapping(value = "/error/{error}", method = RequestMethod.GET)
	public ModelAndView resolveLoginException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex, @PathVariable("error")String error) {
		
		ModelAndView model = new ModelAndView("error");
		model.addObject("error", "Tienes que entrar en el sistema antes de acceder a la web.");
 
		return model;
	}

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		ModelAndView model = new ModelAndView("error");
		model.addObject("error", "Ha ocurrido un error.");
 
		return model;
	}
	
}
