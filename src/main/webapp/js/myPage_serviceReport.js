/* ====  서비스 이용현황 차트  ==== */
    var data;
    var opt;
    var chartW, chartH; 

/**  new **/
google.charts.load('current', {packages:['corechart'], callback: pie_drawChart2});
google.charts.setOnLoadCallback(pie_drawChart2);
function pie_drawChart2() {

	data = new google.visualization.DataTable();
	data.addColumn('string', '서비스이용 현황');
	data.addColumn('number', '사용수');
	data.addRows([
		['전자계약서 생성', 50],
		['전자계약서 전송', 30],
		['전자계약서 완료 ', 15],
		['전자계약서 정정요청', 5],
	]);
	
	chartW=$("#piechart").innerWidth();
    charth=$("#piechart").innerHeight();
    
	opt = {
		'width': chartW,
		'height': charth,
		pieSliceText: 'label',
		legend: 'none',
		pieStartAngle: 100,
		chartArea: {width: chartW, height: charth-15},
		colors: ['#2cbaff', '#0071ff', '#2d97ee', '#89caff'],
		backgroundColor: { fill: "#e4eef4" }
	};
	
	var chart = new google.visualization.PieChart(document.getElementById('piechart'));
	chart.draw(data, opt);
	chart_legend();
}

function chart_legend(){
    var column=['전자계약서 생성','전자계약서 전송','전자계약서 완료','전자계약서 정정요청']
    $("#legend").empty();
    for(var i in opt.colors){
        $("#legend").append('<span class="label"></span><label>'+column[i]+'</label>');
        $("#legend").find("span").eq(i).css("background-color",opt.colors[i]);
    }

    var margin=Math.floor((chartW/opt.colors.length)/2-($("#legend").find("span").innerWidth()/2));
    $("#legend").find("span").css("margin-left",margin+"px");
}
$(window).resize(function() {
	pie_drawChart2();
})