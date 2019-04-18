package com.company;

import java.sql.SQLException;

public class AccountTesting {

    /**
     * Serves as the entry point for the sample program.<p>
     *
     * @param args
     *     ignored
     */
    public static void main (String[] args) throws InterruptedException {

        DatabaseConnection db= DatabaseConnection.getInstance();

        final Account[] bank = { new Account("A"), new Account("B"), new Account("C") };

        bank[0].setBalance();
        bank[1].setBalance();
        bank[2].setBalance();

        // no deadlock testing
        int NUM_TRANSFERS = 5;
        for( int i = 0; i < NUM_TRANSFERS; i++ ) {
            double amountA = 20;
            System.out.printf("Transferring $%.2f from Account A to Account B.\n", amountA);
            System.out.println("Account A previous balance: " + bank[0].getBalance());
            System.out.println("Account B previous balance: " + bank[1].getBalance());
            Thread a = new Thread(() -> {
                try {
                    bank[0].transferTo(bank[0], bank[1], amountA);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            double amountB = 30;
            System.out.printf("Transferring $%.2f from Account B to Account A.\n", amountB);
            System.out.println("Account A previous balance: " + bank[0].getBalance());
            System.out.println("Account B previous balance: " + bank[1].getBalance());
            Thread b = new Thread(() -> {
                try {
                    bank[1].transferTo(bank[1], bank[0], amountB);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            a.start();
            b.start();
            a.join();
            b.join();
        }
        // no concurrent testing
        for( int j = 0; j < NUM_TRANSFERS; j++ ) {
            double amountC = 20;
            System.out.printf("Transferring $%.2f from Account A to Account B.\n", amountC);
            System.out.println("Account A previous balance: " + bank[0].getBalance());
            System.out.println("Account B previous balance: " + bank[1].getBalance());
            Thread c = new Thread(() -> {
                try {
                    bank[0].transferTo(bank[0], bank[1], amountC);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            c.start();
            double amountD = 30;
            System.out.printf("Transferring $%.2f from Account B to Account A.\n", amountD);
            System.out.println("Account A previous balance: " + bank[0].getBalance());
            System.out.println("Account B previous balance: " + bank[1].getBalance());
            Thread d = new Thread(() -> {
                try {
                    bank[1].transferTo(bank[1], bank[0], amountD);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            d.start();

            double amountE = 15;
            System.out.printf("Transferring $%.2f from Account C to Account A.\n", amountE);
            System.out.println("Account A previous balance: " + bank[0].getBalance());
            System.out.println("Account C previous balance: " + bank[1].getBalance());
            Thread e = new Thread(() -> {
                try {
                    bank[2].transferTo(bank[2], bank[0], amountE);
                } catch (SQLException f) {
                    f.printStackTrace();
                }
            });

            e.start();
        }
    }
}
