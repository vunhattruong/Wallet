package com.company;

import java.io.*;
import java.sql.*;

/**
 */
public class Account implements IAccount, Serializable {
    private final String name;
    private       double balance;

    public Account (String name) {
        this.name = name;
    }

    /**
     * Returns this account's balance.<p>
     */
    public double getBalance () {
        return balance;
    }

    /**
     * Sets this account's balance.<p>
     */
    public void setBalance () {
        balance = 100.00;
    }

    private static Connection getConnection() throws ClassNotFoundException, SQLException {

        Connection con = null;
        String url = "jdbc:mysql://127.0.0.1:3306/wallet?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=GMT&useSSL=false";
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection(url, "root", "root");
        return con;

    }

    /**
     * Deposits the specified amount to the specified account.<p>
     *
     * @param amount
     *     amount to deposit (may be negative)
     */
    public synchronized void deposit(double amount) throws SQLException {
        System.out.println("Depositing " + amount + " to Account " + name);
        double newBalance = balance + amount;                    // adjust the balance
        System.out.println("Then Account " + name + " has new balance is " + newBalance);
        balance = newBalance;

        Connection c = null;

        PreparedStatement ps = null;
        try {
            c = this.getConnection();
            ps = c.prepareStatement(" update account set balance=? where account_name='" + name + "' ");
            ps.setDouble(1, balance);
            ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Updated successfully to DB !");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                ps.close();
            }
            if (c != null) {
                c.close();
            }
        }
    }

    /**
     * Withdraws the specified amount to the specified account.<p>
     *
     * @param amount
     *     amount to Withdraw (may be negative)
     */
    public synchronized void withdraw (double amount) throws SQLException {
        System.out.println(Thread.currentThread().getName() + " is trying to withdraw amount: " + amount + " from account: " + name);
        if (balance < amount) {
            System.out.println("Not enough balance in account: " + name + " to withdraw");
            System.out.println("returning zero to: " + Thread.currentThread().getName());
        }
        double newBalance = balance - amount;                 // adjust the balance
        System.out.println(Thread.currentThread().getName() + " successfully withdraw amount: " + amount + " from account: " + name);
        balance = newBalance;
        System.out.println("Updated balance of Account " + name + " is: " + balance);

        Connection c = null;

        PreparedStatement ps = null;
        try {
            c = this.getConnection();
            ps = c.prepareStatement(" update account set balance=? where account_name='" + name + "' ");
            ps.setDouble(1, balance);
            ps.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Updated successfully to DB !");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (ps != null) {
                ps.close();
            }
            if (c != null) {
                c.close();
            }
        }
    }

    public void transferTo(Account accountFrom, Account accountTo, double amount) throws SQLException {
        accountFrom.withdraw(amount);
        accountTo.deposit(amount);
    }

    @Override
    public String toString() {
        return name;
    }
}