<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% 
	String ctxPath = request.getContextPath();
%>

<style type="text/css">
	div.section{
		margin: 30px 0px 30px 50px;
		width: 85%;
	}
	div.tab_select{
		margin-top: 10px;
	}
	a.tab_area{
		background-color: #ecf2f9;
		font-size: 12pt;
		padding: 5px;
		border-radius: 5px;
		color: black;
		text-decoration: none;
	}
	a.tab_area:hover{
		text-decoration: none;
		color: black;
	}	
	a.selected{
		background-color: #0071bd;
		color: white;
	}
	a.selected:hover{
		color: white;
	}
	span.period {
		margin-right: 10px;
	}
	span.chkbox {
		font-size: 9pt;
		border: solid 1px gray;
		color: gray;
		border-radius: 5px;
		margin-right: 2px;
		padding-left: 3px;	
	}	
	input.dateType{
		display: none;
	}	
	input.datepicker{
		width: 100px;
		height: 20px;
	}
	label{
		cursor: pointer;
		font-weight: normal !important; 
	}
	table.tbl{
		margin: 10px 5px;
		width: 90%;
	}
	div.section tr, div.section td{
		border: solid 1px #ccc;
		border-collapse: collapse;
	}
	td.th{		
		background-color: #ccd9e6;
		padding: 5px;
		font-weight: bold;
	}	
	#table th{
		background-color: #395673; 
		color: #ffffff;
		padding: 5px;
		border: solid 1px #ccc;
		border-collapse: collapse;
	}
	tr.tr_hover:hover{
		cursor: pointer;
		background-color: #eef2f7;
	}
</style>

<link rel="stylesheet" type="text/css" href="<%= ctxPath%>/resources/css/datepicker.css"/>

<script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	
	// 특정 문서를 클릭하면 그 문서의 상세 정보를 보여주도록 한다.
	$(document).on('click','tr.docuInfo',function(){
		var checkArr = new Array();	
		$("input[name=ncat]:checked").each(function(index,item){			
			var ncat = $(item).val();
			checkArr.push(ncat);			
		});
		
		var checkArres = checkArr.join();	
		
		var ano = $(this).children(".ano").text();
		var ncatname = $(this).children(".ncatname").text();		
		var fromDate= $("input#fromDate").val();
		var toDate= $("input#toDate").val();
		var ncat = checkArres;
		var sort= $("select#sort").val();		
		var searchWord= $("input#searchWord").val();
		
		goView(ano, ncatname, fromDate, toDate, ncat, sort, searchWord);
	});
	
	if("${sort}"=="" && "${searchWord}"=="" && "${fromDate}"=="" && "${toDate}"=="" && "${ncat}"==""){
		goSearch(1);
	}
	else {
		goSearch2('${currentShowPageNo}');
		
		$("input#fromDate").val("${fromDate}");
		$("input#toDate").val("${toDate}");
		$("select#sort").val("${sort}");
		$("input#searchWord").val("${searchWord}");
		
		var n = "${ncat}".split(',');		
		
		for(var i=0; i<n.length; i++){
			$("input:checkbox[id='chx"+n[i]+"']").prop("checked", true);
		}		
	}
	
	$("input#searchWord").bind("keydown", function(event){
		if(event.keyCode == 13){
			// 엔터를 했을 경우
			goSearch(1);
		}
	});
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
	    dd='0'+dd
	} 

	if(mm<10) {
	    mm='0'+mm
	} 

	today = yyyy+'-'+mm+'-'+dd;
	//console.log(today);
	
	// === 전체 datepicker 옵션 일괄 설정하기 ===  
    //     한번의 설정으로 $("input#fromDate"), $('input#toDate')의 옵션을 모두 설정할 수 있다.
	$(function() {
		// 모든 datepicker에 대한 공통 옵션 설정
		$.datepicker.setDefaults({
			dateFormat: 'yy-mm-dd'		// Input Display Format 변경
			,showOtherMonths: true		// 빈 공간에 현재월의 앞뒤월의 날짜를 표시
			,showMonthAfterYear:true	// 년도 먼저 나오고, 뒤에 월 표시
			,changeYear: true			// 콤보박스에서 년 선택 가능
			,changeMonth: true			// 콤보박스에서 월 선택 가능                
			,showOn: "both"				// button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
			,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" // 버튼 이미지 경로
			,buttonImageOnly: true		// 기본 버튼의 회색 부분을 없애고, 이미지만 보이게 함
		//	,buttonText: "선택"			// 버튼에 마우스 갖다 댔을 때 표시되는 텍스트                
			,yearSuffix: "년"			// 달력의 년도 부분 뒤에 붙는 텍스트
			,monthNamesShort: ['1','2','3','4','5','6','7','8','9','10','11','12'] // 달력의 월 부분 텍스트
			,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] // 달력의 월 부분 Tooltip 텍스트
			,dayNamesMin: ['일','월','화','수','목','금','토'] // 달력의 요일 부분 텍스트
			,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] // 달력의 요일 부분 Tooltip 텍스트
		//	,minDate: "-1M"				// 최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
		//	,maxDate: "+1M"				// 최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)                    
		});

		// input을 datepicker로 선언
		$("input#fromDate").datepicker();                    
		$("input#toDate").datepicker();		
		
	});	
}); // end of $(document).ready(function(){})--------------------
	
	
	// Function Declaration
	function setSearchDate(start){
		var num = start.substring(0,1);
		var str = start.substring(1,2);
	
		var today = new Date();
	
		var endDate = $.datepicker.formatDate('yy-mm-dd', today);
		$('#toDate').val(endDate);
		
		if(str == 'd'){
			today.setDate(today.getDate() - num);
		} else if (str == 'w'){
			today.setDate(today.getDate() - (num*7));
		} else if (str == 'm'){
			today.setMonth(today.getMonth() - num);
			today.setDate(today.getDate() + 1);
		}
		
		var startDate = $.datepicker.formatDate('yy-mm-dd', today);
		$('#fromDate').val(startDate);
		
		// 종료일은 시작일 이전 날짜 선택하지 못하도록 비활성화
		$("#toDate").datepicker( "option", "minDate", startDate );
		
		// 시작일은 종료일 이후 날짜 선택하지 못하도록 비활성화
		$("#fromDate").datepicker( "option", "maxDate", endDate );		
	}
	
	
	// 페이지 로딩 시 해당하는 내역 전체 보여주기(페이징처리)
	function goSearch(currentShowPageNo){			
		var checkArr = new Array();	
		$("input[name=ncat]:checked").each(function(index,item){			
			var ncat = $(item).val();
			checkArr.push(ncat);
		});
		// console.log(checkArr);
		
		var checkArres = checkArr.join();
		var fromDate= $("input#fromDate").val();
		var toDate= $("input#toDate").val();
		var ncat= checkArres;
		var sort= $("select#sort").val();
		var searchWord= $("input#searchWord").val();
		
		$.ajax({			
			url:"<%= ctxPath%>/t1/norm_templist.tw",
			data:{"fromDate":$("input#fromDate").val()
				, "toDate":$("input#toDate").val()
				, "ncat": checkArres
				, "sort":$("select#sort").val()
				, "searchWord":$("input#searchWord").val()
				, "currentShowPageNo":currentShowPageNo},
			dataType:"json",
			success:function(json){				
				
				var html = "";
				
				if(json.length > 0){
					$.each(json, function(index, item){
						
						var image = "<%= ctxPath%>/resources/images/sia/disk.gif";
						
						html += "<tr class='tr_hover docuInfo'>";
						html += "<td align='center' style='padding: 5px;'>"+ item.rno +"</td>";
						if(item.fileName != null){
							html += "<td>&nbsp;"+ item.atitle +"&nbsp;<img src='"+ image +"'/></td>";
						}
						else if(item.fileName == null){
							html += "<td>&nbsp;"+ item.atitle +"</td>";
						}
						html += "<td class='ncatname' align='center'>"+ item.ncatname +"</td>";
						html += "<td class='ano' align='center'>"+ item.ano +"</td>";						
						html += "<td align='center'>"+ item.asdate +"</td>";
						html += "</tr>";
					});
				}
				else{
					html += "<tr>";
					html += "<td colspan='5' align='center' style='padding: 5px;'>해당하는 글이 없습니다</td>";
					html += "</tr>";
				}
				
				$("tbody#approvalDisplay").html(html);
				
				// 페이지바 함수 호출
				makeApprovalPageBar(currentShowPageNo);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});		
	}// end of function goSearch(){}--------------------
	
	
	function goSearch2(currentShowPageNo){	
		
		$.ajax({
			url:"<%= ctxPath%>/t1/norm_templist.tw",
			data:{"fromDate":"${fromDate}"
				, "toDate":"${toDate}"
				, "ncat": "${ncat}"
				, "sort":"${sort}"
				, "searchWord":"${searchWord}"
				, "currentShowPageNo":currentShowPageNo},
			dataType:"json",
			success:function(json){				
				
				var html = "";
				
				if(json.length > 0){
					$.each(json, function(index, item){
						
						var image = "<%= ctxPath%>/resources/images/sia/disk.gif";
						
						html += "<tr class='tr_hover docuInfo'>";
						html += "<td align='center' style='padding: 5px;'>"+ item.rno +"</td>";
						if(item.fileName != null){
							html += "<td>&nbsp;"+ item.atitle +"&nbsp;<img src='"+ image +"'/></td>";
						}
						else if(item.fileName == null){
							html += "<td>&nbsp;"+ item.atitle +"</td>";
						}
						html += "<td class='ncatname' align='center'>"+ item.ncatname +"</td>";
						html += "<td class='ano' align='center'>"+ item.ano +"</td>";						
						html += "<td align='center'>"+ item.asdate +"</td>";
						html += "</tr>";
					});
				}
				else{
					html += "<tr>";
					html += "<td colspan='5' align='center' style='padding: 5px;'>해당하는 글이 없습니다</td>";
					html += "</tr>";
				}
				
				$("tbody#approvalDisplay").html(html);				
				
				// 페이지바 함수 호출
				makeApprovalPageBar(currentShowPageNo);	
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});		
	}// end of function goSearch2(){}--------------------
	
	
	// 페이지바 Ajax로 만들기
	function makeApprovalPageBar(currentShowPageNo){
		
		$("input[name=currentShowPageNo]").val(currentShowPageNo);
		
		var checkArr = new Array();	
		$("input[name=ncat]:checked").each(function(index,item){			
			var ncat = $(item).val();
			checkArr.push(ncat);			
		});
		// console.log(checkArr);
		
		var checkArres = checkArr.join();		
		
		// totalPage 수 알아오기
		$.ajax({
			url:"<%= ctxPath%>/t1/getNormTempTotalPage.tw",
			data:{"fromDate":$("input#fromDate").val()
				, "toDate":$("input#toDate").val()
				, "ncat": checkArres
				, "sort":$("select#sort").val()
				, "searchWord":$("input#searchWord").val()
				, "sizePerPage":"10"},
			type:"get",
			dataType:"json",
			success:function(json){
				
				if(json.totalPage > 0){
					// 글이 있는 경우
					
					var totalPage = json.totalPage;
					
					var pageBarHTML = "<ul class='pagination' style='list-style: none;'>";
					
					var blockSize = 5;
					
					var loop = 1;
					
					if(typeof currentShowPageNo == "string"){
						currentShowPageNo = Number(currentShowPageNo);
					}
					
					var pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1;
					
					// === [맨처음][이전] 만들기 ===
					if(pageNo != 1) {
						pageBarHTML += "<li><a href='javascript:goSearch(\"1\")'>First</a></li>";
						pageBarHTML += "<li><a href='javascript:goSearch(\""+(pageNo-1)+"\")'>Prev</a></li>";
					}
					
					while(!(loop > blockSize || pageNo > totalPage)) {
						
						if(pageNo == currentShowPageNo) {
							pageBarHTML += "<li class='active'><a>"+pageNo+"</a></li>";
						}
						else {
							pageBarHTML += "<li><a href='javascript:goSearch(\""+pageNo+"\")'>"+pageNo+"</a></li>";
						}

						loop++;
						pageNo++;
					}// end of while--------------------
					
					// === [다음][마지막] 만들기 ===
					if(pageNo <= totalPage) {
						pageBarHTML += "<li><a href='javascript:goSearch(\""+pageNo+"\")'>Next</a></li>";
						pageBarHTML += "<li><a href='javascript:goSearch(\""+totalPage+"\")'>Last</a></li>";
					}
					
					pageBarHTML += "</ul>";
					
					$("div#pageBar").html(pageBarHTML);
					
				}				
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
		});
	}
	
	
	function goView(ano, ncatname, fromDate, toDate, ncat, sort, searchWord){
		var frm = document.goViewFrm;
		
		frm.ano.value = ano;
		frm.sort.value = sort;
		frm.ncatname.value = ncatname;
		frm.searchWord.value = searchWord;
		frm.fromDate.value = fromDate;
		frm.toDate.value = toDate;		
		frm.ncat.value = ncat;
		
		frm.method = "get";
		frm.action = "<%= ctxPath%>/t1/myDocuNorm_temp_detail.tw";
		frm.submit();
	} 
</script>

<div class="section">
<img src="<%= ctxPath%>/resources/images/sia/document.png" width="26px;">
<span style="font-size: 14pt; font-weight: bold;">임시저장함</span>
	<div class="tab_select">
		<a href="<%= ctxPath%>/t1/myDocuNorm_temp.tw" class="tab_area selected">일반 결재 문서</a> 
		<a href="<%= ctxPath%>/t1/myDocuSpend_temp.tw" class="tab_area">지출 결재 문서</a>
		<a href="<%= ctxPath%>/t1/myDocuVacation_temp.tw" class="tab_area">근태/휴가 결재 문서</a>			
	</div>

	<form name="searchFrm">	
		<table class="tbl">			
			<tr>
				<td width="20%" class="th">저장일자</td>
				<td width="80%">&nbsp;&nbsp;
					<span class="period">
						<span class="chkbox">
							<input type="radio" class="dateType" id="dateType1" onclick="setSearchDate('0d')"/>
							<label for="dateType1">오늘</label>
						</span>
						<span class="chkbox">
							<input type="radio" class="dateType" id="dateType2" onclick="setSearchDate('1w')"/>
							<label for="dateType2">1주일</label>
						</span>
						<span class="chkbox">
							<input type="radio" class="dateType" id="dateType3" onclick="setSearchDate('1m')"/>
							<label for="dateType3">1개월</label>
						</span>
						<span class="chkbox">
							<input type="radio" class="dateType" id="dateType4" onclick="setSearchDate('3m')"/>
							<label for="dateType4">3개월</label>
						</span>
						<span class="chkbox">
							<input type="radio" class="dateType" id="dateType5" onclick="setSearchDate('6m')"/>
							<label for="dateType5">6개월</label>
						</span>
					</span>
					<input type="hidden" value="${fromDate}" id="hiddendate"/>
					<input type="hidden" value="${toDate}" id="hiddendate2"/>
					<input type="text" class="datepicker" id="fromDate" name="fromDate" autocomplete="off" > - <input type="text" class="datepicker" id="toDate" name="toDate" autocomplete="off">					
				</td>
			</tr>
			<tr>			
				<td class="th">일반 결재 문서</td>
				<td>&nbsp;&nbsp;
					<label for="chx1"><input type="checkbox" name="ncat" id="chx1" value="1"> 회의록</label>&nbsp;&nbsp;
					<label for="chx2"><input type="checkbox" name="ncat" id="chx2" value="2"> 위임장</label>&nbsp;&nbsp;
					<label for="chx3"><input type="checkbox" name="ncat" id="chx3" value="3"> 외부공문</label>&nbsp;&nbsp;
					<label for="chx4"><input type="checkbox" name="ncat" id="chx4" value="4"> 협조공문</label>&nbsp;&nbsp;
				</td>
			</tr>			
			<tr>
				<td class="th">문서검색</td>
				<td>&nbsp;&nbsp;
					<select name="sort" id="sort">
						<option value="">전체보기</option>
						<option value="atitle">제목</option>
						<option value="ano">문서번호</option>												
					</select>&nbsp;
					<input type="text" name="searchWord" id="searchWord" style="height: 20px;"/> <button type="button" onclick="goSearch(1)">검색</button>
				</td>
			</tr>	
		</table>
		
		<br>
		
		<table id="table" class="tbl">
			<thead>
			<tr>
				<th style="width: 70px; text-align: center;">번호</th>
				<th style="width: 300px; text-align: center;">제목</th>
				<th style="width: 100px; text-align: center;">문서분류</th>
				<th style="width: 100px; text-align: center;">문서번호</th>
				<th style="width: 120px; text-align: center;">저장일</th>
			</tr>
			</thead>
			<tbody id="approvalDisplay"></tbody>		
		</table>
		
		<div id="pageBar" style="width: 90%; margin-left: 42%;"></div>
	</form>
	
	<form name="goViewFrm">
		<input type="hidden" name="ano" value="" />
		<input type="hidden" name="ncatname" value="" />
		<input type="hidden" name="fromDate" value="" />
		<input type="hidden" name="toDate" value="" />
		<input type="hidden" name="ncat" value="" />
		<input type="hidden" name="sort" value="" />
		<input type="hidden" name="searchWord" value="" />
		<input type="hidden" name="currentShowPageNo" value="" />
	</form>
</div>
