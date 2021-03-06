<%@ page language="java" import="java.util.*,java.text.DecimalFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.smict.person.model.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% 

	String addr_provincename = (String) request.getAttribute("addr_provincename");
	String addr_aumphurname = (String) request.getAttribute("addr_aumphurname");
	String addr_districtname = (String) request.getAttribute("addr_districtname");
	
%>
<!DOCTYPE html>

<html>
	<head>
		<title>Smart Dental:รายละเอียดข้อมูลสาขา</title>
		<link rel="icon" href="img/favicon.ico" type="image/x-icon"/>
	</head>
	<body>
		<div class="uk-grid uk-grid-collapse">
			<div class="uk-width-1-10">
				<%@include file="nav-right.jsp" %>
			</div>
			<div class="uk-width-9-10">
				<%@include file="nav-top.jsp" %>
				
				<form id="branch" action="branchEdit" method="post" >
					<div class="uk-width-1-1 uk-grid uk-grid-collapse padding5 uk-form" >
							<!-- Start Branch Detail-->
							<div class="uk-width-4-10 padding5 border-gray">
								<% if(request.getAttribute("alertMessage") != null) {%>
									 <h3 class="uk-text-success uk-text-center"><%=request.getAttribute("alertMessage").toString()%></h3> 
								<% }else{ %>
								<% } %>
								<div class="uk-grid uk-grid-collapse">
							 	<p class="uk-text-muted uk-width-1-1">ข้อมูลสาขา </p>
							 	<div class="uk-width-1-2 uk-text-right">แบรนด์บรษัท : </div>
								<div class="uk-width-1-2">
									<s:select list="brandMap" name="branchModel.brand_id" id="brandList" />
									<s:hidden name="hdbrand_id" value="%{branchModel.brand_id}" />
								</div>
								<div class="uk-width-1-2 uk-text-right">รหัสสาขา : </div>
								<div class="uk-width-1-2">
									<s:textfield name="branchModel.branch_id" pattern="[A-z]{1,4}" placeholder="กรอกข้อมูล เป็นภาษาอังกฤษเท่านั้น"   maxlength="4" id="branch_id" required="required" />
									<s:hidden name="hdbranch_id" value="branchModel.branch_id" maxlength="4" id="hdbranch_id" />
								</div>
								<div class="uk-width-1-2 uk-text-right">ชื่อสาขา : </div>
								<div class="uk-width-1-2">
									<s:textfield id="branch_name" name="branchModel.branch_name" pattern="[A-zก-๙].{1,}" placeholder="กรอกข้อมูล เป็นภาษาอังกฤษ-ไทยเท่านั้น" required="required" />
								</div>
								<div class="uk-width-1-2 uk-text-right">แพทย์ผู้ดำเนินการ : </div>
								<div class="uk-width-1-2">
									<s:select list="doctorMap" name="branchModel.doctor_id" id="doctorList" />
								</div> 
								<div class="uk-width-1-2 uk-text-right">ค่าตอบแทน : </div>
								<div class="uk-width-1-2">
									<%-- <input type="text" id="price_doctor" pattern="[0-9].{2,}" title="กรอกข้อมูล เป็นตัวเลขและต้องมากกว่า 3 หลักเท่านั้น" maxlength="10" name="branchModel.price_doctor" value="<%=price_doctor%>" required="required"> --%>
									<s:textfield name="branchModel.price_doctor" />
								</div>
								<div class="uk-width-1-1 padding5 border-gray">
							 		<p class="uk-text-muted uk-width-1-1">ข้อมูลที่อยู่</p>
							 		<div class="uk-grid uk-grid-collapse"> 
							 			
							 		<div class="uk-width-1-2 uk-text-right">เลขที่ : </div>
									<div class="uk-width-1-2">
										<s:textfield id="addr_no" name="branchModel.addr_no" pattern="[0-9].{0,}" placeholder="กรอกข้อมูล เป็นตัวเลขเท่านั้น" required="required" />
									</div>
									<div class="uk-width-1-2 uk-text-right">หมู่ : </div>
									<div class="uk-width-1-2">
										<s:textfield id="addr_bloc" name="branchModel.addr_bloc" />
									</div>
									<div class="uk-width-1-2 uk-text-right">หมู่บ้าน : </div>
									<div class="uk-width-1-2">
										<s:textfield id="addr_village" name="branchModel.addr_village" pattern="[A-zก-๙].{1,}" placeholder="กรอกข้อมูล เป็นภาษา ไทย-อังกฤษเท่านั้น" required="required" />
									</div>
									<div class="uk-width-1-2 uk-text-right">ซอย : </div>
									<div class="uk-width-1-2">
										<s:textfield  id="addr_alley" name="branchModel.addr_alley" pattern="[A-zก-๙0-9].{1,}" placeholder="กรอกข้อมูล เป็นตัวเลขเท่านั้น" required="required" />
									</div>
									<div class="uk-width-1-2 uk-text-right">ถนน : </div>
									<div class="uk-width-1-2">
										<s:textfield id="addr_road" name="branchModel.addr_road" pattern="[A-zก-๙].{1,}" placeholder="กรอกข้อมูล เป็นภาษา ไทย-อังกฤษเท่านั้น" required="required" />
									</div>
									 
									<div class="uk-width-1-2 uk-text-right ">จังหวัด : </div>
									<div class="uk-width-1-2 sele2">
										<select id="addr_provinceid" name="branchModel.addr_provinceid"  >
											<option value="">เลือกจังหวัด </option> 
										</select> 
									</div>
									<div class="uk-width-1-2 uk-text-right ">อำเภอ : </div>
									<div class="uk-width-1-2 sele2">
										<select id="addr_aumphurid" name="branchModel.addr_aumphurid" >
											<option value="">เลือกอำเภอ</option>
										</select>
									</div>
									<div class="uk-width-1-2 uk-text-right ">ตำบล : </div>
									<div class="uk-width-1-2 sele2">
										<select id="addr_districtid" name="branchModel.addr_districtid" >
											<option value="">เลือกตำบล</option>
										</select>
									</div>
									<div class="uk-width-1-2 uk-text-right">รหัสไปรษณีย์ : </div>
									<div class="uk-width-1-2">
										<s:textfield  id="addr_zipcode" pattern="[0-9].{1,5}" maxlength="5" placeholder="กรอกข้อมูล เป็นตัวเลขเท่านั้น" name="branchModel.addr_zipcode" required="required" />
									</div>
								</div>
								</div>
								<div class="uk-width-1-1 padding5 border-gray">
								
							 		<p class="uk-text-muted uk-width-1-1">ข้อมูลที่เบอร์โทรศัพท์</p>
							 		<div class="uk-grid uk-grid-collapse"> 
							 		
								 		<div class="uk-width-1-2 uk-text-right">เบอร์โทรศัพท์ : </div>
										<div class="uk-width-1-2">
											<s:textfield id="tel_id" name="branchModel.tel" pattern="[0-9]{1,9}" placeholder="กรอกข้อมูล เป็นตัวเลขเท่านั้น" maxlength="9" required="required" />
										</div>
										
										<div class="uk-width-1-2 uk-text-right">เบอร์โทรศัพท์มือถือ : </div>
										<div class="uk-width-1-2">
											<s:textfield id="tels_id" name="branchModel.tels" pattern="[0-9]{1,10}" placeholder="กรอกข้อมูล เป็นตัวเลขเท่านั้น" maxlength="10" />
										</div>
									</div>
								</div>
								<div class="uk-container-center"> 
									<s:hidden name="olddoctor_id" value="%{branchModel.doctor_id}" 
										id="hide_doctor_list" />
									<s:hidden name="branchModel.branch_code" value="%{branchModel.branch_code}" />
									<s:hidden value="%{branchModel.addr_provinceid}" id="hide_province" />
									<s:hidden value="%{branchModel.addr_aumphurid}" id="hide_amphur" />
									<s:hidden value="%{branchModel.addr_districtid}" id="hide_district" />
									<button type="submit" class="uk-button uk-button-success uk-button-large "><i class="uk-icon-floppy-o"></i> บันทึกข้อมูล </button>
								</div>
								</div>
							</div>
							<!-- End Branch Detail-->
							<!-- Start Set up branch & doctor -->
							<div class="uk-width-6-10 padding5 border-gray">
								<div class="uk-grid uk-grid-collapse">
									<p class="uk-text-muted uk-width-1-1"> จัดการข้อมูลสาขา </p>
									<div class="uk-width-1-3">
										<a class="uk-button uk-button-primary" href="doctor-standard.jsp"><i class="uk-icon-money uk-icon-medium"></i> <br/>จัดการค่า Standard</a>
									</div>
									<div class="uk-width-1-3">
										<a href="price-list.jsp" class="uk-button uk-button-primary"><i class="uk-icon-money uk-icon-medium"></i> <br/>จัดการรายได้แพทย์</a>
									</div>
								</div>
								<div class="uk-grid uk-grid-collapse uk-margin-medium-top">
									<p class="uk-text-muted uk-width-1-1">ห้องรักษา</p>
									<div class="uk-width-1-1 uk-text-left">
										<a class="uk-button uk-button-success uk-margin-medium" 
											title="เพิ่มห้อง"
											data-uk-modal="{target:'#add_treamemt_room_modal'}"
											id="add-room">
											<span>เพิ่มห้อง</span>
											<li class=f"uk-icon-plus"></li>
										</a>
									</div>
									<div class="uk-width-1-1">
										<table class="uk-table uk-table-hover uk-table-condensed border-gray" 
											id="tb_treatment_room">
											<thead>
												<tr class="hd-table">
													<th class="uk-width-9-10 uk-text-center">รายการห้อง</th>
													<th class="uk-width-1-10"></th>
												</tr>
											</thead>
											<tbody>
												<s:iterator value="treatRoomList" var="room">
												<tr>
													<td class="uk-text-center"><s:property value="#room.room_name" /></td>
													<td>
														<div class="uk-button-dropdown" 
															data-uk-dropdown="{mode:'click'}"
															data-room-id="">
															<button class="uk-button uk-button-danger">
																จัดการ
															</button>
															<div class="uk-dropdown uk-dropdown-small">
																<form action="treatmentRoomAction" method="get">
																	<input type="hidden"  name="mode" value="">
																	<input type="hidden" name="branch_id" value="">
																	<ul class="uk-nav uk-nav-dropdown">
																		<li>
																			<a href=""
																				class="edit-tr"
																				data-mode="edit"
																				data-rid='<s:property value="#room.room_id" />'
																				data-rname='<s:property value="#room.room_name" />'
																				data-rbcode='<s:property value="%{branchModel.branch_code}" />'
																				data-uk-modal="{target:'#add_treamemt_room_modal'}">
																				แก้ไข 
																			</a>
																		</li>
																		<li>
																			<a href='delTr-<s:property value="#room.room_id" />-<s:property value="%{branchModel.branch_code}" />'
																				class="delete-tr"
																				data-mode="delete">
																				ลบ
																			</a>
																		</li>
																	</ul>
																</form>
															</div>
														</div>
													</td>
													</s:iterator>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
							<!-- End Set up branch & doctor -->
						</div>
						</form>
					</div>
				</div> 

<!-- THIS IS MODAL ZONE -->
<!-- TREATMENT'S ROOM FORM -->
<div class="uk-modal" id="add_treamemt_room_modal">
	<div class="uk-modal-dialog">
		<li class="uk-modal-close uk-close"></li>
		<h2 id="trform-header">เพิ่มข้อมูลห้องรักษา</h2>
		<!-- content -->
		<form action="addTreatmentRoom" method="post" class="uk-form" id="frm-modal-troom">
			<div class="uk-grid uk-grid-collapse">
				<div class="uk-width-1-6"></div>
				<div class="uk-width-4-6 uk-text-center">
						<div class="uk-form-row uk-text-left">
							<label class="uk-form-label" for="room-name">ชื่อหรือหมายเลขห้อง</label>
							<div class="uk-form-controls">
								<s:textfield name="treatRoomModel.room_name" placeholder="2003 (ห้องตรวจฟัน)" id="room-name" class="uk-width-1-1" />
								<s:hidden name="treatRoomModel.room_branch_code" value="%{branchModel.branch_code}" />
								<s:hidden name="branchModel.brand_name" />
								<s:hidden name="branchModel.brand_id" />
								<s:hidden name="branchModel.branch_name" />
								<s:hidden name="branchModel.branch_id" />
								<s:hidden name="branchModel.branch_code" />
								<s:hidden name="branchModel.doctor_name" />
								<s:hidden name="treatRoomModel.room_id" id="trid" />
							</div>
						</div>
				</div>
				<div class="uk-width-1-6"></div>
			</div>
			<div class="uk-modal-footer" id="trform-footer">
				<div class="uk-grid uk-grid-collapse uk-text-right">
					<div class="uk-width-1-1">
						<input type="submit" class="uk-button uk-button-success" value="Add">
						<input type="reset" id="modal-cancel" class="uk-button uk-button-danger uk-modal-close" value="Cancel">
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<!-- TREATMENT'S ROOM FORM -->
<!-- THIS IS MODAL ZONE -->

		<script>
			$(document).on("change","select[name='branchModel.addr_provinceid']",function(){
			
			var index = $("select[name='branchModel.addr_provinceid']").index(this); //GetIndex
			$("select[name='branchModel.addr_aumphurid'] option[value!='']").remove();  //remove Option select amphur by index is not value =''
			$("select[name='branchModel.addr_districtid'] option[value!='']").remove();  //remove Option select amphur by index is not value =''
			if($(this).val() != ''){ 
				$("select[name='branchModel.addr_aumphurid'] option[value ='']").text("กรุณาเลือกอำเภอ");
				$("#addr_aumphurid").select2();
				$.ajax({
			        type: "post",
			        url: "ajax/ajax-addr-amphur.jsp", //this is my servlet 
			        data: {method_type:"get",addr_provinceid:$(this).val()},
			        async:false, 
			        success: function(result){
			        	var obj = jQuery.parseJSON(result);
			        	for(var i = 0 ;  i < obj.length;i++){
			        		$("select[name='branchModel.addr_aumphurid']").append($('<option>').text(obj[i].amphur_name+" "+obj[i].amphur_name_eng).attr('value', obj[i].addr_aumphurid));
			        	}
				    } 
			     });

				$('#addr_aumphurid option[value="' + $("#hide_amphur").val() + '"]')
					.attr('selected', 'selected');
				$("#addr_aumphurid").select2().trigger('change');
			}else{
				$("select[name='branchModel.addr_aumphurid']  option[value ='']").text("กรุณาเลือกจังหวัด");
				$("select[name='branchModel.addr_districtid'] option[value!='']").remove();
				$("select[name='branchModel.addr_districtid'] option[value ='']").text("กรุณาอำเภอ");
			}
		}).on("change","select[name='branchModel.addr_aumphurid']",function(){
			 
			$("select[name='branchModel.addr_districtid'] option[value!='']").remove(); //remove Option select district by index is not value =''
			
			if($(this).val() != ''){
				$("select[name='branchModel.addr_districtid'] option[value ='']").text("กรุณาตำบล"); 
				$("#addr_districtid").select2();
				$.ajax({
			        type: "post",
			        url: "ajax/ajax-addr-district.jsp", //this is my servlet 
			        data: {method_type:"get",addr_aumphurid:$(this).val()},
			        async:false, 
			        success: function(result){
			        	var obj = jQuery.parseJSON(result);
			        	for(var i = 0 ;  i < obj.length;i++){
			        		
			        		$("select[name='branchModel.addr_districtid']").append($('<option>').text(obj[i].district_name+" "+obj[i].district_name_eng).attr('value', obj[i].district_id));
			        		
			        	}
				    } 
			     });

				$('#addr_districtid option[value="' + $("#hide_district").val() + '"]')
					.attr('selected', 'selected');
				$("#addr_districtid").select2();
			}else{
				$("select[name='branchModel.addr_districtid'] option[value ='']").text("กรุณาอำเภอ");
			}

		}).ready(function(){

			/*PREPARE MODAL FOR UPDATE TREATMENT ROOM.*/
			$(".edit-tr").on('click',function(){
				var mode = $(this).data('mode');
				var rid = $(this).data('rid');
				var rname = $(this).data('rname');
				var rbcode = $(this).data('rbcode');

				if(mode === 'edit'){
					$("#room-name").val(rname);
					$("#trid").val(rid);
					$("#frm-modal-troom").attr('action', 'editTr');
				}
			});

			$("#add-room").on('click', function(){
				$("#frm-modal-troom").attr('action', 'addTreatmentRoom');
			});
			/*PREPARE MODAL FOR UPDATE TREATMENT ROOM.*/
			

			/*SET DEFAULT DOCTOR LIST*/
			$('#doctorList option[value="' + $("#hide_doctor_list").val() + '"]').attr('selected', 'selected');

			$( ".m-setting" ).addClass( "uk-active" );

			$.ajax({
		        type: "post",
		        url: "ajax/ajax-addr-province.jsp", //this is my servlet
		        data: {method_type:"get",addr_provinceid:""},
		        async:false, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result);
		        	for(var i = 0 ;  i < obj.length;i++){ 	
		        	$("select[name='branchModel.addr_provinceid']").append($('<option>').text(obj[i].province_name+" "+obj[i].province_name_eng).attr('value', obj[i].addr_provinceid));
		        	}	 
			    } 
		     });

			$('#addr_provinceid option[value="' + $("#hide_province").val() + '"]')
				.attr('selected', 'selected');
			$("#addr_provinceid").select2().trigger('change');
			
			
			$(".province").change(function(){
				var province = $(".province").val(); 
				if(province==0){
					$('.amphur').children('option:not(:first)').remove();
					$('.district').children('option:not(:first)').remove(); 
				}else{
					$('.amphur').children('option:not(:first)').remove();
					$('.district').children('option:not(:first)').remove(); 
					if(province==1){
						$(".amphur").append( 
								"<option value='1'>เขตบางรัก</option>"+
					            "<option value='2'>เขตดุสิต</option>");
					}			
			        
				}
		    });
			$(".amphur").change(function(){
				var amphur = $(".amphur").val(); 
				if(amphur==0){
					$('.district').children('option:not(:first)').remove(); 
				}else{
					$('.district').children('option:not(:first)').remove(); 
					if(amphur ==1){
						$(".district").append( 
								"<option value='1'>สีลม</option>"+
					            "<option value='2'>สุริยวงศ์</option>");
					}else if(amphur ==2){
						$(".district").append( 
								"<option value='3'>ถนนนครไชยศรี</option>"+
					            "<option value='4'>สี่แยกมหานาค</option>");
					}
						
			        
				}
		    });
			$(".district").change(function(){
				var district = $(".district").val(); 
				if(district==1 || district==2){
					$("#postid").val(10500);
				}else if(district==3 || district==4){
					$("#postid").val(10300);
				}else{
					$("#postid").val("");
				}
		    });
		    $(".btn-reset").click(function(){
		    	$('.table-components tbody > tr:not(:first-child)').remove();
		    	$('.table-components-product tbody > tr:not(:first-child)').remove();
		    	$('.table-components-medicine tbody > tr:not(:first-child)').remove();
		    	});
		    
		    $('.table-components .add-elements').on('click', function(){ 
				var clone = $(".table-components tbody tr:first-child");
				clone.find('.btn').removeClass('add-elements uk-button-success').addClass('delete-elements uk-button-danger').html('<i class="uk-icon-times"></i>');
				clone.find('td:first-child').html($('.table-components tbody tr').length + 1); 
				clone.clone().appendTo('.table-components tbody');
				clone.find('td:first-child').html(1);
				clone.find('.btn').removeClass('delete-elements uk-button-danger').addClass('add-elements uk-button-success').html('<i class="uk-icon-plus"></i>');
				
				$('.table-components tbody tr:last-child td:nth-child(2)').on('change', function(event) {
					$(this).closest("tr").find('td:nth-child(3) input').focus();
				});
			});
		    
		    $('.table-components').on( "click", ".delete-elements", function() {
				$(this).closest("tr").remove();
				var n = 0;
				$('.table-components tbody tr').each(function(){
					n++;
					$(this).find('td:first-child').html(n);
				});
			});
		    
		    $('.table-components-product .add-elements').on('click', function(){ 
				var clone = $(".table-components-product tbody tr:first-child");
				clone.find('.btnproduct').removeClass('add-elements uk-button-success').addClass('delete-elements uk-button-danger').html('<i class="uk-icon-times"></i>');
				clone.find('td:first-child').html($('.table-components-product tbody tr').length + 1); 
				clone.clone().appendTo('.table-components-product tbody');
				clone.find('td:first-child').html(1);
				clone.find('.btnproduct').removeClass('delete-elements uk-button-danger').addClass('add-elements uk-button-success').html('<i class="uk-icon-plus"></i>');
				
				$('.table-components-product tbody tr:last-child td:nth-child(2)').on('change', function(event) {
					$(this).closest("tr").find('td:nth-child(3) input').focus();
				});
			});
		    
		    $('.table-components-product').on( "click", ".delete-elements", function() {
				$(this).closest("tr").remove();
				var n = 0;
				$('.table-components-product tbody tr').each(function(){
					n++;
					$(this).find('td:first-child').html(n);
				});
			});
		    
		    $('.table-components-medicine .add-elements').on('click', function(){ 
				var clone = $(".table-components-medicine tbody tr:first-child");
				clone.find('.btnmedicine').removeClass('add-elements uk-button-success').addClass('delete-elements uk-button-danger').html('<i class="uk-icon-times"></i>');
				clone.find('td:first-child').html($('.table-components-medicine tbody tr').length + 1); 
				clone.clone().appendTo('.table-components-medicine tbody');
				clone.find('td:first-child').html(1);
				clone.find('.btnmedicine').removeClass('delete-elements uk-button-danger').addClass('add-elements uk-button-success').html('<i class="uk-icon-plus"></i>');
				
				$('.table-components-medicine tbody tr:last-child td:nth-child(2)').on('change', function(event) {
					$(this).closest("tr").find('td:nth-child(3) input').focus();
				});
			});
		    
		    $('.table-components-medicine').on( "click", ".delete-elements", function() {
				$(this).closest("tr").remove();
				var n = 0;
				$('.table-components-medicine tbody tr').each(function(){
					n++;
					$(this).find('td:first-child').html(n);
				});
			});
		    
		    $('.table-components tbody').on('change', 'input', function() {
		    	var tr = $(this).closest("tr");
		    	
		    	var val1 = tr.find('td:nth-child(2) input').val().trim();
		    	var val2 = tr.find('td:nth-child(3) input').val().trim();
		    	
			});   
		       
		    
		    
		    $("#btnr").click(function(){
		    	$(".rl").first().clone().appendTo(".rs"); 
		    });
		    
		    
		    $(".btnrs").click(function(){ 
		    	 
		    	 $(this).parents(".rl").remove();
		    });
		    
		}); 
		
		</script>		
	</body>
</html>