// JavaScript Document
$(function(){
	function slideTop(obj){
		var _this = $(obj)
		var Timer;
		_this.hover(function(){
			clearInterval(Timer);
			},function(){
				Timer = setInterval(function(){
					scrollTop(_this);
					},2000)
				}).trigger("mouseleave");
			function scrollTop(box){
				var self = box.find("ul:first");
				var heights = self.find("li:first").height();
				self.animate({"marginTop":-heights+"px"},1000,function(){
					self.css("marginTop",0).find("li:first").appendTo(self);
					})
				}
		}
	slideTop(".index-succ")	
	})