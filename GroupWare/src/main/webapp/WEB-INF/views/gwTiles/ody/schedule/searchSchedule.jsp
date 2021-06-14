<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<meta charset="UTF-8">

<style type="text/css">

table#schedule{
	margin-top: 100px;
}
table#schedule th, td{
 	padding: 10px 5px;
 	vertical-align: middle;
}

tr.infoSchedule{
	background-color: white;
}

.info{
	background-color: #eef2f7;
	cursor: pointer;
}
.table th {
	color: #fff;
	background-color: #395673;
	text-align: center;
}

.table td{
	text-align: center;
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
	width: 50px;
	height: 30px;
	font-size: 12pt;
	padding: 3px 0px;
}

</style>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
		
		
		// 검색할 때 필요한 datepicker
		  //모든 datepicker에 대한 공통 옵션 설정
		    $.datepicker.setDefaults({
		        dateFormat: 'yy-mm-dd' //Input Display Format 변경
		        ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
		        ,showMonthAfterYear:true //년도 먼저 나오고, 뒤에 월 표시
		        ,changeYear: true //콤보박스에서 년 선택 가능
		        ,changeMonth: true //콤보박스에서 월 선택 가능                
		        ,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] //달력의 월 부분 텍스트
		        ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip 텍스트
		        ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 부분 텍스트
		        ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 부분 Tooltip 텍스트             
		    });
		
		    //input을 datepicker로 선언
		    $("input#fromDate").datepicker();                    
		    $("input#toDate").datepicker();
		    
		    $('#fromDate').datepicker("option", "maxDate", $("#toDate").val());
		    $('#fromDate').datepicker("option", "onClose", function ( selectedDate ) {
		        $("#toDate").datepicker( "option", "minDate", selectedDate );
		    });

		    $('#toDate').datepicker("option", "minDate", $("#fromDate").val());
		    $('#toDate').datepicker("option", "onClose", function ( selectedDate ) {
		        $("#fromDate").datepicker( "option", "maxDate", selectedDate );
		    });
		    
		    //From의 초기값을 오늘 날짜로 설정
		    $('input#fromDate').datepicker('setDate', '-1M'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
		    
		    //To의 초기값을 3일후로 설정
		    $('input#toDate').datepicker('setDate', 'today'); //(-1D:하루전, -1M:한달전, -1Y:일년전), (+1D:하루후, +1M:한달후, +1Y:일년후)
		  
		  
		    

			$("tr.infoSchedule").bind("mouseover",function(){			
				$(this).addClass("info");			
			}); 
			
			$("tr.infoSchedule").bind("mouseout",function(){

				$(this).removeClass("info");
			});
			
			
			
			$("tr.infoSchedule").click(function(){
			//	console.log($(this).html());
				var sdno = $(this).children(".sdno").text();
				godetail(sdno);
		
			});
		
		 // 검색 할 때 엔터를 친 경우
	      $("input#searchWord").keydown(function(event){
				if(event.keyCode == 13){ 
					goSearch();
				}
	      });
	      
	      if(${not empty requestScope.paraMap}){
	    	  $("input[name=startdate]").val("${requestScope.paraMap.startdate}");
	    	  $("input[name=enddate]").val("${requestScope.paraMap.enddate}");
				$("select#searchType").val("${requestScope.paraMap.searchType}");
				$("input#searchWord").val("${requestScope.paraMap.searchWord}");
				$("select#sizePerPage").val("${requestScope.paraMap.str_sizePerPage}");
				$("select#fk_bcno").val("${requestScope.paraMap.fk_bcno}");
			}
		 
	}); // end of $(document).ready(function(){}-------------
	
	function godetail(sdno){
		
		<%--location.href="<%= ctxPath%>/view.action?seq="+seq;--%>
		
		// === #124. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
	    //           사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
	    //           현재 페이지 주소를 뷰단으로 넘겨준다.
	    $("input[name=sdno]").val(sdno);
		var frm = document.goViewFrm;
		
		
		frm.method="get";
		frm.action="<%= ctxPath%>/t1/detailSchedule.tw";
		frm.submit();
	
	} // end of function goView(seq){} 
			

	// 검색 기능
	function goSearch(){

		if($("select#searchType").val()=="" && $("input#searchWord").val()!=""){
			alert("검색 조건을 선택하세요");
			return;
		}
		else{
		var frm = document.searchSchedule;
        frm.method="get";
        frm.action="<%= ctxPath%>/t1/searchSchedule.tw";
        frm.submit();
		}
	}

</script>

<div style="margin: 30px 0px 30px 50px; width: 88%;">

	<div>
		<i class="far fa-calendar-alt fa-2x"></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">일정 검색결과</span>&nbsp;&nbsp;<a  href="<%= ctxPath%>/t1/schedule.tw"><span>◀캘린더로 돌아가기</span></a>

		<div id="searchPart" style="float: right; margin-top: 50px;">
				<form name="searchSchedule">
					<div>
					<input type="text" id="fromDate" name="startdate" style="width: 90px;" readonly="readonly">&nbsp;&nbsp; 
	            -&nbsp;&nbsp; <input type="text" id="toDate" name="enddate" style="width: 90px;" readonly="readonly">&nbsp;&nbsp;
	            	<select id="fk_bcno" name="fk_bcno" style="height: 30px;">
						<option value="">전체</option>
						<option value="1">내 캘린더</option>
						<option value="2">사내 캘린더</option>
					</select>&nbsp;&nbsp;	
					<select id="searchType" name="searchType" style="height: 30px;">
						<option value="">선택</option>
						<option value="subject">제목</option>
						<option value="content">내용</option>
						<option value="joinemployee">공유자</option>
					</select>&nbsp;&nbsp;	
					<input type="text" id="searchWord" value="${requestScope.searchWord}" style="height: 30px; width:120px; " name="searchWord"/>
					<button type="button" class="btn_normal" style="display: inline-block;"  onclick="goSearch()">검색</button>
					<select id="sizePerPage" name="sizePerPage" style="height: 30px;">
						<option value="10">10</option>
						<option value="15">15</option>
						<option value="20">20</option>
					</select>
					
					</div>
				</form>
			</div>
	</div>
	<table id="schedule" class="table table-bordered">
	
		<thead>
			<tr>
				<th style="text-align: center; width: 20%;">일자</th>
				<th style="text-align: center; width: 15%;">캘린더</th>
				<th style="text-align: center; width: 10%;">등록자</th>
				<th style="text-align: center; width: 30%">제목</th>
				<th style="text-align: center;">내용</th>
			</tr>
		</thead>
		
		<tbody>
		<c:if test="${empty scheduleList}">
			<tr>
				<td colspan="5" style="text-align: center;">검색 결과가 없습니다.</td>
			</tr>
		</c:if>
			<c:if test="${not empty scheduleList}">
				<c:forEach var="svo" items="${scheduleList}">
					<tr class="infoSchedule">
						<td style="display: none;" class="sdno">${svo.sdno}</td>
						<td >${svo.startdate} - ${svo.enddate}</td>
						<td>${svo.bcname} - ${svo.scname}</td>
						<td>${svo.name}</td>
						<td>${svo.subject}</td>
						<td>${svo.content}</td>
					</tr>
				</c:forEach>
			</c:if>
		</tbody>
	</table>

<div align="center" style="width: 70%; border: solid 0px gray; margin: 20px auto;">${requestScope.pageBar}</div>

</div>

 <form name="goViewFrm"> 
    	<input type="hidden" name="sdno"/>
   		<input type="hidden" name="listgobackURL" value="${requestScope.listgobackURL}"/>
    </form>   