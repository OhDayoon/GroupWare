package com.t1works.groupware.ody.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@Controller
public class ChattingOdyController {

	
	// === #173. (웹채팅관련4) === //
	
	@RequestMapping(value="/t1/chatting/chatwith.tw")
	public String requiredLogin_chatwith(HttpServletRequest request, HttpServletResponse response) {

		return "ody/chatwith";
	}
	
	
}
