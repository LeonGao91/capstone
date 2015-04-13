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
        [6, 8, 16],
        [7, 2, 19],
        [8, 4, 2],
        [9, 24, 66],
        [10, 23, 32],
        [11, 37, 34],
        [12, 30, 12],
        [13, 25, 54],
        [14, 11, 14],
        [15, 11, 19],
        [16, 8, 16],
        [17, 2, 19],
        [18, 4, 2],
        [19, 24, 66],
        [20, 23, 32],
        [21, 37, 34],
        [22, 30, 12],
        [23, 25, 54],
        [24, 11, 14],
        [25, 11, 19]
    ]);

    var options = {
        chart: {
        },
        width: $('.main_panel').width() * 0.9,
        height: 300
    };

    var chart = new google.charts.Line(document.getElementById('linechart_material'));

    chart.draw(data, options);
}