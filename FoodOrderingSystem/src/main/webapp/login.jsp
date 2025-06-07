<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户登录 - 点餐系统</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>用户登录</h2>
        <%
            // 显示登录失败的提示信息
            String errorMessage = (String) request.getAttribute("errorMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <p class="error-message"><%= errorMessage %></p>
        <%
            }
            // 显示注册成功的提示信息
            String successMessage = (String) request.getAttribute("successMessage");
            if (successMessage != null && !successMessage.isEmpty()) {
        %>
            <p class="success-message"><%= successMessage %></p>
        <%
            }
        %>
        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label for="username">用户名:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">密码:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit">登录</button>
        </form>
        <p>还没有账号？ <a href="register.jsp">立即注册</a></p>
    </div>
</body>
</html>