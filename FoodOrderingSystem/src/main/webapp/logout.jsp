```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>退出登录</title>
</head>
<body>
    <%
        // 使当前 Session 无效，这将清除所有与该用户会话相关的数据
        session.invalidate();
        // 重定向到登录页面
        response.sendRedirect("login.jsp");
    %>
</body>
</html>
```
* **作用：** 这段 JSP 代码非常简单。`session.invalidate()` 会销毁当前的 HTTP Session，从而清除用户登录状态以及购物车等所有 Session 中存储的数据。然后 `response.sendRedirect("login.jsp")` 会将浏览器重定向回登录页面。
