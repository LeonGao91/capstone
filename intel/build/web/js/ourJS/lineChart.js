/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
google.load('visualization', '1.1', {packages: ['line']});
google.setOnLoadCallback(drawChart);

function drawChart() {

    var data = new google.visualization.DataTable();
    data.addColumn('number', 'Date');
    data.addColumn('number', 'Health');
    data.addColumn('number', 'Trust');

    data.addRows([
        [1, 37, 34],
        [2, 30, 12],
        [3, 25, 54],
        [4, 11, 14],
        [5, 11, 19],
        [6, 8, 12]
    ]);

    var options = {
        chart: {
        },
        width: 700,
        height: 300
    };

    var chart = new google.charts.Line(document.getElementById('linechart_material'));

    chart.draw(data, options);
}