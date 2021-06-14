<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">

	div#gwContent{left: 80px;}
	
	div#employeeMap {
      background-color: #0071bd;
      color: #fff;
      font-weight: bold;
   }
   
   div#employeeMapTitle{
      border: solid 0px red;
      margin: 30px 0px 30px 50px;
      font-size: 22pt;
      font-weight: bold;
   }
   
   div#employeeBox{
      border: solid 0px red;
      background-color: #e9e9ed;
      margin: 0px 0px 30px 50px;
      float: left;
      width: 25%;
      padding: 0px 20px;
   }
   
   div.companyName{
      font-size: 15pt;
      padding: 15px 0px;
   }
   
   div.clickMenu{
      border-top: solid 2px #cfcfcf;
      font-size: 15pt;
      padding: 15px 0px;
   }
   
   div#allBt {
      float: left;
      margin-bottom: 20px;
      border: solid 0px red;
      margin: 20px 0px 20px 50px;
   }
   
   div#allBt button{
      width: 175px;
      height: 30px;
      margin-left: 20px;
      background-color: #5e5e5e;
      color:#fff;
      border: none;
   }
   
   span.plus, span.minus{
      display: inline-block;
      width: 30px;
   }
   
   div#employeeMapContainer li{
      line-height: 30px;
   
   }
   
   div#employeeMapContainer ul{
      list-style-type: none;
   }
   
   
   table#employeeListTable tr.employeeListTr{
      cursor:pointer;
      height: 50px;
   }
   
   table#employeeListTable tr{
      border-top: solid 1px #696969t;
      border-bottom: solid 1px #696969;
      text-align: center;
   }
   
   table#employeeListTable tr.employeeListTr:hover{
      background-color: #0071bd;
      color: #fff;
   }
   
   ul.hideInfo li:hover{
      color: #0071bd;
      font-weight: bold;
   }
   
   div#employeeMapA{
      margin-top:20px;
      margin-bottom: 50px; 
      font-size:13pt;
   }
   
   div#employeeMapA a{
      text-decoration: none;
      color: #696969;
   }

   table#oneEmployeeTable{
      width: 95%;
      margin: 0 auto;
   }

   table#oneEmployeeTable th, table#oneEmployeeTable td {
      border: solid 1px #696969;
      border-collapse: collapse;
      height: 50px;
      vertical-align: middle;
   }
   
   table#oneEmployeeTable th {text-align: center;}
   table#oneEmployeeTable td {padding-left: 30px;}
      

   
   div#gwContent {overflow: hidden;}
   
   table#oneInfo,#oneInfoUpdate{
   		border : solid 0px black;
   		border-collapse: collapse;
   		width:1000px;
   		height: 150px;
   		margin: 0 auto;
   		font-size: 12pt;
   }
	
   
   table#oneInfo td, table#oneInfoUpdate td {
   		border : solid 1px black;
   		border-collapse: collapse;
   		vertical-align: middle;
   }
   
   
   td.oneInfoMenu {
   		font-weight: bolder;
   		text-align: center;
   		background-color: #E9E9ED;
   }
   
   input.updateInfo {
   		border: solid 1px black;
   		font-size:16px;
   		width:180px;
   }
   
   select.updateInfo {
   		font-size:15px;
   		width:100px;
   }
   	
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		$("ul.hideInfo").hide();
		$("div.hideDeptInfo").hide();
		$("span.minus").hide();
		
		
		// searchType과 searchWord가 존재하는 경우
		if("${searchType}"!=""){
			$("select#searchType").val("${searchType}");
		}
		if("${searchWord}"!=""){
			 $("input#searchWord").val("${searchWord}")
		}
		
		
		// 회사명 클릭이벤트
		$("div.companyName").click(function(){
			if($("div.hideDeptInfo").css('display')=='none'){
				$("div.hideDeptInfo").show(500);
				$(this).next().show(500);
				$(this).find("span.minus").show();
				$(this).find("span.plus").hide();
				
			}
			else{
				$("div.hideDeptInfo").hide();
				$(this).next().hide();
				$("span.minus").hide();
				$("span.plus").show();
				$("ul.hideInfo").hide();
				
			}
		});	// end of $("div.companyName").click(function(){-----
		
		
		// 각부서명 클릭이벤트
		$("div.clickMenu").click(function(){
			if($(this).next().css('display')=='none'){
				$(this).next().show(500);
				$(this).find("span.minus").show();
				$(this).find("span.plus").hide();
			}
			else{
				$(this).next().hide();
				$(this).find("span.minus").hide();
				$(this).find("span.plus").show();
			}
		}); // end of $("div.clickMenu").click(function(){-----

		
		// 엔터눌러도 직원검색가능하도록 이벤트 설정
		$("input#searchWord").keydown(function(event){
			 if(event.keyCode==13){ 
				 goSearchEmployee();
			  }
		});	// end of $("input#searchWord").keydown(function(event){----
			
		
		$("div#oneInfoUpdate").hide();
		
		// 인사관리 버튼 클릭 시 
		$("button#goInfoUpdate").click(function(){
			
			if($("td#employeeid").text()!=""){
				$("div#oneInfoUpdate").show();
				$("input#email").val($("td#email").text());
				$("input#employeeid").val($("td#employeeid").text());
				$("input#name").val($("td#name").text());
				$("input#cmobile").val($("td#cmobile").text());
				$("input#mobile").val($("td#mobile").text());
				$("input#duty").val($("td#duty").text());
				

			}
			else{
				alert("특정회원을 먼저 클릭해 주세요");
			}
		}); // end of $("button#goInfoUpdate").click(function(){
		
		// 부서 변경 시 직무 자동완성 되도록 하는 기능	
		$("select#dname").change(function(){
			
			var dname = $("select#dname").val()

			$.ajax({
				url:"<%= request.getContextPath()%>/t1/selectDuty.tw",
				type:"get",
				data:{"dname":dname},
				dataType:"json",
				success:function(json){
					$("input#duty").val(json.duty);
					
				},
				error: function(request, status, error){
		        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }				
			});

		});	// end of $("select#dname").change(function(){
		
		// 수정완료 버튼 클릭 시
		$("button#goUpdate").click(function(){
			
			var employeeid = $("input#employeeid").val();
			var email = $("input#email").val();
			var name = $("input#name").val();
			var cmobile = $("input#cmobile").val();
			var mobile = $("input#mobile").val();
			var dname = $("select#dname").val();
			var pname = $("select#pname").val();
			
			// console.log("employeeid :"+employeeid );
			// 이메일 유효성 검사
			regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			
			var bemail = regExp.test(email);
			
			if(name.trim()!="" && cmobile.trim()!="" && mobile.trim()!="" && bemail){
				
				var form_data = $("form[name=goUpdate]").serialize();
				
				$.ajax({
					url:"<%= request.getContextPath()%>/t1/updateOneInfo.tw",
					type:"post",
					data:form_data,
					dataType:"json",
					success:function(json){
						// 정상적으로 update됐을 경우
						if(json.n == 1){
							
						alert("직원정보 변경이 완료 되었습니다.");
						location.href="javascript:history.go(0)";
						$("div#oneInfoUpdate").hide();
						}
						
					},
					error: function(request, status, error){
			        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }	
				});// end of $.ajax({ 

			}
			else {
				alert("정보를 올바르게 입력해주세요");
				return;
			}
			
			
			
		});// end of $("button#goUpdate").click(function(){
		
			
		
	}); // end of $(document).ready(function(){
		
// ========= function declaration =========
	
	// 전체폴더 열기
	function showAll(){
		$("span.minus").show();
		$("span.plus").hide();
		$("ul.hideInfo").show();
		$("div.hideDeptInfo").show(500); 
		
	}// end of function showAll(){-----
		
	// 전체폴더 닫기
	function hideAll(){
		$("span.minus").hide();
		$("span.plus").show();
		$("ul.hideInfo").hide();
		$("div.hideDeptInfo").hide();
		
	}// end of function hideAll(){-------
		
	// 직원검색기능	
	function goSearchEmployee(){
		
		var searchType= $("select#searchType").val();
		var searchWord= $("input#searchWord").val().trim();
		
		if(searchWord!="" && searchType==""){ // 검색어는 입력했지만 검색조건을 선택하지 않은 경우
			alert("검색조건을 선택해 주세요.");
			return;
		}
		
		location.href="<%=ctxPath%>/t1/personnelManage.tw?searchType="+searchType+"&searchWord="+searchWord;	
	
	} // end of function goSearchEmployee(){------- 	
	
		
	// 특정직원 정보 조회	
	function getOneEmployeeInfo(employeeid){
		 
		$("div#oneInfoUpdate").hide();
		
		$.ajax({
	    	url:"<%=ctxPath%>/t1/employeeInfoAjaxHsy.tw",
	   		type:"POST",
	   		data:{"employeeid":employeeid},
	   		dataType:"json",
	   		success:function(json){
				
	   			$("td#employeeid").text(json.employeeid);
	   			$("td#email").text(json.email);
	   			$("td#name").text(json.name);
	   			$("td#cmobile").text(json.cmobile);
	   			$("td#mobile").text(json.mobile);
	   			$("td#dname").text(json.dname);
	   			$("td#pname").text(json.pname);
	   			$("td#duty").text(json.duty);
	   			$("img#employeesimg").prop("src","<%= ctxPath%>/resources/images/bwb/"+json.fileName);

	   		},
	   		error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
	   }); // end of ajax-----------------------------
	   
	} // end of function getOneEmployeeInfo(employeeid){---------
	

		
</script>

<div id="content">
	<div id="sebuInfo1">
		
	<div id="employeeMapContainer" style="border:solid 0px red;">
      <div id="employeeMapTitle">T1Works 인사관리</div>
      
      <div id="allBt">
         <button type="button" style="margin-left:0px;" onclick='javascript:showAll();'>⊕&nbsp;&nbsp;전체폴더열기</button>
         <button type="button" onclick='javascript:hideAll();'>⊖&nbsp;&nbsp;전체폴더닫기</button>
      </div>
      
      <div style="float:right; margin-right: 400px;">
         <select id="searchType" style="height: 30px; width: 120px; cursor:pointer;">
            <option value="">&nbsp;&nbsp;선택하세요</option>
            <option value="name">&nbsp;&nbsp;직원명</option>
            <option value="dname">&nbsp;&nbsp;소속</option>
            <option value="pname">&nbsp;&nbsp;직위</option>
            <option value="email">&nbsp;&nbsp;이메일</option>
         </select>
         <input type="text" id="searchWord" placeholder="   검색어를 입력하세요." style="height: 30px; width: 230px;" />
         <button type="button" onclick="javascript:goSearchEmployee();" style="height: 30px; background-color: #5e5e5e; color:#fff; border:none;">검색</button>
      </div>
      
      <div style="clear:both;"></div>
      
      <div id="employeeBox" style="cursor:pointer;">
         <div class="companyName">
            <span class="plus" style="color:#262626; font-weight:bold;">▷</span>
            <span class="minus" style="color:#5e5e5e; font-weight:bold;">◢</span>
            T1Works
         </div>
         <c:forEach var="memberVO" items="${employeeList}">
            <ul class="hideInfo">
            <c:if test="${memberVO.fk_pcode eq '4'}">
               <li id="${memberVO.employeeid}">${memberVO.name} 사장</li>
            </c:if>
            </ul>
         </c:forEach>
         
         <c:forEach var="departmentVO" items="${departmentList}">
            <div class="clickMenu hideDeptInfo">
               <span class="plus" style="color:#262626; font-weight:bold;">▷</span>
               <span class="minus" style="color:#5e5e5e; font-weight:bold;">◢</span>
               ${departmentVO.dname}
            </div>
            <ul class="hideInfo">
            <c:forEach var="memberVO" items="${employeeList}">
               <c:if test="${departmentVO.dcode eq memberVO.fk_dcode}">
                  <li id="${memberVO.employeeid}">${memberVO.name}
                     <c:if test="${memberVO.fk_pcode eq '1'}">사원</c:if>
                     <c:if test="${memberVO.fk_pcode eq '2'}">대리</c:if>
                     <c:if test="${memberVO.fk_pcode eq '3'}">부장</c:if>
                  </li>
               </c:if>
            </c:forEach>
            </ul>
         </c:forEach>
         
      </div>
      
      <div style="border: solid 0px red; float:right; width: 700px; margin-right:400px;">
         <div style="margin-bottom:20px;"><h4>직원목록</h4></div>
         <table id="employeeListTable">
            <tr style="height:50px; background-color: #e9e9ed; font-weight: bold;">
               <th style="width: 100px; text-align: center;">사번</th>
               <th style="width: 100px; text-align: center;">직원명</th>
               <th style="width: 90px; text-align: center;">직위</th>
               <th style="width: 100px; text-align: center;">소속</th>
               <th style="width: 150px; text-align: center;">전화번호</th>
               <th style="width: 150px; text-align: center;">연락처</th>
               <th style="width: 170px; text-align: center;">이메일</th>
            </tr>
            <c:if test="${not empty pagingEmployeeList}">
               <c:forEach var="memberVO" items="${pagingEmployeeList}">
                  <tr class="employeeListTr" id="${memberVO.employeeid}" onclick="getOneEmployeeInfo('${memberVO.employeeid}');">
                     <td>${memberVO.employeeid}</td>
                     <td>${memberVO.name}</td>
                     <td>${memberVO.pname}</td>
                     <td>${memberVO.dname}</td>
                     <td>${memberVO.mobile}</td>
                     <td>${memberVO.cmobile}</td>
                     <td>${memberVO.email}</td>
                  </tr>
               </c:forEach>
            </c:if>
            <c:if test="${empty pagingEmployeeList}">
               <tr><td colspan="7" style="height: 50px;">검색조건에 일치하는 결과가 없습니다.</td></tr>
            </c:if>
         </table>
         <div id="employeeMapA" align="center">${pageBar}</div>
      </div>
  	  </div>
  	  
  	  <div id="oneInfo" style="clear:both; margin-bottom:100px; margin-left:350px;">
  	  		<span style="margin-left:260px; margin-bottom:15px; font-size:15pt; font-weight:bolder;">&#60;수정전&#62;</span>
      		<table id="oneInfo">
      			<tbody>
	      			<tr> 
	      				<td rowspan="3" style="width:130px"><img id="employeesimg" style="height:149px; width:130px;"></td>
	      				<td class="oneInfoMenu" style="width:100px;">사번</td>
	      				<td id="employeeid" style="padding-left:15px; width:130px;"></td>
	      				<td class="oneInfoMenu" style="width:100px;">이메일</td>
	      				<td id="email" colspan="2" style="padding-left:15px;"></td>
	      				<td style="width:200px;"><button id="goInfoUpdate" style="margin-left:40px;">인사관리</button></td>
	      			</tr>
	      			<tr> 
	      				<td class="oneInfoMenu">성명</td>
	      				<td id="name" style="padding-left:15px;"></td>
	      				<td class="oneInfoMenu">전화번호</td>
	      				<td id="cmobile" style="padding-left:15px;"></td>
	      				<td class="oneInfoMenu" style="width:150px;">핸드폰</td>
	      				<td id="mobile" style="padding-left:15px;"></td>
	      			</tr>
	      			<tr> 
	      				<td class="oneInfoMenu">소속</td>
	      				<td id="dname" style="padding-left:15px;"></td>
	      				<td class="oneInfoMenu">직위</td>
	      				<td id="pname" style="padding-left:15px;"></td>
	      				<td class="oneInfoMenu">직무</td>
	      				<td id="duty" style="padding-left:15px;"></td>
	      			</tr>
      			</tbody>
      		</table>
      </div>
      	<form name="goUpdate">
      	<div id="oneInfoUpdate" style="margin-bottom:150px; margin-left:600px;">
      		<span style="margin-left:10px; margin-bottom:15px; font-size:15pt; font-weight:bolder;">&#60;수정후&#62;</span>		
      		<table id="oneInfoUpdate">
      			<tbody>
	      			<tr> 
	      				<td rowspan="3" style="width:130px"> <img id="employeesimg" style="height:140px; width:130px;"> </td>
	      				<td class="oneInfoMenu" style="width:100px;">사번</td>
	      				<td style="padding-left:7px;"> <input id="employeeid" name="employeeid" class="updateInfo" type="text" readonly="readonly" style="width:120px;" /> </td>
	      				<td class="oneInfoMenu" style="width:100px;">이메일</td>
	      				<td colspan="3" style="padding-left:7px;"> <input type="email" required size="20" maxlength="20" id="email" name="email" class="updateInfo" type="text" /> </td>
	      			</tr>
	      			<tr> 
	      				<td class="oneInfoMenu">성명</td>
	      				<td style="padding-left:7px;"><input id="name" name="name" class="updateInfo" type="text" style="width:120px;" /></td>
	      				<td class="oneInfoMenu">전화번호</td>
	      				<td style="padding-left:7px;"><input id="cmobile" name="cmobile" class="updateInfo" type="text"/></td>
	      				<td class="oneInfoMenu" style="width:150px;">핸드폰</td>
	      				<td style="padding-left:7px;"><input id="mobile" name="mobile" class="updateInfo" type="text"/></td>
	      			</tr>
	      			<tr> 
	      				<td class="oneInfoMenu">소속</td>
	      				<td style="padding-left:7px;">
	      					<select id="fk_dcode" name="fk_dcode" class="updateInfo">
	      						<c:forEach var="Department" items="${departmentList}">
	      							<option value="${Department.dcode}">${Department.dname}</option>
	      						</c:forEach>
	      					</select>
	      				</td>
	      				<td class="oneInfoMenu">직위</td>
	      				<td style="padding-left:7px;">
	      					<select id="fk_pcode" name="fk_pcode" class="updateInfo">
	      						<c:forEach var="position" items="${positionList}">
	      							<option value="${position.fk_pcode}">${position.pname}</option>
	      						</c:forEach>
	      					</select>
	      				</td>
	      				<td class="oneInfoMenu">직무</td>
	      				<td style="padding-left:7px;"><input id="duty" name="duty" class="updateInfo" type="text" readonly="readonly"/></td>
	      			</tr>
      			</tbody>
      		</table>
      		<div align="right">
	      		<button id="goUpdate" type="button" style="margin-top:15px;">수정완료</button>
	      		<button type="reset">취소</button>
      		</div>
    	 </div>
    	 </form>    
      </div>
  	  
</div>