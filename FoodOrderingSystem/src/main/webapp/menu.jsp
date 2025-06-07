<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="model.Dish" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>点餐菜单 - 点餐系统</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="header">
        <h1>点餐系统</h1>
        <div class="user-info">
            <%
                User currentUser = (User) session.getAttribute("currentUser");
                if (currentUser != null) {
            %>
                <p>欢迎, <%= currentUser.getUsername() %>!</p>
                <p><a href="logout.jsp">退出登录</a></p>
            <%
                } else {
            %>
                <p><a href="login.jsp">请登录</a></p>
            <%
                }
            %>
        </div>
    </div>
    <div class="container">
        <h2>今日菜单</h2>
        <div class="dish-list">
            <%
                List<Dish> dishes = (List<Dish>) request.getAttribute("dishes");
                if (dishes != null && !dishes.isEmpty()) {
                    for (Dish dish : dishes) {
            %>
                <div class="dish-item">
                    <img src="<%= dish.getImageUrl() %>" alt="<%= dish.getName() %>">
                    <h3><%= dish.getName() %></h3>
                    <p>价格: ¥<%= dish.getPrice() %></p>
                    <button>加入购物车</button>
                </div>
            <%
                    }
                } else {
            %>
                <p>暂无餐品。</p>
            <%
                }
            %>
        </div>
    </div>
</body>
</html>