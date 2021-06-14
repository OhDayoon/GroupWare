package com.t1works.groupware.bwb.service;

import java.util.List;
import java.util.Map;

import com.t1works.groupware.bwb.model.MemberBwbVO;

public interface InterHomepageBwbService {
   
   // 직원테이블에서 select해오기
   MemberBwbVO selectMember(Map<String, String> paraMap);
   
   // 로그인 기록테이블에 insert하기 
   int insertlogin_history(Map<String, String> paraMap);
   
   // 출퇴근기록 테이블에 insert하기(출근시간)
   int insertIntime(Map<String, String> paraMap);
   
   // 출퇴근기록 테이블에서 select하기(출근시간)
   String selectIntime(Map<String, String> paraMap);
   
   // 지각여부 판단하기(update)
   void updatelateno(Map<String, String> paraMap);
   
   // 지각여부 판단하기(select)
   String selectlateno(String intime);
   
   // 출퇴근기록 테이블에 update하기(퇴근시간)
   String updateOuttime(Map<String, String> paraMap) throws Throwable;
   
   // 출퇴근기록 테이블에서 select작업(퇴근시간)
   String selectOuttime(Map<String, String> paraMap);
   
   // 이용자의 총 연차수 가지고 오기
   String selectTotaloffCnt(String pcode);
   
   // 이용자의 사용연차수 가지고 오기
   String selectUseoffCnt(String fk_employeeid);
   
   // 나의 월별 출퇴근기록 가지고오기
   List<Map<String, String>> selectmyMonthIndolence(String fk_employeeid);
   
   // 부서 월별 출퇴근기록 가지고오기
   List<Map<String, String>> selectDepMonthIndolence(String fk_dcode);
   
   // 여행상품들의 일정 뽑아오기
   List<Map<String, String>> productSchedule();
   
   // 이용자의 최근1주일 근로시간 가지고오기
   Map<String, String> selecthourMap(Map<String, String> searchMap);
   
   // 일주일치 날짜 가지고오기	
   Map<String, String> selectWeekDay(String today);
   
   // word-cloud 차트를 위해 데이터 뽑아오기
   List<String> selectWordList();
   
   // 검색어 입력 시 자동검색기능(ajax처리)
   List<String> wordSearch(Map<String, String> paraMap);
   
   // 검색어 입력 후 URL주소 뽑아오기
   String goSebuMenu(Map<String, String> paraMap);
   
   // 해당 검색어 tbl_word에 insert시켜주기
   void insertWord(String searchWord);
   
   // 고객여행일정 가지고오기
   List<Map<String, String>> selectScheduleList(String clientname);

   
   
  
   
}