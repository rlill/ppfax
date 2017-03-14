<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:useBean id="random" class="farm.chaos.ppfax.utils.RandomBean" scope="application" />
<c:set var="qid" value="${random.nextId}" />

<div class="imglistitem">
<a href="#" onclick="parent.setInputValue('${inputId}', '${imageId}'); document.getElementById('${qid}').style.border='2px dashed #24f'; return false;">
<img id="${qid}" src="/img/2/${imageId}"/>
</a>
<p style="font-family: Verdana, Arial, sans-serif; font-size: 10pt;"><c:out value="${title}"/></p>
</div>
