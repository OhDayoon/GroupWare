<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
 	String ctxPath = request.getContextPath();
	//     /groupware
%>

<link href='<%=ctxPath %>/resources/fullcalendar/main.min.css' rel='stylesheet' />
<style type="text/css">

table#rscar th, td{
	text-align: center;
	vertical-align: middle;
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
	padding: 3px 0px;
}
/* 달력 css */
div#calendarWrapper {
	float: left;
}

.fc-scroller {
   	overflow-y: hidden !important;
	}

a{
    color: #000;
    text-decoration: none;
    background-color: transparent;
    cursor: pointer;
}

a:hover {
    color: #000;
    cursor: pointer;
    text-decoration: none;
    background-color: transparent;
}
.fc-daygrid{
    color: #000;
    cursor: pointer;
    text-decoration: none;
    background-color: transparent;
}


.fc-header-toolbar{
	height: 30ox;
}

div.car{
	float: left;
/* 	display: inline-block; */
	width:500px;
	margin-left: 300px;
	margin-bottom: 140px;
}

.carClick{
	background-color: #b1b8cd;
}

.info{
	background-color: #b1b8cd;
	cursor: pointer;
}

</style>


<script src='<%=ctxPath %>/resources/fullcalendar/main.min.js'></script>
<script src='<%=ctxPath %>/resources/fullcalendar/ko.js'></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
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

var cgtime= curYear + "-" + curMonth + "-" + curDay;

$(document).ready(function(){

	
	
	$("button#btn_Reserve").hide(); // 등록버튼 숨기기
	
	$("div#myModal").modal('hide');
	var cno="";
	var chgdate="";

	$("input#chgdate").val(cgtime); 

	$("tr.selectCar").bind("mouseover",function(){			
		$(this).addClass("info");			
	}); 
	
	$("tr.selectCar").bind("mouseout",function(){
		$(this).removeClass("info");
	});
	
	$("tr.selectCar").click(function(){
		$("tr.selectCar").removeClass("carClick");
		var cno = $(this).find("td#findno").html();
		var carname= $(this).find("td#findname").html();

		$("input#cno").val(cno);
		$("input#carname").val(carname);
		$(this).addClass("carClick");
		ajaxReserve();
	});
	
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
          initialView: 'dayGridMonth',
          locale: 'ko',
          selectable: true,
	      editable: false,
	      headerToolbar: {
	    	    left: 'prev',
	    	    center: 'title',
	    	    right: 'next' // today 추가?
	    	  },
	    	  
          dateClick: function(info) {
        	//  alert('Date: ' + info.dateStr);
        	    $(".fc-day").css('background','none'); // 현재 날짜 배경색 없애기
        	    info.dayEl.style.backgroundColor = '#b1b8cd';
        	    chgdate=info.dateStr;
        	    $("input#chgdate").val(chgdate);
        	    
        	    if($("input#cno").val().trim()!=""){
        	    	ajaxReserve();
        	    }
        	  }
        });
        
        
        calendar.render();
        calendar.setOption('height', 510);
        
        
        // '등록' 버튼 클릭 이벤트
    	$("button#btn_Reserve").click(function(){
    		func_reserve();
    	});
    
        
    	$("button#realReserve").click(function(event){
    		 event.stopPropagation();
             event.preventDefault();
             
             var purpose = $("input#purpose").val().trim();
             var destination = $("input#destination").val().trim();
             if(purpose == "" && destination == ""){
            	 alert("목적 및 도착지를 입력하세요");
            	 return;
             }
             else if(purpose != "" && destination == ""){
            	 alert("도착지를 입력하세요");
            	 return;
             }
             else if(purpose == "" && destination != ""){
            	 alert("목적을 입력하세요");
            	 return;
             }
             else{
 
                 $.ajax({
                 	url: "<%= ctxPath%>/t1/addReserveCar.tw",
                 	type: "post",
                 	data: $("form[name=reserveCar]").serialize(),
                 	dataType: "json",
                 	success:function(json){
                 		if(json.n>0){
                 			alert("예약되었습니다.");
                 			$("#myModal").modal('hide');
                 			ajaxReserve();
                 		}
                 		
                 	},
                 	error: function(request, status, error){
         	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
                 	}
                  });
             }
    	});
    	
}); // end of $(document).ready(function(){}------

	function ajaxReserve(){
	
		var cno=$("input#cno").val();
    	var chgdate=$("input#chgdate").val();
    	 
		if( cno!= "" && chgdate != "" ){
         	$.ajax({
         		url:"<%= ctxPath%>/t1/rscar/reserveCar.tw",
         		data:{"cno":cno, "chgdate":chgdate},
         		dataType: "json",
         		success:function(json){
         		 //	console.log("json:"+json);
         		 	
         		 // 불러온 배열을 함수에 값 지정해주기
         		 	showReserve(json);
         		}
         		, error: function(request, status, error){
    	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
      	      }
         	});
         	
         }
	}
	
	
	
	function showReserve(json){
	//	console.log("개수:"+json.length); 
		
		var chgdate=$("input#chgdate").val();

		// 선택한 날짜 숫자로 바꿔주는 과정
		var chgYear = chgdate.substring(0,4);
		var chgMonth = chgdate.substring(5,7);
		var chgDay = chgdate.substring(8,10);
	
		chgdate=chgYear+""+chgMonth+""+chgDay;

		
		var rscno=Array();
		var rctime= Array();
		var name=Array();
		var fk_employeeid=Array();
		var rcsubject=Array();
		var rdestination=Array();
		var rcpeople=Array();
		var cstatus=Array();
		var employeeid = $("input[name=fk_employeeid]").val();
		$("tbody#rstimeList tr").remove(); // 입력하신 정보가 없습니다. 사라지게 만들기
		
		// select 해온 값을 table td 값에 쌓아둔다.
		$.each(json, function(index,item){
			rscno.push(item.rscno);
			rctime.push(item.rctime);
			name.push(item.name);
			fk_employeeid.push(item.fk_employeeid);
			rcsubject.push(item.rcsubject);
			rdestination.push(item.rdestination);
			rcpeople.push(item.rcpeople);
			cstatus.push(item.cstatus);
		});
		
	
		for(var i=0;i<24;i++){ // 시간 정보 입력
			
			var time="";
			var html="";
			var fontType="";
			var clickEvent="";
			var chkBox="";
			var index=jQuery.inArray(i,rctime); // 시간배열에 i가 존재하는지 index값을 설정  i에 맞는 시간대가 있다면 index의 값은 무조건 0이상이된다. 
			console.log(i+"인덱스값: "+index);
			console.log("사긴"+curHour);
			// 선택한 날짜와 오늘날짜가 같을 때
			if(parseInt(curTime)==parseInt(chgdate)){ 
				if(i>=curHour){
					 fontType="#000";
				
					 // 이미 예약된 값에는 체크박스가 생성되면 안된다.
					if(index > -1){
						fontType = "#D1D4DB";
						
					} 
					else {
						chkBox = "<input type='checkbox' id='rsCheck"+i+"' name='rsCheck' value="+i+">";
						
					}
				}
				else {
					// 오늘 날짜의 예약 등록 불가능한 경우
					fontType = "#D1D4DB";
				}
			}
			// 오늘 이후 날짜의 예약 등록 가능한 경우
			else if (parseInt(curTime)<parseInt(chgdate)) {
				
				fontType = "#000";

				if(index > -1){
					fontType = "#D1D4DB";
					clickEvent = "style='cursor:pointer;'";
				} else {
					chkBox = "<input type='checkbox' id='rsCheck"+i+"' name='rsCheck' value="+i+">";
					
				}
			} 
			else {
				// 오늘 이전 날짜의 예약 등록 불가능한 경우
				fontType = "#D1D4DB";
			}

			if( index > -1){

				html += "<tr><td style='vertical-align: middle;' ><label id='id_time' ><font color='"+fontType+"'>"+timeText(rctime[index])+"</label></span></td>";
				html += "<td style='vertical-align: middle;' >"+name[index]+"</td>";
				
				html += "<td style='vertical-align: middle;' >"+rdestination[index]+"</td>";
				html += "<td style='vertical-align: middle; ' >"+rcsubject[index]+"</td>";
				console.log(rcpeople[index]);
				
				if(rcpeople[index]==undefined){
					html +="<td></td>"
				}
				else{
					html += "<td style='vertical-align: middle;'>"+rcpeople[index]+"</td>";
				}
				
				if(fk_employeeid[index] != employeeid) {
						html += "<td></td><td></td></tr>";
				} 
				else {
					if(parseInt(curTime)>parseInt(chgdate)){ // 현재 날짜가 클릭한 날짜보다 큰 경우
						if(cstatus[index]=='0'){
							html += "<td style='vertical-align: middle;'>미승인</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>삭제</button></td></tr>";
						}
						else if(cstatus[index]=='1'){
							html += "<td style='vertical-align: middle;'>사용완료</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>삭제</button></td></tr>";
						}
					}
					else if(parseInt(curTime)==parseInt(chgdate)){
						
						if(i<curHour){
							if(cstatus[index]=='0'){
								html += "<td style='vertical-align: middle;'>미승인</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>삭제</button></td></tr>";
							}
							else if(cstatus[index]=='1'){
								html += "<td style='vertical-align: middle;'>사용완료</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>삭제</button></td></tr>";
							}
						}
						else if(i==curHour){
							if(cstatus[index]=='0'){
								html += "<td style='vertical-align: middle;'>승인요청</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>취소</button></td></tr>";
							}
							else if(cstatus[index]=='1'){
								html += "<td style='vertical-align: middle;'>사용중</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>삭제</button></td></tr>";
							}
						}
						else{
							if(cstatus[index]=='0'){
								html += "<td style='vertical-align: middle;'>승인요청</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>취소</button></td></tr>";
							}
							else if(cstatus[index]=='1'){
								html += "<td style='vertical-align: middle;'>승인완료</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>취소</button></td></tr>";
							}
						}
			
					}
					else{ // 현재 날짜가 클릭한 날짜보다 작은 경우
						if(cstatus[index]=='0'){
							html += "<td style='vertical-align: middle;'>승인요청</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>취소</button></td></tr>";
						}
						else if(cstatus[index]=='1'){
							html += "<td style='vertical-align: middle;'>승인완료</td><td><button id='btn_delete' class='btn_r'  onclick='deleteReserve("+rscno[index]+")';>취소</button></td></tr>";
						}
					}
					
				}
			} 
			else {
				html += "<tr><td style='vertical-align: middle;'>"+chkBox+"<label for='rsCheck"+i+"' id='id_time'><font color='"+fontType+"'>"+timeText(i)+"</font></label></td>";
				html += "<td></td><td></td><td></td><td></td><td></td><td></td></tr>";
			}
			$("tbody#rstimeList").append(html);
		}// end of for----
		
		
		$("button#btn_Reserve").show();
	} // end of reservationList
			
	
	// 시간대 구성
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
	
	// 예약 등록버튼을 눌렀을 때 발생하는 함수
	function func_reserve(){
		var checkCnt= $("input:checkbox[name=rsCheck]:checked").length;
		
		if(checkCnt<1){
			alert("시간을 선택하세요.");
			return false; // 종료
		}
		
		else{
			
			// 선택한 회의실 값 모달창에 보내주기
			var carname = $("input#carname").val();
			
			var chkTime="";
			
			$("input:checkbox[name=rsCheck]:checked").each(function(){
				chkTime= chkTime+ $(this).val()+",";
			});
			
			if(chkTime.length>0){
				chkTime = chkTime.substring(0,chkTime.length-1);
			}
			
			showTime(chkTime);
			
			
			$("button[id='btn_Reserve']").attr("data-target","#myModal");
			$("td#carname").html(carname);
			
			
		}
	}
	
	// modal 창에 선택한 시간 보여주고 input태그에 선택한 시간 값 넣어주기
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
		$("td#strtime").html(strTime);
		$("input#rctime").val(val);
	}
	
	
	// 카카오 지도 팝업창 오픈
	function openMap(){
		
		var destination = $("input#destination").val().trim();
		$("input#destination").val(destination);
		if(destination==""){
			alert("목적지를 입력하세요.");
			return;
		}
		else{
			openChild=window.open("<%= ctxPath%>/t1/searchMap.tw?searchPlace="+destination, "openChild", "left=100px, top=100px, width=800px, height=800px");
	   		openChild.document.getElementById('keyword').value = document.getElementById('destination').value;
	      }
	
	}
	
	// 차량 삭제하기
	function deleteReserve(rscno){

		$.ajax({
			url: "<%= ctxPath%>/t1/rsCar/delReserveCar.tw",
			type: "post",
			data: {"rscno":rscno},
			dataType: "json",
			success:function(json){
				alert("예약내역이 삭제되었습니다.");
				 ajaxReserve();
			}
			, error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
  	      }
		});
	}

	
</script>

<div class="rsCarContainer" style="margin: 30px 0px 30px 50px;"> 
	<i class='fas fa-tasks fa-lg' style='font-size:24px'></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">차량 대여신청</span>
	
	<div>
		<div id="calendarWrapper" >
			<div id="calendar" style="width: 450px; margin-top: 50px; margin-bottom: 80px; margin-left: 200px;" ></div>
		</div>
		
	  
		<div class="car" >
			<table style="width: 90%; margin-top: 200px;" class="table table-bordered" >
	            <tr><td colspan="2">차량 목록</td></tr>
	            	<c:if test="${empty requestScope.carList}">
	            		<tr><td colspan="2">차량이 존재하지 않습니다.</td></tr>
	            	</c:if>
	            	<c:if test="${not empty requestScope.carList}">
			        	 <c:forEach var="cvo" items="${requestScope.carList}">
				        	<tr class="selectCar">
					            <td id="findno" style="display: none;">${cvo.cno}</td>
					            <td id="findname"  style="width: 30%; text-align: left; padding-left: 15px;">${cvo.carname}</td>
					            <td style="width: 70%; text-align: left; border-left: none; padding-left: 15px; ">차량번호: ${cvo.carlicense}</td>
				           </tr>
	        			 </c:forEach>
	          		</c:if>
	         </table>
		</div>
	</div>
	
	<div class="middle">
		<table  style="width: 92%;" class="table table-bordered" id="rscar">
			<colgroup>
				<col width="15%">
				<col width="15%">
				<col width="20%">
				<col width="15%">
				<col>
				<col width="10%">
				<col width="10%">
			</colgroup>
			
			<thead>
				<tr>
					<th colspan="7">예약시간 선택</th>
				</tr>
				<tr>
				   <th>사용시간</th>
				   <th>신청자</th>
				   <th>도착지</th>
				   <th>목적</th>
				   <th>탑승자</th>
				   <th>예약현황</th>
				   <th>취소 / 삭제</th>
				</tr>
			</thead>
			<tbody id="rstimeList">
				<tr>
					<td colspan="7">입력된 정보가 없습니다.</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	
	<div align="center" style="margin-top: 20px; margin-bottom: 20px;">
		<button id="btn_Reserve" class="btn_normal"  data-toggle="modal" data-target="" >등록</button>
	</div>


</div>

<!-- The Modal -->
 <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">신청등록</h4>
        </div>
        <div class="modal-body">
         	<form name="reserveCar">
         		<table style="width: 100%;" class="table table-bordered">
         			<tr>
         				<td style="text-align: left; ">신청차량</td>
         				<td id="carname" style="text-align: left; padding-left: 5px;"></td>
         			</tr>
         			<tr>
         				<td style="text-align: left;">신청시간</td>
         				<td id="strtime" style="text-align: left; padding-left: 5px;"></td>
         			</tr>
         			
         			<tr>
         				<td style="text-align: left;">도착지</td>
         				<td style="text-align: left; padding-left: 5px;">
         					  <input type="text" class="form-control" placeholder="Search" id="destination" name="rdestination" style="width:250px; display: inline-block;">
						      <button class="btn_normal" type="button" onclick="openMap()">검색</button>
         				</td>	
         			</tr>
         			
         			<tr>
         				<td style="text-align: left;">목적</td>
         				<td style="text-align: left; padding-left: 5px;"><input type="text" id="purpose" class="form-control" name="rcsubject"/></td>
         			</tr>
         			<tr>
         				<td style="text-align: left;">탑승자</td>
         				<td style="text-align: left; padding-left: 5px;"><input type="text" id="rcpeople" class="form-control" name="rcpeople"/></td>
         			</tr>
         		</table>
         		<!-- input 값에 사번, 직원명, 부서, 신청회의실번호, 신청시간, 용도 넣어줘서 이걸 insert할 때 사용하기 -->
         		<div>
					<input type="hidden" id="cno" value="" name="cno"/>
					<input type="hidden" id="chgdate" value="" name="rcdate"/>
					<input type="hidden" id="rctime" value="" name="rctime"/>
					<input type="hidden" name="fk_employeeid" value="${sessionScope.loginuser.employeeid}"/>
				</div>
         	</form>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer" align="center">
        	<button type="button" id="realReserve" class="btn_normal" data-dismiss="modal">예약</button>
          <button type="button" class="btn_r"  data-dismiss="modal">취소</button>
        </div>
        
      </div>
    </div>
  </div>
  






<!-- 예약 현황 보기 클릭시 값 넘겨줄 때 사용 -->
<div>
	<input type="hidden" id="cno" value=""/>
	<input type="hidden" id="carname" value=""/>
	<input type="hidden" id="chgdate" value=""/>
</div>
<div>
	<input type="hidden" id="rdestination"/>
</div>
