<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%@ attribute name="type" required="true" type="java.lang.String" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<title>PPFax</title>
	<link rel="stylesheet" type="text/css" href="/ppfax.css" />
	<script src="/jquery-3.1.1.min.js"></script>
<%--
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
 --%>
</head>
<body>

<div class="head">
<div class="pplogo">
  	<h1>&Pi;&Pi;&Phi;&Alpha;&Xi;</h1>
</div>

<div class="menu">
<ul>
	<c:if test="${admin}">
		<li><a class="${type eq 'user' ? 'active' : ''}" href="/admin">Users</a></li>
	</c:if>
    <li><a class="${type eq 'article' ? 'active' : ''}" href="/article">Articles</a></li>
    <li><a class="${type eq 'image' ? 'active' : ''}" href="/image">Images</a></li>
    <li><a class="${type eq 'category' ? 'active' : ''}" href="/category">Categories</a></li>
	<c:if test="${admin}">
	    <li><a target="_blank" href="/swagger/index.html">API</a></li>
	</c:if>
</ul>
</div>
<div class="clear"></div>
</div>
