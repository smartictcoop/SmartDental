package com.smict.appointment.action;

import java.util.List;

public class AppointmentModel {
	
	private int appointmentID;
	private int doctorID, lab_id;
	private String postponeReferenceID;
	private int remindDate;
	private String appointmentCode;
	private String branchCode, branchID,authenBranchcode;
	private String dateStart, dateEnd, dateTimeZoneStart, dateTimeZoneEnd, date, timeStart, timeEnd,dateToday,datetodayend;
	private int contactStatus, appointmentStatus,appconstatus;
	private String description,appointCode,recommend,referID,conractdes,contactdate,contimestart;
	private List<AppointmentModel> appoinmentList;
	private int remindDateCount;
	private String reason;
	
	/**
	 * Appointment Code.
	 */
	private int appointmentCodeID;
	private String prefix;
	private char separator;
	private int length, nextNumber, increment;
	
	/**
	 * Patients
	 */
	private String HN,branch_hn;
	private String firstNameTH, lastNameTH,patPrenameth,pattimestart,pattimeend;
	private String firstNameEN, lastNameEN;
	private String identification;
	
	/**
	 * Symptom
	 */
	private String symptom,sympDescription;
	private int symptomID;
	
	/**
	 * branch
	 */
	private String branchName;
	
	
	/**
	 * doctor
	 */
	private String docfirstname,doclastname,docprenameth;
	private int docid;
	private String colour;
	
	/**
	 * doctor
	 */
	private String create_date_lab,require_date_lab,update_date_lab,status_lab; 
	
	/**
	 * GETTER & SETTER ZONE
	 */
	
	public int getAppointmentID() {
		return appointmentID;
	}
	public int getDoctorID() {
		return doctorID;
	}
	public String getHN() {
		return HN;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public String getBranchID() {
		return branchID;
	}
	public String getDateStart() {
		return dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public String getDescription() {
		return description;
	}
	public List<AppointmentModel> getAppoinmentList() {
		return appoinmentList;
	}
	public void setAppointmentID(int appointmentID) {
		this.appointmentID = appointmentID;
	}
	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}
	public void setHN(String hN) {
		HN = hN;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void setAppoinmentList(List<AppointmentModel> appoinmentList) {
		this.appoinmentList = appoinmentList;
	}
	public String getFirstNameTH() {
		return firstNameTH;
	}
	public String getLastNameTH() {
		return lastNameTH;
	}
	public String getFirstNameEN() {
		return firstNameEN;
	}
	public String getLastNameEN() {
		return lastNameEN;
	}
	public String getIdentification() {
		return identification;
	}
	public void setFirstNameTH(String firstNameTH) {
		this.firstNameTH = firstNameTH;
	}
	public void setLastNameTH(String lastNameTH) {
		this.lastNameTH = lastNameTH;
	}
	public void setFirstNameEN(String firstNameEN) {
		this.firstNameEN = firstNameEN;
	}
	public void setLastNameEN(String lastNameEN) {
		this.lastNameEN = lastNameEN;
	}
	public void setIdentification(String identification) {
		this.identification = identification;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getSymptom() {
		return symptom;
	}
	public int getSymptomID() {
		return symptomID;
	}
	public void setSymptom(String symptom) {
		this.symptom = symptom;
	}
	public void setSymptomID(int symptomID) {
		this.symptomID = symptomID;
	}
	public String getDateTimeZoneStart() {
		return dateTimeZoneStart;
	}
	public String getDateTimeZoneEnd() {
		return dateTimeZoneEnd;
	}
	public void setDateTimeZoneStart(String dateTimeZoneStart) {
		this.dateTimeZoneStart = dateTimeZoneStart;
	}
	public void setDateTimeZoneEnd(String dateTimeZoneEnd) {
		this.dateTimeZoneEnd = dateTimeZoneEnd;
	}
	public String getTimeStart() {
		return timeStart;
	}
	public String getTimeEnd() {
		return timeEnd;
	}
	public void setTimeStart(String timeStart) {
		this.timeStart = timeStart;
	}
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	public int getContactStatus() {
		return contactStatus;
	}
	public int getAppointmentStatus() {
		return appointmentStatus;
	}
	public void setContactStatus(int contactStatus) {
		this.contactStatus = contactStatus;
	}
	public void setAppointmentStatus(int appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	public String getAppointmentCode() {
		return appointmentCode;
	}
	public void setAppointmentCode(String appointmentCode) {
		this.appointmentCode = appointmentCode;
	}
	public String getPostponeReferenceID() {
		return postponeReferenceID;
	}
	public void setPostponeReferenceID(String postponeReferenceID) {
		this.postponeReferenceID = postponeReferenceID;
	}
	public int getRemindDate() {
		return remindDate;
	}
	public void setRemindDate(int remindDate) {
		this.remindDate = remindDate;
	}
	public String getBranchName() {
		return branchName;
	}
	public String getDocfirstname() {
		return docfirstname;
	}
	public String getDoclastname() {
		return doclastname;
	}
	public int getDocid() {
		return docid;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public void setDocfirstname(String docfirstname) {
		this.docfirstname = docfirstname;
	}
	public void setDoclastname(String doclastname) {
		this.doclastname = doclastname;
	}
	public void setDocid(int docid) {
		this.docid = docid;
	}
	public String getAppointCode() {
		return appointCode;
	}
	public void setAppointCode(String appointCode) {
		this.appointCode = appointCode;
	}
	public String getRecommend() {
		return recommend;
	}
	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}
	public String getReferID() {
		return referID;
	}
	public void setReferID(String referID) {
		this.referID = referID;
	}
	public String getDocprenameth() {
		return docprenameth;
	}
	public void setDocprenameth(String docprenameth) {
		this.docprenameth = docprenameth;
	}
	public String getPatPrenameth() {
		return patPrenameth;
	}
	public void setPatPrenameth(String patPrenameth) {
		this.patPrenameth = patPrenameth;
	}
	public String getSympDescription() {
		return sympDescription;
	}
	public void setSympDescription(String sympDescription) {
		this.sympDescription = sympDescription;
	}
	public String getConractdes() {
		return conractdes;
	}
	public String getContactdate() {
		return contactdate;
	}
	public String getContimestart() {
		return contimestart;
	}
	public void setConractdes(String conractdes) {
		this.conractdes = conractdes;
	}
	public void setContactdate(String contactdate) {
		this.contactdate = contactdate;
	}
	public void setContimestart(String contimestart) {
		this.contimestart = contimestart;
	}
	public int getAppconstatus() {
		return appconstatus;
	}
	public void setAppconstatus(int appconstatus) {
		this.appconstatus = appconstatus;
	}
	public String getPattimestart() {
		return pattimestart;
	}
	public String getPattimeend() {
		return pattimeend;
	}
	public void setPattimestart(String pattimestart) {
		this.pattimestart = pattimestart;
	}
	public void setPattimeend(String pattimeend) {
		this.pattimeend = pattimeend;
	}
	public String getDateToday() {
		return dateToday;
	}
	public void setDateToday(String dateToday) {
		this.dateToday = dateToday;
	}
	public String getBranch_hn() {
		return branch_hn;
	}
	public void setBranch_hn(String branch_hn) {
		this.branch_hn = branch_hn;
	}
	public String getAuthenBranchcode() {
		return authenBranchcode;
	}
	public void setAuthenBranchcode(String authenBranchcode) {
		this.authenBranchcode = authenBranchcode;
	}
	public String getDatetodayend() {
		return datetodayend;
	}
	public void setDatetodayend(String datetodayend) {
		this.datetodayend = datetodayend;
	}
	public int getRemindDateCount() {
		return remindDateCount;
	}
	public void setRemindDateCount(int remindDateCount) {
		this.remindDateCount = remindDateCount;
	}
	public String getPrefix() {
		return prefix;
	}
	public char getSeparator() {
		return separator;
	}
	public int getLength() {
		return length;
	}
	public int getNextNumber() {
		return nextNumber;
	}
	public int getIncrement() {
		return increment;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public void setSeparator(char separator) {
		this.separator = separator;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setNextNumber(int nextNumber) {
		this.nextNumber = nextNumber;
	}
	public void setIncrement(int increment) {
		this.increment = increment;
	}
	public int getAppointmentCodeID() {
		return appointmentCodeID;
	}
	public void setAppointmentCodeID(int appointmentCodeID) {
		this.appointmentCodeID = appointmentCodeID;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getColour() {
		return colour;
	}
	public void setColour(String colour) {
		this.colour = colour;
	}
	public int getLab_id() {
		return lab_id;
	}
	public void setLab_id(int lab_id) {
		this.lab_id = lab_id;
	}
	public String getCreate_date_lab() {
		return create_date_lab;
	}
	public void setCreate_date_lab(String create_date_lab) {
		this.create_date_lab = create_date_lab;
	}
	public String getRequire_date_lab() {
		return require_date_lab;
	}
	public void setRequire_date_lab(String require_date_lab) {
		this.require_date_lab = require_date_lab;
	}
	public String getUpdate_date_lab() {
		return update_date_lab;
	}
	public void setUpdate_date_lab(String update_date_lab) {
		this.update_date_lab = update_date_lab;
	}
	public String getStatus_lab() {
		return status_lab;
	}
	public void setStatus_lab(String status_lab) {
		this.status_lab = status_lab;
	}

}
