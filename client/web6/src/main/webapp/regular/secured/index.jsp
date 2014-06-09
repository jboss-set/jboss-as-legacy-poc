<%@ page contentType="text/html; charset=ISO-8859-1" %>
<html>
<body>
HIGHLY UNSECURE AUTHENTICATED PAGE! Welcome to the application, <%= request.getRemoteUser() %>!
<br>
<form action="/test/regular/secured/test" method="POST">
URL: <input type="text" name="url" value="127.0.0.1:1199"/>
<br />
Prefix: <input type="text" name="prefix" value="test-server"/>
<br />
Stateful: <input type="checkbox" name="stateful"/>
<br />
Secured: <input type="checkbox" name="secured"/>
<br />
<input type="hidden" name="viewid" value="index.jsp"/>
<input type="submit" value="Invoke" />
</form>
<%

if(request != null){
	Object data = request.getSession().getAttribute("OUTPUT");
	if(data!=null) out.println(data);
}

%>
</body>
</html>