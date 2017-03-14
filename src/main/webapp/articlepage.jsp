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

<div class="body">

<div class="content">
<ppf:breadcrumb category="${article.category}"/>

<h2><c:out value="${article.title}"/></h2>
<h1><c:out value="${article.headline}"/></h1>
<p><b><c:out value="${article.teasertext}"/></b></p>

<c:forEach var="paragraph" items="${paragraphs}">
<div>
	<h3><c:out value="${paragraph.headline}"/></h3>

	<c:choose>
	<c:when test="${paragraph.style eq 'WIDE_IMAGE'}">

		<c:if test="${not empty paragraph.image}">
			<c:set var="alt"><c:out value="${paragraph.image.title}"/></c:set>
			<img src="/img/6/${paragraph.image.id}" alt="${alt}"/>
		</c:if>

		<p><c:out value="${paragraph.bodyText}"/></p>

	</c:when>
	<c:when test="${paragraph.style eq 'IMAGE_LEFT'}">

		<p>
			<c:if test="${not empty paragraph.image}">
				<c:set var="alt"><c:out value="${paragraph.image.title}"/></c:set>
				<img src="/img/4/${paragraph.image.id}" align="left" alt="${alt}"/>
			</c:if>
			<c:out value="${paragraph.bodyText}"/>
		</p>

	</c:when>
	<c:when test="${paragraph.style eq 'IMAGE_RIGHT'}">

		<p>
			<c:if test="${not empty paragraph.image}">
				<c:set var="alt"><c:out value="${paragraph.image.title}"/></c:set>
				<img src="/img/4/${paragraph.image.id}" align="right" alt="${alt}"/>
			</c:if>
			<c:out value="${paragraph.bodyText}"/>
		</p>

	</c:when>
	</c:choose>

</div>
</c:forEach>

<p>
<i>${article.author.name}</i><br>
</p>

</div><%-- content --%>

<div class="sidebar">
	<c:out value="${article.category.sidebarcontent}"/>
</div>

<div class="clear"></div>

</div><%-- body --%>

<div class="footer">
Impressum
</div>

</body>
</html>
