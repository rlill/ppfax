<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%@ attribute name="category" required="true" type="farm.chaos.ppfax.model.Category" %>

<div class="breadcrumb">

<c:forEach var="cat" items="${category.breadcrumb}">
	<b>/</b> <a href="${cat.path}"><c:out value="${cat.name}"/></a>
</c:forEach>

</div>
