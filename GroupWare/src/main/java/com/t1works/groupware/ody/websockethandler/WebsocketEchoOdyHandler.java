package com.t1works.groupware.ody.websockethandler;

import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


import com.t1works.groupware.bwb.model.MemberBwbVO;


public class WebsocketEchoOdyHandler extends TextWebSocketHandler {

	
    private List<WebSocketSession> connectedUsers = new ArrayList<WebSocketSession>(); 
    
	
    public void init() throws Exception {
       
    }
 
    @Override
    public void afterConnectionEstablished(WebSocketSession wsession) throws Exception {
    	
    	
    	connectedUsers.add(wsession);
    	
    

    
        String connectingUserName = "「";
    
        for (WebSocketSession webSocketSession : connectedUsers) {
            Map<String, Object> map = webSocketSession.getAttributes();
            
           MemberBwbVO loginuser = (MemberBwbVO)map.get("loginuser"); 
           
           // 접속자 직급 알려주기
           String pcode = loginuser.getFk_pcode();
           if("1".equals(pcode)) {
        	   pcode="사원";
           }
           else if("2".equals(pcode)) {
        	   pcode="대리";
           }
           else if("3".equals(pcode)) {
        	   pcode="부장";
           }
           else if("4".equals(pcode)) {
        	   pcode="사장";
           }
          
           // 부서명 알려주기
           String dname = loginuser.getFk_dcode();
           
           if(dname !=null && dname!="") {
	           if("1".equals(dname)) {
	        	   dname="(CS1팀)";
	           }
	           else if("2".equals(dname)) {
	        	   dname="(CS2팀)";
	           }
	           else if("3".equals(dname)) {
	        	   dname="(CS3팀)";
	           }
	           else if("4".equals(dname)) {
	        	   dname="(인사팀)";
	           }
	           else if("5".equals(dname)) {
	        	   dname="(총무팀)";
	           }
	           
           }
           else {
        	   dname="";
           }
        
           
            connectingUserName += "<span class='ip' style='display: none;'>"+ webSocketSession.getRemoteAddress().getAddress().getHostAddress() +"</span><span class='loginuserName'>"+loginuser.getName()+" "+pcode+"<span style= 'font-size: 10pt;'>"+dname+"</span></span>#"; 
          
        }
        connectingUserName += "」";
    
   
        
        
        for (WebSocketSession webSocketSession : connectedUsers) {
            webSocketSession.sendMessage(new TextMessage(connectingUserName));
        }
       
    }
    
 
    @Override
    protected void handleTextMessage(WebSocketSession wsession, TextMessage message) throws Exception {
       
      
       Map<String, Object> map = wsession.getAttributes();
       MemberBwbVO loginuser = (MemberBwbVO)map.get("loginuser");  
  
       
    // 접속자 직급 알려주기
       String pcode = loginuser.getFk_pcode();
       if("1".equals(pcode)) {
    	   pcode="사원";
       }
       else if("2".equals(pcode)) {
    	   pcode="대리";
       }
       else if("3".equals(pcode)) {
    	   pcode="부장";
       }
       else if("4".equals(pcode)) {
    	   pcode="사장";
       }
      
       // 부서명 알려주기
       String dname = loginuser.getFk_dcode();
       
       if(dname !=null && dname!="") {
           if("1".equals(dname)) {
        	   dname="(CS1팀)";
           }
           else if("2".equals(dname)) {
        	   dname="(CS2팀)";
           }
           else if("3".equals(dname)) {
        	   dname="(CS3팀)";
           }
           else if("4".equals(dname)) {
        	   dname="(인사팀)";
           }
           else if("5".equals(dname)) {
        	   dname="(총무팀)";
           }
           
       }
       else {
    	   dname="";
       }
    
  
        MessageOdyVO messageVO = MessageOdyVO.convertMessage(message.getPayload());
      
        String hostAddress = "";
 
        // 시간
        Calendar currentDate = Calendar.getInstance(); // 현재날짜와 시간을 얻어온다.
	    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
	    String currentTime = dateFormat.format(currentDate.getTime());
	      
        
        for (WebSocketSession webSocketSession : connectedUsers) {
            if (messageVO.getType().equals("all")) { 
               // 채팅할 대상이 "전체" 일 경우
               // 메시지를 자기자신을 뺀 나머지 모든 사용자들에게 메시지를 보냄.
                if (!wsession.getId().equals(webSocketSession.getId())) {  
                   // wsession 은 메시지를 보낸 클라이언트임.
                   // webSocketSession 은 웹소켓서버에 연결된 모든 클라이언트중 하나임.
                   // wsession.getId() 와  webSocketSession.getId() 는 자동증가되는 고유한 숫자로 나옴 
                    webSocketSession.sendMessage(
                           new TextMessage("<span class='ip' style='display: none;'>"+wsession.getRemoteAddress().getAddress().getHostAddress()+"</span><p style='font-size:11pt; ' class='loginuserName'>" +loginuser.getName()+" "+pcode+"<span style='font-size:10pt;'>"+dname+"</span></p>" + "<span style='background-color: #fff; padding:0px 4px;'>"+messageVO.getMessage()+"</span>&nbsp;<span style='font-size: 10pt;'>"+currentTime+"</span>"));  
                }
            } 
            else { // 채팅할 대상이 "전체"가 아닌 특정대상(지금은 귓속말대상 IP address 임) 일 경우 
               hostAddress = webSocketSession.getRemoteAddress().getAddress().getHostAddress(); 
                          // webSocketSession 은 웹소켓서버에 연결한 모든 클라이언트중 하나이며, 그 클라이언트의 IP address를 알아오는 것임.  
                
               if (messageVO.getTo().equals(hostAddress)) { 
                   // messageVO.getTo() 는 클라이언트가 보내온 귓속말대상 IP address 임.
            	   System.out.println("messageVo.getTO()"+messageVO.getTo());
                    webSocketSession.sendMessage(
                            new TextMessage(

                                    "<span class='ip' style='display: none;'>"+ wsession.getRemoteAddress().getAddress().getHostAddress() +"</span><p style='font-size:11pt;' class='loginuserName'>" +loginuser.getName()+" "+pcode+"<span style='font-size:10pt;'>"+dname+"</span></p>"  + "<span style='background-color: #fff; padding:0px 4px; '>"+messageVO.getMessage()+"</span>&nbsp;<span  style='font-size: 10pt;'>"+currentTime+"</span>"
                                   ) 
                    );
                    break; 
                }
            }
        }

    }
    

    @Override
    public void afterConnectionClosed(WebSocketSession wsession, CloseStatus status)  throws Exception {
       
       Map<String, Object> map = wsession.getAttributes();
       MemberBwbVO loginuser = (MemberBwbVO)map.get("loginuser");
       
       connectedUsers.remove(wsession);
       // 웹소켓 서버에 연결되어진 클라이언트 목록에서 연결은 끊은 클라이언트는 삭제시킨다.
       
        for (WebSocketSession webSocketSession : connectedUsers) {
           
           // 퇴장했다라는 메시지를 자기자신을 뺀 나머지 모든 사용자들에게 메시지를 보내도록 한다.
    /*     if (!wsession.getId().equals(webSocketSession.getId())) { 
                webSocketSession.sendMessage(
                   new TextMessage(wsession.getRemoteAddress().getAddress().getHostAddress() +" [<span style='font-weight:bold;'>" +loginuser.getName()+ "</span>]" + "님이 <span style='color: red;'>퇴장</span>했습니다.")
                ); 
            } */
        }
       
    
        
        
        String connectingUserName = "「";
        
        for (WebSocketSession webSocketSession : connectedUsers) {
            Map<String, Object> map2 = webSocketSession.getAttributes();
            MemberBwbVO loginuser2 = (MemberBwbVO)map2.get("loginuser");  
           
   
            connectingUserName += loginuser2.getName()+" "; 
        }
        
        connectingUserName += "」";
        
        for (WebSocketSession webSocketSession : connectedUsers) {
            webSocketSession.sendMessage(new TextMessage(connectingUserName));
        }
        
    }
    
}
