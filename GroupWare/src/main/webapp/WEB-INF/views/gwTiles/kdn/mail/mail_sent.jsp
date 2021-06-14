<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String ctxPath = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/resources/css/kdn/mail.css" />

<script type="text/javascript">
$(document).ready(function(){
	
	var arrEmailSeq = [];
	var str_arrEmailSeq = ""; 
	// 체크박스 전체선택/전체해제
	$("input#selectAll").change(function(){
		if($("input#selectAll").prop("checked")){
			$("input[name=thisEmail]").prop('checked',true);
			var arrThisEmail = document.getElementsByName("thisEmail");
			for(var i=0; i<arrThisEmail.length; i++){
				var thisEmailSeq = arrThisEmail[i].value;
				/* console.log("thisEmailSeq : "+thisEmailSeq); */
				/* console.log("배열 값유무 확인 : "+arrEmailSeq.indexOf(thisEmailSeq)); */
				if(arrEmailSeq.indexOf(thisEmailSeq) == -1){
					/* arrEmailSeq.splice(arrEmailSeq.indexOf(thisEmailSeq),1); */
					arrEmailSeq.push(thisEmailSeq);
				}
			}//end of for --------------------
				/* console.log("최종 배열 값 :"+arrEmailSeq); */
			
		} else {
			$("input[name=thisEmail]").prop('checked',false);
			arrEmailSeq = [];
			/* console.log(arrEmailSeq); */
		}
	});
	
	$("input[name=thisEmail]").change(function(){
		check_checkbox();
		var thisEmailSeq = $(this).val();
		console.log("개별선택시 thisEmailSeq"+thisEmailSeq);
		if($(this).prop("checked") == false){
			arrEmailSeq.splice(arrEmailSeq.indexOf(thisEmailSeq),1);
		}
		if($(this).prop("checked") == true){
			arrEmailSeq.push(thisEmailSeq);
		}
		console.log(arrEmailSeq);
	});
	
	var gobackURL = "${requestScope.gobackURL}";
	gobackURL = gobackURL.replaceAll('&', ' ');
	
	// 메일 완전삭제
	$("button#delImmed").click(function(){
		
		str_arrEmailSeq = arrEmailSeq.toString();
		/* console.log("최종 배열 :"+str_arrEmailSeq); */
		if (confirm("선택하신 메일을 완전히 삭제하시겠습니까?") == true){    //확인
			location.href="<%=ctxPath%>/t1/delImmed.tw?mailBoxNo=2&str_arrEmailSeq="+str_arrEmailSeq+"&gobackURL="+gobackURL;
		 }else{   //취소
		     return false;
		 }
		
	});
	
	//읽음표시 변경
	$("select#readStatus").change(function(){
		if($(this).val() == "0"){
			str_arrEmailSeq = arrEmailSeq.toString();
			//console.log("최종 배열 :"+str_arrEmailSeq);
			if(str_arrEmailSeq != ""){
				//console.log("바꾸기 전:"+gobackURL);
				//console.log(gobackURL);
				location.href="<%=ctxPath%>/t1/readStatus.tw?mailBoxNo=4&readStatus=0&str_arrEmailSeq="+str_arrEmailSeq+"&gobackURL="+gobackURL;
			}
			$(this).val("");
		} else if($(this).val() == "1") {
			str_arrEmailSeq = arrEmailSeq.toString();
			//console.log("최종 배열 :"+str_arrEmailSeq);
			if(str_arrEmailSeq != ""){
				location.href="<%=ctxPath%>/t1/readStatus.tw?mailBoxNo=4&readStatus=1&str_arrEmailSeq="+str_arrEmailSeq+"&gobackURL="+gobackURL;
			}
			$(this).val("");
		} else {
			$(this).val("");
		}
	});
	
	// 조건검색
	$("input#searchWord").keydown(function(){
		if(event.keyCode == 13){  //엔터 했을 경우
			goSearch();
		}
	});
	
	//보기개수 변경
	$("select#sizePerPage").change(function(){
			goSearch();
	});
	
	//보기개수 선택시 선택값 유지시키기
	if(${not empty requestScope.paraMap}){
		$("select#searchType").val("${requestScope.paraMap.searchType}");
		$("input#searchWord").val("${requestScope.paraMap.searchWord}");
	  	$("select#sizePerPage").val("${requestScope.paraMap.sizePerPage}");
	  }
	
});//end of $(document).ready(function() -------------------

//체크박스 체크유무검사
function check_checkbox(){
	var arrChckbox = document.getElementsByName("thisEmail");
	var bFlag = false;
	
	for(var i=0; i<arrChckbox.length; i++){
		if(!arrChckbox[i].checked){
			bFlag = true;		
			break;
		}
	}//end of for --------------------
	
	if(bFlag){	//하위 체크박스 중 1개라도 체크가 해제된 경우
		document.getElementById("selectAll").checked = false;
	}
	else {	//하위 체크박스가 모두 체크된 경우
		document.getElementById("selectAll").checked = true;
	}
}// end of function func_chinaCheck()---------------------------------------

// 메일 열람하기
function goView(seq){
		console.log(seq);		
		var frm = document.goViewFrm;
		frm.fk_seq.value = seq;
		frm.searchType.value = "${requestScope.paraMap.searchType}";
	    frm.searchWord.value = "${requestScope.paraMap.searchWord}";
	    
		frm.method="get";
		frm.action="<%=ctxPath%>/t1/viewMail.tw";
		frm.submit();
		
	}//end of function goView('${boardvo.seq}') ---------------

// 조건검색
function goSearch(){
	var frm = document.searchFrm;
	frm.method = "get";
	frm.action = "<%=ctxPath%>/t1/mail_sent.tw";
	$("#searchFrm").submit();
}// end of function goSearch() -----------------------
	
</script>
<div id="mail-header" style="background-color: #e6f2ff; width: 100%; height: 120px; padding: 20px;">
	 <h4 style="margin-bottom: 20px; font-weight: bold;"><a class="anchor-style" href="<%=ctxPath%>/t1/mail_sent.tw">보낸메일함</a></h4>
	 <div id="left-header">
		 <button id="delImmed" type="button" class="btn-style"><i class="fas fa-times fa-lg"></i>&nbsp;완전삭제</button>
		 <select id="readStatus">
		 	<option value="">읽음표시</option>
		 	<option value="1">읽음</option>
		 	<option value="0">읽지않음</option>
		 </select>
	 <div id="right-header" style="float: right;">
	 	<form name="searchFrm" id="searchFrm" style="display:inline-block;">
			 <select name="searchType">
			 	<option value="subject">제목</option>
			 	<option value="sender">받는사람</option>
			 	<option value="content">내용</option>
			 </select>
			<input type="text" name="searchWord" id="searchWord"/>
		 	<button type="submit" onclick="goSearch()" class="btn-style">검색</button>
		 	<select name="sizePerPage" id="sizePerPage">
			 	<option value="10">10개보기</option>
			 	<option value="15">15개보기</option>
			 	<option value="20">20개보기</option>
			 </select>
	 	</form>
	 </div>
	 </div>
</div>

 <div id="mail-list" style="height: 100%;">
 	<table class="table" >
 		<thead>
 			<tr>
 				<th width=3%><input type="checkbox" name="selectAll" id="selectAll" style="margin-left: 10px;"/></th>
 				<th width=3%>첨부파일</th>
 				<th width=13%>받는사람</th>
 				<th width=67%>제목</th>
 				<th width=20%>발신일시</th>
 			</tr>
 		</thead>
 		<tbody>
 		<c:if test="${not empty requestScope.emailList}"> 		
	 		<c:forEach var="evo" items="${requestScope.emailList}" varStatus="status">
	 			<tr>
	 				<td><input type="checkbox" name="thisEmail" id="thisEmail" value="${evo.seq}" style="margin-left: 10px;"/></td>
	 				<td>
	 					<c:if test="${not empty evo.fileName}">
	 						<i class="fas fa-paperclip"></i>
	 					</c:if>
	 				</td>
	 				
	 				<%--받는사람이 여러명인 경우와 한명인 경우 고려 시작 --%>
	 				<td>
		 				<c:if test="${empty evo.receiverCnt}">
		 					${evo.receiverName}&lt;${evo.receiverEmail}&gt;
		 				</c:if>
	 					<c:if test="${not empty evo.receiverCnt}">
		 					${evo.receiverName}&lt;${evo.receiverEmail}&gt;&nbsp;외&nbsp;${evo.receiverCnt}명
		 				</c:if>
	 				</td>
	 				<%--받는사람이 여러명인 경우와 한명인 경우 고려 끝 --%>
	 				
	 				<td>
	 				<input type="hidden" name="seq" value="${evo.seq}" />
	 				<c:if test="${evo.readStatus eq '0' }">
		 				<a href="javascript:goView('${evo.fk_seq}')" class="anchor-style" style="font-weight:bold;">${evo.subject}</a>
		 			</c:if>
		 			<c:if test="${evo.readStatus eq '1' }">
		 				<a href="javascript:goView('${evo.fk_seq}')" class="anchor-style">${evo.subject}</a>
		 			</c:if>
	 				</td>
	 				<td>${evo.sendingDate}</td>
	 			</tr>
	 		</c:forEach>
 		</c:if>
 		<c:if test="${empty requestScope.emailList}">
 			<tr>
 				<td colspan="6" align="center"> 저장된 보낸메일이 없습니다. </td>
 			</tr>
 		</c:if>
 		</tbody>
 	</table>
 	
 	<%-- 페이지 바 보여주기 --%>
 	<div align="center" style="width: 70%; margin: 20px auto;">
     	${requestScope.pageBar}
     </div>
 	
 	
 	 <form name="goViewFrm">
 	 	<input type="hidden" name="mailBoxNo" value="2">
    	<input type="hidden" name="fk_seq"/>
    	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
    	<input type="hidden" name="searchType" />
      	<input type="hidden" name="searchWord" />
 	</form>
 
 </div>
