package com.t1works.groupware.hsy.service;

import java.util.List;
import java.util.Map;

import com.t1works.groupware.hsy.model.DepartmentHsyVO;
import com.t1works.groupware.hsy.model.DoLateVO;
import com.t1works.groupware.hsy.model.MemberHsyVO;

public interface InterMemberHsyService {

	// 모든 부서에 대한 정보 가져오기
	List<DepartmentHsyVO> selectAllDepartment();

	// 모든 직원 정보 가져오기
	List<MemberHsyVO> selectAllMember();

	// 주소록 직원목록 총페이지
	int selectTotalPage(Map<String, String> paraMap);

	// 페이징 직원목록 가져오기
	List<MemberHsyVO> selectPagingMember(Map<String, String> paraMap);

	// 사번에 해당하는 직원정보 가져오기
	MemberHsyVO employeeInfoAjaxHsy(String employeeid);

	// 특정직원의 근태결재 승인처리 완료된 문서번호 
	List<String> getAttendanceAno(String employeeid);
	
	// 월급명세서에 필요한 정보 가져오기 => 근태내역 (연차/병가/지각/반차/경조휴가 사용 일 수)
	Map<String, Integer> getAttendanceForSalary(Map<String, String> paraMap);

	// 해당 직원의 특정 년도, 월의  총 야근 시간 가져오기
	int getTotalLateWorkTime(Map<String, String> paraMap);

	// 해당 년도, 월의 실적건수와 지난달의 실적건수 가져오기
	Map<String, String> getDoneCnt(Map<String, String> paraMap);

	// 계층형 조직도를 가져오기
	List<MemberHsyVO> hierarchicalEmployeeList();

	// 처리 업무가 존재하는 날짜와 날짜별 처리 업무 수 가져오기
	List<Map<String, String>> getBonusDate(Map<String, String> paraMap);

	// 직급에 맞는 건당성과금 가져오기
	String getCommissionpercase(String employeeid);

	// 야근수당 리스트에 보여줄 항목 가져오기 
	List<DoLateVO> getOverNightList(Map<String, String> paraMap);

	// 사번에 해당하는 직원의 오늘의 근태 정보 가져오기 => 현재  병가, 반차, 연차, 경조휴가, 출장 여부 표시
	Map<String,String> getAttendanceState(String employeeid);

	// 현재시간이 14시 이전인지 이후인지 알아오기
	int isTwoBefore();

	// 특정 직원의 읽지않은 메일수, 결재중인 문서 수, 14일 이내에 결재완료된 문서 수 가져오기
	Map<String, Integer> getquickMenuInfo(Map<String,String> paraMap);

	

}
