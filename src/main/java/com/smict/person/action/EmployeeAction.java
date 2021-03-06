package com.smict.person.action;

import java.io.File;
import java.io.IOException;
import com.opensymphony.xwork2.ActionSupport;
import com.smict.person.data.AddressData;
import com.smict.person.data.DoctorData;
import com.smict.person.data.EmployeeData;
import com.smict.person.data.FamilyData;
import com.smict.person.data.PatientData;
import com.smict.person.data.Pre_nameData;
import com.smict.person.data.TelephoneData;
import com.smict.person.model.AddressModel;
import com.smict.person.model.FamilyModel;
import com.smict.person.model.PatientModel;
import com.smict.person.model.Person;
import com.smict.person.model.Pre_nameModel;
import com.smict.person.model.TelephoneModel;
import ldc.util.Auth;
import ldc.util.DateUtil;
import ldc.util.Encrypted;
import ldc.util.Storage;
import ldc.util.Validate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.ServletActionContext;

@SuppressWarnings("serial")
public class EmployeeAction extends ActionSupport{
	private Person employeemodel;
	private Map<String,String> branchlist;
	private List<Person> employeelist;
	private FamilyModel famModel;
	private HashMap<String, String> telType = new HashMap<String, String>();
	private TelephoneModel telModel = new TelephoneModel();
	private TelephoneModel emTelModel = new TelephoneModel();
	private List<TelephoneModel> telList = new ArrayList<TelephoneModel>();
	
	/**
	 * CONSTRUCTOR
	 */
	/**
	 * FILE UPLOADING
	 */
	private File picProfile;
	private String picProfileContentType;
	private String picProfileFileName;
	public EmployeeAction(){
		Auth.authCheck(false);
	}
	
	public String getemployeelist() throws IOException, Exception{
		EmployeeData employeedata = new EmployeeData();
		setEmployeelist(employeedata.getListemployee());
		EmployeeData empdata1 = new EmployeeData();
		setBranchlist(empdata1.Get_branchList());
		return NONE;
	}
	public String addemployee() throws IOException, Exception{

		EmployeeData empdata = new EmployeeData();
		setBranchlist(empdata.Get_branchList());

		/**
		 * Fetch telephone type.
		 */
		DoctorData docDB = new DoctorData();
		setTelType(docDB.getTelephoneTypeList());
		
		return NONE;
	}
	
	public String excute() throws IOException, Exception{
		HttpServletRequest request = ServletActionContext.getRequest(); 
		AddressData addrData = new AddressData();
		
		/**
		 * Encrypt the password
		 */
		employeemodel.setEmppassword(Auth.encrypt(employeemodel.getEmppassword()));
		
		/**
		 * Address.
		 */
		List <AddressModel>addrlist = new ArrayList<AddressModel>();
		String[] 	addr_no = request.getParameterValues("employeemodel.addr_no"),
					addr_bloc = request.getParameterValues("employeemodel.addr_bloc"),
					addr_village = request.getParameterValues("employeemodel.addr_village"),
					addr_alley = request.getParameterValues("employeemodel.addr_alley"),
					addr_road = request.getParameterValues("employeemodel.addr_road"),
					addr_provinceid = request.getParameterValues("employeemodel.addr_provinceid"),
					addr_aumphurid = request.getParameterValues("employeemodel.addr_aumphurid"),
					addr_districtid = request.getParameterValues("employeemodel.addr_districtid"),
					addr_typeid = request.getParameterValues("employeemodel.addr_typeid"),
					addr_zipcode = request.getParameterValues("employeemodel.addr_zipcode");	
		
		int i = 0;
		
		for(String addr_list : addr_no){
			if(!addr_list.equals("") || !addr_bloc[i].equals("")|| !addr_village[i].equals("")|| !addr_alley[i].equals("")
					|| !addr_road[i].equals("")|| !addr_provinceid[i].equals("")|| !addr_districtid[i].equals("")|| !addr_aumphurid[i].equals("")){
				AddressModel addrModel = new AddressModel();
				addrModel.setAddr_no(addr_list);
				addrModel.setAddr_bloc(addr_bloc[i]);
				addrModel.setAddr_village(addr_village[i]);
				addrModel.setAddr_alley(addr_alley[i]);
				addrModel.setAddr_road(addr_road[i]);
				addrModel.setAddr_provinceid(addr_provinceid[i]);
				addrModel.setAddr_aumphurid(addr_aumphurid[i]);
				addrModel.setAddr_districtid(addr_districtid[i]);
				addrModel.setAddr_typeid(addr_typeid[i]);
				addrModel.setAddr_zipcode(addr_zipcode[i]);
				addrlist.add(addrModel);
				i++;
			}
		}
		if(addrlist.size()>0){
			employeemodel.setAddr_id(addrData.add_multi_address(addrlist));
		}

		/**
		 * Telephone
		 */
		TelephoneData telData = new TelephoneData();
		employeemodel.setTel_id(telData.add_multi_telephone(telModel));
		
		/**
		 * Birth date.
		 */
		String birthDateEn = request.getParameter("birthdate_eng");
		String birthDateTh = request.getParameter("birthdate_th");
		String BirthDate="";
		if(!birthDateEn.equals("")){
			String[] parts = birthDateEn.split("-");
			BirthDate = parts[2]+"-"+parts[1]+"-"+parts[0];
		}else if(!birthDateTh.equals("")){
			String[] parts = birthDateTh.split("-");
			int convertDate =  Integer.parseInt(parts[2]);
			convertDate -= 543;
			BirthDate = convertDate+"-"+parts[1]+"-"+parts[0];
		}
		employeemodel.setBirth_date(BirthDate);
		/**
		 * UPLOAD PICTURE FILE.
		 */
		if(getPicProfileFileName() != null){
			String time = new DateUtil().curTime();
			String fName = new Encrypted().encrypt(employeemodel.getFirstname_en() + "-" + employeemodel.getLastname_en() + "-" + time).replaceAll("[-+.^:=/\\,]","");
			employeemodel.setProfile_pic(
					new Storage().file(getPicProfile(), getPicProfileContentType(), getPicProfileFileName())
						.storeAs("../Document/picture/profile/", fName)
						.getDestPath()
			);
		}
		/**
		 * Hire date
		 */
		
		String HireDateEn = request.getParameter("hiredate_eng");
		String HireDateTh = request.getParameter("hiredate_th");
		String HireDate="";
		if(!HireDateEn.equals("")){
			String[] parts1 = HireDateEn.split("-");
			HireDate = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		}else if(!HireDateTh.equals("")){
			String[] parts1 = HireDateTh.split("-");
			int converthireDate =  Integer.parseInt(parts1[2]);
			converthireDate -= 543;
			HireDate = converthireDate+"-"+parts1[1]+"-"+parts1[0];
		}
		employeemodel.setHired_date(HireDate);
		
	//	int fami_id = Integer.parseInt(request.getParameter("family_id"));
		
		EmployeeData employeedata = new EmployeeData();
		employeedata.addemployeeinsert(employeemodel);
		
		//List
		EmployeeData empdata = new EmployeeData();
		setBranchlist(empdata.Get_branchList());
		
		EmployeeData employeelistdata = new EmployeeData();
		setEmployeelist(employeelistdata.getListemployee());
		//endList
		return SUCCESS;
	}
	
	public String editemployee() throws IOException, Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		AddressData addrData = new AddressData();
		TelephoneData telData = new TelephoneData();
		Pre_nameData pnameData = new Pre_nameData();
		PatientData pData = new PatientData();
		FamilyData famData = new FamilyData();

		String emp_id = request.getParameter("pro_id");		
		EmployeeData employeedata = new EmployeeData();
		setEmployeemodel(employeedata.editemployee(emp_id));
		
		String birthDateTh  = employeemodel.getBirth_date();
		if(new Validate().Check_String_notnull_notempty(birthDateTh)){
			String[] parts = birthDateTh.split("-");
			int convertDate =  Integer.parseInt(parts[0]);
			convertDate += 543;
			birthDateTh = parts[2]+"-"+parts[1]+"-"+convertDate;
			employeemodel.setBirth_date(birthDateTh);
		}

		List<AddressModel> AddrList = new ArrayList<AddressModel>();
		List<Pre_nameModel> pnameList = new ArrayList<Pre_nameModel>();
		List<PatientModel> pList = new ArrayList<PatientModel>();
		AddrList = addrData.getMultiAddr(employeemodel.getAddr_id());
		request.setAttribute("addressList", AddrList);
		//System.out.println("-get addrlist success "+dateFormat.format(new Date()));

		/**
		 * Get get multiple telephone list.
		 */
		telList = telData.get_telList(employeemodel.getTel_id());
		request.setAttribute("telList", telList);
		//System.out.println("-get tel success "+dateFormat.format(new Date()));

		/**
		 * Get emergency telephone.
		 */
		emTelModel = telData.getEmergencyTelById(employeemodel.getTel_id());
		
		pList = pData.get_identification_type(employeemodel.getIdentification_type());
		request.setAttribute("pList", pList);
	//	System.out.println("-get iden success "+dateFormat.format(new Date()));
		
		pnameList = pnameData.select_pre_name(employeemodel.getPre_name_id(), "", "");
		request.setAttribute("pnameList", pnameList);
		//System.out.println("-get pre name success "+dateFormat.format(new Date()));
		
		famModel = new FamilyModel();
		employeemodel.setFam_id(famData.getFamilyID(employeemodel.getIdentification()));
		
		EmployeeData empdata1 = new EmployeeData();
		setBranchlist(empdata1.Get_branchList());

		/**
		 * Fetch telephone type.
		 */
		DoctorData docDB = new DoctorData();
		setTelType(docDB.getTelephoneTypeList());
		
		return SUCCESS;
	}
	
	
	public String empupdate() throws IOException, Exception{
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		//System.out.println("Start update ----------------"+ dateFormat.format(new Date())); 
		HttpServletRequest request = ServletActionContext.getRequest(); 
		AddressData addrData = new AddressData();
		TelephoneData telData = new TelephoneData();
		List <TelephoneModel> tellist = new ArrayList<TelephoneModel>();
		List <AddressModel>addrlist = new ArrayList<AddressModel>();
		EmployeeData empdataaddr = new EmployeeData();
		Person employeeaddr = new Person();
		employeeaddr = empdataaddr.editemployee(employeemodel.emp_id);
		employeemodel.setAddr_id(employeeaddr.getAddr_id());
		employeemodel.setTel_id(employeeaddr.getTel_id());
		employeemodel.setFam_id(employeeaddr.getFam_id());
		String[] 	addr_no = request.getParameterValues("employeemodel.addr_no"),
				addr_bloc = request.getParameterValues("employeemodel.addr_bloc"),
				addr_village = request.getParameterValues("employeemodel.addr_village"),
				addr_alley = request.getParameterValues("employeemodel.addr_alley"),
				addr_road = request.getParameterValues("employeemodel.addr_road"),
				addr_provinceid = request.getParameterValues("employeemodel.addr_provinceid"),
				addr_aumphurid = request.getParameterValues("employeemodel.addr_aumphurid"),
				addr_districtid = request.getParameterValues("employeemodel.addr_districtid"),
				addr_typeid = request.getParameterValues("employeemodel.addr_typeid"),
				addr_zipcode = request.getParameterValues("employeemodel.addr_zipcode");
	
		String[] tel = request.getParameterValues("tel_number");
		String[] teltype = request.getParameterValues("teltype");
		int i = 0;
		
		for(String addr_list : addr_no){
			if(!addr_list.equals("") || !addr_bloc[i].equals("")|| !addr_village[i].equals("")|| !addr_alley[i].equals("")
					|| !addr_road[i].equals("")|| !addr_provinceid[i].equals("")|| !addr_districtid[i].equals("")|| !addr_aumphurid[i].equals("")){
				AddressModel addrModel = new AddressModel();
				addrModel.setAddr_no(addr_list);
				addrModel.setAddr_bloc(addr_bloc[i]);
				addrModel.setAddr_village(addr_village[i]);
				addrModel.setAddr_alley(addr_alley[i]);
				addrModel.setAddr_road(addr_road[i]);
				addrModel.setAddr_provinceid(addr_provinceid[i]);
				addrModel.setAddr_aumphurid(addr_aumphurid[i]);
				addrModel.setAddr_districtid(addr_districtid[i]);
				addrModel.setAddr_typeid(addr_typeid[i]);
				addrModel.setAddr_zipcode(addr_zipcode[i]);
				addrlist.add(addrModel);
				
			}
			i++;
		}
		if(addrlist.size()>0){
			addrData.del_multi_address(employeemodel.getAddr_id());
			addrData.add_multi_address(addrlist,employeemodel.getAddr_id());
		}else{
//			addrData.del_multi_address(employeemodel.getAddr_id());
		}
		/**
		 * UPLOAD PICTURE FILE.
		 */
		if(getPicProfileFileName() != null){
			String time = new DateUtil().curTime();
			String fName = new Encrypted().encrypt(employeemodel.getFirstname_en() + "-" + employeemodel.getLastname_en() + "-" + time).replaceAll("[-+.^:=/\\,]","");
			employeemodel.setProfile_pic(
					new Storage().file(getPicProfile(), getPicProfileContentType(), getPicProfileFileName())
						.storeAs("../Document/picture/profile/", fName)
						.getDestPath()
			);
			
		}
		
		/**
		 * DELETE OLD FILE PICTURE WHEN HAVE NEW PROFILE PICTURE.
		 */
		if(!employeemodel.getProfile_pic().equals(employeeaddr.getProfile_pic())){
			// Delete old file
			new Storage().delete(employeeaddr.getProfile_pic());
		}

		/**
		 * Telephone.
		 */
		telData.updateMultiTelephone(employeemodel.getTel_id(), telModel);
		
		//System.out.println("-tel updated"+dateFormat.format(new Date()));
		String birthDateEn = request.getParameter("birthdate_eng");
		String birthDateTh = request.getParameter("birthdate_th");
		String BirthDate="";
		
		if(!birthDateEn.equals("")){
			String[] parts = birthDateEn.split("-");
			BirthDate = parts[2]+"-"+parts[1]+"-"+parts[0];
		}else if(!birthDateTh.equals("")){
			String[] parts = birthDateTh.split("-");
			int convertDate =  Integer.parseInt(parts[2]);
			convertDate -= 543;
			BirthDate = convertDate+"-"+parts[1]+"-"+parts[0];
		}
		employeemodel.setBirth_date(BirthDate);
		String HireDateEn = request.getParameter("hiredate_eng");
		String HireDateTh = request.getParameter("hiredate_th");
		String HireDate="";
		
		if(!HireDateEn.equals("")){
			String[] parts1 = HireDateEn.split("-");
			HireDate = parts1[2]+"-"+parts1[1]+"-"+parts1[0];
		}else if(!HireDateTh.equals("")){
			String[] parts1 = HireDateTh.split("-");
			int converthireDate =  Integer.parseInt(parts1[2]);
			converthireDate -= 543;
			HireDate = converthireDate+"-"+parts1[1]+"-"+parts1[0];
		}
		employeemodel.setHired_date(HireDate);
		EmployeeData employeedataupdate = new EmployeeData();
		
		employeedataupdate.empupdate(employeemodel);
		
		//List
		EmployeeData empdata = new EmployeeData();
		setBranchlist(empdata.Get_branchList());
		EmployeeData employeelistdata = new EmployeeData();
		setEmployeelist(employeelistdata.getListemployee());
		//endList
		
		return SUCCESS;
	}
	public String empsearch() throws IOException, Exception{		
		EmployeeData empdata1 = new EmployeeData();
		setBranchlist(empdata1.Get_branchList());
		EmployeeData employeedata = new EmployeeData();
		setEmployeelist(employeedata.getListemployeeSearch(employeemodel.getWork_status(),employeemodel.getBranch_id()));
		
		return SUCCESS;
	}
	public String empstatus() throws IOException, Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		String emp_id = request.getParameter("pro_id");
		EmployeeData employeedata1 = new EmployeeData();
		setEmployeemodel(employeedata1.editemployee(emp_id));
		EmployeeData employeedata2 = new EmployeeData();
		employeedata2.empsearchupdate(employeemodel);
		
		EmployeeData empdata1 = new EmployeeData();
		setBranchlist(empdata1.Get_branchList());
		EmployeeData employeedata = new EmployeeData();
		String checkIF = employeemodel.getWork_status();
		if(checkIF.equals("1")){
			employeemodel.setWork_status("0");
			setEmployeelist(employeedata.getListemployeeSearch(employeemodel.getWork_status(),employeemodel.getBranch_id()));
		}else{
			employeemodel.setWork_status("1");
			setEmployeelist(employeedata.getListemployeeSearch(employeemodel.getWork_status(),employeemodel.getBranch_id()));
		}
		
		
		return SUCCESS;
	}
	
	//public void validateExcute(){
//		HttpServletRequest request = ServletActionContext.getRequest(); 
	//	String comfrim = request.getParameter("confirmpass");
	//	if(employeemodel.getEmppassword().length()<0 || employeemodel.getEmppassword()!= comfrim ){
	//		addFieldError("employeemodel.emppassword","Please review your password");
	//	}
//	}
	public Person getEmployeemodel() {
		return employeemodel;
	}

	public void setEmployeemodel(Person employeemodel) {
		this.employeemodel = employeemodel;
	}

	public Map<String,String> getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(Map<String,String> branchlist) {
		this.branchlist = branchlist;
	}

	public List<Person> getEmployeelist() {
		return employeelist;
	}

	public void setEmployeelist(List<Person> employeelist) {
		this.employeelist = employeelist;
	}
	public FamilyModel getFamModel() {
		return famModel;
	}
	public void setFamModel(FamilyModel famModel) {
		this.famModel = famModel;
	}

	public HashMap<String, String> getTelType() {
		return telType;
	}

	public void setTelType(HashMap<String, String> telType) {
		this.telType = telType;
	}

	public TelephoneModel getTelModel() {
		return telModel;
	}

	public void setTelModel(TelephoneModel telModel) {
		this.telModel = telModel;
	}

	public List<TelephoneModel> getTelList() {
		return telList;
	}

	public void setTelList(List<TelephoneModel> telList) {
		this.telList = telList;
	}

	public TelephoneModel getEmTelModel() {
		return emTelModel;
	}

	public void setEmTelModel(TelephoneModel emTelModel) {
		this.emTelModel = emTelModel;
	}

	public File getPicProfile() {
		return picProfile;
	}

	public void setPicProfile(File picProfile) {
		this.picProfile = picProfile;
	}

	public String getPicProfileContentType() {
		return picProfileContentType;
	}

	public void setPicProfileContentType(String picProfileContentType) {
		this.picProfileContentType = picProfileContentType;
	}

	public String getPicProfileFileName() {
		return picProfileFileName;
	}

	public void setPicProfileFileName(String picProfileFileName) {
		this.picProfileFileName = picProfileFileName;
	}


}
