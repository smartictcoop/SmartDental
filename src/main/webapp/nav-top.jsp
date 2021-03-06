<%@page import="com.smict.person.model.TelephoneModel"%>
<%@ page language="java" import="java.util.*,java.text.DecimalFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.smict.person.model.PatientModel" %>
<%@ page import="com.smict.person.data.PatientData" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link href="css/uikit.gradient.css" rel="stylesheet"/>
<link href="css/bootstrap-datepicker3.css" rel="stylesheet">
<link href="css/select2.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/rules.css" rel="stylesheet">
<link href="css/clusterize.css" rel="stylesheet">
<!-- Full Calendar -->
<link href='css/fullcalendar.css' rel='stylesheet' />
<!-- Full Calendar -->

<link href="css/components/datepicker.gradient.css" rel="stylesheet">   
<link href="css/jquery.dataTables.min.css" rel="stylesheet">
<link rel="icon" href="img/favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="css/sweetalert2.min.css">
<link rel="stylesheet" type="text/css" href="css/components/form-advanced.gradient.min.css">
<link rel="stylesheet" type="text/css" href="css/components/form-select.gradient.css">
<link rel="stylesheet" type="text/css" href="css/components/sortable.gradient.css">
<link rel="stylesheet" type="text/css" href="css/components/autocomplete.gradient.css"> 
<link href="css/components/accordion.gradient.min.css" rel="stylesheet">
<link href="css/components/nestable.gradient.min.css" rel="stylesheet">
<link href="css/jquery-clockpicker.css" rel="stylesheet">  


<nav class="uk-panel uk-panel-box " style="padding:5px;"> 
	<div class="uk-grid">
		<div id="menu-top-left" class="uk-text-left uk-width-2-6"> 
			<div id="add_patient" class="uk-modal ">
			    <div class="uk-modal-dialog uk-modal-dialog-large uk-form " >
			        <a class="uk-modal-close uk-close"></a>
			         <div class="uk-modal-header"><i class="uk-icon-user-md"></i> คนไข้</div>
			         <form action="topPatient" method="post">
			         	<div class="uk-width-1-1 uk-overflow-container">
			         		
			         		<input type="hidden" id="hn" name="servicePatModel.hn">
			         		<input type="hidden" id="hnFormat" name="servicePatModel.hnFormat">
			         		<input type="hidden" id="addr_id" name="servicePatModel.addr_id">
			         		<input type="hidden" id="fam_id" name="servicePatModel.fam_id">
			         		<input type="hidden" id="be_allergic_id" name="servicePatModel.be_allergic_id">
			         		<input type="hidden" id="patneed_id" name="servicePatModel.patneed_id">
			         		<input type="hidden" id="pat_congenital_disease_id" name="servicePatModel.pat_congenital_disease_id">
			         		<input type="hidden" id="tel_id" name="servicePatModel.tel_id">
			         		
					</div>
			         <div class="uk-modal-footer uk-text-right">
			         	<button class="uk-button uk-button-success" type="submit">ตกลง</button>
			         	<button class="uk-button uk-button-danger uk-modal-close">ยกเลิก</button> 
			         </div>
			         </form>
			    </div>
			</div>
			<div class="uk-button-dropdown" data-uk-dropdown="" aria-haspopup="true" aria-expanded="false">
                <a href="selectPatient" class="uk-button uk-button-success" title="เลือกคนไข้" >
                	<i class="uk-icon-user"></i>
            	</a>
            </div>
			<div class="uk-button-dropdown" data-uk-dropdown="" aria-haspopup="true" aria-expanded="false">
                <a href="beginAddPatient" class="uk-button uk-button-success" title="เพิ่มคนไข้" >
            		<i class="uk-icon-user-plus"></i>
        		</a>
            </div>
            
			<a href="#add_app" class="uk-button uk-button-primary" data-uk-modal>
				<i class="uk-icon-calendar-plus-o"></i> เพิ่มนัดหมาย
			</a>
			<div id="add_app" class="uk-modal ">
			    <div class="uk-modal-dialog uk-form " >
			        <a class="uk-modal-close uk-close"></a>
			         <div class="uk-modal-header"><i class="uk-icon-group"></i> เพิ่มนัดหมาย</div>
			         	<div class="uk-form-icon">
    						<i class="uk-icon-user">
    						</i>
			         	<input class="uk-width-1-1" type="text" id="event" name="event" placeholder="ชื่อลูกค้า"> 
			         	</div>
			         	-
			         	<div class="uk-form-icon">
							<i class="uk-icon-phone">
    						</i>
							<input type="text" pattern="[0-9]"  title="กกรอกเฉพาะตัวเลขเท่านั้น" id="room" name="room" placeholder="เบอร์โทรศัพท์"> 
						</div>
						-
						<input type="radio" name="r11" checked> N <input type="radio" name="r11" > NR <input type="radio" name="r11" > R
						<hr/>    
						<div class="uk-grid uk-grid-small">
							<div class="uk-width-1-2">
			         		<select class="uk-width-1-1 pt" size="5">
			         			<option selected value="0">เลือกแพทย์</option>
					            <option value="1">ทพ.วิจิต</option>
					            <option value="2">ทพ.สดใส</option>
					        </select> 
					        </div>
					        <div class="uk-width-1-2">
					        <select class="uk-width-1-1 month" size="5">
			         			<option value="0">เลือกเดือน</option> 
					        </select>  
					        </div>
					    </div> 
						<hr/>
						<div class="uk-grid uk-grid-small">
							<div class="uk-width-1-2">
							<select class="uk-width-1-1 day" size="5"> 
								<option value="0">เลือกวัน</option>
							</select>
							</div>
							<div class="uk-width-1-2">
							<select class="uk-width-1-1 time" size="5"> 
								<option value="0">เลือกเวลา</option>
							</select>
							</div>
						</div>
						<hr/>

			    </div>
			</div> 
			<a href="sendLabBegin" class="uk-button uk-button-primary">
				 งาน lab
			</a>
		</div>
		<div id="menu-top-center" class="uk-text-center uk-width-2-6 bor-rightAndleft " >
		<div class="uk-grid uk-grid-collapse">
			<div class="uk-width-3-5">
			<strong>
				<s:property value="servicePatModel.pre_name_th"/> 
				<s:property value="servicePatModel.firstname_th"/> 
				<s:property value="servicePatModel.lastname_th"/>
			</strong>
			<br>
			<s:if test="servicePatModel.hnBranch == null">
				<em>N/A</em>
			</s:if>
			<s:else>
				<em><s:property value="servicePatModel.hnBranch"/></em>
			</s:else>
			</div>
			<s:if test="servicePatModel.hnBranch != null">
			<div  class=" uk-button-dropdown " data-uk-dropdown >
				<button class="uk-button">
					 <i class=" uk-icon-warning uk-icon-small"></i> 
					 <span class="uk-badge uk-badge-notification uk-badge-danger " id="countallcon">0</span>
				</button>			
				<div class="uk-dropdown  uk-dropdown-width-2 ">
					    <div class="uk-grid uk-dropdown-grid">
					        <div class="uk-width-1-2 uk-text-left">
					            <h4 class="uk-text-primary ">โรคประจำตัว</h4>					            					   
					         		<ul>
						         		<s:if test="%{servicePatModel.congenList.isEmpty()}">
											<li>ไม่มีโรคประจำตัว</li>
										</s:if>	
										<s:else>		         	
											<s:iterator value="servicePatModel.congenList" status="congen"> 
												<li class="uk-text-danger textcon"><s:property value="congenital_name_th"/></li>
												<s:if test="#congen.last== true">
									           	 	<li class="hidden" id="congenC"><s:text name="%{#congen.count}" /></li>
								           	 	</s:if>	
											</s:iterator>											
										</s:else>
										<li class="hidden" id="congenC">0</li>
									</ul>
					        </div>
					
					        <div class="uk-width-1-2 uk-text-left">
					            <h4 class="uk-text-primary ">ประวัติแพ้ยา</h4>
					         		<ul>
						         		<s:if test="%{servicePatModel.beallergic.isEmpty()}">
											<li>ไม่มีประวัติแพ้ยา</li>
										</s:if>	
										<s:else>		         	
											<s:iterator value="servicePatModel.beallergic" status="bealler"> 
												<li class="uk-text-danger textallergic"><s:property value="beallergic_name_th"/></li>
												<s:if test="#bealler.last== true">
									           	 	<li class="hidden" id="beallerC"><s:text name="%{#bealler.count}" /></li>
								           	 	</s:if>	
											</s:iterator>
										</s:else>
										<li class="hidden" id="beallerC">0</li>
									</ul>	
					        </div>
					    </div>
				</div>
			</div>
						<div class="uk-button-dropdown " data-uk-dropdown>
				 <!-- This is the button toggling the dropdown -->
				 <button class="uk-button">
					 <i class=" uk-icon-file-o uk-icon-small"></i> 
					 <span class="uk-badge uk-badge-notification uk-badge-danger" id="countall">0</span>
				 </button>				
				 <!-- This is the dropdown -->
				 <div class="uk-dropdown  uk-dropdown-width-2 ">
					    <div class="uk-grid uk-dropdown-grid">
					        <div class="uk-width-1-2 uk-text-left">
					            <h4 class="uk-text-primary ">เอกสารที่คนไข้ต้องการ</h4>					            					   
					         		<ul>
						         		<s:if test="%{servicePatModel.documentneed.isEmpty()}">
												<li>ไม่มีเอกสารที่คนไข้ต้องการ</li>
										</s:if>	
										<s:else>
							        	<s:iterator value="servicePatModel.documentneed" status="docneed">
							           	 	<li><a><s:property value="doc_name"/></a></li>
								           	 	<s:if test="#docneed.last== true">
								           	 	<li class="hidden" id="docneed"><s:text name="%{#docneed.count}" /></li>
							           	 	</s:if>		
							            </s:iterator>
							             </s:else>								            	
							            	<li class="hidden" id="docneed">0</li>

									</ul>
					        </div>
					
					        <div class="uk-width-1-2 uk-text-left">
					            <h4 class="uk-text-primary ">สิ่งที่คนไข้ต้องการ</h4>
					         		<ul>
									           <s:iterator value="servicePatModel.patneed_message" status="patneed">
									           	<s:if test="servicePatModel.patneed_message[#patneed.index] != null">
									            	 <li><a><s:property  value='servicePatModel.patneed_message[#patneed.index]' /></a></li>
									            	 <s:if test="#patneed.last== true">
									            	 <li class="hidden" id="patneed"><s:text name="%{#patneed.count}" /></li>
									            	 </s:if>
									            </s:if>
									            </s:iterator>
											     <s:else>										          
													<li>ไม่มีสิ่งที่คนไข้ต้องการ</li>
												</s:else>		
									            	<li class="hidden" id="patneed">0</li>	 			            				            	 
									            
									</ul>	
					        </div>
					    </div>
				</div>
			 
			</div>	
			</s:if>
		</div>
		</div>
		<div id="menu-top-right" class="uk-text-right uk-width-2-6">
	
			<div class="uk-button-dropdown" data-uk-dropdown="{mode:'click'}">
				 <!-- This is the button toggling the dropdown -->
				 <button class="uk-button" id="treatbtn">
					 <i class=" uk-icon-stethoscope uk-icon-small"></i> 
					 <span class="uk-badge uk-badge-notification uk-badge-danger" id="counttreatment_patient">0</span>
				 </button>				
				 <!-- This is the dropdown -->
			    <div class="uk-dropdown uk-dropdown-small list-stack-job " >			    	
			         <ul class="uk-nav uk-nav-dropdown" id="treatment-patient"> 
			        	<li class="uk-nav-header">การรักษายังไม่เสร็จสิ้น</li>
			            <li class="uk-nav-divider"></li>	            	
			        </ul>
			    </div>
			</div>
			<div class="uk-button-dropdown"  data-uk-dropdown="{pos:'bottom-right',mode:'click'}">
				 <!-- This is the button toggling the dropdown -->
				 <button class="uk-button" id="appointmentMode">
					 <i class=" uk-icon-calendar-check-o uk-icon-small"></i> 
					 <span class="uk-badge uk-badge-notification uk-badge-danger" id="appointment_count">0</span>
				 </button>				
				 <!-- This is the dropdown -->
			    <div class="uk-dropdown uk-dropdown-small clusterize-scroll" id="appointmentdiv" style="max-height:50vh;">
			        <ul class="uk-nav uk-nav-dropdown clusterize-content" id="appointment">
			        	<!-- <li class="uk-nav-header">การนัดหมายที่ใกล้จะถึง</li>            	
			            <li class="uk-nav-divider"></li> -->
			            <li class="clusterize-no-data">Loading data…</li>
			        </ul>
			    </div>
			</div>
			<div class="uk-button-dropdown" data-uk-dropdown="{mode:'click'}">
				 <!-- This is the button toggling the dropdown -->
				 <button class="uk-button">
					 <i class=" uk-icon-phone uk-icon-small"></i> 
					 <span class="uk-badge uk-badge-notification uk-badge-danger">3</span>
				 </button>				
				 <!-- This is the dropdown -->
			    <div class="uk-dropdown uk-dropdown-small">
			        <ul class="uk-nav uk-nav-dropdown ">
			        	<li class="uk-nav-header">แจ้งเตือนการโทร</li>
			        	<li><a href="homecall.jsp"  class="uk-text-left">HOME CALL 
			        		<span class="uk-badge uk-badge-notification uk-badge-danger uk-text-right noti">2</span>
			        	</a></li>
			            <li><a href="recall-all.jsp" class="uk-text-left">RE CALL 
			            	<span class="uk-badge uk-badge-notification uk-badge-danger uk-text-right noti2">1</span>
			            </a></li>
			            
			        </ul>
			    </div>
			</div>
		</div>
	</div>
</nav> 

<script src="js/jquery-2.2.4.min.js"></script>
<script src="js/bootstrap-datepicker-th.js"></script>
<script src="js/uikit.min.js"></script>
<script src="js/components/notify.js"></script>
<script src="js/components/datepicker.min.js"></script>
<script src="js/components/accordion.min.js"></script>
<script src="js/components/nestable.min.js"></script>
<script src="js/components/form-select.min.js"></script>
<script src="js/components/autocomplete.min.js"></script> 
<script src="js/core/tab.min.js"></script> 
<script src="js/clusterize.min.js"></script> 
<!-- Full Calendar -->
<script src="js/moment.min.js"></script>
<script src="js/fullcalendar.min.js"></script>
<script src="js/th.js"></script>  
<!-- Full Calendar -->

<script src="js/sweetalert2.min.js"></script>  
<script src="js/jquery-clockpicker.js"></script>
<script src="js/jquery.dataTables.min.js"></script> 
<script src="js/select2.min.js"></script>

<script>
$(document).ready(function() {
		var appArr = [] 
		/*TABLE ADD BRANCH #addBranch*/
		$("#tbBranch").DataTable();
		$.ajax({
	        type: "post",
	        url: "ajax/ajax-treatment-patient-waiting-count.jsp", //this is my servlet 
	        data: {method_type:"get"},
	        async:true, 
	        success: function(result){
	        	var obj = jQuery.parseJSON(result);
	        	var countnumber = 1;
	        	for(var i = 0 ;  i < obj.length;i++){
	        		$("#counttreatment_patient").text(obj[i].patcount);
	        	}
	        	
		    } 
	     });
		
		$('#treatbtn').click(function () {
			var treatment_patientText = '<li class="uk-nav-header">การรักษายังไม่เสร็จสิ้น</li>';
			
			$.ajax({
		        type: "post",
		        url: "ajax/ajax-treatment-patient-waiting.jsp", //this is my servlet 
		        data: {method_type:"get"},
		        async:true, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result);
		        	var countnumber = 1;
		        	for(var i = 0 ;  i < obj.length;i++){
		        		
		        		treatment_patientText += '<li class="uk-text-left"><a href="getPatientShowAfterSaveTreatment-'+obj[i].treatmentPatientID+'">'+countnumber+'. '+obj[i].patient_name+'<small class="uk-text-center"><br>HN '+obj[i].patient_hn+'</small></a></li><li class="uk-nav-divider"></li>'
		        		countnumber++;
		        	}
		        	$("#treatment-patient").html(treatment_patientText); 
		        	$("#counttreatment_patient").text(i);
			    } 
		     });
			
		});

		/* appoinment */
		$.ajax({
		        type: "post",
		        url: "ajax/ajax-appointment-count.jsp", //this is my servlet 
		        data: {method_type:"get"},
		        async:true, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result);
		        	for(var i = 0 ;  i < obj.length;i++){		        		
		        		$("#appointment_count").text(obj[i].countall);
		        	}
		        	
			    } 
		     });
		var clusterize = new Clusterize({
  		  rows: appArr,
  		  rows_in_block:30,
  		  scrollId: 'appointmentdiv',
  		  contentId: 'appointment'
  		});
		$('#appointmentMode').click(function () {
			appArr = [] 
			appArr.push('<li class="uk-nav-header">การนัดหมายที่ใกล้จะถึง</li>')
			$.ajax({
		        type: "post",
		        url: "ajax/ajax-appointment.jsp", //this is my servlet 
		        data: {method_type:"get"},
		        async:true, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result);
		        	var countapp = 1;
		        	for(var i = 0 ;  i < obj.length;i++){
		        		if(obj[i].isview == 0){
		        			appArr.push('<li class="uk-text-left"><a class="isview" href="updateIsviewStatus-'+obj[i].appID+'">'+countapp+'. '+obj[i].pat_name+'<br><small>วันที่ '+obj[i].appDate+'</small></a></li><li class="uk-nav-divider"></li>')
		        		}else{
		        			appArr.push('<li class="uk-text-left "><a class="uk-text-muted" href="getAppointmentpatient-'+obj[i].appID+'">'+countapp+'. '+obj[i].pat_name+'<br><small>วันที่ '+obj[i].appDate+'</small></a></li><li class="uk-nav-divider"></li>')
		        		}
		        		 
		        		countapp++;
		        	}
		        	/* $("#appointment").html(appText);  */
		        	clusterize.update(appArr)
			    } 
		     }); 
		});

		
	   	// patient alert
	   	/* patienShow();
		var timerId = setInterval(function() {  
			patienShow();
			//clearInterval(timerId);
		}, 5000); */
		function patienShow(){
			// show patient 
			var textvalue = '<li class="uk-nav-header">รายการงานที่ทำงานค้างอยู่</li>';
			$.ajax({
		        type: "post",
		        url: "ajax/ajax-treatment-working.jsp", //this is my servlet 
		        data: {method_type:"get"},
		        async:true, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result);
		        	for(var i = 0 ;  i < obj.length;i++){
		        		textvalue += '<li><a href="treatmentAlert?hn='+obj[i].hn+'">'+obj[i].hnname+'<small> '+obj[i].title_status+'<br>'+obj[i].minute+'</small></a></li><li class="uk-nav-divider"></li>'
		        		 
		        	}
		        	$("#menu").html(textvalue); 
			    } 
		     }); 
			
			// count patient
			$.ajax({
		        type: "post",
		        url: "ajax/ajax-treatment-working-count.jsp", //this is my servlet 
		        data: {method_type:"get"},
		        async:true, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result); 
		        	for(var i = 0 ;  i < obj.length;i++){
		        		$("#countpatient").html(obj[i].counthn);
		        	}
			    } 
		    });
		} 
		  
		$("#conallis").ready(function(){
			var conpatneed = parseInt($("#patneed").text());
			var condocneed = parseInt($("#docneed").text());
			var conall =condocneed+conpatneed;			
			if(conall > 0 ){
			$("#countall").text(conall);
			}
			
		});
		$("#countallconall").ready(function(){
			var beallerC = parseInt($("#beallerC").text());
			var congenC = parseInt($("#congenC").text());
			var conallC =congenC+beallerC;			
			if(conallC > 0 ){
			$("#countallcon").text(conallC);
			}
			
		});
		$("#tablechoose_patient").DataTable();
		// นัดหมาย
		$(".pt").change(function(){
			var pt = $(".pt").val(); 
			if(pt==0){
				$('.month').children('option:not(:first)').remove(); 
				$('.day').children('option:not(:first)').remove();
				$('.time').children('option:not(:first)').remove(); 
			}else{
				$('.month').children('option:not(:first)').remove();
				$('.day').children('option:not(:first)').remove();
				$('.time').children('option:not(:first)').remove();
				if(pt==1){
					$(".month").append( 
							"<option value='1'>เดือนนี้</option>"+
				            "<option value='2'>เดือนหน้า</option>");
				}else{
					$(".month").append( 
							"<option value='1'>เดือนนี้</option>");
				}
				
		        
			}
	    });	
		$(".month").change(function(){
			var month = $(".month").val(); 
			if(month==0){
				$('.day').children('option:not(:first)').remove(); 
				$('.time').children('option:not(:first)').remove();
			}else{
				$('.day').children('option:not(:first)').remove();
				$('.time').children('option:not(:first)').remove();
				var month = $(".month").val(); 
				if(month=='1'){
					 $(".day").append(
								"<option>วันจันทร์ที่ 1</option>"+
								"<option>วันพุธที่ 3</option>"+
								"<option>วันพฤหัสบดีที่ 4</option>"+
								"<option>วันจันทร์ที่ 8</option>"+
								"<option>วันพุธที่ 10</option>"+
								"<option>วันพฤหัสบดีที่ 11</option>"+
								"<option>วันจันทร์ที่ 15</option>"+
								"<option>วันพุธที่ 17</option>"+
								"<option>วันพฤหัสบดีที่ 18</option>"+
								"<option>วันจันทร์ที่ 22</option>"+
								"<option>วันพุธที่ 24</option>"+
								"<option>วันพฤหัสบดีที่ 25</option>");
				}else{
					 $(".day").append(
								"<option>วันอังคารที่ 2</option>"+
								"<option>วันศุกร์ที่ 3</option>"+
								"<option>วันอังคารที่ 9</option>"+
								"<option>วันศุกร์ที่ 10</option>"+
								"<option>วันอังคารที่ 16</option>"+
								"<option>วันศุกร์ที่ 17</option>"+
								"<option>วันอังคารที่ 23</option>"+
								"<option>วันศุกร์ที่ 24</option>"+
								"<option>วันอังคารที่ 30</option>");
				}
		       
			}
	    });
		$(".day").change(function(){
			var month = $(".day").val(); 
			if(month==0){
				$('.time').children('option:not(:first)').remove(); 
			}else{
				$('.time').children('option:not(:first)').remove();
		        $(".time").append(
						"<option>8:00 น</option>"+
						"<option>9:00 น</option>"+
						"<option>10:00 น</option>"+
						"<option>11:00 น</option>"+
						"<option>12:00 น</option>"+
						"<option>13:00 น</option>"+
						"<option>14:00 น</option>"+
						"<option>15:00 น</option>"+
						"<option>16:00 น</option>"+
						"<option>17:00 น</option>"+
						"<option>18:00 น</option>"+
						"<option>19:00 น</option>"+
						"<option>20:00 น</option>"+
						"<option>21:00 น</option>");
			}
	    });
		 
		// นัดหมาย
	
		$("#birthdate").click(function(){
			if($("#birthdate").text() == "Thai year"){
				$("#birthdate").text("English year");
			}else{
				$("#birthdate").text("Thai year");	
			}
		});
		$("#fpatient-quick").submit(function(event){
			if($("#idtel").val().length === 0 && $("#idline").val().length === 0 && $("#email").val().length === 0){
				swal(
						'ผิดพลาด!',
						'กรุณาระบุ กรอกข้อมูล เบอร์โทรศัพท์ IDLINE หรือ Email อย่างใดอย่างหนึ่ง',
						'error'
					)
				event.preventDefault();
			}
		});
		$("#savecalendar").click(function(){
			 
			var title = $("#event").val();
			var date = $("#date").val();
			var time1 = $("#time1").val();
			var time2 = $("#time2").val();
			var room = $("#room").val();
			
			if(title == ''){
				swal(
					'ผิดพลาด!',
					'กรุณาระบุ หัวข้อ!',
					'error'
				)
			}else if(room == ''){
				swal(
					'ผิดพลาด!',
					'กรุณาระบุ ห้อง!',
					'error'
				)
			}else if(date == ''){
				swal(
					'ผิดพลาด!',
					'กรุณาระบุ วันที่!',
					'error'
				)
			}else if(time1 == ''){
				swal(
					'ผิดพลาด!',
					'กรุณาระบุ เวลาเริ่ม!',
					'error'
				)
			}else if(time2 == ''){
				swal(
					'ผิดพลาด!',
					'กรุณาระบุ เวลาจบ!',
					'error'
				)
			}else{
				day = date.substring(0, 2);
				month = date.substring(3, 5);
				year = date.substring(6, 10);
				time1 = year+"-"+month+"-"+day+"-"+time1+":00";
				time2 = year+"-"+month+"-"+day+"-"+time2+":59"; 
				
				var letters = '0123456789ABCDEF'.split('');
			    var color = '#';
			    for (var i = 0; i < 6; i++ ) {
			        color += letters[Math.floor(Math.random() * 16)];
			    }
				
				swal({
	    			  title: 'คุณต้องการเพิ่ม event หรือไม่?',
	    			  text: "หากต้องการเพิ่มให้กดตกลง!",
	    			  type: 'warning',
	    			  showCancelButton: true,
	    			  confirmButtonColor: '#3085d6',
	    			  cancelButtonColor: '#d33',
	    			  confirmButtonText: 'คกลง!',
	    			  cancelButtonText: 'ยกเลิก',
	    			}).then(function(isConfirm) {
	    			  if (isConfirm) {
				
						$.ajax({    
							 
					        type: "post",
					        url: "ajax/ajax-save-event.jsp", //this is my servlet 
					        data: {event:title,time1:time1,time2:time2,color:color,hong:room},
					        async:false, 
					        success: function(result){
					        	  
					        	obj = JSON.parse(result); 
					        	 
						     } 
					     });
						//$('#calendar').fullCalendar( 'removeEvents');
						$('#calendar').fullCalendar( 'addEventSource', obj);
						
						swal(
			    			      'เพิ่ม!',
			    			      'ข้อมูล event ได้ถูกเพิ่มแล้ว.',
			    			      'success'
			    		);
						
						$("#event").val("");
						$("#date").val("");
						$("#time1").val("");
						$("#time2").val("");
						$("#room").val("");
						
		    		 }else{
		    				  swal(
				    			'ข้อมูล event ไม่ถูกเพิ่ม!',
				    			'ข้อมูล event ยังไม่ถูกเพิ่ม.',
				    			'error'
				    		);  
		    			  }
		    		})
			}
		});
});		

function tab1() { 
	
	 $("#idline").val(""); 
	 $("#email").val("");
};
function tab2() { 
	 
	 $("#idno").val(""); 
	 $("#email").val("");
};
function tab3() { 
	 
	 $("#idno").val(""); 
	 $("#idline").val("");
};

$('.clockpicker').clockpicker()
.find('input').change(function(){
	console.log(this.value);
});
var input = $('#single-input').clockpicker({
placement: 'bottom',
align: 'left',
autoclose: true,
'default': 'now'
});

$('.clockpicker-with-callbacks').clockpicker({
	donetext: 'Done',
	init: function() { 
		console.log("colorpicker initiated");
	},
	beforeShow: function() {
		console.log("before show");
	},
	afterShow: function() {
		console.log("after show");
	},
	beforeHide: function() {
		console.log("before hide");
	},
	afterHide: function() {
		console.log("after hide");
	},
	beforeHourSelect: function() {
		console.log("before hour selected");
	},
	afterHourSelect: function() {
		console.log("after hour selected");
	},
	beforeDone: function() {
		console.log("before done");
	},
	afterDone: function() {
		console.log("after done");
	}
})
.find('input').change(function(){
	console.log(this.value);
});

//Manually toggle to the minutes view
$('#check-minutes').click(function(e){
// Have to stop propagation here
e.stopPropagation();
input.clockpicker('show')
		.clockpicker('toggleView', 'minutes');
});
if (/mobile/i.test(navigator.userAgent)) {
$('input').prop('readOnly', true);
}
</script>

