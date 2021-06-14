package com.t1works.groupware.hsy.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.t1works.groupware.hsy.model.*;

@Component
@Service
public class MemberHsyService implements InterMemberHsyService {

	@Autowired 
	private InterMemberHsyDAO mdao;
	
	@Autowired 
	private InterDepartmentHsyDAO ddao;
	
	@Autowired 
	private InterTodoHsyDAO tdao;
	
	// 모든 부서에 대한 정보 가져오기
	@Override
	public List<DepartmentHsyVO> selectAllDepartment() {
		List<DepartmentHsyVO> departmentList= ddao.selectAllDepartment();
		return departmentList;
		
	} // end of public List<DepartmentHsyVO> selectAllDepartment() {----
	
	// 모든 직원 정보 가져오기
	@Override
	public List<MemberHsyVO> selectAllMember() {
		List<MemberHsyVO> employeeList= mdao.selectAllMember();
		return employeeList;
	} // end of public List<MemberHsyVO> selectAllMember() {----

	// 주소록 직원목록 총페이지
	@Override
	public int selectTotalPage(Map<String, String> paraMap) {

		int n=0;
		
		// 검색조건이 있는 경우
		if(paraMap.get("searchType")!=null && paraMap.get("searchWord")!=null && !("".equals(paraMap.get("searchType")) || "".equals(paraMap.get("searchWord")))) {
			n=mdao.selectTotalPage1(paraMap);
		}
		
		// 검색조건이 없는 경우
		else { 
			n=mdao.selectTotalPage2(paraMap.get("sizePerPage"));
		}
		
		return n;
	} // end of public int selectTotalPage(Map<String, String> paraMap) {-----

	// 페이징 직원목록 가져오기
	@Override
	public List<MemberHsyVO> selectPagingMember(Map<String, String> paraMap) {

		 List<MemberHsyVO> selectPagingMember=null;
		 
		 int currentShowPageNo= Integer.parseInt(paraMap.get("currentShowPageNo"));
		 int sizePerPage= Integer.parseInt(paraMap.get("sizePerPage"));
		 
		 String start=String.valueOf((currentShowPageNo * sizePerPage) - (sizePerPage - 1));
		 String end=String.valueOf((currentShowPageNo * sizePerPage));
		 
		 paraMap.put("start", start);
		 paraMap.put("end", end);
		 
		 Map<String, String> paraMap2= new HashMap<>();
		 paraMap2.put("start", start);
		 paraMap2.put("end", end);
		 
		// 검색조건이 있는 경우
		 if(paraMap.get("searchType")!=null && paraMap.get("searchWord")!=null && !("".equals(paraMap.get("searchType")) || "".equals(paraMap.get("searchWord")))) {
			selectPagingMember=mdao.selectPagingMember1(paraMap);
		}
		 
		// 검색조건이 없는 경우
		else { 
			selectPagingMember=mdao.selectPagingMember2(paraMap2);
		}
		
		return selectPagingMember;
	} // end of public List<MemberHsyVO> selectPagingMember(Map<String, String> paraMap) {----

	// 사번에 해당하는 직원정보 가져오기
	@Override
	public MemberHsyVO employeeInfoAjaxHsy(String employeeid) {

		MemberHsyVO mvo= mdao.employeeInfoAjaxHsy(employeeid);
		return mvo;
	} // end of public MemberHsyVO employeeInfoAjaxHsy(String employeeid) ---

	
	// 특정직원의 근태결재 승인처리 완료된 문서번호
	@Override
	public List<String> getAttendanceAno(String employeeid) {
		
		List<String> anoList= mdao.getAttendanceAno(employeeid);
		return anoList;
	} // end of public List<String> getAttendanceAno(String employeeid) {----

	
	// 월급명세서에 필요한 정보 가져오기 => 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수)
	@Override
	public Map<String, Integer> getAttendanceForSalary(Map<String, String> paraMap) {
		
		Map<String,Integer> attendanceMap= mdao.getAttendanceForSalary(paraMap);
		return attendanceMap;
	} // end of public Map<String, String> getAttendanceForSalary(Map<String, String> paraMap) {----
	
	
	// 해당 직원의 특정 년도, 월의  총 야근 시간 가져오기
	@Override
	public int getTotalLateWorkTime(Map<String, String> paraMap) {

		int totalLateWorkTime= mdao.getTotalLateWorkTime(paraMap);
		return totalLateWorkTime;
	} // end of public int getTotalLateWorkTime(Map<String, String> paraMap) {-----

	
	// 해당 년도, 월의 실적건수와 지난달의 실적건수 가져오기
	@Override
	public Map<String, String> getDoneCnt(Map<String, String> paraMap) {

		Map<String,String> doneCntMap= mdao.getDoneCnt(paraMap);
		return doneCntMap;
	
	} // end of public Map<String, String> getDoneCnt(Map<String, String> paraMap) {

	// 계층형 조직도를 가져오기
	@Override
	public List<MemberHsyVO> hierarchicalEmployeeList() {
		
		List<MemberHsyVO> mvoList= mdao.hierarchicalEmployeeList();
		
		for(MemberHsyVO mvo: mvoList) {
			
			// fk_pcode로 margin을 주기 위해서 기존[사장:4, 부장:3, 대리:2, 사원:1] 값을  [사장:1, 부장:2, 대리:3, 사원:4]로 수정
			if("4".equals(mvo.getFk_pcode()))      mvo.setFk_pcode("1");
			else if("3".equals(mvo.getFk_pcode())) mvo.setFk_pcode("2");
			else if("2".equals(mvo.getFk_pcode())) mvo.setFk_pcode("3");
			else mvo.setFk_pcode("4");
			
		}// end of for------------------
		
		return mvoList;
		
	}// end of public List<MemberHsyVO> hierarchicalEmployeeList() {--------

	// 처리 업무가 존재하는 날짜와 날짜별 처리 업무 수 가져오기
	@Override
	public List<Map<String, String>> getBonusDate(Map<String,String> paraMap) {

		List<Map<String,String>> bonusDateList= tdao.getBonusDate(paraMap);
		return bonusDateList;
		
	}// end of public List<Map<String, String>> getBonusDate(String employeeid) {----

	
	// 직급에 맞는 건당성과금 가져오기
	@Override
	public String getCommissionpercase(String employeeid) {
		String commissionpercase=ddao.getCommissionpercase(employeeid);
		return commissionpercase;
	} // end of public String getCommissionpercase(String employeeid) {------------

	
	// 야근수당 리스트에 보여줄 항목 가져오기 
	@Override
	public List<DoLateVO> getOverNightList(Map<String, String> paraMap) {
		
		List<DoLateVO> dlvoList= mdao.getOverNightList(paraMap);
		return dlvoList;
		
	} // end of public List<DoLateVO> getOverNightList(String employeeid) {------

	
	// 사번에 해당하는 직원의 오늘의 근태 정보 가져오기 => 현재  병가, 반차, 연차, 경조휴가, 출장 여부 표시
	@Override
	public Map<String,String> getAttendanceState(String employeeid) {
		Map<String,String> attendanceStateMap= mdao.getAttendanceState(employeeid);
		return attendanceStateMap;
	} // end of public List<String> getAttendanceState(String employeeid) {-------

	
	// 현재시간이 14시 이전인지 이후인지 알아오기
	@Override
	public int isTwoBefore() {

		int n= mdao.isTwoBefore(); 
		return n;
	} // end of public int isTwoBefore() {-------------------

	
	
	// 특정 직원의 읽지않은 메일수, 결재중인 문서 수, 14일 이내에 결재완료된 문서 수 가져오기
	@Override
	public Map<String, Integer> getquickMenuInfo(Map<String,String> paraMap) {
		Map<String,Integer> quickMenuInfoMap= mdao.getquickMenuInfo(paraMap);
		return quickMenuInfoMap;
	} // end of public Map<String, Integer> getquickMenuInfo(String employeeid) {-----
	 

	
	
	
	
}
