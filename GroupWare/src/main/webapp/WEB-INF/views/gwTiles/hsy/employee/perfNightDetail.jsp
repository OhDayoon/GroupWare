<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath= request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	div#salaryDetailContainer{
		border: solid 0px red;
		width:1400px;
		margin: 0 auto;
		position: relative;
		left: -80px;
		margin-bottom: 200px;
	}

	div#salaryDetailTitle{
		border: solid 0px red;
		margin: 70px 0px 30px 50px;
		font-size: 22pt;
		font-weight: bold;
	}
	
	div#salaryDetailOption label{
		font-size: 13pt;
		margin-left: 10px;
		cursor: pointer;
	}
	
	div#salaryDetailOption input{cursor: pointer;}
	
	div#orderOption select{
		width: 140px;
		height: 30px;
		margin-right: 2px;
	}
	
	table#salaryDetailTable1, table#salaryDetailTable2 {
		border: solid 2px #a5a5a7;
		border-collapse: collapse;
		width: 85%;
		margin: 30px 0px 30px 50px;
	}
	
	table#salaryDetailTable1 th, table#salaryDetailTable1 td, table#salaryDetailTable2 th, table#salaryDetailTable2 td{
		border: solid 1px #a5a5a7;
		text-align: center;
		border-right: solid 2px #a5a5a7;
		font-size: 12.5pt;
	}
	
	table#salaryDetailTable1 tr.tableTop,table#salaryDetailTable2 tr.tableTop  {border-bottom: solid 2px #a5a5a7; }
	
	table#salaryDetailTable1 th, table#salaryDetailTable2 th{
		background-color: #003d66;
		color: #fff;
		font-weight: bold;
		height: 40px;
		padding: 5px;
		vertical-align: center;
	}
	
	table#salaryDetailTable1 td {height: 80px;}
	table#salaryDetailTable2 td {height: 60px;}
	
	tr.colorTr{background-color: #ffff99;}
	
</style>

<script type="text/javascript">


	$(document).ready(function(){
		
		// 내림차순 기본선택값으로 지정
		$("select#sortOption").val("desc");
		
		var sortOption= $("select#sortOption").val();
		
		if("${from}"!=""){ // 홈페이지에서 야근 찍고 넘어온 경우 야간수당 내역이 default
			getOverNightList(sortOption);
		}
		else{ // 사이드메뉴로 넘어온 경우 default값은 성과금내역 보기, 내림차순
			getBonusList(sortOption);		
		}
		
		// 성과금 내역 또는 야근수당 내역 라디오 버튼을 클릭한 경우 이벤트
		$("input[name=salaryDetailOption]").click(function(){
			
			// 기존의 테이블 비우기
			$("div#tableLocation").html("");
			
			var selctedOption= $(this).val();
			var sortOption= $("select#sortOption").val();
			
			if(selctedOption=='radio1'){ // 성과금 내역을 클릭한 경우
				getBonusList(sortOption); // 성과금내역 함수 호출
			}
			else{ // 야근수당 내역을 클릭한 경우
				getOverNightList(sortOption); // 야근수당 내역 함수 호출
			}
			
		}); // end of $("input[name=salaryDetailOption]").click(function(){-----	
			
			
		// 일시 정렬기준 change 이벤트
		$("select#sortOption").change(function(){

			var selctedOption= $("input[name=salaryDetailOption]:checked").val();
			var sortOption= $(this).val();
			
			if(selctedOption=='radio1'){ // 성과금 내역을 클릭한 경우
				getBonusList(sortOption); // 성과금내역 함수 호출
			}
			else{ // 야근수당 내역을 클릭한 경우
				getOverNightList(sortOption);
			}
			
		}); // end of $("select#sortOption").change(function(){-----------------
			
	}); // end of $(document).ready(function(){-----

	// === function declaration ===
	// 성과금 내역을 가져오는 함수
	function getBonusList(sortOption){
		
		$.ajax({
			url:"<%=ctxPath%>/t1/getBonusList.tw",
	   		type:"POST",
	   		data:{"employeeid":"${loginuser.employeeid}","sortOption":sortOption},
	   		dataType:"json",
	   		success:function(json){
	   			
	   			var html= "<table id='salaryDetailTable1'>"+
				   			"<tr>"+
				   				"<th rowspan='2' style='border-top: solid 2px #003d66; border-left: solid 2px #003d66;'>일시</th>"+
				   				"<th colspan='3' style='border-top: solid 2px #003d66;'>성과내역</th>"+
				   				"<th rowspan='2' style='border-top: solid 2px #003d66; border-right: solid 2px #003d66;'>성과금</th>"+
				   			"</tr>"+
				   			"<tr class='tableTop'>"+
				   				"<th>목표건</th>"+
				   				"<th>달성건</th>"+
				   				"<th>달성률</th>"+
				   			"</tr>";
				 
				 if(json.length==0){ // 실적이 존재하지 않거나, 실적이 존재하지만 성과금이 존재하지 않는 경우
					 html+= "<tr>"+
					 	   	 	"<td colspan='5'>성과금 내역이 존재하지 않습니다.</td>"+
				 		    "</tr>";
				 }
				 else{ // 성과금 내역이 존재하는 경우
				 
					 $.each(json,function(index,item){
						
						 var bonus=Number(item.bonus).toLocaleString('en');
						 
						 html+=	 "<tr>"+   
							 		"<td>"+item.date+"</td>"+
					   				"<td style='width:30%'>"+
					   					item.goalCnt+
					   					"&nbsp;건<br/><span style='font-size:9pt;'>(&nbsp;목표건:&nbsp;전월실적&nbsp;("+item.prevCnt+"건)&nbsp;+&nbsp;2건&nbsp;)</span>"+
					   				"</td>"+
					   				"<td>"+item.doneCnt+"&nbsp;건</td>"+
					   				"<td style='width:30%'>"+
					   					item.achievementRate+
					   					"<br/><span style='font-size:9pt;'>(&nbsp;달성률:&nbsp;달성건&nbsp;("+item.doneCnt+"건)&nbsp;/&nbsp;목표건&nbsp;("+item.goalCnt+"건)&nbsp;*&nbsp;100&nbsp;)</span>"+
					   				"</td>"+
					   				"<td style='color:#0000e6; font-weight: bold;'>"+bonus+"&nbsp;원</td>"+
				   				"</tr>";
					 
					 }); // end of $.each(json,function(index,item){--------
				 } // end of else----------------------
				 
	   			html+="</table>";
	   			
	   			$("div#tableLocation").html(html);
	   		
	   		},
	   		error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({-------------- 		
		
	} // end of function getBonusList(){---------------------

		
	// 야근 내역을 가져오는 함수	
	function getOverNightList(sortOption){
		
		$.ajax({
			url:"<%=ctxPath%>/t1/getOverNightList.tw",
	   		type:"POST",
	   		data:{"employeeid":"${loginuser.employeeid}","sortOption":sortOption},
	   		dataType:"json",
	   		success:function(json){
	   			
	   			var html= "<table id='salaryDetailTable2'>"+
				   			"<tr>"+
				   				"<th style='border-top: solid 2px #003d66; border-left: solid 2px #003d66;'>일시</th>"+
				   				"<th style='border-top: solid 2px #003d66; border-left: solid 2px #003d66;'>야간&nbsp;근무&nbsp;시간</th>"+
				   				"<th style='border-top: solid 2px #003d66; border-left: solid 2px #003d66;'>사유</th>"+
				   				"<th style='border-top: solid 2px #003d66; border-left: solid 2px #003d66;'>처리&nbsp;시간</th>"+
				   				"<th style='border-top: solid 2px #003d66; border-right: solid 2px #003d66;'>야근&nbsp;수당</th>"+
				   			"</tr>";
				   			
				 if(json.length==0){ // 야근내역이 존재하지 않는 경우
					 html+= "<tr>"+
					 	   	 	"<td colspan='5'>야근 수당 내역이 존재하지 않습니다.</td>"+
				 		    "</tr>";
				 }
				 else{ // 야근수당 내역이 존재하는 경우
					
					 $.each(json,function(index,item){
						
						 var overnightPay=Number(item.overnightPay).toLocaleString('en');
						
						 if(index==0 && sortOption=="desc" && "${from}"!="" )  html+= "<tr class='colorTr'>"; // 홈페이지에서 야근 찍고 넘어온 경우 + 내림차순인 경우
 						 else if(index==(json.length-1) && sortOption=="asc" && "${from}"!="" ) html+= "<tr class='colorTr'>"; // 홈페이지에서 야근 찍고 넘어온 경우 + 오름차순인 경우
						 else	html+= "<tr>";
					     
						 html+=		"<td>"+item.doLateSysdate+"</td>"+
					   				"<td style='font-size: 11pt;'>19시&nbsp;00분&nbsp;부터<br/>"+
					   					item.endLateTime+"&nbsp;까지"+
					   				"</td>"+
					   				"<td>"+item.doLateWhy+"</td>"+
					   				"<td>"+item.doLateTime+"&nbsp;시간</td>"+
					   				"<td style='color:#0000e6; font-weight: bold;'>"+overnightPay+"&nbsp;원</td>"+
				   				"</tr>";
					 
					 }); // end of $.each(json,function(index,item){--------
				 } // end of else----------------------
				 
	   			html+="</table>";
	   			
	   			$("div#tableLocation").html(html);
	   		
	   		},
	   		error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({-------------- 	
	} // end of function getBonusList(){---------------------
		
		
</script>


<div id="salaryDetailContainer">
	
	<div id="salaryDetailTitle">${loginuser.name}님의 성과금/야근수당 내역</div>
	<div id="salaryDetailOption" style="margin: 50px 0px 30px 50px; border: solid 0px red;">
		<c:if test="${from ne ''}">
			<input type="radio" id="radio1" value="radio1" name="salaryDetailOption" /><label for="radio1" style="margin-right: 20px;">성과금&nbsp;내역</label>
			<input type="radio" id="radio2" value="radio2" name="salaryDetailOption" checked/><label for="radio2">야근수당&nbsp;내역</label>
		</c:if>
		<c:if test="${from eq ''}">
			<input type="radio" id="radio1" value="radio1" name="salaryDetailOption" checked /><label for="radio1" style="margin-right: 20px;">성과금&nbsp;내역</label>
			<input type="radio" id="radio2" value="radio2" name="salaryDetailOption" /><label for="radio2">야근수당&nbsp;내역</label>
		</c:if>
	</div>
	<div id="orderOption" style="border:solid 0px blue; width:88.5%;" align="right">
		<span style="margin-right:15px; font-size: 12pt; color: #4c4c4d;">정렬기준</span>
		<select id="sortOption">
			<option value="asc">&nbsp;&nbsp;일시&nbsp;오름차순</option>
			<option value="desc">&nbsp;&nbsp;일시&nbsp;내림차순</option>
		</select>
	</div>
	<div id="tableLocation"></div>
	
	
</div>
