package com.t1works.groupware.kdn.model;

import org.springframework.web.multipart.MultipartFile;

public class EmailKdnVO {
	
	private String seq;    			// 메일번호
	private String fk_seq;			// 보낸메일함 저장된 사본메일의 원본메일번호
	private String fk_employeeid ;  // 사번
	private String senderEmail;   // 발신자메일주소
	private String senderName;      // 발신자명
	private String receiverName;	// 수신자명
	private String receiverEmail; // 수신자메일주소
	private String ccEmail;       // 참조메일주소
	private String subject;         // 제목
	private String content;         // 내용
	private String previousseq;      // 이전글번호
	private String previoussubject;  // 이전글제목
	private String nextseq;          // 다음글번호
	private String nextsubject;      // 다음글제목
	private String sendingDate;     // 발신일시
	private String checkImportant;  // 중요표시(0:not starred(중요x), 1:starred(중요o))
	private String saveSentMail;    // 보낸메일함저장여부(0:저장안함, 1:저장)
	private String moveToTrash;     // 삭제여부(0:삭제안함, 1:삭제함(휴지통 이동))
	private String readStatus;      // 읽음여부(0:읽지않음, 1:읽음)
	private String groupno; 			// 답변글쓰기에 있어서 그룹번호
    private String parentSeq;		// 답변글의 원글번호. 자신의 글(답변글)에 있어서 원글(부모글)이 누구인지에 대한 정보값
    private String depthno;			// 답변글 등록시 글목록에서 들여쓰기용
    private String name;			// 이메일 검색용. tbl_employee의 name 컬럼
    
    private MultipartFile attach;
   
    private String fileName;    // WAS(톰캣)에 저장될 파일명(2020120809271535243254235235234.png) 
    private String orgFilename; // 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명
    private String fileSize;
	
    // ======= select용 변수 추가
    private String receiverCnt; // 받은 사람 수 
    
	public EmailKdnVO() {}
	
	public EmailKdnVO(String seq,String fk_seq, String fk_employeeid,String senderEmail,String senderName, String receiverName,String receiverEmail,String ccEmail
			,String subject,String content,String sendingDate,String checkImportant,String saveSentMail,String moveToTrash,String readStatus,
			String groupno,String parentSeq,String depthno,String fileName,String orgFilename,String fileSize) {
		this.seq = seq;
		this.fk_seq = fk_seq;
		this.fk_employeeid = fk_employeeid;
		this.senderEmail = senderEmail;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.receiverEmail = receiverEmail;
		this.ccEmail = ccEmail;
		this.subject = subject;
		this.content = content;
		this.sendingDate = sendingDate;
		this.saveSentMail = saveSentMail;
		this.moveToTrash = moveToTrash;
		this.readStatus = readStatus;
		this.checkImportant = checkImportant;
		this.groupno = groupno;
		this.parentSeq = parentSeq;
		this.depthno = depthno;
		this.fileName = fileName;
		this.orgFilename = orgFilename;
		this.fileSize = fileSize;
	}
	
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getFk_seq() {
		return fk_seq;
	}

	public void setFk_seq(String fk_seq) {
		this.fk_seq = fk_seq;
	}

	public String getFk_employeeid() {
		return fk_employeeid;
	}
	public void setFk_employeeid(String fk_employeeid) {
		this.fk_employeeid = fk_employeeid;
	}
	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getCcEmail() {
		return ccEmail;
	}

	public void setCcEmail(String ccEmail) {
		this.ccEmail = ccEmail;
	}

	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendingDate() {
		return sendingDate;
	}
	public void setSendingDate(String sendingDate) {
		this.sendingDate = sendingDate;
	}
	public String getSaveSentMail() {
		return saveSentMail;
	}
	public void setSaveSentMail(String saveSentMail) {
		this.saveSentMail = saveSentMail;
	}
	public String getMoveToTrash() {
		return moveToTrash;
	}
	public void setMoveToTrash(String moveToTrash) {
		this.moveToTrash = moveToTrash;
	}
	public String getReadStatus() {
		return readStatus;
	}
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	public String getCheckImportant() {
		return checkImportant;
	}

	public void setCheckImportant(String checkImportant) {
		this.checkImportant = checkImportant;
	}

	public String getGroupno() {
		return groupno;
	}

	public void setGroupno(String groupno) {
		this.groupno = groupno;
	}

	public String getParentSeq() {
		return parentSeq;
	}

	public void setParentSeq(String parentSeq) {
		this.parentSeq = parentSeq;
	}

	public String getDepthno() {
		return depthno;
	}

	public void setDepthno(String depthno) {
		this.depthno = depthno;
	}

	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getOrgFilename() {
		return orgFilename;
	}

	public void setOrgFilename(String orgFilename) {
		this.orgFilename = orgFilename;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getPreviousseq() {
		return previousseq;
	}

	public void setPreviousseq(String previousseq) {
		this.previousseq = previousseq;
	}

	public String getPrevioussubject() {
		return previoussubject;
	}

	public void setPrevioussubject(String previoussubject) {
		this.previoussubject = previoussubject;
	}

	public String getNextseq() {
		return nextseq;
	}

	public void setNextseq(String nextseq) {
		this.nextseq = nextseq;
	}

	public String getNextsubject() {
		return nextsubject;
	}

	public void setNextsubject(String nextsubject) {
		this.nextsubject = nextsubject;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReceiverCnt() {
		return receiverCnt;
	}

	public void setReceiverCnt(String receiverCnt) {
		this.receiverCnt = receiverCnt;
	}

	
	
}
