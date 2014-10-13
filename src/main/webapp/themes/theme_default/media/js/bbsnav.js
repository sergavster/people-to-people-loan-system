
	function bbsNav(){
		var navUl,navLi,navDiv,oDiv;
			navLi = document.getElementById("navul").getElementsByTagName("li");
			oDiv = document.getElementById("navlist").getElementsByTagName("div");
			for(var i = 0; i < navLi.length; i++)
			{
				navLi[i].index = i;
				navLi[i].onmouseover = function ()
				{
					for(var n = 0; n < navLi.length; n++) navLi[n].className="";
					this.className = "hover2";
					for(var n = 0; n < navLi.length; n++) oDiv[n].style.display = "none";
					oDiv[this.index].style.display = "block"
				}	
			}
		
		}
		bbsNav();
