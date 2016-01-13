<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/context/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<title>提示信息</title>
		<link rel="stylesheet" type="text/css" href="${ctx}/styles/style.css" ></link>
	</head>
	<body>
	<%@ include file="/WEB-INF/context/commons/nav.jsp"%>
		<div id="hld">
			<div class="wrapper">
				<div class="block small center login">
					<div class="block_head">
						<div class="bheadl"></div>
						<div class="bheadr"></div>
						<h2>
							提示信息
						</h2>
					</div>
					<div class="block_content" align="center">
						<div>
							您的权限不足,请联系管理员!
						</div>
						<p/><p/>
						<form action="#">
							<input type="button" class="submit" onclick="window.history.back(); return false;" value="确  定" />
						</form>
					</div>
					<div class="bendl"></div>
					<div class="bendr"></div>
				</div>
			</div>
		</div>
	</body>
</html>