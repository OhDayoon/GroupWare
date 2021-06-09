<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
				console.log("최종 배열 값 :"+arrEmailSeq);
			
		} else {
			$("input[name=thisEmail]").prop('checked',false);
			arrEmailSeq = [];
			/* console.log(arrEmailSeq); */
		}
	});
	
	//체크박스 개별체크시
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
		console.log("개별체크 후 최종 배열값: "+arrEmailSeq);
	});
	
	//중요표시해제
	$("button#goStar").click(function(){
		str_arrEmailSeq = arrEmailSeq.toString();
		console.log("최종 배열 :"+str_arrEmailSeq);
		location.href="<%=ctxPath%>/t1/goStar.tw?mailBoxNo=2&checkImportant=0&str_arrEmailSeq="+str_arrEmailSeq;
		
	});
	
	//읽음표시 변경
	$("select#readStatus").change(function(){
		if($(this).val() == "0"){
			str_arrEmailSeq = arrEmailSeq.toString();
			//console.log("최종 배열 :"+str_arrEmailSeq);
			if(str_arrEmailSeq != "0"){
				location.href="<%=ctxPath%>/t1/readStatus.tw?mailBoxNo=3&readStatus=0&str_arrEmailSeq="+str_arrEmailSeq;
			}
			$(this).val("");
		} else if($(this).val() == "1") {
			str_arrEmailSeq = arrEmailSeq.toString();
			//console.log("최종 배열 :"+str_arrEmailSeq);
			if(str_arrEmailSeq != ""){
				location.href="<%=ctxPath%>/t1/readStatus.tw?mailBoxNo=3&readStatus=1&str_arrEmailSeq="+str_arrEmailSeq;
			}
			$(this).val("");
		} else {
			$(this).val("");
		}
	});

});//$(document).ready(function() ---------------------------


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

function goView(seq){
		var frm = document.goViewFrm;
		frm.seq.value = seq;
		frm.searchType.value = "${requestScope.paraMap.searchType}";
	    frm.searchWord.value = "${requestScope.paraMap.searchWord}";
		
	    frm.method="get";
		frm.action="<%=ctxPath%>/t1/viewMail.tw";
		frm.submit();
	    
		
	}//end of function goView('${boardvo.seq}') ---------------
	
function goStar(){
	str_arrEmailSeq = arrEmailSeq.toString();
	console.log("최종 배열 :"+str_arrEmailSeq);
	<%-- location.href="<%=ctxPath%>/t1/goStar.tw?seq=${evo.seq}&str_arrEmailSeq="+str_arrEmailSeq; --%>
	}
	
</script>
<div id="mail-header" style="background-color: #e6f2ff; width: 100%; height: 120px; padding: 20px;">
	 <h4 style="margin-bottom: 20px; font-weight: bold;">중요메일함</h4>
	 <div id="left-header">
		 <button type="button" id="goStar" class="btn-style">중요표시해제</button>
		 <select id="readStatus">
		 	<option value="">읽음표시</option>
		 	<option value="1">읽음</option>
		 	<option value="0">읽지않음</option>
		 </select>
	 <div id="right-header" style="float: right;">
		 <select name="mailSearch">
		 	<option value="">선택</option>
		 	<option value="subject">제목</option>
		 	<option value="sender">받는사람</option>
		 	<option value="content">내용</option>
		 </select>
		<input type="text" />
	 	<button type="submit">검색</button>
	 	<select name="numberOfEmails">
		 	<option value="10">10개보기</option>
		 	<option value="20">20개보기</option>
		 	<option value="30">30개보기</option>
		 </select>
	 </div>
	 </div>
</div>

 <div id="mail-list">
 	<table class="table" >
 		<thead>
 			<tr>
 				<th width=3%><input type="checkbox" name="selectAll" id="selectAll" style="margin-left: 10px;"/></th>
 				<th width=3%>중요</th>
 				<th width=3%>첨부파일</th>
 				<th width=13%>보낸사람</th>
 				<th width=67%>제목</th>
 				<th width=20%>수신일시</th>
 			</tr>
 		</thead>
 		<tbody>
 		<c:if test="${not empty requestScope.emailList}"> 		
	 		<c:forEach var="evo" items="${requestScope.emailList}" varStatus="status">
	 			<tr>
	 				<td><input type="checkbox" name="thisEmail" id="thisEmail" value="${evo.seq}" style="margin-left: 10px;"/></td>
	 				<td >
	 					<c:if test="${evo.checkImportant eq '0'}">
	 						<i class="far fa-star"></i>
	 					</c:if>
	 					<c:if test="${evo.checkImportant eq '1'}">
	 						<i class="fas fa-star" style="color: #ffcc00;"></i>
	 					</c:if>
	 				</td>
	 				<td>
	 					<c:if test="${not empty evo.fileName}">
	 						<i class="fas fa-paperclip"></i>
	 					</c:if>
	 				</td>
	 				<td>${evo.senderName}&lt;${evo.senderEmail}&gt;</td>
	 				<td>
	 				<input type="hidden" name="seq" value="${evo.seq}" />
	 				<c:if test="${evo.readStatus eq '0' }">
		 				<a href='javascript:goView("${evo.seq}")' class="anchor-style" style="font-weight: bolder;">${evo.subject}</a>
	 				</c:if>
	 				<c:if test="${evo.readStatus eq '1' }">
		 				<a href='javascript:goView("${evo.seq}")' class="anchor-style">${evo.subject}</a>
	 				</c:if>
	 				</td>
	 				<td>${evo.sendingDate}</td>
	 			</tr>
	 		</c:forEach>
 		</c:if>
 		<c:if test="${empty requestScope.emailList}">
 			<tr>
 				<td colspan="6" align="center"> 중요메일이 없습니다. </td>
 			</tr>
 		</c:if>
 		</tbody>
 	</table>
 	
 	<%-- 페이지 바 보여주기 --%>
 	<div align="center" style="width: 70%; margin: 20px auto;">
     	${requestScope.pageBar}
     </div>
 	
 	
 	
 	 <form name="goViewFrm">
 	 <input type="hidden" name="mailBoxNo" value="3">
    	<input type="hidden" name="seq"/>
    	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
    	<input type="hidden" name="searchType" />
      	<input type="hidden" name="searchWord" />
	 </form>
 
 </div>
