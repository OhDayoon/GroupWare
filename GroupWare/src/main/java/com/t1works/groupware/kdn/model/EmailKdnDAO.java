package com.t1works.groupware.kdn.model;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class EmailKdnDAO implements InterEmailKdnDAO {
	
	@Resource
	private SqlSessionTemplate sqlsession3; // 원격DB에 연결

	// 이메일주소 자동완성을 위한 주소록 가져오기
	@Override
	public List<String> getEmailList() {
		List<String> emailList = sqlsession3.selectList("email.getEmailList");
		return emailList;
	}

	// tbl_mailinbox 테이블에서 groupno 컬럼의 최대값 구하기
	@Override
	public int getGroupnoMax() {
		int max = sqlsession3.selectOne("email.getGroupnoMax");
	    return max;
	}

	// 파일첨부가 없는 메일쓰기
	@Override
	public int send(EmailKdnVO evo) {
		int n = sqlsession3.insert("email.send", evo);
		return n;
	}

	// 파일첨부가 있는 메일쓰기
	@Override
	public int sendWithFile(EmailKdnVO evo) {
		int n = sqlsession3.insert("email.sendWithFile", evo);
		return n;
	}
	
	// 받은메일함 총 이메일 건수 구해오기
	@Override
	public int getTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("email.getTotalCount", paraMap);
		return n;
	}

	// 읽지않은 메일 총 건수 구해오기
	@Override
	public int getTotalUnreadEmail(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("email.getTotalUnreadEmail", paraMap);
		return n;
	}
	
	// (받은메일함, 중요메일함) 페이징 처리한 이메일목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public List<EmailKdnVO> emailListSearchWithPaging(Map<String, String> paraMap) {
		List<EmailKdnVO> boardList = sqlsession3.selectList("email.emailListSearchWithPaging", paraMap);
		return boardList;
	}

	//이메일 열람하기
	@Override
	public EmailKdnVO getView(Map<String, String> paraMap) {
		EmailKdnVO evo = sqlsession3.selectOne("email.getView",paraMap);
		return evo;
	}

	// 보낸메일함 총 이메일 건수 구해오기
	@Override
	public int getMailSentTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("email.getMailSentTotalCount", paraMap);
		return n;
	}

	// 중요메일함 총 이메일 건수 구해오기
	@Override
	public int getMailImportantTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("email.getMailImportantTotalCount", paraMap);
		return n;
	}

	// 보낸메일함의 이메일 열람하기
	@Override
	public EmailKdnVO getSentMailView(Map<String, String> paraMap) {
		EmailKdnVO evo = sqlsession3.selectOne("email.getSentMailView",paraMap);
		return evo;
	}

	// 중요메일함의 이메일 열람하기
	@Override
	public EmailKdnVO getImportantMailView(Map<String, String> paraMap) {
		EmailKdnVO evo = sqlsession3.selectOne("email.getImportantMailView",paraMap);
		return evo;
	}
	
	// 보낸메일함에 저장하기
	@Override
	public int saveSentMail(EmailKdnVO evo) {
		int n = sqlsession3.insert("email.saveSentMail", evo);
		return n;
	}

	// 보낸메일함에 저장하기(첨부파일 있는 경우)
	@Override
	public int saveSentMailwithAttach(EmailKdnVO evo) {
		int n = sqlsession3.insert("email.saveSentMailwithAttach", evo);
		return n;
	}

	// 새 메일번호 추출하기
	@Override
	public int getNewSeq() {
		int n = sqlsession3.selectOne("email.getNewSeq");
		return n;
	}

	// 보낸메일함 페이징 처리한 이메일목록 가져오기(검색어 유무 상관없이 모두 다 포함한것
	@Override
	public List<EmailKdnVO> sentEmailListSearchWithPaging(Map<String, String> paraMap) {
		List<EmailKdnVO> boardList = sqlsession3.selectList("email.sentEmailListSearchWithPaging", paraMap);
		return boardList;
	}

	// 받은메일함의 메일을 휴지통 이동없이 완전삭제 완료하기 
	@Override
	public int delImmed(List<String> emailSeqList) {
		//System.out.println("받은메일함 삭제가 호출됨");
		int n = sqlsession3.delete("email.delImmed",emailSeqList);
		return n;
	}

	// 보낸메일함의 메일 삭제하기
	@Override
	public int delSentMail(List<String> emailSeqList) {
		//System.out.println("보낸메일함 삭제가 호출됨");
		int n = sqlsession3.delete("email.delSentMail",emailSeqList);
		return n;
	}

	// 중요메일함 메일 삭제(중요표시 해제), 받은메일함의 메일 중요표시해제
	@Override
	public int removeStar(List<String> emailSeqList) {
		int n = sqlsession3.update("email.removeStar", emailSeqList);
		return n;
	}

	// 받은메일함의 메일 중요표시하기
	@Override
	public int addStar(List<String> emailSeqList) {
		int n =sqlsession3.update("email.addStar", emailSeqList);
		return n;
	}

	// 받은메일함의 메일 휴지통으로 이동하기
	@Override
	public int moveToTrash(List<String> emailSeqList) {
		int n = sqlsession3.update("email.moveToTrash", emailSeqList);
		return n;
	}

	// 휴지통 총 이메일 건수 구해오기
	@Override
	public int getTrashTotalCount(Map<String, String> paraMap) {
		int n =sqlsession3.selectOne("email.getTrashTotalCount", paraMap);
		return n;
	}

	// 휴지통 페이징 처리한 이메일목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)
	@Override
	public List<EmailKdnVO> trashListSearchWithPaging(Map<String, String> paraMap) {
		List<EmailKdnVO> boardList = sqlsession3.selectList("email.trashListSearchWithPaging", paraMap);
		return boardList;
	}

	// 휴지통 메일 열람하기
	@Override
	public EmailKdnVO getTrashView(Map<String, String> paraMap) {
		EmailKdnVO evo = sqlsession3.selectOne("email.getTrashView",paraMap);
		return evo;
	}

	// 휴지통 메일을 받은메일함으로 이동시키기
	@Override
	public int moveToMailInbox(List<String> emailSeqList) {
		int n = sqlsession3.update("email.moveToMailInbox", emailSeqList);
		return n;
	}

	//읽지않음으로 변경
	@Override
	public int markAsUnread(List<String> emailSeqList) {
		int n = sqlsession3.update("email.markAsUnread",emailSeqList);
		return n;
	}

	//읽음으로 변경
	@Override
	public int markAsRead(List<String> emailSeqList) {
		int n = sqlsession3.update("email.markAsRead",emailSeqList);
		return n;
	}

	// 보낸메일함 메일 읽지 않음으로 변경
	@Override
	public int markAsUnreadSentMail(List<String> emailSeqList) {
		int n = sqlsession3.update("email.markAsUnreadSentMail",emailSeqList);
		return n;
	}

	// 보낸메일함 메일 읽음으로 변경
	@Override
	public int markAsReadSentMail(List<String> emailSeqList) {
		int n = sqlsession3.update("email.markAsReadSentMail",emailSeqList);
		return n;
	}

	// 휴지통 비우기
	@Override
	public int emptyTrash(String email) {
		int n = sqlsession3.update("email.emptyTrash",email);
		return n;
	}

	// 해당 seq의 수신자이메일 가져오기 (여러명일수도 있고 한명일 수도 있다)
	@Override
	public String receiverEmail(String seq) {
		String receiverEmail= sqlsession3.selectOne("email.receiverEmail",seq);
		return receiverEmail;
	}

	// 한개의 email에 해당하는 사원명 가져오기
	@Override
	public String getName(String receiverEmail) {
		String getName= sqlsession3.selectOne("email.getName",receiverEmail);
		return getName;
	}

	// 해당 seq의 수신자이메일 가져오기 (여러명일수도 있고 한명일 수도 있다 + null 일 수도 있다)
	@Override
	public String ccEmail(String seq) {
		String ccEmail= sqlsession3.selectOne("email.ccEmail",seq);
		return ccEmail;
	}

	

}
