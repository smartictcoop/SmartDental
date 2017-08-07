
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
     * Load freeBusy.
     */
    var loadFreeBusy = function(obj){
    	console.log("loadFreeBusy", obj);
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
    		console.log("success", data);
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
    		console.log("pagestat users", pageStat.users);
    		console.log("pagestat events", pageStat.events);
            console.log("PAGESTAT", pageStat);
    		if(obj.onSuccess){
    			obj.onSuccess();
    		}

    	})
    	.fail(function(data, xhr, status) {
    		console.log("error");
    		if(obj.onFail){
    			obj.onFail();
    		}
    	})
    	.always(function(data, xhr, status) {
    		console.log("complete");
    		if(obj.onAlways){
    			obj.onAlways();
    		}
    	});
    }


    /**
     * Get AJAX appointment list.
     */
    var loadAppointment = function(obj){
    	console.log("This is AJAX appointment.");
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
    		console.log("load appointment success", data);
    		//{"id":734,"start":"2017-07-24:08:20:00.0","end":"2017-07-24:10:00:00.0","title":"เวรลงตรวจ","userId":3},
    		console.log("pagestat: ", pageStat)
    		$.each(data, function(index, value) {
    			$.each(pageStat.userId, function(ind, val) {
					if(value.userId == val){
						value.userId = ind;
					}	    					
    			});
    		});
    		pageStat.agenda = data;

    		if(obj.onSuccess){
    			obj.onSuccess();
    		}
    	})
    	.fail(function() {
    		console.log("error");
    		if(obj.onFail){
    			obj.onFail();
    		}
    	})
    	.always(function() {
    		console.log("complete");
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
    	// clearPageStat();
		var setDate = new Date();
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
	        	console.log("event render", pageStat.calEvent);
	          if (calEvent.end.getTime() < new Date().getTime()) {
	            $event.css('backgroundColor', '#aaa');
	            $event.find('.wc-time').css({
	              backgroundColor: '#999',
	              border:'1px solid #888'
	            });
	          }
	        },
	        eventNew : function(calEvent, $event, FreeBusyManager, calendar) {
	        	pageStat.calEvent = calEvent;
	          	var isFree = true;
	          	/*console.log("calEvent", calEvent);
	          	console.log("$event", $event);
	          	console.log("FreeBusyManager", FreeBusyManager);
	          	console.log("calendar", calendar);*/

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
	        },
	        eventClick: function(calEvent, element, dayFreeBusyManager, calendar, clickEvent){
	        	pageStat.calEvent = calEvent;
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
    	console.log('blockUI');
    	var modal = UIkit.modal.blockUI(msg);
    	console.log("modal", modal);
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
    		UIkit.modal('#ldc-modal-add-frm', {bgclose: false, keyboard: false}).show();
    		if(callBack){
    			callBack();
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
        console.log("loopDoctorButton");
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