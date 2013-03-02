<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ page session="false" %>

<h3>We have found following Jobs for you..</h3>

<c:forEach items="${jobs}" var="vo">
<dl style="border: 1px solid blue;">
	<dt>Job Id:</dt>
	<dd><c:out value="${vo.job.id}"></c:out></dd>
	<dt>Job Title:</dt>
	<dd><c:out value="${vo.job.jobTitle}"/></dd>
	<dt>Company:</dt>
	<dd><c:out value="${vo.job.company.companyName}"/></dd>
	<dt>Skills Required:</dt>
	<c:forEach items="${vo.job.skills}" var="skill">
		<dd><c:out value="${skill}"/></dd>
	</c:forEach>
	
	<dt>Job Location:</dt>
	<dd><c:out value="${vo.job.formattedAddress}"/></dd>
	
	<dt>Distance:</dt>
	<dd><c:out value="${vo.distance.text}"/></dd>
	
	<dt>Time to reach by road:</dt>
	<dd><c:out value="${vo.duration.text}"/></dd>
	
</dl>

</c:forEach>
