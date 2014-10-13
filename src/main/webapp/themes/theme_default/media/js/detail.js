jQuery(function(){	 
	var user_id=$("#user_id").val();          
     $("#userInfo").click(function(){
   	  $.ajax({
				"url": "detailForUserInfo.html?user_id="+user_id,
				"type": "post",
				"cache": "false",
				"data": {},
				"dataType": "json",
				"error": function(){
				},
				"success": function(data){
					var user=data.user;
					var info=data.info;
					var nature=user.nature;
					var html_ul=$("#userinfo_ul");
					var html_li;
					var sex=user.sex;
					var marry=info.marry;
					var education=info.education;
					var company_office=info.company_office;
					if(sex=='1'){
						sex="男";
					}else{
						sex="女";
					}
					if(marry!=null&&marry!=''){
						marry="${Typet((info.marry!0),'name')}";
						alert("11"+marry);
					}else{
						marry='-';
					}
					if(education!=null&&education!=''){
						alert("222"+info.education);
						education='${Typet((info.education!0),"name")}';
					}else{
						education='-';
					}
					if(company_office!=null&&company_office!=''){
						alert("3333"+info.company_office);
						company_office='${Typet((info.company_office!0),"name")}';
					}else{
						company_office='-';
					}
					if(nature==2){
				     html_li=' <li>注册日期： '+user.birthday+'</li>'+
					            '<li>所在地：'+user.provincetext+'-'+user.citytext+'-'+user.areatext+'</li>'+
					            '<li>注册地址：'+user.address+' </li>';
					}else{
					 html_li=' <li>性别： '+sex+'</li>'+
					            '<li>婚姻状况：'+marry+'</li>'+
					            '<li>居住地：'+user.citytext+' </li>'+
								'<li>出生年月： '+user.birthday+'  </li>'+
								'<li>文化程度：'+education+'</li>'+
								'<li>职业：'+company_office+'</li>';
					}
					html_ul.html(html_li);
				} 
			});
     });
       $("#accountInfo").click(function(){
                    	  $.ajax({
    							"url": "detailForBorrowSummary.html?user_id="+user_id,
    							"type": "post",
    							"cache": "false",
    							"data": {},
    							"dataType": "json",
    							"error": function(){
    							},
    							"success": function(data){
    								var userAccountSummary=data.data;
    								var html_ul=$("#account_info_ul");
    								var html_li=' <li>借入总额：￥ '+userAccountSummary.borrowTotal+'</li>'+
    								            '<li>借出总额： ￥'+userAccountSummary.investTotal+'</li>'+
    								            '<li>充值总额：￥'+userAccountSummary.rechargeTotal+' </li>'+
			    								'<li>提现总额： ￥'+userAccountSummary.cashTotal+'  </li>'+
			    								'<li>待还总额：￥'+userAccountSummary.repayTotal+'</li>'+
			    								'<li>待收总额：￥'+(userAccountSummary.collectTotal).toFixed(2)+'</li>';
			    					html_ul.html(html_li);
    							} 
    						});
                      });
    $("#repaymentInfo").click(function(){
                    	  $.ajax({
    							"url": "detailForRepaymentInfo.html?user_id="+user_id,
    							"type": "post",
    							"cache": "false",
    							"data": {},
    							"dataType": "json",
    							"error": function(){
    							},
    							"success": function(data){
    								var userRepayInfo=data.data;
    								var html_ul=$("#repayment-info-ul");
    								var html_li=' <li>借款 '+userRepayInfo.borrow_success_count+'次成功</li>'+
    								            '<li> '+userRepayInfo.flow_borrow_count+' 次流标</li>'+
    								            '<li>'+userRepayInfo.repay_fail_count+' 笔待还款</li>'+
			    								'<li> '+userRepayInfo.repay_success_count+' 笔已成功还款 </li>'+
			    								'<li>'+userRepayInfo.advance_repay_count+' 笔提前还款</li>'+
			    								'<li>'+userRepayInfo.late_repay_count+'笔迟还款</li>'+
			    								'<li> '+userRepayInfo.overdue_repay_count+'笔30天之内的逾期还款</li>'+
			    								'<li> '+userRepayInfo.overdues_repay_count+' 笔超过30天的逾期还款</li>'+
			    								'<li> '+userRepayInfo.overdue_unrepay_count+' 笔逾期未还款</li>';
			    					html_ul.html(html_li);
    							} 
    						});
                      });
    
   
});


/**
 * 和PHP一样的时间戳格式化函数
 * @param  {string} format    格式
 * @param  {int}    timestamp 要格式化的时间 默认为当前时间
 * @return {string}           格式化的时间字符串
 */
function date(format, timestamp){
    var a, jsdate=((timestamp) ? new Date(timestamp*1000) : new Date());
    var pad = function(n, c){
        if((n = n + "").length < c){
            return new Array(++c - n.length).join("0") + n;
        } else {
            return n;
        }
    };
    var txt_weekdays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
    var txt_ordin = {1:"st", 2:"nd", 3:"rd", 21:"st", 22:"nd", 23:"rd", 31:"st"};
    var txt_months = ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
    var f = {
        // Day
        d: function(){return pad(f.j(), 2)},
        D: function(){return f.l().substr(0,3)},
        j: function(){return jsdate.getDate()},
        l: function(){return txt_weekdays[f.w()]},
        N: function(){return f.w() + 1},
        S: function(){return txt_ordin[f.j()] ? txt_ordin[f.j()] : 'th'},
        w: function(){return jsdate.getDay()},
        z: function(){return (jsdate - new Date(jsdate.getFullYear() + "/1/1")) / 864e5 >> 0},
       
        // Week
        W: function(){
            var a = f.z(), b = 364 + f.L() - a;
            var nd2, nd = (new Date(jsdate.getFullYear() + "/1/1").getDay() || 7) - 1;
            if(b <= 2 && ((jsdate.getDay() || 7) - 1) <= 2 - b){
                return 1;
            } else{
                if(a <= 2 && nd >= 4 && a >= (6 - nd)){
                    nd2 = new Date(jsdate.getFullYear() - 1 + "/12/31");
                    return date("W", Math.round(nd2.getTime()/1000));
                } else{
                    return (1 + (nd <= 3 ? ((a + nd) / 7) : (a - (7 - nd)) / 7) >> 0);
                }
            }
        },
       
        // Month
        F: function(){return txt_months[f.n()]},
        m: function(){return pad(f.n(), 2)},
        M: function(){return f.F().substr(0,3)},
        n: function(){return jsdate.getMonth() + 1},
        t: function(){
            var n;
            if( (n = jsdate.getMonth() + 1) == 2 ){
                return 28 + f.L();
            } else{
                if( n & 1 && n < 8 || !(n & 1) && n > 7 ){
                    return 31;
                } else{
                    return 30;
                }
            }
        },
       
        // Year
        L: function(){var y = f.Y();return (!(y & 3) && (y % 1e2 || !(y % 4e2))) ? 1 : 0},
        //o not supported yet
        Y: function(){return jsdate.getFullYear()},
        y: function(){return (jsdate.getFullYear() + "").slice(2)},
       
        // Time
        a: function(){return jsdate.getHours() > 11 ? "pm" : "am"},
        A: function(){return f.a().toUpperCase()},
        B: function(){
            // peter paul koch:
            var off = (jsdate.getTimezoneOffset() + 60)*60;
            var theSeconds = (jsdate.getHours() * 3600) + (jsdate.getMinutes() * 60) + jsdate.getSeconds() + off;
            var beat = Math.floor(theSeconds/86.4);
            if (beat > 1000) beat -= 1000;
            if (beat < 0) beat += 1000;
            if ((String(beat)).length == 1) beat = "00"+beat;
            if ((String(beat)).length == 2) beat = "0"+beat;
            return beat;
        },
        g: function(){return jsdate.getHours() % 12 || 12},
        G: function(){return jsdate.getHours()},
        h: function(){return pad(f.g(), 2)},
        H: function(){return pad(jsdate.getHours(), 2)},
        i: function(){return pad(jsdate.getMinutes(), 2)},
        s: function(){return pad(jsdate.getSeconds(), 2)},
        //u not supported yet
       
        // Timezone
        //e not supported yet
        //I not supported yet
        O: function(){
            var t = pad(Math.abs(jsdate.getTimezoneOffset()/60*100), 4);
            if (jsdate.getTimezoneOffset() > 0) t = "-" + t; else t = "+" + t;
            return t;
        },
        P: function(){var O = f.O();return (O.substr(0, 3) + ":" + O.substr(3, 2))},
        //T not supported yet
        //Z not supported yet
       
        // Full Date/Time
        c: function(){return f.Y() + "-" + f.m() + "-" + f.d() + "T" + f.h() + ":" + f.i() + ":" + f.s() + f.P()},
        //r not supported yet
        U: function(){return Math.round(jsdate.getTime()/1000)}
    };
       
    return format.replace(/[\\]?([a-zA-Z])/g, function(t, s){
        if( t!=s ){
            // escaped
            ret = s;
        } else if( f[s] ){
            // a date function exists
            ret = f[s]();
        } else{
            // nothing special
            ret = s;
        }
        return ret;
    });
}