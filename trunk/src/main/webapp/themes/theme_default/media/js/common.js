//对网站js进行整合
//包含一些常用的js
//nav()头部导航样式,可以选择tab形式或是下拉形式
//slideFn() 实现无缝的上滚，无缝左滚
//flashSlider() 实现轮播图片左滚
//toggleNav()导航内容收缩展开
//tabChange()tab切换，可以点击，鼠标滑过
//flowChange() 改变流转标的分数
//floatBox() 浮动导航，自由配置初始高度
//fadeSlide()轮播图片淡入淡出
//登录框的文字显示
//QQ显示函数
//地区级联处理
//账户中心头像上传
//借款图标详情介绍
//
//
//
jQuery(function(){
	function getQueryString(name){
		var reg=new  RegExp("(^|&)"+name+"=([^&]*)(&|$)");
		var r =window.location.search.substr(1).match(reg);
		if(r!=null)
			return  unescape(r[2]);
		return  null;
	}

	var searchNames=["sType","sApr","sLimit","sAccount"];
	for(var item in searchNames){
		searchDisplay(searchNames[item]);
	}

	function searchDisplay(searchName){
		var sTypeValue=getQueryString(searchName);
		if(sTypeValue==null) sTypeValue="all";
		var sTypeLi=$("#"+searchName).children();
		sTypeLi.each(function(i){
			$(this).removeClass("clicks");
			var value=$(this).attr("data-value");
			if(value==sTypeValue) $(this).addClass("clicks");
		});
	}

	function getSearchString(){
		var searchStr="";
		for(var item in searchNames){
			var searchName=searchNames[item];
			var sTypeLi=$("#"+searchName).children();
			sTypeLi.each(function(i){
				if($(sTypeLi[i]).hasClass("clicks")){
					searchStr+=searchName+"="+$(sTypeLi[i]).attr("data-value")+"&";
				}

			})
		}
		return searchStr;
	}	
//我要投资页面类别搜索------------------------------------------------------------------------	
		
	function reg(){
        var iput=document.getElementById("infonlv");
        iput.value=iput.value.replace(/[^0-9\.]/g,'');
        iput.value=iput.value.replace(/\.{2,}/g,'.');
        if(/\.\d+\.+/.test(iput.value)){
            iput.value=iput.value.replace(/\.+$/g,'');
        }
    }
   $("#infonlv").keyup(function(){
	   	reg();
	});
//我要贷款页面利率输入控制小数点------------------------------------------------------------------------	
	   
	 $("[onclick='change_picktime()']").each(function(){
        this.className = "js-datetime";
		})
	jQuery(function(){  
		$.datepicker.regional['zh-CN'] = {  
		  clearText: '清除',  
		  clearStatus: '清除已选日期',  
		  closeText: '关闭',  
		  closeStatus: '不改变当前选择',  
		  prevText: '<上月',  
		  prevStatus: '显示上月',  
		  prevBigText: '<<',  
		  prevBigStatus: '显示上一年',  
		  nextText: '下月>',  
		  nextStatus: '显示下月',  
		  nextBigText: '>>',  
		  nextBigStatus: '显示下一年',  
		  currentText: '今天',  
		  currentStatus: '显示本月',  
		  monthNames: ['一月','二月','三月','四月','五月','六月', '七月','八月','九月','十月','十一月','十二月'],  
		  monthNamesShort: ['一','二','三','四','五','六', '七','八','九','十','十一','十二'],  
		  monthStatus: '选择月份',  
		  yearStatus: '选择年份',  
		  weekHeader: '周',  
		  weekStatus: '年内周次',  
		  dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  
		  dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  
		  dayNamesMin: ['日','一','二','三','四','五','六'],  
		  dayStatus: '设置 DD 为一周起始',  
		  dateStatus: '选择 m月 d日, DD',  
		  dateFormat: 'yy-mm-dd',  
		  firstDay: 1,  
		  initStatus: '请选择日期',  
		  isRTL: false  
		};  
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);  
    $('#datepicker').datepicker({changeMonth:true,changeYear:true});  
  });  
	$(".js-datetime").datepicker({ buttonText: "Choose" , changeMonth: true,  changeYear: true, dateFormat: "yy-mm-dd",currentText: 'Now', showOtherMonths: true,gotoCurrent: true,yearRange:"1900:2020"});
//时间插件------------------------------------------------------------------------	

});
//常用js函数------------------------------------------------------------------------	

	(function($){
        $.fn.extend({
            nav:function(options){
                var defaults = {
                    isTab: true,
					childLi:".navul",
					childContent:".navList",
					hoverClassName:"hover",
					hasClassName:"hover2"
                };
                var options = $.extend(defaults,options);
                 this.each(function(){
                    var o = options;
                    var obj = $(this);
                    var items = $(o.childLi,obj);
                    var oDiv = $(o.childContent,obj);
                     if(o.isTab){
                         oDiv.eq(0).show(); // tab切换可以使用这句;
                     }
                     var showDiv = function(){
                         items.each(function(i){
                            if($(items[i]).hasClass(o.hasClassName))
                            {
								$(items).removeClass(o.hoverClassName);
                                $(items[i]).addClass(o.hoverClassName);
                                oDiv.hide();
                                $(oDiv[i]).show();
                            }
                         });
                     };
                     if(o.isTab==true){
                        showDiv();
                     }
                    items.hover(function(){
                        var index = items.index(this);
                        $(items).removeClass(o.hoverClassName);
                        $(items[index]).addClass(o.hoverClassName);
                        $(oDiv).hide();
                        $(oDiv[index]).show();
                    });
                     $(this).mouseleave(function(){
                        if(o.isTab==true){
                            showDiv();
                        }else{
                            oDiv.hide();
                        }
                    });

                })
            }
        })
    })(jQuery);
	//首页导航------------------------------------------------------------------------------

    (function($){
        $.fn.extend({
            slideFn:function(options){
                var defaults = {
                    isTop:true,//是否
                    slideTimer:"3000"
                };
                var options = $.extend(defaults,options);
                this.each(function(){
                    var o = options;
                    var obj = $(this);
                    var oUl = obj.find("ul:first");
                    var oLi = $("li",oUl);

                    var Timer;
                    obj.hover(function(){
                        clearInterval(Timer);
                    },function(){
                        Timer = setInterval(function(){
                            if(o.isTop==true){
                                slideTop(oUl);
                            }else{
                                slideLeft(oUl);
                            }
                        }, o.slideTimer )
                    }).trigger("mouseleave");

                    var slideTop = function(box){
                        var oLiHeight = box.find("li:first").height();
                        box.animate({"marginTop":-oLiHeight+"px"},800,function(){
                            box.css("marginTop",0).find("li:first").appendTo(box);
                        })
                    };//上滚
                    var slideLeft = function(box2){
                        box2.css("width",((oLi.width())*(oLi.length))+"px");
                        var oLiWidth = box2.find("li:first").width();
                        box2.animate({"marginLeft":-oLiWidth+"px"},800,function(){
                            box2.css("marginLeft",0).find("li:first").appendTo(box2);
                        })
                    };//左滚
                })
            }

        })
    })(jQuery);
	//实现无缝上下滚动 无缝左右滚动--------------------------------------------------------------

    (function(jQuery) {
        jQuery.fn.flashSlider = function(options) {
            var options = jQuery.extend({}, jQuery.fn.flashSlider.defaults, options);
            this.each(function() {
                var obj = jQuery(this);
                var curr = 1; //当前索引
                var jQueryimg = obj.find("img");
                var s = jQueryimg.length;
                var w = jQueryimg.eq(0).width();
                var h = jQueryimg.eq(0).height();

                var jQueryflashelement = jQuery("ul", obj);
                options.height == 0 ? obj.height(h) : obj.height(options.height);
                options.width == 0 ? obj.width(w) : obj.width(options.width);
                obj.css("overflow", "hidden");
                obj.css("position", "relative");
                jQueryflashelement.css('width', s * w);
                if (!options.vertical) {
                    jQuery("li", obj).css('float', 'left')
                }
                else {
                    jQueryimg.css('display', 'block')
                };
                if (options.controlsShow) {
                    var navbtnhtml = '<div id="flashnvanum"><div class="numbox">';
                    for (var i = 0; i < s; i++) {
                        navbtnhtml += '<span>' + (i + 1) + '</span>';
                    }
                    navbtnhtml += '</div></div>';
                    obj.append(navbtnhtml);
                    obj.find("#flashnvanum span").hover(function() {
                        var num = jQuery(this).html();
                        flash(num, true);
                    }, function() {
                        timeout = setTimeout(function() {
                            flash((curr / 1 + 1), false);
                        }, options.pause / 4);
                    });
                };
                function setcurrnum(index) {
                    obj.find("#flashnvanum span").eq(index).addClass('on').siblings().removeClass('on');
                }
                function flash(index, clicked) {
                    jQueryflashelement.stop();
                    var next = index == s ? 1 : index + 1;
                    curr = index - 1;
                    setcurrnum((index - 1));
                    if (!options.vertical) {
                        p = ((index - 1) * w * -1);
                        jQueryflashelement.animate(
                                { marginLeft: p },
                                options.speed, options.easing
                        );
                    } else {
                        p = ((index - 1) * h * -1);
                        jQueryflashelement.animate(
                                { marginTop: p },
                                options.speed, options.easing
                        );
                    };
                    if (clicked) {
                        clearTimeout(timeout);
                    };
                    if (options.auto && !clicked) {
                        timeout = setTimeout(function() {
                            flash(next, false);
                        }, options.speed + options.pause);
                    };
                }
                var timeout;
                //初始化
                setcurrnum(0);
                if (options.auto) {
                    timeout = setTimeout(function() {
                        flash(2, false);
                    }, options.pause);
                }
            });
        };
        //默认值
        jQuery.fn.flashSlider.defaults = {
            controlsShow: true, //是否显示数字导航
            vertical: false, //纵向还是横向滑动
            speed: 800, //滑动速度
            auto: true, //是否自定滑动
            pause: 4000, //两次滑动暂停时间
            easing: "swing", //easing效果，默认swing，更多效果需要easing插件支持
            height: 222, //容器高度，不设置自动获取图片高度
            width: 741//容器宽度，不设置自动获取图片宽度
        }
    })(jQuery);
	//轮播图片-------------------------------------------------------

    (function($){
        $.fn.extend({
            toggleNav:function(options){
                var defaults = {
                    allShow:false,//是否全部显示
					childTitle:".toggle-title",//标题
					childContent:".toggle-content",//内容
					itemToggleBox:".togglediv",
					hoverClassName:"hover"//hover过后的样式
                };
                var options = $.extend(defaults,options);
                this.each(function(){
                    var o =options;
                    var obj = $(this);
                    var objDom = $(o.itemToggleBox, obj);
                    if(o.allShow==true){
                        $(o.childTitle,obj).addClass(o.hoverClassName)
                    }
                    var clickFn = function(box){
                        var oLi = $(o.childTitle,box);
                        var oDiv = $(o.childContent,box);
                        if(oLi.hasClass(o.hoverClassName))
                        {
                            oDiv.show();
                        }
                        oLi.toggle(function(){
                            if(oLi.hasClass(o.hoverClassName))
                            {
                                if(!oDiv.is(":animated")){
                                    $(this).removeClass(o.hoverClassName);
                                    oDiv.slideUp();
                                }
                            }else{
                                if(!oDiv.is(":animated")){
                                    $(this).addClass(o.hoverClassName);
                                    oDiv.slideDown();
                                }
                            }//有hover样式 则先隐藏，再显示
                        },function(){
                            if(!oLi.hasClass(o.hoverClassName))
                            {
                                if(!oDiv.is(":animated")){
                                    $(this).addClass(o.hoverClassName);
                                    oDiv.slideDown();
                                }
                            }else{
                                if(!oDiv.is(":animated")){
                                    $(this).removeClass(o.hoverClassName);
                                    oDiv.slideUp();
                                }
                            }//没有hover样式 则先显示，再隐藏

                        })
                    };
                    objDom.each(function(i){
                        clickFn($(this));
                    });

                })
            }
        })
    })(jQuery);
	//我的账户收缩导航显示----------------------------------------------------------------

    (function($){
        $.fn.extend({
            tabChange:function(options){
                var defaults = {
                    isClick:false,
                    isHover:true,
					childLi:".tab-ul ",//tab选项卡
					childContent:".tab-contentbox",//tab内容
					hoverClassName:"hover"//选中当前选项卡的样式
                };
                var options = $.extend(defaults,options);
                this.each(function(){
                    var o = options;
                    var obj = $(this);
                    var oLi = $(o.childLi,obj);
                    var oDiv = $(o.childContent,obj);
                    var Timer;
                    var oLiClick = function(){
                        oLi.eq(0).addClass(o.hoverClassName);
                        oDiv.eq(0).show();
                        oLi.click(function(){
                            var index = oLi.index(this);
                            $(oLi[index]).addClass(o.hoverClassName).siblings().removeClass(o.hoverClassName);
                            oDiv.fadeOut().hide();
                            $(oDiv[index]).fadeIn().show();
                        })
                    };
                    var oLiHover = function(){
                        oLi.eq(0).addClass(o.hoverClassName);
                        oDiv.eq(0).show();
                        oLi.mouseenter(function(){
                                var index = oLi.index(this);
                                $(oLi[index]).addClass(o.hoverClassName).siblings().removeClass(o.hoverClassName);
                                oDiv.hide();
                                $(oDiv[index]).show();
                        });
                    };

                    if(o.isClick==true)
                    {
                        oLiClick();
                    }
                    else if(o.isHover==true)
                    {
                        oLiHover();
                    }

                })
            }
        });
    })(jQuery);
	//tab切换-----------------------------------------------------------------------
	
	(function($){
		$.fn.extend({
			flowChange:function(options){
				var defaults = {
					changeBox:"#flow_num",//对form表单的循环
					addBtn:".add",//增加按钮
					lessBtn:".less",//减少按钮
					numTxt:".nums",//文本显示框
					maxNumBox:".max"//最大数的div
				};
				var options = $.extend(defaults,options);
				this.each(function(){
					var o = options;
					var obj = $(this);
					var changeNum = $(o.changeBox,obj);
					var getNum = function(obj0){
						var inputBox = $(o.numTxt,obj0);
						inputBox.blur(function(){
							inputNum  = parseInt(inputBox.attr("value"));
							var maxNum = parseInt($(o.maxNumBox,obj0).text());
							if(!isNaN(inputNum))
							{
								if(inputNum>maxNum)
								{
									inputBox.attr("value",maxNum);
								}
								else{
									inputBox.attr("value",inputNum);
								}
							}else{
								inputBox.attr("value",1);
							}
						})
					}
					var add = function(obj1){//增加
						var addObj = $(o.addBtn,obj1);
						addObj.click(function(){
							var num = $(o.numTxt,obj1).attr("value");
							var maxNum = parseInt($(o.maxNumBox).text());
							num++;
							if(num>maxNum)
								return false;//获取当前的对象的 最大分数  判断是否大于，大于剩下的分数 就不在增加
							$(o.numTxt,obj1).attr("value",num);
						})
					}
					var less = function(obj2){ //减少
						var lessObj = $(o.lessBtn,obj2);
						lessObj.click(function(){
							var num = $(o.numTxt,obj2).attr("value");
							num--;
							if(num<1)
							{
								return false;
							}//获取当前的对象的最小值  判断是否小于0，小于0 就不在减少
							$(o.numTxt,obj2).attr("value",num);
						})
					}
					changeNum.each(function(){
						add(this); //对当前form下面的 增加分数 进行操作
						less(this);//对当前form下面的 减少分数 进行操作
						getNum(this);
					})
	
				})
			}
		})
	})(jQuery);
	//流转标的份数加减---------------------------------------------------------------------------

	(function($){
		$.fn.extend({
			floatBox:function(options){
				var defaults = {
					topVal:180,
					scrollTopVal:300
				};
				var options = $.extend(defaults,options);
				this.each(function(){
					var o = options;
					var obj = $(this);
					$(window).scroll(function (){
						var offsetTop = $(window).scrollTop() + parseInt(o.topVal) +"px";//滚动的高度加上当前的绝对定位的top值
						obj.animate({top : offsetTop},{duration:500 , queue:false});
					});
				});
			}
		})				
	})(jQuery);
	//浮动导航-----------------------------------------------------------------------

	(function($){
		$.fn.extend({
			fadeSlide:function(options){
			var defaults = {
				slideTime:"3000",//变化时间
				childLi:".fade-ul li",//数字选项
				childContent:".fade-content div",//显示内容
				hoverClass:"hover"//hover过后的样式
				};
			var options = $.extend(defaults,options);
			this.each(function(){
				var o = options;
				var obj = $(this);
				var oLi = $(o.childLi,obj);
				var oDiv = $(o.childContent,obj);
				var Timer;
				var index = 1;
				var leng = (oLi.length);
				$(oDiv[0]).css({"display":"block"})
				oLi.mouseover(function(){
					index = oLi.index(this);
					showImg(index);
					}).eq(0).mouseover();
				obj.hover(function(){
						clearInterval(Timer);
					},function(){
						Timer = setInterval(function(){
								showImg(index);
								index++;
								if(index==leng)
								{
									index=0;	
								}
							},o.slideTime)	
					}).trigger("mouseleave");
					function showImg(index){
						$(oDiv[index]).fadeIn("slow").show().siblings().fadeOut().hide();
						$(oLi).removeClass(o.hoverClass).eq(index).addClass(o.hoverClass);
					}	
				});
			}
		});
	})(jQuery);
	//轮播图片淡入淡出---------------------------------------------------------------------------------
	
//常用js插件------------------------------------------------------------------------------------------
$(function(){
    function loginFun(){
        var userBox = $("input[name='username']");
        var pwdBox = $("input[name='password']");
        userBox.focus(function(){
            var icon = $(this).next("span.formicon");
            icon.hide();
        }).blur(function(){
                var icon = $(this).next("span.formicon");
                icon.show();
                var value = $(this).val();
                if(value=="")
                {
                    icon.addClass("erricon");
                    icon.removeClass("righticon");
                }
                else{
                    icon.removeClass("erricon");
                    icon.addClass("righticon");
                }
            });
        pwdBox.focus(function(){
            var icon = $(this).next("span.formicon");
            icon.hide();
        }).blur(function(){
                var icon = $(this).next("span.formicon");
                icon.show();
                var value = $(this).val();
                if(value=="")
                {
                    icon.addClass("erricon");
                    icon.removeClass("righticon");
                }
                else{
                    icon.removeClass("erricon");
                    icon.addClass("righticon");
                }
            });
    }
    loginFun();
});
//登录显示填写信息内容---------------------------------------------------------------------------------------------------------------


//QQ显示----------------------------------------------------------------------------------------------------------------------------
var online= new Array();
var urlroot = "http://gdp.istudy.com.cn/";
var tOut = -1;
var drag = false;
var g_safeNode = null;
lastScrollY = 0;
var kfguin;
var ws;
var companyname;
var welcomeword;
var type;
var wpadomain;
var eid;
var Browser = {
    ie:/msie/.test(window.navigator.userAgent.toLowerCase()),
    moz:/gecko/.test(window.navigator.userAgent.toLowerCase()),
    opera:/opera/.test(window.navigator.userAgent.toLowerCase()),
    safari:/safari/.test(window.navigator.userAgent.toLowerCase())
};
if(kfguin)
{

    //_Ten_rightDivHtml = '<div id="_Ten_rightDiv" style="position:absolute; top:160px; right:1px; display:none;">';

    //_Ten_rightDivHtml += kf_getPopup_Ten_rightDivHtml(kfguin,ws,wpadomain);

    //_Ten_rightDivHtml += '</div>';

    //document.write(_Ten_rightDivHtml);
    if(type==1 && kf_getCookie('hasshown')==0)
    {
        companyname = companyname.substr(0,15);
        welcomeword = kf_processWelcomeword(welcomeword);

        kfguin = kf_getSafeHTML(kfguin);
        companyname = kf_getSafeHTML(companyname);

        welcomeword = welcomeword.replace(/<br>/g,'\r\n');
        welcomeword = kf_getSafeHTML(welcomeword);
        welcomeword = welcomeword.replace(/\r/g, "").replace(/\n/g, "<BR>");

        window.setTimeout("kf_sleepShow()",200);
    }
    window.setTimeout("kf_moveWithScroll()",1);
}

function kf_getSafeHTML(s)
{
    var html = "";
    var safeNode = g_safeNode;
    if(!safeNode){
        safeNode = document.createElement("TEXTAREA");
    }
    if(safeNode){
        if(Browser.moz){
            safeNode.textContent = s;
        }
        else{
            safeNode.innerText = s;
        }
        html = safeNode.innerHTML;
        if(Browser.moz){
            safeNode.textContent = "";
        }
        else{
            safeNode.innerText = "";
        }
        g_safeNode = safeNode;
    }
    return html;
}

function kf_moveWithScroll()
{
    if(typeof window.pageYOffset != 'undefined') {
        nowY = window.pageYOffset;
    }
    else if(typeof document.compatMode != 'undefined' && document.compatMode != 'BackCompat') {
        nowY = document.documentElement.scrollTop;
    }
    else if(typeof document.body != 'undefined') {
        nowY = document.body.scrollTop;
    }

    percent = .1*(nowY - lastScrollY);
    if(percent > 0)
    {
        percent=Math.ceil(percent);
    }
    else
    {
        percent=Math.floor(percent);
    }

    //document.getElementById("_Ten_rightDiv").style.top = parseInt(document.getElementById("_Ten_rightDiv").style.top) + percent+"px";
    if(document.getElementById("kfpopupDiv"))
    {
        document.getElementById("kfpopupDiv").style.top = parseInt(document.getElementById("kfpopupDiv").style.top) + percent+"px";
    }
    lastScrollY = lastScrollY + percent;
    tOut = window.setTimeout("kf_moveWithScroll()",1);
}

function kf_hide()
{
    if(tOut!=-1)
    {
        clearTimeout(tOut);
        tOut=-1;
    }
    //document.getElementById("_Ten_rightDiv").style.visibility = "hidden";
    //document.getElementById("_Ten_rightDiv").style.display = "none";
    kf_setCookie('hasshown', 1, '', '/', wpadomain);
}

function kf_hidekfpopup()
{
    if(tOut!=-1)
    {
        clearTimeout(tOut);
        tOut=-1;
    }
    document.getElementById("kfpopupDiv").style.visibility = "hidden";
    document.getElementById("kfpopupDiv").style.display = "none";
    tOut=window.setTimeout("kf_moveWithScroll()",1);
    kf_setCookie('hasshown', 1, '', '/', wpadomain);
}

function kf_getPopupDivHtml(kfguin,reference,companyname,welcomeword, wpadomain)
{
    var temp = '';
    temp += '<span class="zixun0704_x"><a href="javascript:void(0);" onclick="kf_hidekfpopup();return false;"><!--鍏抽棴--></a></span>';
    temp += '<img src="'+urlroot+'web/pic_zixun0704_nv.jpg" class="zixun0704_img" />';
    temp += '<p class="zixun0704_font">'+welcomeword+'</p>';
    temp += '<div class="zixun0704_button"><a href="javascript:void(0);" onclick="kf_openChatWindow(1,\'b\',\''+kfguin+'\')"><img src="'+urlroot+'web/pic_zixun0704QQ.jpg" /></a>&nbsp;<a href="javascript:void(0);" onclick="kf_hidekfpopup();return false;"><img src="'+urlroot+'web/pic_zixun0704_later.jpg" /></a></div>';

    return temp;
}

function kf_openChatWindow(flag, wpadomain, kfguin)
{
    window.open('http://b.qq.com/webc.htm?new=0&sid='+kfguin+'&eid='+eid+'&o=&q=7', '_blank', 'height=544, width=644,toolbar=no,scrollbars=no,menubar=no,status=no');
    if(flag==1)
    {
        kf_hidekfpopup();
    }
    return false;
}
//added by simon 2008-11-04 end

function kf_validateWelcomeword(word)
{
    var count = 0;

    for(var i=0;i<word.length;i++)
    {
        if(word.charAt(i)=='\n')
        {
            count++;
        }
        if(count>2)
        {
            return 2;
        }
    }
    if(word.length > 57+2*count)
    {
        return 1;
    }

    count = 0;
    var temp = word.indexOf('\n');
    while(temp!=-1)
    {
        word = word.substr(temp+1);
        if(temp-1<=19)
        {
            count += 19;
        }
        else if(temp-1<=38)
        {
            count += 38;
        }
        else if(temp-1<=57)
        {
            count += 57;
        }

        temp = word.indexOf('\n');
    }
    count+=word.length;
    if(count>57)
    {
        return 3;
    }

    return 0;
}

function kf_processWelcomeword(word)
{
    word = word.substr(0,57+10);
    var result = '';
    var count = 0;
    var temp = word.indexOf('<br>');

    while(count<57 && temp!=-1)
    {

        if(temp<=19)
        {
            count += 19;
            if(count<=57)
            {
                result += word.substr(0,temp+5);
            }
            else
            {
                result += word.substr(0,57-count>word.length?word.length:57-count);
            }
        }
        else if(temp<=38)
        {
            count += 38;
            if(count<=57)
            {
                result += word.substr(0,temp+5);
            }
            else
            {
                result += word.substr(0,57-count>word.length?word.length:57-count);
            }
        }
        else if(temp<=57)
        {
            count += 57;
            if(count<=57)
            {
                result += word.substr(0,temp+5);
            }
            else
            {
                result += word.substr(0,57-count>word.length?word.length:57-count);
            }
        }

        word = word.substr(temp+5);
        temp = word.indexOf('<br>');
    }

    if(count<57)
    {
        result += word.substr(0,57-count>word.length?word.length:57-count);
    }

    return result;
}

function kf_setCookie(name, value, exp, path, domain)
{
    var nv = name + "=" + escape(value) + ";";

    var d = null;
    if(typeof(exp) == "object")
    {
        d = exp;
    }
    else if(typeof(exp) == "number")
    {
        d = new Date();
        d = new Date(d.getFullYear(), d.getMonth(), d.getDate(), d.getHours(), d.getMinutes() + exp, d.getSeconds(), d.getMilliseconds());
    }

    if(d)
    {
        nv += "expires=" + d.toGMTString() + ";";
    }

    if(!path)
    {
        nv += "path=/;";
    }
    else if(typeof(path) == "string" && path != "")
    {
        nv += "path=" + path + ";";
    }

    if(!domain && typeof(VS_COOKIEDM) != "undefined")
    {
        domain = VS_COOKIEDM;
    }

    if(typeof(domain) == "string" && domain != "")
    {
        nv += "domain=" + domain + ";";
    }

    document.cookie = nv;
}

function kf_getCookie(name)
{
    var value = "";
    var cookies = document.cookie.split("; ");
    var nv;
    var i;

    for(i = 0; i < cookies.length; i++)
    {
        nv = cookies[i].split("=");

        if(nv && nv.length >= 2 && name == kf_rTrim(kf_lTrim(nv[0])))
        {
            value = unescape(nv[1]);
        }
    }

    return value;
}

function kf_sleepShow()
{
    kf_setCookie('hasshown', 0, '', '/', wpadomain);
    var position_1 = (document.documentElement.clientWidth-381)/2+document.body.scrollLeft;
    var position_2 = (document.documentElement.clientHeight-159)/2+document.body.scrollTop;
    popupDivHtml = '<div class="zixun0704" id="kfpopupDiv" onmousedown="MyMove.Move(\'kfpopupDiv\',event,1);"  style="z-index:10000; position: absolute; top: '+position_2+'px; left: '+position_1+'px;color:#000;font-size: 12px;cursor:move;height: 159px;width: 381px;">';
    popupDivHtml += kf_getPopupDivHtml(kfguin,ws,companyname,welcomeword, wpadomain);
    popupDivHtml += '</div>';
    if(document.body.insertAdjacentHTML)
    {
        document.body.insertAdjacentHTML("beforeEnd",popupDivHtml);
    }
    else
    {
        $("#footer").before(popupDivHtml);
    }
}

function kf_dealErrors()
{
    kf_hide();
    return true;
}

function kf_lTrim(str)
{
    while (str.charAt(0) == " ")
    {
        str = str.slice(1);
    }
    return str;
}

function kf_rTrim(str)
{
    var iLength = str.length;
    while (str.charAt(iLength - 1) == " ")
    {
        str = str.slice(0, iLength - 1);
        iLength--;
    }
    return str;
}

window.onerror = kf_dealErrors;
var MyMove = new Tong_MoveDiv();

function Tong_MoveDiv()
{
    this.Move=function(Id,Evt,T)
    {
        if(Id == "")
        {
            return;
        }
        var o = document.getElementById(Id);
        if(!o)
        {
            return;
        }
        evt = Evt ? Evt : window.event;
        o.style.position = "absolute";
        o.style.zIndex = 9999;
        var obj = evt.srcElement ? evt.srcElement : evt.target;
        var w = o.offsetWidth;
        var h = o.offsetHeight;
        var l = o.offsetLeft;
        var t = o.offsetTop;
        var div = document.createElement("DIV");
        document.body.appendChild(div);
        div.style.cssText = "filter:alpha(Opacity=10,style=0);opacity:0.2;width:"+w+"px;height:"+h+"px;top:"+t+"px;left:"+l+"px;position:absolute;background:#000";
        div.setAttribute("id", Id +"temp");
        this.Move_OnlyMove(Id,evt,T);
    }

    this.Move_OnlyMove = function(Id,Evt,T)
    {
        var o = document.getElementById(Id+"temp");
        if(!o)
        {
            return;
        }
        evt = Evt?Evt:window.event;
        var relLeft = evt.clientX - o.offsetLeft;
        var relTop = evt.clientY - o.offsetTop;
        if(!window.captureEvents)
        {
            o.setCapture();
        }
        else
        {
            window.captureEvents(Event.MOUSEMOVE|Event.MOUSEUP);
        }

        document.onmousemove = function(e)
        {
            if(!o)
            {
                return;
            }
            e = e ? e : window.event;

            var bh = Math.max(document.body.scrollHeight,document.body.clientHeight,document.body.offsetHeight,
                document.documentElement.scrollHeight,document.documentElement.clientHeight,document.documentElement.offsetHeight);
            var bw = Math.max(document.body.scrollWidth,document.body.clientWidth,document.body.offsetWidth,
                document.documentElement.scrollWidth,document.documentElement.clientWidth,document.documentElement.offsetWidth);
            var sbw = 0;
            if(document.body.scrollWidth < bw)
                sbw = document.body.scrollWidth;
            if(document.body.clientWidth < bw && sbw < document.body.clientWidth)
                sbw = document.body.clientWidth;
            if(document.body.offsetWidth < bw && sbw < document.body.offsetWidth)
                sbw = document.body.offsetWidth;
            if(document.documentElement.scrollWidth < bw && sbw < document.documentElement.scrollWidth)
                sbw = document.documentElement.scrollWidth;
            if(document.documentElement.clientWidth < bw && sbw < document.documentElement.clientWidth)
                sbw = document.documentElement.clientWidth;
            if(document.documentElement.offsetWidth < bw && sbw < document.documentElement.offsetWidth)
                sbw = document.documentElement.offsetWidth;

            if(e.clientX - relLeft <= 0)
            {
                o.style.left = 0 +"px";
            }
            else if(e.clientX - relLeft >= bw - o.offsetWidth - 2)
            {
                o.style.left = (sbw - o.offsetWidth - 2) +"px";
            }
            else
            {
                o.style.left = e.clientX - relLeft +"px";
            }
            if(e.clientY - relTop <= 1)
            {
                o.style.top = 1 +"px";
            }
            else if(e.clientY - relTop >= bh - o.offsetHeight - 30)
            {
                o.style.top = (bh - o.offsetHeight) +"px";
            }
            else
            {
                o.style.top = e.clientY - relTop +"px";
            }
        }

        document.onmouseup = function()
        {
            if(!o) return;

            if(!window.captureEvents)
            {
                o.releaseCapture();
            }
            else
            {
                window.releaseEvents(Event.MOUSEMOVE|Event.MOUSEUP);
            }

            var o1 = document.getElementById(Id);
            if(!o1)
            {
                return;
            }
            var l0 = o.offsetLeft;
            var t0 = o.offsetTop;
            var l = o1.offsetLeft;
            var t = o1.offsetTop;

            //alert(l0 + " " +  t0 +" "+ l +" "+t);

            MyMove.Move_e(Id, l0 , t0, l, t,T);
            document.body.removeChild(o);
            o = null;
        }
    }


    this.Move_e = function(Id, l0 , t0, l, t,T)
    {
        if(typeof(window["ct"+ Id]) != "undefined")
        {
            clearTimeout(window["ct"+ Id]);
        }

        var o = document.getElementById(Id);
        if(!o) return;
        var sl = st = 8;
        var s_l = Math.abs(l0 - l);
        var s_t = Math.abs(t0 - t);
        if(s_l - s_t > 0)
        {
            if(s_t)
            {
                sl = Math.round(s_l / s_t) > 8 ? 8 : Math.round(s_l / s_t) * 6;
            }

            else
            {
                sl = 0;
            }
        }
        else
        {
            if(s_l)
            {
                st = Math.round(s_t / s_l) > 8 ? 8 : Math.round(s_t / s_l) * 6;
            }
            else
            {
                st = 0;
            }
        }

        if(l0 - l < 0)
        {
            sl *= -1;
        }
        if(t0 - t < 0)
        {
            st *= -1;
        }
        if(Math.abs(l + sl - l0) < 52 && sl)
        {
            sl = sl > 0 ? 2 : -2;
        }
        if(Math.abs(t + st - t0) < 52 && st)
        {
            st = st > 0 ? 2 : -2;
        }
        if(Math.abs(l + sl - l0) < 16 && sl)
        {
            sl = sl > 0 ? 1 : -1;
        }
        if(Math.abs(t + st - t0) < 16 && st)
        {
            st = st > 0 ? 1 : -1;
        }
        if(s_l == 0 && s_t == 0)
        {
            return;
        }
        if(T)
        {
            o.style.left = l0 +"px";
            o.style.top = t0 +"px";
            return;
        }
        else
        {
            if(Math.abs(l + sl - l0) < 2)
            {
                o.style.left = l0 +"px";
            }
            else
            {
                o.style.left = l + sl +"px";
            }
            if(Math.abs(t + st - t0) < 2)
            {
                o.style.top = t0 +"px";
            }
            else
            {
                o.style.top = t + st +"px";
            }

            window["ct"+ Id] = window.setTimeout("MyMove.Move_e('"+ Id +"', "+ l0 +" , "+ t0 +", "+ (l + sl) +", "+ (t + st) +","+T+")", 1);
        }
    }
}

function wpa_count()
{
    var body = document.getElementsByTagName('body').item(0);
    var img = document.createElement('img');
    var now = new Date();
    img.src = "http://"+wpadomain+".qq.com/cgi/wpac?kfguin=" + kfguin + "&ext=0" + "&time=" + now.getTime() + "ip=172.23.30.15&";
    img.style.display = "none";
    body.appendChild(img);
}



<!-- Modify 20121223-->
function onload_hide() {
    $('#divFloatToolsView').animate({width:'hide', opacity:'hide'}, 'normal', function () {
        $('#divFloatToolsView').hide();
        kf_setCookie('RightFloatShown', 1, '', '/', '');
    });
    $('#aFloatTools_Show').attr('style', 'display:block');
    $('#aFloatTools_Hide').attr('style', 'display:none');
}

function onload_show() {
    $('#divFloatToolsView').animate({width:'show', opacity:'show'}, 'normal', function () {
        $('#divFloatToolsView').show();
        kf_setCookie('RightFloatShown', 0, '', '/', '');
    });
    $('#aFloatTools_Show').attr('style', 'display:none');
    $('#aFloatTools_Hide').attr('style', 'display:block');

}
//QQ显示end----------------------------------------------------------------------------------------


//ajax 地区级联--------------------------------------------------------------------------------------
jQuery(document).ready(function (){
    jQuery("#province").change(function(){
        var province = jQuery(this).val();
        var count = 0;
        jQuery.ajax({
            url:"/member/identify/showarea.html",
            dataType:'json',
            data:"pid="+province,
            success:function(json){
                jQuery("#city option").each(function(){
                    jQuery(this).remove();
                });
                jQuery("<option value='0'>请选择</option>").appendTo("#city");
                jQuery(json).each(function(){
                    jQuery("<option value='"+json[count].id+"'>"+json[count].name+"</option>").appendTo("#city");
                    count++;
                });
            }
        });
    });
    jQuery("#city").change(function(){
        var city = jQuery(this).val();
        var count = 0;
        jQuery.ajax({
            url:"/member/identify/showarea.html",
            dataType:'json',
            data:"pid="+city,
            success:function(json){
                jQuery("#area option").each(function(){
                    jQuery(this).remove();
                });
                jQuery("<option value='0'>请选择</option>").appendTo("#area");
                jQuery(json).each(function(){
                    jQuery("<option value='"+json[count].id+"'>"+json[count].name+"</option>").appendTo("#area");
                    count++;
                });
                if(count>0)
                {
                    jQuery("#area").show();
                }else
                {
                    jQuery("#area").hide();
                }
            }
        });
    });
});
//ajax 地区级联--------------------------------------------------------------------------------------------------

//异步处理头像上传-----------------------------------------------------------------------------------------------

jQuery.extend({

    createUploadIframe: function(id, uri)
    {

        //create frame
        var frameId = 'jUploadFrame' + id;
        var iframeHtml = '<iframe id="' + frameId + '" name="' + frameId + '" style="position:absolute; top:-9999px; left:-9999px"';
        if(window.ActiveXObject)
        {
            if(typeof uri== 'boolean'){
                iframeHtml += ' src="' + 'javascript:false' + '"';

            }
            else if(typeof uri== 'string'){
                iframeHtml += ' src="' + uri + '"';

            }
        }
        iframeHtml += ' />';
        jQuery(iframeHtml).appendTo(document.body);

        return jQuery('#' + frameId).get(0);
    },
    createUploadForm: function(id, fileElementId, data)
    {
        //create form
        var formId = 'jUploadForm' + id;
        var fileId = 'jUploadFile' + id;
        var form = jQuery('<form  action="" method="POST" name="' + formId + '" id="' + formId + '" enctype="multipart/form-data"></form>');
        if(data)
        {
            for(var i in data)
            {
                jQuery('<input type="hidden" name="' + i + '" value="' + data[i] + '" />').appendTo(form);
            }
        }
        var oldElement = jQuery('#' + fileElementId);
        var newElement = jQuery(oldElement).clone();
        jQuery(oldElement).attr('id', fileId);
        jQuery(oldElement).before(newElement);
        jQuery(oldElement).appendTo(form);



        //set attributes
        jQuery(form).css('position', 'absolute');
        jQuery(form).css('top', '-1200px');
        jQuery(form).css('left', '-1200px');
        jQuery(form).appendTo('body');
        return form;
    },

    ajaxFileUpload: function(s) {
        // TODO introduce global settings, allowing the client to modify them for all requests, not only timeout
        s = jQuery.extend({}, jQuery.ajaxSettings, s);
        var id = new Date().getTime()
        var form = jQuery.createUploadForm(id, s.fileElementId, (typeof(s.data)=='undefined'?false:s.data));
        var io = jQuery.createUploadIframe(id, s.secureuri);
        var frameId = 'jUploadFrame' + id;
        var formId = 'jUploadForm' + id;
        // Watch for a new set of requests
        if ( s.global && ! jQuery.active++ )
        {
            jQuery.event.trigger( "ajaxStart" );
        }
        var requestDone = false;
        // Create the request object
        var xml = {}
        if ( s.global )
            jQuery.event.trigger("ajaxSend", [xml, s]);
        // Wait for a response to come back
        var uploadCallback = function(isTimeout)
        {
            var io = document.getElementById(frameId);
            try
            {
                if(io.contentWindow)
                {
                    xml.responseText = io.contentWindow.document.body?io.contentWindow.document.body.innerHTML:null;
                    xml.responseXML = io.contentWindow.document.XMLDocument?io.contentWindow.document.XMLDocument:io.contentWindow.document;

                }else if(io.contentDocument)
                {
                    xml.responseText = io.contentDocument.document.body?io.contentDocument.document.body.innerHTML:null;
                    xml.responseXML = io.contentDocument.document.XMLDocument?io.contentDocument.document.XMLDocument:io.contentDocument.document;
                }
            }catch(e)
            {
                jQuery.handleError(s, xml, null, e);
            }
            if ( xml || isTimeout == "timeout")
            {
                requestDone = true;
                var status;
                try {
                    status = isTimeout != "timeout" ? "success" : "error";
                    // Make sure that the request was successful or notmodified
                    if ( status != "error" )
                    {
                        // process the data (runs the xml through httpData regardless of callback)
                        var data = jQuery.uploadHttpData( xml, s.dataType );
                        // If a local callback was specified, fire it and pass it the data
                        if ( s.success )
                            s.success( data, status );

                        // Fire the global callback
                        if( s.global )
                            jQuery.event.trigger( "ajaxSuccess", [xml, s] );
                    } else
                        jQuery.handleError(s, xml, status);
                } catch(e)
                {
                    status = "error";
                    jQuery.handleError(s, xml, status, e);
                }

                // The request was completed
                if( s.global )
                    jQuery.event.trigger( "ajaxComplete", [xml, s] );

                // Handle the global AJAX counter
                if ( s.global && ! --jQuery.active )
                    jQuery.event.trigger( "ajaxStop" );

                // Process result
                if ( s.complete )
                    s.complete(xml, status);

                jQuery(io).unbind()

                setTimeout(function()
                {	try
                {
                    jQuery(io).remove();
                    jQuery(form).remove();

                } catch(e)
                {
                    jQuery.handleError(s, xml, null, e);
                }

                }, 100)

                xml = null

            }
        }
        // Timeout checker
        if ( s.timeout > 0 )
        {
            setTimeout(function(){
                // Check to see if the request is still happening
                if( !requestDone ) uploadCallback( "timeout" );
            }, s.timeout);
        }
        try
        {

            var form = jQuery('#' + formId);
            jQuery(form).attr('action', s.url);
            jQuery(form).attr('method', 'POST');
            jQuery(form).attr('target', frameId);
            if(form.encoding)
            {
                jQuery(form).attr('encoding', 'multipart/form-data');
            }
            else
            {
                jQuery(form).attr('enctype', 'multipart/form-data');
            }
            jQuery(form).submit();

        } catch(e)
        {
            jQuery.handleError(s, xml, null, e);
        }

        jQuery('#' + frameId).load(uploadCallback	);
        return {abort: function () {}};

    },

    uploadHttpData: function( r, type ) {
        var data = !type;
        data = type == "xml" || data ? r.responseXML : r.responseText;
        // If the type is "script", eval it in global context
        if ( type == "script" )
            jQuery.globalEval( data );
        // Get the JavaScript object, if JSON is used.
        if ( type == "json" )
            eval( "data = " + data );
        // evaluate scripts within html
        if ( type == "html" )
            jQuery("<div>").html(data).evalScripts();
        return data;
    }
});

//截取头像-----------------------------------------------------------------------------------------------------------------
var cropX,cropY,cropW,cropH;
function ajaxFileUpload(_url)
{


    $.ajaxFileUpload
        (
            {
                url:_url,
                secureuri:false,
                fileElementId:'upload',
                dataType: 'json',
                data:{name:'logan', id:'id'},
                success: function (data, status)
                {

                    if(typeof(data.error) != 'undefined')
                    {
                        if(data.error != '')
                        {
                            alert("error"+data.error);
                        }else
                        {
                            $("#cropimg").html("<img src=\""+data.msg+"?r="+Math.random()+"\" id=\"target\"></img>");
                            $("#preview").attr("src",data.msg+"?r="+Math.random());
                            crop();
                            $("#useravatar").hide();
                            $("#cropview").show();
                            var imgHeight = (300-(data.height))/2;
                            $("#cropimgbox").css({"paddingTop":imgHeight+"px","height":(300-imgHeight)+"px"});
							$(".user_help").text("请上传你网站的头像")
                        }
                    }
                },
                error: function (data, status, e)
                {
                    alert(e);
                }
            }
        );
    return false;
}

function crop(){
    /**crop**/
    var cropObj={};
    var boundx=100;boundy=100;
    cropObj=$("#target").Jcrop({
        onChange: updatePreview,
        onSelect: updatePreview,
        aspectRatio: 1,
        setSelect: boxborder(),
        minSize:[30,30]
    },function(){
        // Use the API to get the real image size
        var bounds = this.getBounds();
        boundx = bounds[0];
        boundy = bounds[1];
        // Store the API in the jcrop_api variable
        jcrop_api = this;
    });

    function updatePreview(c)
    {
        if (parseInt(c.w) > 0)
        {
            var rx = 100 / c.w;
            var ry = 100 / c.h;
            cropX=c.x;
            cropY=c.y;
            cropW=c.w;
            cropH=c.h;
            $('#preview').css({
                width: Math.round(rx * boundx) + 'px',
                height: Math.round(ry * boundy) + 'px',
                marginLeft: '-' + Math.round(rx * c.x) + 'px',
                marginTop: '-' + Math.round(ry * c.y) + 'px'
            });
        }

    };

    function boxborder(){
        return [0,0,100,100];
    }
    return cropObj;
}
//绑定保存头像功能



$("#saveavatar").bind("click",saveAvatar);
//异步保存头像
function saveAvatar(){

    $.post("../../saveAvatar.action",
        {
            cropX:cropX,
            cropY:cropY,
            cropW:cropW,
            cropH:cropH
        },
        function(data,textStatus){
            if(data.flag){
                //	alert(data.flag);
                $("#cropview").hide();
                var img=$("#useravatar").find("img");
                var src=img.attr("src")+"&t="+(new Date()).getTime();
                img.attr("src",src);
                $("#useravatar").show();
				$(".user_help").text("保存成功")
            }else{

            }
        });
}
//截取头像----------------------------------------------------------------------------------------------------------------------------


//标的详情显示----------------------------------------------------------------------------------------------------------------------------
/* ===========================================================
 * bootstrap-tooltip.js v2.0.2
 * http://twitter.github.com/bootstrap/javascript.html#tooltips
 * ========================================================== */
!function( $ ) {
    "use strict"
    /* TOOLTIP PUBLIC CLASS DEFINITION
     * =============================== */
    var Tooltip = function ( element, options ) {
        this.init('tooltip', element, options)
    }
    Tooltip.prototype = {

        constructor: Tooltip

        , init: function ( type, element, options ) {
            var eventIn
                , eventOut

            this.type = type
            this.$element = $(element)
            this.options = this.getOptions(options)
            this.enabled = true

            if (this.options.trigger != 'manual') {
                eventIn  = this.options.trigger == 'hover' ? 'mouseenter' : 'focus'
                eventOut = this.options.trigger == 'hover' ? 'mouseleave' : 'blur'
                this.$element.on(eventIn, this.options.selector, $.proxy(this.enter, this))
                this.$element.on(eventOut, this.options.selector, $.proxy(this.leave, this))
            }

            this.options.selector ?
                (this._options = $.extend({}, this.options, { trigger: 'manual', selector: '' })) :
                this.fixTitle()
        }

        , getOptions: function ( options ) {
            options = $.extend({}, $.fn[this.type].defaults, options, this.$element.data())

            if (options.delay && typeof options.delay == 'number') {
                options.delay = {
                    show: options.delay
                    , hide: options.delay
                }
            }

            return options
        }

        , enter: function ( e ) {
            var self = $(e.currentTarget)[this.type](this._options).data(this.type)

            if (!self.options.delay || !self.options.delay.show) {
                self.show()
            } else {
                self.hoverState = 'in'
                setTimeout(function() {
                    if (self.hoverState == 'in') {
                        self.show()
                    }
                }, self.options.delay.show)
            }
        }

        , leave: function ( e ) {
            var self = $(e.currentTarget)[this.type](this._options).data(this.type)

            if (!self.options.delay || !self.options.delay.hide) {
                self.hide()
            } else {
                self.hoverState = 'out'
                setTimeout(function() {
                    if (self.hoverState == 'out') {
                        self.hide()
                    }
                }, self.options.delay.hide)
            }
        }

        , show: function () {
            var $tip
                , inside
                , pos
                , actualWidth
                , actualHeight
                , placement
                , tp

            if (this.hasContent() && this.enabled) {
                $tip = this.tip()
                this.setContent()

                if (this.options.animation) {
                    $tip.addClass('fade')
                }

                placement = typeof this.options.placement == 'function' ?
                    this.options.placement.call(this, $tip[0], this.$element[0]) :
                    this.options.placement

                inside = /in/.test(placement)

                $tip
                    .remove()
                    .css({ top: 0, left: 0, display: 'block' })
                    .appendTo(inside ? this.$element : document.body)

                pos = this.getPosition(inside)

                actualWidth = $tip[0].offsetWidth
                actualHeight = $tip[0].offsetHeight

                switch (inside ? placement.split(' ')[1] : placement) {
                    case 'bottom':
                        tp = {top: pos.top + pos.height, left: pos.left + pos.width / 2 - actualWidth / 2}
                        break
                    case 'top':
                        tp = {top: pos.top - actualHeight, left: pos.left + pos.width / 2 - actualWidth / 2}
                        break
                    case 'left':
                        tp = {top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left - actualWidth}
                        break
                    case 'right':
                        tp = {top: pos.top + pos.height / 2 - actualHeight / 2, left: pos.left + pos.width}
                        break
                }

                $tip
                    .css(tp)
                    .addClass(placement)
                    .addClass('in')
            }
        }

        , setContent: function () {
            var $tip = this.tip()
            $tip.find('.tooltip-inner').html(this.getTitle())
            $tip.removeClass('fade in top bottom left right')
        }

        , hide: function () {
            var that = this
                , $tip = this.tip()

            $tip.removeClass('in')

            function removeWithAnimation() {
                var timeout = setTimeout(function () {
                    $tip.off($.support.transition.end).remove()
                }, 500)

                $tip.one($.support.transition.end, function () {
                    clearTimeout(timeout)
                    $tip.remove()
                })
            }

            $.support.transition && this.$tip.hasClass('fade') ?
                removeWithAnimation() :
                $tip.remove()
        }

        , fixTitle: function () {
            var $e = this.$element
            if ($e.attr('title') || typeof($e.attr('data-original-title')) != 'string') {
                $e.attr('data-original-title', $e.attr('title') || '').removeAttr('title')
            }
        }

        , hasContent: function () {
            return this.getTitle()
        }

        , getPosition: function (inside) {
            return $.extend({}, (inside ? {top: 0, left: 0} : this.$element.offset()), {
                width: this.$element[0].offsetWidth
                , height: this.$element[0].offsetHeight
            })
        }

        , getTitle: function () {
            var title
                , $e = this.$element
                , o = this.options

            title = $e.attr('data-original-title')
                || (typeof o.title == 'function' ? o.title.call($e[0]) :  o.title)

            title = (title || '').toString().replace(/(^\s*|\s*$)/, "")

            return title
        }

        , tip: function () {
            return this.$tip = this.$tip || $(this.options.template)
        }

        , validate: function () {
            if (!this.$element[0].parentNode) {
                this.hide()
                this.$element = null
                this.options = null
            }
        }

        , enable: function () {
            this.enabled = true
        }

        , disable: function () {
            this.enabled = false
        }

        , toggleEnabled: function () {
            this.enabled = !this.enabled
        }

        , toggle: function () {
            this[this.tip().hasClass('in') ? 'hide' : 'show']()
        }

    }


    /* TOOLTIP PLUGIN DEFINITION
     * ========================= */

    $.fn.tooltip = function ( option ) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('tooltip')
                , options = typeof option == 'object' && option
            if (!data) $this.data('tooltip', (data = new Tooltip(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.tooltip.Constructor = Tooltip

    $.fn.tooltip.defaults = {
        animation: true
        , delay: 0
        , selector: false
        , placement: 'top'
        , trigger: 'hover'
        , title: ''
        , template: '<div class="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
    }

}( window.jQuery );


//投标详情显示----------------------------------------------------------------------------------------------------------
!function( $ ) {

    "use strict"

    var Popover = function ( element, options ) {
        this.init('popover', element, options)
    }

    /* NOTE: POPOVER EXTENDS BOOTSTRAP-TOOLTIP.js
     ========================================== */

    Popover.prototype = $.extend({}, $.fn.tooltip.Constructor.prototype, {

        constructor: Popover

        , setContent: function () {
            var $tip = this.tip()
                , title = this.getTitle()
                , content = this.getContent()

            $tip.find('.popover-title')[ $.type(title) == 'object' ? 'append' : 'html' ](title)
            $tip.find('.popover-content > *')[ $.type(content) == 'object' ? 'append' : 'html' ](content)

            $tip.removeClass('fade top bottom left right in')
        }

        , hasContent: function () {
            return this.getTitle() || this.getContent()
        }

        , getContent: function () {
            var content
                , $e = this.$element
                , o = this.options

            content = $e.attr('data-content')
                || (typeof o.content == 'function' ? o.content.call($e[0]) :  o.content)

            content = content.toString().replace(/(^\s*|\s*$)/, "")

            return content
        }

        , tip: function() {
            if (!this.$tip) {
                this.$tip = $(this.options.template)
            }
            return this.$tip
        }

    })


    /* POPOVER PLUGIN DEFINITION
     * ======================= */

    $.fn.popover = function ( option ) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('popover')
                , options = typeof option == 'object' && option
            if (!data) $this.data('popover', (data = new Popover(this, options)))
            if (typeof option == 'string') data[option]()
        })
    }

    $.fn.popover.Constructor = Popover

    $.fn.popover.defaults = $.extend({} , $.fn.tooltip.defaults, {
        placement: 'right'
        , content: ''
        , template: '<div class="popover"><div class="arrow"></div><div class="popover-inner"><div class="popover-content"><p></p></div></div></div>'
    })
    $("[id^='info']").popover();
}( window.jQuery );

jQuery(function(){
    jQuery("[rel='tooltip']").tooltip();
});//显示首页的标的详情介绍
//发标页面的备注信息显示----------------------------------------------------------------------------------------------------------


//倒计时显示功能---------------------------------------------------------------------------------------------------------
function showRemainTime(){
    var endtimes=$(".endtime");
    endtimes.each(function(){
        RemainTime($(this));
    });
}
showRemainTime();
setInterval("showRemainTime()",1000);
function RemainTime(t){
    var iDay,iHour,iMinute,iSecond,account;
    var sDay="",sTime="";
    var at="data-time"
    var iTime=t.attr(at);
    if (iTime >= 0){
        iDay = parseInt(iTime/24/3600);
        iHour = parseInt((iTime/3600)%24);
        iMinute = parseInt((iTime/60)%60);
        iSecond = parseInt(iTime%60);
        if (iDay > 0){
            sDay = iDay + "天";
        }
        sTime =sDay + iHour + "小时" + iMinute + "分钟" + iSecond + "秒";
        if(iTime==0){
            sTime="<span style='color:green'>时间到了！</span>";
        }
        t.attr(at,iTime-1);
    }else{
        sTime="<span style='color:red'>此标已过期！</span>";
    }
    t.html(sTime);
}

//倒计时显示功能-----------------------------------------------------------------------------------------------------