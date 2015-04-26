$(document).ready(function() {
	//combination chart
	function drawCombination(){
	var data = [
		{
			label: 'Health, Trust',
			strokeColor: '#337ab7',
			data: [
				{ x: 32, y: 91 }, { x: 55, y: 80 }, { x: 50, y: 88 },
				{ x: 80, y: 80 }, { x: 86, y: 90 }, { x: 95, y: 90 }
			]
		},
		{
      			label: '', //fix the y-scale from 50-100
      			strokeColor: 'rgba(255, 255, 255, 0)',
      			pointColor: 'rgba(255, 255, 255, 0)',
      			pointStrokeColor: 'rgba(255, 255, 255, 0)',
      			data: [
        			{ x: 0, y: 50 }, { x: 100, y: 100 }, 
      			]		
    		}
				
	];

	var ctx = document.getElementById("combinationChart").getContext("2d");
	var myCombination = new Chart(ctx).Scatter(data, {
		scaleGridLineColor: "rgba(175, 175, 175, 0.5)",
		scaleLineColor: "rgba(175, 175, 175, 0.5)",
		bezierCurve: true,
		scaleShowGridLines: true,

	});
	}
	
	//read and write eye chart
	function drawEye(){
		var ctx1 = document.getElementById("eyeChartRead").getContext("2d");
                var ctx2 = document.getElementById("eyeChartWrite").getContext("2d");
	

                var data1 = {
                labels: ["TxVoltage", "TxTiming", "TxVoltage", "TxTiming"],
                datasets: [
                {
                    label: "Average",
                    fillColor: "rgba(51, 122, 183,0.3)",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: [10,9,10,12]
                },
                {
                    label: "Minimum",
                    fillColor: "rgba(237,29,65,0.2)",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: [7,6,7,10]
                }
                ]
                };
                var data2 = {
                labels: ["RxVoltage", "RxTiming", "RxVoltage", "RxTiming"],
                datasets: [
                {
                    label: "Average",
                    fillColor: "rgba(51, 122, 183,0.3)",
                    strokeColor: "rgba(51, 122, 183,1)",
                    pointColor: "rgba(51, 122, 183,1)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(51, 122, 183,1)",
                    data: [9,10,12,11]
                },
                {
                    label: "Minimum",
                    fillColor: "rgba(237,29,65,0.2)",
                    strokeColor: "rgba(237,29,65,0.6)",
                    pointColor: "rgba(237,29,65,0.6)",
                    pointStrokeColor: "#fff",
                    pointHighlightFill: "#fff",
                    pointHighlightStroke: "rgba(237,29,65,0.6)",
                    data: [6,8,10,7]
                }
                ]
                };
                var option = {
                    //Number - Point label font size in pixels
                    pointLabelFontSize : 12,
                };
		
                var read = new Chart(ctx1).Radar(data1,option);
                var write = new Chart(ctx2).Radar(data2,option);
	}


	//switch the chart
	$("#changeEye").click(function(){
		$("<div class='row' id='eyeChart'><div class='col-md-6 center-block'><div class='eyeTitle'>Read</div><canvas id='eyeChartRead' class='center-block'></canvas></div><div class='col-md-6 center-block'><div class='eyeTitle'>Write</div><canvas id='eyeChartWrite' class='center-block'></canvas></div></div>").appendTo("#charts");
		drawEye();
		
		//remove other charts
		$("#linechart_material").hide();
		$("#combination").remove();

		//change title
		var title = $(this).children().children().html();
		$("#chart_header").html(title+"<span class='caret'></span>");
		
		//list control
		$(this).hide();
		$("#changeIncrement").show();
		$("#changeCombination").show();
		
	});

	$("#changeCombination").click(function(){
		$("<div id='combination'><canvas id='combinationChart'></canvas></div>").appendTo("#charts");
		drawCombination();

		$("#linechart_material").hide();
		$("#eyeChart").remove();

		var title = $(this).children().children().html();
		$("#chart_header").html(title+"<span class='caret'></span>");

		$(this).hide();
		$("#changeIncrement").show();
		$("#changeEye").show();
	});
	
	
	$("#changeIncrement").click(function(){
		$("#linechart_material").show();
		
		$("#eyeChart").remove();
		$("#combination").remove();

		var title = $(this).children().children().html();
		$("#chart_header").html(title+"<span class='caret'></span>");

		$(this).hide();
		$("#changeEye").show();
		$("#changeCombination").show();
	});
});