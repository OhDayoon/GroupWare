package com.t1works.groupware.hsy.model;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class MemberHsyDAO implements InterMemberHsyDAO {

	@Autowired 
	private SqlSessionTemplate sqlsession2;
	
	
	// 모든 직원 정보 가져오기
	@Override
	public List<MemberHsyVO> selectAllMember() {
		List<MemberHsyVO> employeeList= sqlsession2.selectList("memberHsy.selectAllMember");
		return employeeList;
	}

	// 검색조건이 있는 직원목록 총페이지
	@Override
	public int selectTotalPage1(Map<String, String> paraMap) {
		int n=sqlsession2.selectOne("memberHsy.selectTotalPage1",paraMap);
		return n;
	}

	// 검색조건이 없는 직원목록 총페이지
	@Override
	public int selectTotalPage2(String sizePerPage) {
		int n=sqlsession2.selectOne("memberHsy.selectTotalPage2", sizePerPage);
		return n;
	}

	// 검색조건이 있는 직원목록 가져오기 (페이징처리)
	@Override
	public List<MemberHsyVO> selectPagingMember1(Map<String, String> paraMap) {
		List<MemberHsyVO> selectPagingMember=sqlsession2.selectList("memberHsy.selectPagingMember1", paraMap);
		return selectPagingMember;
		
	}

	// 검색조건이 없는 직원목록 가져오기 (페이징처리)
	@Override
	public List<MemberHsyVO> selectPagingMember2(Map<String, String> paraMap2) {
		
		List<MemberHsyVO> selectPagingMember= sqlsession2.selectList("memberHsy.selectPagingMember2", paraMap2);
		return selectPagingMember;
	}

	// 사번에 해당하는 직원정보 가져오기
	@Override
	public MemberHsyVO employeeInfoAjaxHsy(String employeeid) {

		MemberHsyVO mvo= sqlsession2.selectOne("memberHsy.employeeInfoAjaxHsy", employeeid);
		return mvo;
	}

	
	// 특정직원의 근태결재 승인처리 완료된 문서번호
	@Override
	public List<String> getAttendanceAno(String employeeid) {
		
		List<String> anoList= sqlsession2.selectList("memberHsy.getAttendanceAno",employeeid);
		return anoList;
	} // end of public List<String> getAttendanceAno(String employeeid) {---

	
	// 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수) 가져오기
	@Override
	public Map<String, Integer> getAttendanceForSalary(Map<String, String> paraMap) {
		
		Map<String,Integer> attendanceMap= sqlsession2.selectOne("memberHsy.getAttendanceForSalary", paraMap);
		return attendanceMap;
	} // end of public Map<String, String> getAttendanceForSalary(Map<String, String> paraMap) {-----

	// 해당 직원의 특정 년도, 월의  총 야근 시간 가져오기
	@Override
	public int getTotalLateWorkTime(Map<String, String> paraMap) {
		
		int totalLateWorkTime= sqlsession2.selectOne("memberHsy.getTotalLateWorkTime",paraMap);
		return totalLateWorkTime;
	} // end of public int getTotalLateWorkTime(Map<String, String> paraMap) {----

	
	// 해당 년도, 월의 실적건수와 지난달의 실적건수 가져오기
	@Override
	public Map<String, String> getDoneCnt(Map<String, String> paraMap) {
		
		Map<String,String> doneCntMap= sqlsession2.selectOne("memberHsy.getDoneCnt",paraMap);
		return doneCntMap;
		
	} // end of public Map<String, String> getDoneCnt(Map<String, String> paraMap) {-----

	
	// 계층형 조직도를 가져오기
	@Override
	public List<MemberHsyVO> hierarchicalEmployeeList() {

		List<MemberHsyVO> mvoList= sqlsession2.selectList("memberHsy.hierarchicalEmployeeList");
		return mvoList;
	
	} // end of public List<MemberHsyVO> hierarchicalEmployeeList() {-----

	
	// 야근수당 리스트에 보여줄 항목 가져오기 
	@Override
	public List<DoLateVO> getOverNightList(Map<String,String> paraMap) {

		List<DoLateVO> dlvoList= sqlsession2.selectList("memberHsy.getOverNightList", paraMap);
		return dlvoList;
	} // end of public List<DoLateVO> getOverNightList(String employeeid) {-------

	
	// 사번에 해당하는 직원의 오늘의 근태 정보 가져오기 => 현재  병가, 반차, 연차, 경조휴가, 출장 여부 표시
	@Override
	public Map<String,String> getAttendanceState(String employeeid) {

		Map<String,String> attendanceStateMap= sqlsession2.selectOne("memberHsy.getAttendanceState", employeeid);
		return attendanceStateMap;
	} // end of public List<String> getAttendanceState(String employeeid) {------

	
	// 현재시간이 14시 이전인지 이후인지 알아오기
	@Override
	public int isTwoBefore() {
		int n= sqlsession2.selectOne("memberHsy.isTwoBefore");
		return n;
	} // end of public int isTwoBefore() {----------------

	
	// 특정 직원의 읽지않은 메일수, 결재중인 문서 수, 14일 이내에 결재완료된 문서 수 가져오기
	@Override
	public Map<String, Integer> getquickMenuInfo(Map<String,String> paraMap) {
		Map<String,Integer> quickMenuInfoMap= sqlsession2.selectOne("memberHsy.getquickMenuInfo", paraMap);
		return quickMenuInfoMap;
	} // end of public Map<String, Integer> getquickMenuInfo(String employeeid) {--------

	


}
