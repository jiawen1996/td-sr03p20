<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%-- page1.jsp --%>
	<form method=post action="page2.jsp">
		<input type="text" name="message" /> <input type="submit"
			name="submit" value="Submit">
	</form>
	<jsp:useBean id="stagiaire" class="Stagiaire" />
	<jsp:setProperty name="stagiaire" property="prenomF" value="Lambda" />
</body>
</html>