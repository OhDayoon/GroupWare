<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	String ctxPath = request.getContextPath();
%>

<style>

	table.rentalTable{
		width: 1200px;
		border: solid 0px red;
		font-size:12pt;
		margin-top:90px;
		margin-left:120px;
		border-collapse: collapse;
	}
	
	div#rentalHeader{
		font-size:15pt;
		margin-top:50px;
		margin-left: 580px;
	}
	
	span.rentalSpan {
		text-decoration: underline;
		margin-right:20px;
		cursor:pointer;
	}
	
	table.rentalTable th{
		border-bottom:solid 1px black;
		font-weight: bolder;
		font-size:13pt;
		background-color: #c2d6d6;
	}
	
	table.rentalTable td{
		border-bottom:solid 1px black;
		height:40px;
	}
	
	
	
</style>

<script>

	$(document).ready(function(){
		
		$("div#rentalGoods").hide();
		
		// 차량관리 클릭 시
		$("span#manageCar").click(function(){
			
			$("div#rentalCar").show();
			$("div#rentalGoods").hide();	
			
		});
		
		// 사무용품관리 클릭 시
		$("span#manageGoods").click(function(){
			
			$("div#rentalGoods").show();
			$("div#rentalCar").hide();	
			
		});
		
		
		// 차량관리에서 승인버튼 클릭시
		$("button.okCar").click(function(){
			
			var rscno = $(this).parent().parent().find("input#rscno").val();
			var employeeName = $(this).parent().parent().find("td#employeeName").text();
			
			var bool = confirm(employeeName+"님의 차량예약에 대한 승인을 하시겠습니까?");
			
			if(bool){
			
				$.ajax({
					url:"<%= ctxPath%>/t1/updateCarRental.tw",
					data:{"rscno":rscno},
					type:"post",
					dataType:"json",
					success:function(json){
						if(json.n == 1){
							alert("승인이 완료되었습니다.");
							location.href="javascript:history.go(0)";
						}
					},
			    	error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }
				}); // end of ajax();
			}
			else{
				location.href="javascript:history.go(0)";
			}
			
			
		});// end of $("button.okCar").click(function(){
			
		// 사무용품관리에서 승인버튼 클릭시
		$("button.okgoods").click(function(){
			
			var rsgno = $(this).parent().parent().find("input#rsgno").val();
			var employeeName = $(this).parent().parent().find("td#employeeName").text();
			
			var bool = confirm(employeeName+"님의 사무용품 예약에 대한 승인을 하시겠습니까?");
			
			if(bool){
				$.ajax({
					url:"<%= ctxPath%>/t1/updateGoodsRental.tw",
					data:{"rsgno":rsgno},
					type:"post",
					dataType:"json",
					success:function(json){
						if(json.n == 1){
							alert("승인이 완료되었습니다.");
							location.href="javascript:history.go(0)";
						}
					},
			    	error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }
				}); // end of ajax();
			}
			else{
				location.href="javascript:history.go(0)";
			}
			
		});// end of $("button.okCar").click(function(){	
		
	});

</script>

<div id="rentalHeader"> 
	<span class="rentalSpan" id="manageCar">차량관리</span>
	<span class="rentalSpan" id="manageGoods">사무용품관리</span>
</div>

<div id="rentalCar" class="rentalDiv">

	<table class="rentalTable">
		<thead> 
			<tr> 
				<th>대여구분</th>
				<th>예약날짜</th>
				<th>사용예정시간</th>
				<th>차량명</th>
				<th>도착지</th>
				<th>신청자</th>
				<th>탑승자</th>
				<th>목적</th>
				<th>승인여부</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty carList}">
					미승인된 내역이 존재하지 않습니다.
			</c:if>
			<c:if test="${not empty carList}">
				<c:forEach var="carvo" items="${carList}">
				<tr> 
					<td>차량대여 <input type="hidden" id="rscno" value="${carvo.rscno}"/> </td>
					<td>${carvo.rcdate}</td>
					<td>${carvo.rctime}</td>
					<td>${carvo.carname}</td>
					<td>${carvo.rdestination}</td>
					<td id="employeeName">${carvo.name}</td>
					<td>${carvo.rcpeople}</td>
					<td>${carvo.rcsubject}</td>
					<td> <button type="button" class="okCar">승인</button> </td>
				</tr>
				</c:forEach>
			</c:if>
			
		</tbody>
		
	
	</table>

</div>

<div id="rentalGoods" class="rentalDiv">
	
	<table class="rentalTable">
		<thead> 
			<tr> 
				<th>대여구분</th>
				<th>예약날짜</th>
				<th>사용예정시간</th>
				<th>사무용품명</th>
				<th>신청자</th>
				<th>목적</th>
				<th>승인여부</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty goodsList}">
					미승인된 내역이 존재하지 않습니다.
			</c:if>
			<c:if test="${not empty goodsList}">
				<c:forEach var="goodsvo" items="${goodsList}">
				<tr>
					<td> 사무용품대여<input type="hidden" value="${goodsvo.rsgno}"/> </td>
					<td>${goodsvo.rgdate}</td>
					<td>${goodsvo.rgtime}</td>
					<td>${goodsvo.goodsname}</td>
					<td id="employeeName">${goodsvo.name}</td>
					<td>${goodsvo.rgsubject}</td>
					<td><button type="button" class="okgoods">승인</button></td>
				</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>
	
</div>