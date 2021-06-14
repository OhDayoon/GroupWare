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

select.schedule{
	height: 30px;
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

#joinEmp:focus{
	outline: none;
}

.plusEmp{
		float:left; 
		background-color:#333333; 
		color:white;
		border-radius: 10%;
		padding: 8px;
		margin: 3px;
		transition: .8s;
		cursor: pointer;
		margin-top: 6px;
}

.ui-autocomplete {
max-height: 100px;
overflow-y: auto;
}

button.btn_normal{
	background-color: #0071bd;
	border: none;
	color: white;
	width: 70px;
	height: 30px;
	font-size: 12pt;
	padding: 3px 0px;
}
</style>

<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
<script type="text/javascript">

$(document).ready(function(){
	
	// 캘린더 소분류 카테고리 숨기기
//    $("select.scategory").hide();
	
	
		// 시작시간
		var str_s = $("span#startdate").text();
		console.log(str_s);
		var target = str_s.indexOf(":");
		var smin= str_s.substring(target+1);
		console.log(smin);
		var shour = str_s.substring(target-2,target);
		console.log(shour);
		
		$("select#startHour").val(shour);
		$("select#startMin").val(smin);

		// 종료시간
		var str_e = $("span#enddate").text();
		target = str_e.indexOf(":");
		var emin= str_e.substring(target+1);
		console.log("종료분:"+emin);
		var ehour = str_e.substring(target-2,target);

		$("select#endHour").val(ehour);
		$("select#endMin").val(emin);
		
		
		if(shour=='00' && smin=='00' && ehour=='23' && emin=='59' ){
			$("input#allday").prop("checked",true);
		}
		else{
			$("input#allday").prop("checked",false);
		}
	
		//시작 일시
		target = str_s.indexOf(" ");
		var start = str_s.substring(0,target);
		$("input#startDate").val(start);
		// 종료 일시
		target = str_e.indexOf(" ");
		var end = str_e.substring(0,target);
		$("input#endDate").val(end);
		
		// 시작시간, 종료시간		
		var html="";
		for(var i=0;i<24;i++){
			if(i<10){
				html+="<option value='0"+i+"'>0"+i+"</option>";
			}
			else{
				html+="<option value='"+i+"'>"+i+"</option>";
			}
			
		}

		$("select#startHour").html(html);
		$("select#endHour").html(html);
		
		// 시작 종료 분 
		html="";
		for(var i=0;i<60;i=i+5){
			if(i<10){
				html+="<option value='0"+i+"'>0"+i+"</option>";
			}
			else {
				html+="<option value='"+i+"'>"+i+"</option>";
			}
		}
			html+="<option value='"+59+"'>"+59+"</option>"
		$("select#startMin").html(html);
		$("select#endMin").html(html);	

		// '종일' 체크박스 클릭시
		$("input#allDay").click(function() {
			var checked = $('input#allDay:checked').val();
			if(checked == "1")	{
				$("select#startHour").val("00");
				$("select#startMin").val("00");
				$("select#endHour").val("23");
				$("select#endMin").val("59");
				$("select#startHour").prop("disabled",true);
				$("select#startMin").prop("disabled",true);
				$("select#endHour").prop("disabled",true);
				$("select#endMin").prop("disabled",true);
			} else {
				$("select#startHour").prop("disabled",false);
				$("select#startMin").prop("disabled",false);
				$("select#endHour").prop("disabled",false);
				$("select#endMin").prop("disabled",false);
			}
		});
		

		$("select[name=fk_bcno]").val(${svo.fk_bcno});
		
		
		var fk_bcno =$("select.calType").val();
		var fk_employeeid = $("input[name=fk_employeeid]").val();
		if(fk_bcno!=""){
			$.ajax({
				url: "<%= ctxPath%>/t1/selectCategory.tw",
				data: {"fk_bcno":fk_bcno, "fk_employeeid":fk_employeeid},
				dataType: "json",
				success:function(json){
					var html ="";
					if(json.length>0){
						
						$.each(json, function(index, item){
							html+="<option value='"+item.scno+"'>"+item.scname+"</option>"
						});
						$("select.scategory").html(html);
						$("select.scategory").val(${svo.fk_scno});
						$("select.scategory").show();
					}
				},
				error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					
				}
				
			});
		}	
		// 캘린더 종류 선택
		$("select.calType").change(function(){
			var fk_bcno =$("select.calType").val();
			var fk_employeeid = $("input[name=fk_employeeid]").val();
				$.ajax({
					url: "<%= ctxPath%>/t1/selectCategory.tw",
					data: {"fk_bcno":fk_bcno, "fk_employeeid":fk_employeeid},
					dataType: "json",
					success:function(json){
						var html ="";
						if(json.length>0){
							
							$.each(json, function(index, item){
								html+="<option value='"+item.scno+"'>"+item.scname+"</option>"
							});
							$("select.scategory").html(html);
							$("select.scategory").show();
						}
					},
					error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
						
					}
					
				});
		});
		
		// 이미 저장되어있는 공유자
		var sjoin = $("span#storedEmp").text();
		if(sjoin!="공유자가 없습니다."){
		var sjoinArr =sjoin.split(",");
		var sjoinemp="";
		for(var i=0;i<sjoinArr.length;i++){
			sjoinemp=sjoinArr[i];
			console.log(sjoinemp);
			addJoinEmp(sjoinemp);
		}
		}
		// 공유자 추가하기
		$("input#joinEmp").bind("keyup",function(){
				var joinEmp = $(this).val();
				console.log(joinEmp);
				$.ajax({
					url:"<%= ctxPath%>/t1/insertSchedule/searchJoinEmpList.tw",
					data:{"joinEmp":joinEmp},
					dataType:"json",
					success : function(json){
						var list = [];
						console.log("수:"+json.length);
						if(json.length > 0){
								$.each(json,function(index,item){
									var inputEmp = item.name;
									if(!joinEmp.includes(inputEmp)){
										list.push(inputEmp+"("+item.employeeid+"/"+item.dname+")");
									}
								});
							$("input#joinEmp").autocomplete({
								source:list,
								select: function(event, ui) {
									addJoinEmp(ui.item.value);
									return false;
						        },
						        focus: function(event, ui) {
						            return false;
						        }
							});
						}
					}
				});
		});
		
	
		// 공유자 삭제하기
		$(document).on('click','.plusEmp',function(){
				var text = $(this).text();
				if(confirm(text +"사원을 삭제하시겠습니까?")){
					$(this).fadeOut(200);
					$(this).empty();
					var joinEmp = "";
					var jArr = joinEmp.split(",");
					for ( var i;i<jArr.length;i++) {
						if(jArr[i]==text){
							jArr[i]="";
						}else{
							jArr[i]+=",";
						}
						joinEmp+=jArr[i];
				      }
					joinEmp=joinEmp.substr(0,joinEmp.length-1);
					console.log(joinEmp);
				}
			});
		
		// 
	
		
		
		// 수정 버튼 클릭
		$("button#edit").click(function(){
			
			var subject = $("input#subject").val().trim();
			var calType = $("select.calType").val().trim();
			console.log($("select[name=scno]").val());
			// 달력 유효성 검사
			var startDate = $("input#startDate").val();	
	    	var sArr = startDate.split("-");
	    	var startDate= "";	
	    	for(var i=0;i<sArr.length;i++){
	    		startDate+=sArr[i];
	    	}
	    	var endDate = $("input#endDate").val();	
	    	var eArr = endDate.split("-");   
	     	var endDate= "";
	     	for(var i=0;i<eArr.length;i++){
	     		endDate+=eArr[i];
	     	}
			
	   
	     	var startHour= $("select#startHour").val();
	     	var endHour = $("select#endHour").val();
	     	var startMin= $("select#startMin").val();
	     	var endMin= $("select#endMin").val();
	        // 조회기간 종료일이 시작일 보다 작을 때 경고
	        if (endDate - startDate < 0){
	         	alert("종료일이 시작일 보다 작습니다."); return;
	        }
	        // 시작일과 종료일 같을 때 시간과 분에 대한 유효성 검사
	        else if(endDate==startDate){
	        	if(startHour>endHour){
	        		alert("종료일이 시작일 보다 작습니다."); return;
	        	}
	        	else if(startHour==endHour){
	        		if(startMin>endMin){
	        			alert("종료일이 시작일 보다 작습니다."); return;
	        		}
	        		else if(startMin==endMin){
	        			alert("시작일과 종료일이 동일합니다."); return;
	        		}
	        	}
	        }
	        // 제목 유효성 검사
	        if(subject==""){
				alert("제목을 입력하세요."); return;
			}
	        // 캘린더 검사
			else if(calType==""){
				alert("캘린더 종류를 선택하세요."); return;
			}
			else{
				// 달력 형태로 만들어야 한다.(시작일과 종료일)
				// 오라클에 들어갈 date 형식으로 만들기
				var sdate = startDate+$("select#startHour").val()+$("select#startMin").val()+"00";
				var edate = endDate+$("select#endHour").val()+$("select#endMin").val()+"00";
				
				$("input[name=startdate]").val(sdate);
				$("input[name=enddate]").val(edate);
				
				var join = $("span.plusEmp").text();
				
				var jcomma=join.replace(/ /g,",");
			//	alert(jcomma);
				
				jcomma=jcomma.substring(0,jcomma.length-1);
				$("input[name=joinemployee]").val(jcomma);
			
				var frm = document.scheduleFrm;
				frm.action="<%= ctxPath%>/t1/schedule/editEndSchedule.tw";
				frm.method="post";
				frm.submit();
				
			}
		});
		
		
		
		
		
		
		
		
}); // end of $(document).ready(function(){})----------


function addJoinEmp(value){
	var joinEmp = $("span.plusEmp").text();
	var $div = $("div.extraArea");
	var $span = $("<span class='plusEmp'>").text(value+" ");
	if(joinEmp.includes(value)){
		alert("이미 추가한 사원입니다.");
	}
	else{
		joinEmp += value+",";
		$div.append($span);
	}
	$("#joinEmp").val("");
}			



</script>
<div style="margin: 30px 0px 30px 50px; width: 88%;">
<i class="far fa-calendar-alt fa-2x"></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">일정 수정하기</span>&nbsp;&nbsp;<a  href="<%= ctxPath%>/t1/schedule.tw"><span style="color: #395673;">◀캘린더로 돌아가기</span></a>

	<form name="scheduleFrm">
		<table id="schedule" class="table  table-bordered">
			<tr>
				<th>일자</th>
				<td>
					<input type="date" id="startDate" value="" style="height: 30px;"/>&nbsp; 
					<select id="startHour" class="schedule"></select> 시
					<select id="startMin" class="schedule"></select> 분
					- <input type="date" id="endDate" value="" style="height: 30px;"/>&nbsp;
					<select id="endHour" class="schedule" ></select> 시
					<select id="endMin" class="schedule"></select> 분&nbsp;
					<label for="allDay"><input type="checkbox" id="allDay" name="allDay" value="1" />&nbsp;<span>종일</span></label>
					<input type="hidden" name="startdate"/>
					<input type="hidden" name="enddate"/>
					<div style="display: none;"><span id="startdate">${svo.startdate}</span><span id="enddate">${svo.enddate}</span></div>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td><input type="text" id="subject" name="subject" value="${svo.subject}" class="form-control"/></td>
			</tr>
			
			<tr>
				<th>캘린더선택</th>
				<td>
					<select class="calType schedule" name="fk_bcno" >
						<c:choose>
						<c:when test="${loginuser.fk_pcode =='3' && loginuser.fk_dcode == '4'}">
							<option value="">선택하세요</option>
							<option value="1">내 캘린더</option>
							<option value="2">사내 캘린더</option>
						</c:when>
						<c:otherwise>
							<option value="">선택하세요</option>
							<option value="1">내 캘린더</option>
						</c:otherwise>
						</c:choose>
					</select>
			
				
				&nbsp;
				<select class="scategory schedule" name="fk_scno" >
				</select>
				</td>
			</tr>
			<tr>
				<th>색상</th>
				<td><input type="color" id="color" name="color" value="${svo.color}"/></td>
			</tr>
			<tr>
				<th>장소</th>
				<td><input type="text" name="place" value="${svo.place}" class="form-control"/></td>
			</tr>
			
			<tr>
				<th>공유</th>
				<td>
				<span id="storedEmp" style="display: none;">${svo.joinemployee}</span>
				<input type="text" id="joinEmp" name="joinEmp" class="form-control"/><input type="hidden" name="joinemployee" />
				<div class="extraArea"></div>
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea rows="10" cols="100" style="height: 200px;" name="content" id="content" class="form-control">${svo.content}</textarea></td>
			</tr>
		</table>
		<input type="hidden" value="${sessionScope.loginuser.employeeid}" name="fk_employeeid"/>
		<input type="hidden" value="${svo.sdno}" name="sdno"/>
	</form>
	
	<div style="float: right;">
		<button type="button" class="btn_normal" id="edit" style="margin-right: 10px;">수정</button>
		<button type="button" class="btn_normal"  onclick="javascript:location.href='<%= ctxPath%>/${requestScope.gobackURL1}'">취소</button>
	</div>
</div>

