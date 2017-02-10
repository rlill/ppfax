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

	&Pi;&Pi;&Phi;&Alpha;&Xi; demo site

	<ppf:mainmenu/>

</div>

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
			<img src="/img/6/${paragraph.image.id}"/>
		</c:if>

		<p><c:out value="${paragraph.bodyText}"/></p>

	</c:when>
	<c:when test="${paragraph.style eq 'IMAGE_LEFT'}">

		<p>
			<c:if test="${not empty paragraph.image}">
				<img src="/img/4/${paragraph.image.id}" align="left"/>
			</c:if>
			<c:out value="${paragraph.bodyText}"/>
		</p>

	</c:when>
	<c:when test="${paragraph.style eq 'IMAGE_RIGHT'}">

		<p>
			<c:if test="${not empty paragraph.image}">
				<img src="/img/4/${paragraph.image.id}" align="right"/>
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

</body>
</html>
