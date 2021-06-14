<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String ctxPath = request.getContextPath();
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<style>
	table#clientList{
		border:solid 0px black;
		border-collapse: collapse;
		width: 800px;
		margin-left:45px;
	}
	
	table#clientList td{
		border:solid 1px black;
		text-align: center;
	}
	
	table#clientList th{
		border:solid 1px black;
		font-weight: bolder;
		text-align: center;
	}
	
	table#headerMenu {
		border-collapse: collapse;
		width: 800px;
		margin-left:29px;
		margin-bottom:20px;
	}
	
	table#headerMenu td{
		border:solid 1px black;
		padding-left: 5px;
		text-align: center;
	}
	
	table#headerMenu th{
		border:solid 1px black;
		padding-left: 5px;
		text-align: center;
	}
	
	h4.modal-title{
		margin-left:15px;
		font-weight: bolder;
	}
	
	div.client-pageBar{
		border:solid 0px red;
		margin-top: 20px;
		margin-left: 280px;
		width: 300px;
		text-align: center; 
	}
	
	div#DepartmentMenu{
		border:solid 1px black;
	}
	
	label.period{
		width:60px;
	}
	
	span.dsubmenu{
		display: inline-block;
		width: 80px;
		margin-right: 20px;
		font-weight: bolder;
	}
	
	div.ddiv{
		margin-left:15px;
		height:50px;
		border-bottom:solid 1px #ccc;
		padding-top:10px;"
	}
	
	div#dbuttons{
		padding-top:10px;
		margin-left: 400px;
	}
	
	div#DepartmentInfo{
		width:1000px;
		border:solid 1px black;
		margin-top: 15px;
	}
	
	span.sInfo{
		display:inline-block;
	}
	
	span.sheader{
		font-weight: bolder;
	}
	
	button.ingdetail{
		font-size:8pt;
		display: inline-block;
		margin-bottom:5px;
	}
	
	div.getOneInfo{
		margin-bottom:15px;
		border-bottom: solid 1px #ccc;
	}
	
	div#pageBar{
		border:solid 0px red;
		text-align:center;
	}
	
	div#toDoTitle{
		font-weight: bolder;
		font-size: 15pt;
	}
	
	div#infoMenu{
		background-color: #94b8b8 !important;
	}
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		// 체크박스 선택 및 입력한 값 유지시키기 시작
		var periodChoice = "${paraMap.period}";
		var searchProject = "${paraMap.searchProject}";
		var searchWhoCharge = "${paraMap.searchWhoCharge}";
		var statusChoice_es = "${paraMap.statusChoice_es}";
		
		$("input:radio[name=periodChoice]").each(function(index,item){
			
			if(periodChoice == $(item).val()){
				$(item).prop("checked",true);
				return false;
			}
			
		});
		
		if(statusChoice_es!=""){
			
			var statusChoiceArr = statusChoice_es.split(",");
			
			$("input:checkbox[name=statusChoice]").each(function(index,item){
				
				for(var i=0; i<statusChoiceArr.length; i++){
					if($(item).val()==statusChoiceArr[i]){
						$(item).prop("checked",true)
						break;
					}
				}// end of for--
			})// end of each(function
		}// end of if
		
		$("input#searchProject").val(searchProject);
		$("input#searchWhoCharge").val(searchWhoCharge);
		
		// 체크박스 선택 및 입력한 값 유지시키기 끝
		
		
		
		
		
		
		// 검색하기 
		$("button#whatSearch").click(function(){

			var period = $("input:radio[name=periodChoice]:checked").val();
			
			var statusChoiceArr = new Array();
			
			$("input:checkbox[name=statusChoice]:checked").each(function(index,item){
				
				var statusChoice = $(this).val();
				statusChoiceArr.push(statusChoice);
				
			}); // end of $("input:radio[name=statusChoice]").each(function(index,item){
			
			var statusChoice_es = statusChoiceArr.join();
			
			var searchProject = $("input#searchProject").val();
			var searchWhoCharge = $("input#searchWhoCharge").val();
			
			location.href="<%= ctxPath%>/t1/departmentTodo.tw?period="+period+"&statusChoice_es="+statusChoice_es+"&searchProject="+searchProject+"&searchWhoCharge="+searchWhoCharge;

			
		});// end of $("button#whatSearch").click(function(){
			
		// 특정 행을 클릭했을때	
		$("div.getOneInfo").click(function(){
			
			var startdate = $(this).find("span.startDate").text();
			var pNo = $(this).find("input.pNo").val();
			var endDate = $(this).prop("id");
			var currentShowPageNo = "1";
			
			if(startdate=="-"){
				alert("해당업무는 시작이 되지 않았습니다. \n다른업무를 선택해주세요.");
				return;
			}
			else{
				$("div#myModal").modal();
				getOneDoInfo(pNo,endDate,currentShowPageNo);
			}
			
		}); // end of $("div#btn").click(function(){
		
			
			
		// 업무상태값이 진행중일때 메일보내기
		$(document).on("click",("span.producting"),function(){
			var clientmobile1 = $(this).parent().prev().prop("id");
			var clientmobile = clientmobile1.split("-").join("");
			var clientname = $(this).parent().prev().prev().prev().text();
			
			var fk_pNo =  $(this).parent().prev().find("input.fk_pNo").val();
			
			$.ajax({
				url:"<%= ctxPath%>/t1/sendEmailIngTodo.tw",
				data:{"clientmobile":clientmobile,
					  "fk_pNo":fk_pNo},
				type:"post",
				dataType:"json",
				success:function(json){
					
					if(json.n==0){
						alert(clientname+" 님에게 성공적으로 [여행준비물]메일을 전송했습니다.");
					}
					else{
						alert("메일전송이 실패했습니다.");
					}
				},
		    	error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
				
			}); // end of ajax
			
			
		});// end of $(document).on("click",("span.producting"),function(){
			
		// 업무상태값이 완료일때 메일보내기
		$(document).on("click",("span.productCompleted"),function(){
			var clientmobile1 = $(this).parent().prev().prop("id");
			var clientmobile = clientmobile1.split("-").join("");
			var clientname = $(this).parent().prev().prev().prev().text();
			var fk_pNo =  $(this).parent().prev().find("input.fk_pNo").val();
			
			$.ajax({
				url:"<%= ctxPath%>/t1/sendEmailIngDone.tw",
				data:{"clientmobile":clientmobile,
					  "fk_pNo":fk_pNo},
				type:"post",
				dataType:"json",
				success:function(json){
					if(json.n==0){
						alert(clientname+" 님에게 성공적으로 홍보메일을 전송했습니다.");
					}
					else{
						alert("메일전송이 실패했습니다.");
					}
				},
		    	error: function(request, status, error){
		            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }
			});
			
			
			
		}); // end of $(document).on("click",("span.productCompleted"),function(){
		
			
	})// end of $(document).ready(function(){
		
	// Function Declaration
	
	// 모달창 내용 #1
	function getOneDoInfo(pNo,endDate,currentShowPageNo){
		 
		$.ajax({
			url:"<%= ctxPath%>/t1/deptgetOneInfoheader.tw",
			type:"get",
			data:{"pNo":pNo},
			dataType:"json",
			success:function(json){
				
				var html = "";
				html+="<table id='headerMenu'>";
				html+="<th class='menuName'>프로젝트명</th>";
				html+="<th class='menuName'>현재인원수</th>"
				html+="<th class='menuName'>최소인원수</th>";
				html+="<th class='menuName'>최대인원수</th>";
				html+="<th class='menuName'>여행기간</th>";
				html+="<tr>";
				html+="<td>"+json.pname+"</td>";
				html+="<td>"+json.nowno+"</td>";
				html+="<td>"+json.minino+"</td>";
				html+="<td>"+json.maxno+"</td>";
				html+="<td>"+json.startDate+" - "+json.endDate+"("+json.period+")</td>";
				html+="</tr>";
				html+="</talbe>"
				
				$("div.modal-body").html(html);
				
				if(endDate == '-'){
					$("h4.modal-title").text("진행중");
				}
				else{
					$("h4.modal-title").text("진행완료");
				}
				
				selectClient(pNo,endDate,currentShowPageNo);
				
			},
	    	error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
		}); // end of $.ajax({ 
		
		
	}// end of function getOneDoInfo(pNo,endDate)
	
	// 부서업무 중 특정업무에 대한 페이징처리한 고객리스트 뽑아오기
	function selectClient(pNo,endDate,currentShowPageNo){
		
		$.ajax({
			url:"<%= ctxPath%>/t1/selectClient.tw",
			data:{"pNo":pNo,
				  "currentShowPageNo":currentShowPageNo},
			type:"post",
			dataType:"json",
			success:function(json){
				var html ="";
				html+="<table id='clientList'>";
				html+="<th colspan='4' style='text-align:center;'>*** 예약자명단 ***</th>";
				html+="<tr>"
				html+="<td>예약자명</td>";
				html+="<td>인원수</td>";
				html+="<td>연락처</td>";
				html+="<td></td>";
				html+="</tr>"
				
				$.each(json,function(index,item){
					
					html +="<tr>";
					html +="<td>"+item.clientname+"</td>";
					html +="<td>"+item.cnumber+"</td>";
					html +="<td id='"+item.clientmobile+"'>"+item.clientmobile+"<input class='fk_pNo' type='hidden' value='"+pNo+"'></td>";
					if(endDate == '-'){
						html +="<td><span class='producting'>메일보내기</span></td>";
					}
					else{
						html +="<td><span class='productCompleted'>메일보내기</span></td>";
					}
					html +="</tr>";
				});
				
				html +="</table>";
				$("div.client-body").html(html);
				
				// 페이징바 함수 호출
				clientPagebar(pNo,endDate,currentShowPageNo);
				
			},
	    	error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
	
		}); // end of $.ajax({
				
	}// end of function selectClient(pNo,endDate,currentShowPageNo){
		
	function clientPagebar(pNo,endDate,currentShowPageNo){
		
		$.ajax({
			url:"<%= ctxPath%>/t1/clientPagebar.tw",
			data:{"sizePerPage":"3",
				  "pNo":pNo},
			dataType:"json",
			success:function(json){
				
				if(json.totalPage>0){
					
					var totalPage = json.totalPage;
					
					var pageBarHTML = "<ul style='list-style: none;'>";
					
					var blockSize = 3;
					
					var loop = 1;
					
					if(typeof currentShowPageNo == "string"){
			        	currentShowPageNo = Number(currentShowPageNo);
			        }
					
					var pageNo = Math.floor((currentShowPageNo - 1)/blockSize) * blockSize + 1;
					
					// === [맨처음][이전] 만들기 ===       a href='javascript:selectClient("+pNo+","+endDate+",1)'
					if(pageNo != 1) {
						pageBarHTML += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='javascript:selectClient("+pNo+",\""+endDate+"\",\"1\")'>[맨처음]</a></li>";
						pageBarHTML += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='javascript:selectClient("+pNo+",\""+endDate+"\",\""+(pageNo-1)+"\")'>[이전]</a></li>";
						
					}
					
					
					while(!((loop>blockSize)||pageNo>totalPage)) {
						
						if(pageNo == currentShowPageNo) {
							pageBarHTML += "<li style='display:inline-block; width:50px; font-size:10pt; border:solid 0px gray; color:red; padding:2px 4px;'>"+pageNo+"</li>";
			            }
						else {
							pageBarHTML += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='javascript:selectClient("+pNo+",\""+endDate+"\",\""+pageNo+"\")'>"+pageNo+"</a></li>";
						}
						
						loop++;
						pageNo++;
					}// end of while() { 
					
					// === [다음][마지막] 만들기 ===
					if(pageNo <= totalPage) { // 마지막 페이지에서는 만들 필요가 없기때문에
						
						pageBarHTML += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='javascript:selectClient("+pNo+",\""+endDate+"\",\""+pageNo+"\")'>[다음]</a></li>";
						pageBarHTML += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='javascript:selectClient("+pNo+",\""+endDate+"\",\""+totalPage+"\")'>[마지막]</a></li>";
						
					}
					
					pageBarHTML += "</ul>";
		               
					$("div.client-pageBar").html(pageBarHTML);
					
					
				}// end of if(json.totalPage>0){
				
				
			},
	    	error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
			
		});
		
		
	} // end of function clientPagebar(currentShowPageNo)

</script>
<div id="content" style="border: solid 0px black; margin-left: 70px; margin-top: 50px; width:1000px;">
	<div id="toDoTitle" style="margin-top: 30px; margin-bottom: 30px;">${dname} 부서의 전체 업무현황</div>
	<div id="DepartmentMenu"  style="height:250px;">
		<form name="whatSearch">
		<div id="period" class="ddiv">
			<span class="dsubmenu">기간(기한일)</span>
			<label class="period" id="period1"><input name="periodChoice" type="radio" id="period1" value="7">1주일</label>
			<label class="period" id="period2"><input name="periodChoice" type="radio" id="period2" value="30">1개월</label>
			<label class="period" id="period3"><input name="periodChoice" type="radio" id="period3" value="90">3개월</label>
			<label class="period" id="period4"><input name="periodChoice" type="radio" id="period4" value="-1">전체</label>
		</div>
		<div id="doStatus" class="ddiv">
			<span class="dsubmenu">업무상태</span>
			<label id="status1"><input name="statusChoice" type="checkbox" id="status1" value="1">미배정</label>
			<label id="status2"><input name="statusChoice" type="checkbox" id="status2" value="2">미시작</label>
			<label id="status3"><input name="statusChoice" type="checkbox" id="status3" value="3">진행중</label>
			<label id="status4"><input name="statusChoice" type="checkbox" id="status4" value="4">보류</label>
			<label id="status5"><input name="statusChoice" type="checkbox" id="status5" value="5">지연</label>
			<label id="status6"><input name="statusChoice" type="checkbox" id="status6" value="6">완료</label>
		</div>
		<div id="projectName" class="ddiv">
			<span class="dsubmenu">프로젝트명</span>
			<input type="text" id="searchProject" name="searchProject" placeholder="내용을 입력해주세요."  />
		</div>
		<div id="whoAssigned" class="ddiv">
			<span class="dsubmenu">담당자명</span>
			<input type="text" id="searchWhoCharge" name="searchWhoCharge" placeholder="내용을 입력해주세요."  />
		</div>
		<div id="dbuttons">	
		<button type="button" id="whatSearch">검색</button>
		<button type="reset">초기화</button>
		</div>
		</form> 
	</div>
	
	
	<div id="DepartmentInfo">
		<div id="infoMenu" style="padding-left:9px; margin-bottom:6px; border-bottom:solid 1px #ccc;">
			<span class="sInfo sheader" style="width:50px;">순번</span>
			<span class="sInfo sheader" style="width:240px;">프로젝트명</span>
			<span class="sInfo sheader" style="width:100px;">배정자</span>
			<span class="sInfo sheader" style="width:100px;">배정일</span>
			<span class="sInfo sheader" style="width:100px;">시작일</span>
			<span class="sInfo sheader" style="width:100px;">기한일</span>
			<span class="sInfo sheader" style="width:100px;">완료일</span>
			<span class="sInfo sheader" style="width:70px;">담당자</span>
			<span class="sInfo sheader" style="width:70px;">상태</span>
		</div>
	
		
		<div id="infoContent" style="margin-left:10px;">
			<c:if test="${not empty productList}">
				<c:forEach var="product" items="${productList}">
					<div class="getOneInfo" id="${product.endDate}">
					<input type="hidden" class="pNo" value="${product.pNo}"/>
					<span class="sInfo" style="width:50px; padding-left:8px;">${product.rno}</span>
					<c:if test="${product.hurryno eq 1}">
						<span class="sInfo" style="width:240px; color:red;">${product.pName}</span>
					</c:if>
					<c:if test="${product.hurryno eq 0}">
						<span class="sInfo" style="width:240px;">${product.pName}</span>
					</c:if>
					<span class="sInfo" style="width:100px;">${product.name}</span>
					<span class="sInfo" style="width:100px;">${product.assignDate}</span>
					<span class="startDate sInfo" style="width:100px;">${product.startDate}</span>
					<span class="sInfo" style="width:100px;">${product.dueDate}</span>
					<span class="sInfo" style="width:100px;">${product.endDate}</span>
					<span class="sInfo" style="width:70px;">${product.employeeName}</span>
					<c:choose>
						<c:when test="${product.ingdetail eq '0'}"><button class="ingdetail">진행중</button></c:when>
						<c:when test="${product.ingdetail eq '-1'}"><button class="ingdetail">보류</button></c:when>
						<c:when test="${product.startDate == '-'}">
							<c:if test="${product.assignDate == '-'}">
								<button class="ingdetail">미배정</button>
							</c:if>
							<c:if test="${product.assignDate != '-'}">
								<button class="ingdetail">미시작</button>
							</c:if>
						</c:when>
						<c:when test="${product.endDate != '-'}"><button class="ingdetail">완료</button></c:when>
						<c:otherwise><button class="ingdetail">지연</button></c:otherwise>
					</c:choose>
					</div>
				</c:forEach>
			</c:if>
			<c:if test="${empty productList}">
				${loginuser.name}님의 부서업무가 존재하지 않습니다.
			</c:if>
		</div>
		
		<div id="pageBar">${pageBar}</div>
	</div>

   <!-- Modal -->
   <div class="modal fade" id="myModal" role="dialog">
     <div class="modal-dialog modal-lg">
       <div class="modal-content">
         <div class="modal-header">
           <button type="button" class="close" data-dismiss="modal">&times;</button>
           <h4 class="modal-title"></h4>
         </div>
         <div class="modal-body">
         </div>
         <div class="client-body">
         </div>
         <div class="client-pageBar">
         </div>
         
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">닫기</button>
        </div>
      </div>
    </div>
  </div>
</div>