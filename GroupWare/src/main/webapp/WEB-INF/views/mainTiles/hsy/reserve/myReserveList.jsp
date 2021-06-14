<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath= request.getContextPath(); %>

<style type="text/css">
	
	div#reserveTitle{
		border: solid 0px red;
		width:70%;
		margin:0 auto;
		padding:10px 0px;
		font-size: 18pt;
		font-weight: bold;
	}
	
	div#reserveTitle span {color: #0071bd;}
		
	div#reserveList{
		border: solid 0px red;
		width:70%;
		margin:0 auto;
		margin-top:30px;
	}
	
	div.outerBox{
		border: solid 2px #0071bd; 
		padding: 30px 50px 0px 50px;
		margin-bottom:40px;
	}
	
	div.outerBox:hover {
		border: solid 2px #ff4d4d;
		box-shadow:1px 1px 1px #333;
	}
	
	table.reserveInfoTable1, table.reserveInfoTable1 th, table.reserveInfoTable1 td {
		border: solid 2px #bfbfbf;
		border-collapse: collapse;
		text-align: center;
	}
	
	table.reserveInfoTable1, table.reserveInfoTable2{
		width: 100%;
	}
	
	table.reserveInfoTable2, table.reserveInfoTable2 th, table.reserveInfoTable2 td {
		border: solid 2px #bfbfbf;
		border-collapse: collapse;
	}
	
	table.reserveInfoTable2 td {padding-left:20px;}
	
	div.reserveDetailInfo{
		border: solid 0px red;
		margin: 20px 0px 15px 0px;
		background-color: #d7d9da;
		padding: 5px 0px 5px 20px;
		font-weight: bold;
	}
	
	div.funcBt span{
		display: inline-block;
		width:110px;
		height: 25px;
		background-color: #5d5f5d;
		color: #fff;
		font-weight: bold;
		font-size: 12pt;
		cursor:pointer;
		text-align: center;
		padding-top:5px;
		position: relative;
		top:-38px;
		border: solid 1px #5d5f5d;
	}
	
	div.funcBt span:hover{
		background-color: #d8dad8;
		color: #000;
		border: solid 1px #000;
	}
	
	span#goHome{
		cursor: pointer;
		color: #9e9e9e;
		font-size:15pt;
	}
	
	span#goHome:hover{
		color: #333;
		font-weight: bolder;
	}
	
	button#moreList{
		background-color: #0071bd;
		font-weight: bold;
		color: #fff;
		cursor: pointer;
		height: 30px;
		width: 70px;
		border: none;
		margin-right: 10px;
	}
	
	button#moreList:hover {box-shadow:2px 2px 2px #333;}
	
	input.ccount {width:50px;}
	
	span.status{
		display: inline-block;
		color: #fff;
		font-weight: bold;
		font-size: 12pt;
		text-align: center;
		padding-top:5px;
		width: 100px;
		height: 25px;
		margin-bottom: 10px;
	}
	
	span.status0 {background-color: #ff4d4d;}
	span.status1 {background-color: #0071bd ;}
	
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		reserveList("1");

		// 더보기버튼 (또는 처음으로 버튼) 클릭 이벤트  
		$("button#moreList").click(function(){
			
			if($(this).text()=="처음으로"){
				$("div#reserveList").empty();
				$("span#currentCount").text("0");
				reserveList("1");
				$(this).text("더보기");
			}
			else{
				reserveList($(this).val());
			}
		}); // end of $("button#moreList").click(function(){-----
		
		// 더보기 버튼이 필요없는 경우 더보기 버튼 가리기
		if(Number("${totalCount}")<=3){
			$("button#moreList").hide();
		}
		
		// 인원수 변경 클릭시 이벤트처리 =>  최초 화면 호출 시 존재하는 요소가 아니므로 아래처럼 이벤트 구현
		$(document).on("click", "span.changeCount", function (){
			
			var ccountPrev= $(this).parent().prev().find("input.ccountPrev").val();
			var cnumber= $(this).parent().prev().find("input.ccount").val();
			var pNo= $(this).parent().prev().prev().prop('id');
			var price= $(this).parent().prev().find("td.price").prop('id');
			
			// 추가결제할 금액 산정
			var needPayCount= Number(cnumber)-Number(ccountPrev);
			var totalPrice= Number(price)*needPayCount; 
			
			// 결제 시 필요한 변수 선언
			var clientname= $("span#name").text();
			var clientmobile= "${clientmobile}";
			var fk_pNo= pNo;
			
			
			// 1) 기존인원수하고 변경인원수가 같은 경우 (바꾸지 않고 인원수 변경을 클릭한 경우)
			if(ccountPrev==cnumber){
				alert("기존에 예약하신 인원 수와 변경하신 인원 수가 동일합니다.");
				$(this).parent().prev().find("input.ccount").focus();
				return;
			}
			
			// 2) 인원수 변경시 유효성 검사
			var cnt= parseInt(cnumber);
			 
			var regExp= /^[0-9]+$/; // 숫자만 체크하는 정규표현식
		   	var bool= regExp.test(cnt);
		   	
	   		if(!bool){ // 문자로 입력한 경우
		   		alert("인원수를 제대로 입력해주세요.");
		   		$(this).parent().prev().find("input.ccount").val(ccountPrev);
		   		$(this).parent().prev().find("input.ccount").focus();
		        return; 
	   		}
	   		
	        if(cnt < 1 || cnt > 300) {
	           alert("인원수는 최소 1명 이상 300명 이하만 선택 가능합니다.");
	           $(this).parent().prev().find("input.ccount").val(ccountPrev);
	           $(this).parent().prev().find("input.ccount").focus();
	           return;
	        }
	        
	        // 3) 여행확정 상품인지 확인하기
	        $.ajax({ 
				url:"<%=ctxPath%>/t1/checkProductStatus.tw",
				type:"GET",
				data:{"pNo":pNo},
				dataType: "JSON",
				success: function(json){
					if(json.n=="0"){  // 2) 여행확정 상품인 경우 인원수 변경 불가
						alert(json.pName+"은 여행확정 상품이므로 인원수 변경이 불가능합니다."
							  +" 자세한 사항은 관리자에게 문의바랍니다.");
						window.location.reload(true);	
					}
					else{ // 3) 여행확정 상품이 아닌경우 
						if(Number(ccountPrev)>Number(cnumber)){ // 4-1) 새롭게 입력받은 인원수가 기존 예약인원수보다 적은 경우
							changeCount(pNo,needPayCount); // 고객테이블 update와 제품테이블 udpate 트랜잭션처리
							alert("해당 상품의 예약 인원수가 변경되었습니다.\n"+
								  "7일 이내에 잔여금액이 결제하신 계좌로 입금됩니다.\n"+
								  "자세한 사항은 관리자에게 문의바랍니다.")
							window.location.reload(true);	
						}
						
						else{ // 4-2) 새롭게 입력받은 인원수가 기존 예약인원수보다 많은 경우
							
							// 5) 새롭게 입력받은 인원수가 예약가능한지 확인
							$.ajax({
								url:"<%=ctxPath%>/t1/checkClientAjax.tw",
								data:{"fk_pNo":fk_pNo,"cnumber":needPayCount},
								type:"POST",
								dataType:"JSON",
								success:function(json){
									if(json.n==1){ // 예약가능한 인원수인 경우
										// 6) 예약가능한 경우 결제 후 고객테이블 update와 제품테이블 udpate 트랜잭션처리
										var bool= confirm("추가하신 "+needPayCount+"명에 대한 추가결제가 필요합니다. 결제를 진행하시겠습니까?");
										if(bool){ // 결제진행 동의
											 // 아임포트 결제 팝업창 띄우기
								             var url = "<%=ctxPath%>/t1/orderPayment.tw?amount="+totalPrice+"&buyer_name="+clientname
								            		   +"&buyer_tel="+clientmobile+"&fk_pNo="+fk_pNo;
								             window.open(url, "orderPayment","left=350px, top=100px, width=800px, height=570px"); 
								             changeCount(pNo,needPayCount); // 고객테이블 update와 제품테이블 udpate 트랜잭션처리
								             window.location.reload(true);
										}
										else{ // 결제동의 비동의
											 window.location.reload(true);
										}
									} // end of if---------------
									else{ // 예약불가능한 인원수인 경우
										alert("해당 상품에 추가예약 가능한 인원은 "+json.count+"명 입니다.\n"
											 +"인원수를 변경해주세요.");
										 window.location.reload(true);
									} // end of else------------------------
								},
								error: function(request, status, error){
						        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
						        }
							}); // end of $.ajax({-------------- 
						}
					} // end of else------------------
				},
				error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			}); // end of $.ajax({------------- 
		}); // end of $(document).on("click", "span.changeCount", function (){-------
		
			
			
		
		// 백원빈 작업 시작 //
		// 엑셀출력하기 버튼 클릭 시
		$("button#excelBtn").click(function(){
			var frm = document.excelFrm;
			frm.action="<%=ctxPath%>/excel/downloadSchedule.tw";
			frm.method="post";
			frm.submit();
		});
		// 백원빈 작업  끝 //
			
			
			
	}); // end of $(document).ready(function(){-------
		
		
	// === function declaration ===
		
	var len= 3; // "더보기" 클릭할때 보여줄 단위 크기 
	function reserveList(start){ 	
		$.ajax({
			url:"<%=ctxPath%>/t1/moreReserveListAjax.tw",
			type:"POST",
			data:{"clientmobile":"${clientmobile}","start":start,"len": len},
			dataType: "JSON",
			success: function(json){
			
	            var html="";
	            if(json.length>0){ // 데이터가 존재하는 경우 (데이터가 존재하지 않는 경우는 controller에서 처리함)

	            	$.each(json, function(index,item){
	            		var paidPrice= Number(item.price) * Number(item.cnumber);
	            		var paidPriceComma= paidPrice.toLocaleString('en')+"원";
	            		
	            		$("span#name").text(item.clientname);
	            		$("input#clientname").val(item.clientname);
	            		
	            		html+="<div class='outerBox'>"+
	            				"<div align='right'><span class='status "+item.statusColor+"'>"+item.status+"</span></div>"+
			            		"<table class='reserveInfoTable1'>"+
				            		"<tr>"+
					            		"<th style='width:50%;'>여행 상품명</th>"+
					            		"<th>여행 기간</th>"+
					            	"</tr>"+
					            	"<tr>"+
					            		"<td style='height:40px;'>"+item.pName+"</td>"+
					            		"<td>"+item.startDate+"&nbsp;-&nbsp;"+item.endDate+"&nbsp;["+item.period+"]</td>"+
				            		"</tr>"+
			            		"</table>"+
	            				"<div class='reserveDetailInfo' id='"+item.pNo+"'>예약상세정보</div>"+
			            		"<table class='reserveInfoTable2'>"+
				            		"<tr>"+
					            		"<th style='width:15%; height:40px;'>예약자명</th>"+
					            		"<td style='width:30%;'>"+item.clientname+"</td>"+
					            		"<th style='width:20%;'>연락처</th>"+
					            		"<td style='width:35%;'>"+item.clientmobile+"</td>"+
				            		"</tr>"+
				            		"<tr>"+
					            		"<th style='height:40px;'>예약 인원수</th>"+
					            		"<td>"+
					            			"<input type='number' class='ccount' value='"+item.cnumber+"' min='1' max='300' />&nbsp;&nbsp;명"+
					            			"<input type='hidden' class='ccountPrev' value='"+item.cnumber+"' />"+
					            		"</td>"+
					            		"<th>결제완료 금액</th>"+
					            		"<td class='price' id='"+item.price+"'>"+paidPriceComma+"</td>"+
				            		"</tr>"+
			            		"</table>"+
	            				"<div class='funcBt'><span style='left:280px;' class='changeCount'>인원수 변경</span><span style='left:690px;' onclick='javascript:cancelReserve("+item.pNo+","+item.cnumber+")'>예약 취소</span></div>"+
	            			"</div>";
	            		
	            	}); // end of $.each(json,function(index,item){----
	            	
	            	$("div#reserveList").append(html);         
			
	            	// 더보기 value 속성에 값 지정
	            	$("button#moreList").val(Number(start)+len);
	            	
	            	// 지금까지 출력된 목록의 개수 누적 기록
	            	$("span#currentCount").text(Number($("span#currentCount").text())+json.length);
	            	
	            	// 더보기 값이 더이상 없는 경우 
	            	if($("span#totalCount").text()==$("span#currentCount").text()){
	            		$("button#moreList").text("처음으로");
	            	}
	         	}
	        },
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		});
	            	
	}// end of function reserveList(start){-------------------------

	
	// ==== function declaration =====
	function cancelReserve(pNo, cnumber){
		$.ajax({ // 1) 여행확정 상품인지 확인
			url:"<%=ctxPath%>/t1/checkProductStatus.tw",
			type:"GET",
			data:{"pNo":pNo},
			dataType: "JSON",
			success: function(json){
				if(json.n=="0"){  // 2) 여행확정 상품인 경우 취소 불가
					alert(json.pName+"은 여행확정 상품이므로 취소가 불가능합니다.");
				}
				else{ // 3) 여행확정 상품이 아닌경우 취소처리=> 트랜잭션 (고객테이블 delete, 제품테이블 update)
					
					$.ajax({
						url:"<%=ctxPath%>/t1/deleteUpdateClientAjax.tw",
						type:"POST",
						data:{"fk_pNo":pNo,"clientmobile":"${clientmobile}","cnumber":cnumber},
						dataType: "JSON",
						success: function(json){
							if(json.result==1){ // 트랜잭션처리 성공한 경우
								alert("해당 상품의 예약이 취소되었습니다.\n"+
									  "7일 이내에 환불금액이 결제하신 계좌로 입금됩니다.\n"+
									  "자세한 사항은 관리자에게 문의바랍니다.")
								window.location.reload(true);	
							}
							else{ // 트랜잭션처리 실패한 경우
								alert("시스템 오류로 예약취소에 실패했습니다. 관리자에게 문의바랍니다.");
							}
						}
					}); // end of $.ajax({------- 
				
				} // end of else------------------
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({------------- 
		
	} // end of function cancleReserve(------------------
	
			
	// 인원 수 변경 시 고객테이블 update와 제품테이블 udpate 트랜잭션처리
	function changeCount(pNo,needPayCount){
		$.ajax({
			url:"<%=ctxPath%>/t1/changeCountAjax.tw",
			type:"POST",
			data:{"fk_pNo":pNo,"clientmobile":"${clientmobile}","cnumber":needPayCount},
			dataType: "JSON",
			success: function(json){
				if(json.result!=1){ //트랜잭션처리에 실패한 경우
					alert("시스템 오류로 예약취소에 실패했습니다. 관리자에게 문의바랍니다.");
				}
			},
			error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({------- 
	}// end of function changeCount(){---------------
	
	
	// 결제 성공시 실행되는 메소드
	function goPaymentEnd(){
		alert("추가결제에 성공하셨습니다. 즐거운 여행되시기 바랍니다.");
	} // end of function goPaymentEnd(){-----------------	
		
</script>

<div id="content">
	<form name="excelFrm">
	<div style="padding-bottom:35px;"></div>
	<div id="reserveTitle"><span id="name"></span>&nbsp;님의 예약현황<input type="hidden" id="clientname" name="clientname" />
	<button type="button" id="excelBtn" style="background-color:#00b33c; color:white; margin-left:700px; width:75px; height:35px; cursor:pointer;">엑셀출력</button></div>
	</form>
	
	<div id="reserveList"></div>
	<div align="center" style="margin: 30px 0px 10px 0px;">
		<button type="button" id="moreList" value="">더보기</button>
		<span id="currentCount">0</span>&nbsp;/&nbsp;<span id="totalCount">${totalCount}</span>
	</div>
	<div style="width:70%; margin:0 auto; padding-bottom:40px;" align="right"><span id="goHome" onclick="location.href='<%=ctxPath%>/t1/travelAgency.tw'">홈으로</span></div>
</div>
