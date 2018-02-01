<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="formTags" uri="FormsTags" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Advanced search</title>
</head>
<body>
	Advanced search<br>
	<form action="AdvSearch.do" method="post">
		<formTags:MetadataFilters metadataTypes="${metadataTypes}"/>
		<input type="submit" value="search">
	</form>
</body>
</html>