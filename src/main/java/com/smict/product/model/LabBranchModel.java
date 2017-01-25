package com.smict.product.model;
 

public class LabBranchModel {
	
	private String lab_id;
	private String lab_name;
	private String addr_id;
	private String branch_id;
	private String branch_name;
	
	//Contructors
	public LabBranchModel(){}
 
	public LabBranchModel(String lab_id, String lab_name, String addr_id, String branch_id, String branch_name) {
		super();
		this.lab_id = lab_id;
		this.lab_name = lab_name;
		this.addr_id = addr_id;
		this.branch_id = branch_id;
		this.branch_name = branch_name;
	}

	//Reset
	public void Reset_LabModel(){
		this.lab_id = "";
		this.lab_name = "";
		this.addr_id = ""; 
	}

	public String getLab_id() {
		return lab_id;
	}

	public void setLab_id(String lab_id) {
		this.lab_id = lab_id;
	}

	public String getLab_name() {
		return lab_name;
	}

	public void setLab_name(String lab_name) {
		this.lab_name = lab_name;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	public String getBranch_id() {
		return branch_id;
	}

	public void setBranch_id(String branch_id) {
		this.branch_id = branch_id;
	}

	public String getBranch_name() {
		return branch_name;
	}

	public void setBranch_name(String branch_name) {
		this.branch_name = branch_name;
	}
	
	//Get Set
}
