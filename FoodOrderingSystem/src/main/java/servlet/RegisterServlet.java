package servlet;

import dao.UserDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDao userDao;

    public void init() {
        userDao = new UserDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // 处理中文乱码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.isEmpty() || password == null || password.isEmpty() || !password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "请填写所有字段并确保密码一致！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // 检查用户名是否已存在
        if (userDao.getUserByUsername(username) != null) {
            request.setAttribute("errorMessage", "该用户名已被注册！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // 实际项目中密码应该加密存储

        if (userDao.registerUser(newUser)) {
            // 注册成功，自动跳转到登录页面
            request.setAttribute("successMessage", "注册成功，请登录！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            // 注册失败
            request.setAttribute("errorMessage", "注册失败，请稍后再试！");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}