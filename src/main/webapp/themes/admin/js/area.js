jQuery(document).ready(function (){
	jQuery("#province").change(function(){
		var province = jQuery(this).val();
		var count = 0;
		jQuery.ajax({
			url:"/tools/showarea.html",
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
			url:"/tools/showarea.html",
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