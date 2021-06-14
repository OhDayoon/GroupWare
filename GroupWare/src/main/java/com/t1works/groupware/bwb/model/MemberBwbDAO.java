package com.t1works.groupware.bwb.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;


//=== #32. DAO 선언 ===
@Repository
public class MemberBwbDAO implements InterMemberBwbDAO {
   
   @Resource
   private SqlSessionTemplate sqlsession;
   
   
   @Override
   public MemberBwbVO selectMember(Map<String, String> paraMap) {
      
      MemberBwbVO mvo = sqlsession.selectOne("memberBwb.selectMember", paraMap);
      
      return mvo;
   }


   @Override
   public int insertlogin_history(Map<String, String> paraMap) {
	   
	  int n = sqlsession.insert("memberBwb.insertlogin_history", paraMap);
	   
	  return n;
   }

   // 출퇴근기록 테이블에 insert하기(출근시간)
   @Override
   public int insertIntime(Map<String, String> paraMap) {
	   
	   int n = sqlsession.insert("memberBwb.insertIntime", paraMap);
	   return n;
   }

   // 출퇴근기록 테이블에 select하기(출근시간)
   @Override
   public String selectIntime(Map<String, String> paraMap) {
	   
	   String intime = sqlsession.selectOne("memberBwb.selectIntime", paraMap);
	   return intime;
   }

   // 지각여부 판단하기(update)
	@Override
	public void updatelateno(Map<String, String> paraMap) {
		
		sqlsession.update("memberBwb.updateLateno", paraMap);

	}

	// 지각여부 판단하기(selects)	
	@Override
	public String selectlateno(String intime) {
		
		String latenoTime = sqlsession.selectOne("memberBwb.selectlateno", intime);
		return latenoTime;
	}

	// 출퇴근기록 테이블에 update하기(퇴근시간)
	@Override
	public int updateOuttime(Map<String, String> paraMap) {
		
		int n = sqlsession.update("memberBwb.updateOuttime", paraMap);
	    return n;
	}

	// 출퇴근테이블에서 select하기(퇴근시간)
	@Override
	public String selectOuttime(Map<String, String> paraMap) {
		
		String outtime = sqlsession.selectOne("memberBwb.selectOuttime", paraMap);
		
	    return outtime;
	}

	// 모든직위 가져오기
	@Override
	public List<MemberBwbVO> selectPositionList() {
		
		List<MemberBwbVO> selectPositionList = sqlsession.selectList("memberBwb.selectPositionList");
		return selectPositionList;
	}

	
	// 소속에 따른 직무설명 가져오기
	@Override
	public String selectDuty(String dname) {

		String duty = sqlsession.selectOne("memberBwb.selectDuty", dname);
		return duty;
	}

	
	// pname과 dname을 통해 pcode,dcode 가져오기.
	@Override
	public Map<String, String> selectPDcode(Map<String, String> paraMap) {
		
		Map<String, String> PDMap = sqlsession.selectOne("memberBwb.selectPDcode", paraMap);
		
		return PDMap;
	}

	// 회원정보 업데이트하기
	@Override
	public int updateOneInfo(MemberBwbVO mvo) {
		
		int n = sqlsession.update("memberBwb.updateOneInfo", mvo);
		
		return n;
	}

	
	// pcode에 따른 연차수 가져오기
	@Override
	public String selectOffCnt(String pcode) {
		
		String offcnt = sqlsession.selectOne("memberBwb.selectOffCnt", pcode);
		
		return offcnt;
	}
	
	
	// 직원정보등록하기 ==> 추후 ajax이용할때, 코드 변경예정...
	@Override
	public int registerOne(MemberBwbVO mvo) {
		
		int n = sqlsession.insert("memberBwb.registerOne", mvo);
		return n;
	}
	
	
	// 등록한 직원의 fk_dcode를 통해 managerid 알아오기
	@Override
	public String selectMangerid(String dcode) {
		
		String managerid = sqlsession.selectOne("memberBwb.selectMangerid", dcode);
		return managerid;
	}

	// 부서명가져오기
	@Override
	public String selectdname(String dcode) {
		
		String dname = sqlsession.selectOne("memberBwb.selectdname", dcode);
		return dname;
		
	}

	// 직위가져오기
	@Override
	public String selectpname(String pcode) {
		
		String pname = sqlsession.selectOne("memberBwb.selectpname", pcode);
		return pname;
	}

	
	// 비밀번호 변경하기
	@Override
	public int updatePasswd(Map<String, String> paraMap) {
		
		int n = sqlsession.update("memberBwb.updatePasswd", paraMap);
		return n;
	}

	// employeeid를 가지고 주민번호 가져오기
	@Override
	public String selectJubun(String string) {
		
		String jubun = sqlsession.selectOne("memberBwb.selectJubun", string);
		return jubun;
	}

	// 부장을 제외한 직원 ID를 가져오기
	@Override
	public List<MemberBwbVO> selectMemberList(String dcode) {
		
		List<MemberBwbVO> memberList = sqlsession.selectList("memberBwb.selectMemberList", dcode);
		return memberList;
	}

	// 부서 전체 이름,코드 가져오기
	@Override
	public List<MemberBwbVO> selectDepartmentList() {
		
		List<MemberBwbVO> departmentList = sqlsession.selectList("memberBwb.selectDepartmentList");
		return departmentList;
	}

	// 야근테이블에 insert하기
	@Override
	public int insertdoLate(Map<String, String> paraMap) {
		
		int n = sqlsession.insert("memberBwb.insertdoLate", paraMap);
		return n;
		
	}

	// 이용자의 총 연차수 가지고 오기
	@Override
	public String selectTotaloffCnt(String pcode) {
		
		String totalOffCnt = sqlsession.selectOne("memberBwb.selectTotaloffCnt", pcode);
		return totalOffCnt;
	}

	// 이용자의 사용연차수 가지고 오기
	@Override
	public String selectUseoffCnt(String fk_employeeid) {
		
		String useOffCnt = sqlsession.selectOne("memberBwb.selectUseoffCnt", fk_employeeid);
		return useOffCnt;
	}

	 // 나의 월별 출퇴근기록 가지고오기
	@Override
	public List<Map<String, String>> selectmyMonthIndolence(String fk_employeeid) {
		
		List<Map<String, String>> myIndolenceList = sqlsession.selectList("memberBwb.selectmyMonthIndolence", fk_employeeid);
		return myIndolenceList;
	}
	
	
	// 부서 월별 출퇴근기록 가지고오기
	@Override
	public List<Map<String, String>> selectDepMonthIndolence(String fk_dcode) {
		List<Map<String,String>> depIndolenceList = sqlsession.selectList("memberBwb.selectDepMonthIndolence", fk_dcode);
		return depIndolenceList;
	}

	// 여행상품들의 일정 뽑아오기
	@Override
	public List<Map<String, String>> productSchedule() {
		List<Map<String, String>> productList = sqlsession.selectList("memberBwb.productSchedule");
		return productList;
	}

	
	// 이용자의 최근1주일 근로시간 가지고오기
	@Override
	public Map<String, String> selecthourMap(Map<String, String> searchMap) {
		
		Map<String, String> hourMap = sqlsession.selectOne("memberBwb.selecthourMap", searchMap);
		return hourMap;
	}
	
	
	// 일주일치 날짜 가지고오기
	@Override
	public Map<String, String> selectWeekDay(String today) {
		Map<String, String> weekMap = sqlsession.selectOne("memberBwb.selectWeekDay", today);
		return weekMap;
	}

	// word-cloud 차트를 위해 데이터 뽑아오기
	@Override
	public List<String> selectWordList() {
		List<String> wordList = sqlsession.selectList("memberBwb.selectWordList");
		
		return wordList;
	}

	
	// 검색어 입력 시 자동검색기능(ajax처리)
	@Override
	public List<String> wordSearch(String searchWord) {
		
		List<String> wordList = sqlsession.selectList("memberBwb.wordSearch", searchWord);
		return wordList;
	}

	// 검색어 입력 후 URL주소 뽑아오기
	@Override
	public String goSebuMenu(String searchWord) {
		String sebuAddress = sqlsession.selectOne("memberBwb.goSebuMenu", searchWord);
		return sebuAddress;
	}

	
	// 해당 검색어 tbl_word에 insert시켜주기
	@Override
	public void insertWord(String searchWord) {
		sqlsession.insert("memberBwb.insertWord", searchWord);
	}

	
	// 고객여행일정 가지고오기
	@Override
	public List<Map<String, String>> selectScheduleList(String clientname) {
		List<Map<String, String>> scheduleList = sqlsession.selectList("memberBwb.selectScheduleList", clientname);
		return scheduleList;
	}
	
	
   
   
   
   
}