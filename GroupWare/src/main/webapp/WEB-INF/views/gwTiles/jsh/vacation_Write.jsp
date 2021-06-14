<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<style>
div#radio{
margin: 50px 50px;
font-size: 16px;
}

h3{
	font-weight:bold;
}
div#containerview{
	margin: 30px 0px 30px 50px;
	width: 80%;
}
div.section table, div.section th, div.section td{
	border: solid 1px #ccc;
	border-collapse: collapse;
}

#table1 {
	float: right; 
	width: 300px; 
	border-collapse: collapse;
	margin-right: 200px;
	margin-bottom: 50px;
}

#table1 th, #table1 td{
	text-align: center;
}
#table1 th {
	background-color: #395673; 
	color: #ffffff;
}
#table2 th, #table3 th, #table4 th, #table5 th {
	width: 150px;
}

th{
	background-color: #ccd9e6;
	padding: 7px;
}
td{
	padding: 7px;
}

#table2 {
	width: 70%;
	margin: 50px auto;
}
#table3 {
	width: 70%;
	margin: 10px auto;
}
#table4 {
	width: 70%;
	margin: 10px auto;
}
#table5 {
	width: 70%;
	margin: 10px auto;
}
input.btn {
	width: 70px;
	border-radius: 0;
	font-weight: bold;
}

td.opinion{
	border: solid 1px white;
}
textarea{
width:99%;
height:350px;
}
button.btn1:hover{
 background-color: #c3c6c9;
}
</style>

<script type="text/javascript">
		$(document).ready(function(){
			//사이드바 세부메뉴 나타내기 
			$("div#submenu3").show();
			
			$("div#containerview").hide();
			var obj = []; //전역변수 
					
			
			
			//근태결재내역 문서 카테고리 라디오 클릭 이벤트
			$("input[name=vcatname]").click(function(){
				
				$("td#smarteditor").empty();
				$("td#smarteditor").html('<textarea name="acontent" id="acontent"></textarea>');
				
					   	
				//오늘날짜 알아오기
			   	var date = new Date();
			   	var year = date.getFullYear();
			   	var month = date.getMonth()+1;
			   	var date = date.getDate();
			   	if(month <10){
			   		month = '0'+month;
			   	}
			   	else{
			   		month=month;
			   	}
			   	if(date <10){
			   		date = '0'+day;
			   	}
			   	else{
			   		date=date;
			   	}
			   	
			   	
			   	 var today = (year+"년 "+month+"월 "+date+"일");
			     //	console.log(today);
				 var vcatname;
				 var html;
				 var html1;
				 
				$("input[name=vcatname]:checked").each(function(index,item){			
			      vcatname = $(this).val();
			     
				 //alert(ncat);
			      
				});
				
				
				<%-- ===  스마트 에디터 구현 시작 ===  --%>
		       //스마트에디터 프레임생성
		       nhn.husky.EZCreator.createInIFrame({
		           oAppRef: obj,
		           elPlaceHolder: "acontent",
		           sSkinURI: "<%= request.getContextPath() %>/resources/smarteditor/SmartEditor2Skin.html",
		           htParams : {
		               // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
		               bUseToolbar : true,            
		               // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
		               bUseVerticalResizer : true,    
		               // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
		               bUseModeChanger : true,
		           }
		       });
			     <%-- === 스마트 에디터 구현 끝 === --%>
				
				
				if(vcatname =="병가"){
					$("tr#changeVcat").show();
					html+="<th>요청기간</th>"+
					       "<td colspan='3'><input type='date' name='slstart' id='slstart'/>"+"&nbsp;~&nbsp;"+"<input type='date' name='slend' id='slend'/></td>";
					  
						  
				}
				else if(vcatname =="반차"){
					$("tr#changeVcat").show();
					html+="<th>요청기간</th>"+
						  "<td colspan='3'><input type='date' name='afdate' id='afdate'/>&nbsp;&nbsp"+
						   "<select name='afdan' id='afdan'>"+
						   "<option value='1'>오전</option>"+
						   "<option value='2'>오후</option>"+
						   "</select>"+
						   "&nbsp&nbsp<span style='font-weight:bold; color:red;'>[남은일수:"+${requestScope.leftOffCnt}+"]</span></td>";
						
				}
				else if(vcatname =="연차"){
					$("tr#changeVcat").show();
					html+="<th>요청기간</th>"+
					  "<td colspan='3'><input type='date' name='daystart' id='daystart'/>"+"&nbsp;~&nbsp;"+"<input type='date' name='dayend' id='dayend'/>"+
					  "&nbsp&nbsp<span style='font-weight:bold; color:red;'>[남은일수:"+${requestScope.leftOffCnt}+"]</span></td>";
					
						
				}
				else if(vcatname =="경조휴가"){
					$("tr#changeVcat").show();
					html+="<th>요청기간</th>"+
					      "<td colspan='3'><input type='date' name='congstart' id='congstart'/>"+"&nbsp;~&nbsp;"+"<input type='date' name='congend' id='congend'/></td>";
					
						
				}
				else if(vcatname =="출장"){
					$("tr#changeVcat").show();
					html+="<th>출장기간</th>"+
					       "<td colspan='3'><input type='date' name='bustart' id='bustart'/>"+"&nbsp;~&nbsp;"+"<input type='date' name='buend' id='buend'/></td>";
					
					html1+="<th>출장지</th>" +
							"<td ><input type='text' name='buplace' id='buplace' /></td>"+
							"<th>출장인원</th>"+
							"<td><input type='number' name='bupeople' id='bupeople' />명</td>";
						
						
				}
				else if(vcatname =="추가근무"){
					$("tr#changeVcat").show();
					html+="<th>요청시간</th>"+
					
						  "<td td colspan='3'>"+
						  "<input type='date' name='ewdate' id= 'ewdate'/>&nbsp;<input type='number' min='1' max='10' name ='ewhours' id='ewhours'/>시간</td>"
					
						
				}
				
				
				//출장일 때에만  <tr id='html1'> 행을 보여준다.
				if(vcatname =="출장"){
					$("tr#html1").show();
					$("tr#html1").html(html1);
					$("tr#changeVcat").html(html);
					
				}
				else{
					$("tr#html1").hide();
					$("tr#changeVcat").html(html);
				}
				
				$("div#today").html(today);
				$("span#vcat").html(vcatname);
				$("h3#vcat").html(vcatname);
				$("div#containerview").show();
				
			}); //$("input[name=ncat]").click(function(){})------------------------------------
			
		
			
    // 문서 제출하기
    $("button#insertWrite").click(function(){
	  
	    var leftOffCnt = Number(${requestScope.leftOffCnt}); //남은 휴가수 
	   	
	   	var afdate = $("input#afdate").val(); // 반차
	   	var daystart  = $("input#daystart").val(); // 연차시작
	   	var dayend  = $("input#dayend").val(); // 연차끝
	   	var i =1;
	   	var afdates = 0; //반차사용일자변수
	   	var daydates = 0;  //연차사용일자변수

			
		    var vcatname ;
			$("input[name=vcatname]:checked").each(function(index,item){			
			      vcatname = $(this).val();
			     
				 //alert(ncat);
				
			});
			
			
			// 글제목 유효성 검사
	         var atitleVal = $("input#atitle").val().trim();
	         if(atitleVal == "") {
	            alert("글제목을 입력하세요!!");
	            return false;
	         }
	         
	       
	         
		    // 병가 요청기간 유효성 검사
		    if(vcatname =="병가"){     
		         var slstart = $("input#slstart").val().trim();
		         var slend = $("input#slend").val().trim();
		         if(slstart == "" || slend == "" ) {
		            alert("요청기간을 입력하세요!!");
		            return false;
		         }
		         else if(slstart > slend){
		        	 alert("요청기간을 올바르게 입력하세요!!");
			         return false;
		         }
	         
		    }
		    else if(vcatname =="반차"){
		         // 반차 유효성 검사
		         var afdate = $("input#afdate").val();
		         var afdan = $("select#afdan").val();
		         if(afdate == "" || afdan == "" ) {
		            alert("요청기간을 입력하세요!!");
		            return;
		         }
		         else{
		        	 
		        	 	if(afdate){
		        	   		afdates = 0.5;
		        	   		leftOffCnt = leftOffCnt - afdates ;
		        	   	}
		        	   	
		        	   	if(leftOffCnt<afdates){
		        	   		alert("남은 휴가수를 초과하였습니다. 남은 일수 확인 후 다시 신청하세요.");
		        	   		return false;
		        	   	}
		        	   	else{
		        	   		alert("남은 휴가일수는  "+ leftOffCnt+"일 입니다.");
		        	   		
		        	   	}
		        	 
		         }
	         
		    }
		    else if(vcatname =="연차"){
		         // 연차 유효성 검사
		         var daystart = $("input#daystart").val();
		         var dayend = $("input#dayend").val();
		         if(daystart == "" || dayend == "" ) {
		            alert("요청기간을 입력하세요!!");
		            return false;
                   }
		         else if(daystart > dayend){
		        	 alert("요청기간을 올바르게 입력하세요!!");
			         return false;
		         }
		         else{
		        	 
		        	 daydates = (dayend.substr(8,10)) - (daystart.substr(8,10)) + 1;
		        	
		        	 leftOffCnt = leftOffCnt - daydates;
		        	 
	        		if(leftOffCnt<daydates){
	        	   		alert("남은 휴가수를 초과하였습니다. 남은 일수 확인 후 다시 신청하세요.");
	        	   		return false;
	        	   	}
	        	   	else{
	        	   		alert("남은 휴가일수는  "+leftOffCnt+"일 입니다.");
	        	   	}
		        	 
		        	 
		        	 
		         }
		         
		         
		    }
		    else if(vcatname =="경조휴가"){
		         // 경조휴가 유효성 검사
		         var congstart = $("input#congstart").val().trim();
		         var congend = $("input#congend").val().trim();
		            if(congstart == ""|| congend == "" ) {
		            alert("요청기간을 입력하세요!!");
		            return false;
		         
		        }
	            else if( congstart > congend){
		        	 alert("요청기간을 올바르게 입력하세요!!");
			         return false;
		         }
	            
		    }
		    else if(vcatname =="출장"){
		         // 출장 유효성 검사
		         var bustart = $("input#bustart").val().trim();
		         var buend = $("input#buend").val().trim();
		         var buplace = $("input#buplace").val().trim();
		        
		         if(bustart == "" || buend == ""  ) {
		            alert("요청기간을 입력하세요!!");
		            return false;
		         }
		         else if( bustart > buend){
		        	 alert("요청기간을 올바르게 입력하세요!!");
			         return false;
		         }
		         else if( buplace == ""){
		        	 alert("출장지를 입력하세요!!");
			          return false;
		         }
		    }
		    else if(vcatname =="추가근무"){
		         // 추가근무 유효성 검사
		         var ewdate = $("input#ewdate").val().trim();
		         var ewhours = $("input#ewhours").val().trim();
		         if(ewdate == "" || ewhours == ""  ) {
		            alert("요청시간을 입력하세요!!");
		            return;
		         }
		    }     
		         
		         
		  var bool = confirm(vcatname+"을 제출하시겠습니까? ");
		  if(bool){
		 
	 
		 
		   <%-- === 스마트 에디터 구현 시작 === --%>
 		   
           //id가 content인 textarea에 에디터에서 대입
	         obj.getById["acontent"].exec("UPDATE_CONTENTS_FIELD", []);
	         
	       <%-- === 스마트 에디터 구현 끝 === --%>
	         
	          
	
	         
	       <%-- === 스마트에디터 구현 시작 === --%>
	        // 스마트에디터 사용시 무의미하게 생기는 p태그 제거
		       var contentval = $("textarea#acontent").val();
	              
		       // === 확인용 ===
	           // alert(contentval); // content에 내용을 아무것도 입력치 않고 쓰기할 경우 알아보는것. 
	           // "<p>&nbsp;</p>" 이라고 나온다.
	           
	           // 스마트에디터 사용시 무의미하게 생기는 p태그 제거하기전에 먼저 유효성 검사를 하도록 한다.
	           // 글내용 유효성 검사 
	           if(contentval == "" || contentval == "<p>&nbsp;</p>") {
	              alert("글내용을 입력하세요!!");
	              return;
	           }
	           
	           // 스마트에디터 사용시 무의미하게 생기는 p태그 제거하기
	           contentval = $("textarea#acontent").val().replace(/<p><br><\/p>/gi, "<br>"); //<p><br></p> -> <br>로 변환
		       /*    
		                   대상문자열.replace(/찾을 문자열/gi, "변경할 문자열");
		           ==> 여기서 꼭 알아야 될 점은 나누기(/)표시안에 넣는 찾을 문자열의 따옴표는 없어야 한다는 점입니다. 
		                        그리고 뒤의 gi는 다음을 의미합니다.
		
		              g : 전체 모든 문자열을 변경 global
		              i : 영문 대소문자를 무시, 모두 일치하는 패턴 검색 ignore
		       */    
	           contentval = contentval.replace(/<\/p><p>/gi, "<br>"); //</p><p> -> <br>로 변환  
	           contentval = contentval.replace(/(<\/p><br>|<p><br>)/gi, "<br><br>"); //</p><br>, <p><br> -> <br><br>로 변환
	           contentval = contentval.replace(/(<p>|<\/p>)/gi, ""); //<p> 또는 </p> 모두 제거시
	       
	           $("textarea#acontent").val(contentval);
	           
	         <%-- === 스마트에디터 구현 끝 === --%>
	 
	 
	         // 폼(form) 을 전송(submit)
	         var frm = document.writeGFrm;
	         frm.method = "POST";
	         frm.action = "<%= ctxPath%>/t1/vacation_WriteEnd.tw";
	         frm.submit();   
	         
		  }
		  else{
			  alert(vcatname+" 제출을 취소하셨습니다.");
			  location.href="<%=ctxPath %>/t1/vacation_Write.tw";
		  }
	  });	
		
			
			
			
			
			
			
	  //문서 임시저장하기
	  $("button#saveWrite").click(function(){
		
		  var vcatname ;
			$("input[name=vcatname]:checked").each(function(index,item){			
			      vcatname = $(this).val();
			     
				 //alert(ncat);
				
			});
	  
		 
			  <%-- === 스마트 에디터 구현 시작 === --%>
	        //id가 content인 textarea에 에디터에서 대입
		         obj.getById["acontent"].exec("UPDATE_CONTENTS_FIELD", []); 
		      <%-- === 스마트 에디터 구현 끝 === --%>
		         
	  
		         
			       <%-- === 스마트에디터 구현 시작 === --%>
      	       // 스마트에디터 사용시 무의미하게 생기는 p태그 제거
			       var contentval = $("textarea#acontent").val();
		              
			       // === 확인용 ===
		           // alert(contentval); // content에 내용을 아무것도 입력치 않고 쓰기할 경우 알아보는것.
		           // "<p>&nbsp;</p>" 이라고 나온다.
		           
		           // 스마트에디터 사용시 무의미하게 생기는 p태그 제거하기전에 먼저 유효성 검사를 하도록 한다.
		           // 글내용 유효성 검사 
		          
		           // 스마트에디터 사용시 무의미하게 생기는 p태그 제거하기
		           contentval = $("textarea#acontent").val().replace(/<p><br><\/p>/gi, "<br>"); //<p><br></p> -> <br>로 변환
			       /*    
			                   대상문자열.replace(/찾을 문자열/gi, "변경할 문자열");
			           ==> 여기서 꼭 알아야 될 점은 나누기(/)표시안에 넣는 찾을 문자열의 따옴표는 없어야 한다는 점입니다. 
			                        그리고 뒤의 gi는 다음을 의미합니다.
			
			              g : 전체 모든 문자열을 변경 global
			              i : 영문 대소문자를 무시, 모두 일치하는 패턴 검색 ignore
			       */    
		           contentval = contentval.replace(/<\/p><p>/gi, "<br>"); //</p><p> -> <br>로 변환  
		           contentval = contentval.replace(/(<\/p><br>|<p><br>)/gi, "<br><br>"); //</p><br>, <p><br> -> <br><br>로 변환
		           contentval = contentval.replace(/(<p>|<\/p>)/gi, ""); //<p> 또는 </p> 모두 제거시
		       
		           $("textarea#acontent").val(contentval);
		           
		         <%-- === 스마트에디터 구현 끝 === --%>
	         
	         var bool = confirm(vcatname+"을 임시저장함에 저장하시겠습니까? ");
			  if(bool){
				  
			  
				 // 폼(form) 을 전송(submit)
		         var frm = document.writeGFrm;
		         frm.method = "POST";
		         frm.action = "<%= ctxPath%>/t1/vacation_saveWrite.tw";
		         frm.submit(); 
			  }
			  else{
				  alert(vcatname+" 저장을 취소하셨습니다.");
				  location.href="<%=ctxPath %>/t1/vacation_Write.tw";
				  
			  }
	         
	          
		  });	
			
		
		}); //end of $(document).ready(function(){
		
	 
		
</script>

<form name="writeGFrm"  enctype="multipart/form-data">

<div id="containerall">
	<div id=radio>
 		<span style="border:solid 2px gray; width:400px; height:100px; font-size:20px; padding:10px 10px;">근태/휴가결재문서작성</span>
		<span style="margin-left:20px;">&nbsp;&nbsp;
			<label for="sickleave"><input type="radio" name="vcatname" id="sickleave" value="병가"> 병가</label>&nbsp;&nbsp;
			<label for="afternoonoff"><input  type="radio" name="vcatname" id="afternoonoff" value="반차"> 반차</label>&nbsp;&nbsp;
			<label for="dayoff"><input type="radio" name="vcatname" id="dayoff" value="연차"> 연차</label>&nbsp;&nbsp;
			<label for="congoff"><input type="radio" name="vcatname" id="congoff" value="경조휴가"> 경조휴가</label>&nbsp;&nbsp;
			<label for="businesstrip"><input type="radio" name="vcatname" id="businesstrip" value="출장"> 출장</label>&nbsp;&nbsp;
			<label for="extrawork"><input  type="radio" name="vcatname" id="extrawork" value="추가근무"> 추가근무</label>&nbsp;&nbsp;
		</span>
 	</div>
	<div id="containerview">
 	
	<hr class="hr">
		<h3 id="vcat" align="center" ></h3>
	<hr class="hr">
	<br>
	
	<div class="section">
		<table id="table1">		
			<tr>
				<th style="width:100px; height:40px;">대리</th>
				<th>부장</th>
				<th>사장</th>
			</tr>			
			<tr>
				<td style="height:70px;"></td>
				<td></td>
				<td></td>
			</tr>
		</table>
		
		<table id="table2">
			<tr>
				<th>작성자</th>
				<td colspan="3"><input type="hidden" name="fk_employeeid" value="${sessionScope.loginuser.employeeid}" />${sessionScope.loginuser.name}</td>
				
			</tr>
			<tr>
				<th style="width:200px;">수신참조</th>
				<td colspan="3"><input type="hidden" name="arecipient1" value="${requestScope.write_view.managerid}"/>${requestScope.write_mview.name} ${requestScope.write_mview.pname} (${requestScope.write_view.dname})</td>
			</tr>
			<tr>
				<th>문서상태</th>
				<td style="width:" colspan="3"><input type="hidden" name="astatus" value="0"/>작성중...	</td>
				
			</tr>
			<tr>
				<th>제목</th>
				<td colspan="3"><input type="text" name="atitle" id="atitle"></td>
			</tr>
			
			
			<tr id="changeVcat">
				
			</tr>				
			
			<tr id="html1">
				
			</tr>
			
			<tr>
				<th style="height:350px;">글내용</th>
				<td colspan="3" id="smarteditor">
				
				</td>
			</tr>
				
			<tr>
				<th>첨부파일</th>
				<td colspan="3">
					<input type="file" name="attach" id="attach"/>
				</td>
			</tr>
		</table>
 			
		<div align="center">상기와 같은 내용으로 <span id="vcat" style="font-weight: bold;"></span>계 을(를) 제출하오니 재가바랍니다.</div>
		<div align="right" id="today" style="margin: 4px 0; margin-right: 15%;">기안일:</div>
		<div align="right" style="margin-right: 15%;">신청자: <span style="font-weight:bold;">${sessionScope.loginuser.name} </span> ${requestScope.write_view.pname} (${requestScope.write_view.dname})</div>
		
		
		
		
		<div style="margin-top: 20px;">
			<span style="margin-left: 15%">
			    <button type="button"  class="btn btn1"  onclick="javascript:location.href='<%=ctxPath %>/t1/vacation_Write.tw'" >취소</button>
				
			</span>
			<span style="margin-left: 55%;">
				<button type="button" class="btn btn-primary" id="insertWrite" >제출하기</button>
    			<button type="button" class="btn btn-danger" id="saveWrite" >저장하기</button>
			</span>
		</div>
		
		<br><br>

     </div>
  </div>
 </div>

</form>
		