<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<title>PPFax</title>
	<link rel="stylesheet" type="text/css" href="/ppfax.css" />
</head>
<body>

Article

<h3>${article.title}</h3>
<h2>${article.headline}</h2>
<p><b>${article.teasertext}</b></p>

<c:forEach var="paragraph" items="${paragraphs}">
	<u>${paragraph.headline}</u><br>
	<p>${paragraph.bodyText}</p>
</c:forEach>

<i>${article.authorId}</i><br>


</body>
</html>
