package dao;

import model.Dish;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class DishDao {

    /**
     * 获取所有餐品
     * @return 餐品列表
     */
    public List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT id, name, price, image_url FROM dishes";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Dish dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setPrice(rs.getBigDecimal("price"));
                dish.setImageUrl(rs.getString("image_url"));
                dishes.add(dish);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dishes;
    }

    /**
     * 根据ID获取单个餐品
     * @param dishId 餐品ID
     * @return 找到的餐品对象，如果未找到则返回 null
     */
    public Dish getDishById(int dishId) {
        Dish dish = null;
        String sql = "SELECT id, name, price, image_url FROM dishes WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, dishId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                dish = new Dish();
                dish.setId(rs.getInt("id"));
                dish.setName(rs.getString("name"));
                dish.setPrice(rs.getBigDecimal("price"));
                dish.setImageUrl(rs.getString("image_url"));
            }
        } catch (SQLException e) {
                System.err.println("Error fetching dish by ID: " + e.getMessage()); // 打印具体错误信息
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dish;
    }

    /**
     * 更新餐品信息
     * @param dish 要更新的餐品对象 (ID 必须存在)
     * @return 更新成功返回 true，否则返回 false
     */
    public boolean updateDish(Dish dish) {
        String sql = "UPDATE dishes SET name = ?, price = ?, image_url = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dish.getName());
            pstmt.setBigDecimal(2, dish.getPrice());
            pstmt.setString(3, dish.getImageUrl());
            pstmt.setInt(4, dish.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating dish: " + e.getMessage()); // 打印具体错误信息
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}