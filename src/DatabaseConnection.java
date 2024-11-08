import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    public static Connection getConnection() {
        // Carregar o arquivo config.properties
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Ler o tipo de banco de dados (1 = MySQL, 2 = PostgreSQL)
        String dbType = properties.getProperty("db.type");

        // Variáveis para armazenar a URL de conexão, usuário e senha
        String url = "";
        String username = "";
        String password = "";

        if ("1".equals(dbType)) {
            // Configurações para o MySQL
            url = properties.getProperty("mysql.url");
            username = properties.getProperty("mysql.username");
            password = properties.getProperty("mysql.password");
        } else if ("2".equals(dbType)) {
            // Configurações para o PostgreSQL
            url = properties.getProperty("postgresql.url");
            username = properties.getProperty("postgresql.username");
            password = properties.getProperty("postgresql.password");
        } else {
            System.out.println("Tipo de banco de dados inválido no arquivo de configuração.");
            return null;
        }

        // Tente conectar ao banco de dados
        try {
            if ("1".equals(dbType)) {
                // Conectar ao MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                return DriverManager.getConnection(url, username, password);
            } else if ("2".equals(dbType)) {
                // Conectar ao PostgreSQL
                Class.forName("org.postgresql.Driver");
                return DriverManager.getConnection(url, username, password);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
