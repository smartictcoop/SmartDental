
	/**
	 * Global variables.
	 */
	var pageStat = {
		/**
		 * Available times.
		 */
		events: [], 

		/**
		 * Doctor's name list.
		 */
		users: [],

		/**
		 * Doctor's id
		 */
		userId: [],

		/**
		 * Appointment list.
		 */
		agenda: [],

		/**
		 * Week calendar instance.
		 */
		calendarInstance: null,

		/**
		 * var calEvent
		 */
		calEvent: []
	}

    /**
     * isPostpone()
     * checking postpone status
     */
    var isPostpone = function(){
        if($("#ldc-header-title").data('reference-code') != "" || 
            (typeof(Storage) != "undefined" && typeof(localStorage.postpone) != "undefined")){
            /**
             * Change title name.
             */
            $("#ldc-header-title").html("โปรดเลือกช่วงเวลาเพื่อทำการเลื่อนการนัดหมาย");

            /**
             * Set modal activity.
             */
            console.log("postpone true");
            let reason = $("#ldc-header-title").data('reason')
            let appointID = $("#ldc-header-title").data('appointment-id');
            let refID = $("#ldc-header-title").data('reference-code');
            console.log("HN", $("#ldc-header-title").data('hn'));
            let hn = $("#ldc-header-title").data('hn').replace('#', '');
            $("#ldc-modal-add-frm").find('form').prop('action', 'add-new-postpone');

            /**
             * Check browser support.
             */
            if (typeof(Storage) != 'undefined') {
                if(appointID != "" && refID != ""){
                    // Store
                    var postpone = {
                        reason: reason,
                        refCode: refID,
                        appID: appointID,
                        hn: hn
                    }
                    localStorage.setItem("postpone", JSON.stringify(postpone));
                }

                /**
                 * On submit postpone.
                 */
                $("#ldc-modal-add-frm").on('click', '#ldc-add-appointment', function(event) {
                    localStorage.removeItem("postpone");
                    $(this).parent('form').submit();
                });
            } else {
                let msg = "ฟังชั่นก์ localStorage ไม่รองรับในเบราเซอร์ของคุณ โปรดดำเนินการดังนี้<br/> - อัพเดทเบราเซอร์ของคุณ<br/> - ติดต่อผู้ดูแลระบบ";
                console.log("ฟังชั่นก์ localStorage ไม่รองรับในเบราเซอร์ของคุณ");
                uiKitModalBlockUI(msg, 4000);
            }
            return true;
        }else{
            console.log("postpone false");
            return false;
        }
    }

    /**
     * Load freeBusy.
     */
    var loadFreeBusy = function(obj){
    	$.ajax({
			url: "ajax-get-doctor-appointment",
			type: 'POST',
			dataType: 'json',
			data: {
				'scheduleModel.startDateTime': obj.startDateTime,
				'scheduleModel.endDateTime': obj.endDatetime
			},
    	})
    	.done(function(data, xhr, status) {
    		pageStat.events = data;
    		let v = new Array();
    		pageStat.users = new Array();
    		$.each(data, function(index, value) {
    			let i = 0, ind2 = 0;
    			$.each(v, function(ind, val) {
    				if(val == value.doctor){
    					i = 1;
    					ind2 = ind;
    					return
    				}
    			});

    			if(i === 0){
    				v.push(value.doctor);
    				pageStat.events[index].userId = v.length - 1;
    				pageStat.userId[v.length - 1] = value.doctorId;
    			}else{
    				pageStat.events[index].userId = ind2;
    				pageStat.userId[ind2] = value.doctorId;
    			}
    		});
    		pageStat.users = JSON.parse(JSON.stringify(v));
    		pageStat.events = JSON.parse(JSON.stringify(pageStat.events));
    		if(obj.onSuccess){
    			obj.onSuccess();
    		}

    	})
    	.fail(function(data, xhr, status) {
    		if(obj.onFail){
    			obj.onFail();
    		}
    	})
    	.always(function(data, xhr, status) {
    		if(obj.onAlways){
    			obj.onAlways();
    		}
    	});
    }


    /**
     * Get AJAX appointment list.
     */
    var loadAppointment = function(obj){
    	$.ajax({
    		url: "ajax-get-doctor-appointment-list",
    		type: 'POST',
    		dataType: 'json',
    		data: {
    			'appointmentModel.dateStart': obj.dateStart,
    			'appointmentModel.dateEnd': obj.dateEnd
    		},
    	})
    	.done(function(data, xhr, status) {
    		//{"id":734,"start":"2017-07-24:08:20:00.0","end":"2017-07-24:10:00:00.0","title":"เวรลงตรวจ","userId":3},
        console.log("USERID", pageStat.userId);
    		$.each(data, function(index, value) {
          console.log("userID before", value.userId);

    			$.each(pageStat.userId, function(ind, val) {
            console.log("COMPARE", value.userId, val, "INDEX", ind);
            if(value.userId == val){
  						value.userId = ind;

              /**
               * Break or exit loop.
               */
              return false;
  					}	    					
    			});
    		});
        console.log("DATA", data);
    		pageStat.agenda = data;

    		if(obj.onSuccess){
    			obj.onSuccess();
    		}
    	})
    	.fail(function() {
    		if(obj.onFail){
    			obj.onFail();
    		}
    	})
    	.always(function() {
    		if(obj.onAlways){
    			obj.onAlways();
    		}
    	});
    	
    }
 
    /**
     * Set select2 in modal
     */
    var setModalSelect2 = function(callBack, id){
    	$("#modal-group").on('mouseover', id, function(event) {
    		event.preventDefault();
    		$(this).select2();
  			if(callBack){
  				callBack();	
  			}
    	});
    }

    /**
     * Function call week calendar
     */
    var callWeekCalendar = function(){
        var setDate = new Date(sessionStorage.dateDefault);
      	pageStat.calendarInstance = $('#calendar').weekCalendar({
	      	date: setDate,
	        timeslotsPerHour: 12,
	        scrollToHourMillis : 0,
		    allowCalEventOverlap: true,
	        use24Hour: true,
		    overlapEventsSeparate: true,
		    totalEventsWidthPercentInOneColumn : 95,
		    hourLine: true,

	        height: function($calendar){
	          return $(window).height() - $('h1').outerHeight(true);
	        },
	        eventRender : function(calEvent, $event) {
                pageStat.calEvent = calEvent;
                let curTime = new Date().getTime();
                let eventBGColor = '';
                let headEventBGColor = '', bd = '';
                let appStatus = calEvent.appointment_status;

                if(appStatus == 0 || appStatus == 1 || appStatus == 6){
                    /**
                     * 0 = Success, 1 = Disapppoint, 6 = Disgard
                     * BgColor Black
                     */
                     eventBGColor = '#AAA6A6';
                     headEventBGColor = calEvent.colour;
                     bd = '1px solid #666363';
                } else if (appStatus == 5) {
                    /**
                     * 5 = Wait
                     * BgColor Blue
                     */
                     eventBGColor = '#4557FF';
                     headEventBGColor = calEvent.colour;
                     bd = '1px solid #0D22E7';
                } else if (appStatus == 2) {
                     /**
                      * 2 = Confirm
                      * BgColor Green
                      */
                     eventBGColor = '#42C13F';
                     headEventBGColor = calEvent.colour;
                     bd = '1px solid #288026';
                } else if (appStatus == 4) {
                    /**
                     * 4 = Postpone
                     * BgColor Yellow
                     */
                     eventBGColor = '#EAD33B';
                     headEventBGColor = calEvent.colour;
                     bd = '1px solid #C9B111';
                } else if (appStatus == 3 || appStatus == 7) {
                    /**
                     * 3 = Cancel, 7 = ETC
                     * BgColor Red
                     */
                     eventBGColor = '#F03535';
                     headEventBGColor = calEvent.colour;
                     bd = '1px solid #BE1010';
                }

                $event.css('backgroundColor', eventBGColor);
                $event.find('.wc-time').css({
                  backgroundColor: headEventBGColor,
                  border:bd
                });
            },
	        eventNew : function(calEvent, $event, FreeBusyManager, calendar) {
                pageStat.calEvent = calEvent;
                var isFree = true;
                if(pageStat.calEvent.end.getTime() < new Date().getTime()){
                    /**
                     * this event has pass away.
                     */
                    uiKitModalBlockUI(
                        "<h2>ไม่สามารถสร้างการนัดหมายในช่วงเวลาที่ผ่านมาแล้ว</h2>",
                        3000
                    );
                    $(calendar).weekCalendar('removeEvent',calEvent.id);
                    return false;
                }else{
                    /**
                     * displayFreeBusys is : false.
                     */
                    $.each(FreeBusyManager.getFreeBusys(calEvent.start, calEvent.end), function() {
                        /**
                         * Checking whether start event & end event time that equals each other and 
                         * and this state have free {true|false} status is false that mean you can't
                         * create event on this state.
                         */
                        if (
                          this.getStart().getTime() != calEvent.end.getTime()
                          && this.getEnd().getTime() != calEvent.start.getTime()
                          && !this.getOption('free')
                        ){
                          isFree = false;
                          return false;
                        }
                    });

                    if (!isFree) {
                        uiKitModalBlockUI(
                            "<h2>ไม่สามารถสร้างการนัดหมายใน (ช่องทึบ)ช่วงเวลาที่ไม่ว่างได้!</h2>",
                            3000
                        );
                        $(calendar).weekCalendar('removeEvent',calEvent.id);
                        return false;
                    }
                  UIkit.modal("#ldc-modal-conf", {bgclose: false, keyboard: false}).show();
                  /*calEvent.id = calEvent.userId +'_'+ calEvent.start.getTime();
                  $(calendar).weekCalendar('updateFreeBusy', {
                    userId: calEvent.userId,
                    start: calEvent.start,
                    end: calEvent.end,
                    free:false
                  });*/
                }

            },
	        eventClick: function(calEvent, element, dayFreeBusyManager, calendar, clickEvent){
                pageStat.calEvent = calEvent;
                console.log("EVENT CLICK", pageStat.calEvent);

                /**
                 * Check appointment's expiry date.
                 */
                if(calEvent.end.getTime() < new Date().getTime()){
                    /**
                     * Agenda has pass away.
                     * Return something.
                     */
                }else{
                    /**
                     * Showing modal.
                     */
                    UIkit.modal("#ldc-modal-doonclick").show();
                    let contactStatus = "getAppointmentpatient-" + pageStat.calEvent.id;
                    let appointmentStatus = "getAppointmentList-" + pageStat.calEvent.id;
                    $("#ldc-modal-edit-status").prop('href', contactStatus);
                    $("#ldc-modal-appointment-delete").prop('href', appointmentStatus);
                }
            },
	        draggable: function(calEvent, element) {
	        	pageStat.calEvent = calEvent;
	        	return false;
	        },
	        data: function(start, end, callback) {

	            callback({
	              options: {
	                defaultFreeBusy: {
	                  free:false
	                }
	              },
			      /*freebusys: [
			        {'start': new Date(year, month, day), 'end': new Date(year, month, day+3), 'free': false, userId: [0,1,2,3]},
			        {'start': new Date(year, month, day, 8), 'end': new Date(year, month, day, 12), 'free': true, userId: [0,1,2,3]},
			        {'start': new Date(year, month, day+1, 8), 'end': new Date(year, month, day+1, 12), 'free': true, userId: [0,1,2,3]},
			        {'start': new Date(year, month, day+2, 8), 'end': new Date(year, month, day+2, 12), 'free': true, userId: [0,1,2,3]},
			        {'start': new Date(year, month, day+1, 14), 'end': new Date(year, month, day+1, 18), 'free': true, userId: [0,1,2,3]},
			        {'start': new Date(year, month, day+2, 8), 'end': new Date(year, month, day+2, 12), 'free': true, userId: [0,3]},
			        {'start': new Date(year, month, day+2, 14), 'end': new Date(year, month, day+2, 18), 'free': true, userId: 1}
			      ],
	              events: [
		        	{"id":734,"start":"2017-07-24:08:20:00.0","end":"2017-07-24:10:00:00.0","title":"เวรลงตรวจ","userId":3},
		        	{"id":735,"start":"2017-07-24:13:50:00.0","end":"2017-07-24:15:30:00.0","title":"เวรลงตรวจ","userId":2},
		        	{"id":736,"start":"2017-07-24:13:50:00.0","end":"2017-07-24:15:40:00.0","title":"เวรลงตรวจ","userId":1},
		        	{"id":737,"start":"2017-07-24:15:15:00.0","end":"2017-07-24:20:50:00.0","title":"เวรลงตรวจ","userId":1},
		        	{"id":738,"start":"2017-07-24:16:45:00.0","end":"2017-07-24:19:15:00.0","title":"เวรลงตรวจ","userId":3},
		        	{"id":739,"start":"2017-07-24:17:25:00.0","end":"2017-07-24:18:55:00.0","title":"เวรลงตรวจ","userId":2}
	    			]*/
	    			freebusys: pageStat.events,
				 	events: pageStat.agenda
	            }); 
	        },
	        // users: ['<a href="">วีศรุต คุ้มวิไล</a>', 'ลมโชย เย็นจริง', 'สมจิตร ค้อนทองคำ', 'จักรวาล ดวงดาว'],
	        users: pageStat.users,
	        showAsSeparateUser: true,
	        displayOddEven: true,
	        displayFreeBusys: true,
        	buttons: false,
	        buttonText: false, 
	        daysToShow: 1,
	        switchDisplay: {'1 day': 1, '3 next days': 3, 'work week': 5, 'full week': 7},
	        headerSeparator: ' ',
	        useShortDayNames: true,
	        // I18N
	        firstDayOfWeek: $.datepicker.regional['th'].firstDay,
	        shortDays: $.datepicker.regional['th'].dayNamesShort,
	        longDays: $.datepicker.regional['th'].dayNames,
	        shortMonths: $.datepicker.regional['th'].monthNamesShort,
	        longMonths: $.datepicker.regional['th'].monthNames,
	        dateFormat: 'd F y'

    	});


      $('#data_source').change(function() {
        $calendar.weekCalendar('refresh');
        // updateMessage();
      });

      // updateMessage();
    }

    /**
     * Make uikit start modal block ui
     */
    var uiKitModalBlockUI = function(msg, sec){
    	var modal = UIkit.modal.blockUI(msg);
    	setTimeout(
    		function(){ 
    			modal.hide();
    		}, 
    		sec
		);
    }

    /**
     * Set select date listener.
     */
    var selectDateListener = function(callBack){
    	$("#ldc-select-date-wrap").on('change', '#selectDate', function(event) {
    		event.preventDefault();
    		callBack($(this));
    	});
    }

    /**
     * Remove event listener 
     */
    var removeEventListener = function(callBack, elem){
    	$("#modal-group").on('click', elem, function(event) {
    		event.preventDefault();
    		pageStat.calendarInstance.weekCalendar('removeEvent');
    		if(callBack){
	    		callBack();
    		}
    	});
    }

    /**
     * Modal add new event form.
     */
    var modalAddEventForm = function(callBack){
    	$("#modal-group").on('click', '#ldc-modal-confirm', function(event) {
    		event.preventDefault();
            let hn = $("#ldc-hid-inp-patient-hn").val();

            let openModal = function(callBack){
                UIkit.modal('#ldc-modal-add-frm', {bgclose: false, keyboard: false}).show();
                if(callBack){
                    callBack();
                }
            }

            if(hn !== ""){
                openModal(callBack);
            }else{
                if(isStorageSupport() && localStorage.postpone !== undefined){
                    console.log("undefined", localStorage.postpone);
                    openModal(callBack);
                }else{
                    console.log("non-undefined");
                    uiKitModalBlockUI(
                        '<div class="uk-text-center uk-h1">กรุณาเลือกคนไข้ก่อนทำการสร้างนัดหมาย</div>', 
                        3000
                    );
                }
            }
    	});
    }

    /**
     * Clear page stat
     */
    var clearPageStat = function(){
		pageStat.events = [];
		pageStat.agenda = [];
		pageStat.users = [];
		pageStat.userId = [];
		pageStat.calEvent = [];
    }

    /**
     * Loop doctor details'button.
     */
    var loopDoctorButton = function(obj){
        let html = " ";
        if(pageStat.users.length > 0){
            $.each(pageStat.users, function(index, val) {
                html += "<li><a href='view-appointment-by-doctor-" + pageStat.userId[index] + "'><h3>" + val + "</h3></a></li>";
            });
        }else{
            html = "<li><h3>ยังไม่มีแพทย์ลงตรวจในวันนี้</h3></li>";
        }
        $(obj.target).html('').append(html);
        if(obj.callBack){
            obj.callBack();
        }
    }

    /**
     * Setting up edit appointment modal.
     * @author Wesarut.khm@gmail.com
     * @param {[json Object]} obj [Object for each ajax status (done|fail|always)]
     * @param {[int]} id  [Appointment id.]
     */
    var setModalEditAppointment = function(obj){
      $.ajax({
        url: 'ajax-get-appointment-' + obj.id,
        type: 'POST',
        dataType: 'json',
        data: {param1: 'value1'}, 
      })
      .done(function(data, xhr, status) {
        console.log("STATUS", "done");
        if(obj.done){
          obj.done(data);
        }
      })
      .fail(function() {
        console.log("STATUS", "fail");
        if(obj.fail){
          obj.fail();
        }
      })
      .always(function() {
        console.log("STATUS", "always");
        if(obj.always){
          obj.always();
        }
      });
    }



    /**
     * All about date default.
     */
    

    /**
     * Init date default.
     */
    var initDate = function(obj){

        /**
         * Check browser support.
         */
        if (isStorageSupport()) {
            let date = "";
            if(typeof(sessionStorage.dateDefault) === "undefined"){
                /**
                 * Set today.
                 */
                setDateDefault(new Date());
            }

            /**
             * Yesterday button.
             */
            $(obj.wrap).on('click', obj.yesterday.id, function(event) {
                event.preventDefault();
                obj.yesterday.act();
            }); 

            /**
             * Tomorrow button.
             */
            $(obj.wrap).on('click', obj.tomorrow.id, function(event) {
                event.preventDefault();
                obj.tomorrow.act();
            });

            /**
             * Today button.
             */
            $(obj.wrap).on('click', obj.today.id, function(event) {
                event.preventDefault();
                obj.today.act();
            });
        } else {
            let msg = "ฟังชั่นก์ localStorage ไม่รองรับในเบราเซอร์ของคุณ โปรดดำเนินการดังนี้<br/> - อัพเดทเบราเซอร์ของคุณ<br/> - ติดต่อผู้ดูแลระบบ";
            console.log("ฟังชั่นก์ localStorage ไม่รองรับในเบราเซอร์ของคุณ");
            uiKitModalBlockUI(msg, 4000);
        }

    }

    /**
     * Set week calendar goto date.
     */
    var setGotoDate = function(weekCalendarInstance, date){
        if(weekCalendarInstance !== null){
            /**
             * Clear pageStat
             */
            clearPageStat();

            /**
             * Change page to the selected date.
             */
            weekCalendarInstance.weekCalendar('gotoDate', date);

            /**
             * Update freeBusy & appointment
             */
            // console.log("thisObj Date format : ", new Date(date).toString('yyyy-MM-dd'));
            loadFreeBusy({
                startDateTime: new Date(date).toString('yyyy-MM-dd') + " 00:00:00", 
                endDatetime: new Date(date).toString('yyyy-MM-dd') + " 23:59:59",
                onSuccess: false, 
                onFail: false, 
                onAlways: function(){
                    /**
                     * Generate doctor detail button.
                     */
                    loopDoctorButton({
                        target: "#ldc-doctor-detail"
                    });
                
                    /**
                     * Get agenda list.
                     */
                    loadAppointment({
                        dateStart: new Date(date).toString('yyyy-MM-dd') + " 00:00:00", 
                        dateEnd: new Date(date).toString('yyyy-MM-dd') + " 23:59:59", 
                        onSuccess: false,
                        onFail: false,
                        onAlways: function(){
                            /**
                             * Recall Weekcalendar for update column details
                             */
                            callWeekCalendar();

                            /**
                             * Refresh appointment.
                             * (It generate event & appointment dataset in json type already.
                             * - pageStat.events & pageStat.agenda
                             * You just refresh it!)
                             */
                           weekCalendarInstance.weekCalendar('refresh');

                            /**
                             * Generate doctor detail nav.
                             */
                            loopDoctorButton({
                                navID: "#ldc-doctor-detail"
                            });
                        }
                    });
                }
            });

        
        }else{
            console.log("setGotoDate()", "weekCalendarInstance: null");
        }
    }
    

    /**
     * Set date in sessionStorage
     */
    var setDateDefault = function(date){

        /**
         * Check browser support.
         */
        if (isStorageSupport()) {
            /**
             * Set date.
             */
            sessionStorage.setItem("dateDefault", date);
        } else {
            let msg = "ฟังชั่นก์ localStorage ไม่รองรับในเบราเซอร์ของคุณ โปรดดำเนินการดังนี้<br/> - อัพเดทเบราเซอร์ของคุณ<br/> - ติดต่อผู้ดูแลระบบ";
            console.log("ฟังชั่นก์ localStorage ไม่รองรับในเบราเซอร์ของคุณ");
            uiKitModalBlockUI(msg, 4000);
        }
    }

    /**
     * Checking whether user's browser is support Storage.
     */
    var isStorageSupport = function(){
        return typeof(Storage) !== "undefined" ? true : false;
    }