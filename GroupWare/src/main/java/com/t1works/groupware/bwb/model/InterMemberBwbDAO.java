package com.t1works.groupware.bwb.model;

import java.util.List;
import java.util.Map;


public interface InterMemberBwbDAO {

   // 로그인 시 직원 select해오기
   MemberBwbVO selectMember(Map<String, String> paraMap);
   
   // 로그인기록 테이블에 insert하기
   int insertlogin_history(Map<String, String> paraMap);

   // 출퇴근기록 테이블에 insert하기(출근시간)
   int insertIntime(Map<String, String> paraMap);

   // 출퇴근기록 테이블에서 출근시간 select하기
   String selectIntime(Map<String, String> paraMap);
   
   // 지각여부 판단하기(update)
   void updatelateno(Map<String, String> paraMap);
   
   // 지각여부 판단하기(select)
   String selectlateno(String intime);
   
   // 출퇴근기록 테이블에 update하기(퇴근시간)
   int updateOuttime(Map<String, String> paraMap);
   
   // 출퇴근테이블에서 select하기(퇴근시간)
   String selectOuttime(Map<String, String> paraMap);
   
   // 모든직위 가져오기
   List<MemberBwbVO> selectPositionList();
  
   // 소속에 따른 직무설명 가져오기
   String selectDuty(String dname);
   
   // pname과 dname을 통해 pcode,dcode 가져오기.
   Map<String, String> selectPDcode(Map<String, String> paraMap);
   
   // 회원정보 업데이트하기 
   int updateOneInfo(MemberBwbVO mvo);
   
   // pcode에 따른 연차수 가져오기
   String selectOffCnt(String pcode);
   
   // 직원정보등록하기 ==> 추후 ajax이용할때, 코드 변경예정...
   int registerOne(MemberBwbVO mvo);
   
   // 등록한 직원의 fk_dcode를 통해 managerid 알아오기
   String selectMangerid(String dcode);
   
   // 부서명 가져오기
   String selectdname(String dcode);
   
   // 직위 가져오기
   String selectpname(String pcode);
   
   // 비밀번호 변경하기
   int updatePasswd(Map<String, String> paraMap);
   
   // employeeid를 가지고 주민번호 가져오기
   String selectJubun(String string);
   
   // 부장을 제외한 직원 ID를 가져오기
   List<MemberBwbVO> selectMemberList(String dcode);
   
   // 부서 전체 이름,코드 가져오기
   List<MemberBwbVO> selectDepartmentList();
   
   // 야근테이블에 insert하기
   int insertdoLate(Map<String, String> paraMap);
   
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
	List<String> wordSearch(String searchWord);
	
	// 검색어 입력 후 URL주소 뽑아오기
	String goSebuMenu(String searchWord);
	
	// 해당 검색어 tbl_word에 insert시켜주기
	void insertWord(String searchWord);
	
	// 고객여행일정 가지고오기
	List<Map<String, String>> selectScheduleList(String clientname);


}