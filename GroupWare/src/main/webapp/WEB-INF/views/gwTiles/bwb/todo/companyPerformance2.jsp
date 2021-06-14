<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% 
	String ctxPath = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	.highcharts-figure, .highcharts-data-table table {
	    min-width: 310px; 
	    max-width: 800px;
	    margin: 1em auto;
	}
	
	#container {
	    height: 400px;
	}
	
	.highcharts-data-table table {
		font-family: Verdana, sans-serif;
		border-collapse: collapse;
		border: 1px solid #EBEBEB;
		margin: 10px auto;
		text-align: center;
		width: 100%;
		max-width: 500px;
	}
	.highcharts-data-table caption {
	    padding: 1em 0;
	    font-size: 1.2em;
	    color: #555;
	}
	.highcharts-data-table th {
		font-weight: 600;
	    padding: 0.5em;
	}
	.highcharts-data-table td, .highcharts-data-table th, .highcharts-data-table caption {
	    padding: 0.5em;
	}
	.highcharts-data-table thead tr, .highcharts-data-table tr:nth-child(even) {
	    background: #f8f8f8;
	}
	.highcharts-data-table tr:hover {
	    background: #f1f7ff;
	}

</style>
<script src="https://code.highcharts.com/highcharts.js"></script>
<script src="https://code.highcharts.com/modules/series-label.js"></script>
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>

<div id="contentBox" style="width:1300px; margin:0 auto;" >
	<div id="selectDiv" style="margin-bottom:50px; margin-top:50px; margin-left:900px;">
		<select id="siljukYear">
			<c:forEach var="i" begin="${paraMap.minYear}" end="${paraMap.maxYear}">
				<option value="${i}">${i}</option>
			</c:forEach>
		</select>
		<select id="siljukMonth">
		</select>
	</div>
    <div id="container" style="margin-left:-50px; width:85%; height:600px;"></div>
</div>




<script type="text/javascript">
	
	
	$(document).ready(function(){
		
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth()+1;
		
		var minMonth = Number("${paraMap.minMonth}");
		var minYear = "${paraMap.minYear}";
		var maxMonth = Number("${paraMap.maxMonth}");
		var maxYear = "${paraMap.maxYear}";
		
		
		// 문서 초기 로딩 시 select태그에 최근 년에 해당하는 월 선택값 넣어주기
		$("select#siljukYear").val(maxYear);
		
		var html ="";
		for(var i=1; i<(maxMonth+1); i++){
			html+="<option value='"+i+"'>"+i+"</option>";
		}
		
		
		$("select#siljukMonth").html(html);
		$("select#siljukMonth").val(maxMonth);
		
		var selectedYear = $("select#siljukYear").val();	
		var selectedMonth = $("select#siljukMonth").val();
		
		// 차트구현
		goChart(selectedYear,selectedMonth);
		
		
		// 년도 선택시 실적이 존재하는 월만 보여주기 및 해당년도/월에 해당하는 데이터 가져오기
		$("select#siljukYear").bind("change",function(){
			
			var selectedYear = $(this).val();
			 
			
			// 월 select태그 값 정해주기
			if(selectedYear==minYear){// 실적이 존재하는 가장 오래된 년도 선택시
				var html ="";
				for(var i=minMonth; i<13; i++){
					html+="<option value='"+i+"'>"+i+"</option>";
				}
				
				$("select#siljukMonth").html(html);
				
			}
			else if(selectedYear==maxYear){
				var html ="";
				for(var i=1; i<(maxMonth+1); i++){
					html+="<option value='"+i+"'>"+i+"</option>";
				}
				// $("select#siljukMonth option:selected").val(maxMonth);
				// $("select#siljukMonth").val("${paraMap.maxMonth}");
				
				$("select#siljukMonth").html(html);
				$("select#siljukMonth").val(maxMonth);
			}
			else{
				var html ="";
				for(var i=1; i<13; i++){
					html+="<option value='"+i+"'>"+i+"</option>";
				}
				$("select#siljukMonth").html(html);
			}
			
			var selectedMonth = $("select#siljukMonth").val();
			
			
			// select태그 값 변동시, 차트구현
			goChart(selectedYear,selectedMonth);
			
			
		}); // end of $("select#siljukMonth").bind("change",function(){
		
		
			
		// 월 선택시 실적이 존재하는 월만 보여주기 및 해당년도/월에 해당하는 데이터 가져오기
		$("select#siljukMonth").bind("change",function(){
			
			var selectedYear = $("select#siljukYear").val();	
			var selectedMonth = $(this).val();
			
			
			// select태그 값 변동시, 차트구현
			goChart(selectedYear,selectedMonth);
			
			

		}); // end of $("select#siljukMonth").bind("change",function(){
		
	}); // end of $(document).ready(function(){
	
		
	// function()	
	// select태그 값 변동시, 차트구현 
	function goChart(selectedYear,selectedMonth){
		
		$.ajax({ // 1st ajax:선택한 월부터 -2개월 전까지 년-월 뽑아오기
			url:"<%= ctxPath%>/t1/selectPerformanceMonth.tw",
			data:{"selectedYear":selectedYear,
				  "selectedMonth":selectedMonth},
			dataType:"json",
			success:function(json){
				
				var performanceWhenArr = [];
				performanceWhenArr.push(json.lastDate);
				performanceWhenArr.push(json.middleDate);
				performanceWhenArr.push(json.firstDate);
				 
				var performanceWhenArres = performanceWhenArr.join();
				
				var performanceDeptCntArr = [];    // 부서 각각의 개월수 건수마다의 배열
				var totalArr = []; // 부서별 3개월간의 총 합 건수 배열
				
				$.ajax({ // 2st ajax: 선택한 월부터 -2개월전까지 데이터 뽑아오기
					url:"<%= ctxPath%>/t1/selectCoPerformance.tw",
					data:{"performanceWhenArres":performanceWhenArres},
					dataType:"json",
					success:function(json){
						// [ {cs1팀, 1개월값,2개월값,3개월값 }, {동일},{동일}, {type: ~~, name:~~,[ {cs1팀, 3개월총합건수 } , {동일 } , { } ],~~~~~~ }  ] 
						
						$.each(json,function(index,item){ 
							
							var nowCnt = Number(item.nowCnt);
							var prevCnt = Number(item.prevCnt);
							var pprevCnt = Number(item.pprevCnt);
							var totalCnt = Number(item.totalCnt);
							
							
							if(index < json.length){
								var subArr = []; // data:[완성시키기]
								subArr.push(pprevCnt);
								subArr.push(prevCnt);
								subArr.push(nowCnt);
								
								performanceDeptCntArr.push({type:'column', name:item.dname,data:subArr});
								totalArr.push({name:item.dname, y:totalCnt,color: Highcharts.getOptions().colors[index]});
								
							} // end of if(index < json.length()){

						}); // end of each ------------------------------
						
				
						// 평균 데이터 넣어주기 (index==json.length() 일때)		
						$.ajax({
							url:"<%= ctxPath%>/t1/selectAvgCnt.tw",
							data:{"performanceWhenArres":performanceWhenArres},
							dataType:"json",
							success:function(json){
								var avgArr = [];
								
								$.each(json,function(index,item){
									
									avgArr.push(Number(item.avgCnt)); // 평균 배열 만들어짐
									
								}); // end of each
								
								// 평균그래프 만들어주기
								performanceDeptCntArr.push({type:'spline',name:'평균',data:avgArr,
									marker: {
						            lineWidth: 2,
						            lineColor: Highcharts.getOptions().colors[3],
						            fillColor: 'white'
									}
						        });
								
								// 파이형 차트 객체데이터 만들어주기
								performanceDeptCntArr.push({type: 'pie',
							        						name: 'Total consumption',
							        						data: totalArr,
							        				        center: [100, 80],
							        				        size: 100,
							        				        showInLegend: false,
							        				        dataLabels: {enabled: false}});
								
								
								// 차트구현하기 시작
								Highcharts.chart('container', {
								    title: {
								        text: '부서 실적 현황'
								    },
								    xAxis: {
								        categories: performanceWhenArr  // 배열가져오기
								    }, 
								    yAxis: {

								    	max: 15,
							    	},
								    labels: {
								        items: [{
								            html: '<span style="font-weight:bolder;">최근 3개월간 부서 실적의 합계</span>',
								            style: {
								                left: '50px',
								                top: '20px',
								                color: ( // theme
								                    Highcharts.defaultOptions.title.style &&
								                    Highcharts.defaultOptions.title.style.color
								                ) || 'black'
								            }
								        }]
								    },
								    series: performanceDeptCntArr
								});
								// 차트구현하기 끝
								
							},
					    	error: function(request, status, error){
					            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					        }
							
						}); // end of 3st ajax(/t1/selectAvgCnt.tw)
						
						
						
						
						
					},// end of success  2st ajax
			    	error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }
					
				});// end of 2st $.ajax({


			}, // end of success 1st ajax
	    	error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }
			
		}); // end of 1st $.ajax({
		
	}// end of function goChart(){
	
	
</script>