package com.anthonini.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/style")
public class StyleController {

	@RequestMapping("/new")
	public String form() {
		return "style/form";
	}
}
