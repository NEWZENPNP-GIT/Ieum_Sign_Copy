function memorialDay(name, month, day){
    this.name = name;
    this.month = month;
    this.day = day;
}

/**
 * 2018 년도 토, 일요일을 제외한 법정공휴일 
 */
var memorialDays = Array (
    new memorialDay("신정", 1, 1),
    new memorialDay("설날", 1, 1),
    new memorialDay("3·1절", 3, 1),
    new memorialDay("식목일", 4, 5),
    new memorialDay("석가탄신일", 4, 8),
    new memorialDay("어린이날", 5, 5),
    new memorialDay("현충일", 6, 6),
    new memorialDay("제헌절", 7, 17),
    new memorialDay("광복절", 8, 15),
    new memorialDay("추석", 9, 24),
    new memorialDay("추석", 9, 25),
    new memorialDay("추석", 9, 26),
    new memorialDay("개천절", 10, 3),
    new memorialDay("한글날", 10, 9),
    new memorialDay("성탄절", 12, 25)
   );
   
/**
 * 법정공휴일 여부 확인
 */   
function memorialDayCheck(month, day) {
    var memorial = 0;
    for (var i = 0; i < memorialDays.length; i++) {
        if (memorialDays[i].month == month && memorialDays[i].day == day){
            return true;
        }        
    }
    return false;
}

/**
 * 휴일을 제외한 일수 구한다.
 * @param {*} dateFrom 
 * @param {*} dateTo 
 * @param {*} char 
 * 
 */
function calcDate ( dateFrom, dateTo, char) {

    var dateFromArr = dateFrom.split(char);
    var dateToArr = dateTo.split(char);
    var date1 = new Date(dateFromArr[0], dateFromArr[1]-1 , dateFromArr[2]); // 2017-11-30
    var date2 = new Date(dateToArr[0], dateToArr[1]-1 , dateToArr[2]); // 2017-12-6
    var count = 0;
    var memorialCount = 0;
    
    while(true) {  
        var temp_date = date1;
        
        if(temp_date.getTime() > date2.getTime()) {
            //console.log("count : " + count);
            break;
        } else {
            var tmp = temp_date.getDay();
            if(tmp == 0 || tmp == 6) {
                // 주말
                //console.log("주말");
            } else {
                // 평일
                //console.log("평일");
                if(!memorialDayCheck(temp_date.getMonth()+1, temp_date.getDate())) {
                    count++;   
                }      
            }
            temp_date.setDate(date1.getDate() + 1); 
        }
    }
    return count;
}
