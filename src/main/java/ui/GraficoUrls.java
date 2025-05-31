package ui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static database.Conexao.conectar;

public class GraficoUrls extends JPanel {

    public GraficoUrls() {
        DefaultCategoryDataset dataset = carregarDados();

        JFreeChart chart = ChartFactory.createBarChart(
                "Classificação de URLs",
                "Classificação",
                "Quantidade",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        this.add(chartPanel);
    }

    private DefaultCategoryDataset carregarDados() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String sql = "SELECT classificacao, COUNT(*) AS total FROM urls GROUP BY classificacao";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String classificacao = rs.getString("classificacao");
                int total = rs.getInt("total");
                dataset.addValue(total, "URLs", classificacao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataset;
    }
}
