<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Dish" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>更新餐品 - 点餐系统</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
<style>
    /* 为更新表单添加一些额外样式 */
    .update-form {
        max-width: 500px;
        margin: 20px auto;
        padding: 30px;
        border: 1px solid #e0e0e0;
        border-radius: 8px;
        background-color: #fcfcfc;
        box-shadow: 0 4px 12px rgba(0,0,0,0.08);
    }
    .update-form h2 {
        color: #333;
        margin-bottom: 25px;
        text-align: center;
    }
    .update-form .form-group label {
        font-weight: 600;
        margin-bottom: 8px;
    }
    .update-form .form-group input[type="text"],
    .update-form .form-group input[type="number"] {
        padding: 12px;
        border-radius: 6px;
        border: 1px solid #ccc;
        font-size: 1em;
    }
    .update-form button[type="submit"] {
        background-color: #007bff;
        color: white;
        padding: 12px 25px;
        border: none;
        border-radius: 6px;
        cursor: pointer;
        font-size: 1.1em;
        margin-top: 20px;
        transition: background-color 0.3s ease;
    }
    .update-form button[type="submit"]:hover {
        background-color: #0056b3;
    }
    .back-link {
        display: block;
        text-align: center;
        margin-top: 20px;
        color: #007bff;
        text-decoration: none;
    }
    .back-link:hover {
        text-decoration: underline;
    }
</style>
</head>
<body>
    <div class="header">
        <h1>点餐系统</h1>
        <div class="user-info">
            <%
                model.User currentUser = (model.User) session.getAttribute("currentUser");
                if (currentUser != null) {
            %>
                <p>欢迎, <%= currentUser.getUsername() %>!</p>
                <p><a href="logout.jsp">退出登录</a></p>
            <%
                }
            %>
        </div>
    </div>

    <div class="container update-form">
        <h2>更新餐品信息</h2>

        <%
            // 显示错误或成功消息
            String errorMessage = (String) request.getAttribute("errorMessage");
            String successMessage = (String) request.getAttribute("successMessage");
            if (errorMessage != null && !errorMessage.isEmpty()) {
        %>
            <p class="error-message"><%= errorMessage %></p>
        <%
            } else if (successMessage != null && !successMessage.isEmpty()) {
        %>
            <p class="success-message"><%= successMessage %></p>
        <%
            }
        %>

        <form action="DishServlet" method="post">
            <input type="hidden" name="action" value="updateDish">

            <%
                // 预填充餐品信息（如果从菜单页跳转过来）
                Dish dishToUpdate = (Dish) request.getAttribute("dishToUpdate");
            %>

            <div class="form-group">
                <label for="dishId">餐品 ID:</label>
                <input type="number" id="dishId" name="dishId" required
                       value="<%= dishToUpdate != null ? dishToUpdate.getId() : "" %>">
            </div>
            <div class="form-group">
                <label for="name">餐品名称:</label>
                <input type="text" id="name" name="name" required
                       value="<%= dishToUpdate != null ? dishToUpdate.getName() : "" %>">
            </div>
            <div class="form-group">
                <label for="price">价格:</label>
                <input type="text" id="price" name="price" required
                       value="<%= dishToUpdate != null ? String.format("%.2f", dishToUpdate.getPrice()) : "" %>"
                       pattern="^\d+(\.\d{1,2})?$" title="请输入有效价格，最多两位小数">
            </div>
            <div class="form-group">
                <label for="imageUrl">图片 URL (可选):</label>
                <input type="text" id="imageUrl" name="imageUrl"
                       value="<%= dishToUpdate != null ? (dishToUpdate.getImageUrl() != null ? dishToUpdate.getImageUrl() : "") : "" %>">
            </div>
            <button type="submit">更新餐品</button>
        </form>

        <a href="DishServlet" class="back-link">返回菜单</a>
    </div>
</body>
</html>