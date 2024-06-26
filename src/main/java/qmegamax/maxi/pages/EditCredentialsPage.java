package main.java.qmegamax.maxi.pages;

import main.java.qmegamax.maxi.Main;
import main.java.qmegamax.maxi.util.ButtonRenderer;
import main.java.qmegamax.maxi.util.Credential;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.java.qmegamax.maxi.Main.*;

public class EditCredentialsPage extends JFrame{
    public static EditCredentialsPage current;
    JButton refreshButton;

    public EditCredentialsPage(){
        this.setTitle("Edit credentials");
        Main.setImage(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel subPanel = new JPanel(new GridBagLayout());
        panel.add(subPanel,gbc);

        JLabel label1 = new JLabel("All credentials:");
        subPanel.add(label1);

        DefaultTableModel defaultTableModel=new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {return column==5;}
        };
        defaultTableModel.addColumn("Id");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Email");
        defaultTableModel.addColumn("Password");
        defaultTableModel.addColumn("Type");
        defaultTableModel.addColumn("Edit");

        refreshButton = new JButton("Refresh");

        JTable table1 = new JTable(defaultTableModel){
            public String getToolTipText( MouseEvent e )
            {
                int row = rowAtPoint( e.getPoint() );
                int column = columnAtPoint( e.getPoint() );

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
        table1.getColumn("Edit").setCellRenderer(new ButtonRenderer());
        table1.getColumn("Edit").setCellEditor(
                new ButtonEditor1(new JCheckBox(),refreshButton));
        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setPreferredSize(new Dimension(700,700));
        panel.add(scrollPane1,gbc);

        refreshButton.addActionListener(e -> {
            ArrayList<Credential> credentials=GetDataFromDatabase("credentials",Credential.getEmpty());

            int currentRow=0;
            DefaultTableModel resetModel = (DefaultTableModel) table1.getModel();
            resetModel.setRowCount(0);
            for(Credential credential:credentials) {

                if (table1.getRowCount() < currentRow + 1) {
                    TableModel model = table1.getModel();
                    DefaultTableModel defaultTableModel1 = (DefaultTableModel) model;
                    defaultTableModel1.addRow(new Object[]{"-","-", "-", "-", "-", "Edit"});
                }

                table1.setValueAt(credential.id, currentRow, 0);
                table1.setValueAt(credential.name, currentRow, 1);
                table1.setValueAt(credential.email, currentRow, 2);
                table1.setValueAt(credential.password, currentRow, 3);
                table1.setValueAt(credential.type, currentRow, 4);
                currentRow++;
            }
        });
        panel.add(refreshButton,gbc);

        JButton btnNewButton1 = new JButton("Add a user");
        btnNewButton1.addActionListener(e -> {
            new AddUserPage();
        });
        panel.add(btnNewButton1,gbc);

        JButton btnNewButton = new JButton("Back");
        JFrame frame=this;
        btnNewButton.addActionListener(e -> {
            new ConfirmReservationsPage();
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(btnNewButton,gbc);

        this.add(panel);
        this.setSize(800,1200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        current=this;
    }
}
class ButtonEditor1 extends DefaultCellEditor {
    protected JButton button;
    protected JButton refreshButton;
    private String label;

    public ButtonEditor1(JCheckBox checkBox,JButton refreshButton) {
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

        new EditCurrentCredentialPage(GetDataFromDatabase("credentials",Credential.getEmpty()).get(row));

        refreshButton.doClick();
        return button;
    }

    public Object getCellEditorValue() {
        return new String(label);
    }
}