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
import java.math.BigDecimal;
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

        // --- 管理员权限检查 (针对更新功能) ---
        String action = request.getParameter("action");
        if ("showUpdateForm".equals(action)) {
            // 只有特定用户 (这里简化为 username "admin") 才能访问更新页面
            if (currentUser.getUsername().equals("admin")) {
                String dishIdStr = request.getParameter("dishId");
                if (dishIdStr != null && !dishIdStr.isEmpty()) {
                    try {
                        int dishId = Integer.parseInt(dishIdStr);
                        Dish dishToUpdate = dishDao.getDishById(dishId);
                        if (dishToUpdate != null) {
                            request.setAttribute("dishToUpdate", dishToUpdate);
                            request.getRequestDispatcher("update_dish.jsp").forward(request, response);
                            return; // 转发后停止后续处理
                        } else {
                            request.setAttribute("errorMessage", "未找到要更新的餐品。");
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("errorMessage", "无效的餐品ID格式。");
                    }
                }
                // 如果没有提供dishId或找不到，仍然跳转到更新表单，但不会预填充数据
                request.getRequestDispatcher("update_dish.jsp").forward(request, response);
                return;
            } else {
                // 非管理员用户尝试访问更新功能，重定向到菜单页或显示权限不足消息
                request.setAttribute("errorMessage", "您没有权限访问此功能。");
                // Fall through to display menu or redirect to menu
            }
        }
        // --- 管理员权限检查 结束 ---


        // 获取所有餐品列表 (正常显示菜单)
        List<Dish> dishes = dishDao.getAllDishes();
        request.setAttribute("dishes", dishes); // 将餐品列表存入请求属性

        // 转发到点餐页面
        request.getRequestDispatcher("menu.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 处理中文乱码
        // 检查用户是否已登录
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            response.sendRedirect("login.jsp"); // 未登录跳转到登录页面
            return;
        }

        String action = request.getParameter("action");

        if ("addToCart".equals(action)) {
            // ... (保持不变，与之前代码一致) ...
            String dishIdStr = request.getParameter("dishId");
            if (dishIdStr != null && !dishIdStr.isEmpty()) {
                try {
                    int dishId = Integer.parseInt(dishIdStr);
                    Dish dishToAdd = dishDao.getDishById(dishId);

                    if (dishToAdd != null) {
                        @SuppressWarnings("unchecked")
                        List<Dish> cart = (List<Dish>) session.getAttribute("cart");
                        if (cart == null) {
                            cart = new ArrayList<>();
                        }
                        cart.add(dishToAdd);
                        session.setAttribute("cart", cart);

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
            // ... (保持不变，与之前代码一致) ...
            session.removeAttribute("cart");
            request.setAttribute("addMessage", "购物车已清空！");
        } else if ("checkout".equals(action)) {
            // ... (保持不变，与之前代码一致) ...
            @SuppressWarnings("unchecked")
            List<Dish> cart = (List<Dish>) session.getAttribute("cart");
            BigDecimal totalAmount = BigDecimal.ZERO;

            if (cart != null && !cart.isEmpty()) {
                for (Dish dish : cart) {
                    totalAmount = totalAmount.add(dish.getPrice());
                }
                session.removeAttribute("cart");
                request.setAttribute("addMessage", "结算成功！订单总金额: ¥" + String.format("%.2f", totalAmount));
            } else {
                request.setAttribute("addMessage", "购物车为空，无法结算。");
            }
        } else if ("updateDish".equals(action)) { // <--- 新增的更新餐品逻辑
            // --- 管理员权限检查 ---
            if (!currentUser.getUsername().equals("admin")) {
                request.setAttribute("errorMessage", "您没有权限执行此操作。");
                doGet(request, response); // 重定向回菜单页
                return;
            }
            // --- 管理员权限检查 结束 ---

            String dishIdStr = request.getParameter("dishId");
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String imageUrl = request.getParameter("imageUrl");

            if (dishIdStr == null || dishIdStr.isEmpty() ||
                name == null || name.isEmpty() ||
                priceStr == null || priceStr.isEmpty()) {
                request.setAttribute("errorMessage", "餐品ID、名称和价格不能为空！");
                request.getRequestDispatcher("update_dish.jsp").forward(request, response);
                return;
            }

            try {
                int id = Integer.parseInt(dishIdStr);
                BigDecimal price = new BigDecimal(priceStr);

                Dish dishToUpdate = new Dish();
                dishToUpdate.setId(id);
                dishToUpdate.setName(name);
                dishToUpdate.setPrice(price);
                dishToUpdate.setImageUrl(imageUrl != null ? imageUrl : ""); // 图片URL可以为空

                if (dishDao.updateDish(dishToUpdate)) {
                    request.setAttribute("successMessage", "餐品更新成功！");
                } else {
                    request.setAttribute("errorMessage", "餐品更新失败，请检查ID是否存在。");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "价格或ID格式无效。");
                e.printStackTrace();
            }
            // 重新加载更新页面以显示消息
            request.getRequestDispatcher("update_dish.jsp").forward(request, response);
            return; // 转发后停止后续处理
        }

        // 处理完POST请求后，通常会再次显示菜单页面，所以调用 doGet 来刷新页面数据
        // 注意：如果是 updateDish 的 POST，我们已经转发到 update_dish.jsp，所以这里不需要再调用 doGet
        // 只有当 action 是 addToCart, clearCart, checkout 时才调用 doGet
        if (!"updateDish".equals(action)) {
            doGet(request, response);
        }
    }
}