<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.net.InetAddress"%>
<% 
	String ctxPath = request.getContextPath();

	// 웹 채팅 서버 받기
	InetAddress inet = InetAddress.getLocalHost(); 
	String serverIP = inet.getHostAddress();
	int portnumber = request.getServerPort();
	String serverName = "http://"+serverIP+":"+portnumber; 
%>

<script>

	$(document).ready(function(){
		
		displayNew(); // 신규 공지사항 있을시 new 아이콘 띄우기
		
		<%-- === 검색어 입력시 자동글 완성하기 시작(백원빈) === --%>
		$("div#displayList").hide();
		
		$("input#searchwords").keyup(function(event){
			
			// 검색어의 길이
			var wordLength = $(this).val().trim().length;
			
			if(wordLength ==0){
				$("div#displayList").hide();
			}
			else if(event.keyCode == 13){
				goSearch();
			}
			else{
				$.ajax({
					url:"<%= ctxPath%>/t1/wordSearch.tw",
					data:{"searchWord":$("input#searchwords").val()},
					dataType:"json",
					success:function(json){// "[]" 또는 "[{"word":"xxx"},{"word":"xxx"}]"
						<%-- === #112. 검색어 입력시 자동글 완성하기7 === --%>
						if(json.length > 0){ // 검색 된 데이터가 있는 경우임
							
							var html = "";
							$.each(json, function(index,item){
								
								var word = item.word; 
								// word=> "첫번째 java","두번째 JaVa" 이런것임
								
								// 입력한 검색어만 자동글에 대소문자 상관없이 해당부분의 위치가 파랗게 나오도록 해보겠다.
								var index = word.toLowerCase().indexOf($("input#searchwords").val().toLowerCase());
								// word=> "첫번째 java","두번째 java"
								// 만약에 검색어라 jAva 이라면 index는 4가 된다.
								
								var len = $("input#searchwords").val().length;
								// 검색어의 길이 len = 4
								
								// javascript에서 substr=> 길이만큼 읽어오기 , substring ~부터~ 까지 뽑아오기
								word = word.substr(0,index) + "<span style='color:#b30000;'>"+word.substr(index,len)+"</span>"+word.substr(index+len);
								
								html += "<span style='cursor:pointer;' class='word'>"+word+"</span><br>";
								
							});//end of $.each(json, function(index,item){
							
							$("div#displayList").show();
							$("div#displayList").html(html);	
						}
						else{
							$("div#displayList").show();
							$("div#displayList").text("결과가 없습니다.");	
						}
						
						
						
					},
					error: function(request, status, error){
	                    alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	                }
				});// end of $.ajax({
			
			} // end of else
			
		}); // end of $("input#searchwords").keyup(function(){
		
			
		$(document).on("click","span.word",function(){
			
			// 텍스트박스에 검색된 결과중 선택한 문자열을 입력해준다.
			$("input#searchwords").val($(this).text());
			
			// 다시 감추기
			$("div#displayList").hide();
			
		});
		
		
		// 검색하기
		$("input#goButton").click(function(){
			goSearch();
		});
		
		
		
		<%-- === 검색어 입력시 자동글 완성하기 끝(백원빈) === --%>

	}); // end of $(document).ready(function(){
		
	//function declaration
	function goSearch(){
		
		var frm = document.searchFrm;
		frm.action = "<%=ctxPath%>/t1/searchSebuAddress.tw";
		frm.method = "get";
		
		$.ajax({ // 세부메뉴 테이블에 존재하는 모든 메뉴명 가져오기
			url:"<%= ctxPath%>/t1/wordSearch.tw",
			data:{"searchWord":""},
			dataType:"json",
			success:function(json){
				
				var bool = false;
				$.each(json,function(index,item){
					var searchWord = $("input#searchwords").val();

					if(searchWord==item.word){
						bool = true;
						frm.submit();
					}
					
				}); // end of $.each(json,function(){
				
				if(!bool){
					alert("검색결과가 존재하지 않습니다.");	
					location.href="javascript:history.go(0)";
				}
				
			},
			error: function(request, status, error){
                alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
            }
		});// end of $.ajax({
		
		
		
	}// end of function goSearch(){
	
		
function displayNew(){
	
	$.ajax({
		url:"<%=ctxPath%>/t1/checkNewNotice.tw",
		dataType:"json",
		success:function(json){
			var html = "";		
			if(json.n == 1){
				html = '<i style="display: block; position: relative; top: 8px;" class="fas fa-bullhorn fa-2x"></i><span style="border:solid 1px red; background-color: red; border-radius:100%; font-size: 6px; height: 12px; width: 12px; display:block; line-height: 8px; padding-right: 1px; position: relative; top:-19px; left: 28px;">n</span>';
			} else {
				html = '<i class="fas fa-bullhorn fa-2x"></i>';
			}
			console.log("공지사항 아이콘 : "+json.n);
			$("td.bullhorn").html(html);
		},
		error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
        }
		
	});
	
}
	
</script>
<div id="mainMenu"> 
	<div id="left-menu">
		<span id="menu1" onclick="location.href='<%= ctxPath%>/t1/home.tw'"><img id="gwLogo" src="<%=ctxPath%>/resources/images/login/t1works_logo.jpg" /></span>
		<div style="display: inline-block; vertical-align: middle; margin-top:15px;">  
		  <span style="font-weight: bold;">${loginuser.name}
		     <c:if test="${loginuser.fk_pcode eq 1}">사원님</c:if>
		     <c:if test="${loginuser.fk_pcode eq 2}">대리님</c:if>
		     <c:if test="${loginuser.fk_pcode eq 3}">부장님</c:if>
		     <c:if test="${loginuser.fk_pcode eq 4}">사장님</c:if></span><span>&nbsp;환영합니다.</span><br>
		  <span class="navbar-btn" onclick="location.href='<%= ctxPath%>/t1/logout.tw'">
		   	  <c:if test="${not empty loginuser}">로그아웃</c:if>
		  </span>
		</div>
	</div>
	<div id="right-menu">
		<table id="right-menu-style">
		   	<tbody>
		   		<tr style="height: 30px;">
		   			<td class="t1tour-shortcut"><i class="fas fa-plane fa-2x"></i></td>
		   			<td class="mypage"><i class="far fa-user-circle fa-2x"></i></td>
		   			<td class="schedule"><i class="far fa-calendar-alt fa-2x"></i></td>
		   			<td class="rsv"><i class="far fa-calendar-check fa-2x"></i></td>
		   			<td class="notice bullhorn"></td>
		   			<td class="groupchat" onclick='window.open("<%= serverName%><%= ctxPath%>/t1/chatting/chatwith.tw", "", "left=100px, top=100px, width=750px, height=600px");'><i class="far fa-comments fa-2x"></i></td>
		   			<td rowspan="2">
		   			<form name="searchFrm" style="display:inline-block;">
				   	<input type="text" id="searchwords" name="searchWord" autocomplete="off" style="color:black;" />
				   	<input type="button" id="goButton" class="navbar-btn" value="검색"/>
				   </form>
		   			</td>
		   		</tr>
		   		<tr style="height: 20px;">
		   			<td class="t1tour-shortcut">티원투어</td>
		   			<td class="mypage">마이페이지</td>
		   			<td class="schedule">일정관리</td>
		   			<td class="rsv">예약현황</td>
		   			<td class="notice">공지사항</td>
		   			<td class="notice">그룹챗</td>
		   		</tr>
		    </tbody>
	    </table>
		<div id="displayList" style="border:solid 1px gray; border-top: 0px; padding-top:3px; width:138px; top:-15px; left:393px; position:relative; overflow:auto;">
	    </div>
   </div>
   
</div>
