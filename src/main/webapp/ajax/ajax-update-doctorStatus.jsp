<%@page import="org.codehaus.jettison.json.JSONObject"%>
<%@ page language="java" import="java.util.*,java.text.DecimalFormat" pageEncoding="utf-8"%>
<%@ page import="ldc.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="java.io.*" %>
<% 
DBConnect agent = new DBConnect();



	String docID = request.getParameter("doctorid"), statusname = request.getParameter("statusname"), workdayid = request.getParameter("workdayid");
	String SQL = "UPDATE doctor_workday  SET ";
				if(statusname.equals("Waiting")){
					SQL +="checkin_status = '2' "
						+",	checkin_datetime = now() ";
				}else{
					SQL +="checkin_status = '3' "
							+",	checkout_datetime = now() ";
				}
				SQL += "WHERE doctor_id = '"+docID+"' AND workday_id = '"+workdayid+"' AND DATE_FORMAT(start_datetime,'%Y-%m-%d') = CURDATE() ; ";
				if(statusname.equals("Waiting")){
				SQL += "INSERT INTO doctor_workday_line (branch_id,doctor_id,income_type,startdate_time,finish_datetime,work_hour,late_min,early_min,checkin_time) "
						+"SELECT branch_standard_rel_doctor.branch_id,branch_standard_rel_doctor.doctor_id, "
						+"branch_standard_rel_doctor.income_type,branch_standard_rel_doctor.startdate_time, "
						+"branch_standard_rel_doctor.finish_datetime,branch_standard_rel_doctor.work_hour, "
						+"branch_standard_rel_doctor.late_min,branch_standard_rel_doctor.early_min,NOW() "
						+"FROM branch_standard_rel_doctor "
						+"WHERE doctor_id = '"+docID+"' AND branch_id = '"+Auth.user().getBranchID()+"' ";
				}else{
					SQL +="UPDATE doctor_workday_line SET "
							+"checkout_time = NOW() "
							+"WHERE doctor_id = '"+docID+"' AND branch_id = '"+Auth.user().getBranchID()+"' AND checkout_time is null ";
				}
						
	agent.connectMySQL();
	JSONObject jsonOBJ = new JSONObject();
	jsonOBJ.put("status", agent.exeUpdate(SQL) > 0 ? "success" : "false");
	out.print(jsonOBJ);
	agent.disconnectMySQL();

%>