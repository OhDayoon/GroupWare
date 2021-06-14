package com.t1works.groupware.sia.model;

import org.springframework.web.multipart.MultipartFile;

public class ApprovalSiaVO {
	
	private int ano;				// 문서번호
	private String fk_employeeid;	// 사번
	private String anocode;			// 결재구분 (1:일반결재, 2:지출결재, 3:근태결재)
	private String arecipient1;		// 수신자1
	private String arecipient2;		// 수신자2
	private String arecipient3;		// 수신자3
	private String atitle;			// 제목
	private String astatus;			// 결재상태 (0:제출, 1:결재진행중, 2:반려, 3:승인완료)
	private String approvaldate1;	// 수신자1 결재날짜
	private String approvaldate2;	// 수신자2 결재날짜
	private String approvaldate3;	// 수신자3 결재날짜
	private String acontent;		// 글내용
	private String asdate;			// 기안일	
	private String apaper;			// 문서상태 (0:수신, 1:발신, 2:임시보관)
	private String fileName;		// 첨부파일
	private String orgFilename;		// 진짜 파일명
	private String fileSize;		// 파일크기
	
	private MultipartFile attach;
	
	// 일반결재
	private String ncat;		// 일반결재카테고리 (1:회의록, 2:위임장, 3:외부공문, 4:협조공문)
	private int fk_ano;			// 문서번호
	private String ncatname;	// 일반결재카테고리명 (1:회의록, 2:위임장, 3:외부공문, 4:협조공문)
	
	// 회의록
	private String mdate;	// 회의시간
	
	// 위임장
	private String fk_wiimdate;	// 위임기간
	
	// 협조공문
	private String comname;	// 타회사명
	
	// 직원
	private String employeeid;	// 사번
	private String fk_dcode;	// 부서코드
	private String fk_pcode;	// 직급코드
	private String name;		// 직원명
	
	// 부서
	private String dcode;		// 부서코드
	private String dname;		// 부서명
	
	// 직급
	private String pcode;		// 직급코드
	private String pname;		// 직급명
	
	// 지출결재
	private String scat;		// 지출결재카테고리 (1:지출결의서, 2:법인카드사용신청서)	
	private String scatname;	// 지출결재카테고리명 (1:지출결의서, 2:법인카드사용신청서)
	
	// 지출결의서
	private String exdate;		// 지출일자
	private int exprice;		// 지출금액
	
	// 법인카드사용신청서
	private String codate;		// 사용예정일
	private String cocardnum;	// 카드번호
	private String copurpose;	// 지출목적 (1:교통비, 2:사무비품, 3:주유비, 4:출장비, 5:식비, 6:기타)
	private int coprice;		// 예상금액
		

	// 근태결재
	private String vno;			// 근태결재카테고리 (1:병가, 2:반차, 3:연차, 4:경조휴가, 5:출장, 6:추가근무)	
	private String vcatname;	// 근태결재카테고리명 (1:병가, 2:반차, 3:연차, 4:경조휴가, 5:출장, 6:추가근무)
	
	// 병가
	private String slstart;		// 사용예정시작일자
	private String slend;		// 사용예정마지막일자
	private int sldates;		// 사용일수
	
	// 반차
	private String afdate;	// 사용예정일
	private int afdan;		// 오전오후 (1: 오전, 2:오후)
	private int afdates;	// 사용일수
	
	// 연차
	private String daystart;	// 사용예정시작일자
	private String dayend;		// 사용예정마지막일자
	private int daydates;		// 사용일수
	
	// 경조휴가
	private String congstart;	// 사용예정시작일자
	private String congend;		// 사용예정마지막일자
	private int congdates;		// 사용일수
	
	// 출장
	private String bustart;		// 사용예정시작일자
	private String buend;		// 사용예정마지막일자
	private int budates;		// 사용일수
	private String buplace;		// 출장지
	private int bupeople;		// 출장인원
	
	// 추가근무
	private String ewdate;		// 사용예정일
	private int ewhours;		// 추가근무시간
	
	
	// 전자결재 의견작성
	private String ono;			// 의견번호
	private String ocontent;	// 의견내용
	private String odate;		// 작성일자
	
	
	// 결재로그
	private String logstatus;	// 결재상태
	private String logdate;		// 결재일자
	

	private String rno;

	private String parentAno;
	
	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public String getFk_employeeid() {
		return fk_employeeid;
	}

	public void setFk_employeeid(String fk_employeeid) {
		this.fk_employeeid = fk_employeeid;
	}

	public String getAnocode() {
		return anocode;
	}

	public void setAnocode(String anocode) {
		this.anocode = anocode;
	}

	public String getArecipient1() {
		return arecipient1;
	}

	public void setArecipient1(String arecipient1) {
		this.arecipient1 = arecipient1;
	}

	public String getArecipient2() {
		return arecipient2;
	}

	public void setArecipient2(String arecipient2) {
		this.arecipient2 = arecipient2;
	}

	public String getArecipient3() {
		return arecipient3;
	}

	public void setArecipient3(String arecipient3) {
		this.arecipient3 = arecipient3;
	}

	public String getAtitle() {
		return atitle;
	}

	public void setAtitle(String atitle) {
		this.atitle = atitle;
	}

	public String getAstatus() {
		return astatus;
	}

	public void setAstatus(String astatus) {
		this.astatus = astatus;
	}

	public String getApprovaldate1() {
		return approvaldate1;
	}

	public void setApprovaldate1(String approvaldate1) {
		this.approvaldate1 = approvaldate1;
	}

	public String getApprovaldate2() {
		return approvaldate2;
	}

	public void setApprovaldate2(String approvaldate2) {
		this.approvaldate2 = approvaldate2;
	}

	public String getApprovaldate3() {
		return approvaldate3;
	}

	public void setApprovaldate3(String approvaldate3) {
		this.approvaldate3 = approvaldate3;
	}

	public String getAcontent() {
		return acontent;
	}

	public void setAcontent(String acontent) {
		this.acontent = acontent;
	}

	public String getAsdate() {
		return asdate;
	}

	public void setAsdate(String asdate) {
		this.asdate = asdate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getApaper() {
		return apaper;
	}

	public void setApaper(String apaper) {
		this.apaper = apaper;
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

	public String getNcat() {
		return ncat;
	}

	public void setNcat(String ncat) {
		this.ncat = ncat;
	}

	public int getFk_ano() {
		return fk_ano;
	}

	public void setFk_ano(int fk_ano) {
		this.fk_ano = fk_ano;
	}

	public String getNcatname() {
		return ncatname;
	}

	public void setNcatname(String ncatname) {
		this.ncatname = ncatname;
	}

	public String getMdate() {
		return mdate;
	}

	public void setMdate(String mdate) {
		this.mdate = mdate;
	}

	public String getFk_wiimdate() {
		return fk_wiimdate;
	}

	public void setFk_wiimdate(String fk_wiimdate) {
		this.fk_wiimdate = fk_wiimdate;
	}

	public String getComname() {
		return comname;
	}

	public void setComname(String comname) {
		this.comname = comname;
	}

	public String getEmployeeid() {
		return employeeid;
	}

	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}

	public String getFk_dcode() {
		return fk_dcode;
	}

	public void setFk_dcode(String fk_dcode) {
		this.fk_dcode = fk_dcode;
	}

	public String getFk_pcode() {
		return fk_pcode;
	}

	public void setFk_pcode(String fk_pcode) {
		this.fk_pcode = fk_pcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getScat() {
		return scat;
	}

	public void setScat(String scat) {
		this.scat = scat;
	}

	public String getScatname() {
		return scatname;
	}

	public void setScatname(String scatname) {
		this.scatname = scatname;
	}

	public String getExdate() {
		return exdate;
	}

	public void setExdate(String exdate) {
		this.exdate = exdate;
	}

	public int getExprice() {
		return exprice;
	}

	public void setExprice(int exprice) {
		this.exprice = exprice;
	}

	public String getCodate() {
		return codate;
	}

	public void setCodate(String codate) {
		this.codate = codate;
	}

	public String getCocardnum() {
		return cocardnum;
	}

	public void setCocardnum(String cocardnum) {
		this.cocardnum = cocardnum;
	}

	public String getCopurpose() {
		return copurpose;
	}

	public void setCopurpose(String copurpose) {
		this.copurpose = copurpose;
	}

	public int getCoprice() {
		return coprice;
	}

	public void setCoprice(int coprice) {
		this.coprice = coprice;
	}

	public String getVno() {
		return vno;
	}

	public void setVno(String vno) {
		this.vno = vno;
	}

	public String getVcatname() {
		return vcatname;
	}

	public void setVcatname(String vcatname) {
		this.vcatname = vcatname;
	}

	public String getSlstart() {
		return slstart;
	}

	public void setSlstart(String slstart) {
		this.slstart = slstart;
	}

	public String getSlend() {
		return slend;
	}

	public void setSlend(String slend) {
		this.slend = slend;
	}

	public int getSldates() {
		return sldates;
	}

	public void setSldates(int sldates) {
		this.sldates = sldates;
	}

	public String getAfdate() {
		return afdate;
	}

	public void setAfdate(String afdate) {
		this.afdate = afdate;
	}

	public int getAfdan() {
		return afdan;
	}

	public void setAfdan(int afdan) {
		this.afdan = afdan;
	}

	public int getAfdates() {
		return afdates;
	}

	public void setAfdates(int afdates) {
		this.afdates = afdates;
	}

	public String getDaystart() {
		return daystart;
	}

	public void setDaystart(String daystart) {
		this.daystart = daystart;
	}

	public String getDayend() {
		return dayend;
	}

	public void setDayend(String dayend) {
		this.dayend = dayend;
	}

	public int getDaydates() {
		return daydates;
	}

	public void setDaydates(int daydates) {
		this.daydates = daydates;
	}

	public String getCongstart() {
		return congstart;
	}

	public void setCongstart(String congstart) {
		this.congstart = congstart;
	}

	public String getCongend() {
		return congend;
	}

	public void setCongend(String congend) {
		this.congend = congend;
	}

	public int getCongdates() {
		return congdates;
	}

	public void setCongdates(int congdates) {
		this.congdates = congdates;
	}

	public String getBustart() {
		return bustart;
	}

	public void setBustart(String bustart) {
		this.bustart = bustart;
	}

	public String getBuend() {
		return buend;
	}

	public void setBuend(String buend) {
		this.buend = buend;
	}

	public int getBudates() {
		return budates;
	}

	public void setBudates(int budates) {
		this.budates = budates;
	}

	public String getBuplace() {
		return buplace;
	}

	public void setBuplace(String buplace) {
		this.buplace = buplace;
	}

	public int getBupeople() {
		return bupeople;
	}

	public void setBupeople(int bupeople) {
		this.bupeople = bupeople;
	}

	public String getEwdate() {
		return ewdate;
	}

	public void setEwdate(String ewdate) {
		this.ewdate = ewdate;
	}

	public int getEwhours() {
		return ewhours;
	}

	public void setEwhours(int ewhours) {
		this.ewhours = ewhours;
	}

	public String getOcontent() {
		return ocontent;
	}

	public void setOcontent(String ocontent) {
		this.ocontent = ocontent;
	}

	public String getOdate() {
		return odate;
	}

	public void setOdate(String odate) {
		this.odate = odate;
	}

	public String getLogstatus() {
		return logstatus;
	}

	public void setLogstatus(String logstatus) {
		this.logstatus = logstatus;
	}	

	public String getLogdate() {
		return logdate;
	}

	public void setLogdate(String logdate) {
		this.logdate = logdate;
	}
	
	public String getRno() {
		return rno;
	}

	public void setRno(String rno) {
		this.rno = rno;
	}

	public String getParentAno() {
		return parentAno;
	}

	public void setParentAno(String parentAno) {
		this.parentAno = parentAno;
	}

	public String getOno() {
		return ono;
	}

	public void setOno(String ono) {
		this.ono = ono;
	}
	

}
