package servlet;

import dao.DishDao;
import model.Dish;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
        if (session.getAttribute("currentUser") == null) {
            response.sendRedirect("login.jsp"); // 未登录跳转到登录页面
            return;
        }

        List<Dish> dishes = dishDao.getAllDishes();
        request.setAttribute("dishes", dishes); // 将餐品列表存入请求属性

        request.getRequestDispatcher("menu.jsp").forward(request, response); // 转发到点餐页面
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 在这里可以处理点餐（添加到购物车）的逻辑，此处简化
        doGet(request, response);
    }
}