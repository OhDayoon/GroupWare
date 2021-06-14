<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String ctxPath = request.getContextPath(); %>

<style type="text/css">

table#schedule{
	margin-top: 70px;
}

table#schedule th, td{
 	padding: 10px 5px;
 	vertical-align: middle;
}

a{
    color: #395673;
    text-decoration: none;
    cursor: pointer;
}

a:hover {
    color: #395673;
    cursor: pointer;
    text-decoration: none;
	font-weight: bold;
}

button.btn_normal{
	background-color: #0071bd;
	border: none;
	color: white;
	width: 70px;
	height: 30px;
	font-size: 12pt;
	padding: 3px 0px;
	margin-right: 10px;
}

</style>

<script type="text/javascript">

$(document).ready(function(){
	
	// 시작시간
	var str_s = $("span#startdate").text();
//	console.log(str_s);
	var target = str_s.indexOf(":");
	var smin= str_s.substring(target+1);
//	console.log(smin);
	var shour = str_s.substring(target-2,target);
//	console.log(shour);
	
	// 종료시간
	var str_e = $("span#enddate").text();
	target = str_e.indexOf(":");
	var emin= str_e.substring(target+1);
	console.log(emin);
	var ehour = str_e.substring(target-2,target);
	console.log(ehour);
	
	if(shour=='00' && smin=='00' && ehour=='23' && emin=='59' ){
		$("input#allday").prop("checked",true);
	}
	else{
		$("input#allday").prop("checked",false);
	}

	
});



// 일정 삭제하기
function delSchedule(sdno){

	if(confirm("일정을 삭제하시겠습니까?")){
		$.ajax({
			url: "<%= ctxPath%>/t1/schedule/deleteSchedule.tw",
			type: "post",
			data: {"sdno":sdno},
			dataType: "json",
			success:function(json){
				if(json.n==1){
					alert("일정을 삭제하였습니다.");
					location.href="<%= ctxPath%>/t1/schedule.tw";
				}
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});
	}
	else{
		return false; 
	}
}


function editSchedule(sdno){
	var frm =  document.goEdit;
	frm.action = "<%= ctxPath%>/t1/schedule/editSchedule.tw";
	frm.method = "post";
	frm.submit();
}

</script>

<div style="margin: 30px 0px 30px 50px; width: 88%;">
<i class="far fa-calendar-alt fa-2x"></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">일정 상세보기</span>&nbsp;&nbsp;<a  href="<%= ctxPath%>/t1/schedule.tw"><span>◀캘린더로 돌아가기</span></a>

		<table id="schedule" class="table table-bordered">
			<tr>
				<th style="width: 160px; vertical-align: middle;" >일자</th>
				<td>
					<span id="startdate">${svo.startdate}</span>&nbsp;~&nbsp;<span id="enddate">${svo.enddate}</span>&nbsp;&nbsp;
					<input type="checkbox" id="allday" disabled/>&nbsp;종일
				</td>
			</tr>
			<tr>
				<th style="vertical-align: middle;">제목</th>
				<td>${svo.subject}</td>
			</tr>
			
			<tr>
				<th style="vertical-align: middle;">캘린더선택</th>
				<td>
				<c:if test="${svo.fk_bcno eq '2'}">
					전체 캘린더 - ${svo.scname}
				</c:if>
				<c:if test="${svo.fk_bcno eq '1'}">
					내 캘린더 - ${svo.scname}
				</c:if></td>
			</tr>
			<tr>
				<th style="vertical-align: middle;">장소</th>
				<td>${svo.place}</td>
			</tr>
			
			<tr>
				<th style="vertical-align: middle;">공유</th>
				<td>${svo.joinemployee}</td>
			</tr>
			<tr>
				<th style="vertical-align: middle;">내용</th>
				<td><textarea id="content" rows="10" cols="100" style="height: 200px; border: none;" readonly>${svo.content}</textarea></td>
			</tr>
			<tr>
				<th style="vertical-align: middle;">작성자</th>
				<td>${svo.name}</td>
			</tr>
		</table>
	<input type="hidden" value="${sessionScope.loginuser.employeeid}" />
	<input type="hidden" value="${svo.fk_bcno}" />
	<c:set var="fk_employeeid" value="${requestScope.svo.fk_employeeid}" />
	<c:set var="bcno" value="${svo.fk_bcno}"/>
	<c:set var="employeeid" value="${sessionScope.loginuser.employeeid}"/>

	<div style="float: right;">
		<c:if test="${not empty requestScope.listgobackURL}">
			<c:if test="${bcno eq'2' && loginuser.fk_pcode =='3' && loginuser.fk_dcode == '4' }">
				<button type="button" id="edit" class="btn_normal" onclick="editSchedule('${svo.sdno}')">수정</button>
				<button type="button" class="btn_normal" onclick="delSchedule('${svo.sdno}')">삭제</button>
			</c:if>
			<c:if test="${bcno eq '1' && fk_employeeid eq employeeid}">
				<button type="button" id="edit" class="btn_normal" onclick="editSchedule('${svo.sdno}')">수정</button>
				<button type="button" class="btn_normal" onclick="delSchedule('${svo.sdno}')">삭제</button>
			</c:if>
				<button type="button" id="cancel" class="btn_normal" style="margin-right: 0px;" onclick="javascript:location.href='<%= ctxPath%>/${requestScope.listgobackURL}'">취소</button>
		</c:if>
		<c:if test="${empty requestScope.listgobackURL}">
			<c:if test="${bcno eq'2' && loginuser.fk_pcode =='3' && loginuser.fk_dcode == '4' }">
				<button type="button" id="edit" class="btn_normal" onclick="editSchedule('${svo.sdno}')">수정</button>
				<button type="button" class="btn_normal" onclick="delSchedule('${svo.sdno}')">삭제</button>
			</c:if>
			<c:if test="${bcno eq '1' && fk_employeeid eq employeeid}">
				<button type="button" id="edit" class="btn_normal" onclick="editSchedule('${svo.sdno}')">수정</button>
				<button type="button" class="btn_normal" onclick="delSchedule('${svo.sdno}')">삭제</button>
			</c:if>
				<button type="button" id="cancel" class="btn_normal" style="margin-right: 0px;" onclick="javascript:location.href='<%= ctxPath%>/t1/schedule.tw'">취소</button>
		</c:if>
	
	</div>
</div>

<form name="goEdit">
	<input type="hidden" value="${svo.sdno}" name="sdno"/>
	<input type="hidden" name="gobackURL1" value="${requestScope.gobackURL1}"/>
	
</form>