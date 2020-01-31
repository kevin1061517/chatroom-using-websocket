package com.kevin.server;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/webforchatroom.herokuapp.com")
public class ViewController {
	@GetMapping("/index")
	public String test() {
		System.out.println("test");
		return "index";
	}
	@GetMapping(value = "/test")
	public String html() {
		return "hello world";
	}
	@GetMapping(value = "/")
	@ResponseBody
	public String index() {
		return "index";
	}
}
