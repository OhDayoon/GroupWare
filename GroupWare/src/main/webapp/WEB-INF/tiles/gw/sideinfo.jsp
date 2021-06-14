<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath();%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(document).ready(function(){
	
	$.ajax({
		url:"<%=ctxPath%>/t1/displayTotalEmail.tw",
		dataType:"json",
		success:function(json){
			var totalEmailCount = json.totalEmailCount;
			var totalUnreadEmail = json.totalUnreadEmail;
			
			$("span#totalUnreadEmail").children().text(totalUnreadEmail);
			$("span#totalEmail").text(totalEmailCount);
		},
		error: function(request, status, error){
			alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
 		}
		
	});
	
	
});

function goEmptyTrash(){
	if (confirm("휴지통을 비우시겠습니까?") == true){    //확인
		location.href="<%=ctxPath%>/t1/emptyTrash.tw";
	 }else{   //취소
	     return false;
	 }
	
}

</script>

<div id="contentWrapper">
   <!-- 사이드바 -->
   <div id="sidemenu">
      <p class="menu-btn mail" id="mail"><i class="far fa-envelope fa-lg"></i><br><span>메일</span></p>
      <p class="menu-btn myDocument"><i class="far fa-file-alt fa-lg"></i><br><span>내문서함</span></p>
      <p class="menu-btn electronPay"><i class="fas fa-desktop fa-lg"></i><br><span>전자결재</span></p>
      <p class="menu-btn employeeMap"><i class="far fa-address-card fa-lg"></i><br><span>주소록</span></p>
      <p class="menu-btn employeeBoard"><i class="far fa-list-alt fa-lg"></i><br><span>게시판</span></p>
      <p class="menu-btn salary"><i class="fas fa-won-sign fa-md"></i><br><span>월급관리</span></p>
      <p class="menu-btn toDo"><i class="fas fa-file-signature fa-lg"></i><br><span>업무관리</span></p>
      <p class="menu-btn reservation"><i class="fas fa-tasks fa-lg"></i><br><span>예약관리</span></p>
   </div>

   <!-- 세부메뉴  -->
    <div id="contentLeft" >
       <!-- 메일 -->
       <div id="submenu1" class="mail">
         <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
               <li>
                   <a href="<%= ctxPath%>/t1/new_mail.tw">메일쓰기</a>                
               </li>
               <li>
                   <a href="<%= ctxPath%>/t1/mail.tw">받은메일함&nbsp;<span id="totalUnreadEmail"><b></b></span>&nbsp;/&nbsp;<span id="totalEmail"></span></a>
               </li>
               <li>
                   <a href="<%= ctxPath%>/t1/mail_sent.tw">보낸메일함</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/mail_important.tw">중요메일함</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/mail_trash.tw" style="width:150px; display:inline-block;">휴지통</a>
                   <a class="delicon" href="javascript:goEmptyTrash()" style="display:inline-block; width:46px;"><i class="far fa-trash-alt" style="float:right; margin-right: 15px; font-size: 12pt;"></i></a>
               </li>
           </ul>
        </div>
        <!-- 내문서함 -->
        <div id="submenu2" class="myDocument">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
               <li class="active">
                   <a href="<%=ctxPath%>/t1/myDocuNorm_rec.tw">수신함</a>
                  <ul>
                       <li>
                           <a href="<%=ctxPath%>/t1/myDocuNorm_rec.tw">미결재문서</a>
                       </li>
                       <li>
                           <a href="<%=ctxPath%>/t1/myDocuNorm_complete.tw">결재완료문서</a>
                       </li>
                   </ul>
               </li>
               <li class="clickable">
                   <a href="<%=ctxPath%>/t1/myDocuNorm_send.tw">발신함</a>
               </li>
               <li class="clickable">
                   <a href="<%=ctxPath%>/t1/myDocuNorm_temp.tw">임시저장함</a>
               </li>
           </ul>
        </div>
        <!-- 전자결재 -->
        <div id="submenu3" class="electronPay">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul id="leftNavigation" class="submenu-style">
               <li class="active">
                   <a href="javascript:void(0)">일반결재</a>
                  <ul>
                       <li>
                           <a href="<%=ctxPath%>/t1/generalPayment_List.tw">결재내역</a>
                       </li>
                       <li>
                           <a href="<%=ctxPath%>/t1/generalPayment_Write.tw">결재문서 작성</a>
                       </li>
                   </ul>
               </li>
               <li>
                   <a href="javascript:void(0)">지출결재</a>
                   <ul>
                       <li>
                           <a href="<%=ctxPath%>/t1/expApproval_List.tw">결재내역</a>
                       </li>
                       <li>
                           <a href="<%=ctxPath%>/t1/expApproval_Write.tw">결재문서 작성</a>
                       </li>
                   </ul>
               </li>
               <li>
                   <a href="javascript:void(0)">근태/휴가</a>
                   <ul>
                       <li>
                           <a href="<%=ctxPath%>/t1/vacation_List.tw">결재내역</a>
                       </li>
                       <li>
                           <a href="<%=ctxPath%>/t1/vacation_Write.tw">결재문서 작성</a>
                       </li>
                   </ul>
               </li>
           </ul>
        </div>
        <!-- 주소록 -->
        <div id="submenu4" class="employeeMap">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
               <li class="clickable">
                   <a href="<%=ctxPath%>/t1/employeeMap.tw">직원목록</a>
               </li>
               <li class="clickable">
                   <a href="<%=ctxPath%>/t1/employeeChart.tw">조직도</a>
               </li>
           </ul>
        </div>
        <!-- 게시판 -->
        <div id="submenu5" class="employeeBoard">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/employeeBoard.tw">공지사항</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/suggestionBoard.tw">건의사항</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/generalBoard.tw">자유게시판</a>
               </li>
           </ul>
        </div>
        <!-- 월급관리 -->
        <div id="submenu6" class="salary">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/salary.tw ">수당월별내역</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/salaryDetail.tw">성과금/야근수당내역</a>
               </li>
           </ul>
        </div>
        <!-- 업무관리 -->
        <div id="submenu7" class="toDo">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
           <c:choose>
                <c:when  test="${loginuser.fk_pcode eq '1'|| loginuser.fk_pcode eq '2'}">
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/employeeTodo.tw">나의업무현황</a>
                  </li>
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/employeePerformance.tw">나의실적현황</a>
                  </li>
               </c:when>
               <c:when  test="${loginuser.fk_dcode eq '4' && loginuser.fk_pcode eq '3'}">
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/personnelManage.tw">인사관리</a>
                  </li>
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/registerNewEmployee.tw">신입사원등록</a>
                  </li>
              </c:when>
              <c:when  test="${loginuser.fk_dcode eq '5' && loginuser.fk_pcode eq '3'}">
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/rentalManage.tw">대여관리</a>
                  </li>
              </c:when>
              <c:when  test="${loginuser.fk_pcode eq '3' && (loginuser.fk_dcode eq '1' || loginuser.fk_dcode eq '2' || loginuser.fk_dcode eq '3')}">
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/leaderTodo.tw">나의업무현황</a>
                  </li>
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/departmentTodo.tw">부서업무현황</a>
                  </li>
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/departmentPerformance.tw">부서실적현황</a>
                  </li>
               </c:when>
               <c:when test="${loginuser.fk_pcode eq'4'}">
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/companyPerformance.tw">회사실적현황</a>
                  <li class="clickable">
                      <a href="<%=ctxPath%>/t1/companyPerformance/departmentPerformance.tw">부서별 실적현황</a>>
                  </li>
               </c:when>
            </c:choose>
           </ul>
        </div>
        <!-- 예약관리 -->
        <div id="submenu8" class="reservation">
           <a href="javascript:void(0)"><span id="closebtn-style"><i class="fas fa-times fa-lg closebtn"></i></span></a><br>
           <ul class="submenu-style">
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/rsRoom.tw">회의실대여신청</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/rsCar.tw">차량 대여신청</a>
               </li>
               <li class="clickable">
                   <a href="<%= ctxPath%>/t1/rsGoods.tw">사무용품 대여신청</a>
               </li>
               <li class="clickable">
               <a href="<%= ctxPath%>/t1/myReservedRoom.tw">나의 예약내역</a>
            </li>
           </ul>
        </div>
    </div>
    
</div>