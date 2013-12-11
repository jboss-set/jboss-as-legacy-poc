<%@ page contentType="text/html; charset=ISO-8859-1" %>
<html>
<body>
Welocme to unsecured page!
Or? '<%= request.getRemoteUser() %>!'
<br>
This page deals with UT magic!
<br>
<form action="/legacy/transactional/test" method="POST">
URL: <input type="text" name="url" value="127.0.0.1:1099"/>
<br />
Prefix: <input type="text" name="prefix"/>
<br />
Stateful: <input type="checkbox" name="stateful"/>
<br />
Secured: <input type="checkbox" name="secured"/>
<br />
<select name="list_id">
  <option value="rollback">rollback</option>
  <option value="rollbackOnly">rollbackOnly</option>
  <option value="throwException">throwException</option>
  <option value="commit">commit</option>
  <option value="doNothing">doNOthing</option>
</select>
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