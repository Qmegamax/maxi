package main.java.qmegamax.maxi.pages;

import main.java.qmegamax.maxi.util.ButtonRenderer;
import main.java.qmegamax.maxi.util.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static main.java.qmegamax.maxi.Main.*;

public class ConfirmReservationsPage extends JFrame {

    JButton refreshButton;

    public ConfirmReservationsPage() {
        this.setTitle("Confirm reservations");
        ImageIcon img = new ImageIcon(PATH + "icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel subPanel = new JPanel(new GridBagLayout());
        panel.add(subPanel, gbc);

        JLabel label1 = new JLabel("Pending reservations:");
        subPanel.add(label1);

        DefaultTableModel defaultTableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5 || column == 6;
            }
        };
        defaultTableModel.addColumn("Id");
        defaultTableModel.addColumn("Time");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Notes");
        defaultTableModel.addColumn("Table");
        defaultTableModel.addColumn("Confirm");
        defaultTableModel.addColumn("Deny");

        refreshButton = new JButton("Refresh");

        JTable table1 = new JTable(defaultTableModel) {
            public String getToolTipText(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                int column = columnAtPoint(e.getPoint());

                Object value = getValueAt(row, column);
                return value == null ? null : value.toString();
            }
        };
        table1.getColumnModel().getColumn(0).setPreferredWidth(20);
        table1.getColumnModel().getColumn(1).setPreferredWidth(70);
        table1.getColumnModel().getColumn(2).setPreferredWidth(90);
        table1.getColumnModel().getColumn(3).setPreferredWidth(100);
        table1.getColumnModel().getColumn(4).setPreferredWidth(10);
        table1.getColumnModel().getColumn(5).setPreferredWidth(20);
        table1.getColumnModel().getColumn(6).setPreferredWidth(20);
        table1.getColumn("Confirm").setCellRenderer(new ButtonRenderer());
        table1.getColumn("Confirm").setCellEditor(
                new ButtonEditor(new JCheckBox(), refreshButton));
        table1.getColumn("Deny").setCellRenderer(new ButtonRenderer());
        table1.getColumn("Deny").setCellEditor(
                new ButtonEditor(new JCheckBox(), refreshButton));
        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setPreferredSize(new Dimension(740, 800));
        panel.add(scrollPane1, gbc);

        refreshButton.addActionListener(e -> {
            ArrayList<Reservation> reservations = GetDataFromDatabase("pendingReservations",Reservation.getEmpty());

            reservations.sort((d1, d2) -> d1.date.compareTo(d2.date));

            int currentRow = 0;
            DefaultTableModel resetModel = (DefaultTableModel) table1.getModel();
            resetModel.setRowCount(0);
            for (Reservation reservation : reservations) {

                if (table1.getRowCount() < currentRow + 1) {
                    TableModel model = table1.getModel();
                    DefaultTableModel defaultTableModel1 = (DefaultTableModel) model;
                    defaultTableModel1.addRow(new Object[]{"-", "-", "-", "-", "-", "Confirm", "Deny"});
                }

                table1.setValueAt(reservation.id, currentRow, 0);
                table1.setValueAt(reservation.date.toString(), currentRow, 1);
                table1.setValueAt(reservation.name, currentRow, 2);
                table1.setValueAt(reservation.notes, currentRow, 3);
                table1.setValueAt(String.valueOf(reservation.table), currentRow, 4);
                currentRow++;
            }
        });
        panel.add(refreshButton, gbc);

        JButton btnNewButton = new JButton("Edit credentials");
        JFrame frame = this;
        btnNewButton.addActionListener(e -> {
            new EditCredentialsPage();
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(btnNewButton, gbc);

        JButton btnNewButton_1 = new JButton("Edit reservations");
        btnNewButton_1.addActionListener(e -> {
            new EditReservationsPage();
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(btnNewButton_1, gbc);

        this.add(panel);
        this.setSize(860, 1200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    protected JButton refreshButton;
    private String label;

    public ButtonEditor(JCheckBox checkBox,JButton refreshButton) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        this.refreshButton=refreshButton;
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = value.toString();
        button.setText(label);

        try {
        String SQL = "DELETE FROM pendingReservations WHERE reservationId = ? ";
        PreparedStatement pstmt = CONNECTION.prepareStatement(SQL);
        pstmt.setString(1, String.valueOf(table.getValueAt(row,0)));
        pstmt.executeUpdate();

        } catch (SQLException e) {System.out.println("uhoh");}

        if(column==5){
            try {
                String sql = " insert into reservations (reservationId, time, name, notes, tableId)"
                        + " values (?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = CONNECTION.prepareStatement(sql);
                ArrayList<Reservation> reservations=GetDataFromDatabase("reservations",Reservation.getEmpty());
                preparedStmt.setInt (1, reservations.isEmpty()?1:reservations.get(reservations.size()-1).id+1);
                preparedStmt.setString (2, table.getValueAt(row,1).toString());
                preparedStmt.setString   (3, table.getValueAt(row,2).toString());
                preparedStmt.setString(4, table.getValueAt(row,3).toString());
                preparedStmt.setInt    (5, Integer.parseInt(table.getValueAt(row,4).toString()));

                preparedStmt.execute();
            }catch (Exception ex) {System.out.println("uhoh");}
        }

        refreshButton.doClick();
        return button;
    }

    public Object getCellEditorValue() {
        return new String(label);
    }
}
