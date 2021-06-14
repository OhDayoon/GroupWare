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
				// console.log("thisEmailSeq : "+thisEmailSeq);
				// console.log("배열 값유무 확인 : "+arrEmailSeq.indexOf(thisEmailSeq));
				if(arrEmailSeq.indexOf(thisEmailSeq) == -1){
					arrEmailSeq.push(thisEmailSeq);
				}
			}//end of for --------------------
				console.log("최종 배열 값 :"+arrEmailSeq);
			
		} else {
			$("input[name=thisEmail]").prop('checked',false);
			arrEmailSeq = [];
			// console.log(arrEmailSeq);
		}
	});
	
	// 체크박스 개별체크시
	$("input[name=thisEmail]").change(function(){
		check_checkbox();
		var thisEmailSeq = $(this).val();
		// console.log("개별선택시 thisEmailSeq"+thisEmailSeq);
		if($(this).prop("checked") == false){
			arrEmailSeq.splice(arrEmailSeq.indexOf(thisEmailSeq),1);
		}
		if($(this).prop("checked") == true){
			arrEmailSeq.push(thisEmailSeq);
		}
		console.log("최종 배열 :"+arrEmailSeq);
	});
	
	// 완전삭제 버튼 클릭시
	$("button#delImmed").click(function(){
		str_arrEmailSeq = arrEmailSeq.toString();
		// console.log("최종 배열 string :"+str_arrEmailSeq);
		if (confirm("선택하신 메일을 완전히 삭제하시겠습니까?") == true){    //확인
			location.href="<%=ctxPath%>/t1/delImmed.tw?mailBoxNo=1&str_arrEmailSeq="+str_arrEmailSeq+"&gobackURL=${requestScope.gobackURL}";
		 }else{   //취소
		     return false;
		 }
	});
	
	// 받은메일함으로 이동 버튼 클릭시
	$("button#toMailinbox").click(function(){
		str_arrEmailSeq = arrEmailSeq.toString();
		// console.log("최종 배열 string :"+str_arrEmailSeq);
		location.href="<%=ctxPath%>/t1/moveToMailInbox.tw?str_arrEmailSeq="+str_arrEmailSeq+"&gobackURL=${requestScope.gobackURL}";
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
	
});

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

//조건검색
function goSearch(){
	var frm = document.searchFrm;
	frm.method = "get";
	frm.action = "<%=ctxPath%>/t1/mail_sent.tw";
	$("#searchFrm").submit();
}// end of function goSearch() -----------------------

</script>
<div id="mail-header" style="background-color: #e6f2ff; width: 100%; height: 120px; padding: 20px;">
	 <h4 style="margin-bottom: 20px; font-weight: bold;"><a class="anchor-style" href="<%=ctxPath%>/t1/mail_trash.tw">휴지통</a></h4>
	 <div id="left-header">
		 <button type="button" id="delImmed" class="btn-style">완전삭제</button>
		 <button type="button" id="toMailinbox" class="btn-style">받은메일함 이동</button>
		 <div id="right-header" style="float: right;">
			 <form name="searchFrm" id="searchFrm" style="display:inline-block;">
				 <select name="searchType">
				 	<option value="">선택</option>
				 	<option value="subject">제목</option>
				 	<option value="sender">받는사람</option>
				 	<option value="content">내용</option>
				 </select>
				<input type="text" />
			 	<button type="submit" name="searchWord" id="searchWord">검색</button>
			 	<select name="sizePerPage" id="sizePerPage">
				 	<option value="10">10개보기</option>
				 	<option value="20">20개보기</option>
				 	<option value="30">30개보기</option>
				 </select>
			 </form>
		 </div>
	 </div>
</div>

 <div id="mail-list">
 	<table class="table" >
 		<thead>
 			<tr>
 				<th width=3%><input type="checkbox" name="selectAll" id="selectAll" style="margin-left: 10px;"/></th>
 				<th width=3%>첨부<br>파일</th>
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
	 				<td>
	 					<c:if test="${not empty evo.fileName}">
	 						<i class="fas fa-paperclip"></i>
	 					</c:if>
	 				</td>
	 				<td>${evo.senderName}&lt;${evo.senderEmail}&gt;</td>
	 				<td>
		 				<input type="hidden" name="seq" value="${evo.seq}" />
		 				<c:if test="${evo.readStatus eq '0' }">
		 					<a href="javascript:goView('${evo.seq}')" class="anchor-style" style="font-weight:bold;">${evo.subject}</a>
		 				</c:if>
		 				<c:if test="${evo.readStatus eq '1' }">
		 					<a href="javascript:goView('${evo.seq}')" class="anchor-style">${evo.subject}</a>
		 				</c:if>
	 				</td>
	 				<td>${evo.sendingDate}</td>
	 			</tr>
	 		</c:forEach>
 		</c:if>
 		<c:if test="${empty requestScope.emailList}">
 			<tr>
 				<td colspan="6" align="center"> 휴지통에 메일이 없습니다. </td>
 			</tr>
 		</c:if>
 		</tbody>
 	</table>
 
 	<%-- 페이지 바 보여주기 --%>
 	<div align="center" style="width: 70%; margin: 20px auto;">
     	${requestScope.pageBar}
     </div>
 
 <form name="goViewFrm">
 		<input type="hidden" name="mailBoxNo" value="4">
    	<input type="hidden" name="seq"/>
    	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
    	<input type="hidden" name="searchType" />
      	<input type="hidden" name="searchWord" />
 </form>
 
 
 
 </div>
