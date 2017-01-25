package com.smict.person.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.smict.all.model.ContypeModel;
import com.smict.all.model.ServicePatientModel;
import com.smict.person.data.PatContypeData;
import com.smict.person.data.PatientData;
import com.smict.person.model.PatientModel;
import com.smict.treatment.action.TreatmentAction;

import ldc.util.CalculateNumber;

public class ContypeAction extends ActionSupport {

	ContypeModel patContypeModel;
	ServicePatientModel servicePatModel;
	String alertStatus, alertMessage;
	
	public void setSessionToServicePatModel(){
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();  
		PatientAction patAction = new PatientAction();
		patAction.getServiceModelNewData(request);
		servicePatModel = (ServicePatientModel) session.getAttribute("ServicePatientModel");
	}
	
	public String getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(String alertStatus) {
		this.alertStatus = alertStatus;
	}

	public String getAlertMessage() {
		return alertMessage;
	}

	public void setAlertMessage(String alertMessage) {
		this.alertMessage = alertMessage;
	}

	public ServicePatientModel getServicePatModel() {
		return servicePatModel;
	}

	public void setServicePatModel(ServicePatientModel servicePatModel) {
		this.servicePatModel = servicePatModel;
	}

	public ContypeModel getPatContypeModel() {
		return patContypeModel;
	}

	public void setPatContypeModel(ContypeModel patContypeModel) {
		this.patContypeModel = patContypeModel;
	}
	
	public String renewalMember() throws Exception{
		
		HttpServletRequest request = ServletActionContext.getRequest();
		setSessionToServicePatModel();
		TreatmentAction treatAction = new TreatmentAction();
		treatAction.setToothList(request);
		
		PatContypeData patConData = new PatContypeData();
		
		if(patConData.renewalMember(patContypeModel)){
			alertStatus = "success";
			alertMessage = "ต่ออายุสมาชิกสำเร็จ";
		}
		
		PatientModel patModel = new PatientModel();
		CalculateNumber classCalNum = new CalculateNumber();
		
		setSessionToServicePatModel();
		return SUCCESS;
	}
	
	public String beginAddPatientContype(){
		
		setSessionToServicePatModel();
		if(servicePatModel == null){
			return "getCustomer";
		}
		
		
		return SUCCESS;
	}
	
	public String addPatientContype(){
		
		setSessionToServicePatModel();
		PatContypeData patcontDB = new PatContypeData();
		
		if(patcontDB.addPatContype(servicePatModel.getHn(), patContypeModel.getSub_contact_id())) {
			alertStatus = "success";
			alertMessage = "เพิ่มประเภทสมาชิกสำเร็จ";
		}else{
			alertStatus = "danger";
			alertMessage = "เพิ่มประเภทสมาชิกไม่สำเร็จ";
		}
		setSessionToServicePatModel();
		return SUCCESS;
	}
	
	public String deletePatientContype(){
		
		setSessionToServicePatModel();
		PatContypeData patcontDB = new PatContypeData();
		
		if(patcontDB.deletePatContype(servicePatModel.getHn(), patContypeModel.getPatient_contypeid())) {
			alertStatus = "success";
			alertMessage = "ลบประเภทสมาชิกสำเร็จ";
		}else{
			alertStatus = "danger";
			alertMessage = "ลบประเภทสมาชิกไม่สำเร็จ";
		}
		setSessionToServicePatModel();
		return SUCCESS;
	}
}
