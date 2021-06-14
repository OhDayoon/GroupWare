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
		$("div#submenu3").show();
		
		 }); //end of $(document).ready(function(){})----------------------
	
		
</script>

<div id="containerview">
	<hr style="border: 2px gray;">
	<h3 align="center">${requestScope.vcatname}</h3>
<hr>
<br>
<div class="section" >
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
			<th>문서상태</th>
			<td style="width: 35%;">
				<c:if test="${requestScope.epvo.astatus eq '0'}">제출</c:if>
				<c:if test="${requestScope.epvo.astatus eq '1'}">결재진행중</c:if>
				<c:if test="${requestScope.epvo.astatus eq '2'}">반려</c:if>
				<c:if test="${requestScope.epvo.astatus eq '3'}">결재완료</c:if>					
			</td>
			<th>문서번호</th>
			<td>${requestScope.epvo.ano}</td>
		</tr>
		<tr>
			<th>제목</th>
			<td colspan="3">${requestScope.epvo.atitle}</td>
		</tr>
		
		<c:if test="${requestScope.epvo.vcatname eq '병가'}"> 
			<tr>
				<th>요청기간</th>
				<td colspan="3">${requestScope.epvo.slstart} - ${requestScope.epvo.slend}&nbsp;&nbsp;&nbsp;[사용일수: <span style="color: blue; font-weight: bold;">${requestScope.epvo.sldates}</span>일]</td>
			</tr>				
		</c:if>
		<c:if test="${requestScope.epvo.vcatname eq '반차'}">
			<tr>
				<th>요청기간</th>
				<td colspan="3">${requestScope.epvo.afdate}&nbsp;&nbsp;
				<span style="color: #22a0d6; font-weight: bold;"><c:if test="${requestScope.epvo.afdan eq '1'}">오전&nbsp;</c:if><c:if test="${requestScope.epvo.afdan eq '2'}">오후&nbsp;</c:if></span>반차
				</td>
			</tr>
		</c:if>
		<c:if test="${requestScope.epvo.vcatname eq '연차'}">
			<tr>
				<th>요청기간</th>
				<td colspan="3">${requestScope.epvo.daystart} - ${requestScope.epvo.dayend}&nbsp;&nbsp;&nbsp;[<span style="color: #22a0d6; font-weight: bold;">사용일수: ${requestScope.epvo.daydates}일</span>]</td>
			</tr>
		</c:if>
		<c:if test="${requestScope.epvo.vcatname eq '경조휴가'}">
			<tr>
				<th>요청기간</th>
				<td colspan="3">${requestScope.epvo.congstart} - ${requestScope.epvo.congend}&nbsp;&nbsp;&nbsp;[<span style="color: #22a0d6; font-weight: bold;">사용일수: ${requestScope.epvo.congdates}일</span>]</td>
			</tr>
		</c:if>
		<c:if test="${requestScope.epvo.vcatname eq '출장'}">
			<tr>
				<th>출장기간</th>
				<td colspan="3">${requestScope.epvo.bustart} - ${requestScope.epvo.buend}</td>
			</tr>
			<tr>
				<th>출장지</th>
				<td>${requestScope.epvo.buplace}</td>				
				<th>출장인원</th>
				<td>${requestScope.epvo.bupeople}</td>
			</tr>
		</c:if>
		<c:if test="${requestScope.epvo.vcatname eq '추가근무'}">
			<tr>
				<th>요청시간</th>
				<td colspan="3">${requestScope.epvo.ewdate}시간</td>
			</tr>
		</c:if>	
		
		<tr>
			<th style="height:250px;">글내용</th>
			<td colspan="3">${requestScope.epvo.acontent}</td>
		</tr>
			
		<tr>
		<th>첨부파일</th>
			<td colspan="3">
				<c:if test="${not empty epvo.orgFilename}">
				   <a href ="<%= ctxPath%>/download2.tw?ano=${requestScope.epvo.ano}">${requestScope.epvo.orgFilename}</a>
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
			
	<div align="center">상기와 같은 내용으로 <span style="font-weight: bold;">${requestScope.epvo.vcatname}계</span> 을(를) 제출하오니 재가바랍니다.</div>
	<div align="right" style="margin: 4px 0; margin-right: 15%;">기안일: ${requestScope.epvo.asdate}</div>
	<div align="right" style="margin-right: 15%;">신청자: <span style="font-weight:bold;">${requestScope.epvo.name} </span> ${requestScope.epvo.pname} (${requestScope.epvo.dname})</div>	
		
		
	<table id="table3">
			<tr>
				<th style="width:20%;">결재로그</th>
				<td class="log">
					<c:if test="${not empty requestScope.alogList}">
						<c:forEach var="lvo" items="${requestScope.alogList}">
							<c:if test="${lvo.logstatus == '0'}">
							  	<div><span style="color:#87898c;">${lvo.logdate}&nbsp;</span>${lvo.dname}&nbsp;<span style="font-weight:bold;">${lvo.name}</span> &nbsp;${lvo.pname}<span style="font-weight:bold;"> [제출]</span></div>
							  </c:if>
							  <c:if test="${lvo.logstatus == '1'}">
							  	<div><span style="color:#87898c;">${lvo.logdate}&nbsp;</span>${lvo.dname}&nbsp;<span style="font-weight:bold;">${lvo.name}</span> &nbsp;${lvo.pname}<span style="color:#1b4de3; font-weight:bold;"> [승인]</span></div>
							  </c:if>
							  <c:if test="${lvo.logstatus == '2'}">
							  	<div><span style="color:#87898c;">${lvo.logdate}&nbsp;</span>${lvo.dname}&nbsp;<span style="font-weight:bold;">${lvo.name}</span> &nbsp;${lvo.pname}<span style="color:red; font-weight:bold;"> [반려]</span></div>
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
		<button type="button" class="btn btn1" onclick="javascript:location.href='<%=ctxPath %>/t1/vacation_List.tw'" >전체목록보기</button>
     	<button type="button"  class="btn btn-primary" onclick="javascript:location.href='<%=ctxPath %>/${requestScope.gobackURL}'" >검색결과목록보기</button>
	</div>
	<br><br>
		
</div>
		