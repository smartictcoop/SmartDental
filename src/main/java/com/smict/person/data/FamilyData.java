package com.smict.person.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.smict.person.model.FamilyModel;
import ldc.util.DBConnect;
import ldc.util.DateUtil;
import ldc.util.Validate;

public class FamilyData {
	DBConnect agent = new DBConnect();
	Connection conn = null;
	Statement Stmt = null;
	PreparedStatement pStmt = null;
	ResultSet rs = null;
	DateUtil dateUtil = new DateUtil();

	/**
	 * Fetch employee credentials.
	 * @author anubissmile
	 * @param String ident | identification
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject fetchEmployeeCredentials(String ident){
		String SQL = "SELECT employee.first_name_th AS firstname, employee.last_name_th AS lastname, "
				+ "employee.birth_date AS birth, employee.identification AS ident, "
				+ "employee.profile_pic AS picture, tel_telephone.tel_number AS phone, "
				+ "tel_teltype.tel_typename AS phone_type, 'N/A' AS hn, 'N/A' AS email, "
				+ "'à¸žà¸™à¸±à¸�à¸‡à¸²à¸™' AS JOB, address.addr_no AS `no`, address.addr_bloc AS block, "
				+ "address.addr_village AS village, address.addr_alley AS alley, "
				+ "address.addr_road AS road, districts.DISTRICT_NAME AS district, "
				+ "amphures.AMPHUR_NAME AS city, provinces.PROVINCE_NAME AS province, "
				+ "zipcodes.ZIPCODE AS zipcode, pre_name.pre_name_th AS prename "
				+ "FROM employee "
				+ "LEFT JOIN tel_telephone ON employee.tel_id = tel_telephone.tel_id "
				+ "LEFT JOIN tel_teltype ON tel_telephone.tel_typeid = tel_teltype.tel_typeid "
				+ "LEFT JOIN address ON employee.addr_id = address.addr_id "
				+ "LEFT JOIN districts ON address.addr_districtid = districts.DISTRICT_ID "
				+ "LEFT JOIN amphures ON districts.AMPHUR_ID = amphures.AMPHUR_ID "
				+ "LEFT JOIN provinces ON amphures.PROVINCE_ID = provinces.PROVINCE_ID "
				+ "LEFT JOIN zipcodes ON districts.DISTRICT_ID = zipcodes.DISTRICT_ID "
				+ "LEFT JOIN pre_name ON employee.pre_name_id = pre_name.pre_name_id "
				+ "WHERE employee.identification = '" + ident + "' "
				+ "GROUP BY employee.identification ";

		JSONObject jsonObj = new JSONObject();
		agent.connectMySQL();
		agent.exeQuery(SQL);
		if(agent.size()>0){
			try {
				agent.getRs().next();
				/**
				 * GET ADDRESS.
				 */
				String addr = agent.getRs().getString("no");
				addr += " à¸«à¸¡à¸¹à¹ˆ " + agent.getRs().getString("block");
				addr += " à¸«à¸¡à¸¹à¹ˆà¸šà¹‰à¸²à¸™" + agent.getRs().getString("village");
				addr += " à¸–à¸™à¸™ " + agent.getRs().getString("road");
				addr += " à¸•à¸³à¸šà¸¥ " + agent.getRs().getString("district");
				addr += " à¸­à¸³à¹€à¸ à¸­  " + agent.getRs().getString("city");
				addr += " à¸ˆà¸±à¸‡à¸«à¸§à¸±à¸” " + agent.getRs().getString("province");
				addr += " " + agent.getRs().getString("zipcode");
				jsonObj.put("address", addr);
				
				/**
				 * GET CREDENTIALS.
				 */
				jsonObj.put("name", agent.getRs().getString("firstname"));
				jsonObj.put("lastname", agent.getRs().getString("lastname"));
				jsonObj.put("prename", agent.getRs().getString("prename"));
				jsonObj.put("birth", agent.getRs().getDate("birth"));
				jsonObj.put("ident", agent.getRs().getString("ident"));
				jsonObj.put("hn", agent.getRs().getString("hn"));
				jsonObj.put("email", agent.getRs().getString("email"));
				jsonObj.put("job", agent.getRs().getString("job"));
				jsonObj.put("picture", agent.getRs().getString("picture"));
				jsonObj.put("phone", agent.getRs().getString("phone"));
				jsonObj.put("phone_type", agent.getRs().getString("phone_type"));
				
				/**
				 * CALC AGE BY BIRTH DATE.
				 */
				DateUtil du = new DateUtil();
				int age = du.getMonthsDiff(
					agent.getRs().getString("birth") + " 00:00",
					du.CnvToYYYYMMDD(du.curDate(), '-') + " 00:00"
				);
				System.out.println(age);
				age = (age/12);
				jsonObj.put("age", age);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				agent.disconnectMySQL();
			}
		}
		return jsonObj;
	}


	/**
	 * Fetch patient credentials.
	 * @author anubissmile
	 * @param String ident | identification
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject fetchPatientCredentials(String ident){
		String SQL = "SELECT patient.hn AS hn, patient.first_name_th AS firstname, "
				+ "patient.last_name_th AS lastname, patient.birth_date AS birth, "
				+ "patient.identification AS ident, patient.profile_pic AS picture, "
				+ "patient.email AS email, patient.career AS JOB, "
				+ "tel_telephone.tel_number AS phone, tel_teltype.tel_typename AS phone_type, "
				+ "address.addr_no AS `no`, address.addr_bloc AS block, "
				+ "address.addr_village AS village, address.addr_alley AS alley, "
				+ "address.addr_road AS road, districts.DISTRICT_NAME AS district, "
				+ "amphures.AMPHUR_NAME AS city, provinces.PROVINCE_NAME AS province, "
				+ "zipcodes.ZIPCODE AS zipcode, pre_name.pre_name_th AS  prename "
				+ "FROM patient "
				+ "LEFT JOIN tel_telephone ON patient.tel_id = tel_telephone.tel_id "
				+ "LEFT JOIN tel_teltype ON tel_telephone.tel_typeid = tel_teltype.tel_typeid "
				+ "LEFT JOIN address ON patient.addr_id = address.addr_id "
				+ "LEFT JOIN districts ON address.addr_districtid = districts.DISTRICT_ID "
				+ "LEFT JOIN amphures ON districts.AMPHUR_ID = amphures.AMPHUR_ID "
				+ "LEFT JOIN provinces ON amphures.PROVINCE_ID = provinces.PROVINCE_ID "
				+ "LEFT JOIN zipcodes ON districts.DISTRICT_ID = zipcodes.DISTRICT_ID "
				+ "LEFT JOIN pre_name ON patient.pre_name_id = pre_name.pre_name_id "
				+ "WHERE patient.identification = '" + ident + "' "
				+ "GROUP BY patient.identification ";
		

		JSONObject jsonObj = new JSONObject();
		agent.connectMySQL();
		agent.exeQuery(SQL);
		if(agent.size()>0){
			try {
				agent.getRs().next();
				/**
				 * GET ADDRESS.
				 */
				String addr = agent.getRs().getString("no");
				addr += " à¸«à¸¡à¸¹à¹ˆ " + agent.getRs().getString("block");
				addr += " à¸«à¸¡à¸¹à¹ˆà¸šà¹‰à¸²à¸™" + agent.getRs().getString("village");
				addr += " à¸–à¸™à¸™ " + agent.getRs().getString("road");
				addr += " à¸•à¸³à¸šà¸¥ " + agent.getRs().getString("district");
				addr += " à¸­à¸³à¹€à¸ à¸­  " + agent.getRs().getString("city");
				addr += " à¸ˆà¸±à¸‡à¸«à¸§à¸±à¸” " + agent.getRs().getString("province");
				addr += " " + agent.getRs().getString("zipcode");
				jsonObj.put("address", addr);
				
				/**
				 * GET CREDENTIALS.
				 */
				jsonObj.put("name", agent.getRs().getString("firstname"));
				jsonObj.put("lastname", agent.getRs().getString("lastname"));
				jsonObj.put("prename", agent.getRs().getString("prename"));
				jsonObj.put("birth", agent.getRs().getDate("birth"));
				jsonObj.put("ident", agent.getRs().getString("ident"));
				jsonObj.put("hn", agent.getRs().getString("hn"));
				jsonObj.put("email", agent.getRs().getString("email"));
				jsonObj.put("job", agent.getRs().getString("job"));
				jsonObj.put("picture", agent.getRs().getString("picture"));
				jsonObj.put("phone", agent.getRs().getString("phone"));
				jsonObj.put("phone_type", agent.getRs().getString("phone_type"));
				
				/**
				 * CALC AGE BY BIRTH DATE.
				 */
				DateUtil du = new DateUtil();
				int age = du.getMonthsDiff(
					agent.getRs().getString("birth") + " 00:00",
					du.CnvToYYYYMMDD(du.curDate(), '-') + " 00:00"
				);
				System.out.println(age);
				age = (age/12);
				jsonObj.put("age", age);
				
			} catch (SQLException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				agent.disconnectMySQL();
			}
		}
		return jsonObj;
	}
	
	
	/**
	 * Fetch dentist credentials.
	 * @author anubissmile
	 * @param String ident | identification
	 * @return JSONObject
	 */
	@SuppressWarnings("unchecked")
	public JSONObject fetchDentistCredentials(String ident){
		String SQL = "SELECT doctor.first_name_th AS `name`, doctor.last_name_th AS lastname, "
				+ "pre_name.pre_name_th AS prename, doctor.birth_date AS birth, "
				+ "doctor.identification AS ident, 'N/A' AS hn, 'N/A' AS email, "
				+ "'à¸—à¸±à¸™à¸•à¹�à¸žà¸—à¸¢à¹Œ' AS JOB, doctor.profile_pic AS picture, "
				+ "address.addr_no AS `no`, address.addr_bloc AS block, "
				+ "address.addr_village AS village, address.addr_alley AS alley, "
				+ "address.addr_road AS road, districts.DISTRICT_NAME AS district, "
				+ "amphures.AMPHUR_NAME AS city, provinces.PROVINCE_NAME AS province, "
				+ "zipcodes.ZIPCODE AS zipcode, tel_telephone.tel_number AS phone, "
				+ "tel_teltype.tel_typename AS phone_type FROM doctor "
				+ "LEFT JOIN pre_name ON doctor.doctor_id = pre_name.pre_name_id "
				+ "LEFT JOIN address ON doctor.addr_id = address.addr_id "
				+ "LEFT JOIN districts ON address.addr_districtid = districts.DISTRICT_ID "
				+ "LEFT JOIN amphures ON districts.AMPHUR_ID = amphures.AMPHUR_ID "
				+ "LEFT JOIN provinces ON amphures.PROVINCE_ID = provinces.PROVINCE_ID "
				+ "LEFT JOIN zipcodes ON districts.DISTRICT_ID = zipcodes.DISTRICT_ID "
				+ "LEFT JOIN tel_telephone ON doctor.tel_id = tel_telephone.tel_id "
				+ "LEFT JOIN tel_teltype ON tel_telephone.tel_typeid = tel_teltype.tel_typeid "
				+ "WHERE doctor.identification = '" + ident + "' ";

		JSONObject jsonObj = new JSONObject();
		agent.connectMySQL();
		agent.exeQuery(SQL);
		if(agent.size()>0){
			try {
				agent.getRs().next();
				/**
				 * GET ADDRESS.
				 */
				String addr = agent.getRs().getString("no");
				addr += " à¸«à¸¡à¸¹à¹ˆ " + agent.getRs().getString("block");
				addr += " à¸«à¸¡à¸¹à¹ˆà¸šà¹‰à¸²à¸™" + agent.getRs().getString("village");
				addr += " à¸–à¸™à¸™ " + agent.getRs().getString("road");
				addr += " à¸•à¸³à¸šà¸¥ " + agent.getRs().getString("district");
				addr += " à¸­à¸³à¹€à¸ à¸­  " + agent.getRs().getString("city");
				addr += " à¸ˆà¸±à¸‡à¸«à¸§à¸±à¸” " + agent.getRs().getString("province");
				addr += " " + agent.getRs().getString("zipcode");
				jsonObj.put("address", addr);
				
				/**
				 * GET CREDENTIALS.
				 */
				jsonObj.put("name", agent.getRs().getString("name"));
				jsonObj.put("lastname", agent.getRs().getString("lastname"));
				jsonObj.put("prename", agent.getRs().getString("prename"));
				jsonObj.put("birth", agent.getRs().getDate("birth"));
				jsonObj.put("ident", agent.getRs().getString("ident"));
				jsonObj.put("hn", agent.getRs().getString("hn"));
				jsonObj.put("email", agent.getRs().getString("email"));
				jsonObj.put("job", agent.getRs().getString("job"));
				jsonObj.put("picture", agent.getRs().getString("picture"));
				jsonObj.put("phone", agent.getRs().getString("phone"));
				jsonObj.put("phone_type", agent.getRs().getString("phone_type"));
				
				/**
				 * CALC AGE BY BIRTH DATE.
				 */
				DateUtil du = new DateUtil();
				int age = du.getMonthsDiff(
					agent.getRs().getString("birth") + " 00:00",
					du.CnvToYYYYMMDD(du.curDate(), '-') + " 00:00"
				);
				System.out.println(age);
				age = (age/12);
				jsonObj.put("age", age);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO: handle exception
			} finally {
				agent.disconnectMySQL();
			}
		}
		return jsonObj;
	}
	
	/**
	 * Searching any person (patient, employee, doctor) to add into patient's family list.
	 * @author anubissmile
	 * @param String | search
	 * @return List<FamilyModel>
	 */
	public List<FamilyModel> findAnyPerson(String search, String patHn){
		String SQL = "SELECT employee.emp_id AS row_id, employee.first_name_th AS fname, "
				+ "employee.last_name_th AS lastname, employee.identification AS ident, "
				+ "'employee' AS type, 3 AS typeid "
				+ "FROM employee "
				+ "WHERE ( 	employee.identification = '" + search + "' "
				+ "OR ( employee.first_name_th LIKE '%" + search + "%' OR employee.last_name_th LIKE '%" + search + "%' ) ) "
				+ "AND employee.identification NOT IN ( SELECT 	fam_family_identification FROM 	family WHERE fam_patient_hn = '" + patHn + "' "
				+ "AND fam_family_identification = '" + search + "' ) "
				+ "UNION "
				+ "SELECT doctor.doctor_id AS row_id, doctor.first_name_th AS fname, "
				+ "doctor.last_name_th AS lastname, doctor.identification AS ident, "
				+ "'doctor' AS type, 1 AS typeid "
				+ "FROM doctor "
				+ "WHERE ( doctor.identification = '" + search + "' "
				+ "OR ( doctor.first_name_th LIKE '%" + search + "%' OR doctor.last_name_th LIKE '%" + search + "%' ) ) "
				+ "AND doctor.identification NOT IN ( SELECT fam_family_identification 	FROM family WHERE fam_patient_hn = '" + patHn + "' "
				+ "AND fam_family_identification = '" + search + "' ) "
				+ "UNION "
				+ "SELECT patient.hn AS row_id, patient.first_name_th AS fname, "
				+ "patient.last_name_th AS lastname, patient.identification AS ident, "
				+ "'patient' AS type, 2 AS typeid "
				+ "FROM patient "
				+ "WHERE ( patient.hn != '" + patHn + "' AND patient.identification = '" + search + "' "
				+ "OR ( patient.first_name_th LIKE '%" + search + "%' OR patient.last_name_th LIKE '%" + search + "%' ) ) "
				+ "AND patient.identification NOT IN ( SELECT fam_family_identification FROM family WHERE fam_patient_hn = '" + patHn + "' "
				+ "AND fam_family_identification = '" + search + "' ) "
				+ "GROUP BY ident";
		
		
		List<FamilyModel> famList = new ArrayList<FamilyModel>();
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			while(agent.getRs().next()){
				FamilyModel famModel = new FamilyModel();
				famModel.setCount(agent.getRs().getRow());
				famModel.setFamPatientHN(agent.getRs().getString("row_id"));
				famModel.setFirstname_th(agent.getRs().getString("fname"));
				famModel.setLastname_th(agent.getRs().getString("lastname"));
				famModel.setFamIdentication(agent.getRs().getString("ident"));
				famModel.setUser_type_name(agent.getRs().getString("type"));
				famModel.setUser_type_id(agent.getRs().getInt("typeid"));
				famList.add(famModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return famList;
	}
	
	/**
	 * @author anubissmile
	 * @param String | hn
	 * @return List<FamilyModel>
	 */
	public List<FamilyModel> getFamilyListByHN(String hn){
		String SQL = "SELECT family.fam_id, family.fam_patient_hn, "
				+ "family.fam_family_identification, family.fam_phone_number, "
				+ "family.fam_relative_description, family.fam_family_type_id, "
				+ "PEOPLE.fname, PEOPLE.lastname "
				+ "FROM family "
				+ "INNER JOIN ("
				+ "	SELECT	employee.first_name_th AS fname, "
				+ "employee.last_name_th AS lastname, 	"
				+ "employee.identification AS ident "
				+ "FROM employee "
				+ "UNION "
				+ "SELECT "
				+ "doctor.first_name_th AS fname, "
				+ "doctor.last_name_th AS lastname, "
				+ "doctor.identification AS ident 	"
				+ "FROM doctor 	"
				+ "UNION "
				+ "SELECT "
				+ "patient.first_name_th AS fname, "
				+ "patient.last_name_th AS lastname, "
				+ "patient.identification AS ident "
				+ "FROM patient ) AS PEOPLE ON ( PEOPLE.ident = fam_family_identification ) "
				+ "WHERE family.fam_patient_hn = '" + hn + "'";

		List<FamilyModel> famList = new ArrayList<FamilyModel>();

		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			while(agent.getRs().next()){
				FamilyModel famModel = new FamilyModel();
				famModel.setCount(agent.getRs().getRow());
				famModel.setFamily_id(agent.getRs().getInt("fam_id"));
				famModel.setFamPatientHN(agent.getRs().getString("fam_patient_hn"));	
				famModel.setFamIdentication(agent.getRs().getString("fam_family_identification"));
				famModel.setTel_number(agent.getRs().getString("fam_phone_number"));
				famModel.setRelativeDescription(agent.getRs().getString("fam_relative_description"));
				famModel.setUser_type_id(agent.getRs().getInt("fam_family_type_id"));
				famModel.setFirstname_th(agent.getRs().getString("fname"));
				famModel.setLastname_th(agent.getRs().getString("lastname"));
				int famTypeId = agent.getRs().getInt("fam_family_type_id");
				if(famTypeId == 1){
					famModel.setUser_type_name("à¹�à¸žà¸—à¸¢à¹Œ");
				}else if(famTypeId == 2){
					famModel.setUser_type_name("à¸„à¸™à¹„à¸‚à¹‰");
				}else if(famTypeId == 3){
					famModel.setUser_type_name("à¸žà¸™à¸±à¸�à¸‡à¸²à¸™");
				}
				famList.add(famModel);
			}
		} catch (SQLException e) {
			agent.disconnectMySQL();
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return famList;
	}
	
	public int Gethight_familyID(){
		int result = 0;
		
		String sql = "select max(family_id) as family_id from family";
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while(rs.next()){
				result = rs.getInt("family_id");
			}
			
			if(!rs.isClosed()) rs.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int PlusOne(int family_id){
		switch (family_id) {
		case 0:
			family_id = 1;
			break;

		default:
			++family_id;
			break;
		}
		
		return family_id;
	}
	
	public void add_family(String patHn, String memberIdent, int typeId){
		String sql = "insert into family (fam_patient_hn, fam_family_identification, fam_family_type_id) "
				+ "values "
				+ "('"+patHn+"','"+memberIdent+"', "+typeId+")";
		try {
			
			agent.connectMySQL();
			agent.exeUpdate(sql);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			
			agent.disconnectMySQL();
		}
		
	}
	
	public void update_Family(FamilyModel famModel){
		String sql = "update family set family_id = ? , user_type_id = ?, user = ?, family_user_status = ? where family_id = ?";
		
		try {
			
			Connection connUpdateFam = agent.getConnectMYSql();
			PreparedStatement pStmtUpdateFam = connUpdateFam.prepareStatement(sql);
			pStmtUpdateFam.setInt(1, famModel.getFamily_id());
			pStmtUpdateFam.setInt(2, famModel.getUser_type_id());
			pStmtUpdateFam.setString(3, famModel.getRef_user());
			pStmtUpdateFam.setInt(4, Integer.parseInt(famModel.getFamily_user_status()));
			pStmtUpdateFam.setInt(5, famModel.getFamily_id());
			pStmtUpdateFam.executeUpdate();
			
			if(!pStmtUpdateFam.isClosed()) pStmtUpdateFam.close();
			if(!connUpdateFam.isClosed()) connUpdateFam.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void updateFamilyByUser(FamilyModel famModel){
		String sql = "update family set family_id = "+famModel.getFamily_id()+" , user_type_id = "+famModel.getUser_type_id()+", user = '"+famModel.getRef_user()+"', family_user_status = "+famModel.getFamily_user_status()+" where user = '"+famModel.getRef_user()+"'";
		
		try {
			
			Connection connUpdateFam = agent.getConnectMYSql();
			Statement StmtUpdateFam = connUpdateFam.createStatement();
			StmtUpdateFam.executeUpdate(sql);
			
			if(!StmtUpdateFam.isClosed()) StmtUpdateFam.close();
			if(!connUpdateFam.isClosed()) connUpdateFam.close();
		} catch (IOException e) {
		
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public boolean canJoinFamily(FamilyModel famModel){
		
		String sql = "select * from family where family_id = "+famModel.getFamily_id()+" and user = '"+famModel.getRef_user()+"'";
		
		boolean canJoinFamily = true;
		
		try {
			Connection connJoinFamily = agent.getConnectMYSql();
			Statement StmtJoinFamily = connJoinFamily.createStatement();
			ResultSet rsJoinFamily = StmtJoinFamily.executeQuery(sql);
			while (rsJoinFamily.next()) {
				
				canJoinFamily = false;

			}
			
			if(!rsJoinFamily.isClosed()) rsJoinFamily.close();
			if(!StmtJoinFamily.isClosed()) StmtJoinFamily.close();
			if(!connJoinFamily.isClosed()) connJoinFamily.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
		return canJoinFamily;
	}
	
	public List<FamilyModel> select_Family(FamilyModel famModel){
		
		String sql = "select * from family where ";
		
		if(new Validate().Check_String_notnull_notempty(famModel.getRef_user()))
			sql += "user like '%"+famModel.getRef_user()+"%' and ";
		
		if(new Validate().Check_String_notnull_notempty(famModel.getFamily_user_status()))
			sql += "family_user_status = '"+famModel.getFamily_user_status()+"' and ";
		
		if(famModel.getFamily_id() != 0)
			sql += "family_id = "+famModel.getFamily_id()+"  and ";
		
		if(famModel.getUser_type_id() != 0)
			sql += "user_type_id = "+famModel.getUser_type_id()+"  and ";
			
			sql += "family_id != 0 ";
			List<FamilyModel> ResultList = new ArrayList<FamilyModel>();
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while (rs.next()) {
				
				ResultList.add(new FamilyModel(rs.getInt("family_id"), rs.getInt("user_type_id"), rs.getString("user"), rs.getString("family_user_status")));

			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
		return ResultList;
	}
	
	public FamilyModel select_Family_rModel(FamilyModel famModel){
		
		String sql = "select * from family where ";
		
		if(new Validate().Check_String_notnull_notempty(famModel.getRef_user()))
			sql += "user like '%"+famModel.getRef_user()+"%' and ";
		
		if(new Validate().Check_String_notnull_notempty(famModel.getFamily_user_status()))
			sql += "family_user_status = '"+famModel.getFamily_user_status()+"' and ";
		
		if(famModel.getFamily_id() != 0)
			sql += "family_id = "+famModel.getFamily_id()+"  and ";
		
		if(famModel.getUser_type_id() != 0)
			sql += "user_type_id = "+famModel.getUser_type_id()+"  and ";
			
			sql += "family_id != 0 ";
		
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while (rs.next()) {
				famModel.setFamily_id(rs.getInt("family_id"));
				famModel.setRef_user(rs.getString("user"));
				famModel.setUser_type_id(rs.getInt("user_type_id"));
				famModel.setFamily_user_status(rs.getString("family_user_status"));
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			
			e.printStackTrace();
		}
		return famModel;
	}
	
	public void Delete_Telephone(FamilyModel famModel){
		String sql = "delete from family where family_id = ?";
		
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, famModel.getFamily_id());
			pStmt.executeUpdate();
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public void deleteFamilyUser(FamilyModel famModel){
		String sql = "delete from family where fam_id = "+famModel.getFamily_id();
		
		try {
			
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			Stmt.executeUpdate(sql);
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}
	
	public List<JSONObject> getUNION_FamilyList(int family_id,String first_name_th, String last_name_th, String first_name_en, String last_name_en){
		
		String sql = "SELECT a.family_id, a.`user`, b.first_name_th, b.last_name_th, b.first_name_en, b.last_name_en, c.user_type_name FROM family as a "
				+ "INNER JOIN doctor as b on (a.user = b.doctor_id) "
				+ "INNER JOIN user_type as c on (a.user_type_id = c.user_type_id and a.user_type_id = '1') where ";
		
				if(family_id > 0){
					sql += "a.family_id = "+family_id+" and ";
				}
				
				if(!first_name_th.equals("")){
					sql += "b.first_name_th like '%"+first_name_th+"%' and ";
				}
		
				if(!last_name_th.equals("")){
					sql += "b.last_name_th like '%"+first_name_th+"%' and ";
				}
				
				if(!first_name_en.equals("")){
					sql += "b.first_name_en like '%"+first_name_th+"%' and ";
				}
				
				if(!last_name_en.equals("")){
					sql += "b.last_name_en like '%"+first_name_th+"%' and ";
				}
				
				sql +=  "b.first_name_th != '' ";
				
				
				sql += "UNION ALL "
				+ "SELECT a.family_id, a.`user`, b.first_name_th, b.last_name_th, b.first_name_en, b.last_name_en, c.user_type_name FROM `family` as a "
				+ "INNER JOIN patient as b on (a.`user` = b.hn) "
				+ "INNER JOIN user_type as c on (a.user_type_id = c.user_type_id and a.user_type_id = '2') where ";
				
				if(family_id > 0){
					sql += "a.family_id = "+family_id+" and ";
				}
				
				if(!first_name_th.equals("")){
					sql += "b.first_name_th like '%"+first_name_th+"%' and ";
				}
		
				if(!last_name_th.equals("")){
					sql += "b.last_name_th like '%"+first_name_th+"%' and ";
				}
				
				if(!first_name_en.equals("")){
					sql += "b.first_name_en like '%"+first_name_th+"%' and ";
				}
				
				if(!last_name_en.equals("")){
					sql += "b.last_name_en like '%"+first_name_th+"%' and ";
				}
				
				sql +=  "b.first_name_th != '' ";
				
				sql +="UNION ALL "
				+ "SELECT a.family_id, a.`user`, b.first_name_th, b.last_name_th, b.first_name_en, b.last_name_en, c.user_type_name FROM family as a "
				+ "INNER JOIN employee as b on (a.user = b.emp_id) "
				+ "INNER JOIN user_type as c on (a.user_type_id = c.user_type_id and a.user_type_id = '3') where ";
				
				if(family_id > 0){
					sql += "a.family_id = "+family_id+" and ";
				}
				
				if(!first_name_th.equals("")){
					sql += "b.first_name_th like '%"+first_name_th+"%' and ";
				}
		
				if(!last_name_th.equals("")){
					sql += "b.last_name_th like '%"+first_name_th+"%' and ";
				}
				
				if(!first_name_en.equals("")){
					sql += "b.first_name_en like '%"+first_name_th+"%' and ";
				}
				
				if(!last_name_en.equals("")){
					sql += "b.last_name_en like '%"+first_name_th+"%' and ";
				}
				
				sql +=  "b.first_name_th != '' ";
				
				System.out.println(sql);
				
				List<JSONObject> UNION_FamilyList = new LinkedList<JSONObject>();
				try {
					conn = agent.getConnectMYSql();
					Stmt = conn.createStatement();
					rs = Stmt.executeQuery(sql);
					while (rs.next()) {
						
						JSONObject jsonobj = new JSONObject();
						jsonobj.put("family_id", rs.getString("fam_id"));
						jsonobj.put("first_name_th", rs.getString("first_name_th"));
						jsonobj.put("last_name_th", rs.getString("last_name_th"));
						jsonobj.put("first_name_en", rs.getString("first_name_en"));
						jsonobj.put("last_name_en", rs.getString("last_name_en"));
						jsonobj.put("user_type_name", rs.getString("user_type_name"));
						UNION_FamilyList.add(jsonobj);
					}
					
					if(!rs.isClosed()) rs.close();
					if(!Stmt.isClosed()) Stmt.close();
					if(!conn.isClosed()) conn.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				return UNION_FamilyList;
	}
	
	public List<FamilyModel> getFamModel_MemberFamilyList(int family_id,String first_name_th, String last_name_th, String first_name_en, String last_name_en){
		
		String sql = "SELECT a.family_id, b.first_name_th, b.last_name_th, b.first_name_en, b.last_name_en, c.user_type_id, c.user_type_name FROM family as a "
				+ "INNER JOIN doctor as b on (a.user = b.doctor_id) "
				+ "INNER JOIN user_type as c on (a.user_type_id = c.user_type_id and a.user_type_id = '1') where ";
		
				if(family_id > 0){
					sql += "a.family_id = "+family_id+" and ";
				}
				
				if(!first_name_th.equals("")){
					sql += "b.first_name_th like '%"+first_name_th+"%' and ";
				}
		
				if(!last_name_th.equals("")){
					sql += "b.last_name_th like '%"+first_name_th+"%' and ";
				}
				
				if(!first_name_en.equals("")){
					sql += "b.first_name_en like '%"+first_name_th+"%' and ";
				}
				
				if(!last_name_en.equals("")){
					sql += "b.last_name_en like '%"+first_name_th+"%' and ";
				}
				
				sql +=  "b.first_name_th != '' ";
				
				
				sql += "UNION ALL "
				+ "SELECT a.family_id, b.first_name_th, b.last_name_th, b.first_name_en, b.last_name_en, c.user_type_id, c.user_type_name FROM `family` as a "
				+ "INNER JOIN patient as b on (a.`user` = b.hn) "
				+ "INNER JOIN user_type as c on (a.user_type_id = c.user_type_id and a.user_type_id = '2') where ";
				
				if(family_id > 0){
					sql += "a.family_id = "+family_id+" and ";
				}
				
				if(!first_name_th.equals("")){
					sql += "b.first_name_th like '%"+first_name_th+"%' and ";
				}
		
				if(!last_name_th.equals("")){
					sql += "b.last_name_th like '%"+first_name_th+"%' and ";
				}
				
				if(!first_name_en.equals("")){
					sql += "b.first_name_en like '%"+first_name_th+"%' and ";
				}
				
				if(!last_name_en.equals("")){
					sql += "b.last_name_en like '%"+first_name_th+"%' and ";
				}
				
				sql +=  "b.first_name_th != '' ";
				
				sql +="UNION ALL "
				+ "SELECT a.family_id, b.first_name_th, b.last_name_th, b.first_name_en, b.last_name_en, c.user_type_id, c.user_type_name FROM family as a "
				+ "INNER JOIN employee as b on (a.user = b.emp_id) "
				+ "INNER JOIN user_type as c on (a.user_type_id = c.user_type_id and a.user_type_id = '3') where ";
				
				if(family_id > 0){
					sql += "a.family_id = "+family_id+" and ";
				}
				
				if(!first_name_th.equals("")){
					sql += "b.first_name_th like '%"+first_name_th+"%' and ";
				}
		
				if(!last_name_th.equals("")){
					sql += "b.last_name_th like '%"+first_name_th+"%' and ";
				}
				
				if(!first_name_en.equals("")){
					sql += "b.first_name_en like '%"+first_name_th+"%' and ";
				}
				
				if(!last_name_en.equals("")){
					sql += "b.last_name_en like '%"+first_name_th+"%' and ";
				}
				
				sql +=  "b.first_name_th != '' ";
				
				
				List<FamilyModel> UNION_FamilyList = new ArrayList<FamilyModel>();
				try {
					conn = agent.getConnectMYSql();
					Stmt = conn.createStatement();
					ResultSet rsgetFamModel_MemberFamilyList = Stmt.executeQuery(sql);
					while (rsgetFamModel_MemberFamilyList.next()) {
						
						FamilyModel famModel = new FamilyModel();
						famModel.setFamily_id(rsgetFamModel_MemberFamilyList.getInt("family_id"));
						famModel.setUser_type_id(rsgetFamModel_MemberFamilyList.getInt("user_type_id"));
						famModel.setFirstname_th(rsgetFamModel_MemberFamilyList.getString("first_name_th"));;
						famModel.setLastname_th(rsgetFamModel_MemberFamilyList.getString("last_name_th"));
						famModel.setFirstname_en(rsgetFamModel_MemberFamilyList.getString("first_name_en"));
						famModel.setLastname_en(rsgetFamModel_MemberFamilyList.getString("last_name_en"));
						famModel.setUser_type_name(rsgetFamModel_MemberFamilyList.getString("user_type_name"));
						
						UNION_FamilyList.add(famModel);
					}
					
					if(!rsgetFamModel_MemberFamilyList.isClosed()) rsgetFamModel_MemberFamilyList.close();
					if(!Stmt.isClosed()) Stmt.close();
					if(!conn.isClosed()) conn.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				} catch (Exception e) {
					
					e.printStackTrace();
				}
				
				return UNION_FamilyList;
	}
	
	public void addFamilyTelephone(FamilyModel famModel){
		String sql = "insert ignore into family_tel value(?, ?, ?)";
		
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, famModel.getFamily_id());
			pStmt.setString(2, famModel.getTel_number());
			pStmt.setString(3, famModel.getTel_typename());
			pStmt.executeUpdate();
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void updateFamilyID(FamilyModel famModel){
		String sql = "update family set family_id = "+famModel.getFamily_id()+", user = '"+famModel.getRef_user()+"', user_type_id = "+famModel.getUser_type_id()+", "
				+ "family_user_status = "+famModel.getFamily_user_status()+" where ";
		
		try {
			
			Connection connUpdateFamID = agent.getConnectMYSql();
			Statement stmtUpdateFamID = connUpdateFamID.createStatement();
			stmtUpdateFamID.executeUpdate(sql);
			
			if(!stmtUpdateFamID.isClosed()) stmtUpdateFamID.close();
			if(!connUpdateFamID.isClosed()) connUpdateFamID.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void updateFamilyTelephone(FamilyModel famModel){
		String sql = "update family_tel set tel_number = '"+famModel.getTel_number()+"', tel_typename = '"+famModel.getTel_typename()+"' where family_id = "+famModel.getFamily_id();
		
		try {
			
			Connection connUpdateFamTel = agent.getConnectMYSql();
			Statement stmtUpdateFamTel = connUpdateFamTel.createStatement();
			stmtUpdateFamTel.executeUpdate(sql);
			
			if(!connUpdateFamTel.isClosed()) connUpdateFamTel.close();
			if(!connUpdateFamTel.isClosed()) connUpdateFamTel.close();
			
			if(!stmtUpdateFamTel.isClosed()) stmtUpdateFamTel.close();
			if(!connUpdateFamTel.isClosed()) connUpdateFamTel.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public int getPatFamilyID(String user, int user_type_id){
		String sql = "SELECT * FROM `family` where `user` = '"+user+"' and user_type_id = '"+user_type_id+"';";
		int patFatmilyId = 0;
		try {
			
			
			Connection aConnGetPatFamilyId = agent.getConnectMYSql();
			Statement aStmt = aConnGetPatFamilyId.createStatement();
			ResultSet aResultset = aStmt.executeQuery(sql);
			
			while (aResultset.next()){
				patFatmilyId = aResultset.getInt("family_id");
			}
			
			if(!aStmt.isClosed()) aStmt.close();
			if(!aConnGetPatFamilyId.isClosed()) aConnGetPatFamilyId.close();
			if(!aResultset.isClosed()) aResultset.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return patFatmilyId;
	}
	
	public int getPatFamilyID(String user){
		String sql = "SELECT * FROM `family` where `user` = '"+user+"'";
		int patFatmilyId = 0;
		try {
			
			
			Connection aConnGetPatFamilyId = agent.getConnectMYSql();
			Statement aStmt = aConnGetPatFamilyId.createStatement();
			ResultSet aResultset = aStmt.executeQuery(sql);
			
			while (aResultset.next()){
				patFatmilyId = aResultset.getInt("family_id");
			}
			
			if(!aStmt.isClosed()) aStmt.close();
			if(!aConnGetPatFamilyId.isClosed()) aConnGetPatFamilyId.close();
			if(!aResultset.isClosed()) aResultset.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return patFatmilyId;
	}
	
	public List<String> getPatFamilyTel(int family_id){
		String sql = "SELECT * FROM `family_tel` where `family_id` = "+family_id;
		List<String> listFamTel = new ArrayList<String>();
		try {
			
			
			Connection aConnGetPatFamilyId = agent.getConnectMYSql();
			Statement aStmt = aConnGetPatFamilyId.createStatement();
			ResultSet aResultset = aStmt.executeQuery(sql);
			
			while (aResultset.next()){
				listFamTel.add(aResultset.getString("tel_number"));
				listFamTel.add(aResultset.getString("tel_typename"));
			}
			
			if(!aStmt.isClosed()) aStmt.close();
			if(!aConnGetPatFamilyId.isClosed()) aConnGetPatFamilyId.close();
			if(!aResultset.isClosed()) aResultset.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return listFamTel;
	}
	
	public int getFamilyID(String empid){
		String sql = "SELECT * FROM `family` where `fam_family_identification` = '"+empid+"' ";
		int empFatmilyId = 0;
		try {
			
			
			Connection aConnGetPatFamilyId = agent.getConnectMYSql();
			Statement aStmt = aConnGetPatFamilyId.createStatement();
			ResultSet aResultset = aStmt.executeQuery(sql);
			
			while (aResultset.next()){
				empFatmilyId = aResultset.getInt("family_id");
			}
			
			if(!aStmt.isClosed()) aStmt.close();
			if(!aConnGetPatFamilyId.isClosed()) aConnGetPatFamilyId.close();
			if(!aResultset.isClosed()) aResultset.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return empFatmilyId;
	}
}
