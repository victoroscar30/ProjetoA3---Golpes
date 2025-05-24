package database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.Set;

public class InsereCSV {

    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://localhost:3306/projeto_golpes";
        String username = "gustavinho";
        String password = "123456";

        String csvFilePath = "C:/Users/Gustavo/Downloads/a3 bora.csv";

        // Define allowed ENUM values
        Set<String> allowedClassificacoes = Set.of("segura", "suspeita", "phishing", "desconhecida");

        try (Connection connection = DriverManager.getConnection(jdbcURL, username, password)) {
            connection.setAutoCommit(false);

            String sql = "INSERT INTO urls (url, dominio, classificacao) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;

            int count = 0;

            // Skip header line
            lineReader.readLine();

            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(";", -1);  // -1 keeps trailing empty strings

                // Defensive: Trim all fields
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }

                // Map columns
                String url = data[3];
                String dominio = data[2];
                String classificacao = data[1];

                // Fix ENUM inconsistencies
                if ("desconhecido".equalsIgnoreCase(classificacao)) {
                    classificacao = "desconhecida";
                } else if ("malware".equalsIgnoreCase(classificacao)) {
                    classificacao = "suspeita";
                } else if (classificacao == null || classificacao.isEmpty()) {
                    classificacao = "desconhecida";
                }

                // Validate ENUM
                if (!allowedClassificacoes.contains(classificacao)) {
                    System.out.println("⚠️ Invalid classificacao: " + classificacao + " at row " + (count + 1) + ". Defaulting to 'desconhecida'.");
                    classificacao = "desconhecida";
                }

                // Debug log (optional)
                System.out.println("Inserting row: url=" + url + ", dominio=" + dominio + ", classificacao=" + classificacao);

                // Insert
                statement.setString(1, url);
                statement.setString(2, dominio);
                statement.setString(3, classificacao);
                statement.addBatch();

                // Batch execution every 1000 rows
                if (count % 1000 == 0 && count > 0) {
                    statement.executeBatch();
                }

                count++;
            }

            lineReader.close();

            // Execute remaining batch
            statement.executeBatch();

            connection.commit();

            System.out.println("✅ Data import completed successfully! Total rows: " + count);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("❌ Error occurred during import.");
        }
    }
}
