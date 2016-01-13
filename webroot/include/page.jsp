<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/commons/taglibs.jsp"%>
<%@ page import="com.gotop.framework.core.pagination.PageResponse"%>
<%
    PageResponse rpage = (PageResponse) request.getAttribute("page");
    String pageInfoNum_str = request.getParameter("pageInfoNum");
    pageInfoNum_str = pageInfoNum_str == null ? "1" : pageInfoNum_str;
    Integer pageInfoNum = Integer.parseInt(pageInfoNum_str);
    int totalPageCount = rpage.getTotalPageCount();
    int currentPageNo = rpage.getCurrentPageNo();
    int pageInfoSize = 8;//页码个数
    int pageInfoMaxNum = pageInfoSize * pageInfoNum;//最大页码
    int pageInfoStartNum = pageInfoMaxNum + 1 - pageInfoSize;//开始页码
    boolean noNextPageInfo = pageInfoMaxNum > totalPageCount; //是否有下一组页码
    pageInfoMaxNum = noNextPageInfo ? totalPageCount : pageInfoMaxNum;//页面最大的页数不能大于总页数
%>
<script type="text/javascript">
//上一组页码
function nextPageInfo(page){
 	var pageInfoNum=parseInt($("#pageInfoNum").val());
 	pageInfoNum++;
 	$("#pageInfoNum").val(pageInfoNum);
	pageGo(page);
}
//下一组页码
function prePageInfo(page){
 	var pageInfoNum=parseInt($("#pageInfoNum").val());
 	pageInfoNum--;
 	$("#pageInfoNum").val(pageInfoNum);
	pageGo(page);
}
</script>
<input type="hidden" name="pageTotalPageCount" id="pageTotalPageCount"
	value="${page.totalPageCount}" />
<input type="hidden" value="${page.pageSize}" name="pageSize"
	id="pageSize" />
<input type="hidden" value="${page.currentPageNo}" name="pageNumber"
	id="pageNumber" />
<input type="hidden" value="<%=pageInfoNum%>" name="pageInfoNum"
	id="pageInfoNum" />

<div id="tnt_pagination">
	<%
	    if (pageInfoNum > 1) {
	%>
	<a href="javascript:prePageInfo(<%=pageInfoStartNum - 1%>)"><s:text name="FrontPage"><s:param><%=pageInfoSize%></s:param></s:text></a>
	<%
	    } else {
	%>
	<span class="disabled_tnt_pagination"><s:text name="FrontPage"><s:param><%=pageInfoSize%></s:param></s:text></span>
	<%
	    }
	%>
	<%
	    for (int i = pageInfoStartNum; i <= pageInfoMaxNum; i++) {
	        if (currentPageNo == i) {
	%>
	<span class="active_tnt_link"><%=i%></span>
	<%
	    } else {
	%><a href="javascript:pageGo(<%=i%>)"><%=i%></a>
	<%
	    }
	    }
	%>
	<%
	    if (noNextPageInfo) {
	%>
	<span class="disabled_tnt_pagination"><s:text name="BehindPage"><s:param><%=pageInfoSize%></s:param></s:text></span>
	<%
	    } else {
	%>
	<a href="javascript:nextPageInfo(<%=pageInfoMaxNum + 1%>)"><s:text name="BehindPage"><s:param><%=pageInfoSize%></s:param></s:text></a>
	<%
	    }
	%>

</div>

