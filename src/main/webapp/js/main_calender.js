
(function ($) {
	/* "YYYY-MM[-DD]" => Date */
	function strToDate(str) {
		try {
			var array = str.split('-');
			var year = parseInt(array[0]);
			var month = parseInt(array[1]);
			var day = array.length > 2? parseInt(array[2]): 1 ;
			if (year > 0 && month >= 0) {
				return new Date(year, month - 1, day);
			} else {
				return null;
			}
		} catch (err) {}; // just throw any illegal format
	};

	/* Date => "YYYY-MM-DD" */
	function dateToStr(d) {
		/* fix month zero base */
		var year = d.getFullYear();
		var month = d.getMonth();
		return year + "-" + (month + 1) + "-" + d.getDate();
	};
	
	function dateToStr_detail(d) {
		/* fix month zero base */
		var year = d.getFullYear();
		var month = d.getMonth();
		console.log(year);
		console.log(month);
		var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
		return year + "." + (monthNames[month]);
	};

	$.fn.calendar = function (options) {
		var _this = this;
		var opts = $.extend({}, $.fn.calendar.defaults, options);
		var week;
		var tHead;
		
//		/if($(_this).hasClass("full")){
			week = ['SUN', 'MON', 'TUE', 'WED', 'THUR', 'FRI', 'SAT'];
		/*}else{
			week = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
		}*/
		
		tHead= week.map(function (day) {
			return "<th>" + day + "</th>";
		}).join("");

		_this.init = function () {
			var tpl;
			
			if(_this.hasClass("full")){
				tpl = '<table class="cal">' +
				'<caption>' +
				'	<span class="prev Material_icons"><a href="javascript:void(0);">chevron_left</a></span>' +
				'	<span class="next Material_icons"><a href="javascript:void(0);">chevron_right</a></span>' +
				'	<span class="month NanumBarunGothic"><span>' +
				"</caption>" +
				"<thead><tr>" +
				tHead +
				"</tr></thead>" +
				"<tbody>" +
				"</tbody>" + "</table>";
			}else{
				tpl = '<table class="cal">' +
				'<caption>' +
				'	<span class="prev NanumSquare_R"><a href="javascript:void(0);">＜</a></span>' +
				'	<span class="next NanumSquare_R"><a href="javascript:void(0);">＞</a></span>' +
				'	<span class="month NanumSquare_R"><span>' +
				"</caption>" +
				"<thead><tr>" +
				tHead +
				"</tr></thead>" +
				"<tbody>" +
				"</tbody>" + "</table>";
			}
			/*var full_tpl = '<table class="cal">' +
			"<thead><tr>" +
			tHead +
			"</tr></thead>" +
			"<tbody>" +
			"</tbody>" + "</table>";*/

			/*if($(_this).hasClass("full")){
				html= $(full_tpl);
			}else{
				html= $(tpl);
			}*/
			var html= $(tpl);
			_this.append(html);
		};


		
		_this.getCurrentDate = function () {
			return _this.data('date');
		}

		_this.init();
		/* in date picker mode, and input date is empty,
		 * should not update 'data-date' field (no selected).
		 */
		var initDate = opts.date? opts.date: new Date();
		if (opts.date || !opts.picker) {
			_this.data('date', dateToStr(initDate));
		}
		_this.update(initDate);

		/* event binding */
		_this.delegate('tbody td', 'click', function () {
			var $this = $(this);
			if(!$(_this).hasClass("full")){
				_this.find('.active').removeClass('active');
				$this.addClass('active');
			}
			_this.data('date', $this.find('a').data('date'));
			/* if the 'off' tag become selected, switch to that month */
			if ($this.hasClass('off')) {
				_this.update(strToDate(_this.data('date')));
			}
			var layerPop=$("#calreview_wrap");
			layerPop.css("top", $(this).offset().top-(layerPop.height()/2)+$(this).height()/2);
			layerPop.css("left",$(this).offset().left+$(this).width());
			
			layerPop.removeClass("hidden");
		});
		
		/*$(".cal_view").delegate('tbody td', 'mouseover', function () {
			console.log("FASDFASDF ");
			
		});*/

		function updateTable(monthOffset) {
			var date;
			
			//main달력 
			if(_this.hasClass("full")){
				date = strToDate(_this.find('.month').text());
			}else{
				date=$.fn._thisDate;
			}
			
			date.setMonth(date.getMonth() + monthOffset);
			$('.jquery-calendar').each(function () {
				$(this).update(date);
			});
			
		};

		_this.find('.next').click(function () {
			updateTable(1);
		});

		_this.find('.prev').click(function () {
			updateTable(-1);
		});

		return this;
	};
	$.fn._thisDate;
	$.fn.update = function (date) {
		var _this = this;
		var mDate = new Date(date);
		mDate.setDate(1); /* start of the month */
		var day = mDate.getDay(); /* value 0~6: 0 -- Sunday, 6 -- Saturday */
		mDate.setDate(mDate.getDate() - day) /* now mDate is the start day of the table */
		
		$.fn._thisDate=date;
		function dateToTag(d) {
			var tag;
			
			//if($(_this).hasClass("full")){
			tag= $('<td class="day_'+d.getDate()+'"><a></a></td>');
			/*}else{
				tag= $('<td><a href="javascript:void(0);"></a></td>');
			}*/
			
			var a = tag.find('a');
			a.text(d.getDate());
			a.data('date', dateToStr(d));
			if (date.getMonth() != d.getMonth()) { // the bounday month
				tag.addClass('off');
			} else if (_this.data('date') == a.data('date')) { // the select day
				if($(_this).hasClass("detail")){
					tag.addClass('today');
				}
				_this.data('date', dateToStr(d));
			}
			return tag;
		};

		var tBody = _this.find('tbody');
		tBody.empty(); /* clear previous first */
		var cols = Math.ceil((day + daysInMonth(date))/7);
		for (var i = 0; i < cols; i++) {
			var tr = $('<tr></tr>');
			for (var j = 0; j < 7; j++, mDate.setDate(mDate.getDate() + 1)) {
				tr.append(dateToTag(mDate));
			}
			tBody.append(tr);
		}

		/* @ set month head */
		var monthStr;
		if(_this.hasClass("full")){
			//main
			monthStr = dateToStr(date).replace(/-\d+$/, '');
		}else{
			//일정관리 페이지
			monthStr=dateToStr_detail(date);
		}
		
		_this.find('.month').text(monthStr);
		
		if(_this.hasClass("detail")){
			var _thisMonth;
			
			if(date.getMonth() < 9){
				_thisMonth="0"+(date.getMonth()+1);
			}else{
				_thisMonth=date.getMonth()+1;
			}
			
			if(!_this.find("caption").find("span").hasClass("mon")){
				_this.find("caption").append("<span class='mon'>"+_thisMonth+"</span>");
			}else{
				_this.find('.mon').text(_thisMonth);
			}
		}
	};
	 
	function daysInMonth(d) {
		var newDate = new Date(d);
		newDate.setMonth(newDate.getMonth() + 1);
		newDate.setDate(0);
		return newDate.getDate();
	}
	
	$.fn.dayTitleInsert= function(startDay,endDay,scheduleTitle,color,type){
		var firstIdx=0;
		for(var i=startDay; i<=endDay; i++){
			var insertSpan;
			var thisIdx;
			if(type=="circle"){
				type=type;
			}else{
				type="";
			}
		    if(i==startDay){
		    	insertSpan="<span class='title "+color+" "+type+"'>"+scheduleTitle+"</span>";
		    	if(($(".jquery-calendar").find(".day_"+i).find("span").length)>0){
		    		firstIdx=$(".jquery-calendar").find(".day_"+i).find("span").length;
		    	}
		    }else{
		    	insertSpan="<span class='"+color+" "+type+"'></span>";
		    }
		    
		    thisIdx=$(".jquery-calendar").find(".day_"+i).find("span").length;

		    $(".jquery-calendar").find(".day_"+i).append(insertSpan);
		    
		    if(firstIdx!== thisIdx){
		    	var gap=firstIdx-thisIdx;

	    		$(".jquery-calendar").find(".day_"+i).find("span[class^='"+color+"']").css("top",((gap*8)+(gap*3))+"px")
		    }
		}
	}
	
	$.fn.calendar.defaults = {
		date: new Date(),
		picker: false,
	};

	$(window).load(function () {
		$('.jquery-calendar').each(function () {
			$(this).calendar();
		});
	});

}($));
