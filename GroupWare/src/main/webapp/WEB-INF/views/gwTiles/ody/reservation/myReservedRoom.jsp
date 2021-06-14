<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath();%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<style type="text/css">

div#reserveList{
	clear: both;
	margin-top: 60px;
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

select{
	height: 25px;
}

button.btn_normal{
	background-color: #0071bd;
	border: none;
	color: white;
	width: 60px;
	height: 30px;
	font-size: 12pt;
	padding: 3px 0px;
}

button.btn_r{
	border: solid 1px #ccc;
	width:60px;
	height: 30px;
	font-size: 12pt;
	padding: 1px 0px;
}

</style>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>
<script type="text/javascript">

	var curDate = new Date();
	
	var curHour=curDate.getHours();
	
	var currtime = curDate.getHours();
	var curDay = curDate.getDate();
	var curMonth = curDate.getMonth() + 1;
	if(curMonth.toString().length < 2){
		curMonth = "0"+curMonth;
	}
	
	if(curDay.toString().length < 2){
		curDay = "0"+curDay;
	}
	var curYear = curDate.getFullYear();
	var curTime = curYear + "" + curMonth + "" + curDay;

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
		    
		    
		    // 검색 할 때 엔터를 친 경우
		    $("input#searchWord").keydown(function(event){
					if(event.keyCode == 13){ 
						goSearch();
					}
		    });
		    
		    var startdate = "${requestScope.paraMap.startdate}";
		    var enddate = "${requestScope.paraMap.enddate}";
		    
		    if(startdate != "" && enddate != "") {
		    	$("input#fromDate").val(startdate);
	    	    $("input#toDate").val(enddate);
		    } 
				    
		    if(${not empty requestScope.paraMap}){
				$("select[name=roomno]").val("${requestScope.paraMap.roomno}");
				$("select[name=rstatus]").val("${requestScope.paraMap.rstatus}");
				$("input#searchWord").val("${requestScope.paraMap.searchWord}");
			}
		    
	
		    $('.modal').on('hidden.bs.modal', function (e) {
		        console.log('modal close');
		      $(this).find('form')[0].reset()
		    });
		    
		    $("button#editReserve").click(function(){
		    	event.stopPropagation();
	            event.preventDefault();
	             
	            var checkCnt= $("input:checkbox[name=chktime]:checked").length;
	     		var chkTime="";
	            $("input:checkbox[name=chktime]:checked").each(function(){
					chkTime= chkTime+ $(this).val()+",";
				});
				
				if(chkTime.length>0){
					chkTime = chkTime.substring(0,chkTime.length-1);
				}
				
				showTime(chkTime);
				
	     		if(checkCnt<1){
	     			alert("시간을 선택하세요.");
	     			return false; // 종료
	     		}
	     		
	            var purpose = $("input#purpose").val().trim();
	             
	            if(purpose == ""){
	            	 alert("목적을 입력하세요");
	            }
	            
	            else{
	            	
	            	<%-- form으로 값 넘겨주기--%>
	            	 var frm = document.editRoom;
	            	 frm.method = "POST";
	                 frm.action = "<%= ctxPath%>/t1/editReserveRoom.tw";
	                 frm.submit(); 
	            }
		    });
	}); // end of $(document).ready(function(){})----------------
	
	
	function goSearch(){
		
		var frm = document.searchRoom;
        frm.method="get";
        frm.action="<%= ctxPath%>/t1/myReservedRoom.tw";
        frm.submit();
				

	}
	
	function delMyrsroom(rsroomno,roomname,rdate,rtime){		
		
		if(confirm(rdate+" , "+rtime +"시에 예약된 "+roomname+" 내역을 취소하시겠습니까?")){
			$.ajax({
				url: "<%= ctxPath%>/t1/delmyReservedRoom.tw",
				type:"post",
				data:{"rsroomno": rsroomno},
				dataType:"json",
				success:function(){
					alert("예약내역이 삭제되었습니다.");
					location.href="javascript:history.go(0);"; 
				}, 
				error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		  	    }
			});
		}
	}
	
	
	 function editMyrsroom(rsroomno,roomname,rdate,rtime,fk_roomno){
		 var chgYear = rdate.substring(0,4);
		 var chgMonth = rdate.substring(5,7);
		 var chgDay = rdate.substring(8,10);
		
		 chgdate=chgYear+""+chgMonth+""+chgDay;

		 // 시간 비교
		 if(parseInt(chgdate)<parseInt(curTime)){
				 alert("변경할 수 없습니다.");
				 return false;
		}
		 else if(parseInt(chgdate)==parseInt(curTime) && parseInt(curHour)>parseInt(rtime)){
				 alert("변경할 수 없습니다.");
				 return false;
		 }
			 
		 // 모달창 보여주기
		 else{
			 $("div#myModal").modal('show');
			 $("select#selectroom").val(fk_roomno);
			 $("input[name=fk_roomno]").val(fk_roomno);
			 $("input[name=rdate]").val(rdate);
			 $("input[name=rsroomno]").val(rsroomno);
			 
			 $.ajax({
					url: "<%= ctxPath%>/t1/reserve/checkTimeRoom.tw",
					data:{"rdate":rdate, "fk_roomno":fk_roomno},
					dataType: "json",
					success:function(json){
						
						var rtime= Array();
						var index=0;
						var html="";
						$.each(json,function(index,item){
							rtime.push(item.rtime);
						  });
						
						for(var i=0;i<24;i++){
							var index=jQuery.inArray(i,rtime); 
							if(parseInt(chgdate)<parseInt(curTime)){
								index=0;
							}
							else if(parseInt(chgdate)==parseInt(curTime) && i<parseInt(curHour)){
								index=0;
							}
							if(index<0){
								html+="<input type='checkbox' name='chktime' value='"+i+"'/>&nbsp;&nbsp;<span>"+timeText(i)+"</span><br>"
							}
						}
						
						$("td#strtime").html(html);
					},
					error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			  	    }
				 });
			 
			 $('input#rdate').datepicker({
				    onSelect: function (date, datepicker) {
				    	$("td#strtime").html("");
	                    if (rdate != "") {
	                    	 $("input[name=rdate]").val(date);
	                    	func_chktime(date,fk_roomno);
	                    }
	                }
			 	
				});
				
			$("select#selectroom").change(function(){
				fk_roomno=$("select#selectroom").val();
				var rdate=$("input[name=rdate]").val();
				func_chktime(rdate,fk_roomno);
			});
			
				 
			
		 }

	 }
	 
	 
	 function timeText(val){
			
			var timeText="";
			
			if(val<10){
				stime="0"+val;
			}
			else{
				stime=val;
			}
			
			ntime= parseInt(val)+1;
			console.log((typeof(ntime)));
			sntime= ntime.toString();
			
			if(ntime<10){
				sntime="0"+sntime;
			}
			
			timeText=stime+":00 ~ "+sntime+":00";
			
			return timeText;
		}
	 
	 
	 function func_chktime(rdate,fk_roomno){
		 var chgYear = rdate.substring(0,4);
		 var chgMonth = rdate.substring(5,7);
		 var chgDay = rdate.substring(8,10);
		
		 var chgdate=chgYear+""+chgMonth+""+chgDay;
		 
		 $("input[name=fk_roomno]").val(fk_roomno);
		 $("input[name=rdate]").val(rdate);
		
		 $.ajax({
				url: "<%= ctxPath%>/t1/reserve/checkTimeRoom.tw",
				data:{"rdate":rdate, "fk_roomno":fk_roomno},
				dataType: "json",
				success:function(json){
					
					var rtime= Array();
					var index=0;
					var html="";
					$.each(json,function(index,item){
						rtime.push(item.rtime);
					  });
					
					for(var i=0;i<24;i++){
						var index=jQuery.inArray(i,rtime); 
						
						if(parseInt(chgdate)<parseInt(curTime)){
							index=0;
						}
						else if(parseInt(chgdate)==parseInt(curTime) && i<parseInt(curHour)){
							index=0;
						}
						if(index<0){
							html+="<input type='checkbox' name='chktime' value='"+i+"'/>&nbsp;&nbsp;<span>"+timeText(i)+"</span><br>"
						}
					}
					
					$("td#strtime").html(html);
				},
				error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		  	    }
			 });
	 }
	 
	 
	 
	 
	 function showTime(val){
			var strTime="";
			
			var arrTime= new Array();
			
			if(val.indexOf(',')>0){
				arrTime=val.split(',');
			}
			else{
				arrTime[0]=val;
			}
			
			for(var i=0;i<arrTime.length;i++){
				strTime=strTime+timeText(arrTime[i])+", ";
			}
			console.log(strTime.length);
			strTime=strTime.substring(0,strTime.length-2);
			$("input#rtime").val(val);
		}
	 
	 
	 
	 
	 
	 
	 
</script>

<div id="myReserved" style="margin: 30px 0px 30px 50px;">

	<i class='fas fa-tasks fa-lg' style='font-size:24px ;'></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">나의 예약내역</span>
	

	<div style="margin-top: 50px;">
		<ul class="nav nav-pills">
			 <li class="active"><a href="<%= ctxPath%>/t1/myReservedRoom.tw">회의실 예약 내역</a></li>
			 <li><a style="color: black;" href="<%= ctxPath%>/t1/myReservedCar.tw">차량 예약 내역</a></li>
			 <li><a style="color: black;" href="<%= ctxPath%>/t1/myReservedGoods.tw">사무용품 예약 내역</a></li>
		</ul>
	</div>

    <div style="float: right; margin-top: 15px; margin-right: 179px; display: inline-block;">
    	<form name="searchRoom">
			<input type="text" id="fromDate" name="startdate" style="width: 90px;" readonly="readonly">&nbsp;&nbsp; 
		     -&nbsp;&nbsp; <input type="text" id="toDate" name="enddate" style="width: 90px;" readonly="readonly">&nbsp;&nbsp;
		     <select name="roomno">
		     	<option value="0">회의실명</option>
		     	<c:forEach var="room" items="${requestScope.roomList}">	
		     		<option value="${room.roomno}">${room.roomname}</option>
		     	</c:forEach>
		     </select>
			<input type="text" name="searchWord" id="searchWord" placeholder="목적을 입력하세요."/>		
		<button type="button" class="btn_normal" onclick="goSearch()">검색</button>	
		</form>
	</div>

	<div id="reserveList" >
		<table class="table" style="width: 90%;">
			<thead>
				<tr>
					<th style="width: 12%">예약날짜</th>
					<th style="width: 12%">시간</th>
					<th style="width: 12%">회의실명</th>
					<th>목적</th>
					<th>변경</th>
					<th style="width: 10%">취소</th>
				</tr>
			</thead>		
			
			<c:if test="${empty myRoomList}">
				<tr><td colspan="5">회의실 예약 내역이 없습니다.</td></tr>
			</c:if>
			
			<c:if test="${not empty myRoomList}">
				<c:forEach var="myRoom" items="${requestScope.myRoomList}">	
					<tr>
						<td style="vertical-align: middle;">${myRoom.rdate}</td>
						<c:if test="${myRoom.rtime<9}">
						<td style="vertical-align: middle;">0${myRoom.rtime}:00 ~ 0${myRoom.rtime+1}:00</td>
						</c:if>
						<c:if test="${myRoom.rtime==9}">
						<td style="vertical-align: middle;">0${myRoom.rtime}:00 ~ ${myRoom.rtime+1}:00</td>
						</c:if>
						<c:if test="${myRoom.rtime >9}">
						<td style="vertical-align: middle;">${myRoom.rtime}:00 ~ ${myRoom.rtime+1}:00</td>
						</c:if>
						
						<td style="vertical-align: middle;">${myRoom.roomname}</td>
						<td style="vertical-align: middle; text-align: left;">${myRoom.rsubject}</td>
						<td><button type="button" class='btn_r' onclick="editMyrsroom('${myRoom.rsroomno}','${myRoom.roomname}','${myRoom.rdate}','${myRoom.rtime}','${myRoom.fk_roomno}')">변경</button></td>
						<td><button type="button" class='btn_r' onclick="delMyrsroom('${myRoom.rsroomno}','${myRoom.roomname}','${myRoom.rdate}','${myRoom.rtime}')">취소</button></td>
					</tr>
				</c:forEach>
			</c:if>	
			
		</table>	
	
	</div>

	<div align="center">${pageBar}</div>

</div>

<!-- 수정할 수 있는 모달창 생성 -->
<!-- The Modal -->
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">예약 변경</h4>
        </div>
        <div class="modal-body">
         	<form name="editRoom">
         		<table style="width: 100%;" class="table table-bordered">
         			<tr>
         				<td style="text-align: left; vertical-align: middle;">신청날짜</td>
         				<td style="text-align: left; "><input type="text" id="rdate" name="rdate"/></td>
         			</tr>
         			<tr>
         				<td style="text-align: left; vertical-align: middle;">신청회의실</td>
         				<td style="text-align: left; padding-left: 5px;">
	         				<select id="selectroom">
	         					<c:forEach var="room" items="${roomList}">
	         					<option value="${room.roomno}">${room.roomname}</option>
	         					</c:forEach>
	         				</select>
         				</td>
         			</tr>
         			<tr>
         				<td style="text-align: left; vertical-align: middle;">신청 가능 시간</td>
         				<td id="strtime" style="text-align: left; padding-left: 5px;"></td>
         			</tr>
         			<tr>
         				<td style="text-align: left; vertical-align: middle;">목적</td>
         				<td style="text-align: left; padding-left: 5px;"><input type="text" id="purpose" class="form-control" name="rsubject"/></td>
         			</tr>
         		</table>
         		<!-- input 값에 사번, 직원명, 부서, 신청회의실번호, 신청시간, 용도 넣어줘서 이걸 insert할 때 사용하기 -->
         		<div>
					<input type="hidden" id="roomno" value="" name="fk_roomno"/>
					<input type="hidden" name="fk_employeeid" value="${sessionScope.loginuser.employeeid}"/>
					<input type="hidden" id="rtime" value="" name="rtime"/>
					<input type="hidden" id="rsroomno" value="" name="rsroomno"/>
				</div>
         	</form>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="button" id="editReserve" class="btn_normal" data-dismiss="modal">예약</button>
          <button type="button" class="btn_r" data-dismiss="modal" >취소</button>
        </div>
        
      </div>
    </div>
  </div>