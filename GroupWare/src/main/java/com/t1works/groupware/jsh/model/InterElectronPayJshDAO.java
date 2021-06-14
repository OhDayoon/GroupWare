package com.t1works.groupware.jsh.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InterElectronPayJshDAO {
	
	//공통//////////////////////////////////////////////////////////////////////////
	
	// 하나의 문서보기에서 결재로그 보여주기
	List<ElectronPayJshVO> oneLogList(Map<String, String> paraMap);
	
	// 하나의 문서 수신자정보 받아오기
	ElectronPayJshVO receiver(Map<String, String> paraMap);
	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////
	
	

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

	//일반결재 글쓰기
	ElectronPayJshVO WriteJsh(Map<String, String> paraMap);
	// 수신자 정보 select해오기
	ElectronPayJshVO mWriteJsh(HashMap<String, String> paraMap);


	// 1) insert될 문서번호를 알아온다
	String insertno();
	
	// 2) 전자결재테이블 insert
	int Electricadd(ElectronPayJshVO epvo);
	// 3) ncatname 조건에 따라 insert 시켜줌 일반결재테이블에 insert  
	int Generaladd(ElectronPayJshVO epvo);
	// 4) ncatname 조건에 따라 (회의록,위임장,외부공문,협조공문) 테이블에 insert 시켜줌 
	int selectadd(ElectronPayJshVO epvo);

	// 글쓰기( 파일첨부가 있는 글쓰기  ) 
	int Electricadd_withFile(ElectronPayJshVO epvo);

	//임시저장함 insert-첨부파일X
	int ElectricSave(ElectronPayJshVO epvo);
	//임시저장함 insert-첨부파일O
	int ElectricSave_withFile(ElectronPayJshVO epvo);

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//지출결재
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	
	//페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
	List<ElectronPayJshVO> expListSearchWithPaging(Map<String, String> paraMap);

	// 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다.
	int expGetTotalCount(Map<String, String> paraMap);

	//검색어 입력시 자동글 완성하기
	List<String> expWordSearchShow(Map<String, String> paraMap);

	// 하나의 전자결재내역 문서 보여주기
	ElectronPayJshVO expOneView(Map<String, String> paraMap);
	
	// 2) 전자결재테이블 insert
	int ElectricExpadd(ElectronPayJshVO epvo);
	// 3) scatname 조건에 따라 insert 시켜줌 전자결재테이블에 insert 
	int expAdd(ElectronPayJshVO epvo);
	// 4) scatname 조건에 따라 (지출결의서, 법인카드사용신청서) 테이블에 insert 시켜줌 
	int selectExpadd(ElectronPayJshVO epvo);

	// 2) 파일첨부가 있는 전자결재테이블 글쓰기 insert
	int ElectricExpadd_withFile(ElectronPayJshVO epvo);

	
	//임시저장함 insert-첨부파일X
	int saveExpadd(ElectronPayJshVO epvo);
	//임시저장함 insert-첨부파일o
	int saveExpadd_withFile(ElectronPayJshVO epvo);

	

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//근태결재
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	//페이징 처리한 글목록 가져오기(검색이 있든지, 검색이 없든지 모두 다 포함한 것)
	List<ElectronPayJshVO> vacListSearchWithPaging(Map<String, String> paraMap);
	
	// 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다.
	int vacGetTotalCount(Map<String, String> paraMap);
	
	//검색어 입력시 자동글 완성하기
	List<String> vacWordSearchShow(Map<String, String> paraMap);
		
	
	
	// 2) 전자결재테이블 insert
	int ElectricVacadd(ElectronPayJshVO epvo);
	// 3) scatname 조건에 따라 insert 시켜줌 전자결재테이블에 insert 
	int vacAdd(ElectronPayJshVO epvo);
	// 4) scatname 조건에 따라 (지출결의서, 법인카드사용신청서) 테이블에 insert 시켜줌 
	int selectVacadd(ElectronPayJshVO epvo);
	
	// 6) 결재로그에 insert
	int logInsert(ElectronPayJshVO epvo);
	
	
	// 2) 파일첨부가 있는 전자결재테이블 글쓰기 insert
	int ElectricVacadd_withFile(ElectronPayJshVO epvo);

	
	//임시저장함 insert-첨부파일X
	int saveVacadd(ElectronPayJshVO epvo);
	//임시저장함 insert-첨부파일o
	int saveVacadd_withFile(ElectronPayJshVO epvo);

	// 하나의 근태결재내역 문서 보여주기
	ElectronPayJshVO vacOneView(Map<String, String> paraMap);








	

    
	
	

}
