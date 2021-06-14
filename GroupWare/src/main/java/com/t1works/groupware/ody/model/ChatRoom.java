package com.t1works.groupware.ody.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatRoom {
	private int roomNo; // 방번호
	private Set<WebSocketSession> sessions = new HashSet<>();

	public ChatRoom(int roomNo) {
		this.roomNo = roomNo;
	}

	public void sendMessage(String message) {
		TextMessage sendMessage = new TextMessage(message);
		
		try {
			for(WebSocketSession session : sessions) {
				session.sendMessage(sendMessage);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void enterUser(WebSocketSession session) {
		sessions.add(session);
	}
	
	public boolean containerUser(WebSocketSession session) {
		return this.sessions.contains(session);
	}
	
	public void exitUser(WebSocketSession session) {
		this.sessions.remove(session);
	}


	public int sizeOfSessions() {
		return this.sizeOfSessions();
	}

	public int getRoomNo() {
		return this.roomNo;
	}
}
