<%@ tag %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>

<%@ attribute name="category" required="true" type="farm.chaos.ppfax.model.Category" %>

<c:set var="subcats" value="${category.subCategories}"/>

<c:if test="${not empty subcats}">
<div class="subcatmenu">

<c:forEach var="cat" items="${subcats}">
	<a href="${cat.path}">&rarr;<c:out value="${cat.name}"/></a>
</c:forEach>

</div>
</c:if>
