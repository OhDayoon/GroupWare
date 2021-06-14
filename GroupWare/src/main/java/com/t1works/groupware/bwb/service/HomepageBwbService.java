package com.t1works.groupware.bwb.service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.t1works.groupware.bwb.model.InterMemberBwbDAO;
import com.t1works.groupware.bwb.model.MemberBwbVO;
import com.t1works.groupware.common.AES256;
import com.t1works.groupware.common.Sha256;


@Component
@Service
public class HomepageBwbService implements InterHomepageBwbService {
   
   @Autowired
   private InterMemberBwbDAO dao;
   
   @Autowired
   private AES256 aes;
   
   // 로그인시 해당 회원 조회해오기
   @Override
   public MemberBwbVO selectMember(Map<String, String> paraMap) {
      
	  // employeeid를 가지고 주민번호 가져오기
	  String sjubun = dao.selectJubun(paraMap.get("employeeid"));
	  
	  
	  
	  String jubun = "";
	  String fjubun ="";
	  MemberBwbVO mvo = null;
	  
	  try {
		  if(sjubun.length()>13) { 
			  jubun = aes.decrypt(sjubun);
			  fjubun = jubun.substring(0, 6); // 930305
		  }
		  else fjubun = sjubun.substring(0, 6); // 930305;
		  
	  } catch (UnsupportedEncodingException | GeneralSecurityException e) {
		e.printStackTrace();
	  }
	  
	 
	  
	  String passwd = paraMap.get("passwd");
	  
	  if(fjubun.equalsIgnoreCase(passwd)) { // 초기 비밀번호를 안바꾼 사람

		  mvo = dao.selectMember(paraMap);
	  }
	  else {
		  paraMap.put("passwd", Sha256.encrypt(passwd));
		  mvo = dao.selectMember(paraMap);

	  }
      
      return mvo;
   }
   
   
   
   // 로그인기록 테이블에 insert하기
   @Override
   public int insertlogin_history(Map<String, String> paraMap) {
	   
	   int n = dao.insertlogin_history(paraMap);
	   
	   return n;
   }
   
   // 출퇴근기록 테이블에 insert하기(출근시간)
   @Override
   public int insertIntime(Map<String, String> paraMap) {
	   
	   int n = dao.insertIntime(paraMap);
	   return n;
   }
   
   // 출퇴근기록 테이블에 select하기(출근시간)
   @Override
   public String selectIntime(Map<String, String> paraMap) {
	   
	   String intime = dao.selectIntime(paraMap);
	   return intime;
   }
   
   
   // 지각여부 판단하기(update)
   @Override
   public void updatelateno(Map<String, String> paraMap) {
	   
	   dao.updatelateno(paraMap);
	   
   }
   
   
	// 지각여부 판단하기(select)
	@Override
	public String selectlateno(String intime) {
		
		String latenoTime = dao.selectlateno(intime);
		
		return latenoTime;
	}
	
	// 출퇴근기록 테이블에 update하기(퇴근시간)
	@Override
	@Transactional(propagation=Propagation.REQUIRED, isolation=Isolation.READ_COMMITTED, rollbackFor= {Throwable.class})
	public String updateOuttime(Map<String, String> paraMap) throws Throwable{
		
		 int n = dao.updateOuttime(paraMap);
		
		 // 출퇴근테이블에서 select하기(퇴근시간)
		 if(n==1) {
			 String outtime = dao.selectOuttime(paraMap); // xx:xx:xx
			 
			 int outTimehh = Integer.parseInt(outtime.substring(0, 2)); // 시간만 뽑아오기
			 int outTimemm = Integer.parseInt(outtime.substring(3, 5)); // 분만 뽑아오기
			 
			 String doLateTime = "0"; // 적용되는 시간
			 
			 if((outTimehh==19 && outTimemm>=45)||(outTimehh==20 && outTimemm<45)) {
				 doLateTime="1";
			 }
			 else if((outTimehh==20 && outTimemm>=45)||(outTimehh==21 && outTimemm<45)) {
				 doLateTime="2";
			 }
			 else if((outTimehh==21 && outTimemm>=45)||(outTimehh==22 && outTimemm<45)) {
				 doLateTime="3";
			 }
			 else if((outTimehh==22 && outTimemm>=45)||(outTimehh==23 && outTimemm<45)) {
				 doLateTime="4";
			 }
			 else if((outTimehh==23 && outTimemm>=45 && outTimemm<=59)) {
				 doLateTime="5";
			 }
			 
			 // 야근테이블에 insert하기
			 if(!doLateTime.equalsIgnoreCase("0")) {
				 paraMap.put("doLateTime", doLateTime);
				 int m = dao.insertdoLate(paraMap);
				 
				 if(m==1) {
					 return outtime;
				 }
				 else {
					 return "";
				 }
			 } // end of if(!doLateTime.equalsIgnoreCase("0")) 
			 else {
				 return outtime;
			 }
			 
		 }// end of if(n==1) {
		 else {
			 return "";
		 }

	}// end of public String updateOuttime(Map<String, String> paraMap) throws Throwable


	// 출퇴근기록 테이블에서 select작업(퇴근시간)
	@Override
	public String selectOuttime(Map<String, String> paraMap) {
		String outtime = dao.selectOuttime(paraMap);
		return outtime;
	}


	// 이용자의 총 연차수 가지고 오기
	@Override
	public String selectTotaloffCnt(String pcode) {
		String totalOffCnt = dao.selectTotaloffCnt(pcode);
		return totalOffCnt;
	}


	// 이용자의 사용연차수 가지고 오기
	@Override
	public String selectUseoffCnt(String fk_employeeid) {
		
		String useOffCnt = dao.selectUseoffCnt(fk_employeeid);
		return useOffCnt;
	}


	 // 나의 월별 출퇴근기록 가지고오기
	@Override
	public List<Map<String, String>> selectmyMonthIndolence(String fk_employeeid) {
		
		List<Map<String, String>> myIndolenceList = dao.selectmyMonthIndolence(fk_employeeid);
		return myIndolenceList;
	}


	// 부서 월별 출퇴근기록 가지고오기
	@Override
	public List<Map<String, String>> selectDepMonthIndolence(String fk_dcode) {
		List<Map<String,String>> depIndolenceList = dao.selectDepMonthIndolence(fk_dcode);
		return depIndolenceList;
	}


	// 여행상품들의 일정 뽑아오기
	@Override
	public List<Map<String, String>> productSchedule() {
		List<Map<String, String>> productList = dao.productSchedule();
		return productList;
	}


	// 이용자의 최근1주일 근로시간 가지고오기
	@Override
	public Map<String, String> selecthourMap(Map<String, String> searchMap) {
		
		Map<String, String> hourMap = dao.selecthourMap(searchMap);
		
		// 넘어온 Map파라미터에서 날짜 뽑아와서 format후 다시 또 새로운 MAP에 넣어주기
		String gooutdate = searchMap.get("doneDay"); // 2021-05-05
		String mm = gooutdate.substring(5, 7);
		String dd = gooutdate.substring(8);
		gooutdate = mm+"월"+dd+"일";
		
		String doneHour=""; // 근무시간
		
		if(hourMap==null) {
			hourMap = new HashMap<>();
			doneHour="0";
			hourMap.put("doneHour", doneHour);
			hourMap.put("gooutdate", gooutdate);
		}
		else {
			String intime = hourMap.get("intime");
			String outtime = hourMap.get("outtime");
			
			hourMap.put("gooutdate", gooutdate);
			
    		int intimeHH = Integer.parseInt(intime.substring(0, 2)); // 09
    		int intimeMM = Integer.parseInt(intime.substring(3, 5)); // 15
    		
    		int outtimeHH = Integer.parseInt(outtime.substring(0, 2));
    		int outtimeMM = Integer.parseInt(outtime.substring(3, 5));
    		
    		
    		if(outtimeMM>=intimeMM) { // 09:15 ~ 17:30
    			doneHour = String.valueOf(outtimeHH-intimeHH);
    		}
    		else {// 09:15 ~ 17:05
    			doneHour = String.valueOf(outtimeHH-intimeHH-1);
    		}
			
    		hourMap.put("doneHour", doneHour);
		}
		
		
		return hourMap;

		
		/*
    	for(Map<String,String> resultMap:hourList) {
    		

    		String intime = resultMap.get("intime"); // 09:15
    		int intimeHH = Integer.parseInt(intime.substring(0, 2)); // 09
    		int intimeMM = Integer.parseInt(intime.substring(3, 5)); // 15
    		
    		String outtime = resultMap.get("outtime");
    		int outtimeHH = Integer.parseInt(outtime.substring(0, 2));
    		int outtimeMM = Integer.parseInt(outtime.substring(3, 5));
    		
    		String doneHour="";
    		if(outtimeMM>intimeMM) { // 09:15 ~ 17:30
    			doneHour = String.valueOf(outtimeHH-intimeHH);
    			totalhour += outtimeHH-intimeHH;
    			resultMap.put("totalhour", String.valueOf(totalhour));
    		}
    		else {// 09:15 ~ 17:05
    			doneHour = String.valueOf(outtimeHH-intimeHH-1);
    			totalhour += outtimeHH-intimeHH;
    			resultMap.put("totalhour", String.valueOf(totalhour));
    		}
	
    		resultMap.put("doneHour", doneHour);
    	}
    	*/
		
	} // end of 


	 // 일주일치 날짜 가지고오기
	@Override
	public Map<String, String> selectWeekDay(String today) {
		
		Map<String, String> weekMap = dao.selectWeekDay(today);
		return weekMap;
	}


	// word-cloud 차트를 위해 데이터 뽑아오기
	@Override
	public List<String> selectWordList() {
		
		List<String> wordList = dao.selectWordList();
		return wordList;
	}


	// 검색어 입력 시 자동검색기능(ajax처리)
	@Override
	public List<String> wordSearch(Map<String, String> paraMap) {
		
		String pcode= paraMap.get("pcode");
		String dcode= paraMap.get("dcode");
		String searchWord = paraMap.get("searchWord");
		List<String> wordList = dao.wordSearch(searchWord);
		
		
		if("1".equalsIgnoreCase(pcode)||"2".equalsIgnoreCase(pcode)) { // 사원 + 대리
			
			// 부서업무현황, 부서실적현황, 부서별 실적현황,회사실적현황,인사관리,대여관리,신입사원등록 제거
			wordList.remove("부서업무현황");
			wordList.remove("부서실적현황");
			wordList.remove("부서별 실적현황");
			wordList.remove("회사실적현황");
			wordList.remove("인사관리");
			wordList.remove("신입사원등록");
			wordList.remove("대여관리");
			
			if("4".equalsIgnoreCase(dcode)||"5".equalsIgnoreCase(dcode)) { // 총무팀,인사팀인경우
				// 나의업무현황,나의실적현황 제거
				wordList.remove("나의업무현황");
				wordList.remove("나의실적현황");
			}
			
		}
		else if("3".equalsIgnoreCase(pcode)) { // 부장로그인 시
			
			// 부서별 실적현황, 회사실적현황,나의실적현황,인사관리,대여관리,신입사원등록 제거
			
			wordList.remove("부서별 실적현황");
			wordList.remove("회사실적현황");
			wordList.remove("나의실적현황");
			wordList.remove("인사관리");
			wordList.remove("대여관리");
			wordList.remove("신입사원등록");
			
			if("4".equalsIgnoreCase(dcode)) {
				wordList.add("인사관리");
				wordList.add("신입사원등록");
			}
			else if("5".equalsIgnoreCase(dcode)) {
				wordList.add("대여관리");
			}
			
		}
		else {
			
			// 나의업무현황, 부서업무현황, 부서실적현황, 대여관리, 인사관리, 신입사원등록, 나의업무현황,나의실적현황
			wordList.remove("나의업무현황");
			wordList.remove("부서업무현황");
			wordList.remove("부서실적현황");
			wordList.remove("대여관리");
			wordList.remove("인사관리");
			wordList.remove("신입사원등록");
			wordList.remove("나의실적현황");
		}
		
		
		return wordList;
	}


	// 검색어 입력 후 URL주소 뽑아오기
	@Override
	public String goSebuMenu(Map<String, String> paraMap) {
		
		String pcode= paraMap.get("pcode");
		String searchWord = paraMap.get("searchWord"); // cs부장
		
		String sebuAddress = dao.goSebuMenu(searchWord);
		
		// cs부장이 로그인을 했을 경우
		if("3".equalsIgnoreCase(pcode)&&searchWord.equalsIgnoreCase("나의업무현황")) {
			sebuAddress = "/t1/leaderTodo.tw";
		}
		
		return sebuAddress;
	}


	// 해당 검색어 tbl_word에 insert시켜주기
	@Override
	public void insertWord(String searchWord) {
		dao.insertWord(searchWord);
	}


	// 고객여행일정 가지고오기
	@Override
	public List<Map<String, String>> selectScheduleList(String clientname) {
		List<Map<String, String>> scheduleList = dao.selectScheduleList(clientname);
		return scheduleList;
	}


   
}