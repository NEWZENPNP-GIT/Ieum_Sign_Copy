/*var ctx ;
var barChartData;

function setChartData(){
     chart DATA INSERT
    barChartData = {
        labels : ["1","2","3","4","5","6","7","8","9","10","11","12"],
        datasets : [
            {
                fillColor : "rgba(214,239,129,1)",
                strokeColor : "rgba(214,239,129,0)",
                highlightFill: "rgba(214,239,129,0)",
                highlightStroke: "rgba(214,239,129,0)",
                //data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
                data : [50,60,70,80,90,180,90,80,70,60,10,20]
            },
            {
                fillColor : "rgba(93,158,218,1)",
                strokeColor : "rgba(93,158,218,0)",
                highlightFill : "rgba(93,158,218,1)",
                highlightStroke : "rgba(93,158,218,1)",
                //data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
                data : [50,60,70,80,90,200,90,80,70,60,10,20]
            }
        ]

    }
}

$(window).load(function(){

	var ctx = document.getElementById("canvas").getContext("2d");
    window.myBar = new Chart(ctx).Bar(barChartData, {
        responsive : true
    });
});

$(window).resize(function() {
	setChartData();
});

setChartData();
*/

/** 구글 차트 **/
var data;
var options;
var chart;
google.charts.load('current', {'packages':['corechart','bar']});
google.charts.setOnLoadCallback(bar_drawChart);

function bar_drawChart() {
  if ($("#barchart").length == 0) return false;


  var url = rootPath+"statistics/getMainGraphist.do";
  var chartData = [
    ['', '전자문서 생성', '사업주 서명', '전자문서 전송', '계약지연(1주일이상)', '계약 완료']
  ];
  var pushData;
  $.ajax({
    url:url,
      crossDomain : true,
    dataType:"json",
    type:"GET",
    success:function(result)
    {
      $.each(result.data, function(i,v){
        pushData = [v.viewMonth + '월', v.v1, v.v2, v.v3, v.v4, v.v5];
        chartData.push(pushData);
      });
      drawChart();
    },
    error:function(request,status,error){
          alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
    }
  }).responseText;

  // var dummyData = [
	//   ['', '계약서 생성', '사업주 서명', '계약서 전송', '계약지연(1주일이상)', '계약 완료'],
	//   ['1월', 100, 80, 20, 30, 45],
	//   ['2월', 100, 80, 20, 30, 45],
	//   ['3월', 80, 80, 20, 30, 45],
	//   ['4월', 100, 80, 20, 30, 45],
	//   ['5월', 20, 80, 20, 30, 45],
	//   ['6월', 45, 80, 20, 30, 45],
	//   ['7월', 100, 80, 20, 30, 45],
	//   ['8월', 45, 80, 20, 30, 45],
	//   ['9월', 100, 80, 20, 30, 45],
	//   ['10월', 30, 20, 20, 30, 45],
	//   ['11월', 20, 80, 20, 30, 45],
	//   ['12월', 100, 80, 20, 30, 45],
  // ]

  function drawChart() { // draw graph
    var data = google.visualization.arrayToDataTable(chartData);

    var chartW=$("#barchart").innerWidth();
    var charth=$("#barchart").innerHeight();

    var options = {
      bar : {
        groupHeight: '100%'
      },
      chart: {},
      chartArea: {
        width: '90%'
      },
      bars: 'horizontal',
      // witdth: '100%',
      legend: {
        position:'top',
        alignment: 'end',
        textStyle: {
          fontSize: 12,
          bold: true,
          color: '#333333'
        }
      },
      hAxis: {
        textStyle: {
          fonsSize: 12,
          color: '#aeb8c3'
        }
      },
      vAxis: {
        minValue: 0,
        baselineColor: '#fff',
        textStyle: {
          fonsSize: 12,
          color: '#aeb8c3'
        }
      },
      isStacked: true,
      colors: ['#2b2f48','#0f1f97','#0a66d9','#2d97ee','#89caff']
    };

    chart = new google.visualization.ColumnChart(document.getElementById('barchart'));
    chart.draw(data, google.charts.Bar.convertOptions(options));

  }

}

$(window).resize(function() {
  bar_drawChart();
})
