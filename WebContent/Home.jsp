<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix="formTags" uri="FormsTags" %>    
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Media Catalog</title>
</head>
<body>
	<form action="Home.jsp" method="post">
		<input name="search" type="text">
		<input type="submit">
	</form>
	<a href ="Upload.html">Upload file</a><br>
	<formTags:MediaTags tags = '${sessionScope.SelectedTags}'/>
	<br>
	<formTags:MediaTags />
	<c:set var = "search" scope = "session" value = "${param.search}"/>
	<c:if test = "${fn:length(search) > 0}" >
		Results for ${param.search}
	</c:if>
	<formTags:MediaGallery search = '${param.search}'/>
</body>
</html>