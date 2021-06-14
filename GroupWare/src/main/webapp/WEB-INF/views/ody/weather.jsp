<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>
<title>Insert title here</title>
<script type="text/javascript" src="https://code.jquery.com/jquery-1.12.4.min.js" ></script>
<script type="text/javascript">

var weatherTimejugi = 0;  // 단위는 밀리초

$(document).ready(function(){
	 
	
    var todayw = new Date();

    // 오늘 정보
	var wyear = todayw.getFullYear();
	var wmonth = todayw.getMonth()+1;
	var wday = todayw.getDate();
	var whour = todayw.getHours();
	var wminutes = todayw.getMinutes();	
	
	if(whour<10){
		whour='0'+whour;
	}
	if(wmonth<10){
		wmonth='0'+wmonth;
	}
	if(wday<10){
		wday='0'+wday;
	}

	
	var arr =[];
	var harr = new Array('02','05','08','11','14','17','20','23');
	console.log("현재시간"+whour);
	// sky,pop,pty 정보
	for(var i=0;i<harr.length;i++){
		var h=harr[i]-whour;
		console.log("지금h"+h);
	//	console.log(h);
		if(whour!='00' && whour!='01'){
    		if(h==-1 || h==0 || h==-2){
    			var wnow=harr[i]+"00"; 			
    			wtoday = wyear+""+wmonth+""+wday;
    //			console.log("지금"+wnow);
    		}
		}
		else if(whour=='00' || h>0){
		//	alert("현재시간");
			wtoday = ydyear+""+ydmonth+""+ydday;
			console.log("밤12시"+wtoday);
			var wnow=harr[7]+"00";
			console.log("12시체크"+wnow);
		}
	}
	
	
	// 동네예보
	$.ajax({
        url : "<%= ctxPath%>/t1/weatherhome.tw",
        type : "get",
        timeout: 30000,
        contentType: "application/json",
        data: {"today":wtoday, "now":wnow},
        dataType : "json",
        success : function(data, status, xhr) {
            let dataHeader = data.result.response.header.resultCode;
            let data1 = data.result.response.body.items.item;
            
            if (dataHeader == "00") {
            	for (var i = 0; i < data1.length; i++) {
                    var item = data1[i];
                    switch (item.category) {
                        
                        case "POP":
                        	var pop =parseInt(item.fcstValue);
                            break;
                    }
            	}
               $("#pop").html("강수확률 : "+pop+"%");
            }
           
        },
        error : function(e, status, xhr, data) {
            console.log("error == >");
            console.log(e);
        }
    });

	// 미세먼지
	
		   
   // 시간이 대략 매 30분 0초가 되면 기상청 날씨정보를 자동 갱신해서 가져오려고 함.
      // (매 정시마다 변경되어지는 날씨정보를 정시에 보내주지 않고 대략 30분이 지난다음에 보내주므로)
   
      var now = new Date();
      var minute = now.getMinutes();  // 현재시각중 분을 읽어온다.
      
      if(minute < 30) { // 현재시각중 분이 0~29분 이라면
         weatherTimejugi = (30-minute)*60*1000;  // 현재시각의 분이 0분이라면 weatherTimejugi에 30분을 넣어준다.
                                                 // 현재시각의 분이 5분이라면 weatherTimejugi에 25분을 넣어준다.
                                                 // 현재시각의 분이 29분이라면 weatherTimejugi에 1분을 넣어준다.
      }
      else if(minute == 30) {
         weatherTimejugi = 1000;                 // 현재시각의 분이 30분이라면 weatherTimejugi에 1초 넣어준다.
      }
      else {                                      // 현재시각의 분이 31~59분이라면
         weatherTimejugi = ( (60-minute)+30 )*60*1000;  // 현재시각의 분이 31분이라면 weatherTimejugi에 (29+30)분을 넣어준다.
                                                        // 현재시각의 분이 40분이라면 weatherTimejugi에 (20+30)분을 넣어준다.
                                                        // 현재시각의 분이 59분이라면 weatherTimejugi에 (1+30)분을 넣어준다.
      }
      
		loopshowWeather();
}); // end of 

// ------ 기상청 날씨정보 공공API XML데이터 호출하기 -------- //

function showWeather(){
	   $.ajax({
		  url:"<%= request.getContextPath()%>/opendata/weatherXML.tw",
		  type: "GET",
		  dataType: "XML",
		  success: function(xml){
			  var rootElement = $(xml).find(":root");
			  var localArr = $(rootElement).find("local");
			  console.log(localArr);
			  for(var i=0;i<localArr.length;i++){
            	  var local = $(localArr).eq(i);
            	  
		          var icon = $(local).attr("icon");
		          if(icon ==""){
		        	  icon="없음";
		          }
		      
		          if($(local).text()=="서울"){
						
						var ta = $(local).attr("ta");
						console.log("서울시공ㄴ:"+ta);
						$("#sky").html("<img style='width: 100px; height: 100px;' src='<%= ctxPath%>/resources/images/ody/"+icon+".png'/>");
	  					 $("#t1h").html(ta);
	 		
					}

              } // end of for-------------------------
                 
		                                    
		  }, // end of success: function(xml){}----------------
		  error: function(request, status, error){
	            alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
	      }
	   });
	   
	   $.ajax({
			  type: "GET",
			  url: "http://openapi.seoul.go.kr:8088/504543517764617936396d50484259/json/RealtimeCityAir/1/99",
			  data: {},
			  success: function(response){
					// 마포구의 미세먼지 값만 가져와보기
					let mapo = response["RealtimeCityAir"]["row"][5];
					let guName = mapo['MSRSTE_NM'];
					let guMise = mapo['PM10'];
					let guchoMise = mapo['PM25'];
					console.log(guName, guMise,guchoMise);
					
					var miseStatus="";
					if(guMise<=30){
						miseStatus="좋음" 
					}
					else if(guMise<=80){
						miseStatus="보통"
					}
					else if(guMise<=150){
						miseStatus="나쁨"
					}
					else{
						miseStatus="매우나쁨"
					}
					
					$("#miseStatus").html("미세먼지 : "+miseStatus);
					$("#mise").html("("+guMise+"㎍/m³)");
					
					var miseimage="";
					var chomiseStatus="";
					if(guchoMise<=15){
						chomiseStatus="좋음" 
						
					}
					else if(guchoMise<=35){
						chomiseStatus="보통"
						
					}
					else if(guchoMise<=75){
						chomiseStatus="나쁨"
						
					}
					else{
						chomiseStatus="매우나쁨"
						
					}
		
					if(guMise<=30 && guchoMise<=15){
						miseimage = "<%= ctxPath%>/resources/images/ody/good.png";
					}
					else if((guMise>30 && guMise<=80) || (guchoMise >15 && guchoMise<=35)){
						miseimage = "<%= ctxPath%>/resources/images/ody/normal.png";
					}
					else if((guMise>80 && guMise<=150) || (guchoMise >35 && guchoMise<=75)){
						miseimage = "<%= ctxPath%>/resources/images/ody/sad.png";
					}
					else if(guMise>150 || guchoMise>75 ){
						miseimage = "<%= ctxPath%>/resources/images/ody/devil.png";
					}
					
					$("#chomiseStatus").html("초미세먼지 : "+chomiseStatus);
					$("#chomise").html("("+guchoMise+"㎍/m³)");
					$("#miseimg").html("<img style='width: 100px; height: 100px;' src='"+ miseimage +"'/>");
			  }
			});
			
} // end of function showWeather(){}----------


function loopshowWeather() {
    showWeather();
    
    setTimeout(function() {
          showWeather();   
       }, weatherTimejugi); // 현재시각의 분이 5분이라면 weatherTimejugi가 25분이므로 25분후인 30분에 showWeather();를 실행한다.
    
    setTimeout(function() {
            loopshowWeather();   
       }, weatherTimejugi + (60*60*1000));  // 현재시각의 분이 5분이라면 weatherTimejugi가 25분이므로 25분후인 30분에 1시간을 더한후에 showWeather();를 실행한다.
 }// end of loopshowWeather() --------------------------   



</script>
<div id="today-w" style="float: left; width: 50%; height: 100%; border-right: solid 1px blue;">
 	<span style="font-weight: bold;">오늘의 날씨</span><br><br>
 	<span  id="sky"></span>
 	<div style="padding-top: 30px; display: inline-block; float: right; margin-right: 20px;"><span id="t1h" style="font-size: 25pt; font-weight: bold; margin-left: 10px; "></span><span style="font-size: 25pt; font-weight: bold; ">ºC</span><br>
 	<span style="margin-left: 10px;" id="pop"></span><br><span id="gap"></span></div>
 	</div>
<div style="float: right; width: 50%; height: 100%; " >
 		<span style="text-align: left; font-weight: bold;">미세먼지 농도</span><br>
 		<div align="center" style="margin-top: 20px;">
 		<span id="miseimg"></span><br><br>
 		<span id="miseStatus" ></span><span id="mise"></span><br>
 		<span id="chomiseStatus"></span><span id="chomise"></span>
 		</div>
 	</div>