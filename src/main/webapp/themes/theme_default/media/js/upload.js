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
							//$("#preview").css({"height":"auto","width":"auto"});
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
		$(".user_help").html("请上传你网站的头像").css("color","#c09853")
		var boundx=100;boundy=100;
		$("#preview").css({"height":"auto","width":"auto"});
		cropObj=$("#target").Jcrop({
			setSelect:[0,0,80,80],
			onChange: updatePreview,
			onSelect: updatePreview,
			aspectRatio: 1,
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
			  console.log(cropX,cropY,cropW,cropH)
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
					$(".user_help").html("头像保存成功").css("color","#000")
				}else{
					
				}
			});
}