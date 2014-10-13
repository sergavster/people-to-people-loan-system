//---------------------------------------------------【显示当前日期】
function showLocale(objD){
	var str,colorhead,colorfoot;

	var yy = objD.getYear();

	if(yy<1900) yy = yy+1900;

	var MM = objD.getMonth()+1;

	if(MM<10) MM = '0' + MM;

	var dd = objD.getDate();

	if(dd<10) dd = '0' + dd;

	var hh = objD.getHours();

	if(hh<10) hh = '0' + hh;

	var mm = objD.getMinutes();

	if(mm<10) mm = '0' + mm;

	var ss = objD.getSeconds();

	if(ss<10) ss = '0' + ss;

	var ww = objD.getDay();

	if  ( ww==0 )  colorhead="<span>";

	if  ( ww > 0 && ww < 6 )  colorhead="<span>";

	if  ( ww==6 )  colorhead="<span>";

	if  (ww==0)  ww="星期日";

	if  (ww==1)  ww="星期一";

	if  (ww==2)  ww="星期二";

	if  (ww==3)  ww="星期三";

	if  (ww==4)  ww="星期四";

	if  (ww==5)  ww="星期五";

	if  (ww==6)  ww="星期六";

	colorfoot="</span>"

	str = colorhead + yy + "-" + MM + "-" + dd + "<span class='n'> </span>" + hh + ":" + mm + ":" + ss + colorfoot;

	return(str);
}

function tick(){
	var today;
	today = new Date();
	$("dd.time-date").html(showLocale(today));
	window.setTimeout("tick()", 1000);
}

$(function(){
	tick();
});


//---------------------------------------------------【表格隔行变色】
$(function(){
    var $element = $(".valBox .fp").find("li");

	$(".valBox .vp li:odd").addClass("color");
	$(".valBox .bp li:odd").addClass("color");
	$("#main").find("table tr.s:odd").addClass("color");
    $(".ii tr").hover(function(){
        $(this).addClass("hover");
    },function(){
        $(this).removeClass("hover");
    });
});


//---------------------------------------------------【左边栏导航】
$(function(){
    $("div.nav-visitors-item").click(function(){
        if($(this).hasClass("hover")){
            $(this).removeClass("hover");
            $(this).next("ul.visitors-block").toggle();
        }else{
            $(this).addClass("hover");
            $(this).next("ul.visitors-block").toggle();
        }
    });

//    $("a.v").click(function(){
//        $("a.v").removeClass("fb");
//        $("div.bl").addClass("hidden");
//        $(this).addClass("fb");
//        $(this).parents("div.nav-oneself").find("div.bl").removeClass("hidden");
//    });
});

$(function(){
    $(".new-menu li .v").click(function(){
        if($(this).parent().hasClass("hover")){
            $(this).parent().removeClass("hover");
        }else{
            $(this).parent().addClass("hover");
        }
    });

    $(".secondary-menu li a").click(function(){
        $(".secondary-menu li a").removeClass("fb");
        $(".visitors-block li .v").removeClass("fb");
        $(this).addClass("fb");
        $(this).parents(".secondary-menu").prev().addClass("fb");
    });
});


//---------------------------------------------------【表单】
var INPUT_VAL = "";
$("textarea.remark,.toolbor-module .search-value").focus(function(){
    INPUT_VAL = $(this).val();
	hoverVal = $(this).attr("data-value");
    if(INPUT_VAL === hoverVal){
        $(this).val("");
        $(this).css("color","#333");
    }
})
    .blur(function(){
        if($(this).val() === ""){
            $(this).val(hoverVal);
            $(this).css("color","#9f9f9f");
        }
    });


//---------------------------------------------------【全选 & 反选】
$(function(){
    var $element = $("table.data-list");

    $element.find(".ch input").click(function(){
        if($(this).attr("checked")){
            $element.find(".c").attr("checked","checked");
        }else{
            $element.find(".c").removeAttr("checked");
        }
    });

    $element.find(".c").click(function(){
        if($element.find(".ch input").attr("checked")){
            $element.find(".ch input").removeAttr("checked");
        }else{
            for(var i=0;i<$element.find(".c").length;i++){
                $element.find(".ch input").attr("checked","checked");
                if(!$element.find(".c").eq(i).attr("checked")){
                    $element.find(".ch input").removeAttr("checked");
                    break;
                }
            }
        }

    });
});


//---------------------------------------------------【标签页切换】
$(function(){
    var $parametersTab = $("#parametersTab");

    $parametersTab.find("ul li div.t").click(function(){
        var index = $parametersTab.find("ul li div.t").index($(this));

        $parametersTab.find("ul li div.t").removeClass("active");
        $parametersTab.find(".customer-info").addClass("hidden");
        $parametersTab.find(".customer-info").eq(index).removeClass("hidden");
        $(this).addClass("active");
    });
});

$(function(){
    var $t = $("#main").find(".tag li");
    var $b = $("#main").find(".valBox");

    $t.click(function(){
        var index = $("#main").find(".tag li").index($(this));

        $t.removeClass("active");
        $(this).addClass("active");
        $b.addClass("hidden");
        $b.eq(index).removeClass("hidden");

    });
});


//---------------------------------------------------【弹窗】
//function alertPop(title,content){
//    var html = '<div class="bgfif" id="bgfif"></div><div id="popup"><div class="top"><strong>用户详细信息查看</strong><a href="javascript:void(0);"title="关闭"class="close">关闭</a></div><div class="main"><ul class="clearfix"><li><span class="c">用户名：</span><span class="v">admin</span></li><li><span class="c">真实姓名：</span><span class="v">admin</span></li><li><span class="c">邮箱：</span><span class="v">478849259@qq.com</span></li><li><span class="c">性别：</span><span class="v">男</span></li><li><span class="c">民族：</span><span class="v">哈沙克族</span></li><li><span class="c">生日：</span><span class="v">1988-2-9</span></li><li><span class="c">籍贯：</span><span class="v">陕西省西安市</span></li><li><span class="c">证件：</span><span class="v">522531198802092564</span></li><li><span class="c">电话：</span><span class="v">15623254596</span></li><li><span class="c">QQ：</span><span class="v">478849259</span></li><li><span class="c">余额：</span><span class="v">999,999,999</span></li><li><span class="c">信用积分：</span><span class="v">451</span></li></ul></div></div>';
//
//    $("body").append(html);
//
//    $("#popup").find(".top strong").html(title);
//    $("#popup").find(".main").html("<iframe src=\""+content+"\" width=\"100%\" scrolling=\"auto\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\"></iframe>");
//
//    $("#popup").find(".close").click(function(){
//        $("#bgfif").remove();
//        $("#popup").remove();
//    });
//}


//---------------------------------------------------【下拉】
$(function(){
    var $element = $(".select-box");

    $element.mouseenter(function(){
        $(this).find(".tag-options").show();
    });

    $element.mouseleave(function(){
        $(this).find(".tag-options").hide();
    });

    $(".tag-options li").hover(function(){
        $(this).addClass("hover");
    },function(){
        $(this).removeClass("hover");
    });

    $(".tag-options li").click(function(){
        $(this).parents(".tag-options").find("li").removeClass("open-selected");
        $(this).addClass("open-selected");
        var e = $(this).text();
        $(this).parents(".select-box").find(".tag-select").text(e);
        $(this).parents(".select-box").find(".tag-options").hide();
    });

});


//---------------------------------------------------【刷新当前页】
$(function(){
    $("a.refresh-page").click(function(){
        window.location.reload();
    });
});


//---------------------------------------------------【nav】
$(function(){
    var url = window.location.href;
    var $wrap = $(".nav-wrap");
    var $nav = $(".nav-oneself");
    var $topNav = $("#topNav").find("li a");

    var needData = url.split("admin/")[1].split("/")[1]; //区分单页
    var need_Data = url.split("admin/")[1].split("/")[0]; //区分栏目

    //去掉分页干扰
    if(needData.indexOf("?page") != -1){
        needData = needData.split("?page")[0];
    }

    for(var i=0;i<$nav.length;i++){
        for(var j=0;j<$nav.eq(i).find(".visitors-block li").length;j++){
            if($nav.eq(i).find(".visitors-block li a").eq(j).attr("data-nav") === needData){
                var $e = $nav.eq(i).find(".visitors-block li a").eq(j);
                $e.addClass("fb");
                $e.parents(".nav-oneself").find(".bl").removeClass("hidden");
                $e.parents(".nav-oneself").addClass("hover");
                $e.parents(".nav-oneself").find(".nav-visitors-item").addClass("hover");
            }
        }
    }

    for(var k=0;k<$topNav.length;k++){
        if($topNav.eq(k).attr("data-topnav").split("/")[2] === need_Data){
            $topNav.eq(k).addClass("active");
        }
    }

    //如果是栏目管理
    if(need_Data === "article"){
        for(var q=0;q<$(".secondary-menu li a").length;q++){
            if($(".secondary-menu li a").eq(q).attr("data-nav") === needData){
                $(".secondary-menu li a").eq(q).parent().addClass("hover");
                $(".secondary-menu li a").eq(q).parent().parent().parent().addClass("hover");
            }
        }
    }
});


///-------------------------------------------------------------------------
//jQuery弹出窗口 By Await [2009-11-22]
//--------------------------------------------------------------------------
/*参数：[可选参数在调用时可写可不写,其他为必写]
 ----------------------------------------------------------------------------
 title:	窗口标题
 content:  内容(可选内容为){ text | id | img | url | iframe }
 width:	内容宽度
 height:	内容高度
 drag:  是否可以拖动(ture为是,false为否)
 time:	自动关闭等待的时间，为空是则不自动关闭
 showbg:	[可选参数]设置是否显示遮罩层(0为不显示,1为显示)
 cssName:  [可选参数]附加class名称
 ------------------------------------------------------------------------*/
//示例:
//------------------------------------------------------------------------
//simpleWindown("例子","text:例子","500","400","true","3000","0","exa")
//------------------------------------------------------------------------
var showWindown = true;
var templateSrc = "http://leotheme.cn/wp-content/themes/Dreamy"; //设置loading.gif路径
function alertPop(title,content,width,height,drag,time,showbg,cssName) {
    $("#windown-box").remove(); //请除内容
    var width = width>= 950?this.width=950:this.width=width;	    //设置最大窗口宽度
    var height = height>= 527?this.height=527:this.height=height;  //设置最大窗口高度
    if(showWindown == true) {
        var simpleWindown_html = new String;
        simpleWindown_html = "<div id=\"windownbg\" style=\"height:"+$(document).height()+"px;filter:alpha(opacity=0);opacity:0;z-index: 999901\"></div>";
        simpleWindown_html += "<div id=\"windown-box\">";
        simpleWindown_html += "<div id=\"windown-title\"><h2></h2><span id=\"windown-close\">关闭</span></div>";
        simpleWindown_html += "<div id=\"windown-content-border\"><div id=\"windown-content\"></div></div>";
        simpleWindown_html += "</div>";
        $("body").append(simpleWindown_html);
        show = false;
    }
    contentType = content.substring(0,content.indexOf(":"));
    content = content.substring(content.indexOf(":")+1,content.length);
    switch(contentType) {
        case "text":
            $("#windown-content").html(content);
            break;
        case "id":
            $("#windown-content").html($("#"+content+"").html());
            break;
        case "img":
            $("#windown-content").ajaxStart(function() {
                $(this).html("<img src='"+templateSrc+"/images/loading.gif' class='loading' />");
            });
            $.ajax({
                error:function(){
                    $("#windown-content").html("<p class='windown-error'>加载数据出错...</p>");
                },
                success:function(html){
                    $("#windown-content").html("<img src="+content+" alt='' />");
                }
            });
            break;
        case "url":
            var content_array=content.split("?");
            $("#windown-content").ajaxStart(function(){
                $(this).html("<img src='"+templateSrc+"/images/loading.gif' class='loading' />");
            });
            $.ajax({
                type:content_array[0],
                url:content_array[1],
                data:content_array[2],
                error:function(){
                    $("#windown-content").html("<p class='windown-error'>加载数据出错...</p>");
                },
                success:function(html){
                    $("#windown-content").html(html);
                }
            });
            break;
        case "iframe":
            $("#windown-content").ajaxStart(function(){
                $(this).html("<img src='"+templateSrc+"/images/loading.gif' class='loading' />");
            });
            $.ajax({
                error:function(){
                    $("#windown-content").html("<p class='windown-error'>加载数据出错...</p>");
                },
                success:function(html){
                    $("#windown-content").html("<iframe src=\""+content+"\" width=\"100%\" height=\""+parseInt(height)+"px"+"\" scrolling=\"auto\" frameborder=\"0\" marginheight=\"0\" marginwidth=\"0\"></iframe>");
                }
            });
    }
    $("#windown-title h2").html(title);
    if(showbg == "true") {$("#windownbg").show();}else {$("#windownbg").remove();};
    $("#windownbg").animate({opacity:"0.5"},"normal");//设置透明度
    $("#windown-box").show();
    if( height >= 527 ) {
        $("#windown-title").css({width:(parseInt(width)+22)+"px"});
        $("#windown-content").css({width:(parseInt(width)+17)+"px",height:height+"px"});
    }else {
        $("#windown-title").css({width:(parseInt(width)+10)+"px"});
        $("#windown-content").css({width:width+"px",height:height+"px"});
    }
    var	cw = document.documentElement.clientWidth,ch = document.documentElement.clientHeight,est = document.documentElement.scrollTop;
    var _version = $.browser.version;
    if ( _version == 6.0 ) {
        $("#windown-box").css({left:"50%",top:(parseInt((ch)/2)+est)+"px",marginTop: -((parseInt(height)+53)/2)+"px",marginLeft:-((parseInt(width)+32)/2)+"px",zIndex: "999999"});
    }else {
        $("#windown-box").css({left:"50%",top:"50%",marginTop:-((parseInt(height)+53)/2)+"px",marginLeft:-((parseInt(width)+32)/2)+"px",zIndex: "999999"});
    };
    var Drag_ID = document.getElementById("windown-box"),DragHead = document.getElementById("windown-title");

    var moveX = 0,moveY = 0,moveTop,moveLeft = 0,moveable = false;
    if ( _version == 6.0 ) {
        moveTop = est;
    }else {
        moveTop = 0;
    }
    var	sw = Drag_ID.scrollWidth,sh = Drag_ID.scrollHeight;
    DragHead.onmouseover = function(e) {
        if(drag == "true"){DragHead.style.cursor = "move";}else{DragHead.style.cursor = "default";}
    };
    DragHead.onmousedown = function(e) {
        if(drag == "true"){moveable = true;}else{moveable = false;}
        e = window.event?window.event:e;
        var ol = Drag_ID.offsetLeft, ot = Drag_ID.offsetTop-moveTop;
        moveX = e.clientX-ol;
        moveY = e.clientY-ot;
        document.onmousemove = function(e) {
            if (moveable) {
                e = window.event?window.event:e;
                var x = e.clientX - moveX;
                var y = e.clientY - moveY;
                if ( x > 0 &&( x + sw < cw) && y > 0 && (y + sh < ch) ) {
                    Drag_ID.style.left = x + "px";
                    Drag_ID.style.top = parseInt(y+moveTop) + "px";
                    Drag_ID.style.margin = "auto";
                }
            }
        }
        document.onmouseup = function () {moveable = false;};
        Drag_ID.onselectstart = function(e){return false;}
    }
    $("#windown-content").attr("class","windown-"+cssName);
    var closeWindown = function() {
        $("#windownbg").remove();
        $("#windown-box").fadeOut("slow",function(){$(this).remove();});
    }
    if( time == "" || typeof(time) == "undefined") {
        $("#windown-close").click(function() {
            $("#windownbg").remove();
            $("#windown-box").fadeOut("slow",function(){$(this).remove();});
        });
    }else {
        setTimeout(closeWindown,time);
    }
}


//---------------------------------------------------【翻页】
$(function(){
    if($("#paginator")){
        var e = parseInt($(".page-num").val());
        $(".pages a").removeClass("current");
        $(".pages a").eq(e - 1).addClass("current");
    }
});


//---------------------------------------------------【pop】
$(function(){
    var $msgClass = $(".msgClass");
    if($msgClass){
        if($.trim($msgClass.text()) != ""){
            $msgClass.fadeIn("600",function(){
                setTimeout(function(){
                    $msgClass.fadeOut("400");
                },800)
            });
        }
    }
});


//---------------------------------------------------【左边栏隐藏】
$(function(){
   $(".navToggle").addClass("z-sj");

   $(".mc-m").click(function(){
       var $element = $("#navPanelContainer");

       if($element.is(":visible")){
           $element.hide();
           $(".navToggle").removeClass("z-sj");
           $(".navToggle").addClass("f-sj");
       }else{
           $element.show();
           $(".navToggle").removeClass("f-sj");
           $(".navToggle").addClass("z-sj");
       }
   });
});


//---------------------------------------------------【获取URL中的值，赋给select】
function urlFindStr(e){
    var url = window.location.href;
    var str = url.split("?")[1];
    var n = str.split("&");
    var m;

    for(var i=0;i<n.length;i++){
        if(n[i].split("=")[0] === e){
            m = n[i].split("=")[1];
        }
    }

    if(m){
        return m;
    }else{
        return false;
    }
}

$(function(){
    if($(".search")) {
        var s = window.location.href.split("?")[1].split("&");
        for(var i=0;i<s.length;i++){
            var e = s[i].split("=")[0];
            if($("#" + e)){
                var c = decodeURI(s[i].split("=")[1]);
                if($("#" + e).attr("type") === "text"){
                    $("#" + e).val(c);
                }else{
                    for(var j=0;j<$("#" + e).find("li").length;j++){
                        if($("#" + e).find("li").eq(j).attr("data-value") === c){
                            $("#" + e).find("li").eq(j).click();
                        }
                    }
                }
            }
        }
    }
});








