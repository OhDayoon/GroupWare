$(document).ready(function(){
	// == 상단메뉴 ==
	$("td.t1tour-shortcut").click(function(){
		window.open("travelAgency.tw");
	});
	$("td.mypage").click(function(){
		location.href="mypage.tw";
	});
	$("td.schedule").click(function(){
		location.href="schedule.tw";
	});
	$("td.notice").click(function(){
		location.href="employeeBoard.tw";
	});
	$("td.rsv").click(function(){
		location.href="travelschedule.tw";
	});
	
	// == 사이드바 == 
	$("div#contentLeft").hide();	
	
	// 세부메뉴 보이기
	$("p.menu-btn").click(function(){
		$(this).css("border-left","solid 3px #0071bd");
		$("p.menu-btn").not($(this)).css("border-left","solid 3px #333333");
		showSubmenu($(this).index());
		$("div#sideinfo-content-container").addClass('content-open-style');
	});
	
	// 세부메뉴 닫기
	$("i.closebtn").click(function(){
		$("p.menu-btn").css("border-left","solid 3px #333333");
		$("div#contentLeft").animate({
	        width: "hide"
	    }, 100, "linear");
		$("div#sideinfo-content-container").removeClass('content-open-style');
	});
	
});

// Function Declaration

function showSubmenu(thismenu){
	var submenuIndex = $("div#contentLeft").children().eq(thismenu).index();
	$("div#contentLeft").animate({
	        width: "show"
	 }, 100, "linear");
	$("div#contentLeft").children().hide();
	$("div#contentLeft").children().eq(submenuIndex).show();
}

