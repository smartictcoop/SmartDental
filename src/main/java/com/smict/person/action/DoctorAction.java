package com.smict.person.action;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.smict.all.model.DoctTimeModel;
import com.smict.person.data.AddressData;
import com.smict.person.data.BookBankData;
import com.smict.person.data.BranchData;
import com.smict.person.data.DoctorData;
import com.smict.person.data.DoctorTypeData;
import com.smict.person.data.EducationData;
import com.smict.person.data.PatientData;
import com.smict.person.data.Pre_nameData;
import com.smict.person.data.TelephoneData;
import com.smict.person.data.WorkHistoryData;
import com.smict.person.model.AddressModel;
import com.smict.person.model.BookBankModel;
import com.smict.person.model.BranchModel;
import com.smict.person.model.DoctorModel;
import com.smict.person.model.PatientModel;
import com.smict.person.model.Person;
import com.smict.person.model.Pre_nameModel;
import com.smict.person.model.TelephoneModel;

import ldc.util.Auth;
import ldc.util.DateUtil;
import ldc.util.Servlet;
import ldc.util.Validate;

@SuppressWarnings("serial")
public class DoctorAction extends ActionSupport {
	DoctorModel docModel;
	DoctTimeModel docTimeM;
	TelephoneModel telModel;
	
	/**
	 * GETTER & SETTER.
	 */
	private HashMap<String, String> telType = new HashMap<String, String>();
	
	Map<String,String> branchlist;
	String docID,branchID;
	List<DoctorModel> branchStandardList, branchMgrList;

	/**
	 * CONSTRUCTOR
	 */

	public Map<String, String> getBranchlist() {
		return branchlist;
	}

	public List<DoctorModel> getBranchStandardList() {
		return branchStandardList;
	}

	public void setBranchStandardList(List<DoctorModel> branchStandardList) {
		this.branchStandardList = branchStandardList;
	}

	public List<DoctorModel> getBranchMgrList() {
		return branchMgrList;
	}

	public void setBranchMgrList(List<DoctorModel> branchMgrList) {
		this.branchMgrList = branchMgrList;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public String getBranchID() {
		return branchID;
	}

	public void setBranchID(String branchID) {
		this.branchID = branchID;
	}

	public void setBranchlist(Map<String, String> branchlist) {
		this.branchlist = branchlist;
	}

	public DoctorAction(){
		Auth.authCheck(false);
	}
	
	public String addDoctor(){
		DoctorData docDB = new DoctorData();
		/**
		 * Fetch telephone type. 
		 */
		setTelType(docDB.getTelephoneTypeList());
		
		return SUCCESS;
	}
	
	public String begin() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		DoctorData docData = new DoctorData();
		List<DoctorModel> docList = docData.Get_DoctorList(null);
		request.setAttribute("doctorList", docList); 
		
		return SUCCESS;
	}
	public String excute() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest(); 
		
		/**
		 * ADDRESS.
		 */
		AddressData addrData = new AddressData();
		List <AddressModel>addrlist = new ArrayList<AddressModel>();
		String[] 	addr_no = request.getParameterValues("docModel.addr_no"),
					addr_bloc = request.getParameterValues("docModel.addr_bloc"),
					addr_village = request.getParameterValues("docModel.addr_village"),
					addr_alley = request.getParameterValues("docModel.addr_alley"),
					addr_road = request.getParameterValues("docModel.addr_road"),
					addr_provinceid = request.getParameterValues("docModel.addr_provinceid"),
					addr_aumphurid = request.getParameterValues("docModel.addr_aumphurid"),
					addr_districtid = request.getParameterValues("docModel.addr_districtid"),
					addr_typeid = request.getParameterValues("docModel.addr_typeid"),
					addr_zipcode = request.getParameterValues("docModel.addr_zipcode");	
		
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
			docModel.setAddr_id(addrData.add_multi_address(addrlist));
		}

		/**
		 * TELEPHONE
		 */
		TelephoneData telData = new TelephoneData();
		docModel.setTel_id(telData.add_multi_telephone(telModel));


		DoctorData docData = new DoctorData();
		List <BranchModel> branchlist = new ArrayList<BranchModel>();
		String[] docbranch = request.getParameterValues("doctor_branch");
		if(docbranch!=null){
			for(String branch_list : docbranch){
				String branch_id = branch_list;
				BranchModel branchModel = new BranchModel();
				branchModel.setBranch_id(branch_id);
				branchlist.add(branchModel);
			}
		}
		if(branchlist.size()>0){
			docModel.setBranchID(docData.AddDoctorBranch(branchlist));
		}

		BookBankData bankData = new BookBankData();
		List <BookBankModel>bankList = new ArrayList<BookBankModel>();
		String[] account_num = request.getParameterValues("account_num");
		String[] account_name = request.getParameterValues("account_name");
		String[] bank_id = request.getParameterValues("bank_id");
		i = 0;
		for(String account : account_num){
			if(!account.equals("")){
				BookBankModel bankMo = new BookBankModel();
				bankMo.setBank_id(bank_id[i]);
				bankMo.setBookbank_no(account);
				bankMo.setBookbank_name(account_name[i]);
				bankList.add(bankMo);
				i++;
			}
		}
		if(bankList.size()>0){
			docModel.setBookBankId(bankData.add_multi_bookbank(bankList));
		}

		EducationData eduData = new EducationData();
		List <Person> eduList = new ArrayList<Person>();	
		String[] education_vocabulary_id = request.getParameterValues("docModel.education_vocabulary_id");
		String[] education_name = request.getParameterValues("docModel.education_name");
		i = 0;
		for(String edu_name : education_name){
			if(!edu_name.equals("")){
				Person perModel = new Person();
				perModel.setEducation_th(edu_name);
				perModel.setEducation_vocabulary_id(Integer.parseInt(education_vocabulary_id[i]));
				eduList.add(perModel);
			}
			i++;
		}
		if(eduList.size()>0){
			docModel.setEdu_id(eduData.add_multi_edu(eduList));
		} 
		
		WorkHistoryData workData = new WorkHistoryData();
		List <DoctorModel> workList = new ArrayList<DoctorModel>();
		String[] location = request.getParameterValues("docModel.location"),
				work_type = request.getParameterValues("docModel.work_type"),
				position = request.getParameterValues("docModel.position"),
				salary = request.getParameterValues("docModel.salary"),
				start_date = request.getParameterValues("docModel.start_date"),
				end_date = request.getParameterValues("docModel.end_date"),
				remark_message = request.getParameterValues("docModel.remark_message");	
		i=0;
		for(String lo : location){
			
			DoctorModel docM = new DoctorModel();
			
			String sdate = start_date[i];
			String edate = end_date[i];
			if(!sdate.equals("")){
				String[] parts = sdate.split("-");
				sdate = parts[2]+"-"+parts[1]+"-"+parts[0];
			}
			if(!edate.equals("")){
				String[] parts = edate.split("-");
				edate = parts[2]+"-"+parts[1]+"-"+parts[0];
			}
			
			docM.setLocation(lo);
			docM.setWork_type(work_type[i]);
			docM.setPosition(position[i]);
			docM.setSalary(salary[i]);
			docM.setRemark_message(remark_message[i]);
			docM.setStart_date(sdate);
			docM.setEnd_date(edate);
			
			workList.add(docM);
			i++;
		}
		if(workList.size()>0){
			docModel.setWork_history_id(workData.add_multi_work(workList));
		}
		
		/**
		 * Set birth date TH/EN.
		 */
		String birthDateEn = request.getParameter("birthdate_eng");
		String birthDateTh = request.getParameter("birthdate_th"),
				hireddate = request.getParameter("hireddate");
		System.out.println("hireddate : "+hireddate);
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
		docModel.setBirth_date(BirthDate);
		
		/**
		 * Set hire date.
		 */
		String hireDate = request.getParameter("hireddate");
		DateUtil d = new DateUtil();
		if(new ldc.util.Validate().Check_String_notnull_notempty(hireDate)){
			hireDate = d.convertDateSpecificationPattern("dd-mm-YYYY", "YYYY-mm-dd", hireDate, false) + " 00:00:00";
			docModel.setHireDate(hireDate);
		}else{
			/**
			 * Add to default & prevent to null.
			 */
			docModel.setHireDate("0000-00-00 00:00:00");
		}
		
		/**
		 * Add doctor
		 */
		int doc_id = docData.AddDoctor(docModel);
		setDocID(Integer.toString(doc_id));
		
		/**
		 * Make MGR branch list
		 */
		if(doc_id>0){
			List <BranchModel> mgrbranchlist = new ArrayList<BranchModel>();
			String[] MngBranch = request.getParameterValues("doctor_boss_branch");
			
			if(MngBranch!=null){
				i = 0;
				for(String mgnBranch : MngBranch){ 
					i++;
					BranchModel branchModel = new BranchModel();
					branchModel.setDoctor_id(doc_id);
					branchModel.setBranch_id(mgnBranch);
					mgrbranchlist.add(branchModel);
				}
				docData.UpdateMgrBranch(mgrbranchlist);
				
			}		
			
			return SUCCESS;
		}
		
		/*
		 for(TelephoneModel showModel : tellist){
			System.out.println("number "+showModel.getTel_number().toString());
			System.out.println("id "+showModel.getTel_typeid());
		 }
		*/
	
		return SUCCESS;
	}
	
	public String getDoctorDetail(){
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		//System.out.println("Start GetDoctorDetail ---------------------- "+ dateFormat.format(new Date())); 
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpSession session = request.getSession();
		
		int doctor_id;
		
		if(request.getParameter("d")!=null){
			doctor_id = Integer.parseInt(request.getParameter("d"));
		}else if(getDocID()!=null){
			doctor_id = Integer.parseInt(getDocID());
		}
		else{
			doctor_id =  (Integer) session.getAttribute("doc_id");
			session.removeAttribute("doc_id");
		}
		BranchData branchData = new BranchData();
		DoctorData docData = new DoctorData();
		AddressData addrData = new AddressData();
		TelephoneData telData = new TelephoneData();
		BookBankData bankData = new BookBankData();
		PatientData pData = new PatientData();
		Pre_nameData pnameData = new Pre_nameData();
		WorkHistoryData workData = new WorkHistoryData();
		EducationData eduData = new EducationData();
		try {
			
			docModel = docData.Get_DoctorDetail(doctor_id);
			docModel.setBirth_date_en(docModel.getBirth_date());
			String birthDateTh  = docModel.getBirth_date();
			if(new Validate().Check_String_notnull_notempty(birthDateTh)){
				String[] parts = birthDateTh.split("-");
				int convertDate =  Integer.parseInt(parts[0]);
				convertDate += 543;
				birthDateTh = parts[2]+"-"+parts[1]+"-"+convertDate;
				docModel.setBirth_date(birthDateTh);
			}
			//System.out.println("-get data doctor success "+dateFormat.format(new Date()));
			
			
			List<BranchModel> branchList = new ArrayList<BranchModel>();
			List<BranchModel> branchMGRList = new ArrayList<BranchModel>();
			List<AddressModel> AddrList = new ArrayList<AddressModel>();
			List<TelephoneModel> telList = new ArrayList<TelephoneModel>();
			List<BookBankModel>	bankList = new ArrayList<BookBankModel>();
			List<PatientModel> pList = new ArrayList<PatientModel>();
			List<Pre_nameModel> pnameList = new ArrayList<Pre_nameModel>();
			List<DoctorModel> workList = new ArrayList<DoctorModel>();
			List <DoctorModel> eduList = new ArrayList<DoctorModel>();
			setBranchStandardList(docData.getBranchStandard(doctor_id));
			docModel.setCheckSize(docData.branchMgrCheckSize(doctor_id));
			branchList = branchData.get_doctor_branch_detail(doctor_id);
			request.setAttribute("branchList", branchList);
			//System.out.println("-get branch success "+dateFormat.format(new Date()));
			
			branchMGRList = branchData.get_mgr_branch_detail(docModel.getBranchID());
			request.setAttribute("branchMGRList", branchMGRList);
			//System.out.println("-get mgr branch success "+dateFormat.format(new Date()));
			
			AddrList = addrData.getMultiAddr(docModel.getAddr_id());
			request.setAttribute("addressList", AddrList);
			//System.out.println("-get addrlist success "+dateFormat.format(new Date()));
			
			telList = telData.get_telList(docModel.getTel_id());
			request.setAttribute("telList", telList);
			//System.out.println("-get tel success "+dateFormat.format(new Date()));
			
			bankList = bankData.get_bookBank_detail(docModel.getBookBankId());
			request.setAttribute("bankList", bankList);
			//System.out.println("-get bank success "+dateFormat.format(new Date()));
			
			pList = pData.get_identification_type(docModel.getIdentification_type());
			request.setAttribute("pList", pList);
		//	System.out.println("-get iden success "+dateFormat.format(new Date()));
			
			pnameList = pnameData.select_pre_name(docModel.getPre_name_id(), "", "");
			request.setAttribute("pnameList", pnameList);
			//System.out.println("-get pre name success "+dateFormat.format(new Date()));
			
			workList = workData.getMultiWork(docModel.getWork_history_id());
			request.setAttribute("workList", workList);
			//System.out.println("-get work success "+dateFormat.format(new Date()));
			
			eduList = eduData.get_multi_edu(docModel.getEdu_id());
			request.setAttribute("eduList", eduList);
			request.setAttribute("titleID", docModel.getTitle());
			//System.out.println("get finish ---------------------- "+dateFormat.format(new Date()));
			
			return SUCCESS;
		} catch (IOException e) {
			return ERROR;
		} catch (Exception e) {
			return ERROR;
		}
	}
	public String UpdateDoctor(){
		//DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
		//System.out.println("Start update ----------------"+ dateFormat.format(new Date())); 
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpSession session = request.getSession();
		AddressData addrData = new AddressData();
		TelephoneData telData = new TelephoneData();
		DoctorData docData = new DoctorData();
		BookBankData bankData = new BookBankData();
		WorkHistoryData workData = new WorkHistoryData();
		EducationData eduData = new EducationData();
		List <TelephoneModel> tellist = new ArrayList<TelephoneModel>();
		List <AddressModel>addrlist = new ArrayList<AddressModel>();
		List <BranchModel> branchlist = new ArrayList<BranchModel>();
		List <BranchModel> mgrbranchlist = new ArrayList<BranchModel>();
		List <BookBankModel>bankList = new ArrayList<BookBankModel>();
		List<DoctorModel> workList = new ArrayList<DoctorModel>();
		List <Person> eduList = new ArrayList<Person>();
		
		String[] 	addr_no = request.getParameterValues("docModel.addr_no"),
				addr_bloc = request.getParameterValues("docModel.addr_bloc"),
				addr_village = request.getParameterValues("docModel.addr_village"),
				addr_alley = request.getParameterValues("docModel.addr_alley"),
				addr_road = request.getParameterValues("docModel.addr_road"),
				addr_provinceid = request.getParameterValues("docModel.addr_provinceid"),
				addr_aumphurid = request.getParameterValues("docModel.addr_aumphurid"),
				addr_districtid = request.getParameterValues("docModel.addr_districtid"),
				addr_typeid = request.getParameterValues("docModel.addr_typeid"),
				addr_zipcode = request.getParameterValues("docModel.addr_zipcode");
	
		String[] tel = request.getParameterValues("tel_number");
		String[] teltype = request.getParameterValues("teltype");
		
		String[] account_num = request.getParameterValues("account_num");
		String[] account_name = request.getParameterValues("account_name");
		String[] bank_id = request.getParameterValues("bank_id");
		
		String[] docbranch = request.getParameterValues("doctor_branch");
		String[] MngBranch = request.getParameterValues("doctor_boss_branch");
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
			addrData.del_multi_address(docModel.getAddr_id());
			addrData.add_multi_address(addrlist,docModel.getAddr_id());
		}else{
			addrData.del_multi_address(docModel.getAddr_id());
		}
		//System.out.println("-addr updated"+dateFormat.format(new Date()));
		i = 0;
		for(String tel_list : tel){
			if(!tel_list.equals("")){
				int teltypeList = Integer.parseInt(teltype[i]) ;
				TelephoneModel telModel = new TelephoneModel();
				telModel.setTel_number(tel_list);
				telModel.setTel_typeid(teltypeList);
				tellist.add(telModel);
			}
			i++;
		}
		if(tellist.size()>0){
			telData.del_multi_telephone(docModel.getTel_id());
			telData.add_multi_telephone(tellist,docModel.getTel_id());
		}else{
			telData.del_multi_telephone(docModel.getTel_id());
		}
		//System.out.println("-tel updated"+dateFormat.format(new Date()));
		if(docbranch!=null){
			for(String branch_list : docbranch){
				String branch_id = branch_list ;
				BranchModel branchModel = new BranchModel();
				branchModel.setBranch_id(branch_id);
				branchlist.add(branchModel);
			}
		}
		if(branchlist.size()>0){
			docData.del_doctor_branch(docModel.getBranchID());
			docData.AddDoctorBranch(branchlist,docModel.getBranchID());
		}else{
			docData.del_doctor_branch(docModel.getBranchID());
		}
		//System.out.println("-branch updated"+dateFormat.format(new Date()));
		if(MngBranch!=null){
			i = 0;
			for(String mgnBranch : MngBranch){ 
				i++;
				BranchModel branchModel = new BranchModel();
				branchModel.setDoctor_id(docModel.getDoctorID());
				branchModel.setBranch_id(mgnBranch);
				mgrbranchlist.add(branchModel);
			}
			docData.UpdateMgrBranch(mgrbranchlist,docModel.getDoctorID());
		}		
		
		i = 0;
		for(String account : account_num){
			if(!account.equals("")){
				BookBankModel bankMo = new BookBankModel();
				bankMo.setBank_id(bank_id[i]);
				bankMo.setBookbank_no(account);
				bankMo.setBookbank_name(account_name[i]);
				bankList.add(bankMo);
				
			}
			i++;
		}
		if(bankList.size()>0){
			bankData.del_multi_bookbank(docModel.getBookBankId());
			bankData.add_multi_bookbank(bankList, docModel.getBookBankId());
		}else{
			bankData.del_multi_bookbank(docModel.getBookBankId());
		}	
		//System.out.println("-bank updated"+dateFormat.format(new Date()));
		String[] education_vocabulary_id = request.getParameterValues("education_vocabulary_id");
		String[] education_name = request.getParameterValues("education_name");
		i = 0;
		for(String edu_name : education_name){
			if(!edu_name.equals("")){
				Person perModel = new Person();
				perModel.setEducation_th(edu_name);
				perModel.setEducation_vocabulary_id(Integer.parseInt(education_vocabulary_id[i]));
				eduList.add(perModel);
			}
			i++;
		}
		if(eduList.size()>0){
			eduData.del_multi_edu(docModel.getEdu_id());
			eduData.add_multi_edu(eduList, docModel.getEdu_id());
		} else{
			eduData.del_multi_edu(docModel.getEdu_id());
		}
		//System.out.println("-edu updated"+dateFormat.format(new Date()));
		
		String[] location = request.getParameterValues("docModel.location"),
				work_type = request.getParameterValues("docModel.work_type"),
				position = request.getParameterValues("docModel.position"),
				salary = request.getParameterValues("docModel.salary"),
				start_date = request.getParameterValues("docModel.start_date"),
				end_date = request.getParameterValues("docModel.end_date"),
				remark_message = request.getParameterValues("docModel.remark_message");	
		i=0;
		for(String lo : location){
			if(!lo.equals("")){
				String sdate = start_date[i];
				String edate = end_date[i];
				if(!sdate.equals("")){
					String[] parts = sdate.split("-");
					sdate = parts[2]+"-"+parts[1]+"-"+parts[0];
				}
				if(!edate.equals("")){
					String[] parts = edate.split("-");
					edate = parts[2]+"-"+parts[1]+"-"+parts[0];
				}
				DoctorModel docM = new DoctorModel();
				docM.setLocation(location[i]);
				docM.setWork_type(work_type[i]);
				docM.setPosition(position[i]);
				docM.setSalary(salary[i]);
				docM.setRemark_message(remark_message[i]);
				docM.setStart_date(sdate);
				docM.setEnd_date(edate);
				
				workList.add(docM);
			}
			i++;
			
		}
		if(workList.size()>0){
			workData.del_multi_work(docModel.getWork_history_id());
			workData.add_multi_work(workList,docModel.getWork_history_id());
		}else{
			workData.del_multi_work(docModel.getWork_history_id());
		}
		//System.out.println("-work updated"+dateFormat.format(new Date()));
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
		docModel.setBirth_date(BirthDate);
		docData.UpdateDoctor(docModel);

		session.setAttribute("doc_id", docModel.getDoctorID()); 
		//System.out.println("Update success ------------------"+dateFormat.format(new Date()));
		return SUCCESS;
	}
	
	public String DocTime_begin() {
		HttpServletRequest request = ServletActionContext.getRequest(); 
		HttpSession session = request.getSession();
		int doctor_id=0;
		docTimeM = new DoctTimeModel();
		if(request.getParameter("d")!=null){
			doctor_id = Integer.parseInt(request.getParameter("d"));
			docTimeM.setDoctorID(doctor_id);
		}else{
			doctor_id =  (Integer) session.getAttribute("doc_id");
			session.removeAttribute("doc_id");
		}
		BranchData branchData = new BranchData();
		List<BranchModel> branchList = new ArrayList<BranchModel>();
		
		branchList = branchData.get_doctor_branch_detail(doctor_id);
		request.setAttribute("branchList", branchList);
		
		return SUCCESS;
	}
	public String DocTime_excute() {
		HttpServletRequest request = ServletActionContext.getRequest(); 
		
		DoctorData docData = new DoctorData();
		
			docData.delDoctorTime(docTimeM.getDoctorID(), docTimeM.getBranch_id());
			docData.addDoctorTime(docTimeM);
		
		BranchData branchData = new BranchData();
		List<BranchModel> branchList = new ArrayList<BranchModel>();
		
		branchList = branchData.get_doctor_branch_detail(docTimeM.getDoctorID());
		request.setAttribute("branchList", branchList);
		request.setAttribute("d", docTimeM.getDoctorID());
		return SUCCESS;
	}
	public String Doctype_begin() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		DoctorTypeData docTypeData = new DoctorTypeData();
		List<DoctorModel> docTypeList = docTypeData.select_DocType("", "", "", "");
		request.setAttribute("doctorTypeList", docTypeList); 
		 
		return SUCCESS;
	}
	public String Doctype_excute() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest(); 
		DoctorTypeData docTypeData = new DoctorTypeData();
		String saveEdu 	= 	request.getParameter("saveEdu");
		String updatEdu 	=	request.getParameter("updatEdu");
		String deletEdu 	=	request.getParameter("deletEdu");
		
		if(saveEdu!=null){
			docTypeData.add_DocType(docModel);
		}
		if(updatEdu!=null){
			String position_id = request.getParameter("EduIDUp") ,
					position_name_th= request.getParameter("EduTHUp") ,
					position_name_en= request.getParameter("EduENUp"),
					position_name_short	= request.getParameter("EduShUp");
			docTypeData.UpdateDocType(position_id, position_name_th, position_name_en, position_name_short);
		}
		if(deletEdu!=null){
			String position_id = request.getParameter("EduIDdel");
			Boolean delStatus = docTypeData.DeleteDocType(position_id);
			if(delStatus){
				request.setAttribute("del_status", "Deleted !");
			}else { request.setAttribute("del_status", "Deleted Not Success!"); }
			
		}
		List<DoctorModel> docTypeList = docTypeData.select_DocType("", "", "", "");
		request.setAttribute("doctorTypeList", docTypeList); 
	return SUCCESS;
	}

	
	/**
	 * GETTER & SETTER ZONE.
	 */
	public DoctorModel getDocModel() {
		return docModel;
	}
	public void setDocModel(DoctorModel docModel) {
		this.docModel = docModel;
	}
	

	public DoctTimeModel getDocTimeM() {
		return docTimeM;
	}
	public void setDocTimeM(DoctTimeModel docTimeM) {
		this.docTimeM = docTimeM;
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

	public String getBranchStandard() throws IOException, Exception{
		
		DoctorData docdata = new DoctorData();
		BranchData branchdata = new BranchData();
		setBranchlist(branchdata.Get_branchList());
		/**
		 * get doc id.
		 */
		setBranchStandardList(docdata.getBranchStandard(docModel.getDoctorID()));
		
		return SUCCESS;
	}
	public String addBranchStandard() throws IOException, Exception{
		DoctorData docdata = new DoctorData();
		if(docdata.branchStandardCheck(docModel)){
			docdata.addBranchStandard(docModel);
			BranchData branchdata = new BranchData();
			setBranchlist(branchdata.Get_branchList());
		}
		else{
			addActionError("สาขานี้ถูกเพิ่มไปแล้ว!");
			BranchData branchdata = new BranchData();
			setBranchlist(branchdata.Get_branchList());
			setBranchStandardList(docdata.getBranchStandard(docModel.getDoctorID()));
			return INPUT;
		}
		
		/**
		 * redirect
		 */
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			new Servlet().redirect(request, response, "getBranchStandard-" + docModel.getDoctorID());
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return INPUT;
	}
	public String DeleteBranchStandard(){
		DoctorData docdata = new DoctorData();
		docdata.DeleteBranchStandard(docModel);
		/**
		 * redirect
		 */
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			new Servlet().redirect(request, response, "getBranchStandard-" + docModel.getDoctorID());
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return INPUT;
	}
	public String getBranchMgr() throws IOException, Exception{
		
		DoctorData docdata = new DoctorData();
		BranchData branchdata = new BranchData();
		setBranchlist(branchdata.Get_branchList());
		/**
		 * get doc id.
		 */
		setBranchMgrList(docdata.getBranchMgr(docModel.getDoctorID()));
		
		return SUCCESS;
	}
	public String addBranchMgr() throws IOException, Exception{
		DoctorData docdata = new DoctorData();
		int i = docdata.branchMgrCheckSize(docModel.getDoctorID());
		if( i<2 && docdata.branchMgrCheck(docModel)){
			docdata.addBranchMgr(docModel);
			BranchData branchdata = new BranchData();
			setBranchlist(branchdata.Get_branchList());
		}
		else if(docdata.branchMgrCheck(docModel)){
			addActionError("จำนวนสาขาเต็มแล้ว");
			BranchData branchdata = new BranchData();
			setBranchlist(branchdata.Get_branchList());
			setBranchMgrList(docdata.getBranchMgr(docModel.getDoctorID()));
			return INPUT;
		}
		else{
			addActionError("สาขานี้ถูกเพิ่มไปแล้ว!");
			BranchData branchdata = new BranchData();
			setBranchlist(branchdata.Get_branchList());
			setBranchMgrList(docdata.getBranchMgr(docModel.getDoctorID()));
			return INPUT;
			
		}
		
		/**
		 * redirect
		 */
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			new Servlet().redirect(request, response, "getBranchMgr-" + docModel.getDoctorID());
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return INPUT;
	}
	public String DeleteBranchMgr(){
		DoctorData docdata = new DoctorData();
		docdata.DeleteBranchMgr(docModel);
		/**
		 * redirect
		 */
		HttpServletRequest request =  ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			new Servlet().redirect(request, response, "getBranchMgr-" + docModel.getDoctorID());
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return INPUT;
	}	
}
