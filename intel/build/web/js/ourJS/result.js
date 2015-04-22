$(document).ready(function() {
            //show the chart
            $(".changeChart").click(function(){
                var chart = $(this).attr("chart");
                var title = $(this).children().children().html();
                
                $("#eyeChart").hide();
                $("#combination").hide();
                 $("#linechart_material").hide();
                 $(chart).show();
                $("#chart_header").html(title+"<span class=\"caret\"></span>");
            });
            
            var g = new JustGage({
                  id: "trust",
                  value: 32,
                  min: 0,
                  max: 100,
                  title: "Trust",
                  label: "SCORE",
                  levelColors: ["D00000","00CC33"],
                  startAnimationTime : 2000
                });
                var g2 = new JustGage({
                  id: "health",
                  value: 91,
                  min: 0,
                  max: 100,
                  title: "Health",
                  label: "SCORE",
                  levelColors: ["D00000","00CC33"],
                  startAnimationTime : 2000
                });
});