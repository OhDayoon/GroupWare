<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<% String ctxPath = request.getContextPath(); %>

<style>
div#containerview{
	margin: 30px 0px 30px 50px;
	width: 80%;
}
div.section table, div.section th, div.section td{
	border: solid 1px #ccc;
	border-collapse: collapse;
}

#table1 {
	float: right; 
	width: 300px; 
	border-collapse: collapse;
	margin-right: 200px;
	margin-bottom: 50px;
}

#table1 th, #table1 td{
	text-align: center;
}
#table1 th {
	background-color: #395673; 
	color: #ffffff;
}
#table2 th, #table3 th, #table4 th, #table5 th {
	width: 150px;
}

th{
	background-color: #ccd9e6;
	padding: 7px;
}
td{
	padding: 7px;
}

#table2 {
	width: 70%;
	margin: 50px auto;
}
#table3 {
	width: 70%;
	margin: 10px auto;
}
#table4 {
	width: 70%;
	margin: 10px auto;
}
#table5 {
	width: 70%;
	margin: 10px auto;
}
input.btn {
	width: 70px;
	border-radius: 0;
	font-weight: bold;
}

td.opinion{
	border: solid 1px white;
}

div#delBtn{
	display: inline-block;
	font-size: 9pt;
	border: none;
	background-color: #e89996; 
	color: white;
	font-weight: bold;
	width: 11pt;
	height: 11pt;
}
div#delBtn:hover{
	cursor: pointer;
	background-color: #bc2e29;
}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		
		// 의견보기
		goViewOpinion();
		
		$("input#reset").click(function(){			
			$("textarea#ocontent").val("");
		});
		
		// 결재로그 보기
		goViewLogList();
		
		// 도장찍기
		goViewStamp();
	});
	
	// Function Declaration
	
	// 의견 작성
	function goAddWrite(){
		var opinionVal = $("textarea#ocontent").val().trim();
		if(opinionVal == ""){
			alert("의견을 작성하세요!!");
			return; // 종료
		}
		
		var form_data = $("form[name=addWriteFrm]").serialize();
		
		$.ajax({
			url:"<%=ctxPath%>/t1/addOpinion.tw",
			data:form_data,
			type:"post",
			dataType:"json",
			success:function(json){ 
				goViewOpinion();
				
				$("textarea#ocontent").val("");
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});		
	}// end of function goAddWrite(){}--------------------
	
	// 의견 작성 리스트 보기
	function goViewOpinion(){
		$.ajax({
			url:"<%= ctxPath%>/t1/opinionList.tw",
			data:{"parentAno":"${requestScope.avo.ano}"},
			dataType:"json",
			success:function(json){				
								
				var html = "";
				
				if(json.length > 0){
					$.each(json, function(index, item){						
						if(item.userid == item.employeeid){
							html += "<div>▶"+item.dname+"&nbsp;<span style='font-weight: bold;'>"+item.name+"&nbsp;"+item.pname+"&nbsp;&nbsp;["+item.odate+"]&nbsp;&nbsp;<div id='delBtn' onclick='delMyOpinion("+item.ono+")'>&nbsp;X&nbsp;</div></div>";
						}
						else{
							html += "<div>▶"+item.dname+"&nbsp;<span style='font-weight: bold;'>"+item.name+"&nbsp;"+item.pname+"&nbsp;&nbsp;["+item.odate+"]</div>";
						}
						html += "<div>"+ item.ocontent +"</div>";
						html += "<hr style='margin: 2px;'>";
					});
				}
				else{
					html += "<div>의견이 존재하지 않습니다.</div>";
				}
				
				$("td#opinionDisplay").html(html);
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}// end of function goViewOpinion(){}--------------------
	
	
	// 목록보기
	function goback(){
		var $myFrm= document.myFrm;
		$myFrm.method="POST";
		$myFrm.action="<%=ctxPath%>/t1/myDocuSpend_rec.tw";
		$myFrm.submit();
	}
	
	
	// 결재
	function goApproval(){
		
		var bool = confirm("결재 승인 하시겠습니까?");
		
		if(bool){
			var formData = $("form[name=approvalDocu]").serialize();
			
			$.ajax({
				url:"<%=ctxPath%>/t1/approval.tw",
				data:formData,
				type:"post",
				dataType:"json",
				success:function(json){ 

					if(json.n == 1){					
						alert("결재 승인 되었습니다");						
						location.href = "<%=ctxPath%>/t1/myDocuSpend_complete.tw";						
					}
					else{
						alert("결재 승인 실패했습니다");
					}
					
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}			
			});
		}		
	}
	
	// 반려
	function goReject(){
		
		var bool = confirm("반려 처리 하시겠습니까?");
		
		if(bool){
			var formData = $("form[name=approvalDocu]").serialize();
			
			$.ajax({
				url:"<%=ctxPath%>/t1/reject.tw",
				data:formData,
				type:"post",
				dataType:"json",
				success:function(json){ 
					
					if(json.n == 1){					
						alert("반려 처리 되었습니다.");
						location.href = "<%=ctxPath%>/t1/myDocuSpend_complete.tw";
					}
					else{
						alert("반려 처리 실패했습니다.");
					}					
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}			
			});	
		}		
	}
	
	// 결재의견 삭제하기
	function delMyOpinion(ono){
		var bool = confirm("의견을 삭제하시겠습니까?");
	 	
		if(bool){
			$.ajax({
				url:"<%=ctxPath%>/t1/delMyOpinion.tw",
				data:{"ono":ono},
				type:"post",
				dataType:"json",
				success:function(json){			
					
					if(json.n == 1){					
						alert("의견이 삭제되었습니다.");
						goViewOpinion();
					}
					else{
						alert("의견 삭제가 실패했습니다.");
					}
					
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}			
			});
		}		
	}
	
	
	// 결재로그 리스트 보기
	function goViewLogList(){
		$.ajax({
			url:"<%= ctxPath%>/t1/approvalLogList.tw",
			data:{"parentAno":"${requestScope.avo.ano}"},
			dataType:"json",
			success:function(json){				
								
				var html = "";
				
				if(json.length > 0){
					$.each(json, function(index, item){
						
						var logstatus = item.logstatus;
						if(logstatus == '0'){
							logstatus = "제출";
						}
						else if(logstatus == '1'){
							logstatus = "승인";
						}
						else if(logstatus == '2'){
							logstatus = "반려";
						}
						
						html += "<div>["+item.logdate+"]&nbsp;"+item.dname+"&nbsp;"+item.name+"&nbsp;"+item.pname+"&nbsp;<span style='color: red; font-weight: bold;'>"+logstatus+"</span></div>";						
						html += "<hr style='margin: 2px;'>";
					});
				}
				
				$("span#logDisplay").html(html);
				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}// end of function goViewOpinion(){}--------------------
	
	
	// 도장찍기
	function goViewStamp(){
		$.ajax({
			url:"<%= ctxPath%>/t1/approvalLogList.tw",
			data:{"parentAno":"${requestScope.avo.ano}"},
			dataType:"json",
			success:function(json){				
								
				var html = "";
				
				if(json.length > 0){
					$.each(json, function(index, item){
						
						var approvalImg1 = "<%= ctxPath%>/resources/images/sia/approval_1.png";
						var approvalImg2 = "<%= ctxPath%>/resources/images/sia/approval_2.png";
						var approvalImg3 = "<%= ctxPath%>/resources/images/sia/approval_3.png";
						var rejectedImg1 = "<%= ctxPath%>/resources/images/sia/rejected_1.png";
						var rejectedImg2 = "<%= ctxPath%>/resources/images/sia/rejected_2.png";
						var rejectedImg3 = "<%= ctxPath%>/resources/images/sia/rejected_3.png";
						
						var html = "";
						
						if(item.pcode == '2'){
							if(item.logstatus == '1'){
								html += "<img src='"+approvalImg1+"' style='height: 40px;'/>"
							}
							else if(item.logstatus == '2'){
								html += "<img src='"+rejectedImg1+"' style='height: 40px;'/>"
							}
							$("td#pcode2").html(html);
						}
						
						if(item.pcode == '3'){
							if(item.logstatus == '1'){
								html += "<img src='"+approvalImg2+"' style='height: 40px;'/>"
							}
							else if(item.logstatus == '2'){
								html += "<img src='"+rejectedImg2+"' style='height: 40px;'/>"
							}
							$("td#pcode3").html(html);
						}
						
						if(item.pcode == '4'){
							if(item.logstatus == '1'){
								html += "<img src='"+approvalImg3+"' style='height: 40px;'/>"
							}
							else if(item.logstatus == '2'){
								html += "<img src='"+rejectedImg3+"' style='height: 40px;'/>"
							}
							$("td#pcode4").html(html);
						}
						
					});
				}
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
</script>

<div id="containerview">	
	<h2 style="font-size: 20pt; font-weight: bold;" align="center">${requestScope.avo.scatname}</h2>
	<hr style="background-color: #395673; height: 1.5px; width: 80%;">
	<br>
	<div class="section">
		<table id="table1">		
			<tr>
				<th style="width:100px; height:40px;">대리</th>
				<th>부장</th>
				<th>사장</th>
			</tr>
			<tr>
				<td style="height:70px;" id="pcode2"></td>
				<td id="pcode3"></td>
				<td id="pcode4"></td>
			</tr>
		</table>
		
		<table id="table2">
			<tr>
				<th>문서상태</th>
				<td style="width: 35%;">
					<c:if test="${requestScope.avo.astatus eq '0'}">제출</c:if>
					<c:if test="${requestScope.avo.astatus eq '1'}">결재진행중</c:if>
					<c:if test="${requestScope.avo.astatus eq '2'}">반려</c:if>
					<c:if test="${requestScope.avo.astatus eq '3'}">승인완료</c:if>					
				</td>
				<th>문서번호</th>
				<td>${requestScope.avo.ano}</td>
			</tr>			
			<tr>
				<th>제목</th>
				<td colspan="3">${requestScope.avo.atitle}</td>
			</tr>
			<c:if test="${requestScope.avo.scat eq '1'}">
				<tr>
					<th>지출일자</th>
					<td colspan="3">${requestScope.avo.exdate}</td>
				</tr>
				<tr>
					<th>지출금액</th>
					<td colspan="3"><fmt:formatNumber value="${requestScope.avo.exprice}" pattern="#,###"></fmt:formatNumber>원</td>
				</tr>
			</c:if>
			<c:if test="${requestScope.avo.scat eq '2'}">
				<tr>
					<th>사용예정일</th>
					<td colspan="3">${requestScope.avo.codate}</td>
				</tr>
				<tr>
					<th>카드번호</th>
					<td colspan="3">${requestScope.avo.cocardnum}</td>
				</tr>
				<tr>
					<th>예상금액</th>
					<td><fmt:formatNumber value="${requestScope.avo.coprice}" pattern="#,###"></fmt:formatNumber>원</td>				
					<th>지출목적</th>
					<td>
						<c:if test="${requestScope.avo.copurpose eq '1'}">교통비</c:if>
						<c:if test="${requestScope.avo.copurpose eq '2'}">사무비품</c:if>
						<c:if test="${requestScope.avo.copurpose eq '3'}">주유비</c:if>
						<c:if test="${requestScope.avo.copurpose eq '4'}">출장비</c:if>	
						<c:if test="${requestScope.avo.copurpose eq '5'}">식비</c:if>
						<c:if test="${requestScope.avo.copurpose eq '6'}">기타</c:if>
					</td>
				</tr>
			</c:if>
			
			<tr>
				<th style="height:250px;">글내용</th>
				<td colspan="3">${requestScope.avo.acontent}</td>
			</tr>
				
			<tr>
				<th>첨부파일</th>
				<td colspan="3"><a href="<%= ctxPath%>/t1/download.tw?ano=${requestScope.avo.ano}">${requestScope.avo.orgFilename}</a></td>
			</tr>
		</table>
		
		<div align="center">상기와 같은 내용으로 <span style="font-weight: bold;">${requestScope.avo.scatname}</span> 을(를) 제출하오니 재가바랍니다.</div>
		<div align="right" style="margin: 4px 0; margin-right: 15%;">기안일: ${requestScope.avo.asdate}</div>
		<div align="right" style="margin-right: 15%;">신청자: ${requestScope.avo.dname} ${requestScope.avo.name}</div>
		
		<table id="table3">
			<tr>
				<th>결재로그</th>
				<td>
					<span id="logDisplay"></span>
				</td>
			</tr>
		</table>
		<table id="table4">
			<tr>
				<th>결재의견</th>
				<td id="opinionDisplay"></td>
			</tr>
		</table>
		
		<form name="addWriteFrm">
			<input type="hidden" name="employeeid" value="${sessionScope.loginuser.employeeid}" />
			<input type="hidden" name="name" value="${sessionScope.loginuser.name}"/>
			<input type="hidden" name="parentAno" value="${requestScope.avo.ano}" />
			
			<table id="table5">
				<tr>				
					<th>의견작성</th>
					<td style="border: solid 1px white;" align="center"> <textarea id="ocontent" name ="ocontent" style="width:100%; height: 70px; margin-left: 15px;"></textarea>					
					</td>
					<td style="width:10%; border: solid 1px white;" align="right"><input type="button" onclick="goAddWrite()" class="btn btn-info" style="margin-bottom: 5px;" value="작성"/><br>
					<input type="button" id="reset" class="btn btn-default" value="취소"/></td>				
				</tr>
			</table>
		</form>
		
		
		<div style="margin-top: 20px;">
			<span style="margin-left: 15%">
				<input type="button" class="btn" onclick="goback();" value="목록"/>
			</span>
			<span style="margin-left: 55%;">
				<input type="button" class="btn btn-primary" onclick="goApproval();" value="결재"/>				
				<input type="button" class="btn btn-danger" onclick="goReject();" value="반려"/>
			</span>
		</div>
		
		<br><br>
	</div>
	
	<form name="approvalDocu">
		<input type="hidden" name="ano" value="${ano}"/>
		<input type="hidden" name="astatus" value="${requestScope.avo.astatus}"/>
		<input type="hidden" name="arecipient1" value="${requestScope.avo.arecipient1}"/>
		<input type="hidden" name="arecipient2" value="${requestScope.avo.arecipient2}"/>
		<input type="hidden" name="arecipient3" value="${requestScope.avo.arecipient3}"/>
		<input type="hidden" name="employeeid" value="${sessionScope.loginuser.employeeid}" />
		<input type=hidden name="fk_pcode" value="${sessionScope.loginuser.fk_pcode}" />		
	</form>
	
	
	<form name="myFrm">
		<input type="hidden" name="ano" value="${ano}" />
		<input type="hidden" name="scatname" value="${scatname}" />		
		<input type="hidden" name="astatus" value="${astatus}" />
		<input type="hidden" name="fromDate" value="${fromDate}" />
		<input type="hidden" name="toDate" value="${toDate}" />
		<input type="hidden" name="scat" value="${scat}" />
		<input type="hidden" name="sort" value="${sort}" />
		<input type="hidden" name="searchWord" value="${searchWord}" />
		<input type="hidden" name="currentShowPageNo" value="${currentShowPageNo}" />
	</form>
</div>