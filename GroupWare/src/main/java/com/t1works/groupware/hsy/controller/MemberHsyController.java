package com.t1works.groupware.hsy.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.bwb.service.InterHomepageBwbService;
import com.t1works.groupware.hsy.model.DepartmentHsyVO;
import com.t1works.groupware.hsy.model.DoLateVO;
import com.t1works.groupware.hsy.model.MemberHsyVO;
import com.t1works.groupware.hsy.service.InterMemberHsyService;

@Component
@Controller
public class MemberHsyController {
	
	@Autowired 
	private InterMemberHsyService service;
	
	@Autowired 
	private InterHomepageBwbService service2;
	
	
	// 주소록 매핑 주소
	@RequestMapping(value="/t1/employeeMap.tw")        // 로그인이 필요한 url
	public ModelAndView requiredLogin_employeeMap(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 주소록표기
		// 1) 모든 부서에 대한 정보 가져오기
		List<DepartmentHsyVO> departmentList= service.selectAllDepartment();
		mav.addObject("departmentList", departmentList);
		
		
		// 2) 모든 직원 정보 가져오기
		List<MemberHsyVO> employeeList= service.selectAllMember();
		mav.addObject("employeeList",employeeList);
		
		
		// 3) 페이징바 처리 직원정보 가져오기
		String currentShowPageNo= request.getParameter("currentShowPageNo");
		if(currentShowPageNo == null) currentShowPageNo = "1";
		
		try {
			Integer.parseInt(currentShowPageNo);
		} catch(NumberFormatException e) {
			currentShowPageNo = "1"; 
		}
		
		String sizePerPage= "7"; // 7행씩 고정
		if(!"7".equals(sizePerPage)) sizePerPage = "7";
		
		String searchType= request.getParameter("searchType"); // name, dname, pname, email
		String searchWord= request.getParameter("searchWord");
		
		if(searchType!=null && !"".equals(searchType) && !("name".equalsIgnoreCase(searchType)||"dname".equalsIgnoreCase(searchType)||"pname".equalsIgnoreCase(searchType)||"email".equalsIgnoreCase(searchType))) {
			searchType="name";
		}
		
		if(searchType!=null && !"email".equalsIgnoreCase(searchType)) { // 대소문자 구분없이 검색가능하도록 설정
			searchWord=searchWord.toUpperCase();
		}
		else if("email".equalsIgnoreCase(searchType)) {
			searchWord=searchWord.toLowerCase();
		}
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("currentShowPageNo", currentShowPageNo);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("searchType", searchType);  // null인경우 존재
		paraMap.put("searchWord", searchWord);  // null인경우 존재 
		int totalPage = service.selectTotalPage(paraMap);
		
		if( Integer.parseInt(currentShowPageNo) > totalPage || Integer.parseInt(currentShowPageNo) < 1) {
			currentShowPageNo = "1";
			paraMap.put("currentShowPageNo", currentShowPageNo);
		}
		
		List<MemberHsyVO> pagingEmployeeList= service.selectPagingMember(paraMap);
		mav.addObject("pagingEmployeeList", pagingEmployeeList);
		
		if(searchType!=null) searchType=searchType.toLowerCase(); // 대문자로 장난친 경우 소문자로 변경해서 넣어주기 위해 tolowerCase사용
		
		mav.addObject("searchType", searchType);  // view단에 값을 넣어주기 위한 용도 
		mav.addObject("searchWord", searchWord);  // view단에 값을 넣어주기 위한 용도
		
		// 4) 페이징바 생성
		String pageBar= "";
		int blockSize= 5;
		int loop=1;
		int pageNo=0;
		
		pageNo= ((Integer.parseInt(currentShowPageNo)-1)/blockSize) * blockSize + 1 ;
		if(searchType == null) searchType="";
		
		if(searchWord == null) searchWord="";
		
		if(pageNo != 1) {
			pageBar += "&nbsp;<a href='employeeMap.tw?currentShowPageNo=1&searchType="+searchType+"&searchWord="+searchWord+"'>[맨처음]</a>&nbsp;"; 
			pageBar += "&nbsp;<a href='employeeMap.tw?currentShowPageNo="+(pageNo-1)+"&searchType="+searchType+"&searchWord="+searchWord+"'>[이전]</a>&nbsp;";
		}
		
		while(!(loop>blockSize || pageNo > totalPage) ) {
			if(pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "&nbsp;<span style='color:#fff; background-color: #003d66; font-weight:bold; padding:2px 4px;'>"+pageNo+"</span>&nbsp;";
			}
			else {
				pageBar += "&nbsp;<a href='employeeMap.tw?currentShowPageNo="+pageNo+"&searchType="+searchType+"&searchWord="+searchWord+"'>"+pageNo+"</a>&nbsp;";
			}
			loop++;
			pageNo++;
		}// end of while-----------------------------
		
		if(pageNo <= totalPage) {
			pageBar += "&nbsp;<a href='employeeMap.tw?currentShowPageNo="+pageNo+"&searchType="+searchType+"&searchWord="+searchWord+"'>[다음]</a>&nbsp";
			pageBar += "&nbsp;<a href='employeeMap.tw?currentShowPageNo="+totalPage+"&searchType="+searchType+"&searchWord="+searchWord+"'>[마지막]</a>&nbsp";
		}
		
		mav.addObject("pageBar", pageBar);
		
		mav.setViewName("hsy/employee/employeeMap.gwTiles");
		return mav;
		
	} // end of public ModelAndView employeeMap(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {--------------------------------------------
	
	
	
	// post방식으로 한명의 직원정보 조회해오는 ajax 매핑주소
	@ResponseBody
	@RequestMapping(value="/t1/employeeInfoAjaxHsy.tw", produces="text/plain;charset=UTF-8", method= {RequestMethod.POST})
	public String employeeInfoAjaxHsy(HttpServletRequest request) {
		
		String employeeid= request.getParameter("employeeid");
		
		// 1) 사번에 해당하는 직원정보 가져오기
		MemberHsyVO mvo= service.employeeInfoAjaxHsy(employeeid);

		// 2) mvo의 정보를 json에 넣어주기
		JSONObject jsonObj= new JSONObject();  
		jsonObj.put("employeeid", mvo.getEmployeeid());
		jsonObj.put("email", mvo.getEmail());
		jsonObj.put("name", mvo.getName());
		jsonObj.put("cmobile", mvo.getCmobile());
		jsonObj.put("mobile", mvo.getMobile());
		jsonObj.put("dname", mvo.getDname());
		jsonObj.put("pname", mvo.getPname());
		jsonObj.put("duty", mvo.getDuty());
		jsonObj.put("fileName", mvo.getFileName());
		
		// System.out.println(mvo.getFileName());
		
		// 3) 사번에 해당하는 직원의 오늘의 근태 정보 가져오기 => 현재  병가, 반차, 연차, 경조휴가, 출장 여부 표시
		Map<String,String> attendanceStateMap= service.getAttendanceState(employeeid);
		
	
		String attendanceSeq= "근무 중";  // 정상출근인 경우 또는 반차사용 이외 시간에 출근한 상태인 경우 attendanceSeq == "근무중"이 된다
		
		// 반차인 경우 오전반차, 오후반차에 따라 (14시 00분 기준) 현재시각과 비교해 상태 표시여부를 달리 해줘야 한다
		// 현재시간이 14시 이전인지 이후인지 알아오기
		int n= service.isTwoBefore(); 
		
		/*
		 	n==0 : 현재시간은 2시 이전이다
		 	n==1 : 현재시간은 2시 이후이다 (2시 포함) 
		*/
		
		// 근태 정보는 병가, 반차, 연차, 경조휴가, 출장 중 하나에만 해당된다
		if(!"0".equals(attendanceStateMap.get("sickleave"))) attendanceSeq="병가";         
		else if(!"0".equals(attendanceStateMap.get("dayoff"))) attendanceSeq="연차";    
		else if(!"0".equals(attendanceStateMap.get("congoff"))) attendanceSeq="경조 휴가";    
		else if(!"0".equals(attendanceStateMap.get("businesstrip"))) attendanceSeq="출장";    
		
		else if(!"0".equals(attendanceStateMap.get("afternoonoff1"))) {  // 오전반차
			if(n==1) attendanceSeq="근무 중" ; // 반차시간 지나고 출근상태
			else attendanceSeq="오전 반차";  // 오전반차 상태
		}
		else if(!"0".equals(attendanceStateMap.get("afternoonoff2"))) {  // 오후반차
			if(n==1) attendanceSeq="오후 반차" ; // 오후반차 상태 
			else attendanceSeq="근무 중";  // 반차시간 이전에 출근상태
		}

		jsonObj.put("attendanceSeq", attendanceSeq);
		
		return jsonObj.toString();
	
	} // end of public String employeeInfoAjaxHsy(HttpServletRequest request) {-----
	
	
	
	// 월급관리 url 매핑
	@RequestMapping(value="/t1/salary.tw")        // 로그인이 필요한 url
	public ModelAndView requiredLogin_salary(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser= (MemberBwbVO) session.getAttribute("loginuser");
		
		// 1) 특정 직원의 입사일 가져오기 => 입사일을 년도, 월, 일 단위로 나눠서 뷰단으로 넘기기
		String hiredate= loginuser.getHiredate();
		
		// System.out.println(loginuser.getHiredate());
		// 2010-07-10 00:00:00.0
		
		mav.addObject("hireYear", hiredate.substring(0,4));
		mav.addObject("hireMonth", hiredate.substring(5,7));
		mav.addObject("hireDay", hiredate.substring(8,10));
		
		// 2) 현재날짜 가져오기 => 년도, 월, 일 단위로 나눠서 뷰단으로 넘기기
		Calendar currentDate = Calendar.getInstance();
		int currentYear = currentDate.get(Calendar.YEAR);
		int currentMonth = currentDate.get(Calendar.MONTH)+1;
		
		mav.addObject("currentYear", currentYear);
		mav.addObject("currentMonth", currentMonth);
		
		mav.setViewName("hsy/employee/salary.gwTiles");
		return mav;
		
	} // end of public ModelAndView requiredLogin_salary(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) ----
	
	
	
	// 입력 비밀번호가 일치하는지 확인하는 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/passwdCheckForSalary.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String passwdCheckForSalary(HttpServletRequest request) {
		
		String employeeid=request.getParameter("employeeid");
		String passwd=request.getParameter("passwd");
		
		Map<String,String> paraMap = new HashMap<>();
        paraMap.put("employeeid", employeeid);
        paraMap.put("passwd", passwd);
        
        // 입력한 비밀번호가 일치하는지에 따라 json에 값 넣어주기
        MemberBwbVO mvo= service2.selectMember(paraMap);
        
        JSONObject jsonObj= new JSONObject();
		if(mvo==null) jsonObj.put("n", 0);   // 비밀번호가 일치하지 않는 경우
		else jsonObj.put("n", 1); // 비밀번호가 일치하는 경우
		
		
		return jsonObj.toString();
		
	} // end of public String passwdCheckForSalary(MemberHsyVO mvo) {----
	

	// 선택한 년도의 월급이 존재하는 월들의 정보 가져오는 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/salaryListInfoByYear.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String salaryListInfoByYear(HttpServletRequest request) {
		
		
		String employeeid= request.getParameter("employeeid");
	
		// 1) 특정직원의 소속, 직위, 기본급 가져오기
		MemberHsyVO mvo= service.employeeInfoAjaxHsy(employeeid);
		
		JsonObject jsonObj= new JsonObject();
		jsonObj.addProperty("dname", mvo.getDname());
		jsonObj.addProperty("pname", mvo.getPname());
		jsonObj.addProperty("salary", mvo.getSalary());
		
		return new Gson().toJson(jsonObj);
		
	} // end of public String salaryListInfoByYear(HttpServletRequest request)---
	
	
	// 월급명세서 상페 페이지 매핑 url
	@RequestMapping(value="/t1/salaryDetailForm.tw", method= {RequestMethod.POST})
	public ModelAndView salaryDetailForm(HttpServletRequest request, ModelAndView mav) {
		
		// 1) 월급명세서 출력하기를 선택한 년도와 월
		String employeeid= request.getParameter("employeeid");
		String year= request.getParameter("year");
		String month= request.getParameter("month");
		
		// 월이 한자리인 경우 앞에 0 붙여서 넘기기
		if(month.length()==1) month="0"+month;
		mav.addObject("employeeid", employeeid);
		mav.addObject("year", year);
		mav.addObject("month", month);
		
		// 2) 현재날짜 가져오기
		Calendar currentDate = Calendar.getInstance();
		int currentYear = currentDate.get(Calendar.YEAR);
		String currentMonth = String.valueOf(currentDate.get(Calendar.MONTH)+1);
		String currentDay= String.valueOf(currentDate.get(Calendar.DATE));
		
		// 월이 한자리인 경우 앞에 0 붙여서 넘기기
		if(currentMonth.length()==1) currentMonth="0"+currentMonth;

		// 일이 한자리인 경우 앞에 0 붙여서 넘기기
		if(currentDay.length()==1) currentDay="0"+currentDay;
		
		mav.addObject("currentYear", currentYear);
		mav.addObject("currentMonth", currentMonth);
		mav.addObject("currentDay",currentDay);
		
		// 3-1) 월급명세서에 필요한 정보 가져오기 => 인적사항 (사번/성명/소속/직급) + 건당성과금(성과금 계산을 위함)
		MemberHsyVO mvo= service.employeeInfoAjaxHsy(employeeid);
		mav.addObject("mvo", mvo);
		
		// 3-2) 월급명세서에 필요한 정보 가져오기 => 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수)
		
		// 해당 직원의 근태결재 승인처리 완료된 문서번호 가져오기
		List<String> anoList= service.getAttendanceAno(employeeid);
		
		// 근태결재 승인처리 완료된 문서번호 가공하기 => in절에 사용될 수 있도록 데이터 가공
		String anoForIn="";
		if(anoList.size()==0) anoForIn="-1";
		else{
			for(int i=0; i<anoList.size();i++) {   
	
				String str= (i < anoList.size()-1) ? "," : "";
				anoForIn+= anoList.get(i)+str;
			} // end of for------------------------
		}
	
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("date",year+"-"+month);
		paraMap.put("anoForIn", anoForIn);
		
		// 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수) 가져오기
		Map<String,Integer> attendanceMap= service.getAttendanceForSalary(paraMap);
		
		
		// 정상출근 일 수 => 해당 년도, 월의 평일 수에서 휴가사용일 제외 (해당 년도 월의 평일수를 구해서 view단으로 넘기기 => 자바스크립트에서 계산)
		// ========== 특정 년도, 월의 평일 수 구하기 사전작업
		HttpSession session = request.getSession();
		MemberBwbVO loginuser= (MemberBwbVO) session.getAttribute("loginuser");
		
		String hiredate= loginuser.getHiredate();
		
		String hireYear= hiredate.substring(0,4);
		String hireMonth= hiredate.substring(5,7);
		String hireDay= hiredate.substring(8,10);
		
		int nYear= Integer.parseInt(year);
		
		String startDate = year+month+"01"; // 입사년도, 월과 다른 경우 시작일은 01일 부터 카운트
		String endDate = "";
		 
		if(year.equals(hireYear) && month.equals(hireMonth)) { // 입사년도, 월과 같은 경우 시작일은 입사일부터 카운트
			startDate=year+month+hireDay; // 입사년도, 월과 같은 경우 시작일은 입사일부터 카운트
		}
		
		if("02".equals(month)) {  // 월급명세서 출력 선택날짜가 2월인 경우 윤달 고려
			if(nYear%4==0) endDate=year+month+29; // 윤달인 경우
			else endDate=year+month+28; // 윤달이 아닌 경우
		}
		else if("04".equals(month)||"06".equals(month)||"09".equals(month)||"11".equals(month)) { // 마지막날이 30일인 경우
			endDate=year+month+30;
		}
		else { // 마지막날이 31일인 경우
			endDate=year+month+31;
		}
		
		// System.out.println(startDate);
		// System.out.println(endDate);
		
		// ========== 특정 년도, 월의 평일 수 구하기
		
	    // 공휴일 설정 (신정, 삼일절, 어린이날, 성탄절 설정)
	    List<Map<String,String>> holidayList = new ArrayList<>();
	    Map<String, String> holidayMap = new HashMap<>();

	    holidayMap.put("holidayDt", year+"0101");	//신정
	    holidayList.add(holidayMap);

	    holidayMap = new HashMap<>();
	    holidayMap.put("holidayDt", year+"0301");	//삼일절
	    holidayList.add(holidayMap);

	    holidayMap = new HashMap<>();
	    holidayMap.put("holidayDt", year+"0505");	//어린이날
	    holidayList.add(holidayMap);

	    holidayMap = new HashMap<>();
	    holidayMap.put("holidayDt", year+"1225");	//성탄절
	    holidayList.add(holidayMap);


	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    int workDays=0;
	    
	    try{
			Calendar start = Calendar.getInstance();
	        start.setTime(sdf.parse(startDate)); //시작일 날짜 설정

	      	Calendar end = Calendar.getInstance();
	      	end.setTime(sdf.parse(endDate)); //종료일 날짜 설정

	      	Calendar hol = Calendar.getInstance();

			int workingDays = 0;
	    	int holDays = 0;
	    	
	      	while (!start.after(end)) {
	      		int day = start.get(Calendar.DAY_OF_WEEK);	 //주말인지 확인하기
	      		int holday = 0; 

	      		if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)){
		      		workingDays++;	//평일 수 증가
		      	}
	      		
		      	//시작일과 공휴일이 같을때 공휴일이 주말인지 체크 => 주말이 아니면 holDays +1 증가
		      	if(!holidayList.isEmpty()){
		      		for(int i=0;i<holidayList.size();i++){
			      		hol.setTime(sdf.parse((String)holidayList.get(i).get("holidayDt").toString()));	//실제 공휴일 날짜 설정
			      		holday = hol.get(Calendar.DAY_OF_WEEK); //주말인지 확인하기
			      		if (start.equals(hol) &&(holday != Calendar.SATURDAY) && (holday != Calendar.SUNDAY)){	//공휴일수: 공휴일이 평일인경우 +1
				      		holDays++;
			      		}	  
		      		} // end of for-----------------
		      	}
		      	start.add(Calendar.DATE, 1);
	      	} // end of while-----------------------
	      	
	    // System.out.printf("최종일수 %d", workingDays-holDays); //평일 수 - 평일인 공휴일수

	    workDays = workingDays-holDays;  // 해당 월의 총 근무수 (연차를 사용하지 않은 경우)
				
		}catch(Exception e){
			e.printStackTrace();
		}
		
	    
	    // 3-3) 월급명세서에 필요한 정보 가져오기 => 근속수당 계산을 위해 근속년도 view단으로 넘기기
	    int continueYear= nYear-Integer.parseInt(hireYear);
	    
	    if(continueYear%5==0  && hireMonth.equals(month)) { // 근속수당 100만원 지급 (5년마다)
	    	mav.addObject("continueYear", continueYear);
	    }
	    else { // 근속수당 해당 없는 경우
	    	mav.addObject("continueYear", 0);
	    }
	    
		mav.addObject("workDays", workDays);
		mav.addObject("attendanceMap", attendanceMap);
		
		
		// 3-4) 월급명세서에 필요한 정보 가져오기 => 해당 년도, 월의  총 야근 시간 가져오기
		int totalLateWorkTime= service.getTotalLateWorkTime(paraMap);
		mav.addObject("totalLateWorkTime", totalLateWorkTime); // 야근시간이 없는 경우 0
		
		
		// 3-5) 월급명세서에 필요한 정보 가져오기 => 해당 년도, 월의 실적건수와 지난달의 실적건수 가져오기
		Map<String,String> doneCntMap= service.getDoneCnt(paraMap);
		mav.addObject("doneCntMap", doneCntMap);
		
		mav.setViewName("hsy/salaryDetail");
		return mav;
		
	} // end of public ModelAndView salaryDetailForm(HttpServletRequest request, ModelAndView mav) {------
	
	
	// 조직도 매핑 url
	@RequestMapping(value="/t1/employeeChart.tw")        // 로그인이 필요한 url
	public ModelAndView requiredLogin_employeeChart(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 계층형 조직도를 가져오기
		List<MemberHsyVO> mvoList= service.hierarchicalEmployeeList();
		mav.addObject("mvoList",mvoList);
		mav.setViewName("hsy/employee/employeeChart.gwTiles");
		return mav;
		
	} // end of public ModelAndView requiredLogin_employeeChart(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {---
	
	
	// 성과금, 야근수당 정보 페이지 매핑 url
	@RequestMapping(value="/t1/salaryDetail.tw")        // 로그인이 필요한 url
	public ModelAndView requiredLogin_salaryDetail(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String from= request.getParameter("from"); // 홈페이지에서 야근 찍고 넘어온 경우
		if(from==null) from=""; // 홈페이지에서 야근 안찍고 사이드메뉴 야근수당 정보페이지로 넘어온 경우
		mav.addObject("from",from);
		
		mav.setViewName("hsy/employee/perfNightDetail.gwTiles");
		return mav;
	} // end of public ModelAndView requiredLogin_salaryDetail(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {----
	
	
	// 성과금 리스트 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/getBonusList.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getBonusList(HttpServletRequest request) {
		
		String employeeid= request.getParameter("employeeid");
		String sortOption= request.getParameter("sortOption");
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("sortOption", sortOption);
		
		// 1) 처리 업무가 존재하는 날짜와 날짜별 처리 업무 수 가져오기
		List<Map<String,String>> bonusDateList= service.getBonusDate(paraMap);
		
		JSONArray jsonArr= new JSONArray();
		
		if(bonusDateList.size()!=0) { // 실적이 존재하는 날짜가 있는 경우
			for(int i=0; i<bonusDateList.size(); i++) {
	
				// 2) 전달의 실적이 존재하는 지 확인
				String bonusDate1= bonusDateList.get(i).get("endDate");
				
				String bonusDate2="";
				if(i<bonusDateList.size()-1) { // 실적이 존재하는 마지막 달이 아닌 경우
					bonusDate2= bonusDateList.get(i+1).get("endDate");
				}
				
				String[] bonusDate1Arr= bonusDate1.split("-"); // 2021-03
				
				// 일시 표시해주기 위해 데이터 가공
				String date= bonusDate1Arr[0]+"년 "+bonusDate1Arr[1]+"월";
				
				String prevMonth="";
				
				// 2-1) 전달 날짜 만들기
				
				// 실적이 존재하는 마지막 달인 경우 전달처리 하지 않는다
				if("01".equals(bonusDate1Arr[1]) && i!=bonusDateList.size()-1) { // 1월인 경우 전달이 12월이므로 다른 월들과 다르게 처리
					prevMonth="12";  // 전달처리
					bonusDate1Arr[0]= String.valueOf(Integer.parseInt(bonusDate1Arr[0])-1); // 전년처리  
				}
				else if (i!=bonusDateList.size()-1) {
					prevMonth= String.valueOf(Integer.parseInt(bonusDate1Arr[1])-1); 
					if(prevMonth.length()==1) prevMonth="0"+prevMonth;
				}
				
				bonusDate1Arr[1]= prevMonth;
				
				String prevDate= bonusDate1Arr[0]+"-"+bonusDate1Arr[1];
				
				String goalCnt= "";
				String prevCnt="";
				
				// 2-2) 전달에 실적이 존재하는지 비교하기
				
				// 실적이 존재하는 마지막 달인 경우 목표치는 무조건 2건
				if(!bonusDate2.equals(prevDate) || i==bonusDateList.size()-1 ) {  // 전달에 실적이 없는 경우 목표치는 2건
					prevCnt="0";
					goalCnt= "2";
				}
				else {  // 전달에 실적이 있는 경우 목표치는 전달실적+2건
					prevCnt=String.valueOf(Integer.parseInt(bonusDateList.get(i+1).get("cnt")));
					goalCnt= String.valueOf(Integer.parseInt(prevCnt)+2);
				}
			
				JSONObject jsonObj= new JSONObject();
				
				// 3) 달성률 계산하기 (실적/목표*100) => 소수 첫째자리까지 표시
				
				double nAchievementRate= Math.round((( Integer.parseInt(bonusDateList.get(i).get("cnt"))/Integer.parseInt(goalCnt) * 100)*10)/10.0);
				String achievementRate= String.valueOf(nAchievementRate)+" %";
				
				// 4) 성과금 계산하기 => 달성건-목표건 * 건당 성과금
				
				// 직급에 맞는 건당성과금 가져오기
				String commissionpercase= service.getCommissionpercase(employeeid);
				int achievementcnt= Integer.parseInt(bonusDateList.get(i).get("cnt"))-Integer.parseInt(goalCnt);
				
				if(achievementcnt>0) { // 성과금이 존재하는 경우
					int bonus= achievementcnt*Integer.valueOf(commissionpercase);
					
					jsonObj.put("date",date); // 일시
					jsonObj.put("prevCnt",prevCnt);// 전월실적
					jsonObj.put("goalCnt",goalCnt); // 목표건
					jsonObj.put("doneCnt",bonusDateList.get(i).get("cnt")); // 달성건
					jsonObj.put("achievementRate",achievementRate); // 달성률
					jsonObj.put("bonus",bonus); // 성과금
				}
				else { // 성과금이 존재하지 않는 경우
					continue; 
				}
				
				jsonArr.put(jsonObj);
				
			} // end of for(int i=0; i<bonusDateList.size(); i++) {
		}
		
		return jsonArr.toString();
		// 실적이 존재하지 않으면 jsonArr.length==0 이 된다
		// 실적이 존재하지만 성과금이 없는 경우도 jsonArr.length==0 이 된다
		
	} // end of public String getBonusList(HttpServletRequest request) {-------
	
	
	// 야근수당 리스트  ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/getOverNightList.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String getOverNightList(HttpServletRequest request) {
		
		String employeeid= request.getParameter("employeeid");
		String sortOption= request.getParameter("sortOption");
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("sortOption", sortOption);
		
		// 1) 야근수당 리스트에 보여줄 항목 가져오기 
		List<DoLateVO> dlvoList= service.getOverNightList(paraMap);
		
		JSONArray jsonArr= new JSONArray();
		
		// 야근 기록이 존재하는 경우
		if(dlvoList.size()!=0) {
			
			for(DoLateVO dlvo : dlvoList) {
			
				JSONObject jsonObj= new JSONObject();
				
				// 2) 야근 일시를 보여주기 위해서 데이터 가공
				String[] doLateSysdateArr= dlvo.getDoLateSysdate().split("-");
				String doLateSysdate= doLateSysdateArr[0]+"년 "+doLateSysdateArr[1]+"월 "+doLateSysdateArr[2]+"일";
				dlvo.setDoLateSysdate(doLateSysdate);
				
				// 3) 야근이 종료된 시간을 보여주기 위해서 데이터 가공
				String[] endLateTimeArr= dlvo.getEndLateTime().split(":");
				String endLateTime= endLateTimeArr[0]+"시 "+endLateTimeArr[1]+"분";
				dlvo.setEndLateTime(endLateTime);
				
				jsonObj.put("doLateSysdate", dlvo.getDoLateSysdate());
				jsonObj.put("doLateTime", dlvo.getDoLateTime());
				jsonObj.put("doLateWhy", dlvo.getDoLateWhy());
				jsonObj.put("endLateTime", dlvo.getEndLateTime());
				jsonObj.put("overnightPay", dlvo.getOvernightPay());
				
				jsonArr.put(jsonObj);
			} // end of for(DoLateVO dlvo : dlvoList) {-------
		}
		
		return jsonArr.toString();
		//야근 기록이 존재하지 않는 경우 jsonArr.length==0이 된다
		
	} // end of public String getOverNightList(HttpServletRequest request) {------
	
	
	// 그룹웨어 홈페이지 퀵메뉴에 필요한 정보 가져오는 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/quickMenuInfo.tw", method= {RequestMethod.POST})
	public String quickMenuInfo(HttpServletRequest request) {
	
		String employeeid= request.getParameter("employeeid");
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("employeeid", employeeid);
		
		// 1) 특정 직원의 읽지않은 메일수, 결재중인 문서 수, 14일 이내에 결재완료된 문서 수 가져오기
		Map<String,Integer> quickMenuInfoMap= service.getquickMenuInfo(paraMap);
		
		JsonObject jsonObj= new JsonObject();
		jsonObj.addProperty("notReadCnt", quickMenuInfoMap.get("notReadCnt"));
		jsonObj.addProperty("ingDocuCnt", quickMenuInfoMap.get("ingDocuCnt"));
		jsonObj.addProperty("doneDocuCnt", quickMenuInfoMap.get("doneDocuCnt"));
		
		return new Gson().toJson(jsonObj);
		
	} // end of public String quickMenuInfo(HttpServletRequest request) {---------------------
	
	
	
	// 월급명세서 엑셀파일 다운로드 매핑 url
	@RequestMapping(value="/excel/downloadSalaryExcelFile.tw", method= {RequestMethod.POST}) 
	public String downloadExcelFile(HttpServletRequest request, Model model) {
		
		// 엑셀파일에 필요한 정보 가져오기
		// 1) 월급명세서 출력하기를 선택한 년도와 월
		String employeeid= request.getParameter("employeeid");
		String year= request.getParameter("year");
		String month= request.getParameter("month");
		
		// 월이 한자리인 경우 앞에 0 붙여서 sql문으로 사용
		if(month.length()==1) month="0"+month;
		
		// 2) 현재날짜 가져오기
		Calendar currentDate = Calendar.getInstance();
		int currentYear = currentDate.get(Calendar.YEAR);
		String currentMonth = String.valueOf(currentDate.get(Calendar.MONTH)+1);
		String currentDay= String.valueOf(currentDate.get(Calendar.DATE));
		
		// 월이 한자리인 경우 앞에 0 붙여서 넘기기
		if(currentMonth.length()==1) currentMonth="0"+currentMonth;

		// 일이 한자리인 경우 앞에 0 붙여서 넘기기
		if(currentDay.length()==1) currentDay="0"+currentDay;
		
		// 3-1) 월급명세서에 필요한 정보 가져오기 => 인적사항 (사번/성명/소속/직급) + 건당성과금(성과금 계산을 위함)
		MemberHsyVO mvo= service.employeeInfoAjaxHsy(employeeid);
		
		// 3-2) 월급명세서에 필요한 정보 가져오기 => 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수)
		// 해당 직원의 근태결재 승인처리 완료된 문서번호 가져오기
		List<String> anoList= service.getAttendanceAno(employeeid);
		
		// 근태결재 승인처리 완료된 문서번호 가공하기 => in절에 사용될 수 있도록 데이터 가공
		String anoForIn="";
		if(anoList.size()==0) anoForIn="-1";
		else{
			for(int i=0; i<anoList.size();i++) {   

			String str= (i < anoList.size()-1) ? "," : "";
			anoForIn+= anoList.get(i)+str;
			} // end of for------------------------
		}
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("date",year+"-"+month);
		paraMap.put("anoForIn", anoForIn);
		
		// 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수) 가져오기
		Map<String,Integer> attendanceMap= service.getAttendanceForSalary(paraMap);
		
		// 정상출근 일 수 => 해당 년도, 월의 평일 수에서 휴가사용일 제외
		// ========== 특정 년도, 월의 평일 수 구하기 사전작업
		HttpSession session = request.getSession();
		MemberBwbVO loginuser= (MemberBwbVO) session.getAttribute("loginuser");
		
		String hiredate= loginuser.getHiredate();
		
		String hireYear= hiredate.substring(0,4);
		String hireMonth= hiredate.substring(5,7);
		String hireDay= hiredate.substring(8,10);
		
		int nYear= Integer.parseInt(year);
		
		String startDate = year+month+"01"; // 입사년도, 월과 다른 경우 시작일은 01일 부터 카운트
		String endDate = "";
		 
		if(year.equals(hireYear) && month.equals(hireMonth)) { // 입사년도, 월과 같은 경우 시작일은 입사일부터 카운트
			startDate=year+month+hireDay; // 입사년도, 월과 같은 경우 시작일은 입사일부터 카운트
		}
		
		if("02".equals(month)) {  // 월급명세서 출력 선택날짜가 2월인 경우 윤달 고려
			if(nYear%4==0) endDate=year+month+29; // 윤달인 경우
			else endDate=year+month+28; // 윤달이 아닌 경우
		}
		else if("04".equals(month)||"06".equals(month)||"09".equals(month)||"11".equals(month)) { // 마지막날이 30일인 경우
			endDate=year+month+30;
		}
		else { // 마지막날이 31일인 경우
			endDate=year+month+31;
		}
		
		// System.out.println(startDate);
		// System.out.println(endDate);
		
		// ========== 특정 년도, 월의 평일 수 구하기
		
	    // 공휴일 설정 (신정, 삼일절, 어린이날, 성탄절 설정)
	    List<Map<String,String>> holidayList = new ArrayList<>();
	    Map<String, String> holidayMap = new HashMap<>();

	    holidayMap.put("holidayDt", year+"0101");	//신정
	    holidayList.add(holidayMap);

	    holidayMap = new HashMap<>();
	    holidayMap.put("holidayDt", year+"0301");	//삼일절
	    holidayList.add(holidayMap);

	    holidayMap = new HashMap<>();
	    holidayMap.put("holidayDt", year+"0505");	//어린이날
	    holidayList.add(holidayMap);

	    holidayMap = new HashMap<>();
	    holidayMap.put("holidayDt", year+"1225");	//성탄절
	    holidayList.add(holidayMap);


	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    int workDays=0;
	    
	    try{
			Calendar start = Calendar.getInstance();
	        start.setTime(sdf.parse(startDate)); //시작일 날짜 설정

	      	Calendar end = Calendar.getInstance();
	      	end.setTime(sdf.parse(endDate)); //종료일 날짜 설정

	      	Calendar hol = Calendar.getInstance();

			int workingDays = 0;
	    	int holDays = 0;
	    	
	      	while (!start.after(end)) {
	      		int day = start.get(Calendar.DAY_OF_WEEK);	 //주말인지 확인하기
	      		int holday = 0; 

	      		if ((day != Calendar.SATURDAY) && (day != Calendar.SUNDAY)){
		      		workingDays++;	//평일 수 증가
		      	}
	      		
		      	//시작일과 공휴일이 같을때 공휴일이 주말인지 체크 => 주말이 아니면 holDays +1 증가
		      	if(!holidayList.isEmpty()){
		      		for(int i=0;i<holidayList.size();i++){
			      		hol.setTime(sdf.parse((String)holidayList.get(i).get("holidayDt").toString()));	//실제 공휴일 날짜 설정
			      		holday = hol.get(Calendar.DAY_OF_WEEK); //주말인지 확인하기
			      		if (start.equals(hol) &&(holday != Calendar.SATURDAY) && (holday != Calendar.SUNDAY)){	//공휴일수: 공휴일이 평일인경우 +1
				      		holDays++;
			      		}	  
		      		} // end of for-----------------
		      	}
		      	start.add(Calendar.DATE, 1);
	      	} // end of while-----------------------
	      	
	    // System.out.printf("최종일수 %d", workingDays-holDays); //평일 수 - 평일인 공휴일수

	    workDays = workingDays-holDays;  // 해당 월의 총 근무수 (연차를 사용하지 않은 경우)
				
		}catch(Exception e){
			e.printStackTrace();
		}
	    
	    // 정상출근 일 수 계산
	    int inoutCnt= workDays- (attendanceMap.get("afdates")+attendanceMap.get("sldates")+attendanceMap.get("daydates")+
	    			  attendanceMap.get("congdates")+attendanceMap.get("lateday"));
	    
	    
	    // 3-3) 월급명세서에 필요한 정보 가져오기 => 근속수당 계산을 위해 근속년도 view단으로 넘기기
	    int continueYear= nYear-Integer.parseInt(hireYear);
	    
	    // 근속수당 100만원 지급 (5년마다) => 해당사항 없는 경우 0이 된다
	    if(!(continueYear%5==0  && hireMonth.equals(month))) { 
	    	continueYear=0;
	    }
		
		// 3-4) 월급명세서에 필요한 정보 가져오기 => 해당 년도, 월의  총 야근 시간 가져오기
		int totalLateWorkTime= service.getTotalLateWorkTime(paraMap); // 야근시간이 없는 경우 0
		int totalLateWorkTimeMoney= totalLateWorkTime*10000;
		
		
		// 3-5) 월급명세서에 필요한 정보 가져오기 => 해당 년도, 월의 실적건수와 지난달의 실적건수 가져오기
		Map<String,String> doneCntMap= service.getDoneCnt(paraMap);
		
		int specificDoneCnt= Integer.parseInt(doneCntMap.get("specificDoneCnt"));
		int prevDoneCnt= Integer.parseInt(doneCntMap.get("prevDoneCnt"));
		int goalCnt= prevDoneCnt+2;
		
		// 성과금 계산하기
		int bonus= (specificDoneCnt-goalCnt) * Integer.parseInt(mvo.getCommissionpercase());
		
		
		// 3-6) 지급합계 구하기 => 기본급+근속수당+야근수당+성과금+식대
		int totalSalary=0;
		totalSalary+=Integer.parseInt(mvo.getSalary());
		
		if(continueYear>0) totalSalary+= 1000000;
		if(totalLateWorkTimeMoney>0) totalSalary+=totalLateWorkTimeMoney;
		if(bonus>0) totalSalary+=bonus;
		
		totalSalary+=(workDays*5000);
		
		// === 엑셀파일에 필요한 데이터를 가지고 엑셀 시트 생성하기 ===
		// 1) 시트생성
		SXSSFWorkbook workbook = new SXSSFWorkbook();
	    SXSSFSheet sheet = workbook.createSheet(paraMap.get("date")+" 월급명세서");
		
	    // 2) 시트 열 너비 설정
        sheet.setColumnWidth(0, 2000);  // A열
        sheet.setColumnWidth(1, 2000);  // B열
        sheet.setColumnWidth(2, 2000);	// C열
        sheet.setColumnWidth(3, 4000);	// D열
        sheet.setColumnWidth(4, 3000);	// E열
        sheet.setColumnWidth(5, 3000);	// F열
        sheet.setColumnWidth(6, 4000);	// G열
        sheet.setColumnWidth(7, 3000);	// H열
        sheet.setColumnWidth(8, 3000);	// I열
        
	    Row row= null;
	    Cell cell= null;
		
	    // 3) CellStyle 설정 
	    // 3-1) 제목: 파란배경색, 가운데정렬(가로/세로), 하얀글씨, 굵은 글씨
	    CellStyle titleStyle = workbook.createCellStyle();
	    titleStyle.setAlignment(HorizontalAlignment.CENTER);
	    titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    titleStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
	    titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	 
	    Font titleFont = workbook.createFont();
	    titleFont.setBold(true);
	    titleFont.setColor(IndexedColors.WHITE.getIndex());
	    titleFont.setFontHeight((short)450);
	    titleStyle.setFont(titleFont); 
	    
	    
	    // 3-2) 헤더:  회색배경색, 가운데정렬 (가로/세로), 굵은 글씨, 상하좌우 굵은 테두리
	    CellStyle headerStyle = workbook.createCellStyle();
	    headerStyle.setAlignment(HorizontalAlignment.CENTER);
	    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    headerStyle.setBorderTop(BorderStyle.THICK);
	    headerStyle.setBorderBottom(BorderStyle.THICK);
	    headerStyle.setBorderLeft(BorderStyle.THICK);
	    headerStyle.setBorderRight(BorderStyle.THICK);
	    
	    Font headerFont = workbook.createFont();
	    headerFont.setBold(true);
	    headerFont.setFontHeight((short)250);
	    headerStyle.setFont(headerFont); 
	   
	    
	    // 3-3) th1: 가운데정렬 (가로/세로), 굵은 글씨, 위쪽 굵은 테두리
	    CellStyle thStyle1 = workbook.createCellStyle();
	    thStyle1.setAlignment(HorizontalAlignment.CENTER);
	    thStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
	    thStyle1.setBorderTop(BorderStyle.THICK);
	    thStyle1.setBorderBottom(BorderStyle.THIN);
	    thStyle1.setBorderLeft(BorderStyle.THIN);
	    thStyle1.setBorderRight(BorderStyle.THIN);
	    
	    Font thFont = workbook.createFont();
	    thFont.setBold(true);
	    thStyle1.setFont(thFont); 
	    
	    
	    // 3-4) th2: 가운데정렬 (가로/세로), 굵은 글씨
	    CellStyle thStyle2 = workbook.createCellStyle();
	    thStyle2.setAlignment(HorizontalAlignment.CENTER);
	    thStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
	    thStyle2.setBorderTop(BorderStyle.THIN);
	    thStyle2.setBorderBottom(BorderStyle.THIN);
	    thStyle2.setBorderLeft(BorderStyle.THIN);
	    thStyle2.setBorderRight(BorderStyle.THIN);
	    
	    thStyle2.setFont(thFont); 
	    
	    
	    // 3-5) th3: 가운데정렬 (가로/세로), 굵은 글씨, 아래 굵은 테두리
	    CellStyle thStyle3 = workbook.createCellStyle();
	    thStyle3.setAlignment(HorizontalAlignment.CENTER);
	    thStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
	    thStyle3.setBorderTop(BorderStyle.THIN);
	    thStyle3.setBorderBottom(BorderStyle.THICK);
	    thStyle3.setBorderLeft(BorderStyle.THIN);
	    thStyle3.setBorderRight(BorderStyle.THIN);
	    
	    thStyle3.setFont(thFont); 
	    
	    
	    // 3-6) th4: 가운데정렬 (가로/세로), 굵은 글씨, 위쪽 오른쪽 굵은 테두리
	    CellStyle thStyle4 = workbook.createCellStyle();
	    thStyle4.setAlignment(HorizontalAlignment.CENTER);
	    thStyle4.setVerticalAlignment(VerticalAlignment.CENTER);
	    thStyle4.setBorderTop(BorderStyle.THICK);
	    thStyle4.setBorderBottom(BorderStyle.THIN);
	    thStyle4.setBorderLeft(BorderStyle.THIN);
	    thStyle4.setBorderRight(BorderStyle.THICK);
	    
	    thStyle4.setFont(thFont); 
	    
	    
	    // 3-7) td1: 가운데정렬 (가로/세로), 위쪽 굵은 테두리
	    CellStyle tdStyle1 = workbook.createCellStyle();
	    tdStyle1.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle1.setBorderTop(BorderStyle.THICK);
	    tdStyle1.setBorderBottom(BorderStyle.THIN);
	    tdStyle1.setBorderLeft(BorderStyle.THIN);
	    tdStyle1.setBorderRight(BorderStyle.THIN);
	    
	    
	    // 3-8) td2: 가운데정렬 (가로/세로), 아래 굵은 테두리
	    CellStyle tdStyle2 = workbook.createCellStyle();
	    tdStyle2.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle2.setBorderTop(BorderStyle.THIN);
	    tdStyle2.setBorderBottom(BorderStyle.THICK);
	    tdStyle2.setBorderLeft(BorderStyle.THIN);
	    tdStyle2.setBorderRight(BorderStyle.THIN);
	    
	    
	    // 3-9) td3: 가운데정렬 (가로/세로), 오른쪽 굵은 테두리
	    CellStyle tdStyle3 = workbook.createCellStyle();
	    tdStyle3.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle3.setBorderTop(BorderStyle.THIN);
	    tdStyle3.setBorderBottom(BorderStyle.THIN);
	    tdStyle3.setBorderLeft(BorderStyle.THIN);
	    tdStyle3.setBorderRight(BorderStyle.THICK);
	    
	    
	    // 3-10) td4: 가운데정렬 (가로/세로), 상우하좌 얇은 테두리
	    CellStyle tdStyle4 = workbook.createCellStyle();
	    tdStyle4.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle4.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle4.setBorderTop(BorderStyle.THIN);
	    tdStyle4.setBorderBottom(BorderStyle.THIN);
	    tdStyle4.setBorderLeft(BorderStyle.THIN);
	    tdStyle4.setBorderRight(BorderStyle.THIN);
	    
	    
	    // 3-11) td5: 가운데정렬 (가로/세로), 위쪽 오른쪽 두꺼운 테두리
	    CellStyle tdStyle5 = workbook.createCellStyle();
	    tdStyle5.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle5.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle5.setBorderTop(BorderStyle.THICK);
	    tdStyle5.setBorderBottom(BorderStyle.THIN);
	    tdStyle5.setBorderLeft(BorderStyle.THIN);
	    tdStyle5.setBorderRight(BorderStyle.THICK);
	    
	    // 3-12) td6: 가운데정렬 (가로/세로), 아래 오른쪽 두꺼운 테두리
	    CellStyle tdStyle6 = workbook.createCellStyle();
	    tdStyle6.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle6.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle6.setBorderTop(BorderStyle.THIN);
	    tdStyle6.setBorderBottom(BorderStyle.THICK);
	    tdStyle6.setBorderLeft(BorderStyle.THIN);
	    tdStyle6.setBorderRight(BorderStyle.THICK);
	    
	    
	    // 3-13) td7: 가운데정렬 (가로/세로), 아래 두꺼운 테두리 => 나머지 테두리 없음
	    CellStyle tdStyle7 = workbook.createCellStyle();
	    tdStyle7.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle7.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle7.setBorderBottom(BorderStyle.THICK);
	    
	    // 3-14) td8: 가운데정렬 (가로/세로), 아래 오른쪽 두꺼운 테두리 => 나머지 테두리 없음
	    CellStyle tdStyle8 = workbook.createCellStyle();
	    tdStyle8.setAlignment(HorizontalAlignment.CENTER);
	    tdStyle8.setVerticalAlignment(VerticalAlignment.CENTER);
	    tdStyle8.setBorderBottom(BorderStyle.THICK);
	    tdStyle8.setBorderRight(BorderStyle.THICK);
	    
	    // 3-15) money1: 천단위 쉼표, 금액, 가운데정렬 (가로/세로), 상우하좌 얇은 테두리
	    CellStyle moneyStyle1 = workbook.createCellStyle();
	    moneyStyle1.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
	    moneyStyle1.setAlignment(HorizontalAlignment.CENTER);
	    moneyStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
	    moneyStyle1.setBorderTop(BorderStyle.THIN);
	    moneyStyle1.setBorderBottom(BorderStyle.THIN);
	    moneyStyle1.setBorderLeft(BorderStyle.THIN);
	    moneyStyle1.setBorderRight(BorderStyle.THIN);
	    
	    
	    // 3-16) money2: 천단위 쉼표, 금액, 가운데정렬 (가로/세로), 아래 두꺼운 테두리
	    CellStyle moneyStyle2 = workbook.createCellStyle();
	    moneyStyle2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
	    moneyStyle2.setAlignment(HorizontalAlignment.CENTER);
	    moneyStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
	    moneyStyle2.setBorderTop(BorderStyle.THIN);
	    moneyStyle2.setBorderBottom(BorderStyle.THICK);
	    moneyStyle2.setBorderLeft(BorderStyle.THIN);
	    moneyStyle2.setBorderRight(BorderStyle.THIN);
	    
	    
	    // 3-17) money3: 천단위 쉼표, 금액, 가운데정렬 (가로/세로), 오른쪽 두꺼운 테두리
	    CellStyle moneyStyle3 = workbook.createCellStyle();
	    moneyStyle3.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
	    moneyStyle3.setAlignment(HorizontalAlignment.CENTER);
	    moneyStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
	    moneyStyle3.setBorderTop(BorderStyle.THIN);
	    moneyStyle3.setBorderBottom(BorderStyle.THIN);
	    moneyStyle3.setBorderLeft(BorderStyle.THIN);
	    moneyStyle3.setBorderRight(BorderStyle.THICK);
	    
	    
	    // 3-18) money4: 천단위 쉼표, 금액, 가운데정렬 (가로/세로), 오른쪽 아래 두꺼운 테두리
	    CellStyle moneyStyle4 = workbook.createCellStyle();
	    moneyStyle4.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
	    moneyStyle4.setAlignment(HorizontalAlignment.CENTER);
	    moneyStyle4.setVerticalAlignment(VerticalAlignment.CENTER);
	    moneyStyle4.setBorderTop(BorderStyle.THIN);
	    moneyStyle4.setBorderBottom(BorderStyle.THICK);
	    moneyStyle4.setBorderLeft(BorderStyle.THIN);
	    moneyStyle4.setBorderRight(BorderStyle.THICK);
	    
	    
	    // 3-19) 아래문구: 가운데정렬 (가로/세로), 굵은 글씨 
	    CellStyle bottomStyle1 = workbook.createCellStyle();
	    bottomStyle1.setAlignment(HorizontalAlignment.CENTER);
	    bottomStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
	   
	    Font bottomFont = workbook.createFont();
	    bottomFont.setBold(true);
	    bottomFont.setFontHeight((short)300);
	    bottomStyle1.setFont(bottomFont); 
	    
	    
	    // 3-20) 오늘날짜: 가운데정렬 (가로/세로), 굵은 글씨
	    CellStyle bottomStyle2 = workbook.createCellStyle();
	    bottomStyle2.setAlignment(HorizontalAlignment.CENTER);
	    bottomStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
	   
	    
	    // 4) 엑셀 데이터값 입력
	    // 4-1) 첫번째 행 cell 설정 (1-8열 병합)		
	    row= sheet.createRow(0); 
	    row.setHeight((short) 1200); // 첫번째 행 높이 설정
	    
	    for(int i=1; i<9; i++) {  
             cell = row.createCell(i);
             cell.setCellStyle(titleStyle);
             cell.setCellValue(year+"년 "+month+"월 "+"급여명세서");
        }

	    sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, 8)); // 시작 행, 끝 행, 시작 열, 끝 열 
	    
	    // 4-2) 세번째 행 생성 (1-2열 병합, 4-5열 병합, 7-8열 병합)
	    row= sheet.createRow(2);
	    row.setHeight((short) 700); // 세번째 행 높이 설정
	    
	    for(int i=1; i<3; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("인적사항");
        }
	    
	    sheet.addMergedRegion(new CellRangeAddress(2, 4, 1, 2));
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("사번");
	    
	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(tdStyle1);
            cell.setCellValue(mvo.getEmployeeid());
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(2, 2, 4, 5));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("성명");
	    
	    for(int i=7; i<9; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(tdStyle5);
            cell.setCellValue(mvo.getName());
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(2, 2, 7, 8));
	    
	    // 4-3) 네번째 행 생성 (1-2열 병합, 4-5열 병합, 7-8열 병합)
	    row= sheet.createRow(3);
	    row.setHeight((short) 700); // 네번째 행 높이 설정
	    
	    cell=row.createCell(1);
	    cell.setCellStyle(headerStyle);
	    cell=row.createCell(2);
	    cell.setCellStyle(headerStyle);
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle2);
	    cell.setCellValue("소속");
	    
	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(tdStyle4);
            cell.setCellValue(mvo.getDname());
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(3, 3, 4, 5));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(thStyle2);
	    cell.setCellValue("직급");
	    
	    for(int i=7; i<9; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(tdStyle3);
            cell.setCellValue(mvo.getPname());
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(3, 3, 7, 8));
	    
	    // 4-4) 다섯번째 행 생성 (4-8열 병합)
	    row= sheet.createRow(4);
	    row.setHeight((short) 700); // 다섯번째 행 높이 설정
	    
	    cell=row.createCell(1);
	    cell.setCellStyle(headerStyle);
	    cell=row.createCell(2);
	    cell.setCellStyle(headerStyle);

	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle3);
	    cell.setCellValue("급여총액");
	    
	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle4);
            cell.setCellValue(totalSalary);
        }
	    
	    sheet.addMergedRegion(new CellRangeAddress(4, 4, 4, 6));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(tdStyle7);
	    cell = row.createCell(7);
	    cell.setCellStyle(tdStyle7);
	    cell = row.createCell(8);
	    cell.setCellStyle(tdStyle8);
	    
	    // 4-5) 일곱번째 행 생성 (1-2열 병합)
	    row= sheet.createRow(6);
	    row.setHeight((short) 700); // 일곱번째 행 높이 설정
		   
	    for(int i=1; i<3; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("근태내역");
        }
	    
	    sheet.addMergedRegion(new CellRangeAddress(6, 7, 1, 2));
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("정상출근");
	    
	    cell = row.createCell(4);
	    cell.setCellStyle(tdStyle1);
	    cell.setCellValue(inoutCnt);
	    
	    cell = row.createCell(5);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("연차휴가");
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(tdStyle1);
	    cell.setCellValue(attendanceMap.get("daydates"));
	    
	    cell = row.createCell(7);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("병가");
	    
	    cell = row.createCell(8);
	    cell.setCellStyle(tdStyle5);
	    cell.setCellValue(attendanceMap.get("sldates"));
	    
	    // 4-6) 여덟번째 행 생성 (1-2열 병합)
	    row= sheet.createRow(7);
	    row.setHeight((short) 700); // 여덟번째 행 높이 설정
		
	    cell=row.createCell(1);
	    cell.setCellStyle(headerStyle);
	    cell=row.createCell(2);
	    cell.setCellStyle(headerStyle);
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle3);
	    cell.setCellValue("지각");
	    
	    cell = row.createCell(4);
	    cell.setCellStyle(tdStyle2);
	    cell.setCellValue(attendanceMap.get("lateday"));
	    
	    cell = row.createCell(5);
	    cell.setCellStyle(thStyle3);
	    cell.setCellValue("반차휴가");
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(tdStyle2);
	    cell.setCellValue(attendanceMap.get("afdates"));
	    
	    cell = row.createCell(7);
	    cell.setCellStyle(thStyle3);
	    cell.setCellValue("경조휴가");
	    
	    cell = row.createCell(8);
	    cell.setCellStyle(tdStyle6);
	    cell.setCellValue(attendanceMap.get("congdates"));
	    
	    // 4-7) 열번째 행 생성 (1-2열 병합, 4-5열 병합, 7-8열 병합)
	    row= sheet.createRow(9);
	    row.setHeight((short) 700); // 열번째 행 높이 설정
	    
	    for(int i=1; i<3; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(headerStyle);
            cell.setCellValue("세부내역");
        }
	    
	    sheet.addMergedRegion(new CellRangeAddress(9, 12, 1, 2));
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("항목");
	    
	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(thStyle1);
            cell.setCellValue("금액");
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(9, 9, 4, 5));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(thStyle1);
	    cell.setCellValue("항목");
	    
	    for(int i=7; i<9; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(thStyle4);
            cell.setCellValue("금액");
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(9, 9, 7, 8));
	    
	    
	    // 4-8) 열한번째 행 생성 (1-2열 병합, 4-5열 병합, 7-8열 병합)
	    row= sheet.createRow(10);
	    row.setHeight((short) 700); // 열한번째 행 높이 설정
		
	    cell=row.createCell(1);
	    cell.setCellStyle(headerStyle);
	    cell=row.createCell(2);
	    cell.setCellStyle(headerStyle);
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle2);
	    cell.setCellValue("기본급");

	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle1);
            cell.setCellValue(Integer.parseInt(mvo.getSalary()));
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(10, 10, 4, 5));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(thStyle2);
	    cell.setCellValue("근속수당");
	    
	    for(int i=7; i<9; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle3);
            if(continueYear==0) cell.setCellValue("-"); 
            else cell.setCellValue(1000000);
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(10, 10, 7, 8));
	    
	    // 4-9) 열두번째 행 생성 (1-2열 병합, 4-5열 병합, 7-8열 병합)
	    row= sheet.createRow(11);
	    row.setHeight((short) 700); // 열두번째 행 높이 설정
	    
	    cell=row.createCell(1);
	    cell.setCellStyle(headerStyle);
	    cell=row.createCell(2);
	    cell.setCellStyle(headerStyle);
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle2);
	    cell.setCellValue("야근수당");
	    
	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle1);
            if(totalLateWorkTimeMoney==0) cell.setCellValue("-");
            else cell.setCellValue(totalLateWorkTimeMoney);
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(11, 11, 4, 5));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(thStyle2);
	    cell.setCellValue("상여금");
	    
	    for(int i=7; i<9; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle3);
            if(bonus > 0) cell.setCellValue(bonus);
            else cell.setCellValue("-");
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(11, 11, 7, 8));
	    
	    // 4-10) 열세번째 행 생성 (1-2열 병합, 4-5열 병합, 7-8열 병합)
	    row= sheet.createRow(12);
	    row.setHeight((short) 700); // 열세번째 행 높이 설정
	    
	    cell=row.createCell(1);
	    cell.setCellStyle(headerStyle);
	    cell=row.createCell(2);
	    cell.setCellStyle(headerStyle);
	    
	    cell = row.createCell(3);
	    cell.setCellStyle(thStyle3);
	    cell.setCellValue("식대");
	    
	    for(int i=4; i<6; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle2);
            cell.setCellValue(workDays*5000);
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(12, 12, 4, 5));
	    
	    cell = row.createCell(6);
	    cell.setCellStyle(thStyle3);
	    cell.setCellValue("지급합계");
	    
	    for(int i=7; i<9; i++) {  
            cell = row.createCell(i);
            cell.setCellStyle(moneyStyle4);
            cell.setCellValue(totalSalary);
        }
		
	    sheet.addMergedRegion(new CellRangeAddress(12, 12, 7, 8));
	    
	    
	    // 4-11) 열다섯번째 행 생성 (1-8열 병합)
	    row= sheet.createRow(14); 
	    row.setHeight((short) 1000); // 열다섯번째 행 높이 설정
	    
	    for(int i=1; i<9; i++) {  
             cell = row.createCell(i);
             cell.setCellStyle(bottomStyle1);
             cell.setCellValue("귀하의 노고에 감사드립니다");
        }

	    sheet.addMergedRegion(new CellRangeAddress(14, 14, 1, 8));
	    
	    // 4-12) 열여섯번째 행 생성 (1-8열 병합)
	    row= sheet.createRow(15); 
	    row.setHeight((short) 1000); // 열여섯번째 행 높이 설정
	    
	    for(int i=1; i<9; i++) {  
             cell = row.createCell(i);
             cell.setCellStyle(bottomStyle2);
             cell.setCellValue(currentYear+"년 "+currentMonth+"월 "+currentDay+"일");
        }

	    sheet.addMergedRegion(new CellRangeAddress(15, 15, 1, 8));

	    // 4-13) 열일곱번째 행 생성 (7-8열 병합) 
	    row= sheet.createRow(16); 
	    row.setHeight((short) 1000); // 열일곱번째 행 높이 설정
	    
	    for(int i=7; i<9; i++) {  
             cell = row.createCell(i);
             cell.setCellStyle(bottomStyle1);
             cell.setCellValue("T1WORKS");
        }

	    sheet.addMergedRegion(new CellRangeAddress(16, 16, 7, 8));

	    // 5) 데이터 엑셀로 출력
	    model.addAttribute("locale", Locale.KOREA); // 한글 인식시키기
        model.addAttribute("workbook", workbook);
        model.addAttribute("workbookName", "월급명세서");
        
        return "excelDownloadView";
	    
	} // end of public String downloadExcelFile(HttpServletRequest request, Model model) {---
	
	
	// 이메일 주소록 매핑 url (팝업창)
	@RequestMapping(value="/t1/emailEmployeeMap.tw")        // 로그인이 필요한 url
	public ModelAndView requiredLogin_emailEmployeeMap(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 주소록표기
		// 1) 모든 부서에 대한 정보 가져오기
		List<DepartmentHsyVO> departmentList= service.selectAllDepartment();
		mav.addObject("departmentList", departmentList);
		
		// 2) 모든 직원 정보 가져오기
		List<MemberHsyVO> employeeList= service.selectAllMember();
		
		mav.addObject("employeeList",employeeList);
		mav.setViewName("hsy/emailEmployeeMap");
		return mav;
	} // end of public ModelAndView requiredLogin_emailEmployeeMap(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {---
		
		
		
}
