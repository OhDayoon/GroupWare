package com.t1works.groupware.ody.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.common.MyUtil;
import com.t1works.groupware.ody.model.MemberOdyVO;
import com.t1works.groupware.ody.model.ScalCategoryOdyVO;
import com.t1works.groupware.ody.model.ScheduleOdyVO;
import com.t1works.groupware.ody.service.InterScheduleOdyService;


@Component
@Controller
public class ScheduleOdyController {
	
	@Autowired
	private  InterScheduleOdyService service;
	
	@RequestMapping(value="/t1/schedule.tw")
	public String requiredLogin_showSchedule(HttpServletRequest request, HttpServletResponse response) {
		
		return "ody/schedule/showSchedule.gwTiles";
	}
	
	// 전체 캘린더를 불러오는것(추후 내 캘린더, 사내캘린더 나눠서 불러올 수 있도록 해야함)
	@ResponseBody
	@RequestMapping(value="/t1/selectSchedule.tw", produces="text/plain;charset=UTF-8")
	public String selectSchedule(HttpServletRequest request) {
		// 등록된 일정 가져오기
		
				String fk_employeeid = request.getParameter("fk_employeeid");
				
				List<ScheduleOdyVO> scheduleList = service.getScheduleList(fk_employeeid);
				JSONArray jsArr = new JSONArray();
				if(scheduleList!=null) {
					for(ScheduleOdyVO svo : scheduleList) {
						JSONObject jsObj = new JSONObject();
						jsObj.put("subject", svo.getSubject());
						jsObj.put("startdate", svo.getStartdate());
						jsObj.put("enddate", svo.getEnddate());
						jsObj.put("color", svo.getColor());
						jsObj.put("sdno", svo.getSdno());
						jsObj.put("bcno", svo.getFk_bcno());
						jsObj.put("scno", svo.getFk_scno());
						jsObj.put("fk_employeeid", svo.getFk_employeeid());
						jsObj.put("joinemployee", svo.getJoinemployee());
						jsArr.put(jsObj);
					}
				}
				
				return jsArr.toString();
	}
	
	// 일정관리 등록 페이지로 이동
	@RequestMapping(value="/t1/insertSchedule.tw", method = {RequestMethod.GET})
	public String requiredLogin_insertSchedule(HttpServletRequest request, HttpServletResponse response) {
		
		// form에서 받아온 날짜
		String chooseDate = request.getParameter("chooseDate");
		request.setAttribute("chooseDate", chooseDate);
		return "ody/schedule/insertSchedule.gwTiles";
	}
	
	// 일정등록시 카테고리 정보 보여주기
	@ResponseBody
	@RequestMapping(value="/t1/selectCategory.tw", produces="text/plain;charset=UTF-8")
	public String getSmallCategory(HttpServletRequest request) {
		
		String fk_bcno = request.getParameter("fk_bcno");
		String fk_employeeid= request.getParameter("fk_employeeid");
		
	//	System.out.println("대분류"+fk_bcno);
	//	System.out.println("사번"+fk_employeeid);
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fk_bcno", fk_bcno);
		paraMap.put("fk_employeeid", fk_employeeid);
		
		List<ScalCategoryOdyVO> scategoryList = service.getSmallCategory(paraMap);
	//	System.out.println("개수"+scategoryList.size());
		
		JSONArray jsArr = new JSONArray();
		if(scategoryList!=null) {
			for(ScalCategoryOdyVO scvo : scategoryList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("scno", scvo.getScno());
				jsObj.put("scname", scvo.getScname());
				
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}
	
	
	// 사원 명단 불러오기
	@ResponseBody
	@RequestMapping(value="/t1/insertSchedule/searchJoinEmpList.tw", produces="text/plain;charset=UTF-8")
	public String searchJoinEmpList(HttpServletRequest request) {
		
		String joinEmp = request.getParameter("joinEmp");
		
		// 사원 명단 불러오기
		List<MemberOdyVO> joinEmpList = service.searchJoinEmpList(joinEmp);

		JSONArray jsArr = new JSONArray();
		if(joinEmpList!=null) {
			for(MemberOdyVO mvo : joinEmpList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("name", mvo.getName());
				jsObj.put("employeeid", mvo.getEmployeeid());
				jsObj.put("dname", mvo.getDname());
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
		
	}

	// 일정 등록하기
	@RequestMapping(value="/t1/schedule/registerSchedule.tw", method = {RequestMethod.POST})
	public ModelAndView registerSchedule(ModelAndView mav, HttpServletRequest request, ScheduleOdyVO svo) {
		
		
		String startdate= request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String subject = request.getParameter("subject");
		String color = request.getParameter("color");
		String place = request.getParameter("place");
		String joinemployee = request.getParameter("joinemployee");
		String content = request.getParameter("content");
		String fk_scno = request.getParameter("fk_scno");
		String fk_bcno= request.getParameter("fk_bcno");
		String fk_employeeid= request.getParameter("fk_employeeid");
	//	System.out.println(joinemployee);
		
		
		Map<String,String> paraMap = new HashMap<String, String>();
		paraMap.put("startdate", startdate);
		paraMap.put("enddate", enddate);
		paraMap.put("subject", subject);
		paraMap.put("color", color);
		paraMap.put("joinemployee", joinemployee);
		paraMap.put("place", place);
		paraMap.put("content", content);
		paraMap.put("fk_scno", fk_scno);
		paraMap.put("fk_bcno",fk_bcno);
		paraMap.put("fk_employeeid", fk_employeeid);
		
		int n = service.registerSchedule(paraMap);
		
		if(n == 0) {
			mav.addObject("message", "일정 등록에 실패하였습니다.");
			mav.addObject("loc", request.getContextPath()+"/t1/schedule.tw");
		}
		else {
			mav.addObject("message", "일정 등록에 성공하였습니다.");
			mav.addObject("loc", request.getContextPath()+"/t1/schedule.tw");
		}
		
		
		mav.setViewName("msg");
		
		return mav;
	}
	
	// 일정상세보기
	@RequestMapping(value="/t1/detailSchedule.tw")
	public ModelAndView getDetailSchedule(ModelAndView mav, HttpServletRequest request) {
		
		
		String sdno = request.getParameter("sdno");
		
		// 검색하고 나서 취소 버튼 클릭했을 때 필요함
		String listgobackURL = request.getParameter("listgobackURL");
		mav.addObject("listgobackURL",listgobackURL);

		
		// 수정하기 창으로 넘어갔을 때 필요함
		String gobackURL1 = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL1", gobackURL1);

		
		try {
			Integer.parseInt(sdno);
			ScheduleOdyVO svo = service.getDetailSchedule(sdno);
			mav.addObject("svo", svo);
			mav.setViewName("ody/schedule/detailSchedule.gwTiles");
		}catch (NumberFormatException e) {
			mav.setViewName("redirect:/t1/schedule.tw");
		}
		return mav;

	}
	
	// 일정 상세보기에서 삭제 클릭
	@ResponseBody
	@RequestMapping(value="/t1/schedule/deleteSchedule.tw",method = {RequestMethod.POST})
	public String delSchedule(HttpServletRequest request) {
		
		String sdno = request.getParameter("sdno");
		
		int n = 0;
		
		try {
		 n = service.delSchedule(sdno);
		}catch (Throwable e) {	
		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n); // 정상이라면 {"n":1"} 오류가 발생하면 {"n":0}
		return jsonObj.toString();
	}
	
	// 일정 수정하기
	@RequestMapping(value="/t1/schedule/editSchedule.tw",method = {RequestMethod.POST})
	public ModelAndView requiredLogin_editSchedule(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		
		
		String sdno = request.getParameter("sdno");
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
		
		String gobackURL1 = request.getParameter("gobackURL1");
		mav.addObject("gobackURL1", gobackURL1);

		try {
			Integer.parseInt(sdno);
			ScheduleOdyVO svo = service.getDetailSchedule(sdno);
			
			if( !loginuser.getEmployeeid().equals(svo.getFk_employeeid()) ) {
				String message = "다른 사용자의 글은 수정이 불가합니다.";
				String loc = "javascript:history.back()";
				
				mav.addObject("message", message);
				mav.addObject("loc", loc);
				mav.setViewName("msg");
			}
			else {
				mav.addObject("svo", svo);
				mav.setViewName("ody/schedule/editSchedule.gwTiles");
			}
		}catch (NumberFormatException e) {
			mav.setViewName("redirect:/t1/schedule.tw");
		}
		
		return mav;
	}

	
	// 일정 수정 완료
	@RequestMapping(value="/t1/schedule/editEndSchedule.tw", method = {RequestMethod.POST})
	public ModelAndView editEndSchedule(ModelAndView mav, HttpServletRequest request) {
		
		String startdate= request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String subject = request.getParameter("subject");
		String color = request.getParameter("color");
		String place = request.getParameter("place");
		String joinemployee = request.getParameter("joinemployee");
		String content = request.getParameter("content");
		String fk_scno = request.getParameter("fk_scno");
		String fk_bcno= request.getParameter("fk_bcno");
		String fk_employeeid= request.getParameter("fk_employeeid");
		String sdno= request.getParameter("sdno");

		
		Map<String,String> paraMap = new HashMap<String, String>();
		paraMap.put("startdate", startdate);
		paraMap.put("enddate", enddate);
		paraMap.put("subject", subject);
		paraMap.put("color", color);
		paraMap.put("joinemployee", joinemployee);
		paraMap.put("place", place);
		paraMap.put("content", content);
		paraMap.put("fk_scno", fk_scno);
		paraMap.put("fk_bcno",fk_bcno);
		paraMap.put("fk_employeeid", fk_employeeid);
		paraMap.put("sdno", sdno);
		int n = 0;
		
		try {
		 n = service.editEndSchedule(paraMap);
		 
		 if(n==1) {
			 mav.addObject("message", "일정을 수정하였습니다.");
			 mav.addObject("loc", request.getContextPath()+"/t1/schedule.tw");
		 }
		 else {
			 mav.addObject("message", "일정 수정에 실패하였습니다.");
			 mav.addObject("loc", "javascript:history.back()");
		 }
		 	mav.setViewName("msg");
		}catch (Throwable e) {	
			mav.setViewName("redirect:/t1/schedule.tw");
		}
		return mav;
	}
	
	
	// 일정 검색 기능
	@RequestMapping(value="/t1/searchSchedule.tw", method = {RequestMethod.GET})
	public ModelAndView searchSchedule(HttpServletRequest request, ModelAndView mav) {
		

		HttpSession session = request.getSession();
		// 메모리에 생성되어져 있는 session을 불러오는 것이다.
		
		MemberBwbVO loginuser =(MemberBwbVO)session.getAttribute("loginuser");
		session.setAttribute("readCountPermission", "yes");
		// 회의실 리스트
		String fk_employeeid = loginuser.getEmployeeid();
		
		List<ScheduleOdyVO> scheduleList = null;

		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String str_sizePerPage = request.getParameter("sizePerPage");
		String fk_bcno = request.getParameter("fk_bcno");

		if(fk_bcno== null || "".equals(fk_bcno)) {
			fk_bcno="";
		}
		
		if(str_sizePerPage == null || 
				   !("10".equals(str_sizePerPage) || "15".equals(str_sizePerPage) || "20".equals(str_sizePerPage))) {
				str_sizePerPage ="10";
		}
		
		if(searchType==null || (!"subject".equals(searchType) && !"name".equals(searchType) && !"content".equals(searchType)  && !"joinemployee".equals(searchType))) {
			searchType="";
		}
		
		if(searchWord== null || "".equals(searchWord) || searchWord.trim().isEmpty()) {// 
			searchWord="";
		}
		
		if(startdate == null || "".equals(startdate)) {
			startdate="";
		}
		
		if(enddate==null || "".equals(enddate)) {
			enddate="";
		}
		
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("startdate", startdate);
		paraMap.put("enddate", enddate);
		paraMap.put("fk_employeeid", fk_employeeid);
		paraMap.put("fk_bcno", fk_bcno);
		paraMap.put("str_sizePerPage", str_sizePerPage);

		
		int totalCount =0;        // 총 게시물 건수		
		int currentShowPageNo =0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int sizePerPage= Integer.parseInt(str_sizePerPage);
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
	    
	    
	    // 총 일정 건수(totalCount)
	    totalCount = service.getTotalCount(paraMap);
      
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage); 

		if(str_currentShowPageNo == null) {
			currentShowPageNo=1;
		}
		else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo<1 || currentShowPageNo>totalPage) {
					currentShowPageNo=1;
				}
			
			}catch (NumberFormatException e) {
				currentShowPageNo=1;
			}
		}
		
		
		startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
	    endRno = startRno + sizePerPage - 1;
	      
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
	    
	   
	    scheduleList = service.scheduleListSearchWithPaging(paraMap);
	    // 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
	
		mav.addObject("paraMap", paraMap);
	
		
		// === #121. 페이지바 만들기 === //
		
		
		int blockSize= 5;
		
		int loop = 1;
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
	   
		String pageBar = "<ul style='list-style:none;'>";
		
		String url = "searchSchedule.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo!=1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&searchType="+searchType+"&searchWord="+searchWord+"&fk_bcno="+fk_bcno+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&searchType="+searchType+"&searchWord="+searchWord+"&fk_bcno="+fk_bcno+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		while(!(loop>blockSize || pageNo>totalPage)) {
			
			if(pageNo==currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; font-weight: bold;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&searchType="+searchType+"&searchWord="+searchWord+"&fk_bcno="+fk_bcno+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while--------------------
		
		// === [다음][마지막] 만들기 === //
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&searchType="+searchType+"&searchWord="+searchWord+"&fk_bcno="+fk_bcno+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&searchType="+searchType+"&searchWord="+searchWord+"&fk_bcno="+fk_bcno+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		pageBar += "</ul>";
		
		mav.addObject("pageBar",pageBar);
		
		String listgobackURL = MyUtil.getCurrentURL(request);
	//	 System.out.println("~~~ 확인용 검색gobackURL:"+listgobackURL);
		
		mav.addObject("listgobackURL",listgobackURL);
		mav.addObject("scheduleList", scheduleList);
		mav.setViewName("ody/schedule/searchSchedule.gwTiles");

		return mav;
	}
	
	
	// 내 캘린더 보여주기
	@ResponseBody
	@RequestMapping(value="/t1/showMyCalendar.tw",method = {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String showMyCalendar(HttpServletRequest request) {
		
		String employeeid = request.getParameter("fk_employeeid");
		List<ScalCategoryOdyVO> empSList = service.getEmpSList(employeeid);
		
		JSONArray jsonArr = new JSONArray();
		
		if(empSList!=null) {
			for(ScalCategoryOdyVO scvo:empSList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("scno", scvo.getScno());
				jsObj.put("scname", scvo.getScname());
				jsonArr.put(jsObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// 사내 캘린더 보여주기
		@ResponseBody
		@RequestMapping(value="/t1/showCompanyCalendar.tw",method = {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
		public String showCompanyCalendar(HttpServletRequest request) {
			
			
			List<ScalCategoryOdyVO> comCalList = service.getFullSList();
			
			JSONArray jsonArr = new JSONArray();
			
			if(comCalList!=null) {
				for(ScalCategoryOdyVO scvo:comCalList) {
					JSONObject jsObj = new JSONObject();
					jsObj.put("scno", scvo.getScno());
					jsObj.put("scname", scvo.getScname());
					jsonArr.put(jsObj);
				}
			}
			return jsonArr.toString();
		}
		

	
	// 내 캘린더 추가 기능
	@ResponseBody
	@RequestMapping(value="/t1/addMyCalendar.tw",method = {RequestMethod.POST})
	public String addMyCalendar(HttpServletRequest request) {
		String scname = request.getParameter("scname");
		String employeeid = request.getParameter("fk_employeeid");
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("scname",scname);
		paraMap.put("employeeid",employeeid);
		
		
			int n = 0;
		
			try {
				n = service.addMyCalendar(paraMap);
			}catch (Throwable e) {
				
			}
		
		JSONObject jsObj = new JSONObject();
		if(n==1) {
			jsObj.put("n", n);
		}
		return jsObj.toString();
	}
	
	// 사내 캘린더 추가 기능
	@ResponseBody
	@RequestMapping(value="/t1/addComCalendar.tw",method = {RequestMethod.POST})
	public String addComCalendar(HttpServletRequest request) {
		String scname = request.getParameter("scname");
		String employeeid = request.getParameter("fk_employeeid");
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("scname",scname);
		paraMap.put("employeeid",employeeid);
		
		int n = 0;
		
		try {
			n = service.addComCalendar(paraMap);
		}catch (Throwable e) {
			
		}
		
	
		JSONObject jsObj = new JSONObject();
		if(n==1) {
			jsObj.put("n", n);
		}
		return jsObj.toString();
	}
	
	
	
	// 소분류 캘린더 삭제
	@ResponseBody
	@RequestMapping(value="/t1/deleteCalendar.tw" ,method = {RequestMethod.POST})
	public String deleteCalendar(HttpServletRequest request) {
		String scno = request.getParameter("scno");

		int n = service.deleteCalendar(scno);
		
		JSONObject jsObj = new JSONObject();
		if(n==1) {
			jsObj.put("n", n);
		}
		return jsObj.toString();
	}
	
	// 내 캘린더명 수정하기 
	@ResponseBody
	@RequestMapping(value="/t1/editMyCalendar.tw" ,method = {RequestMethod.POST})
	public String editCalendar(HttpServletRequest request) {
		String scno = request.getParameter("scno");
		String scname = request.getParameter("scname");
		String employeeid= request.getParameter("employeeid");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("scno", scno);
		paraMap.put("scname", scname);
		paraMap.put("employeeid", employeeid);
		
		int n = 0;
		
		try {
			n = service.editMyCalendar(paraMap);
		}catch (Throwable e) {
			
		}
		JSONObject jsObj = new JSONObject();
		if(n==1) {
			jsObj.put("n", n);
		}
		return jsObj.toString();
	}


	// 사내 캘린더명 수정하기 
	@ResponseBody
	@RequestMapping(value="/t1/editComCalendar.tw" ,method = {RequestMethod.POST})
	public String editComCalendar(HttpServletRequest request) {
			String scno = request.getParameter("scno");
			String scname = request.getParameter("scname");
			String employeeid= request.getParameter("employeeid");
			
			Map<String,String> paraMap = new HashMap<>();
			paraMap.put("scno", scno);
			paraMap.put("scname", scname);
			paraMap.put("employeeid", employeeid);
			
			int n = 0;
			
			try {
				n = service.editComCalendar(paraMap);
			}catch (Throwable e) {
				
			}
			JSONObject jsObj = new JSONObject();
			if(n==1) {
				jsObj.put("n", n);
			}
			return jsObj.toString();
	}

	
	// 홈페이지에서 내 캘린더 보이기
	@ResponseBody
	@RequestMapping(value="/t1/showMyCalendarHome.tw", method = {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String showMyCalendarHome(HttpServletRequest request) {
		
		String employeeid = request.getParameter("employeeid");
		
		List<ScheduleOdyVO> myCalendarList = service.getMyCalendarList(employeeid);
		
	
		JSONArray jsArr = new JSONArray();
		
		if(myCalendarList!=null) {
			for(ScheduleOdyVO svo : myCalendarList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("subject", svo.getSubject());
				jsObj.put("startdate", svo.getStartdate());
				jsObj.put("enddate", svo.getEnddate());
				jsObj.put("color", svo.getColor());
				jsObj.put("sdno", svo.getSdno());
			
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}

	
	
	// 홈페이지에서 해당 날짜를 클릭했을 때 내 일정 가져오기
	
	@ResponseBody
	@RequestMapping(value="/t1/myCalendarInfo.tw",method = {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String myCalendarInfo(HttpServletRequest request) {
		
		String employeeid = request.getParameter("employeeid");
		String date = request.getParameter("date");
		
		Map<String,String> paraMap = new HashMap<String, String>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("date", date);
		
		List<ScheduleOdyVO> mycalInfoList = service.myCalendarInfo(paraMap);
		
		JSONArray jsArr = new JSONArray();
		
		if(mycalInfoList!=null) {
			for(ScheduleOdyVO svo : mycalInfoList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("subject", svo.getSubject());
				jsObj.put("startdate", svo.getStartdate());
				jsObj.put("enddate", svo.getEnddate());
				jsObj.put("color", svo.getColor());
				jsObj.put("sdno", svo.getSdno());
				jsObj.put("stime", svo.getStime());
				jsObj.put("etime", svo.getEtime());
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}



	@ResponseBody
	@RequestMapping(value="/t1/todayMyCal.tw",method = {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String todayMyCal(HttpServletRequest request) {
		
		String employeeid = request.getParameter("employeeid");
	
		List<ScheduleOdyVO> todayMyCalList = service.todayMyCal(employeeid);
		
		JSONArray jsArr = new JSONArray();
		
		if(todayMyCalList!=null) {
			for(ScheduleOdyVO svo : todayMyCalList) {
				JSONObject jsObj = new JSONObject();
				jsObj.put("subject", svo.getSubject());
				jsObj.put("startdate", svo.getStartdate());
				jsObj.put("enddate", svo.getEnddate());
				jsObj.put("color", svo.getColor());
				jsObj.put("sdno", svo.getSdno());
				jsObj.put("stime", svo.getStime());
				jsObj.put("etime", svo.getEtime());
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}
	




}
