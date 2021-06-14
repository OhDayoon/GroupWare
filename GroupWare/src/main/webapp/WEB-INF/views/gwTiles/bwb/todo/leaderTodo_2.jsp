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
		float:left;
		margin-right: 30px; 
	}
	
	div#yesAssign {
		background-color: #ccc;
		display: inline-block;
	}
	
	div.assign {
		border: solid 1px black;
		width: 150px;
		height: 30px;
		margin-bottom: 20px;
		margin-top: 50px;
		cursor: pointer;
	}
	
	
	div.detail {
		border: solid 0px blue;
		width: 1200px;
		height: 300px;
	}
	
	span.todoMenu {
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
		
		
		
			
		// select태그 반복문을 돌려서 초기 담당자 넣어주기
		$("select.whoAssign").each(function(index,item){
			
			var fk_employeeid = $(this).prop("name");
			$(this).val(fk_employeeid);
		
		}); // end of $("select.whoAssign").each(function(index,item){
			
		
		// 배정자 변경버튼 클릭 시 	
		$("button.changeAssign").click(function(){
			
			var fk_employeeid = $(this).parent().find("select.whoAssign").val(); // 새로운 담당자
			var ori_fk_employeeid = $(this).parent().find("select.whoAssign").prop("name"); // 기존담당자
			var pNo = $(this).parent().find("input.pNo").val();
			var hurryno = $(this).parent().find("input.hurryno").val();
			
			if(fk_employeeid==ori_fk_employeeid){
				alert("새로운 담당자를 선택후 변경해주세요");
				return;
			}
			else{
				$.ajax({
					url:"<%= ctxPath%>/t1/changeAssign.tw",
					type:"post",
					data:{"pNo":pNo,
					      "hurryno":hurryno,	
						  "fk_employeeid":fk_employeeid},
					dataType:"json",
					success:function(json){
							if(json.n==1){
								alert("담당자 배정이 변경되었습니다.");
								location.href="javascript:history.go(0)";
							}
					},
			    	error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }

				}); // end of $.ajax({ 
				
			}// end of else()
		}); // end of $("button#changeAssign").click(function(){
		

	}); // end of $(document).ready(function(){

</script>

<div id="content">
	
	<h2>${loginuser.name}님의 업무현황</h2>
	
	<div id="noAssign" class="assign" onclick="location.href='<%= ctxPath%>/t1/leaderTodo.tw'" style="padding-left:25px; padding-top:5px;">미배정업무[${requestScope.noAssignedProduct}]건</div>
	<div id="yesAssign" class="assign" onclick="location.href='<%= ctxPath%>/t1/leaderTodo2.tw'" style="padding-left:25px; padding-top:5px;">배정업무[${requestScope.assignedProduct}]건</div>
	
	<div id="yesAssignDetail" class="detail">
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
				<span class="info" style="width:90px; text-align:center;">
					<input class="hurryno" type="hidden" value="${product.hurryno}">
					<c:if test="${product.hurryno eq 1}">
						<span style="background-color:#ff471a; color:white; display:inline-block; width:32px;">긴급</span>
					</c:if>
					<c:if test="${product.hurryno eq 0}">
						<span style="font-size:12pt;">-</span>
					</c:if>
				</span>
				<span class="info" style="width:250px">${product.pName}</span>
				<span class="info" style="width:100px; text-align:center;">${product.name}</span>
				<span class="info" style="width:100px; text-align:center;">${product.assignDate}</span>
				<span class="info" style="width:100px; text-align:center;">${product.startDate}</span>
				<span class="info" style="width:100px; text-align:center;">${product.dueDate}</span>
				<span class="info" style="width:80px">
					<select class="whoAssign" name="${product.fk_employeeid}" style="width:70px;">
						<c:forEach var="member" items="${memberList}">
							<option value="${member.employeeid}">${member.name}</option>
						</c:forEach>
					</select>
				</span>
				<button type="button" class="changeAssign" style="margin-bottom:5px;">배정변경</button>
			</div>
			</c:forEach>
		</c:if>
		
		<c:if test="${empty productList}">
			<div style="margin-top:15px; font-color:red; ">배정된 업무가 존재하지 않습니다.</div>
		</c:if>
		
		<div id="pageBar"></div>
	</div>

</div>