$(document).ready(function() {
    
    $("#theForm").ajaxSubmit({
        url: 'http://localhost:8080/home/testing', 
        type: 'post',
        success: SuccessOccur
    });
    function SuccessOccur(data, status, req) {
        if (status == "success") {
            alert(req.responseText);
            $('#text').replaceWith(req.responseText);
        }
    }
    });
//    $('#testbutton').click(function() { 
//        var xmlRequest = $.ajax({
//          url: "http://localhost:8080/home/testing",
//          processData: false,
//          data: "",
//          success: SuccessOccur,
//          error: Error
//        });
//        function SuccessOccur(data, status, req) {
//            if (status == "success") {
//                alert(req.responseText);
//                $('#text').replaceWith(req.responseText);
//            }
//        }
//        function Error(data, status, req) {
//            alert(req.responseText + status);
//        }
//    });

