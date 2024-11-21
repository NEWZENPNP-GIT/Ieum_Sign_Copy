'use strict';

/* eslint-disable require-jsdoc */
/* eslint-env jquery */
/* global moment, tui, chance */
/* global findCalendar, CalendarList, ScheduleList, generateSchedule */

(function(window, Calendar) {
  var cal, resizeThrottled;
  var useCreationPopup = true;
  var useDetailPopup = true;
  var datePicker, selectedCalendar;

  cal = new Calendar('#calendar', {
    defaultView: 'month',
    useCreationPopup: useCreationPopup,
    useDetailPopup: useDetailPopup,
    calendars: CalendarList,
    template: {
      milestone: function(model) {
        return '<span class="calendar-font-icon ic-milestone-b"></span> <span style="background-color: ' + model.bgColor + '">' + model.title + '</span>';
      },
      allday: function(schedule) {
        return getTimeTemplate(schedule, true);
      },
      time: function(schedule) {
        return getTimeTemplate(schedule, false);
      }
    }
  });

  // event handlers
  cal.on({
    'clickSchedule': function(e) {
      console.log('clickSchedule', e);
    },
    'clickDayname': function(date) {
      console.log('clickDayname', date);
    },
    'beforeCreateSchedule': function(e) {
      console.log('beforeCreateSchedule', e);
      saveNewSchedule(e);


      //  2018.07.19 내용수정 및 추가 시작
      var data = {
        dateFrom:moment(e.start._date).format('YYYYMMDDHHmmss'),
        dateTo:moment(e.end._date).format('YYYYMMDDHHmmss'),
        scheduleType:e.calendarId,
        subject:e.title,
        comments:e.title,
        location:e.raw.location,
        openType: '',
      };

      if (e.state == '개인') {
        data.openType = '1';
      } else if (e.state == '기업') {
        data.openType = '3';
      }

      var url = rootPath+"schedule/insSchedule.do";

      $.ajax({
        url:url,
        crossDomain : true,
        dataType:"json",
        type:"POST",
        data: data,
        success:function(result) {
          console.log(result)
          if (viewType == 'D') {
            getCalendar({
              유형: viewType,
              기간From: moment(cal.getDate().getTime()).format('YYYYMMDD'),
              기간To:  moment(cal.getDate().getTime() ).format('YYYYMMDD')
            });
          } else if(viewType == 'M') {
            moveMonthCnt = 0;
            getCalendar({
              유형: viewType,
              기간From: moment().subtract(0, 'months').date(1).format('YYYYMMDD'),
              기간To: moment().subtract(- 1, 'months').date(0).format('YYYYMMDD')
            });
          } else if(viewType == 'W' || viewType == 'W2' || viewType == 'W3') {
            getCalendar({
              유형: viewType,
              기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
              기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
            });
          }
        },
      error:function(request,status,error){
        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      }
    });
  },
  'beforeUpdateSchedule': function(e) {
    console.log('beforeUpdateSchedule', e);
    // e.schedule.start = e.start;
    // e.schedule.end = e.end;
    // cal.updateSchedule(e.schedule.id, e.schedule.calendarId, e.schedule);

    var data = {
      dateFrom:moment(e.start._date).format('YYYYMMDDHHmmss'),
      dateTo:moment(e.end._date).format('YYYYMMDDHHmmss'),
      scheduleId:e.schedule.id,
      scheduleType:e.schedule.calendarId,
      openType:'',
      subject:e.schedule.title,
      comments:e.schedule.title,
      //location:e.schedule.raw.location,
    };

    if (e.schedule.state == '개인') {
      data.openType = '1';
    } else if (e.schedule.state == '기업') {
      data.openType = '3';
    }

    if (e.schedule.raw != null) {
      data.location = e.schedule.raw.location;
    } else {
      data.location = e.schedule.location;
    }
    console.log(data);

    var url = rootPath+"schedule/updSchedule.do";

    $.ajax({
      url:url,
      crossDomain : true,
      dataType:"json",
      type:"POST",
      data: data,
      success:function(result) {
        console.log(result)
        if (viewType == 'D') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDate().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDate().getTime() ).format('YYYYMMDD')
          });
        } else if(viewType == 'M') {
          moveMonthCnt = 0;
          getCalendar({
            유형: viewType,
            기간From: moment().subtract(0, 'months').date(1).format('YYYYMMDD'),
            기간To: moment().subtract(- 1, 'months').date(0).format('YYYYMMDD')
          });
        } else if(viewType == 'W' || viewType == 'W2' || viewType == 'W3') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
          });
        }
      },
      error:function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      }
    });
  },
  'beforeDeleteSchedule': function(e) {
    console.log('beforeDeleteSchedule', e);
    cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);

    var data = {
      scheduleId:e.schedule.id
    }

    var url = rootPath+"schedule/delSchedule.do";

    $.ajax({
      url:url,
      crossDomain : true,
      dataType:"json",
      type:"POST",
      data: data,
      success:function(result)
      {
        console.log(result)
        if (viewType == 'D') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDate().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDate().getTime() ).format('YYYYMMDD')
          });
        } else if(viewType == 'M') {
          moveMonthCnt = 0;
          getCalendar({
            유형: viewType,
            기간From: moment().subtract(0, 'months').date(1).format('YYYYMMDD'),
            기간To: moment().subtract(- 1, 'months').date(0).format('YYYYMMDD')
          });
        } else if(viewType == 'W' || viewType == 'W2' || viewType == 'W3') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
          });
        }
      },
      error:function(request,status,error){
            alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
      }
    });
  },
  'afterRenderSchedule': function(e) {
      var schedule = e.schedule;
      // var element = cal.getElement(schedule.id, schedule.calendarId);
      // console.log('afterRenderSchedule', element);
  }
});

/**
 * Get time template for time and all-day
 * @param {Schedule} schedule - schedule
 * @param {boolean} isAllDay - isAllDay or hasMultiDates
 * @returns {string}
 */
function getTimeTemplate(schedule, isAllDay) {
  var html = [];
  var start = moment(schedule.start.toUTCString());
  if (!isAllDay) {
    html.push('<strong>' + start.format('HH:mm') + '</strong> ');
  }
  if (schedule.isPrivate) {
    html.push('<span class="calendar-font-icon ic-lock-b"></span>');
    html.push(' Private');
  } else {
    if (schedule.isReadOnly) {
        html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
    } else if (schedule.recurrenceRule) {
        html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
    } else if (schedule.attendees.length) {
        html.push('<span class="calendar-font-icon ic-user-b"></span>');
    } else if (schedule.location) {
        html.push('<span class="calendar-font-icon ic-location-b"></span>');
    }
    html.push(' ' + schedule.title);
  }

  return html.join('');
}

/**
 * A listener for click the menu
 * @param {Event} e - click event
 */
//상단 조회 select box
function onClickMenu(e) {
  var target = $(e.target).closest('a[role="menuitem"]')[0];
  var action = getDataAction(target);
  var options = cal.getOptions();
  var viewName = '';

  moveMonthCnt = 0;

  console.log(target);
  console.log(action);
  switch (action) {
    case 'toggle-daily':
      viewName = 'day';
      viewType = 'D';
      break;
    case 'toggle-weekly':
      viewName = 'week';
      viewType = 'W';
      break;
    case 'toggle-monthly':
      options.month.visibleWeeksCount = 0;
      viewName = 'month';
      viewType = 'M';

      break;
    case 'toggle-weeks2':
      options.month.visibleWeeksCount = 2;
      viewName = 'month';
      viewType = 'W2';
      break;
    case 'toggle-weeks3':
      options.month.visibleWeeksCount = 3;
      viewName = 'month';
      viewType = 'W3';
      break;
    case 'toggle-narrow-weekend':
      options.month.narrowWeekend = !options.month.narrowWeekend;
      options.week.narrowWeekend = !options.week.narrowWeekend;
      viewName = cal.getViewName();

      target.querySelector('input').checked = options.month.narrowWeekend;
      break;
    case 'toggle-start-day-1':
      options.month.startDayOfWeek = options.month.startDayOfWeek ? 0 : 1;
      options.week.startDayOfWeek = options.week.startDayOfWeek ? 0 : 1;
      viewName = cal.getViewName();

      target.querySelector('input').checked = options.month.startDayOfWeek;
      break;
    case 'toggle-workweek':
      options.month.workweek = !options.month.workweek;
      options.week.workweek = !options.week.workweek;
      viewName = cal.getViewName();

      target.querySelector('input').checked = !options.month.workweek;
      break;
    default:
      break;
  }

  cal.setOptions(options, true);
  cal.changeView(viewName, true);

  setDropdownCalendarType();
  setRenderRangeText();
  setSchedules();
}

//calendar 목록조회
function getCalendar(opt, callback) {

  var url = rootPath+"schedule/getMainScheduleList.do";

  $.ajax({
    url:url,
    crossDomain : true,
    dataType:"json",
    type:"GET",
    data: {
      유형:opt.datatype,
      기간From: opt.기간From,
      기간To: opt.기간To,
    },
    success:function(rs) {
      console.log(rs);

      var paletteColor = ['#9e5fff', '#00a9ff', '#ff5583', '#03bd9e', '#bbdc00', '#df2428', '#13c2e1', '#3f19d1', '#c2f14f', '#c78d28'];

      ScheduleList = [];

      if (CalendarList.length == 0) {
        $.each(rs['캘린더'], function(i, v){
          var calendar = {
            id: v.commCode,
            name: v.commName,
            color: paletteColor[i],
            bgColor: paletteColor[i],
            dragBgColor: paletteColor[i],
            borderColor: paletteColor[i]
          };
          addCalendar(calendar);
        });
      }

      $.each(rs.data, function(i, v) {
        var colorIdx = 0;
        $.each(rs['캘린더'], function(ii, vv) {
          if (vv.commCode == v.scheduleType) {
              colorIdx = ii;
              return;
          }
        });

        var scheduleData = {
          id: v.scheduleId,
          calendarId: v.scheduleType,
          title: v.comments,
          category: 'time',
          dueDateClass: '',
          start: moment(v.dateFrom, 'YYYYMMDDHHmmss').format(),
          end: moment(v.dateTo, 'YYYYMMDDHHmmss').format(),
          isAllDay: v.dateFrom.substring(8,12) == '0000' && v.dateTo.substring(8,12) == '2359',
          location: v.location,
          // state: v.openType,
          bgColor: paletteColor[colorIdx],
          dragBgColor: paletteColor[colorIdx],
          borderColor: paletteColor[colorIdx],
          color: '#fff',
          raw: {
            creator: {
              name: v.empName !== undefined ? v.empName : '',
              id: v.userId !== undefined ? v.userId : '',
            }
          }
        };

        if (v.openType == '1') {
          scheduleData.state = '개인';
        } else if (v.openType == '3') {
          scheduleData.state = '기업';
        }

        scheduleData.state = '기업';

        ScheduleList.push(scheduleData);

      });

      cal.clear();
      cal.createSchedules(ScheduleList);

      if (callback != undefined) {
        callback()
      }

      $('.tui-full-calendar-checkbox-round').click();
      $('.tui-full-calendar-checkbox-round').click();

    },
    error:function(request,status,error){
      alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  });

}

//일정 조회 상단 클릭 이벤트
var moveMonthCnt = 0;
var viewType = 'M';
function onClickNavi(e) {
  var action = getDataAction(e.target);

  console.log(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
  switch (action) {
    case 'move-prev':
        cal.prev();
        if (viewType == 'D') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDate().getTime() ).format('YYYYMMDD'),
            기간To:  moment(cal.getDate().getTime()).format('YYYYMMDD')
          });
        }else if (viewType == 'M') {
          moveMonthCnt += 1;
          getCalendar({
            유형: viewType,
            기간From: moment().subtract(moveMonthCnt, 'months').date(1).format('YYYYMMDD'),
            기간To: moment().subtract(moveMonthCnt - 1, 'months').date(0).format('YYYYMMDD')
          });
        }else if(viewType == 'W' || viewType == 'W2' || viewType == 'W3') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
          });
        }
        break;
    case 'move-next':
        cal.next();
        if (viewType == 'D') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDate().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDate().getTime() ).format('YYYYMMDD')
          });
        }else if(viewType == 'M') {
          moveMonthCnt -= 1;
          getCalendar({
            유형: viewType,
            기간From: moment().subtract(moveMonthCnt, 'months').date(1).format('YYYYMMDD'),
            기간To: moment().subtract(moveMonthCnt - 1, 'months').date(0).format('YYYYMMDD')
          });
        }else if(viewType == 'W' || viewType == 'W2' || viewType == 'W3') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
          });
        }
        break;
    case 'move-today':
        cal.today();
        if (viewType == 'D') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDate().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDate().getTime() ).format('YYYYMMDD')
          });
        }else if(viewType == 'M') {
          moveMonthCnt = 0;
          getCalendar({
            유형: viewType,
            기간From: moment().subtract(moveMonthCnt, 'months').date(1).format('YYYYMMDD'),
            기간To: moment().subtract(moveMonthCnt - 1, 'months').date(0).format('YYYYMMDD')
          });
        }else if(viewType == 'W' || viewType == 'W2' || viewType == 'W3') {
          getCalendar({
            유형: viewType,
            기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
            기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
          });
        }
        break;
    default:
        return;
  }

  setRenderRangeText();
  setSchedules();
}

function onNewSchedule() {
  var title = $('#new-schedule-title').val();
  var location = $('#new-schedule-location').val();
  var isAllDay = document.getElementById('new-schedule-allday').checked;
  var start = datePicker.getStartDate();
  var end = datePicker.getEndDate();
  var calendar = selectedCalendar ? selectedCalendar : CalendarList[0];

  if (!title) {
    return;
  }

  cal.createSchedules([{
    id: String(chance.guid()),
    calendarId: calendar.id,
    title: title,
    isAllDay: isAllDay,
    start: start,
    end: end,
    category: isAllDay ? 'allday' : 'time',
    dueDateClass: '',
    color: calendar.color,
    bgColor: calendar.bgColor,
    dragBgColor: calendar.bgColor,
    borderColor: calendar.borderColor,
    raw: {
        location: location
    },
    state: calendar.state
  }]);

  $('#modal-new-schedule').modal('hide');
}

function onChangeNewScheduleCalendar(e) {
  var target = $(e.target).closest('a[role="menuitem"]')[0];
  var calendarId = getDataAction(target);
  changeNewScheduleCalendar(calendarId);
}

function changeNewScheduleCalendar(calendarId) {
  var calendarNameElement = document.getElementById('calendarName');
  var calendar = findCalendar(calendarId);
  var html = [];

  html.push('<span class="calendar-bar" style="background-color: ' + calendar.bgColor + '; border-color:' + calendar.borderColor + ';"></span>');
  html.push('<span class="calendar-name">' + calendar.name + '</span>');

  calendarNameElement.innerHTML = html.join('');

  selectedCalendar = calendar;
}

function createNewSchedule(event) {
  var start = event.start ? new Date(event.start.getTime()) : new Date();
  var end = event.end ? new Date(event.end.getTime()) : moment().add(1, 'hours').toDate();

  if (useCreationPopup) {
      cal.openCreationPopup({
          start: start,
          end: end
      });
  }
}
function saveNewSchedule(scheduleData) {
  var calendar = scheduleData.calendar || findCalendar(scheduleData.calendarId);
  var schedule = {
      id: String(chance.guid()),
      title: scheduleData.title,
      isAllDay: scheduleData.isAllDay,
      start: scheduleData.start,
      end: scheduleData.end,
      category: scheduleData.isAllDay ? 'allday' : 'time',
      dueDateClass: '',
      color: calendar.color,
      bgColor: calendar.bgColor,
      dragBgColor: calendar.bgColor,
      borderColor: calendar.borderColor,
      raw: {
          'class': scheduleData.raw['class'],
          location: scheduleData.raw.location
      },
      state: scheduleData.state
  };
  if (calendar) {
      schedule.calendarId = calendar.id;
      schedule.color = calendar.color;
      schedule.bgColor = calendar.bgColor;
      schedule.borderColor = calendar.borderColor;
  }

  cal.createSchedules([schedule]);

  refreshScheduleVisibility();
}

function onChangeCalendars(e) {
  var calendarId = e.target.value;
  var checked = e.target.checked;
  var viewAll = document.querySelector('.lnb-calendars-item input');
  var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
  var allCheckedCalendars = true;

  if (calendarId === 'all') {
      allCheckedCalendars = checked;

      calendarElements.forEach(function(input) {
          var span = input.parentNode;
          input.checked = checked;
          span.style.backgroundColor = checked ? span.style.borderColor : 'transparent';
      });

      CalendarList.forEach(function(calendar) {
          calendar.checked = checked;
      });
  } else {
      findCalendar(calendarId).checked = checked;

      allCheckedCalendars = calendarElements.every(function(input) {
          return input.checked;
      });

      if (allCheckedCalendars) {
          viewAll.checked = true;
      } else {
          viewAll.checked = false;
      }
  }

  refreshScheduleVisibility();
}

function refreshScheduleVisibility() {
  var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));

  CalendarList.forEach(function(calendar) {
      cal.toggleSchedules(calendar.id, !calendar.checked, false);
  });

  cal.render(true);

  calendarElements.forEach(function(input) {
      var span = input.nextElementSibling;
      span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
  });
}

function setDropdownCalendarType() {
  var calendarTypeName = document.getElementById('calendarTypeName');
  var calendarTypeIcon = document.getElementById('calendarTypeIcon');
  var options = cal.getOptions();
  var type = cal.getViewName();
  var iconClassName;

  if (type === 'day') {
      type = 'Daily';
      iconClassName = 'calendar-icon ic_view_day';
  } else if (type === 'week') {
      type = 'Weekly';
      iconClassName = 'calendar-icon ic_view_week';
  } else if (options.month.visibleWeeksCount === 2) {
      type = '2 weeks';
      iconClassName = 'calendar-icon ic_view_week';
  } else if (options.month.visibleWeeksCount === 3) {
      type = '3 weeks';
      iconClassName = 'calendar-icon ic_view_week';
  } else {
      type = 'Monthly';
      iconClassName = 'calendar-icon ic_view_month';
  }

  calendarTypeName.innerHTML = type;
  calendarTypeIcon.className = iconClassName;
}

function setRenderRangeText() {
  var renderRange = document.getElementById('renderRange');
  var options = cal.getOptions();
  var viewName = cal.getViewName();
  var html = [];
  if (viewName === 'day') {
      html.push(moment(cal.getDate().getTime()).format('YYYY.MM.DD'));
  } else if (viewName === 'month' &&
      (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
      html.push(moment(cal.getDate().getTime()).format('YYYY.MM'));
  } else {
      html.push(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
      html.push(' ~ ');
      html.push(moment(cal.getDateRangeEnd().getTime()).format(' MM.DD'));
  }
  renderRange.innerHTML = html.join('');
}

function setSchedules() {
  console.log(ScheduleList);
  cal.clear();
  // generateSchedule(cal.getViewName(), cal.getDateRangeStart(), cal.getDateRangeEnd());
  cal.createSchedules(ScheduleList);
  refreshScheduleVisibility();
}

function setEventListener() {
  $('#menu-navi').on('click', onClickNavi);
  $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
  $('#lnb-calendars').on('change', onChangeCalendars);

  $('#btn-save-schedule').on('click', onNewSchedule);
  $('#btn-new-schedule').on('click', createNewSchedule);

  $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);

  window.addEventListener('resize', resizeThrottled);

  // 위와 같은 방식으로 변경해서 정리 필요
  $('#calendar').on('click', '.tui-full-calendar-icon.tui-full-calendar-ic-checkbox', function(){
    var stIpt = $('#tui-full-calendar-schedule-start-date');
    var stDatetime = moment(stIpt.val(), 'YYYY-MM-DD HH:mm').format('YYYY-MM-DD') + ' 00:00';
    stIpt.val(stDatetime);
    var edIpt = $('#tui-full-calendar-schedule-end-date');
    var edDatetime = moment(edIpt.val(), 'YYYY-MM-DD HH:mm').format('YYYY-MM-DD') + ' 23:59';
    edIpt.val(edDatetime);
  });
}

function getDataAction(target) {
    return target.dataset ? target.dataset.action : target.getAttribute('data-action');
}

resizeThrottled = tui.util.throttle(function() {
  cal.render();
}, 50);

window.cal = cal;

setDropdownCalendarType();
setRenderRangeText();
setSchedules();
setEventListener();

getCalendar({
  유형: 'M',
  기간From: moment().startOf('month').format('YYYYMMDD'),
  기간To: moment().endOf('month').format('YYYYMMDD')
}, function(){
		var calendarList = document.getElementById('calendarList');
	    var html = [];
	    CalendarList.forEach(function(calendar) {
	        html.push('<div class="lnb-calendars-item"><label>' +
	            '<input type="checkbox" class="tui-full-calendar-checkbox-round" value="' + calendar.id + '" checked>' +
	            '<span style="border-color: ' + calendar.borderColor + '; background-color: ' + calendar.borderColor + ';"></span>' +
	            '<span>' + calendar.name + '</span>' +
	            '</label></div>'
	        );
	    });
	    calendarList.innerHTML = html.join('\n');
	});

})(window, tui.Calendar);


//'use strict';
//
///* eslint-disable require-jsdoc */
///* eslint-env jquery */
///* global moment, tui, chance */
///* global findCalendar, CalendarList, ScheduleList, generateSchedule */
//
//(function(window, Calendar) {
//    var cal, resizeThrottled;
//    var useCreationPopup = true;
//    var useDetailPopup = true;
//    var datePicker, selectedCalendar;
//
//    cal = new Calendar('#calendar', {
//        defaultView: 'month',
//        useCreationPopup: useCreationPopup,
//        useDetailPopup: useDetailPopup,
//        calendars: CalendarList,
//        template: {
//            milestone: function(model) {
//                return '<span class="calendar-font-icon ic-milestone-b"></span> <span style="background-color: ' + model.bgColor + '">' + model.title + '</span>';
//            },
//            allday: function(schedule) {
//                return getTimeTemplate(schedule, true);
//            },
//            time: function(schedule) {
//                return getTimeTemplate(schedule, false);
//            }
//        }
//    });
//
//    // event handlers
//    cal.on({
//        'clickSchedule': function(e) {
//            console.log('clickSchedule', e);
//        },
//        'clickDayname': function(date) {
//            console.log('clickDayname', date);
//        },
//        'beforeCreateSchedule': function(e) {
//            console.log('beforeCreateSchedule', e);
//            saveNewSchedule(e);
////  2018.07.19 내용수정 및 추가 시작
//            var data = {
//    			dateFrom:moment(e.start).format('YYYYMMDDHHmmss'),
//    			dateTo:moment(e.end).format('YYYYMMDDHHmmss'),
//    			scheduleType:e.calendarId,
//    			openType:'1',
//    			subject:e.title,
//    			comments:'',
//    			location:e.raw.location
//    		}
//
//            var url = rootPath+"schedule/insSchedule.do";
//
//    		$.ajax({
//    			url:url,
//    		    crossDomain : true,
//    			dataType:"json",
//    			type:"POST",
//    			data: data,
//    			success:function(result)
//    			{
//    				console.log(result)
//    				getCalendar({
//						유형: 'M',
//						기간From: moment(cal.getDate().getTime()).date(1).format('YYYYMMDD'),
//						기간To:  moment(cal.getDate().getTime()).endOf('month').format('YYYYMMDD')
//					});
//
//    			},
//    			error:function(request,status,error){
//    		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
//    			}
//    		});
//
//
////  2018.07.19 내용수정 및 추가 끝
//        },
//        'beforeUpdateSchedule': function(e) {
//            console.log('beforeUpdateSchedule', e);
//            e.schedule.start = e.start;
//            e.schedule.end = e.end;
//            cal.updateSchedule(e.schedule.id, e.schedule.calendarId, e.schedule);
// //  2018.07.19 내용수정 및 추가 시작
//            var data = {
//    			dateFrom:moment(e.start).format('YYYYMMDDHHmmss'),
//    			dateTo:moment(e.end).format('YYYYMMDDHHmmss'),
//    			scheduleType:e.calendarId,
//    			openType:'1',
//    			subject:e.title,
//    			comments:'',
//    			location:e.raw.location
//    		}
//
//            var url = rootPath+"schedule/updSchedule.do";
//
//    		$.ajax({
//    			url:url,
//    		    crossDomain : true,
//    			dataType:"json",
//    			type:"POST",
//    			data: data,
//    			success:function(result)
//    			{
//    				console.log(result)
//    				getCalendar({
//						유형: 'M',
//						기간From: moment(cal.getDate().getTime()).date(1).format('YYYYMMDD'),
//						기간To:  moment(cal.getDate().getTime()).endOf('month').format('YYYYMMDD')
//					});
//
//    			},
//    			error:function(request,status,error){
//    		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
//    			}
//    		});
////  2018.07.19 내용수정 및 추가 끝
//        },
//        'beforeDeleteSchedule': function(e) {
//            console.log('beforeDeleteSchedule', e);
//            cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);
//
////  2018.07.19 내용수정 및 추가   시작
//            var data = {
//            	scheduleId:e.schedule.id
//    		}
//
//            var url = rootPath+"schedule/delSchedule.do";
//
//    		$.ajax({
//    			url:url,
//    		    crossDomain : true,
//    			dataType:"json",
//    			type:"POST",
//    			data: data,
//    			success:function(result)
//    			{
//    				console.log(result)
//    				getCalendar({
//						유형: 'M',
//						기간From: moment(cal.getDate().getTime()).date(1).format('YYYYMMDD'),
//						기간To:  moment(cal.getDate().getTime()).endOf('month').format('YYYYMMDD')
//					});
//    			},
//    			error:function(request,status,error){
//    		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
//    			}
//    		});
////  2018.07.19 내용수정 및 추가  끝
//        },
//        'afterRenderSchedule': function(e) {
//            var schedule = e.schedule;
//            // var element = cal.getElement(schedule.id, schedule.calendarId);
//            // console.log('afterRenderSchedule', element);
//        }
//    });
//
//    /**
//     * Get time template for time and all-day
//     * @param {Schedule} schedule - schedule
//     * @param {boolean} isAllDay - isAllDay or hasMultiDates
//     * @returns {string}
//     */
//    function getTimeTemplate(schedule, isAllDay) {
//        var html = [];
//        var start = moment(schedule.start.toUTCString());
//        if (!isAllDay) {
//            html.push('<strong>' + start.format('HH:mm') + '</strong> ');
//        }
//        if (schedule.isPrivate) {
//            html.push('<span class="calendar-font-icon ic-lock-b"></span>');
//            html.push(' Private');
//        } else {
//            if (schedule.isReadOnly) {
//                html.push('<span class="calendar-font-icon ic-readonly-b"></span>');
//            } else if (schedule.recurrenceRule) {
//                html.push('<span class="calendar-font-icon ic-repeat-b"></span>');
//            } else if (schedule.attendees.length) {
//                html.push('<span class="calendar-font-icon ic-user-b"></span>');
//            } else if (schedule.location) {
//                html.push('<span class="calendar-font-icon ic-location-b"></span>');
//            }
//            html.push(' ' + schedule.title);
//        }
//
//        return html.join('');
//    }
//
//    /**
//     * A listener for click the menu
//     * @param {Event} e - click event
//     */
//    function onClickMenu(e) {
//        var target = $(e.target).closest('a[role="menuitem"]')[0];
//        var action = getDataAction(target);
//        var options = cal.getOptions();
//        var viewName = '';
//
//        moveMonthCnt = 0;
//
//        console.log(target);
//        console.log(action);
//        switch (action) {
//            case 'toggle-daily':
//                viewName = 'day';
//                viewType = 'D';
//                break;
//            case 'toggle-weekly':
//                viewName = 'week';
//                viewType = 'W';
//                break;
//            case 'toggle-monthly':
//                options.month.visibleWeeksCount = 0;
//                viewName = 'month';
//                viewType = 'M';
//                break;
//            case 'toggle-weeks2':
//                options.month.visibleWeeksCount = 2;
//                viewName = 'month';
//                viewType = 'M';
//                break;
//            case 'toggle-weeks3':
//                options.month.visibleWeeksCount = 3;
//                viewName = 'month';
//                viewType = 'M';
//                break;
//            case 'toggle-narrow-weekend':
//                options.month.narrowWeekend = !options.month.narrowWeekend;
//                options.week.narrowWeekend = !options.week.narrowWeekend;
//                viewName = cal.getViewName();
//
//                target.querySelector('input').checked = options.month.narrowWeekend;
//                break;
//            case 'toggle-start-day-1':
//                options.month.startDayOfWeek = options.month.startDayOfWeek ? 0 : 1;
//                options.week.startDayOfWeek = options.week.startDayOfWeek ? 0 : 1;
//                viewName = cal.getViewName();
//
//                target.querySelector('input').checked = options.month.startDayOfWeek;
//                break;
//            case 'toggle-workweek':
//                options.month.workweek = !options.month.workweek;
//                options.week.workweek = !options.week.workweek;
//                viewName = cal.getViewName();
//
//                target.querySelector('input').checked = !options.month.workweek;
//                break;
//            default:
//                break;
//        }
//
//        cal.setOptions(options, true);
//        cal.changeView(viewName, true);
//
//        setDropdownCalendarType();
//        setRenderRangeText();
//        setSchedules();
//    }
//
////  2018.07.19 내용수정 및 추가  시작
//
//	//calendar 목록조회
//	function getCalendar(opt, callback) {
//
//		var url = rootPath+"schedule/getMainScheduleList.do";
//
//   		$.ajax({
//   			url:url,
//   		    crossDomain : true,
//   			dataType:"json",
//   			type:"GET",
//   			data: {
//   				유형:'D',
//				기간From:'20180601',
//				기간To:'20180630'
//   			},
//   			success:function(rs)
//   			{
//   				console.log(rs);
//
//   				var ScheduleList = [];
//
//   				$.each(rs.data, function(i, v) {
//   					ScheduleList.push({
//					    id: v.scheduleId,
//					    calendarId: v.scheduleType,
//					    title: v.comments,
//					    category: 'time',
//					    dueDateClass: '',
//					    start: moment(v.dateFrom, 'YYYYMMDDHHmmss').format(),
//					    end: moment(v.dateTo, 'YYYYMMDDHHmmss').format(),
//					    isAllDay:'',
//					    location: v.location,
//					    state: v.commName,
//					    bgColor:'#00a9ff',
//					    borderColor:'#00a9ff',
//					    color: '#fff'
//					});
//   				});
//
//   				var paletteColor = ['#9e5fff', '#00a9ff', '#ff5583', '#03bd9e', '#bbdc00'];
//
//   				$.each(rs['캘린더'], function(i, v){
//   					var calendar = {
//   	   		          id: v.commCode,
//   	   		          name: v.commName,
//   	   		          color: paletteColor[i],
//   	   		          bgColor: paletteColor[i],
//   	   		          dragBgColor: paletteColor[i],
//   	   		          borderColor: paletteColor[i]
//   	   		        };
//   	   		        addCalendar(calendar);
//   				});
//
//   				cal.clear();
//  			    cal.createSchedules(ScheduleList);
//
//  			    if (callback != undefined) {
//  			    	callback()
//  			    }
//
//   			},
//   			error:function(request,status,error){
//   		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
//   			}
//   		});
//
//	}
//
//	//일정 조회 상단 클릭 이벤트
//    var moveMonthCnt = 0;
//    var viewType = 'M';
//    function onClickNavi(e) {
//        var action = getDataAction(e.target);
//
//        switch (action) {
//            case 'move-prev':
//                cal.prev();
//                if (viewType == 'D') {
//                	getCalendar({
//                		유형: viewType,
//                		기간From: moment(cal.getDate().getTime() ).format('YYYYMMDD'),
//                		기간To:  moment(cal.getDate().getTime()).format('YYYYMMDD')
//                	});
//                }else if (viewType == 'W') {
//                	getCalendar({
//                		유형: viewType,
//                		기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
//                		기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
//                	});
//                }else if (viewType == 'M') {
//                	moveMonthCnt += 1;
//                	getCalendar({
//                		유형: viewType,
//                		기간From: moment().subtract(moveMonthCnt, 'months').date(1).format('YYYYMMDD'),
//                		기간To: moment().subtract(moveMonthCnt - 1, 'months').date(0).format('YYYYMMDD')
//                	});
//                }
//                break;
//            case 'move-next':
//                cal.next();
//                if (viewType == 'D') {
//                	getCalendar({
//                		유형: viewType,
//                		기간From: moment(cal.getDate().getTime()).format('YYYYMMDD'),
//                		기간To:  moment(cal.getDate().getTime()).format('YYYYMMDD')
//                	});
//                }else if(viewType == 'W') {
//                	getCalendar({
//                		유형: viewType,
//                		기간From: moment(cal.getDateRangeStart().getTime()).format('YYYYMMDD'),
//                		기간To:  moment(cal.getDateRangeEnd().getTime()).format('YYYYMMDD')
//                	});
//                }else if(viewType == 'M') {
//                	moveMonthCnt -= 1;
//                	getCalendar({
//                		유형: viewType,
//                		기간From: moment().subtract(moveMonthCnt, 'months').date(1).format('YYYYMMDD'),
//                		기간To: moment().subtract(moveMonthCnt - 1, 'months').date(0).format('YYYYMMDD')
//                	});
//                }
//                break;
//            case 'move-today':
//                cal.today();
//                moveMonthCnt = 0;
//                break;
//            default:
//                return;
//        }
//
//        setRenderRangeText();
//        setSchedules();
//    }
////  2018.07.19 내용수정 및 추가  끝
//
//    function onNewSchedule() {
//        var title = $('#new-schedule-title').val();
//        var location = $('#new-schedule-location').val();
//        var isAllDay = document.getElementById('new-schedule-allday').checked;
//        var start = datePicker.getStartDate();
//        var end = datePicker.getEndDate();
//        var calendar = selectedCalendar ? selectedCalendar : CalendarList[0];
//
//        if (!title) {
//            return;
//        }
//
//        cal.createSchedules([{
//            id: String(chance.guid()),
//            calendarId: calendar.id,
//            title: title,
//            isAllDay: isAllDay,
//            start: start,
//            end: end,
//            category: isAllDay ? 'allday' : 'time',
//            dueDateClass: '',
//            color: calendar.color,
//            bgColor: calendar.bgColor,
//            dragBgColor: calendar.bgColor,
//            borderColor: calendar.borderColor,
//            raw: {
//                location: location
//            },
//            state: 'Busy'
//        }]);
//
//        $('#modal-new-schedule').modal('hide');
//    }
//
//    function onChangeNewScheduleCalendar(e) {
//        var target = $(e.target).closest('a[role="menuitem"]')[0];
//        var calendarId = getDataAction(target);
//        changeNewScheduleCalendar(calendarId);
//    }
//
//    function changeNewScheduleCalendar(calendarId) {
//        var calendarNameElement = document.getElementById('calendarName');
//        var calendar = findCalendar(calendarId);
//        var html = [];
//
//        html.push('<span class="calendar-bar" style="background-color: ' + calendar.bgColor + '; border-color:' + calendar.borderColor + ';"></span>');
//        html.push('<span class="calendar-name">' + calendar.name + '</span>');
//
//        calendarNameElement.innerHTML = html.join('');
//
//        selectedCalendar = calendar;
//    }
//
//    function createNewSchedule(event) {
//        var start = event.start ? new Date(event.start.getTime()) : new Date();
//        var end = event.end ? new Date(event.end.getTime()) : moment().add(1, 'hours').toDate();
//
//        if (useCreationPopup) {
//            cal.openCreationPopup({
//                start: start,
//                end: end
//            });
//        }
//    }
//    function saveNewSchedule(scheduleData) {
//        var calendar = scheduleData.calendar || findCalendar(scheduleData.calendarId);
//        var schedule = {
//            id: String(chance.guid()),
//            title: scheduleData.title,
//            isAllDay: scheduleData.isAllDay,
//            start: scheduleData.start,
//            end: scheduleData.end,
//            category: scheduleData.isAllDay ? 'allday' : 'time',
//            dueDateClass: '',
//            color: calendar.color,
//            bgColor: calendar.bgColor,
//            dragBgColor: calendar.bgColor,
//            borderColor: calendar.borderColor,
//            raw: {
//                'class': scheduleData.raw['class'],
//                location: scheduleData.raw.location
//            },
//            state: scheduleData.state
//        };
//        if (calendar) {
//            schedule.calendarId = calendar.id;
//            schedule.color = calendar.color;
//            schedule.bgColor = calendar.bgColor;
//            schedule.borderColor = calendar.borderColor;
//        }
//
//        cal.createSchedules([schedule]);
//
//        refreshScheduleVisibility();
//    }
//
//    function onChangeCalendars(e) {
//        var calendarId = e.target.value;
//        var checked = e.target.checked;
//        var viewAll = document.querySelector('.lnb-calendars-item input');
//        var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
//        var allCheckedCalendars = true;
//
//        if (calendarId === 'all') {
//            allCheckedCalendars = checked;
//
//            calendarElements.forEach(function(input) {
//                var span = input.parentNode;
//                input.checked = checked;
//                span.style.backgroundColor = checked ? span.style.borderColor : 'transparent';
//            });
//
//            CalendarList.forEach(function(calendar) {
//                calendar.checked = checked;
//            });
//        } else {
//            findCalendar(calendarId).checked = checked;
//
//            allCheckedCalendars = calendarElements.every(function(input) {
//                return input.checked;
//            });
//
//            if (allCheckedCalendars) {
//                viewAll.checked = true;
//            } else {
//                viewAll.checked = false;
//            }
//        }
//
//        refreshScheduleVisibility();
//    }
//
//    function refreshScheduleVisibility() {
//        var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));
//
//        CalendarList.forEach(function(calendar) {
//            cal.toggleSchedules(calendar.id, !calendar.checked, false);
//        });
//
//        cal.render(true);
//
//        calendarElements.forEach(function(input) {
//            var span = input.nextElementSibling;
//            span.style.backgroundColor = input.checked ? span.style.borderColor : 'transparent';
//        });
//    }
//
//    function setDropdownCalendarType() {
//        var calendarTypeName = document.getElementById('calendarTypeName');
//        var calendarTypeIcon = document.getElementById('calendarTypeIcon');
//        var options = cal.getOptions();
//        var type = cal.getViewName();
//        var iconClassName;
//
//        if (type === 'day') {
//            type = 'Daily';
//            iconClassName = 'calendar-icon ic_view_day';
//        } else if (type === 'week') {
//            type = 'Weekly';
//            iconClassName = 'calendar-icon ic_view_week';
//        } else if (options.month.visibleWeeksCount === 2) {
//            type = '2 weeks';
//            iconClassName = 'calendar-icon ic_view_week';
//        } else if (options.month.visibleWeeksCount === 3) {
//            type = '3 weeks';
//            iconClassName = 'calendar-icon ic_view_week';
//        } else {
//            type = 'Monthly';
//            iconClassName = 'calendar-icon ic_view_month';
//        }
//
//        calendarTypeName.innerHTML = type;
//        calendarTypeIcon.className = iconClassName;
//    }
//
//    function setRenderRangeText() {
//        var renderRange = document.getElementById('renderRange');
//        var options = cal.getOptions();
//        var viewName = cal.getViewName();
//        var html = [];
//        if (viewName === 'day') {
//            html.push(moment(cal.getDate().getTime()).format('YYYY.MM.DD'));
//        } else if (viewName === 'month' &&
//            (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
//            html.push(moment(cal.getDate().getTime()).format('YYYY.MM'));
//        } else {
//            html.push(moment(cal.getDateRangeStart().getTime()).format('YYYY.MM.DD'));
//            html.push(' ~ ');
//            html.push(moment(cal.getDateRangeEnd().getTime()).format(' MM.DD'));
//        }
//        renderRange.innerHTML = html.join('');
//    }
//
////  2018.07.19 내용수정 및 추가 시작
//    function setSchedules() {
//        cal.clear();
////        generateSchedule(cal.getViewName(), cal.getDateRangeStart(), cal.getDateRangeEnd());
//        cal.createSchedules(ScheduleList);
//        refreshScheduleVisibility();
//    }
////  2018.07.19 내용수정 및 추가 끝
//
//    function setEventListener() {
//        $('#menu-navi').on('click', onClickNavi);
//        $('.dropdown-menu a[role="menuitem"]').on('click', onClickMenu);
//        $('#lnb-calendars').on('change', onChangeCalendars);
//
//        $('#btn-save-schedule').on('click', onNewSchedule);
//        $('#btn-new-schedule').on('click', createNewSchedule);
//
//        $('#dropdownMenu-calendars-list').on('click', onChangeNewScheduleCalendar);
//
//        window.addEventListener('resize', resizeThrottled);
//    }
//
//    function getDataAction(target) {
//        return target.dataset ? target.dataset.action : target.getAttribute('data-action');
//    }
//
//    resizeThrottled = tui.util.throttle(function() {
//        cal.render();
//    }, 50);
//
//    window.cal = cal;
//
//    setDropdownCalendarType();
//    setRenderRangeText();
//    setSchedules();
//    setEventListener();
//
//
//
//    getCalendar({
//		유형: 'M',
//		기간From: moment().startOf('month').format('YYYYMMDD'),
//		기간To: moment().endOf('month').format('YYYYMMDD')
//	}, function(){
//		var calendarList = document.getElementById('calendarList');
//	    var html = [];
//	    CalendarList.forEach(function(calendar) {
//	        html.push('<div class="lnb-calendars-item"><label>' +
//	            '<input type="checkbox" class="tui-full-calendar-checkbox-round" value="' + calendar.id + '" checked>' +
//	            '<span style="border-color: ' + calendar.borderColor + '; background-color: ' + calendar.borderColor + ';"></span>' +
//	            '<span>' + calendar.name + '</span>' +
//	            '</label></div>'
//	        );
//	    });
//	    calendarList.innerHTML = html.join('\n');
//	});
//
//})(window, tui.Calendar);
