  //
var tabIndex;
	    function SelectMenu(index) {
	        for (var i = 1; i <= 6; i++){
	            if (i == index)
	                continue;
	            if (document.getElementById("sub_menu_" + i) && document.getElementById("sub_menu_" + i).style.display != "none")
	                document.getElementById("sub_menu_" + i).style.display = "none";
	            if(i == tabIndex){
				document.getElementById("menu_" + i).className = "cur";
	                continue;
				}	
	            if (document.getElementById("menu_" + i) && document.getElementById("menu_" + i).className != "mainmenu")
	                document.getElementById("menu_" + i).className = "mainmenu";
	            //alert(document.getElementById('menu_'+i).className);
	         }
	        if (document.getElementById("sub_menu_" + index) && document.getElementById("sub_menu_" + index).style.display != "block")
	            document.getElementById("sub_menu_" + index).style.display = "block";
	        else if(tabIndex == 0) 
	            document.getElementById("sub_menu_1").style.display = "block";
	        if (document.getElementById("menu_" + index) && document.getElementById("menu_" + index).className != "cur") {
	            document.getElementById("menu_" + index).className = "cur";
	            //alert(document.getElementById('menu_' + index).className);
	        }
	    }
	    
	    function HideAllMenu() {
	        for (var i = 1; i <= 6; i++) {
	            if (i == tabIndex)
	                continue;
	            if (document.getElementById("sub_menu_" + i) && document.getElementById("sub_menu_" + i).style.display == "block")
	                document.getElementById("sub_menu_" + i).style.display = "none";
	            if (document.getElementById("menu_" + i) && document.getElementById("menu_" + i).className != "mainmenu")
	                document.getElementById("menu_" + i).className = "mainmenu";
	        }
	         if (document.getElementById("sub_menu_" + tabIndex))
	            document.getElementById("sub_menu_" + tabIndex).style.display = "block";
	         else
	            document.getElementById("sub_menu_1").style.display = "block";
	        
	          if (document.getElementById("menu_" + tabIndex) && document.getElementById("menu_" + tabIndex).className != "cur") 
	            document.getElementById("menu_" + tabIndex).className = "cur";
	    }
	      
	    function HideMenu(e, subMenuElementID){
	        if(!isMouseToSubMenu(e, subMenuElementID))
	        HideAllMenu();
	    } 
	    
	    function HideSubMenu(e, handler){
	        if(isMouseLeaveOrEnter(e, handler))
	        {
	        HideAllMenu();
	        }
	    }     

	    function isMouseLeaveOrEnter(e, handler){
	      if (e.type != 'mouseout' && e.type != 'mouseover') return false;
	       var reltg = e.relatedTarget ? e.relatedTarget : e.type == 'mouseout' ? e.toElement : e.fromElement;
	       while (reltg && reltg != handler)
	                 reltg = reltg.parentNode;
	       return (reltg != handler);
	    }

	    function isMouseToSubMenu(e, subMenuElementID){
	     if (e.type != 'mouseout')
	        return false;
	     var reltg = e.relatedTarget ? e.relatedTarget : e.toElement;
	     while(reltg && reltg.id != subMenuElementID)
	        reltg = reltg.parentNode;
	      return reltg;
	    }
	    
	    SelectMenu(tabIndex);
	
	    //首部滚动效果
	    function qg(id)
{
   return document.getElementById(id);
}
var anndelay = 3000;
var anncount = 0;
var annheight = 24;
var annst = 1;
function announcementScroll()
{
   if( ! annst)
   {
      qg('announcementbody').innerHTML += '<br style="clear: both" />' + qg('announcementbody').innerHTML;
      qg('announcementbody').scrollTop = 0;
      if(qg('announcementbody').scrollHeight > annheight * 3)
      {
         annst = setTimeout('announcementScroll()', anndelay);
      }
      else
      {
         qg('announcement').onmouseover = qg('announcement').onmouseout = null;
      }
      return;
   }
   if(anncount == annheight)
   {
      if(qg('announcementbody').scrollHeight - annheight <= qg('announcementbody').scrollTop)
      {
         qg('announcementbody').scrollTop = qg('announcementbody').scrollHeight / 2 - annheight;
      }
      anncount = 0;
      annst = setTimeout('announcementScroll()', anndelay);
   }
   else
   {
      qg('announcementbody').scrollTop ++ ;
      anncount ++ ;
      annst = setTimeout('announcementScroll()', 10);
   }
}
announcementScroll();