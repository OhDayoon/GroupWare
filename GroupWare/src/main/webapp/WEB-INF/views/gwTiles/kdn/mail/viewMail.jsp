<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% String ctxPath = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/resources/css/kdn/mail.css" />

<style type="text/css">
	span.myEmail{font-weight: bold;}
</style>

<script type="text/javascript">
$(document).ready(function(){
	
	// 아이콘 hover 효과
	$('a.viewmail-icon').hover(function() {
		$(this).children().css("color", "black");
	}, function(){
		$(this).children().css("color", "#999999");
	});
	
	// 로그인한 유저의 이메일이 받는사람에 있는지 참조에 있는지 한눈에 알아보기 위해 빨간색으로 표시
	var employeeid= "${loginuser.employeeid}";
	$("span#"+employeeid).addClass("myEmail");
	
});

// 휴지통으로 이동
function moveToTrash(seq){
	
	
	var frm = document.toTrashFrm;
	frm.seq.value = seq;
	frm.searchType.value = "${requestScope.paraMap.searchType}";
    frm.searchWord.value = "${requestScope.paraMap.searchWord}";
	
	frm.method="get";
	frm.action="<%=ctxPath%>/t1/moveToTrash.tw";
	
	if (confirm("이 메일을 휴지통으로 이동하시겠습니까?") == true){    //확인
		frm.submit();
	 }else{   //취소
	     return false;
	 }
	
}
// 메일 완전 삭제
function delImmed(seq){
	var frm = document.delImmedFrm;
	frm.seq.value = seq;
	frm.searchType.value = "${requestScope.paraMap.searchType}";
    frm.searchWord.value = "${requestScope.paraMap.searchWord}";
	
	frm.method="get";
	frm.action="<%=ctxPath%>/t1/delImmed.tw";
	
	if (confirm("메일을 완전히 삭제하시겠습니까?") == true){    //확인
		frm.submit();
	 }else{   //취소
	     return false;
	 }
	
}

// 회신메일쓰기
function goReply(seq){
	var frm = document.replyFrm;
	frm.parentSeq.value = seq;
	
	frm.method="post";
	frm.action="<%=ctxPath%>/t1/new_mail.tw";
	frm.submit();
}

</script>


<div id="viewmail-container">

<c:set var="gobackURL2" value="${fn:replace(requestScope.gobackURL,'&', ' ') }" />

<c:if test="${not empty requestScope.evo}">
	<c:if test="${not empty requestScope.gobackURL}">
		<a href="<%=ctxPath%>/${requestScope.gobackURL}" class="viewmail-icon"><i class="fas fa-arrow-left fa-lg viewmail-icon" style="color: #999999; margin-top: 20px; margin-left:0;"></i></a>
	</c:if>
	<c:if test="${empty requestScope.gobackURL}">
		<a href="mail.tw"><i class="fas fa-arrow-left fa-lg viewmail-icon" style="color:black;"></i></a>
	</c:if>

	<div id="btn-container" class="float-right">
	<c:if test="${not empty requestScope.evo.previousseq}">
		<c:choose>
			<c:when test="${requestScope.mailBoxNo eq '2'}"><!-- 보낸메일함 메일 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=2&seq=${requestScope.evo.previousseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-left fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:when>
			<c:when test="${requestScope.mailBoxNo eq '3'}"><!-- 중요메일함 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=3&seq=${requestScope.evo.previousseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-left fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:when>
			<c:when test="${requestScope.mailBoxNo eq '4'}"><!-- 휴지통 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=4&seq=${requestScope.evo.previousseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-left fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:when>
			<c:otherwise><!-- 받은메일함 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=1&seq=${requestScope.evo.previousseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-left fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${not empty requestScope.evo.nextseq}">
		<c:choose>
			<c:when test="${requestScope.mailBoxNo eq '2'}"><!-- 보낸메일함 메일 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=2&seq=${requestScope.evo.nextseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-right fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:when>
			<c:when test="${requestScope.mailBoxNo eq '3'}"><!-- 중요메일함 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=3&seq=${requestScope.evo.nextseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-right fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:when>
			<c:when test="${requestScope.mailBoxNo eq '4'}"><!-- 휴지통 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=4&seq=${requestScope.evo.nextseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-right fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:when>
			<c:otherwise><!-- 받은메일함 열람의 경우 -->
				<a href="javascript:location.href='viewMail.tw?mailBoxNo=1&seq=${requestScope.evo.nextseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'" class="viewmail-icon"><i class="fas fa-chevron-right fa-lg viewmail-icon" style="color: #999999;"></i></a>
			</c:otherwise>
		</c:choose>
	</c:if>
	<c:if test="${requestScope.mailBoxNo eq '1' || requestScope.mailBoxNo eq '3'}">
		<a href="javascript:goReply('${evo.seq}')" class="viewmail-icon"><i class="fas fa-reply fa-lg viewmail-icon" style="color: #999999;"></i></a>
	</c:if>
		<c:if test="${requestScope.mailBoxNo eq '2' || requestScope.evo.moveToTrash eq '1'}"><!-- 보낸메일함, 휴지통 메일 열람의 경우 -->
			<a href="javascript:delImmed('${requestScope.evo.seq}')" class="viewmail-icon"><i class="far fa-trash-alt fa-lg viewmail-icon" style="color: #999999;"></i></a>
		</c:if>
		<c:if test="${requestScope.mailBoxNo eq '1' && requestScope.evo.moveToTrash eq '0'}"><!-- 받은메일함 열람의 경우 -->
			<a href="javascript:moveToTrash('${requestScope.evo.seq}')" class="viewmail-icon"><i class="far fa-trash-alt fa-lg viewmail-icon" style="color: #999999;"></i></a>
		</c:if>
	
	</div>
	<hr style="clear:both; margin: 10px 0 20px 0;">
	<h3>${requestScope.evo.subject}</h3>
	<h4>${requestScope.evo.senderName}&lt;${requestScope.evo.senderEmail}&gt;</h4>
	<span style="color: #999999;">보낸날짜: </span><span>${requestScope.evo.sendingDate}</span><br>
	<span style="color: #999999;">받는사람: </span>
	<span>
		<c:forEach var="receiverMap" items="${receiverList}" varStatus="status">
			<c:if test="${!status.last}">
				<span id='${receiverMap.idEmail}'>${receiverMap.receiverName}&lt;${receiverMap.receiverEmail}&gt;</span>&nbsp;,&nbsp;
			</c:if>
			<c:if test="${status.last}">
				<span id='${receiverMap.idEmail}'>${receiverMap.receiverName}&lt;${receiverMap.receiverEmail}&gt;</span>&nbsp;
			</c:if>
		</c:forEach>
	</span><br>
	
	<c:if test="${not empty ccList}">
	<span style="color: #999999;">참조메일:</span>
		<span>
			<c:forEach var="ccMap" items="${ccList}" varStatus="status">
				<c:if test="${!status.last}">
					<span  id='${ccMap.idEmail}'>${ccMap.ccName}&lt;${ccMap.ccEmail}&gt;</span>&nbsp;,&nbsp;
				</c:if>
				<c:if test="${status.last}">
					<span id='${ccMap.idEmail}'>${ccMap.ccName}&lt;${ccMap.ccEmail}&gt;</span>&nbsp;
				</c:if>
			</c:forEach>
		</span><br>
	</c:if>	
	
	<hr>
	<p>${requestScope.evo.content}</p>
	
	<c:if test="${not empty requestScope.evo.fileName}">
	<hr style="border-top: double 3px #eee; margin-top: 50px;">
		<span style="color: #999999;">첨부파일: </span>
		<span>
			<c:choose>
				<c:when test="${not empty requestScope.evo.fk_seq}"><!-- 보낸메일함 -->
					<a href="<%=ctxPath%>/t1/downloadEmailAttach.tw?seq=${requestScope.evo.seq}&mailBoxNo=2">${requestScope.evo.orgFilename}</a>
				</c:when>
				<c:otherwise><!-- 받은메일함, 중요메일함 -->
					<a href="<%=ctxPath%>/t1/downloadEmailAttach.tw?seq=${requestScope.evo.seq}&mailBoxNo=1">${requestScope.evo.orgFilename}</a>
				</c:otherwise>
			</c:choose>
		</span>
	</c:if>
	
	<hr style="clear:both; margin: 10px 0 20px 0;">
	<c:if test="${requestScope.evo.seq eq requestScope.evo.parentSeq}"><!-- 회신한 이전 메일 보기 -->
		<h3>${requestScope.evo.subject}</h3>
		<h4>${requestScope.evo.senderName}&lt;${requestScope.evo.senderEmail}&gt;</h4>
		<span style="color: #999999;">보낸날짜: </span><span>${requestScope.evo.sendingDate}</span><br>
		<span style="color: #999999;">받는사람: </span><span>${requestScope.evo.receiverName}&lt;${requestScope.evo.receiverEmail}&gt;</span><br>
		<c:if test="${not empty requestScope.evo.ccEmail}">
			<span style="color: #999999;">참조메일: </span><span>${requestScope.evo.ccEmail}</span><br>
		</c:if>
		<hr>
		<p>${requestScope.evo.content}</p>
		
		<c:if test="${not empty requestScope.evo.fileName}">
			<hr style="border-top: double 3px #eee; margin-top: 50px;">
			<span style="color: #999999;">첨부파일: </span>
			<span>
				<c:choose>
					<c:when test="${not empty requestScope.evo.fk_seq}"><!-- 보낸메일함 -->
						<a href="<%=ctxPath%>/t1/downloadEmailAttach.tw?seq=${requestScope.evo.seq}&mailBoxNo=2">${requestScope.evo.orgFilename}</a>
					</c:when>
					<c:otherwise><!-- 받은메일함, 중요메일함 -->
						<a href="<%=ctxPath%>/t1/downloadEmailAttach.tw?seq=${requestScope.evo.seq}&mailBoxNo=1">${requestScope.evo.orgFilename}</a>
					</c:otherwise>
				</c:choose>
			</span>
	</c:if>
		
	</c:if>
	
	
</c:if>	
<c:if test="${empty requestScope.evo}">
	<c:if test="${not empty requestScope.gobackURL}">
		<a href="<%=ctxPath%>/${requestScope.gobackURL}" class="viewmail-icon"><i class="fas fa-arrow-left fa-lg viewmail-icon" style="color: #999999; margin-top: 20px; margin-left:0;"></i></a>
	</c:if>
	<c:if test="${empty requestScope.gobackURL}">
		<a href="mail.tw"><i class="fas fa-arrow-left fa-lg viewmail-icon" style="color:black;"></i></a>
	</c:if>
	<h4>메일이 존재하지 않습니다.</h4>
</c:if>

</div>

 <form name="toTrashFrm">
    	<input type="hidden" name="seq" />
    	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
    	<input type="hidden" name="searchType" />
      	<input type="hidden" name="searchWord" />
 </form>
 <form name="delImmedFrm">
    	<input type="hidden" name="seq" />
    	<input type="hidden" name="mailBoxNo" value="${requestScope.mailBoxNo}" />
    	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
    	<input type="hidden" name="searchType" />
      	<input type="hidden" name="searchWord" />
 </form>
 <form name="replyFrm">
    	<input type="hidden" name="groupno" value="${requestScope.evo.groupno}" />
    	<input type="hidden" name="depthno" value="${requestScope.evo.depthno}"/>
    	<input type="hidden" name="parentSeq" />
      	<input type="hidden" name="subject" value="${requestScope.evo.subject}"/>
      	<input type="hidden" name="replyEmail" value="${requestScope.evo.senderEmail}"/>
      	<input type="hidden" name="replyToName" value="${requestScope.evo.senderName}"/>
 </form>