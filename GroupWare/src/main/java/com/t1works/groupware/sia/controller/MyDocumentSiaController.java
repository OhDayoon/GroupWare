package com.t1works.groupware.sia.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.t1works.groupware.common.FileManager;
import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.bwb.service.InterHomepageBwbService;
import com.t1works.groupware.sia.model.ApprovalSiaVO;
import com.t1works.groupware.sia.service.InterMyDocumentSiaService;

@Component
@Controller
public class MyDocumentSiaController {
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterMyDocumentSiaService service;	
	
	// 파일업로드 및 다운로드를 해주는 FileManager 클래스 의존객체 주입하기  
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private FileManager fileManager;
	
	@Autowired // Type에 따라 알아서 Bean 을 주입해준다.
	private InterHomepageBwbService service2;
	
	
	// 내문서함 - 수신함 - 일반결재문서 (기본으로 일반결재문서가 보여짐!)
	@RequestMapping(value="/t1/myDocuNorm_rec.tw")
	public String requiredLogin_myDocuNorm_rec(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("ncat", request.getParameter("ncat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuNorm_rec.gwTiles";		
	}
	
	// 내문서함 - 수신함 - 지출결재문서
	@RequestMapping(value="/t1/myDocuSpend_rec.tw")
	public String requiredLogin_myDocuSpend_rec(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("scat", request.getParameter("scat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuSpend_rec.gwTiles";		
	}
	
	// 내문서함 - 수신함 - 근태휴가결재문서
	@RequestMapping(value="/t1/myDocuVacation_rec.tw")
	public String requiredLogin_myDocuVacation_rec(HttpServletRequest request, HttpServletResponse response) {		
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("vno", request.getParameter("vno"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuVacation_rec.gwTiles";		
	}
	
	
	// 내문서함 - 발신함 - 일반결재문서 (기본으로 일반결재문서가 보여짐!)
	@RequestMapping(value="/t1/myDocuNorm_send.tw")
	public String requiredLogin_myDocuNorm_send(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("ncat", request.getParameter("ncat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuNorm_send.gwTiles";		
	}
	
	// 내문서함 - 발신함 - 지출결재문서
	@RequestMapping(value="/t1/myDocuSpend_send.tw")
	public String requiredLogin_myDocuSpend_send(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("scat", request.getParameter("scat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuSpend_send.gwTiles";		
	}
	
	// 내문서함 - 발신함 - 근태휴가결재문서
	@RequestMapping(value="/t1/myDocuVacation_send.tw")
	public String requiredLogin_myDocuVacation_send(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("vno", request.getParameter("vno"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuVacation_send.gwTiles";		
	}
	
	
	// 내문서함 - 임시저장함 - 일반결재문서 (기본으로 일반결재문서가 보여짐!)
	@RequestMapping(value="/t1/myDocuNorm_temp.tw")
	public String requiredLogin_myDocuNorm_temp(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("ncat", request.getParameter("ncat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuNorm_temp.gwTiles";		
	}
	
	// 내문서함 - 임시저장함 - 지출결재문서
	@RequestMapping(value="/t1/myDocuSpend_temp.tw")
	public String requiredLogin_myDocuSpend_temp(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("scat", request.getParameter("scat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuSpend_temp.gwTiles";		
	}
	
	// 내문서함 - 임시저장함 - 근태휴가결재문서
	@RequestMapping(value="/t1/myDocuVacation_temp.tw")
	public String requiredLogin_myDocuVacation_temp(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("vno", request.getParameter("vno"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuVacation_temp.gwTiles";		
	}
	
	
	// 내문서함 - 결재완료문서 - 일반결재문서 (기본으로 일반결재문서가 보여짐!)
	@RequestMapping(value="/t1/myDocuNorm_complete.tw")
	public String requiredLogin_myDocuNorm_complete(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("ncat", request.getParameter("ncat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
				
		return "sia/myDocument/myDocuNorm_complete.gwTiles";		
	}
	
	// 내문서함 - 결재완료문서 - 지출결재문서
	@RequestMapping(value="/t1/myDocuSpend_complete.tw")
	public String requiredLogin_myDocuSpend_complete(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("scat", request.getParameter("scat"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		return "sia/myDocument/myDocuSpend_complete.gwTiles";
	}
	
	// 내문서함 - 결재완료문서 - 근태휴가결재문서
	@RequestMapping(value="/t1/myDocuVacation_complete.tw")
	public String requiredLogin_myDocuVacation_complete(HttpServletRequest request, HttpServletResponse response) {
		
		request.setAttribute("ano", request.getParameter("ano"));
		request.setAttribute("astatus", request.getParameter("astatus"));
		request.setAttribute("fromDate", request.getParameter("fromDate"));
		request.setAttribute("toDate", request.getParameter("toDate"));
		request.setAttribute("vno", request.getParameter("vno"));
		request.setAttribute("sort", request.getParameter("sort"));		
		request.setAttribute("searchWord", request.getParameter("searchWord"));
		request.setAttribute("currentShowPageNo", request.getParameter("currentShowPageNo"));
				
		return "sia/myDocument/myDocuVacation_complete.gwTiles";		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 일반결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/norm_reclist.tw", produces="text/plain;charset=UTF-8")
	public String norm_reclist(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();		
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getnorm_reclist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("ncatname", appvo.getNcatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 수신함 - 일반결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getTotalPage.tw", method= {RequestMethod.GET})
	public String getTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 수신함 - 일반결재 글의 총 페이지수를 알아오기
		int totalPage = service.getTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
		
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 수신함 - 일반결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuNorm_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuNorm_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String ncatname = request.getParameter("ncatname");
				
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("ncatname", request.getParameter("ncatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("ncat", request.getParameter("ncat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("ncatname", ncatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			ApprovalSiaVO avo = service.myDocuNorm_detail(paraMap);			
			
			mav.addObject("avo", avo);
			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuNorm_detail.gwTiles");
		
		return mav;
	}
		
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 지출결재문서에 해당하는 문서 조회하기 (Ajax 처리)
	@ResponseBody
	@RequestMapping(value="/t1/spend_reclist.tw", produces="text/plain;charset=UTF-8")
	public String spend_reclist(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getSpend_reclist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("scatname", appvo.getScatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 수신함 - 지출결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getSpendTotalPage.tw", method= {RequestMethod.GET})
	public String getSpendTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 수신함 - 지출결재 글의 총 페이지수를 알아오기
		int totalPage = service.getSpendTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
				
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 수신함 - 지출 결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuSpend_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuSpend_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String scatname = request.getParameter("scatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("scatname", request.getParameter("scatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("scat", request.getParameter("scat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("scatname", scatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			ApprovalSiaVO avo = service.myDocuSpend_detail(paraMap);			
			mav.addObject("avo", avo);			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuSpend_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////

	
	// 내문서함 - 수신함 - 근태결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/vacation_reclist.tw", produces="text/plain;charset=UTF-8")
	public String vacation_reclist(HttpServletRequest request) {
					
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");		
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getVacation_reclist(paraMap);		
		
		JSONArray jsonArr = new JSONArray();		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("vcatname", appvo.getVcatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 수신함 - 근태결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getVacationTotalPage.tw", method= {RequestMethod.GET})
	public String getVacationTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 수신함 - 근태결재 글의 총 페이지수를 알아오기
		int totalPage = service.getVacationTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);		
		
		return jsonObj.toString();
	}
	
	
	// 내문서함 - 수신함 - 근태결재문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuVacation_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuVacation_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String vcatname = request.getParameter("vcatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("vcatname", request.getParameter("vcatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("vno", request.getParameter("vno"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("vcatname", vcatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			// 근태결재문서 한 개 상세보기
			ApprovalSiaVO avo = service.myDocuVacation_detail(paraMap);						
			
			mav.addObject("avo", avo);		
			
		} catch (NumberFormatException e) {
			
		}
		
		
		mav.setViewName("sia/myDocumentDetail/myDocuVacation_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 결재의견 작성하기
	@ResponseBody
	@RequestMapping(value="/t1/addOpinion.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String addOpinion(ApprovalSiaVO avo) { // 그대로 보여주는 것이기 때문에 return 타입은 String
		
		int n = service.addOpinion(avo);
		
		JSONObject jsonObj = new JSONObject();		
		jsonObj.put("n", n);
		jsonObj.put("employeeid", avo.getEmployeeid());
				
		return jsonObj.toString();
	}
	
	
	// 내문서함 - 수신함 - 결재의견 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/opinionList.tw", method= {RequestMethod.GET}, produces="text/plain;charset=UTF-8")
	public String readOpinion(HttpServletRequest request) {
		
		String parentAno = request.getParameter("parentAno");
		
		List<ApprovalSiaVO> opinionList = service.getOpinionList(parentAno);
		
		JSONArray jsonArr = new JSONArray(); // []
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		if(opinionList != null) {
			for(ApprovalSiaVO avo : opinionList) {
				JSONObject jsonObj = new JSONObject();				
				jsonObj.put("dname", avo.getDname());
				jsonObj.put("name", avo.getName());
				jsonObj.put("pname", avo.getPname());
				jsonObj.put("odate", avo.getOdate());
				jsonObj.put("ocontent", avo.getOcontent());
				jsonObj.put("employeeid", avo.getEmployeeid());
				jsonObj.put("userid", userid);
				jsonObj.put("ono", avo.getOno());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();		
	}
	
	
	// 내문서함 - 수신함 - 결재버튼 클릭
	@ResponseBody
	@RequestMapping(value="/t1/approval.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String approval(HttpServletRequest request) { 
		
		String ano = request.getParameter("ano");
		String astatus = request.getParameter("astatus");
		String arecipient1 = request.getParameter("arecipient1");
		String arecipient2 = request.getParameter("arecipient2");
		String arecipient3 = request.getParameter("arecipient3");	
		String employeeid = request.getParameter("employeeid");
		String fk_pcode = request.getParameter("fk_pcode");
				
		if(arecipient2 == null) {
			arecipient2 = "";
		}
		if(arecipient3 == null) {
			arecipient3 = "";
		}
		
		
		Map<String, String> paraMap = new HashMap<>(); 
		paraMap.put("ano", ano);  
		paraMap.put("astatus", astatus);
		paraMap.put("arecipient1", arecipient1);
		paraMap.put("arecipient2", arecipient2);
		paraMap.put("arecipient3", arecipient3);
		paraMap.put("employeeid", employeeid);
		paraMap.put("fk_pcode", fk_pcode); 
	 
		int n = service.approval(paraMap); 
		
		
		JSONObject jsonObj = new JSONObject();		
		jsonObj.put("n", n);
		
		return jsonObj.toString();
	}
	
	
	// 내문서함 - 수신함 - 반려버튼 클릭
	@ResponseBody
	@RequestMapping(value="/t1/reject.tw", method= {RequestMethod.POST}, produces="text/plain;charset=UTF-8")
	public String reject(HttpServletRequest request, ApprovalSiaVO avo) { 
		
		String ano = request.getParameter("ano");
		String arecipient1 = request.getParameter("arecipient1");
		String arecipient2 = request.getParameter("arecipient2");
		String arecipient3 = request.getParameter("arecipient3");
		String employeeid = request.getParameter("employeeid");
		String vcatname = request.getParameter("vcatname");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);  
		paraMap.put("arecipient1", arecipient1);
		paraMap.put("arecipient2", arecipient2);
		paraMap.put("arecipient3", arecipient3);
		paraMap.put("employeeid", employeeid);
		paraMap.put("vcatname", vcatname);
		
		int n = service.reject(paraMap); 	
		
		JSONObject jsonObj = new JSONObject();		
		jsonObj.put("n", n);
				
		return jsonObj.toString();
	}
	
	
	// 결재로그 리스트보기
	@ResponseBody
	@RequestMapping(value="/t1/approvalLogList.tw", produces="text/plain;charset=UTF-8")
	public String approvalLogList(HttpServletRequest request) {
		
		String parentAno = request.getParameter("parentAno");
		
		List<ApprovalSiaVO> approvalLogList = service.approvalLogList(parentAno);
		
		JSONArray jsonArr = new JSONArray();
		
		if(approvalLogList != null) {
			for(ApprovalSiaVO avo : approvalLogList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("dname", avo.getDname());
				jsonObj.put("name", avo.getName());
				jsonObj.put("pname", avo.getPname());
				jsonObj.put("logstatus", avo.getLogstatus());
				jsonObj.put("logdate", avo.getLogdate());
				jsonObj.put("pcode", avo.getPcode());
				
				jsonArr.put(jsonObj);
			}
		}
		
		return jsonArr.toString();
	}	
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////

	
	// 내문서함 - 발신함 - 일반결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/norm_sendlist.tw", produces="text/plain;charset=UTF-8")
	public String norm_sendlist(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getnorm_sendlist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("ncatname", appvo.getNcatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 발신함 - 일반결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getNormSendTotalPage.tw", method= {RequestMethod.GET})
	public String getNormSendTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
				
		Map<String, String> paraMap = new HashMap<>();		  
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 발신함 - 일반결재 글의 총 페이지수를 알아오기
		int totalPage = service.getNormSendTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
		
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 발신함 - 일반결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuNorm_send_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuNorm_send_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String ncatname = request.getParameter("ncatname");
				
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("ncatname", request.getParameter("ncatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("ncat", request.getParameter("ncat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("ncatname", ncatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			ApprovalSiaVO avo = service.myDocuNorm_send_detail(paraMap);			
			
			mav.addObject("avo", avo);
			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuNorm_send_detail.gwTiles");
		
		return mav;
	}
		
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 발신함 - 지출결재문서에 해당하는 문서 조회하기 (Ajax 처리)
	@ResponseBody
	@RequestMapping(value="/t1/spend_sendlist.tw", produces="text/plain;charset=UTF-8")
	public String spend_sendlist(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getSpend_sendlist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("scatname", appvo.getScatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 발신함 - 지출결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getSpendSendTotalPage.tw", method= {RequestMethod.GET})
	public String getSpendSendTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 발신함 - 지출결재 글의 총 페이지수를 알아오기
		int totalPage = service.getSpendSendTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
				
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 발신함 - 지출 결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuSpend_send_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuSpend_send_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String scatname = request.getParameter("scatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("scatname", request.getParameter("scatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("scat", request.getParameter("scat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("scatname", scatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			ApprovalSiaVO avo = service.myDocuSpend_detail(paraMap);			
			mav.addObject("avo", avo);			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuSpend_send_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////

	
	// 내문서함 - 발신함 - 근태결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/vacation_sendlist.tw", produces="text/plain;charset=UTF-8")
	public String vacation_sendlist(HttpServletRequest request) {
					
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");		
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		 
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);		
		
		List<ApprovalSiaVO> approvalvo = service.getVacation_sendlist(paraMap);		
		
		JSONArray jsonArr = new JSONArray();		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("vcatname", appvo.getVcatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 발신함 - 근태결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getVacationSendTotalPage.tw", method= {RequestMethod.GET})
	public String getVacationSendTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 발신함 - 근태결재 글의 총 페이지수를 알아오기
		int totalPage = service.getVacationSendTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);		
		
		return jsonObj.toString();
	}
	
	
	// 내문서함 - 발신함 - 근태결재문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuVacation_send_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuVacation_send_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String vcatname = request.getParameter("vcatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("vcatname", request.getParameter("vcatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("vno", request.getParameter("vno"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("vcatname", vcatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			// 근태결재문서 한 개 상세보기
			ApprovalSiaVO avo = service.myDocuVacation_send_detail(paraMap);						
			
			mav.addObject("avo", avo);		
			
		} catch (NumberFormatException e) {
			
		}
		
		
		mav.setViewName("sia/myDocumentDetail/myDocuVacation_send_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////	
	
	// 내문서함 - 임시저장함 - 일반결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/norm_templist.tw", produces="text/plain;charset=UTF-8")
	public String norm_templist(HttpServletRequest request) {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
		
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();		
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getnorm_templist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("ncatname", appvo.getNcatname());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 임시저장함 - 일반결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getNormTempTotalPage.tw", method= {RequestMethod.GET})
	public String getNormTempTotalPage(HttpServletRequest request) {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 임시저장함 - 일반결재 글의 총 페이지수를 알아오기
		int totalPage = service.getNormTempTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
		
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 임시저장함 - 일반결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuNorm_temp_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuNorm_temp_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String ncatname = request.getParameter("ncatname");
				
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("ncatname", request.getParameter("ncatname"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("ncat", request.getParameter("ncat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String userid = null;		
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			userid = loginuser.getEmployeeid();			
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("ncatname", ncatname);
		paraMap.put("userid", userid);		
		
		try {
			ApprovalSiaVO avo = service.myDocuNorm_temp_detail(paraMap);
			ApprovalSiaVO mng = service.viewMng(paraMap);
			
			mav.addObject("avo", avo);
			mav.addObject("mng", mng);
			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuNorm_temp_detail.gwTiles");
		
		return mav;
	}
		
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 지출결재문서에 해당하는 문서 조회하기 (Ajax 처리)
	@ResponseBody
	@RequestMapping(value="/t1/spend_templist.tw", produces="text/plain;charset=UTF-8")
	public String spend_templist(HttpServletRequest request) {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getSpend_templist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("scatname", appvo.getScatname());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 임시저장함 - 지출결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getSpendTempTotalPage.tw", method= {RequestMethod.GET})
	public String getSpendTempTotalPage(HttpServletRequest request) {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 임시저장함 - 지출결재 글의 총 페이지수를 알아오기
		int totalPage = service.getSpendTempTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
				
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 임시저장함 - 지출 결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuSpend_temp_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuSpend_temp_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String scatname = request.getParameter("scatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("scatname", request.getParameter("scatname"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("scat", request.getParameter("scat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String userid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			userid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("scatname", scatname);
		paraMap.put("userid", userid);
		
		try {
			ApprovalSiaVO avo = service.myDocuSpend_temp_detail(paraMap);
			ApprovalSiaVO mng = service.viewMng(paraMap);
			
			mav.addObject("avo", avo);
			mav.addObject("mng", mng);
						
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuSpend_temp_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////

	
	// 내문서함 - 임시저장함 - 근태결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/vacation_templist.tw", produces="text/plain;charset=UTF-8")
	public String vacation_templist(HttpServletRequest request) {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");		
		
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getVacation_templist(paraMap);		
		
		JSONArray jsonArr = new JSONArray();		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("vcatname", appvo.getVcatname());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				jsonObj.put("fileName", appvo.getFileName());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 임시저장함 - 근태결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getVacationTempTotalPage.tw", method= {RequestMethod.GET})
	public String getVacationTempTotalPage(HttpServletRequest request) {
		
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 수신함 - 근태결재 글의 총 페이지수를 알아오기
		int totalPage = service.getVacationTempTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);		
		
		return jsonObj.toString();
	}
	
	
	// 내문서함 - 임시저장함 - 근태결재문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuVacation_temp_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuVacation_temp_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String vcatname = request.getParameter("vcatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("vcatname", request.getParameter("vcatname"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("vno", request.getParameter("vno"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
				
		String userid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			userid = loginuser.getEmployeeid(); 
			String pcode = loginuser.getFk_pcode();
			
			String totalOffCnt = service2.selectTotaloffCnt(pcode);
			
			// 이용자의 사용연차수 가지고 오기
	    	String useOffCnt = service2.selectUseoffCnt(userid);
	    	
	    	// 전체 연차수
	    	double dtotalOffCnt = Double.parseDouble(totalOffCnt);
	    	// 사용 연차수
	    	double duseOffCnt = Double.parseDouble(useOffCnt);
	    	
	    	// 이용자의 남은연차수
	    	String leftOffCnt = String.valueOf(dtotalOffCnt-duseOffCnt);		
		
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("ano", ano);
			paraMap.put("vcatname", vcatname);
			paraMap.put("userid", userid);
			
			// 근태결재문서 한 개 상세보기
			ApprovalSiaVO avo = service.myDocuVacation_temp_detail(paraMap);						
			ApprovalSiaVO mng = service.viewMng(paraMap);
			
			mav.addObject("avo", avo);
			mav.addObject("mng", mng);
			mav.addObject("leftOffCnt", leftOffCnt);			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuVacation_temp_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 파일 삭제
	@ResponseBody
	@RequestMapping(value="/t1/removeFile.tw", method= {RequestMethod.POST})
	public String removeFile(HttpServletRequest request) {
		
		String ano = request.getParameter("ano");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		
		ApprovalSiaVO avo = service.getViewFile(paraMap);
		String fileName = avo.getFileName();
		
		if(fileName != null && !"".equals(fileName)) {
			paraMap.put("fileName", fileName);
			
			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");
			String path = root+"resources"+File.separator+"files";
			
			paraMap.put("path", path);
		}
	 
		int n = service.removeFile(paraMap);
		
		JSONObject jsonObj = new JSONObject();		
		jsonObj.put("n", n);
				
		return jsonObj.toString();
	}
	
	
	
	// 내문서함 - 임시저장함/발신함 - 삭제버튼 클릭
	@ResponseBody
	@RequestMapping(value="/t1/remove.tw", method= {RequestMethod.POST})
	public String remove(HttpServletRequest request) { 
		
		String ano = request.getParameter("ano");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		
		ApprovalSiaVO avo = service.getViewFile(paraMap);
		String fileName = avo.getFileName();
		
		if(fileName != null && !"".equals(fileName)) {
			paraMap.put("fileName", fileName);
			
			HttpSession session = request.getSession();
			String root = session.getServletContext().getRealPath("/");
			String path = root+"resources"+File.separator+"files";
			
			paraMap.put("path", path);			
		}
	 
		int n = service.remove(paraMap);
		
		JSONObject jsonObj = new JSONObject();		
		jsonObj.put("n", n);
				
		return jsonObj.toString();
	}
	
	
	// 내문서함 - 임시저장함 - 일반결재 - 저장버튼 클릭	
	@RequestMapping(value="/t1/save.tw", method= {RequestMethod.POST})
	public ModelAndView save(ModelAndView mav, ApprovalSiaVO avo, MultipartHttpServletRequest mrequest) { 
		
		String mdate1 = mrequest.getParameter("mdate1");
		String mdate2 = mrequest.getParameter("mdate2");
		String mdate3 = mrequest.getParameter("mdate3");
		String fk_wiimdate1 = mrequest.getParameter("fk_wiimdate1");
		String fk_wiimdate2 = mrequest.getParameter("fk_wiimdate2");
		
		String mdate = mdate1+" "+mdate2+" ~ "+mdate3;
		String fk_wiimdate = fk_wiimdate1+" ~ "+fk_wiimdate2;
		
		avo.setMdate(mdate);
		avo.setFk_wiimdate(fk_wiimdate);
		
		MultipartFile attach = avo.getAttach();
		
		if(attach != null && !attach.isEmpty()) {
			// 첨부파일이 있을 경우
			
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"files";
			
			String newFileName = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				String originalFilename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				
				avo.setFileName(newFileName);
				avo.setOrgFilename(originalFilename);
				
				fileSize = attach.getSize();
				avo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		try {
			if(attach == null || attach.isEmpty()) {
				// 첨부파일이 없는 경우
				n = service.save(avo);
			}
			else {
				// 첨부파일이 있는 경우
				n = service.save_withFile(avo);
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n==1) {
			mav.setViewName("redirect:/t1/myDocuNorm_temp.tw");
		}
		else {
			System.out.println("실패!");
		}
				
		return mav;
	}	
	
	
	// 내문서함 - 임시저장함 - 일반결재 - 제출버튼 클릭	
	@RequestMapping(value="/t1/submit.tw", method= {RequestMethod.POST})
	public ModelAndView submit(ModelAndView mav, ApprovalSiaVO avo, MultipartHttpServletRequest mrequest) { 
		
		String mdate1 = mrequest.getParameter("mdate1");
		String mdate2 = mrequest.getParameter("mdate2");
		String mdate3 = mrequest.getParameter("mdate3");
		String fk_wiimdate1 = mrequest.getParameter("fk_wiimdate1");
		String fk_wiimdate2 = mrequest.getParameter("fk_wiimdate2");
		
		String mdate = mdate1+" "+mdate2+" ~ "+mdate3;
		String fk_wiimdate = fk_wiimdate1+" ~ "+fk_wiimdate2;
		
		System.out.println(mdate);

		HttpSession session = mrequest.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		avo.setFk_employeeid(userid);
		avo.setMdate(mdate);
		avo.setFk_wiimdate(fk_wiimdate);
		
		MultipartFile attach = avo.getAttach();
		
		if(attach != null && !attach.isEmpty()) {
			// 첨부파일이 있을 경우
			
			session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"files";
			
			String newFileName = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				String originalFilename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				
				avo.setFileName(newFileName);
				avo.setOrgFilename(originalFilename);
				
				fileSize = attach.getSize();
				avo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		try {
			if(attach == null || attach.isEmpty()) {
				// 첨부파일이 없는 경우
				
				n = service.submit(avo);
			}
			else {
				// 첨부파일이 있는 경우
				
				n = service.submit_withFile(avo);
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n==1) {
			mav.setViewName("redirect:/t1/myDocuNorm_send.tw");
		}
		else {
			System.out.println("실패!");
		}
				
		return mav;
	}
	
	
	// 내문서함 - 임시저장함 - 지출결재 - 저장버튼 클릭	
	@RequestMapping(value="/t1/saveSpend.tw", method= {RequestMethod.POST})
	public ModelAndView saveSpend(ModelAndView mav, ApprovalSiaVO avo, MultipartHttpServletRequest mrequest) { 
		
		MultipartFile attach = avo.getAttach();
		
		if(attach != null && !attach.isEmpty()) {
			// 첨부파일이 있을 경우
			
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"files";
			
			String newFileName = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				String originalFilename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				
				avo.setFileName(newFileName);
				avo.setOrgFilename(originalFilename);
				
				fileSize = attach.getSize();
				avo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		try {
			if(attach == null || attach.isEmpty()) {
				// 첨부파일이 없는 경우
				n = service.saveSpend(avo);
			}
			else {
				// 첨부파일이 있는 경우
				n = service.saveSpend_withFile(avo);
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n==1) {
			mav.setViewName("redirect:/t1/myDocuSpend_temp.tw");
		}
		else {
			System.out.println("실패!");
		}
				
		return mav;
	}
	
	
	// 내문서함 - 임시저장함 - 지출결재 - 제출버튼 클릭	
	@RequestMapping(value="/t1/submitSpend.tw", method= {RequestMethod.POST})
	public ModelAndView submitSpend(ModelAndView mav, ApprovalSiaVO avo, MultipartHttpServletRequest mrequest) { 
		
		HttpSession session = mrequest.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		avo.setFk_employeeid(userid);
		
		MultipartFile attach = avo.getAttach();
		
		if(attach != null && !attach.isEmpty()) {
			// 첨부파일이 있을 경우
			
			session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"files";
			
			String newFileName = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				String originalFilename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				
				avo.setFileName(newFileName);
				avo.setOrgFilename(originalFilename);
				
				fileSize = attach.getSize();
				avo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		try {
			if(attach == null || attach.isEmpty()) {
				// 첨부파일이 없는 경우
				n = service.submitSpend(avo);
			}
			else {
				// 첨부파일이 있는 경우
				n = service.submitSpend_withFile(avo);
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n==1) {
			mav.setViewName("redirect:/t1/myDocuSpend_send.tw");
		}
		else {
			System.out.println("실패!");
		}
				
		return mav;
	}
	
	
	// 내문서함 - 임시저장함 - 근태결재 - 저장버튼 클릭
	@RequestMapping(value="/t1/saveVacation.tw", method= {RequestMethod.POST})
	public ModelAndView saveVacation(ModelAndView mav, ApprovalSiaVO avo, MultipartHttpServletRequest mrequest) { 
					
		MultipartFile attach = avo.getAttach();
		
		if(attach != null && !attach.isEmpty()) {
			// 첨부파일이 있을 경우
			
			HttpSession session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"files";
			
			String newFileName = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				String originalFilename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				
				avo.setFileName(newFileName);
				avo.setOrgFilename(originalFilename);
				
				fileSize = attach.getSize();
				avo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		try {
			if(attach == null || attach.isEmpty()) {
				// 첨부파일이 없는 경우
				n = service.saveVacation(avo);
			}
			else {
				// 첨부파일이 있는 경우
				n = service.saveVacation_withFile(avo);
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n==1) {
			mav.setViewName("redirect:/t1/myDocuVacation_temp.tw");
		}
		else {
			System.out.println("실패!");
		}
				
		return mav;
	}
	
	// 내문서함 - 임시저장함 - 지출결재 - 제출버튼 클릭	
	@RequestMapping(value="/t1/submitVacation.tw", method= {RequestMethod.POST})
	public ModelAndView submitVacation(ModelAndView mav, ApprovalSiaVO avo, MultipartHttpServletRequest mrequest) { 
		
		HttpSession session = mrequest.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		avo.setFk_employeeid(userid);
		
		MultipartFile attach = avo.getAttach();
		
		if(attach != null && !attach.isEmpty()) {
			// 첨부파일이 있을 경우
			
			session = mrequest.getSession();
			String root = session.getServletContext().getRealPath("/");
			
			String path = root+"resources"+File.separator+"files";
			
			String newFileName = "";
			
			byte[] bytes = null;
			
			long fileSize = 0;
			
			try {
				bytes = attach.getBytes();
				
				String originalFilename = attach.getOriginalFilename();
				
				newFileName = fileManager.doFileUpload(bytes, originalFilename, path);
				
				avo.setFileName(newFileName);
				avo.setOrgFilename(originalFilename);
				
				fileSize = attach.getSize();
				avo.setFileSize(String.valueOf(fileSize));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		int n = 0;
		try {
			if(attach == null || attach.isEmpty()) {
				// 첨부파일이 없는 경우
				n = service.submitVacation(avo);
			}
			else {
				// 첨부파일이 있는 경우
				n = service.submitVacation_withFile(avo);
			}			
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		if(n==1) {
			mav.setViewName("redirect:/t1/myDocuVacation_send.tw");
		}
		else {
			System.out.println("실패!");
		}
				
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
			
	// 내문서함 - 수신함 - 결재완료 - 일반결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/norm_completelist.tw", produces="text/plain;charset=UTF-8")
	public String norm_completelist(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();		
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getnorm_completelist(paraMap);
		
		JSONArray jsonArr = new JSONArray();
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("ncatname", appvo.getNcatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				
				jsonArr.put(jsonObj);
			}
		}
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 수신함 - 결재완료 - 일반결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getNormCompleteTotalPage.tw", method= {RequestMethod.GET})
	public String getNormCompleteTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String ncat = request.getParameter("ncat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(ncat.length() == 0) {
			a = "";
		}
		else {
			String[] ncatArr = ncat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<ncatArr.length; i++) {
				cnt++;
				
				if(cnt < ncatArr.length) {
					a += "'"+ncatArr[i]+"',";
				}
				else {
					a += "'"+ncatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 수신함 - 결재완료 - 일반결재 글의 총 페이지수를 알아오기
		int totalPage = service.getNormCompleteTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
		
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 수신함 - 결재완료 - 일반결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuNorm_complete_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuNorm_complete_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String ncatname = request.getParameter("ncatname");
				
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("ncatname", request.getParameter("ncatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("ncat", request.getParameter("ncat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("ncatname", ncatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			ApprovalSiaVO avo = service.myDocuNorm_detail(paraMap);			
			
			mav.addObject("avo", avo);
			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuNorm_complete_detail.gwTiles");
		
		return mav;
	}
		
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 결재완료 - 지출결재문서에 해당하는 문서 조회하기 (Ajax 처리)
	@ResponseBody
	@RequestMapping(value="/t1/spend_completelist.tw", produces="text/plain;charset=UTF-8")
	public String spend_completelist(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");
				
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null || currentShowPageNo == "") {
			currentShowPageNo = "1";
		}
		
		try {
			Integer.parseInt(currentShowPageNo); 
		} catch (Exception e) {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getSpend_completelist(paraMap);
		
		JSONArray jsonArr = new JSONArray();		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("scatname", appvo.getScatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 검색에 해당하는 수신함 - 결재완료 - 지출결재 글의 총 페이지수를 알아오기
	@ResponseBody
	@RequestMapping(value="/t1/getSpendCompleteTotalPage.tw", method= {RequestMethod.GET})
	public String getSpendCompleteTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String scat = request.getParameter("scat");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(scat.length() == 0) {
			a = "";
		}
		else {
			String[] scatArr = scat.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<scatArr.length; i++) {
				cnt++;
				
				if(cnt < scatArr.length) {
					a += "'"+scatArr[i]+"',";
				}
				else {
					a += "'"+scatArr[i]+"'";
				}				
			}
		}
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 수신함 - 결재완료 - 지출결재 글의 총 페이지수를 알아오기
		int totalPage = service.getSpendCompleteTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage); 
				
		return jsonObj.toString();		
	}
	
	
	// 내문서함 - 수신함 - 결재완료 - 지출 결재 문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuSpend_complete_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuSpend_complete_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String scatname = request.getParameter("scatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("scatname", request.getParameter("scatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("scat", request.getParameter("scat"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("scatname", scatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			ApprovalSiaVO avo = service.myDocuSpend_detail(paraMap);			
			mav.addObject("avo", avo);			
		} catch (NumberFormatException e) {
			
		}
		
		mav.setViewName("sia/myDocumentDetail/myDocuSpend_complete_detail.gwTiles");
		
		return mav;
	}
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 결재완료문서 - 근태결재문서에 해당하는 문서 조회하기
	@ResponseBody
	@RequestMapping(value="/t1/vacation_completelist.tw", produces="text/plain;charset=UTF-8")
	public String vacation_completelist(HttpServletRequest request) {
					
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String currentShowPageNo = request.getParameter("currentShowPageNo");		
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}
			}
		}
		
		if(currentShowPageNo == null ||currentShowPageNo=="") {
			currentShowPageNo = "1";
		}
		
		int sizePerPage = 10;
		
		int startRno = ((Integer.parseInt(currentShowPageNo) - 1) * sizePerPage) + 1;
		int endRno = startRno + sizePerPage - 1;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("startRno", String.valueOf(startRno));
		paraMap.put("endRno", String.valueOf(endRno));
		paraMap.put("userid", userid);
		
		List<ApprovalSiaVO> approvalvo = service.getVacation_completelist(paraMap);		
		
		JSONArray jsonArr = new JSONArray();		
		
		if(approvalvo != null) {
			for(ApprovalSiaVO appvo : approvalvo) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("atitle", appvo.getAtitle());
				jsonObj.put("ano", appvo.getAno());
				jsonObj.put("vcatname", appvo.getVcatname());
				jsonObj.put("astatus", appvo.getAstatus());
				jsonObj.put("asdate", appvo.getAsdate());
				jsonObj.put("rno", appvo.getRno());
				
				jsonArr.put(jsonObj);
			}
		}		
		
		return jsonArr.toString();
	}
	
	
	// 수신함 - 결재완료 - 근태결재 totalPage 알아오기 (Ajax 로 처리)
	@ResponseBody
	@RequestMapping(value="/t1/getVacationCompleteTotalPage.tw", method= {RequestMethod.GET})
	public String getVacationCompleteTotalPage(HttpServletRequest request) {
		
		String astatus = request.getParameter("astatus");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		String vno = request.getParameter("vno");
		String sort = request.getParameter("sort");
		String searchWord = request.getParameter("searchWord");
		String sizePerPage = request.getParameter("sizePerPage");
		
		if(astatus == null || (!"0".equals(astatus) && !"1".equals(astatus) && !"2".equals(astatus) && !"3".equals(astatus))) {
			astatus = "";
		}
		if(fromDate == null || toDate == null) {
			fromDate = "";
			toDate = "";
		}
		if(sort == null || (!"atitle".equals(sort) && !"ano".equals(sort))) {
			sort = "";
		}
		if(searchWord == null || "".equals(searchWord) || searchWord.trim().isEmpty()) {
			searchWord = "";
		}
		
		String a = "";
		if(vno.length() == 0) {
			a = "";
		}
		else {
			String[] vnoArr = vno.split(","); // [2,3]
			int cnt = 0;
			for(int i=0; i<vnoArr.length; i++) {
				cnt++;
				
				if(cnt < vnoArr.length) {
					a += "'"+vnoArr[i]+"',";
				}
				else {
					a += "'"+vnoArr[i]+"'";
				}				
			}
		}		
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO) session.getAttribute("loginuser");		
		String userid = loginuser.getEmployeeid();
		
		Map<String, String> paraMap = new HashMap<>();		
		paraMap.put("astatus", astatus);
		paraMap.put("fromDate", fromDate);
		paraMap.put("toDate", toDate);
		paraMap.put("sort", sort);
		paraMap.put("searchWord", searchWord);
		paraMap.put("a", a);
		paraMap.put("sizePerPage", sizePerPage);
		paraMap.put("userid", userid);
		
		// 검색에 해당하는 결재완료 - 근태결재 글의 총 페이지수를 알아오기
		int totalPage = service.getVacationCompleteTotalPage(paraMap);
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("totalPage", totalPage);		
		
		return jsonObj.toString();
	}
	

	// 내문서함 - 결재완료 - 근태결재문서 한 개 상세보기
	@RequestMapping(value="/t1/myDocuVacation_complete_detail.tw", produces="text/plain;charset=UTF-8")
	public ModelAndView myDocuVacation_complete_detail(HttpServletRequest request, ModelAndView mav) {
		String ano = request.getParameter("ano");		
		String vcatname = request.getParameter("vcatname");
		
		mav.addObject("ano", request.getParameter("ano"));
		mav.addObject("vcatname", request.getParameter("vcatname"));		
		mav.addObject("astatus", request.getParameter("astatus"));
		mav.addObject("fromDate", request.getParameter("fromDate"));
		mav.addObject("toDate", request.getParameter("toDate"));
		mav.addObject("vno", request.getParameter("vno"));
		mav.addObject("sort", request.getParameter("sort"));		
		mav.addObject("searchWord", request.getParameter("searchWord"));
		mav.addObject("currentShowPageNo", request.getParameter("currentShowPageNo"));
		
		
		String employeeid = null;
		
		HttpSession session = request.getSession();
		MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
		
		if(loginuser != null) {
			employeeid = loginuser.getEmployeeid(); 
		}
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		paraMap.put("vcatname", vcatname);
		paraMap.put("employeeid", employeeid);
		
		try {
			// 근태결재문서 한 개 상세보기
			ApprovalSiaVO avo = service.myDocuVacation_complete_detail(paraMap);						
			
			mav.addObject("avo", avo);		
			
		} catch (NumberFormatException e) {
			
		}
		
		
		mav.setViewName("sia/myDocumentDetail/myDocuVacation_complete_detail.gwTiles");
		
		return mav;
	}
	
	
	// 첨부파일 다운로드 받기
	@RequestMapping(value="/t1/download.tw")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		
		String ano = request.getParameter("ano");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ano", ano);
		
		response.setContentType("text/html; charset=UTF-8"); // 리턴타입이 void 이기 때문에 view단이 없기 때문에 html로 작성해야 한다.
		PrintWriter out = null;
		
		try {
			Integer.parseInt(ano);
			
			ApprovalSiaVO avo = service.getViewFile(paraMap);
			
			if(avo == null ||(avo != null &&  avo.getFileName() == null)) {
				out = response.getWriter();
				// 웹브라우저상에 메시지를 쓰기 위한 객체 생성
				
				out.println("<script type='text/javascript'>alert('존재하지 않는 문서번호이거나 첨부파일 없으므로 파일 다운로드가 불가합니다!!'); history.back();</script>");
				
				return; // 종료
			}
			else {
				String fileName = avo.getFileName();
								
				String orgFilename = avo.getOrgFilename();
				
				HttpSession session = request.getSession();
				String root = session.getServletContext().getRealPath("/");
				
				String path = root+"resources"+File.separator+"files";
				
				// **** file 다운로드 하기 **** //
				boolean flag = false; // file 다운로드의 성공,실패를 알려주는 용도
				flag = fileManager.doFileDownload(fileName, orgFilename, path, response);
				
				if(!flag) {
					
					out = response.getWriter();

					out.println("<script type='text/javascript'>alert('파일 다운로드가 실패되었습니다!!'); history.back();</script>");            
				}				
			}
			
		} catch (NumberFormatException e) {
			try {
				out = response.getWriter();
				
				out.println("<script type='text/javascript'>alert('파일 다운로드가 불가합니다!!'); history.back();</script>");
			} catch (IOException e1) {				
				
			}			
		} catch (IOException e2) {				
			
		}
		
	}
	
	
	// 스마트에디터. 드래그앤드롭을 사용한 다중사진 파일업로드 
	@RequestMapping(value="/t1/image/multiplePhotoUpload.tw", method={RequestMethod.POST})
	public void multiplePhotoUpload(HttpServletRequest req, HttpServletResponse res) {
		
		HttpSession session = req.getSession();
		String root = session.getServletContext().getRealPath("/");
		String path = root+"resources"+File.separator+"photo_upload";
		
		File dir = new File(path);
		if(!dir.exists())
			dir.mkdirs();
		
		String strURL = "";
		
		try {
			if(!"OPTIONS".equals(req.getMethod().toUpperCase())) {
				String filename = req.getHeader("file-name"); //파일명을 받는다 - 일반 원본파일명
				
				InputStream is = req.getInputStream();
				
				String newFilename = fileManager.doFileUpload(is, filename, path);
				
				int width = fileManager.getImageWidth(path+File.separator+newFilename);
				
				if(width > 600)
					width = 600;
				
				String CP = req.getContextPath();
				
				strURL += "&bNewLine=true&sFileName="; 
				strURL += newFilename;
				strURL += "&sWidth="+width;
				strURL += "&sFileURL="+CP+"/resources/photo_upload/"+newFilename;
			}		
		
			PrintWriter out = res.getWriter();
			out.print(strURL);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// 결재의견 삭제하기
	@ResponseBody
	@RequestMapping(value="/t1/delMyOpinion.tw", method= {RequestMethod.POST})
	public String delMyOpinion(HttpServletRequest request) {
		
		String ono = request.getParameter("ono");
		
		Map<String, String> paraMap = new HashMap<>();
		paraMap.put("ono", ono);
		
		int n = service.delMyOpinion(paraMap);
		
		JSONObject jsonObj = new JSONObject();		
		jsonObj.put("n", n);
				
		return jsonObj.toString();
	}
	

}
