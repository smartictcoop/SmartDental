<%@ page language="java" import="java.util.*,java.text.DecimalFormat"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.smict.all.model.*"%>
<%@ page import="com.smict.treatment.data.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html>
<html>
	<head>
		<title>Smart Dental:การรักษา</title>
		<link rel="icon" href="img/favicon.ico" type="image/x-icon"/>
	</head>
<body>
	<div class="uk-grid uk-grid-collapse">
		<div class="uk-width-1-10">
			<%@include file="nav-right.jsp"%>
		</div>
		<div class="uk-width-9-10">
			<%@include file="nav-top.jsp"%>
			<div class="uk-grid uk-grid-collapse">
				<div class="uk-width-4-10 ">

					<div class="uk-grid bg-gray padding5  border-gray">

						<div class="uk-width-2-3 ">
							<h3 class="hd-text padding5 uk-text-primary">ประวัติคนไข้</h3>
							<h4 class="hd-text ">
								<small class=" uk-text-primary">HN : </small>
								<s:property value="patModel.hn" />
							</h4>
							<h4 class="hd-text">
								<small class=" uk-text-primary">ชื่อ-สกุล (ไทย) : </small>
								<s:property value="patModel.pre_name_th" />
								<s:property value="patModel.firstname_th" />
								<s:property value="patModel.lastname_th" />
							</h4>
							<h4 class="hd-text">
								<small class=" uk-text-primary">ชื่อ-สกุล (ENG) : </small>
								<s:property value="patModel.pre_name_en" />
								<s:property value="patModel.firstname_en" />
								<s:property value="patModel.lastname_en" />
							</h4>
							<h4 class="hd-text">
								<small class=" uk-text-primary">อายุ : </small>
								<s:property value="patModel.age" />
								ปี
							</h4>
							<h4 class="hd-text">
								<small class=" uk-text-primary">เบอร์โทร: </small>
								<s:iterator value="patModel.ListTelModel"
									status="telStatus">
									<s:if test="%{#telStatus.index > 0}">,</s:if>
									<s:property value="tel_number" /> - <s:property
										value="tel_typename" />
								</s:iterator>
							</h4>
							<h4 class="hd-text">
								<small class=" uk-text-primary">ค้างชำระ: </small><span
									class="red"><s:property
										value="patModel.deposit_money" /> บาท</span>
							</h4>
							<h4 class="hd-text">
								<small class=" uk-text-primary">แผนการรักษา: </small><a
									href="" class="uk-button uk-button-primary">จัดการ</a>
							</h4>
						</div>
						<div class="uk-width-1-3  ">
							<img src='<s:property value="patModel.profile_pic"/>'
								alt="No Profile Picture" class="profile-pic">
						</div>

					</div>
	<div id="tooth-table-pic" class="uk-overflow-container">
		<table class="tooth-table border-gray ">
			<% if(request.getAttribute("toothListUp")!=null){ 
			List toothlist = (List) request.getAttribute("toothListUp"); %>
			<tr class="tooth-pic">
		    	<%	
		    	for (Iterator iterA = toothlist.iterator(); iterA.hasNext();) {
	       			ToothModel toothModel = (ToothModel) iterA.next();
	    				%>  
	    				<td id="tooth_<%=toothModel.getTooth_num()%>">
					<img src="img/tooth/<%=toothModel.getTooth_num()%>/<%=toothModel.getTooth_num()%>.jpg" />
			
				</td>
				<%}  %>
			</tr>
			
			<tr class="uk-text-center">
				<%	
		    	for (Iterator iterA = toothlist.iterator(); iterA.hasNext();) {
	       			ToothModel toothModel = (ToothModel) iterA.next();
	    				%> 
	    					<td><%=toothModel.getTooth_num()%></td>
	    				<%	} %>
			</tr>
		<%	}
		%>
		</table>
		
		<table class="tooth-table border-gray ">
			<% if(request.getAttribute("toothListLow")!=null){ 
			List toothlist = (List) request.getAttribute("toothListLow"); %>
			<tr class="uk-text-center">
				<%	
		    	for (Iterator iterA = toothlist.iterator(); iterA.hasNext();) {
	       			ToothModel toothModel = (ToothModel) iterA.next();
	    				%> 
	    					<td><%=toothModel.getTooth_num()%></td>
	    				<%	} %>
			</tr>
			<tr class="tooth-pic">
		    	<%	
		    	for (Iterator iterA = toothlist.iterator(); iterA.hasNext();) {
	       			ToothModel toothModel = (ToothModel) iterA.next();
	    				%>  
	    				<td id="tooth_<%=toothModel.getTooth_num()%>">
					<img src="img/tooth/<%=toothModel.getTooth_num()%>/<%=toothModel.getTooth_num()%>.jpg" />
				</td>
				<%	} %>
			</tr>
			
			
		<%	}
		%>
		</table>
	</div>

				</div>
				<div class="uk-width-6-10">

					<div class="uk-grid uk-grid-collapse">

						<div class="uk-width-4-10 border-gray padding5">

							<div class="uk-form">
								<h4 class="hd-text uk-text-primary">แพทย์ผู้รักษา</h4>
								<input type="text" class="uk-form-small uk-width-1-1"
									id="doctor_name" name="" value='<s:property value='docModel.pre_name_th' /><s:property value='docModel.first_name_th' /> <s:property value='docModel.last_name_th' />' readonly="readonly"> 
								<h4 class="hd-text uk-text-primary">ห้อง</h4>
								<input type="text" class="uk-form-small" id="treatment_code"
									readonly="readonly" value='<s:property value='treatModel.treatment_patient_roomName' />' />
							</div>
						</div>

						<div class="uk-width-6-10 border-gray padding5">


							<div class="uk-text-center uk-width-1-1 padding10">
								
								<a href="getTreatmentpatMedicine-<s:property value='treatModel.treatment_patient_ID' />"><button
										class="uk-button uk-button-primary uk-button-large uk-width-1-4" >
										<h1 class="white">
											<i class="uk-icon-medkit"></i>
										</h1>
										เพิ่มยา
									</button></a>

							</div>


						</div>
					</div>

					<div class="uk-grid uk-grid-collapse">
						<div class="uk-width-1-1">
								<div class="uk-panel uk-panel-box padding5 ">
								<div class="uk-panel uk-panel-box padding5 ">
									<h4 class="hd-text uk-text-primary"><i class="uk-icon-th-list"></i>
										รายการรักษา 
										<a href="#chooseTreatMent" 
											class="uk-button uk-button-primary uk-button-small uk-width-1-4"
											data-uk-modal="{bgclose:false}">
											เพิ่มการรักษา</a>
											
											<a href="" 
											class="showcontinuous uk-button uk-button-primary uk-button-small uk-width-1-4 hidden"
											data-uk-modal>ตั้งค่าการรักษาต่อเนื่อง
											</a>
									</h4>
									<hr class="margin5 ">
									<div class="treatment-bill ">
										<table class="uk-table uk-table-condensed ">
											<thead>
												<tr class="hd-table">
													<th class="uk-text-center">ประเภทการรักษา</th>
													<th class="uk-text-center">รหัสการรักษา</th>
													<th class="uk-text-center">การรักษา</th>
													<th class="uk-text-center">ราคา</th>
													<th class="uk-text-center">จัดการ</th>
												</tr>
											</thead>
											<tbody>
												<s:iterator value="treatPatList">
													<tr>
														<s:if test="treat_line_iscon == 1 ">
															<td class="uk-text-center continuous<s:property value="treat_line_iscon" />">การรักษาธรรมดา</td>
														</s:if>
														<s:else>
															<td class="uk-text-center continuous<s:property value="treat_line_iscon" />">การรักษาต่อเนื่อง</td>
														</s:else>
														<td class="uk-text-center"><s:property value="treatMent_code" /></td>
														<td class="uk-text-center"><s:property value="treatMent_name" /></td>
														<td class="uk-text-center"><s:property value="treatment_price" /></td>
														<td class="uk-text-center">
															<a href="#delete_treatpatLine" onclick="deleteTreatmentLine(<s:property value="treatpatLine_id" />,<s:property value="treatment_patient_id" />,<s:property value="treatment_ID" />)"
															 class="uk-button uk-button-danger uk-button-small" data-uk-modal >
															<i class="uk-icon-eraser"> ลบ</i>
															</a>
														</td>
													</tr>
												</s:iterator>
											</tbody>

										</table>
									</div>
									</div>

								</div>
						</div>
					</div>

				</div>
			</div>
			<!-- Modal---Treatment -->
			<div id="treatment" class="uk-modal ">
			<form action="addTreatmentPatientLine" id="submitAddTreat">
				<div class="uk-modal-dialog uk-form uk-modal-dialog-large">
					<a class="uk-modal-close uk-close"></a>
					<div class="uk-modal-header">
						<h2>
							<i class="uk-icon-stethoscope"></i> เพิ่มการรักษา
						</h2>
					</div>
					<div class="uk-grid uk-grid-collapse">
						<div class="uk-width-3-4">
							<h3><i class="uk-icon-th-list"></i> รายการรักษา <a href="#treatmentEmergency" id="treatshow" class="uk-button uk-button-success uk-button-small"
							data-uk-modal>เพิ่มการรักษาฉุกเฉิน</a></h3> 
							
							<table id="treatmentChooseTable" class="uk-table uk-table-hover uk-table-striped uk-table-condensed border-gray ">
								<thead>
									<tr class="hd-table">
										<th class="uk-text-center">เลือก</th>
										<th class="uk-text-center">ประเภทการรักษา</th>
										<th class="uk-text-center">รหัสการรักษา</th>
										<th class="uk-text-center">ชื่อการรักษา</th>
										<th class="uk-text-center">ราคา</th>
									</tr>
								</thead>
								<tbody>
									<s:iterator value="treatMasterList" var="tm">
										<tr>
											<th class="uk-text-center"><input type="radio" class="call-all " id="treatmentid-<s:property value="treatment_id" />" name="treatModel.treatment_ID" value="<s:property value="treatment_id" />" /></th>
											<s:if test="#tm.treatment_iscon == 1 ">
												<th class="uk-text-center">การรักษาธรรมดา</th>
											</s:if>
											<s:else>
												<th class="uk-text-center">การรักษาต่อเนื่อง</th>
											</s:else>
											<th class="uk-text-center"><s:property value="treatment_code" /></th>
											<th class="uk-text-center"><s:property value="treatment_nameth" /></th>
											<th class="uk-text-right"><s:property value="price" /></th>
										</tr>
									</s:iterator>

								</tbody>
							</table>
						</div>
						<div class="uk-width-1-4  uk-panel uk-panel-box padding5 ">
							<div class="uk-grid uk-grid-collapse">
								<div class="uk-width-1-1 padding5" >
									<h3>รายชื่อแพทย์</h3>
									<input type="text" class="uk-form-small uk-width-1-1"
									id="doctor_name" name="" value='<s:property value='docModel.pre_name_th' /><s:property value='docModel.first_name_th' /> <s:property value='docModel.last_name_th' />' readonly="readonly">
								</div>
								
								<div class="uk-width-1-1  ">
									<h3 >ประเภท</h3>
									<div class="uk-button-dropdown" data-uk-dropdown="{mode:'click'}">
										 <button class="uk-button" type="button" >เลือกประเภท
										 	<i class="uk-icon-caret-down"></i>
										 </button>
										 <div class="uk-dropdown uk-dropdown-small">
											<ul class="uk-nav uk-nav-dropdown uk-dropdown-close" data-uk-switcher="{connect:'#my-id-one', animation: 'fade'}">
											    <li id="hd_tooth" class="select-type " value="1"><a href="">Tooth</a></li>
											    <li id="hd_surf" class="select-type " value="2"><a href="">Surface</a></li>
											    <li id="hd_Mouth" class="select-type " value="3"><a href="">Mouth</a></li>
											    <li id="hd_quadrant" class="select-type " value="4"><a href="" >Quadrant</a></li>
											    <li id="hd_Sextant" class="select-type " value="5"><a href="">Sextant</a></li>
											    <li id="hd_arch" class="select-type " value="6"><a href="">Arch</a></li>
											    <li id="hd_toothRange" class="select-type " value="7"><a href="">Tooth Range</a></li>
											</ul>
										</div>										
									</div>
								</div>
									<div class="uk-width-1-1 uk-panel uk-panel-box padding5 ">
									<ul id="my-id-one"  class="uk-switcher type-proced " style="min-height: 25vh;"> 
										<li class="show-type-all" id="show_Tooth"><!-- Tooth  -->
											<div class="uk-grid">
												<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Tooth</h3>
												<div class="uk-width-1-2">
													<h5 class="hd-text uk-text-primary margin5">ซี่ฟัน</h5>   
													<input type="text" class="show-type" id="tooth_tooth" name="treatModel.tooth" pattern="[0-9].{0,2}" title="กรอกข้อมูล เป็นตัวเลขเท่านั้น" required="required" class="uk-form-small uk-width-1-1"  >
												</div>
											
											</div>
											
										</li>
										<li class="show-type-all" id="show_Surface"><!-- Surface  -->
											<div class="uk-grid">
											<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Surface</h3>
												<div class="uk-width-1-2">
													<h5 class="hd-text uk-text-primary margin5">ซี่ฟัน</h5> 
													<input type="text" class="show-type" id="surf_tooth" name="treatModel.surface_tooth" pattern="[0-9].{0,2}" title="กรอกข้อมูล เป็นตัวเลขเท่านั้น" class="uk-form-small uk-width-1-1" >
												</div>
												<div class="uk-width-1-2">
													<h5 class="hd-text uk-text-primary margin5">ด้านฟัน</h5>
													<input type="text" class="show-type" id="surf" readonly="readonly" name="treatModel.surface" pattern="[A-Z].{0,6}" title="กรอกข้อมูล เป็นอักษณตัวใหญ่เท่านั้น" class="uk-form-small uk-width-1-1" >
												</div>
											</div>
											<table class="surface-table uk-width-1-1">
												<tr>
													<td></td>
													<td><button class="uk-button uk-button-small " id="B," onclick="btnFunction(this)" type="button" value="1">B</button></td>
													<td><button class="uk-button uk-button-small " id="F," onclick="btnFunction(this)" type="button" value="1">F</button></td>
													<td></td>
												</tr>
												<tr>
													<td><button class="uk-button uk-button-small "id="M," onclick="btnFunction(this)" type="button" value="1">M</button></td>
													<td><button class="uk-button uk-button-small "id="O," onclick="btnFunction(this)" type="button" value="1">O</button></td>
													<td><button class="uk-button uk-button-small "id="I," onclick="btnFunction(this)" type="button" value="1">I</button></td>
													<td><button class="uk-button uk-button-small "id="D," onclick="btnFunction(this)" type="button" value="1">D</button></td>
												</tr>
												<tr>
													<td></td>
													<td colspan="2"><button class="uk-button uk-button-small " id="L" onclick="btnFunction(this)" type="button" value="1">L</button></td>
													<td></td>
												</tr>
											</table>
										</li>
										<li class="show-type-all" id="show_Mouth"><!-- Mouth  -->
										<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Mouth</h3>
											<h5 class="hd-text uk-text-primary margin5">เลือกทั้งปาก</h5>
										</li>
										<li class="show-type-all" id="show_Quadrant"><!-- Quadrant  -->
											<div class="uk-grid"> 
											<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Quadrant</h3>
												<label class="uk-width-1-2 margin5">
													<input type="radio" class="check-type-radio req-Qua" name="treatModel.quadrant" value="UL"/> UL(1)</label>
												<label class="uk-width-1-2 margin5">
													<input type="radio" class="check-type-radio req-Qua" name="treatModel.quadrant" value="UR" /> UR(2)</label>
												<label class="uk-width-1-2 margin5">
													<input type="radio" class="check-type-radio req-Qua" name="treatModel.quadrant" value="LL" /> LL(4)</label>
												<label class="uk-width-1-2 margin5">
													<input type="radio" class="check-type-radio req-Qua" name="treatModel.quadrant" value="LR" /> LR(3)</label>
											</div>
										</li>
										<li class="show-type-all" id="show_Sextant"><!-- Sextant -->
										<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Sextant</h3>
											<h5 class="hd-text uk-text-primary margin5">Sextant</h5>
										</li>
										<li class="show-type-all" id="show_Arch"><!-- Arch -->
											<div class="uk-grid">
											<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Arch</h3>
												<label class="uk-width-1-1 margin5"><input type="radio" class="check-type-radio req-Arch" name="treatModel.arch" value="U"/> U</label>
												<label  class="uk-width-1-1 margin5"><input type="radio" class="check-type-radio req-Arch" name="treatModel.arch" value="L"/> L</label>
											</div>
											
										</li>
										<li  class="show-type-all" style="overflow-x: scroll;" id="show_ToothRange"><!-- Tooth Range -->
										<h3 class="uk-text-center uk-width-1-1 uk-text-primary">Tooth Range</h3>
											<table  class="tooth-table border-gray uk-width-1-1">
												<tr class="tooth-pic-upper">
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper0' type="button" value="0">18</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper1' type="button" value="1">17</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper2' type="button" value="2">16</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper3' type="button" value="3">15</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper4' type="button" value="4">14</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper5' type="button" value="5">13</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper6' type="button" value="6">12</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper7' type="button" value="7">11</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper8' type="button" value="8">21</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper9' type="button" value="9">22</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper10' type="button" value="10">23</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper11' type="button" value="11">24</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper12' type="button" value="12">25</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper13' type="button" value="13">26</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper14' type="button" value="14">27</button></td>
													<td><button class="uk-button uk-button-small select-upper" onclick="tooth_range_button(this)" id='check-upper15' type="button" value="15">28</button></td>
												</tr>
											</table>
											<input type="hidden" class="tooth-upper checkall" value="" />
											<input type="hidden" class="tooth-lower checkall" value="" />
											<input type="hidden" class="tooth-keep checkall"  value="" />
											<table class="tooth-table border-gray uk-width-1-1">
												<tr  class="tooth-pic-lower" >
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower0' type="button" value="0">38</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower1' type="button" value="1">37</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower2' type="button" value="2">36</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower3' type="button" value="3">35</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower4' type="button" value="4">34</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower5' type="button" value="5">33</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower6' type="button" value="6">32</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower7' type="button" value="7">31</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower8' type="button" value="8">41</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower9' type="button" value="9">42</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower10' type="button" value="10">43</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower11' type="button" value="11">44</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower12' type="button" value="12">45</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower13' type="button" value="13">46</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower14' type="button" value="14">47</button></td>
													<td><button class="uk-button uk-button-small select-lower"  onclick="tooth_range_button(this)" id='check-lower15' type="button" value="15">48</button></td>
												</tr>
											</table>
											<input type="hidden" class="tooth-rangekeep checkall" name="treatModel.toothRange" value="" />
										</li>
									</ul>
									</div>
								
							</div>
						</div>
					</div>
					<div class="uk-modal-footer uk-text-right">
						<input type="hidden" value='' name="treatModel.tooth_types" id="tooth_typeName" />
						<input type="hidden" value="<s:property value="treatModel.treatment_patient_ID" />" name="treatModel.treatment_patient_ID" />
						<button class="uk-button uk-button-success" type="submit" id="checktreatment">ตกลง</button>
						<button class="uk-button uk-button-danger uk-modal-close">ยกเลิก</button>
					</div>
				</div>
				</form>
			</div>
			<!-- Modal---TreatmentEmergency -->
			<div id="treatmentEmergency" class="uk-modal ">

				<div class="uk-modal-dialog uk-form uk-modal-dialog">
					<a class="uk-modal-close uk-close"></a>
					<div class="uk-modal-header">
						<h2>
							<i class="uk-icon-stethoscope"></i> เพิ่มการรักษาฉุกเฉิน
						</h2>
					</div>
					<div class="uk-grid">
						<div class="uk-width-1-1">
							<div class="uk-grid uk-grid-collapse">
								<div class="uk-width-1-3 uk-text-right">
									<h3>รายชื่อแพทย์ : </h3>
								</div>
								<div class="uk-width-2-3">
									<input type="text" class="uk-form uk-width-2-3"
									id="doctor_name" name="" id="doclistemergency" value='<s:property value='docModel.pre_name_th' /><s:property value='docModel.first_name_th' /> <s:property value='docModel.last_name_th' />' readonly="readonly">
								</div>
							</div>
							
							
						</div>
						<div class="uk-width-1-1">
							<div class="uk-grid uk-grid-collapse">
								<div class="uk-width-1-3 uk-text-right">
								<h3>รายการรักษา : </h3> 
								</div>
								<div class="uk-width-1-3">
									<select style="width:300px" required="required" id="treatlistemergency">
											<option value=''>กรุณาเลือก</option>
										<s:iterator value="treatMasterList">									
											<option value="<s:property value="treatment_id" />"><s:property value="treatment_code" />/<s:property value="treatment_nameth" /></option>																																	
										</s:iterator>
									</select>
								</div>
							</div>
							
							
						</div>
						<div class="uk-width-1-1">
							<div class="uk-grid uk-grid-collapse">
								<div class="uk-width-1-3 uk-text-right">
								<h3>ค่า DF แพทย์ : </h3> 
								</div>
								<div class="uk-width-2-3">
									<input type="number" class="uk-text-right" name="" value="0" required="required" />%
								</div>
								
							</div>
							
							
						</div>

					</div>
					<div class="uk-modal-footer uk-text-right">
						<button class="uk-button uk-button-success"  type="button" id="addTreatEmergency">ตกลง</button>
						<button class="uk-button uk-button-danger uk-modal-close">ยกเลิก</button>
					</div>
				</div>

			</div>
			<!--End Modal---Treatment -->
			<!-- Modal---Delete Treatment patient Line -->
					<div id="delete_treatpatLine" class="uk-modal ">
						<form action="deleteTreatMentpatLine" method="post"> 
					    <div class="uk-modal-dialog uk-modal-dialog-small uk-form" >
				         	<div class="uk-modal-body"><i class="uk-icon-exclamation-circle"></i> ต้องการยืนยันการลบหรือไม่</div>
				         	<div class="uk-modal-footer uk-text-right">
				         		<input type="hidden" id="treatIDdel" value="" name="treatModel.treatment_ID">
			                    <input type="hidden" id="treatpatlineID" value="" name="treatModel.treatpatLine_id">
			                    <input type="hidden" id="treatpatID" value="<s:property value='treatModel.treatment_patient_ID' />" name="treatModel.treatment_patient_ID">
			                    <button type="submit" class="uk-button uk-button-default uk-button-success"> ยืนยัน</button>
			                    <button class="uk-button uk-button-default uk-button-danger uk-modal-close"> ยกเลิก</button>
                			</div>

					    </div>
					    </form>
					</div> 
			<!-- END Modal---Delete Treatment patient Line -->
			<!-- Modal---Choose Treatment patient  -->
					<div id="chooseTreatMent" class="uk-modal ">
					    <div class="uk-modal-dialog uk-modal-dialog-small uk-form" >
					    	<a class="uk-modal-close uk-close"></a>
					    	<div class="uk-modal-header">					    	
								<h2>
									<i class="uk-icon-stethoscope"></i> เลือกรูปแบบการเพิ่มการรักษา
								</h2>								
							</div>
				         	<div class="uk-modal-body uk-grid">
				         			<div class="uk-width-1-2">
				         			<a href="#treatment" id="addtreat" data-uk-modal="{bgclose:false}">
				         			<button
										class="uk-button uk-button-primary uk-button-large uk-width-1-1" >
										<h1 class="white">
											<i class="uk-icon-stethoscope"></i>
										</h1>
										เพิ่มการรักษาทั่วไป
									</button>
									</a>
									</div>
									<div class="uk-width-1-2">
									<a href="">
									<button
										class="uk-button uk-button-primary uk-button-large uk-width-1-1" data-uk-modal="{bgclose:false}">
										<h1 class="white">
											<i class="uk-icon-stethoscope"></i>
										</h1>
										เพิ่มการรักษาตามแผน
									</button>
									</a>
									</div>
				         	</div>
					    </div>
					</div> 
			<!-- END Modal---Choose Treatment patient  -->
			<!--  Modal--- Treatment patient plane  -->
			<!-- END Modal--- Treatment patient plane -->
			<!--  Modal--- Treatment patient continuous  -->
			<!-- END Modal--- Treatment patient continuous -->
			
		</div>
	</div>

<script>
	$(document).ready(function () {
		<% if(request.getAttribute("toothHistory")!=null){ 
			
			List<ToothModel> toothHistory = (List) request.getAttribute("toothHistory"); 
			for(ToothModel tm :toothHistory){%>
			$('#tooth_<%=tm.getTooth_num()%>').prepend('<img class="case" onerror=this.style.display="none" src="img/tooth/<%=tm.getTooth_num()%>/<%=tm.getTooth_pic_code()%>/<%=tm.getSurface()%>.png" />');
			<%}
		}%>
		if($('.continuous2').text() == 'การรักษาต่อเนื่อง'){
			$('.showcontinuous').removeClass('hidden');
		}
		$('select').select2();
		$('#treatmentChooseTable').dataTable();
		var  modal1 = UIkit.modal("#chooseTreatMent", {center: true, modal: true}), 
	    	modal2 = UIkit.modal("#treatmentEmergency", {modal: false});
		/* treatment_patient */
		$('.select-type').on('click', function() {
				var checktype = $(this).text();
				var type_values =	$(this).val();
				$('#tooth_typeName').val(type_values);
				$('.checkall').val('');
				$('.show-type').val('');
				$('.show-type').removeAttr('required');
				$('.surface-table').find("tr").find("td").find("button").removeClass(" uk-button-primary ");
				$('.check-type-radio').removeAttr("required");
				$('.check-type-radio').prop('checked', false);
				$('.select-upper').removeClass(' uk-button-primary');
				$('.select-lower').removeClass(' uk-button-primary');
		 		$('.checkall').val('');
					if(checktype == 'Tooth'){
						$("#tooth_tooth").attr('required',true);
					}else if(checktype == 'Surface'){
						$("#surf_tooth").attr('required',true);
						$("#surf").attr('required',true);  	
					}else if(checktype == 'Mouth'){
						
					}else if(checktype == 'Quadrant'){
						$('.req-Qua').attr('required',true);
					}else if(checktype == 'Sextant'){
						
					}else if(checktype == 'Arch'){
						$('.req-Arch').attr('required',true);
					}else{
						
					}
		});
		$('.req-Qua').on('click', function() {
			$('.req-Qua').removeAttr('required');
		});
		$('.req-Arch').on('click', function() {
			$('.req-Arch').removeAttr('required');
		});		
		/* treatment_patient select treatment and choose type */
		$(".call-all").click( function () {
			var treatID = $(this).val();
				$('.checkall').val('');
				$('.select-type').removeClass( "hidden uk-active" );
				$('.select-type').attr( "aria-expanded", false );
				$('.show-type-all').removeClass( " uk-active" );
				$('.show-type').val('');
				$('.show-type').removeAttr('required');
				$('.surface-table').find("tr").find("td").find("button").removeClass(" uk-button-primary ");
				$('.check-type-radio').removeAttr("required");
				$('.check-type-radio').prop('checked', false);
				$('.select-upper').removeClass(' uk-button-primary');
				$('.select-lower').removeClass(' uk-button-primary');
		 		$('.checkall').val('');
			$.ajax({
		        type: "post",
		        url: "ajax/ajax-treatment-check-type.jsp", //this is my servlet 
		        data: {treatID: treatID},
		        async:false, 
		        success: function(result){
		        	var obj = jQuery.parseJSON(result);
		        	var check = 0;
		        	for(var i = 0 ;  i < obj.length;i++){
	
			    			if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "Tooth" ){
			    				$('#hd_tooth').addClass( "hidden" );
			    			}else if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "Surface"){
			    				$('#hd_surf').addClass( "hidden" );
			    			}else if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "Mouth"){
			    				$("#hd_Mouth").addClass( "hidden" );
			    			}else if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "Quadrant"){
			    				$('#hd_quadrant').addClass( "hidden" );
			    			}else if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "Sextant"){
			    				$('#hd_Sextant').addClass( "hidden" );
			    			}else if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "Arch"){
			    				$('#hd_arch').addClass( "hidden" );
			    			}else if(obj[i].treatmentcheck == 'nu' && obj[i].treatmentName == "ToothRang"){
			    				$('#hd_toothRange').addClass( "hidden" );
			    			}							
							if(check == 0){
								if(obj[i].treatmentcheck != 'nu'){
									if(obj[i].treatmentName == "Tooth"){
										setFirstSelect($('#hd_tooth'),$('#show_Tooth'));
				    					$("#tooth_tooth").attr('required',true);
				    					$('#tooth_typeName').val('1');
				    					check++;
				    				}else if(obj[i].treatmentName == "Surface"){
				    					setFirstSelect($('#hd_surf'),$('#show_Surface'));
				    					$("#surf_tooth").attr('required',true);
										$("#surf").attr('required',true);  
										$('#tooth_typeName').val('2');
				    					check++;
				    				}else if(obj[i].treatmentName == "Mouth"){
				    					setFirstSelect($('#hd_Mouth'),$('#show_Mouth'));
				    					$('#tooth_typeName').val('3');
				    					check++;
				    				}else if(obj[i].treatmentName == "Quadrant"){
				    					setFirstSelect($('#hd_quadrant'),$('#show_Quadrant'));
				    					$('.req-Qua').attr('required',true);
				    					$('#tooth_typeName').val('4');
				    					check++;
				    				}else if(obj[i].treatmentName == "Sextant"){
				    					setFirstSelect($('#hd_Sextant'),$('#show_Sextant'));
				    					$('#tooth_typeName').val('5');
				    					check++;
				    				}else if(obj[i].treatmentName == "Arch"){
				    					setFirstSelect($('#hd_arch'),$('#show_Arch'));
				    					$('#tooth_typeName').val('6');
				    					$('.req-Arch').attr('required',true);
				    					check++;
				    				}else{
				    					setFirstSelect($('#hd_toothRange'),$('#show_ToothRange'));
				    					$('#tooth_typeName').val('7');
				    					check++;
				    				}
								}
							}
			    							    							    			
			    		}		        			        	
		        }
		     });
		});
		/* treatment_patient select treatment and choose type end */

		function setFirstSelect(select,show_div){
			$(select).addClass( "uk-active" ).attr( "aria-expanded", true );
			$(show_div).addClass( "uk-active" ).attr( "aria-expanded", true );
			
		}
		
	}).on("click","#addTreatEmergency",function () {
		
		if($('#treatlistemergency').val() != '' ){
				
				var docid =	<s:property value='docModel.doctorID' />;
				var treatid = $('#treatlistemergency').val();
				$.ajax({
			        type: "post",
			        url: "ajax/ajax-add-treatmentEmergency.jsp", //this is my servlet 
			        data: {docid: docid , treatid : treatid},
			        async:true, 
			        success: function(result){
			        	var obj = JSON.parse(result);
	                     if(obj.status == 'success'){
	                    	 $('.call-all').attr('disabled', false);
	                 		$.ajax({
	                	        type: "post",
	                	        url: "ajax/ajax-treatment-check-doctor-by-id.jsp", //this is my servlet 
	                	        data: {docid: docid},
	                	        async:false, 
	                	        success: function(result){
	                	        	var objs = jQuery.parseJSON(result);
	                	        	for(var i = 0 ;  i < objs.length;i++){	                	        		
	                		    			$('#treatmentid-'+objs[i].treatmentID).attr('disabled', true);
	                		    		}	                	        		
	                	        	}	                		     
	                	     });
	                 		 swal(
	                                 'การอนุมัติสำเร็จ',
	                                 'คลิกตกลงเพื่อทำรายการใหม่',
	                                 'success'                     
	                               );
	                 		 /* for modal after success */
	                 		var modal = UIkit.modal("#treatmentEmergency");
	                		if ( modal.isActive() ) {
	                		    modal.hide();
	                		} else {
	                		    modal.show();
	                		}
	                 		/* $("#doclist").select2("val", docid); */
	                     }else{
	                         swal(
	                                 'การอนุมัติไม่สำเร็จ',
	                                 'การรักษาที่เพิ่มมาได้เปิดใช่งานอยู่แล้ว',
	                                 'error'
	                               );
	                              } 
	                     }
				     
			     });
		
		}else{
			swal(
                    'การอนุมัติไม่สำเร็จ',
                    'กรุณาเลือกการรักษา',
                    'error'
                    
                  );
		}
			
	});
	$('#addtreat').click(function () {
		$('.call-all').attr('disabled', false);
		var docid = <s:property value='docModel.doctorID' />;
		if(docid != ''){
		$.ajax({
	        type: "post",
	        url: "ajax/ajax-treatment-check-doctor-by-id.jsp", //this is my servlet 
	        data: {docid: docid},
	        async:false, 
	        success: function(result){
	        	var obj = jQuery.parseJSON(result);
	        	for(var i = 0 ;  i < obj.length;i++){
	        		
		    			$('#treatmentid-'+obj[i].treatmentID).attr('disabled', true);
		    		}
	        		
	        	}
		     
	     });
		}
	});
	$("#treatshow").click(function () {
		var modal = UIkit.modal("#treatmentEmergency");
		if ( modal.isActive() ) {
		    modal.hide();
		} else {
		    modal.show();
		}
			$("#doclistemergency").select2("val", $("#doclist").val());
	})
	function btnFunction(elem){
				
				 var suf = $("#surf").val();
				 var btn =  elem;
				 if(btn.value=='1'){
					 
					 suf += btn.id;
					 $("#surf").val(suf);
					 btn.value='2';
					 elem.className +=" uk-button-primary ";
					
				 }else if(btn.value=='2'){ 
					 var suf = suf.replace(btn.id, "");
					 $("#surf").val(suf);  
					 btn.value='1';
					 elem.className =" ";
					 elem.className +=" uk-button uk-button-small ";
				 }  
			}
	function deleteTreatmentLine(treatpatlineID,treatpatID,treatIDdel) {
		$('#treatpatlineID').val(treatpatlineID);
		$('#treatpatID').val(treatpatID);
		$('#treatIDdel').val(treatIDdel);
	}
	function tooth_range_button(elem){
		var keep = elem;
		var checkTRup = $('.tooth-upper').val();
		var checkTRlow = $('.tooth-lower').val();
		var checkTRange = $('.tooth-keep').val();
		var keppalltooth = $('.tooth-rangekeep').val();
		if(keep.className == "uk-button uk-button-small select-upper" && checkTRlow == ''){
			if(checkTRup < 1){
				elem.className +=' uk-button-primary';
	 			$('.tooth-keep').val(elem.value);	 			
	 			 checkTRup =  1;
	 			$('.tooth-upper').val(checkTRup);
			}else if(checkTRup < 2){
				if(checkTRange < elem.value){		 			
		 			for(var i = checkTRange;i<=elem.value ; i++){		 				
		 				$('.select-upper').eq(i).addClass(' uk-button-primary');
		 				keppalltooth += $('#check-upper'+i).text()+',';
		 			}
		 			checkTRup = parseInt(checkTRup) + 1;
		 			$('.tooth-rangekeep').val(keppalltooth);
		 			$('.tooth-upper').val(checkTRup);
		 		}else{

		 			for(var i = elem.value ;i <= checkTRange ; i++){		 				
		 				$('.select-upper').eq(i).addClass(' uk-button-primary');
		 				keppalltooth += $('#check-upper'+i).text()+',';
		 			}
		 			checkTRup = parseInt(checkTRup) + 1;
		 			$('.tooth-rangekeep').val(keppalltooth);
		 			$('.tooth-upper').val(checkTRup);
		 		}
			}else{
				$('.select-upper').removeClass(' uk-button-primary');
		 		$('.checkall').val('');
			}			
		}else if(keep.className == "uk-button uk-button-small select-upper uk-button-primary" && checkTRlow == ''){
			$('.select-upper').removeClass(' uk-button-primary');
	 		$('.checkall').val('');
		}else if(keep.className == "uk-button uk-button-small select-lower" && checkTRup == ''){
			if(checkTRlow < 1){
				elem.className +=' uk-button-primary';		 			
	 			$('.tooth-keep').val(elem.value);
	 			checkTRlow =  1;
	 			$('.tooth-lower').val(checkTRlow);
			}else if(checkTRlow < 2){
		 		if(checkTRange < elem.value){		 			
		 			for(var i = checkTRange;i<=elem.value ; i++){		 				
		 				$('.select-lower').eq(i).addClass(' uk-button-primary');
		 				keppalltooth += $('#check-lower'+i).text()+',';
		 			}
		 			checkTRlow = parseInt(checkTRlow) + 1;
		 			$('.tooth-lower').val(checkTRlow);
		 			$('.tooth-rangekeep').val(keppalltooth);
		 		}else{

		 			for(var i = elem.value ;i <= checkTRange ; i++){		 				
		 				$('.select-lower').eq(i).addClass(' uk-button-primary');
		 				keppalltooth += $('#check-lower'+i).text()+',';
		 			}
		 			checkTRlow = parseInt(checkTRlow) + 1;
		 			$('.tooth-lower').val(checkTRlow);
		 			$('.tooth-rangekeep').val(keppalltooth);
		 		}
			}else{
				$('.select-lower').removeClass(' uk-button-primary');
		 		$('.checkall').val('');
			}
		}else if(keep.className == "uk-button uk-button-small select-lower uk-button-primary" && checkTRup == ''){
			$('.select-lower').removeClass(' uk-button-primary');
	 		$('.checkall').val('');
		}else{
			$('.select-upper').removeClass(' uk-button-primary');
			$('.select-lower').removeClass(' uk-button-primary');
	 		$('.checkall').val('');
		}
	 		
	}

</script>
</body>
</html>
