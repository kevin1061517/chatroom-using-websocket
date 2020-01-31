package com.kevin.server;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
	@GetMapping("/index")
	public String test() {
		System.out.println("test");
		return "index";
	}
	@GetMapping(value = "/test")
	public String html() {
		return "index.html";
	}
	
}
