<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%@ attribute name="category" required="true" type="farm.chaos.ppfax.model.Category" %>

<div>

<c:forEach var="cat" items="${category.breadcrumb}">
	<a href="${cat.path}"><c:out value="${cat.name}"/></a>
</c:forEach>

</div>
