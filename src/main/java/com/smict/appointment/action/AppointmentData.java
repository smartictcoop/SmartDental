package com.smict.appointment.action;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.smict.schedule.model.ScheduleModel;

import ldc.util.Auth;
import ldc.util.DBConnect;
import ldc.util.DateUtil;

public class AppointmentData {
	private DBConnect agent = new DBConnect();
	private DateUtil dateutil = new DateUtil();
	private ResultSet rs;
	
	/**
	 * Post update appointment info.
	 * @param appModel
	 * @return
	 */
	public HashMap<String, Integer> postEditAppointment(AppointmentModel appModel){
		int recMaster = 0;
		int recSymptom = 0;
		HashMap<String, Integer> recMap = new HashMap<String, Integer>();
		String SQLMaster = "UPDATE `dentist_appointment` SET `recommend`='" + appModel.getDescription() + "', "
				+ "`reminder_date`='" + appModel.getRemindDateCount() + "' "
						+ "WHERE (`id`='" + appModel.getAppointmentID() + "')";
		
		String SQLSymptom = "UPDATE `appointment_symptom_relate` SET `symptom_id`='" + appModel.getSymptomID() + "', "
				+ "`description`='" + appModel.getSymptom() + "' "
						+ "WHERE (`appointment_id`='" + appModel.getAppointmentID() + "')";
		
		agent.connectMySQL();
		agent.begin();
		/**
		 * Master table
		 */
		recMaster = agent.exeUpdate(SQLMaster);
		recSymptom = agent.exeUpdate(SQLSymptom);
		if(recMaster > 0 && recSymptom > 0){
			agent.commit();
		}else{
			agent.rollback();
		}
		
		agent.disconnectMySQL();
		
		/**
		 * Set result hashmap.
		 */
		recMap.put("master", recMaster);
		recMap.put("symptom", recSymptom);
		return recMap;
	}
	
	/**
	 * Get appointment by id through ajax.
	 * @author anubi
	 * @param String appointmentID | Appointment ID
	 * @return AppointmentModel appModel
	 */
	public AppointmentModel ajaxGetAppointmentByID(String appointmentID){
		AppointmentModel appModel = new AppointmentModel();
		String SQL = "SELECT dentist_appointment.id, dentist_appointment.`code`, "
				+ "dentist_appointment.doctor_id, dentist_appointment.hn, "
				+ "dentist_appointment.recommend, appointment_symptom.symptom_th, "
				+ "dentist_appointment.appointment_status, dentist_appointment.contact_status, "
				+ "appointment_symptom_relate.description, dentist_appointment.datetime_start, "
				+ "dentist_appointment.datetime_end, dentist_appointment.refer_other_appointment_id, "
				+ "appointment_symptom.id as 'symtomp_id', dentist_appointment.reminder_date "
				+ "FROM dentist_appointment "
				+ "INNER JOIN appointment_symptom_relate ON dentist_appointment.id = appointment_symptom_relate.appointment_id "
				+ "INNER JOIN appointment_symptom ON appointment_symptom_relate.symptom_id = appointment_symptom.id "
				+ "WHERE dentist_appointment.id = '" + appointmentID + "' ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		if(agent.size() > 0){
			rs = agent.getRs();
			try {
				while(rs.next()){
					appModel.setAppointmentID(rs.getInt("id"));
					appModel.setAppointmentCode(rs.getString("code"));
					appModel.setDoctorID(rs.getInt("doctor_id"));
					appModel.setHN(rs.getString("hn"));
					appModel.setRecommend(rs.getString("recommend"));
					appModel.setSymptom(rs.getString("symptom_th"));
					appModel.setAppointmentStatus(rs.getInt("appointment_status"));
					appModel.setContactStatus(rs.getInt("contact_status"));
					appModel.setDescription(rs.getString("description"));
					appModel.setDateStart(rs.getString("datetime_start"));
					appModel.setDateEnd(rs.getString("datetime_end"));
					appModel.setPostponeReferenceID(rs.getString("refer_other_appointment_id"));
					appModel.setSymptomID(rs.getInt("symtomp_id"));
					appModel.setRemindDate(rs.getInt("reminder_date"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				agent.disconnectMySQL();
			}
			return appModel;
		}
		agent.disconnectMySQL();
		return null;
	}
	
	/**
	 * Inserting symptom relate.
	 * @param AppointmentModel appModel
	 * @return int rec | Count of row that get affected.
	 */
	public int postInsertSymptomRelate(AppointmentModel appModel){
		int rec = 0;
		String SQL = "INSERT INTO `appointment_symptom_relate` (`appointment_id`, `symptom_id`, `description`) "
				+ "VALUES ('" + appModel.getAppointmentID() + "', '" + appModel.getSymptomID() + "', '" + appModel.getSymptom() + "')";
		agent.connectMySQL();
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
	 * Get all symptom.
	 * @author anubi
	 * @return List<AppointmentModel> sympList | Symptom dataset.
	 */
	public List<AppointmentModel> getSymptom(){
		List<AppointmentModel> sympList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT * FROM `appointment_symptom` LIMIT 0, 500";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				rs = agent.getRs();
				while(rs.next()){
					AppointmentModel appoModel = new AppointmentModel();
					appoModel.setSymptomID(rs.getInt("id"));
					appoModel.setSymptom(rs.getString("symptom_th"));
					sympList.add(appoModel);
				}
			}
		} catch (SQLException e) {
			System.out.println("Query error : " + e.getMessage());
			System.out.println("At : AppointmentData.getSymptom() ");
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		
		return sympList;
	}
	
	/**
	 * Update appointment code's next number.
	 * @param AppointmentModel appModel
	 * @return int rec | Count of row that affected.
	 */
	public int updateAppointmentNextNumber(AppointmentModel appModel){
		int rec = 0;
		String SQL = "UPDATE `_appointment_generation_code` SET "
				+ "`next_number`='" + appModel.getNextNumber() + "', "
				+ "`updated_date`= NOW() "
				+ "WHERE (`id`='" + appModel.getAppointmentCodeID() + "')";
		
		agent.connectMySQL();
		rec = agent.exeUpdate(SQL);
		agent.disconnectMySQL();
		return rec;
	}
	
	/**
	 * Update _appointment_generation_code tables
	 * @param AppointmentModel appModel
	 * @return int rec | Count of row that affected.
	 */
	public int updateAppointmentGenerationCode(AppointmentModel appModel){
		int rec = 0;
		String SQL = "UPDATE `_appointment_generation_code` SET "
				+ "`prefix`='" + appModel.getPrefix() + "', "
				+ "`separators`='" + appModel.getSeparator() + "', `length`='" + appModel.getLength() + "', "
				+ "`increment`='" + appModel.getIncrement() + "', `branch_id`='" + appModel.getBranchID() + "', "
				+ "`branch_code`='" + appModel.getBranchCode() + "', "
				+ "`updated_date`= NOW() "
				+ "WHERE (`id`='" + appModel.getAppointmentCodeID() + "')";
		
		agent.connectMySQL();
		rec = agent.exeUpdate(SQL);
		agent.disconnectMySQL();
		return rec;
	}
	
	/**
	 * Insert new appointment generation code.
	 * @author anubi
	 * @param AppointmentModel appModel
	 * @return int rec | Count of row that affected.
	 */
	public int insertAppointmentGenerationCode(AppointmentModel appModel){
		int rec = 0;
		String SQL = "INSERT INTO `_appointment_generation_code` (`prefix`, `separators`, "
				+ "`length`, `next_number`, "
				+ "`increment`, `branch_id`, "
				+ "`branch_code`, `created_date`, "
				+ "`updated_date`) "
				+ "VALUES ('" + appModel.getPrefix() + "', '" + appModel.getSeparator() + "', "
				+ "'" + appModel.getLength() + "', '" + appModel.getNextNumber() + "', "
				+ "'" + appModel.getIncrement() + "', '" + appModel.getBranchID() + "', "
				+ "'" + appModel.getBranchCode() + "', NOW(), NOW())";
		
		/**
		 * If doesn't exist then insert it!.
		 */
		if(agent.isExist("_appointment_generation_code", "branch_id = '" + appModel.getBranchID() + "' AND branch_code = '" + appModel.getBranchCode() + "'") < 1){
			agent.connectMySQL();
			rec = agent.exeUpdate(SQL);
			agent.disconnectMySQL();
		}
		
		return rec;
	}
	
	/**
	 * Get the latest the appointment code.
	 * @author anubi
	 * @param AppointmentModel appModel
	 * @return AppointmentModel appointmentModel
	 */
	public AppointmentModel getLatestAppointmentCode(AppointmentModel appModel){
		AppointmentModel appointmentModel = new AppointmentModel();
		String SQL = "SELECT _appointment_generation_code.id, _appointment_generation_code.prefix, _appointment_generation_code.separators, "
				+ "_appointment_generation_code.length, _appointment_generation_code.next_number, "
				+ "_appointment_generation_code.increment, _appointment_generation_code.branch_id, "
				+ "_appointment_generation_code.branch_code, _appointment_generation_code.created_date, "
				+ "_appointment_generation_code.updated_date FROM _appointment_generation_code "
				+ "WHERE _appointment_generation_code.branch_id = '" + appModel.getBranchID() + "' AND "
				+ "_appointment_generation_code.branch_code = '" + appModel.getBranchCode() + "' "
				+ "ORDER BY _appointment_generation_code.id DESC "
				+ "LIMIT 0, 1 ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		if(agent.size() > 0){
			/**
			 * Retreive value.
			 */
			try {
				rs = agent.getRs();
				rs.next();
				appointmentModel.setAppointmentCodeID(rs.getInt("id"));
				appointmentModel.setSeparator(rs.getString("separators").charAt(0));
				appointmentModel.setPrefix(rs.getString("prefix"));
				appointmentModel.setLength(rs.getInt("length"));
				appointmentModel.setNextNumber(rs.getInt("next_number"));
				appointmentModel.setIncrement(rs.getInt("increment"));
				appointmentModel.setBranchID(rs.getString("branch_id"));
				appointmentModel.setBranchCode(rs.getString("branch_code"));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				agent.disconnectMySQL();
			}
		}
		return appointmentModel;
	}
	
	/**
	 * Get doctor's agenda (without branch conditions).
	 * @author anubi
	 * @param AppointmentModel appModel |
	 * @return List<AppointmentModel> appList |
	 */
	public List<AppointmentModel> ajaxGetAgendaByDoctor(AppointmentModel appModel){
		List<AppointmentModel> appList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment.id, dentist_appointment.doctor_id, "
				+ "dentist_appointment.hn, dentist_appointment.recommend, "
				+ "dentist_appointment.branch_code, dentist_appointment.branch_id, "
				+ "dentist_appointment.datetime_start, dentist_appointment.datetime_end, "
				+ "dentist_appointment.appointment_status, dentist_appointment.contact_status, "
				+ "dentist_appointment.created_date, dentist_appointment.updated_date, "
				+ "dentist_appointment.`code`, dentist_appointment.refer_other_appointment_id, "
				+ "dentist_appointment.reminder_date, doctor.first_name_th, "
				+ "doctor.last_name_th, doctor.colour, pre_name.pre_name_th "
				+ "FROM dentist_appointment "
				+ "INNER JOIN doctor ON dentist_appointment.doctor_id = doctor.doctor_id "
				+ "LEFT JOIN pre_name ON doctor.pre_name_id = pre_name.pre_name_id "
				+ "WHERE dentist_appointment.doctor_id = '" + appModel.getDoctorID() + "' AND dentist_appointment.datetime_start LIKE '" + appModel.getDate() + "%' "
				+ "ORDER BY dentist_appointment.datetime_start ASC ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			rs = agent.getRs();
			while(rs.next()){
				AppointmentModel aModel = new AppointmentModel();
				aModel.setAppointmentID(rs.getInt("id"));
				aModel.setDoctorID(rs.getInt("doctor_id"));
				aModel.setDocprenameth(rs.getString("pre_name_th"));
				aModel.setDocfirstname(rs.getString("first_name_th"));
				aModel.setDoclastname(rs.getString("last_name_th"));
				aModel.setColour(rs.getString("colour"));
				aModel.setHN(rs.getString("hn"));
				aModel.setDescription(rs.getString("recommend"));
				aModel.setBranchCode(rs.getString("branch_code"));
				aModel.setBranchID(rs.getString("branch_id"));
				aModel.setDateStart(rs.getString("datetime_start"));
				aModel.setDateEnd(rs.getString("datetime_end"));
				aModel.setAppointmentStatus(rs.getInt("appointment_status"));
				aModel.setContactStatus(rs.getInt("contact_status"));
				aModel.setAppointmentCode(rs.getString("code"));
				aModel.setPostponeReferenceID(rs.getString("refer_other_appointment_id"));
				aModel.setRemindDate(rs.getInt("reminder_date"));
				appList.add(aModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appList;
	}
	
	/**
	 * Get all doctor's schedule by date range (without branch conditions).
	 * @author anubi
	 * @param List<AppointmentModel> appModel
	 * @return List<ScheduleModel> scheduleList
	 */
	public List<ScheduleModel> getAllDoctorScheduleByDateRange(AppointmentModel appModel){
		List<ScheduleModel> scheduleList = new ArrayList<ScheduleModel>();
		String SQL = "SELECT doctor_workday.workday_id, doctor_workday.doctor_id, "
				+ "doctor_workday.start_datetime, doctor_workday.end_datetime, "
				+ "doctor_workday.work_hour, doctor_workday.branch_id, "
				+ "doctor_workday.branch_room_id, doctor_workday.checkin_status, "
				+ "doctor_workday.checkin_datetime, doctor_workday.checkout_datetime, "
				+ "doctor.pre_name_id, doctor.first_name_th, doctor.colour, "
				+ "doctor.last_name_th, doctor.first_name_en, "
				+ "doctor.last_name_en, pre_name.pre_name_th, "
				+ "pre_name.pre_name_en, branch.branch_code, branch.branch_id, "
				+ "branch.branch_name FROM doctor_workday "
				+ "LEFT JOIN doctor ON doctor_workday.doctor_id = doctor.doctor_id "
				+ "LEFT JOIN pre_name ON doctor.pre_name_id = pre_name.pre_name_id "
				+ "LEFT JOIN branch ON doctor_workday.branch_id = branch.branch_code	 "
				+ "WHERE doctor_workday.doctor_id = '" + appModel.getDoctorID() + "' "
				+ "AND doctor_workday.start_datetime LIKE '" + appModel.getDate() + "%' ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				rs = agent.getRs();
				while(rs.next()){
					ScheduleModel schModel = new ScheduleModel();
					schModel.setWorkDayId(rs.getInt("workday_id"));
					schModel.setDoctorId(rs.getInt("doctor_id"));
					schModel.setStartDateTime(rs.getString("start_datetime"));
					schModel.setEndDateTime(rs.getString("end_datetime"));
					schModel.setWorkHour(rs.getInt("work_hour"));
					schModel.setStrBranchID(rs.getString("branch.branch_id"));
					schModel.setStrBranchCode(rs.getString("branch.branch_code"));
					schModel.setBranchName(rs.getString("branch_name"));
					schModel.setBranchRoomId(rs.getInt("branch_room_id"));
					schModel.setCheckInStatus(rs.getString("checkin_status"));
					schModel.setCheckInDateTime(rs.getString("checkin_datetime"));
					schModel.setCheckOutDateTime(rs.getString("checkout_datetime"));
					schModel.setPre_name_th(rs.getString("pre_name_th"));
					schModel.setFirst_name_th(rs.getString("first_name_th"));
					schModel.setLast_name_th(rs.getString("last_name_th"));
					scheduleList.add(schModel);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		
		return scheduleList;
	}
	
	/**
	 * Chunk all doctor's appointment.
	 * @author anubi
	 * @param AppointmentModel appModel
	 * @return List<AppointmentModel> appList
	 */
	public List<AppointmentModel> getDoctorAppointment(AppointmentModel appModel){
		List<AppointmentModel> appList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment.id, dentist_appointment.doctor_id, "
				+ "dentist_appointment.hn, dentist_appointment.recommend, "
				+ "dentist_appointment.branch_code, dentist_appointment.branch_id, "
				+ "dentist_appointment.datetime_start, dentist_appointment.datetime_end, "
				+ "dentist_appointment.contact_status, dentist_appointment.appointment_status, "
				+ "dentist_appointment.created_date, dentist_appointment.updated_date, "
				+ "dentist_appointment.`code`, dentist_appointment.refer_other_appointment_id, "
				+ "pre_name.pre_name_th, doctor.first_name_th, "
				+ "doctor.last_name_th, doctor.colour "
				+ "FROM dentist_appointment "
				+ "INNER JOIN doctor ON dentist_appointment.doctor_id = doctor.doctor_id "
				+ "LEFT JOIN pre_name ON doctor.pre_name_id = pre_name.pre_name_id "
				+ "WHERE (dentist_appointment.datetime_start BETWEEN '" + appModel.getDateStart() + "' AND '" + appModel.getDateEnd() + "') AND "
				+ "dentist_appointment.branch_id = '" + appModel.getBranchID() + "' AND "
				+ "dentist_appointment.branch_code = '" + appModel.getBranchCode() + "' ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				ResultSet rs = agent.getRs();
				while(rs.next()){
					AppointmentModel aModel = new AppointmentModel();
					aModel.setAppointmentID(rs.getInt("id"));
					aModel.setDoctorID(rs.getInt("doctor_id"));
					aModel.setHN(rs.getString("hn"));
					aModel.setDescription(rs.getString("recommend"));
					aModel.setBranchCode(rs.getString("branch_code"));
					aModel.setBranchID(rs.getString("branch_id"));
					aModel.setContactStatus(rs.getInt("contact_status"));
					aModel.setAppointmentStatus(rs.getInt("appointment_status"));
					aModel.setDateStart(rs.getString("datetime_start"));
					aModel.setDateEnd(rs.getString("datetime_end"));
					aModel.setAppointCode(rs.getString("code"));
					aModel.setPostponeReferenceID(rs.getString("refer_other_appointment_id"));
					aModel.setDocfirstname(rs.getString("first_name_th"));
					aModel.setDoclastname(rs.getString("last_name_th"));
					aModel.setDocprenameth(rs.getString("pre_name_th"));
					aModel.setColour(rs.getString("colour"));
					appList.add(aModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		agent.disconnectMySQL();
		return appList;
	}
	
	/**
	 * Make a new appointment into weed calendar.
	 * @author anubi
	 * @param AppointmentModel appModel | 
	 * @return int rec | Count of row that get affected.
	 */
	public List<Integer> postMakeAppointmentWeekCalendar(AppointmentModel appModel){
		int rec = 0;
		List<Integer> idList = new ArrayList<Integer>();
		String SQL = "INSERT INTO `dentist_appointment` (`doctor_id`, `code`, `hn`, "
				+ "`recommend`, `branch_code`, "
				+ "`branch_id`, `datetime_start`, "
				+ "`datetime_end`, `contact_status`, "
				+ "`appointment_status`, `reminder_date`, `created_date`, "
				+ "`updated_date`, `refer_other_appointment_id`, `lab_id` ) "
				+ "VALUES ('" + appModel.getDoctorID() + "', '" + appModel.getAppointCode() + "', '" + appModel.getHN() + "', "
				+ "'" + appModel.getDescription() + "', '" + appModel.getBranchCode() + "', "
				+ "'" + appModel.getBranchID() + "', '" + appModel.getDateStart() + "', "
				+ "'" + appModel.getDateEnd() + "', '2', "
				+ "'5', '" + appModel.getRemindDateCount() + "', NOW(), NOW(), '" + appModel.getPostponeReferenceID() +"','" + appModel.getLab_id() + "')";
		
		agent.connectMySQL();
		agent.begin();
		rec = agent.exeUpdate(SQL);
		if(rec > 0){
			try {
				while(agent.getRs().next()){
					idList.add(agent.getRs().getInt(1));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			agent.commit();
		}else{
			agent.rollback();
		}
		agent.disconnectMySQL();
		return idList;
	}
	
	/**
	 * Make a new appointment into calendar.
	 * @param AppointmentModel appModel |
	 * @return int rec | Count of row that get affected.
	 */
	public int postMakeAppointment(AppointmentModel appModel){
		int rec = 0;
		String SQL = "INSERT INTO `dentist_appointment` "
				+ "(`doctor_id`, `hn`, "
				+ "`description`, `branch_code`, "
				+ "`branch_id`, `datetime_start`, "
				+ "`datetime_end`, `status`, "
				+ "`created_date`, `updated_date`) "
				+ "VALUES ('" + appModel.getDoctorID() + "', "
				+ "'" + appModel.getHN() + "', "
				+ "'" + appModel.getDescription() + "', "
				+ "'" + appModel.getBranchCode() + "', "
				+ "'" + appModel.getBranchID() + "', "
				+ "'" + appModel.getDateStart() + "', "
				+ "'" + appModel.getDateEnd() + "', "
				+ "'0', "
				+ "NOW(), "
				+ "NOW()"
				+ ") ";
		
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
	 * Get doctor's appointment that incoming.
	 * @author anubi
	 * @param AppointmentModel appModel
	 * @return List<AppointmentModel> appModelList
	 */
	public List<AppointmentModel> getAppointmentIncoming(AppointmentModel appModel){
		List<AppointmentModel> appModelList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment.id, dentist_appointment.doctor_id, "
				+ "dentist_appointment.hn, dentist_appointment.description, "
				+ "dentist_appointment.branch_code, dentist_appointment.branch_id, "
				+ "dentist_appointment.datetime_start, dentist_appointment.datetime_end, "
				+ "dentist_appointment.`contact_status`, dentist_appointment.`appointment_status`, dentist_appointment.created_date, "
				+ "dentist_appointment.updated_date, patient.hn, patient.first_name_th, "
				+ "patient.last_name_th, patient.first_name_en, "
				+ "patient.last_name_en, patient.identification "
				+ "FROM dentist_appointment "
				+ "INNER JOIN patient ON dentist_appointment.hn = patient.hn "
				+ "WHERE dentist_appointment.doctor_id = '" + appModel.getDoctorID() + "' AND "
				+ "(dentist_appointment.branch_code = '" + appModel.getBranchID() + "' OR dentist_appointment.branch_id = '" + appModel.getBranchID() + "') AND "
				+ "dentist_appointment.`status` = '0' "
				+ "ORDER BY dentist_appointment.datetime_start ASC ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					AppointmentModel apModel = new AppointmentModel();
					/*dentist_appointment.id,
					dentist_appointment.doctor_id,
					dentist_appointment.hn,
					dentist_appointment.description,
					dentist_appointment.branch_code,
					dentist_appointment.branch_id,
					dentist_appointment.datetime_start,
					dentist_appointment.datetime_end,
					dentist_appointment.`status`,
					dentist_appointment.created_date,
					dentist_appointment.updated_date
					patient.first_name_th,
					patient.last_name_th,
					patient.first_name_en,
					patient.last_name_en,
					patient.identification*/
					
					apModel.setAppointmentID(agent.getRs().getInt("id"));
					apModel.setDoctorID(agent.getRs().getInt("doctor_id"));
					apModel.setHN(agent.getRs().getString("hn"));
					apModel.setDescription(agent.getRs().getString("description"));
					apModel.setBranchCode(agent.getRs().getString("branch_code"));
					apModel.setBranchID(agent.getRs().getString("branch_id"));
					apModel.setDateStart(agent.getRs().getString("datetime_start"));
					apModel.setDateEnd(agent.getRs().getString("datetime_end"));
					
					apModel.setFirstNameTH(agent.getRs().getString("first_name_th"));
					apModel.setLastNameTH(agent.getRs().getString("last_name_th"));
					apModel.setFirstNameEN(agent.getRs().getString("first_name_en"));
					apModel.setLastNameEN(agent.getRs().getString("last_name_en"));
					apModel.setIdentification(agent.getRs().getString("identification"));
					appModelList.add(apModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appModelList;
	}
	public AppointmentModel getAppointmentallDetail(AppointmentModel appModel){
		AppointmentModel apModel = new AppointmentModel();
		String SQL = "SELECT dentist_appointment.id,dentist_appointment.`code`, "
				+ "dentist_appointment.doctor_id,dentist_appointment.hn, "
				+ "dentist_appointment.recommend,dentist_appointment.branch_code, "
				+ "dentist_appointment.branch_id,dentist_appointment.datetime_start, "
				+ "dentist_appointment.datetime_end,dentist_appointment.contact_status, "
				+ "dentist_appointment.appointment_status,dentist_appointment.refer_other_appointment_id, "
				+ "dentist_appointment.reminder_date,dentist_appointment.created_date, "
				+ "dentist_appointment.updated_date,dentist_appointment_status_log.description, "
				+ "branch.branch_name,doctor.first_name_th,doctor.last_name_th, "
				+ "pre_name.pre_name_th,patient.first_name_th,patient.last_name_th,p1.pre_name_th,patient.contact_time_start,patient.contact_time_end "
				+ ",lab_tra.id as lab_id,lab_tra.create_date,lab_tra.required_date,lab_tra.update_date "
				+ ",CASE lab_tra.lab_status WHEN 'W' THEN 'รอรับ lab' WHEN 'R' THEN 'รับ lab แล้ว'  ELSE 'ไม่พบ lab' END as status_lab "
				+ "FROM "
				+ "dentist_appointment "
				+ "LEFT JOIN dentist_appointment_status_log ON dentist_appointment.id = dentist_appointment_status_log.appointment_id "
				+ "INNER JOIN branch ON branch.branch_id = dentist_appointment.branch_id "
				+ "INNER JOIN doctor ON doctor.doctor_id = dentist_appointment.doctor_id "
				+ "INNER JOIN pre_name ON doctor.pre_name_id = pre_name.pre_name_id "
				+ "INNER JOIN patient ON patient.hn = dentist_appointment.hn "
				+ "INNER JOIN pre_name p1 ON patient.pre_name_id = p1.pre_name_id "
				+ "LEFT JOIN lab_transaction lab_tra ON lab_tra.id = dentist_appointment.lab_id "
				+ "WHERE dentist_appointment.id = '"+appModel.getAppointmentID()+"' ";
		
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					
					apModel.setAppointmentID(agent.getRs().getInt("dentist_appointment.id"));
					apModel.setAppointCode(agent.getRs().getString("dentist_appointment.code"));
					apModel.setDoctorID(agent.getRs().getInt("dentist_appointment.doctor_id"));
					apModel.setHN(agent.getRs().getString("dentist_appointment.hn"));
					apModel.setRecommend(agent.getRs().getString("dentist_appointment.recommend"));
					apModel.setDescription(agent.getRs().getString("dentist_appointment_status_log.description"));
					apModel.setBranchCode(agent.getRs().getString("dentist_appointment.branch_code"));
					apModel.setBranchID(agent.getRs().getString("dentist_appointment.branch_id"));
					apModel.setAppconstatus(agent.getRs().getInt("dentist_appointment.contact_status"));
					apModel.setAppointmentStatus(agent.getRs().getInt("dentist_appointment.appointment_status"));
					apModel.setReferID(agent.getRs().getString("dentist_appointment.refer_other_appointment_id"));
					apModel.setBranchName(agent.getRs().getString("branch.branch_name"));
					apModel.setDocfirstname(agent.getRs().getString("doctor.first_name_th"));
					apModel.setDoclastname(agent.getRs().getString("doctor.last_name_th"));
					apModel.setFirstNameTH(agent.getRs().getString("patient.first_name_th"));
					apModel.setLastNameTH(agent.getRs().getString("patient.last_name_th"));
					apModel.setPatPrenameth(agent.getRs().getString("p1.pre_name_th"));
					apModel.setDocprenameth(agent.getRs().getString("pre_name.pre_name_th"));
					apModel.setPattimestart(agent.getRs().getString("patient.contact_time_start"));
					apModel.setPattimeend(agent.getRs().getString("patient.contact_time_end"));
					String start = dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","dd/MM/yyyy HH:mm",agent.getRs().getString("dentist_appointment.datetime_start"),false);
					String startarr [] = start.split(" ");
					apModel.setDate(startarr [0]);
					apModel.setTimeStart(startarr[1]);
					apModel.setTimeEnd(dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","HH:mm",agent.getRs().getString("dentist_appointment.datetime_end"),false));
					
					apModel.setCreate_date_lab(agent.getRs().getString("create_date"));
					apModel.setRequire_date_lab(agent.getRs().getString("required_date"));
					apModel.setUpdate_date_lab(agent.getRs().getString("update_date"));
					apModel.setStatus_lab(agent.getRs().getString("status_lab"));
					apModel.setLab_id(agent.getRs().getInt("lab_id"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return apModel;
	}
	public List<AppointmentModel> getAppointmentSymptomRelate(AppointmentModel appModel){
		List<AppointmentModel> appModelList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT appointment_symptom_relate.id,appointment_symptom_relate.appointment_id, "
				+ "appointment_symptom_relate.symptom_id,appointment_symptom_relate.description, "
				+ "appointment_symptom.symptom_th,appointment_symptom.symptom_en "
				+ "FROM "
				+ "appointment_symptom_relate "
				+ "INNER JOIN appointment_symptom ON appointment_symptom_relate.symptom_id = appointment_symptom.id "
				+ "WHERE appointment_symptom_relate.appointment_id = '"+appModel.getAppointmentID()+"' ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					AppointmentModel apModel = new AppointmentModel();
					apModel.setSympDescription(agent.getRs().getString("appointment_symptom_relate.description"));
					appModelList.add(apModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appModelList;
	}
	public List<AppointmentModel> getAppointmentContactLog(AppointmentModel appModel){
		List<AppointmentModel> appModelList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment_contact_log.id,dentist_appointment_contact_log.appointment_id, "
				+ "dentist_appointment_contact_log.description,dentist_appointment_contact_log.status_code, "
				+ "dentist_appointment_contact_log.created_date "
				+ "FROM "
				+ "dentist_appointment_contact_log "
				+ "WHERE dentist_appointment_contact_log.appointment_id = '"+appModel.getAppointmentID()+"' "
				+ "ORDER BY dentist_appointment_contact_log.id ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					AppointmentModel apModel = new AppointmentModel();
					apModel.setConractdes(agent.getRs().getString("dentist_appointment_contact_log.description"));
					apModel.setContactStatus(agent.getRs().getInt("dentist_appointment_contact_log.status_code"));
					apModel.setContactdate(dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","dd/MM/yyyy HH:mm",agent.getRs().getString("dentist_appointment_contact_log.created_date"),false));
					appModelList.add(apModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appModelList;
	}
	public int updateAppointmentStatus(int appID,String conStatus,String appStatus){
		int rec = 0;
		if(conStatus != null || appStatus != null){
			String SQL = "UPDATE dentist_appointment "
					+ "SET ";
					if(conStatus != null){
						SQL += "contact_status = '"+conStatus+"'";
					}
					
					if(appStatus != null){
						SQL += "appointment_status = '"+appStatus+"'";
					}
					SQL +="WHERE id = '"+appID+"' ";
			
			agent.connectMySQL();
			agent.begin();
			rec = agent.exeUpdate(SQL);
			if(rec > 0){
				agent.commit();
			}else{
				agent.rollback();
			}
			agent.disconnectMySQL();
		}
		return rec;
	}
	public int insertAppointmentContactLog(AppointmentModel appModel){
		int rec = 0;
		String SQL = "INSERT INTO `dentist_appointment_contact_log` "
				+ "(`appointment_id`, `description`, "
				+ "`status_code`, `created_date`) "
				+ "VALUES ('" + appModel.getAppointmentID() + "', "
				+ "'" + appModel.getConractdes() + "', "
				+ "'" + appModel.getContactStatus() + "', "
				+ "NOW()"
				+ ") ";
		
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
	public int insertAppointmentStatusLog(AppointmentModel appModel){
		int rec = 0;
		String SQL = "INSERT INTO `dentist_appointment_status_log` "
				+ "(`appointment_id`, `description`, "
				+ "`status_code`, `created_date`) "
				+ "VALUES ('" + appModel.getAppointmentID() + "', "
				+ "'" + appModel.getDescription() + "', "
				+ "'" + appModel.getAppointmentStatus() + "', "
				+ "NOW()"
				+ ") ";
		
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
	public int updateAppointmentIsView(AppointmentModel appModel){
		int rec = 0;
		String SQL = "UPDATE  dentist_appointment "
				+ "SET "
				+ "isview = '1' "
				+ "WHERE id = '" + appModel.getAppointmentID() + "' ";
		
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
	public int updateAppointmentIsdayView(AppointmentModel appModel){
		int rec = 0;
		String SQL = "UPDATE  dentist_appointment "
				+ "SET "
				+ "isdayview = now() "
				+ "WHERE id = '" + appModel.getAppointmentID() + "' ";
		
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
	 * delete appointment
	 * @param appModel
	 * @return
	 */
	public int deleteAppoinment(int appModelid){
		int rec = 0;
		String SQL = "DELETE FROM  dentist_appointment "
				+ "WHERE id = '" + appModelid + "' ";
		
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
	public List<AppointmentModel> getAppointmentListByID(AppointmentModel appModel){
		List<AppointmentModel> appModelList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment.id, dentist_appointment.`code`, "
				+ "dentist_appointment.doctor_id, dentist_appointment.hn, "
				+ "dentist_appointment.recommend, dentist_appointment.branch_code, "
				+ "dentist_appointment.branch_id, dentist_appointment.datetime_start, "
				+ "dentist_appointment.datetime_end, dentist_appointment.contact_status, "
				+ "dentist_appointment.appointment_status, pre_name.pre_name_th, "
				+ "patient.first_name_th, patient.last_name_th, "
				+ "patient_file_id.branch_hn, dentist_appointment.branch_code "
				+ "FROM dentist_appointment "
				+ "INNER JOIN patient ON patient.hn = dentist_appointment.hn "
				+ "INNER JOIN pre_name ON patient.pre_name_id = pre_name.pre_name_id "
				+ "INNER JOIN patient_file_id ON patient.hn = patient_file_id.hn "
				+ "WHERE dentist_appointment.branch_code = '" + Auth.user().getBranchCode() + "' AND "
				+ "dentist_appointment.`id` = '" + appModel.getAppointmentID() + "' "
				+ "ORDER BY datetime_start ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					AppointmentModel apModel = new AppointmentModel();
					apModel.setAppointmentID(agent.getRs().getInt("dentist_appointment.id"));
					apModel.setAppointCode(agent.getRs().getString("dentist_appointment.code"));
					apModel.setHN(agent.getRs().getString("dentist_appointment.hn"));
					apModel.setBranch_hn(agent.getRs().getString("patient_file_id.branch_hn"));
					apModel.setAppconstatus(agent.getRs().getInt("dentist_appointment.contact_status"));
					apModel.setAppointmentStatus(agent.getRs().getInt("dentist_appointment.appointment_status"));
					apModel.setFirstNameTH(agent.getRs().getString("patient.first_name_th"));
					apModel.setLastNameTH(agent.getRs().getString("patient.last_name_th"));
					apModel.setBranchCode(agent.getRs().getString("dentist_appointment.branch_code"));
					apModel.setPatPrenameth(agent.getRs().getString("pre_name.pre_name_th"));
					String start = dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","dd/MM/yyyy HH:mm",agent.getRs().getString("dentist_appointment.datetime_start"),true);
					String startarr [] = start.split(" ");
					apModel.setDate(startarr [0]);
					apModel.setTimeStart(startarr[1]);
					apModel.setTimeEnd(dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","HH:mm",agent.getRs().getString("dentist_appointment.datetime_end"),true));
					appModelList.add(apModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appModelList;
	}
	public List<AppointmentModel> getAppointmentListShow(){
		List<AppointmentModel> appModelList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment.id,dentist_appointment.`code`,  "
				+ "dentist_appointment.doctor_id,dentist_appointment.hn, "
				+ "dentist_appointment.recommend,dentist_appointment.branch_code, "
				+ "dentist_appointment.branch_id,dentist_appointment.datetime_start,dentist_appointment.datetime_end,  "
				+ "dentist_appointment.contact_status,dentist_appointment.appointment_status,"
				+ "pre_name.pre_name_th,patient.first_name_th,patient.last_name_th,patient_file_id.branch_hn,dentist_appointment.branch_code "
				+ "FROM "
				+ "dentist_appointment "
				+ "INNER JOIN patient ON patient.hn = dentist_appointment.hn "
				+ "INNER JOIN pre_name ON patient.pre_name_id = pre_name.pre_name_id "
				+ "INNER JOIN patient_file_id ON patient.hn = patient_file_id.hn "
				+ "WHERE DATE_FORMAT(dentist_appointment.datetime_start,'%Y-%m-%d') = CURDATE() "
				+ "AND dentist_appointment.branch_code = '"+Auth.user().getBranchCode()+"' "
				+ "ORDER BY datetime_start ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					AppointmentModel apModel = new AppointmentModel();
					apModel.setAppointmentID(agent.getRs().getInt("dentist_appointment.id"));
					apModel.setAppointCode(agent.getRs().getString("dentist_appointment.code"));
					apModel.setHN(agent.getRs().getString("dentist_appointment.hn"));
					apModel.setBranch_hn(agent.getRs().getString("patient_file_id.branch_hn"));
					apModel.setAppconstatus(agent.getRs().getInt("dentist_appointment.contact_status"));
					apModel.setAppointmentStatus(agent.getRs().getInt("dentist_appointment.appointment_status"));
					apModel.setFirstNameTH(agent.getRs().getString("patient.first_name_th"));
					apModel.setLastNameTH(agent.getRs().getString("patient.last_name_th"));
					apModel.setBranchCode(agent.getRs().getString("dentist_appointment.branch_code"));
					apModel.setPatPrenameth(agent.getRs().getString("pre_name.pre_name_th"));
					String start = dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","dd/MM/yyyy HH:mm",agent.getRs().getString("dentist_appointment.datetime_start"),true);
					String startarr [] = start.split(" ");
					apModel.setDate(startarr [0]);
					apModel.setTimeStart(startarr[1]);
					apModel.setTimeEnd(dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","HH:mm",agent.getRs().getString("dentist_appointment.datetime_end"),true));
					appModelList.add(apModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appModelList;
	}
	public List<AppointmentModel> getAppointmentListSearchDate(String dateStart,String dateEnd,String branchid){
		List<AppointmentModel> appModelList = new ArrayList<AppointmentModel>();
		String SQL = "SELECT dentist_appointment.id,dentist_appointment.`code`,  "
				+ "dentist_appointment.doctor_id,dentist_appointment.hn, "
				+ "dentist_appointment.recommend,dentist_appointment.branch_code, "
				+ "dentist_appointment.branch_id,dentist_appointment.datetime_start,dentist_appointment.datetime_end,  "
				+ "dentist_appointment.contact_status,dentist_appointment.appointment_status,"
				+ "pre_name.pre_name_th,patient.first_name_th,patient.last_name_th,patient_file_id.branch_hn,dentist_appointment.branch_code "
				+ "FROM "
				+ "dentist_appointment "
				+ "INNER JOIN patient ON patient.hn = dentist_appointment.hn "
				+ "INNER JOIN pre_name ON patient.pre_name_id = pre_name.pre_name_id "
				+ "INNER JOIN patient_file_id ON patient.hn = patient_file_id.hn "
				+ "WHERE dentist_appointment.datetime_start BETWEEN '"+dateStart+ " 00:00:00" +"' AND '"+dateEnd+ " 23:59:59" + "' ";
				if(branchid.equals("0")){
				
				}else{
					SQL += "AND dentist_appointment.branch_id = '"+branchid+"' ";
				}
				SQL += "ORDER BY datetime_start ";
		
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					AppointmentModel apModel = new AppointmentModel();
					apModel.setAppointmentID(agent.getRs().getInt("dentist_appointment.id"));
					apModel.setAppointCode(agent.getRs().getString("dentist_appointment.code"));
					apModel.setHN(agent.getRs().getString("dentist_appointment.hn"));
					apModel.setBranch_hn(agent.getRs().getString("patient_file_id.branch_hn"));
					apModel.setAppconstatus(agent.getRs().getInt("dentist_appointment.contact_status"));
					apModel.setAppointmentStatus(agent.getRs().getInt("dentist_appointment.appointment_status"));
					apModel.setFirstNameTH(agent.getRs().getString("patient.first_name_th"));
					apModel.setLastNameTH(agent.getRs().getString("patient.last_name_th"));
					apModel.setBranchCode(agent.getRs().getString("dentist_appointment.branch_code"));
					apModel.setPatPrenameth(agent.getRs().getString("pre_name.pre_name_th"));
					String start = dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","dd/MM/yyyy HH:mm",agent.getRs().getString("dentist_appointment.datetime_start"),true);
					String startarr [] = start.split(" ");
					apModel.setDate(startarr [0]);
					apModel.setTimeStart(startarr[1]);
					apModel.setTimeEnd(dateutil.convertDateSpecificationPattern("yyyy-MM-dd HH:mm:ss.S","HH:mm",agent.getRs().getString("dentist_appointment.datetime_end"),true));
					appModelList.add(apModel);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return appModelList;
	}
	public String getDatetime(){
		String date= null;
		String SQL = "SELECT NOW() ";
	
		agent.connectMySQL();
		agent.exeQuery(SQL);
		try {
			if(agent.size() > 0){
				while(agent.getRs().next()){
					
				 date = agent.getRs().getString("NOW()"); 
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			agent.disconnectMySQL();
		}
		return date;
	}
	
	
}
