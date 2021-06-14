package com.t1works.groupware.hsy.controller;

import java.util.Calendar;
import java.util.*;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.common.GoogleMail;
import com.t1works.groupware.hsy.model.ClientHsyVO;
import com.t1works.groupware.hsy.model.ProductHsyVO;
import com.t1works.groupware.hsy.model.TodoHsyVO;
import com.t1works.groupware.hsy.service.InterProductHsyService;
import com.t1works.groupware.hsy.service.InterTodoHsyService;

@Component
@Controller
public class TodoHsyController {
	
	@Autowired 
	private InterTodoHsyService service;
	
	@Autowired 
	private InterProductHsyService service2;
	
	@Autowired
	private GoogleMail mail;
	
	// 사원 업무관리 페이지 매핑 url
	@RequestMapping(value="/t1/employeeTodo.tw")    // 로그인이 필요한 url
	public ModelAndView requiredLogin_employeeTodo(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		
		String requiredState=request.getParameter("requiredState");
		// 1) 해당 페이지가 로드되면 처음에는 신규등록업무를 보여주기 위함
		if(requiredState==null) requiredState="0"; 		
		
		// "0":신규등록업무, "1":진행중업무, "2":진행완료업무 에 해당하지 않는 값이 url에 입력된 경우 신규등록업무로 보여준다
		if(!("0".equals(requiredState) || "1".equals(requiredState) || "2".equals(requiredState))) {
			requiredState="0";
		}
		
		
		String periodOption= request.getParameter("periodOption"); 
		// 2) 업무완료 페이지가 로드되면 처음에는 1주일 기간을 보여주기 위함
		if(periodOption==null) periodOption="week"; 
		
		if(!("week".equals(periodOption) || "month".equals(periodOption) || "3months".equals(periodOption)||"all".equals(periodOption))) {
			periodOption="week";
		}
			
		
		// 3) 업무관리페이지에서 보여줄 정보 페이징바 처리해서 정보 가져오기
		String currentShowPageNo= request.getParameter("currentShowPageNo");
		if(currentShowPageNo == null) currentShowPageNo = "1";
		
		try {
			Integer.parseInt(currentShowPageNo);
		} catch(NumberFormatException e) {
			currentShowPageNo = "1"; 
		}
		
		String sizePerPage= "4"; // 4행씩 고정
		if(!"4".equals(sizePerPage)) sizePerPage = "4";
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("currentShowPageNo", currentShowPageNo);
		paraMap.put("sizePerPage", sizePerPage);
		
		// 로그인유저의 사원번호와 requiredState, periodOption를 paraMap에 추가하기

		HttpSession session= request.getSession();
		MemberBwbVO loginuser= (MemberBwbVO) session.getAttribute("loginuser");
		paraMap.put("employeeid", loginuser.getEmployeeid());
		paraMap.put("requiredState", requiredState);
		paraMap.put("periodOption", periodOption);
		
		int totalPage = service.selectTotalPage(paraMap);
		
		if( Integer.parseInt(currentShowPageNo) > totalPage || Integer.parseInt(currentShowPageNo) < 1) {
			currentShowPageNo = "1";
			paraMap.put("currentShowPageNo", currentShowPageNo);
		}
		
		// 페이징바 처리한 업무관리 목록가져오기(트랜잭션 처리=> 지연된업무 update처리 동시진행)
		List<TodoHsyVO> tvoList= null;
		
		try {
			tvoList = service.employeeTodoList(paraMap); 
			// 해당항목이 없는경우 tvoList.size() == 0
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		
		mav.addObject("requiredState", requiredState);
		mav.addObject("tvoList", tvoList);
		mav.addObject("periodOption", periodOption);
		
		// 4) 페이징바 생성
		String pageBar= "";
		int blockSize= 5;
		int loop=1;
		int pageNo=0;
		
		pageNo= ((Integer.parseInt(currentShowPageNo)-1)/blockSize) * blockSize + 1 ;

		if(pageNo != 1) {
			pageBar += "&nbsp;<a href='employeeTodo.tw?currentShowPageNo=1&requiredState="+requiredState+"&periodOption="+periodOption+"'>[맨처음]</a>&nbsp;"; 
			pageBar += "&nbsp;<a href='employeeTodo.tw?currentShowPageNo="+(pageNo-1)+"&requiredState="+requiredState+"&periodOption="+periodOption+"'>[이전]</a>&nbsp;";
		}
		
		while(!(loop>blockSize || pageNo > totalPage) ) { // totalPage가 0인경우 페이지바는 생성되지 않는다
			if(pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "&nbsp;<span style='color:#fff; background-color: #003d66; font-weight:bold; padding:2px 4px;'>"+pageNo+"</span>&nbsp;";
			}
			else {
				pageBar += "&nbsp;<a href='employeeTodo.tw?currentShowPageNo="+pageNo+"&requiredState="+requiredState+"&periodOption="+periodOption+"'>"+pageNo+"</a>&nbsp;";
			}
			loop++;
			pageNo++;
		}// end of while-----------------------------
		
		if(pageNo <= totalPage) {
			pageBar += "&nbsp;<a href='employeeTodo.tw?currentShowPageNo="+pageNo+"&requiredState="+requiredState+"&periodOption="+periodOption+"'>[다음]</a>&nbsp";
			pageBar += "&nbsp;<a href='employeeTodo.tw?currentShowPageNo="+totalPage+"&requiredState="+requiredState+"&periodOption="+periodOption+"'>[마지막]</a>&nbsp";
		}
		
		mav.addObject("pageBar", pageBar);
		
		
		// 5) 신규등록업무, 진행중업무, 진행완료업무 총 건수 표시
		Map<String,String> cntMap= service.selectCntTodoByrequiredState(loginuser.getEmployeeid());
		
		mav.addObject("newTodoCnt", cntMap.get("newTodoCnt"));
		mav.addObject("ingTodoCnt", cntMap.get("ingTodoCnt"));
		mav.addObject("doneTodoCnt", cntMap.get("doneTodoCnt"));

		
		// 6) 기간별로 완료한 업무 총건수 나눠서 표시
		Map<String,String> donePeriodMap= service.selectCntTodoByPeriod(loginuser.getEmployeeid());
		mav.addObject("weekCnt", donePeriodMap.get("weekCnt"));
		mav.addObject("monthCnt", donePeriodMap.get("monthCnt"));
		mav.addObject("threeMonthsCnt", donePeriodMap.get("threeMonthsCnt"));
		
		
		mav.setViewName("hsy/toDo/employeeTodo.gwTiles");
		return mav;
		
	}// end of public ModelAndView requiredLogin_employeeTodo(----
	
	
	// 신규등록업무에서 진행중업무로 업데이트하는 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/goWorkStart.tw", method= {RequestMethod.POST})
	public String goWorkStart(HttpServletRequest request) {
		
		String fk_pNo= request.getParameter("fk_pNo");
		int n= service.goWorkStart(fk_pNo);
		// 업데이트 성공 시 n==1
		
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	} // end of public String goWorkStart(HttpServletRequest request) {------
	
	
	// 진행중인 업무 상태변경(진행중 또는 보류)에 따라 update하기
	@ResponseBody
	@RequestMapping(value="/t1/updateIngDetail.tw", method= {RequestMethod.POST})
	public String updateIngDetail(TodoHsyVO tvo) {
		
		int n=service.updateIngDetail(tvo);
		
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("n", n);  // update성공 시 n==1
		
		return jsonObj.toString();
	}// end of public String goWorkStart(HttpServletRequest request) {--------
	
	
	// 진행중 업무에서 진행완료 업무로 업데이트하는 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/goWorkDone.tw", method= {RequestMethod.POST})
	public String goWorkDone(HttpServletRequest request) {
			
		String fk_pNo= request.getParameter("fk_pNo");
		int n= service.goWorkDone(fk_pNo);
		// 업데이트 성공 시 n==1
		
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	} // end of public String goWorkDone(HttpServletRequest request) {------
	
	
	// 특정업무 상세 정보 조회1 (상품에 대한 정보)
	@ResponseBody
	@RequestMapping(value="/t1/selectProductInfoForModal.tw",produces="text/plain;charset=UTF-8")
	public String selectProductInfoForModal(HttpServletRequest request) {
			
		// 1) 여행상품 상세번호 알아오기
		String pNo= request.getParameter("fk_pNo");
				
		// pNo 존재하는지 확인하기 (url 장난 방지) => 숫자형태가 아닌 문자입력 또는 존재하지 않는 pNo 입력한 경우
		int n= service2.isExistPno(pNo);
		if(n==0) { // url 장난친 경우 제일 첫번째 상품을 보여준다
			pNo="1"; 
		}
		
		// 2) pNo를 이용하여 필요한 상품정보 가져오기
		ProductHsyVO pvo= service2.selectOneProductInfo(pNo);
		
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("pName", pvo.getpName());
		jsonObj.put("miniNo", pvo.getMiniNo());
		jsonObj.put("maxNo", pvo.getMaxNo());
		jsonObj.put("nowNo", pvo.getNowNo());
		jsonObj.put("startDate", pvo.getStartDate());
		jsonObj.put("endDate", pvo.getEndDate());
		jsonObj.put("period", pvo.getPeriod());
		
		return jsonObj.toString();
		
	} // end of public String selectProductInfoForModal(HttpServletRequest request)---
	
	
	// 특정업무 상세 정보 조회2 (고객에 대한 정보)
	@ResponseBody
	@RequestMapping(value="/t1/selectClientInfoForModal.tw",method= {RequestMethod.POST},produces="text/plain;charset=UTF-8")
	public String selectClientInfoForModal(HttpServletRequest request) {

		// 1) 여행상품 상세번호 알아오기
		String pNo= request.getParameter("fk_pNo");
			
		// 2) 페이징바 처리한 고객정보 가져오기 사전작업 => post방식
		String currentShowPageNo=request.getParameter("currentShowPageNo");
		if(currentShowPageNo==null) {
			currentShowPageNo="1";
		}
		
		int sizePerPage=3;
		
		int startRno = (( Integer.parseInt(currentShowPageNo) - 1 ) * sizePerPage) + 1;
	    int endRno = startRno + sizePerPage - 1; 
		
	    Map<String,String> paraMap= new HashMap<>();
	    paraMap.put("pNo", pNo);
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
	
		// 3) paraMap을 이용하여 해당 currentShowPageNo에 속하는 고객정보 가져오기
		List<ClientHsyVO> cvoList= service.selectClientInfoByPno(paraMap);
		
		JSONArray jsonArr= new JSONArray();
		for(ClientHsyVO cvo: cvoList) { // 고객정보가 없는 경우 => 자바스크립트에 처리 (jsonArr.length==0)
			
			JSONObject jsonObj= new JSONObject();
			jsonObj.put("clientname",cvo.getClientname());
			jsonObj.put("clientmobile",cvo.getClientmobile());
			jsonObj.put("cnumber",cvo.getCnumber());
			jsonObj.put("fk_pNo",cvo.getFk_pNo());
			jsonObj.put("endDate",cvo.getEndDate());
			
			jsonArr.put(jsonObj);
		}// end of for--------------------
		
		return jsonArr.toString();
		
	} // end of public String selectProductInfoForModal(HttpServletRequest request) {-----
	
	
	// 특정업무 상세 정보 조회3 (고객에 대한 정보 totalPage 알아오기)
	@ResponseBody
	@RequestMapping(value="/t1/getclientLisTotalPage.tw",method= {RequestMethod.GET})
	public String getclientLisTotalPage(HttpServletRequest request) {
		
		String fk_pNo= request.getParameter("fk_pNo");
		String sizePerPage= request.getParameter("sizePerPage");		
		if(!"3".equals(sizePerPage)) sizePerPage = "3";
		
		// fk_pNo 존재하는지 확인하기 (url 장난 방지) => 숫자형태가 아닌 문자입력 또는 존재하지 않는 pNo 입력한 경우
		int n= service2.isExistPno(fk_pNo);
		
		JSONObject jsonObj= new JSONObject();
		
		if(n!=0) { //  fk_pNo 존재하는 경우
			Map<String,String> paraMap= new HashMap<>();
			paraMap.put("fk_pNo", fk_pNo);
			paraMap.put("sizePerPage", sizePerPage);
			
			// 특정업무에 관한 고객의 totalPage 수 알아오기
			int totalPage= service.getclientLisTotalPage(paraMap);
			jsonObj.put("totalPage", totalPage); 
		}
		else { // fk_pNo 존재하지 않는 경우
			jsonObj.put("totalPage", 0); 
		}
		
		return jsonObj.toString();
		
	} // end of public String getclientLisTotalPage(HttpServletRequest request) {-------
	
	
	// 대리, 사원 나의실적현황 매핑 url
	@RequestMapping(value="/t1/employeePerformance.tw")    // 로그인 필요 url
	public ModelAndView requiredLogin_employeePerformance(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {

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
		
		mav.setViewName("hsy/toDo/employeePerformance.gwTiles");
		return mav;
	} // end of public ModelAndView employeePerformance(ModelAndView mav) {--------
	
	
	// 특정직원이 선택한 년월로부터 6개월 이전의 처리 업무 건 수, 담당 고객 수  가져오기 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/getEmployeePerformance.tw",method= {RequestMethod.POST},produces="text/plain;charset=UTF-8")
	public String getEmployeePerformance(HttpServletRequest request) {
		
		// 특정 직원의 정보 가져오기
		HttpSession session = request.getSession();
		MemberBwbVO loginuser= (MemberBwbVO) session.getAttribute("loginuser");
		String employeeid= loginuser.getEmployeeid();
		
		String year= request.getParameter("year");
		String month= request.getParameter("month");
		
		// 월이 한자리인 경우 앞에 0 붙여서 map에 담기
		if(month.length()==1) month="0"+month;
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("date",year+"-"+month);
		
		// 특정 직원의 입사일 가져오기 => paraMap2에서 사용하기 위함
		String hiredate= loginuser.getHiredate();
		hiredate= hiredate.substring(0,4)+"-"+hiredate.substring(5,7);
		
		// 1) 선택 날짜로 부터 6개월 이전까지의 날짜리스트 만들기 => sql 이용
		Map<String,String> dateMap= service.getDateBeforeSix(paraMap.get("date"));
		
		// 확장for문을 돌리기 위해 날짜 오름차순으로 리스트에 넣어주기
		List<String> dateList= new ArrayList<>();
		
		dateList.add(dateMap.get("prev5"));
		dateList.add(dateMap.get("prev4"));
		dateList.add(dateMap.get("prev3"));
		dateList.add(dateMap.get("prev2"));
		dateList.add(dateMap.get("prev1"));
		dateList.add(paraMap.get("date"));
		
		JsonArray jsonArr= new JsonArray();
		
		for(String specificDate: dateList) {
			
			// 2) 해당년월의 처리업무에 해당하는 fk_pno 가져오기=> 고객테이블과 연관시키기 위함
			paraMap.put("specificDate", specificDate);
			List<String> fk_pnoList= service.getFk_pnoListByDate(paraMap);
			
			// list에 담긴 fk_pno 가공하기 =>  in절에 사용될 수 있도록 데이터 가공
			String fk_pnoForIn="";
			if(fk_pnoList.size()==0) fk_pnoForIn="-1"; // 해당 6개월 이내에 처리업무건수가 한 건도 없는 경우 (-1은 존재하지 않는 상품번호이다)
			else { // 해당 6개월 이내에 처리업무건수가 한 건 이상 있는 경우
				
				for(int i=0; i<fk_pnoList.size();i++) {   
					String str= (i < fk_pnoList.size()-1) ? "," : "";
					fk_pnoForIn+= fk_pnoList.get(i)+str;
				} // end of for------------------------
			}
			
			// 3) 해당년월의 처리 업무 건 수 와 고객 수 가져오기 
			Map<String,String> paraMap2= new HashMap<>();
			paraMap2.put("employeeid", employeeid);
			paraMap2.put("specificDate", specificDate);
			paraMap2.put("fk_pnoForIn", fk_pnoForIn);
			paraMap2.put("hiredate", hiredate);
			
			// System.out.println(employeeid);
			// System.out.println(specificDate);
			// System.out.println(fk_pnoForIn);
			// System.out.println(hiredate);
			
			// 입사년도 전의 날짜값인 경우 '-' => sql로 처리
			Map<String,String> EachPerfAndClientCnt= service.getPerfAndClientCnt(paraMap2);
			
			JsonObject jsonObj= new JsonObject();
			jsonObj.addProperty("specificDate", EachPerfAndClientCnt.get("specificDate"));
			
			// 입사년도 전의 날짜인 경우 처리 업무 건 수와 담당 고객 수 '-'로 처리
			if("-".equals(EachPerfAndClientCnt.get("specificDate"))) {
				jsonObj.addProperty("perfNumber", "-");
				jsonObj.addProperty("clienNumber", "-");
			}
			else {
				jsonObj.addProperty("perfNumber", EachPerfAndClientCnt.get("perfNumber"));
				jsonObj.addProperty("clienNumber", EachPerfAndClientCnt.get("clienNumber"));
			}
			
			jsonArr.add(jsonObj);
			
		} // end of for(String specificDate: dateList) {---------
		
		return new Gson().toJson(jsonArr);
		
	} // end of public String getEmployeePerformance(HttpServletRequest request) {-------
	
	
	// 자세히 보기 클릭 시 해당년월에 대한 처리업무와 담당고객 정보를 모달에 넣어주기 위한 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/getPerfClientInfoForModal.tw",method= {RequestMethod.POST},produces="text/plain;charset=UTF-8")
	public String getPerfClientInfoForModal(HttpServletRequest request) {
		
		String certainDate= request.getParameter("certainDate");
		String employeeid= request.getParameter("employeeid");
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("certainDate", certainDate);
		paraMap.put("employeeid", employeeid);
		
		// 특정 년월에 끝난 업무 정부 가져오기 => 업무명, 시작일, 종료일, 담당 고객 수 (종료일 오름차순)
		List<TodoHsyVO> modalList= service.getPerfClientInfoForModal(paraMap);
		
		JSONArray jsonArr= new JSONArray();
		
		if(modalList.size()!=0) {
			for(TodoHsyVO tvo:modalList) {
				
				JSONObject jsonObj= new JSONObject();
				jsonObj.put("rno", tvo.getRno());
				jsonObj.put("nowNo", tvo.getNowNo());
				jsonObj.put("pName", tvo.getpName());
				jsonObj.put("startDate", tvo.getStartDate());
				jsonObj.put("endDate", tvo.getEndDate());
				jsonObj.put("dueDate", tvo.getDueDate());
				
				jsonArr.put(jsonObj);
				
			} // end of for(TodoHsyVO tvo:modalList) {-----
		} 
		
		return jsonArr.toString();
		// 특정 년월에 끝난 업무가 한 건도 없으면 jsonArr.length==0
		
	} // end of public String getPerfClientInfoForModal(HttpServletRequest request) {----
	
	
	
	// 여행준비물 메일 보내기 ajax 매핑 url
	@ResponseBody
	@RequestMapping(value="/t1/sendEmailIngTodo.tw",method= {RequestMethod.POST},produces="text/plain;charset=UTF-8")
	public String sendEmailIngTodo(HttpServletRequest request) {
		
		// 고객 테이블의 pk인 clientmobile, fk_pNo를 통해 메일 보낼 때 사용 될 정보 가져오기
		String clientmobile= request.getParameter("clientmobile");
		String fk_pNo= request.getParameter("fk_pNo");
		
		Map<String,String> paraMap= new HashMap<>();
		paraMap.put("clientmobile", clientmobile);
		paraMap.put("fk_pNo", fk_pNo);
		
		ClientHsyVO cvo= service.getInfoForSendEmailIngTodo(paraMap);
		
		int n=0;
		try {
			mail.sendmail(cvo);
			
		} catch(Exception e) { // 메일 전송이 실패한 경우
			e.printStackTrace();
			n=1;
		}
		
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("n", n);  
		
		 /*
	 	 	n==1 메일 전송 실패
	 	 	n==0 메일 전송 성공
		 */
		
		return jsonObj.toString();
	} // end of public String sendEmailIngTodo(HttpServletRequest request) {-----
	
}
