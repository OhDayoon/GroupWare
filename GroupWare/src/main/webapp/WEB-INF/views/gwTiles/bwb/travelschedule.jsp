<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	String ctxPath = request.getContextPath();
%>

<style>

	div.fc-daygrid-event-harness{
		margin-bottom:20px !important;
	}
	
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
		    	
		    	$.ajax({
	                 url: '<%= ctxPath%>/t1/selectProductSchedule.tw',
	                 dataType: "json",
	                 success:function(json) {
	                	 var events = [];
	                     if(json!=null){
	                         
	                             $.each(json, function(index, item) {

	                                  var startdate =moment(item.startdate).format('YYYY-MM-DD');
	                                  var enddate   =moment(item.enddate).format('YYYY-MM-DD');
	                                  var subject = item.name;
  	 								  var sort = Number(item.sort);
  	 								  
  	 								  if(sort==1){// 국내여행일때
	                                	  events.push({
	                                               title: subject,
	                                               start: startdate,
	                                               end: enddate,
	                                               color:"#66b3ff"
	                                      }); // events.push
  	 								  }
  	 								  else if(sort==2){ // 유럽여행일때
  	 									events.push({
                                            title: subject,
                                            start: startdate,
                                            end: enddate,
                                            color:"#8c8cd9"
                                   		}); // events.push
  	 								  }
  	 								  else{ // 미국여행일때
  	 									events.push({
                                            title: subject,
                                            start: startdate,
                                            end: enddate,
                                            color:"#94b8b8"
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


<div id="indolenceMenu" style="margin-left:800px; margin-top:30px; font-size:20pt; font-weight:bolder;">
	<span>T1여행사의 상품일정</span>
</div>



<div id="calendar" style="width:80%; margin-left:200px; margin-top:50px;">
</div>