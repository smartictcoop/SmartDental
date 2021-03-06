<%@ page language="java" import="java.util.*,java.text.DecimalFormat" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ page import="com.smict.product.model.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html>
	<head>
		<title>Smart Dental : promotion</title>
		<link href="css/style-promotion.css"rel="stylesheet">	
		<link rel="icon" href="img/favicon.ico" type="image/x-icon"/>
	<style>
	
		.errorMessage{
			color :red;
			position : absolute;
			top : 25px;
		}
	</style>
	</head> 
	<body>

	<div class="uk-grid uk-grid-collapse">
			<div class="uk-width-1-10">
				<%@include file="nav-right.jsp" %>
			</div>
			<div class="uk-width-9-10">
				<%@include file="backend-promotion-manage-top.jsp" %>
				<form id="createPro" action="addPromotionPoints" method="post" >
					<div class="padding5 border-gray uk-panel uk-panel-box bg-gray">					
						<div class="uk-panel uk-panel-box " style="min-height: 99vh;">
							<div class="uk-panel-header">
								<h3 class ="uk-panel-title"><i class="uk-icon-th-list"></i> โปรโมชั่น</h3>
							</div>
					<div class="uk-grid">
						<div class="uk-width-1-1 uk-overflow-container">
							<div class="uk-grid">
								<div class="uk-width-2-10"></div>
								<div class="uk-width-6-10 ridge">									
										<input type="hidden" name="protionModel.points_id" 
										value="<s:property value='protionModel.points_id' />" />
										<input type="hidden" name="protionModel.promotion_id" value="<s:property value='protionModel.promotion_id' />">
										<div class="uk-grid">
											
											<div class="uk-width-1-2">
												<h3>รูปแบบแต้มสมาชิก</h3>
											</div>
														
										</div>
										<div class="uk-grid mt-1">
											<div class="uk-width-3-5 uk-form ">
												<s:radio  name="protionModel.points_type" id="notcount" list="#{'0':' ไม่ได้รับแต้มสมาชิก'}"  checked="true" />	
											</div>																						
										</div>
										<div class="uk-grid mt-1">
										<div class="uk-width-3-5 uk-form ">
												<s:radio  name="protionModel.points_type" id="yescount" list="#{'1':' ได้รับแต้มสมาชิก'}" />																									
										</div>
										</div>
<!-- 										<div class="uk-grid mt-0 hidden pointsnumber">
											
											<div class="uk-width-2-5 uk-form uk-form-icon">
												<i class="uk-icon-money"></i>
												<input type="text"   name="protionModel.point" id="pointsNum" class="uk-width-1-1 numeric"  value="0" />
											</div>
										</div> -->
										<div class=" ridge hidden pointsnumber">																						
											<table class="uk-table uk-table-condensed uk-table-hover uk-form">
												<thead>
													<tr>
														<th class="uk-text-center">ประเภทลูกค้า</th>
														<th class="uk-text-center">จำนวน</th>
													</tr>
													
												</thead>
												<tbody class="tbodyContact">

												</tbody>
											</table>	
										</div>										
									</div>
									<div class="uk-width-2-10"></div>									
								</div>																																																	
						</div>
						<div class="uk-width-1-1  uk-text-center">	
							<div class="uk-form-icon">	
								<button class=" uk-button uk-button-success" type="submit" id="allsave" >บันทึก</button>
							</div>
							<div class="uk-form-icon">
		                        <a href="promotionManagement-<s:property value='protionModel.promotion_id' />" class=" uk-button uk-button-danger" >ยกเลิก</a>
		                    </div>
		                </div>    
					</div>	
					</div>

			</div>
					
			</form>
	</div>
</div>
<script src="js/autoNumeric.min.js"></script>				
	<script>		
		$(document).ready(function(){
			$(".numeric").autoNumeric('init');
			if(<s:property value='protionModel.points_id' />!= 0) {
				if(<s:property value='protionModel.points_type' /> == 1){	
					$('#yescount').prop('checked',true);
					prosubAjax()
					checkHidden($('.pointsnumber'),"none");
					$(".numeric").autoNumeric('init');
					/* $('#pointsNum').val(<s:property value='protionModel.points' />); */
				}
			}
				
		});
		function checkHidden(name1,name2) {
			$(name1).removeClass('hidden');
    		$(name2).addClass('hidden');
		}
		function prosubAjax() {
			return $.ajax({     
			    type: "post",
			    url: "ajax_json_contactPointsLine", 
			    data: {promotionID:<s:property value='protionModel.promotion_id' />},
			    async:false, 
			    success: function(result){ 
				    if (result != '') {	
				    	var selectContact = "";
				    	$.each(result, function(i, value) { 
					    	selectContact += '<tr> '+
					    					'<th class="uk-text-center">'+value.sub_name+''+
					    					'<input type="hidden" name="protionModel.point_contactID" value="'+value.pro_sub_id+'" /></th>'+
					    					'<th class="uk-text-center">'+
					    					'<input type="text"   name="protionModel.firstpoints" id="pointsNum"'+
					    					'class=" numeric"  value="'+value.points+'" /></th>'+
					    					'</tr>';
					    					
				    	});          
				    	$(".tbodyContact").html(selectContact);  
				    }
			    }
			});
		}
		$(document).on("change","input[name='protionModel.points_type']",function(){
			var typeprosub =  <s:property value='protionModel.points_type' /> 
	    	if($(this).val()== 1){
	    		if(typeprosub == 1){
	    			prosubAjax()
	    			checkHidden($('.pointsnumber'),"none");
		    		$(".numeric").autoNumeric('init');
	    		}else{
	    			$.ajax({     
					    type: "post",
					    url: "ajax_json_contactPoints", 
					    data: {promotionID:<s:property value='protionModel.promotion_id' />},
					    async:false, 
					    success: function(result){ 
						    if (result != '') {	
						    	var selectContact = "";
						    	$.each(result, function(i, value) { 
							    	selectContact += '<tr> '+
							    					'<th class="uk-text-center">'+value.sub_name+''+
							    					'<input type="hidden" name="protionModel.point_contactID" value="'+value.pro_sub_id+'" /></th>'+
							    					'<th class="uk-text-center">'+
							    					'<input type="text"   name="protionModel.firstpoints" id="pointsNum"'+
							    					'class=" numeric"  value="0" /></th>'+
							    					'</tr>';
							    					
						    	});          
						    	$(".tbodyContact").html(selectContact);  
						    }
					    }
					});
	    		}
	    			    		
	    		checkHidden($('.pointsnumber'),"none");
	    		$(".numeric").autoNumeric('init');
	    	}else{
	    		
	    		checkHidden("none",$('.pointsnumber'));	    		
	    		/* $('#pointsNum').val('0'); */
	    		$(".tbodyContact").html("");
	    	}
		});	
/* 		$(document).on("click",".plancallall",function(){
			
		}); */
		
		
	</script>				
	</body>
</html>