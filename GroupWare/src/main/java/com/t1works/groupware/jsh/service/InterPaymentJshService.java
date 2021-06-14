package com.t1works.groupware.jsh.service;


import java.util.*;

import com.t1works.groupware.jsh.model.ElectronPayJshVO;

public interface InterPaymentJshService {
	
	
	
	///////////////////////////////////////////////////////////////////////////////////
	//공통항목
	
	
	// 하나의 문서보기에서 결재로그 보여주기
	List<ElectronPayJshVO> oneLogList(Map<String, String> paraMap);


	// 하나의 문서 수신자정보 받아오기
	ElectronPayJshVO receiver(Map<String, String> paraMap);
   
	
	
	
	
	
	/////////////////////////////////////////////////////////////////////////////////
	

	// 일반결재내역 목록 보여주기
	List<ElectronPayJshVO> generalPayment_List();

	// 일반결재내역 페이징 처리를 안한 검색어가 있는 전체 글목록 보여주기 == //
	List<ElectronPayJshVO> electronListSearch(Map<String, String> paraMap);

	//검색어 입력시 자동글 완성하기
	List<String> wordSearchShow(Map<String, String> paraMap);

	// 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다.
	int getTotalCount(Map<String, String> paraMap);

	 //페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
	List<ElectronPayJshVO> electronListSearchWithPaging(Map<String, String> paraMap);

	// 하나의 일반결재내역 문서 보여주기
	ElectronPayJshVO generalOneView(Map<String, String> paraMap);

	//하나의 일반결재내역에서 결재의견목록 보여주기
	List<ElectronPayJshVO> oneOpinionList(Map<String, String> paraMap);

	// 글쓰기페이지 요청
	ElectronPayJshVO WriteJsh(Map<String, String> paraMap);
	// 수신자 정보 select해오기
	ElectronPayJshVO mWriteJsh(HashMap<String, String> paraMap);
	
	
	// 일반결재 문서insert(파일첨부 X)
	int addPayment(ElectronPayJshVO epvo) throws Throwable;
	// 첨부파일 있는 경우 글쓰기
	int addPayment_withFile(ElectronPayJshVO epvo) throws Throwable;

	//임시저장함 insert-첨부파일X
	int savePayment(ElectronPayJshVO epvo) throws Throwable;
	//임시저장함 insert-첨부파일O
	int savePayment_withFile(ElectronPayJshVO epvo) throws Throwable;

	
	
	
	
	/////////////////////////////////////////////////////////////////////////////
	////지출결재
	
	//페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
	List<ElectronPayJshVO> expListSearchWithPaging(Map<String, String> paraMap);

	// 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다.
	int expGetTotalCount(Map<String, String> paraMap);
	
	//검색어 입력시 자동글 완성하기
	List<String> expWordSearchShow(Map<String, String> paraMap);

	// 하나의 전자결재내역 문서 보여주기
	ElectronPayJshVO expOneView(Map<String, String> paraMap);
	
	
	// 파일첨부가 없는 전자결재 문서 글쓰기 insert
	int addExpPayment(ElectronPayJshVO epvo);
	// 파일첨부가 있는 전자결재 문서 글쓰기 insert
	int addExpPayment_withFile(ElectronPayJshVO epvo);
	
	
	//임시저장함 insert-첨부파일X
	int saveExpPayment(ElectronPayJshVO epvo);
	//임시저장함 insert-첨부파일O
	int saveExpPayment_withFile(ElectronPayJshVO epvo);
	
	

	/////////////////////////////////////////////////////////////////////////////
	////근태결재
	
	//페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
	List<ElectronPayJshVO> vacListSearchWithPaging(Map<String, String> paraMap);
	
	// 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다.
	int vacGetTotalCount(Map<String, String> paraMap);
	
	//검색어 입력시 자동글 완성하기
	List<String> vacWordSearchShow(Map<String, String> paraMap);

	// 파일첨부가 없는 근태결재 문서 글쓰기 insert
	int addVacPayment(ElectronPayJshVO epvo);
	// 파일첨부가 있는 근태결재 문서 글쓰기 insert
	int addVacPayment_withFile(ElectronPayJshVO epvo);
	
	
	//임시저장함 insert-첨부파일X
	int saveVacPayment(ElectronPayJshVO epvo);
	//임시저장함 insert-첨부파일O
	int saveVacPayment_withFile(ElectronPayJshVO epvo);

	// 하나의 근태결재내역 문서 보여주기
	ElectronPayJshVO vacOneView(Map<String, String> paraMap);









}
