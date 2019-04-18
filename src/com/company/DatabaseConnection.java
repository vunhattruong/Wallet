package com.company;

public class DatabaseConnection {
    private static DatabaseConnection jdbc;

    //JDBCSingleton prevents the instantiation from any other class.
    private DatabaseConnection() {
    }

    //Now we are providing global point of access.
    public static DatabaseConnection getInstance() {
        if (jdbc == null) {
            jdbc = new DatabaseConnection();
        }
        return jdbc;
    }
}
