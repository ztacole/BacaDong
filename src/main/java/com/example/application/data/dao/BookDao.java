package com.example.application.data.dao;

import com.example.application.data.DBService;
import com.example.application.data.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookDao {
    Connection conn;
    PreparedStatement ps;
    ResultSet rs;
    Book book;
    ArrayList<Book> books;

    // Constructor
    public BookDao() {
        this.conn = DBService.getConnection();
    }

    // Get newest books (sorted by publish_date descending)
    public ArrayList<Book> getNewestBooks(int limit) {
        books = new ArrayList<>();
        try {
            String sql = "SELECT b.*, c.name as category_name FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "ORDER BY b.publish_date DESC LIMIT ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setPublisher(rs.getString("publisher"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setImageCover(rs.getString("image_cover"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return books;
    }

    // Get top rated books (sorted by average rating descending)
    public ArrayList<Book> getTopRatedBooks(int limit) {
        books = new ArrayList<>();
        try {
            String sql = "SELECT b.*, c.name as category_name, AVG(bh.rating) as avg_rating " +
                    "FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "LEFT JOIN book_history bh ON b.id = bh.book_id " +
                    "GROUP BY b.id " +
                    "ORDER BY avg_rating DESC LIMIT ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setPublisher(rs.getString("publisher"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setImageCover(rs.getString("image_cover"));
                book.setAverageRating(rs.getDouble("avg_rating"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return books;
    }

    // Get most viewed books (sorted by review count descending)
    public ArrayList<Book> getMostViewedBooks(int limit) {
        books = new ArrayList<>();
        try {
            String sql = "SELECT b.*, c.name as category_name, COUNT(bh.id) as view_count " +
                    "FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "LEFT JOIN book_history bh ON b.id = bh.book_id " +
                    "GROUP BY b.id " +
                    "ORDER BY view_count DESC LIMIT ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setPublisher(rs.getString("publisher"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setImageCover(rs.getString("image_cover"));
                book.setViewCount(rs.getInt("view_count"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return books;
    }

    // Get books by category with sorting options
    public ArrayList<Book> getBooksByCategory(String category, String sortBy) {
        books = new ArrayList<>();
        try {
            String sql = "SELECT b.*, c.name as category_name, AVG(bh.rating) as avg_rating, COUNT(bh.id) as view_count " +
                    "FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "LEFT JOIN book_history bh ON b.id = bh.book_id " +
                    "WHERE c.name = ? " +
                    "GROUP BY b.id ";

            // Add sorting based on parameter
            switch (sortBy.toLowerCase()) {
                case "popular":
                    sql += "ORDER BY view_count DESC";
                    break;
                case "rating":
                    sql += "ORDER BY avg_rating DESC";
                    break;
                case "newest":
                    sql += "ORDER BY b.publish_date DESC";
                    break;
                case "oldest":
                    sql += "ORDER BY b.publish_date ASC";
                    break;
                default: // alphabetical
                    sql += "ORDER BY b.title ASC";
            }

            ps = conn.prepareStatement(sql);
            ps.setString(1, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setPublisher(rs.getString("publisher"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setImageCover(rs.getString("image_cover"));
                book.setAverageRating(rs.getDouble("avg_rating"));
                book.setViewCount(rs.getInt("view_count"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return books;
    }

    // Record a book view (add to book_history when user views book details)
    public boolean recordBookView(int bookId, int memberId) {
        try {
            String sql = "INSERT INTO book_history (book_id, member_id) VALUES (?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bookId);
            ps.setInt(2, memberId);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResources();
        }
    }

    // Get book details by ID
    public Book getBookById(int id) {
        book = null;
        try {
            String sql = "SELECT b.*, c.name as category_name, AVG(bh.rating) as avg_rating, COUNT(bh.id) as view_count " +
                    "FROM books b " +
                    "JOIN categories c ON b.category_id = c.id " +
                    "LEFT JOIN book_history bh ON b.id = bh.book_id " +
                    "WHERE b.id = ? " +
                    "GROUP BY b.id";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                book = new Book();
                book.setId(rs.getInt("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setCategoryId(rs.getInt("category_id"));
                book.setCategoryName(rs.getString("category_name"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setPublisher(rs.getString("publisher"));
                book.setSynopsis(rs.getString("synopsis"));
                book.setImageCover(rs.getString("image_cover"));
                book.setAverageRating(rs.getDouble("avg_rating"));
                book.setViewCount(rs.getInt("view_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return book;
    }

    // Helper method to close resources
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
