<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib prefix="formTags" uri="FormsTags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Media Catalog</title>
</head>
<body>
	<form action="Search.do" method="post">
		<input name="search" type="text">
		<input type="submit">
	</form>
	<a href ="Upload.html">Upload file</a><br>
	<formTags:MediaGallery />
</body>
</html>