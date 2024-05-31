console.log("This is script file");


const toggleSidebar = () => {
    if ($(".sidebar").is(":visible")){
        //true
        $(".sidebar").css("display","none");
        $(".content").css("margin-left","0%");
    } else{
        //false
        $(".sidebar").css("display","block");
        $(".content").css("margin-left","20%");
    }
};

const closeAlert = () => {
    if($("#alert-box").is(":visible")) {
        $("#alert-box").css("display","none");
    } else {
        $("#alert-box").css("display","block");
    }
};