function autofill(event,inputId,divId,url,callbackName){
   var myEvent = event || window.event;   
   var keyCode = myEvent.keyCode;
   var username=$(inputId).value; 
   		if(keyCode>=48 && keyCode<=90 || keyCode==8 ){
   			if(username.length==0){
   				$(divId).style.display="none";
   				return ;
   			}else{
	   			var myAjax = new Ajax.Request(url,{method:"post", 
				parameters:'pageParam='+username, 
				onComplete:function (originalRequest) {
					var result = originalRequest.responseText;
					var rt = result.parseJSON();
					var content='';
					if (rt.success) {
						var data = rt.data;
						for(var i=0;i<data.length;i++){
						var tempName=String(data[i]);
						var strs= new Array(); 
						strs=tempName.split(","); 
						content += "<a href=\"javascript:"+callbackName+"('"+data[i]+"')\" >"+strs[1]+"</a>";
						$(divId).style.display="block";
		            	$(divId).innerHTML=content+"<div onclick=\""+divId+".style.display='none'\" class='close'>关闭</div>";
						}
					} else {
						$(divId).style.display="none";
						alert(rt.info);
					}
				}, onException:showException}); 	
   			}
   		}
}