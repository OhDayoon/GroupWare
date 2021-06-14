<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath= request.getContextPath(); %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap-theme.min.css">
<style type="text/css">
	
	div#employeeTodoBox{
		border: solid 0px red;
		width:1400px;
		margin:0 auto;
		position: relative;
		left: -110px;
		padding-bottom: 100px;
	}
	
	div#tabMenu span{
		display: inline-block;
		width:150px;
		height: 50px;
		font-weight: bold;
		padding-top:17px;
		cursor: pointer;
		text-align: center;
		margin-left:-5px;
		border: solid 2px #cecece;
		border-bottom: 0px;
	}
	
	div#tabMenu span:hover{
		color:#005c99;
		font-weight: bold;
	}
	
	div#toDoBox{
		margin:0 auto;
		padding-left:12px;
		padding-right:12px;
		width:75%;
		clear:both;
		border: solid 2px #cecece;
		margin: 5px 0px 30px 60px;
	}
	
	div.eachtoDoList{
		height:50px;
		border-bottom: solid 2px #cecece;
		padding-top: 17px;
	}

	div.ajaxEachList {
		height:70px;
		padding-top: 25px;
	}
	
	div#toDoBox span{
		border: solid 0px red;
		margin-left: 15px;
		display: inline-block;
		text-align: center;
	}
	
	div#toDoTitle span{
		text-align: center;
		font-weight: bold;
	}
	
	button.state{
		background-color: #737373;
		color: #fff;
		font-weight:bold;
		width:120px;
		height: 40px;
		margin: -10px 0px 0px 20px;
		padding-top:5px;
		border: none;
	}
	
	button.goWork:hover{
		box-shadow: 2px 2px 2px #333;
		background-color:#4d4d4d;
	}
	
	span.hurry{
		background-color: #ff3300;
		color: #fff;
		font-weight:bold;
	}
	
	select.ingDetail{
		width:120px;
		height: 25px;
		margin: -10px 0px 0px 15px;
	}
	
	select.delayState{
		background-color: #750099;
		color:#fff;
		font-weight: bold;
	}
	
	div#periodOption label.period{
		cursor: pointer;
		color: #9e9e9e;
		font-size: 10pt;
	}
	
	div#periodOption label.period:hover{
		color: #7a7aeb;
		font-weight: bolder;
	}
	
	div#toDoList select {cursor:pointer;}
	
	div.forModal{
		border: solid 0px red;
		height:30px;
	}
	
	table.infoTable, table.infoTable th, table.infoTable td{
		border: solid 2px #bdc2bd;
		border-collapse: collapse;
		text-align: center;
		vertical-align: middle;
	} 
	
	table.infoTable {
		width: 95%;
		margin: 0 auto;
	}
	
	table.infoTable th{
		background-color: #003d66;
		padding: 10px 0px;
		color: #fff;
		border-top: solid 2px #003d66;
	}
	
	table.infoTable td{
		padding: 10px 0px;
	}
	
	span.sendMail{
		display: inline-block;
		background-color: #0071bd;
		cursor: pointer;
		color:#fff;
		font-weight: bolder;
		width: 100px;
		padding: 5px 0px;
		text-align: center;
		position: relative;
		left: 50px;
	}
	
	span.sendMail:hover{box-shadow: 3px 3px 3px #ccc;}
	
	div.topTag{
		background-color: #666666;
		color:#fff;
		font-weight: bolder;
		padding: 5px 0px;
		width: 100px;
		text-align: center;
		margin-bottom: 20px;
		margin-left: 20px;
	}
	
	
</style>

<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
	   
		$("div#periodOption").hide();
		
		// 선택된 업무유형에 따라 버튼색깔 다르게 표시하기=> "0":신규등록업무, "1":진행중업무, "2":진행완료업무
		if("${requiredState}"=="0"){
			$("span#newTodo").css("background-color","#0071bd");
			$("span#newTodo").css("color","#fff");
		}
		else if("${requiredState}"=="1"){
			$("span#ingTodo").css("background-color","#0071bd");
			$("span#ingTodo").css("color","#fff");
			// 진행중업무의 경우 메뉴 1개가 더 추가되므로 너비 조절해주기
			$("div#toDoBox").css("width","85%");
			$("div#pageBar").css("width","88%");
		}
		else{
			$("span#doneTodo").css("background-color","#0071bd");
			$("span#doneTodo").css("color","#fff");
			// 진행완료 업무인 경우 프로젝트명의 margin이 달리지므로 이에따라 다른 margin도 조절
			$("span#pname").css("margin-left","30px");
			$("div#toDoBox").css("width","73%");
			$("div#pageBar").css("width","76%");
			
			// 진행완료업무가 선택된 경우 기가옵션 보여주기, 선택된 기간옵션 색깔 유지시켜주기
			$("div#periodOption").show();
			var periodOption= "${periodOption}";
			if(periodOption == "week"){
				$("label#week").css("color","#005c99");
			}
			else if(periodOption == "month"){
				$("label#month").css("color","#005c99");
			}
			else if(periodOption=="3months"){
				$("label#3months").css("color","#005c99");
			}
			else{
				$("label#all").css("color","#005c99");
			}
		}

		// 페이지가 로드될때 진행중업무 상태에 맞는 상태값 유지시켜주기
		$("select.postpone").each(function(index,item){
			var state= $(this).prop("name");
			$(this).val(state);
		}); // end of $("select.postpone").each(function(index,item){
		

		// 지연상태인 경우 select태그 배경색 변경하기  => 페이지가 로드될때 적용
		$("select.delay").each(function(index,item){
			if($(item).val()=="1"){
				$(item).addClass("delayState");
			}
		}); // end of $("select.delay").each(function(index,item){
			
			
		// 진행중인 경우 상태변경(진행중 또는 보류)에 따라 update하기
		$("select.postpone").change(function(){
			var fk_pNo= $(this).prop('id');	
			var ingDetail= $(this).val();
			
			if($(this).val()!="2"){  // 진행완료를 클릭한 경우는 제외
				$.ajax({
					url:"<%=ctxPath%>/t1/updateIngDetail.tw",
					type:"POST",
					data:{"fk_pNo":fk_pNo,"ingDetail":ingDetail},
					dataType: "JSON",
					success: function(json){
						if(json.n==1){ // update 성공한 경우
							alert("상태가 변경되었습니다.");
						}
					},
					error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }
				}); // end of $.ajax({--------------
			}
		}); // end of $("select.ingDetail").change(function(){--------- 	
		
			
		// 진행완료버튼 클릭시 진행중업무에서 진행완료업무로 이동시키기
		$("select.ingDetail").change(function(){
			
			if($(this).val()=="2"){  // 진행완료를 클릭한 경우
				var productName= $(this).parent().find("span.productName").text();
				var fk_pNo= $(this).prop('id');	
				var fullState= $(this).parent().find("span.fullState").prop("id");
				
				if(fullState=="0"){
					alert(productName+" 상품은 예약인원이 부족하여 진행완료 처리가 불가능합니다.");
					window.location.reload(true);
				}
				else{
					goWorkDone(fk_pNo,productName);
				}
			}
		})// end of $("select.ingDetail").change(function(){----------- 	
		
		
		// 진행완료 업무에서 기간옵션 클릭 이벤트
		$("label.period").click(function(){
			
			var periodOption= $(this).prop('id');
			location.href="<%=ctxPath%>/t1/employeeTodo.tw?&requiredState=2&periodOption="+periodOption;
		
		}); // end of $("label.period").click(function(){---------
		
		 
		// 진행중 업무, 진행완료 업무의 특정 행 클릭시 해당업무를 자세히 모여주는 모달창 띄어주기 => 신규등록업무는 해당사항 없음
		if("${requiredState}"!="0"){
			
			// 1) hover 이벤트 (마우스커서, 글씨굵기 변경)
			$("div.forModal").hover(function(){
				$(this).css("cursor","pointer"); 
				$(this).css("font-weight","bold"); 
			},function(){
				$(this).css("font-weight","normal");
			}); // end of $("div.forModal").hover(function(){----
			
			// 2) click 이벤트 => 업무 세부정보 모달창으로 표시
			$("div.forModal").click(function(){
				
				var fk_pno= $(this).prop('id');
				getOneProductInfo(fk_pno);	// 모달창에 필요한 정보 가져오는 함수
				$("div.modal").modal();

			}); // end of $("div.forModal").click(function(){---- 	
				
		}// end of if("${requiredState}"!="0"){---------
			
		
		// 진행 중 업무 메일 보내기 버튼 클릭 시 이벤트 => 여행준비물 메일 보내기
		$(document).on('click',('span.sendMailIng'),function(){
		
			var clientmobile= $(this).prev().prev().val();
			var fk_pNo= $(this).prev().val();
			var clientname= $(this).parent().prev().prev().text();
			
			$.ajax({  // 여행준비물 메일 보내기
				url:"<%=ctxPath%>/t1/sendEmailIngTodo.tw",
				data:{"clientmobile":clientmobile, "fk_pNo":fk_pNo},
				type:"POST",
				dataType:"JSON",
				success:function(json){
					if(json.n==0){
						alert(clientname+" 님에게 성공적으로 [여행준비물]메일을 전송했습니다.");
					}	
					else{
						alert("메일 전송에 실패했습니다. 다시 시도해주세요.");
					}
				},
				error: function(request, status, error){
		        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			}); // end of $.ajax({------
			
		}); // end of $(document).on('click','(span.sendMailIng)',function(){--------
		
			
		// 진행 완료 업무 메일 보내기 버튼 클릭 시 이벤트 => 여행사 홍보 메일 보내기
		$(document).on('click',('span.sendMailEnd'),function(){
		
			var clientmobile= $(this).prev().prev().val();
			var fk_pNo= $(this).prev().val();
			var clientname= $(this).parent().prev().prev().text();
			
			$.ajax({  // 여행사 홍보 메일 보내기
				url:"<%=ctxPath%>/t1/sendEmailIngDone.tw",
				data:{"clientmobile":clientmobile, "fk_pNo":fk_pNo},
				type:"POST",
				dataType:"JSON",
				success:function(json){
					if(json.n==0){
						alert(clientname+" 님에게 성공적으로 [여행사 홍보]메일을 전송했습니다.");
					}	
					else{
						alert("메일 전송에 실패했습니다. 다시 시도해주세요.");
					}
				},
				error: function(request, status, error){
		        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			}); // end of $.ajax({------
			
		}); // end of $(document).on('click','(span.sendMailIng)',function(){--------
			
			
	}); // end of $(document).ready(function(){--------------------------
	
		
		
	// ===== function declaration =====
		
	// requiredState에 따라 해당정보를 가져오기위해  동일 url로 보내는 메소드
	function getEmployeeTodo(requiredState){  // "0":신규등록업무, "1":진행중업무, "2":진행완료업무
		location.href="<%=ctxPath%>/t1/employeeTodo.tw?requiredState="+requiredState;
	} // end of function getEmployeeTodo(requiredState){-----
	
	
	// 진행시작버튼 클릭시 실행되는 함수 => 진행시작버튼 클릭시 진행중업무로 이동
	function goWorkStart(fk_pNo,pName){
		var bool= confirm('"'+pName+'" 에 대해서 업무를 시작하시겠습니까?');
		if(bool){  // 업무시작처리
			$.ajax({
				url:"<%=ctxPath%>/t1/goWorkStart.tw",
				type:"POST",
				data:{"fk_pNo":fk_pNo},
				dataType: "JSON",
				success: function(json){
					if(json.n==1){ // 업무시작에 성공한 경우
						alert("업무 시작처리 되었습니다. [진행중 업무]에서 확인 가능합니다.")
						window.location.reload(true);
					}
					else{ // 업무시작에 실패한 경우
						alert("시스템 오류로 업무시작에 실패했습니다. 관리자에게 문의바랍니다.");
					}
				},
				error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			}); // end of $.ajax({-------
		}
	} // end of function goWorkStart(fk_pNo,pName){-----
	
		
	// 진행완료버튼 클릭시 실행되는 함수 => 진행중업무에서 진행완료업무로 이동
	function goWorkDone(fk_pNo,productName){
		var bool= confirm('"'+productName+'" 업무를 완료처리 하시겠습니까?');
		if(bool){  // 업무완료처리
			$.ajax({
				url:"<%=ctxPath%>/t1/goWorkDone.tw",
				type:"POST",
				data:{"fk_pNo":fk_pNo},
				dataType: "JSON",
				success: function(json){
					if(json.n==1){ // 업무시작에 성공한 경우
						alert("업무가 완료처리 되었습니다. [진행완료 업무]에서 확인 가능합니다.")
						window.location.reload(true);
					}
					else{ // 업무시작에 실패한 경우
						alert("시스템 오류로 업무완료에 실패했습니다. 관리자에게 문의바랍니다.");
					}
				},
				error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			}); // end of $.ajax({-------
		}
	} // end of function goWorkDone(fk_pNo,pName){--------
	
		
	// 특정업무 상세 정보 조회		
	function getOneProductInfo(fk_pNo){
		
		// 1) 상품정보 알아오기
		$.ajax({      
			url:"<%=ctxPath%>/t1/selectProductInfoForModal.tw",  
			type:"GET",
			data:{"fk_pNo":fk_pNo},
			dataType: "JSON",
			success: function(json){

				var topTag="";
				if("${requiredState}"=="1"){
				 	topTag="<div class='topTag' style='background-color:#ff4d4d;'>진행중</div>"
				}
				else if("${requiredState}"=="2"){
				 	topTag="<div class='topTag'>진행완료</div>"
				}
				
				var html=topTag+ 
						 "<table class='productDetailInfoTable infoTable'>"+
			      			"<tr>"+
			      				"<th style='width:35%; border-left: solid 2px #003d66;'>프로젝트명</th>"+
			      				"<th style='width:21%;'>여행기간</th>"+
			      				"<th style='width:12%;'>예약인원수</th>"+
			      				"<th style='width:16%;'>최소예약인원수</th>"+
			      				"<th style='border-right: solid 2px #003d66;''>최대예약인원수</th>"+
			      			"</tr>"+
			      			"<tr>"+
			      				"<td>"+json.pName+"</td>"+
			      				"<td>"+
			      					json.startDate+"<br/>-&nbsp;"+json.endDate+"<br/>"+
			      					"["+json.period+"]"+
			      				"</td>"+
			      				"<td>"+json.nowNo+"명</td>"+
			      				"<td>"+json.miniNo+"명</td>"+
			      				"<td>"+json.maxNo+"명</td>"+
			      			"</tr>"+
			      		 "</table>";
			      		 
			     $("div.modal-body").html(html); 		 
				
			     selectClientInfoForModal(fk_pNo,1); // 페이징 처리한 특정 업무의 고객정보 가져오는 함수 호출
			     
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({---------
			
	} // end of function getOneProductInfo(fk_pno){-------
		
	
	// 특정 업무의 고객정보 알아오기 (페이징바 처리된 정보)
	function selectClientInfoForModal(fk_pNo,currentShowPageNo){
		$.ajax({      
			url:"<%=ctxPath%>/t1/selectClientInfoForModal.tw",  
			type:"post",
			data:{"fk_pNo":fk_pNo,"currentShowPageNo":currentShowPageNo},
			dataType: "JSON",
			success: function(json){
				var html="";
				if(json.length>0){
					html= "<div style='margin:30px 0px 15px 0px; padding-left:35px; font-weight: bold;'>▶&nbsp;예약자 명단&nbsp;◀</div>"+
				      	  "<table class='clientDetailInfoTable infoTable'>"+
				      	  	  "<tr>"+
				      		     "<th style='width:25%; border-left: solid 2px #003d66;'>예약자명</th>"+
				      			 "<th style='width:20%;'>인원수</th>"+
				      			 "<th style='text-align:left; padding-left:105px; width: 40%; border-right: solid 2px #003d66;'>연락처</th>"+
				      		  "</tr>";
		      		
					$.each(json,function(index,item){
						
						// 고객 연락처 데이터 가공해서 보여주기
						var clientmobile= item.clientmobile;
						clientmobile= clientmobile.substr(0,3)+"-"+clientmobile.substr(3,4)+"-"+clientmobile.substr(7,4);
						
						html+= "<tr>"+
							   		"<td>"+item.clientname+"</td>"+
							   		"<td>"+item.cnumber+"명</td>"+
							   		"<td style='text-align:left; padding-left:80px;'>"+
						   				clientmobile+
						   				"<input type='hidden' class='clientmobile' value='"+item.clientmobile+"' />"+
						   				"<input type='hidden' class='fk_pNo' value='"+item.fk_pNo+"' />";
						   				
		   				if(item.endDate!="0"){ // 종료된 업무 => 여행사 홍보 메일보내기
		   					html+=      "<span class='sendMail sendMailEnd'>메일 보내기</span>";
		   				}
		   				else{ // 진행 중 업무 => 여행준비물 메일보내기
		   					html+=      "<span class='sendMail sendMailIng'>메일 보내기</span>";
		   				}
			   			
		   				html+=		"</td>"+
						   	   "</tr>";
						   			
					}); // end of $.each(json,funtion(index,item){-------
					
					html+="</table>";
				} // end of if-------------
				else{
					html= "<div style='margin:30px 0px 15px 0px; text-align:center; font-weight: bold;'>예약고객이 존재하지 않습니다.</div>";
				}
				
				$("div.modal-body2").html(html);	 
				clientLisPageBar(fk_pNo,currentShowPageNo); // 페이지바 함수 호출
				
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({---------	
	} // end of function selectClientInfoForModal(){-------	
		
		
		
	// 고객 예약정보 페이징바 생성 함수
	function clientLisPageBar(fk_pNo,currentShowPageNo){
		
		// 1) 특정업무에 관한 고객의 totalPage 수 알아오기
		$.ajax({      
			url:"<%=ctxPath%>/t1/getclientLisTotalPage.tw",  
			type:"GET",
			data:{"fk_pNo":fk_pNo,"sizePerPage":"3"},
			dataType: "JSON",
			success: function(json){
				if(json.totalPage>0){ // 예약고객이 존재하는 경우 => 페이징바 생성 
					
					// 2) 페이징바 생성을 위한 사전작업
					var totalPage = json.totalPage;
					var pageBar= ""
					var blockSize = 3;
		            var loop=1;
		            
		            if(typeof currentShowPageNo == "string"){ // currentShowPageNo 숫자처리
		            	currentShowPageNo= Number(currentShowPageNo)	   
		            }
		            
		            var pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1; 
		            
		         	// 2) 페이징바 생성
		            if(pageNo!=1) {
		            	pageBar+="&nbsp;<a href='javascript:selectClientInfoForModal("+fk_pNo+",\"1\")'>[맨처음]</a>&nbsp;";
		            	pageBar+="&nbsp;<a href='javascript:selectClientInfoForModal("+fk_pNo+",\""+(pageNo-1)+"\")'>[이전]</a>&nbsp;";
		    		}
		    		
                    while( !(loop > blockSize || pageNo > totalPage) ) {
   					
						if(pageNo == currentShowPageNo) {
							pageBar += "&nbsp;<span style='color:#fff; background-color: #003d66; font-weight:bold; padding:2px 4px;'>"+pageNo+"</span>&nbsp;";
						}
						else {
							pageBar += "&nbsp;<a href='javascript:selectClientInfoForModal("+fk_pNo+",\""+pageNo+"\")'>"+pageNo+"</a>&nbsp;";
						}
						
						loop++;
						pageNo++;
					}// end of while------------------------
		    		
					if(pageNo <= totalPage) {
						pageBar += "&nbsp;<a href='javascript:selectClientInfoForModal("+fk_pNo+",\""+pageNo+"\")'>[다음]</a>&nbsp;";
						pageBar += "&nbsp;<a href='javascript:selectClientInfoForModal("+fk_pNo+",\""+totalPage+"\")'>[마지막]</a>&nbsp;";
					}
		            
				} // end of if(json.totalPage>0){------------------------
					
				pageBar += "</ul>";
				$("div.modal-pageBar").html(pageBar);
					
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({---------
		
	}// end of function pageBarClientList(currentShowPageNo){------
		
		
		
</script>

<div id="employeeTodoBox">
	<div style="margin: 70px 0px 0px 65px; font-size: 22pt; font-weight: bold;" >${sessionScope.loginuser.name}님의&nbsp;업무현황</div>
	<div id="tabMenu" style="margin: 30px 0px 0px 50px; float:left;">
		<span id="newTodo" onclick="getEmployeeTodo('0')" style="margin-left:10px;">신규등록업무&nbsp;(${newTodoCnt}건)</span>
		<span id="ingTodo" onclick="getEmployeeTodo('1')" style="border-left:0px; border-right:0px;">진행중업무&nbsp;(${ingTodoCnt}건)</span>
		<span id="doneTodo" onclick="getEmployeeTodo('2')">진행완료업무&nbsp;(${doneTodoCnt}건)</span>
		
		<%-- 진행완료업무 클릭시에만 보여지는 날짜선택 옵션 --%>
		<div style="float: right; margin-left: 190px; padding-top:17px;" id="periodOption">
			<label style="color: #333; font-size: 10pt;">기간설정&nbsp;|&nbsp;</label>
			<label class="period" id="week">1주일&nbsp;[${weekCnt}건]</label>&nbsp;&nbsp;
			<label class="period" id="month">1개월&nbsp;[${monthCnt}건]</label>&nbsp;&nbsp;
			<label class="period" id="3months">3개월&nbsp;[${threeMonthsCnt}건]</label>&nbsp;&nbsp;
			<label class="period" id="all">전체&nbsp;[${doneTodoCnt}건]</label>
		</div>
	</div>
	
	<div id="toDoBox">
		<div id="toDoTitle" class="eachtoDoList">
			<span style="margin-left:20px; width:50px;">순번</span>
			<span style="margin-left:58px; width:250px; text-align:left; padding-left:60px;" id="pname">프로젝트명</span>
			<span style="width:70px;">배정자</span>
			<span style="width:110px;">배정일</span>
			<span style="width:110px;">시작일</span>
			<c:if test="${requiredState ne '2'}">  <%-- 신규등록업무와 진행중 업무에만 존재 --%>
				<span style="width:110px;">기한일</span> 
			</c:if>
			<c:if test="${requiredState eq '1'}">  <%-- 진행중 업무에만 존재 --%>
				<span style="width:110px;">예약현황</span> 
			</c:if>
			<c:if test="${requiredState eq '2'}">  <%-- 진행완료 업무에만 존재 --%>
				<span style="width:110px;">완료일</span> 
			</c:if>
			<span style="width:120px;">상태</span>
		</div>

		<div id="toDoList">
			<c:if test="${not empty tvoList}">
				<c:forEach var="tvo" items="${tvoList}" varStatus="status">
					<c:choose>
						<c:when test="${status.last}">
							<div class="eachtoDoList ajaxEachList" style="border:none;">
						</c:when>
						<c:otherwise>
							<div class="eachtoDoList ajaxEachList">
						</c:otherwise>
					</c:choose>
					<div class="forModal" style="float:left;" id="${tvo.fk_pNo}">
						<span style="margin-left:20px; width:50px;">${tvo.rno}</span>
						
						<c:if test="${tvo.hurryno eq '1' and requiredState ne '2'}">
							<span class="hurry">&nbsp;긴급&nbsp;</span>
							<span style="width:250px; color:red; text-align:left; margin-left:5px;" class="productName">${tvo.pName}</span>
						</c:if>
						<c:if test="${tvo.hurryno eq '0' and requiredState ne '2'}">
							<span style="width:250px; margin-left:58px; text-align:left;" class="productName">${tvo.pName}</span>
						</c:if>
						<c:if test="${requiredState eq '2'}">
							<span style="width:250px; margin-left:30px; text-align:left;" class="productName">${tvo.pName}</span>
						</c:if>
						
						<span style="width:70px;">${tvo.name}</span>
						<span style="width:110px;">${tvo.assignDate}</span>
						<span style="width:110px;">${tvo.startDate}</span>
						
						<c:if test="${requiredState ne '2'}">
							<span style="width:110px;">${tvo.dueDate}</span>  <%-- 신규등록업무와 진행중 업무에만 존재 --%>
						</c:if>
						
						<c:if test="${requiredState eq '1'}">  <%-- 진행중 업무에만 존재 --%>
							<c:if test="${tvo.fullState eq'0'}"> <%-- 예약인원 미충족 --%>
								<span style="width:110px;" class="fullState" id="${tvo.fullState}">${tvo.nowNo}명&nbsp;/&nbsp;${tvo.maxNo}명</span> 
							</c:if>
							<c:if test="${tvo.fullState eq'1'}"> <%-- 최소예약인원 충족 --%>
								<span style="width:110px; color:blue;" class="fullState" id="${tvo.fullState}">${tvo.nowNo}명&nbsp;/&nbsp;${tvo.maxNo}명</span> 
							</c:if>
							<c:if test="${tvo.fullState eq'2'}"> <%-- 최대예약인원 충족 --%>
								<span style="width:110px; color:red;" class="fullState" id="${tvo.fullState}">${tvo.nowNo}명&nbsp;/&nbsp;${tvo.maxNo}명</span> 
							</c:if>
						</c:if>
						
						<c:if test="${requiredState eq '2'}">  <%-- 진행완료 업무에만 존재 --%>
							<span style="width:110px;">${tvo.endDate}</span> 
						</c:if>
					</div>
						
					<c:if test="${requiredState eq '0'}">
						<button type="button" class="state goWork" onclick="goWorkStart('${tvo.fk_pNo}','${tvo.pName}')">진행시작하기</button> <%-- 신규등록 업무에만 존재 --%>
					</c:if>
					<c:if test="${requiredState eq '1'}"> <%-- 진행중 업무에만 존재 --%>
					  	<c:if test="${tvo.ingDetail eq '0' or tvo.ingDetail eq '-1' }"> <%-- 지연된 업무가 아닌 경우 --%>
							<select class="ingDetail postpone" id="${tvo.fk_pNo}" name="${tvo.ingDetail}">
								<option value="0">&nbsp;진행중</option> 
								<option value="-1">&nbsp;보류</option>
								<option value="2">&nbsp;진행완료</option>
							</select>
						</c:if>
						<c:if test="${tvo.ingDetail ne '0' and tvo.ingDetail ne '-1' }"> <%-- 지연된 업무인 경우 --%>
							<select class="ingDetail delay" id="${tvo.fk_pNo}">
								<option value="1" style="background-color: #730099; color: #fff; font-weight: bold;">&nbsp;지연 &nbsp;(&nbsp;+${tvo.ingDetail}일&nbsp;)</option> 
								<option value="2" style="background-color: #fff; color: black; font-weight: normal;">&nbsp;진행완료</option>
							</select>
						</c:if>
					</c:if>
					
					<c:if test="${requiredState eq '2'}">
						<button type="button" class="state" style="cursor:context-menu;">진행완료</button> <%-- 진행완료 업무에만 존재 --%>
					</c:if>
				</div>
			</c:forEach>
		</c:if>
			
		<c:if test="${empty tvoList}">
			<div class="eachtoDoList ajaxEachList" align="center" style="font-weight: bold;">업무내역이 없습니다.</div>
		</c:if>
	</div>
</div>	
	
	<div align="right" style="margin: 30px 0px; width:78%; font-size:14pt; clear:both;" id="pageBar">${pageBar}</div>
	
	
	<!-- 각 업무상세정보 modal로 보여주기 -->
	<div class="modal fade modalLocation" id="layerpop" >
  		<div class="modal-dialog modal-lg">
	    	<div class="modal-content">
		      	<!-- header -->
		      	<div class="modal-header">
		        	<!-- 닫기(x) 버튼 -->
		        	<button type="button" class="close" data-dismiss="modal">×</button>
		        	<!-- header title -->
		        	<h4 class="modal-title">업무 세부정보</h4>
		      	</div>
		      	<!-- body -->
		      	<div>
			      	<!-- body의 특정업무 상세정보 (ajax로 값이 들어온다) -->
			      	<div class="modal-body"></div>
			      	<!-- body의 특정업무 고객예약 상세정보 (ajax로 값이 들어온다) -->
			      	<div class="modal-body2"></div>
			      	<!-- body의 페이지바 (ajax로 값이 들어온다) -->
			      	<div class="modal-pageBar" align="center" style="font-size:12pt; margin: 20px 0px;"></div>
		      	</div>
		      	<!-- Footer -->
		      	<div class="modal-footer">
		        	<button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
		      	</div>
	    	</div>
  		</div>
	</div>
