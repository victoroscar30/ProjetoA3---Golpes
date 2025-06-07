package ui;

import database.Conexao;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

public class GraficoUsuariosPanel extends JPanel {

    public GraficoUsuariosPanel() {
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        String sql = """
            SELECT DATE(data_criacao) as data, COUNT(*) as total
            FROM usuarios
            WHERE data_criacao >= NOW() - INTERVAL 30 DAY
            GROUP BY DATE(data_criacao)
            ORDER BY DATE(data_criacao)
        """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String data = rs.getDate("data").toLocalDate()
                        .format(DateTimeFormatter.ofPattern("dd/MM"));
                int total = rs.getInt("total");
                dataset.addValue(total, "Cadastros", data);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao gerar gráfico de cadastros: " + e.getMessage());
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "Fluxo de Cadastros de Usuários (últimos 30 dias)",
                "Data",
                "Nº de Cadastros",
                dataset,
                PlotOrientation.VERTICAL,
                false, true, false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }
}
