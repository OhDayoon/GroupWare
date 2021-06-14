package com.t1works.groupware.sia.model;

import java.util.List;
import java.util.Map;

public interface InterMyDocumentSiaDAO {

	// 내문서함 - 수신함 - 일반결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getnorm_reclist(Map<String, String> paraMap);

	// 검색에 해당하는 수신함 - 일반결재 글의 총 페이지수를 알아오기
	int getTotalPage(Map<String, String> paraMap);

	// 내문서함 - 수신함 - 일반결재문서 문서 한 개 상세보기
	ApprovalSiaVO myDocuNorm_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 지출결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getSpend_reclist(Map<String, String> paraMap);

	// 검색에 해당하는 수신함 - 지출결재 글의 총 페이지수를 알아오기
	int getSpendTotalPage(Map<String, String> paraMap);

	// 내문서함 - 수신함 - 지출 결재 문서 한 개 상세보기
	ApprovalSiaVO myDocuSpend_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 근태결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getVacation_reclist(Map<String, String> paraMap);

	// 검색에 해당하는 수신함 - 근태결재 글의 총 페이지수를 알아오기
	int getVacationTotalPage(Map<String, String> paraMap);

	// 내문서함 - 수신함 - 근태결재문서 한 개 상세보기
	ApprovalSiaVO myDocuVacation_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////	

	// 내문서함 - 수신함 - 결재의견 작성하기
	int addOpinion(ApprovalSiaVO avo);

	// 내문서함 - 수신함 - 결재의견 조회하기
	List<ApprovalSiaVO> getOpinionList(String parentAno);

	// 내문서함 - 수신함 - 결재버튼 클릭
	int approval(Map<String, String> paraMap);
	// 결재로그 테이블에 insert 하기(승인)
	int approvalLog(Map<String, String> paraMap);
	
	// 내문서함 - 수신함 - 반려버튼 클릭
	int reject(Map<String, String> paraMap);	
	// 결재로그 테이블에 insert 하기(반려)
	int rejectLog(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 발신함 - 일반결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getnorm_sendlist(Map<String, String> paraMap);

	// 검색에 해당하는 발신함 - 일반결재 글의 총 페이지수를 알아오기
	int getNormSendTotalPage(Map<String, String> paraMap);

	// 내문서함 - 발신함 - 일반결재문서 문서 한 개 상세보기
	ApprovalSiaVO myDocuNorm_send_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 발신함 - 지출결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getSpend_sendlist(Map<String, String> paraMap);

	// 검색에 해당하는 발신함 - 지출결재 글의 총 페이지수를 알아오기
	int getSpendSendTotalPage(Map<String, String> paraMap);

	// 내문서함 - 발신함 - 지출 결재 문서 한 개 상세보기
	ApprovalSiaVO myDocuSpend_send_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 발신함 - 근태결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getVacation_sendlist(Map<String, String> paraMap);

	// 검색에 해당하는 발신함 - 근태결재 글의 총 페이지수를 알아오기
	int getVacationSendTotalPage(Map<String, String> paraMap);

	// 내문서함 - 발신함 - 근태결재문서 한 개 상세보기
	ApprovalSiaVO myDocuVacation_send_detail(Map<String, String> paraMap);

	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 일반결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getnorm_templist(Map<String, String> paraMap);

	// 검색에 해당하는 임시저장함 - 일반결재 글의 총 페이지수를 알아오기
	int getNormTempTotalPage(Map<String, String> paraMap);

	// 내문서함 - 임시저장함 - 일반결재문서 문서 한 개 상세보기
	ApprovalSiaVO myDocuNorm_temp_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 임시저장함 - 지출결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getSpend_templist(Map<String, String> paraMap);

	// 검색에 해당하는 임시저장함 - 지출결재 글의 총 페이지수를 알아오기
	int getSpendTempTotalPage(Map<String, String> paraMap);

	// 내문서함 - 임시저장함 - 지출 결재 문서 한 개 상세보기
	ApprovalSiaVO myDocuSpend_temp_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 근태결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getVacation_templist(Map<String, String> paraMap);

	// 검색에 해당하는 임시저장함 - 근태결재 글의 총 페이지수를 알아오기
	int getVacationTempTotalPage(Map<String, String> paraMap);

	// 내문서함 - 임시저장함 - 근태결재문서 한 개 상세보기
	ApprovalSiaVO myDocuVacation_temp_detail(Map<String, String> paraMap);

	
	// 수신자 정보 찾기
	ApprovalSiaVO viewMng(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 파일 삭제
	int removeFile(Map<String, String> paraMap);
	
	// 내문서함 - 임시저장함 - 삭제버튼 클릭
	int remove(Map<String, String> paraMap);
	
	// 문서번호에 따라 삭제해야할 파일 조회
	ApprovalSiaVO getViewFile(Map<String, String> paraMap);
	
	// 내문서함 - 임시저장함 - 일반결재 - 저장버튼 클릭
	int approvalSave(ApprovalSiaVO avo); // 첨부파일 없는 경우 저장
	int approvalSave_withFile(ApprovalSiaVO avo); // 첨부파일 있는 경우 저장
	
	int optionSave(ApprovalSiaVO avo); // 일반결재 문서 종류에 따라 테이블 update
	
	// 내문서함 - 임시저장함 - 일반결재 - 제출버튼 클릭
	int approvalSubmit(ApprovalSiaVO avo); // 첨부파일 없는 경우 저장
	int approvalSubmit_withFile(ApprovalSiaVO avo); // 첨부파일 있는 경우 저장
	
	int optionSaveSpend(ApprovalSiaVO avo); // 지출결재 문서 종류에 따라 테이블 update

	int optionSaveVacation(ApprovalSiaVO avo); // 근태결재 문서 종류에 따라 테이블 update	
	
	// 결재로그 테이블에 insert하기 (제출)
	int submitLog(Map<String, String> paraMap);

	//////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 결재완료 - 일반결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getnorm_completelist(Map<String, String> paraMap);

	// 검색에 해당하는 수신함 - 결재완료 - 일반결재 글의 총 페이지수를 알아오기
	int getNormCompleteTotalPage(Map<String, String> paraMap);

	// 내문서함 - 수신함 - 결재완료 - 일반결재문서 문서 한 개 상세보기
	ApprovalSiaVO myDocuNorm_complete_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 결재완료 - 지출결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getSpend_completelist(Map<String, String> paraMap);

	// 검색에 해당하는 수신함 - 결재완료 - 지출결재 글의 총 페이지수를 알아오기
	int getSpendCompleteTotalPage(Map<String, String> paraMap);

	// 내문서함 - 수신함 - 결재완료 - 지출 결재 문서 한 개 상세보기
	ApprovalSiaVO myDocuSpend_complete_detail(Map<String, String> paraMap);
	
	//////////////////////////////////////////////////////////////////////	

	// 내문서함 - 수신함 - 결재완료 - 근태결재문서에 해당하는 문서 조회하기
	List<ApprovalSiaVO> getVacation_completelist(Map<String, String> paraMap);

	// 검색에 해당하는 수신함 - 결재완료 - 근태결재 글의 총 페이지수를 알아오기
	int getVacationCompleteTotalPage(Map<String, String> paraMap);
	
	// 내문서함 - 수신함 - 결재완료 - 근태결재문서 한 개 상세보기
	ApprovalSiaVO myDocuVacation_complete_detail(Map<String, String> paraMap);

	// 결재의견 삭제하기
	int delMyOpinion(Map<String, String> paraMap);

	// 결재로그 리스트보기
	List<ApprovalSiaVO> approvalLogList(String parentAno);

	// 연차 반차 개수 복구하기
	int subtract(Map<String, String> paraMap);
	

	
	

	

	

	
	
	

	
	

}
