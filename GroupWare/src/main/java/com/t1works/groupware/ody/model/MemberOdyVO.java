package com.t1works.groupware.ody.model;

public class MemberOdyVO {
	
	   
	   private String employeeid;      // 사번
	   private String name;            // 직원명
	   private String dname;           // 부서명
	   private String dcode;
	   private String fk_dcode; 
	   private String fk_pcode;
	   
	   ///////////////////////////////////
	   private boolean chattingAttend = false;
	   
	   ////////////////////
	   


	public String getDcode() {
		   return dcode;
	   }
	
		public void setDcode(String dcode) {
			this.dcode = dcode;
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
	
		public String getEmployeeid() {
		      return employeeid;
	   }
	   
	   public void setEmployeeid(String employeeid) {
	      this.employeeid = employeeid;
	   }
	   	   
	   public String getName() {
	      return name;
	   }
	   
	   public void setName(String name) {
	      this.name = name;
	   }

	   public String getDname() {
			return dname;
	   }
	
	   public void setDname(String dname) {
			this.dname = dname;
	   }
	  ///////////////////////////////////////////////
	   //채팅 참여자 유무
	   
	   public boolean isChattingAttend() {
		   	return chattingAttend;
	   }

	   public void setChattingAttend(boolean chattingAttend) {
		   this.chattingAttend = chattingAttend;
	   }
///////////////////////////////////////////////////////
	   
	
}
