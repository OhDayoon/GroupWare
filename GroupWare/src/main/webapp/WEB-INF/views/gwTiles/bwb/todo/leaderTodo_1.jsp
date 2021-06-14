<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<% 
	String ctxPath = request.getContextPath();
%>

<style>
	
	div#content {
		margin-left: 100px;
		margin-top: 50px;
	}
	
	div#noAssign {
		background-color: #ccc;
		float:left;
		margin-right: 30px; 
	}
	
	div.assign {
		border: solid 1px black;
		width: 150px;
		height: 30px;
		margin-bottom: 20px;
		margin-top: 50px;
		cursor: pointer;
	}
	
	div#yesAssign {
		display: inline-block;
	}
	
	div.detail {
		border: solid 0px blue;
		width: 1300px;
		height: 300px;
	}
	
	.hurryStyle{
		color: red;
	}
	
	span.todoMenu{
		font-size: 12pt;
		font-weight: bolder;
		border:solid 0px black;
		display:inline-block;
		width: 150px;
	}
	
	span.info {
		border:solid 0px black;
		display:inline-block;
	}
	
	div#pageBar{
		border:solid 0px black;
		margin-top: 15px;
		text-align: center;
		width: 70%;
	}
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		$("div#pageBar").html("${pageBar}");
		
		
		// 배정버튼 클릭 시
		$("button.goAssign").click(function(){

			var pNo = $(this).parent().find("input.pNo").val();
			var hurryCheck = $(this).parent().find($("input:checkbox[name=hurryCheck]")).prop("checked");
			var hurryno = 0;
			var employeeid = $(this).parent().find("select.whoAssign").val();
			console.log(employeeid);
			
			if(hurryCheck){ // 긴급여부 체크 여부
				hurryno=1;
			}
			
			$.ajax({
				url:"<%= ctxPath%>/t1/goAssign.tw",
				type:"post",
				data:{"pNo":pNo,
					  "hurryno":hurryno,
					  "employeeid":employeeid},
				dataType:"json",
				success:function(json){
						if(json.n==1){
							alert("담당자 배정이 완료되었습니다.");
							location.href="javascript:history.go(0)";
						}
				},
		    	error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }

			}); // end of $.ajax({ 

		}); // end of $("button".goAssign).click(function(){
		
			
		// 긴급 체크박스 클릭시 프로젝트명 빨간색으로 표기
		$("input:checkbox[name=hurryCheck]").click(function(){
			
			var bool = $(this).prop("checked");
			
			if(bool){
				$(this).parent().parent().find("span.pName").addClass("hurryStyle");
			}
			else{
				$(this).parent().parent().find("span.pName").removeClass("hurryStyle");
			}
			
			
		}); // end of $("input:checkbox[name=hurryCheck]").click(function(){
		
		
	}); // end of $(document).ready(function(){

</script>

<div id="content">
	
	<h2>${loginuser.name}님의 업무현황</h2>
	
	<div id="noAssign" class="assign" onclick="location.href='<%= ctxPath%>/t1/leaderTodo.tw'" style="padding-left:25px; padding-top:5px;">미배정업무[${requestScope.noAssignedProduct}]건</div>
	<div id="yesAssign" class="assign" onclick="location.href='<%= ctxPath%>/t1/leaderTodo2.tw'" style="padding-left:25px; padding-top:5px;">배정업무[${requestScope.assignedProduct}]건</div>
	
	<div id="noAssignDetail" class="detail">
		<div style="border-bottom: solid 1px gray; width:1000px; margin-bottom : 10px;">
			<span class="todoMenu" style="width:50px; text-align:center;">순번</span>
			<span class="todoMenu" style="width:90px; text-align:center;">긴급여부</span>
			<span class="todoMenu" style="width:250px;">프로젝트명</span>
			<span class="todoMenu" style="width:100px; text-align:center;">배정자</span>
			<span class="todoMenu" style="width:100px; text-align:center;">배정일</span>
			<span class="todoMenu" style="width:100px; text-align:center;">시작일</span>
			<span class="todoMenu" style="width:100px; text-align:center;">기한일</span>
			<span class="todoMenu" style="width:80px">담당자</span>
		</div>
		
		<c:if test="${not empty productList}">
			<c:forEach var="product" items="${productList}">
			<div style="border-bottom: solid 1px gray; width:1000px; margin-bottom : 10px;">
				<input class="pNo" type="hidden" value="${product.pNo}">
				<span class="info" style="width:50px; text-align:center;">${product.rno}</span>
				<span class="info" style="width:90px; text-align:center;"> <input type="checkbox" name="hurryCheck" class="hurryCheck" /></span>
				<span class="info" style="width:250px">${product.pName}</span>
				<span class="info" style="width:100px; text-align:center;">${product.name}</span>
				<span class="info" style="width:100px; text-align:center; font-size:12pt;"> - </span>
				<span class="info" style="width:100px; text-align:center; font-size:12pt;"> - </span>
				<span class="info" style="width:100px; text-align:center;">${product.dueDate}</span>
				<span class="info" style="width:80px">
					<select class="whoAssign" style="width:70px;">
						<c:forEach var="member" items="${memberList}">
							<option value="${member.employeeid}">${member.name}</option>
						</c:forEach>
					</select>
				</span>
				<button type="button" class="goAssign" style="margin-bottom:5px;">배정</button>
			</div>
			</c:forEach>
		</c:if>
		
		<c:if test="${empty productList}">
			<div>배정되지 않은 업무가 존재하지 않습니다.</div>
		</c:if>
		
		<div id="pageBar"></div>
	</div>

</div>