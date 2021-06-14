package com.t1works.groupware.sia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.t1works.groupware.common.FileManager;
import com.t1works.groupware.sia.model.ApprovalSiaVO;
import com.t1works.groupware.sia.model.InterMyDocumentSiaDAO;

@Component
@Service
public class MyDocumentSiaService implements InterMyDocumentSiaService {
	
	@Autowired
	private InterMyDocumentSiaDAO dao;
	
	@Autowired
	private FileManager fileManager;
	
	// 내문서함 - 수신함 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_reclist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_reclist = dao.getnorm_reclist(paraMap);
		return norm_reclist;
	}
	
	// 검색에 해당하는 수신함 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 수신함 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuNorm_detail(paraMap);
		return avo;
	}

	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_reclist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_reclist = dao.getSpend_reclist(paraMap);
		return spend_reclist;
	}


	// 검색에 해당하는 수신함 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getSpendTotalPage(paraMap);
		return totalPage;
	}
		
	// 내문서함 - 수신함 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuSpend_detail(paraMap);
		return avo;		
	}

	/////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_reclist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_reclist = dao.getVacation_reclist(paraMap);
		return vacation_reclist;
	}

	// 검색에 해당하는 수신함 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getVacationTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 수신함 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuVacation_detail(paraMap);
		return avo;
	}
	
	/////////////////////////////////////////////////////////////////////	

	// 내문서함 - 수신함 결재의견 작성하기
	@Override
	public int addOpinion(ApprovalSiaVO avo) {
		int n = dao.addOpinion(avo);
		return n;
	}

	// 내문서함 - 수신함 결재의견 조회하기
	@Override
	public List<ApprovalSiaVO> getOpinionList(String parentAno) {
		List<ApprovalSiaVO> avo = dao.getOpinionList(parentAno);
		return avo;
	}
	
	// 내문서함 - 수신함 결재버튼 클릭
	@Override
	public int approval(Map<String, String> paraMap) {
		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approval(paraMap);
		
		if(n == 1) {
			// 결재로그 테이블 insert(승인)
			result = dao.approvalLog(paraMap);
		}
		return result;
	}

	// 내문서함 - 수신함 반려버튼 클릭
	@Override
	public int reject(Map<String, String> paraMap) {
		int n = 0, result = 0; 
		
		// 전자결재 테이블 update
		n = dao.reject(paraMap);
		
		if(n == 1) {
			// 결재로그 테이블 insert(반려)
			result = dao.rejectLog(paraMap);
		}
		
		if("반차".equals(paraMap.get("vcatname")) || "연차".equals(paraMap.get("vcatname"))) {
			result = dao.subtract(paraMap);
		}
		
		return result;
	}

	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 발신함 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_sendlist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_sendlist = dao.getnorm_sendlist(paraMap);
		return norm_sendlist;
	}
	
	// 검색에 해당하는 발신함 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getNormSendTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getNormSendTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 발신함 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_send_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuNorm_send_detail(paraMap);
		return avo;
	}

	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 발신함 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_sendlist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_sendlist = dao.getSpend_sendlist(paraMap);
		return spend_sendlist;
	}

	// 검색에 해당하는 발신함 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendSendTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getSpendSendTotalPage(paraMap);
		return totalPage;
	}
		
	// 내문서함 - 발신함 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_send_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuSpend_send_detail(paraMap);
		return avo;		
	}

	/////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 발신함 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_sendlist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_sendlist = dao.getVacation_sendlist(paraMap);
		return vacation_sendlist;
	}

	// 검색에 해당하는 발신함 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationSendTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getVacationSendTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 발신함 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_send_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuVacation_send_detail(paraMap);
		return avo;
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_templist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_templist = dao.getnorm_templist(paraMap);
		return norm_templist;
	}
	
	// 검색에 해당하는 임시저장함 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getNormTempTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getNormTempTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 임시저장함 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_temp_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuNorm_temp_detail(paraMap);
		return avo;
	}

	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 임시저장함 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_templist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_templist = dao.getSpend_templist(paraMap);
		return spend_templist;
	}

	// 검색에 해당하는 임시저장함 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendTempTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getSpendTempTotalPage(paraMap);
		return totalPage;
	}
		
	// 내문서함 - 임시저장함 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_temp_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuSpend_temp_detail(paraMap);
		return avo;		
	}

	/////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_templist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_templist = dao.getVacation_templist(paraMap);
		return vacation_templist;
	}

	// 검색에 해당하는 임시저장함 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationTempTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getVacationTempTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 임시저장함 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_temp_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuVacation_temp_detail(paraMap);
		return avo;
	}
	
	// 수신자 정보 찾기
	@Override
	public ApprovalSiaVO viewMng(Map<String, String> paraMap) {
		ApprovalSiaVO mng = dao.viewMng(paraMap);
		return mng;
	}
	
	////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 임시저장함 - 파일 삭제	
	@Override
	public int removeFile(Map<String, String> paraMap) {
		int n = dao.removeFile(paraMap);
				
		if(n == 1) {
			String fileName = paraMap.get("fileName");
			String path = paraMap.get("path");
			
			if(fileName != null && !"".equals(fileName)) {
				try {
					fileManager.doFileDelete(fileName, path);
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}		
		}
		
		return n;
	}	
	
	// 내문서함 - 임시저장함 - 삭제버튼 클릭
	@Override
	public int remove(Map<String, String> paraMap) {
		int n = dao.remove(paraMap);
		
		if(n == 1) {
			String fileName = paraMap.get("fileName");
			String path = paraMap.get("path");
			
			if(fileName != null && !"".equals(fileName)) {
				try {
					fileManager.doFileDelete(fileName, path);
				} catch (Exception e) {					
					e.printStackTrace();
				}
			}
		}
		return n;
	}	

	// 문서번호에 따라 삭제해야할 파일 조회
	@Override
	public ApprovalSiaVO getViewFile(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.getViewFile(paraMap);
		return avo;
	}
	
	// 내문서함 - 임시저장함 - 일반결재 - 저장버튼 클릭
	@Override
	public int save(ApprovalSiaVO avo) throws Throwable {
		// 첨부파일이 없는 경우
		
		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSave(avo);
		
		if(n == 1) {
			// 문서 종류에 따라 테이블 update			
			result = dao.optionSave(avo);
		}		
		return result;
	}
	@Override
	public int save_withFile(ApprovalSiaVO avo) {
		// 첨부파일이 있는 경우
		
		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSave_withFile(avo);
		
		if(n == 1) {			
			// 문서 종류에 따라 테이블 update			
			result = dao.optionSave(avo);
		}		
		return result;
	}	
	
	// 내문서함 - 임시저장함 - 일반결재 - 제출버튼 클릭
	@Override
	public int submit(ApprovalSiaVO avo) throws Throwable {
		// 첨부파일이 없는 경우
		
		int n = 0, m = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSubmit(avo);
		
		if(n == 1) {
			// 문서 종류에 따라 테이블 update			
			m = dao.optionSave(avo);
		}
		
		if(m == 1) {
			
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", avo.getFk_employeeid());			
			paraMap.put("ano", String.valueOf(avo.getAno()));			
			
			result = dao.submitLog(paraMap);
		}
		
		System.out.println("확인용 : " + avo.getMdate());
		
		return result;
	}
	@Override
	public int submit_withFile(ApprovalSiaVO avo) {
		// 첨부파일이 있는 경우
		
		int n = 0, m = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSubmit_withFile(avo);
		
		if(n == 1) {			
			// 문서 종류에 따라 테이블 update			
			m = dao.optionSave(avo);
		}
		
		if(m == 1) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", avo.getFk_employeeid());			
			paraMap.put("ano", String.valueOf(avo.getAno()));			
			
			result = dao.submitLog(paraMap);
		}
		
		return result;
	}
	
	
	// 내문서함 - 임시저장함 - 지출결재 - 저장버튼 클릭
	@Override
	public int saveSpend(ApprovalSiaVO avo) throws Throwable {
		// 첨부파일이 없는 경우
		
		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSave(avo);
		
		if(n == 1) {
			// 문서 종류에 따라 테이블 update			
			result = dao.optionSaveSpend(avo);
		}		
		return result;
	}
	@Override
	public int saveSpend_withFile(ApprovalSiaVO avo) {
		// 첨부파일이 있는 경우

		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSave_withFile(avo);
		
		if(n == 1) {			
			// 문서 종류에 따라 테이블 update			
			result = dao.optionSaveSpend(avo);
		}
		
		return result;		
	}
	
	// 내문서함 - 임시저장함 - 지출결재 - 제출버튼 클릭
	@Override
	public int submitSpend(ApprovalSiaVO avo) {
		// 첨부파일이 없는 경우
		
		int n = 0, m = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSubmit(avo);
		
		if(n == 1) {
			// 문서 종류에 따라 테이블 update			
			m = dao.optionSaveSpend(avo);
		}
		
		if(m == 1) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", avo.getFk_employeeid());			
			paraMap.put("ano", String.valueOf(avo.getAno()));			
			
			result = dao.submitLog(paraMap);
		}
		
		return result;
	}
	@Override
	public int submitSpend_withFile(ApprovalSiaVO avo) {
		// 첨부파일이 있는 경우
		
		int n = 0, m = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSubmit_withFile(avo);
		
		if(n == 1) {			
			// 문서 종류에 따라 테이블 update			
			m = dao.optionSaveSpend(avo);
		}
		
		if(m == 1) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", avo.getFk_employeeid());			
			paraMap.put("ano", String.valueOf(avo.getAno()));			
			
			result = dao.submitLog(paraMap);
		}
		
		return result;
	}
	
	
	// 내문서함 - 임시저장함 - 근태결재 - 저장버튼 클릭
	@Override
	public int saveVacation(ApprovalSiaVO avo) throws Throwable {
		// 첨부파일이 없는 경우
		
		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSave(avo);
		
		if(n == 1) {
			// 문서 종류에 따라 테이블 update			
			result = dao.optionSaveVacation(avo);
		}		
		return result;
	}

	@Override
	public int saveVacation_withFile(ApprovalSiaVO avo) throws Throwable {
		// 첨부파일이 있는 경우
		
		int n = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSave_withFile(avo);
		
		if(n == 1) {			
			// 문서 종류에 따라 테이블 update			
			result = dao.optionSaveVacation(avo);
		}		
		return result;
	}
	
	// 내문서함 - 임시저장함 - 근태결재 - 제출버튼 클릭
	@Override
	public int submitVacation(ApprovalSiaVO avo) {
		// 첨부파일이 없는 경우
		
		int n = 0, m = 0,result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSubmit(avo);
		
		if(n == 1) {
			// 문서 종류에 따라 테이블 update			
			m = dao.optionSaveVacation(avo);
		}
		
		if(m == 1) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", avo.getFk_employeeid());			
			paraMap.put("ano", String.valueOf(avo.getAno()));			
			
			result = dao.submitLog(paraMap);
		}
		
		return result;
	}
	@Override
	public int submitVacation_withFile(ApprovalSiaVO avo) {
		// 첨부파일이 있는 경우
		
		int n = 0, m = 0, result = 0;
		
		// 전자결재 테이블 update
		n = dao.approvalSubmit_withFile(avo);
		
		if(n == 1) {			
			// 문서 종류에 따라 테이블 update			
			m = dao.optionSaveVacation(avo);
		}
		
		if(m == 1) {
			Map<String, String> paraMap = new HashMap<>();
			paraMap.put("userid", avo.getFk_employeeid());			
			paraMap.put("ano", String.valueOf(avo.getAno()));			
			
			result = dao.submitLog(paraMap);
		}
		
		return result;
	}
	
	////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 수신함 - 결재완료 - 일반결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getnorm_completelist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> norm_completelist = dao.getnorm_completelist(paraMap);
		return norm_completelist;
	}
	
	// 검색에 해당하는 수신함 - 결재완료 - 일반결재 글의 총 페이지수를 알아오기
	@Override
	public int getNormCompleteTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getNormCompleteTotalPage(paraMap);
		return totalPage;
	}

	// 내문서함 - 수신함 - 결재완료 - 일반결재문서 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuNorm_complete_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuNorm_complete_detail(paraMap);
		return avo;
	}

	//////////////////////////////////////////////////////////////////////

	// 내문서함 - 수신함 - 결재완료 - 지출결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getSpend_completelist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> spend_completelist = dao.getSpend_completelist(paraMap);
		return spend_completelist;
	}

	// 검색에 해당하는 수신함 - 결재완료 - 지출결재 글의 총 페이지수를 알아오기
	@Override
	public int getSpendCompleteTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getSpendCompleteTotalPage(paraMap);
		return totalPage;
	}
		
	// 내문서함 - 수신함 - 결재완료 - 지출 결재 문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuSpend_complete_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuSpend_complete_detail(paraMap);
		return avo;		
	}
	
	//////////////////////////////////////////////////////////////////////
	
	// 내문서함 - 결재완료 - 근태결재문서에 해당하는 문서 조회하기
	@Override
	public List<ApprovalSiaVO> getVacation_completelist(Map<String, String> paraMap) {
		List<ApprovalSiaVO> vacation_completelist = dao.getVacation_completelist(paraMap);
		return vacation_completelist;
	}

	// 검색에 해당하는 결재완료 - 근태결재 글의 총 페이지수를 알아오기
	@Override
	public int getVacationCompleteTotalPage(Map<String, String> paraMap) {
		int totalPage = dao.getVacationCompleteTotalPage(paraMap);
		return totalPage;
	}
	
	// 내문서함 - 결재완료 - 근태결재문서 한 개 상세보기
	@Override
	public ApprovalSiaVO myDocuVacation_complete_detail(Map<String, String> paraMap) {
		ApprovalSiaVO avo = dao.myDocuVacation_complete_detail(paraMap);
		return avo;
	}

	
	// 결재의견 삭제하기
	@Override
	public int delMyOpinion(Map<String, String> paraMap) {
		int n = dao.delMyOpinion(paraMap);
		return n;
	}

	
	// 결재로그 리스트보기
	@Override
	public List<ApprovalSiaVO> approvalLogList(String parentAno) {
		List<ApprovalSiaVO> avo = dao.approvalLogList(parentAno);
		return avo;
	}

	

	


	

	

}
