/* ====  서비스 이용현황 차트  ==== */
var data;
var opt;
var chartW, chartH; 

google.charts.load('current', {packages:['corechart'], callback: pie_drawChart});
google.charts.setOnLoadCallback(pie_drawChart);

function pie_drawChart() {

	data = new google.visualization.DataTable();
	data.addColumn('string', '서비스이용 현황');
	data.addColumn('number', '사용수');
	for (var i in chartColumn){
		data.addRows([
			[chartColumn[i],chartValue[i]],
		]);
	}
	
	/*chartW=$(".chart_wrap").find(".con").innerWidth();
    charth=$(".chart_wrap").find(".con").innerHeight()*/
    //-(parseInt($("#piechart").css("padding-top").replace(/[^-\d\.]/g, ''))+15);
	
	chartW=chartWrap.innerWidth();
    charth=chartWrap.innerHeight();

    opt = {
		'width': chartW,
		'height': charth,
		pieSliceText: 'label',
		legend: 'none',
		pieStartAngle: 100,
		chartArea: {width: chartW, height: charth-20},
		colors: ['#2cbaff', '#0071ff', '#2d97ee', '#89caff'],
		backgroundColor: { fill: "#e4eef4" }
	};
	
	var chart = new google.visualization.PieChart(document.getElementById('piechart'));
	chart.draw(data, opt);
	chart_legend();
}

function chart_legend(){
	legendWrap.empty();
    for(var i in opt.colors){
    	legendWrap.append('<div class="chartlabel"><span class="rect"></span><span>'+chartColumn[i]+'</span></div>');
    	legendWrap.find(".chartlabel").eq(i).find(".rect").css("background-color",opt.colors[i]);
    }

    //var margin=Math.floor((chartW/opt.colors.length)/2-($("#legend").find("span").innerWidth()/2));
    //legendWrap.find("span").css("margin-left",margin+"px");
}
$(window).resize(function() {
	pie_drawChart();
})