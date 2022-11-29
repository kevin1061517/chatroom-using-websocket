package com.kevin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/webforchatroom.herokuapp.com")
public class ViewController {
	@RequestMapping({"/", "/index"})
	public String test() {
		System.out.println("test");
		return "index";
	}

	@RequestMapping(value = "/test")
	public String html() {
		return "test";
	}
}
