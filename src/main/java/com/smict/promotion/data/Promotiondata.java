package com.smict.promotion.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.IsNull;

import com.smict.person.model.BranchModel;
import com.smict.promotion.model.PromotionDetailModel;
import com.smict.promotion.model.PromotionModel;
import com.smict.treatment.model.TreatmentModel;

import ldc.util.Auth;
import ldc.util.DBConnect;
import ldc.util.DateUtil;

public class Promotiondata {
	DBConnect agent = new DBConnect();
	Connection conn = null;
	Statement Stmt = null;
	PreparedStatement pStmt = null;
	ResultSet rs = null;
	DateUtil dateUtil = new DateUtil();	
	
	public int addpromotioninsert(PromotionModel protionModel) throws IOException, Exception{
		
		String SQL = "INSERT INTO promotion(name,start_date,end_date,use_condition,billcostover,ismonday,istuesday,"
				+ "iswendesday,isthursday,isfriday,issaturday,issunday,is_allday,is_alltime,start_time,end_time,"
				+ "is_allsubcontact,is_birthmonth,is_allage,from_age,to_age,is_treatmentcount,is_allbranch) VALUES "
				
					+ "('"+protionModel.getName()
					+"','"+protionModel.getStart_date()
					+"','"+protionModel.getEnd_date()
					+"','"+protionModel.getUse_condition()
					+"',"+protionModel.getBillcostover()
					+",'"+protionModel.getIsmonday()
					+"','"+protionModel.getIstuesday()
					+"','"+protionModel.getIswendesday()
					+"','"+protionModel.getIsthursday()
					+"','"+protionModel.getIsfriday()
					+"','"+protionModel.getIssaturday()
					+"','"+protionModel.getIssunday()
					+"','"+protionModel.getIs_allday()
					+"','"+protionModel.getIs_alltime()
					+"','"+protionModel.getStart_time()
					+"','"+protionModel.getEnd_time()
					+"','"+protionModel.getIs_allsubcontact()
					+"','"+protionModel.getIs_birthmonth()
					+"','"+protionModel.getIs_allage()
					+"',"+protionModel.getFrom_age()
					+","+protionModel.getTo_age()
					+","+protionModel.getIs_treatmentcount()
					+",'"+protionModel.getIs_allbranch()+"') "; 

			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
			ResultSet rs = pStmt.getGeneratedKeys();
			int promotion_id=0;
			if (rs.next()){
				promotion_id=rs.getInt(1);
			}			
			if (!rs.isClosed())
				rs.close();
			if (!pStmt.isClosed())
				pStmt.close();
			if (!conn.isClosed())
				conn.close();

			return promotion_id;

		
		}
	
	public void addpromotionbranchinsert(PromotionModel protionModel) throws IOException, Exception{
		
		String SQL = "INSERT INTO promotion_condition_branch(branch_id,promotion_id,status) VALUES ";
			int i=0;		
				for(String Probranch : protionModel.getPromotion_branch_id()){
					if(i>0)
						SQL+=",";
					
				SQL+=	 "('"+Probranch
					+"',"+protionModel.getPromotion_id()
					+",'Active') ";
					i++;
				}
					
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();

			if (!pStmt.isClosed())
				pStmt.close();
			if (!conn.isClosed())
				conn.close();
		
		}
	public void addpromotioncontactinsert(PromotionModel protionModel) throws IOException, Exception{
		
		String SQL = "INSERT INTO promotion_condition_subcontact(sub_contact_id,promotion_id,status) VALUES ";
			int i=0;		
				for(int Procontact : protionModel.getSub_contact_id()){
					if(i>0)
						SQL+=",";
					
				SQL+=	 "("+Procontact
					+","+protionModel.getPromotion_id()
					+",'Active') ";
					i++;
				}
					
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
			
			if (!pStmt.isClosed())
				pStmt.close();
			if (!conn.isClosed())
				conn.close();
		
		}
	public List<PromotionDetailModel> getListPromotiondetail(int id ){
		
		String sql = "SELECT "
				+ "treatment_master.treatment_nameth,promotion_detail.product_type,promotion_detail.discount_baht,promotion_detail.discount_percent "
				+ "FROM "
				+ "promotion "
				+ "INNER JOIN promotion_detail ON promotion.id = promotion_detail.promotion_id "
				+ "INNER JOIN treatment_master ON promotion_detail.product_id = treatment_master.treatment_code "
				+ "Where promotion_detail.promotion_id = "+id+" "
				+ "UNION ALL "
				+ "SELECT "
				+ "pro_product.product_name,promotion_detail.product_type,promotion_detail.discount_baht,promotion_detail.discount_percent "
				+ "FROM "
				+ "promotion_detail "
				+ "INNER JOIN pro_product ON pro_product.product_id = promotion_detail.product_id "
				+ "Where promotion_detail.promotion_id = "+id+" ";

		List<PromotionDetailModel> promotiondetaillist = new LinkedList<PromotionDetailModel>();
//		HashMap<String, String> pDetailMap = new HashMap<String, String>();
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			String pid = "", pName = "";
			
			while (rs.next()) {
				PromotionDetailModel promotiondetailModel = new PromotionDetailModel();
				
				promotiondetailModel.setProduct_type(rs.getString("product_type"));
				promotiondetailModel.setType(rs.getString("treatment_nameth"));
				promotiondetailModel.setDiscount_baht(rs.getInt("discount_baht"));
				promotiondetailModel.setDiscount_percent(rs.getInt("discount_percent"));
				promotiondetaillist.add(promotiondetailModel);
				
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return promotiondetaillist;
		}
public List<PromotionModel> getListPromotion(){
		
		String sql = "SELECT "
				+ "pi.id, pi.name, pi.start_date, pi.end_date, "
				+ "pi.use_condition, pi.billcostover, pi.ismonday, pi.istuesday, "
				+ "pi.iswendesday, pi.isthursday, pi.isfriday, pi.issaturday, pi.issunday, pi.start_time, pi.end_time "
				+ "FROM "
				+ "promotion AS pi ORDER BY 'pi.id' ASC ";
				
		List<PromotionModel> promotionList = new LinkedList<PromotionModel>();
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			while (rs.next()) {
				PromotionModel promotionModel = new PromotionModel();
				
				promotionModel.setPromotion_id(rs.getInt("id"));
				promotionModel.setName(rs.getString("name"));
				promotionModel.setStart_date(rs.getString("start_date"));
				promotionModel.setEnd_date(rs.getString("end_date"));
				promotionModel.setUse_condition(rs.getString("use_condition"));
				promotionModel.setBillcostover(rs.getDouble("billcostover"));
				promotionModel.setIsmonday(rs.getString("ismonday"));
				promotionModel.setIstuesday(rs.getString("istuesday"));
				promotionModel.setIswendesday(rs.getString("iswendesday"));
				promotionModel.setIsthursday(rs.getString("isthursday"));
				promotionModel.setIsfriday(rs.getString("isfriday"));
				promotionModel.setIssaturday(rs.getString("issaturday"));
				promotionModel.setIssunday(rs.getString("issunday"));
				promotionModel.setStart_time(rs.getString("start_time"));
				promotionModel.setEnd_time(rs.getString("end_time"));
				Promotiondata promoDatadetail = new Promotiondata();
				promotionModel.setPromotiondetailModel(promoDatadetail.getListPromotiondetail(promotionModel.getPromotion_id()));
				
				promotionList.add(promotionModel);
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return promotionList;
	}

	public boolean PromotionDelete(PromotionModel protionModel) throws IOException, Exception{
		
		String SQL = "DELETE FROM promotion  "
				+ " where id = "+protionModel.getPromotion_id()+"";
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			int sStmt = pStmt.executeUpdate();

			if (!Stmt.isClosed())
				Stmt.close();
			if (!conn.isClosed())
				conn.close();
			
			if(sStmt>0){
				return true;
			}
		
				return false;
		
	}
public List<PromotionModel> getmemberlist(){
		
		String sql = "SELECT "
				+ "promotion_sub_contact.sub_contact_name,promotion_contact.contact_name,promotion_sub_contact.contact_id, "
				+ "promotion_subcontact_type.`name`,promotion_sub_contact.sub_contact_type_id,promotion_sub_contact.address, "
				+ "promotion_sub_contact.sub_contact_id,promotion_sub_contact.status, "
				+ "promotion_sub_contact.sms_piority,promotion_sub_contact.company_name,promotion_sub_contact.amount_default "
				+ "FROM "
				+ "promotion_contact "
				+ "INNER JOIN promotion_sub_contact ON promotion_sub_contact.contact_id = promotion_contact.contact_id "
				+ "LEFT  JOIN promotion_subcontact_type ON promotion_subcontact_type.id = promotion_sub_contact.sub_contact_type_id ";

				
		List<PromotionModel> promotionList = new LinkedList<PromotionModel>();
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			while (rs.next()) {
				PromotionModel promotionModel = new PromotionModel();
				
				promotionModel.setSub_contactid(rs.getString("promotion_sub_contact.sub_contact_id"));
				promotionModel.setSub_contactname(rs.getString("sub_contact_name"));
				promotionModel.setName(rs.getString("promotion_contact.contact_name"));
				promotionModel.setContypeName(rs.getString("promotion_subcontact_type.name"));
				promotionModel.setContact_id(rs.getString("promotion_sub_contact.contact_id"));
				promotionModel.setSub_contact_type_id(rs.getString("promotion_sub_contact.sub_contact_type_id"));
				promotionModel.setStatus_subcontact(rs.getInt("promotion_sub_contact.status"));
				promotionList.add(promotionModel);
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return promotionList;
	}
	public List<PromotionModel> getSubcontactwalletLinelist(String subwalid){
		
		String sql = "SELECT "
				+ "promotion_subcontact_wallet_line.id,promotion_subcontact_wallet_line.subcontact_wallet_id, "
				+ "promotion_subcontact_wallet_line.amount, "
				+ "promotion_subcontact_wallet_line.type,promotion_subcontact_wallet_line.emp_id, "
				+ "promotion_subcontact_wallet_line.create_date,promotion_subcontact_wallet_line.isstatus "
				+ "FROM "
				+ "promotion_subcontact_wallet_line "
				+ "WHERE promotion_subcontact_wallet_line.subcontact_wallet_id = "+subwalid+" "
				+ "AND promotion_subcontact_wallet_line.isstatus = 't' ";
	
				
		List<PromotionModel> promotionList = new LinkedList<PromotionModel>();
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			while (rs.next()) {
				PromotionModel promotionModel = new PromotionModel();
				
				promotionModel.setSub_contact_wallet_lineid(rs.getString("promotion_subcontact_wallet_line.id"));
				promotionModel.setSub_contact_wallet_line_type(rs.getString("promotion_subcontact_wallet_line.type"));
				promotionModel.setSub_contact_wallet_line_emp_id(rs.getString("promotion_subcontact_wallet_line.emp_id"));
				promotionModel.setAmount(rs.getDouble("promotion_subcontact_wallet_line.amount"));
				promotionModel.setSub_wallet_line_date(rs.getString("promotion_subcontact_wallet_line.create_date"));
				promotionModel.setSub_wallet_line_status(rs.getString("promotion_subcontact_wallet_line.isstatus"));
				promotionList.add(promotionModel);
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return promotionList;
	}
public int insertMember(PromotionModel protionModel) throws IOException, Exception{
				if(StringUtils.isEmpty(protionModel.getSms_piority())){
					protionModel.setSms_piority("0");
				}
	String SQL = "INSERT INTO promotion_sub_contact (sub_contact_name,contact_id,sms_piority,status ";
			if(protionModel.getContact_id().equals("2")){
				SQL += ",address,company_name,sub_contact_type_id ";
			}
			if(!StringUtils.isEmpty(protionModel.getSub_contact_type_id())){
				if(protionModel.getSub_contact_type_id().equals("3") ){
					SQL += ",amount_default ";
				}
			}	
			SQL += ") VALUES "
				+ "('"+protionModel.getSub_contactname()+"','"+protionModel.getContact_id()+"' "
				+ ",'"+protionModel.getSms_piority()+"',1 ";
			if(protionModel.getContact_id().equals("2")){
				SQL += ",'"+protionModel.getSub_contact_addr()+"','"+protionModel.getSub_contact_companyName()+"'"
					+ ",'"+protionModel.getSub_contact_type_id()+"' ";
			}
			if(!StringUtils.isEmpty(protionModel.getSub_contact_type_id())){
				if(protionModel.getSub_contact_type_id().equals("3")){
					SQL += ",'"+protionModel.getSub_contact_amount()+"' ";
				}
			}
			SQL += ") ";
		conn = agent.getConnectMYSql();
		pStmt = conn.prepareStatement(SQL);
		pStmt.executeUpdate();
		ResultSet rs = pStmt.getGeneratedKeys();
		int promotion_id=0;
		if (rs.next()){
			promotion_id=rs.getInt(1);
		}
		if (!rs.isClosed())
			rs.close();
		if (!pStmt.isClosed())
			pStmt.close();
		if (!conn.isClosed())
			conn.close();
		
		return promotion_id;

	
	}

	public void insertsubcontactWallet(int subID,double amount,String hn){
	
		String SQL ="insert into promotion_subcontact_wallet (sub_contact_id,total_amount,isstatus,patient_hn) "
				+ "VALUES "
				+ " ('"+subID+"','"+amount+"','t','"+hn+"') ";
	
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}	
	public void updateStatusSubcontact(PromotionModel protionModel){
		
		String SQL ="UPDATE promotion_sub_contact "
				+ "SET "
				+ "status = ";
				if(protionModel.getStatus_subcontact() == 1){
					SQL+="'0' ";
				}else{
					SQL+="'1' ";
				}
				SQL+="WHERE sub_contact_id = "+protionModel.getSub_contactid()+" ";
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}
	public PromotionModel getMemberModel(int subID,int subtype){
		
		String sql = "SELECT "
				+ "promotion_sub_contact.sub_contact_id,promotion_sub_contact.sub_contact_name, "
				+ "promotion_sub_contact.contact_id,promotion_sub_contact.sms_piority, "
				+ "promotion_sub_contact.sub_contact_type_id,promotion_sub_contact.address, "
				+ "promotion_sub_contact.amount_default,promotion_sub_contact.company_name, "
				+ "promotion_sub_contact.`status` ";
			if(subtype == 2){
				sql += ",promotion_subcontact_wallet.total_amount,promotion_subcontact_wallet.id ";
			}				
				sql += "FROM "
					+ "promotion_sub_contact ";
			if(subtype == 2){
				sql += "INNER  JOIN promotion_subcontact_wallet ON promotion_sub_contact.sub_contact_id "
					+ "= promotion_subcontact_wallet.sub_contact_id AND  promotion_subcontact_wallet.isstatus = 't' ";
			}					
				sql += "WHERE promotion_sub_contact.sub_contact_id = "+subID+" ";

		PromotionModel promotionModel = new PromotionModel();
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			while (rs.next()) {
								
				promotionModel.setSub_contactid(rs.getString("promotion_sub_contact.sub_contact_id"));
				promotionModel.setSub_contactname(rs.getString("sub_contact_name"));
				promotionModel.setSub_contact_addr(rs.getString("promotion_sub_contact.address"));
				promotionModel.setSms_piority(rs.getString("promotion_sub_contact.sms_piority"));
				promotionModel.setContact_id(rs.getString("promotion_sub_contact.contact_id"));
				promotionModel.setSub_contact_type_id(rs.getString("promotion_sub_contact.sub_contact_type_id"));
				promotionModel.setStatus_subcontact(rs.getInt("promotion_sub_contact.status"));
				promotionModel.setSub_contact_amount(rs.getDouble("promotion_sub_contact.amount_default"));
				promotionModel.setSub_contact_companyName(rs.getString("promotion_sub_contact.company_name"));
				if(subtype == 2){
					promotionModel.setTotal_amount(rs.getDouble("promotion_subcontact_wallet.total_amount"));
					promotionModel.setSub_contact_walletid(rs.getString("promotion_subcontact_wallet.id"));
				}
				
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return promotionModel;
	}	
	public boolean IsSameMembertype(String subID){
		
		String sql = "SELECT "
				+ "promotion_sub_contact.sub_contact_id,promotion_sub_contact.sub_contact_name, "
				+ "promotion_sub_contact.contact_id,promotion_sub_contact.sms_piority, "
				+ "promotion_sub_contact.sub_contact_type_id,promotion_sub_contact.address, "
				+ "promotion_sub_contact.amount_default,promotion_sub_contact.company_name, "
				+ "promotion_sub_contact.`status` "			
				+ "FROM "
				+ "promotion_sub_contact "				
				+ "WHERE promotion_sub_contact.sub_contact_id = "+subID+" ";
		boolean check = true;
		PromotionModel promotionModel = new PromotionModel();
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			while (rs.next()) {
								
				promotionModel.setUse_condition(rs.getString("promotion_sub_contact.sub_contact_type_id"));
				if(promotionModel.getUse_condition().equals("2")){
					check =false;
				}
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return check;
	}
	public boolean IsSameWallet(String subID){
		
		String sql = "SELECT "
				+ "sub_contact_id "		
				+ "FROM "
				+ "promotion_subcontact_wallet "				
				+ "WHERE sub_contact_id = "+subID+" ";
		boolean check = true;
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sql);
			
			while (rs.next()) {								
					check =false;
			}
			
			if(!rs.isClosed()) rs.close();
			if(!Stmt.isClosed()) Stmt.close();
			if(!conn.isClosed()) conn.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return check;
	}	
	public void updateisStatusSubcontactWallet(String subID,String ischeck){
		
		String SQL ="UPDATE promotion_subcontact_wallet "
				+ "SET "
				+ "isstatus = '"+ischeck+"' "
				+ "WHERE sub_contact_id = '"+subID+"' ";
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}
	public void updateSubcontact(PromotionModel protionModel){
		
		if(StringUtils.isEmpty(protionModel.getSms_piority())){
			protionModel.setSms_piority("0");
		}
		
		String SQL ="UPDATE promotion_sub_contact "
						+ "SET "
						+ "sub_contact_name = '"+protionModel.getSub_contactname()+"' "
						+ ",sms_piority = '"+protionModel.getSms_piority()+"' ";
					if(protionModel.getContact_id().equals("2")){
					SQL += ",sub_contact_type_id = '"+protionModel.getSub_contact_type_id()+"' "
						+ ",address = '"+protionModel.getSub_contact_addr()+"' "
						+ ",company_name = '"+protionModel.getSub_contact_companyName()+"' ";
						if(!StringUtils.isEmpty(protionModel.getSub_contact_type_id())){
							if(protionModel.getSub_contact_type_id().equals("3") ){
								SQL += ",amount_default = '"+protionModel.getSub_contact_amount()+"'";
							}
						}
					}
						SQL += "WHERE sub_contact_id = "+protionModel.getSub_contactid()+" ";
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}
	public void insertSubcontactWalletline(String subwalID,Double totalamount,int type){
		
		String SQL ="INSERT INTO promotion_subcontact_wallet_line "
				+ "(subcontact_wallet_id,amount,emp_id,type,create_date,isstatus) "
				+ "VALUES "
				+ " ( '"+subwalID+"' "
				+ ",'"+totalamount+"' "
				+ ",'"+Auth.user().getEmpUsr()+"' "
				+ ",'"+type+"',now(),'t') ";

		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}	
	public void AdjustSubcontactWallet(String subwalID,Double amount){
		
		String SQL ="UPDATE promotion_subcontact_wallet "
				+ "SET "
				+ "total_amount = '"+amount+"' "
				+ "WHERE id = '"+subwalID+"' ";
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}
	public void updateDefaultmoneySubcontact(PromotionModel protionModel){
		
		String SQL ="UPDATE promotion_sub_contact "
				+ "SET "
				+ "amount_default = '"+protionModel.getSub_contact_amount()+"' "
				+ "WHERE sub_contact_id = "+protionModel.getSub_contactid()+" ";
		try {
			conn = agent.getConnectMYSql();
			pStmt = conn.prepareStatement(SQL);
			pStmt.executeUpdate();
	
					
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
	
	}	
	
}



