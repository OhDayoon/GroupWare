<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
 	String ctxPath = request.getContextPath();
	//     /groupware
%>
<!DOCTYPE html>

<meta charset="UTF-8">

<link href='<%=ctxPath %>/resources/fullcalendar/main.min.css' rel='stylesheet' />
<style type="text/css">

div#wrapper1{
	float: left; display: inline-block; width: 20%; margin-top:250px; font-size: 13pt;
}

div#wrapper2{
	display: inline-block;width: 80%; padding-left: 20px;
}
/* full calendar css 시작 */
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
	height: 30px;
}
.fc-day-number .fc-sat .fc-past { color:#0000FF; }     /* 토요일 */
.fc-day-number .fc-sun .fc-past { color:#FF0000; }    /* 일요일 */

/* full calendar css 끝*/
ul{
	list-style: none;
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

button.btn_edit{
	border: none;
	background-color: #fff;
}


</style>

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>

<!-- full calendar에 관련된 script -->
<script src='<%=ctxPath %>/resources/fullcalendar/main.min.js'></script>
<script src='<%=ctxPath %>/resources/fullcalendar/ko.js'></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	
	// 내 캘린더 종류 보여주기
	showmyCal();
	// 사내 캘린더 종류 보여주기
	showCompanyCal();
	
	
	$("input#allComCal").click(function(){
		var allCheck = $(this).is(":checked");
		
		if (allCheck == false){
			$("input:checkbox[name=comscno]").prop("checked",false);
		}
		else{
			$("input:checkbox[name=comscno]").prop("checked",true);
		}
	});
	// 사내캘린더 체크박스 체크/ 해제		
	$(document).on("click","input:checkbox[name=comscno]",function(){	
		
		 var ccno = document.querySelectorAll("input.comscno");
	      ccno.forEach(function (csel) {
	        csel.addEventListener("change", function () {
	        	 calendar.refetchEvents();
	        });
	      });
	      
		var bool = $(this).prop("checked");
		
		if(bool){
			
			var flag=false;
						
			$("input:checkbox[name=comscno]").each(function(index, item){
				
				var bChecked = $(item).prop("checked");
				
				if(!bChecked){
					flag=true;
					return false; 
				}
				
			}); // end of $("input:checkbox[name=person]").each(function(index, item){})---------

			if(!flag){		
                $("input#allComCal").prop("checked",true);
			}
		}
		else{
			$("input#allComCal").prop("checked",false);
		}
		
	});
	
	// === 체크박스 전체 선택/전체 해제 === //
	$("input#allMyCal").click(function(){		
		var bool = $(this).prop("checked");
		$("input:checkbox[name=myscno]").prop("checked", bool);
	}); // end of $("input:checkbox[id=checkall]").click(function(){})-------
	
	
	// 내 캘린더 선택
	$(document).on("click","input:checkbox[name=myscno]",function(){	
	
		var scno = document.querySelectorAll("input.myscno");
	      scno.forEach(function (sel) {
	        sel.addEventListener("change", function () {
	        	 calendar.refetchEvents();
	        });
	      });

		var bool = $(this).prop("checked");
		
		if(bool){
			
			var flag=false;
			
			
			$("input:checkbox[name=myscno]").each(function(index, item){
				
				var bChecked = $(item).prop("checked");
				
				if(!bChecked){
					flag=true;
					return false; 
				}
				
			}); // end of $("input:checkbox[name=person]").each(function(index, item){})---------

			if(!flag){		
                $("input#allMyCal").prop("checked",true);
			}
			 
		}
		else{
			$("input#allMyCal").prop("checked",false);
		}
	
	});
		
			
	
	
	
	
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
	  
	    // 풀캘린더와 관련된 소스코드(화면이 로드되면 전체 화면 보이게 해줌)
	    var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
	        initialView: 'dayGridMonth',
	        locale: 'ko',
	        selectable: true,
		    editable: false,
		    headerToolbar: {
		    	  left: 'prev,next today',
		          center: 'title',
		          right: 'dayGridMonth dayGridWeek dayGridDay'
		    },
		    dayMaxEventRows: true, // for all non-TimeGrid views
		    views: {
		      timeGrid: {
		        dayMaxEventRows: 3 // adjust to 6 only for timeGridWeek/timeGridDay
		      }
		    },
		  
			// db와 연동하는 법
		    events:function(info, successCallback, failureCallback){
		
		    	 $.ajax({
	                 url: '<%= ctxPath%>/t1/selectSchedule.tw',
	                 data:{"fk_employeeid":$('input#fk_employeeid').val()},
	                 dataType: "json",
	                 success:function(json) {
	                	 var events = [];
	                     if(json!=null){
	                         
	                             $.each(json, function(index, item) {
	            						
	                                    var startdate=moment(item.startdate).format('YYYY-MM-DD HH:mm:ss');
	                                    var enddate=moment(item.enddate).format('YYYY-MM-DD HH:mm:ss');
	                                    var subject = item.subject;
	                      
	                                   // 사내 캘린더 소분류 보이게
	                                   if($("input:checkbox[name=comscno]:checked").length<=$("input:checkbox[name=comscno]").length){
		                                   for(var i=0;i<$("input:checkbox[name=comscno]:checked").length;i++){
		                                	  
		                                		   if($("input:checkbox[name=comscno]:checked").eq(i).val() == item.scno){
		   			                          //         alert($("input:checkbox[name=myscno]:checked").eq(i).val());
		                                			   events.push({
		   			                                	   			id: item.scno,
		   			                                               title: item.subject,
		   			                                               start: startdate,
		   			                                               end: enddate,
		   			                                        	   url: "<%= ctxPath%>/t1/detailSchedule.tw?sdno="+item.sdno,
		   			                                               color: item.color,
		   			                                               cid:item.scno
		   			                                   }); // events.push
		   		                                    
		   	                                    }
		                                	   
		                                   }
		                                 
	                                   }
	                                    
	                                   // 내캘린더 소분류 보이게
	                                  if($("input:checkbox[name=myscno]:checked").length<=$("input:checkbox[name=myscno]").length){
		                                   for(var i=0;i<$("input:checkbox[name=myscno]:checked").length;i++){
		                                	  
		                                		   if($("input:checkbox[name=myscno]:checked").eq(i).val() == item.scno && item.fk_employeeid ==  "${loginuser.employeeid}"){
		   			                          //         alert($("input:checkbox[name=myscno]:checked").eq(i).val());
		                                			   events.push({
		   			                                	   			id: item.scno,
		   			                                               title: item.subject,
		   			                                               start: startdate,
		   			                                               end: enddate,
		   			                                        	   url: "<%= ctxPath%>/t1/detailSchedule.tw?sdno="+item.sdno,
		   			                                               color: item.color,
		   			                                               cid:item.scno
		   			                                   }); // events.push
		   	                                    }
		                                   }
	                                   }

	                                  // 공유받은 캘린더
	                                  if (item.bcno==1 && item.fk_employeeid != "${loginuser.employeeid}" && "${loginuser.employeeid}".indexOf(item.joinemployee)){
	                                        
   	                                   events.push({
   	                                	   			id: item.bcno,
   	                                               title: item.subject,
   	                                               start: startdate,
   	                                               end: enddate,
   	                                        	   url: "<%= ctxPath%>/t1/detailSchedule.tw?sdno="+item.sdno,
   	                                               color: item.color,
   	                                              cid:"3"
   	                                   }); // events.push
   	                           		}
	                                
	                             });  //.each()
	                         }                             
	                         console.log(events);                       
	                         successCallback(events);                               
	                  }//success: function end                          
	          }); //ajax end
	        }, //events:function end	       
			// 날짜 클릭할 때 발생하는 이벤트(일정 등록창으로 넘어간다)
	        dateClick: function(info) {
	      	//  alert('Date: ' + info.dateStr);
	      	    $(".fc-day").css('background','none'); // 현재 날짜 배경색 없애기
	      	    info.dayEl.style.backgroundColor = '#b1b8cd';
	      	    date=info.dateStr;
	      	    $("input[name=chooseDate]").val(date);
	      	    
	      	    var frm = document.dateFrm;
	      	    frm.method="GET";
	      	    frm.action="<%= ctxPath%>/t1/insertSchedule.tw";
	      	    frm.submit();
	      	    
	      	  },
	      	  // 사내캘린더와 내 캘린더를 나누어서 보여줄 수 있다.
	    	 eventDidMount: function (arg) {
		            var bcno = document.querySelectorAll(".fk_bcno");
		            bcno.forEach(function (v) {
		              if (v.checked) {
		                if (arg.event.extendedProps.cid === v.value) {
		                	console.log("cid"+arg.event.extendedProps.cid);
		                	console.log("v"+ v.value);
		                  arg.el.style.display = "block";
		                }
		              } else {
		                if (arg.event.extendedProps.cid === v.value) {
		                  arg.el.style.display = "none";
		                }
		              }
		            });
		      }
	  });
      calendar.render();
 
      var bcno = document.querySelectorAll("input.fk_bcno");
      bcno.forEach(function (el) {
        el.addEventListener("change", function () {
        	 calendar.refetchEvents();
          console.log(el);
        });
      });
      
     
   
      // 검색 할 때 엔터를 친 경우
      $("input#searchWord").keydown(function(event){
			if(event.keyCode == 13){ 
				goSearch();
			}
      });
     
      
     // 모달 창에서 입력된 값 초기화 
      $('.modal').on('hidden.bs.modal', function (e) {
    	    $(this).find('form')[0].reset();
    	});
      
    
}); // end of $(document).ready(function()

	// 내 캘린더 보여주기	
	function showmyCal(){
		$.ajax({
			 url: "<%= ctxPath%>/t1/showMyCalendar.tw",
			 type: "get",
			 data: {"fk_employeeid": "${sessionScope.loginuser.employeeid}"},
			 dataType: "json",
			 success:function(json){
				 var html="<table style='width:80%;'>";
				 if(json.length>0){
					 
					 $.each(json, function(index, item){
						 html+="<tr style='font-size: 11pt;'>";
						 html+="<td style='width:60%; padding: 3px 0px;'><input type='checkbox' name='myscno' class='fk_bcno myscno' style='margin-right: 3px;' value='"+item.scno+"' checked/>"+item.scname+"</td>";
						 html+="<td style='width:20%; padding: 3px 0px;'><button class='btn_edit editCal' data-target='editCal' onclick='editMyCalendar("+item.scno+",\""+item.scname+"\")'><i class='fas fa-edit'></i></button></td>";
						 html+="<td style='width:20%; padding: 3px 0px;'><button class='btn_edit delCal' onclick='delCalendar("+item.scno+",\""+item.scname+"\")'><i class='fas fa-trash'></i></button></td></tr>";
					 });
					 html+="</table>";
				 }
				 $("div#myCal").html(html);
			 },
			 error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		         }	 	
		});
	}	
	
	// 사내 캘린더 보여주기
	function showCompanyCal(){
		$.ajax({
			 url: "<%= ctxPath%>/t1/showCompanyCalendar.tw",
			 type: "get",
			 dataType: "json",
			 success:function(json){
					 var html="<table style='width:80%;'>";
					 if(json.length>0){
						 
						 $.each(json, function(index, item){
							 html+="<tr style='font-size: 11pt;'>";
							 html+="<td style='width:60%; padding: 3px 0px;'><input type='checkbox' name='comscno' class='fk_bcno comscno' style='margin-right: 3px;' value='"+item.scno+"' checked/>"+item.scname+"</td>";
							 if("${loginuser.fk_pcode}" =='3' && "${loginuser.fk_dcode}" == '4' ){
							 html+="<td style='width:20%; padding: 3px 0px;'><button class='btn_edit' data-target='editCal' onclick='editComCalendar("+item.scno+",\""+item.scname+"\")'><i class='fas fa-edit'></i></button></td>";
							 html+="<td style='width:20%; padding: 3px 0px;'><button class='btn_edit delCal' onclick='delCalendar("+item.scno+",\""+item.scname+"\")'><i class='fas fa-trash'></i></button></td>";
							 }
						});
					 		html+="</tr></table>";
					 	
					 }
				 $("div#companyCal").html(html);
				 
			},
			 error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	         }	 	
		});
	}	
	
	// 내 캘린더 소분류 추가하기
	function addMyCalendar(){
		$('#addMyCal').modal('show');	
	}
	
	// 내 캘린더 추가 모달창에서 추가 버튼 클릭시
	function goAddMyCal(){
		
		if($("input.addscname").val().trim() == ""){
  		  alert("캘린더명을 입력하세요");
  		  return;
  	  }
  	  else{
  		  
  		 $.ajax({
  			 url: "<%= ctxPath%>/t1/addMyCalendar.tw",
  			 type: "post",
  			 data: {"scname": $("input.addscname").val(), "fk_employeeid": "${loginuser.employeeid}"},
  			 dataType: "json",
  			 success:function(json){
  				 
  				 if(json.n!=1){
  					alert("이미 존재하는 캘린더 명입니다.");
  					return;
  				 }
  				 else if(json.n==1){
  					 $('#addMyCal').modal('hide'); 
  					 alert("내 캘린더에 "+$("input.addscname").val()+"명이 추가되었습니다.");
  					$("input.addscname").val("");
  					 showmyCal();
  					
  				 }
  			 },
  			 error: function(request, status, error){
   	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
     	         }	 
  		 });
  	  }
	}
	
	
	// 사내 캘린더 소분류 추가하기
	function addComCalendar(){
		$('#addComCal').modal('show');	
	}
	
	// 시내 캘린더 추가 모달창에서 추가 버튼 클릭시
	function goAddComCal(){
		
		if($("input.addcomscname").val().trim() == ""){
  		  alert("캘린더명을 입력하세요");
  		  return;
  	  }
  	  else{
  		  
  		 $.ajax({
  			 url: "<%= ctxPath%>/t1/addComCalendar.tw",
  			 type: "post",
  			 data: {"scname": $("input.addcomscname").val(), "fk_employeeid": "${loginuser.employeeid}"},
  			 dataType: "json",
  			 success:function(json){
  				 if(json.n!=1){
   					alert("이미 존재하는 캘린더 명입니다.");
   					return;
   				 }
  				 else if(json.n==1){
  					 $('#addComCal').modal('hide'); 				
  					 alert("사내 캘린더에 "+$("input.addcomscname").val()+"명이 추가되었습니다.");
  					 $("input.addcomscname").val("");
  					 showCompanyCal(); 
  					
  				 }
  			 },
  			 error: function(request, status, error){
   	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
     	         }	 
  		 });
  	  }
	}
	
	// 내 캘린더 수정 모달창 나타나기
	function editMyCalendar(scno,scname){
		$('#editMyCal').modal('show');
		$("input.editmyscno").val(scno);
		$("input.editmyscname").val(scname);
	}
	
	// 내캘린더 수정 모달창에서 수정하기 클릭
	function goEditMyCal(){
		if($("input.editmyscname").val().trim() == ""){
	  		  alert("캘린더명을 입력하세요");
	  		  return;
	  	  }
	  	  else{
			$.ajax({
				url:"<%= ctxPath%>/t1/editMyCalendar.tw",
				type: "post",
				data:{"scno":$("input.editmyscno").val(), "scname": $("input.editmyscname").val(), "employeeid":"${loginuser.employeeid}"},
				dataType:"json",
				success:function(json){
					if(json.n!=1){
	   					alert("이미 존재하는 캘린더 명입니다.");
	   					return;
	   				 }
					if(json.n==1){
						$('#editMyCal').modal('hide');
						alert("캘린더를 수정하였습니다.");
						showmyCal(); 
					}
				},
				 error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			    }
			});
	  	  }
	}
	
	
	// 사내 캘린더 수정 모달창 나타나기
	function editComCalendar(scno,scname){
		$('#editComCal').modal('show');
		$("input.editcomscno").val(scno);
		$("input.editcomscname").val(scname);
	}
	
	// 사내캘린더 수정 모달창에서 수정하기 클릭
	function goEditComCal(){
		if($("input.editcomscname").val().trim() == ""){
	  		  alert("캘린더명을 입력하세요");
	  		  return;
	  	  }
	  	  else{
			$.ajax({
				url:"<%= ctxPath%>/t1/editComCalendar.tw",
				type: "post",
				data:{"scno":$("input.editcomscno").val(), "scname": $("input.editcomscname").val(), "employeeid":"${loginuser.employeeid}"},
				dataType:"json",
				success:function(json){
					if(json.n!=1){
	   					alert("이미 존재하는 캘린더 명입니다.");
	   					return;
	   				 }
					if(json.n==1){
						$('#editComCal').modal('hide');
						alert("캘린더를 수정하였습니다.");
						showCompanyCal();
					}
				},
				 error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			    }
			});
	  	  }
	}
	
	
	// 캘린더 소분류 카테고리 삭제하기
	function delCalendar(scno,scname){
		
		if(confirm(scname+" 캘린더를 삭제 하시겠습니까?")){
		$.ajax({
			url:"<%= ctxPath%>/t1/deleteCalendar.tw",
			type: "post",
			data:{"scno":scno},
			dataType:"json",
			success:function(json){
				if(json.n==1){
					alert("해당 캘린더를 삭제하였습니다.");
					location.href="javascript:history.go(0);"; 
				}
			},
			 error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		    }
		});
		}
		
	}
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

	
	<div style="margin: 30px 0px 30px 50px; ">
	<i class="far fa-calendar-alt fa-2x"></i>&nbsp;<span style="font-size: 18pt; font-weight: bold;">일정관리</span>
	
	<div style="width: 90%;">
		<div id="wrapper1">
			<input type="hidden" value="${sessionScope.loginuser.employeeid}" id="fk_employeeid"/>
			
			<input type="checkBox" value="2" id="allComCal" class="fk_bcno" name="bcno" checked/>&nbsp;&nbsp;<span>사내 캘린더</span>
			<c:if test="${loginuser.fk_pcode =='3' && loginuser.fk_dcode == '4' }">
				<button class="btn_edit" style="float: right;" data-target="addComCal" onclick="addComCalendar()"><i class='fas'>&#xf055;</i></button>
			</c:if>
			<div id="companyCal" style="margin-left: 50px; margin-bottom: 10px;"></div>
			<input type="checkBox" value="1" id="allMyCal" class="fk_bcno" name="bcno" checked/>&nbsp;&nbsp;<span>내 캘린더</span>
			<button class="btn_edit" style="float: right;" data-target="addMyCal" onclick="addMyCalendar()"><i class='fas'>&#xf055;</i></button>
			<div id="myCal" style="margin-left: 50px; margin-bottom: 10px;"></div>
			<input type="checkBox" value="3" class="fk_bcno" name="bcno" checked/>&nbsp;&nbsp;공유받은 캘린더
		</div>
		
		
		<div id="wrapper2">
			
			<div id="searchPart" style="float: right;">
				<form name="searchSchedule">
					<div>
					<input type="text" id="fromDate" name="startdate" style="width: 90px;" readonly="readonly">&nbsp;&nbsp; 
	            -&nbsp;&nbsp; <input type="text" id="toDate" name="enddate" style="width: 90px;" readonly="readonly">&nbsp;&nbsp;
					<select id="searchType" name="searchType" style="height: 30px;">
						<option value="">선택하세요</option>
						<option value="subject">제목</option>
						<option value="content">내용</option>
						<option value="joinemployee">공유자</option>
					</select>&nbsp;&nbsp;	
					<input type="text" id="searchWord" value="" style="height: 30px; width:120px; " name="searchWord"/>
					<button type="button" class="btn_normal" style="display: inline-block;"  onclick="goSearch()">검색</button>
					</div>
				</form>
			</div>
			
		
			<div id="calendar" style="margin-top: 100px;" ></div>
			
		</div>

	</div>
</div>
<!-- 내 캘린더 추가 Modal -->
  <div class="modal fade" id="addMyCal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">내 캘린더 추가</h4>
        </div>
        <div class="modal-body">
         	<table style="width: 100%;" class="table table-bordered">
         			<tr>
         				<td style="text-align: left; ">종류</td>
         				<td><input type="text" class="addscname" /></td>
         			</tr>
         			<tr>
         				<td style="text-align: left;">생성자</td>
         				<td id="strtime" style="text-align: left; padding-left: 5px;">${loginuser.name}</td>
         			</tr>
         		</table>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="button" id="addMy" class="btn" onclick="goAddMyCal()" >추가</button>
            <button type="button" class="btn" data-dismiss="modal">취소</button>
        </div>
        
      </div>
    </div>
  </div>


<!-- 사내 캘린더 추가 Modal -->
  <div class="modal fade" id="addComCal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">사내 캘린더 추가</h4>
        </div>
        <div class="modal-body">
         	<table style="width: 100%;" class="table table-bordered">
         			<tr>
         				<td style="text-align: left; ">종류</td>
         				<td><input type="text" class="addcomscname"/></td>
         			</tr>
         			<tr>
         				<td style="text-align: left;">생성자</td>
         				<td id="strtime" style="text-align: left; padding-left: 5px;">${loginuser.name}</td>
         			</tr>
         		</table>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="button" id="addCom" class="btn" onclick="goAddComCal()" >추가</button>
            <button type="button" class="btn" data-dismiss="modal">취소</button>
        </div>
        
      </div>
    </div>
  </div>
  

<!-- 내 캘린더 수정 Modal -->
  <div class="modal fade" id="editMyCal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">내 캘린더 수정</h4>
        </div>
        <div class="modal-body">
         	<table style="width: 100%;" class="table table-bordered">
         			<tr>
         				<td style="text-align: left; ">종류</td>
         				<td><input type="text" class="editmyscname"/><input type="hidden" value="" class="editmyscno"></td>
         			</tr>
         			<tr>
         				<td style="text-align: left;">생성자</td>
         				<td id="strtime" style="text-align: left; padding-left: 5px;">${loginuser.name}</td>
         			</tr>
         		</table>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="button" id="addCom" class="btn" onclick="goEditMyCal()" >수정</button>
            <button type="button" class="btn" data-dismiss="modal">취소</button>
        </div>
        
      </div>
    </div>
  </div>

<!-- 사내 캘린더 수정 Modal -->
  <div class="modal fade" id="editComCal" role="dialog">
    <div class="modal-dialog modal-sm">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">사내 캘린더 수정</h4>
        </div>
        <div class="modal-body">
         	<table style="width: 100%;" class="table table-bordered">
         			<tr>
         				<td style="text-align: left; ">종류</td>
         				<td><input type="text" class="editcomscname"/><input type="hidden" value="" class="editcomscno"></td>
         			</tr>
         			<tr>
         				<td style="text-align: left;">생성자</td>
         				<td id="strtime" style="text-align: left; padding-left: 5px;">${loginuser.name}</td>
         			</tr>
         		</table>
        </div>
        
        <!-- Modal footer -->
        <div class="modal-footer">
        	<button type="button" id="addCom" class="btn" onclick="goEditComCal()" >수정</button>
            <button type="button" class="btn" data-dismiss="modal">취소</button>
        </div>
        
      </div>
    </div>
  </div>
     
<form name="dateFrm">
	<input type="hidden" name="chooseDate" value=""/>	
</form>