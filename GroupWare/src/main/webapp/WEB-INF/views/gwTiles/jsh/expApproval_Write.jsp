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
		
		//일반결재내역 문서 카테고리 라디오 클릭 이벤트
		$("input[name=scatname]").click(function(){
			
			$("input#atitle").val("");
			$("input#aconent").val("");
			
			$("td#smarteditor").empty();
			$("td#smarteditor").html('<textarea rows="30" cols="160" name="acontent" id="acontent"></textarea>');
			
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
			 var scatname;
			 var html1;
			 var html2;
			 var html3;
			 
			$("input[name=scatname]:checked").each(function(index,item){			
				scatname = $(this).val();
		     
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
			
				
				
				if(scatname =="지출결의서"){
					$("tr.changeScat").show();
					html1+="<th>지출일자</th>"+
					       "<td colspan='3'>&nbsp;<input type='date' name='exdate' id='exdate'/></td>";
					       
					      
				    html2+="<th>지출금액</th>"+
					       "<td colspan='3'><input type='number'  name ='exprice' id='exprice'/>원</td>";
					       
					  
						  
				}
				else if(scatname =="법인카드사용신청서"){
					$("tr.changeNcat").show();
					html1+="<th>사용예정일</th>"+
						  "<td colspan='3'>&nbsp;<input type='date' name='codate' id='codate'/></td>";
						  
						  
						  
					html2+= "<th>카드번호</th>"+
						    "<td colspan='3'><input type='hidden' name='cocardnum' value=' [경영] 111-222-333'/>[경영]111-222-333</td>";
						  
						  
				    html3+= 
						  "<th>예상금액</th>"+
						  "<td><input type='number'  name ='coprice' id='coprice'/>원</td>"+
						  "<th>지출목적</th>"+
						  "<td><select class='myselect' name='copurpose' id='exprice'> "+
			           		"	<option value='1'>교통비</option> "+
			           		"	<option value='2'>사무비품</option> "+
			           		"	<option value='3'>주유비</option> "+
			           		"	<option value='4'>출장비</option> "+
			           		"	<option value='5'>식비</option> "+
			           		"	<option value='6' selected>기타</option> "+
		           		"</select></td>";
				}
				
				
				if(scatname =="지출결의서"){
					$("tr#changeScat3").hide();
					$("tr#changeScat1").html(html1);
					$("tr#changeScat2").html(html2);
				}
				else{
					$("tr#changeScat3").show();
					$("tr#changeScat1").html(html1);
					$("tr#changeScat2").html(html2);
					$("tr#changeScat3").html(html3);
				}
				
				
				$("div#today").html(today);
				$("span#scat").html(scatname);
				$("h3#scat").html(scatname);
				$("div#containerview").show();
				
			}); //$("input[name=ncat]").click(function(){})------------------------------------
			
			
		 // 문서 제출하기
		$("button#insertWrite").click(function(){
		 
		    $("input[name=scatname]:checked").each(function(index,item){			
			  scatname = $(this).val();
			
			});
		 
		    //글제목 유효성검사
		    var atitleVal = $("input#atitle").val().trim();
            if(atitleVal == "") {
	            alert("글제목을 입력하세요!!");
	            return false;
	         }
		 
		    // 지출결의서 유효성 검사
		    if(scatname =="지출결의서"){     
		         var exdate = $("input#exdate").val(); //지출일자
		         var exprice = $("input#exprice").val().trim();// 지출금액
		         if(exdate == "" ) {
		            alert("지출일자를 입력하세요!!");
		            return false;
		         }
		         else if(exprice == ""){
		        	 alert("지출금액을 입력하세요!!");
			         return false;
		         }
		     
		    }
		    else if(scatname =="법인카드사용신청서"){
		        // 법인카드사용신청서 유효성 검사
		         var codate = $("input#codate").val(); //사용예정일
		         var coprice = $("select#coprice").val(); //예상금액
		         if(codate == "" ) {
		            alert("사용예정일을 입력하세요!!");
		            return false;
		         }
		         else if(coprice == ""){
		        	 alert("예상금액을 입력하세요!!");
			         return false;
		         }
		    }    
		         
		    var bool = confirm(scatname+"을 제출하시겠습니까? ");
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
		         frm.action = "<%= ctxPath%>/t1/expApproval_WriteEnd.tw";
		         frm.submit();   
				}	
				  else{
					  alert(scatname+" 제출을 취소하셨습니다.");
					  location.href="<%=ctxPath %>/t1/expApproval_Write.tw";
				  }
		  });	// 문서제출 -----------------------------
			
		
		//문서 임시저장하기
		 $("button#saveWrite").click(function(){
			  
			  var scatname ;
				$("input[name=scatname]:checked").each(function(index,item){			
				      scatname = $(this).val();			
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
				
			  var bool = confirm(scatname+"를 임시저장함에 저장하시겠습니까? ");
			  if(bool){
			  
				 // 폼(form) 을 전송(submit)
		         var frm = document.writeGFrm;
		         frm.method = "POST";
		         frm.action = "<%= ctxPath%>/t1/expApproval_saveWrite.tw";
		         frm.submit(); 
			  }
			  else{
				  alert(scatname+" 저장을 취소하셨습니다.");
				  location.href="<%=ctxPath %>/t1/expApproval_Write.tw";
			  }
		          
		});	//임시보관함 ------------------------------------------
			
     }); //end of $(document).ready(function(){})----------------------
	
		
</script>

<form name="writeGFrm"  enctype="multipart/form-data">

<div id="containerall">
	<div id=radio>
 		<span style="border:solid 2px gray; width:400px; height:100px; font-size:20px; padding:10px 10px;">전자결재문서작성</span>
				<span style="margin-left:20px;">&nbsp;&nbsp;
					<label for="expense"><input type="radio" name="scatname" id="expense" value="지출결의서"> 지출결의서</label>&nbsp;&nbsp;
					<label for="cocard"><input  type="radio" name="scatname" id="cocard" value="법인카드사용신청서"> 법인카드사용신청서</label>&nbsp;&nbsp;
					<label for="ex1"><input type="radio" name="scatname" id="ex1" value="출장명세서"> 출장명세서(EX)</label>&nbsp;&nbsp;
					<label for="ex2"><input type="radio" name="scatname" id="ex2" value="퇴직금정산신청서"> 퇴직금정산신청서(EX)</label>&nbsp;&nbsp;
				</span>
 	</div>
	<div id="containerview">
 	
	<hr class="hr">
		<h3 id="scat" align="center"></h3>
	<hr class="hr">
	<br>
	<div class="section" >
		<table id="table1">
		
			<tr>
				<th style="width:100px; height:70px; ">대리</th>
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
				<th style="width:200px;">수신참조</th>
				<td colspan="3"><input type="hidden" name="arecipient1" value="${requestScope.write_view.managerid}"/>${requestScope.write_mview.name} ${requestScope.write_mview.pname} (${requestScope.write_view.dname})</td>
			</tr>
			
			<tr>
				<th>제목</th>
				<td colspan="3"><input type="text" width="80%"name="atitle" id="atitle"></td>
			</tr>
			<tr>
				<th>작성자</th>
				<td colspan='3'><input type="hidden" name="fk_employeeid" value="${sessionScope.loginuser.employeeid}" />${sessionScope.loginuser.name}</td>
			</tr>
			<tr class="changeScat" id="changeScat1">
			
			</tr>
			<tr class="changeScat" id="changeScat2">
			
			</tr>
			<tr class="changeScat" id="changeScat3">
			
			</tr>
			
			
			<tr>
				<th>문서상태</th>
				<td colspan='3'><input type="hidden" name="astatus" value="0"/>작성중..</td>
			</tr>
			
			
			<tr>
				<th style="height:350px;">지출내용</th>
				<td colspan='3' id="smarteditor"> 
				</td>
			</tr>
			
			<tr>
				<th>첨부파일</th>
				<td colspan='3'>
					<input type="file" name="attach"/>
				</td>
			</tr>
		</table>
		
		<div align="center">상기와 같은 내용으로 <span id="scat" style="font-weight: bold;"></span>을(를) 제출하오니 재가바랍니다.</div>
		<div align="right" id="today" style="margin: 4px 0; margin-right: 15%;">기안일:</div>
		<div align="right" style="margin-right: 15%;">신청자: <span style="font-weight:bold;">${sessionScope.loginuser.name} </span> ${requestScope.write_view.pname} (${requestScope.write_view.dname})</div>
		
		
		
		
		<div style="margin-top: 20px;">
			<span style="margin-left: 15%">
			    <button type="button"  class="btn btn1" onclick="javascript:location.href='<%=ctxPath %>/t1/expApproval_Write.tw'" >취소</button>
				
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
		
		