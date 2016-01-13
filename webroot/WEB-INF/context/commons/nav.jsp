<%@ page language="java" pageEncoding="UTF-8"%>
<!-- header begins -->
<div id="header">
	<div class="hdrl"></div>
	<div class="hdrr"></div>
	<ul id="nav">
		<li><a href="index.action">首页</a></li>
		<li><a data-name="account" href="javascript:void(0);">用户管理</a></li>
		<li><a data-name="player" href="javascript:void(0);">玩家分析</a></li>
		<li><a data-name="props" href="javascript:void(0);">充值消费</a></li>
	</ul>
</div>
<div id="sub_nav">
	<div data-name="account">
		<ul>
			<li><a href="account/list.action">用户列表</a></li>
		</ul>
	</div>
	<div data-name="player">
		<ul>
			<li><a href="player/sale.action">购买统计</a></li>
			<li>|</li>
			<li><a href="player/basic.action">基本统计</a></li>
			<li>|</li>
			<li><a href="player/active.action">活跃度统计</a></li>
			<li>|</li>
			<li><a href="player/retain.action">留存率统计</a></li>
			<li>|</li>
			<li><a href="player/channel.action">渠道分析</a></li>
			<li>|</li>
			<li><a href="player/online.action">在线查询</a></li>
			<li>|</li>
			<li><a href="player/trend.action">用户分析</a></li>
			<li>|</li>
			<li><a href="player/onlineCount.action">在线统计</a></li>
			<li>|</li>
			<li><a href="player/amount.action">钻石查询</a></li>
			<li>|</li>
			<li><a href="player/jingyuan.action">精元查询</a></li>
			<li>|</li>
			<li><a href="player/getItem.action">物品获取记录</a></li>
			<li>|</li>
			<li><a href="player/useItem.action">物品使用记录</a></li>
		</ul>
	</div>
	<div data-name="props">
		<ul>
			<li><a href="props/bill.action">玩家充值列表</a></li>
			<li>|</li>
			<li><a href="props/pay.action">充值统计</a></li>
			<li>|</li>
			<li><a href="props/trend.action">收入趋势</a></li>
			<li>|</li>
			<li><a href="props/interval.action">充值时段</a></li>
			<li>|</li>
			<li><a href="props/income.action">收入分析</a></li>
			<li>|</li>
			<li><a href="props/honor.action">荣誉值统计</a></li>
			<li>|</li>
			<li><a href="props/exchange.action">兑换值统计</a></li>
		</ul>
	</div>
</div>
<!-- #header ends -->
<script type="text/javascript">
<!--
	function showSubNav(allSub, showDataName) {
		var sub = allSub.filter("[data-name=" + showDataName + "]");
		allSub.css({
			display : "none"
		});
		sub.css({
			display : "block"
		});
	}
	var allSubNav = $("#sub_nav > div");
	allSubNav.css({
		display : "none"
	});
	var pathName = window.document.location.pathname;
	var pattern = new RegExp("^" + ctx + "/([^/]+)/.*");
	var r = pattern.exec(pathName);
	if (r) {
		dataName = r[1];
		showSubNav(allSubNav, dataName);
	}
	var nav = $("#nav a");
	nav.each(function() {
		$(this).mouseover(function() {
			var data = $(this).attr("data-name");
			showSubNav(allSubNav, data);
		});
	});
//-->
</script>
