<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="jshome" value="${ctx}/scripts"/>
<c:set var="csshome" value="${ctx}/styles"/>
<c:set var="imagehome" value="${ctx}/images"/>
<c:set var="widgethome" value="${ctx}/widgets"/>
<c:set var="wdatehome" value="${widgethome}/wdate"/>
<c:set var="exthome" value="${widgethome}/ext"/>
<c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${ctx}/" />
