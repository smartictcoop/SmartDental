<%@page import="com.smict.person.model.TelephoneModel"%>
<%@ page language="java" import="java.util.*,java.text.DecimalFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.smict.person.model.PatientModel" %>
<%@ page import="com.smict.person.data.PatientData" %>
<link href="css/uikit.gradient.css"rel="stylesheet"/>
<link href="css/bootstrap-datepicker3.css" rel="stylesheet">
<link href="css/select2.min.css" rel="stylesheet">
<link href="css/style.css"rel="stylesheet">
<link href='css/fullcalendar.css' rel='stylesheet' /> 
<link href="css/components/datepicker.gradient.css"rel="stylesheet">   
<link href="css/jquery.dataTables.min.css"rel="stylesheet">

<link rel="stylesheet" type="text/css" href="css/sweetalert2.min.css">
<link rel="stylesheet" type="text/css" href="css/components/form-advanced.gradient.min.css">
<link rel="stylesheet" type="text/css" href="css/components/form-select.gradient.css">
<link rel="stylesheet" type="text/css" href="css/components/sortable.gradient.css">
<link rel="stylesheet" type="text/css" href="css/components/autocomplete.gradient.css"> 
<link href="css/components/accordion.gradient.min.css"rel="stylesheet">
<link href="css/components/nestable.gradient.min.css"rel="stylesheet">
<link href="css/jquery-clockpicker.css"rel="stylesheet">  

<nav class="uk-panel uk-panel-box " style="padding:5px;"> 
	<div class="uk-grid">
		<div id="menu-top-left" class="uk-text-left uk-width-1-2"> 
			<a href="getemployeelist" class="uk-button uk-button-success" ><i class="uk-icon-user-md"></i> เลือกพนักงาน</a>
			
			<div class="uk-button-dropdown" data-uk-dropdown="" aria-haspopup="true" aria-expanded="false">
                <a href="addemployee" class="uk-button uk-button-primary" ><i class=" uk-icon-plus"></i> เพิ่มข้อมูลพนักงาน</a>
            </div>
          
			<!--  <a class="uk-button uk-button-primary" href="doctor-time.jsp">วันที่ทำงาน</a>
            <a class="uk-button uk-button-primary" href="doctor-time-edit.jsp">เวลาเข้างาน</a>
            <a class="uk-button uk-button-primary" href="doctor-standard.jsp">ค่า Stand by</a> 
            <a class="uk-button uk-button-primary" href="brandBegin">Brand</a> -->
		</div>
		
	</div>
</nav> 

<script src="js/jquery-2.2.4.min.js"></script>
<script src="js/bootstrap-datepicker-th.js"></script>
<script src="js/uikit.min.js"></script>
<script src="js/components/datepicker.min.js"></script>
<script src="js/components/accordion.min.js"></script>
<script src="js/components/nestable.min.js"></script>
<script src="js/components/form-select.min.js"></script>
<script src="js/components/autocomplete.min.js"></script> 
<script src="js/moment.min.js"></script>
<script src="js/fullcalendar.min.js"></script>
<script src="js/th.js"></script>  
<script src="js/sweetalert2.min.js"></script>  
<script src="js/jquery-clockpicker.js"></script>
<script src="js/jquery.dataTables.min.js"></script> 
<script src="js/select2.min.js"></script>


