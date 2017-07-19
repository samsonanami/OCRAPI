package com.fintech.oracle.oracle.rest;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Home redirection to swagger api documentation 
 */
@Controller
public class HomeController {


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "redirect:api-docs";
	}
}
