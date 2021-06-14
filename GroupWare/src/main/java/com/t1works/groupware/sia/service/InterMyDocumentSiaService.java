package com.t1works.groupware.sia.service;

import java.util.List;
import java.util.Map;

import com.t1works.groupware.sia.model.ApprovalSiaVO;

public interface InterMyDocumentSiaService {

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

	// 내문서함 - 수신함 - 근태결재 반려버튼 클릭
	int reject(Map<String, String> paraMap);
	
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
	
	// 내문서함 - 임시저장함/발신함 - 삭제버튼 클릭
	int remove(Map<String, String> paraMap);
	
	// 문서번호에 따라 삭제해야할 파일 조회
	ApprovalSiaVO getViewFile(Map<String, String> paraMap);
	
	// 내문서함 - 임시저장함 - 일반결재 - 저장버튼 클릭
	int save(ApprovalSiaVO avo) throws Throwable;
	int save_withFile(ApprovalSiaVO avo) throws Throwable;
	
	// 내문서함 - 임시저장함 - 일반결재 - 제출버튼 클릭
	int submit(ApprovalSiaVO avo) throws Throwable;
	int submit_withFile(ApprovalSiaVO avo) throws Throwable;
	
	// 내문서함 - 임시저장함 - 지출결재 - 저장버튼 클릭
	int saveSpend(ApprovalSiaVO avo) throws Throwable;
	int saveSpend_withFile(ApprovalSiaVO avo) throws Throwable;
	
	// 내문서함 - 임시저장함 - 지출결재 - 제출버튼 클릭
	int submitSpend(ApprovalSiaVO avo);
	int submitSpend_withFile(ApprovalSiaVO avo) throws Throwable;	
	
	// 내문서함 - 임시저장함 - 근태결재 - 저장버튼 클릭
	int saveVacation(ApprovalSiaVO avo) throws Throwable;
	int saveVacation_withFile(ApprovalSiaVO avo) throws Throwable;
	
	// 내문서함 - 임시저장함 - 근태결재 - 제출버튼 클릭
	int submitVacation(ApprovalSiaVO avo) throws Throwable;
	int submitVacation_withFile(ApprovalSiaVO avo) throws Throwable;
	
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

	

	

	


	
	

}
