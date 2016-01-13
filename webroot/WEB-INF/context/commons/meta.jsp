<%@ page contentType="text/html; charset=UTF-8"%>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8"><!-- 设置头文件类型 -->
<meta http-equiv="Cache-Control" content="no-store"/> <!-- 设置缓存 -->
<meta http-equiv="Pragma" content="no-cache"/> <!-- 设置页面不使用缓存 -->
<meta http-equiv="Expires" content="0"/>
<meta http-equiv="X-UA-Compatible" content="IE=7">

<!-- 全局JS -->
<script language="javascript"  src="${jshome}/script.js"></script>
<script language="javascript"  src="${jshome}/jquery-1.7.2.min.js"></script>


<!-- 设置JAVASCRIPT 项目根目录 全局变量-->
<script type="text/javascript">
	var ctx = "${ctx}";
	var baseCtx="<%=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()%>";
</script>
<!-- 全局CSS -->
<link href="${csshome}/css.css" rel="stylesheet" type="text/css" />