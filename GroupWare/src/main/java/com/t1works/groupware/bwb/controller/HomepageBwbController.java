package com.t1works.groupware.bwb.controller;

import java.util.*;

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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.bwb.service.InterHomepageBwbService;
import com.t1works.groupware.bwb.service.InterMemberBwbService;

@Controller
public class HomepageBwbController {
	
	 @Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	 private InterHomepageBwbService service;
	 
	 @Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	 private InterMemberBwbService service2;
	 
	 
	 // 출퇴근기록 테이블에 insert 및 select작업(출근시간)
	 @ResponseBody
	 @RequestMapping(value="/t1/insertSelectIntime.tw", method= {RequestMethod.POST})
	 public String requiredLogin_insertSelectIntime(HttpServletRequest request, HttpServletResponse response) {
		 
		 String fk_employeeid = request.getParameter("fk_employeeid");
		 String gooutdate = request.getParameter("gooutdate"); 
		
		 
		 Map<String,String> paraMap = new HashMap<>();
		 paraMap.put("fk_employeeid", fk_employeeid);
		 paraMap.put("gooutdate", gooutdate);
		 
		 
		 
		 service.insertIntime(paraMap);
		 
		 String intime = service.selectIntime(paraMap);

		 JSONObject jsonobj = new JSONObject();
		 jsonobj.put("intime", intime);
		 
		 // 지각여부 시간 판단하기(select)
		 String latenoTime = service.selectlateno(intime);
		 jsonobj.put("latenoTime", latenoTime); // 1일경우 지각, 0,-1일 경우 정상출근
		 
		 // 지각여부 판단하기(update)
		 if("1".equalsIgnoreCase(latenoTime)) {
			 service.updatelateno(paraMap);
		 }
	
		 
		 return jsonobj.toString();
	 }
	 
	 
	// 출퇴근기록 테이블에서 select작업
	 @ResponseBody
	 @RequestMapping(value="/t1/selectIntime.tw")
	 public String requiredLogin_selectIntime(HttpServletRequest request, HttpServletResponse response) {
 
		 String fk_employeeid = request.getParameter("fk_employeeid");
		 String gooutdate = request.getParameter("gooutdate"); 
		 
		 Map<String,String> paraMap = new HashMap<>();
		 paraMap.put("fk_employeeid", fk_employeeid);
		 paraMap.put("gooutdate", gooutdate);

		 String intime = service.selectIntime(paraMap);
		 if(intime==null) {
			 intime="";
		 }
		 
		 JSONObject jsonobj = new JSONObject();
		 jsonobj.put("intime", intime);
		 
		 String latenoTime = service.selectlateno(intime);
		 jsonobj.put("latenoTime", latenoTime); // 1일경우 지각, 0,-1일 경우 정상출근

		 return jsonobj.toString();
	 }
	 
	 
	 // 퇴근버튼 클릭시 출퇴근 테이블에 insert 및 select작업 및 야근테이블에 insert작업 (트랜젝션 처리)
	 @ResponseBody
	 @RequestMapping(value="/t1/insertSelectOuttime.tw", method= {RequestMethod.POST})
	 public String requiredLogin_insertSelectOuttime(HttpServletRequest request, HttpServletResponse response) {
		 
		 String fk_employeeid = request.getParameter("fk_employeeid");
		 String gooutdate = request.getParameter("gooutdate"); 
		 
		 
		 Map<String,String> paraMap = new HashMap<>();
		 paraMap.put("fk_employeeid", fk_employeeid);
		 paraMap.put("gooutdate", gooutdate);
		 
		 
		 String outtime = "";
		 
		 // 출퇴근테이블에 insert하기(퇴근시간)
		 try {
			outtime = service.updateOuttime(paraMap);
		 } catch (Throwable e) {
			e.printStackTrace();
		 }

		 JSONObject jsonobj = new JSONObject();
		 jsonobj.put("outtime", outtime);
		 
		 return jsonobj.toString();
		 
	 }// end of public String requiredLogin_insertSelectOuttime
	
	 
	// 출퇴근기록 테이블에서 select작업(퇴근시간)
	 @ResponseBody
	 @RequestMapping(value="/t1/selectOuttime.tw")
	 public String requiredLogin_selectOuttime(HttpServletRequest request, HttpServletResponse response) {
 
		 String fk_employeeid = request.getParameter("fk_employeeid");
		 String gooutdate = request.getParameter("gooutdate"); 
		 
		 Map<String,String> paraMap = new HashMap<>();
		 paraMap.put("fk_employeeid", fk_employeeid);
		 paraMap.put("gooutdate", gooutdate);

		 String outtime = service.selectOuttime(paraMap);
		 
		 if(outtime==null) {
			 outtime="";
		 }
		 
		 JSONObject jsonobj = new JSONObject();
		 jsonobj.put("outtime", outtime);
		 
		 return jsonobj.toString();
	 }// end of @RequestMapping(value="/t1/selectOuttime.tw")
	 
	 @RequestMapping(value="/t1/myMonthIndolence.tw")
	 public ModelAndView requiredLogin_myMonthIndolence(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		 
		 HttpSession session = request.getSession();
		 MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		 
		 mav.addObject("loginuser", loginuser);
		 mav.setViewName("bwb/myMonthIndolence.gwTiles");
		 return mav;
	 }// end of public ModelAndView requiredLogin_myMonthIndolence
	 
	 // 나의 월별 출퇴근기록 가지고오기(ajax처리)
	 @ResponseBody
	 @RequestMapping(value="/t1/selectmyMonthIndolence.tw",produces="text/plain;charset=UTF-8")
	 public String selectmyMonthIndolence(HttpServletRequest request) {
		 
		 String fk_employeeid = request.getParameter("fk_employeeid");
		 
		 // 나의 월별 출퇴근기록 가지고오기
		 List<Map<String,String>> myIndolenceList = service.selectmyMonthIndolence(fk_employeeid);
		 
		 JSONArray jsonArr = new JSONArray();
		 for(Map<String,String> resultMap :myIndolenceList) {
			 
			 JSONObject jsonObj = new JSONObject();
			 jsonObj.put("name", resultMap.get("name"));
			 jsonObj.put("intime", resultMap.get("intime"));
			 jsonObj.put("outtime", resultMap.get("outtime"));
			 
			 jsonArr.put(jsonObj);
		 }

		 return jsonArr.toString();
		 
	 }// end of public String selectmyMonthIndolence
	 
	 // 부서근태관리 클릭시 맵핑주소
	 @RequestMapping(value="/t1/depMonthIndolence.tw")
	 public ModelAndView requiredLogin_depMonthIndolence(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		 
		 HttpSession session = request.getSession();
		 MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		 
		 String dcode = loginuser.getFk_dcode();
		 String dname = service2.selectdname(dcode);
		 
		 mav.addObject("dname", dname);
		 mav.addObject("loginuser", loginuser);
		 mav.setViewName("bwb/depMonthIndolence.gwTiles");
		 return mav;
	 }// end of public ModelAndView requiredLogin_myMonthIndolence
	 
	 
	// 부서별 출퇴근기록 가지고오기(ajax처리)
	 @ResponseBody
	 @RequestMapping(value="/t1/selectDepMonthIndolence.tw",produces="text/plain;charset=UTF-8")
	 public String selectDepMonthIndolence(HttpServletRequest request) {
		 
		 String fk_dcode = request.getParameter("fk_dcode");
		 
		 // 부서 월별 출퇴근기록 가지고오기
		 List<Map<String,String>> depIndolenceList = service.selectDepMonthIndolence(fk_dcode);
		 
		 JSONArray jsonArr = new JSONArray();
		 for(Map<String,String> resultMap :depIndolenceList) {
			 
			 JSONObject jsonObj = new JSONObject();
			 jsonObj.put("name", resultMap.get("name"));
			 jsonObj.put("intime", resultMap.get("intime"));
			 jsonObj.put("outtime", resultMap.get("outtime"));
			 
			 jsonArr.put(jsonObj);
		 }
		 System.out.println(jsonArr);
		 return jsonArr.toString();
		 
	 }// end of public String selectmyMonthIndolence
	 
	 
	 @RequestMapping(value="/t1/travelschedule.tw")
	 public ModelAndView travelschedule(ModelAndView mav) {
		 
		 
		 mav.setViewName("bwb/travelschedule.gwTiles");
		 
		 return mav;
	 }// end of public ModelAndView travelschedule(ModelAndView mav) {
	 
	 
	 @ResponseBody
	 @RequestMapping(value="/t1/selectProductSchedule.tw",produces="text/plain;charset=UTF-8" )
	 public String selectProductSchedule() {
		 
		 
		 // 여행상품들의 일정 뽑아오기
		 List<Map<String,String>> productList = service.productSchedule();
		 
		 JSONArray jsonArr = new JSONArray();
		 for(Map<String,String> resultMap:productList) {
			 JSONObject jsonObj = new JSONObject();
			 jsonObj.put("name", resultMap.get("pname"));
			 jsonObj.put("startdate", resultMap.get("startdate"));
			 jsonObj.put("enddate", resultMap.get("enddate"));
			 if(resultMap.get("pname").indexOf("국내")>0) {
				 jsonObj.put("sort", 1);
			 }
			 else if(resultMap.get("pname").indexOf("유럽")>0) {
				 jsonObj.put("sort", 2);
			 }
			 else {
				 jsonObj.put("sort", 3);
			 }
			 jsonArr.put(jsonObj);
		 }
		 
		 return jsonArr.toString();
		 
	 }// end of public String selectProductSchedule() {
	 
	 
	 // 검색어 입력 시 자동검색기능(ajax처리)
	 @ResponseBody
	 @RequestMapping(value="/t1/wordSearch.tw",produces="text/plain;charset=UTF-8")
	 public String requiredLogin_wordSearch(HttpServletRequest request,HttpServletResponse response) {
		 
		 HttpSession session = request.getSession();
		 MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		 String dcode = loginuser.getFk_dcode();
		 String pcode = loginuser.getFk_pcode();
		 
		 String searchWord = request.getParameter("searchWord");
		 Map<String,String> paraMap = new HashMap<>();
		 paraMap.put("dcode", dcode);
		 paraMap.put("pcode", pcode);
		 paraMap.put("searchWord", searchWord);
		 
		 
		 List<String> wordList = service.wordSearch(paraMap);
		 
		 JSONArray jsonArr = new JSONArray();
		 
		 if(wordList!=null) {
			 
			 for(String word:wordList) {
	    			JSONObject jsonObj = new JSONObject();
	    			jsonObj.put("word", word);
	    			
	    			jsonArr.put(jsonObj);
    		  }
		 }
		 
		 return jsonArr.toString();
	 }// end of public String wordSearch() {
	 
	 
	 // 검색어 입력 후 URL주소 뽑아오기(Mapping 주소)
	 @RequestMapping(value="/t1/searchSebuAddress.tw")
	 public ModelAndView requiredLogin_searchSebuAddress(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		 
		 HttpSession session = request.getSession();
		 MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		 String dcode = loginuser.getFk_dcode();
		 String pcode = loginuser.getFk_pcode();
		 
		 String searchWord = request.getParameter("searchWord");
		 
		 Map<String,String> paraMap = new HashMap<>();
		 paraMap.put("dcode", dcode);
		 paraMap.put("pcode", pcode);
		 paraMap.put("searchWord", searchWord);
		 
		 if(searchWord==null || "".equalsIgnoreCase(searchWord)) {
			 mav.setViewName("redirect:/t1/home.tw");
		 }
		 else {
			 // 검색어 입력 후 URL주소 뽑아오기
			 String sebuAddress = service.goSebuMenu(paraMap);
			 
			 // 해당 검색어 tbl_word에 insert시켜주기
			 service.insertWord(searchWord);
			 
			 mav.setViewName("redirect:"+sebuAddress);
		 }
		 return mav;
	 }
	 
	 
	 // 여행사 홈페이지에서 특정 고객의 일정 엑셀 출력
	 @RequestMapping(value="/excel/downloadSchedule.tw", method= {RequestMethod.POST})
	 public String downloadSchedule(HttpServletRequest request, Model model) {
		 
		 String clientname = request.getParameter("clientname");
		 
		 // 고객여행일정 가지고오기
		 List<Map<String,String>> scheduleList = service.selectScheduleList(clientname);
		 
		 // 시트를 생성하고, 행을 생성하고, 셀을 생성하고, 셀안에 내용을 넣어주면 된다.
		 SXSSFWorkbook workbook = new SXSSFWorkbook();
	      
	     // 시트생성
	     SXSSFSheet sheet = workbook.createSheet("여행 일정");
	     
	     // 시트 열 너비 설정
	     sheet.setColumnWidth(0, 8000);
	     sheet.setColumnWidth(1, 1500);
	     sheet.setColumnWidth(2, 2000);
	     sheet.setColumnWidth(3, 4000);
	     sheet.setColumnWidth(4, 4000);
	     sheet.setColumnWidth(5, 4000);
	     
	     // 행의 위치를 나타내는 변수 
		 int rowLocation = 0;
		 CellStyle mergeRowStyle = workbook.createCellStyle();
	     mergeRowStyle.setAlignment(HorizontalAlignment.CENTER);
	     mergeRowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	     // import org.apache.poi.ss.usermodel.VerticalAlignment 으로 해야함.
			
		 CellStyle headerStyle = workbook.createCellStyle();
		 headerStyle.setAlignment(HorizontalAlignment.CENTER);
		 headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		 
		 mergeRowStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());   
		 mergeRowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 
		 headerStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());   
		 headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		 
		// Cell 폰트(Font) 설정하기
		// 폰트 적용을 위해 POI 라이브러리의 Font 객체를 생성해준다.
		 Font mergeRowFont = workbook.createFont(); // import org.apache.poi.ss.usermodel.Font; 으로 한다.
		 mergeRowFont.setFontName("나눔고딕");
		 mergeRowFont.setFontHeight((short)500);
		 mergeRowFont.setColor(IndexedColors.BLACK.getIndex());
		 mergeRowFont.setBold(true);
		 
		 mergeRowStyle.setFont(mergeRowFont);
		 
		// CellStyle 테두리 Border
		// 테두리는 각 셀마다 상하좌우 모두 설정해준다.
		// setBorderTop, Bottom, Left, Right 메소드와 인자로 POI라이브러리의 BorderStyle 인자를 넣어서 적용한다.
		 headerStyle.setBorderTop(BorderStyle.THICK);
		 headerStyle.setBorderBottom(BorderStyle.THICK);
		 headerStyle.setBorderLeft(BorderStyle.THIN);
		 headerStyle.setBorderRight(BorderStyle.THIN);
		 
		// Cell Merge 셀 병합시키기
        /* 셀병합은 시트의 addMergeRegion 메소드에 CellRangeAddress 객체를 인자로 하여 병합시킨다.
           CellRangeAddress 생성자의 인자로(시작 행, 끝 행, 시작 열, 끝 열) 순서대로 넣어서 병합시킬 범위를 정한다. 배열처럼 시작은 0부터이다.  
        */
        // 병합할 행 만들기
        Row mergeRow = sheet.createRow(rowLocation);  // 엑셀에서 행의 시작은 0 부터 시작한다.
        
          
        for(int i=0; i<6; i++) {
           Cell cell = mergeRow.createCell(i);
           cell.setCellStyle(mergeRowStyle);
           cell.setCellValue(clientname+"님의 여행일정");
        }
        
        // 셀 병합하기
        sheet.addMergedRegion(new CellRangeAddress(rowLocation, rowLocation, 0, 5)); // 시작 행, 끝 행, 시작 열, 끝 열 
        
        // CellStyle 천단위 쉼표, 금액
        CellStyle moneyStyle = workbook.createCellStyle();
        moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
		 
        // 헤더 행 생성
        Row headerRow = sheet.createRow(++rowLocation); // 엑셀에서 행의 시작은 0 부터 시작한다.
        
        // 해당 행의 첫번째 열 셀 생성
        Cell headerCell = headerRow.createCell(0); // 엑셀에서 열의 시작은 0 부터 시작한다.
        headerCell.setCellValue("여행명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 두번째 열 셀 생성
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("인원수");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 세번째 열 셀 생성
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("예약자명");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 네번째 열 셀 생성
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("출발일");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 다섯번째 열 셀 생성
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue("종료일");
        headerCell.setCellStyle(headerStyle);
        
        // 해당 행의 여섯번째 열 셀 생성
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue("결제금액");
        headerCell.setCellStyle(headerStyle);
        
        // 여행일정 내용에 해당하는 행 및 셀 생성하기
        Row bodyRow = null;
        Cell bodyCell = null;
        
        for(int i=0; i<scheduleList.size(); i++) {
            
            Map<String,String> scheduleMap = scheduleList.get(i);
            
            // 행생성
            bodyRow = sheet.createRow(i + (rowLocation+1) );
            
            // 데이터 여행명 표시 
            bodyCell = bodyRow.createCell(0);
            bodyCell.setCellValue(scheduleMap.get("pname"));
            
            // 데이터 부서명 표시 
            bodyCell = bodyRow.createCell(1);
            bodyCell.setCellValue(scheduleMap.get("cnumber"));
            
            // 데이터 사원번호 표시 
            bodyCell = bodyRow.createCell(2);
            bodyCell.setCellValue(scheduleMap.get("clientname"));
            
            // 데이터 사원명 표시 
            bodyCell = bodyRow.createCell(3);
            bodyCell.setCellValue(scheduleMap.get("startdate"));
            
            // 데이터 입사일자 표시 
            bodyCell = bodyRow.createCell(4);
            bodyCell.setCellValue(scheduleMap.get("enddate"));
            
            // 데이터 월급 표시 
            bodyCell = bodyRow.createCell(5);
            bodyCell.setCellValue(Integer.parseInt(scheduleMap.get("price")));

         }// end of for----------------------------------------
		 
		 
         // model은 아래와 같이 addAttribute를 통해 값을 넣어준다.
         model.addAttribute("locale", Locale.KOREA);
         model.addAttribute("workbook", workbook);
         model.addAttribute("workbookName", "여행일정"); 
		 
		 return "excelDownloadView";
		 
	 }// end of public String downloadSchedule
	 
	 
	 
}

