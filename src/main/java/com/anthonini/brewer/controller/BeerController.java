package com.anthonini.brewer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/beer")
public class BeerController {

	@RequestMapping("/new")
	public String form() {
		return "/beer/form";
	}
}
