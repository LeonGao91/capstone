$(document).ready(function() {
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
});