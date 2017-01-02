<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@include file="header.jsp"%>

<h2>Crontab</h2>

<font color="red">${errormessage}</font>
<table class="grid">

<thead>
<tr>
	<th>min</th>
	<th>hour</th>
	<th>dom</th>
	<th>mon</th>
	<th>year</th>
	<th>dow</th>
	<th>max<br>runtime</th>
	<th>status</th>
	<th><table class="jobdetail"><tr><th colspan="3">command</th></tr>
		<tr><td>
			last run
		</td><td>
			next run
		</td></tr>
	</table></th>

</tr>
</thead>

<tbody>
<c:forEach var="s" items="${jobs}">

<c:choose>
<c:when test="${not empty modify and s.id eq modify}">
  <tr>
  	<form action="/admin" method="POST">
  	<input type="hidden" name="action" value="updatejob"/>
  	<input type="hidden" name="id" value="${s.id}"/>
	<td><input type="text" name="minute" value="${s.minute}" size="5"></td>
	<td><input type="text" name="hour" value="${s.hour}" size="5"></td>
	<td><input type="text" name="dayofmonth" value="${s.dayOfMonth}" size="5"></td>
	<td><input type="text" name="month" value="${s.month}" size="5"></td>
	<td><input type="text" name="year" value="${s.year}" size="5"></td>	
	<td><input type="text" name="dayofweek" value="${s.dayOfWeek}" size="5"></td>
	<td><input type="text" name="maxruntime" value="${s.maxRunMinutes}" size="5"></td>
	<td>
	<select name="status" size="1">
		<c:forEach var="j" items="${jobstatus}">
			<c:set var="sel" value="${(s.status eq j) ? 'selected' : ''}"/>
			<option value="${j.code}" ${sel}>${j}</option>
		</c:forEach>
	</select>
	</td>
	<td><input type="text" name="command" value="${fn:escapeXml(s.command)}" size="40"><br>
		<span class="mhl">Notification email</span><br>
		<input type="text" name="notification" size="40" value="${fn:escapeXml(s.notification)}">
	</td>
	<td>
		<input type="submit" value="update"/>
	</form>
	</td><td>
		<form action="/admin" method="POST">
			<input type="hidden" name="action" value="deletejob"/>
			<input type="hidden" name="id" value="${s.id}"/>
			<input type="submit" value="delete"/>
		</form>
	</td>
	</td><td>
		<form action="/admin" method="GET">
			<input type="submit" value="x"/>
		</form></td>
  </tr>
</c:when>
<c:otherwise>
  <tr>
	<td>${s.minute}</td>
	<td>${s.hour}</td>
	<td>${s.dayOfMonth}</td>
	<td>${s.month}</td>
	<td>${s.year}</td>	
	<td>${s.dayOfWeek}</td>
	<td>${s.maxRunMinutes}</td>
	<td>${s.status}</td>
	<td>
		${s.command}
	<table class="jobdetail">
		<tr><td>
			<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${s.lastRun}" timeZone="GMT" />
		</td><td>
			<c:choose>
			<c:when test="${s.status eq 'ENABLED' || s.status eq 'TEST' || s.status eq 'XSLT'}">
				<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${s.nextRun}" timeZone="GMT" />
			</c:when>
			<c:otherwise>
				disabled
			</c:otherwise>
			</c:choose>
		</td></tr>
	</table>
	</td>
	<td>
		<form action="/admin" method="GET">
			<input type="hidden" name="modify" value="${s.id}"/>
			<input type="submit" value="edit"/>
		</form>
	</td>
  </tr>
</c:otherwise>
</c:choose>

</c:forEach>

<form action="/admin" method="POST">
<c:choose>
    <c:when test="${not empty id}">
		<input type="hidden" name="action" value="updatejob">
		<c:set var="updateTitle" value="Update"/>		
    </c:when>
    <c:otherwise>
		<input type="hidden" name="action" value="addjob">
		<c:set var="updateTitle" value="Create"/>
    </c:otherwise>
</c:choose>
<tr><td colspan="11">
<h3><c:out value="${updateTitle}"/> new cronjob</h3>
</td></tr>

<tr>
	<th>min</th>
	<th>hour</th>
	<th>dom</th>
	<th>mon</th>
	<th>year</th>
	<th>dow</th>
	<th>max<br>runtime</th>
	<th>status</th>
	<th>command</th>
</tr>

<tr>
	<td><input type="text" name="minute" size="5" value="${minute}"></td>
	<td><input type="text" name="hour" size="5" value="${hour}"></td>
	<td><input type="text" name="dayofmonth" size="5" value="${dayofmonth}"></td>
	<td><input type="text" name="month" size="5" value="${month}"></td>
	<td><input type="text" name="year" size="5" value="${year}"></td>	
	<td><input type="text" name="dayofweek" size="5" value="${dayofweek}"></td>
	<td><input type="text" name="maxruntime" size="5" value="${maxruntime}"></td>
	<td>
		<select name="status" size="1">
		<c:forEach var="j" items="${jobstatus}">
			<c:set var="sel" value="${not empty id && status eq j.code ? 'selected' : ''}"/>
			<option value="${j.code}" ${sel}>${j}</option>
		</c:forEach>
	    </select>  
    </td>
	<td><input type="text" name="command" size="40" value="${fn:escapeXml(command)}"></td>
</tr>

<tr>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td><span class="mhl">Notification email</span></td>
</tr>

<tr>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td><input type="text" name="notification" size="40" value="${fn:escapeXml(notification)}">
	</td>
	<td>
	<c:choose>
	    <c:when test="${not empty id}">
			<input type="hidden" name="id" value="${id}"/>
			<input type="submit" value="update">
		</c:when>
	    <c:otherwise>
			<input type="submit" value="add">
	    </c:otherwise>
	</c:choose>	
	</td>
</tr>
</form>
</tbody>

</table>


</body>
</html>
