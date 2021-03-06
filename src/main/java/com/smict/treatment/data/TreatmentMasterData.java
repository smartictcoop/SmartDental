package com.smict.treatment.data;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.smict.all.model.ToothModel;
import com.smict.all.model.TreatmentMasterModel;
import com.smict.person.model.BrandModel;
import com.smict.product.model.ProductModel;
import com.smict.treatment.model.TreatmentContinuousModel;
import com.smict.treatment.model.TreatmentModel;

import ldc.util.Auth;
import ldc.util.DBConnect;
import ldc.util.DateUtil;
import ldc.util.Validate;

public class TreatmentMasterData
{
	DBConnect agent = new DBConnect();
	Connection conn = null;
	Statement Stmt = null;
	PreparedStatement pStmt = null;
	ResultSet rs = null;
	DateUtil dateUtil = new DateUtil();
	
	

	/**
	 * Update treatment's pricelist.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param TreatmentModel tModel | Treatment Model
	 * @param String[] conditions | Where clause conditions.
	 * @return int rec | Count of row that get affected.
	 */
	public int updateTreatmentPriceList(TreatmentModel tModel, BrandModel bModel, String[] conditions){
		int rec = 0;
		/**
		 * Clear old item.
		 */
		String where  = "";
		if(conditions.length > 0){
			if(conditions.length == 2){
				where = "WHERE (`" + conditions[0] + "` = '" + conditions[1] + "')";
			}else if(conditions.length == 3){
				where = "WHERE (`" + conditions[0] + "` " + conditions[1] + " '" + conditions[2] + "')";
			}
		}
		
		String SQL = "DELETE FROM `treatment_pricelist` " + where;
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(SQL);
		int brandCount = bModel.getBrandIDArr().length;
		if(tModel.getAmountPrice() != null && tModel.getWelfarePrice() != null){
			if(tModel.getAmountPrice().length == brandCount && tModel.getWelfarePrice().length == brandCount){
				List<String> valList = new ArrayList<String>();
				int i = 0;
				StringBuilder sb = new StringBuilder();
				SQL = "INSERT INTO `treatment_pricelist` (`treatment_id`, `brand_id`, `amount`, `price_typeid`) VALUES ";
				for(int bID : bModel.getBrandIDArr()){
					//('5', '5', '5', '5')
					/**
					 * Normal price.
					 */
					sb.append("( ")
							.append("'").append(String.valueOf(tModel.getTreatmentID())).append("', ")
							.append("'").append(String.valueOf(bID)).append("', ")
							.append("'").append(String.valueOf(tModel.getAmountPrice()[i])).append("', ")
							.append("'").append(String.valueOf(tModel.getAmountPriceType()[i])).append("'")
							.append(" )").toString();
					valList.add(sb.toString());
					sb.setLength(0);
					
					/**
					 * Welfare price.
					 */
					sb.append("( ")
							.append("'").append(String.valueOf(tModel.getTreatmentID())).append("', ")
							.append("'").append(String.valueOf(bID)).append("', ")
							.append("'").append(String.valueOf(tModel.getWelfarePrice()[i])).append("', ")
							.append("'").append(String.valueOf(tModel.getWelfarePriceType()[i])).append("'")
							.append(" )").toString();
					valList.add(sb.toString());
					sb.setLength(0);
					++i;
				}

				SQL += StringUtils.join(valList, " , ").toString();
				rec = agent.exeUpdate(SQL);
				if(rec > 0){
					agent.commit();
				}else{
					agent.rollback();
				}
			}else{
				agent.commit();
			}
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		
		return rec;
	}

	/**
	 * Update treatment's tooth type.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param TreatmentModel tModel | 
	 * @param String[] conditions | Where clause conditions.
	 * @return int rec | Count of row that get affected.
	 */
	public int updateTreatmentToothType(TreatmentModel tModel, String[] conditions){
		int rec = 0;
		/**
		 * Clear old item.
		 */
		String where  = "";
		if(conditions.length > 0){
			if(conditions.length == 2){
				where = "WHERE (`" + conditions[0] + "` = '" + conditions[1] + "')";
			}else if(conditions.length == 3){
				where = "WHERE (`" + conditions[0] + "` " + conditions[1] + " '" + conditions[2] + "')";
			}
		}
		String SQL = "DELETE FROM `treatment_type` " + where;
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(SQL);
		if(tModel.getToothTypeIDArr().length > 0){
			/**
			 * Insert new treatment tooth type val.
			 */
			List<String> valList = new ArrayList<String>();
			StringBuilder sb = new StringBuilder();
			for(int v : tModel.getToothTypeIDArr()){
				valList.add(sb.append(" ('").append(tModel.getTreatmentID()).append("', '").append(v).append("') ").toString());
				sb.setLength(0);
			}
			SQL = "INSERT INTO `treatment_type` (`treatment_id`, `tooth_type_id`) VALUES ";
			SQL += StringUtils.join(valList, ", ");
			rec = agent.exeUpdate(SQL);
			if(rec > 0){
				agent.commit();
			}else{
				agent.rollback();
			}
		}else{
			agent.commit();
		}
		agent.disconnectMySQL();
		return rec;
	}
	
	/**
	 * Update treatment master table
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi | wesarut.khm@gmail.com
	 * @param TreatmentModel tModel | 
	 * @param String[] conditions | Where clause conditions.
	 * @return int rec | Count of record that get affected.
	 */
	public int updateTreatmentMaster(TreatmentModel tModel, String[] conditions){
		int rec = 0;
		String where  = "";
		if(conditions.length > 0){
			if(conditions.length == 2){
				where = "WHERE (`" + conditions[0] + "` = '" + conditions[1] + "')";
			}else if(conditions.length == 3){
				where = "WHERE (`" + conditions[0] + "` " + conditions[1] + " '" + conditions[2] + "')";
			}
		}
		String SQL = "UPDATE `treatment_master` "
				+ "SET `code`='" + tModel.getTreatmentCode() + "', "
				+ "`nameth`='" + tModel.getTreatmentNameTH() + "', "
				+ "`nameen`='" + tModel.getTreatmentNameEN() + "', "
				+ "`auto_homecall`='" + tModel.getAutoHomeCall() + "', "
				+ "`recall_typeid`='" + tModel.getRecall() + "', "
				+ "`is_continue`= '" + tModel.getIsContinue() + "', "
				+ "`is_repeat`= '" + tModel.getIsRepeat() + "', "
				+ "`treatment_mode`= '" + tModel.getTreatmentMode() + "', "
				+ "`category_id`='" + tModel.getTreatmentCategoryID() + "', "
				+ "`tooth_pic_code`= '" + tModel.getToothPicCode() + "' " + where;
		
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(SQL);
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return rec;
	}
	
	/**
	 * Fetch treatment's price list.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | Where clause conditions
	 * @return List<TreatmentModel> | Result list.
	 */
	public List<TreatmentModel> selectTreatmentPricelist(String[] conditions){
		List<TreatmentModel> priceList = new ArrayList<TreatmentModel>();
		/**
		 * Set where conditions
		 */
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = "WHERE treatment_pricelist." + conditions[0] + " = '" + conditions[1] + "'";
			}else if(conditions.length == 3){
				where = "WHERE treatment_pricelist." + conditions[0] + " " + conditions[1] + " '" + conditions[2] + "'";
			}
		}
		
		String SQL = "SELECT treatment_pricelist.id, treatment_pricelist.treatment_id, "
				+ "treatment_pricelist.brand_id, treatment_pricelist.amount, "
				+ "treatment_pricelist.price_typeid FROM treatment_pricelist " + where;
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size()>0){
				rs = agent.getRs();
				int i = 0;
				
				while(rs.next()){
					TreatmentModel tModel = new TreatmentModel();
					tModel.setIterator(i+1);
					tModel.setPriceListID(rs.getInt("id"));
					tModel.setPriceListTreatID(rs.getInt("treatment_id"));
					tModel.setBrandID(rs.getInt("brand_id"));
					tModel.setPriceTypeID(rs.getInt("price_typeid"));
					tModel.setAmountP(rs.getDouble("amount"));
					priceList.add(tModel);
					++i;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error @ TreatmentMasterData.selectTreatmentPriceList()");
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return priceList;
	}
	
	/**
	 * Get treatment with where conditions.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param conditions
	 * @return List<TreatmentModel> tModel | 
	 */
	public List<TreatmentModel> selectTreatmentWhere(String[] conditions){
		List<TreatmentModel> tList = new ArrayList<TreatmentModel>();

		/**
		 * Set where conditions
		 */
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = "WHERE treatment_master." + conditions[0] + " = '" + conditions[1] + "'";
			}else if(conditions.length == 3){
				where = "WHERE treatment_master." + conditions[0] + " " + conditions[1] + " '" + conditions[2] + "'";
			}
		}
		String SQL = "SELECT treatment_master.id, treatment_master.`code`, "
				+ "treatment_master.nameth, treatment_master.nameen, "
				+ "treatment_master.auto_homecall, treatment_master.recall_typeid, "
				+ "treatment_master.is_continue, treatment_master.is_repeat, "
				+ "treatment_master.treatment_mode, treatment_master.category_id, "
				+ "treatment_master.tooth_pic_code, treatment_category.id, "
				+ "treatment_category.group_id FROM treatment_master "
				+ "INNER JOIN treatment_category ON treatment_master.category_id = treatment_category.id " + where;
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		rs = agent.getRs();
		try {
			if(agent.size() > 0){
				while(rs.next()){
					TreatmentModel tModel = new TreatmentModel();
					tModel.setTreatmentID(rs.getInt("id"));
					tModel.setTreatmentCode(rs.getString("code"));
					tModel.setTreatmentNameTH(rs.getString("nameth"));
					tModel.setTreatmentNameEN(rs.getString("nameen"));
					tModel.setAutoHomeCall(rs.getInt("auto_homecall"));
					tModel.setRecall(rs.getInt("recall_typeid"));
					tModel.setIsContinue(rs.getInt("is_continue"));
					tModel.setIsRepeat(rs.getInt("is_repeat"));
					tModel.setTreatmentMode(rs.getInt("treatment_mode"));
					tModel.setTreatmentCategoryID(rs.getInt("category_id"));
					tModel.setToothPicCode(rs.getString("tooth_pic_code"));
					tModel.setTreatmentGroupID(rs.getInt("group_id"));
					tList.add(tModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error @ TreatmentMasterData.selectTreatmentWhere()"); 
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return tList;
	}
	
	public List<TreatmentModel> getTreatmentByContinuousType(boolean isContinuous){
		List<TreatmentModel> tList = new ArrayList<TreatmentModel>();
		char continuous = isContinuous ? '2' : '1';
		String SQL = "SELECT treatment_master.id, treatment_master.`code`, "
				+ "treatment_master.nameth, treatment_master.nameen, "
				+ "treatment_master.auto_homecall, treatment_master.recall_typeid, "
				+ "treatment_master.is_continue, treatment_master.is_repeat, "
				+ "treatment_master.treatment_mode, treatment_master.category_id, "
				+ "treatment_master.tooth_pic_code "
				+ "FROM treatment_master "
				+ "WHERE treatment_master.is_continue = '" + continuous + "'";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				int i = 1;
				while(agent.getRs().next()){
					TreatmentModel tModel = new TreatmentModel();
					tModel.setIterator(i);
					tModel.setTreatmentID(agent.getRs().getInt("id"));
					tModel.setTreatmentNameTH(agent.getRs().getString("nameth"));
					tModel.setTreatmentNameEN(agent.getRs().getString("nameen"));
					tList.add(tModel);
					++i;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Can't get treatment right now! \n @TreatmentMasterData.getTreatmentByContinuousType");
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		
		return tList;
	}
	
	public int addMedicineTreatmentContinuousDetail(String strValSQL){
		String SQL = "INSERT INTO `product_phase_detail` (`phase_id`, `product_id`, `amount`, `amount_free`, `created_date`, `updated_date`) VALUES ";
		StringBuilder sb = new StringBuilder();
		sb.append(SQL).append(strValSQL);
		int rec = 0;
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(sb.toString());
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return rec;
	}
	
	public int addTreatmentContinuousDetail(String strValSQL){
		String SQL = "INSERT INTO `treatment_phase_detail` (`phase_id`, `treatment_id`, `created_date`, `updated_date`) VALUES ";
		StringBuilder sb = new StringBuilder();
		sb.append(SQL).append(strValSQL);
		int rec = 0;
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(sb.toString());
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return rec;
	}
	
	/**
	 * Insert new treatment continuous details
	 * @author anubi 
	 * @param int treatmentID
	 * @param int phase
	 * @param int countNo
	 * @param int startPRange
	 * @param int endPRange
	 * @return int rec | Count of row that get affected.
	 */
	public HashMap<String, Integer> addTreatmentContinuous(int treatmentID, int phase, int countNo, int price, int startPRange, int endPRange){
		String SQL = "INSERT INTO "
				+ "`treatment_continuous_phase` ("
				+ "`treatment_id`, "
				+ "`phase`, "
				+ "`count_no`, "
				+ "`price`, "
				+ "`start_price_range`, "
				+ "`end_price_range`, "
				+ "`created_date`, "
				+ "`updated_date`"
				+ ") VALUES ("
				+ "'" + treatmentID + "', '" + phase + "', "
				+ "'" + countNo + "', '" + price + "', "
				+ "'" + startPRange + "', '" + endPRange + "', "
				+ "NOW(), NOW())";
		
		String getInsertID = "SELECT LAST_INSERT_ID() as id;";
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		
		agent.connectMySQL();
		agent.begin();
		resultMap.put("NUM_ROW", agent.exeUpdate(SQL));
		if(resultMap.get("NUM_ROW") > 0){
			/**
			 * GET INSERT ID.
			 */
			agent.exeQuery(getInsertID);
			if(agent.size() > 0){
				rs = agent.getRs();
				try {
					int i=1;
					StringBuilder sb = new StringBuilder();
					while(rs.next()){
						resultMap.put(
							sb.append("ID").append(String.valueOf(i)).toString(), 
							rs.getInt("id")
						);
						i++;
					}
				} catch (SQLException e) {
					agent.rollback();
					agent.disconnectMySQL();
					e.printStackTrace();
				}
				agent.commit();
			}else{
				/**
				 * ROLL BACK.
				 */
				agent.rollback();
			}
		}else{
			/**
			 * INSERTING MISTAKE.
			 */
			agent.rollback();
		}
		agent.disconnectMySQL();
		return resultMap;
	}
	
	/**
	 * Get teratment list type of continuous or non-continuous (Default is non-continuous);
	 * @param TreatmentModel tModel | 
	 * @param boolean isContinuous | (true : continuous, false : non-continuous)
	 * @return
	 */
	public List<TreatmentModel> getTreatmentContinuous(TreatmentModel tModel, boolean isContinuous){
		List<TreatmentModel> tList = new ArrayList<TreatmentModel>();
		char contMode = '1';
		if(isContinuous){
			contMode = '2';
		}
		String SQL = "SELECT treatment_master.id, "
				+ "treatment_master.`code`, "
				+ "treatment_master.nameth, "
				+ "treatment_master.nameen, "
				+ "treatment_master.auto_homecall, "
				+ "treatment_master.recall_typeid, "
				+ "treatment_master.is_continue, "
				+ "treatment_master.is_repeat, "
				+ "treatment_master.treatment_mode, "
				+ "treatment_master.category_id, "
				+ "treatment_master.tooth_pic_code "
				+ "FROM treatment_master "
				+ "WHERE treatment_master.is_continue = '" + contMode + "' ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		rs = agent.getRs();
		
		try {
			if(agent.size()>0){
				while(rs.next()){
					TreatmentModel tm = new TreatmentModel();
					tm.setTreatmentID(rs.getInt("id"));
					tm.setTreatmentCode(rs.getString("code"));
					tm.setTreatmentNameTH(rs.getString("nameth"));
					tm.setTreatmentNameEN(rs.getString("nameen"));
					tm.setAutoHomeCall(rs.getInt("auto_homecall"));
					tm.setRecall(rs.getInt("recall_typeid"));
					tm.setIsContinue(rs.getInt("is_continue"));
					tm.setIsRepeat(rs.getInt("is_repeat"));
					tm.setTreatmentMode(rs.getInt("treatment_mode"));
					tm.setTreatmentCategoryID(rs.getInt("category_id"));
					tm.setToothPicCode(rs.getString("tooth_pic_code"));
					tList.add(tm);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return tList;
	}
	
	
	public int deleteMedFromTreatmentMaster(String[] conditions, TreatmentModel tModel){
		int rec = 0;
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = "WHERE (`" + conditions[0] + "` = '" + conditions[1] + "')";
			}else if(conditions.length == 3){
				where = "WHERE (`" + conditions[0] + "` " + conditions[1] + " '" + conditions[2] + "')";
			}
		}
		String SQL = "DELETE FROM `treatment_product` " + where;

		agent.connectMySQL();
		agent.begin();
		try{
			rec = agent.exeUpdate(SQL);
			agent.commit();
		} catch (Exception e){
			agent.rollback();
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		
		return rec;
	}
	
	
	/**
	 * Add medicine into the treatment master.
	 * @author anubi | wesarut.khm@gmail.com
	 * @param TreatmentModel tModel | 
	 * @param ProductModel pModel |
	 * @return int rec | Count of row that get affected.
	 */
	public int addMedIntoTreatmentMaster(TreatmentModel tModel, ProductModel pModel){
		String SQL = "INSERT INTO `treatment_product` (`treatment_id`, `product_id`, `amount`, `amount_free`) ";
				
		int rec = 0;
		int treatmentID = tModel.getTreatmentID();
		int[] productID = pModel.getProduct_id_arr(), vol = pModel.getProduct_volumn(), volFree = pModel.getProduct_volumn_free();
		
		/**
		 * Query preparation.
		 */
		System.out.println(productID.length);
		System.out.println(vol.length);
		System.out.println(volFree.length);
		int i = 0; // i is shorten from Iterator or Index.
		SQL += " VALUES ";
		List<String> valList = new ArrayList<String>();
		for(int pid : productID){
			if(volFree[i] <= vol[i]){
				valList.add(" ('" + treatmentID + "', '" + pid + "', '" + vol[i] + "', '" + volFree[i] + "') ");
			}
			++i;
		}
		
		SQL += StringUtils.join(valList, ", ");
		System.out.println(SQL);
		
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(SQL);
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		
		
		return 0;
	}
	
	/**
	 * Get med and product by where clause conditions.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | Where clause conditions.
	 * @param TreatmentModel tModel | Treatment model.
	 * @return List<ProductModel> pList | 
	 */
	public List<ProductModel> getMedicineAndProductListByCondition(String[] conditions, TreatmentModel tModel){
		List<ProductModel> pList = new ArrayList<ProductModel>();
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = "WHERE (`" + conditions[0] + "` = '" + conditions[1] + "')";
			}else if(conditions.length == 3){
				where = "WHERE (`" + conditions[0] + "` " + conditions[1] + " '" + conditions[2] + "')";
			}
		}
		String SQL = "SELECT treatment_product.id, treatment_product.treatment_id, "
				+ "treatment_product.product_id, treatment_product.amount, "
				+ "treatment_product.amount_free, pro_product.product_id, "
				+ "pro_product.product_name, pro_product.product_name_en, "
				+ "pro_product.price, pro_product.create_by, "
				+ "pro_product.create_datetime, pro_product.update_by, "
				+ "pro_product.update_datetime, pro_product.productunit_id, "
				+ "pro_product.producttype_id, pro_product.productgroup_id, "
				+ "pro_product.productbrand_id, pro_product.hide_on_treatment "
				+ "FROM treatment_product "
				+ "INNER JOIN pro_product ON treatment_product.product_id = pro_product.product_id " + where;
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				int iterator = 0;
				while(agent.getRs().next()){
					ProductModel pModel = new ProductModel();
					pModel.setProduct_id(agent.getRs().getInt("product_id"));
					pModel.setProduct_phase_amount(agent.getRs().getInt("amount"));
					pModel.setProduct_phase_amountfree(agent.getRs().getInt("amount_free"));
					pModel.setProduct_name(agent.getRs().getString("product_name"));
					pModel.setProduct_name_en(agent.getRs().getString("product_name_en"));
					pModel.setPrice(agent.getRs().getDouble("price"));
					pModel.setIterator(++iterator);
					pList.add(pModel);
					System.out.println(String.valueOf(pModel.getProduct_phase_amount()) + " " + String.valueOf(pModel.getProduct_phase_amountfree()));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error @ TreatmentMasterData.getMedicineAndProductListByCondition()");
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return pList;
	}
	
	/**
	 * Get treatment continuous's treatment phase.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | String array where clause conditions.
	 * @param TreatmentModel tModel |
	 * @return List<TreatmentContinuousModel>
	 */
	public List<TreatmentContinuousModel> getTreatmentContinuousTreatmentPhase(String[] conditions, TreatmentModel tModel){
		List<TreatmentContinuousModel> tModelList = new ArrayList<TreatmentContinuousModel>();
		/**
		 * Generating where clause conditions.
		 */
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = " WHERE (" + conditions[0] + " = '" + conditions[1] + "') ";
			}else if(conditions.length == 3){
				where = " WHERE (" + conditions[0] + " " + conditions[1] + " '" + conditions[2] + "') ";
			}
		}
		
		String SQL = "SELECT treatment_continuous_phase.id, treatment_continuous_phase.treatment_id, "
				+ "treatment_continuous_phase.phase, treatment_phase_detail.id, "
				+ "treatment_phase_detail.phase_id, treatment_phase_detail.treatment_id AS detail_treatment_id, "
				+ "treatment_master.id, treatment_master.`code`, "
				+ "treatment_master.nameth, treatment_master.nameen "
				+ "FROM treatment_continuous_phase "
				+ "INNER JOIN treatment_phase_detail ON treatment_continuous_phase.id = treatment_phase_detail.phase_id "
				+ "INNER JOIN treatment_master ON treatment_phase_detail.treatment_id = treatment_master.id " + where;
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				int iterate = 0;
				while(agent.getRs().next()){
					TreatmentContinuousModel tcModel = new TreatmentContinuousModel();
					tcModel.setIterate(iterate+1);
					tcModel.setPhaseID(agent.getRs().getInt("phase_id"));
					tcModel.setTreatmentID(agent.getRs().getInt("detail_treatment_id"));
					tcModel.setPhase(agent.getRs().getInt("phase"));
					tcModel.setTreatmentCode(agent.getRs().getString("code"));
					tcModel.setTreatmentNameTH(agent.getRs().getString("nameth"));
					tcModel.setTreatmentNameEN(agent.getRs().getString("nameen"));
					tModelList.add(tcModel);
					++iterate;
				}
			}
		} catch (SQLException e) {
			System.out.println("\n" + SQL + "\n");
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		
		return tModelList;
	}
	
	/**
	 * Get treatment continuous's product phase.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | String array where clause conditions.
	 * @param TreatmentModel tModel | 
	 * @return List<TreatmentContinuousModel> | 
	 */
	public List<TreatmentContinuousModel> getTreatmentContinuousProductPhase(String[] conditions, TreatmentModel tModel){
		List<TreatmentContinuousModel> tModelList = new ArrayList<TreatmentContinuousModel>();
		/**
		 * Generating where clause conditions.
		 */
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = " WHERE (" + conditions[0] + " = '" + conditions[1] + "') ";
			}else if(conditions.length == 3){
				where = " WHERE (" + conditions[0] + " " + conditions[1] + " '" + conditions[2] + "') ";
			}
		}

		String SQL = "SELECT treatment_continuous_phase.id, treatment_continuous_phase.treatment_id, "
				+ "treatment_continuous_phase.phase, treatment_continuous_phase.count_no, "
				+ "treatment_continuous_phase.price, treatment_continuous_phase.start_price_range, "
				+ "treatment_continuous_phase.end_price_range, product_phase_detail.id, "
				+ "product_phase_detail.phase_id, product_phase_detail.product_id, "
				+ "product_phase_detail.amount, product_phase_detail.amount_free, "
				+ "pro_product.product_id, pro_product.product_name, "
				+ "pro_product.product_name_en, pro_product.price "
				+ "FROM treatment_continuous_phase "
				+ "INNER JOIN product_phase_detail ON treatment_continuous_phase.id = product_phase_detail.phase_id "
				+ "INNER JOIN pro_product ON product_phase_detail.product_id = pro_product.product_id " + where;
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				int iterate = 0;
				while(agent.getRs().next()){
					TreatmentContinuousModel tcModel = new TreatmentContinuousModel();
					tcModel.setIterate(iterate+1);
					tcModel.setPhaseID(agent.getRs().getInt("phase_id"));
					tcModel.setPhase(agent.getRs().getInt("phase"));
					tcModel.setTreatmentID(agent.getRs().getInt("treatment_id"));
					tcModel.setProductID(agent.getRs().getInt("product_id"));
					tcModel.setProductNameTH(agent.getRs().getString("product_name"));
					tcModel.setProductNameEN(agent.getRs().getString("product_name_en"));
					tcModel.setAmount(agent.getRs().getInt("amount"));
					tcModel.setAmountFree(agent.getRs().getInt("amount_free"));
					tModelList.add(tcModel);
					++iterate;
				}
			}
		} catch (SQLException e) {
			System.out.println("\n" + SQL + "\n");
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		
		return tModelList;
	}
	
	/**
	 * Get treatment continuous phase detail.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | String array where clause conditions.
	 * @param TreatmentModel tModel | 
	 * @return List<TreatmentContinuousModel> |
	 */
	public List<TreatmentContinuousModel> getTreatmentContinuousPhaseDetail(String[] conditions, TreatmentModel tModel){
		List<TreatmentContinuousModel> tModelList = new ArrayList<TreatmentContinuousModel>();
		/**
		 * Set where clause conditions.
		 */
		String where = "";
		if(conditions != null){
			if(conditions.length == 2){
				where = " WHERE (" + conditions[0] + " = '" + conditions[1] + "') "; 
			}else if(conditions.length == 3){
				where = " WHERE (" + conditions[0] + " " + conditions[1] + " '" + conditions[2] + "') ";
			}
		}
		
		String SQL = "SELECT * FROM `treatment_continuous_phase` " + where;
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				int iterator = 0;
				while(agent.getRs().next()){
					TreatmentContinuousModel tcModel = new TreatmentContinuousModel();
					tcModel.setIterate(iterator+1);
					tcModel.setPhaseID(agent.getRs().getInt("id"));
					tcModel.setTreatmentID(agent.getRs().getInt("treatment_id"));
					tcModel.setPhase(agent.getRs().getInt("phase"));
					tcModel.setCountNo(agent.getRs().getInt("count_no"));
					tcModel.setPrice(agent.getRs().getDouble("price"));
					tcModel.setStartPriceRange(agent.getRs().getDouble("start_price_range"));
					tcModel.setEndPriceRange(agent.getRs().getDouble("end_price_range"));
					tcModel.setCreatedDate(agent.getRs().getString("created_date"));
					tcModel.setUpdatedDate(agent.getRs().getString("updated_date"));
					tModelList.add(tcModel);
					++iterator;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return tModelList;
	}
	
	/**
	 * Get medicine and product that outer side from treatment product list.
	 * @author anubissmile
	 * @param TreatmentModel tModel | Treatment model.
	 * @return List<ProductModel>
	 */
	public List<ProductModel> getMedicineAndProductByTreatmentID(TreatmentModel tModel){
		List<ProductModel> productList = new ArrayList<ProductModel>();
		String SQL = "SELECT pro_product.product_id, "
				+ "pro_product.product_name, "
				+ "pro_product.product_name_en, "
				+ "pro_product.price "
				+ "FROM pro_product "
				+ "WHERE pro_product.product_id NOT IN ( "
				+ "	SELECT 	pro_product.product_id 	"
				+ "	FROM treatment_product "
				+ "	LEFT JOIN pro_product ON pro_product.product_id = treatment_product.product_id 	"
				+ "	WHERE treatment_product.treatment_id = '" + tModel.getTreatmentID() + "' "
				+ ") ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		rs = agent.getRs();
		try {
			if(agent.size() > 0){
				int iterator = 0;
				while(rs.next()){
					ProductModel pModel = new ProductModel();
					pModel.setProduct_id(rs.getInt("product_id"));
					pModel.setProduct_name(rs.getString("product_name"));
					pModel.setProduct_name_en(rs.getString("product_name_en"));
					pModel.setPrice(rs.getDouble("price"));
					pModel.setIterator(++iterator);
					productList.add(pModel);
				}
			}
		} catch (SQLException e) {
			agent.disconnectMySQL();
			e.printStackTrace();
		}
		return productList;
	}

	
	/**
	 * Add new treatment price list.
	 * @param TreatmentModel tModel | 
	 * @param BrandModel bModel | 
	 * @return int rec | Count of records that get affected.
	 */
	public int addTreatmentPriceList(TreatmentModel tModel, BrandModel bModel){
		int rec = 0;
		List<String> sqlList = new ArrayList<String>();
		String SQL = "INSERT INTO `treatment_pricelist` "
				+ "(`treatment_id`, "
				+ "`brand_id`, "
				+ "`amount`, "
				+ "`price_typeid`) ";
		
		
		if(bModel.getBrandIDArr().length > 0){
			int i = 0;
			double price = 0;
			int type = 0;
			SQL += " VALUES ";
			for(int brandID : bModel.getBrandIDArr()){
				price = type = 0;
				if(tModel.getAmountPrice()[i] >= 0){
					/**
					 * - If amount price was not equal to 0
					 * - Then insert amount price.
					 */
					price = tModel.getAmountPrice()[i];
					type = tModel.getAmountPriceType()[i];
					sqlList.add(" ('" + tModel.getTreatmentID() + "', '" + brandID + "', '" + price + "', '" + type + "') ");
				}
				
				if(tModel.getWelfarePrice()[i] >= 0){
					/**
					 * - If welfare price was not equal to 0
					 * - Then insert amount price.
					 */
					price = tModel.getWelfarePrice()[i];
					type = tModel.getWelfarePriceType()[i];
					sqlList.add(" ('" + tModel.getTreatmentID() + "', '" + brandID + "', '" + price + "', '" + type + "') ");
				}
				
				++i;
			}
		}
		
		SQL += StringUtils.join(sqlList, ", ");
		
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(SQL);
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		
		return rec;
	}
	
	/**
	 * Add new treatment master.
	 * @author anubissmile
	 * @param TreatmentModel tModel | Model of tretment.
	 * @return int rec | Count of records that get affected.
	 */
	public int[] addTreatmentMaster(TreatmentModel tModel, BrandModel bModel){
		
		/**
		 * - int[] rec = {count of row treatment_master, count of row treatment_tooth_type, treatment_master_id, count of row treatment_pricelist}
		 */
		int[] rec = new int[4];
		String[] SQL = {null, null, null};
		int insertID = 0;
		
		/**
		 * Insert treatment_master table command.
		 */
		SQL[0] = "INSERT INTO `treatment_master` (`code`, `nameth`, "
				+ "`nameen`, `auto_homecall`, "
				+ "`recall_typeid`, `is_continue`, `is_repeat`, `is_social_security`, "
				+ "`treatment_mode`, `category_id`, "
				+ "`tooth_pic_code`) "
				+ "VALUES ('" + tModel.getTreatmentCode() + "', '" + tModel.getTreatmentNameTH() + "', "
				+ "'" + tModel.getTreatmentNameEN() + "', '" + tModel.getAutoHomeCall() + "', "
				+ "'" + tModel.getRecall() + "', '" + tModel.getIsContinue() + "', '" + tModel.getIsRepeat() +"', '" + tModel.getIsSocial() +"', "
				+ "'" + tModel.getTreatmentMode() + "', '" + tModel.getTreatmentCategoryID() + "', "
				+ "'" + tModel.getToothPicCode() + "') ";
		
		/**
		 * Get latest insert id
		 */
		SQL[1] = " SELECT LAST_INSERT_ID() as `last_insert_id` ";
		
		/**
		 * Insert treatment_type table command.
		 */
		SQL[2]= "INSERT INTO `treatment_type` (`treatment_id`, `tooth_type_id`) ";
		
		agent.connectMySQL();
//		agent0.begin();
		/**
		 * Execute insert treatment_master
		 */
		rec[0] = agent.exeUpdate(SQL[0]);
		
		/**
		 * Get last insert id.
		 */
		agent.exeQuery(SQL[1]);
		try {
			agent.getRs().next();
			rec[2] = insertID = agent.getRs().getInt("last_insert_id");
		} catch (SQLException e) {
			agent.disconnectMySQL();
			e.printStackTrace();
		}
		
		/**
		 * Execute insert treatment_type
		 */
		if(insertID > 0){
			List<String> val = new ArrayList<String>();
			for(int toothType : tModel.getToothTypeIDArr()){
				val.add(" ('" + insertID + "', '" + toothType + "') ");
			}
			SQL[2] += " VALUES " + StringUtils.join(val, ", ");
			System.out.println(SQL[2]);
			rec[1] = agent.exeUpdate(SQL[2]);
		}else{
			rec[1] = 0;
		}
		
		/**
		 * Insert treatment price list.
		 */
		tModel.setTreatmentID(rec[2]);
		rec[3] = 0;
		if(rec[2] > 0){
			rec[3] = addTreatmentPriceList(tModel, bModel);
		}
		
		
		
		/**
		 * Commit or rollback.
		 */
		/*if(rec[0] > 0 && rec[3] > 0){
			agent.commit();
			agent.commit();
		}else{
			agent.rollback();
			agent.rollback();
		}*/
		agent.disconnectMySQL();
		return rec;
	}
	
public List<TreatmentMasterModel> select_treatment_master(){
		
		//String doctor_name = "", room_name = "";
		
		String sqlQuery = "select treatment_master.id,treatment_master.`code`,treatment_master.nameth,treatment_master.is_continue, "
				+ "treatment_pricelist.amount "
				+ "FROM treatment_master  "
				+ "INNER JOIN treatment_pricelist ON treatment_master.id = treatment_pricelist.treatment_id "
				+ "WHERE treatment_pricelist.price_typeid = '1' "
				+ "AND treatment_pricelist.brand_id = (SELECT brand_id FROM branch where branch_id = '"+Auth.user().getBranchID()+"') "
				+ "GROUP BY treatment_master.id ";
		
		List <TreatmentMasterModel> resultList = new ArrayList<TreatmentMasterModel>();
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sqlQuery);
			while (rs.next()){   
				TreatmentMasterModel treatMasModel = new TreatmentMasterModel();
				treatMasModel.setTreatment_id(rs.getString("treatment_master.id"));
				treatMasModel.setTreatment_code(rs.getString("treatment_master.code"));
				treatMasModel.setTreatment_iscon(rs.getString("treatment_master.is_continue"));
				treatMasModel.setTreatment_nameth(rs.getString("treatment_master.nameth"));
				treatMasModel.setPrice(rs.getString("treatment_pricelist.amount"));
				resultList.add(treatMasModel);
			}
			
			if (!rs.isClosed())
				rs.close();
			if (!Stmt.isClosed())
				Stmt.close();
			if (!conn.isClosed())
				conn.close();
			
			return resultList;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return null;
		
	}
public List<TreatmentMasterModel> select_treatment_master_history(String hn,int treatment_id) throws IOException, Exception {
	
	//String doctor_name = "", room_name = "";
	
	String sqlQuery = "select a.treatment_code, a.treatment_nameth, a.treatment_nameen, a.brand_id, a.doctor_revenue_sharing "
			+ ",a.lab_percent, a.autohomecall, a.recall_typeid, a.treatment_type, a.price_standard, a.price_benefit, a.treatment_mode "
			+ ",a.type_tooth, a.type_surface, a.type_mouth, a.type_quadrant, a.type_sextant, a.type_arch, a.type_tooth_range "
			+ "FROM treatment_master a "
			+ "WHERE a.treatment_code <> '' "
			+ "and a.treatment_code not in (SELECT treatment_code FROM history_treatment WHERE treatment_id = "+treatment_id+") "
			+ "and a.treatment_code not in (SELECT treatment_code FROM treatcontinue_setup WHERE treatment_code = a.treatment_code and hn = '"+hn+"') "
			+ "ORDER BY a.treatment_code";

	/*if (servicePatientModel.getDoctor_id() != 0)
		sqlQuery += "and a.doctor_id = '"+servicePatientModel.getDoctor_id()+"' "; 
	if(servicePatientModel.getRoom_id() != 0)
		sqlQuery += "and a.room_id = "+servicePatientModel.getRoom_id()+" ";*/
	
	//System.out.println(sqlQuery);
	conn = agent.getConnectMYSql();
	Stmt = conn.createStatement();
	rs = Stmt.executeQuery(sqlQuery);
	
	List <TreatmentMasterModel> resultList = new ArrayList<TreatmentMasterModel>();
	   
	  
	String treatment_mode = "";
	 
	while (rs.next()){   
		treatment_mode = rs.getString("treatment_mode");
		if(treatment_mode.equals("1")) treatment_mode = "à¸�à¸²à¸£à¸£à¸±à¸�à¸©à¸²à¹�à¸šà¸šà¸˜à¸£à¸£à¸¡à¸”à¸²";
		else if (treatment_mode.equals("2")) treatment_mode = "à¸�à¸²à¸£à¸£à¸±à¸�à¸©à¸²à¹�à¸šà¸šà¸•à¹ˆà¸­à¹€à¸™à¸·à¹ˆà¸­à¸‡";
		 
		TreatmentMasterModel smModel = new TreatmentMasterModel();
		
		smModel.setTreatment_code(rs.getString("treatment_code"));
		smModel.setTreatment_nameth(rs.getString("treatment_nameth"));
		smModel.setTreatment_nameen(rs.getString("treatment_nameen"));
		smModel.setBrand_id(rs.getInt("brand_id"));
		smModel.setDoctor_revenue_sharing(rs.getString("doctor_revenue_sharing"));
		smModel.setLab_percent(rs.getInt("lab_percent"));
		smModel.setAutohomecall(rs.getString("autohomecall"));
		smModel.setRecall_typeid(rs.getString("recall_typeid"));
		smModel.setTreatment_type(rs.getString("treatment_type"));
		smModel.setPrice_standard(rs.getString("price_standard"));
		smModel.setPrice_benefit(rs.getString("price_benefit"));
		smModel.setTreatment_mode(treatment_mode);
		
		smModel.setType_tooth(rs.getString("type_tooth"));
		smModel.setType_surface(rs.getString("type_surface"));
		smModel.setType_mouth(rs.getString("type_mouth"));
		smModel.setType_quadrant(rs.getString("type_quadrant"));
		smModel.setType_sextant(rs.getString("type_sextant"));
		smModel.setType_arch(rs.getString("type_arch"));
		smModel.setType_tooth_range(rs.getString("type_tooth_range"));
		resultList.add(smModel); 
		  
	}
	
	if (!rs.isClosed())
		rs.close();
	if (!Stmt.isClosed())
		Stmt.close();
	if (!conn.isClosed())
		conn.close();
	
	return resultList;
	
}
	public List<TreatmentMasterModel> select_drug(TreatmentMasterModel treatmentMasterModel) throws IOException, Exception {
	
	//String doctor_name = "", room_name = "";
	
	String sqlQuery = "select a.treatment_code, a.product_id, b.product_name, a.product_transfer, a.product_free, b.price " 
			+ "FROM treatment_product a inner join pro_product b on(b.product_id = a.product_id) "
			+ "WHERE a.product_id <> ''";

	/*if (servicePatientModel.getDoctor_id() != 0)
		sqlQuery += "and a.doctor_id = '"+servicePatientModel.getDoctor_id()+"' "; 
	if(servicePatientModel.getRoom_id() != 0)
		sqlQuery += "and a.room_id = "+servicePatientModel.getRoom_id()+" ";*/
	
	//System.out.println(sqlQuery);
	conn = agent.getConnectMYSql();
	Stmt = conn.createStatement();
	rs = Stmt.executeQuery(sqlQuery);
	
	List <TreatmentMasterModel> resultList = new ArrayList<TreatmentMasterModel>();
	TreatmentMasterModel smModel = null;   
	  
	String treatment_mode = "";
	 
	while (rs.next()){    
		
		resultList.add(new TreatmentMasterModel(rs.getString("product_id"), rs.getString("product_name"), rs.getString("price"), 
				rs.getString("product_transfer"), rs.getString("product_free"))); 
	//	smModel.setTel_number(rs.getString("tel_number")); 
	}
	
	if (!rs.isClosed())
		rs.close();
	if (!Stmt.isClosed())
		Stmt.close();
	if (!conn.isClosed())
		conn.close();
	
	return resultList;
	
}	

	public int AddTreatmentMaster(TreatmentMasterModel treMod,ToothModel tootModel){
		int rt=0;
		String sql = "INSERT INTO treatment_master "
				+ "(treatment_code, treatment_nameth, treatment_nameen, brand_id, doctor_revenue_sharing, lab_percent, autohomecall, "
				+ "recall_typeid, price_standard, price_benefit, tooth_pic_code,treatment_group_code,treatment_mode,type_tooth,type_surface,type_mouth,type_quadrant,type_sextant,type_arch,type_tooth_range) "
				+ "VALUES ('"+treMod.getTreatment_code()+"', '"+treMod.getTreatment_nameth()+"', '"+treMod.getTreatment_nameen()+"', "+treMod.getBrand_id()+", '"+treMod.getDoctor_revenue_sharing()+"', "
						+ ""+treMod.getLab_percent()+", '"+treMod.getAutohomecall()+"', '"+treMod.getRecall_typeid()+"', '"+treMod.getPrice_standard()+"', '"+treMod.getPrice_benefit()+"',"
								+ " '"+treMod.getTooth_pic_code()+"','"+treMod.getTreatment_group_code()+"','"+treMod.getSet_treatmant()+"','"+tootModel.getType_tooth()+"',"
										+ "'"+tootModel.getType_surface()+"','"+tootModel.getType_mouth()+"','"+tootModel.getType_quadrant()+"','"+tootModel.getType_sextant()+"',"
												+ "'"+tootModel.getType_arch()+"','"+tootModel.getType_tooth_rang()+"')" ;
		
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rt= Stmt.executeUpdate(sql);
			
			Stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return rt;
	}
	public void AddTreatmentDoctor(String treatment_code, String doctorid){
  
		String sql = "INSERT INTO treatment_doctor "
				+ "(treatment_code, doctor_id) "
				+ "VALUES ('"+treatment_code+"', '"+doctorid+"')" ;
		
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			Stmt.executeUpdate(sql);
			
			Stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	} 
	public int select_treatment_product_id(){
		String sqlQuery = "select max(treatment_product_id) as treatment_product_id "
				+ "from treatment_product ";
		int ResultInt = 0; 
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sqlQuery);
			if(rs.next()) {
				ResultInt = rs.getInt("treatment_product_id"); 
				ResultInt += 1;
			}
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (Exception e)  { 
			e.printStackTrace();
		} 
		return ResultInt;
}
	public void AddTreatmentProductYa(int treatment_product_id, String treatment_code, String product_id, 
			int product_transfer, int product_free){
		  
		String sql = "INSERT INTO treatment_product "
				+ "(treatment_product_id, treatment_code, product_id, product_transfer, product_free) "
				+ "VALUES ("+treatment_product_id+", '"+treatment_code+"', '"+product_id+"', "+product_transfer+", "+product_free+")" ;
		
		try {
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			Stmt.executeUpdate(sql);
			
			Stmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	
		
	public List<BrandModel> select_brand(int brand_id, String brand_name) throws IOException, Exception
	{
		String sqlQuery = "select * from brand where ";

		if (brand_id !=0)
			sqlQuery += "brand_id = " + brand_id + " and ";

		if (new Validate().Check_String_notnull_notempty(brand_name))
			sqlQuery += "brand_name like '%" + brand_name + "%' and ";
		
		sqlQuery += "brand_id != '' ";
		//System.out.println("-----------");
		//System.out.println(sqlQuery);
		
		
		conn = agent.getConnectMYSql();
		Stmt = conn.createStatement();
		rs = Stmt.executeQuery(sqlQuery);
		
		List<BrandModel> ResultList = new ArrayList<BrandModel>();
		while(rs.next()){
			ResultList.add(new BrandModel(rs.getInt("brand_id"), rs.getString("brand_name")));
		}
		
		if (!rs.isClosed())
			rs.close();
		if (!Stmt.isClosed())
			Stmt.close();
		if (!conn.isClosed())
			conn.close();
		
		return ResultList;
		
	}
	
	
	
	
	public int GetHighest_add_brand()
	
	{
		String sqlQuery = "select MAX(brand_id) as brand_id from brand";
		int ResultInt = 0;
		
		try 
		{
			conn = agent.getConnectMYSql();
			Stmt = conn.createStatement();
			rs = Stmt.executeQuery(sqlQuery);
			if(rs.next())
			{
				ResultInt = rs.getInt("brand_id");
				//ResultString = rs.getString("pre_name_th");
				//ResultString = rs.getString("pre_name_en");
			}
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResultInt;
	}
	
	
	
	
	public int PlusOneID_FormatID(int brand_id)
	{
		if(brand_id == 0)
		{
			brand_id = 1;
		}
		else
		{
			brand_id = brand_id+1;
		}
		
		return brand_id;
	}
	
	
	
	
	public Boolean DeleteBrand(int brand_id)
	{
		String sqlQuery = "delete from brand where brand_id = ?";
		Boolean delete_success = false;
		try {

			conn = agent.getConnectMYSql();
			
			pStmt = conn.prepareStatement(sqlQuery);
			pStmt.setInt(1, brand_id);
			int rowsupdate = pStmt.executeUpdate();
			

			if (rowsupdate > 0)
				delete_success = true;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (!pStmt.isClosed())
					pStmt.close();
				if (!conn.isClosed())
					conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return delete_success;
	}
	
	
	
	public int UpdateBrand(int brand_id, String brand_name)
	{
		
		String sqlQuery = "update brand set brand_id = ? , brand_name = ? where brand_id = ?";
		
		//System.out.println(sqlQuery);
		
		int rowsupdate = 0;
		try 
		{

			conn = agent.getConnectMYSql();
			conn.setAutoCommit(false);
			pStmt = conn.prepareStatement(sqlQuery);
			pStmt.setInt(1, brand_id);
			pStmt.setString(2, brand_name);
			pStmt.setInt(3, brand_id);

			
			rowsupdate = pStmt.executeUpdate();
			conn.commit();

		} 
		
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally 
			{
				try 
				{
					if (!pStmt.isClosed())
						pStmt.close();
					if (!conn.isClosed())
						conn.close();
				} 
			
				catch (SQLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		return rowsupdate;
	} 
	public HashMap<String, Integer> addTreatmentContinuouspatient(int treatmentID, int phase, int countNo, int price, int startPRange, int endPRange,String patid){
		String SQL = "INSERT INTO "
				+ "`treatment_continuous_phase_patient` ("
				+ "`treatment_id`, "
				+ "`hn`,"
				+ "`phase`, "
				+ "`count_no`, "
				+ "`price`, "
				+ "`start_price_range`, "
				+ "`end_price_range` "
				+ ") VALUES ("
				+ "'" + treatmentID + "',(SELECT treatment_patient.patient_hn FROM treatment_patient WHERE treatment_patient.id = "+patid+"), '" + phase + "', "
				+ "'" + countNo + "', '" + price + "', "
				+ "'" + startPRange + "', '" + endPRange + "') ";
		
		String getInsertID = "SELECT LAST_INSERT_ID() as id;";
		HashMap<String, Integer> resultMap = new HashMap<String, Integer>();
		
		agent.connectMySQL();
		agent.begin();
		resultMap.put("NUM_ROW", agent.exeUpdate(SQL));
		if(resultMap.get("NUM_ROW") > 0){
			/**
			 * GET INSERT ID.
			 */
			agent.exeQuery(getInsertID);
			if(agent.size() > 0){
				rs = agent.getRs();
				try {
					int i=1;
					StringBuilder sb = new StringBuilder();
					while(rs.next()){
						resultMap.put(
							sb.append("ID").append(String.valueOf(i)).toString(), 
							rs.getInt("id")
						);
						i++;
					}
				} catch (SQLException e) {
					agent.rollback();
					agent.disconnectMySQL();
					e.printStackTrace();
				}
				agent.commit();
			}else{
				/**
				 * ROLL BACK.
				 */
				agent.rollback();
			}
		}else{
			/**
			 * INSERTING MISTAKE.
			 */
			agent.rollback();
		}
		agent.disconnectMySQL();
		return resultMap;
	}
	public int addTreatmentContinuousPhaseDetailPatient(String strValSQL){
		String SQL = "INSERT INTO `treatment_phase_detail_patient` (`phase_id`, `treatment_id`, `created_date`, `updated_date`) VALUES ";
		int rec = 0;
		StringBuilder sb = new StringBuilder();
		sb.append(SQL).append(strValSQL);
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(sb.toString());
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return rec;
	}
	public int addTreatmentContinuousDetailpatient(String strValSQL){
		String SQL = "INSERT INTO `treatment_continuous_progress` (`phase_id`, `treatment_id`, `hn`, `count_no`,`status_id`) VALUES ";
		StringBuilder sb = new StringBuilder();
		sb.append(SQL).append(strValSQL);
		int rec = 0;
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(sb.toString());
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return rec;
	}
	public int addMedicineTreatmentContinuousDetailpatient(String strValSQL){
		String SQL = "INSERT INTO `treatment_continuous_product_phase_patient` (`phase_id`, `product_id`, `amount`, `amount_free`) VALUES ";
		StringBuilder sb = new StringBuilder();
		sb.append(SQL).append(strValSQL);
		int rec = 0;
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(sb.toString());
		if(rec > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return rec;
	}	
}
