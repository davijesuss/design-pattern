package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Cliente {

    public static void main(String[] args) {
        
        try {
            Connection conexao = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbigreja?useTimezone=true&serverTimezone=UTC");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
