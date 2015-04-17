var ctx1 = document.getElementById("eyeChartRead").getContext("2d");
var ctx2 = document.getElementById("eyeChartWrite").getContext("2d");
                var data1 = {
    labels: ["Voltage", "Timing", "Voltage", "Timing"],
    datasets: [
        {
            label: "Average",
            fillColor: "rgba(51, 122, 183,0.3)",
            strokeColor: "rgba(51, 122, 183,1)",
            pointColor: "rgba(51, 122, 183,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(51, 122, 183,1)",
            data: [11,8,16,9]
        },
        {
            label: "Minimum",
            fillColor: "rgba(237,29,65,0.2)",
            strokeColor: "rgba(237,29,65,0.6)",
            pointColor: "rgba(237,29,65,0.6)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(237,29,65,0.6)",
            data: [1,1,5,4]
        }
    ]
};
    var data2 = {
    labels: ["Voltage", "Timing", "Voltage", "Timing"],
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
            data: [5,3,2,6]
        }
    ]
};
    var option = {
    //Boolean - Whether to show lines for each scale point
    scaleShowLine : true,

    //Boolean - Whether we show the angle lines out of the radar
    angleShowLineOut : true,

    //Boolean - Whether to show labels on the scale
    scaleShowLabels : false,

    // Boolean - Whether the scale should begin at zero
    scaleBeginAtZero : true,

    //String - Colour of the angle line
    angleLineColor : "rgba(0,0,0,.1)",

    //Number - Pixel width of the angle line
    angleLineWidth : 1,

    //String - Point label font declaration
    pointLabelFontFamily : "'Arial'",

    //String - Point label font weight
    pointLabelFontStyle : "normal",

    //Number - Point label font size in pixels
    pointLabelFontSize : 15,

    //String - Point label font colour
    pointLabelFontColor : "#666",

    //Boolean - Whether to show a dot for each point
    pointDot : true,

    //Number - Radius of each point dot in pixels
    pointDotRadius : 3,

    //Number - Pixel width of point dot stroke
    pointDotStrokeWidth : 1,

    //Number - amount extra to add to the radius to cater for hit detection outside the drawn point
    pointHitDetectionRadius : 20,

    //Boolean - Whether to show a stroke for datasets
    datasetStroke : true,

    //Number - Pixel width of dataset stroke
    datasetStrokeWidth : 2,

    //Boolean - Whether to fill the dataset with a colour
    datasetFill : true


};
var read = new Chart(ctx1).Radar(data1,option);
var write = new Chart(ctx2).Radar(data2,option);