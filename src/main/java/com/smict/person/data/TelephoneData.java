package com.smict.person.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.smict.person.model.TelephoneModel;

import ldc.util.DBConnect;
import ldc.util.DateUtil;
import ldc.util.Validate;

public class TelephoneData {
	DBConnect agent = new DBConnect();
	Connection conn = null;
	Statement Stmt = null;
	PreparedStatement pStmt = null,pStmt2 = null;
	ResultSet rs = null;
	DateUtil dateUtil = new DateUtil();
	
	public int Gethight_telID(){
		int result = 0;
		
		String sql = "select max(tel_id) as tel_id from tel_telephone";
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while(rs.next()){
				result = rs.getInt("tel_id");
			}
			
			if(!rs.isClosed()) rs.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int PlusOne(int tel_id){
		switch (tel_id) {
		case 0:
			tel_id = 1;
			break;

		default:
			++tel_id;
			break;
		}
		
		return tel_id;
	}
	public int add_multi_telephone(List<TelephoneModel> telephoneList){
		int tel_id = 0;
		String sql = "select max(tel_id)+1 as tel_id from tel_telephone";
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while(rs.next()){
				tel_id = rs.getInt("tel_id");
			}
			sql = "INSERT INTO tel_telephone (tel_id,tel_number,tel_typeid) VALUES ";
			int i = 0;
			for (TelephoneModel telModel : telephoneList) {
				i++;
				if(i>1){
					sql += ",";
				}
				sql +="("+tel_id+",'"+telModel.getTel_number()+"',"+telModel.getTel_typeid()+")";				
			}
			pStmt2 = conn.prepareStatement(sql);
			pStmt2.executeUpdate();
			pStmt2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tel_id;
	}
	public int add_multi_telephone(List<TelephoneModel> telephoneList, int tel_id){
		
		try {
			conn = agent.getConnectMYSql();
	
			String sql = "INSERT INTO tel_telephone (tel_id,tel_number,tel_typeid) VALUES ";
			int i = 0;
			for (TelephoneModel telModel : telephoneList) {
				i++;
				if(i>1){
					sql += ",";
				}
				sql +="("+tel_id+",'"+telModel.getTel_number()+"',"+telModel.getTel_typeid()+")";				
			}
			pStmt2 = conn.prepareStatement(sql);
			pStmt2.executeUpdate();
			pStmt2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tel_id;
	}
	
	public int add_multi_telephone(List<TelephoneModel> telephoneList, int tel_id, int startIndex){
		
		try {
			conn = agent.getConnectMYSql();
	
			String sql = "INSERT INTO tel_telephone (tel_id,tel_number,tel_typeid) VALUES ";
			 
			for (int i = startIndex ;i < telephoneList.size(); i++) {
				TelephoneModel telModel = (TelephoneModel) telephoneList.get(i);
				
				if(i>startIndex){
					sql += ",";
				}
				sql +="("+tel_id+",'"+telModel.getTel_number()+"',"+telModel.getTel_typeid()+")";	
				
			}
			pStmt2 = conn.prepareStatement(sql);
			pStmt2.executeUpdate();
			pStmt2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tel_id;
	}
	public void del_multi_telephone(int tel_id){
		try {
			conn = agent.getConnectMYSql();
			String sql = "DELETE FROM tel_telephone WHERE tel_id="+tel_id;
			pStmt = conn.prepareStatement(sql);
			pStmt.executeUpdate();
		//	conn.commit();
			pStmt.close();
			conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void add_telephone(TelephoneModel telModel){
		String sql = "INSERT INTO tel_telephone (tel_id,tel_number,tel_typeid) VALUES (?,?,?)";
		
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, telModel.getTel_id());
			pStmt.setString(2, telModel.getTel_number());
			pStmt.setInt(3, telModel.getTel_typeid());
			pStmt.executeUpdate();

			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void update_telephone(TelephoneModel telModel){
		String sql = "update tel_telephone set tel_number = ? , tel_typeid = ? where tel_id = ?";
		
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, telModel.getTel_number());
			pStmt.setInt(2, telModel.getTel_typeid());
			pStmt.setInt(3, telModel.getTel_id());
			pStmt.executeUpdate();
			
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<TelephoneModel> select_telephone(TelephoneModel telModel){
		
		String sql = "select * from tel_telephone where ";
		
		if(new Validate().Check_String_notnull_notempty(telModel.getTel_number()))
			sql += "tel_number = '"+telModel.getTel_number()+"' and ";
		
		if(new Validate().checkIntegerNotZero(telModel.getTel_typeid()))
			sql += "tel_typeid = '"+telModel.getTel_typeid()+"'  and ";
		
		if(telModel.getTel_id() != 0)
			sql += "tel_id = "+telModel.getTel_id()+"  and ";
			
			sql += "tel_id != 0";
		
			//System.out.println(sql);
			List<TelephoneModel> ResultList = new ArrayList<TelephoneModel>();
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while (rs.next()) {
				ResultList.add(new TelephoneModel(rs.getInt("tel_id"),rs.getString("tel_number"),rs.getInt("tel_typeid")));
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return ResultList;
	}
	
	public TelephoneModel select_telephone_rModel(TelephoneModel telModel){
		
		String sql = "select * from tel_telephone where ";
		
		if(new Validate().Check_String_notnull_notempty(telModel.getTel_number()))
			sql += "tel_number = '"+telModel.getTel_number()+"' and ";
		
		if(new Validate().checkIntegerNotZero(telModel.getTel_typeid()))
			sql += "tel_typeid = '"+telModel.getTel_typeid()+"'  and ";
		
		if(telModel.getTel_id() != 0)
			sql += "tel_id = "+telModel.getTel_id()+"  and ";
			
			sql += "tel_id != 0";
		
			System.out.println(sql);
			TelephoneModel telModel_Result = new TelephoneModel();
			
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while (rs.next()) {
				telModel_Result.setTel_id(rs.getInt("tel_id"));
				telModel_Result.setTel_number(rs.getString("tel_number"));
				telModel_Result.setTel_typeid(rs.getInt("tel_typeid"));
			}
			
			
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return telModel_Result;
	}
	
	public void Delete_Telephone(TelephoneModel telModel){
		String sql = "delete from tel_telephone where tel_id = ?";
		
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, telModel.getTel_id());
			pStmt.executeUpdate();
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void addMultiple_Telephone(TelephoneModel telModel){
		String sql = "insert into multiple_telephone (owners,tel_id,tel_groupid) values (?,?,?)";
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			System.out.println("Insert ----------------------");
			System.out.println("Owners :"+telModel.getOwners());
			pStmt.setString(1, telModel.getOwners());
			pStmt.setInt(2, telModel.getTel_id());
			pStmt.setInt(3, telModel.getTel_groupid());
			pStmt.executeUpdate();
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<TelephoneModel> getMultiple_Telephone(TelephoneModel telModel){
		
		String sql = "SELECT "
				+ "a.tel_id, a.tel_number, a.tel_typeid, d.tel_typename "
				+ "FROM "
				+ "tel_telephone AS a "
			/*	+ "INNER JOIN tel_telephone AS b ON b.tel_id = a.tel_id "
				+ "multiple_telephone AS a "
				
				+ "INNER JOIN tel_telgroup AS c ON c.tel_groupid = a.tel_groupid " */
				+ "INNER JOIN tel_teltype AS d ON d.tel_typeid = a.tel_typeid "  
				+ "where ";
		
		if(new Validate().Check_String_notnull_notempty(telModel.getTel_number()))
			sql += "a.tel_number = '"+telModel.getTel_number()+"' and ";
		
		if(new Validate().checkIntegerNotZero(telModel.getTel_typeid()))
			sql += "d.tel_typeid = "+telModel.getTel_typeid()+" and ";
		
		if(new Validate().Check_String_notnull_notempty(telModel.getTel_typename()))
			sql += "d.tel_typename = '"+telModel.getTel_typename()+"'  and ";
		
	//	if(new Validate().checkIntegerNotZero(telModel.getTel_groupid()))
	//		sql += "c.tel_groupid = "+telModel.getTel_groupid()+" and ";
		
	//	if(new Validate().Check_String_notnull_notempty(telModel.getTel_telgroupname()))
	//		sql += "c.tel_groupname = '"+telModel.getTel_telgroupname()+"'  and ";
		
	//	if(new Validate().Check_String_notnull_notempty(telModel.getOwners()))
	//		sql += "a.owners = '"+telModel.getOwners()+"'  and ";
		
		if(telModel.getTel_id() != 0)
			sql += "a.tel_id = "+telModel.getTel_id()+"  and ";
			
			sql += "a.tel_id != 0";
		
			List<TelephoneModel> ResultList = new ArrayList<TelephoneModel>();
		try {
//			System.out.println(sql);
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			ResultSet rsgetMultiple_Telephone = Stmt.executeQuery(sql);
			while (rsgetMultiple_Telephone.next()) {
				telModel = new TelephoneModel(rsgetMultiple_Telephone.getInt("tel_id"),rsgetMultiple_Telephone.getString("tel_number"), rsgetMultiple_Telephone.getInt("tel_typeid"));
				telModel.setTel_typename(rsgetMultiple_Telephone.getString("tel_typename"));
				ResultList.add(telModel);
			}
			if(!rsgetMultiple_Telephone.isClosed()) rsgetMultiple_Telephone.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return ResultList;
	}
	
	public boolean hasTelTypeID(int tel_typeid){
		
		String sql = "select * from tel_telephone";
		boolean returnTel_typeid = false;
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				returnTel_typeid = true;		
			}
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnTel_typeid;
	}
	
	public int getMAX_TelTypeID(){
		
		String sql = "select max(tel_typeid) from tel_telephone";
		int returnTel_typeid = 0;
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				returnTel_typeid = rs.getInt("tel_typeid");		
			}
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnTel_typeid;
	}
	public  List<TelephoneModel> select_telType(){
		String sql = "select * from tel_teltype";
		
			//System.out.println(sql);
			List<TelephoneModel> ResultList = new ArrayList<TelephoneModel>();
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while (rs.next()) {
				ResultList.add(new TelephoneModel(rs.getString("tel_typename"),rs.getInt("tel_typeid")));
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		return ResultList;
	}
	public boolean has_addTelType(TelephoneModel telModel){
		
		String sql = "insert into tel_teltype values (?,?);";
		boolean add_success = false;
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, telModel.getTel_typeid());
			pStmt.setString(2, telModel.getTel_typename());
			if(pStmt.executeUpdate() > 0) add_success = true;; 
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return add_success;
	}
	public boolean has_updateTelType(TelephoneModel telModel){
		
		String sql = "update tel_teltype set  tel_typename=? where tel_typeid =?";
		boolean add_success = false;
		try {
			
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setString(1, telModel.getTel_typename());
			pStmt.setInt(2, telModel.getTel_typeid());
			if(pStmt.executeUpdate() > 0) add_success = true;
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return add_success;
	}
	public Boolean CheckTypeTel(int tel_typeid)
	{
		String sqlQuery = "select tel_typeid from tel_telephone where tel_typeid = ? limit 1";
		Boolean checkPreName = false;
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sqlQuery);
			pStmt.setInt(1, tel_typeid);
			rs = pStmt.executeQuery();
			
			while(rs.next()){
				checkPreName = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return checkPreName;
	}
	public boolean removeTelTypeID(int tel_typeid){
		String sql = "delete from tel_teltype where tel_typeid = ?";
		boolean remove_success = false;
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1, tel_typeid);
			if(pStmt.executeUpdate()>0) remove_success = true;
			
			if(!pStmt.isClosed()) pStmt.close();
			if(!conn.isClosed()) conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return remove_success;
	}
	
	public List<TelephoneModel> get_telList(int tel_id){
		List<TelephoneModel> telList = new ArrayList<TelephoneModel>();
		String sql = "SELECT * FROM tel_telephone WHERE tel_id = ?";
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(sql);
			pStmt.setInt(1,tel_id);
			rs = pStmt.executeQuery();
			while (rs.next()) {
				TelephoneModel telModel = new TelephoneModel();
				telModel.setTel_id(rs.getInt("tel_id"));
				telModel.setTel_number(rs.getString("tel_number"));
				telModel.setTel_typeid(rs.getInt("tel_typeid"));
				telList.add(telModel);
			}
			if (!rs.isClosed())
				rs.close();
			if (!pStmt.isClosed())
				pStmt.close();
			if (!conn.isClosed())
				conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return telList;
	}
	
	public Map<String, String> get_MapTeltype(){
		Map<String, String> map = new HashMap<String, String>();
		String sql = "SELECT * FROM `tel_teltype`";
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			while (rs.next()) {
				
				map.put(rs.getString("tel_typeid"),rs.getString("tel_typename"));
				
			}
			if (!rs.isClosed())
				rs.close();
			if (!Stmt.isClosed())
				Stmt.close();
			if (!conn.isClosed())
				conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
	}
	
	public List<TelephoneModel> buildTelephoneList(HttpServletRequest request){
		List <TelephoneModel> tellist = new ArrayList<TelephoneModel>();
		String[] tel = request.getParameterValues("tel_number");
		String[] teltype = request.getParameterValues("teltype");
		
		int i = 0;
		for(String tel_list : tel){
			int teltypeList = Integer.parseInt(teltype[i]) ;
			TelephoneModel telModel = new TelephoneModel();
			telModel.setTel_number(tel_list);
			telModel.setTel_typeid(teltypeList);
			tellist.add(telModel);
			i++;
		}
		return tellist;
	}
}
