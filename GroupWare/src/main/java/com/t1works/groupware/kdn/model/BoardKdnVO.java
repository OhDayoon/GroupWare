package com.t1works.groupware.kdn.model;

import org.springframework.web.multipart.MultipartFile;

public class BoardKdnVO {

	private String seq;          // 게시글 시퀀스번호 
	private String postIndex;			 // 실제 게시판 글번호
	   private String fk_employeeid;    // 사용자ID
	   private String name;         // 글쓴이 
	   private String subject;      // 글제목
	   private String content;      // 글내용 
	   private String pw;           // 글암호
	   private String readCount;    // 글조회수
	   private String regDate;      // 글쓴시간
	   private String status;       // 글삭제여부   1:사용가능한 글,  0:삭제된글 
	   private String readStatus;    // 읽음여부
	   private String previousseq;      // 이전글번호
	   private String previoussubject;  // 이전글제목
	   private String nextseq;          // 다음글번호
	   private String nextsubject;      // 다음글제목
	   private String privatePost;		// 비밀글 여부
	   private String fk_categnum;		// 카테고리번호 
	   private String commentCount;     // 댓글수 
	   private String groupno; 			// 답변글쓰기에 있어서 그룹번호
	   private String parentSeq;		// 답변글의 원글번호. 자신의 글(답변글)에 있어서 원글(부모글)이 누구인지에 대한 정보값
	   private String depthno;			// 답변글 등록시 글목록에서 들여쓰기용
	   
	   private MultipartFile attach;
	   
	   private String fileName;    // WAS(톰캣)에 저장될 파일명(2020120809271535243254235235234.png) 
	   private String orgFilename; // 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명
	   private String fileSize;
	   
	   
	   public BoardKdnVO() {}
	   
	   public BoardKdnVO(String seq, String fk_employeeid, String name, String subject, String content, String pw, String readCount,
			String regDate, String status, String fk_categnum,String groupno, String depthno,
	        String fileName, String orgFilename, String fileSize) {
		super();
		this.seq = seq;
		this.fk_employeeid = fk_employeeid;
		this.name = name;
		this.subject = subject;
		this.content = content;
		this.pw = pw;
		this.readCount = readCount;
		this.regDate = regDate;
		this.status = status;
		this.fk_categnum = fk_categnum;
		this.groupno = groupno;
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

	public String getPostIndex() {
		return postIndex;
	}

	public void setPostIndex(String postIndex) {
		this.postIndex = postIndex;
	}

	public String getFk_employeeid() {
		return fk_employeeid;
	}

	public void setFk_employeeid(String fk_employeeid) {
		this.fk_employeeid = fk_employeeid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public String getReadCount() {
		return readCount;
	}

	public void setReadCount(String readCount) {
		this.readCount = readCount;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFk_categnum() {
		return fk_categnum;
	}

	public void setFk_categnum(String fk_categnum) {
		this.fk_categnum = fk_categnum;
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

	public String getPrivatePost() {
		return privatePost;
	}

	public void setPrivatePost(String privatePost) {
		this.privatePost = privatePost;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
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

	public MultipartFile getAttach() {
		return attach;
	}

	public void setAttach(MultipartFile attach) {
		this.attach = attach;
	}

	public String getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	
	
	
}
