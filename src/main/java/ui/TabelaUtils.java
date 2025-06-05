package ui;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class TabelaUtils {

    public static DefaultTableModel construirTabela(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int colunas = metaData.getColumnCount();

        DefaultTableModel model = new DefaultTableModel();

        // Adiciona nomes das colunas
        for (int i = 1; i <= colunas; i++) {
            model.addColumn(metaData.getColumnLabel(i));
        }

        // Adiciona os dados
        while (rs.next()) {
            Object[] row = new Object[colunas];
            for (int i = 1; i <= colunas; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        return model;
    }
}
