jQuery(function(){	
	function nav(obj,childUl,childBox){
        var box = $(obj);
        var oLi = $("li",childUl);
        var oDiv = $(childBox);
		function showTab(){
			oLi.each(function(i){
				if($(oLi[i]).hasClass("hover2"))
					{
						
						oDiv.addClass("hide");
            			$(oDiv[i]).removeClass("hide");
					}
				})
			}
		showTab();
        oLi.mouseenter(function(){
            var index = oLi.index(this);
            $(oLi[index]).addClass("hover").siblings().removeClass("hover");
            oDiv.addClass("hide");
            $(oDiv[index]).removeClass("hide");
        })
		box.mouseleave(function(){
			oLi.removeClass("hover");
			showTab()
			})
    }
    nav(".nav",".navul",".navlist");
	
	function headshow(){
		var box = $(".tb-list");
		var oUl = $(".tb-listul",box);
		box.hover(function(){
			box.addClass("hover");
			oUl.show();
			},function(){
				box.removeClass("hover");
				oUl.hide();
				})
		}
	headshow();
	
	function slideTop(id){
		var $this = $(id);
		$this.hover(function(){clearInterval(scrollTimer)},function(){
				scrollTimer = setInterval(function(){
					scrollNews($this);
					},3000)
			});
			var scrollTimer = setInterval(function(){
				scrollNews($this);
				},3000);		
			
			function scrollNews(obj){
				var $self = obj.find("ul:first");
				var heights = $self.find("li:first").height();
				$self.animate({"marginTop":-heights+"px"},800,function(){
						$self.css({marginTop:0}).find("li:first").appendTo($self);
					})
				}
		}
		slideTop(".sucbox");
		
		function slideLeft(id){
		var $this = $(id);
		$this.hover(function(){clearInterval(scrollTimer)},function(){
				scrollTimer = setInterval(function(){
					scrollNews($this);
					},3000)
			});
			var scrollTimer = setInterval(function(){
				scrollNews($this);
				},3000);		
			
			function scrollNews(obj){
				var $self = obj.find("ul:first");
				var widths = $self.find("li:first").width();
				$self.animate({"marginLeft":-widths+"px"},800,function(){
						$self.css({marginLeft:0}).find("li:first").appendTo($self);
					})
				}
		}
		slideLeft(".footslide");

		function choose(){
			var oBox = $(".chooseJS");
			oBox.each(function(){
				change(this);
				})
			function change(obj){
				var box = $(obj);
				var oLi = $("li",box);
                $(oLi[0]).addClass("clicks");
				oLi.click(function(){
					var index = oLi.index(this);
					$(oLi[index]).addClass("clicks").siblings().removeClass("clicks");

					var url=window.location.href;
					var index = url.indexOf("?");
					if(index>0){
						url=url.substring(0,index);
					}
					//url=url.match(/(.*?)\?/)[1];
					url=url+"?"+getSearchString()+"search=union";
					window.location.href=url;
				})
			}
		}
		choose();

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

		function Tab(obj,childLi,childBox){
        var box = $(obj);
        var oLi = $(childLi,box);
        var oDiv = $(childBox , box);
        oLi.mouseenter(function(){
            var index = oLi.index(this);
            $(oLi[index]).addClass("hover").siblings().removeClass("hover");
            oDiv.hide();
            $(oDiv[index]).show();
        })
    }
   	 Tab(".lz-tab","li",".lz-tabtxt");
   	function Tab2(obj,childLi,childBox){
        var box = $(obj);
        var oLi = $(childLi,box);
        var oDiv = $(childBox , box);
        oLi.click(function(){
            var index = oLi.index(this);
            $(oLi[index]).addClass("active").siblings().removeClass("active");
            oDiv.hide();
            $(oDiv[index]).show();
        })
    }
   	 Tab2(".fb-tab","#tab li","#myTabContent1 .list_1 ");
	 Tab2("#main","#tab li","#myTabContent .list-tab-con");
	 
	 function changeNum(){
        var box = $(".listmain");

        var oUl = $(".lzul",box);

        oUl.each(function(i){ //遍历所有的ul ，并且对当前这个ul下面的 表单进行函数的操作；
            var formBox = $("form",$(oUl[i]));

            add(formBox); //对当前form下面的 增加分数 进行操作
            less(formBox);//对当前form下面的 减少分数 进行操作
            getNum(formBox)
        })
        function  getNum(obj0){
            var inputBox = $(".txt",obj0);

            var maxNum;
            inputBox.blur(function(){
                inputNum  = parseInt(inputBox.attr("value"));
                var maxNum = parseInt($(".max",obj0).text());

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
        function add(obj1){//增加
            var addObj = $(".add",obj1);
            addObj.click(function(){
                var num = $(".txt",obj1).attr("value");
                var maxNum = parseInt($(".max",obj1).text());

                num++;

                if(num>maxNum)
                    return false;//获取当前的对象的 最大分数  判断是否大于，大于剩下的分数 就不在增加
                $(".txt",obj1).attr("value",num);
            })
        }
        function less(obj2){ //减少
            var lessObj = $(".reduse",obj2);

            lessObj.click(function(){
                var num = $(".txt",obj2).attr("value");
                num--;

                if(num<0)
                {
                    return false;
                }//获取当前的对象的最小值  判断是否小于0，小于0 就不在减少
                $(".txt",obj2).attr("value",num);
            })

        }
    }
    changeNum();
	
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
	   
	function underlineBank(){
		var moneyBox = $("#underlinebank");
			moneyBox.blur(function(){
				var money = moneyBox.val();
				var val=$('input:radio[name="type"]:checked').val()
				if(val==2){
					if(money<10000)
					{
						alert("线下充值的单笔最低金额不低于10000元。")
					}
				}		
			})
	}
	underlineBank();	
	 
	 

});

	$(function(){
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
	
	})

		
	jQuery(function(){ 
		var $ = jQuery;
		$(window).scroll(function (){
			var offsetTop = $(window).scrollTop() + 180 +"px";
			$("#floatBtn").animate({top : offsetTop},{duration:500 , queue:false});
			});
	});	
	
	