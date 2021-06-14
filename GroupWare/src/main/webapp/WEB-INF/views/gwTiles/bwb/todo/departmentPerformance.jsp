<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%
	String ctxPath = request.getContextPath();
%>


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
<script src="https://code.highcharts.com/modules/exporting.js"></script>
<script src="https://code.highcharts.com/modules/export-data.js"></script>
<script src="https://code.highcharts.com/modules/accessibility.js"></script>

<figure class="highcharts-figure">
	<div id="siljuk">
		<select id="siljukMonth" style="float:right;">
		</select>
		<select id="siljukYear" style="float:right;">
			<c:forEach var="i" begin="${paraMap.minYear}" end="${paraMap.maxYear}">
				<option value="${i}">${i}</option>
			</c:forEach>
		</select>
	</div>
    <div id="container" style="width:900px; height:600px; margin-left:-200px; margin-top:40px;"></div>
</figure>

<script type="text/javascript">
	
	
	$(document).ready(function(){
		
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth()+1;        // 자바스크립에서 월은 0부터 시작
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
		var dcode = "${paraMap.dcode}";
		
		// 문서 로딩 시 select태그 값에 따른 차트구현
		goChart(selectedYear,selectedMonth,dcode);
		
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
			var dcode = "${paraMap.dcode}";
			
			// select태그 값 변동시, 차트구현
			goChart(selectedYear,selectedMonth,dcode);
			
			
		}); // end of $("select#siljukMonth").bind("change",function(){
		
		// 월 선택시 실적이 존재하는 월만 보여주기 및 해당년도/월에 해당하는 데이터 가져오기
		$("select#siljukMonth").bind("change",function(){
			
			var selectedYear = $("select#siljukYear").val();	
			var selectedMonth = $(this).val();
			var dcode = "${paraMap.dcode}"; 
			
			// select태그 값 변동시, 차트구현
			goChart(selectedYear,selectedMonth,dcode);
			
			

		}); // end of $("select#siljukMonth").bind("change",function(){
			
			
		
	}); // end of $(document).ready(function(){
	
	// function declaration
	
	// select태그 값 변동시, 차트구현 
	function goChart(selectedYear,selectedMonth,dcode){
		
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
				
				var performanceWhoArr = [];
				var performanceDataArr = [];
				$.ajax({ // 2st ajax: 선택한 월부터 -2개월전까지 데이터 뽑아오기
					url:"<%= ctxPath%>/t1/selectPerformance.tw",
					data:{"dcode":dcode,
						  "performanceWhenArres":performanceWhenArres},
					dataType:"json",
					success:function(json){
					// [{"ppreveCnt":"1","name":"육수연","selectCnt":"1","prevCnt":"0"},{"ppreveCnt":"0","name":"구수연","selectCnt":"0","prevCnt":"2"}
					// ,{"ppreveCnt":"0","name":"십수연","selectCnt":"1","prevCnt":"0"},{"ppreveCnt":"1","name":"백수연","selectCnt":"0","prevCnt":"1"}]	
						$.each(json,function(index,item){ // [{name값,[데이터값1,데이터값2,데이터값3]}, {},{}]
										
							performanceDataArr =[];
							
							performanceDataArr.push(Number(item.ppreveCnt));
							performanceDataArr.push(Number(item.prevCnt));
							performanceDataArr.push(Number(item.selectCnt));
							
							// var performanceDataArr = [item.ppreveCnt,item.preveCnt,item.selectCnt];
							performanceWhoArr.push({name: item.name , data: performanceDataArr});
							// performanceWhoArr.push(item.name); //
						}); // end of each ------------------------------
						

						////// 차트 구현 ///////
						Highcharts.chart('container', {
						    chart: {
						        type: 'bar'
						    },
						    title: {
						        text: '<span style="font-weight:bolder">${paraMap.dname}</span>부서의 실적현황'
						    },
						    subtitle: {
						        // text: 'Source: <a href="https://en.wikipedia.org/wiki/World_population">Wikipedia.org</a>'
						    },
						    xAxis: {
						        categories: performanceWhenArr, // 3개월 이름
						        title: {
						            text: null
						        }
						    },
						    yAxis: {
						        min: 0,
						        title: {
						            text: '건수',
						            align: 'high'
						        },
						        labels: {
						            overflow: 'justify'
						        }
						    },
						    tooltip: {
						        valueSuffix: ' 건'
						    },
						    plotOptions: {
						        bar: {
						            dataLabels: {
						                enabled: true
						            }
						        }
						    },
						    legend: {
						        layout: 'vertical',
						        align: 'right',
						        verticalAlign: 'top',
						        x: -40,
						        y: 80,
						        floating: true,
						        borderWidth: 1,
						        backgroundColor:
						            Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
						        shadow: true
						    },
						    credits: {
						        enabled: false
						    },
						    yAxis: {

						    	max: 10

					    	},
						       
						    series: performanceWhoArr 
						    // series: [{직원명1,[3개월 데이터값]}, {직원명2,[3개월 데이터값]}, {직원명3,[3개월 데이터값]}]
						   
						}); // end of Highcharts.chart
						// 차트 구현하기 끝
						
						$("text.highcharts-axis-title").text("실적건수");
						
					},
			    	error: function(request, status, error){
			            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			        }
				});// end of 2st $.ajax({
					
			},
	    	error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	        }

			
		}); // end of 1st $.ajax({
		
	}// end of function goChart(){
	
	
</script>