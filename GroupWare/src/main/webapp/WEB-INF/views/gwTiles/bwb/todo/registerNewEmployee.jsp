<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<%
	String ctxPath = request.getContextPath();
%>    
<style>
	
	
	table#newInfo {
		margin-left: 200px;
		border:solid 0px black;
		margin-top:100px;
		width: 1150px;
		height: 150px;
		font-size:13pt;
		text-align: center;
		border-collapse: collapse;
		
	}
	
	table#newInfo td {
		border:solid 1px black;
		vertical-align: middle;
		border-collapse: collapse;
	}
	
	td.InfoMenu {
		font-weight: bolder;
		background-color: #e9e9ed;
	}
	
	div#button{
		margin-top: 15px;
		margin-left: 1170px;
		margin-bottom: 30px;
	}
	
	button.buttons{
		width: 90px;
		height: 40px;
	}
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		
		var fk_pcode = $("select#fk_pcode").val();
		
		// 문서 로딩 시 연차값 넣어주기
		$.ajax({
			url:"<%= request.getContextPath()%>/t1/selectOffCnt.tw",
			type:"get",
			data:{"fk_pcode":fk_pcode},
			dataType:"json",
			success:function(json){
				$("input#offcnt").val(json.offcnt);
				
			},
			error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }				
		});
		
		// 문서 로딩시 직무 내용 넣어주기
		var dname = $("select#fk_dcode option:selected").text()

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
		
		
		// 부서 선택 시 직무 자동완성 되도록 하는 기능	
		$("select#fk_dcode").change(function(){
			
			var dname = $("select#fk_dcode option:selected").text()

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
			
		// 직위 선택 시 연차 자동완성 되도록 하는 기능	
		$("select#fk_pcode").change(function(){
			
			var fk_pcode = $("select#fk_pcode").val();
			
			$.ajax({
				url:"<%= request.getContextPath()%>/t1/selectOffCnt.tw",
				type:"get",
				data:{"fk_pcode":fk_pcode},
				dataType:"json",
				success:function(json){
					$("input#offcnt").val(json.offcnt);
					
				},
				error: function(request, status, error){
		        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		        }				
			});

		});	// end of $("select#dname").change(function(){	
		
			
			
		// 등록완료 버튼 클릭 시
		$("button#newPerson").click(function(){

			var employeeid = $("input#employeeid").val();
			var email = $("input#email").val();
			var name = $("input#name").val();
			var cmobile1 = $("input#cmobile1").val();
			var cmobile2 = $("input#cmobile2").val();
			var cmobile3 = $("input#cmobile3").val();
			var mobile1 = $("input#mobile1").val();
			var mobile2 = $("input#mobile2").val();
			var mobile3 = $("input#mobile3").val();
			var fk_dcode = $("select#fk_dcode").val();
			var dname = $("select#fk_dcode option:selected").text();
			var fk_pcode = $("select#fk_pcode").val();
			var pname = $("select#fk_pcode option:selected").text();
			
			// 이메일 유효성 검사
			regExp = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
			
			var bemail = regExp.test(email);
			
			if(name.trim()!="" && cmobile1.trim()!="" && cmobile2.trim()!="" && cmobile3.trim()!="" && 
					mobile1.trim()!="" && mobile2.trim()!="" && mobile3.trim()!="" && bemail){
				
				var bool = confirm(name+"님을 "+dname+"부서"+" "+pname+"직위로 등록하시겠습니까?");
				
				if(bool){
					
					if($("input#attach").val() == ""){
				    	registerOne_noAttach();
				    }else{
				    	registerOne_withAttach();
				    }
					
					
				}// end of if(bool)
			}// end of if(~~!="" && bemail){)
			else {
				alert("직원정보를 올바르게 입력해주세요.");
				location.href="javascript:history.go(0)";
				return;
			}
			
		});// end of $("button#goUpdate").click(function(){	
			
			
	}); // end of $(document).ready(function(){
	
		
		
	//Function Declaration
	
	// 파일첨부가 있을때의 회원가입
	function registerOne_withAttach(){
		
		var form_data = $("form[name=newPerson]").serialize();
		
		$("form[name=newPerson]").ajaxForm({
			url:"<%= request.getContextPath()%>/t1/registerOne_withAttach.tw",
			type:"post",
			enctype:"multipart/form-data",
			data:form_data,
			dataType:"json",
			success:function(json){
				// 정상적으로 update됐을 경우
				if(json.n == 1){
					
					alert("직원정보 등록이 완료 되었습니다. \n 주소록에서 확인해주세요");
					location.href="javascript:history.go(0)";
				}
				
			},
			error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }	
		});// end of $.ajaxForm({ 
			
		$("form[name=newPerson]").submit();
		
	}// end of function goRegister_withAttach(){
		
	// 파일첨부가 없을때의 회원가입
	function registerOne_noAttach(){
		alert("ㅇㅇ");
		$.ajax({
			url:"<%= request.getContextPath()%>/t1/registerOne_noAttach.tw",
			type:"post",
			data:{"employeeid":$("input#employeeid").val(),
				  "email":$("input#email").val(),
				  "jubun1":$("input#jubun1").val(),
				  "jubun2":$("input#jubun2").val(),
				  "name":$("input#name").val(),
				  "cmobile1":$("input#cmobile1").val(),
				  "cmobile2":$("input#cmobile2").val(),
				  "cmobile3":$("input#cmobile3").val(),
				  "mobile1":$("input#mobile1").val(),
				  "mobile2":$("input#mobile2").val(),
				  "mobile3":$("input#mobile3").val(),
				  "fk_dcode":$("select#fk_dcode").val(),
				  "fk_pcode":$("select#fk_pcode").val(),
				  },
			dataType:"json",
			success:function(json){

				if(json.n == 1){
					
					alert("직원정보 등록이 완료 되었습니다. \n 주소록에서 확인해주세요");
					location.href="javascript:history.go(0)";
				}
			},
			error: function(request, status, error){
	        	alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }	
			
			
		}); // end of ajax
		
	}// end of function goRegister_noAttach(){	
		
		
		
		
</script>

<div id="content">

	<div id="sebuInfo2">
		<form name="newPerson">
		<span style="position:relative; top:80px; left:200px; font-size:15pt; font-weigth:bolder;">&#60; T1WORKS 신입사원등록 &#62;</span>
   		<table id="newInfo">
  			<tbody>
   			<tr>
   				<td rowspan="3" style="width:100px !important; padding-left:-30px;"> <input type="file" name="attach" id="attach" style="width:150px; font-size:13px !important;"  /> </td> 
   				<td style="width:80px" class="InfoMenu">사번</td>
   				<td style="width:160px"> <input type="text" id="employeeid" name="employeeid" style="width:110px" /> </td>
   				<td style="width:110px" class="InfoMenu">이메일</td>
   				<td><input type="email" id="email" name="email" style="width:230px" /></td>
   				<td style="width:100px" class="InfoMenu">주민번호</td>
   				<td colspan="3">
   				<input type="text" id="jubun1" name="jubun1" style="width:80px" /> - <input type="password" id="jubun2" name="jubun2" style="width:80px" />
   				</td>
   			</tr>
   			<tr> 
   				<td class="InfoMenu">성명</td>
   				<td><input type="text" id="name" name="name" style="width:110px" /></td>
   				<td class="InfoMenu">전화번호</td>
   				<td>
   				<input type="text" id="cmobile1" name="cmobile1" style="width:50px"/> -
   				<input type="text" id="cmobile2" name="cmobile2" style="width:60px"/> -
   				<input type="text" id="cmobile3" name="cmobile3" style="width:60px"/>
   				</td>
   				<td class="InfoMenu">핸드폰</td>
   				<td colspan="3">
   				<input type="text" id="mobile1" name="mobile1" style="width:60px"/> -
   				<input type="text" id="mobile2" name="mobile2" style="width:70px"/> -
   				<input type="text" id="mobile3" name="mobile3" style="width:70px"/>
   				</td>
   			</tr>
   			<tr> 
   				<td class="InfoMenu">소속</td>
   				<td>
   					<select id="fk_dcode" name="fk_dcode">
   						<c:forEach var="Department" items="${departmentList}">
   							<option value="${Department.dcode}">${Department.dname}</option>
   						</c:forEach>
   					</select>
   				</td>
   				<td class="InfoMenu">직위</td>
   				<td>
   					<select id="fk_pcode" name=fk_pcode>
   						<c:forEach var="position" items="${positionList}">
   							<option value="${position.fk_pcode}">${position.pname}</option>
   						</c:forEach>
   					</select>
   				</td>
   				<td class="InfoMenu">총연차</td>
   				<td style="width:50px"><input type="text" id="offcnt" name="offcnt" readonly="readonly" style="width:30px" /></td>
   				<td style="width:50px" class="InfoMenu">직무</td>
   				<td><input type="text" id="duty" name="duty" readonly="readonly" style="width:110px"/></td>
   			</tr>
  			</tbody>
   		</table>
   		<div id="button">
   			<button type="button" id="newPerson" class="buttons">등록완료</button>
   			<button type="reset" class="buttons">취소</button>
   		</div>
		</form>
	</div>

</div>