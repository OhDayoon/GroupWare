package com.t1works.groupware.sia.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class MyDocumentSiaDAO implements InterMyDocumentSiaDAO {

	@Resource
	private SqlSessionTemplate sqlsession4;
	
	// 내문서함 - 수신함 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_reclist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_reclist = sqlsession4.selectList("mydocument_sia.getnorm_reclist", paraMap);
		return norm_reclist;
	}
	
	// 검색에 해당하는 수신함 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getTotalPage", paraMap);
		return totalPage;
	}
	
	// 내문서함 - 수신함 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuNorm_detail", paraMap);
		return avo;
	}
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_reclist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_reclist = sqlsession4.selectList("mydocument_sia.getSpend_reclist", paraMap);
		return spend_reclist;
	}

	// 검색에 해당하는 수신함 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getSpendTotalPage", paraMap);
		return totalPage;
	}	
	
	// 내문서함 - 수신함 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuSpend_detail", paraMap);
		return avo;
	}
	
	/////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_reclist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_reclist = sqlsession4.selectList("mydocument_sia.getVacation_reclist", paraMap);
		return vacation_reclist;
	}

	// 검색에 해당하는 수신함 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getVacationTotalPage", paraMap);
		return totalPage;
	}

	// 내문서함 - 수신함 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuVacation_detail", paraMap);
		return avo;
	}

	/////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 결재의견 작성하기
	@Override
	public int addOpinion(ApprovalSiaVO avo) {
		int n = sqlsession4.insert("mydocument_sia.addOpinion", avo);
		return n;
	}

	// 내문서함 - 수신함 결재의견 조회하기
	@Override
	public List<ApprovalSiaVO> getOpinionList(String parentAno) {
		List<ApprovalSiaVO> avo = sqlsession4.selectList("mydocument_sia.getOpinionList", parentAno);
		return avo;
	}

	// 내문서함 - 수신함 - 결재버튼 클릭
	@Override
	public int approval(Map<String, String> paraMap) {
		int n = sqlsession4.update("mydocument_sia.approval", paraMap);
		return n;
	}
	// 결재로그 테이블에 insert 하기(승인)
	@Override
	public int approvalLog(Map<String, String> paraMap) {
		int n = sqlsession4.insert("mydocument_sia.approvalLog", paraMap);
		return n;
	}

	// 내문서함 - 수신함 - 반려버튼 클릭
	@Override
	public int reject(Map<String, String> paraMap) {
		int n = sqlsession4.update("mydocument_sia.reject", paraMap);
		return n;
	}
	// 결재로그 테이블에 insert 하기(반려)
	@Override
	public int rejectLog(Map<String, String> paraMap) {
		int n = sqlsession4.insert("mydocument_sia.rejectLog", paraMap);
		return n;
	}
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////	
	
	// 내문서함 - 발신함 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_sendlist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_sendlist = sqlsession4.selectList("mydocument_sia.getnorm_sendlist", paraMap);
		return norm_sendlist;
	}
	
	// 검색에 해당하는 발신함 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getNormSendTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getNormSendTotalPage", paraMap);
		return totalPage;
	}
	
	// 내문서함 - 발신함 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_send_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuNorm_send_detail", paraMap);
		return avo;
	}
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 발신함 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_sendlist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_sendlist = sqlsession4.selectList("mydocument_sia.getSpend_sendlist", paraMap);
		return spend_sendlist;
	}

	// 검색에 해당하는 발신함 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendSendTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getSpendSendTotalPage", paraMap);
		return totalPage;
	}	
	
	// 내문서함 - 발신함 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_send_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuSpend_send_detail", paraMap);
		return avo;
	}
	
	/////////////////////////////////////////////////////////////////////

	// 내문서함 - 발신함 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_sendlist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_sendlist = sqlsession4.selectList("mydocument_sia.getVacation_sendlist", paraMap);
		return vacation_sendlist;
	}

	// 검색에 해당하는 발신함 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationSendTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getVacationSendTotalPage", paraMap);
		return totalPage;
	}

	// 내문서함 - 발신함 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_send_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuVacation_send_detail", paraMap);
		return avo;
	}
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_templist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_templist = sqlsession4.selectList("mydocument_sia.getnorm_templist", paraMap);
		return norm_templist;
	}
	
	// 검색에 해당하는 임시저장함 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getNormTempTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getNormTempTotalPage", paraMap);
		return totalPage;
	}
	
	// 내문서함 - 임시저장함 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_temp_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuNorm_temp_detail", paraMap);
		return avo;
	}
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 임시저장함 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_templist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_templist = sqlsession4.selectList("mydocument_sia.getSpend_templist", paraMap);
		return spend_templist;
	}

	// 검색에 해당하는 임시저장함 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendTempTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getSpendTempTotalPage", paraMap);
		return totalPage;
	}	
	
	// 내문서함 - 임시저장함 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_temp_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuSpend_temp_detail", paraMap);
		return avo;
	}
	
	/////////////////////////////////////////////////////////////////////

	// 내문서함 - 임시저장함 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_templist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_templist = sqlsession4.selectList("mydocument_sia.getVacation_templist", paraMap);
		return vacation_templist;
	}

	// 검색에 해당하는 임시저장함 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationTempTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getVacationTempTotalPage", paraMap);
		return totalPage;
	}

	// 내문서함 - 임시저장함 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_temp_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuVacation_temp_detail", paraMap);
		return avo;
	}
	

	// 수신자 정보 찾기
	@Override
	public ApprovalSiaVO viewMng(Map<String, String> paraMap) {
		ApprovalSiaVO mng = sqlsession4.selectOne("mydocument_sia.viewMng", paraMap);
		return mng;
	}
	
	/////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 파일 삭제
	@Override
	public int removeFile(Map<String, String> paraMap) {
		int n = sqlsession4.update("mydocument_sia.removeFile", paraMap);
		return n;
	}	
	
	// 내문서함 - 임시저장함/발신함 - 삭제버튼 클릭
	@Override
	public int remove(Map<String, String> paraMap) {
		int n = sqlsession4.delete("mydocument_sia.remove", paraMap);
		return n;
	}
	
	// 문서번호에 따라 삭제해야할 파일 조회
	@Override
	public ApprovalSiaVO getViewFile(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.getViewFile", paraMap);
		return avo;
	}
	
	// 내문서함 - 임시저장함 - 저장버튼 클릭		
	// 전자결재 테이블 update 첨부파일 없는 경우 - 임시저장 상태
	@Override
	public int approvalSave(ApprovalSiaVO avo) {
		int n = sqlsession4.update("mydocument_sia.approvalSave", avo);
		return n;
	}
	// 전자결재 테이블 update 첨부파일 있는 경우 - 임시저장 상태
	@Override
	public int approvalSave_withFile(ApprovalSiaVO avo) {
		int n = sqlsession4.update("mydocument_sia.approvalSave_withFile", avo);		
		return n;
	}
	
	// 전자결재 테이블 update 첨부파일 없는 경우 - 일반결재 - 제출
	@Override
	public int approvalSubmit(ApprovalSiaVO avo) {
		int n = sqlsession4.update("mydocument_sia.approvalSubmit", avo);		
		return n;
	}
	// 전자결재 테이블 update 첨부파일 있는 경우 - 일반결재 - 제출
	@Override
	public int approvalSubmit_withFile(ApprovalSiaVO avo) {
		int n = sqlsession4.update("mydocument_sia.approvalSubmit_withFile", avo);
		return n;
	}

	// 문서 종류에 따라 테이블 update - 일반결재
	@Override
	public int optionSave(ApprovalSiaVO avo) {
		int result = sqlsession4.update("mydocument_sia.optionSave", avo);
		return result;
	}
	
	// 문서 종류에 따라 테이블 update - 지출결재
	@Override
	public int optionSaveSpend(ApprovalSiaVO avo) {
		int result = sqlsession4.update("mydocument_sia.optionSaveSpend", avo);
		return result;
	}
	
	// 문서 종류에 따라 테이블 update - 근태결재
	@Override
	public int optionSaveVacation(ApprovalSiaVO avo) {
		int result = sqlsession4.update("mydocument_sia.optionSaveVacation", avo);
		return result;
	}

	// 결재로그 테이블에 insert하기 (제출)
	@Override
	public int submitLog(Map<String, String> paraMap) {
		int n = sqlsession4.insert("mydocument_sia.submitLog", paraMap);
		return n;
	}
	
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 결재완료 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_completelist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_completelist = sqlsession4.selectList("mydocument_sia.getnorm_completelist", paraMap);
		return norm_completelist;
	}
	
	// 검색에 해당하는 수신함 - 결재완료 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getNormCompleteTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getNormCompleteTotalPage", paraMap);
		return totalPage;
	}
	
	// 내문서함 - 수신함 - 결재완료 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_complete_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuNorm_complete_detail", paraMap);
		return avo;
	}
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 결재완료 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_completelist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_completelist = sqlsession4.selectList("mydocument_sia.getSpend_completelist", paraMap);
		return spend_completelist;
	}

	// 검색에 해당하는 수신함 - 결재완료 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendCompleteTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getSpendCompleteTotalPage", paraMap);
		return totalPage;
	}	
	
	// 내문서함 - 수신함 - 결재완료 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_complete_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuSpend_complete_detail", paraMap);
		return avo;
	}
	
	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 결재완료 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_completelist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_completelist = sqlsession4.selectList("mydocument_sia.getVacation_completelist", paraMap);
		return vacation_completelist;
	}

	// 검색에 해당하는 수신함 - 결재완료 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationCompleteTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession4.selectOne("mydocument_sia.getVacationCompleteTotalPage", paraMap);
		return totalPage;
	}
	
	// 내문서함 - 수신함 - 결재완료 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_complete_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = sqlsession4.selectOne("mydocument_sia.myDocuVacation_complete_detail", paraMap);
		return avo;
	}

	
	// 결재의견 삭제하기
	@Override
	public int delMyOpinion(Map<String, String> paraMap) {
		int n = sqlsession4.delete("mydocument_sia.delMyOpinion", paraMap);
		return n;
	}

	// 결재로그 리스트보기
	@Override
	public List<ApprovalSiaVO> approvalLogList(String parentAno) {
		List<ApprovalSiaVO> avo = sqlsession4.selectList("mydocument_sia.approvalLogList", parentAno);
		return avo;
	}

	// 연차 반차 개수 복구하기
	@Override
	public int subtract(Map<String, String> paraMap) {
		int n = sqlsession4.update("mydocument_sia.subtract", paraMap);
		return n;
	}

	




	



	
	



	


}
