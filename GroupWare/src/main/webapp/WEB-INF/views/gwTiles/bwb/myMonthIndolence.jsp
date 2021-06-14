<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	String ctxPath = request.getContextPath();
%>

<style>

</style>
<link href='<%=ctxPath %>/resources/fullcalendar/main.min.css' rel='stylesheet' />

<!-- full calendar에 관련된 script -->
<script src='<%=ctxPath %>/resources/fullcalendar/main.min.js'></script>
<script src='<%=ctxPath %>/resources/fullcalendar/ko.js'></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
<script>
	
	$(document).ready(function(){
	    // 풀캘린더와 관련된 소스코드(화면이 로드되면 전체 화면 보이게 해줌)
	    var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
	        initialView: 'dayGridMonth',
	        locale: 'ko',
	        selectable: true,
		    editable: false,
		    headerToolbar: {
		    	  left: 'prev',
		          center: 'title',
		          right: 'next'
		    },
			// db와 연동하는 법
		    events:function(info, successCallback, failureCallback){
				
		    	var fk_employeeid ="${loginuser.employeeid}"; 
		    	
		    	$.ajax({
	                 url: '<%= ctxPath%>/t1/selectmyMonthIndolence.tw',
	                 data:{"fk_employeeid":fk_employeeid},
	                 dataType: "json",
	                 success:function(json) {
	                	 var events = [];
	                     if(json!=null){
	                         
	                             $.each(json, function(index, item) {

	                                    var enddate =moment(item.outtime).format('YYYY-MM-DD HH:mm:ss');     
	                                    var subject = item.name;
	                         			
	                                    if(item.intime != undefined){// 조건을 안걸어줄경우, 자동으로 sysdate가 들어옴.
	                                    	
	                                    	  var startdate=moment(item.intime).format('YYYY-MM-DD HH:mm:ss'); // 다윤씨가 말하는 시작일 
                                    		  events.push({
		                                               title: item.name+" 출근",
		                                               start: startdate,
		                                               color:"blue"
		                                      }); // events.push

	                                    }
	                                    
	                                    
	                                    if(item.outtime != undefined){// 조건을 안걸어줄경우, 자동으로 sysdate가 들어옴.
	                                    	var enddate =moment(item.outtime).format('YYYY-MM-DD HH:mm:ss');
	                                    
 		                                    events.push({
 	                                           title: item.name+" 퇴근",
 	                                           start: enddate,
 	                                           color:"red"
 	                               		    }); // events.push
	 	                                 
	                                    }
	                                    	   		                                    
	                             });  //.each()
	                             
	                         }                             
                      
	                         successCallback(events);                               
	                  }//success: function end                          
	          }); //ajax end	                                
	                        
	        }
	      	  
	  });
      calendar.render();
      calendar.setOption('height', 600);
	}); // end of 
	
	
</script>


<div id="indolenceMenu" style="margin-left:610px; margin-top:30px; font-size:20pt; font-weight:bolder;">
	<span>${loginuser.name}님의 월별 근태관리</span>
</div>



<div id="calendar" style="width:70%; margin-left:100px; margin-top:50px;">
</div>
  



