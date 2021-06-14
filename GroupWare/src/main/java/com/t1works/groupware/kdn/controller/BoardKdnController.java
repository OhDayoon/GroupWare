package com.t1works.groupware.kdn.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.t1works.groupware.common.FileManager;
import com.t1works.groupware.common.MyUtil;
import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.kdn.model.BoardKdnVO;
import com.t1works.groupware.kdn.model.CommentKdnVO;
import com.t1works.groupware.kdn.service.InterBoardKdnService;

@Controller
public class BoardKdnController {
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterBoardKdnService service;
	
	@Autowired     // Type에 따라 알아서 Bean 을 주입해준다.
	private FileManager fileManager;
	
	// === 게시판 글쓰기 폼 페이지 요청 하기 ===
	@RequestMapping(value="/t1/noticePostUpload.tw")
	public ModelAndView requiredLogin_noticePostUpload(HttpServletRequest request, HttpServletResponse response, Map<String,String> paraMap, BoardKdnVO boardvo, ModelAndView mav) {
		mav.setViewName("kdn/board/noticepostupload.gwTiles");
		return mav;
	}
	
	// === 게시판 글쓰기 완료 요청 ===
	@RequestMapping(value="/t1/uploadComplete.tw", method= {RequestMethod.POST})
	public ModelAndView noticeUploadComplete(ModelAndView mav, BoardKdnVO boardvo, MultipartHttpServletRequest mrequest) {
		
		// === 첨부파일 있는 경우 작업 시작 ===
		MultipartFile attach = boardvo.getAttach();
		
		if(!attach.isEmpty()) {
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path =root+"resources"+File.separator+"files";
			
			String newFileName = "";// WAS(톰캣)의 디스크에 저장될 파일명 
			byte[] bytes = null; //첨부파일의 내용을 담는 것
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				//첨부파일의 내용물을 읽어오는것
				String originalFilnename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilnename, path);
				
				System.out.println(">>> 확인용 newFileName : "+newFileName);
				
	        // 3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
				
				boardvo.setFileName(newFileName); // WAS(톰캣)에 저장될 파일명
				boardvo.setOrgFilename(originalFilnename); // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	            										   // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				boardvo.setFileSize(String.valueOf(fileSize)); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		// === 첨부파일 있는 경우 작업 끝 ===
		
		int n = 0;

		// 첨부파일이 없는 경우
		if(attach.isEmpty()) {
			n = service.noticePostUpload(boardvo);	// 파일첨부가 없는 글쓰기
		} else { //첨부파일이 있는 경우
			n = service.noticeUploadwithFile(boardvo);
		}
		
		
		if(n==1) {	//글쓰기가 성공한 경우
			mav.setViewName("redirect:/t1/employeeBoard.tw");
		} else {	//글쓰기가 실패한 경우
			mav.setViewName("kdn/error/uploadfail.gwTiles");
		}
		
		return mav;
	} 
	
	// === 공지사항 첨부파일 다운로드 받기 ===
	@RequestMapping(value="/t1/downloadNoticeFile.tw")
	public void requiredLogin_download(HttpServletRequest request, HttpServletResponse response) {
		
		String seq = request.getParameter("seq");
		// 첨부파일이 있는 글번호
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		
		// 첨부파일이 있는 글번호에서 fileName값(ex. 20210604100859609711349587800.png)과 orgFilename(ex. cloth_buckaroo_3.png)을 DB에서 가져와야한다
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = null;
		
		try {
			Integer.parseInt(seq);
			BoardKdnVO boardvo = service.getViewWithNoAddCount(paraMap);
			
			if(boardvo == null || (boardvo != null && boardvo.getFileName() == null)) {
				out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'>alert('존재하지 않는 글번호이거나 첨부파일이 없으므로 파일 다운로드가 불가합니다!!'); history.back();</script>");
				return;
			} else {
				String fileName = boardvo.getFileName();
				// WAS(톰캣) 디스크에 저장된 파일명
				
				String orgFilename = boardvo.getOrgFilename();
				// 다운로드시 보여줄 파일명
				
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				System.out.println(" ~~~ webapp의 절대경로 root => "+root);
				//~~~ webapp의 절대경로 => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\
				
				String path =root+"resources"+File.separator+"files";
				
				//path가 첨부파일이 저장될 톰캣의 폴더가 된다
				System.out.println(" ~~~ webapp의 절대경로 path => "+path);
				// ~~~ webapp의 절대경로 => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files
				
				// **** file 다운로드 하기 **** //
				boolean flag = false; // file 다운로드의 성공,실패를 알려주는 용도 
				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				
				if(!flag) { // 다운로드가 실패할 경우 메시지를 띄워준다.
	               out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성.
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
			
		} catch(IOException e2) {
			
		}
		
	}
	
	
	String boardTypeNo = "";
	
	// === 공지사항게시판 글목록 보기 페이지 요청 ===
	@RequestMapping(value="/t1/employeeBoard.tw")
	public ModelAndView requiredLogin_viewNoticeBoard(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		List<BoardKdnVO> boardList = null;
		
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");
		
		boardTypeNo = "1";
		String fk_categnum = request.getParameter("fk_categnum");
		
		String searchType = request.getParameter("searchType");
		String searchWord = request.getParameter("searchWord");
		String str_currentShowPageNo = request.getParameter("currentShowPageNo");
		String str_sizePerPage = request.getParameter("sizePerPage");
		
		if(fk_categnum == null || (!"1".equals(fk_categnum) && !"2".equals(fk_categnum) && !"3".equals(fk_categnum))) {
			fk_categnum = "";
		}
		
		if(searchType == null || (!"subject".equals(searchType) && !"name".equals(searchType) && !"content".equals(searchType))) {
			searchType = "";
		}
		
		if(searchWord == null || "".equals(searchWord)|| searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		if(str_sizePerPage == null || !("5".equals(str_sizePerPage) || "10".equals(str_sizePerPage) || "15".equals(str_sizePerPage)) ) {
			str_sizePerPage = "10";
		}
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("boardTypeNo", boardTypeNo);
		paraMap.put("fk_categnum", fk_categnum);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("currentShowPageNo", str_currentShowPageNo);
		paraMap.put("sizePerPage", str_sizePerPage);
		
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
		
	    
	    totalCount = service.getNoticeTotalCount(paraMap);
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
	    
	    // 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
		boardList = service.noticeBoardListSearchWithPaging(paraMap);
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		// 페이지바 만들기
		int blockSize = 5;
		int loop = 1;
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "employeeBoard.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
		}
		
		
		while( !(loop > blockSize || pageNo > totalPage)) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:20px; font-size:11pt; border: solid 1px #395673; color: #fff; background-color: #395673; font-weight: bold; padding:2px 2px;'>"+pageNo+"</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:20px; font-size:11pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
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
		
		String gobackURL = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL", gobackURL);
		
		mav.addObject("boardList", boardList);
		mav.setViewName("kdn/board/noticeboard.gwTiles");
		
		return mav;
		
	}
	
	// === 공지사항 글1개를 보여주는 페이지 요청 ===
	@RequestMapping(value="/t1/viewNotice.tw")
	public ModelAndView requiredLogin_viewNotice(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 조회하고자 하는 글번호 받아오기
		String seq = request.getParameter("seq");
		
		// 글목록에서 검색되어진 글내용일 경우 이전글제목, 다음글제목은 검색되어진 결과물내의 이전글과 다음글이 나오도록 하기 위한 것이다.  
	      String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      if(searchWord == null) {
	    	  searchWord = ""; 
	      }
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", searchWord);
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
	      /////////////////////////////////////////////////////////////////////////////
		
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
		}
		mav.addObject("gobackURL", gobackURL);
		
		try {
			Integer.parseInt(seq);
			
			String login_userid = null;
			
			HttpSession session = request.getSession();
			MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
			
			if(loginuser != null) {
				login_userid = loginuser.getEmployeeid();
			}
	
			BoardKdnVO boardvo = null;
			
			if("yes".equals(session.getAttribute("readCountPermission"))) { 
				//글목록보기를 클릭한 다음에 특정글을 조회해온 경우
				
				boardvo = service.getView(paraMap, login_userid);
				// 글조회수 증가와 함께 글1개를 조회를 해주는 것
				
				session.removeAttribute("readCountPermission");
				// 중요함!! session 에 저장된 readCountPermission 을 삭제한다.
			
			} else { // 웹브라우저에서 새로고침(F5)을 클릭한 경우이다.
				
				boardvo = service.getViewWithNoAddCount(paraMap);
				// 글조회수 증가는 없고 단순히 글1개 조회만을 해주는 것이다.
				
			}
			
			mav.addObject("boardvo", boardvo);
			
		
		}catch(NumberFormatException e) {

		}
		
		mav.setViewName("kdn/board/viewNoticePost.gwTiles");
		
		return mav;
	}
	
	@ExceptionHandler(java.lang.Throwable.class)
   public void handleThrowable(Throwable e, HttpServletRequest request, HttpServletResponse response) {
      
      System.out.println("~~~~~ 오류메시지 : " + e.getMessage());
      
      try {
         // *** 웹브라우저에 출력하기 시작 *** //
         
         // HttpServletResponse response 객체는 넘어온 데이터를 조작해서 결과물을 나타내고자 할때 쓰인다. 
         response.setContentType("text/html; charset=UTF-8");
         
         PrintWriter out = response.getWriter();   // out 은 웹브라우저에 기술하는 대상체라고 생각하자.
         
         out.println("<html>");
         out.println("<head><title>오류메시지 출력하기</title></head>");
         out.println("<body>");
         out.println("<h1>오류발생</h1>");
         out.printf("<div><span style='font-weight: bold;'>오류메시지</span><br><span style='color: red;'>%s</span></div>", e.getMessage());
         out.printf("<div style='margin: 20px; color: blue; font-weight: bold; font-size: 26pt;'>%s</div>", "장난금지");
         out.println("<a href='/board/index.action'>홈페이지로 가기</a>");
         out.println("</body>");
         out.println("</html>");
         
         // *** 웹브라우저에 출력하기 끝 *** //
      } catch (IOException e1) {
         e1.printStackTrace();
      }
      
   }
	
	// === 공지사항 글수정 페이지 요청 === //
	@RequestMapping(value="/t1/noticeEdit.tw")
	public ModelAndView requiredLogin_noticeEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 수정해야 할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", "");
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
		// 글 수정해야할 글1개 내용 가져오기
		
		// 글조회수(readCount) 증가 없이 단순히 글1개만 조회 해주는 것이다.
		BoardKdnVO boardvo = service.getViewWithNoAddCount(paraMap);
				
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	      
	    if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeid())) {
	        String message = "다른 사용자의 글은 삭제할 수 없습니다.";
	        String loc = "javascript:history.back()";
	         
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("msg");
	     } else {
	    	// 자신의 글을 수정할 경우
	         // 가져온 1개글을 글수정할 폼이 있는 view 단으로 보내준다.
	    	 mav.addObject("boardvo", boardvo);
	    	 mav.setViewName("kdn/board/noticeEdit.gwTiles");
	     }
		
	    String gobackURL = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL", gobackURL);
	    
		return mav;
	}
	
	// === 공지사항 글수정 페이지 완료하기 === //
	@RequestMapping(value="/t1/noticeEditEnd.tw", method= {RequestMethod.POST})
	public ModelAndView noticeEditEnd(ModelAndView mav, BoardKdnVO boardvo, HttpServletRequest request, MultipartHttpServletRequest mrequest) {
		
		String gobackURL = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL", gobackURL);
		
		//  원본글의 글암호와 수정시 입력해준 암호가 일치할때만 글 수정이 가능하도록 해야한다.
		
		// === 첨부파일 있는 경우 작업 시작 ===
		MultipartFile attach = boardvo.getAttach();
		
		if(!attach.isEmpty()) {
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path =root+"resources"+File.separator+"files";
			
			String newFileName = "";// WAS(톰캣)의 디스크에 저장될 파일명 
			byte[] bytes = null; //첨부파일의 내용을 담는 것
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				//첨부파일의 내용물을 읽어오는것
				String originalFilnename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilnename, path);
				
				System.out.println(">>> 확인용 newFileName : "+newFileName);
				
	        // 3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
				
				boardvo.setFileName(newFileName); // WAS(톰캣)에 저장될 파일명
				boardvo.setOrgFilename(originalFilnename); // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	            										   // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				boardvo.setFileSize(String.valueOf(fileSize)); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		// === 첨부파일 있는 경우 작업 끝 ===
		
		int n = 0;

		// 첨부파일 변경안한 경우
		if(attach.isEmpty()) {
			n = service.noticeEdit(boardvo);	// 파일첨부가 없는 글수정
		} else { //첨부파일이 있는 경우
			n = service.noticeEditNewAttach(boardvo); //새 파일첨부가 있는 글수정
		}

		// n 이 1 이라면 정상적으로 변경됨.
	    // n 이 0 이라면 글수정에 필요한 글암호가 틀린경우 
		if(n == 0) {
	         mav.addObject("message", "암호가 일치하지 않아 글 수정이 불가합니다.");
	      }
	      else {
	         mav.addObject("message", "글수정 성공!!");
	      }
		
		mav.addObject("loc", request.getContextPath()+"/t1/viewNotice.tw?seq="+boardvo.getSeq());
		mav.setViewName("msg");
		
		return mav;
		
	}
	
	// === 공지사항 글삭제 페이지 요청 === //
	@RequestMapping(value="/t1/noticeDel.tw")
	public ModelAndView requiredLogin_noticeDel(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 삭제해야 할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", searchWord);
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
		
		// 삭제해야할 글1개 내용 가져와서 로그인한 사람이 쓴 글이라면 글삭제가 가능하지만 다른 사람이 쓴 글은 삭제가 불가하도록 해야 한다.
		BoardKdnVO boardvo = service.getViewWithNoAddCount(paraMap);
		//글조회수(readCount) 증가 없이 단순히 글1개만 조회 해주는 것이다.
		
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	      
	    if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeid()) ) {
	        String message = "다른 사용자의 글은 삭제가 불가합니다.";
	        String loc = "javascript:history.back()";
	         
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("msg");
	     } else { // 자신의 글을 삭제할 경우
	    	 // 글작성시 입력해준 글암호와 일치하는지 여부를 알아오도록 암호를 입력받아주는 del.jsp 페이지를 띄우도록 한다.
	    	 mav.addObject("seq", seq);
	    	 mav.setViewName("kdn/board/noticeDel.gwTiles");
	     }
		return mav;
	}
	
	// === 공지사항 글삭제 페이지 완료하기 === // 
	@RequestMapping(value="/t1/noticeDelEnd.tw", method= {RequestMethod.POST})
	public ModelAndView noticeDelEnd(ModelAndView mav, HttpServletRequest request) {
		
		//  글 삭제를 하려면 원본글의 글암호와 삭제시 입력해준 암호가 일치할때만 글 삭제가 가능하도록 해야한다.
		
		String seq = request.getParameter("seq");
        String pw = request.getParameter("pw");
      
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("seq", seq);
        paraMap.put("pw", pw);
		
        int n = service.noticeDel(paraMap); 
        // n 이 1 이라면 정상적으로 삭제됨.
        // n 이 0 이라면 글삭제에 필요한 글암호가 틀린경우 
        
        if(n == 0) {
            mav.addObject("message", "암호가 일치하지 않아 글 삭제가 불가합니다.");
            mav.addObject("loc", request.getContextPath()+"/t1/viewNotice.tw?seq="+seq);
        }
        else {
           mav.addObject("message", "글삭제 성공!!");
           mav.addObject("loc", request.getContextPath()+"/t1/employeeBoard.tw");
        }
        
        mav.setViewName("msg");
        
        
		return mav;
		
	}
	
	
	
	
	
	
	
	
	
	// === 건의사항게시판 글목록 보기 페이지 요청 ===
	@RequestMapping(value="/t1/suggestionBoard.tw")
	public ModelAndView requiredLogin_viewSuggestion(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		List<BoardKdnVO> boardList = null;
		
		HttpSession session = request.getSession();
		session.setAttribute("readCountPermission", "yes");
		
	    boardTypeNo = "2";
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
		
		if(str_sizePerPage == null || !("5".equals(str_sizePerPage) || "10".equals(str_sizePerPage) || "15".equals(str_sizePerPage)) ) {
			str_sizePerPage = "10";
		}
		
		Map<String, String> paraMap = new HashMap <>();
		paraMap.put("boardTypeNo", boardTypeNo);
		paraMap.put("searchType", searchType);
		paraMap.put("searchWord", searchWord);
		paraMap.put("currentShowPageNo", str_currentShowPageNo);
		paraMap.put("sizePerPage", str_sizePerPage);
		
		int totalCount = 0; // 총 게시물 건수
		int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
		int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
		int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
		int startRno = 0;           // 시작 행번호
	    int endRno = 0;             // 끝 행번호 
		
	    //System.out.println(boardTypeNo);
	    
	    totalCount = service.getTotalCount(paraMap);
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
	    
	    // 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
		boardList = service.listSearchWithPaging(paraMap);
		
		
		// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
		if(!"".equals(searchType) && !"".equals(searchWord)) {
			mav.addObject("paraMap", paraMap);
		}
		
		// 페이지바 만들기
		int blockSize = 5;
		int loop = 1;
		int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
		
		String pageBar = "<ul style='list-style: none;'>";
		String url = "suggestionBoard.tw";
		
		// === [맨처음][이전] 만들기 ===
		if(pageNo != 1) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
			pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
		}
		
		
		while( !(loop > blockSize || pageNo > totalPage)) {
			
			if(pageNo == currentShowPageNo) {
				pageBar += "<li style='display:inline-block; width:20px; font-size:11pt; border: solid 1px #395673; color: #fff; background-color: #395673; font-weight: bold; padding:2px 2px;'>"+pageNo+"</li>";
			} else {
				pageBar += "<li style='display:inline-block; width:20px; font-size:11pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
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
		
		String gobackURL = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL", gobackURL);
		
		mav.addObject("boardList", boardList);
		mav.setViewName("kdn/board/suggestionboard.gwTiles");
		
		return mav;
		
	}

	// === 건의사항 글쓰기 폼 페이지 요청 하기 ===
	@RequestMapping(value="/t1/suggPostUpload.tw")
	public ModelAndView requiredLogin_suggestionPostUpload(HttpServletRequest request, HttpServletResponse response, Map<String,String> paraMap, BoardKdnVO boardvo, ModelAndView mav) {
		
		// === 답변글쓰기 추가로 인한  ===
		String parentSeq = request.getParameter("parentSeq");
		String groupno = request.getParameter("groupno");
		String depthno = request.getParameter("depthno");
		String subject = request.getParameter("subject");
		String privatePost = request.getParameter("privatePost");
		
		
		mav.addObject("parentSeq", parentSeq);
		mav.addObject("groupno", groupno);
		mav.addObject("depthno", depthno);
		mav.addObject("subject", subject);
		mav.addObject("privatePost", privatePost);
		
		//System.out.println("글쓰기 페이지 비밀글 여부 : "+privatePost);
		
		mav.setViewName("kdn/board/suggpostupload.gwTiles");
		return mav;
	}
	
	// === 건의사항 게시판 글쓰기 완료 요청 ===
	@RequestMapping(value="/t1/suggUploadComplete.tw", method= {RequestMethod.POST})
	public ModelAndView suggUploadComplete(ModelAndView mav, BoardKdnVO boardvo, MultipartHttpServletRequest mrequest) {
		// === 첨부파일 있는 경우 작업 시작 ===
		MultipartFile attach = boardvo.getAttach();
		
		if(!attach.isEmpty()) {
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path =root+"resources"+File.separator+"files";
			
			String newFileName = "";// WAS(톰캣)의 디스크에 저장될 파일명 
			byte[] bytes = null; //첨부파일의 내용을 담는 것
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				//첨부파일의 내용물을 읽어오는것
				String originalFilnename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilnename, path);
				
	        // 3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
				
				boardvo.setFileName(newFileName); // WAS(톰캣)에 저장될 파일명
				boardvo.setOrgFilename(originalFilnename); // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	            										   // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				boardvo.setFileSize(String.valueOf(fileSize)); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		// === 첨부파일 있는 경우 작업 끝 ===
		
		int n = 0;

		// 첨부파일이 없는 경우
		if(attach.isEmpty()) {
			n = service.suggPostUpload(boardvo);	// 파일첨부가 없는 글쓰기
		} else { //첨부파일이 있는 경우
			n = service.suggUploadWithFile(boardvo);
		}
		
		if(n==1) {	//글쓰기가 성공한 경우
			mav.setViewName("redirect:/t1/suggestionBoard.tw");
		} else {	//글쓰기가 실패한 경우
			mav.setViewName("kdn/error/uploadfail.gwTiles");
		}
		
		return mav;
	} 
	
	// === 건의사항 첨부파일 다운로드 받기 ===
	@RequestMapping(value="/t1/downloadSuggFile.tw")
	public void requiredLogin_downloadSuggFile(HttpServletRequest request, HttpServletResponse response) {
		
		String seq = request.getParameter("seq");
		// 첨부파일이 있는 글번호
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = null;
		
		try {
			Integer.parseInt(seq);
			BoardKdnVO boardvo = service.getSuggViewWithNoAddCount(paraMap);
			
			if(boardvo == null || (boardvo != null && boardvo.getFileName() == null)) {
				out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'>alert('존재하지 않는 글번호이거나 첨부파일이 없으므로 파일 다운로드가 불가합니다!!'); history.back();</script>");
				return;
			} else {
				String fileName = boardvo.getFileName();
				String orgFilename = boardvo.getOrgFilename();
				
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				//System.out.println(" ~~~ webapp의 절대경로 root => "+root);
				
				String path =root+"resources"+File.separator+"files";
				
				//path가 첨부파일이 저장될 톰캣의 폴더가 된다
				//System.out.println(" ~~~ webapp의 절대경로 path => "+path);
				// ~~~ webapp의 절대경로 => C:\NCS\workspace(spring)\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Board\resources\files
				
				// **** file 다운로드 하기 **** //
				boolean flag = false; // file 다운로드의 성공,실패를 알려주는 용도 
				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				
				if(!flag) { // 다운로드가 실패할 경우 메시지를 띄워준다.
	               out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성.
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
			
		} catch(IOException e2) {
			
		}
		
	}
	
	
	// === 건의사항 글1개를 보여주는 페이지 요청 ===
	@RequestMapping(value="/t1/viewSuggPost.tw")
	public ModelAndView requiredLogin_viewSuggPost(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String seq = request.getParameter("seq");
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다.
		
		String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      if(searchWord == null) {
	    	  searchWord = "";
	      }
	      
	      // 건의사항 댓글 더보기 페이징을 위한 해당 글 댓글 총 갯수 알아오기
	      int totalCmntCount = service.getSuggCmntTotalCnt(seq);
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", searchWord);
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
	      mav.addObject("totalCmntCount", totalCmntCount);
		
		String gobackURL = request.getParameter("gobackURL");
		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
		}
		mav.addObject("gobackURL", gobackURL);

		try {
			Integer.parseInt(seq);
			
			String login_userid = null;
			
			HttpSession session = request.getSession();
			MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
			
			if(loginuser != null) {
				login_userid = loginuser.getEmployeeid();
			}

	
			BoardKdnVO boardvo = null;
		
			if("yes".equals(session.getAttribute("readCountPermission"))) { 
				//글목록보기를 클릭한 다음에 특정글을 조회해온 경우
				
				boardvo = service.getSuggPostView(paraMap, login_userid);
				// 글조회수 증가와 함께 글1개를 조회를 해주는 것
				
				session.removeAttribute("readCountPermission");
				// 중요함!! session 에 저장된 readCountPermission 을 삭제한다.
			
			} else { // 웹브라우저에서 새로고침(F5)을 클릭한 경우이다.
				
				boardvo = service.getSuggViewWithNoAddCount(paraMap);
				// 글조회수 증가는 없고 단순히 글1개 조회만을 해주는 것이다.
			}
			
			mav.addObject("boardvo", boardvo);
			
			
		}catch(NumberFormatException e) {

		}
		
		mav.setViewName("kdn/board/viewSuggPost.gwTiles");
		
		return mav;

	}
	
	// === 건의사항 비밀글 조회 페이지 요청 === //
	@RequestMapping(value="/t1/viewPrivatePost.tw")
	public ModelAndView requiredLogin_viewPrivatePost(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", searchWord);
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
		
	      String gobackURL = request.getParameter("gobackURL");
			if(gobackURL != null) {
				gobackURL = gobackURL.replaceAll(" ", "&");
			}
			mav.addObject("gobackURL", gobackURL);
		
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	    
	    Integer.parseInt(seq);
		
		String login_userid = null;
		
		if(loginuser != null) {
			login_userid = loginuser.getEmployeeid();
		}

		BoardKdnVO boardvo = null;
	
		if("yes".equals(session.getAttribute("readCountPermission"))) { 
			//글목록보기를 클릭한 다음에 특정글을 조회해온 경우
			
			boardvo = service.getSuggPostView(paraMap, login_userid);
			// 글조회수 증가와 함께 글1개를 조회를 해주는 것
			
			session.removeAttribute("readCountPermission");
			// 중요함!! session 에 저장된 readCountPermission 을 삭제한다.
		
		} else { // 웹브라우저에서 새로고침(F5)을 클릭한 경우이다.
			
			boardvo = service.getSuggViewWithNoAddCount(paraMap);
			// 글조회수 증가는 없고 단순히 글1개 조회만을 해주는 것이다.
		}
		
			mav.addObject("boardvo", boardvo);
	    	mav.setViewName("kdn/board/suggpwconfirm.gwTiles");
		
		return mav;
	}
	    
	
	// === 건의사항 글수정 페이지 요청 === //
	@RequestMapping(value="/t1/suggEdit.tw")
	public ModelAndView requiredLogin_suggEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 수정해야 할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      if(searchWord == null) {
	    	  searchWord = "";
	      }
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", searchWord);
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
		// 글 수정해야할 글1개 내용 가져오기
		
		// 글조회수(readCount) 증가 없이 단순히 글1개만 조회 해주는 것이다.
		BoardKdnVO boardvo = service.getSuggViewWithNoAddCount(paraMap);
				
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	      
	    if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeid())) {
	        String message = "다른 사용자의 글은 삭제할 수 없습니다.";
	        String loc = "javascript:history.back()";
	         
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("msg");
	     } else {
	    	// 자신의 글을 수정할 경우
	         // 가져온 1개글을 글수정할 폼이 있는 view 단으로 보내준다.
	    	 mav.addObject("boardvo", boardvo);
	    	 mav.setViewName("kdn/board/suggEdit.gwTiles");
	     }
		
		return mav;
	}
	
	// === 건의사항 글수정 페이지 완료하기 === //
	@RequestMapping(value="/t1/suggEditEnd.tw", method= {RequestMethod.POST})
	public ModelAndView suggEditEnd(ModelAndView mav, BoardKdnVO boardvo, HttpServletRequest request) {
		
		//  원본글의 글암호와 수정시 입력해준 암호가 일치할때만 글 수정이 가능하도록 해야한다.
		
		int n = service.suggEdit(boardvo);
		// n 이 1 이라면 정상적으로 변경됨.
	    // n 이 0 이라면 글수정에 필요한 글암호가 틀린경우 
		
		if(n == 0) {
	         mav.addObject("message", "암호가 일치하지 않아 글 수정이 불가합니다.");
	      }
	      else {
	         mav.addObject("message", "글수정 성공!!");
	      }
		
		mav.addObject("loc", request.getContextPath()+"/t1/viewSuggPost.tw?seq="+boardvo.getSeq());
		mav.setViewName("msg");
		
		return mav;
		
	}
	
	// === 건의사항 글삭제 페이지 요청 === //
	@RequestMapping(value="/t1/suggDel.tw")
	public ModelAndView requiredLogin_suggDel(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 삭제해야 할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	      String searchWord = request.getParameter("searchWord");
	      
	      Map<String,String> paraMap = new HashMap<>();
	      paraMap.put("seq", seq);
	      paraMap.put("searchType", searchType);
	      paraMap.put("searchWord", searchWord);
	      
	      mav.addObject("searchType", searchType);
	      mav.addObject("searchWord", searchWord);
		
		
		// 삭제해야할 글1개 내용 가져와서 로그인한 사람이 쓴 글이라면 글삭제가 가능하지만 다른 사람이 쓴 글은 삭제가 불가하도록 해야 한다.
		BoardKdnVO boardvo = service.getSuggViewWithNoAddCount(paraMap);
		//글조회수(readCount) 증가 없이 단순히 글1개만 조회 해주는 것이다.
		
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	      
	    if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeid()) ) {
	        String message = "다른 사용자의 글은 삭제가 불가합니다.";
	        String loc = "javascript:history.back()";
	         
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("msg");
	     } else { // 자신의 글을 삭제할 경우
	    	 // 글작성시 입력해준 글암호와 일치하는지 여부를 알아오도록 암호를 입력받아주는 del.jsp 페이지를 띄우도록 한다.
	    	 mav.addObject("seq", seq);
	    	 mav.setViewName("kdn/board/suggDel.gwTiles");
	     }
		return mav;
	}
	
	// === 건의사항 글삭제 페이지 완료하기 === // 
	@RequestMapping(value="/t1/suggDelEnd.tw", method= {RequestMethod.POST})
	public ModelAndView suggDelEnd(ModelAndView mav, HttpServletRequest request) {
		
		//  글 삭제를 하려면 원본글의 글암호와 삭제시 입력해준 암호가 일치할때만 글 삭제가 가능하도록 해야한다.
		
		String seq = request.getParameter("seq");
        String pw = request.getParameter("pw");
      
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("seq", seq);
        paraMap.put("pw", pw);
		
        int n = service.suggDel(paraMap); 
        // n 이 1 이라면 정상적으로 삭제됨.
        // n 이 0 이라면 글삭제에 필요한 글암호가 틀린경우 
        
        if(n == 0) {
            mav.addObject("message", "암호가 일치하지 않아 글 삭제가 불가합니다.");
            mav.addObject("loc", request.getContextPath()+"/t1/viewSuggPost.tw?seq="+seq);
        }
        else {
           mav.addObject("message", "글삭제 성공!!");
           mav.addObject("loc", request.getContextPath()+"/t1/suggestionBoard.tw");
        }
        mav.setViewName("msg");
		return mav;
	}
	
	// === 건의사항 댓글쓰기(ajax 처리) === 
	@ResponseBody
	@RequestMapping(value="/t1/addSuggComment.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String requiredLogin_addSuggComment(HttpServletRequest request, HttpServletResponse response, CommentKdnVO commentvo) {
		try {
			service.addSuggComment(commentvo);
		} catch (Throwable e) {

		}
		// 댓글쓰기(insert) 및 원게시물(tbl_board 테이블)에 댓글의 개수 증가(update 1씩 증가)하기 

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", commentvo.getName());
		
		return jsonObj.toString();
	}
	
	/*
	// === 건의사항 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리) ===
	@ResponseBody
	@RequestMapping(value="/t1/suggCommentList.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String suggCommentList(HttpServletRequest request) {
		
		String fk_seq = request.getParameter("fk_seq");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 5; // 한 페이지당 5개의 댓글을 보여줄 것임.
		
		int startRno = (( Integer.parseInt(currentShowPageNo) - 1 ) * sizePerPage) + 1;
	    int endRno = startRno + sizePerPage - 1; 
		
	    Map<String, String> paraMap = new HashMap<>();
	    paraMap.put("fk_seq", fk_seq);
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
		List<CommentKdnVO> commentList = service.getSuggCmntListPaging(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(commentList != null) {
			for(CommentKdnVO cmtvo : commentList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("content", cmtvo.getContent());
				jsonObj.put("name", cmtvo.getName());
				jsonObj.put("fk_employeeid", cmtvo.getFk_employeeid());
				jsonObj.put("regDate", cmtvo.getRegDate());
				jsonObj.put("seq", cmtvo.getSeq());
				
				jsonArr.put(jsonObj);
			}
		}
		
		//System.out.println(jsonArr.toString());
		
		return jsonArr.toString();
		// "[]" 또는
		// "[{"content":"댓글내용물", "name":"작성자명", "regDate":"작성일자"}]
	}
	*/
	
	// === 건의사항 원게시물에 딸린 댓글들을 더보기 버튼으로 페이징처리하기(Ajax 로 처리) ===
	@ResponseBody
	@RequestMapping(value="/t1/suggCommentList.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String suggCommentList(HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		String fk_seq = request.getParameter("fk_seq");
		String startRno = request.getParameter("start");
		String len = request.getParameter("len");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_seq", fk_seq);
		paraMap.put("startRno",startRno);	
		
		String end = String.valueOf(Integer.parseInt(startRno) + Integer.parseInt(len) - 1);
		paraMap.put("endRno", end);
		
		List<CommentKdnVO> commentList = service.getSuggCmntListPaging(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(commentList != null) {
			for(CommentKdnVO cmtvo : commentList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("content", cmtvo.getContent());
				jsonObj.put("name", cmtvo.getName());
				jsonObj.put("fk_employeeid", cmtvo.getFk_employeeid());
				jsonObj.put("regDate", cmtvo.getRegDate());
				jsonObj.put("seq", cmtvo.getSeq());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
		// "[]" 또는
		// "[{"content":"댓글내용물", "name":"작성자명", "regDate":"작성일자"}]
	}
	
	
	
	
	// === 건의사항 원게시물에 딸린 댓글 totalPage 알아오기(Ajax 로 처리) ===
	@ResponseBody
	@RequestMapping(value="/t1/getSuggCmntTotalPage.tw", method= {RequestMethod.GET})
	public String getSuggCmntTotalPage(HttpServletRequest request) {
		
		String fk_seq = request.getParameter("fk_seq");
		String sizePerPage = request.getParameter("sizePerPage");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fk_seq", fk_seq);
		paraMap.put("sizePerPage", sizePerPage);
		// 원글 글번호(parentSeq)에 해당하는 댓글의 총 페이지수를 알아오기
		int totalPage = service.getSuggCmntTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); // {"totalPage"}
		
		return jsonObj.toString();
		// "{"totalPage":5}"
	}
	
	// 건의사항 댓글 수정하기
	@ResponseBody
	@RequestMapping(value="/t1/editSuggComment.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String requiredLogin_editSuggComment(HttpServletRequest request, HttpServletResponse response, CommentKdnVO commentvo) {
		
		String content = request.getParameter("editComntContent");
		String seq = request.getParameter("editCmntSeq");
		
		commentvo.setContent(content);
		commentvo.setSeq(seq);
		
		int n = 0;
		try {
			n = service.editSuggComment(commentvo);
		} catch (Throwable e) {

		}

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	// 건의사항 댓글 삭제하기
	@ResponseBody
	@RequestMapping(value="/t1/delSuggComment.tw", method= {RequestMethod.GET})
	public String requiredLogin_delSuggComment(HttpServletRequest request, HttpServletResponse response, CommentKdnVO commentvo) {
		
		String fk_seq = request.getParameter("fk_seq");
		commentvo.setFk_seq(fk_seq);
		
		int n = service.delSuggComment(commentvo);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	}
	
	
	
	
	// === 자유게시판 글목록 보기 페이지 요청 ===
	@RequestMapping(value="/t1/generalBoard.tw")
	public ModelAndView requiredLogin_viewGeneral(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
	List<BoardKdnVO> boardList = null;
	
	HttpSession session = request.getSession();
	session.setAttribute("readCountPermission", "yes");
	
    boardTypeNo = "3";
	
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
	
	if(str_sizePerPage == null || !("5".equals(str_sizePerPage) || "10".equals(str_sizePerPage) || "15".equals(str_sizePerPage)) ) {
		str_sizePerPage = "10";
	}
	
	Map<String, String> paraMap = new HashMap <>();
	paraMap.put("boardTypeNo", boardTypeNo);
	paraMap.put("searchType", searchType);
	paraMap.put("searchWord", searchWord);
	paraMap.put("currentShowPageNo", str_currentShowPageNo);
	paraMap.put("sizePerPage", str_sizePerPage);
	
	int totalCount = 0; // 총 게시물 건수
	int sizePerPage = Integer.parseInt(str_sizePerPage); // 한 페이지당 보여줄 게시물 건수
	int currentShowPageNo = 0; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함
	int totalPage = 0;          // 총 페이지수(웹브라우저상에서 보여줄 총 페이지 개수, 페이지바)  
	int startRno = 0;           // 시작 행번호
    int endRno = 0;             // 끝 행번호 
	
    
   // System.out.println(boardTypeNo);
    
    
    totalCount = service.getGenTotalCount(paraMap);
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
    
    // 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	boardList = service.genListSearchWithPaging(paraMap);
	
	// 아래는 검색대상 컬럼과 검색어를 유지시키기 위한 것임.
	if(!"".equals(searchType) && !"".equals(searchWord)) {
		mav.addObject("paraMap2", paraMap);
	}
	
	// 페이지바 만들기
	int blockSize = 5;
	int loop = 1;
	int pageNo = ((currentShowPageNo - 1)/blockSize) * blockSize + 1;
	
	String pageBar = "<ul style='list-style: none;'>";
	String url = "generalBoard.tw";
	
	// === [맨처음][이전] 만들기 ===
	if(pageNo != 1) {
		pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+1+"'><i class=\"fas fa-angle-double-left\" style='font-size: 18px;'></i></a></li>";
		pageBar += "<li style='display:inline-block; width:20px; font-size:12pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+(pageNo-1)+"'><i class=\"fas fa-chevron-left\" style='font-size:15px;'></i></a></li>";
	}
	
	
	while( !(loop > blockSize || pageNo > totalPage)) {
		
		if(pageNo == currentShowPageNo) {
			pageBar += "<li style='display:inline-block; width:20px; font-size:11pt; border: solid 1px #395673; color: #fff; background-color: #395673; font-weight: bold; padding:2px 2px;'>"+pageNo+"</li>";
		} else {
			pageBar += "<li style='display:inline-block; width:20px; font-size:11pt;'><a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord+"&currentShowPageNo="+pageNo+"'>"+pageNo+"</a></li>";
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
	
	String gobackURL = MyUtil.getCurrentURL(request);
	mav.addObject("gobackURL", gobackURL);
	
	mav.addObject("boardList", boardList);
	mav.setViewName("kdn/board/generalboard.gwTiles");
	
	return mav;
	
}

	// === 자유게시판 글쓰기 폼 페이지 요청 하기 ===
	@RequestMapping(value="/t1/genPostUpload.tw")
	public ModelAndView requiredLogin_generalPostUpload(HttpServletRequest request, HttpServletResponse response, Map<String,String> paraMap, BoardKdnVO boardvo, ModelAndView mav) {
		mav.setViewName("kdn/board/genpostupload.gwTiles");
		return mav;
	}
	
	// === 자유게시판 게시판 글쓰기 완료 요청 ===
	@RequestMapping(value="/t1/genUploadComplete.tw", method= {RequestMethod.POST})
	public ModelAndView genUploadComplete(ModelAndView mav, BoardKdnVO boardvo, MultipartHttpServletRequest mrequest) {
		
		MultipartFile attach = boardvo.getAttach();
		
		if(!attach.isEmpty()) {
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			//System.out.println(" ~~~ webapp의 절대경로 => "+root);
			
			String path =root+"resources"+File.separator+"files";
			//path가 첨부파일이 저장될 톰캣의 폴더가 된다
			//System.out.println(" ~~~ webapp의 절대경로 => "+path);
			
			String newFileName = "";// WAS(톰캣)의 디스크에 저장될 파일명 
			byte[] bytes = null; //첨부파일의 내용을 담는 것
			long fileSize = 0;	//첨부파일의 크기
			
			try {
				bytes = attach.getBytes();
				//첨부파일의 내용물을 읽어오는것
				String originalFilnename = attach.getOriginalFilename();
				// originalFilnename ==> "강아지.png"
				
				newFileName = fileManager.doFileUpload(bytes, originalFilnename, path);
				
				//System.out.println(">>> 확인용 newFileName : "+newFileName);
				
				boardvo.setFileName(newFileName); // WAS(톰캣)에 저장될 파일명
				boardvo.setOrgFilename(originalFilnename); // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	            										   // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				boardvo.setFileSize(String.valueOf(fileSize)); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		
		// 첨부파일이 없는 경우
		if(attach.isEmpty()) {
			n = service.genPostUpload(boardvo);	// 파일첨부가 없는 글쓰기
		} else { //첨부파일이 있는 경우
			n = service.genUploadWithFile(boardvo);
		}
		
		if(n==1) {	//글쓰기가 성공한 경우
			mav.setViewName("redirect:/t1/generalBoard.tw");
		} else {	//글쓰기가 실패한 경우
			mav.setViewName("kdn/error/uploadfail.gwTiles");
		}
		
		return mav;
	} 
		
	// === 자유게시판 첨부파일 다운로드 받기 ===
	@RequestMapping(value="/t1/downloadGenFile.tw")
	public void requiredLogin_downloadGenFile(HttpServletRequest request, HttpServletResponse response) {
		
		String seq = request.getParameter("seq");
		// 첨부파일이 있는 글번호
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("seq", seq);
		paraMap.put("searchType", "");
		paraMap.put("searchWord", "");
		
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = null;
		
		try {
			Integer.parseInt(seq);
			BoardKdnVO boardvo = service.getGenViewWithNoAddCount(paraMap);
			
			if(boardvo == null || (boardvo != null && boardvo.getFileName() == null)) {
				out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성
				
				out.println("<script type='text/javascript'>alert('존재하지 않는 글번호이거나 첨부파일이 없으므로 파일 다운로드가 불가합니다!!'); history.back();</script>");
				return;
			} else {
				String fileName = boardvo.getFileName();
				// WAS(톰캣) 디스크에 저장된 파일명
				
				String orgFilename = boardvo.getOrgFilename();
				// 다운로드시 보여줄 파일명
				
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				//System.out.println(" ~~~ webapp의 절대경로 root => "+root);
				
				String path =root+"resources"+File.separator+"files";
				
				//path가 첨부파일이 저장될 톰캣의 폴더가 된다
				//System.out.println(" ~~~ webapp의 절대경로 path => "+path);
				
				// **** file 다운로드 하기 **** //
				boolean flag = false; // file 다운로드의 성공,실패를 알려주는 용도 
				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				
				if(!flag) { // 다운로드가 실패할 경우 메시지를 띄워준다.
	               out = response.getWriter(); // 웹브라우저상에 메시지를 쓰기 위한 객체생성.
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
			
		} catch(IOException e2) {
			
		}
		
	}
	
	
		
	// === 자유게시판 글1개를 보여주는 페이지 요청 ===
	@RequestMapping(value="/t1/viewGenPost.tw")
	public ModelAndView requiredLogin_viewGenPost(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	    String searchWord = request.getParameter("searchWord");
	      
	    if(searchWord == null) {
	    	searchWord = "";
	    }
	    
	    // 자유게시판 댓글 더보기 페이징을 위한 해당 글 댓글 총 갯수 알아오기
	    int totalCmntCount = service.getGenCmntTotalCnt(seq);
	    
	    Map<String,String> paraMap = new HashMap<>();
	    paraMap.put("seq", seq);
	    paraMap.put("searchType", searchType);
	    paraMap.put("searchWord", searchWord);
	      
        mav.addObject("searchType", searchType);
        mav.addObject("searchWord", searchWord);
        mav.addObject("totalCmntCount", totalCmntCount);
	      
		// === 페이징 처리되어진 후 특정 글제목을 클릭하여 상세내용을 본 이후 사용자가 목록보기 버튼을 클릭했을때 돌아갈 페이지를 알려주기 위해 현재 페이지 주소를 뷰단으로 넘겨준다.
		String gobackURL = request.getParameter("gobackURL");
		if(gobackURL != null) {
			gobackURL = gobackURL.replaceAll(" ", "&");
		}
		mav.addObject("gobackURL", gobackURL);
		
		BoardKdnVO boardvo = null;
		
		try {
			Integer.parseInt(seq);
			
			String login_userid = null;
			
			HttpSession session = request.getSession();
			MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
			
			if(loginuser != null) {
				login_userid = loginuser.getEmployeeid();
			}
	
			
			if("yes".equals(session.getAttribute("readCountPermission"))) { 
				//글목록보기를 클릭한 다음에 특정글을 조회해온 경우
				
				
				boardvo = service.getGenPostView(paraMap, login_userid);
				// 글조회수 증가와 함께 글1개를 조회를 해주는 것
				//System.out.println("~~~ 확인용 content : " + boardvo.getContent());
				
				session.removeAttribute("readCountPermission");
				// 중요함!! session 에 저장된 readCountPermission 을 삭제한다.
			
			} else { // 웹브라우저에서 새로고침(F5)을 클릭한 경우이다.
				
				boardvo = service.getGenViewWithNoAddCount(paraMap);
				// 글조회수 증가는 없고 단순히 글1개 조회만을 해주는 것이다.
			}
			
			mav.addObject("boardvo", boardvo);
			
		
		}catch(NumberFormatException e) {

		}
		
		mav.setViewName("kdn/board/viewGenPost.gwTiles");
		
		return mav;
	}


	// === 자유게시판 글수정 페이지 요청 ===
	@RequestMapping(value="/t1/generalEdit.tw")
	public ModelAndView requiredLogin_generalEdit(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 수정해야 할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	    String searchWord = request.getParameter("searchWord");
	      
	    Map<String,String> paraMap = new HashMap<>();
	    paraMap.put("seq", seq);
	    paraMap.put("searchType", searchType);
	    paraMap.put("searchWord", "");
	      
	    mav.addObject("searchType", searchType);
	    mav.addObject("searchWord", searchWord);
		
		// 글 수정해야할 글1개 내용 가져오기
		
		// 글조회수(readCount) 증가 없이 단순히 글1개만 조회 해주는 것이다.
		BoardKdnVO boardvo = service.getGenViewWithNoAddCount(paraMap);
		
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	      
	    if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeid())) {
	        String message = "다른 사용자의 글은 삭제할 수 없습니다.";
	        String loc = "javascript:history.back()";
	         
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("msg");
	     } else {
	    	// 자신의 글을 수정할 경우
	         // 가져온 1개글을 글수정할 폼이 있는 view 단으로 보내준다.
	    	 mav.addObject("boardvo", boardvo);
	    	 mav.setViewName("kdn/board/generalEdit.gwTiles");
	     }
		
		return mav;
	}
	
	// === 자유게시판 글수정 페이지 완료하기 ===
	@RequestMapping(value="/t1/generalEditEnd.tw", method= {RequestMethod.POST})
	public ModelAndView generalEditEnd(ModelAndView mav, BoardKdnVO boardvo, HttpServletRequest request, MultipartHttpServletRequest mrequest) {
		
		//  원본글의 글암호와 수정시 입력해준 암호가 일치할때만 글 수정이 가능하도록 해야한다.
		
		String gobackURL = MyUtil.getCurrentURL(request);
		mav.addObject("gobackURL", gobackURL);
		
		//  원본글의 글암호와 수정시 입력해준 암호가 일치할때만 글 수정이 가능하도록 해야한다.
		
		// === 첨부파일 있는 경우 작업 시작 ===
		MultipartFile attach = boardvo.getAttach();
		
		if(!attach.isEmpty()) {
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path =root+"resources"+File.separator+"files";
			
			String newFileName = "";// WAS(톰캣)의 디스크에 저장될 파일명 
			byte[] bytes = null; //첨부파일의 내용을 담는 것
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				//첨부파일의 내용물을 읽어오는것
				String originalFilnename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilnename, path);
				
				System.out.println(">>> 확인용 newFileName : "+newFileName);
				
	        // 3. BoardVO boardvo 에 fileName 값과 orgFilename 값과 fileSize 값을 넣어주기
				
				boardvo.setFileName(newFileName); // WAS(톰캣)에 저장될 파일명
				boardvo.setOrgFilename(originalFilnename); // 게시판 페이지에서 첨부된 파일(강아지.png)을 보여줄 때 사용.
	            										   // 또한 사용자가 파일을 다운로드 할때 사용되어지는 파일명으로 사용.
				fileSize = attach.getSize(); // 첨부파일의 크기(단위는 byte임)
				boardvo.setFileSize(String.valueOf(fileSize)); 
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		// === 첨부파일 있는 경우 작업 끝 ===
		
		int n = 0;

		// 첨부파일 변경안한 경우
		if(attach.isEmpty()) {
			n = service.generalEdit(boardvo);	// 파일첨부가 없는 글수정
		} else { //첨부파일이 있는 경우
			n = service.generalEditNewAttach(boardvo); //새 파일첨부가 있는 글수정
		}
		
		// n 이 1 이라면 정상적으로 변경됨.
	    // n 이 0 이라면 글수정에 필요한 글암호가 틀린경우 
		
		if(n == 0) {
	         mav.addObject("message", "암호가 일치하지 않아 글 수정이 불가합니다.");
	      }
	      else {
	         mav.addObject("message", "글수정 성공!!");
	      }
		
		mav.addObject("loc", request.getContextPath()+"/t1/viewGenPost.tw?seq="+boardvo.getSeq());
		mav.setViewName("msg");
		
		return mav;
		
	}
	
	
	// === 자유게시판 글삭제 페이지 요청 ===
	@RequestMapping(value="/t1/generalDel.tw")
	public ModelAndView requiredLogin_generalDel(HttpServletRequest request, HttpServletResponse response, ModelAndView mav) {
		
		// 삭제해야 할 글번호 가져오기
		String seq = request.getParameter("seq");
		
		String searchType = request.getParameter("searchType");
	    String searchWord = request.getParameter("searchWord");
	      
	    Map<String,String> paraMap = new HashMap<>();
	    paraMap.put("seq", seq);
	    paraMap.put("searchType", searchType);
	    paraMap.put("searchWord", searchWord);
	      
	    mav.addObject("searchType", searchType);
	    mav.addObject("searchWord", searchWord);
	      
		// 삭제해야할 글1개 내용 가져와서 로그인한 사람이 쓴 글이라면 글삭제가 가능하지만 다른 사람이 쓴 글은 삭제가 불가하도록 해야 한다.
		BoardKdnVO boardvo = service.getGenViewWithNoAddCount(paraMap);
		//글조회수(readCount) 증가 없이 단순히 글1개만 조회 해주는 것이다.
		
		HttpSession session = request.getSession();
	    MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");
	      
	    if( !loginuser.getEmployeeid().equals(boardvo.getFk_employeeid()) ) {
	        String message = "다른 사용자의 글은 삭제가 불가합니다.";
	        String loc = "javascript:history.back()";
	         
	        mav.addObject("message", message);
	        mav.addObject("loc", loc);
	        mav.setViewName("msg");
	     } else { // 자신의 글을 삭제할 경우
	    	 // 글작성시 입력해준 글암호와 일치하는지 여부를 알아오도록 암호를 입력받아주는 del.jsp 페이지를 띄우도록 한다.
	    	 mav.addObject("seq", seq);
	    	 mav.setViewName("kdn/board/generalDel.gwTiles");
	     }
		return mav;
	}
	
	// === 자유게시판 글삭제 페이지 완료하기 ===
	@RequestMapping(value="/t1/generalDelEnd.tw", method= {RequestMethod.POST})
	public ModelAndView generalDelEnd(ModelAndView mav, HttpServletRequest request) {
		
		//  글 삭제를 하려면 원본글의 글암호와 삭제시 입력해준 암호가 일치할때만 글 삭제가 가능하도록 해야한다.
		
		String seq = request.getParameter("seq");
        String pw = request.getParameter("pw");
      
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("seq", seq);
        paraMap.put("pw", pw);
		
        int n = service.generalDel(paraMap); 
        // n 이 1 이라면 정상적으로 삭제됨.
        // n 이 0 이라면 글삭제에 필요한 글암호가 틀린경우 
        
        if(n == 0) {
            mav.addObject("message", "암호가 일치하지 않아 글 삭제가 불가합니다.");
            mav.addObject("loc", request.getContextPath()+"/t1/viewGenPost.tw?seq="+seq);
        }
        else {
           mav.addObject("message", "글삭제 성공!!");
           mav.addObject("loc", request.getContextPath()+"/t1/generalBoard.tw");
        }
        
        mav.setViewName("msg");
        
        
		return mav;
		
	}
	
	// === 댓글쓰기(ajax 처리) === 
	@ResponseBody
	@RequestMapping(value="/t1/addComment.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String requiredLogin_addComment(HttpServletRequest request, HttpServletResponse response, CommentKdnVO commentvo) {
		int n=0;
		try {
			n = service.addComment(commentvo);
			
		} catch (Throwable e) {

		}
		// 댓글쓰기(insert) 및 원게시물(tbl_generalboard 테이블)에 댓글의 개수 증가(update 1씩 증가)하기 

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("name", commentvo.getName());
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	// === 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리) ===
/*	@ResponseBody
	@RequestMapping(value="/t1/commentList.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String commentList(HttpServletRequest request) {
		
		String fk_seq = request.getParameter("fk_seq");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if(currentShowPageNo == null) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 5; // 한 페이지당 5개의 댓글을 보여줄 것임.
		
		int startRno = (( Integer.parseInt(currentShowPageNo) - 1 ) * sizePerPage) + 1;
	    int endRno = startRno + sizePerPage - 1; 
		
	    Map<String, String> paraMap = new HashMap<>();
	    paraMap.put("fk_seq", fk_seq);
	    paraMap.put("startRno", String.valueOf(startRno));
	    paraMap.put("endRno", String.valueOf(endRno));
	    
		List<CommentKdnVO> commentList = service.getCommentListPaging(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(commentList != null) {
			for(CommentKdnVO cmtvo : commentList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("content", cmtvo.getContent());
				jsonObj.put("name", cmtvo.getName());
				jsonObj.put("fk_employeeid", cmtvo.getFk_employeeid());
				jsonObj.put("regDate", cmtvo.getRegDate());
				jsonObj.put("seq", cmtvo.getSeq());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	} */
	
	// === 자유게시판 원게시물에 딸린 댓글들을 더보기 버튼으로 페이징처리하기(Ajax 로 처리) ===
	@ResponseBody
	@RequestMapping(value="/t1/commentList.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String commentList(HttpServletRequest request) {
		
		String seq = request.getParameter("seq");
		String fk_seq = request.getParameter("fk_seq");
		String startRno = request.getParameter("start");
		String len = request.getParameter("len");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fk_seq", fk_seq);
		paraMap.put("startRno",startRno);	
		
		String end = String.valueOf(Integer.parseInt(startRno) + Integer.parseInt(len) - 1);
		paraMap.put("endRno", end);
		
		List<CommentKdnVO> commentList = service.getCommentListPaging(paraMap);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		if(commentList != null) {
			for(CommentKdnVO cmtvo : commentList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("content", cmtvo.getContent());
				jsonObj.put("name", cmtvo.getName());
				jsonObj.put("fk_employeeid", cmtvo.getFk_employeeid());
				jsonObj.put("regDate", cmtvo.getRegDate());
				jsonObj.put("seq", cmtvo.getSeq());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}
	
	
	// === 원게시물에 딸린 댓글 totalPage 알아오기(Ajax 로 처리) ===
	@ResponseBody
	@RequestMapping(value="/t1/getCommentTotalPage.tw", method= {RequestMethod.GET})
	public String getCommentTotalPage(HttpServletRequest request) {
		
		String fk_seq = request.getParameter("fk_seq");
		String sizePerPage = request.getParameter("sizePerPage");
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fk_seq", fk_seq);
		paraMap.put("sizePerPage", sizePerPage);
		// 원글 글번호(parentSeq)에 해당하는 댓글의 총 페이지수를 알아오기
		int totalPage = service.getCommentTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); // {"totalPage"}
		
		return jsonObj.toString();
		// "{"totalPage":5}"0
	}
	
	// 자유게시판 댓글 수정하기
	@ResponseBody
	@RequestMapping(value="/t1/editGenComment.tw", method= {RequestMethod.GET})
	public String editGenComment(HttpServletRequest request, HttpServletResponse response, CommentKdnVO commentvo) {
		
		String content = request.getParameter("editComntContent");
		String seq = request.getParameter("editCmntSeq");
		
		commentvo.setContent(content);
		commentvo.setSeq(seq);
		
		int n = 0;
		try {
			n = service.editGenComment(commentvo);
		} catch (Throwable e) {

		}
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	}
	
	// 자유게시판 댓글 삭제하기
	@ResponseBody
	@RequestMapping(value="/t1/delGenComment.tw", method= {RequestMethod.GET})
	public String requiredLogin_delGenComment(HttpServletRequest request, HttpServletResponse response, CommentKdnVO commentvo) {
		
		String fk_seq = request.getParameter("fk_seq");
		commentvo.setFk_seq(fk_seq);
		
		int n = service.delGenComment(commentvo);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("n", n);
		
		return jsonObj.toString();
		
	}
	
	// 홈 페이지 공지사항 게시판 
	@ResponseBody
	@RequestMapping(value="/t1/recentNotice.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String displayNotice(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String,String> paraMap = new HashMap<>();
		paraMap.put("fk_categnum", "");
		paraMap.put("searchWord", "");
		paraMap.put("startRno", "1");
		paraMap.put("endRno", "4");
		
		List<BoardKdnVO> boardList = service.noticeBoardListSearchWithPaging(paraMap);
		//System.out.println(boardList.size());
		JSONArray jsonArr = new JSONArray();
		
		if(boardList != null) {
			for(BoardKdnVO boardvo : boardList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("fk_categnum", boardvo.getFk_categnum());
				jsonObj.put("subject", boardvo.getSubject());
				jsonObj.put("regDate", boardvo.getRegDate());
				jsonObj.put("seq", boardvo.getSeq());
				
				jsonArr.put(jsonObj);
			}
		}

		return jsonArr.toString();
		
	}
	
	// 홈 페이지 공지사항 게시판 
	@ResponseBody
	@RequestMapping(value="/t1/checkNewNotice.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String checkNewNotice(HttpServletRequest request, HttpServletResponse response) {
		
		int n = service.checkNewNotice();
		
		JSONObject jsonObj = new JSONObject();

		if(n > 0) {
			jsonObj.put("n", "1");
		}
			return jsonObj.toString();
			
		}
		
}
