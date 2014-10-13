/**
 * Created with JetBrains WebStorm.
 * User: Administrator
 * Date: 13-1-14
 * Time: 下午3:57
 * To change this template use File | Settings | File Templates.
 */
$(function(){
    function loginFun(){
        var userBox = $("#username");
        var pwdBox = $("#password");

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