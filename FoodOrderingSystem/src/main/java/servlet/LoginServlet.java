package servlet;

import dao.UserDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/LoginServlet") // 告诉 Tomcat 这个 Servlet 映射到哪个 URL
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private UserDao userDao;

    public void init() {
        userDao = new UserDao();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userDao.getUserByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            // 登录成功，将用户信息存入 Session
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user); // 将用户对象存入 session

            // 跳转到点餐页面
            response.sendRedirect("DishServlet");
        } else {
            // 登录失败，设置错误消息并转发回登录页面
            request.setAttribute("errorMessage", "账号或密码错误，请重试！");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}