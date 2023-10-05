package DAO;

import java.sql.*;

import Model.Account;
import Util.ConnectionUtil; 

public class SocialMediaAccountDAO {

    // ------------------------------- SELECTS ----------------------------

    public Account returnAccountById(int accountId) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE account_id=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account returnedAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Account loginAccount(Account account) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username=? AND password=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account returnedAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public Account returnAccountByUsername(String username) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM account WHERE username=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Account returnedAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public boolean accountExists(String username) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT COUNT(*) AS cnt FROM account WHERE username=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int howMany = rs.getInt("cnt");
                if (howMany > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean accountExistsId(int accountId) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "SELECT COUNT(*) AS cnt FROM account WHERE account_id=?;";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int howMany = rs.getInt("cnt");
                if (howMany > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    // ----------------------------- INSERTS-----------------------------------

    public Account createAccount(Account account) {
        Connection Conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO account (username, password) VALUES (?,?)";
        try {
            PreparedStatement preparedStatement = Conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.executeUpdate();
            ResultSet keyResultSet = preparedStatement.getGeneratedKeys();
            if (keyResultSet.next()) {
                int generated_account_id = (int) keyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

}
