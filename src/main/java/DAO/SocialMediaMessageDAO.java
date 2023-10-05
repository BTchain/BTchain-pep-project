package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List; 

import Model.Message;
import Util.ConnectionUtil; 

public class SocialMediaMessageDAO {

    public List<Message> returnMessages() {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message;";
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;

    }

    public List<Message> returnMessByAccId(int postedBy) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE posted_by=?;";
        List<Message> messages = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setInt(1, postedBy);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;

    }

    public Message returnMessById(int messageId) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM message WHERE message_id=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                return message;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void deleteMessById(int messageId) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "DELETE * FROM message WHERE message_id=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateMessById(Message message) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text=?  FROM message WHERE message_id=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage_text());
            preparedStatement.setInt(2, message.getMessage_id());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Message createMessage(Message message) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
            preparedStatement.executeUpdate();
            ResultSet keyResultSet = preparedStatement.getGeneratedKeys();
            if (keyResultSet.next()) {
                int generated_message_id = (int) keyResultSet.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),
                        message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
