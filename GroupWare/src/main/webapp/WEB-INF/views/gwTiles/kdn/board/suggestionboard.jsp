<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix='c' uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix='fn' uri="http://java.sun.com/jsp/jstl/functions" %>
<% String ctxPath = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/resources/css/kdn/board.css"/>

<script type="text/javascript">
$(document).ready(function(){
	
	$("input#searchWord").keydown(function(){
		if(event.keyCode == 13){
			goSearch();
		}
	});
	
	 $("select#sizePerPage").change(function(){
			goSearch();
		});
	
   
 	//보기개수 선택시 선택값 유지시키기
	if(${not empty requestScope.paraMap}){
		$("select#searchType").val("${requestScope.paraMap.searchType}");
		$("input#searchWord").val("${requestScope.paraMap.searchWord}");
	  	$("select#sizePerPage").val("${requestScope.paraMap.sizePerPage}");
	  }  
	
	
	
	
});


// Function Declaration

	function goView(seq){
		var frm = document.goViewFrm;
		frm.seq.value = seq;
		frm.searchType.value = "${requestScope.paraMap.searchType}";
	    frm.searchWord.value = "${requestScope.paraMap.searchWord}";
		
		frm.method="get";
		frm.action="<%=ctxPath%>/t1/viewSuggPost.tw";
		frm.submit();
		
	}//end of function goView('${boardvo.seq}') ---------------

	function goPwConfirm(seq){
		location.href="<%=ctxPath%>/t1/viewPrivatePost.tw?seq="+seq+"&searchType=${requestScope.paraMap.searchType}&searhWord=${requestScope.paraMap.searchWord}&gobackURL=${requestScope.gobackURL}";
	}
	
	function goSearch(){
		var frm = document.searchFrm;
		frm.action = "suggestionBoard.tw";	//상대경로
		frm.method = "GET";
		$("form#searchFrm").submit();
	}

</script>


<div id="board-container">
	<a href="javascript:location.href='suggestionBoard.tw'" style="text-decoration:none; color: black;"><i class="fas fa-exclamation fa-lg"></i>&nbsp;&nbsp;<span style="display: inline-block; font-size:22px;">건의사항</span></a>
	<!-- 보기개수 선택&조건검색 -->
	<div id="search-viewOption">
		<form name="searchFrm" id="searchFrm" style="float:right;">
			<select name="sizePerPage" id="sizePerPage" style="float:right; height: 28px; padding: 4px 0;">
				<option value="">보기개수</option>
				<option value="5">5개씩보기</option>
				<option value="10">10개씩보기</option>
				<option value="15">15개씩보기</option>
			</select>
			<select name="searchType" id="searchType" style=" height: 28px; ">
				<option value="subject">제목</option>
				<option value="name">작성자</option>
				<option value="content">본문</option>
			</select>
			<input type="text" name="searchWord" id="searchWord" style="height: 28px; "autocomplete="off" />
			<button type="button" class="btn-style float-right" onclick="goSearch()"><span style="color: #ffffff;">검색</span></button>
		</form>
	</div>
	<!-- 게시판 글목록 -->
	<div id="board-table-div">
		<table id="board-table" class="table">
			<thead>
				<tr class="thead">
					<th width=3% align="center" id="postNum">No.</th>
					<th width=50% align="center" id="subject">제목</th>
					<th width=5% align="center" id="writer">작성자</th>
					<th width=8% align="center" id="regDate">작성일</th>
					<th width=3% align="center" id="readCount">조회수</th>
					<th width=3% align="center" id="uploadFile">파일</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach var="boardvo" items="${requestScope.boardList}" varStatus="status">
				<tr class="tbody">
					<td><span class="postIndex">${fn:length(boardList) - status.index}</span>
					<input type="hidden" name="postIndex" />
					</td>
					<td>
						<c:if test="${boardvo.depthno == 0 && boardvo.privatePost ne '1'}"><!-- 원글이면서 일반글인 경우 -->
      						<a class="anchor-style" href="javascript:goView('${boardvo.seq}')" >${boardvo.subject}
      						<c:if test="${boardvo.commentCount > 0}">(<span style="font-weight:bold;">${boardvo.commentCount}</span>)</c:if>
      						</a>
						</c:if>
												
						<c:if test="${boardvo.depthno == 0 && boardvo.privatePost eq '1'}"><!-- 원글이면서 비밀글인 경우 -->
							<c:choose>
      							<c:when test="${loginuser.fk_dcode eq '4' && (loginuser.fk_pcode eq '2' || loginuser.fk_pcode eq '3')}"><!-- 인사팀 대리,부장 로그인 시 비번입력 없이 바로 열람가능 -->
      								<a class="anchor-style" href="javascript:goView('${boardvo.seq}')" ><input type="hidden" name="privatePost" class="privatePost" value="${boardvo.privatePost}"/>${boardvo.subject}
      								<c:if test="${boardvo.commentCount > 0}">(<span style="font-weight:bold;">${boardvo.commentCount}</span>)</c:if></a>
				      				<i class="fas fa-lock"></i>
      							</c:when>
      							<c:otherwise><!-- 인사팀 대리,부장 제외 다른 유저가 로그인한 경우 -->
      								<a class="anchor-style" href="javascript:goPwConfirm('${boardvo.seq}')" ><input type="hidden" name="privatePost" class="privatePost" value="${boardvo.privatePost}"/>${boardvo.subject}
      								<c:if test="${boardvo.commentCount > 0}">(<span style="font-weight:bold;">${boardvo.commentCount}</span>)</c:if></a>
						      		<i class="fas fa-lock"></i>
      							</c:otherwise>
      						</c:choose>
						</c:if>	      				
	      				
						<c:if test="${boardvo.depthno > 0 && boardvo.privatePost ne '1'}"><!-- 답글이면서 일반글인 경우 -->
	      					<a class="anchor-style" href="javascript:goView('${boardvo.seq}')" ><span style="font-weight: bold; padding-left: ${boardvo.depthno *20 }px">${boardvo.subject}
	      					<c:if test="${boardvo.commentCount > 0}">(<span style="font-weight:bold;">${boardvo.commentCount}</span>)</c:if></span></a>
	      				</c:if>
	      				
						<c:if test="${boardvo.depthno > 0 && boardvo.privatePost eq '1'}"><!-- 답글이면서 비밀글인 경우 -->
	      					<c:choose>
      							<c:when test="${loginuser.fk_dcode eq '4' && (loginuser.fk_pcode eq '2' || loginuser.fk_pcode eq '3')}"><!-- 인사팀 대리,부장 로그인 시 비번입력 없이 바로 열람가능 -->
      								<a class="anchor-style" href="javascript:goView('${boardvo.seq}')" ><input type="hidden" name="privatePost" class="privatePost" value="${boardvo.privatePost}"/><span style="font-weight: bold; padding-left: ${boardvo.depthno *20 }px">${boardvo.subject}
      								<c:if test="${boardvo.commentCount > 0}">(<span style="font-weight:bold;">${boardvo.commentCount}</span>)</c:if></span></a>
				      				<i class="fas fa-lock"></i>
      							</c:when>
      							<c:otherwise><!-- 인사팀 대리,부장 제외 다른 유저가 로그인한 경우 -->
      								<a class="anchor-style" href="javascript:goPwConfirm('${boardvo.seq}')" ><input type="hidden" name="privatePost" class="privatePost" value="${boardvo.privatePost}"/><span style="font-weight: bold; padding-left: ${boardvo.depthno *20 }px">${boardvo.subject}
      								<c:if test="${boardvo.commentCount > 0}">(<span style="font-weight:bold;">${boardvo.commentCount}</span>)</c:if></span></a>
						      		<i class="fas fa-lock"></i>
      							</c:otherwise>
      						</c:choose>
	      				</c:if>
					</td>
					<td>${boardvo.name}</td>
					<td>${boardvo.regDate}</td>
					<td align="center">${boardvo.readCount}</td>
					<td align="center">
						<c:if test="${not empty boardvo.fileName}">
							<c:if test="${boardvo.privatePost eq '1'}">
								<c:choose>
									<c:when test="${loginuser.employeeid == boardvo.fk_employeeid || (loginuser.fk_dcode eq '4' && (loginuser.fk_pcode eq '2'|| loginuser.fk_pcode eq '3'))}">
										<a href="<%=ctxPath%>/t1/downloadNoticeFile.tw?seq=${boardvo.seq}" class="anchor-style"><i class="fas fa-paperclip"></i></a>
									</c:when>
									<c:otherwise><i class="fas fa-paperclip"></i></c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${boardvo.privatePost eq '0'}">
								<a href="<%=ctxPath%>/t1/downloadNoticeFile.tw?seq=${boardvo.seq}" class="anchor-style"><i class="fas fa-paperclip"></i></a>
							</c:if>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	
	 	 <%-- 페이지 바 --%>
    	<div align="center" style="width: 70%; margin: 20px auto;">${requestScope.pageBar}</div>
		
		<button type="button" class="btn-style float-right" onclick="javascript:location.href='suggPostUpload.tw'"><span style="color: #ffffff;">글쓰기</span></button>
	</div>
	
	<%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
             페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 "검색결과목록보기" 버튼을 클릭했을때
             돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다. --%>
    <form name="goViewFrm">
    	<input type="hidden" name="seq"/>
    	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
    	<input type="hidden" name="searchType" />
      	<input type="hidden" name="searchWord" />
    </form>
</div>

