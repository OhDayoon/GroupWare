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
import com.t1works.groupware.ody.model.*;
import com.t1works.groupware.ody.service.InterReservationOdyService;


@Component
@Controller
public class ReservationOdyController {
	
	@Autowired
	private InterReservationOdyService service;
	
	// 회의실 대여신청 view
	@RequestMapping(value="/t1/rsRoom.tw")
	public String requiredLogin_room(HttpServletRequest request, HttpServletResponse response) {
		
	
		List<RoomOdyVO> roomList= service.getRoomList();
		
	
		request.setAttribute("roomList", roomList);
		return "ody/reservation/reserveRoom.gwTiles";
	}
	
	// 회의실 대여신청 view
	@ResponseBody
	@RequestMapping(value="/t1/rsroom/reserveRoom.tw", produces="text/plain;charset=UTF-8")
	public String requiredLogin_rsroom(HttpServletRequest request, HttpServletResponse response) {
		
		String roomno = request.getParameter("roomno");
		String rdate = request.getParameter("chgdate");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("roomno", roomno);
		paraMap.put("rdate", rdate);
		
		List<RsRoomOdyVO> reserveRoomList = service.getReserveRoomList(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		if(reserveRoomList != null) {
			for(RsRoomOdyVO rsvo : reserveRoomList) {
				JSONObject jsonObj = new JSONObject(); // {}
				 
				jsonObj.put("rsroomno", rsvo.getRsroomno());         // {"no":"101"}
				jsonObj.put("fk_roomno", rsvo.getFk_roomno());    
				jsonObj.put("fk_employeeid", rsvo.getFk_employeeid()); 
				jsonObj.put("name", rsvo.getName()); 
				jsonObj.put("rsubject", rsvo.getRsubject()); // {"no":"101", "name":"오다윤", "writeday":"2021-05-13 11:38:59"}
				jsonObj.put("rdate", rsvo.getRdate());
				jsonObj.put("rtime", rsvo.getRtime());
				jsonObj.put("rdname", rsvo.getRdname());
				
				
				jsonArr.put(jsonObj); // [{"no":"101", "name":"오다윤", "writeday":"2021-05-13 11:38:59"}] 
			} // end of for-------------------
		}

		return jsonArr.toString(); // 
	}
	
	// 회의실 예약하기
	@ResponseBody
	@RequestMapping(value="/t1/addReserveRoom.tw" , method = {RequestMethod.POST})
	public String addReserveRoom(HttpServletRequest request, HttpServletResponse response) {
		
		String rtimeArr = request.getParameter("rtime");
		
		String[] rArr = rtimeArr.split(",");
		
		String fk_roomno = request.getParameter("fk_roomno");
		String rdate= request.getParameter("rdate");
		String fk_employeeid= request.getParameter("fk_employeeid");
		String rsubject= request.getParameter("rsubject");

		
		int n=0;
		for(int i=0;i<rArr.length;i++) {
			String rtime= rArr[i];
		//	System.out.println(rtime);
			Map<String,String> paraMap = new HashMap<>();
			paraMap.put("fk_roomno", fk_roomno);
			paraMap.put("rdate", rdate);
			paraMap.put("fk_employeeid", fk_employeeid);
			paraMap.put("rsubject", rsubject);
			paraMap.put("rtime", rtime);
			n = service.insert_rsRoom(paraMap);
		}
	

		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
		return jsObj.toString();
	}
	
	
	// 사무용품 대여신청 view
	@RequestMapping(value="/t1/rsGoods.tw")
	public String requiredLogin_goods(HttpServletRequest request, HttpServletResponse response) {
		
		
		List<GoodsOdyVO> goodsList= service.getGoodsList();
		
		request.setAttribute("goodsList", goodsList);
		return "ody/reservation/reserveGoods.gwTiles";
	}
	
	// 사무용품 대여신청 view
	@ResponseBody
	@RequestMapping(value="/t1/rsGoods/reserveGoods.tw", produces="text/plain;charset=UTF-8")
	public String requiredLogin_rsGoods(HttpServletRequest request, HttpServletResponse response) {
		
		String gno = request.getParameter("gno");
		String rgdate = request.getParameter("chgdate");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("gno", gno);
		paraMap.put("rgdate", rgdate);
		
		
		List<RsGoodsOdyVO> reserveGoodsList = service.getReserveGoodsList(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		if(reserveGoodsList != null) {
			for(RsGoodsOdyVO rsgvo : reserveGoodsList) {
				JSONObject jsonObj = new JSONObject(); // {}
				 
				jsonObj.put("rsgno", rsgvo.getRsgno());         // {"no":"101"}
				jsonObj.put("fk_gno", rsgvo.getFk_gno());    
				jsonObj.put("fk_employeeid", rsgvo.getFk_employeeid()); 
				jsonObj.put("name", rsgvo.getName()); 
				jsonObj.put("rgsubject", rsgvo.getRgsubject()); // {"no":"101", "name":"오다윤", "writeday":"2021-05-13 11:38:59"}
				jsonObj.put("rgdate", rsgvo.getRgdate());
				jsonObj.put("rgtime", rsgvo.getRgtime());
				jsonObj.put("rdname", rsgvo.getRdname());
				jsonObj.put("gstatus", rsgvo.getGstatus());
				
				jsonArr.put(jsonObj); // [{"no":"101", "name":"오다윤", "writeday":"2021-05-13 11:38:59"}] 
			} // end of for-------------------
		}

		return jsonArr.toString(); // 
	}
	
	
	// 차량 대여 신청
	@RequestMapping(value="/t1/rsCar.tw")
	public String requiredLogin_car(HttpServletRequest request, HttpServletResponse response) {
		
		List<CarOdyVO> carList= service.getCarList();
		
		request.setAttribute("carList", carList);
		return "ody/reservation/reserveCar.gwTiles";
	}
	
	// 차량 대여 신청
	@ResponseBody
	@RequestMapping(value="/t1/rscar/reserveCar.tw", produces="text/plain;charset=UTF-8")
	public String requiredLogin_rsCar(HttpServletRequest request, HttpServletResponse response) {
		
		String cno = request.getParameter("cno");
		String rcdate = request.getParameter("chgdate");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("cno", cno);
		paraMap.put("rcdate", rcdate);
		
		
		List<RsCarOdyVO> reserveCarList = service.getReserveCarList(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		if(reserveCarList != null) {
			for(RsCarOdyVO rscvo : reserveCarList) {
				JSONObject jsonObj = new JSONObject(); // {}
				 
				jsonObj.put("rscno", rscvo.getRscno());         // {"no":"101"}
				jsonObj.put("fk_cno", rscvo.getFk_cno());    
				jsonObj.put("fk_employeeid", rscvo.getFk_employeeid()); 
				jsonObj.put("name", rscvo.getName()); 
				jsonObj.put("rcsubject", rscvo.getRcsubject()); // {"no":"101", "name":"오다윤", "writeday":"2021-05-13 11:38:59"}
				jsonObj.put("rcdate", rscvo.getRcdate());
				jsonObj.put("rctime", rscvo.getRctime());
				jsonObj.put("rdestination", rscvo.getRdestination());
				jsonObj.put("rcpeople", rscvo.getRcpeople());
				jsonObj.put("cstatus", rscvo.getCstatus());
				
				jsonArr.put(jsonObj); // [{"no":"101", "name":"오다윤", "writeday":"2021-05-13 11:38:59"}] 
			} // end of for-------------------
		}

		return jsonArr.toString(); // 
	}
	
	
	
	// 차량 대여신청시 지도 팝업
	@RequestMapping(value="/t1/searchMap.tw")
	public String searchMap(HttpServletRequest request) {
	
		return "ody/searchPlace";
	}
	
	
	// 사무용품 예약하기
		@ResponseBody
		@RequestMapping(value="/t1/addReserveGoods.tw" , method = {RequestMethod.POST})
		public String addReserveGoods(HttpServletRequest request, HttpServletResponse response) {
			
			String rgtimeArr = request.getParameter("rgtime");
			
			String[] rgArr = rgtimeArr.split(",");
			
			String gno = request.getParameter("gno");
			String rgdate= request.getParameter("rgdate");
			String fk_employeeid= request.getParameter("fk_employeeid");
			String rgdepartment= request.getParameter("rgdepartment");
			String rgsubject= request.getParameter("rgsubject");

			int n=0;
			
			for(int i=0;i<rgArr.length;i++) {
				String rgtime= rgArr[i];
			//	System.out.println(rtime);
				Map<String,String> paraMap = new HashMap<>();
				paraMap.put("gno", gno);
				paraMap.put("rgdate", rgdate);
				paraMap.put("fk_employeeid", fk_employeeid);
				paraMap.put("rgdepartment", rgdepartment);
				paraMap.put("rgsubject", rgsubject);
				paraMap.put("rgtime", rgtime);
				n = service.insert_rsGoods(paraMap);
			}
			
			JSONObject jsObj = new JSONObject();
			jsObj.put("n", n);
			return jsObj.toString();
		}
		

	// 회의실 예약 삭제하기
	@ResponseBody
	@RequestMapping(value="/t1/rsroom/delReserveRoom.tw", method= {RequestMethod.POST})
	public String delReserveRoom(HttpServletRequest request) {
		
		String rsroomno = request.getParameter("rsroomno");
		
		int n =service.delReserveRoom(rsroomno);
		System.out.println(n);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		return jsonObj.toString();
	}

	// 사무용품 예약 삭제
	@ResponseBody
	@RequestMapping(value="/t1/rsGoods/delReserveGoods.tw", method= {RequestMethod.POST})
	public String delReserveGoods(HttpServletRequest request) {
		
		String rsgno = request.getParameter("rsgno");
		
		int n =service.delReserveGoods(rsgno);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		return jsonObj.toString();
	}
	
	// 차량 예약하기
	@ResponseBody
	@RequestMapping(value="/t1/addReserveCar.tw", method= {RequestMethod.POST})
	public String addReserveCar(HttpServletRequest request, HttpServletResponse response) {
		
		String rctimeArr = request.getParameter("rctime");
		
		String[] rcArr = rctimeArr.split(",");
		
		String cno = request.getParameter("cno");
		String rcdate= request.getParameter("rcdate");
		String fk_employeeid= request.getParameter("fk_employeeid");
		String rdestination= request.getParameter("rdestination");
		String rcpeople= request.getParameter("rcpeople");
		String rcsubject= request.getParameter("rcsubject");

		int n=0;
		
		for(int i=0;i<rcArr.length;i++) {
			String rctime= rcArr[i];
		//	System.out.println(rtime);
			Map<String,String> paraMap = new HashMap<>();
			paraMap.put("cno", cno);
			paraMap.put("rcdate", rcdate);
			paraMap.put("fk_employeeid", fk_employeeid);
			paraMap.put("rdestination", rdestination);
			paraMap.put("rcpeople", rcpeople);
			paraMap.put("rcsubject", rcsubject);
			paraMap.put("rctime", rctime);
			n = service.insert_rsCar(paraMap);
		}
	
		JSONObject jsObj = new JSONObject();
		jsObj.put("n", n);
		return jsObj.toString();
	}
	
	// 차량 예약 삭제
	@ResponseBody
	@RequestMapping(value="/t1/rsCar/delReserveCar.tw", method= {RequestMethod.POST})
	public String delReserveCar(HttpServletRequest request) {
			
		String rscno = request.getParameter("rscno");
			
		int n =service.delReserveCar(rscno);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		return jsonObj.toString();
	}
		
	
	// 나의 회의실 예약 내역 리스트 
	@RequestMapping(value="/t1/myReservedRoom.tw")
	public ModelAndView requiredLogin_myReservedRoom(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		HttpSession session = request.getSession();
		// 메모리에 생성되어져 있는 session을 불러오는 것이다.
		session.setAttribute("readCountPermission", "yes");
		
		MemberBwbVO loginuser =(MemberBwbVO)session.getAttribute("loginuser");
		
		// 회의실 리스트
		List<RoomOdyVO> roomList= service.getRoomList();
		
		String employeeid = loginuser.getEmployeeid();
		
		List<RsRoomOdyVO> myRoomList = null;
		// 나의 대여 현황
		
		
	
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String str_roomno = request.getParameter("roomno");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		int roomno=0;

		if(str_roomno==null || "".equals(str_roomno)) {
			roomno=0;
		}
		else {
			try {
				roomno= Integer.parseInt(str_roomno);
			}catch (NumberFormatException e) {
				roomno=0;
			}
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
		paraMap.put("roomno", String.valueOf(roomno));
		paraMap.put("searchWord", searchWord);
		paraMap.put("startdate", startdate);
		paraMap.put("enddate", enddate);
		paraMap.put("employeeid", employeeid);


		
		int totalCount =0;     	
		int currentShowPageNo =0;
		int totalPage = 0;        
		int sizePerPage= 10;
		int startRno = 0;           
	    int endRno = 0;            
	    
	    
	    // 나의 예약된 회의실 총 건수(totalCount)
	    totalCount = service.getTotalCountMyRoom(paraMap);
      
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
	    
	    
	    myRoomList = service.showMyRoomListSearchWithPaging(paraMap);
	
	    // 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
	
		mav.addObject("paraMap", paraMap);
	
		
		// === #121. 페이지바 만들기 === //
		
		
		int blockSize= 5;
		
		int loop = 1;
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
	   
		String pageBar = "<ul style='list-style:none;'>";
		
		String url = "myReservedRoom.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo!=1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&roomno="+roomno+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&roomno="+roomno+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		while(!(loop>blockSize || pageNo>totalPage)) {
			
			if(pageNo==currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; font-weight: bold;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&roomno="+roomno+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while--------------------
		
		// === [다음][마지막] 만들기 === //
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&roomno="+roomno+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&roomno="+roomno+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		pageBar += "</ul>";
		
		mav.addObject("pageBar",pageBar);
		
		mav.addObject("roomList", roomList);
		mav.addObject("myRoomList",myRoomList);
		mav.setViewName("ody/reservation/myReservedRoom.gwTiles");

		return mav;
			
	}
	
	// 나의 회의실 예약현황에서 삭제한 경우
	@ResponseBody
	@RequestMapping(value="/t1/delmyReservedRoom.tw", method = {RequestMethod.POST})
	public String delMyReservedRoom(HttpServletRequest request) {
		
		String rsroomno = request.getParameter("rsroomno");
		
		int n=0;
		
		n=service.delReserveRoom(rsroomno);
		
		JSONObject jsObj = new JSONObject();
		
		jsObj.put("n", n);
		return jsObj.toString();
	}
	
	// 나의 사무용품 예약 현황 조회
	@RequestMapping(value="/t1/myReservedGoods.tw")
	public ModelAndView requiredLogin_myReservedGoods(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
	
		HttpSession session = request.getSession();
		// 메모리에 생성되어져 있는 session을 불러오는 것이다.
		
		MemberBwbVO loginuser =(MemberBwbVO)session.getAttribute("loginuser");
		
		// 회의실 리스트
		String employeeid = loginuser.getEmployeeid();
		
		List<GoodsOdyVO> myGoodsList = null;
		// 나의 대여 현황
		List<GoodsOdyVO> goodsList= service.getGoodsList();
		
		session.setAttribute("readCountPermission", "yes");
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String str_gno = request.getParameter("gno");
		String searchWord = request.getParameter("searchWord");
		String str_rstatus = request.getParameter("rstatus");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		int gno = 0;
		
		if(str_gno==null || "".equals(str_gno)) {
			gno=0;
		}
		else {
			try {
				gno= Integer.parseInt(str_gno);
			}catch (NumberFormatException e) {
				gno=0;
			}
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

		int rstatus=-1;
		if(str_rstatus==null || "".equals(str_rstatus)) {
			rstatus=-1;
		}
		else {
			try {
				rstatus= Integer.parseInt(str_rstatus);
			}catch (NumberFormatException e) {
				rstatus=-1;
			}
		}
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("gno", String.valueOf(gno));
		paraMap.put("searchWord", searchWord);
		paraMap.put("startdate", startdate);
		paraMap.put("enddate", enddate);
		paraMap.put("employeeid", employeeid);
		paraMap.put("rstatus",String.valueOf(rstatus));

		
		int totalCount =0;        // 총 게시물 건수		
		int currentShowPageNo =0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int sizePerPage= 10;
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
	    
	    
	    // 총 일정 건수(totalCount)
	    totalCount = service.getTotalCountMyGoods(paraMap);
      
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
	    
	    
	    myGoodsList = service.showMyGoodsListSearchWithPaging(paraMap);
	
	    // 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
	
		mav.addObject("paraMap", paraMap);
	
		
		// === #121. 페이지바 만들기 === //
		
		
		int blockSize= 5;
		
		int loop = 1;
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
	   
		String pageBar = "<ul style='list-style:none;'>";
		
		String url = "myReservedGoods.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo!=1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rsgno="+gno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rsgno="+gno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		while(!(loop>blockSize || pageNo>totalPage)) {
			
			if(pageNo==currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; font-weight: bold;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rsgno="+gno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while--------------------
		
		// === [다음][마지막] 만들기 === //
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rsgno="+gno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rsgno="+gno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		pageBar += "</ul>";
		
		mav.addObject("pageBar",pageBar);
		
		
		
		
		mav.addObject("goodsList", goodsList);
		mav.addObject("myGoodsList",myGoodsList);
		mav.setViewName("ody/reservation/myReservedGoods.gwTiles");
		
		return mav;
	}
	
	
	// 사무용품 예약 삭제
	@ResponseBody
	@RequestMapping(value="/t1/delmyReservedGoods.tw", method= {RequestMethod.POST})
	public String delMyReservedGoods(HttpServletRequest request) {
		
		String rsgno = request.getParameter("rsgno");
		
		int n =service.delReserveGoods(rsgno);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		return jsonObj.toString();
	}
		
		
	
	// 나의 차량 예약 현황 보이기
	@RequestMapping(value="/t1/myReservedCar.tw")
	public ModelAndView requiredLogin_myReservedCar(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
	
		HttpSession session = request.getSession();
	
		MemberBwbVO loginuser =(MemberBwbVO)session.getAttribute("loginuser");
		
		
		String employeeid = loginuser.getEmployeeid();
		// 나의 대여 현황
		List<CarOdyVO> myCarList = null;

		List<CarOdyVO> carList= service.getCarList();
		
		session.setAttribute("readCountPermission", "yes");
		
		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String str_cno = request.getParameter("cno");
		String searchWord = request.getParameter("searchWord");
		String str_rstatus = request.getParameter("rstatus");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		
		int cno = 0;
		
		if(str_cno==null || "".equals(str_cno)) {
			cno=0;
		}
		else {
			try {
				cno= Integer.parseInt(str_cno);
			}catch (NumberFormatException e) {
				cno=0;
			}
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

		int rstatus=-1;
		if(str_rstatus==null || "".equals(str_rstatus)) {
			rstatus=-1;
		}
		else {
			try {
				rstatus= Integer.parseInt(str_rstatus);
			}catch (NumberFormatException e) {
				rstatus=-1;
			}
		}
		
		Map<String, String> paraMap = new HashMap<String, String>();
		paraMap.put("cno", String.valueOf(cno));
		paraMap.put("searchWord", searchWord);
		paraMap.put("startdate", startdate);
		paraMap.put("enddate", enddate);
		paraMap.put("employeeid", employeeid);
		paraMap.put("rstatus",String.valueOf(rstatus));

		
		int totalCount =0;        // 총 게시물 건수		
		int currentShowPageNo =0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함.
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int sizePerPage= 10;
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
	    
	    
	    // 나의 차량 예약 총 건수(totalCount)
	    totalCount = service.getTotalCountMyCar(paraMap);
      
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
	    
	    
	    myCarList = service.showMyCarListSearchWithPaging(paraMap);
	
	    // 페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
	
		mav.addObject("paraMap", paraMap);
	
		
		// === #121. 페이지바 만들기 === //
		
		
		int blockSize= 5;
		
		int loop = 1;
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
	   
		String pageBar = "<ul style='list-style:none;'>";
		
		String url = "myReservedCar.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo!=1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rscno="+cno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rscno="+cno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
		while(!(loop>blockSize || pageNo>totalPage)) {
			
			if(pageNo==currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; font-weight: bold;'>"+pageNo+"</li>";
			}
			else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rscno="+cno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while--------------------
		
		// === [다음][마지막] 만들기 === //
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rscno="+cno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:12pt;'><a href='"+url+"?startdate="+startdate+"&enddate="+enddate+"&rscno="+cno+"&rstatus="+rstatus+"&searchWord="+searchWord+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
		}
		pageBar += "</ul>";
		
		mav.addObject("pageBar",pageBar);
		
		
		
		
		mav.addObject("carList", carList);
		mav.addObject("myCarList",myCarList);
		mav.setViewName("ody/reservation/myReservedCar.gwTiles");
		
		return mav;
	}
	

	// 차량 예약 삭제
	@ResponseBody
	@RequestMapping(value="/t1/delmyReservedCar.tw", method= {RequestMethod.POST})
	public String delMyReservedCar(HttpServletRequest request) {
		
		String rscno = request.getParameter("rscno");
		
		int n =service.delReserveCar(rscno);
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		return jsonObj.toString();
	}
		
	
	// 회의실 에약변경 시간 확인
	@ResponseBody
	@RequestMapping(value="/t1/reserve/checkTimeRoom.tw", produces="text/plain;charset=UTF-8" )
	public String checkTimeRoom(HttpServletRequest request) {
		String rdate = request.getParameter("rdate");
		String fk_roomno = request.getParameter("fk_roomno");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("rdate", rdate);
		paraMap.put("fk_roomno", fk_roomno);
		
		List<RsRoomOdyVO> roomTimeList = service.checkTimeRoom(paraMap);
	
		JSONArray jsArr = new JSONArray();
		
		if(roomTimeList!=null) {
			for(RsRoomOdyVO rvo : roomTimeList) {
				JSONObject jsObj = new JSONObject();			
				jsObj.put("rtime", rvo.getRtime());
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}
	
	
	// 회의실 예약 변경하기
	@RequestMapping(value="/t1/editReserveRoom.tw", method= {RequestMethod.POST})
	public ModelAndView editReserveRoom(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String rtimeArr = request.getParameter("rtime");
		
		String[] rArr = rtimeArr.split(",");
		
		String fk_roomno = request.getParameter("fk_roomno");
		String rdate= request.getParameter("rdate");
		String fk_employeeid= request.getParameter("fk_employeeid");
		String rsubject= request.getParameter("rsubject");
		String rsroomno = request.getParameter("rsroomno");
		int m = service.delReserveRoom(rsroomno);
		int n=0;
		if(m>0) {
			for(int i=0;i<rArr.length;i++) {
				String rtime= rArr[i];
			//	System.out.println(rtime);
				Map<String,String> paraMap = new HashMap<>();
				paraMap.put("fk_roomno", fk_roomno);
				paraMap.put("rdate", rdate);
				paraMap.put("fk_employeeid", fk_employeeid);
				paraMap.put("rsubject", rsubject);
				paraMap.put("rtime", rtime);
	
				n = service.insert_rsRoom(paraMap);
			
			}
	
		}
		if(n>0) {
			mav.addObject("message","회의실 예약이 변경되었습니다.");
			mav.addObject("loc",request.getContextPath()+"/t1/myReservedRoom.tw");
		}
		else {
			mav.addObject("message","회의실 예약 변경을 실패하였습니다.");
			mav.addObject("loc",request.getContextPath()+"/t1/myReservedRoom.tw");
		}
		
		mav.setViewName("msg");

		return mav;
	}
	
	
	// 사무용품 예약 변경 시간 확인
	@ResponseBody
	@RequestMapping(value="/t1/reserve/checkTimeGoods.tw", produces="text/plain;charset=UTF-8" )
	public String checkTimeGoods(HttpServletRequest request) {
		String rgdate = request.getParameter("rgdate");
		String fk_gno = request.getParameter("fk_gno");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("rgdate", rgdate);
		paraMap.put("fk_gno", fk_gno);
		
		List<RsGoodsOdyVO> goodsTimeList = service.checkTimeGoods(paraMap);
	
		JSONArray jsArr = new JSONArray();
		
		if(goodsTimeList!=null) {
			for(RsGoodsOdyVO gvo : goodsTimeList) {
				JSONObject jsObj = new JSONObject();			
				jsObj.put("rgtime", gvo.getRgtime());
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}

	
	// 사무용품 예약 변경하기
	@RequestMapping(value="/t1/editReserveGoods.tw", method= {RequestMethod.POST})
	public ModelAndView editReserveGoods(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String rgtimeArr = request.getParameter("rgtime");
		
		String[] rgArr = rgtimeArr.split(",");
		
		String gno = request.getParameter("fk_gno");
		String rgdate= request.getParameter("rgdate");
		String fk_employeeid= request.getParameter("fk_employeeid");
		String rgsubject= request.getParameter("rgsubject");
		String rsgno= request.getParameter("rsgno");
		
		int n=0;
		int m = service.delReserveGoods(rsgno);
		
		if(m>0) {
			for(int i=0;i<rgArr.length;i++) {
				String rgtime= rgArr[i];
				Map<String,String> paraMap = new HashMap<>();
				paraMap.put("gno", gno);
				paraMap.put("rgdate", rgdate);
				paraMap.put("fk_employeeid", fk_employeeid);
				paraMap.put("rgsubject", rgsubject);
				paraMap.put("rgtime", rgtime);
				n = service.insert_rsGoods(paraMap);
			}
		}
		
		if(n>0) {
			mav.addObject("message","사무용품 예약이 변경되었습니다.");
			mav.addObject("loc",request.getContextPath()+"/t1/myReservedGoods.tw");
		}
		else {
			mav.addObject("message","사무용품 예약 변경을 실패하였습니다.");
			mav.addObject("loc",request.getContextPath()+"/t1/myReservedGoods.tw");
		}
		
		mav.setViewName("msg");
		return mav;
	}

	
	// 차량 예약 변경 시간 확인
	@ResponseBody
	@RequestMapping(value="/t1/reserve/checkTimeCar.tw", produces="text/plain;charset=UTF-8" )
	public String checkTimeCar(HttpServletRequest request) {
		String rcdate = request.getParameter("rcdate");
		String fk_cno = request.getParameter("fk_cno");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("rcdate", rcdate);
		paraMap.put("fk_cno", fk_cno);
		
		List<RsCarOdyVO> carTimeList = service.checkTimeCar(paraMap);
	
		JSONArray jsArr = new JSONArray();
		
		if(carTimeList!=null) {
			for(RsCarOdyVO cvo : carTimeList) {
				JSONObject jsObj = new JSONObject();			
				jsObj.put("rctime", cvo.getRctime());
				jsArr.put(jsObj);
			}
		}
		
		return jsArr.toString();
	}
	
	// 차량 예약 변경하기
	@RequestMapping(value="/t1/editReserveCar.tw", method= {RequestMethod.POST})
	public ModelAndView editReserveCar(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String rctimeArr = request.getParameter("rctime");
		
		String[] rcArr = rctimeArr.split(",");
		
		String cno = request.getParameter("fk_cno");
		String rcdate= request.getParameter("rcdate");
		String fk_employeeid= request.getParameter("fk_employeeid");
		String rdestination= request.getParameter("rdestination");
		String rcpeople= request.getParameter("rcpeople");
		String rcsubject= request.getParameter("rcsubject");
		String rscno= request.getParameter("rscno");
		
		int n=0;
		
		int m = service.delReserveCar(rscno);
		
		if(m>0) {
			for(int i=0;i<rcArr.length;i++) {
				String rctime= rcArr[i];
			//	System.out.println(rtime);
				Map<String,String> paraMap = new HashMap<>();
				paraMap.put("cno", cno);
				paraMap.put("rcdate", rcdate);
				paraMap.put("fk_employeeid", fk_employeeid);
				paraMap.put("rdestination", rdestination);
				paraMap.put("rcpeople", rcpeople);
				paraMap.put("rcsubject", rcsubject);
				paraMap.put("rctime", rctime);
				n = service.insert_rsCar(paraMap);
			}
		}
		
		
		
		
		if(n>0) {
			mav.addObject("message","차량 예약이 변경되었습니다.");
			mav.addObject("loc",request.getContextPath()+"/t1/myReservedCar.tw");
		}
		else {
			mav.addObject("message","차량 예약 변경을 실패하였습니다.");
			mav.addObject("loc",request.getContextPath()+"/t1/myReservedCar.tw");
		}
		
		mav.setViewName("msg");
		return mav;
	}

	
}
