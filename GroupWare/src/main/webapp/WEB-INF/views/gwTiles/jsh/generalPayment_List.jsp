<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<% String ctxPath = request.getContextPath(); %>

<style type="text/css">
   table, th, td {border: solid 1px gray;}

    #table {width: 970px; border-collapse: collapse;}
    #table th, #table td {padding: 5px;}
    #table th {background-color: #DDD;}
     
    .subjectStyle {font-weight: bold;
                   color: navy;
                   cursor: pointer;} 
</style>

<script type="text/javascript">
		$(document).ready(function(){
			 $("div#submenu3").show();
			
		    $("span.subject").bind("mouseover", function(event){
		         var $target = $(event.target);
		         $target.addClass("subjectStyle");
		      });
		      
		      $("span.subject").bind("mouseout", function(event){
		         var $target = $(event.target);
		         $target.removeClass("subjectStyle");
		      });
		      
		      $("div#displayList").hide();
		      
		      $("input#searchWord").keyup(function(event){
		         
		         var wordLength = $(this).val().trim().length;
		         // 검색어의 길이를 알아온다.
		         console.log(wordLength);
		         if(wordLength == 0){
		            // 검색어가 공백이거나 검색어 입력후 백스페이스키를 눌러서 검색어를 모두 지우면 검색된 내용이 안 나오도록 해야 한다.            
		            $("div#displayList").hide();
		         }
		         else{
		        	 $.ajax({
			               url:"<%= ctxPath%>/t1/wordSearchShow.tw",
			               type:"get",
			               data:{"searchType":$("select#searchType").val()
			            	 //  ,"searchType":$("select#searchCategory").val()
			                  , "searchWord":$("input#searchWord").val()},
			               dataType:"json",
			               success:function(json){ // [] 또는 [{"word":"글쓰기 첫번째 java 연습입니다"}, {"word":"글쓰기 두번째 JaVa 연습입니다"}]
			            	   <%-- === #112. 검색어 입력지 자동글 완성하기 7 === --%>
			                   if(json.length > 0){
			                      // 검색된 데이터가 있는 경우
			                       
			                      var html = "";
			                      
			                      $.each(json, function(index, item){
			                         var word = item.word;
			                         // word ==> "글쓰기 첫번째 java 연습입니다"
			                         // word ==> "글쓰기 첫번째 JaVa 연습입니다"                        
			                         
			                         var index = word.toLowerCase().indexOf($("input#searchWord").val().toLowerCase());
			                         // word ==> "글쓰기 첫번째 java 연습입니다"
			                         // word ==> "글쓰기 첫번째 java 연습입니다"
			                         // 만약에 검색어가 jAva 이라면 index 는 8이 된다.
			                         
			                         var len = $("input#searchWord").val().length;
			                         // 검색어의 길이 len = 4
			                         
			                         word.substr(0,index) + "<span style='color: blue;'>"+word.substr(index,len)+"</span>"+word.substr(index+len); 
			                         
			                         html += "<span style='cursor:pointer;'>"+word+"</span><br>";
			                      });
			                      
			                      $("div#displayList").html(html);
			                      $("div#displayList").show();
			                   }
			               },
			               error:function(request, status, error){
			                  alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			               }
			            });            
		         }
		         
		      }); //end of   $("input#searchWord").keyup(function(){ ----------------------------
			 
		      <%-- === #113. 검색어 입력시 자동글 완성하기 8 === --%>
		      $(document).on("click", "span.word", function(){ // span.word 는 body 태그 안에 쓴 것이 아니라 script 안에 있기 때문에 $("span.word") 와 같이 작성하면 안된다. 
		         $("input#searchWord").val($(this).text());
		         // 텍스트박스에 검색된 결과의 문자열을 입력해준다.
		         
		         $("div#displayList").hide();
		         goSearch();
		      });
		    	  
		      // 검색시 검색조건 및 검색어 값 유지시키기
		      if(${not empty requestScope.paraMap}){ 
		    	  // 빨간줄 생겨도 오류 아님! javascript에서 인식 못하지만 기능은 한다!
		    	  $("select#searchCategory").val("${requestScope.paraMap.searchCategory}");
		         $("select#searchType").val("${requestScope.paraMap.searchType}");
		         $("input#searchWord").val("${requestScope.paraMap.searchWord}");
		      } 
		    	  
		    	  
		$("button#search").click(function(){
				   var frm = document.searchFrm;
				   //console.log(frm);
		      		frm.method = "get";
		      		frm.action = "<%= ctxPath%>/t1/generalPayment_List.tw";
		      		frm.submit();
			}); 
		
	
		
			 
		}); //end of  $(document).ready(function(){})--------------------------------------------
		
		 
		  function goView(ano,ncatname){
		      
			  <%-- location.href="<%= ctxPath%>/t1/view.tw?ano="+ano+"&ncatname="+ncatname; --%>
			// === #124. 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
			    //           사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
			    //           현재 페이지 주소를 뷰단으로 넘겨준다.
			    	var frm =document.goViewFrm;
			    	frm.ano.value = ano;
			    	frm.ncatname.value = ncatname;
			    	
			    	frm.method = "get";
			    	frm.action = "<%= ctxPath%>/t1/view.tw";
			        frm.submit();
			  
		      
		   }// end of function goView(seq){}--------------------
		   
		
		   
		   
		   
</script>

<div style="padding-left: 3%;">

   <h2 style="margin-bottom: 30px;">일반결재내역</h2>
   
   
   
   <%-- === #101. 글검색 폼 추가하기 : 카테고리로 검색을 하도록 한다. === --%>
   <form style="margin-bottom: 10px;" action="<%= ctxPath%>/t1/generalPayment_List.tw">
      <select name="searchCategory" id="searchCategory" style="height: 26px;">
         <option value="">::일반결재문서종류::</option>
         <option value="1">회의록</option>
         <option value="2">위임장</option>
         <option value="3">외부공문</option>
         <option value="4">협조공문</option>
      </select>
       <select name="searchType" id="searchType" style="height: 26px;">
         <option value="atitle">글제목</option>
         <option value="name">글쓴이</option>
      </select>
      <input type="text" name="searchWord" id="searchWord" size="40" autocomplete="off" /> 
      <input type="submit"  value="검색"/>
   </form>
   
   <%-- === #106. 검색어 입력시 자동글 완성하기 1 === --%>
   <div id="displayList" style="border: solid 1px gray; border-top: 0px; width: 331px; height: 100px; margin-left: 70px; padding-top: 5px; overflow: auto;">
      
   </div>
   
   <table id="table">
      <tr>
         <th style="width: 60px;  text-align: center;">NO.</th>
         <th style="width: 80px; text-align: center;">일반결재문서</th>
         <th style="width: 360px;  text-align: center;">제목</th>
         <th style="width: 150px; text-align: center;">작성자</th>
         <th style="width: 70px;  text-align: center;">날짜</th>
      </tr>
      
        <c:forEach var="evo" items="${requestScope.electronList}" varStatus="status">
         <tr>
	         <td align="center">${evo.ano}</td>
	         <td align="left"> ${evo.ncatname} </td>
	         <td align="center">
	         			<span class="subject" onclick="goView('${evo.ano}','${evo.ncatname}')">${evo.atitle}</span>
     	     </td>
	         <td align="center"><span >${evo.name}</span>(<span>${evo.dname}</span>)</td>
	         <td align="center">${evo.asdate}</td>   
         </tr>      
      </c:forEach>  
   </table>    
    
     
      <!-- === #122. 페이지바 보여주기  -->
   <div align="center" style="with:70%; border:solid 0px gray ; margin:20px auto;">
   			${requestScope.pageBar}
   </div>
   
   
    <%-- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ 
                  페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후
	        사용자가 "검색된결과목록보기" 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해
	        현재 페이지 주소를 뷰단으로 넘겨준다. --%>
   <form name="goViewFrm">
   	<input type="hidden" name="ano"/>
   	<input type="hidden" name="ncatname"/>
	<input type="hidden" name="gobackURL" value="${requestScope.gobackURL}"/>
   </form>
   
   
   
   
</div>           
              
              