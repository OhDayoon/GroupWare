package com.t1works.groupware.kdn.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.common.FileManager;
import com.t1works.groupware.common.MyUtil;
import com.t1works.groupware.kdn.model.EmailKdnVO;
import com.t1works.groupware.kdn.service.InterEmailKdnService;

@Controller
public class EmailKdnController {
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterEmailKdnService service;
	
	@Autowired     // Type에 따라 알아서 Bean 을 주입해준다.
	private FileManager fileManager;
	
	//세부메뉴에 총 메일건수 표시하기
	@ResponseBody
	@RequestMapping(value="/t1/displayTotalEmail.tw")
	public String requiredLogin_displayTotalEmail(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("email", loginuser.getEmail());
		paraMap.put("searchWord", "");
		
		int totalEmailCount = service.getTotalCount(paraMap); // 총 메일건수
		int totalUnreadEmail = service.getTotalUnreadEmail(paraMap); // 읽지않은 메일 총 건수
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalEmailCount", totalEmailCount);
		jsonObj.put("totalUnreadEmail", totalUnreadEmail);
		
		return jsonObj.toString();
	}
	
	// === 메일쓰기 폼 페이지 요청 하기 ===
	@RequestMapping(value="/t1/new_mail.tw")
	public ModelAndView requiredLogin_new_Email(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 주소록에서 메일 넘겨받은 경우
		String addrsBookEmail = request.getParameter("addrsBookEmail");
		mav.addObject("addrsBookEmail", addrsBookEmail);
		
		// === 회신메일쓰기용 ===
		String replyEmail = request.getParameter("replyEmail");
		String replyToName = request.getParameter("replyToName");
		String parentSeq = request.getParameter("parentSeq");
		String groupno = request.getParameter("groupno");
		String depthno = request.getParameter("depthno");
		String subject = request.getParameter("subject");
		
		mav.addObject("replyEmail", replyEmail);
		mav.addObject("parentSeq", parentSeq);
		mav.addObject("groupno", groupno);
		mav.addObject("depthno", depthno);
		mav.addObject("subject", subject);
		mav.addObject("replyToName", replyToName);
		
		// 이메일주소 자동완성을 위한 주소록 가져오기
		List<String> emailList = service.getEmailList();
		mav.addObject("emailList", emailList);
		
		mav.setViewName("kdn/mail/new_mail.gwTiles");
		return mav;
	}
	
	// === 메일쓰기 완료 요청 ===
	@RequestMapping(value="/t1/sendSuccess.tw", method= {RequestMethod.POST})
	public ModelAndView sendSuccess(HttpServletRequest request, Map<String,String> paraMap, ModelAndView mav, EmailKdnVO evo, MultipartHttpServletRequest mrequest) {
		
		String ccMail = request.getParameter("ccMail");
		String checkSaveSentMail = request.getParameter("saveSentMail");
		// System.out.println("checkSaveSentMail"+checkSaveSentMail);
		String receiverEmail = request.getParameter("receiverEmail");
		String ccEmail = request.getParameter("ccEmail");

		// System.out.println(receiverEmail);
		evo.setReceiverEmail(receiverEmail);
		// System.out.println("ccEmail: "+ccEmail);
		evo.setCcEmail(ccEmail);
		
		if(ccMail == null) {
			ccMail = "";
		}
		
		MultipartFile attach = evo.getAttach();
		
		if(!attach.isEmpty()) {
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path =root+"resources"+File.separator+"files";
			// ~~~ webapp의 절대경로 => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files
			
			// 2. 파일첨부를 위한 변수의 설정 및 값을 초기화한 후 파일올리기
			
			String newFileName = "";// WAS(톰캣)의 디스크에 저장될 파일명 
			byte[] bytes = null; //첨부파일의 내용을 담는 것
			long fileSize = 0;	//첨부파일의 크기
			
			try {
				bytes = attach.getBytes();
				//첨부파일의 내용물을 읽어오는것
				String originalFilnename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilnename, path);
				
	        // 3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
				
				evo.setFileName(newFileName); // WAS(톰캣)에 저장될 파일명
				evo.setOrgFilename(originalFilnename); // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	            										   // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				evo.setFileSize(String.valueOf(fileSize)); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		
		int n=0;
		
		// 첨부파일이 없는 경우
		if(attach.isEmpty()) {
			n = service.send(evo, checkSaveSentMail); // 파일첨부 없는 메일쓰기
		} else { //첨부파일이 있는 경우
			n = service.sendWithFile(evo, checkSaveSentMail); //파일첨부 있는 메일쓰기
		}
		
		if(n==1) {	//메일쓰기가 성공한 경우
			mav.setViewName("redirect:/t1/mail.tw");
		} else {	//글쓰기가 실패한 경우
			mav.setViewName("error/uploadfail.gwTiles");
		}
		
		return mav;
	}
	
	
	// 받은메일함 페이지 요청 ===
	@RequestMapping(value="/t1/mail.tw")
	public ModelAndView list(ModelAndView mav, HttpServletRequest request) {
		
		List<EmailKdnVO> emailList = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String str_sizePerPage = request.getParameter("sizePerPage");
		
		if(searchType == null || (!"subject".equals(searchType) && !"name".equals(searchType) && !"content".equals(searchType))) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord)|| searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		if(str_sizePerPage == null || !("10".equals(str_sizePerPage) || "15".equals(str_sizePerPage) || "20".equals(str_sizePerPage)) ) {
			str_sizePerPage = "10";
		}
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("email", loginuser.getEmail());
		paraMap.put("currentShowPageNo", str_currentShowPageNo);
		paraMap.put("sizePerPage", str_sizePerPage);
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을때와 없을때로 나뉘어진다.
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
	    
	    // 총 게시물 건수(totalCount)
	    totalCount = service.getTotalCount(paraMap);
	    //System.out.println("~~ 확인용 : "+totalCount);
	    
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
	    
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		} else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
	    endRno = startRno + sizePerPage - 1;
		
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
	    emailList = service.emailListSearchWithPaging(paraMap);
		// 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord) && !"".equals(str_sizePerPage)) {
			mav.addObject("paraMap", paraMap);
		}
		
		// 페이지바 만들기
		int blockSize = 5;
		
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;

		String pageBar = "<ul style='list-style: none;'>";
		String url = "mail.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage)) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px #0071bd; background-color: #0071bd; color:#fff; padding:2px 4px;'>"+pageNo+"</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			
			loop++;
			pageNo++;
		}// end of while ----------
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'><i class=\"fas fa-chevron-right\"></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'><i class=\"fas fa-angle-double-right\"></i></a></li>";
		}
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다. ===
		String gobackURL = MyUtil.getCurrentURL(request);
		//System.out.println("페이지 로딩 후 gobackURL :"+gobackURL);
		mav.addObject("gobackURL", gobackURL);
		
		// == 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝== //
		///////////////////////////////////////////////////////////////
		
		mav.addObject("emailList", emailList);
		mav.setViewName("kdn/mail/mail_inbox.gwTiles");
		return mav;
	}

	// 메일 1개 열람하기
	@RequestMapping(value="/t1/viewMail.tw")
	public ModelAndView view(HttpServletRequest request, ModelAndView mav) {
		
		String mailBoxNo = request.getParameter("mailBoxNo");
		
		// 조회하고자 하는 글번호 받아오기
		String seq = request.getParameter("seq"); // 받은메일함에서 넘어온 경우
		String fk_seq= request.getParameter("fk_seq"); // 보낸메일함에서 넘어온 경우
		
		// System.out.println(seq);
		// System.out.println(fk_seq);

		if(seq==null) {seq="0";}
		if(fk_seq==null) {fk_seq="0";}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		// 글목록에서 검색되어진 글내용일 경우 이전글제목, 다음글제목은 검색되어진 결과물내의 이전글과 다음글이 나오도록 하기 위한 것이다.  
        String searchType = request.getParameter("searchType");
        String searchWord = request.getParameter("searchWord");
      
        if(searchType == null) { searchType = ""; }
        if(searchWord == null) { searchWord = ""; }
      
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("seq", seq);
        paraMap.put("searchType", searchType);
        paraMap.put("searchWord", searchWord);
        paraMap.put("mailBoxNo", mailBoxNo);
        paraMap.put("email", loginuser.getEmail());
      
      
        mav.addObject("mailBoxNo", mailBoxNo);
        mav.addObject("searchType", searchType);
        mav.addObject("searchWord", searchWord);
      /////////////////////////////////////////////////////////////////////////////
		
		// 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
		//System.out.println("확인용 gobackURL : "+gobackURL);
		
		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
		}
		mav.addObject("gobackURL", gobackURL);
		
		// 회신한 이전 메일 상세 가져오기
		
		// String parentSeq = request.getParameter("parentSeq");
		
		// int m = service.getPreviousMail();
		
		try {
			Integer.parseInt(seq);
			Integer.parseInt(fk_seq);
			
			EmailKdnVO evo = null;
			
			if(mailBoxNo.equals("1")) { // 받은메일함
				evo = service.getView(paraMap);
				mav.addObject("evo", evo);
			} else if(mailBoxNo.equals("2")) {	// 보낸메일함
				paraMap.put("seq",fk_seq);
				evo = service.getSentMailView(paraMap);
				mav.addObject("evo", evo);
			} else if(mailBoxNo.equals("3")){	// 중요메일함
				evo = service.getImportantMailView(paraMap);
				mav.addObject("evo", evo);
			} else { // 휴지통
				evo = service.getTrashView(paraMap);
				mav.addObject("evo", evo);
			}
		}catch(NumberFormatException e) {

		}
		
		mav.setViewName("kdn/mail/viewMail.gwTiles");
		
		
		// ============= 받는 사람이 여러명인 경우 고려 => 받는사람 이름과 이메일 주소 메소드 따로 생성 시작 (받는사람이 null일 수는 없다)

		String receiverEmail="";
		if("0".equals(seq)) { // 보낸메일함에서 넘어온 경우
			receiverEmail=service.receiverEmail(fk_seq);
		}
		else if("0".equals(fk_seq)) { // 받은메일함에서 넘어온 경우
			receiverEmail=service.receiverEmail(seq);
		}
		
		List<Map<String,String>> receiverList= new ArrayList<>();
		
		if(receiverEmail.indexOf(",")!=-1) { // 수신자가 여러명인 경우
			
			String[] receiverEmailArr= receiverEmail.split(",");
			for(int i=0;i<receiverEmailArr.length;i++) {
				
				Map<String,String> receiverMap= new HashMap<>(); 
				String receiverName= service.getName(receiverEmailArr[i]);
				
				receiverMap.put("receiverEmail", receiverEmailArr[i]);
				receiverMap.put("receiverName", receiverName);
				
				// 로그인한 유저의 이메일에 색깔처리 해주기 위한 작업
				receiverMap.put("idEmail", receiverEmailArr[i].split("\\@")[0]);
				
				receiverList.add(receiverMap);
			} // end of for-------------------------------
		}
		else { // 수신자가 한명인 경우
			Map<String,String> receiverMap= new HashMap<>(); 
			String receiverName= service.getName(receiverEmail);
			receiverMap.put("receiverEmail",receiverEmail);
			receiverMap.put("receiverName", receiverName);
			
			// 로그인한 유저의 이메일에 색깔처리 해주기 위한 작업
			receiverMap.put("idEmail", receiverEmail.split("\\@")[0]);
			
			receiverList.add(receiverMap);
		}
		
		mav.addObject("receiverList", receiverList);
		
		// ============= 받는 사람이 여러명인 경우 고려 => 받는사람 이름과 이메일 주소 메소드 따로 생성 끝
		
		
		// ============= 참조 사람이 여러명인 경우 고려 => 참조사람 이름과 이메일 주소 메소드 따로 생성 시작 (참조사람이 null일 수 있다)

		String ccEmail="";
		if("0".equals(seq)) { // 보낸메일함에서 넘어온 경우
			ccEmail=service.ccEmail(fk_seq);
		}
		else if("0".equals(fk_seq)) { // 받은메일함에서 넘어온 경우
			ccEmail=service.ccEmail(seq);
		}
		
		List<Map<String,String>> ccList= new ArrayList<>();
		
		if(ccEmail!=null && ccEmail.indexOf(",")!=-1) { // 참조수신자가 여러명인 경우
			
			String[] ccEmailArr= ccEmail.split(",");
			for(int i=0;i<ccEmailArr.length;i++) {
				
				Map<String,String> ccMap= new HashMap<>(); 
				String ccName= service.getName(ccEmailArr[i]);
				
				ccMap.put("ccEmail", ccEmailArr[i]);
				ccMap.put("ccName", ccName);
				
				// 로그인한 유저의 이메일에 색깔처리 해주기 위한 작업
				ccMap.put("idEmail", ccEmailArr[i].split("\\@")[0]);
				
				ccList.add(ccMap);
			}
		}
		else if(ccEmail!=null) { // 참조수신자가 한명인 경우
			Map<String,String> ccMap= new HashMap<>(); 
			String ccName= service.getName(ccEmail);
			ccMap.put("ccEmail",ccEmail);
			ccMap.put("ccName", ccName);
			
			// 로그인한 유저의 이메일에 색깔처리 해주기 위한 작업
			ccMap.put("idEmail", ccEmail.split("\\@")[0]);
			
			ccList.add(ccMap);
		}
		
		mav.addObject("ccList", ccList);  // 참조수신자가 없는 경우 ccList는 empty가 된다
		
		// ============= 참조 사람이 여러명인 경우 고려 => 참조사람 이름과 이메일 주소 메소드 따로 생성 끝

		return mav;
	}
	
	// 보낸메일함 페이지 요청
	@RequestMapping(value="/t1/mail_sent.tw")
	public ModelAndView view_sentMail(ModelAndView mav, HttpServletRequest request) {
		
		List<EmailKdnVO> emailList = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
	    String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String str_sizePerPage = request.getParameter("sizePerPage");
		
		if(searchType == null || (!"subject".equals(searchType) && !"name".equals(searchType) && !"content".equals(searchType))) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord)|| searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		if(str_sizePerPage == null || !("10".equals(str_sizePerPage) || "15".equals(str_sizePerPage) || "20".equals(str_sizePerPage)) ) {
			str_sizePerPage = "10";
		}
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("email", loginuser.getEmail());
		paraMap.put("currentShowPageNo", str_currentShowPageNo);
		paraMap.put("sizePerPage", str_sizePerPage);
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을때와 없을때로 나뉘어진다.
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호
	    
	    // 총 게시물 건수(totalCount)
	    totalCount = service.getMailSentTotalCount(paraMap);
	    //System.out.println("~~ 확인용 : "+totalCount);
	    
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
	    
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		} else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
	    endRno = startRno + sizePerPage - 1;
		
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
	    emailList = service.sentEmailListSearchWithPaging(paraMap);
		// 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		// 페이지바 만들기
		int blockSize = 5;
		
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;

		String pageBar = "<ul style='list-style: none;'>";
		String url = "mail_sent.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage)) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px #0071bd; background-color: #0071bd; color:#fff; padding:2px 4px;'>"+pageNo+"</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			
			loop++;
			pageNo++;
		}// end of while ----------
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'><i class=\"fas fa-chevron-right\"></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'><i class=\"fas fa-angle-double-right\"></i></a></li>";
		}
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다. ===
		String gobackURL = MyUtil.getCurrentURL(request);

		if(gobackURL == null) { gobackURL = ""; }
		mav.addObject("gobackURL", gobackURL);
		
		// == 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝== //
		///////////////////////////////////////////////////////////////

		// ============= 받는 사람이 여러명인 경우 고려 => 받는사람이 여러명인 경우 보낸메일함 리스트에는 1명의 이름과 이메일 주소를 표시해주고 나머지는 외 0명으로 표시한다
		for(EmailKdnVO evo: emailList) {
			
			if(evo.getReceiverEmail().indexOf(",")!=-1) { // 수신자가 여러명인 경우
				String[] receiverEmailArr= evo.getReceiverEmail().split(",");
				String receiverName= service.getName(receiverEmailArr[0]); // 한명의 이름만 필요
				evo.setReceiverEmail(receiverEmailArr[0]); // 한명의 이메일만 필요
				evo.setReceiverName(receiverName); // 한명의 이름만 필요
				evo.setReceiverCnt(String.valueOf(receiverEmailArr.length-1));  // 외 0명에 필요
			}
			else { // 수신자가 한명인 경우
				String receiverName= service.getName(evo.getReceiverEmail());
				evo.setReceiverEmail((evo.getReceiverEmail())); 
				evo.setReceiverName(receiverName); 
			}	
		}
		
		mav.addObject("emailList", emailList);
		
		// ============= 받는 사람이 여러명인 경우 고려 => 받는사람이 여러명인 경우 보낸메일함 리스트에는 1명의 이름과 이메일 주소를 표시해주고 나머지는 외 0명으로 표시한다
		
		mav.setViewName("kdn/mail/mail_sent.gwTiles");
		return mav;
	}
	
	// 중요메일함 페이지 요청
	@RequestMapping(value="/t1/mail_important.tw")
	public ModelAndView view_importantMail(ModelAndView mav, HttpServletRequest request) {
		
		List<EmailKdnVO> emailList = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String str_sizePerPage = request.getParameter("sizePerPage");
		
		if(searchType == null || (!"subject".equals(searchType) && !"name".equals(searchType) && !"content".equals(searchType))) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord)|| searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		if(str_sizePerPage == null || !("10".equals(str_sizePerPage) || "15".equals(str_sizePerPage) || "20".equals(str_sizePerPage)) ) {
			str_sizePerPage = "10";
		}
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("email", loginuser.getEmail());
		paraMap.put("currentShowPageNo", str_currentShowPageNo);
		paraMap.put("sizePerPage", str_sizePerPage);
		paraMap.put("checkImportant", "1");
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을때와 없을때로 나뉘어진다.
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호
		
	    // 총 게시물 건수(totalCount)
	    totalCount = service.getMailImportantTotalCount(paraMap);
	    //System.out.println("~~ 확인용 : "+totalCount);
	    
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
	    
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		} else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
	    endRno = startRno + sizePerPage - 1;
		
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
	    emailList = service.emailListSearchWithPaging(paraMap);
		// 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		// 페이지바 만들기
		int blockSize = 5;
		
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;

		String pageBar = "<ul style='list-style: none;'>";
		String url = "mail_important.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage)) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px #0071bd; background-color: #0071bd; color:#fff; padding:2px 4px;'>"+pageNo+"</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			
			loop++;
			pageNo++;
		}// end of while ----------
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'><i class=\"fas fa-chevron-right\"></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'><i class=\"fas fa-angle-double-right\"></i></a></li>";
		}
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다. ===
		String gobackURL = MyUtil.getCurrentURL(request);
		
		mav.addObject("gobackURL", gobackURL);
		
		// == 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝== //
		///////////////////////////////////////////////////////////////
		
		mav.addObject("emailList", emailList);
		mav.setViewName("kdn/mail/mail_important.gwTiles");
		return mav;
	}
	
	// 휴지통 페이지 요청
	@RequestMapping(value="/t1/mail_trash.tw")
	public ModelAndView view_trash(ModelAndView mav, HttpServletRequest request) {
		
		List<EmailKdnVO> emailList = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String str_sizePerPage = request.getParameter("sizePerPage");
		
		if(searchType == null || (!"subject".equals(searchType) && !"name".equals(searchType) && !"content".equals(searchType))) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord)|| searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		if(str_sizePerPage == null || !("10".equals(str_sizePerPage) || "15".equals(str_sizePerPage) || "20".equals(str_sizePerPage)) ) {
			str_sizePerPage = "10";
		}
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("email", loginuser.getEmail());
		paraMap.put("currentShowPageNo", str_currentShowPageNo);
		paraMap.put("sizePerPage", str_sizePerPage);
		
		// 먼저 총 게시물 건수(totalCount)를 구해와야 한다.
	    // 총 게시물 건수(totalCount)는 검색조건이 있을때와 없을때로 나뉘어진다.
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호
		
	    // 총 게시물 건수(totalCount)
	    totalCount = service.getTrashTotalCount(paraMap);
	    //System.out.println("~~ 확인용 : "+totalCount);
	    
	    totalPage = (int)Math.ceil((double)totalCount/sizePerPage);
	    
		if(str_currentShowPageNo == null) {
			// 게시판에 보여지는 초기화면
			currentShowPageNo = 1;
		} else {
			try {
				currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
				if(currentShowPageNo < 1 || currentShowPageNo > totalPage) {
					currentShowPageNo = 1;
				}
			} catch (NumberFormatException e) {
				currentShowPageNo = 1;
			}
		}
		
		startRno = ((currentShowPageNo - 1 ) * sizePerPage) + 1;
	    endRno = startRno + sizePerPage - 1;
		
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
	    emailList = service.trashListSearchWithPaging(paraMap);
		// 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		// 페이지바 만들기
		int blockSize = 5;
		
		int loop = 1;
		// loop는 1부터 증가하여 1개 블럭을 이루는 페이지번호의 개수[ 지금은 10개(== blockSize) ] 까지만 증가하는 용도이다.
		
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;

		String pageBar = "<ul style='list-style: none;'>";
		String url = "mail_trash.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
		}
		
		while( !(loop > blockSize || pageNo > totalPage)) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt; border:solid 1px #0071bd; background-color: #0071bd; color:#fff; padding:2px 4px;'>"+pageNo+"</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:30px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
			}
			
			
			loop++;
			pageNo++;
		}// end of while ----------
		
		// === [다음][마지막] 만들기 ===
		if(pageNo <= totalPage) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'><i class=\"fas fa-chevron-right\"></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+totalPage+"'><i class=\"fas fa-angle-double-right\"></i></a></li>";
		}
		
		pageBar += "</ul>";
		
		mav.addObject("pageBar", pageBar);
		
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다. ===
		String gobackURL = MyUtil.getCurrentURL(request);
		
		mav.addObject("gobackURL", gobackURL);
		
		// == 페이징 처리를 한 검색어가 있는 전체 글목록 보여주기 끝== //
		///////////////////////////////////////////////////////////////
		mav.addObject("emailList", emailList);
		mav.setViewName("kdn/mail/mail_trash.gwTiles");
		return mav;
	}
	
	
	
	// 첨부파일 다운로드 받기
	@RequestMapping(value="/t1/downloadEmailAttach.tw")
	public void requiredLogin_download(HttpServletRequest request, HttpServletResponse response) {
		
		String mailBoxNo = request.getParameter("mailBoxNo");
		String seq = request.getParameter("seq");
		// 첨부파일이 있는 글번호
		
		//System.out.println("메일번호 확인용 : "+seq);
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		
		// 첨부파일이 있는 글번호에서 fileName값(ex. 20210604100859609711349587800.png)과 orgFilename(ex. cloth_buckaroo_3.png)을 DB에서 가져와야한다
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = null;
		
		try {
			Integer.parseInt(seq);
			EmailKdnVO evo = null;
			if(mailBoxNo.equals("2")) {
				evo = service.getSentMailView(paraMap);
			} else {
				evo = service.getView(paraMap);
			} 
			
			if(evo == null || (evo != null && evo.getFileName() == null)) {
				out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'>alert('존재하지 않는 글번호이거나 첨부파일이 없으므로 파일 다운로드가 불가합니다!!'); history.back();</script>");
				return;
			} else {
				String fileName = evo.getFileName();
				
				String orgFilename = evo.getOrgFilename();
				
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				
				String path =root+"resources"+File.separator+"files";
				
				// **** file 다운로드 하기 **** //
				boolean flag = false; // file 다운로드의 성공,실패를 알려주는 용도 
				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				
				if(!flag) {
					// 다운로드가 실패할 경우 메시지를 띄워준다.
	               out = response.getWriter();
	               // 웹브라우저상에 메시지를 쓰기 위한 객체생성.
	               
	               out.println("<script type='text/javascript'>alert('파일 다운로드가 실패되었습니다!!'); history.back();</script>"); 
				}
				
			}
			
		} catch(NumberFormatException e) {
			try {
				out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'>alert('파일 다운로드가 불가합니다!!'); history.back();</script>");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} catch(IOException e2) { }
		
	}
	
	// 받은메일함&보낸메일함 목록에서 단일 혹은 다중 선택하여 메일 완전삭제하기
	@RequestMapping(value="/t1/delImmed.tw")
	public ModelAndView delEnd(ModelAndView mav, HttpServletRequest request) {
		
		String gobackURL = request.getParameter("gobackURL");
		//System.out.println("넘겨받은 URL : "+gobackURL);

		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
			// 이전글제목, 다음글제목을 클릭했을때 돌아갈 페이지 주소를 올바르게 만들어주기 위한 것임
			//System.out.println("확인용 gobackURL(공백제거) : "+gobackURL);
		}
		mav.addObject("gobackURL", gobackURL);
		
		
		String seq = request.getParameter("seq");
		String str_arrEmailSeq = request.getParameter("str_arrEmailSeq");
		//System.out.println("seq : "+seq);
		//System.out.println("str_arrEmailSeq : "+str_arrEmailSeq);
		
		List<String> emailSeqList = new ArrayList<>();
		
		if(str_arrEmailSeq == null && seq != null) {	// 이메일 열람 페이지에서 이메일 1개 휴지통 이동시키는 경우
			emailSeqList.add(seq);
		} else if(str_arrEmailSeq != null && seq == null) {  // 여러 이메일 휴지통 이동시키는 경우
			String[] arrEmailSeq = str_arrEmailSeq.split(",");
			//System.out.println("받은메일함 체크한 메일번호 배열: "+str_arrEmailSeq);
			emailSeqList = Arrays.asList(arrEmailSeq);
		}
		
		String mailBoxNo = request.getParameter("mailBoxNo");
		
        int n = 0;
        if(mailBoxNo.equals("1")) {
        	n = service.delImmed(emailSeqList); // 받은메일함의 메일 휴지통 이동없이 바로 삭제
        } else {
        	n = service.delSentMail(emailSeqList); // 보낸메일함의 메일 삭제
        	
        }
        // n 이 1 이라면 정상적으로 삭제됨.
        // n 이 0 이라면 글삭제에 필요한 글암호가 틀린경우 
        
        if(n == 0) {
            mav.addObject("message", "에러로 인해 메일을 삭제하지 못했습니다.");
            if(mailBoxNo.equals("1")) {
            	mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
            } else {
            mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
           }
        }            
        else {
           mav.addObject("message", "선택하신 메일이 삭제되었습니다.");
           if(mailBoxNo.equals("1")) {
        	   mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
           } else {
        	   mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
           }
        }
        mav.setViewName("msg");
		return mav;
	}
	
	
	// 중요메일함 중요표시체크해제, 받은메일함 중요표시체크/체크해제
	@RequestMapping(value="/t1/goStar.tw")
	public ModelAndView goStar(ModelAndView mav, HttpServletRequest request) {
		
		String gobackURL = request.getParameter("gobackURL");
		//System.out.println("넘겨받은 URL : "+gobackURL);

		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
			// 이전글제목, 다음글제목을 클릭했을때 돌아갈 페이지 주소를 올바르게 만들어주기 위한 것임
			//System.out.println("확인용 gobackURL(공백제거) : "+gobackURL);
		}
		mav.addObject("gobackURL", gobackURL);
		
		String checkImportant = request.getParameter("checkImportant");
		//System.out.println("중요표시여부 0은 표시해제, 1은 표시: "+checkImportant);
		String mailBoxNo = request.getParameter("mailBoxNo");
		//System.out.println("메일함 번호: "+mailBoxNo);
		String str_arrEmailSeq = request.getParameter("str_arrEmailSeq");
		String[] arrEmailSeq = str_arrEmailSeq.split(",");
		//System.out.println("중요메일함 체크한 메일번호 배열: "+str_arrEmailSeq);
		List<String> emailSeqList = Arrays.asList(arrEmailSeq);

		int n = 0, m = 0;
		if(checkImportant.equals("0")) { 
			n = service.removeStar(emailSeqList); //중요표시해제
			if(n == 0) {
				mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
				if(mailBoxNo.equals("1")) { // mailBoxNo가 1인 경우 받은메일함으로 이동
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL); // 받은메일함으로 이동
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL); // 중요메일함으로 이동
				}
				mav.setViewName("msg");
			} else {        
				//mav.addObject("message", "선택하신 메일의 중요표시가 해제되었습니다.");
				if(mailBoxNo.equals("1")) {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				}
				mav.setViewName("pageReloadOnly");
			}
		} else { 
			m = service.addStar(emailSeqList); // 중요표시하기
			if(m == 0) {
				mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
				if(mailBoxNo.equals("1")) {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				}
				mav.setViewName("msg");
			} else {            
				//mav.addObject("message", "선택하신 메일이 중요표시되었습니다.");
				if(mailBoxNo.equals("1")) {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				}
				mav.setViewName("pageReloadOnly");
			}
		}
        
		return mav;
		
	}
	
	// 메일 휴지통으로 이동시키기
	@RequestMapping(value="/t1/moveToTrash.tw")
	public ModelAndView moveToTrash(ModelAndView mav, HttpServletRequest request) {
		
		String gobackURL = request.getParameter("gobackURL");
		//System.out.println("넘겨받은 URL : "+gobackURL);

		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
			// 이전글제목, 다음글제목을 클릭했을때 돌아갈 페이지 주소를 올바르게 만들어주기 위한 것임
			//System.out.println("확인용 gobackURL(공백제거) : "+gobackURL);
		}
		mav.addObject("gobackURL", gobackURL);
		
		String seq = request.getParameter("seq");
		String str_arrEmailSeq = request.getParameter("str_arrEmailSeq");
		
		List<String> emailSeqList = new ArrayList<>();
		
		if(str_arrEmailSeq == null && seq != null) {	 // 이메일 열람 페이지에서 이메일 1개 휴지통 이동시키는 경우
			emailSeqList.add(seq);
		} else if(str_arrEmailSeq != null && seq == null) { // 여러 이메일 휴지통 이동시키는 경우
			String[] arrEmailSeq = str_arrEmailSeq.split(",");
			//System.out.println("받은메일함 체크한 메일번호 배열: "+str_arrEmailSeq);
			emailSeqList = Arrays.asList(arrEmailSeq);
		}
		
		int n = service.moveToTrash(emailSeqList);
		
		if(n == 0) {
            mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
           	mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
           	mav.setViewName("msg");
        }            
        else {
           //mav.addObject("message", "선택하신 메일을 휴지통으로 이동했습니다.");
    	   mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
    	   mav.setViewName("pageReloadOnly");
        }
		return mav;
	}
	
	// 휴지통 메일을 받은메일함으로 이동시키기
	@RequestMapping(value="/t1/moveToMailInbox.tw")
	public ModelAndView moveToMailInbox(ModelAndView mav, HttpServletRequest request) {
		
		String gobackURL = request.getParameter("gobackURL");
		//System.out.println("넘겨받은 URL : "+gobackURL);

		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
			// 이전글제목, 다음글제목을 클릭했을때 돌아갈 페이지 주소를 올바르게 만들어주기 위한 것임
			//System.out.println("확인용 gobackURL(공백제거) : "+gobackURL);
		}
		mav.addObject("gobackURL", gobackURL);
		
		String str_arrEmailSeq = request.getParameter("str_arrEmailSeq");
		String[] arrEmailSeq = str_arrEmailSeq.split(",");
		//System.out.println("받은메일함 체크한 메일번호 배열: "+str_arrEmailSeq);
		List<String> emailSeqList = Arrays.asList(arrEmailSeq);
		
		int n = service.moveToMailInbox(emailSeqList);
		
		if(n == 0) {
			mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
			mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
			mav.setViewName("msg");
		}            
		else {
			//mav.addObject("message", "선택하신 메일을 휴지통으로 이동했습니다.");
			mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
			mav.setViewName("pageReloadOnly");
		}
		return mav;
	}
	
	// 받은메일함, 중요메일함 읽음상태 변경
	@RequestMapping(value="/t1/readStatus.tw")
	public ModelAndView readStatus(ModelAndView mav, HttpServletRequest request) {
		
		String gobackURL = request.getParameter("gobackURL");
		//System.out.println("넘겨받은 URL : "+gobackURL);

		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
			// 이전글제목, 다음글제목을 클릭했을때 돌아갈 페이지 주소를 올바르게 만들어주기 위한 것임
			//System.out.println("확인용 gobackURL(공백제거) : "+gobackURL);
		}
		mav.addObject("gobackURL", gobackURL);
		
		String readStatus = request.getParameter("readStatus");
		//System.out.println("읽음표시 0은 읽지않음, 1은 읽음: "+readStatus);
		String mailBoxNo = request.getParameter("mailBoxNo");
		//System.out.println("메일함 번호: "+mailBoxNo);
		String str_arrEmailSeq = request.getParameter("str_arrEmailSeq");
		String[] arrEmailSeq = str_arrEmailSeq.split(",");
		//System.out.println("체크한 메일번호 배열: "+str_arrEmailSeq);
		List<String> emailSeqList = Arrays.asList(arrEmailSeq);
		
		int n = 0, m = 0;
		if(readStatus.equals("0")) { 
			if(mailBoxNo.equals("4")) {
				n = service.markAsUnreadSentMail(emailSeqList); // 보낸메일함에서 읽지않음으로 변경
			} else {
				n = service.markAsUnread(emailSeqList); // 받은메일함, 중요메일함에서 읽지않음으로 변경
			}
			
			if(n == 0) {
				mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
				mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				/*
				if(mailBoxNo.equals("1")) { // mailBoxNo가 1인 경우 받은메일함으로 이동
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				}*/
				mav.setViewName("msg");
			} else {        
				//mav.addObject("message", "선택하신 메일을 읽지않음 처리했습니다.");
				mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				/*
				if(mailBoxNo.equals("1")) {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} */
				mav.setViewName("pageReloadOnly");
			}
		} else { 
			if(mailBoxNo.equals("4")) {
				m = service.markAsReadSentMail(emailSeqList); // 보낸메일함 메일 읽음으로 변경
			} else {
				m = service.markAsRead(emailSeqList); // 받은메일함, 중요메일함 메일 읽음으로 변경
			}
			
			if(m == 0) {
				mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
				mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				/*
				if(mailBoxNo.equals("1")) {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} */
				mav.setViewName("msg");
			} else {            
				//mav.addObject("message", "선택하신 메일을 읽음 처리했습니다.");
				mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				/*
				if(mailBoxNo.equals("1")) {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} else {
					mav.addObject("loc", request.getContextPath()+"/"+gobackURL);
				} */
				mav.setViewName("pageReloadOnly");
			}
		}
        
		return mav;
		
	}
	
	// 휴지통 비우기
	@RequestMapping(value="/t1/emptyTrash.tw")
	public ModelAndView emptyTrash(ModelAndView mav, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		String email = loginuser.getEmail();
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		paraMap.put("email", email);
		paraMap.put("moveToTrash", "1");
		
		int m = service.getTrashTotalCount(paraMap);
		
		if(m == 0) { // 휴지통에 메일이 없는 경우
		
			mav.addObject("message", "영구삭제할 메일이 없습니다.");
			mav.addObject("loc", request.getContextPath()+"/t1/mail_trash.tw");

		} else { // 휴지통에 메일이 있는 경우
			
			int n = service.emptyTrash(email);
			
			if(n == 0) {
				mav.addObject("message", "에러발생으로 선택하신 작업을 완료하지 못했습니다.");
				mav.addObject("loc", request.getContextPath()+"/t1/mail_trash.tw");
			}            
			else {
				mav.addObject("message", "휴지통에 보관된 모든 메일이 영구삭제되었습니다.");
				mav.addObject("loc", request.getContextPath()+"/t1/mail_trash.tw");
			}
			
		}
		
        mav.setViewName("msg");
		return mav;
	}
	
}
