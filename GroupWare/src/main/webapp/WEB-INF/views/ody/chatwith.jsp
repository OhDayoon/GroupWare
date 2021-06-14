<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<meta charset="UTF-8">
<title>T1Works GroupChat</title>

<style type="text/css">

html,body{
	background-color: #9bbad8;
	overflow: hidden;
	padding: 0px 0px;
	margin: 0px 0px;
	width: 100%;
        height: 100%;
}
input.btn_normal{
	background-color: #0071bd;
	border: none;
	color: white;
	width: 60px;
	height: 30px;
	font-size: 12pt;
	padding: 3px 0px;
	margin-top: 3px;
}

-webkit-scrollbar {
    width: 5px;
    background-color: #9bbad8;
  }
  
div#sendmsg{
	position: fixed; bottom: 10px; width: 100%;
}

button#btnAllDialog{
	border:none;
	font-sixe: 12pt;
	background-color: #9bbad8;
	margin-bottom: 2px;
}



</style>

<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script type="text/javascript">
	var today = new Date();
	var hours = today.getHours(); // 시
	if(hours.toString().length<2){
		hours="0"+hours;
	}
	var minutes = today.getMinutes();  // 분
	if(minutes.toString().length<2){
		minutes="0"+minutes;
	}
	
	$(document).ready(function(){
		
		
		var url = window.location.host; // 웹브라우저의 주소창의 포트까지 가져옴
		// alert("url : "+url);
			// 결과값 url : 192.168.219.104:9090
			
			var pathname = window.location.pathname; // '/'부터 오른쪽에 있는 모든 경로
	    //  alert("pathname : " + pathname);
	    //  결과값  pathname : /board/chatting/multichat.action
			
			var appCtx = pathname.substring(0, pathname.lastIndexOf("/"));  // "전체 문자열".lastIndexOf("검사할 문자");   
	    //  alert("appCtx : " + appCtx);
	    //  결과값  appCtx : /board/chatting
	    
	    	var root = url+appCtx;
	    //   alert("root : " + root);
	    //  결과값   root : 192.168.219.104:9090/board/chatting
	    
	    	var wsUrl = "ws://"+root+"/chatwithStart.tw";  
	     // 웹소켓통신을 하기위해서는 http:// 을 사용하는 것이 아니라 ws:// 을 사용해야 한다. 
	     // /multichatstart.action은 컨트롤러가 아닌 xml에서 작성해야함
	     
	      	var websocket = new WebSocket(wsUrl);       
	      	 // === 웹소켓에 최초로 연결이 되었을 경우에 실행되어지는 콜백함수 정의하기 ===  
	        websocket.onopen = function(){
	      	
	        };
	          
	        // === 메시지 수신시 콜백함수 정의하기 === 
	        websocket.onmessage = function(event) {
	                                 // 자음 ㄴ 임
	          if(event.data.substr(0,1)=="「" && event.data.substr(event.data.length-1)=="」") { 
	        	  
	        	  var event = event.data.replace(/\#/gi, "<br/><br/>");
	        	  event = event.substring(event.indexOf("「")+1, event.lastIndexOf("」"));
	        	 console.log(event);
	             $("div#connectingUserList").html(event);
	          }
	          else {
	               $("div#chatMessage").append(event.data);
	               $("div#chatMessage").append("<br/>");
	               $("div#chatMessage").scrollTop(99999999);
	          }
	        };
	        
	        
	     // === 웹소캣 연결 해제시 콜백함수 정의하기 === 
	        websocket.onclose = function() {
	            // websocket.send("채팅을 종료합니다.");
	        }
	        
	     // 메시지 입력후 엔터하기 
	        $("input#message").keyup(function (key) {
	             if (key.keyCode == 13) {
	                $("input#btnSendMessage").click();
	             }
	          }); 
	     
	        
	     // 메시지 보내기
	        $("input#btnSendMessage").click(function() {
	            if( $("input#message").val() != "") {
	                
	               // ==== 자바스크립트에서 replace를 replaceAll 처럼 사용하기 ====
	                // 자바스크립트에서 replaceAll 은 없다.
	                // 정규식을 이용하여 대상 문자열에서 모든 부분을 수정해 줄 수 있다.
	                // 수정할 부분의 앞뒤에 슬래시를 하고 뒤에 gi 를 붙이면 replaceAll 과 같은 결과를 볼 수 있다.
	                var messageVal = $("input#message").val();
	                messageVal = messageVal.replace(/<script/gi, "&lt;script"); 
	                // 스크립트 공격을 막으려고 한 것임.
	               
	                messageObj = {};
	                messageObj.message = messageVal;
	                messageObj.type = "all";
	                messageObj.to = "all";
	                 
	                var to = $("input#to").val();
	                if ( to != "" ) {
	                    messageObj.type = "one";
	                    messageObj.to = to;
	                }
	                 
	                websocket.send(JSON.stringify(messageObj));
	                // JSON.stringify() 는 값을 그 값을 나타내는 JSON 표기법의 문자열로 변환한다
	                
	                $("div#chatMessage").append("<div style='float:right;'><span style='font-size: 10pt;'>"+hours + ":" + minutes +"</span>&nbsp;<span style='background-color: yellow; padding: 3px 2px;'>" + messageVal + "</span></div>&nbsp;<br/><br/>");
	                $("div#chatMessage").scrollTop(99999999); // 스크롤이 맨위로 간다
	                 
	                $("input#message").val("");
	            }
	        });
	        
	     
	     
	        /////////////////////////////////////////////////////////////
	        
	        
	     // 귀속말대화끊기 버튼은 처음에는 보이지 않도록 한다.
	        $("button#btnAllDialog").hide();
	        $("input#toname").hide();
	        // 아래는 귓속말을 위해서 대화를 나누는 상대방의 이름을 클릭하면 상대방IP주소를 귓속말대상IP주소에 입력하도록 하는 것.
	        $(document).on("click",".loginuserName",function(){
	           /* class loginuserName 은 
	              com.spring.chatting.websockethandler.WebsocketEchoHandler 의 
	              protected void handleTextMessage(WebSocketSession wsession, TextMessage message) 메소드내에
	              133번 라인에 기재해두었음.
	           */
	           var ip = $(this).prev().text();
	           var name=$(this).text();
	        //   alert(ip);
	            $("input#to").val(ip); 
	            $("span#toname").show();
	            $("span#toname").text("@"+name);
	            $("span#privateWho").text($(this).text());
	            $("button#btnAllDialog").show();
	        });
	        
	        
	        // 귀속말대화끊기 버튼을 클릭한 경우는 전체대상으로 채팅하겠다는 말이다. 
	        $("button#btnAllDialog").click(function(){
	              $("input#to").val("");
	              $("span#privateWho").text("");
	              $(this).hide();
	              $("span#toname").text("");
	              $("span#toname").hide();
	        });
	        
	        
	  	});// end of $(document).ready(function(){})-----------------

</script>

<h3 style="margin-top:5px;">T1Works GroupChat</h3>
<div style="width: 100%; height: 90%; margin-legt:10px;">

<div style="float: left; width: 20%; height:480px; border-right: solid 1px gray; margin-left: 10px; margin-right: 20px;">
	- 접속자<br><br>
	<div id="connectingUserList" style="" ></div>
</div>
  <div id="chatMessage" style="overFlow: auto; max-height: 500px; margin-right: 5px;"></div>
  <div id="chatMessageMe" style="overFlow: auto; max-height: 500px; margin-right: 5px;"></div>
</div>
  <div id="sendmsg" style="margin-left: 10px;">
  	<span id="toName"></span><button type="button" id="btnAllDialog">⊖</button><br>
	<input type="text" id="message" size="90" style="height: 35px;"  placeholder="메시지 내용"/>
    <input type="button" id="btnSendMessage" class="btn_normal" value="보내기" />

 </div> 

<input type="hidden" id="to" value=""/>
<input type="hidden" id="toId" value="${empId}"/>