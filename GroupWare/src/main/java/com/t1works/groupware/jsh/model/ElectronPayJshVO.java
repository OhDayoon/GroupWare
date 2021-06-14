package com.t1works.groupware.jsh.model;

import org.springframework.web.multipart.MultipartFile;

public class ElectronPayJshVO {

	private String  ano; 						  // 문서번호
	private String  fk_employeeid;              // 사번
	private String  anocode;      		         //결재구분 (1:일반결재, 2:지출결재, 3:근태결재)
	private String  arecipient1;                 // 수신자    
	private String  atitle;      			  //제목
	private String  astatus;                 // default '0' not null     -- 결재상태 (0:제출, 1:결재진행중, 2:반려, 3:승인완료)
	private String  acontent;      		  // 글내용
	private String  asdate;                // 기안일
	
	private String  apaper;       	 	 //  문서상태 (0:수신, 1:발신, 2:임시보관)
	
	
	 private MultipartFile   attach;
	   /* form 태그에서 type="file" 인 파일을 받아서 저장되는 필드이다. 
		     진짜파일 ==> WAS(톰캣) 디스크에 저장됨.
		      조심할것은 MultipartFile attach 는 오라클 데이터베이스 tbl_board 테이블의 컬럼이 아니다.   
		  /Board/src/main/webapp/WEB-INF/views/tiles1/board/add.jsp 파일에서 input type="file" 인 name 의 이름(attach)과 
		      동일해야만 파일첨부가 가능해진다.!!!!
	*/	      
		      
   private String fileName;    // WAS(톰캣)에 저장될 파일명(2020120809271535243254235235234.png) 
   private String orgFilename; // 진짜 파일명(강아지.png)  // 사용자가 파일을 업로드 하거나 파일을 다운로드 할때 사용되어지는 파일명
   private String fileSize;    // 파일크기    


	
    //////일반결재문서 //////
	private String ncat;     //일반결재카테고리 (1:회의록, 2:위임장, 3:외부공문, 4:협조공문)
	private String fk_ano;      // 문서번호
	private String  ncatname; // 일반결재카테고리명 (1:회의록, 2:위임장, 3:외부공문, 4:협조공문)
	////////
	
	//회의록
	 private String mno;       //순번
	 private String mdate;  //회의시간
	 
	//위임장
	 private String wno; // 순번
	 private String fk_wiimdate; //위임기간
	 
	 //외부공문
	 private String eno; //순번
	 
	 //협조공문
	 private String cno;        //순번
	 private String comname; // 타회사명
	 
	 
	 //지출결재 테이블
	 private String scat;    //지출결재카테고리 (1:지출결의서, 2:법인카드사용신청서)
	 private String scatname; // 지출결재카테고리명 (1:지출결의서, 2:법인카드사용신청서)
	 
	 //지출결의서
	 private String exno;      //지출 순번
	 private String fk_scat;   // 지출결재카테고리
	 
	 private String exdate;    // 지출일자
	 private String exprice;      // 지출금액
	 
	 //법인카드
	 private String cono;      // 순번
	 private String codate;    // 사용예정일
	 private String cocardnum; //카드번호
	 private String copurpose; // 지출목적 (1:교통비, 2:사무비품, 3:주유비, 4:출장비, 5:식비, 6:기타)
	 private String coprice;   //예상금액

	 
	 //근태결재
	 private String vno;  // 근태결재카테고리 (1:병가, 2:반차, 3:연차, 4:경조휴가, 5:출장, 6:추가근무)
	 private String vcatname;// 근태결재카테고리명 (1:병가, 2:반차, 3:연차, 4:경조휴가, 5:출장, 6:추가근무)
	 
	 //병가
	 private String slno; // 순번
	 private String fk_vno; //근태결재카테고리 
	 
	 private String slstart;  // 사용예정시작일자
	 private String slend;   // 사용예정마지막일자
	 private String sldates;   //사용일수
	 
	 //반차
	 private String afno;// 순번
	 private String afdate;//사용예정일
	 private String afdan;  // 오전오후 (1: 오전, 2:오후)
	 private String afdates; // 사용일수 (0.5)
	 
	 //연차
	 private String dayno;// 순번
	 private String daystart;// 사용예정시작일자
	 private String dayend;  //사용예정마지막일자
	 private String daydates;   // 사용일수
	 
	 //경조휴가
	 private String congno;     // 순번
	 private String congstart;  // 사용예정시작일자
	 private String congend;   // 사용예정마지막일자
	 private String congdates;   // 사용일수
	 
	 
	// 출장
	private String buno;
	private String bustart;		// 사용예정시작일자
	private String buend;		// 사용예정마지막일자
	private String budates;		// 사용일수
	private String buplace;		// 출장지
	private String bupeople;		// 출장인원
	
	// 추가근무
	private String ewno;
	private String ewdate;		// 사용예정일
	private String ewhours;		// 추가근무시간
 
	 
	
	//Member
	private String employeeid;     // 사번
	private String fk_dcode; 	   // 부서코드
	private String fk_pcode;	   // 직급코드
	private String name;           // 직원명
	private String passwd;		   // 비밀번호
	private String email;   	   // 회사이메일
	private String mobile;   	   // 연락처
	private String cmobile;  	   // 회사연락처
	private String jubun;   	   // 주민번호
	private String hiredate;  	   // 입사일자
	private String status;  	   // 재직상태 (0:재직중 , 1:퇴사)
	
	private String employeeimg;    // 직원이미지
	
//////
	   
/////// 부서
	   private String dcode;        // 부서코드
	   private String dname;		 // 부서명
	   private String managerid;	 // 부서장사번
	   private String duty;  		 // 직무
		
//////
	 //직급
	   private String pname;  // 직급명
	   private String pcode;  // 직급명
	   
	   
	





	/////////////전자결재 의견작성
	   private String ono;      // 의견순번
	   private String ocontent; // 의견내용
	   private String odate;    // 작성일자
	   
////////////결재로그
	  private String logstatus;	// 결재상태
	  private String logdate;		// 결재일자
	   
	   
	   
	   
	   
	public String getAno() {
		return ano;
	}
	public void setAno(String ano) {
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
	
	public String getApaper() {
		return apaper;
	}
	public void setApaper(String apaper) {
		this.apaper = apaper;
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
	
	
	////// MEMBER
	
	
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
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCmobile() {
		return cmobile;
	}
	public void setCmobile(String cmobile) {
		this.cmobile = cmobile;
	}
	public String getJubun() {
		return jubun;
	}
	public void setJubun(String jubun) {
		this.jubun = jubun;
	}
	public String getHiredate() {
		return hiredate;
	}
	public void setHiredate(String hiredate) {
		this.hiredate = hiredate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getEmployeeimg() {
		return employeeimg;
	}
	public void setEmployeeimg(String employeeimg) {
		this.employeeimg = employeeimg;
	}
	
	////Member///////
	
	
	////일반결재문서
	public String getNcat() {
		return ncat;
	}
	public void setNcat(String ncat) {
		this.ncat = ncat;
	}
	public String getFk_ano() {
		return fk_ano;
	}
	public void setFk_ano(String fk_ano) {
		this.fk_ano = fk_ano;
	}
	public String getNcatname() {
		return ncatname;
	}
	public void setNcatname(String ncatname) {
		this.ncatname = ncatname;
	}
	
	////부서 
	
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
	
	
	public String getManagerid() {
		return managerid;
	}
	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}
	
	public String getDuty() {
		return duty;
	}
	public void setDuty(String duty) {
		this.duty = duty;
	}

	//직급
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	
	
	
	public String getMno() {
		return mno;
	}
	
	public void setMno(String mno) {
		this.mno = mno;
	}
	public String getMdate() {
		return mdate;
	}
	public void setMdate(String mdate) {
		this.mdate = mdate;
	}
	public String getWno() {
		return wno;
	}
	public void setWno(String wno) {
		this.wno = wno;
	}
	public String getFk_wiimdate() {
		return fk_wiimdate;
	}
	public void setFk_wiimdate(String fk_wiimdate) {
		this.fk_wiimdate = fk_wiimdate;
	}
	public String getEno() {
		return eno;
	}
	public void setEno(String eno) {
		this.eno = eno;
	}
	public String getCno() {
		return cno;
	}
	public void setCno(String cno) {
		this.cno = cno;
	}
	public String getComname() {
		return comname;
	}
	public void setComname(String comname) {
		this.comname = comname;
	
	}
	
	
	//전자결재 의견작성
	public String getOno() {
		return ono;
	}
	public void setOno(String ono) {
		this.ono = ono;
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
	
	
	
	
	
	// 지출결재 //
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
	public String getExno() {
		return exno;
	}
	public void setExno(String exno) {
		this.exno = exno;
	}
	public String getFk_scat() {
		return fk_scat;
	}
	public void setFk_scat(String fk_scat) {
		this.fk_scat = fk_scat;
	}
	public String getExdate() {
		return exdate;
	}
	public void setExdate(String exdate) {
		this.exdate = exdate;
	}
	public String getExprice() {
		return exprice;
	}
	public void setExprice(String exprice) {
		this.exprice = exprice;
	}
	public String getCono() {
		return cono;
	}
	public void setCono(String cono) {
		this.cono = cono;
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
	public String getCoprice() {
		return coprice;
	}
	public void setCoprice(String coprice) {
		this.coprice = coprice;
	}
	
	
	
	// 근태
	
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
	public String getSlno() {
		return slno;
	}
	public void setSlno(String slno) {
		this.slno = slno;
	}
	public String getFk_vno() {
		return fk_vno;
	}
	public void setFk_vno(String fk_vno) {
		this.fk_vno = fk_vno;
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
	public String getSldates() {
		return sldates;
	}
	public void setSldates(String sldates) {
		this.sldates = sldates;
	}
	public String getAfno() {
		return afno;
	}
	public void setAfno(String afno) {
		this.afno = afno;
	}
	public String getAfdate() {
		return afdate;
	}
	public void setAfdate(String afdate) {
		this.afdate = afdate;
	}
	public String getAfdan() {
		return afdan;
	}
	public void setAfdan(String afdan) {
		this.afdan = afdan;
	}
	public String getAfdates() {
		return afdates;
	}
	public void setAfdates(String afdates) {
		this.afdates = afdates;
	}
	public String getDayno() {
		return dayno;
	}
	public void setDayno(String dayno) {
		this.dayno = dayno;
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
	public String getDaydates() {
		return daydates;
	}
	public void setDaydates(String daydates) {
		this.daydates = daydates;
	}
	public String getCongno() {
		return congno;
	}
	public void setCongno(String congno) {
		this.congno = congno;
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
	public String getCongdates() {
		return congdates;
	}
	public void setCongdates(String congdates) {
		this.congdates = congdates;
	}
	public String getBuno() {
		return buno;
	}
	public void setBuno(String buno) {
		this.buno = buno;
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
	public String getBudates() {
		return budates;
	}
	public void setBudates(String budates) {
		this.budates = budates;
	}
	public String getBuplace() {
		return buplace;
	}
	public void setBuplace(String buplace) {
		this.buplace = buplace;
	}
	public String getBupeople() {
		return bupeople;
	}
	public void setBupeople(String bupeople) {
		this.bupeople = bupeople;
	}
	public String getEwno() {
		return ewno;
	}
	public void setEwno(String ewno) {
		this.ewno = ewno;
	}
	public String getEwdate() {
		return ewdate;
	}
	public void setEwdate(String ewdate) {
		this.ewdate = ewdate;
	}
	public String getEwhours() {
		return ewhours;
	}
	public void setEwhours(String ewhours) {
		this.ewhours = ewhours;
	}



// 결재로그
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
	
	

	
	
	//rno 받아오기
	private String rno;


	public String getRno() {
		return rno;
	}
	public void setRno(String rno) {
		this.rno = rno;
	} 
	
	
	
	
	
	
	
	
	
	
}
