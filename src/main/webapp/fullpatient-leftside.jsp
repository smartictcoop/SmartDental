<%@ page language="java" import="java.util.*,java.text.DecimalFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.smict.all.model.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="uk-width-4-10 ">
						
<div class="uk-grid bg-gray padding5  border-gray">
	<div class="uk-width-2-3 ">
		<h3 class="hd-text padding5 uk-text-primary">ประวัติคนไข้</h3>	
		<h4 class="hd-text " ><small class=" uk-text-primary">HN : </small> <s:property value="servicePatModel.hnFormat"/></h4> 
		<h4  class="hd-text"><small class=" uk-text-primary">ชื่อ-สกุลไทย : </small> <s:property value="servicePatModel.pre_name_th"/> <s:property value="servicePatModel.firstname_th"/> <s:property value="servicePatModel.lastname_th"/></h4>
		<h4  class="hd-text"><small class=" uk-text-primary">ชื่อ-สกุลต่างชาติ : </small> <s:property value="servicePatModel.pre_name_en"/> <s:property value="servicePatModel.firstname_en"/> <s:property value="servicePatModel.lastname_en"/></h4>
		<h4  class="hd-text"><small class=" uk-text-primary">อายุ : </small> <s:property value="servicePatModel.age"/> ปี</h4>
		
		<h4  class="hd-text"><small class=" uk-text-primary">เบอร์โทร: </small> 
			<s:iterator value="servicePatModel.ListTelModel" status="telStatus">
				<s:if test="%{#telStatus.index > 0}">,</s:if>
				<s:property value="tel_number"/> - <s:property value="tel_typename"/>
			</s:iterator>
		</h4>
		<h4  class="hd-text"><small class=" uk-text-primary">แผนการรักษา: </small><a href="viewAllTreatmentPlan" class="uk-button uk-button-primary">จัดการ</a></h4>
		<h4  class="hd-text"><small class=" uk-text-primary">ประเภทการรักษา: <s:property value="servicePatModel.patient_type_name"/> </small> </h4>
		
		<h4  class="hd-text"><small class=" uk-text-primary">เงินฝาก : </small>
			<b class="uk-text-primary"> <s:property value="servicePatModel.deposit_money"/> บาท</b> - <a href="#">เพิ่มเงินฝาก</a>
		</h4>
		<h4  class="hd-text"><small class=" uk-text-primary">ค้างชำระ: </small><span class="red"> 1,500 บาท</span></h4>
		<h4  class="hd-text"><small class=" uk-text-primary">คะแนนสะสม: </small>  
			<b class="uk-text-success"> 450 คะแนน</b> - <a href="#point" data-uk-modal>ดูคะแนนสะสม</a>
		</h4>
		<h4  class="hd-text"><small class=" uk-text-primary">ดูนัดหมาย: </small>  
			<a href="appoint.jsp" class="uk-button uk-button-primary uk-button-small"><i class="uk-icon-search"></i> ดูนัดหมาย </a>
		</h4>
		<h4  class="hd-text"><small class=" uk-text-primary">ไฟล์ประวัติการรักษา : </small>  
			<a href="document.jsp" class="uk-button uk-button-primary uk-button-small"><i class="uk-icon-search"></i> ดูประวัติการรักษา </a>
		</h4>
		<h4  class="hd-text">
			<small class=" uk-text-primary">รหัสแฟ้ม: </small>
			<button class="uk-button uk-button-primary"">
				<i class="uk-icon-refresh"></i>
			</button>
		</h4>
		<h4  class="hd-text">
			<small class=" uk-text-primary">เพิ่มสาขา: </small>
			<button class="uk-button uk-button-primary" data-uk-modal="{target:'#addBranch'}">
				<i class="uk-icon-search"></i>
			</button>
		</h4>
		
		
		<table id="file" class="uk-table uk-table-striped uk-table-hover uk-table-condensed ">
			<thead>
			        <tr class="hd-table"> 
			            <td class="uk-text-center">สาขา</td>
			            <td class="uk-text-center">รหัส</td>
			            <td class="uk-text-center">ลบ</td>
			        </tr>
			    </thead> 
			    <tbody>
			    	<s:iterator value="servicePatModel.patFileList">
			    		<tr>  
				    		<td class="uk-text-center"> <s:property value="branch_name"/> </td>
					        <td class="uk-text-center"> <s:property value="fileId"/> </td>
					        <td class="uk-text-center">
					        	<button class="uk-button uk-button-danger uk-button-small remove-tr">
					        		<i class="uk-icon-minus"></i>
				        		</button>
					        </td>
			    		<tr>  
			    	</s:iterator>
				</tbody>
		</table>
		<s:url action="entranchEditPatient" var="entranchEditPatient">
		</s:url>
		<a href='<s:property value="entranchEditPatient"/>' class="uk-button uk-button-primary uk-button-small "><i class="uk-icon-pencil-square-o"></i> แก้ไขข้อมูลคนไข้</a> 
		<button class="uk-button uk-button-primary uk-button-small "><i class="uk-icon-print"></i> Print</button>
		<a href="addPatientBranch.jsp" class="uk-button uk-button-primary uk-button-small "><i class="uk-icon-link"></i> สาขา </a>
	</div>
	<div class="uk-width-1-3  ">
		<img src='<s:property value="servicePatModel.profile_pic"/>' alt="No Profile Picture" class="profile-pic">
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
<div class="padding5 border-gray uk-panel uk-panel-box bg-gray">
	<h4 class="hd-text uk-text-primary">โน๊ตการแพทย์</h4>
	<textarea class="boxsizingBorder" rows="5"></textarea>
	<div class="uk-grid">
		<div class="uk-width-1-2">
			<h4  class="hd-text uk-text-primary">โรคประจำตัว </h4>
			<select size="5" style="width:100%;" disabled="true">
				<s:if test="%{servicePatModel.congenList.isEmpty()}">
					<option>ไม่มีโรคประจำตัว</option>
				</s:if>
				<s:else>
					<s:iterator value="servicePatModel.congenList"> 
						<option><s:property value="congenital_name_th"/> - <s:property value="congenital_name_en"/></option>
					</s:iterator>
				</s:else>
			</select>
		</div>
		<div class="uk-width-1-2">
			<h4 class="hd-text uk-text-primary">ประวัติแพ้ยา</h4>
			<select size="5" style="width:100%;" disabled="true">
				<s:if test="%{servicePatModel.beallergic.isEmpty()}">
					<option>ไม่มีประวัติแพ้ยา</option>
				</s:if>
				<s:else>
					<s:iterator value="servicePatModel.beallergic"> 
						<option><s:property value="product_name"/></option>
					</s:iterator>
				</s:else>
			</select>
			</div>
		</div>
	</div>
</div>