package com.t1works.groupware.kdn.model;

public class CommentKdnVO {

	private String seq;			 	//댓글번호  
	private String fk_seq;			 //원글번호
	private String fk_employeeid; 	// 작성자ID
	private String name;		 	//작성자
	private String content;		 	//댓글내용
	private String regDate;		 	//작성일시
	private String countUpdate;		// 댓글 수 업데이트용 +1: 증가, -1:감소

	public CommentKdnVO() {}
	
	public CommentKdnVO(String seq,String fk_seq,String fk_employeeid,String name,String content,String regDate) {
		this.seq = seq;
		this.fk_seq = fk_seq;
		this.fk_employeeid = fk_employeeid;
		this.name = name;
		this.content = content;
		this.regDate = regDate;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRegDate() {
		return regDate;
	}
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getCountUpdate() {
		return countUpdate;
	}

	public void setCountUpdate(String countUpdate) {
		this.countUpdate = countUpdate;
	}


	
	
	
}
