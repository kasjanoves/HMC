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
	<link rel="stylesheet"
		type="text/css"
		href="<c:url value="/resources/style.css" />" >
</head>
<body>
	<div class="MainMenu">
		<form action="Search.do" method="post">
			<input name="search" type="text">
			<input type="submit" value="Go!">
		</form>
		<a href ="<c:url value='Upload.jsp'/>" >Upload</a>|
		<a href ="AdvSearchPage.do" >Advanced search</a><br>
	</div>
	<c:set var = "SelectedTags" value = "${SelectedTags}"/>
	<c:if test = "${SelectedTags != null}" >
		<formTags:MediaTags mediaTags="${SelectedTags}" mode='selected'/>
	</c:if>	
	<br>
	<formTags:MediaTags mediaTags="${AllTags}" />
	<c:set var = "search" value = "${search}"/>
	<c:if test = "${fn:length(search) > 0}" >
		<br>Results for ${search}
	</c:if>
	<formTags:MediaGallery mediaSet='${MediaSet}' />
</body>
</html>