package com.t1works.groupware.kdn.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class BoardKdnDAO implements InterBoardKdnDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession3; // 원격DB에 연결
	
	// =========== 공지사항 =============
	// 공지사항 글쓰기
	@Override
	public int noticePostUpload(BoardKdnVO boardvo) {
		int n = sqlsession3.insert("board.noticePostUpload", boardvo);
		return n;
	}

	// 공지사항 총 게시물 건수(totalCount) 구하기 - 검색이 있을때와 검색이 없을때로 나뉜다.
	@Override
	public int getNoticeTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("board.getNoticeTotalCount", paraMap);
		return n;
	}

	// 공지사항 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public List<BoardKdnVO> noticeBoardListSearchWithPaging(Map<String, String> paraMap) {
		List<BoardKdnVO> boardList = sqlsession3.selectList("board.noticeBoardListSearchWithPaging", paraMap);
		return boardList;
	}
	
	// 공지사항 글 조회수 1 증가하기
	@Override
	public void setAddReadCount(Map<String, String> paraMap) {
		sqlsession3.update("board.setAddReadCount", paraMap);
	}

	// 공지사항 글1개 조회하기
	@Override
	public BoardKdnVO getView(Map<String, String> paraMap) {
		System.out.println("DAO 메소드 호출됨");
		BoardKdnVO boardvo = sqlsession3.selectOne("board.getView", paraMap);
		System.out.println("boardvo 유무: "+boardvo);
		return boardvo;
	}
	
	// 공지사항 글 수정하기
	@Override
	public int noticeEdit(BoardKdnVO boardvo) {
		int n = sqlsession3.update("board.noticeEdit", boardvo);
		return n;
	}
	
	// 공지사항 글 삭제하기
	@Override
	public int noticeDel(Map<String, String> paraMap) {
		int n = sqlsession3.delete("board.noticeDel",paraMap);
		return n;
	}
	
	// 파일 첨부가 있는 글쓰기
	@Override
	public int noticeUploadwithFile(BoardKdnVO boardvo) {
		int n = sqlsession3.insert("board.noticeUploadwithFile",boardvo);
		return n;
	}
	
	// 첨부파일 변경한 경우 글수정
	@Override
	public int noticeEditNewAttach(BoardKdnVO boardvo) {
		int n = sqlsession3.update("board.noticeEditNewAttach", boardvo);
		return n;
	}

	
	// =========== 건의사항 =============

	// (건의사항) 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public List<BoardKdnVO> listSearchWithPaging(Map<String, String> paraMap) {
		List<BoardKdnVO> boardList = sqlsession3.selectList("board.listSearchWithPaging", paraMap);
		return boardList;
	}

	// 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("board.getTotalCount", paraMap);
		return n;
	}

	// 건의사항 게시판 글쓰기 완료 요청
	@Override
	public int suggPostUpload(BoardKdnVO boardvo) {
		int n = sqlsession3.insert("board.suggPostUpload", boardvo);
		return n;
	}

	// 건의사항 글1개 조회하기
	@Override
	public BoardKdnVO getSuggPostView(Map<String, String> paraMap) {
		BoardKdnVO boardvo = sqlsession3.selectOne("board.getSuggPostView", paraMap);
		return boardvo;
	}

	// 건의사항 글 조회수 1 증가하기
	@Override
	public void setAddSuggReadCount(Map<String, String> paraMap) {
		sqlsession3.update("board.setAddSuggReadCount", paraMap);
	}

	// 건의사항 글 수정하기
	@Override
	public int suggEdit(BoardKdnVO boardvo) {
		int n = sqlsession3.update("board.suggEdit", boardvo);
		return n;
	}
	
	// 건의사항 글 삭제하기
	@Override
	public int suggDel(Map<String, String> paraMap) {
		int n = sqlsession3.delete("board.suggDel",paraMap);
		return n;
	}
	
	// 건의사항 댓글쓰기(tbl_suggestionboardcomment 테이블에 insert)
	@Override
	public int addSuggComment(CommentKdnVO commentvo) {
		int n = sqlsession3.insert("board.addSuggComment", commentvo);
		return n;
	}

	// 건의사항 tbl_suggestionboard 테이블에 commentCount 컬럼의 값을 증가(update) 
	@Override
	public int updateSuggCmntCount(String fk_seq) {
		int n = sqlsession3.update("board.updateSuggCmntCount",fk_seq);
		return n;
	}
	
	// 건의사항 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리)
	@Override
	public List<CommentKdnVO> getSuggCmntListPaging(Map<String, String> paraMap) {
		List<CommentKdnVO> commentList = sqlsession3.selectList("board.getSuggCmntListPaging", paraMap);
		return commentList;
	}
	
	// 건의사항 원게시물에 딸린 댓글 totalPage 알아오기(Ajax 로 처리)
	@Override
	public int getSuggCmntTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession3.selectOne("board.getSuggCmntTotalPage", paraMap);
		return totalPage;
	}
	
	// tbl_suggestionboard 테이블에서 groupno 컬럼의 최대값 구하기
	@Override
	public int getGroupnoMax() {
		int max = sqlsession3.selectOne("board.getGroupnoMax");
	    return max;
	}
	
	// 건의사항 댓글 삭제
	@Override
	public int delSuggComment(CommentKdnVO commentvo) {
		int n = sqlsession3.delete("board.delSuggComment", commentvo);
		return n;
	}
	
	// 건의사항 댓글 삭제시 tbl_suggestionboard commentCount 감소시키기
	@Override
	public int updateSuggCmntCountOneDown(String fk_seq) {
		int n =sqlsession3.update("board.updateSuggCmntCountOneDown", fk_seq);
		return n;
	}
	
	// 건의사항 댓글 수정
	@Override
	public int editSuggComment(CommentKdnVO commentvo) {
		//System.out.println("댓글수정 DAO 메소드 호출됨");
		int n = sqlsession3.update("board.editSuggComment", commentvo);
		//System.out.println("댓글수정성공여부 DAO : "+n);
		return n;
	}

	// 건의사항 파일첨부가 있는 글쓰기
	@Override
	public int suggUploadWithFile(BoardKdnVO boardvo) {
		int n = sqlsession3.insert("board.suggUploadWithFile",boardvo);
		return n;
	}

	// 건의사항 특정 글 1개의 총 댓글 수 구해오기
	@Override
	public int getSuggCmntTotalCnt(String seq) {
		int n = sqlsession3.selectOne("board.getSuggCmntTotalCnt", seq);
		return n;
	}
	
	// =========== 자유게시판  =============
	
	// 자유게시판 글쓰기 완료 요청
	@Override
	public int genPostUpload(BoardKdnVO boardvo) {
		int n = sqlsession3.insert("board.genPostUpload", boardvo);
		return n;
	}

	// 자유게시판 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public int getGenTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("board.getGenTotalCount", paraMap);
		return n;
	}

	// 페이징 처리한 글목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public List<BoardKdnVO> genListSearchWithPaging(Map<String, String> paraMap) {
		List<BoardKdnVO> boardList = sqlsession3.selectList("board.genListSearchWithPaging", paraMap);
		return boardList;
	}

	// 자유게시판 글1개 조회하기
	@Override
	public BoardKdnVO getGenPostView(Map<String, String> paraMap) {
		BoardKdnVO boardvo = sqlsession3.selectOne("board.getGenPostView", paraMap);
		return boardvo;
	}

	// 글 조회수 1 증가하기
	@Override
	public void setAddGenReadCount(Map<String, String> paraMap) {
		sqlsession3.update("board.setAddGenReadCount", paraMap);
		
	}

	// 글 수정하기
	@Override
	public int generalEdit(BoardKdnVO boardvo) {
		int n = sqlsession3.update("board.generalEdit", boardvo);
		return n;
	}

	// 글 삭제하기
	@Override
	public int generalDel(Map<String, String> paraMap) {
		int n = sqlsession3.delete("board.generalDel",paraMap);
		return n;
	}

	// 댓글쓰기(tbl_generalboardcomment 테이블에 insert)
	@Override
	public int addComment(CommentKdnVO commentvo) {
		int n = sqlsession3.insert("board.addComment", commentvo);
		return n;
	}

	// 댓글쓰기 성공 후 tbl_generalboard 테이블에 commentCount 컬럼의 값을 1증가(update)
	@Override
	public int updateGenCmntCount(String fk_seq) {
		int n = sqlsession3.update("board.updateGenCmntCount",fk_seq);
		return n;
	}

	// 원게시물에 딸린 댓글들을 페이징처리해서 조회해오기(Ajax 로 처리)
	@Override
	public List<CommentKdnVO> getCommentListPaging(Map<String, String> paraMap) {
		List<CommentKdnVO> commentList = sqlsession3.selectList("board.getCommentListPaging", paraMap);
		return commentList;
	}
	
	// 원게시물에 딸린 댓글 totalPage 알아오기(Ajax 로 처리)
	@Override
	public int getCommentTotalPage(Map<String, String> paraMap) {
		int totalPage = sqlsession3.selectOne("board.getCommentTotalPage", paraMap);
		return totalPage;
	}

	// (자유게시판) 파일첨부가 있는 글쓰기
	@Override
	public int genUploadWithFile(BoardKdnVO boardvo) {
		int n = sqlsession3.insert("board.genUploadWithFile",boardvo);
		return n;
	}

	// 첨부파일 변경한 경우 글수정
	@Override
	public int generalEditNewAttach(BoardKdnVO boardvo) {
		int n = sqlsession3.update("board.generalEditNewAttach", boardvo);
		return n;
	}

	// 자유게시판 댓글 삭제
	@Override
	public int delGenComment(CommentKdnVO commentvo) {
		int n = sqlsession3.delete("board.delGenComment", commentvo);
		return n;
	}

	// 자유게시판 댓글 삭제시 tbl_generalboard commentCount 감소시키기
	@Override
	public int updateGenCmntCountOneDown(String fk_seq) {
		int n =sqlsession3.update("board.updateGenCmntCountOneDown", fk_seq);
		return n;
	}

	// 자유게시판 댓글 수정하기
	@Override
	public int editGenComment(CommentKdnVO commentvo) {
		int n = sqlsession3.update("board.editGenComment", commentvo);
		return n;
	}

	// 자유게시판 특정 글 1개의 총 댓글 수 구해오기
	@Override
	public int getGenCmntTotalCnt(String seq) {
		int n = sqlsession3.selectOne("board.getGenCmntTotalCnt", seq);
		return n;
	}

	// 공지사항 읽음 처리 하기
	@Override
	public int markAsRead(String seq) {
		int n = sqlsession3.update("board.markAsRead", seq);
		return n;
	}

	// 신규 공지사항 유무 확인하기
	@Override
	public int checkNewNotice() {
		int n = sqlsession3.selectOne("board.checkNewNotice");
		return n;
	}

	

	

	

	
	
	

	
	

	

	

	

	


	

	

	

	

	


}
