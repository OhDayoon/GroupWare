package com.t1works.groupware.kdn.model;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public interface InterEmailKdnDAO {

	List<String> getEmailList(); // 이메일주소 자동완성을 위한 주소록 가져오기

	int getGroupnoMax(); // tbl_mailinbox 테이블에서 groupno 컬럼의 최대값 구하기

	int send(EmailKdnVO evo); // 파일첨부가 없는 메일쓰기

	int sendWithFile(EmailKdnVO evo); // 파일 첨부가 있는 이메일 쓰기

	int getTotalCount(Map<String, String> paraMap); // 받은메일함 총 이메일 건수 구해오기
	
	int getTotalUnreadEmail(Map<String, String> paraMap); // 읽지않은 메일 총 건수 구해오기

	List<EmailKdnVO> emailListSearchWithPaging(Map<String, String> paraMap); // 페이징 처리한 이메일목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)

	EmailKdnVO getView(Map<String, String> paraMap); // 이메일 열람하기

	EmailKdnVO getSentMailView(Map<String, String> paraMap); // 보낸메일함의 이메일 열람하기

	EmailKdnVO getImportantMailView(Map<String, String> paraMap); // 중요메일함의 이메일 열람하기
	
	EmailKdnVO getTrashView(Map<String, String> paraMap); // 휴지통 이메일 열람하기
	
	int getMailSentTotalCount(Map<String, String> paraMap); // 보낸메일함 총 이메일 건수 구해오기

	int getMailImportantTotalCount(Map<String, String> paraMap); // 중요메일함 총 이메일 건수 구해오기

	int getTrashTotalCount(Map<String, String> paraMap); // 휴지통 총 이메일 건수 구해오기
	
	int saveSentMail(EmailKdnVO evo); // 보낸메일함에 저장하기

	int saveSentMailwithAttach(EmailKdnVO evo); // 보낸메일함에 저장하기(첨부파일 있는 경우)

	int getNewSeq(); // 메일번호 추출하기

	List<EmailKdnVO> sentEmailListSearchWithPaging(Map<String, String> paraMap); // 보낸메일함 페이징 처리한 이메일목록 가져오기(검색어 유무 상관없이 모두 다 포함한것

	int delImmed(List<String> emailSeqList); // 받은메일함의 메일을 휴지통 이동없이 완전삭제 완료하기 

	int delSentMail(List<String> emailSeqList); // 보낸메일함의 메일 삭제하기

	int removeStar(List<String> emailSeqList); // 중요메일함 메일 삭제(중요표시 해제), 받은메일함의 메일 중요표시해제

	int addStar(List<String> emailSeqList); // 받은메일함의 메일 중요표시하기

	int moveToTrash(List<String> emailSeqList); // 받은메일함의 메일 휴지통으로 이동하기

	List<EmailKdnVO> trashListSearchWithPaging(Map<String, String> paraMap); // 휴지통 페이징 처리한 이메일목록 가져오기(검색어 유무 상관없이 모두 다 포함한것)

	int moveToMailInbox(List<String> emailSeqList); // 휴지통 메일을 받은메일함으로 이동시키기

	int markAsUnread(List<String> emailSeqList); //읽지않음으로 변경

	int markAsRead(List<String> emailSeqList); // 읽음으로 변경

	int markAsUnreadSentMail(List<String> emailSeqList); // 보낸메일함 메일 읽지 않음으로 변경

	int markAsReadSentMail(List<String> emailSeqList); // 보낸메일함 메일 읽음으로 변경

	int emptyTrash(String email); //휴지통 비우기

	String receiverEmail(String seq); // 해당 seq의 수신자이메일 가져오기 (여러명일수도 있고 한명일 수도 있다)
 
	String getName(String receiverEmail); // 한개의 email에 해당하는 사원명 가져오기

	String ccEmail(String seq); // 해당 seq의 참조수신자이메일 가져오기 (여러명일수도 있고 한명일 수도 있다 + null 일 수도 있다)

	

	



	
	

}
