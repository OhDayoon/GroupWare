package com.t1works.groupware.bwb.controller;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.bwb.service.InterHomepageBwbService;
import com.t1works.groupware.common.AES256;

@Controller
public class LoginBwbController {
   
   @Autowired // Type에 따라 알아서 Bean 을 주입해준다.
   private InterHomepageBwbService service;
   
   @Autowired // Type에 따라 알아서 Bean 을 주입해준다.
   private AES256 aes;
   
   
   // 그룹웨어 홈페이지 들어갔을때의 화면 구현
   @RequestMapping(value="/t1/home.tw")
   public ModelAndView goGroupware(HttpServletRequest request,ModelAndView mav) {
      
      String method = request.getMethod();
      
      if(!"post".equalsIgnoreCase(method)) { // get방식
         
         HttpSession session = request.getSession();
         MemberBwbVO loginuser = (MemberBwbVO)session.getAttribute("loginuser");
         
         if(loginuser!=null) { // 로그인이 성공했을 경우
        	
        	String fk_employeeid = loginuser.getEmployeeid();
        	String pcode = loginuser.getFk_pcode();
        	
        	// 이용자의 총 연차수 가지고 오기
        	String totalOffCnt = service.selectTotaloffCnt(pcode);
        	
        	// 이용자의 사용연차수 가지고 오기
        	String useOffCnt = service.selectUseoffCnt(fk_employeeid);
        	
        	Double itotalOffCnt = Double.parseDouble(totalOffCnt);
        	Double iuseOffCnt = Double.parseDouble(useOffCnt);
        	
        	// 이용자의 남은연차수
        	String leftOffCnt = String.valueOf(itotalOffCnt-iuseOffCnt);
        	
        	Map<String,String> offMap = new HashMap<>();
        	offMap.put("totalOffCnt", totalOffCnt);
        	offMap.put("useOffCnt", useOffCnt);
        	offMap.put("leftOffCnt", leftOffCnt);
        	
        	Calendar now = Calendar.getInstance();
        	
        	String currentYear = String.valueOf(now.get(Calendar.YEAR)); // 현재년도 나옴
        	
        	int imonth = now.get(Calendar.MONTH)+1; // 0~11까지 나옴
        	String month = imonth<10?"0"+imonth:String.valueOf(imonth); // 현재 월 나옴
        	
        	int iday = now.get(Calendar.DATE);
    	    String day = iday<10?"0"+iday:String.valueOf(iday);
    	    
    	    String today = currentYear+"-"+month+"-"+day; // 2021-05-31
    	    
    	    // 일주일치 날짜 가지고오기
    	    Map<String,String> weekMap = service.selectWeekDay(today);
    	    List<String> weekList= new ArrayList<>();
    
    	    weekList.add(weekMap.get("pppppppday"));     // 2021-05-30
    	    weekList.add(weekMap.get("ppppppday"));
    	    weekList.add(weekMap.get("pppppday"));
    	    weekList.add(weekMap.get("ppppday"));
    	    weekList.add(weekMap.get("pppday"));
    	    weekList.add(weekMap.get("ppday"));      
    	    weekList.add(weekMap.get("pday"));
    	    
    	    List<Map<String,String>> hourList = new ArrayList<>();
    	    
    	    // 해당날짜에 근무한 시간 뽑아오기
    	    for(String doneDay :weekList) {
    	    	// 아래 메소드의 파라미터에 들어갈 MAP생성
    	    	Map<String,String> searchMap = new HashMap<>();
    	    	searchMap.put("fk_employeeid", fk_employeeid);
    	    	searchMap.put("doneDay", doneDay);
    	    	
    	    	 // 날짜와 사번가지고 일하는 시간 가지고 오기
    	    	Map<String,String> hourMap = service.selecthourMap(searchMap); // 3시간 , 6시간 , 
    	    	hourList.add(hourMap);
    	    	
        	}
    	    
    	    mav.addObject("hourList", hourList);
    	            	
        	
          //일주일치 총 근무시간 뽑아내기
        	int itotalhour = 0;
        	for(Map<String,String> resultMap :hourList) {
        		itotalhour += Integer.parseInt(resultMap.get("doneHour"));
        	}
        	
        	String totalhour = String.valueOf(itotalhour);
        	
        	mav.addObject("totalhour", totalhour);
        	
            // word-cloud 차트를 위해 데이터 뽑아오기
        	List<String> wordList = service.selectWordList();
        	String word = "";
        	
        	for(int i=0; i<wordList.size(); i++) {

        		if(i<wordList.size()-1) {
        			word+=wordList.get(i)+",";
        		}
        		else {
        			word+=wordList.get(i);
        		}
        	}// end of for ---
        	
        	
        	mav.addObject("word", word);
        	
        	mav.addObject("offMap", offMap); 
        	
            mav.setViewName("/bwb/homepage.gwTiles");
         }
         else {
            mav.setViewName("/bwb/login/login");
         }
         
      } // end of f(!"post".equalsIgnoreCase(method))
      else { // post방식
         
         String employeeid = request.getParameter("employeeid");
         String passwd = request.getParameter("passwd");
         String loginip = request.getRemoteAddr(); 
         
         Map<String,String> paraMap = new HashMap<>();
         paraMap.put("employeeid", employeeid);
         paraMap.put("passwd", passwd);
         paraMap.put("loginip",loginip);
         
         // 직원테이블에서 select해오기
         MemberBwbVO mvo = service.selectMember(paraMap);

         if(mvo != null) { // 로그인 성공했을경우
        	 
        	// 로그인 기록테이블에 insert하기 
        	int n = service.insertlogin_history(paraMap);
        	
        	String sJubun = mvo.getJubun();
            
            String jubun = "";
            
            if(sJubun.length()>13) {
           	 try {
   				jubun = aes.decrypt(sJubun);
   			} catch (UnsupportedEncodingException | GeneralSecurityException  e) {
   				e.printStackTrace();
   			} 
            }
            else {
           	 jubun = mvo.getJubun();
            }
        	
        	if(n==1) {
	            HttpSession session =  request.getSession();
	            session.setAttribute("loginuser", mvo);
	            session.setAttribute("loginip", loginip);
	            
	            if(passwd.equals(jubun.substring(0, 6))) {
	            	String message = "초기 비밀번호를 변경해주세요.!!";
	         	    String loc = request.getContextPath()+"/t1/mypage.tw";
	         	   
	         	    paraMap = new HashMap<>();
	                paraMap.put("message", message);
	                paraMap.put("loc", loc);
	         	   
	         	    mav.addObject("paraMap", paraMap);
	         	    mav.setViewName("/bwb/msg"); 
	            }
	            else {  
		            
		            String goBackURL =(String)session.getAttribute("goBackURL");
		            
		            if(goBackURL != null) {
		            	mav.setViewName("redirect:/"+goBackURL);
		            	session.removeAttribute(goBackURL); // 세션에서 반드시 제거해주어야 한다.
		            }
		            else {
		            	mav.setViewName("redirect:/t1/home.tw");
		            }
	            }
	            
        	}// end of if(n==1)
        	
        	
         }// end of if(mvo != null)
         else {// 로그인 실패했을 경우
            String message = "아이디와 비밀번호를 다시 확인해주세요";
            String loc = request.getContextPath()+"/t1/home.tw";
            
            paraMap = new HashMap<>();
            paraMap.put("message", message);
            paraMap.put("loc", loc);
            
            mav.addObject("paraMap", paraMap);
            mav.setViewName("/bwb/msg");      
            
         }

      }// end of post
      return mav;
   }// end of public ---
   
   // 로그아웃 메소드
   @RequestMapping(value="/t1/logout.tw")
   public ModelAndView logout(HttpServletRequest request, ModelAndView mav) {
   
	   HttpSession session = request.getSession();
	   session.invalidate(); // 세션값 삭제
	   
	   String message = "로그아웃 되었습니다.";
	   String loc = request.getContextPath()+"/t1/home.tw";
	   
	   Map<String,String> paraMap = new HashMap<>();
       paraMap.put("message", message);
       paraMap.put("loc", loc);
	   
	   mav.addObject("paraMap", paraMap);

	   mav.setViewName("/bwb/msg"); 
	   
	   return mav;

   }
   
   
}