package com.smict.treatment.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.smict.all.model.ToothModel;
import com.smict.all.model.TreatmentMasterModel;
import com.smict.person.data.BrandData;
import com.smict.person.model.BrandModel;
import com.smict.product.model.ProductModel;
import com.smict.treatment.data.ToothMasterData;
import com.smict.treatment.data.TreatmentData;
import com.smict.treatment.data.TreatmentMasterData;
import com.smict.treatment.model.TreatmentContinuousModel;
import com.smict.treatment.model.TreatmentModel;
import ldc.util.Auth;  

@SuppressWarnings("serial")
public class TreatmentMasterAction extends ActionSupport{
	private TreatmentMasterModel treatmentMasterModel;  
	private TreatmentModel treatmentModel;
	private ProductModel productModel;
	private ToothModel toothModel;
	private BrandModel brandModel;
	private List<BrandModel> brandList;
	private List<ProductModel> productList;
	private List<ProductModel> productList2;
	private HashMap<String, String> brandMap;
	private List<TreatmentModel> treatmentList;
	private List<TreatmentModel> treatmentList2;
	private List<TreatmentModel> treatmentContinuousList;
	private HashMap<String, String> treatmentMap;
	private HashMap<String, String> categoryMap;
	private HashMap<String, String> toothPicMap;
	private HashMap<String, String> toothTypeMap;
	private String alertSuccess, alertError, alertMSG;
	private String triggerStatus;
	private TreatmentContinuousModel treatmentContinuousModel;
	private List<TreatmentContinuousModel> treatmentContinuousModelList;
	
	
	/**
	 * CONSTRUCTOR
	 */
	public TreatmentMasterAction(){
		Auth.authCheck(false);
	}
	

	/**
	 * Get treatment continuous phase detail and put into the edit form.
	 * @author anubi
	 * @return String | Action result.
	 */
	public String getTreatmentContinuousPhaseEdit(){
		/**
		 * Fetch phase details.
		 */
		String[] conditions = {"treatment_continuous_phase.`treatment_id`", String.valueOf(treatmentModel.getTreatmentID())};
		this.getTreatmentContinuousPhaseDetail(conditions, treatmentModel);
		
		/**
		 * Fetch product & treatment detail.
		 */
		treatmentContinuousModel = new TreatmentContinuousModel();
		this.getTreatmentContinuousProductPhase(conditions, treatmentModel);
		this.getTreatmentContinuousTreatmentPhase(conditions, treatmentModel);

		/**
		 * Get medicine list.
		 */
		productList = this.getMedicineAndProductByTreatmentID(treatmentModel);
		
		/**
		 * Get treatment(non-continuous) list.
		 */
		treatmentList = this.getTreatmentContinuous(treatmentModel, false);
		
		
		return SUCCESS;
	}
	
	/**
	 * Displaying treatment's product & medicine list.
	 * @author anubi
	 * @return String | Action result.
	 */
	public String getTreatmentMedicineEdit(){
		/**
		 * Get medicine and product for put into treatment.
		 */
		productList = this.getMedicineAndProductByTreatmentID(treatmentModel);
		
		/**
		 * Get medicine and product that were in the treatment already.
		 */
		String[] conditions = {"treatment_id", String.valueOf(treatmentModel.getTreatmentID())};
		this.fetchProductListByConditions(conditions, treatmentModel);
		return SUCCESS;
	}
	
	/**
	 * Updating continuous treatment's phase details.
	 * @author anubi
	 * @return String | Action result.
	 */
	public String postTreatmentContinuousPhaseDetailEdit(){
		/**
		 * Updating treatment continuous phase master (treatment_continuous_phase).
		 */
		System.out.println();
		
		/**
		 * Updating product phase details.
		 */
		
		/**
		 * Updating treatment phase details.
		 */
		
		return SUCCESS;
	}
	
	/**
	 * Updating treatment's medicine & product list.
	 * @author anubi
	 * @return String | Action result.
	 */
	public String postTreatmentMedicineEdit(){
		/**
		 * Delete old item sets.
		 */
		String[] conditions = {"treatment_id", String.valueOf(treatmentModel.getTreatmentID()) };
		this.deleteMedFromTreatmentMaster(conditions, treatmentModel);
		
		/**
		 * Insert new item sets.
		 */
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		tMasterData.addMedIntoTreatmentMaster(treatmentModel, productModel);
		
		return SUCCESS;
	}
	
	/**
	 * Do editing treatment data.
	 * @author anubi | wesarut.khm@gmail.com
	 * @return String | Action result string.
	 */
	public String postTreatmentByID(){
		String returnResult = SUCCESS;
		HttpServletRequest request = ServletActionContext.getRequest();
		if(request.getMethod().equals("POST")){
			int recTMaster = 0, recTPriceList = 0;
			/**
			 * Edit treatment table.
			 */
			String[] conditions = {"id" , String.valueOf(treatmentModel.getTreatmentID())};
			recTMaster = this.updateTreatmentMaster(treatmentModel, conditions);
			if(recTMaster > 0){
				/**
				 * Edit treatment type.
				 */
				String[] conditions2 = {"treatment_id", String.valueOf(treatmentModel.getTreatmentID())};
				this.updateTreatmentToothType(treatmentModel, conditions2);
				
				/**
				 * Edit treatment pricelist.
				 */
				String[] conditions3 = {"treatment_id", String.valueOf(treatmentModel.getTreatmentID())};
				recTPriceList = this.updateTreatmentPriceList(treatmentModel, brandModel, conditions3);
				if(recTPriceList > 0){
					if(triggerStatus.equals("exit")){
						returnResult = SUCCESS;
					}else if(triggerStatus.equals("next")){
						returnResult = treatmentModel.getIsContinue() == 2 ? "CONTINUOUS" : "NOCONTINUOUS";
					}
				}else{
					returnResult = INPUT;
				}
			}else{
				returnResult = INPUT;
			}
		}else{
			returnResult = INPUT;
		}
		return returnResult;
	}
	
	/**
	 * Get treatment's credential to display in edit form.
	 * @author anubi | wesarut.khm@gmail.com
	 * @return String | Action result string.
	 */
	public String editTreatmentByID(){
		HttpServletRequest request = ServletActionContext.getRequest();
		/**
		 * Fetch brand.
		 */
		this.fetchBrand();
		
		/**
		 * Fetch treatment group.
		 */
		this.fetchTreatmentGroup();
		
		/**
		 * Fetch tooth picture.
		 */
		this.fetchToothFormat();
		
		/**
		 * Fetch tooth type.
		 */
		this.fetchToothType(0);

		/**
		 * Fetch treatment credential.
		 */
		String[] treatmentConditions = {"id", String.valueOf(treatmentModel.getTreatmentID())};
		this.selectTreatmentByID(treatmentConditions);
		
		/**
		 * Fetch category type.
		 */
		this.fetchTreatmentCategory(treatmentModel.getTreatmentGroupID());
		
		/**
		 * Fetch tooth type list.
		 */
		String[] conditions = {"treatment_id", String.valueOf(treatmentModel.getTreatmentID())};
		this.fetchTreatmentType(conditions);
		
		/**
		 * Fetch treatment price list.
		 */
		String[] pricelistConditions = {"treatment_id", String.valueOf(treatmentModel.getTreatmentID())};
		this.fetchTreatmentPriceList(pricelistConditions);
		
		/**
		 * Treatment format & Tooth picture.
		 */
		ToothMasterData toothData= new ToothMasterData();
		List<ToothModel> toothPicList = toothData.select_tooth_pic();
		request.setAttribute("toothPicList", toothPicList);

		List<ToothModel> toothListUp = toothData.select_tooth_list_arch("upper");
		request.setAttribute("toothListUp", toothListUp); 

		List<ToothModel> toothListLow = toothData.select_tooth_list_arch("lower");
		request.setAttribute("toothListLow", toothListLow); 
		return SUCCESS;
	}
	
	
	/**
	 * Get all treatment list filter by continuous type.
	 * @author anubi | wesarut.khm@gmail.com
	 * @return String | Struts action result.
	 */
	public String getTreatmentListFilterByContinuous(){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		treatmentList = tMasterData.getTreatmentByContinuousType(false);
		treatmentContinuousList = tMasterData.getTreatmentByContinuousType(true);
		return SUCCESS;
	}
	
	/**
	 * Add treatment continuous preference.
	 * @author anubi | wesarut.khm@gmail.com
	 * @return String | Action result.
	 */
	public String addTreatmentContinuousPreference(){
		/**
		 * Looping for add treatment continuous 
		 */
		TreatmentMasterData tMastereData = new TreatmentMasterData();
		List<Integer> resultList = new ArrayList<Integer>();
 	 	int phaseCount = treatmentModel.getRound().length;
 	 	int resultLength;
 		for(int i=0; i<phaseCount; i++){
 			HashMap<String, Integer> resultMap = tMastereData.addTreatmentContinuous(
				treatmentModel.getTreatmentID(), 
				i + 1, 
				treatmentModel.getRound()[i], 
				treatmentModel.getPhasePrice()[i], 
				treatmentModel.getStartPriceRange()[i], 
 				treatmentModel.getEndPriceRange()[i]
			);
 			resultLength = resultMap.size();
 			if(resultLength > 1){
 				for(int iterate=1; iterate<resultLength; iterate++){
 					StringBuilder sb = new StringBuilder();
 					resultList.add(
						resultMap.get(
							sb.append("ID").append(String.valueOf(iterate)).toString()
						)
					);
 				}
 			}
 		}
 		
 		/**
 		 * Add treatment phase detail.
 		 */
 		List<String> treatmentValList = new ArrayList<String>();
 		for(String tID : treatmentModel.getStrTreatmentID()){
 			String[] val = tID.split(":#:");
 			StringBuilder sb = new StringBuilder();
 			treatmentValList.add(
 				// Build str to ('5', '5', '5', '5') form.
 				sb.append("(")
 					//Treatment continuous phase id.
					.append("'").append(String.valueOf(resultList.get(Integer.valueOf(val[0])))).append("'").append(", ")
					//Treatment id.
 					.append("'").append(String.valueOf(val[1])).append("'").append(", ")
 					//Timestamps.
 					.append("NOW()").append(", ")
 					.append("NOW()")
 					.append(")").toString()
 			);
 		}
 		int treatRec = tMastereData.addTreatmentContinuousDetail(StringUtils.join(treatmentValList, ','));
 		
 		/**
 		 * Add product phase detail.
 		 */
 		List<String> productValList = new ArrayList<String>();
 		int i=0;
 		for(String pID : productModel.getStr_product_id_arr()){
 			String[] val = pID.split(":#:");
 			StringBuilder sb = new StringBuilder();
 			productValList.add(
				// Build str to ('4', '4', '4', '4', '4', '4') form.
				sb.append("(")
					//Treatment continuous phase id.
					.append("'").append(String.valueOf(resultList.get(Integer.valueOf(val[0])))).append("'").append(", ")
					//Medicine id.
					.append("'").append(String.valueOf(val[1])).append("'").append(", ")
					//Medicine's volumns.
					.append("'").append(String.valueOf(productModel.getProduct_volumn()[i])).append("'").append(", ")
					//Medicine's free volumns.
					.append("'").append(String.valueOf(productModel.getProduct_volumn_free()[i])).append("'").append(", ")
					//Timestamp
					.append("NOW()").append(", ")
					.append("NOW()")
					.append(")").toString()
			);
 			++i;
 		}
 		int productRec = tMastereData.addMedicineTreatmentContinuousDetail(StringUtils.join(productValList, ','));
 		
 		return SUCCESS;
	}
	
	/**
	 * Index of preference of treatment continuous.
	 * @author anubi | wesarut.khm@gmail.com
	 * @return String | Action result
	 */
	public String treatmentContinuousPreference(){
		
		/**
		 * Get medicine list.
		 */
		productList = this.getMedicineAndProductByTreatmentID(treatmentModel);
		
		/**
		 * Get treatment(non-continuous) list.
		 */
		treatmentList = this.getTreatmentContinuous(treatmentModel, false);
		return SUCCESS;
	}
	
	/**
	 * Adding product & med into the treatment master list.
	 * @author anubissmile | wesarut.khm@gmail.com
	 * @return String | Action result.
	 */
	public String addTreatmentMedicineExecute(){
		/**
		 * Adding med into treatment_product table.
		 */
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		tMasterData.addMedIntoTreatmentMaster(treatmentModel, productModel);
		
		return SUCCESS;
	}
	
	/**
	 * Matching medicine list and product list into treatment.
	 * @author anubissmile | wesarut.khm@gmail.com
	 * @return String  | Action result.
	 */
	public String addTreatmentMedicine(){
		
		/**
		 * Get medicine and product for put into treatment.
		 */
		productList = this.getMedicineAndProductByTreatmentID(treatmentModel);
		return SUCCESS;
	}
	
	/**
	 * Get medicine and product by treatment id.
	 * @author anubissmile | wesarut.khm@gmail.com
	 * @param TreatmentModel tModel |
	 * @return List<ProductModel>
	 */
	public List<ProductModel> getMedicineAndProductByTreatmentID(TreatmentModel tModel){
		TreatmentMasterData treatmentMData = new TreatmentMasterData();
		return  treatmentMData.getMedicineAndProductByTreatmentID(tModel);
	}
	
	/**
	 * Get treatment list by type of continuous or non-continuous
	 * @param TreatmentModel tModel
	 * @param boolean isContinuous | (true : continuous , false : none-continuous)
	 * @return List<TreatmentModel>
	 */
	public List<TreatmentModel> getTreatmentContinuous(TreatmentModel tModel, boolean isContinuous){
		TreatmentMasterData treatmentMData = new TreatmentMasterData();
		return  treatmentMData.getTreatmentContinuous(tModel, isContinuous);
	}
	
	public String begin() throws Exception{
		HttpServletRequest request = ServletActionContext.getRequest();
		
		/**
		 * Fetch brand.
		 */
		this.fetchBrand();
		
		/**
		 * Fetch treatment group
		 */
		this.fetchTreatmentGroup();
		
		/**
		 * Fetch tooth picture.
		 */
		this.fetchToothFormat();
		
		/**
		 * Fetch tooth type.
		 */
		this.fetchToothType(0);
		
		/**
		 * Treatment format & Tooth picture.
		 */
		ToothMasterData toothData = new ToothMasterData();
		List<ToothModel> toothPicList = toothData.select_tooth_pic();
		request.setAttribute("toothPicList", toothPicList);

		List<ToothModel> toothListUp = toothData.select_tooth_list_arch("upper");
		request.setAttribute("toothListUp", toothListUp); 
		
		List<ToothModel> toothListLow = toothData.select_tooth_list_arch("lower");
		request.setAttribute("toothListLow", toothListLow); 
		
		
		return SUCCESS;
	}
	
	
	/**
	 * Get treatment category by group id from ajax request.
	 * @author anubissmile
	 * @return String Action result.
	 */
	public String ajaxTreatmentCategory(){
		TreatmentData treatmentData = new TreatmentData();
		JSONArray jsonArr = new JSONArray();
		List<TreatmentModel> treatList = new ArrayList<TreatmentModel>();
		
		/**
		 * Get treatment category
		 */
		if(String.valueOf(treatmentModel.getTreatmentGroupID()) != null && treatmentModel.getTreatmentGroupID() > 0){
			treatList = treatmentData.getTreatmentCategory(treatmentModel.getTreatmentGroupID());
			for(TreatmentModel tModel : treatList){
				JSONObject jsonObj = new JSONObject();
				/*tModel.setTreatmentCategoryID(rs.getInt("category_id"));
				tModel.setTreatmentCategoryName(rs.getString("category_name"));
				tModel.setTreatmentCategoryCode(rs.getString("category_code"));
				tModel.setTreatmentGroupID(rs.getInt("group_id"));
				tModel.setTreatmentGroupCode(rs.getString("group_code"));
				tModel.setTreatmentGroupName(rs.getString("group_name"));*/
				try {
					jsonObj.put("category_id", tModel.getTreatmentCategoryID())
						.put("category_name", tModel.getTreatmentCategoryName())
						.put("category_code", tModel.getTreatmentCategoryCode())
						.put("group_id", tModel.getTreatmentGroupID())
						.put("group_code", tModel.getTreatmentGroupCode())
						.put("group_name", tModel.getTreatmentGroupName());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				jsonArr.put(jsonObj);
			}
			
			/**
			 * Return the response as JSON.
			 */
			try {
				HttpServletResponse response = ServletActionContext.getResponse();
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				response.getWriter().write(jsonArr.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	/**
	 * Add new treatment master.
	 * @author anubissmile
	 * @return String | Action result.
	 */
	public String execute() throws Exception{
		TreatmentMasterData treatmentData = new TreatmentMasterData();
		String strReturn = SUCCESS;
		
		/**
		 * Insert new treatment.
		 * - int[] rec = {count of row treatment_master, count of row treatment_tooth_type, treatment_master_id, count of row treatment_pricelist}
		 */
		int[] rec = treatmentData.addTreatmentMaster(treatmentModel, brandModel);
		
		if(rec[0] > 0 && rec[3] > 0){
			alertSuccess = "Adding new treatment successful.";
			
			/**
			 * Checking for continuous treatment.
			 */
			if(treatmentModel.getIsContinue() == 1){
				strReturn = SUCCESS;
			}else if(treatmentModel.getIsContinue() == 2){
				strReturn = "CONTINUOUS";
			}
			treatmentModel.setTreatmentID(rec[2]);
		}else{
			addActionError("Adding data goes wrong. Please try again or ensuring that your form is completed.");
			strReturn = INPUT;
			begin();
		}
		return strReturn;
	} 
	
	
	/**
	 * PRIVATE ZONE.
	 */
	
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
	private int updateTreatmentPriceList(TreatmentModel tModel, BrandModel bModel, String[] conditions){
		int rec = 0;
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		rec = tMasterData.updateTreatmentPriceList(tModel, bModel, conditions);
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
	private int updateTreatmentToothType(TreatmentModel tModel, String[] conditions){
		int rec = 0;
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		rec = tMasterData.updateTreatmentToothType(tModel, conditions);
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
	private int updateTreatmentMaster(TreatmentModel tModel, String[] conditions){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		int rec = tMasterData.updateTreatmentMaster(tModel, conditions);
		return rec;
	}
	
	/**
	 * Fetching brand and put into List<> brandList and HashMap<String, String> brandMap
	 * @author anubi
	 * @return void
	 */
	private void fetchBrand(){
		BrandData brandData = new BrandData();
		brandList = brandData.chunkBrand();
		brandMap = new HashMap<String, String>();
		for(BrandModel bModel : brandList){
			brandMap.put(
				String.valueOf(bModel.getBrand_id()),
				bModel.getBrand_name()
			);
		}
	}
	
	/**
	 * Fetching treatment group and put into List<> treatmentList and HashMap<> treatmentMap.
	 * @author anubi
	 * @return void
	 */
	private void fetchTreatmentGroup(){
		TreatmentData treatmentData = new TreatmentData();
		treatmentList = treatmentData.getTreatmentGroup(0);
		treatmentMap = new HashMap<String, String>();
		for(TreatmentModel treatModel : treatmentList){
			treatmentMap.put(
				String.valueOf(treatModel.getTreatmentGroupID()),
				treatModel.getTreatmentGroupName()
			);
		}
	}

	/**
	 * Fetching tooth format and put into List<> treatmentList and HashMap<> toothPicMap
	 * @author anubi
	 * @return void
	 */
	private void fetchToothFormat(){
		TreatmentData treatmentData = new TreatmentData();
		treatmentList = treatmentData.getToothPicture("");
		toothPicMap = new HashMap<String, String>();
		for(TreatmentModel tModel : treatmentList){
			toothPicMap.put(
				tModel.getToothPicCode(),
				tModel.getToothPicName()
			);
		}
	}
	
	/**
	 * Fetching tooth type and put into List<> treatmentList
	 * @author anubi
	 * @param int id | Tooth type id.
	 * @return void
	 */
	private void fetchToothType(Integer id){
		if(id == null){
			id = 0;
		}
		TreatmentData treatmentData = new TreatmentData();
		treatmentList = treatmentData.getToothType(0);
	}
	
	/**
	 * Select treatment by ID 
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | Where clause conditions.
	 * @return void 
	 */
	private void selectTreatmentByID(String[] conditions){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		treatmentList2 = tMasterData.selectTreatmentWhere(conditions);
		/**
		 * Set to Model(treatmentModel)
		 */
		if(treatmentList2.size() == 1){
			for(TreatmentModel tModel : treatmentList2){
				treatmentModel.setTreatmentID(tModel.getTreatmentID());
				treatmentModel.setTreatmentCode(tModel.getTreatmentCode());
				treatmentModel.setTreatmentNameTH(tModel.getTreatmentNameTH());
				treatmentModel.setTreatmentNameEN(tModel.getTreatmentNameEN());
				treatmentModel.setAutoHomeCall(tModel.getAutoHomeCall());
				treatmentModel.setRecall(tModel.getRecall());
				treatmentModel.setIsContinue(tModel.getIsContinue());
				treatmentModel.setIsRepeat(tModel.getIsRepeat());
				treatmentModel.setTreatmentMode(tModel.getTreatmentMode());
				treatmentModel.setTreatmentCategoryID(tModel.getTreatmentCategoryID());
				treatmentModel.setTreatmentGroupID(tModel.getTreatmentGroupID());
				treatmentModel.setToothPicCode(tModel.getToothPicCode());
			}
		}
	}
	
	/**
	 * Fetching treatment's price list filter by brand.
	 * <pre>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi
	 * @param String[] conditions | Where clause conditions.
	 * @return void
	 */
	private void fetchTreatmentPriceList(String[] conditions){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		treatmentModel.setPriceListModel(tMasterData.selectTreatmentPricelist(conditions));
	}
	
	/**
	 * Fetching treatment's category.
	 * @author anubi | wesarut.khm@gmail.com
	 * @param int id | Category's id.
	 * @return void
	 */
	private void fetchTreatmentCategory(int id){
		TreatmentData tData = new TreatmentData();
		List<TreatmentModel> tList = tData.getTreatmentCategory(id);
		categoryMap = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder();
		for(TreatmentModel tModel : tList){
			String name = sb.append(tModel.getTreatmentCategoryCode()).append(" ").append(tModel.getTreatmentCategoryName()).toString();
			categoryMap.put(String.valueOf(tModel.getTreatmentCategoryID()), name);
			sb.setLength(0);
		}
	}
	
	/**
	 * Fetching treatment's type by where clause conditions.
	 * <pre>
	 * <strong>Ext.</strong><br/>
	 * - String[] conditions = {"field name", "val"}
	 * - String[] conditions = {"field name", "=", "val"}
	 * - String[] conditions = {"field name", "<>", "val"}
	 * - String[] conditions = {"field name", "<", "val"}
	 * - String[] conditions = {"field name", ">", "val"}
	 * - String[] conditions = {"field name", ">=", "val"}
	 * - String[] conditions = {"field name", "<=", "val"}
	 * </pre>
	 * @author anubi | wesarut.khm@gmail.com
	 * @param String[] conditions | Where clause conditions in String[].
	 * @return void
	 */
	private void fetchTreatmentType(String[] conditions){
		TreatmentData tData = new TreatmentData();
		List<TreatmentModel> tList = tData.getTreatmentToothType(conditions);
		int[] toothTypeID = new int[tList.size()];
		int i = 0;
		for(TreatmentModel tModel : tList){
			toothTypeID[i] = tModel.getToothTypeID();
			++i;
		}
		treatmentModel.setToothTypeIDArr(toothTypeID);
	}
	
	private int deleteMedFromTreatmentMaster(String[] conditions, TreatmentModel tModel){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		return tMasterData.deleteMedFromTreatmentMaster(conditions, tModel);
	}
	
	/**
	 * Fetching productList by where clause conditions.
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
	 */
	private void fetchProductListByConditions(String[] conditions, TreatmentModel tModel){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		productList2 = tMasterData.getMedicineAndProductListByCondition(conditions, tModel);
	}
	
	/**
	 * Get treatment continuous's phase detail.
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
	 * @param conditions
	 * @param tModel
	 */
	private void getTreatmentContinuousPhaseDetail(String[] conditions, TreatmentModel tModel){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		treatmentContinuousModelList = tMasterData.getTreatmentContinuousPhaseDetail(conditions, tModel);
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
	 * @param conditions
	 * @param tModel
	 */
	private void getTreatmentContinuousProductPhase(String[] conditions, TreatmentModel tModel){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		treatmentContinuousModel.setProductPhaseList(tMasterData.getTreatmentContinuousProductPhase(conditions, tModel));
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
	private void getTreatmentContinuousTreatmentPhase(String[] conditions, TreatmentModel tModel){
		TreatmentMasterData tMasterData = new TreatmentMasterData();
		treatmentContinuousModel.setTreatmentPhaseList(tMasterData.getTreatmentContinuousTreatmentPhase(conditions, tModel));
	}
	
	/**
	 * GETTER & SETTER
	 */
	public ToothModel getToothModel() {
		return toothModel;
	}
	public void setToothModel(ToothModel toothModel) {
		this.toothModel = toothModel;
	}
	public TreatmentMasterModel getTreatmentMasterModel() {
		return treatmentMasterModel;
	}
	public void setTreatmentMasterModel(TreatmentMasterModel treatmentMasterModel) {
		this.treatmentMasterModel = treatmentMasterModel;
	}

	public List<BrandModel> getBrandList() {
		return brandList;
	}

	public void setBrandList(List<BrandModel> brandList) {
		this.brandList = brandList;
	}

	public HashMap<String, String> getBrandMap() {
		return brandMap;
	}

	public void setBrandMap(HashMap<String, String> brandMap) {
		this.brandMap = brandMap;
	}

	public List<TreatmentModel> getTreatmentList() {
		return treatmentList;
	}

	public void setTreatmentList(List<TreatmentModel> treatmentList) {
		this.treatmentList = treatmentList;
	}

	public HashMap<String, String> getTreatmentMap() {
		return treatmentMap;
	}

	public void setTreatmentMap(HashMap<String, String> treatmentMap) {
		this.treatmentMap = treatmentMap;
	}

	public TreatmentModel getTreatmentModel() {
		return treatmentModel;
	}

	public void setTreatmentModel(TreatmentModel treatmentModel) {
		this.treatmentModel = treatmentModel;
	}

	public HashMap<String, String> getToothPicMap() {
		return toothPicMap;
	}

	public void setToothPicMap(HashMap<String, String> toothPicMap) {
		this.toothPicMap = toothPicMap;
	}

	public HashMap<String, String> getToothTypeMap() {
		return toothTypeMap;
	}

	public void setToothTypeMap(HashMap<String, String> toothTypeMap) {
		this.toothTypeMap = toothTypeMap;
	}

	public BrandModel getBrandModel() {
		return brandModel;
	}

	public void setBrandModel(BrandModel brandModel) {
		this.brandModel = brandModel;
	}

	public String getAlertSuccess() {
		return alertSuccess;
	}

	public void setAlertSuccess(String alertSuccess) {
		this.alertSuccess = alertSuccess;
	}

	public String getAlertError() {
		return alertError;
	}

	public void setAlertError(String alertError) {
		this.alertError = alertError;
	}

	public String getAlertMSG() {
		return alertMSG;
	}

	public void setAlertMSG(String alertMSG) {
		this.alertMSG = alertMSG;
	}

	public List<ProductModel> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductModel> productList) {
		this.productList = productList;
	}

	public ProductModel getProductModel() {
		return productModel;
	}

	public void setProductModel(ProductModel productModel) {
		this.productModel = productModel;
	}

	public List<TreatmentModel> getTreatmentContinuousList() {
		return treatmentContinuousList;
	}

	public void setTreatmentContinuousList(List<TreatmentModel> treatmentContinuousList) {
		this.treatmentContinuousList = treatmentContinuousList;
	}

	public List<TreatmentModel> getTreatmentList2() {
		return treatmentList2;
	}

	public void setTreatmentList2(List<TreatmentModel> treatmentList2) {
		this.treatmentList2 = treatmentList2;
	}

	public HashMap<String, String> getCategoryMap() {
		return categoryMap;
	}

	public void setCategoryMap(HashMap<String, String> categoryMap) {
		this.categoryMap = categoryMap;
	}



	public String getTriggerStatus() {
		return triggerStatus;
	}



	public void setTriggerStatus(String triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	public List<ProductModel> getProductList2() {
		return productList2;
	}

	public void setProductList2(List<ProductModel> productList2) {
		this.productList2 = productList2;
	}


	public TreatmentContinuousModel getTreatmentContinuousModel() {
		return treatmentContinuousModel;
	}


	public List<TreatmentContinuousModel> getTreatmentContinuousModelList() {
		return treatmentContinuousModelList;
	}


	public void setTreatmentContinuousModel(TreatmentContinuousModel treatmentContinuousModel) {
		this.treatmentContinuousModel = treatmentContinuousModel;
	}


	public void setTreatmentContinuousModelList(List<TreatmentContinuousModel> treatmentContinuousModelList) {
		this.treatmentContinuousModelList = treatmentContinuousModelList;
	}
	
}
