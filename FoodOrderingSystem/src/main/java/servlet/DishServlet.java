package servlet;

import dao.DishDao;
import model.Dish;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal; // 导入 BigDecimal
import java.util.ArrayList;
import java.util.List;

@WebServlet("/DishServlet")
public class DishServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DishDao dishDao;

    public void init() {
        dishDao = new DishDao();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp"); // 未登录跳转到登录页面
            return;
        }

        // 获取所有餐品列表
        List<Dish> dishes = dishDao.getAllDishes();
        request.setAttribute("dishes", dishes); // 将餐品列表存入请求属性

        // 转发到点餐页面
        request.getRequestDispatcher("menu.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp"); // 未登录跳转到登录页面
            return;
        }

        String action = request.getParameter("action");

        if ("addToCart".equals(action)) {
            // 处理添加餐品到购物车
            String dishIdStr = request.getParameter("dishId");
            if (dishIdStr != null && !dishIdStr.isEmpty()) {
                try {
                    int dishId = Integer.parseInt(dishIdStr);
                    // 根据 ID 获取完整的 Dish 对象
                    Dish dishToAdd = dishDao.getDishById(dishId); // 需要在 DishDao 中添加此方法

                    if (dishToAdd != null) {
                        // 从 Session 获取购物车，如果不存在则创建一个新的
                        @SuppressWarnings("unchecked")
                        List<Dish> cart = (List<Dish>) session.getAttribute("cart");
                        if (cart == null) {
                            cart = new ArrayList<>();
                        }
                        cart.add(dishToAdd); // 添加完整的 Dish 对象
                        session.setAttribute("cart", cart); // 更新 Session 中的购物车

                        request.setAttribute("addMessage", "餐品 '" + dishToAdd.getName() + "' 已添加到购物车！");
                    } else {
                        request.setAttribute("addMessage", "未找到ID为 " + dishId + " 的餐品。");
                    }

                } catch (NumberFormatException e) {
                    request.setAttribute("addMessage", "无效的餐品ID。");
                    e.printStackTrace();
                }
            } else {
                request.setAttribute("addMessage", "未选择餐品。");
            }
        } else if ("clearCart".equals(action)) {
            // 清空购物车
            session.removeAttribute("cart");
            request.setAttribute("addMessage", "购物车已清空！");
        } else if ("checkout".equals(action)) {
            // 结算逻辑
            @SuppressWarnings("unchecked")
            List<Dish> cart = (List<Dish>) session.getAttribute("cart");
            BigDecimal totalAmount = BigDecimal.ZERO; // 初始化总金额

            if (cart != null && !cart.isEmpty()) {
                for (Dish dish : cart) {
                    totalAmount = totalAmount.add(dish.getPrice()); // 累加价格
                }
                // 清空购物车（模拟结算完成）
                session.removeAttribute("cart");
                request.setAttribute("addMessage", "结算成功！订单总金额: ¥" + String.format("%.2f", totalAmount));
                // 在实际应用中，这里会创建订单、处理支付等
            } else {
                request.setAttribute("addMessage", "购物车为空，无法结算。");
            }
        }

        // 处理完POST请求后，通常会再次显示菜单页面，所以调用 doGet 来刷新页面数据
        doGet(request, response);
    }
}