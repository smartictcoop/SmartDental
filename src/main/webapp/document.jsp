<%@ page language="java" import="java.util.*,java.text.DecimalFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.smict.document.model.*" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Smart Dental:เอกสาร</title>
	</head> 
	<body>
		<div class="uk-grid uk-grid-collapse">
			<div class="uk-width-1-10">
				<%@include file="nav-right.jsp" %>
			</div>
			<div class="uk-width-9-10">
				<%@include file="nav-top.jsp" %>
				<div class="uk-grid"></div>
				<div class="uk-grid">
				    <%@include file="document-nav.jsp" %>
				    <div class="uk-width-7-10 uk-overflow-container">
				    	<div class="uk-panel uk-panel-box uk-width-medium-1-1">
			    			<div class="uk-panel-badge uk-badge uk-badge-primary">อัพโหลดไฟล์</div>  
                            <div class="uk-panel-header">
							    <h3 class="uk-panel-title"><i class="uk-icon-cloud-upload"></i> อัพโหลดไฟล์
							    <% if(request.getAttribute("status_error") != null) {%>
									 <span class="red "><%=request.getAttribute("status_error").toString()%></span>
									<% } %>
									<% if(request.getAttribute("status_success") != null) {%>
									 <span class="uk-text-success"><%=request.getAttribute("status_success").toString()%></span>
									<% } %>
							    </h3>
							</div> 
							<form action="DocumentUpload" method="POST" enctype="multipart/form-data" class="uk-grid uk-form">
								<div class="uk-width-1-4">
									<p style="margin-bottom:5px;">เลือกไฟล์</p>
									<input class="uk-width-1-1 border" name="myFile" type="file"/>
								</div>
								<div class="uk-width-1-4">
									<p style="margin-bottom:5px;">เพิ่มไปที่</p>
									<select name="NameOfFolder" class="uk-width-1-1">
										<option value="Patient">ข้อมูลคนไข้</option>
										<option value="General">ทั่วไป</option>
										<option value="TreatMentPlan">แผนการรักษา</option>
										<option value="TreatMentHistory">การรักษาคนไข้</option>
										<option value="MedicalHistory">ประวัติทางการแพทย์</option>
										<option value="Financial">ข้อมูลทางการเงิน</option>
										<option value="Finance">อนุมัติสินเชื่อ</option>
										<option value="Mail">จดหมาย</option>
									</select>
								</div>
								<div class="uk-width-1-4">
									<p style="margin-bottom:5px;">วันที่เอกสาร</p>
									<input type="text" name="docDate" id="docDate" class=" uk-width-1-1" required>
								</div>
								<div class="uk-width-1-4">
									<p style="margin-bottom:5px;">เพิ่ม</p>
									<button type="submit"  class="uk-button uk-button-primary"><i class="uk-icon-cloud-upload "></i> อัพโหลด</button>
								</div>
							</form>
			    		</div>
			    		
			    		<div class="uk-panel uk-panel-box uk-width-medium-1-1">
			    			<div class="uk-panel-badge uk-badge uk-badge-primary">ไฟล์</div>  
							<div class="uk-panel-header">
							    <h3 class="uk-panel-title"><i class="uk-icon-cloud"></i> ไฟล์</h3>
							</div> 	
							<table id="document-table" class="cell-border compact hover">
								<thead>
									<tr>
										<th></th>
										<th>Document Name</th>
										<th>Folder Name</th>
										<th>Document Date</th>
										<th>Type</th>
										<th>Upload Date</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<%   
									    if(request.getAttribute("DocumentList")!=null)	{
										    List brandlist = (List) request.getAttribute("DocumentList");
		                                	List <DocumentModel> docmodel = brandlist;
		                                	int x=0;
			            	         	 	for(DocumentModel pbm : docmodel){ 
			            	         	 	x++; 
		            	         	 %>
								        <tr>
								            <td class="uk-text-left">
									            <a href="<%=pbm.getPath()%>" target="_blank" class="uk-button uk-button-primary uk-button-small">
												<i class="uk-icon-cloud-download"></i></a>
								            </td>
								            <td class="uk-text-left"><%=pbm.getDoc_name()%></td>
								            <td class="uk-text-left"><%=pbm.getDocument_folder()%></td>
								            <td class="uk-text-left"><%=pbm.getDocDate()%></td>
								            <td> <i class="<%=pbm.getClass_icon()%>"></i> <%=pbm.getDoc_type()%></td>
								             <td><%=pbm.getUpload_date()%></td>
									        <td class="uk-text-center"><a href="DelDocument?del=<%=pbm.getDocument_id()%>" class="uk-button uk-button-danger uk-button-small">
												<i class="uk-icon-trash"></i></a>
											</td>
								        </tr> 
							        <% } %> 
								        
							        <% }else{ %>
							        	 <tr>
								            <td class="uk-text-center" colspan="5">ไม่พบข้อมูล</td> 
								        </tr> 
							        <% } %> 
								
								</tbody>
							</table>
						
			    		</div> 
			    		
			    		</div>
				    </div>
				</div>
			</div>

		<script>
			$(document).ready(function(){
				$( ".m-document" ).addClass( "uk-active" ); 
				$("#document-table").DataTable();
				var accordion = UIkit.accordion($('.uk-accordion'),{
					showfirst: false 
				});
				$("#docDate").datepicker({
				    format: "dd-mm-yyyy",
			        clearBtn: true,
			        autoclose: true,
			        todayHighlight: true
			    });
			});
		</script>
	</body>
</html>