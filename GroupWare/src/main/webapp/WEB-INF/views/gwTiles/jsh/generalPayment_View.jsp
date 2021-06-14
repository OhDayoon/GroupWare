<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<% String ctxPath = request.getContextPath(); %>

<style>
h3{
	font-weight:bold;
}
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
button.btn {
	margin-left:20px;
	border-radius: 10px;
	font-weight: bold;
}
td.opinion{
	border: solid 1px white;
}
td.log{
	border: solid 1px white;
}

button.btn1:hover{
 background-color: #c3c6c9;
}
</style>

<script type="text/javascript">
		$(document).ready(function(){
			//사이드바 세부메뉴 나타내기 
		//	$("div#submenu3").show();
			
			
			
		});
</script>

<div id="containerview">
	<hr style="border: 2px gray;">
		<h3 align="center">${requestScope.ncatname}</h3>
	<hr>
	<br>
	<div class="section"  >
		<table id="table1">
		
			<tr>
				<th style="width:100px; height:70px; ">대리</th>
				<th>부장</th>
				<th>사장</th>
			</tr>
			
			<tr>
				<td style="height:70px;">
					<c:if test="${not empty requestScope.alogList}">
						<c:forEach var="lvo" items="${requestScope.alogList}">
							<c:if test="${lvo.pcode == '2' && lvo.logstatus == '0'}">
								미결재
							</c:if>
							<c:if test="${lvo.pcode == '2' && lvo.logstatus == '1'}">
								<img src="<%= ctxPath%>/resources/images/sia/approval_1.png" style="height: 40px;"/>
							</c:if>
							<c:if test="${lvo.pcode == '2' && lvo.logstatus == '2'}">
								<img src="<%= ctxPath%>/resources/images/sia/rejected_1.png" style="height: 40px;"/>
								
							</c:if>
						</c:forEach>
					</c:if>
				</td>
				<td>
					<c:if test="${not empty requestScope.alogList}">
						<c:forEach var="lvo" items="${requestScope.alogList}">
							<c:if test="${lvo.pcode == '3' && lvo.logstatus == '0'}">
								미결재
							</c:if>
							<c:if test="${lvo.pcode == '3' && lvo.logstatus == '1'}">
							   <img src="<%= ctxPath%>/resources/images/sia/approval_2.png" style="height: 40px;"/>
							</c:if>
							<c:if test="${lvo.pcode == '3' && lvo.logstatus == '2'}">
								 <img src="<%= ctxPath%>/resources/images/sia/rejected_2.png" style="height: 40px;"/>
							</c:if>
						</c:forEach>
					</c:if>
				</td>
				<td>
					<c:if test="${not empty requestScope.alogList}">
						<c:forEach var="lvo" items="${requestScope.alogList}">
							<c:if test="${lvo.pcode == '4' && lvo.logstatus == '0'}">
								미결재
							</c:if>
							<c:if test="${lvo.pcode == '4' && lvo.logstatus == '1'}">
							 	<img src="<%= ctxPath%>/resources/images/sia/approval_3.png" style="height: 40px;"/>
							</c:if>
							<c:if test="${lvo.pcode == '4' && lvo.logstatus == '2'}">
								<img src="<%= ctxPath%>/resources/images/sia/rejected_3.png" style="height: 40px;"/>
							</c:if>
						</c:forEach>
					</c:if>
				</td>
				
			</tr>
		</table>
		
		
		<table id="table2">
			<tr>
				<th style="width:200px;">수신참조</th>
				<td><span style="font-weight: bold;">${requestScope.receiver.name}&nbsp; ${requestScope.receiver.pname}</span>(<span style="color: blue;">${requestScope.receiver.dname}</span>)</td>
			</tr>
			
			<tr>
				<th>제목</th>
				<td>${epvo.atitle}</td>
			</tr>
		
			
			<tr>
				<th>작성자</th>
				<td>${epvo.name}</td>
			</tr>
		
			<tr>
				<th>소속</th>
				<td>${epvo.dname}</td>
			</tr>
			<c:if test="${requestScope.ncatname eq '회의록'}">
			<tr>
				<th>회의시간</th>
				<td>${epvo.mdate}</td>
			</tr>
			</c:if>
			<c:if test="${requestScope.ncatname eq '위임장'}">
			<tr>
				<th>위임기간</th>
				<td>${epvo.fk_wiimdate}</td>
			</tr>
			</c:if>
			<c:if test="${requestScope.ncatname eq '협조공문'}">
			<tr>
				<th>타회사명</th>
				<td>${epvo.comname}</td>
			</tr>
			</c:if>
			<tr>
				<th>결재상태</th>
				<c:if test="${epvo.astatus==0}">
					<td>제출</td>
				</c:if>
				<c:if test="${epvo.astatus==1}">
					<td>결재진행중</td>
				</c:if>
				<c:if test="${epvo.astatus==2}">
					<td>반려</td>
				</c:if>
				<c:if test="${epvo.astatus==3}">
					<td>결재완료</td>
				</c:if>
				
			</tr>
			
			<tr>
				<th>문서번호</th>
				<td>${epvo.ano}</td>
			</tr>
			
			<tr>
				<th style="height:350px;">글내용</th>
				<td>${epvo.acontent}</td>
			</tr>
			
			<tr>
				<th>첨부파일</th>
				<td>
					<c:if test="${not empty epvo.orgFilename}">
					   <a href ="<%= ctxPath%>/download.tw?ano=${requestScope.epvo.ano}">${requestScope.epvo.orgFilename}</a>
					   <tr>
				            <th>파일크기(bytes)</th>
				            <td colspan="3"><fmt:formatNumber value="${requestScope.epvo.fileSize}" pattern="#,###"></fmt:formatNumber></td>
		         		</tr>
					</c:if>
					
					<c:if test="${empty epvo.orgFilename}">
					   <div align="center" style="color:#22a0d6;">첨부파일이 존재하지 않습니다.</div>
					</c:if>
				</td>
			</tr>
		
		
		</table>
		
		<div align="center">상기와 같은 내용으로 <span style="font-weight: bold;">${requestScope.epvo.ncatname}</span> 을(를) 제출하오니 재가바랍니다.</div>
		<div align="right" style="margin: 4px 0; margin-right: 15%;">기안일: ${requestScope.epvo.asdate}</div>
		<div align="right" style="margin-right: 15%;">신청자: <span style="font-weight:bold;">${requestScope.epvo.name} </span> ${requestScope.epvo.pname} (${requestScope.epvo.dname})</div>	
		
		
		
		<table id="table3">
			<tr>
				<th style="width:20%;">결재로그</th>
				<td class="log">
					<c:if test="${not empty requestScope.alogList}">
						<c:forEach var="lvo" items="${requestScope.alogList}">
							<c:if test="${lvo.logstatus == '0'}">
							  	<div><span style="color:#87898c;">${lvo.logdate}&nbsp;</span>
							  	${lvo.dname}&nbsp;<span style="font-weight:bold;">${lvo.name}
							  	</span> &nbsp;${lvo.pname}<span style="font-weight:bold;"> [제출]</span></div>
							  </c:if>
							  <c:if test="${lvo.logstatus == '1'}">
							  	<div><span style="color:#87898c;">${lvo.logdate}&nbsp;</span>
							  	${lvo.dname}&nbsp;<span style="font-weight:bold;">${lvo.name}</span>
							  	 &nbsp;${lvo.pname}<span style="color:#1b4de3; font-weight:bold;"> [승인]</span></div>
							  </c:if>
							  <c:if test="${lvo.logstatus == '2'}">
							  	<div><span style="color:#87898c;">${lvo.logdate}&nbsp;</span>
							  	${lvo.dname}&nbsp;<span style="font-weight:bold;">${lvo.name}</span> 
							  	&nbsp;${lvo.pname}<span style="color:red; font-weight:bold;"> [반려]</span></div>
							  </c:if>
						  
						</c:forEach>
					</c:if>
					<c:if test="${empty requestScope.alogList}">
					  <div style="color:#22a0d6;"> 결재 로그가 존재하지 않습니다. </div>
					</c:if>
				</td>
			</tr>
		</table>
		<table id="table4">
			<tr>
				<th style="width:20%;">결재의견</th>
				<td class="opinion">
					<c:if test="${not empty requestScope.opinionList}">
						<c:forEach var="ovo" items="${requestScope.opinionList}">
						  <div style="font-weight:bold;">${ovo.dname}&nbsp;${ovo.name}&nbsp;${ovo.pname}&nbsp;&nbsp;<span style="color:#87898c;">[${ovo.odate}]</span></div>
						  <div>>>> &nbsp;<span>${ovo.ocontent}</span></div>
						</c:forEach>
					</c:if>
					
					<c:if test="${empty requestScope.opinionList}">
					  <div style="color:#22a0d6;"> 의견이 존재하지 않습니다. </div>
					</c:if>
				</td>
			</tr>
		</table>
		
	</div>

  
	<div style="margin-top: 20px; margin-left:950px">
		<button type="button" class="btn btn1" onclick="javascript:location.href='<%=ctxPath %>/t1/generalPayment_List.tw'" >전체목록보기</button>
     	<button type="button"  class="btn btn-primary" onclick="javascript:location.href='<%=ctxPath %>/${requestScope.gobackURL}'" >검색결과목록보기</button>
	</div>
	<br><br>
</div>

