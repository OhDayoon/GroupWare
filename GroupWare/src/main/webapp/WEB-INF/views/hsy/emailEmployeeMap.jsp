<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath= request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">

	div#employeeBox{
		border: solid 2px #003d66;
		margin: 20px 0px 30px 10px;
		float: left;
		width: 300px;
		padding: 0px 20px;
	}
	
	div.companyName{
		font-size: 13pt;
		padding: 15px 0px;
		color:#004d80;
		font-weight: bold;
	}
	
	div.clickMenu{
		border-top: solid 1px #003d66;
		font-size: 13pt;
		padding: 15px 0px;
		color:#004d80;
		font-weight: bold;
	}
	
	div#employeeBox li{
		line-height: 33px;
		margin-left: -15px;
	}
	
	div#employeeBox ul{
		list-style-type: none;
		margin: 10px 0px 20px 0px;
	}
	
	button.receiver{
		margin-left: 20px;
		background-color: #000080;
		color: #fff; 
		border: none;
		cursor: pointer;
		height: 25px;
		width: 70px;
	}
	
	button.cc{
		margin-left: 5px;
		background-color: #1a6600;
		color: #fff; 
		border: none;
		cursor: pointer;
		height: 25px;
		width: 70px;
	}
	
	div#receiverBox, div#ccBox{
		width:300px; 
		min-height:200px; 
		border: solid 2px #003d66; 
		float: left;
		margin-left: 40px;
		margin-top: 20px;
	}
	
	div.boxTitle{
		color: #fff;
		font-weight: bold;
		background-color: #003d66;
		height: 30px;
		padding: 5px 0px 0px 20px;
		margin: 2px;
	}
	
	div.emailList,div.emailList2 {
		padding: 5px 0px 5px 15px;
	}
	
	button.del{
		height: 25px;
		width: 70px;
		margin-left: 20px;
		cursor: pointer;
		background-color: #e3e3e8;
		border: solid 1px #000;
	}
	
	button.del1{
		height: 25px;
		width: 70px;
		margin-left: 5px;
		cursor: pointer;
		background-color: #e3e3e8;
		border: solid 1px #000;
	}
</style>

<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script type="text/javascript">
	$(document).ready(function(){
		
		//부서명과 사장이름은 처음부터 show 되도록 설정 
		$("ul.hideInfo").hide();
		$("span.minus").hide();
		$("div.companyName span").show();
		
		// 각부서명 클릭이벤트
		$("div.clickMenu").click(function(){
			if($(this).next().css('display')=='none'){
				$(this).next().show();
				$(this).find("span.minus").show();
				$(this).find("span.plus").hide();
			}
			else{
				$(this).next().hide();
				$(this).find("span.minus").hide();
				$(this).find("span.plus").show();
			}
		}); // end of $("div.clickMenu").click(function(){-----

			
		// 받는사람 버튼 hover
		$("button.receiver").hover(function(){
			$(this).parent().css("color","#e62e00");
		},function(){ // end of mouserover----------------
			$(this).parent().css("color","#000");
		}); // end of mouseout , hover ------------------
		
		
		// 참조 버튼 hover
		$("button.cc").hover(function(){
			$(this).parent().css("color","#e62e00");
		},function(){ // end of mouserover----------------
			$(this).parent().css("color","#000");
		}); // end of mouseout , hover ------------------
		
		
		// 받는사람 버튼 click 이벤트 
		$("button.receiver").click(function(){
			
			if($(this).text()=="받는사람"){
				
				// 1) 받는사람 Box에 추가
				var email= $(this).prop('id');
				var employeeid= $(this).parent().prop('id'); // 받는 사람 BOX에서 제거하기 위해 employeeid를 id값으로 사용
				$("div#receiverBox").append("<div id='receiver"+employeeid+"' class='emailList'>"+email+"</div>");
				
				// 2) 받는사람 버튼을 제거 버튼으로 바꿔주기
				$(this).text("제거");
				$(this).removeClass("receiver");
				$(this).addClass("del");
			}
			else{ // 제거 버튼인 상태 
				
				// 1) 받는사람 Box에서 제거
				var email= $(this).prop('id');
				var employeeid= $(this).parent().prop('id'); // 받는 사람 BOX에서 제거하기 위해 employeeid를 id값으로 사용
				$("div#receiver"+employeeid).hide();
				
				// 2) 제거 버튼을 받는사람으로 바꿔주기
				$(this).text("받는사람");
				$(this).addClass("receiver");
				$(this).removeClass("del");
			}
			
		}); // end of $("button.receiver").click(function(){----
		
			
		// 참조 버튼 click 이벤트 
		$("button.cc").click(function(){
			
			if($(this).text()=="참조"){
				
				// 1) 참조 Box에 추가
				var email= $(this).prop('id');
				var employeeid= $(this).parent().prop('id'); // 참조 BOX에서 제거하기 위해 employeeid를 id값으로 사용
				$("div#ccBox").append("<div id='cc"+employeeid+"' class='emailList2'>"+email+"</div>");
				
				// 2) 참조 버튼을 제거 버튼으로 바꿔주기
				$(this).text("제거");
				$(this).removeClass("cc");
				$(this).addClass("del1");
				
			}
			else{ // 제거 버튼인 상태 
				
				// 1) 참조 Box에서 제거
				var email= $(this).prop('id');
				var employeeid= $(this).parent().prop('id'); // 참조 BOX에서 제거하기 위해 employeeid를 id값으로 사용
				$("div#cc"+employeeid).hide();
				
				// 2) 제거 버튼을 받는사람으로 바꿔주기
				$(this).text("참조");
				$(this).addClass("cc");
				$(this).removeClass("del1");
			}
		}); // end of $("button.receiver").click(function(){----
		
			
		// 취소버튼 클릭 이벤트 => 팝업창 닫기
		$("button#goBack").click(function(){
			self.close();
		});
		
		
		// 적용 버튼 클릭 이벤트 => 받는사람과 참조에 있는 이메일이 부모창에 적용되고 현재 팝업창은 닫힌다
		$("button#apply").click(function(){
			
			var receiverEmailArr= [];  // 여러명일 경우도 고려해서 배열 사용
			$("div.emailList").each(function(index,item){
				receiverEmailArr.push($(item).text());
			});
			
			var receiverEmail= receiverEmailArr.join();
			
			var ccEmailArr= [];
			$("div.emailList2").each(function(index,item){
				ccEmailArr.push($(item).text());
			});
			
			var ccEmail= ccEmailArr.join();
			
			// 자식창에서 부모창으로 값 전달
			opener.document.getElementById("receiverEmail").value= receiverEmail;
			opener.document.getElementById("ccEmail").value= ccEmail;
			
			opener.location.href = "javascript:afterEmailEmployeeMap()";  // 부모창의 함수 실행
			self.close(); // 자식창 닫힘
		
		}); // end of $("button#apply").click(function(){---------------------- 		
		
			
			
	}); // end of $(document).ready(function(){----------------------

</script>

<div align="left" style="margin: 30px 0px 20px 10px;">
	<button type="button" id="apply" style="background-color: #0071bd; color:#fff; border: none; width: 100px; height: 30px; cursor:pointer;">적용</button>
	<button type="button" id="goBack" style="background-color: #e2e2e9; border: none; width: 100px; height: 30px; cursor:pointer;">취소</button>
</div>

<div id="employeeBox">
	<div class="companyName">
		<span class="minus" style="color:#5e5e5e; font-weight:bold; ">■</span>
		T1Works
	</div>
	
	<c:forEach var="departmentVO" items="${departmentList}">
		<div class="clickMenu hideDeptInfo" style='cursor:pointer;'>
			<span class="plus" style="color:#262626; font-weight:bold;">□</span>
			<span class="minus" style="color:#5e5e5e; font-weight:bold;">■</span>
			${departmentVO.dname}
		</div>
		<ul class="hideInfo">
		<c:forEach var="memberVO" items="${employeeList}">
			<c:if test="${departmentVO.dcode eq memberVO.fk_dcode}">
				<li id="${memberVO.employeeid}">${memberVO.name}
					<c:if test="${memberVO.fk_pcode eq '1'}">사원</c:if>
					<c:if test="${memberVO.fk_pcode eq '2'}">대리</c:if>
					<c:if test="${memberVO.fk_pcode eq '3'}">부장</c:if>
					<button type="button" class="receiver" id="${memberVO.email}">받는사람</button>
					<button type="button" class="cc" id="${memberVO.email}">참조</button>
				</li>
			</c:if>
		</c:forEach>
		</ul>
	</c:forEach>
</div>

<div id="receiverBox">
	<div class="boxTitle">받는 사람 목록</div>
</div>
	
<div id="ccBox">
	<div class="boxTitle">참조 목록</div>
</div>