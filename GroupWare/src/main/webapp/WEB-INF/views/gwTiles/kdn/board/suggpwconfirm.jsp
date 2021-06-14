<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<% String ctxPath = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/resources/css/kdn/board.css"/>

<style type="text/css">

   #table {border-collapse: collapse;
          width: 250px;
          }
   #table th, #table td{padding: 5px;}

</style>
<script type="text/javascript">
   $(document).ready(function(){
      
	// 완료버튼
    $("button#btnGoView").click(function(){
       
       // 글암호 유효성 검사
       var pwVal = $("input#pw").val().trim();
       if(pwVal == "") {
          alert("비밀번호를 입력하세요");
          return;
       }
       
       // 폼(form)을 전송(submit)
       var frm = document.viewPrivPostFrm;
       frm.method = "POST";
       frm.action = "<%= ctxPath%>/t1/viewSuggPost.tw?searchWord=${reqestScope.paraMap.searchWord}&gobackURL=${requestScope.gobackURL}";
       frm.submit();
    });
	
    $("input#pw").keydown(function(){
		if(event.keyCode == 13){
			var frm = document.viewPrivPostFrm;
		       frm.method = "POST";
		       frm.action = "<%= ctxPath%>/t1/viewSuggPost.tw?searchWord=${reqestScope.paraMap.searchWord}&gobackURL=${requestScope.gobackURL}";
		       frm.submit();
		}
	});
            
   });// end of $(document).ready(function(){})----------------
   
</script>

<div>
	<c:set var="gobackURL2" value="${fn:replace(requestScope.gobackURL,'&', ' ') }" />
   <form name="viewPrivPostFrm">
      <table id="table" style="display: block; margin: 15% auto 0 auto;">
         <tr>
            <td align="center">비밀번호를 입력하세요</td>
         </tr>
         <tr>
         	<td align="center">
               <input type="password" name="pw" id="pw" /> 
               <input type="hidden" name="seq" value="${requestScope.boardvo.seq}" />       
            </td>
         </tr>
         <tr>
         	<td align="center">
               <button type="button" id="btnGoView" class="btn-style">완료</button>
         <button type="button" onclick="javascript:history.back()" class="cmnt-btn-style">취소</button>      
            </td>
         </tr>
        
      </table>
   </form>
   
</div>