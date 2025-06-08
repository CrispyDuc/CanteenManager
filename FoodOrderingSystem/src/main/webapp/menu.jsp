<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="model.Dish" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.math.BigDecimal" %> <%-- 导入 BigDecimal --%>
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

        <%-- 显示添加成功或失败的消息 --%>
        <%
            String addMessage = (String) request.getAttribute("addMessage");
            if (addMessage != null && !addMessage.isEmpty()) {
        %>
            <p class="success-message"><%= addMessage %></p>
        <%
            }
        %>

        <div class="dish-list">
            <%
                List<Dish> dishes = (List<Dish>) request.getAttribute("dishes");
                if (dishes != null && !dishes.isEmpty()) {
                    for (Dish dish : dishes) {
            %>
                <div class="dish-item">
                    <img src="<%= dish.getImageUrl() %>" alt="<%= dish.getName() %>">
                    <h3><%= dish.getName() %></h3>
                    <p>价格: ¥<%= String.format("%.2f", dish.getPrice()) %></p> <%-- 格式化价格 --%>
                    <form action="DishServlet" method="post">
                        <input type="hidden" name="action" value="addToCart">
                        <input type="hidden" name="dishId" value="<%= dish.getId() %>">
                        <button type="submit">加入购物车</button>
                    </form>
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

        <div class="cart-summary">
            <h3>购物车内容</h3>
            <%
                @SuppressWarnings("unchecked")
                List<Dish> cart = (List<Dish>) session.getAttribute("cart");
                BigDecimal cartTotal = BigDecimal.ZERO; // 初始化购物车总金额

                if (cart != null && !cart.isEmpty()) {
            %>
                <ul>
                    <%
                        for (Dish cartItem : cart) {
                            cartTotal = cartTotal.add(cartItem.getPrice()); // 累加总金额
                    %>
                        <li><%= cartItem.getName() %> - ¥<%= String.format("%.2f", cartItem.getPrice()) %></li>
                    <%
                        }
                    %>
                </ul>
                <p><strong>购物车总计: ¥<%= String.format("%.2f", cartTotal) %></strong></p>
            <%
                } else {
                    out.println("<p>购物车为空。</p>");
                }
            %>

            <div class="cart-actions">
                <form action="DishServlet" method="post" style="display: inline-block; margin-right: 10px;">
                    <input type="hidden" name="action" value="clearCart">
                    <button type="submit">清空购物车</button>
                </form>
                <%
                    if (cart != null && !cart.isEmpty()) { // 只有购物车非空时才显示结算按钮
                %>
                <form action="DishServlet" method="post" style="display: inline-block;">
                    <input type="hidden" name="action" value="checkout">
                    <button type="submit" class="checkout-button">去结算</button>
                </form>
                <%
                    }
                %>
            </div>
        </div>
    </div>
</body>
</html>