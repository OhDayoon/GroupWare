<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
   String ctxPath = request.getContextPath();
%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>T1Works GroupWare</title>
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Noto+Serif+KR:wght@200&display=swap" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<style type="text/css">
body {font-family: 'Noto Serif KR', serif; background-color: #e6f2ff; margin: 0;}
div.content-width{ width: 60%; }
#content-container{
   height: 550px;
   border-top: solid 3px #0071bd;
   margin-left: auto;
   margin-right: auto;
   margin-top: 6%;
   background-color: #ffffff;
   box-shadow: 0px 1px 5px 2px #ccc;
}
/* 로그인 폼 */
div#loginform{
   border: solid 1px blue;
   display: inline-block;
   
}
input#employeeid, input#passwd{
   margin-bottom: 10px;
   padding:10px;
   width: 276px;
   
}
button.submitbtn{
   width: 300px;
   height: 40px;
   background-color: #0071bd;
   color: white;
   border: none;
   border-radius: 3px;
}
button.submitbtn:hover{ cursor: pointer; }
img#logo{
width:300px;
height: auto;
display:block;
margin: 60px auto;
}

@media (max-width: 1540px){
	div#content-container{
	height: 28em; 
	}
}

</style>
<script type="text/javascript">
   $(document).ready(function(){
      
      <%-- 로그인 유효성 검사 추가 --%>
      $("button#login").click(function(){
         
         var employeeid = $("input#employeeid").val().trim();
         var passwd = $("input#passwd").val().trim();
   
         if(employeeid!="" && passwd!=""){
            
            var frm = document.loginFrm;
            frm.method="post";
            frm.action="<%=ctxPath %>/t1/home.tw";
            frm.submit();
         }
         else{
            alert("아이디와 암호를 올바르게 입력해주세요");
         }
      });// end of $("button#login").click(function(){ 
      
    	  
       // 엔터시에도 로그인
       $("input#passwd").keyup(function(event){
    	  
    	   if(event.keyCode == 13){
    		     var employeeid = $("input#employeeid").val().trim();
    	         var passwd = $("input#passwd").val().trim();
    	   
    	         if(employeeid!="" && passwd!=""){
    	            
    	            var frm = document.loginFrm;
    	            frm.method="post";
    	            frm.action="<%=ctxPath %>/t1/home.tw";
    	            frm.submit();
    	         }
    	         else{
    	            alert("아이디와 암호를 올바르게 입력해주세요");
    	         }
    	   }
    	   
       });
       
    	  
   }); // end of $(document).ready(function(){
      
   
</script>
</head>

<body>

<div id="content-container" class="content-width" style="height: 35em;">


<img id="logo" src="<%=request.getContextPath()%>/resources/images/login/t1works_logo_reverse.jpg" />

<div style="display: block; width: 300px; font-weight: bold; margin: 0 auto;">
<p>환영합니다. T1Works 그룹웨어입니다.<br>
사용하시려면 로그인해주세요.
</div>

<div style="width: 300px; margin: 40px auto 0 auto;">
   <form name="loginFrm">
      <input type="text" id="employeeid" name="employeeid" placeholder="사원번호" /><br>
      <input type="password" id="passwd" name="passwd" placeholder="비밀번호" />
   </form> 
   <button type="button" id="login" class="submitbtn">로그인</button>
</div>

</div>


<div style="display: block; width: 402px; margin: 20px auto 0px auto;">

<span style="display: inline-block; text-align: center; font-size: 10pt; width: 400px; line-height: 35px;">
<img src="<%=request.getContextPath()%>/resources/images/login/t1works_icon.png" style="height: 30px; width: auto; vertical-align: middle;" />
(주)티원웍스 copyright &copy; T1WORKS, all rights reserved.</span>
</div>
<div id="footer" style="background-color: #0071bd; margin: 60px 0px 0px 0px; width: 100%; height: 150px;">

<img src="<%=request.getContextPath()%>/resources/images/login/t1works_logo.jpg" style="width: 200px; height: auto; margin: 30px auto 0 auto; display: block;" />

</div>


</body>
</html>