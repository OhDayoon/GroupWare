<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%	String ctxPath = request.getContextPath(); %>
<link rel="stylesheet" type="text/css" href="<%=ctxPath%>/resources/css/kdn/board.css"/>

<style type="text/css">
	
	.move {cursor: pointer;}
	.moveColor {color: #660029; font-weight: bold;}
	
	a {text-decoration: none !important;}
	
	td.comment {text-align: center;}
</style>

<script type="text/javascript">

$(document).ready(function(){
	
    $("span#totalCmntCount").hide();
    $("span#updateCmntCnt").hide();
    $("span#cmntCount").hide();
	//goViewComment(1); // 페이징처리 한 댓글 읽어오기
	displayComment(1);
	
	
	$("span.move").hover(function(){
		                    $(this).addClass("moveColor");
		                }
                        ,function(){
                        	$(this).removeClass("moveColor");
                        });
	
	// 더보기 버튼을 누르면 댓글 더 보여주기  
	$("button#displayMoreCmnt").click(function(){
		var cmntCount = $("span#cmntCount").text();
		var totalCmntCount = $("span#totalCmntCount").text();
		
		console.log("cmntCount: "+cmntCount);
		console.log("totalCmntCount: "+totalCmntCount);
		
		if(cmntCount >= 5){
			$("div#commentDisplay").append("<hr style='margin: 10px 0;'>");
			displayComment($(this).val());
		} else if(cmntCount == totalCmntCount){
			$(this).hide();
		}
		
	});
	
});// end of $(document).ready(function(){})------------------

var updateCmntCnt = 0;
//=== 댓글쓰기 ===
function goWriteComment() {
	
	var contentVal = $("input#commentContent").val().trim();
	if(contentVal == "") {
		alert("댓글 내용을 입력하세요!!");
		return; // 종료
	}
	
	var form_data = $("form[name=writeCmntFrm]").serialize();
	
	$.ajax({
		url:"<%= ctxPath%>/t1/addSuggComment.tw",
		data:form_data,
		type:"post",
		dataType:"json",
		success:function(json){
			$("div#commentDisplay").empty();
			$("span#cmntCount").text(0);
			
			if(updateCmntCnt > 0){
				updateCmntCnt = updateCmntCnt +1;
				$("span#totalCmntCountDisplay").text("댓글("+updateCmntCnt+")");
			} else {
				updateCmntCnt = ${requestScope.totalCmntCount}+1;
				$("span#totalCmntCountDisplay").text("댓글("+updateCmntCnt+")");
			}
			
			console.log("업뎃댓글갯수: "+updateCmntCnt);
			displayComment(1); // 페이징처리 한 댓글 읽어오기 
		   $("input#commentContent").val("");
		   
		},
		error: function(request, status, error){
			alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	 	}
	});
	
}// end of function goAddWrite(){}--------------------------

var len = 5; // 한번에 댓글 5개씩 보기

//display 할 댓글 정보를 추가 요청하기
function displayComment(start){
	// 댓글 수 5개 이하는 더보기 버튼 숨기기, 5개 이상 보이기
	if(${requestScope.totalCmntCount} > 5 || updateCmntCnt >5){
		$("button#displayMoreCmnt").show();
	} else {
		$("button#displayMoreCmnt").hide();
	}
	
	$.ajax({
		url:"<%=ctxPath%>/t1/suggCommentList.tw",
		// type:"GET",
		data:{"fk_seq":"${requestScope.boardvo.seq}"
			 ,"start":start
			 ,"len":len},
		dataType:"JSON",
		success:function(json){
			
			var html = "";
			if(updateCmntCnt > 0){
				$("span#totalCmntCountDisplay").text("댓글("+updateCmntCnt+")");
			} else {
				$("span#totalCmntCountDisplay").text("댓글(${requestScope.totalCmntCount})");
			}
			if(start == "1" && json.length == 0){
                
				html += "댓글이 없습니다.";
				$("div#commentDisplay").html(html);
				
			} else if(json.length > 0) {
				// 처음에 데이터가 존재하는 경우
				$.each(json, function(index,item){	// ajax 반복문
					html += "<strong style='font-size:13px;'>"+item.name+"</strong>&nbsp;&nbsp;";
					html += "<span style='display:inline-block; margin-bottom:5px; font-size:13px;'>"+item.regDate+"</span>&nbsp;&nbsp;";
					
					if($("input[name=fk_employeeid]").val() == item.fk_employeeid){
						html+="<a href='javascript:goEdit("+index+")' class='editCmnt' value='"+index+"' style='margin-right:5px; font-size:13px; text-decoration: none;'>수정</a>";
						html+="<a href='javascript:editComplete("+index+")' class='editConfirm' style='margin-right:5px; font-size:13px; text-decoration: none;'>확인</a>";
						html+="<a href='javascript:delSuggComment("+item.seq+")' class='delCmnt' style='border-left: solid 1px gray; padding-left: 5px; font-size:13px; text-decoration: none;'>삭제</a>";
						html+="<a href='javascript:editCancel("+index+")' class='editCancel' style='border-left: solid 1px gray; padding-left: 5px; font-size:13px; text-decoration: none;'>취소</a><br>";
					} else {
						html+="<br>";
					}
					console.log("인덱스확인용: "+index);
					html += "<span class='content-span' name='content-span' style='font-size:13px;'>"+item.content+"</span>";
					html += "<input class='editComntContent' type='hidden' value='"+item.content+"' />";
					html += "<input class='editCmntSeq' type='hidden' value='"+item.seq+"' />";
					html += "<form name='editCmntFrm'>";
					html += "</form>";
					if(index < json.length-1){
						html +="<hr style='margin: 10px 0;'>";
					}
					
				});	// end of $.each(json, function(index,item){}) ----------------
				
				//댓글 출력하기
				$("div#commentDisplay").append(html);
				$("form[name=editCmntFrm]").hide();
				$("a.editConfirm").hide();
				$("a.editCancel").hide();
				
				// >>> !!! 중요 !!! 더보기... 버튼의 value 속성에 값을 지정하기 <<< //  
				$("button#displayMoreCmnt").val(Number(start)+len);
				
				// cmntCount에 지금까지 출력된 댓글의 개수를 누적해서 기록한다
				$("span#cmntCount").text( Number($("span#cmntCount").text()) +json.length );
				
				if(updateCmntCnt > 0){
					$("span#updateCmntCnt").text(updateCmntCnt);					
				} else {
					$("span#totalCmntCount").text(json.totalCmntCount);
				}

				 // 더보기... 버튼을 계속해서 클릭하여 countHIT 값과 totalHITCount 값이 일치하는 경우 
				 if(updateCmntCnt > 0){
					if($("span#updateCmntCnt").text() == $("span#cmntCount").text()){
						$("button#displayMoreCmnt").hide();
						//$("span#cmntCount").text("0");
					}
				 } else {
					if($("span#totalCmntCount").text() == $("span#cmntCount").text()){
						$("button#displayMoreCmnt").hide();
						//$("span#cmntCount").text("0");
					}
				 }
			} 
		},
		error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		}
	});
	
	
}// end of function displayHIT()-----------------------

var editComntContent ="";
var editCmntSeq = "";

// 댓글 수정 클릭시 입력창 표시하기
function goEdit(idx){
	 // console.log("이 댓글 인덱스 : "+idx);
	//$("input[name=content-input]").eq(idx).toggle();
	$("span.content-span").eq(idx).toggle();
	
	editComntContent = $("input.editComntContent").eq(idx).val();
	editCmntSeq = $("input.editCmntSeq").eq(idx).val();
	
	 console.log("editComntContent : "+editComntContent);
	 console.log("editCmntSeq : "+editCmntSeq);
	
	$("form[name=editCmntFrm]").append("<input name='editComntContent' type='text' style='width:95%;'/>");
	$("form[name=editCmntFrm]").append("<input name='editCmntSeq' type='hidden' />");
	
	$("input[name=editCmntSeq]").val(editCmntSeq);
	$("input[name=editComntContent]").val(editComntContent);
	
	$("form[name=editCmntFrm]").eq(idx).toggle();
	$("a.editCmnt").eq(idx).toggle();
	$("a.delCmnt").eq(idx).toggle();
	$("a.editConfirm").eq(idx).toggle();
	$("a.editCancel").eq(idx).toggle();
	
}

// 댓글 수정 취소
function editCancel(idx){
	console.log("이 수정댓글창 인덱스: "+idx);
	$("form[name=editCmntFrm]").find('input').remove();
	//$("form[name=editCmntFrm]").eq(idx).children().children().remove();
	$("form[name=editCmntFrm]").eq(idx).toggle();
	$("span.content-span").eq(idx).toggle();
	$("a.editCmnt").eq(idx).toggle();
	$("a.delCmnt").eq(idx).toggle();
	$("a.editConfirm").eq(idx).toggle();
	$("a.editCancel").eq(idx).toggle();
}

//확인 클릭시 댓글 수정완료하기
function editComplete(idx){
   
   var form_data = $("form[name=editCmntFrm]").eq(idx).serialize();
   
   $.ajax({
	   url:"<%=request.getContextPath()%>/t1/editSuggComment.tw",
	   data:form_data,
	   type:"get",
	   dataType:"json",
	   success:function(json){
		   if(json.n == 1){
			   //alert('댓글 수정 성공');
			   $("div#commentDisplay").empty();
			   $("span#cmntCount").text(0);
			   displayComment(1);
		   } else {
			   alert('댓글 수정 실패');
		   }
		   
	   },
	   error: function(request, status, error){
            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
       }
   });
   
}  //end of function editComplete(idx) -------

// 댓글 삭제하기
function delSuggComment(comment_seq){
   
   var bool = confirm("댓글을 삭제하시겠습니까?");
   console.log("bool => " + bool);
 
   if(bool){
	   $.ajax({
		   url:"<%=request.getContextPath()%>/t1/delSuggComment.tw",
		   data:{"seq":comment_seq,
			     "fk_seq":"${requestScope.boardvo.seq}"},
		   dataType:"json",
		   success:function(json){
			   if(json.n == 1){
				   console.log("댓글 삭제 성공");
				   
				   $("div#commentDisplay").empty();
				   $("span#cmntCount").text(0);
				   if(updateCmntCnt > 0){
					    updateCmntCnt = updateCmntCnt-1;
						console.log("업뎃댓글갯수: "+updateCmntCnt);
						$("span#updateCmntCnt").text("댓글("+updateCmntCnt+")");
					   displayComment(1);
					} else {
						updateCmntCnt = ${requestScope.totalCmntCount}-1;
						console.log("업뎃댓글갯수: "+updateCmntCnt);
						$("span#totalCmntCountDisplay").text("댓글("+updateCmntCnt+")");
					   displayComment(1);
					}	
				   
			   } else {
				   alert('댓글 삭제가 실패했습니다.');
			   }
		   },
		   error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	       }
	   });
   }
}  //end of function delSuggComment(comment_seq) -------

</script>

<div id="board-container">
	<a href="javascript:location.href='suggestionBoard.tw'" style="text-decoration:none; color: black;"><i class="fas fa-exclamation fa-lg"></i>&nbsp;&nbsp;<span style="display: inline-block; font-size:22px; margin-bottom: 40px;">건의사항</span></a>

	<c:if test="${not empty requestScope.boardvo}">
		<table id="post-table" class="table table-bordered">
			<tr class="thead">
				<th style="width: 100px;">작성자</th>
				<td>${requestScope.boardvo.name}</td>
			</tr>
			<tr class="thead">
				<th>작성일시</th>
				<td>${requestScope.boardvo.regDate}</td>
			</tr>
			<tr class="thead">
				<th>제목</th>
				<td>${requestScope.boardvo.subject}</td>
			</tr>
			<c:if test="${not empty requestScope.boardvo.fileName}">
				<tr class="thead">
					<th>첨부파일</th>
					<td>
						<c:if test="${sessionScope.loginuser != null}">
							<a href="<%=ctxPath%>/t1/downloadSuggFile.tw?seq=${requestScope.boardvo.seq}">${requestScope.boardvo.orgFilename}</a>
						</c:if>
						<c:if test="${sessionScope.loginuser == null}">
							${requestScope.boardvo.orgFilename}
						</c:if>
					</td>
				</tr>
			</c:if>
			<tr class="thead">
				<td colspan='2'>
				    <p style="word-break: break-all;">${requestScope.boardvo.content}</p>
				</td>
			</tr>
		</table>
	</c:if>
	
	<c:if test="${empty requestScope.boardvo}">
		<div style="padding: 50px 0; font-size: 16pt; color: red;">존재하지 않습니다</div>
	</c:if>

	<div id="viewpost-btn-container">
		<c:set var="gobackURL2" value="${fn:replace(requestScope.gobackURL,'&', ' ') }" />
		<c:if test="${requestScope.boardvo.previousseq ne null}">
			<i class="fas fa-angle-double-right"></i>&nbsp;&nbsp;이전글&nbsp;&nbsp;<span class="move" onclick="javascript:location.href='viewSuggPost.tw?seq=${requestScope.boardvo.previousseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'">${requestScope.boardvo.previoussubject}</span><br>
		</c:if>
		<c:if test="${requestScope.boardvo.nextseq ne null}">
			<i class="fas fa-angle-double-right"></i>&nbsp;&nbsp;다음글&nbsp;&nbsp;<span class="move" onclick="javascript:location.href='viewSuggPost.tw?seq=${requestScope.boardvo.nextseq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}&gobackURL=${gobackURL2}'">${requestScope.boardvo.nextsubject}</span>
		</c:if>
		<c:if test="${not empty requestScope.gobackURL}">
			<button type="button" class="float-right btn-style" onclick="javascript:location.href='<%=ctxPath%>/${requestScope.gobackURL}'">목록</button><!-- 기존 검색된결과목록 -->
		</c:if>
		<c:if test="${empty requestScope.gobackURL}">
			<button type="button" class="float-right btn-style" onclick="javascript:location.href='suggestionBoard.tw'">목록</button>
		</c:if>
		<!-- 인사팀 대리만 답변글쓰기 가능 -->
		<c:if test="${loginuser.fk_dcode eq '4' && loginuser.fk_pcode eq'2'}">
		<button type="button" class="float-right btn-style" onclick="javascript:location.href='<%= ctxPath%>/t1/suggPostUpload.tw?parentSeq=${requestScope.boardvo.seq}&groupno=${requestScope.boardvo.groupno}&depthno=${requestScope.boardvo.depthno}&subject=${requestScope.boardvo.subject}&privatePost=${requestScope.boardvo.privatePost}'">답글쓰기</button>
		</c:if>
		
		
		
		<c:if test="${requestScope.boardvo.fk_employeeid eq loginuser.employeeid}">
			<button type="button" class="float-right btn-style" onclick="javascript:location.href='<%= ctxPath%>/t1/suggDel.tw?seq=${requestScope.boardvo.seq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}'">삭제</button>
			<button type="button" class="float-right btn-style" onclick="javascript:location.href='<%= ctxPath%>/t1/suggEdit.tw?seq=${requestScope.boardvo.seq}&searchType=${requestScope.searchType}&searchWord=${requestScope.searchWord}'">수정</button>
		</c:if>
	</div>

	<%-- 댓글쓰기 폼 추가 === --%>
    <c:if test="${not empty sessionScope.loginuser}">
		<form name="writeCmntFrm" style="margin-top: 20px;">
	        <input type="hidden" name="fk_employeeid" value="${sessionScope.loginuser.employeeid}" />
			<input type="hidden" name="name" value="${sessionScope.loginuser.name}" />  
			<%-- 댓글에 달리는 원게시물 글번호(즉, 댓글의 부모글 글번호) --%>
			<input type="hidden" name="fk_seq" value="${requestScope.boardvo.seq}" /> 
			댓글쓰기(500자이내)<br>
			<input id="commentContent" type="text" name="content" style="display: inline-block; width: 93%; height: 28px; margin: 5px 0;"/> 
			<button class="btn-style" id="btnComment" type="button" onclick="goWriteComment()">확인</button> 
			<button class="cmnt-btn-style" type="reset">취소</button>
		</form>
    </c:if>
    
    <%-- <span id="totalCmntCountDisplay" style="display: inline-block; margin-top: 20px; margin-bottom: 10px;">댓글(${requestScope.boardvo.commentCount})</span> --%>
    <span id="totalCmntCountDisplay" style="display: inline-block; margin-top: 20px; margin-bottom: 10px;"></span>
    <!-- ===== #94. 댓글 내용 보여주기 ===== -->
    <div id="commentDisplay" style="clear:both; border-top: solid 1px #eee; border-bottom: solid 1px #eee; padding: 10px 10px;"></div>
	<button type="button" id="displayMoreCmnt" class="btn-style" style="margin-top: 10px; font-size: 8pt;">더보기</button>
	
	<span id="totalCmntCount">${requestScope.totalCmntCount}</span>
	<span id="updateCmntCnt"></span>
    <span id="cmntCount">0</span>
	
    <%-- ==== #136. 댓글 페이지바 ==== --%>
    <div id="pageBar" style="width: 90%; margin: 10px auto; text-align: center;"></div> 
    


</div>





    