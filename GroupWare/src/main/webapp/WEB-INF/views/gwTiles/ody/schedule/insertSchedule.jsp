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
		
		$("select.scategory").hide();
		// === *** 달력 관련 *** === //
		
		
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
		
		//
		
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
		
		
		// 공유자 추가하기
		$("input#joinEmp").keyup(function(){
				var joinEmp = $(this).val();
			
				$.ajax({
					url:"<%= ctxPath%>/t1/insertSchedule/searchJoinEmpList.tw",
					data:{"joinEmp":joinEmp},
					dataType:"json",
					success : function(json){
						var list = [];
				//		console.log("수:"+json.length);
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

		
		// 등록 버튼 클릭
		$("button#register").click(function(){
			
		//	alert($("input#color").val());
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
		//		alert(jcomma);
				
				jcomma=jcomma.substring(0,jcomma.length-1);
				$("input[name=joinemployee]").val(jcomma);
			
				var frm = document.scheduleFrm;
				frm.action="<%= ctxPath%>/t1/schedule/registerSchedule.tw";
				frm.method="post";
				frm.submit();
				
			}
		});
		
	}); // end of $(document).ready(function(){}----------------


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
<i class="far fa-calendar-alt fa-2x"></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">일정 등록</span>

	<form name="scheduleFrm">
		<table id="schedule" class="table table-bordered">
			<tr>
				<th>일자</th>
				<td>
					<input type="date" id="startDate" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp; 
					<select id="startHour" class="schedule"></select> 시
					<select id="startMin" class="schedule"></select> 분
					- <input type="date" id="endDate" value="${requestScope.chooseDate}" style="height: 30px;"/>&nbsp;
					<select id="endHour" class="schedule"></select> 시
					<select id="endMin" class="schedule"></select> 분&nbsp;
					<label for="allDay"><input type="checkbox" id="allDay" name="allDay" value="1"/>&nbsp;<span>종일</span></label>
					<input type="hidden" name="startdate"/>
					<input type="hidden" name="enddate"/>
				</td>
			</tr>
			<tr>
				<th>제목</th>
				<td><input type="text" id="subject" name="subject" class="form-control"/></td>
			</tr>
			
			<tr>
				<th>캘린더선택</th>
				<td>
				
				
					<select class="calType schedule" name="fk_bcno">
					<c:choose>
						<c:when test="${loginuser.fk_pcode =='3' && loginuser.fk_dcode == '4' }">
							<option value="">선택하세요</option>
							<option value="1">내 캘린더</option>
							<option value="2">사내 캘린더</option>
						</c:when>
						<c:otherwise>
							<option value="">선택하세요</option>
							<option value="1">내 캘린더</option>
						</c:otherwise >
						</c:choose>
					</select>
			
				
				&nbsp;
				<select class="scategory schedule" name="fk_scno" >
				</select>
				</td>
			</tr>
			<tr>
				<th>색상</th>
				<td><input type="color" id="color" name="color" value="#0071bd"/></td>
			</tr>
			<tr>
				<th>장소</th>
				<td><input type="text" name="place"  class="form-control"/></td>
			</tr>
			
			<tr>
				<th>공유</th>
				<td>
				<input type="text" id="joinEmp" name="joinEmp"  class="form-control"/><input type="hidden" name="joinemployee"/>
				<div class="extraArea"></div>
				</td>
			</tr>
			<tr>
				<th>내용</th>
				<td><textarea rows="10" cols="100" style="height: 200px;" name="content" id="content"  class="form-control"></textarea></td>
			</tr>
		</table>
		<input type="hidden" value="${sessionScope.loginuser.employeeid}" name="fk_employeeid"/>
	</form>
	
	<div style="float: right;">
	<button type="button" id="register" class="btn_normal" style="margin-right: 10px;">저장</button>
	<button type="button" class="btn_normal" onclick="javascript:location.href='<%= ctxPath%>/t1/schedule.tw'">취소</button>
	</div>
</div>