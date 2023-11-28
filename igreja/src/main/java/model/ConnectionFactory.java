package model;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public static Connection getConnection(String tipoBanco) {
        Connection connection = null;

        switch (tipoBanco) {
            case "pg":
                try {
                    
                    connection = DriverManager.getConnection("url_pg");
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao conectar ao banco PostgreSQL", e);
                }
                break;

            case "mysql":
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbigreja?useTimezone=true&serverTimezone=UTC");
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao conectar ao banco MySQL", e);
                }
                break;

            default:
                throw new RuntimeException("Tipo de banco de dados não suportado: " + tipoBanco);
        }

        return connection;
    }
}
