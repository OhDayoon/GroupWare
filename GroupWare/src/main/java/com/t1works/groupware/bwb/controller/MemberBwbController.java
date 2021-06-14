package com.t1works.groupware.bwb.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.t1works.groupware.bwb.model.CarGoodsBwbVO;
import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.bwb.model.ProductBwbVO;
import com.t1works.groupware.bwb.service.InterHomepageBwbService;
import com.t1works.groupware.bwb.service.InterMemberBwbService;
import com.t1works.groupware.bwb.service.InterProductBwbService;
import com.t1works.groupware.common.AES256;
import com.t1works.groupware.common.FileManager;
import com.t1works.groupware.common.GoogleMail;
import com.t1works.groupware.common.Sha256;
import com.t1works.groupware.hsy.model.ClientHsyVO;
import com.t1works.groupware.hsy.model.DepartmentHsyVO;
import com.t1works.groupware.hsy.model.MemberHsyVO;
import com.t1works.groupware.hsy.service.InterMemberHsyService;
import com.t1works.groupware.hsy.service.InterTodoHsyService;

@Controller
public class MemberBwbController {
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterMemberHsyService service;
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterMemberBwbService service2;
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
    private InterHomepageBwbService service3;
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private AES256 aes;
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterProductBwbService service4;
	
	@Autowired 
	private InterTodoHsyService service5;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private GoogleMail mail;
	
	// 인사부장- 업무관리(인사관리) 매핑 주소
	@RequestMapping(value="/t1/personnelManage.tw")        // 로그인이 필요한 url
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
		
		// 4) 모든 직위 가져오기
		List<MemberBwbVO> positionList = service2.selectPositionList();
		mav.addObject("positionList", positionList);
		
		
		
		try {
			Integer.parseInt(currentShowPageNo);
		} catch(NumberFormatException e) {
			currentShowPageNo = "1"; 
		}
		
		String sizePerPage= "7"; // 5행씩 고정
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
		
		if( Integer.parseInt(currentShowPageNo) > totalPage ) {
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
			pageBar += "&nbsp;<a href='personnelManage.tw?currentShowPageNo=1&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchWord="+searchWord+"'>[맨처음]</a>&nbsp;"; 
			pageBar += "&nbsp;<a href='personnelManage.tw?currentShowPageNo="+(pageNo-1)+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchWord="+searchWord+"'>[이전]</a>&nbsp;";
		}
		
		while(!(loop>blockSize || pageNo > totalPage) ) {
			if(pageNo == Integer.parseInt(currentShowPageNo)) {
				pageBar += "&nbsp;<span style='color:#fff; background-color: #5e5e5e; font-weight:bold; padding:2px 4px;'>"+pageNo+"</span>&nbsp;";
			}
			else {
				pageBar += "&nbsp;<a href='personnelManage.tw?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchWord="+searchWord+"'>"+pageNo+"</a>&nbsp;";
			}
			loop++;
			pageNo++;
		}// end of while-----------------------------
		
		if(pageNo <= totalPage) {
			pageBar += "&nbsp;<a href='personnelManage.tw?currentShowPageNo="+pageNo+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchWord="+searchWord+"'>[다음]</a>&nbsp";
			pageBar += "&nbsp;<a href='personnelManage.tw?currentShowPageNo="+totalPage+"&sizePerPage="+sizePerPage+"&searchType="+searchType+"&searchWord="+searchWord+"'>[마지막]</a>&nbsp";
		}
		
		mav.addObject("pageBar", pageBar);
		
		mav.setViewName("bwb/todo/personnelManage.gwTiles");
		return mav;
		
	} // end of public ModelAndView employeeMap(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {--------------------------------------------
	
	
	// 부서를 통해 직무내용 뽑아오기
	@ResponseBody
	@RequestMapping(value="/t1/selectDuty.tw",produces="text/plain;charset=UTF-8")
	public String requiredLogin_selectDuty(HttpServletRequest request,HttpServletResponse response) {
		
		String dname = request.getParameter("dname");
		
		String duty = service2.selectDuty(dname);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("duty", duty);
			
		return jsonObj.toString();
	}// end of public String requiredLogin_selectDuty(HttpServletRequest request,HttpServletResponse response) {
	
	// 인사관리를 통해 조직원 정보 업데이트하기
	@ResponseBody
	@RequestMapping(value="/t1/updateOneInfo.tw")
	public String updateOneInfo(MemberBwbVO mvo) {
		
		// serialize()한 데이터값 뽑아오기
		String cmobile = mvo.getCmobile();
		String mobile = mvo.getMobile();
		
		String[] cmobileArr = cmobile.split("-");
		String[] mobileArr = mobile.split("-");
		
		cmobile = "";
		mobile = "";
		
		for(String scmobile:cmobileArr){
			cmobile += scmobile;
		}
		mvo.setCmobile(cmobile);
		
		for(String smobile:mobileArr){
			mobile += smobile;
		}

		mvo.setMobile(mobile);
		
		// 회원정보 업데이트하기
		int n = service2.updateOneInfo(mvo);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	
	// 인사부장- 업무관리(신입사원 등록) 매핑 주소
	@RequestMapping(value="/t1/registerNewEmployee.tw")
	public ModelAndView requiredLogin_registerNewEmployee(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) { // 로그인이 필요한 url AOP사용
		
		// 1) 모든 부서명 가저오기
		List<DepartmentHsyVO> departmentList= service.selectAllDepartment();
		mav.addObject("departmentList", departmentList);
		
		// 2) 모든 직위 가져오기
		List<MemberBwbVO> positionList = service2.selectPositionList();
		mav.addObject("positionList", positionList);
		
		
		mav.setViewName("bwb/todo/registerNewEmployee.gwTiles");
		
		return mav;

	}
	
	// pcode에 따른 연차수 가져오기
	@ResponseBody
	@RequestMapping(value="/t1/selectOffCnt.tw")
	public String requiredLogin_selectOffCnt(HttpServletRequest request,HttpServletResponse response) {
		
		String pcode = request.getParameter("fk_pcode");
		
		String offcnt = service2.selectOffCnt(pcode);
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("offcnt", offcnt);
			
		return jsonObj.toString();
		
	}// end of public String requiredLogin_selectDuty(HttpServletRequest request,HttpServletResponse response) {
	
	
	// 직원정보등록하기(파일첨부가 없는)
	@ResponseBody
	@RequestMapping(value="/t1/registerOne_noAttach.tw")
	public String registerOne_noAttach(HttpServletRequest request, MemberBwbVO mvo) {
		
		
		String jubun1 = request.getParameter("jubun1");
		String jubun2 = request.getParameter("jubun2");
		
		String jubun = jubun1+jubun2;

		mvo.setJubun(jubun);
		mvo.setPasswd(jubun1);
		
		String mobile1 = request.getParameter("mobile1");
		String mobile2 = request.getParameter("mobile2");
		String mobile3 = request.getParameter("mobile3");
		
		String mobile = mobile1+mobile2+mobile3;
		mvo.setMobile(mobile);
		
		String cmobile1 = request.getParameter("cmobile1");
		String cmobile2 = request.getParameter("cmobile2");
		String cmobile3 = request.getParameter("cmobile3");
		
		String cmobile = cmobile1+cmobile2+cmobile3;
		mvo.setCmobile(cmobile);
		
		String dcode = request.getParameter("fk_dcode");
		// 등록한 직원의 fk_dcode를 통해 managerid 알아오기
		String managerid = service2.selectMangerid(dcode);
		mvo.setManagerid(managerid);

		int n = service2.registerOne(mvo);

		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("n", n);
			
		return jsonObj.toString();
		
	}// end of public String registerOne
	
	
	
	
	// 직원정보등록하기(파일첨부가 있는)
	@ResponseBody
	@RequestMapping(value="/t1/registerOne_withAttach.tw")
	public String registerOne_withAttach(MultipartHttpServletRequest mrequest, MemberBwbVO mvo) {
		
		
		String jubun1 = mrequest.getParameter("jubun1");
		String jubun2 = mrequest.getParameter("jubun2");
		
		String jubun = jubun1+jubun2;
		
		mvo.setJubun(jubun);
		mvo.setPasswd(jubun1);
		
		String mobile1 = mrequest.getParameter("mobile1");
		String mobile2 = mrequest.getParameter("mobile2");
		String mobile3 = mrequest.getParameter("mobile3");
		
		String mobile = mobile1+mobile2+mobile3;
		mvo.setMobile(mobile);
		
		String cmobile1 = mrequest.getParameter("cmobile1");
		String cmobile2 = mrequest.getParameter("cmobile2");
		String cmobile3 = mrequest.getParameter("cmobile3");
		
		String cmobile = cmobile1+cmobile2+cmobile3;
		mvo.setCmobile(cmobile);
		
		String dcode = mrequest.getParameter("fk_dcode");
		// 등록한 직원의 fk_dcode를 통해 managerid 알아오기
		String managerid = service2.selectMangerid(dcode);
		mvo.setManagerid(managerid);
		
		
		MultipartFile attach = mvo.getAttach(); // 컬럼명X
		
		if(!attach.isEmpty()) { // 첨부파일이 있을경우
			
			HttpSession session = mrequest.getSession();
			// was의 절대경로
			String root = session.getServletContext().getRealPath("/");
			// String root = "C:/Users/BAEK/git/GroupWare/GroupWare/src/main/webapp";
			// 첨부파일이 저장될 was의 폴더
			// String path = root+"resources"+File.separator+"files";
			String path = root+"resources"+File.separator+"images"+File.separator+"bwb";
			
			String newFileName = "";
	        // WAS(톰캣)의 디스크에 저장될 파일명 
	           
            byte[] bytes = null;
            // 첨부파일의 내용물을 담는 것  
           
            long fileSize = 0;
	        // 첨부파일의 크기 
			
            try {
	            bytes = attach.getBytes();
	            // 첨부파일의 내용물을 읽어오는 것 
	            
	            newFileName = fileManager.doFileUpload(bytes, attach.getOriginalFilename(), path); 
	            // 첨부되어진 파일을 업로드 하도록 하는 것이다. 
	            
	            mvo.setFileName(newFileName);
	           
	            mvo.setOrgFilename(attach.getOriginalFilename());
	            
	            fileSize= attach.getSize();
	            mvo.setFileSize(String.valueOf(fileSize));
	            
            } catch(Exception e) {
	              e.printStackTrace();
            }
		}
		
		int n = service2.registerOne(mvo);
		
		
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("n", n);
			
		return jsonObj.toString();
		
	}// end of public String registerOne
	
	// 마이페이지 맵핑
	@RequestMapping(value="/t1/mypage.tw")
	public ModelAndView requiredLogin_mypage(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		String dcode = loginuser.getFk_dcode();
		String pcode = loginuser.getFk_pcode();
		
		String dname="";
		String duty =""; 
		
		if(dcode==null) {
			dcode="-";
			dname="-";
			duty="-";
		}
		else {
			// 부서명 가져오기
			dname = service2.selectdname(dcode);
			// 직무 가져오기
			duty = service2.selectDuty(dname);
		}
		
		
		// 직위 가져오기
		String pname = service2.selectpname(pcode);
		
		String jubun="";
		
		try {
			
			if(loginuser.getJubun().length()>13) {
				jubun = aes.decrypt(loginuser.getJubun());
			}
			else {
				jubun = loginuser.getJubun();
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
		String fJubun = jubun.substring(0,6);
		
		String passwd = loginuser.getPasswd();
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("dname", dname);
		paraMap.put("duty", duty);
		paraMap.put("pname", pname);
		paraMap.put("fJubun", fJubun);
		paraMap.put("passwd", passwd);
		
		mav.addObject("paraMap", paraMap);
		
		mav.setViewName("bwb/mypage/mypage.gwTiles");
		
		return mav;
	}

	// 비밀번호 변경하기
	@ResponseBody
	@RequestMapping(value="/t1/changePwd.tw", method= {RequestMethod.POST})
	public String changePwd(HttpServletRequest request) {
		
		
		String spasswd = request.getParameter("passwd");
		String employeeid = request.getParameter("employeeid");
				
		String passwd = Sha256.encrypt(spasswd);
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("passwd", passwd);
		
		
		int n = service2.updatePasswd(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	
	// 비밀번호 검사하기
	@ResponseBody
	@RequestMapping(value="/t1/checkpasswd.tw", method= {RequestMethod.POST})
	public String checkpasswd(HttpServletRequest request) {
		
		
		String employeeid = request.getParameter("employeeid");
        String passwd = request.getParameter("lastpasswd");
				
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("employeeid", employeeid);
        paraMap.put("passwd", passwd);
		
     // 직원테이블에서 select해오기
        MemberBwbVO mvo = service3.selectMember(paraMap);
		
        JSONObject jsonObj = new JSONObject();
        
        int n =0;
        
		if(mvo==null) {
			n=1;
		}
		
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	
	// CS부장 로그인시 업무관리 => 나의업무현황 => 미배정업무 보여주기
	@RequestMapping(value="/t1/leaderTodo.tw")
	public ModelAndView requiredLogin_leaderTodo_1(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 세션에 저장된 부장 loginuser 가져오기
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		// 부서장 정보가져오기
		String fk_dcode = loginuser.getFk_dcode();
		String employeeid = loginuser.getEmployeeid();
		
		
		Map<String,String> paraMap2 = new HashMap<>();
		paraMap2.put("fk_dcode", fk_dcode);
		paraMap2.put("employeeid", employeeid);
		
		// 부장의 해당부서팀의 직원들 가져오기
		List<MemberBwbVO> memberList = service4.selectMemberList(paraMap2);
		mav.addObject("memberList", memberList);
		

		// 페이지가 처음 로딩될때 currentshowPageNo가져오기
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		

		int noAssignedProduct = 0;	    // 총 상품갯수
		int sizePerPage = 2;	    // 한페이지당 보여주는 갯수
		int currentShowPageNo = 0;  // 현제페이지 번호
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
		
	    
	    // 미배정 상품 총 갯수 구해오기(noAssignedProduct)
	    noAssignedProduct = service4.selectNoAssignedProduct(employeeid);
	    mav.addObject("noAssignedProduct", noAssignedProduct);
	    
	    // 배정 상품 총 갯수 구해오기(assignedProduct)
	    int assignedProduct = service4.selectAssignedTotal(employeeid);
	    mav.addObject("assignedProduct", assignedProduct);
	    
	    totalPage = (int)Math.ceil(((double)noAssignedProduct/sizePerPage));
	    
	    
	    // currentShowPageNo 장난질 막기
	    if(str_currentShowPageNo == null) { // 맨처음 로딩될때는 null값
	    	currentShowPageNo = 1;
	    }
	    else { // currentShowPageNo가 1페이지 미만의 값, 또는 totalPage 값보다 큰값을 적을때
	    	
	    	try {
	    		currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
		    	if(currentShowPageNo<1 || currentShowPageNo>totalPage) {
		    		currentShowPageNo=1;
		    	}
			} catch (NumberFormatException e) { // null이 아닌 문자열이 들어왔을때
				currentShowPageNo=1;
			}
	    	
	    }// end of else
	    
	    
	    startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
        endRno = startRno + sizePerPage - 1;
        
        Map<String,String> paraMap = new HashMap<>();
        
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
	    paraMap.put("fk_managerid",employeeid);
	    
	    // 미배정 상품정보 가져오기
 		List<ProductBwbVO> productList = service4.selectProudctList(paraMap);
 		
 		int blockSize = 3; // 페이지바의 한블록당 보여주는 갯수  1,2,3   4,5,6 ...
 		int loop = 1;      // 1씩 올라간다.
	    
 		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
 		
 		String pageBar = "<ul style='list-style:none;'>";
 		String url = "leaderTodo.tw";
 		
 		// [맨처음][이전] 만들기
 		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:10pt;'><a href='"+url+"?currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:12pt;'><a href='"+url+"?currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
 		
 		while(!((loop>blockSize)||pageNo>totalPage)) {
			
			if(pageNo == currentShowPageNo) {
                pageBar += "<li style='display:inline-block; width:30px; font-size:10pt; border:solid 0px gray; color:red;'>"+pageNo+"</li>";
            }
			else {
				pageBar+="<li style='display:inline-block; width:30px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while() {
 		
 		
 		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) { // 마지막 페이지에서는 만들 필요가 없기때문에
			
			pageBar += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
			
		}
		
		pageBar += "</ul>";
 		
		
		mav.addObject("productList", productList);
		mav.addObject("pageBar", pageBar);
		mav.setViewName("bwb/todo/leaderTodo_1.gwTiles");
		
		return mav;
		
	}// end of public ModelAndView requiredLogin_leaderTodo_1
	
	// 배정하기 버튼 클릭시 ajax처리
	@ResponseBody
	@RequestMapping(value="/t1/goAssign.tw",method= {RequestMethod.POST})
	public String goAssign(HttpServletRequest request) {
		
		
		String pNo = request.getParameter("pNo");
		String hurryno = request.getParameter("hurryno");
		String employeeid = request.getParameter("employeeid");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("pNo", pNo);
		paraMap.put("hurryno", hurryno);
		paraMap.put("employeeid", employeeid);
		
		// 업무테이블에서 배정자 update해주기
		int n = service4.goAssign(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	} // end of public String goAssign(HttpServletRequest request) {
	
	
	
	
	// CS팀장 로그인시 업무관리 => 나의업무현황 => 배정된업무 보여주기
	@RequestMapping(value="/t1/leaderTodo2.tw")
	public ModelAndView requiredLogin_leaderTodo_2(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 세션에 저장된 부장 loginuser 가져오기
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		// 부서장 정보가져오기
		String fk_dcode = loginuser.getFk_dcode();
		String employeeid = loginuser.getEmployeeid();
		
		
		Map<String,String> paraMap2 = new HashMap<>();
		paraMap2.put("fk_dcode", fk_dcode);
		paraMap2.put("employeeid", employeeid);
		
		// 부장의 해당부서팀의 직원들 가져오기
		List<MemberBwbVO> memberList = service4.selectMemberList(paraMap2);
		mav.addObject("memberList", memberList);
		
		
		// 페이지가 처음 로딩될때 currentshowPageNo가져오기
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		

		int assignedProduct = 0;	    // 총 배정된 상품갯수
		int sizePerPage = 2;	    // 한페이지당 보여주는 갯수
		int currentShowPageNo = 0;  // 현제페이지 번호
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
		
	    
	    // 미배정 상품 총 갯수 구해오기(noAssignedProduct)
	    int noAssignedProduct = service4.selectNoAssignedProduct(employeeid);
	    mav.addObject("noAssignedProduct", noAssignedProduct);
	    
	    // 배정 상품 총 갯수 구해오기(assignedProduct)
	    assignedProduct = service4.selectAssignedTotal(employeeid);
	    mav.addObject("assignedProduct", assignedProduct);
	    
	    totalPage = (int)Math.ceil(((double)assignedProduct/sizePerPage));
	    
	    
	    // currentShowPageNo 장난질 막기
	    if(str_currentShowPageNo == null) { // 맨처음 로딩될때는 null값
	    	currentShowPageNo = 1;
	    }
	    else { // currentShowPageNo가 1페이지 미만의 값, 또는 totalPage 값보다 큰값을 적을때
	    	
	    	try {
	    		currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
		    	if(currentShowPageNo<1 || currentShowPageNo>totalPage) {
		    		currentShowPageNo=1;
		    	}
			} catch (NumberFormatException e) { // null이 아닌 문자열이 들어왔을때
				currentShowPageNo=1;
			}
	    	
	    }// end of else
	    
	    
	    startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
        endRno = startRno + sizePerPage - 1;
        
        Map<String,String> paraMap = new HashMap<>();
        
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
	    paraMap.put("fk_managerid",employeeid);
	    
	    // 미배정 상품정보 가져오기
 		List<ProductBwbVO> productList = service4.selectAssignedList(paraMap);
 		
 		int blockSize = 3; // 페이지바의 한블록당 보여주는 갯수  1,2,3   4,5,6 ...
 		int loop = 1;      // 1씩 올라간다.
	    
 		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
 		
 		String pageBar = "<ul style='list-style:none;'>";
 		String url = "leaderTodo2.tw";
 		
 		// [맨처음][이전] 만들기
 		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:10pt;'><a href='"+url+"?currentShowPageNo=1'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+(pageNo-1)+"'>[이전]</a></li>";
		}
 		
 		while(!((loop>blockSize)||pageNo>totalPage)) {
			
			if(pageNo == currentShowPageNo) {
                pageBar += "<li style='display:inline-block; width:30px; font-size:10pt; border:solid 0px gray; color:red;'>"+pageNo+"</li>";
            }
			else {
				pageBar+="<li style='display:inline-block; width:30px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while() {
 		
 		
 		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) { // 마지막 페이지에서는 만들 필요가 없기때문에
			
			pageBar += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+totalPage+"'>[마지막]</a></li>";
			
		}
		
		pageBar += "</ul>";
 		
		
		mav.addObject("productList", productList);
		mav.addObject("pageBar", pageBar);
		mav.setViewName("bwb/todo/leaderTodo_2.gwTiles");
		
		return mav;
		
	}// end of public ModelAndView requiredLogin_leaderTodo_2
	
	
	
	// 배정하기 변경 버튼 클릭시 ajax처리
	@ResponseBody
	@RequestMapping(value="/t1/changeAssign.tw", method= {RequestMethod.POST})
	public String changeAssign(HttpServletRequest request) {
		
		
		String pNo = request.getParameter("pNo");
		String hurryno = request.getParameter("hurryno");
		String employeeid = request.getParameter("fk_employeeid");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("pNo", pNo);
		paraMap.put("hurryno", hurryno);
		paraMap.put("employeeid", employeeid);
		
		// 업무테이블에서 배정자 update해주기
		int n = service4.goAssign(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	} // end of public String changeAssign(HttpServletRequest request) {
	
	
	//  CS팀의 부서장의 부서업무현황
	@RequestMapping(value="/t1/departmentTodo.tw")
	public ModelAndView requiredLogin_departmentTodo(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		String employeeid = loginuser.getEmployeeid();
		mav.addObject("loginuser", loginuser);
		
		String dcode = loginuser.getFk_dcode();
		// 부서장의 부서명 알아오기
		String dname= service2.selectdname(dcode);
		mav.addObject("dname", dname);
		
		String currentShowPageNo_str = request.getParameter("currentShowPageNo");
		String period = request.getParameter("period"); // 라디오박스 기간
		String statusChoice_es = request.getParameter("statusChoice_es"); // 체크박스 업무상태("1,2,3")
		String searchProject = request.getParameter("searchProject"); // 프로젝트명
		String searchWhoCharge = request.getParameter("searchWhoCharge"); // 담당자명
		
		// 기간에서 선택한 period get방식 처리
		if(period == null || period.equalsIgnoreCase("undefined")) {
			period ="7";
		}
		else if(!(period.equalsIgnoreCase("7")||period.equalsIgnoreCase("30")||period.equalsIgnoreCase("-1")||period.equalsIgnoreCase("90"))) {
			period ="7";
		}
		

		String statusChoice ="";
		
		// 상태값에서 선택한 statusChoice_es get방식 처리, 1:미배정, 2:미시작 3:진행중 4:보류 5:지연 6:완료  
		if(statusChoice_es==null || statusChoice_es.equalsIgnoreCase("")||statusChoice_es.equalsIgnoreCase("null")) {
			statusChoice="";
		}			
		else if(statusChoice_es!=null ) { 
			
			statusChoice="";
			String status1 = "(assignDate is null)"; // 미배정
			String status2 = "(assignDate is not null and T.startdate is null)"; // 미시작
			String status3 = "(to_number(ingdetail) = 0)"; // 진행중
			String status4 = "(to_number(ingdetail) = -1)"; // 보류
			String status5 = "(to_number(ingdetail) > 0)"; // 지연
			String status6 = "(T.endDate is not null)"; // 완료
			
			statusChoice=" and ( ";
			
			String[] statusChoiceArr = statusChoice_es.split(",");
			int len= statusChoiceArr.length;
			 
			for(int i=0; i<len; i++) {
				
				String str = (i<len-1)?" or ":") "; 
					
				if(statusChoiceArr[i].equalsIgnoreCase("1")) statusChoice+=status1+str;
				else if(statusChoiceArr[i].equalsIgnoreCase("2")) statusChoice+=status2+str;
				else if(statusChoiceArr[i].equalsIgnoreCase("3")) statusChoice+=status3+str;
				else if(statusChoiceArr[i].equalsIgnoreCase("4")) statusChoice+=status4+str;
				else if(statusChoiceArr[i].equalsIgnoreCase("5")) statusChoice+=status5+str;
				else if(statusChoiceArr[i].equalsIgnoreCase("6")) statusChoice+=status6+str;

			} // end of for(int i=0; i<len; i++) {-------------------
			
			
		}
		
		
		
		// 프로젝트명을 검색한 searchProject get방식 처리
		if(searchProject==null) {
			searchProject="";
		}
		
		// 담당자명을 검색한 searchWhoCharge get방식 처리
		if(searchWhoCharge==null) {
			searchWhoCharge="";
		}
		
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("employeeid", employeeid);
		paraMap.put("period", period);
		paraMap.put("searchProject", searchProject);
		paraMap.put("searchWhoCharge", searchWhoCharge);
		paraMap.put("statusChoice", statusChoice);
		paraMap.put("statusChoice_es", statusChoice_es);
		
		mav.addObject("paraMap", paraMap);
		
		int totalTodo = 0;	        // 부서 총 업무 갯수
		int sizePerPage = 3;	    // 한페이지당 보여주는 갯수
		int currentShowPageNo = 0;  // 현제페이지 번호
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호  
		
	    
	    // 부서 총 업무 갯수알아오기
  		totalTodo = service4.selectdepartProduct(paraMap);
  		// 총 페이지수
  		totalPage = (int)Math.ceil(((double)totalTodo/sizePerPage));
	    
	    // currentShowPageNo_str(get방식 들어온 잘못된 값 처리) 
		if(currentShowPageNo_str == null) {
			currentShowPageNo = 1;
		}
		else {
			try {
				currentShowPageNo = Integer.parseInt(currentShowPageNo_str);
				if(currentShowPageNo<1 || currentShowPageNo>totalPage) {
		    		currentShowPageNo=1;
		    	}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
			
		}
		
		
		// 시작 행번호, 끝 행번호 정의
		startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
        endRno = startRno + sizePerPage - 1;
		
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
        
        
        int blockSize = 3; // 페이지바의 한블록당 보여주는 갯수  1,2,3   4,5,6 ...
 		int loop = 1;      // 1씩 올라간다.
	    
 		// 페이지 번호 정의
 		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
 		
 		
 		String pageBar = "<ul style='list-style:none;'>";
 		String url = "departmentTodo.tw";
 		
 		// [맨처음][이전] 만들기
 		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:70px; font-size:10pt;'><a href='"+url+"?currentShowPageNo=1&period="+period+"&searchProject="+searchProject+"&searchWhoCharge="+searchWhoCharge+"&statusChoice_es="+statusChoice_es+"'>[맨처음]</a></li>";
			pageBar += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+(pageNo-1)+"&period="+period+"&searchProject="+searchProject+"&searchWhoCharge="+searchWhoCharge+"&statusChoice_es="+statusChoice_es+"'>[이전]</a></li>";
		}
 		
 		while(!((loop>blockSize)||pageNo>totalPage)) {
			
			if(pageNo == currentShowPageNo) {
                pageBar += "<li style='display:inline-block; width:30px; font-size:10pt; border:solid 0px gray; color:red;'>"+pageNo+"</li>";
            }
			else {
				pageBar+="<li style='display:inline-block; width:30px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"&period="+period+"&searchProject="+searchProject+"&searchWhoCharge="+searchWhoCharge+"&statusChoice_es="+statusChoice_es+"'>"+pageNo+"</a></li>";
			}
			
			loop++;
			pageNo++;
		}// end of while() {
 		
 		
 		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) { // 마지막 페이지에서는 만들 필요가 없기때문에
			
			pageBar += "<li style='display:inline-block; width:50px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+pageNo+"&period="+period+"&searchProject="+searchProject+"&searchWhoCharge="+searchWhoCharge+"&statusChoice_es="+statusChoice_es+"'>[다음]</a></li>";
			pageBar += "<li style='display:inline-block; width:70px; font-size:10pt;'><a href='"+url+"?currentShowPageNo="+totalPage+"&period="+period+"&searchProject="+searchProject+"&searchWhoCharge="+searchWhoCharge+"&statusChoice_es="+statusChoice_es+"'>[마지막]</a></li>";
			
		}
		
		pageBar += "</ul>";
 		

		mav.addObject("pageBar", pageBar);

		// 배정된 부서 업무 모두 뽑아오기(기간,검색어 허용)
		List<ProductBwbVO> productList = service4.selectAllDepartmentToDo(paraMap);
		
		mav.addObject("productList", productList);
		mav.setViewName("bwb/todo/departmentTodo.gwTiles");
		
		return mav;
	}
	
	
	// 부서업무중 특정행을 클릭시 업무정보 보이기(ajax처리)
	@ResponseBody
	@RequestMapping(value="/t1/deptgetOneInfoheader.tw",produces="text/plain;charset=UTF-8")
	public String requiredLogin_deptgetOneInfoheader(HttpServletRequest request, HttpServletResponse response) {
		
		String pNo = request.getParameter("pNo");
		
		// === #1. 특정 업무 클릭 시 modal창의 header정보 가져오기
		ProductBwbVO pvo = service4.deptgetOneInfoheader(pNo);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("ingdetail", pvo.getIngdetail());
		jsonObj.put("pname", pvo.getpName());
		jsonObj.put("minino", pvo.getMiniNo());
		jsonObj.put("maxno", pvo.getMaxNo());
		jsonObj.put("nowno", pvo.getMaxNo());
		jsonObj.put("startDate", pvo.getStartDate());
		jsonObj.put("endDate", pvo.getEndDate());
		jsonObj.put("period", pvo.getPeriod());
		
		return jsonObj.toString();
	}
	
	
	// 부서업무 중 특정업무에 대한 고객리스트 페이징처리해서 뽑아오기(ajax처리)
	@ResponseBody
	@RequestMapping(value="/t1/selectClient.tw",produces="text/plain;charset=UTF-8",method= {RequestMethod.POST})
	public String selectClient(HttpServletRequest request) {
		
		String pNo = request.getParameter("pNo");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if(currentShowPageNo==null) {
    		currentShowPageNo = "1";
    	}
		
		int sizePerPage = 3;
		int startRno = (( Integer.parseInt(currentShowPageNo) - 1 ) * sizePerPage) + 1;
        int endRno = startRno + sizePerPage - 1;
        
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("pNo", pNo);
        paraMap.put("startRno", String.valueOf(startRno));
        paraMap.put("endRno", String.valueOf(endRno));
		
		// 부서업무 중 특정업무에 대한 고객리스트 뽑아오기(ajax처리)
		List<ProductBwbVO> clientList = service4.selectClient(paraMap);
		JSONArray jsonArr = new JSONArray();
		
		for(ProductBwbVO pvo:clientList) {
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("clientmobile", pvo.getClientmobile());
			jsonObj.put("clientname", pvo.getClientname());
			jsonObj.put("cnumber", pvo.getCnumber());
			
			jsonArr.put(jsonObj);
			
		} // end of for() ----
		
		return jsonArr.toString();
	}// end of public String selectClient(HttpServletRequest request) {
	
	
	// 페이징바 호출
	@ResponseBody
	@RequestMapping(value="/t1/clientPagebar.tw",produces="text/plain;charset=UTF-8")
	public String clientPagebar(HttpServletRequest request) {
		
		
		String sizePerPage = request.getParameter("sizePerPage");
		String pNo = request.getParameter("pNo");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("pNo", pNo);
		
		// 해당 상품에 대한 고객명단의 총 페이지수 알아오기
		int totalPage = service4.selectTotalClient(paraMap);
        
        JSONObject jsonObj = new JSONObject();
    	jsonObj.put("totalPage", totalPage);
		
		return jsonObj.toString();
		
	}// end of public String clientPagebar(HttpServletRequest request) {
	
	
	// CS팀장 로그인 시 부서 실적현황 주소 맵핑
	@RequestMapping(value="/t1/departmentPerformance.tw")
	public ModelAndView requiredLogin_departmentPerformance(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		// 로그인 한 CS부장의 부서코드 알아오기
		String dcode = loginuser.getFk_dcode();
		
		// 해당부서의 실적의 가장 예전 날짜, 최근 날짜 가지고 오기
		Map<String,String> paraMap = service4.selectOldNewDate(dcode);
		paraMap.put("dcode", dcode);
		
		// 부서명 가지고오기
		String dname = service2.selectdname(dcode);
		paraMap.put("dname", dname);
		
		mav.addObject("paraMap", paraMap);
		
		String selectedMonth = paraMap.get("maxMonth");
		String selectedYear = paraMap.get("maxYear");
		
		// 해당 부서의 직원명,건수,이름 가져오기(부장제외)
		Map<String,String> paraMap2 = new HashMap<>();
		paraMap2.put("selectedMonth", selectedMonth);
		paraMap2.put("selectedYear", selectedYear);
		paraMap2.put("dcode", dcode);
		
		

		
		// CS부장의 자기 팀의 직원별 건수 가지고 오기
		// int teamDoneCount = service4.selectDoneCount("1");
		
		/*
		 select count(*) as cnt,fk_employeeid
		from tbl_todo
		where fk_dcode='2' and enddate is not null
		group by fk_employeeid
		*/
		
		mav.setViewName("bwb/todo/departmentPerformance.gwTiles");
		
		return mav;
		
	}// end of public ModelAndView requiredLogin_departmentPerformance
	
	// 선택한 월부터 -2개월 전까지 뽑아오기(ajax처리)
	@ResponseBody
	@RequestMapping(value="/t1/selectPerformanceMonth.tw",produces="text/plain;charset=UTF-8")
	public String selectPerformanceMonth(HttpServletRequest request) {
		
		String selectedYear = request.getParameter("selectedYear");
		String selectedMonth = request.getParameter("selectedMonth");
		
		
		int iselectedMonth = Integer.parseInt(selectedMonth);
		if(iselectedMonth<10) {
			selectedMonth = "0"+selectedMonth;
		}
		
		// 가장 최근날짜 만들기  2021-02 2021-03 2021-04
		String firstDate = selectedYear+"-"+selectedMonth;
		
		// 선택날짜를 가지고 -1달,-2달 값 가지고 오기
		Map<String,String> paraMap = service4.changeDate(firstDate);
		
		// 기준년월 - 2월 만들어주기
		JSONObject jsonObj = new JSONObject();
		
		jsonObj.put("lastDate", paraMap.get("lastDate"));
		jsonObj.put("middleDate", paraMap.get("middleDate"));
		jsonObj.put("firstDate", firstDate);
		
		return jsonObj.toString();
	};// end of public String selectPerformanceMonth(HttpServletRequest request) {
	
	
	
	// 선택한 년,월에 해당하는 실적 데이터 가지고 오기(ajax처리)
	@ResponseBody
	@RequestMapping(value="/t1/selectPerformance.tw",produces="text/plain;charset=UTF-8")
	public String selectPerformance(HttpServletRequest request) {
		
		String dcode = request.getParameter("dcode");
		String performanceWhenArres = request.getParameter("performanceWhenArres");

		// 넘어온 날짜 배열로 만들어주기.
		String[] performanceWhenArr = performanceWhenArres.split(","); 
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("firstDate", performanceWhenArr[2]);

		// 부장을 제외한 직원 ID,이름을 가져오기
		List<MemberBwbVO> memberList = service2.selectMemberList(dcode);

		// [{"employeeid":tw0019, "2021-04": 0, "2021-03":1, "2021-02":0}, {} ,{}]
		
		JSONArray jsonArr = new JSONArray();
		for(MemberBwbVO mvo : memberList) {
				JSONObject jsonObj = new JSONObject();
				
				String employeeid = mvo.getEmployeeid();
				paraMap.put("employeeid", employeeid);
				
				// chart에 들어가기 위한 name값,3개월에 대한 각각 실적건수
				Map<String,String> resultMap = service4.selectCntPerformance(paraMap);
				jsonObj.put("ppreveCnt", resultMap.get("ppreveCnt"));
				jsonObj.put("prevCnt", resultMap.get("prevCnt"));
				jsonObj.put("selectCnt", resultMap.get("selectCnt"));
				jsonObj.put("name", mvo.getName());
				jsonArr.put(jsonObj);
			
		} // end of for
		
		// System.out.println(jsonArr);
		return jsonArr.toString();
		
	} // end of public String selectPerformance(HttpServletRequest request) {
	
	
	
	// 사장 로그인시 - 부서별실적현황 맵핑
	@RequestMapping(value="/t1/companyPerformance/departmentPerformance.tw")
	public ModelAndView requiredLogin_companyPerformance(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String dcode = "";
		
		// 전체부서의 실적의 가장 예전 날짜, 최근 날짜 가지고 오기
		Map<String,String> paraMap = service4.selectOldNewDate(dcode);
		
		mav.addObject("paraMap", paraMap);
		
		
		
		mav.setViewName("bwb/todo/companyPerformance1.gwTiles");
		
		return mav;
	}
	
	
	// 선택한 년,월에 해당하는 부서 전체 실적 데이터 가지고 오기(ajax처리)
	@ResponseBody
	@RequestMapping(value="/t1/selectDepPerformance.tw",produces="text/plain;charset=UTF-8")
	public String selectDepPerformance(HttpServletRequest request) {
		
		String selectedDate = request.getParameter("selectedDate");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("selectedDate", selectedDate);
		
		
		// 부서 전체 이름,코드 가져오기
		List<MemberBwbVO> departmentList = service2.selectDepartmentList();

		// [{name:'부서1팀',y:비율,sliced:true}, {name:'부서2팀', y:비율} ]
		
		JSONArray jsonArr = new JSONArray();
		for(MemberBwbVO dvo : departmentList) {
			
				JSONObject jsonObj = new JSONObject();
				
				String dcode = dvo.getFk_dcode();
				paraMap.put("dcode", dcode);
				
				// chart에 들어가기 위한 부서 name값,3개월에 대한 부서 각각 실적건수
				Map<String,String> resultMap = service4.selectDepCntPerformance(paraMap);
				jsonObj.put("DCnt", resultMap.get("DCnt"));
				jsonObj.put("totalCnt", resultMap.get("totalCnt"));
				jsonObj.put("percentage", resultMap.get("percentage"));
				jsonObj.put("name", dvo.getDname());
				jsonObj.put("compareValue", resultMap.get("compareValue"));
				jsonArr.put(jsonObj);
				// [{cs1팀,16,26.xx}]
		} // end of for
		
		// System.out.println(jsonArr);
		return jsonArr.toString();
		
	} // end of public String selectPerformance(HttpServletRequest request) {

	
	// 사장 로그인시-회사실적현황
	@RequestMapping(value="/t1/companyPerformance.tw")
	public ModelAndView requiredLogin_companyPerformance2(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String dcode = "";

		// 전체부서의 실적의 가장 예전 날짜, 최근 날짜 가지고 오기
		Map<String,String> paraMap = service4.selectOldNewDate(dcode);
		
		mav.addObject("paraMap", paraMap);
		
		
		mav.setViewName("bwb/todo/companyPerformance2.gwTiles");
		
		return mav;
	} // end of public ModelAndView requiredLogin_companyPerformance2
	

	
	@ResponseBody
	@RequestMapping(value="/t1/selectCoPerformance.tw",produces="text/plain;charset=UTF-8")
	public String selectCoPerformance(HttpServletRequest request) {
		
		String performanceWhenArres = request.getParameter("performanceWhenArres");
		// 넘어온 날짜 배열로 만들어주기.
		String[] performanceWhenArr = performanceWhenArres.split(","); 
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("firstDate", performanceWhenArr[2]);
		
		// 부서 전체 이름,코드 가져오기
		List<MemberBwbVO> departmentList = service2.selectDepartmentList();
		
		JSONArray jsonArr = new JSONArray();
		for(MemberBwbVO dvo : departmentList) {
			String dcode = dvo.getFk_dcode();
			paraMap.put("dcode", dcode);
			JSONObject jsonObj = new JSONObject();
			
			// chart에 들어가기 위한 모든 부서 name값,3개월 각각 건수,합구하기
			Map<String,String> resultMap = service4.selectAllDepCntPerformance(paraMap);
			jsonObj.put("dname", dvo.getDname());
			jsonObj.put("nowCnt", resultMap.get("nowCnt"));
			jsonObj.put("prevCnt", resultMap.get("prevCnt"));
			jsonObj.put("pprevCnt", resultMap.get("pprevCnt"));
			jsonObj.put("totalCnt", resultMap.get("totalCnt"));
			jsonArr.put(jsonObj);
			
		}

		return jsonArr.toString();
	}// end of public String selectCoPerformance(HttpServletRequest request) {
	
	// 해당 월의 부서 3개 평균건수 구해오기(Ajax처리)
	@ResponseBody
	@RequestMapping(value="/t1/selectAvgCnt.tw",produces="text/plain;charset=UTF-8")
	public String selectAvgCnt(HttpServletRequest request) {
	
		String performanceWhenArres = request.getParameter("performanceWhenArres");
		
		String[] performanceWhenArr = performanceWhenArres.split(",");
			
		JSONArray jsonArr = new JSONArray();
		
		for(int i=0; i<performanceWhenArr.length; i++) {
			JSONObject jsonObj = new JSONObject();
			
			String selectedMonth = performanceWhenArr[i];
			
			// 해당 월의 부서 3개 평균건수 구해오기
			String avgCnt = service4.selectAvgCnt(selectedMonth);
			jsonObj.put("avgCnt", avgCnt);
			jsonArr.put(jsonObj);
		}
			
		return jsonArr.toString();
	}// end of public String selectAvgCnt(HttpServletRequest request) {
	
	// 총무부장 - 예약관리
	@RequestMapping(value="/t1/rentalManage.tw")
	public ModelAndView requiredLogin_rentalManage(HttpServletRequest request, HttpServletResponse response,ModelAndView mav) {
		
		// 미승인된 차량예약정보 가져오기
		List<CarGoodsBwbVO> carList =service2.selectCarRental();
		mav.addObject("carList", carList);
		
		// 미승인된 사무용품에약정보 가져오기
		List<CarGoodsBwbVO> goodsList =service2.selectGoodsRental();
		mav.addObject("goodsList", goodsList);
		
		mav.setViewName("bwb/todo/rentalManage.gwTiles");
		
		return mav;
	}// end of public ModelAndView requiredLogin_rentalManage(ModelAndView mav) {
	
	
	// 대여관리에서 차량관리 승인 버튼 클릭시 ajax처리
	@ResponseBody
	@RequestMapping(value="/t1/updateCarRental.tw", method= {RequestMethod.POST})
	public String updateCarRental(HttpServletRequest request) {
		
		
		String rscno = request.getParameter("rscno");
		
		// 승인버튼 클릭시 status update처리(차량)
		int n = service4.updateCarRental(rscno);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}// end of public String updateCarRental(HttpServletRequest request) {
	
	
	// 대여관리에서 사무용품관리 승인 버튼 클릭시 ajax처리
	@ResponseBody
	@RequestMapping(value="/t1/updateGoodsRental.tw", method= {RequestMethod.POST})
	public String updateGoodsRental(HttpServletRequest request) {
		
		
		String rsgno = request.getParameter("rsgno");
		
		// 승인버튼 클릭시 status update처리(사무용품)
		int n = service4.updateGoodsRental(rsgno);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	}// end of public String updateCarRental(HttpServletRequest request) {
	
	
	// CS부서장이 부서업무현황에서 진행완료된 업무에서 메일보내기를 클릭 할시
	@ResponseBody
	@RequestMapping(value="/t1/sendEmailIngDone.tw")
	public String sendEmailIngDone(HttpServletRequest request) {
		
		String clientmobile = request.getParameter("clientmobile");
		String fk_pNo = request.getParameter("fk_pNo");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("clientmobile", clientmobile);
		paraMap.put("fk_pNo", fk_pNo);
		
		ClientHsyVO cvo= service5.getInfoForSendEmailIngTodo(paraMap);
		
		int n=0;
		try {
			mail.sendmail_done(cvo);
			
		} catch(Exception e) { // 메일 전송이 실패한 경우
			e.printStackTrace();
			n=1;
		}
		
		JSONObject jsonObj= new JSONObject();
		jsonObj.put("n", n); 
		
		return jsonObj.toString();
	}// end of public String sendEmailIngDone() {
	
}
