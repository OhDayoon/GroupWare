<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String ctxPath = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/resources/css/kdn/mail.css" />
<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	
	 var str_arrCcEmail = "";  // 참조 이메일 배열 string 변환값
     var str_arrReceiverEmail = "";
     var arrAddrsBookCcEmail = ""; // 주소록에서 가져온 참조 이메일
	 var arrAddrsBookEmail = "";     
	
	// 주소록 페이지에서 받는사람 이메일 주소 넘겨받은경우
	var arrReceiverEmail = [];
	var addrsBookEmail = "${requestScope.addrsBookEmail}";
	console.log(addrsBookEmail);
	if(addrsBookEmail != null && addrsBookEmail != ""){
	 // alert ('주소록에서 이메일 가져왓다');
	  $('#receiver').append('<span class="email-ids">'+ addrsBookEmail +' <span class="cancel-email" style="color:gray;"><i class="far fa-window-close"></i></span></span>');
	  arrReceiverEmail.push(addrsBookEmail);
	  // console.log("주소록에서 메일받아온 후 배열:"+addrsBookEmail);
	}
	
	// 회신메일 쓰는 경우
	var replySubject = "${requestScope.subject}";
	// console.log("회신용 메일제목: "+replySubject);
	var preInputRplEmail = "${requestScope.replyEmail}";
	var preInputRplName = "${requestScope.replyToName}";
	if(replySubject != null && replySubject != ""){
		//alert ('회신메일 페이지입니다');
		$('#receiver').append('<span class="email-ids">'+preInputRplName+'&lt;'+preInputRplEmail+'&gt;'+'<span class="cancel-email" style="color:gray;"><i class="far fa-window-close"></i></span></span>');
		//console.log('회신메일 preinput Email : '+preInputRplEmail);
		$("input#subject").val("RE: "+replySubject);
		arrReceiverEmail.push(preInputRplEmail);		
		//console.log("회신이메일 넣은 배열 값: "+arrReceiverEmail);
	}
	
	
	//보낸메일함 저장여부
    $("input[name=saveSentMail]").val("0");
    $("input#saveSentMail").change(function(){
		if($(this).is(":checked") == true) {
			$("input[name=saveSentMail]").val("1");		
		} else {
			$("input[name=saveSentMail]").val("0");
		}
    });
	
	
	// 이메일주소 자동완성을 위한 주소록 목록 가져오기
 	var availableEmail = ${requestScope.emailList};
 	
 	// 받는사람 이메일주소 자동완성
    $("#to-input").autocomplete({
      source: availableEmail
    });
 	
 	// 받는사람 이메일 입력
 	
    var cancelIdx = 0;
    $("#to-input").keydown(function (e) {
   	  if (e.keyCode == 13 || e.keyCode == 32) {
   		 var getValue = $(this).val();
   		 console.log("getValue : "+getValue);
   		 var emailStartIdx = getValue.indexOf("<")+2;
   		 var emailEndIdx = getValue.indexOf(">")-1;
   		 var emailOnly = getValue.substring(emailStartIdx,emailEndIdx);
   		 if(getValue.trim() != null && getValue.trim() != ""){
	   		 $('#receiver').append('<span class="email-ids" name="email-container">'+ getValue +' <span class="cancel-email" name="cancel-btn" style="color:gray;"><input class="mailIndex" type="hidden" value="'+cancelIdx+'"><i class="far fa-window-close"></i></span></span>');
	   		 arrReceiverEmail.push(emailOnly);
	    	 console.log("받는사람 입력창에 입력된 배열값: "+arrReceiverEmail);
   		 }
	   	  cancelIdx=cancelIdx+1; // 취소버튼 인덱스값 주기
   		 $(this).val('');
   	  }
   	  
   	  
   	  
   	  
   	  // 백스페이스 눌렀을때 이메일박스 지우기
   	  if(e.keyCode == 8){
   		  console.log('백스페이스 눌렀다');
   	  }
   	});

    // 참조 이메일주소 자동완성
    $("#cc-input").autocomplete({
      source: availableEmail
    });
    
    var ccCancelIdx = 0;
    // 참조이메일 입력
    var arrCcEmail = [];
    $("#cc-input").keydown(function (e) {
   	  if (e.keyCode == 13 || e.keyCode == 32) {
   		 var getValue = $(this).val();
   		 console.log("getValue : "+getValue);
  		 var ccStartIndex = getValue.indexOf("<")+2;
  		 var ccEndIndex = getValue.indexOf(">")-1;
  		 var emailOnly = getValue.substring(ccStartIndex,ccEndIndex);
  		 if(getValue.trim() != null && getValue.trim() != ""){
	   		 $('#cc').append('<span class="email-ids">'+ getValue +' <span class="cancel-email" name="ccCancel-btn" style="color:gray;"><input class="mailIndex" type="hidden" value="'+ccCancelIdx+'"><i class="far fa-window-close"></i></span></span>');
	   		 arrCcEmail.push(emailOnly);
	    	 console.log(arrCcEmail);
   		 }
  		ccCancelIdx=ccCancelIdx+1; // 취소버튼 인덱스주기
   		 $(this).val('');
   	  }
   	});
    
   	/// Cancel 
   	
	//받는메일주소 취소클릭시
   	$(document).on('click','[name=cancel-btn]',function(){
	      $(this).parent().remove();
	      $("input[name=receiver]").val("");
	      var idx = $(this).find('input').val();
	      console.log("취소버튼의 인덱스값: "+idx);
	      console.log("삭제할 메일: "+arrReceiverEmail[idx]);
	      var thisEmail = arrReceiverEmail[idx];
	      arrReceiverEmail.splice(arrReceiverEmail.indexOf(thisEmail),1,"null");
	      console.log("취소 후 배열값: "+arrReceiverEmail);
	});
    
  //참조메일주소 취소클릭시
   	$(document).on('click','[name=ccCancel-btn]',function(){
	      $(this).parent().remove();
	      $("input#cc-input").val("");
	      var ccIdx = $(this).find('input').val();
	      console.log("cc 취소버튼의 인덱스값: "+ccIdx);
	      console.log("삭제할 참조메일: "+arrCcEmail[ccIdx]);
	      
	      var thisCcEmail = arrCcEmail[ccIdx];
	      arrCcEmail.splice(arrCcEmail.indexOf(thisCcEmail),1,"null");
	      console.log("취소 후 배열값: "+arrCcEmail);
	      
	});
  
  
   	var addrsBookEmail = ""; // 주소록 받는사람 이메일 값
   	var addrsBookCcEmail = "";	// 주소록 참조이메일 값
   	var addrsBookEmailArr = []; // 주소록 받는사람 이메일 배열
   	var addrsBookCcEmailArr = []; // 주소록 참조이메일 배열
  // 주소록에서 추가한 받는메일주소 취소클릭시
  	var thisEmail = "";
	var clicked = false;
  	$(document).on('click','[name=addrs-cancel-btn]',function(){
  		addrsBookEmail = $("input#receiverEmail").val(); // 주소록 받는사람 이메일 값
  		console.log("취소 전 주소록서추가한 이메일 최초 배열: "+addrsBookEmail);
  		$(this).parent().remove();
        $("input[name=receiver]").val("");
	    var addrsIdx = $(this).find('input').val();
	    console.log("취소버튼의 인덱스값: "+addrsIdx);
		
	      addrsBookEmailArr = addrsBookEmail.split(",");
	      console.log("split 후 주소록에서 추가한 이메일 배열: "+addrsBookEmailArr);
	      console.log("삭제할 메일: "+addrsBookEmailArr[addrsIdx]);
	      var thisEmail = addrsBookEmailArr[addrsIdx];
	      addrsBookEmailArr.splice(addrsBookEmailArr.indexOf(thisEmail),1,"null");
	      console.log("취소 후 배열값: "+addrsBookEmailArr);
	      clicked = true; 
  		
	});
  
  
  
  // 주소록에서 추가한 참조메일주소 취소클릭시
  $(document).on('click','[name=addrs-ccCancel-btn]',function(){
	   addrsBookCcEmail = $("input#ccEmail").val();
	  console.log("취소 전 주소록서추가한 참조이메일 최초 배열: "+addrsBookCcEmail);    
	  $(this).parent().remove();
	      $("input#cc-input").val("");
	      var addrsCcIdx = $(this).find('input').val();
	      console.log("cc 취소버튼의 인덱스값: "+addrsCcIdx);
	      addrsBookCcEmailArr = addrsBookCcEmail.split(",");
	      console.log("split 후 주소록에서 추가한 cc이메일 배열: "+addrsBookCcEmailArr);
	      console.log("삭제할 참조메일: "+addrsBookCcEmailArr[addrsCcIdx]);
	      var thisCcEmail = addrsBookCcEmailArr[addrsCcIdx];
	      addrsBookCcEmailArr.splice(addrsBookCcEmailArr.indexOf(thisCcEmail),1,"null");
	      console.log("취소 후 CC배열값: "+addrsBookCcEmailArr);
	      
	});
  
  
  
  
  
    
	
	//내게쓰기
    $("input#write-to-myself").change(function(){
		if($("input#write-to-myself").is(":checked") == true) {
			var myEmail="${loginuser.email}";
			$('#receiver').append('<span class="email-ids write-to-myself">'+myEmail+' <span class="cancel-email" style="color:gray;"><i class="far fa-window-close"></i></span></span>');
			arrReceiverEmail.push(myEmail);
			console.log(arrReceiverEmail);
/* 			if($("input[name=receiver]").value == null ){
				$('#receiver-email').append('<input type="text" name="receiverEmail" value="'+myEmail+'" />');
			} else {
				$("input[name=receiver]").append(myEmail);
			} */
		} else {
			$("span.write-to-myself").remove();
			arrReceiverEmail.splice(arrReceiverEmail.indexOf(myEmail),1);
			console.log(arrReceiverEmail);
			$("input[name=receiverEmail]").val("");
		}
    });
	
	//중요표시 여부
    $("input[name=checkImportant]").val("0");
    $("input#starred").change(function(){
		if($(this).is(":checked") == true) {
			$("input[name=checkImportant]").val("1");		
		} else {
			$("input[name=checkImportant]").val("0");
		}
    });
	
    // 쓰기버튼
    $("button#btnSend").click(function(){
       
       var emailVal = $("input[name=receiverEmail]").val().trim();
       
       var find = $("div#receiver").find('span').val();
       console.log("find: "+find);
       
       if($("div#receiver").find('span').val() == null){ 
          alert("받는사람 이메일주소를 입력하세요");
          return;
       }
       
       addrsBookEmail = $("input#receiverEmail").val();
       addrsBookCcEmail = $("input#ccEmail").val();
       console.log("addrsBookEmailArr : "+addrsBookEmailArr);
       console.log("addrsBookCcEmailArr :"+addrsBookCcEmailArr);
       addrsBookEmailArr = addrsBookEmail.split(",");
       addrsBookCcEmailArr = addrsBookCcEmail.split(",");
       
       // 받은메일,참조메일 최종 배열값 string 변환하기
       if(arrReceiverEmail.indexOf("null") != -1){ // 배열에 null이 있으면 삭제하기
	       arrReceiverEmail.splice(arrReceiverEmail.indexOf('null'),1);
	       console.log("공백제거 후 받은메일 배열: "+arrReceiverEmail);
       } else{
	       str_arrReceiverEmail = arrReceiverEmail.join(',');
	       console.log("받는사람 이메일: "+str_arrReceiverEmail);
       }
       
      
       if(arrCcEmail.indexOf("null") != -1){ // 배열에 null이 있으면 삭제하기
	       arrCcEmail.splice(arrCcEmail.indexOf('null'),1);
	       console.log("공백제거 후 참조 배열: "+arrCcEmail);
       } else{
	       str_arrCcEmail = arrCcEmail.join(',');
	       console.log("참조이메일: "+str_arrCcEmail);
       }
       

       // 주소록에서 가져온 받은메일, 참조메일 최종 string 변환
       var str_addrsBookEmailArr = "";
       var str_addrsBookCcEmailArr = "";
       if(addrsBookEmailArr.indexOf("null") != -1){ // 배열에 null이 있으면 삭제하기
    	   addrsBookEmailArr.splice(addrsBookEmailArr.indexOf('null'),1);
	       console.log("공백제거 후 주소록 받은메일 배열: "+addrsBookEmailArr);
	       console.log("받은메일 배열 데이터타입: "+addrsBookEmailArr);
	       str_addrsBookEmailArr = addrsBookEmailArr.join(',');
       } else{
	       str_addrsBookEmailArr = addrsBookEmailArr.join(',');
	       console.log("주소록 받는사람 이메일: "+str_addrsBookEmailArr);
       }
       
       if(addrsBookCcEmailArr.indexOf("null") != -1){ // 배열에 null이 있으면 삭제하기
    	   addrsBookCcEmailArr.splice(addrsBookCcEmailArr.indexOf('null'),1);
	       console.log("공백제거 후 주소록 참조 배열: "+addrsBookCcEmailArr);
	       str_addrsBookCcEmailArr = addrsBookCcEmailArr.join(',');
       } else{
	       str_addrsBookCcEmailArr = addrsBookCcEmailArr.join(',');
	       console.log("주소록 참조이메일: "+str_addrsBookCcEmailArr);
       }
       
              
		// 일반 메일주소랑 주소록에서 가져온 메일주소 값 합치기
       if(arrReceiverEmail != ""){	// 직접 입력한 메일만 있는 경우
    	   str_arrReceiverEmail = arrReceiverEmail.join(',');
       } else if(arrReceiverEmail == "" && addrsBookEmailArr != ""){ // 직접 입력한 것 없이 주소록 추가 이메일만 있는 경우
    	   str_arrReceiverEmail = str_addrsBookEmailArr;
       } else if(arrReceiverEmail != "" && addrsBookEmailArr != ""){ // 직접 입력한것도 있고 주소록 추가 이메일도 있는 경우
    	   str_addrsBookEmailArr = addrsBookEmail.join(',');
    	   str_arrReceiverEmail = str_arrReceiverEmail+","+str_addrsBookEmailArr;
    	   
       }
       
       console.log("email 최종: "+str_arrReceiverEmail);
    
       if(arrCcEmail != "" ){ // 직접 입력한 참조메일만 있는 경우
			str_arrCcEamil = arrCcEmail.join(',');
       } else if(arrCcEmail == "" && addrsBookCcEmailArr != ""){ // 직접 입력한 것 없이 주소록 추가 참조이메일만 있는 경우
    	   str_arrCcEmail = str_addrsBookCcEmailArr;
       } else if(arrCcEmail != "" && addrsBookCcEmailArr != ""){ // 직접 입력한것도 있고 주소록 추가 참조이메일도 있는 경우
    	   str_addrsBookCcEmailArr = addrsBookCcEmail(',');
    	   str_arrCcEmail = str_arrCcEmail+","+str_addrsBookCcEmailArr;
       }
       
    	   console.log("ccEmail 최종: "+str_arrCcEmail);
       
        // 글제목 유효성 검사
       var subjectVal = $("input#subject").val().trim();
       if(subjectVal == "") {
          alert("제목을 입력하세요");
          return;
       }
       
       // 글내용 유효성 검사(스마트에디터 사용 안 할시)
       
       var contentVal = $("textarea#content").val().trim();
       if(contentVal == "") {
          alert("내용을 입력하세요");
          return;
       }
       
    	// 폼(form) 을 전송(submit)
       var frm = document.newMailFrm;
       frm.receiverEmail.value = str_arrReceiverEmail;
       frm.ccEmail.value = str_arrCcEmail;
       frm.method = "POST";
       frm.action = "<%= ctxPath%>/t1/sendSuccess.tw";
       frm.submit();   
    });
      
 	$("#btnCancel").click(function(){
 		if (confirm("입력하신 내용은 저장되지 않습니다. 메일쓰기를 취소하시겠습니까?") == true){    //확인
			location.href="<%=ctxPath%>/t1/mail.tw";
		 }else{   //취소
		     return false;
		 }
 	});
 	
 	
 	
 	// ======================= 주소록 버튼 추가작업 시작 =============================
 	
 	// 1) 주소록 팝업 열기
 	$("button#contact").bind('click',function(){  
 		
 		window.name="email_employeeMapParent";
 		
 		window.open("<%=ctxPath%>/t1/emailEmployeeMap.tw", "email_employeeMap", "width=800px, height=600px, top=50px, left=500px, scrollbars=yes");
 	}); // end of $("button#contact").bind('click',function(){
 	
   // ======================= 주소록 버튼 추가작업 끝 =============================
 	
	   
 });// end of $(document).ready(function(){})----------------
 
 // 보낸메일함 저장하기
 function checkSaveSentMail(){
	 
	 if($("input#saveSentMail").is(":checked") == false) {
		 	$("input#saveSentMail").prop('checked',true);
			$("input[name=saveSentMail]").val("1");		
		} else {
		 	$("input#saveSentMail").prop('checked',false);
			$("input[name=saveSentMail]").val("0");
		}
 }
 
 
 //======================= 주소록 버튼 추가작업 시작 =============================
 
 // 자식창 (주소록)이 닫치고 실행할 부모 함수
 function afterEmailEmployeeMap(){
	
	// 2) 주소록 팝업에서 적용 클릭시 부모창에 보내 준 데이터 가공 => 수신자, 참조에 넣어주기
	var receiverEmail= $("input#receiverEmail").val();
	var ccEmail= $("input#ccEmail").val();
	//console.log("주소록에서 가져온 메일주소:"+receiverEmail);
	//console.log(typeof receiverEmail)
	
	var addrsCancelIdx = 0;
	if(receiverEmail!=""){
	 	// 여러명일 경우 고려, 참조는 없는 경우도 존재
	 	var receiverEmailArr= receiverEmail.split(",");	
	 	
	 	for(var i=0;i<receiverEmailArr.length;i++){
	 		 $('#receiver').append('<span class="email-ids">'+ receiverEmailArr[i] +' <span class="cancel-email" name="addrs-cancel-btn" style="color:gray;"><input class="mailIndex" type="hidden" value="'+addrsCancelIdx+'"><i class="far fa-window-close"></i></span></span>');
	 		 addrsCancelIdx = addrsCancelIdx+1;
	 	} // end of for-------------------------
		//console.log("주소록에서 추가한 이메일 배열: "+arrReceiverEmail);
	}
	
	var addrsCcCancelIdx = 0;
	if(ccEmail!=""){ 
		var ccEmailArr= ccEmail.split(",");
		for(var i=0;i<ccEmailArr.length;i++){
	 		 $('div#cc').append('<span class="email-ids">'+ ccEmailArr[i] +' <span class="cancel-email" name="addrs-ccCancel-btn" style="color:gray;"><input class="mailIndex" type="hidden" value="'+addrsCcCancelIdx+'"><i class="far fa-window-close"></i></span></span>');
		 	addrsCcCancelIdx =addrsCcCancelIdx+1;
	 	} // end of for-------------------------	
	
	}
	
 } // end of function afterEmailEmployeeMap(){----------------
	
 // ======================= 주소록 버튼 추가작업 끝 =============================
	 
</script>

<div id="mail-header" style="width: 100%; height: 120px; padding: 20px;">
	 <i class="far fa-paper-plane fa-lg"></i>&nbsp;&nbsp;<span style="display: inline-block; font-size:22px; margin-bottom: 20px;">메일쓰기</span>
	 <div id="left-header">
		 <button type="submit" id="btnSend" class="btn-style float-right">보내기</button>
		 <button type="button" id="btnCancel" class="btn-style float-right">취소</button>
		 <input type="checkbox" id="saveSentMail" />&nbsp;&nbsp;<a href="javascript:checkSaveSentMail()" style="text-decoration:none; color:black;">보낸메일함에 저장</a>
	 </div>
</div>
<div id="mailForm-container" style="padding: 10px; border: solid 1px gray;">
 <form name="newMailFrm" enctype="multipart/form-data"> 
	  <input type="hidden" name="saveSentMail" />
      <table id="table">
         <tr>
            <th>보내는사람</th>
            <td>
                <span>${sessionScope.loginuser.name}&lt;${loginuser.email}&gt;</span>
                <input type="hidden" name="senderEmail" value="${sessionScope.loginuser.email}" class="short" readonly />     
            </td>
         </tr>
         <tr>
            <th><label for="receiver">받는사람&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="checkbox" id="write-to-myself" />&nbsp;&nbsp;내게쓰기</th>
            <td>
               <div id="receiver" style="width:95%; border: solid 1px #ccc; display: inline-block;"><input type="text" id="to-input" style="width:300px; margin-right: 10px; display: inline-block; border: none; outline:none; overflow:hidden;"/></div><button type="button" id="contact" class="btn-style" style="display:inline-block;">주소록</button> 
               <input type="hidden" name="receiverEmail" />
            </td>
         </tr>
         <tr>
            <th><label for="cc">참조</label></th>
            <td>
            	<div id="cc" style="width:95%; border: solid 1px #ccc; display: inline-block;"><input type="text" id="cc-input" style="width:300px; margin-right: 10px; display: inline-block; border: none; outline:none;"/></div>
	            <input type="hidden" name="ccEmail" />
            </td>
         </tr>
         <tr>
            <th>
            <input type="hidden" name="checkImportant" />
            	<span>제목&nbsp;&nbsp;<input type="checkbox" id="starred" />&nbsp;&nbsp;중요 <i class="fas fa-star" style="color: #ffcc00"></i></span></th>
            <td>
               <input type="text" name="subject" id="subject" style="width:100%"/>       
            </td>
         </tr>
         <tr>
         	<th>파일첨부</th>
         	<td>
         		<input type="file" name="attach" style="width: 100%;"/>
         	</td>
           </tr>
         <tr>
            <th>내용</th>
            <td>
               <textarea rows="10" cols="100" style="width: 100%; height: 100%;" name="content" id="content"></textarea>       
            </td>
         </tr>
      </table>
      
      
      <!-- 답메일 쓰기 기능용 -->
      <input type="hidden" name="parentSeq" value="${requestScope.parentSeq}"/>
      <input type="hidden" name="groupno" value="${requestScope.groupno}"/>
      <input type="hidden" name="depthno" value="${requestScope.depthno}"/>
      
   </form>
   
   <!-- 주소록에서 가져온 수신자, 참조 값을 넣어주기 위한 hidden form -->
   <form name="fromAddrsBookFrm">
   		<input type="hidden" id="receiverEmail" name="receiverEmail" value="" />
   		<input type="hidden" id="ccEmail" name="ccEmail" value="" />
   </form>
   
</div>  
