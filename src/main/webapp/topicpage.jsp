<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ppf" tagdir="/WEB-INF/tags" %>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<title>PPFax</title>
	<link rel="stylesheet" type="text/css" href="/style.css" />
</head>
<body>

<div class="head">

	<div class="pagetitle">
		&Pi;&Pi;&Phi;&Alpha;&Xi; demo site
	</div>

	<ppf:mainmenu/>

</div>

<div class="content">

<c:if test="${not empty category}">
	<ppf:breadcrumb category="${category}"/>
	<ppf:subcatmenu category="${category}"/>

	<h1>
		<c:out value="${category.name}"/>
	</h1>
</c:if>

<c:forEach var="article" items="${articles}">
<div>
	<h3>${article.title}</h3>
	<h2><a href="${article.publicUri}">${article.headline}</a></h2>
	<p><b>${article.teasertext}</b></p>
</div>
</c:forEach>

</div><%-- content --%>


<div class="sidebar">
	<c:if test="${not empty category}">
		<c:out value="${category.sidebarcontent}"/>
	</c:if>
</div>

<div class="clear"></div>

<div class="footer">
Impressum
</div>

</body>
</html>
