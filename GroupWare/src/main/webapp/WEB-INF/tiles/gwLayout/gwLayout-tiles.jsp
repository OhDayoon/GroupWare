<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<% String ctxPath = request.getContextPath();%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>T1Works 그룹웨어</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- JQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<!-- CSS -->
<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/resources/css/gwStyle.css" />
<link href="<%=ctxPath%>/resources/css/kdn/app.css" rel="stylesheet" type="text/css">
<!-- Font Awesome -->
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css" integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
<!-- Bootstrap -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- JavaScript -->
<script src="<%=ctxPath%>/resources/js/kdn/jquery.ssd-vertical-navigation.min.js"></script>
<script src="<%=ctxPath%>/resources/js/kdn/app.js"></script>
<script src="<%=ctxPath%>/resources/js/kdn/header_sideinfo.js"></script>
<script type="text/javascript" src="<%=ctxPath%>/resources/smarteditor/js/HuskyEZCreator.js" charset="utf-8"></script>
<!-- Google Font -->
<link rel="preconnect" href="https://fonts.gstatic.com">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300&display=swap" rel="stylesheet">

<%--  =====  스피너를 사용하기 위해  jquery-ui 사용하기 ===== --%>
  <link rel="stylesheet" type="text/css" href="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.css" />
  <script type="text/javascript" src="<%= ctxPath%>/resources/jquery-ui-1.11.4.custom/jquery-ui.js"></script>
  
<%-- *** ajax로 파일을 업로드할때 가장 널리 사용하는 방법 ==> ajaxForm *** --%>
<script type="text/javascript" src="<%= ctxPath%>/resources/js/jquery.form.min.js"></script>
<style type="text/css">
body{
	font-family: 'Noto Sans KR', sans-serif;
}

</style>


</head>
<body>
<div id="body-container">
      <tiles:insertAttribute name="header" />
   <div id="sideinfo-content-container">
      <tiles:insertAttribute name="content" />
      <div id="sideinfo">
         <tiles:insertAttribute name="sideinfo" />     
     </div>
    </div>     
  </div>
    <%-- (디자인은 gwStyle.css에 있음) --%>
</body>
</html>
  
