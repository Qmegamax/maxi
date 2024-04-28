package main.java.qmegamax.maxi.pages;

import main.java.qmegamax.maxi.util.Reservation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static main.java.qmegamax.maxi.Main.*;
import static main.java.qmegamax.maxi.Main.GetReservationsFromDatabase;

public class EditReservationsPage extends JFrame{

    JButton refreshButton;

    public EditReservationsPage(){
        this.setTitle("Edit reservations");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel subPanel = new JPanel(new GridBagLayout());
        panel.add(subPanel,gbc);

        JLabel label1 = new JLabel("All reservations:");
        subPanel.add(label1);

        DefaultTableModel defaultTableModel=new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {return column==5;}
        };
        defaultTableModel.addColumn("Id");
        defaultTableModel.addColumn("Time");
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Notes");
        defaultTableModel.addColumn("Table");
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
        table1.getColumn("Edit").setCellRenderer(new ButtonRenderer2());
        table1.getColumn("Edit").setCellEditor(
                new ButtonEditor2(new JCheckBox(),refreshButton));
        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setPreferredSize(new Dimension(700,700));
        panel.add(scrollPane1,gbc);

        refreshButton.addActionListener(e -> {
            ArrayList<Reservation> reservations=GetReservationsFromDatabase();

            reservations.sort((d1,d2) -> d1.date.compareTo(d2.date));

            int currentRow=0;
            DefaultTableModel resetModel = (DefaultTableModel) table1.getModel();
            resetModel.setRowCount(0);
            for(Reservation reservation:reservations) {

                if (table1.getRowCount() < currentRow + 1) {
                    TableModel model = table1.getModel();
                    DefaultTableModel defaultTableModel1 = (DefaultTableModel) model;
                    defaultTableModel1.addRow(new Object[]{"-","-", "-", "-", "-", "Edit"});
                }

                table1.setValueAt(reservation.id, currentRow, 0);
                table1.setValueAt(reservation.date.toString(), currentRow, 1);
                table1.setValueAt(reservation.name, currentRow, 2);
                table1.setValueAt(reservation.notes, currentRow, 3);
                table1.setValueAt(String.valueOf(reservation.table), currentRow, 4);
                currentRow++;
            }
        });
        panel.add(refreshButton,gbc);

        JButton btnNewButton1 = new JButton("Add a reservation");
        btnNewButton1.addActionListener(e -> {
            new AddReservationPage(false);
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
    }
}

class ButtonRenderer2 extends JButton implements TableCellRenderer {

    public ButtonRenderer2() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

class ButtonEditor2 extends DefaultCellEditor {
    protected JButton button;
    protected JButton refreshButton;
    private String label;

    public ButtonEditor2(JCheckBox checkBox,JButton refreshButton) {
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

        Reservation reservation=GetReservationsFromDatabase().get(0);
        for(Reservation everyReservation:GetReservationsFromDatabase()){
            if(everyReservation.id==Integer.parseInt(table.getValueAt(row,0).toString())){reservation=everyReservation;break;}
        }
        new EditCurrentReservationPage(reservation);

        refreshButton.doClick();
        return button;
    }

    public Object getCellEditorValue() {
        return new String(label);
    }
}