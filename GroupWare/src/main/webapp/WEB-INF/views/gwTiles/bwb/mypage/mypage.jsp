<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<style>
	
	table#mypageInfo{
	
		border:solid 1px black;
		margin-left : 100px;
		margin-top  : 100px;
		width: 1000px;
		height: 200px;
		border-collapse: collapse;
	}
	
	table#mypageInfo td{
		border:solid 1px black;
		padding-left: 15px;
	}
	
	button#changePasswd{
		margin-top: 30px;
		margin-left: 100px;
	}
	
	div#passbox{
		margin-top: 30px;
		margin-left: 100px;
	}
	
	button.lastbtn{
		margin-top: 15px;
	}
	
	td.mytd{
		font-weight: bolder;
		background-color: #94b8b8;
		margin-left: 10px;
		width: 120px;
	}
</style>

<script type="text/javascript">

	$(document).ready(function(){
		 
		
		$("div#passbox").hide();
		
		$("button#changePasswd").click(function(){
			
			$("button#changePasswd").hide();
			
			// 이용자가 초기비밀번호를 바꾸기 전 이라면 바로 비밀번호 변경 버튼 가능
			if("${paraMap.fJubun}" == "${paraMap.passwd}"){

				$("div#passbox").show();
				$("div#changePasswd").show();
				$("div#checkUserPasswd").hide();
				
			}
			else{
				$("div#passbox").show();
				$("div#changePasswd").show();
			}
			
			
			
		}); // end of $("button#changePasswd").click(function(){
		
		$("button#goChange").click(function(){
			
			var lastpasswd = $("input#lastpasswd").val();
			var passwd = $("input#passwd").val();
			var passwdCheck = $("input#passwdCheck").val();
			var employeeid = "${sessionScope.loginuser.employeeid}";
			
			
			// 기존비밀번호를 바꾼 사람
			if("${paraMap.fJubun}" != "${paraMap.passwd}"){ // 951011  != 12#$#$#$$#

				$.ajax({
					url:"<%= ctxPath%>/t1/checkpasswd.tw",
					type:"post",
					data:{"lastpasswd":lastpasswd,
						  "employeeid":employeeid},
					dataType:"json",
					success:function(json){
						
						if(json.n == 1){
							alert("기존 비밀번호가 일치하지 않습니다. 다시 확인해주세요");
							$("input#lastpasswd").val("");
							$("input#passwd").val("");
							$("input#passwdCheck").val("");
						}
						else{
							
							if(passwd != passwdCheck){
								alert("비밀번호가 일치하지 않습니다.");
								$("input#lastpasswd").val("");
								$("input#passwd").val("");
								$("input#passwdCheck").val("");
							}
							else{
								updatePasswd(passwd,employeeid);
							}
						}
						
					},
					error: function(request, status, error){
			        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }	

				}); // end of ajax
				
			}// end of if if("${paraMap.fJubun}"
			else{
				// 신규비밀번호와 신규비밀번호 확인이 일치하지 않은사람
				if(passwd != passwdCheck){
					alert("비밀번호가 일치하지 않습니다.");
					$("input#passwd").val("");
					$("input#passwdCheck").val("");
				}
				else{
					updatePasswd(passwd,employeeid);
				}
				
			}// end of else
		}); // end of $("button#goChange").click(function()
		
			
			
			
		
	}); // end of $(document).ready(function(){
	
		
	// Function Declaration
	function updatePasswd(passwd,employeeid){
		
		$.ajax({
			url:"<%= ctxPath%>/t1/changePwd.tw",
			type:"post",
			data:{"passwd":passwd,
				  "employeeid":employeeid},
			dataType:"json",
			success:function(json){
				
				if(json.n == 1){
					alert("비밀번호 변경이 성공했습니다. \n 다시 로그인 해주세요.");
					location.href="<%= ctxPath%>/t1/logout.tw";
				}
				
			},
			error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }	
			
		}); // end of ajax
		
	}// end of function updatePasswd(){

</script>
<div id="content">
	<span style="position:relative; top:80px; left:100px; font-size:15pt; font-weigth:bolder;">&#60; 마이페이지 &#62;</span> 
	<table id="mypageInfo">
		<tr>
			<c:if test="${loginuser.employeeimg eq 'noimage.png'}">
				<td rowspan="5" style="width:170px; padding-left:47px;">${loginuser.employeeimg}</td>
			</c:if>
			<td class="mytd">사번</td>
			<td>${loginuser.employeeid}</td>
			<td class="mytd">이메일</td>
			<td>${loginuser.email}</td>
		</tr>
		<tr>
			<td class="mytd">성명</td>
			<td>${loginuser.name}</td>
			<td class="mytd">주민번호</td>
			<td>${paraMap.fJubun} - ●●●●●●●</td>
		</tr>
		<tr>
			<td class="mytd">소속</td>
			<td>${paraMap.pname}</td>
			<td class="mytd">직무</td>
			<td>${paraMap.duty}</td>
		</tr>
		<tr> 
			<td class="mytd">직위</td>
			<td>${paraMap.dname}</td>
			<td class="mytd">입사일자</td>
			<td>${fn:substring(loginuser.hiredate, 0, 10)}</td>
		</tr>
		<tr>
			<td class="mytd">전화번호</td>
			<td>${fn:substring(loginuser.cmobile,0,2)} - ${fn:substring(loginuser.cmobile,3,6)} - ${fn:substring(loginuser.cmobile,6,10)}</td>
			<td class="mytd">핸드폰</td>
			<td>${fn:substring(loginuser.mobile,0,3)} - ${fn:substring(loginuser.mobile,3,7)} - ${fn:substring(loginuser.mobile,7,11)}</td>
		</tr>
	</table>
	
	<button type="button" id="changePasswd">비밀번호 변경</button>
	
	<div id="passbox">
		<form name="changePasswd">
			<div id="checkUserPasswd">
				<label id="passwd">기존비밀번호</label> <input type="password" name="lastpasswd" id="lastpasswd" />
		    </div>
			<div id="changePasswd">
				<label id="passwd" style="width:116px;">신규비밀번호</label> <input type="password" name="passwd" id="passwd" /><br>
				<label id="passwdCheck" style="width:116px;">신규비밀번호 확인</label> <input type="password" name="passwdCheck" id="passwdCheck" />	
			</div>
			
			<button type="button" id="goChange" class="lastbtn" style="margin-left:250px">변경</button>
			<button type="reset" id="cancel" class="lastbtn">취소</button>
		</form>
	</div>
</div>