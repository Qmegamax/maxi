package main.java.qmegamax.maxi.pages;

import com.mysql.cj.protocol.InternalTime;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import main.java.qmegamax.maxi.Reservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import static main.java.qmegamax.maxi.Main.*;

public class PendingReservationsPage extends JFrame{

    private JTable table1;
    private DefaultTableModel model;
    private JTable table2;

    public PendingReservationsPage(){
        this.setTitle("Reservations Status");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JPanel subPanel = new JPanel(new GridBagLayout());
        panel.add(subPanel,gbc);

        JLabel label1 = new JLabel("Reservations for ");
        subPanel.add(label1);

        JDateChooser dateChooser = new JDateChooser(new Date());
        dateChooser.setMinSelectableDate(new Date());
        dateChooser.setMaxSelectableDate(new Date(new Date().getTime()+ 604800000L));
        dateChooser.setPreferredSize(new Dimension(100,20));
        subPanel.add(dateChooser,gbc);

        model=new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        model.addColumn("Time");
        model.addColumn("Name");
        model.addColumn("Notes");
        model.addColumn("Table");
        table1 = new JTable(model){
            public String getToolTipText( MouseEvent e )
            {
                int row = rowAtPoint( e.getPoint() );
                int column = columnAtPoint( e.getPoint() );

                Object value = getValueAt(row, column);
                return value == null ? null : value.toString();
            }
        };
        table1.getColumnModel().getColumn(0).setPreferredWidth(10);
        table1.getColumnModel().getColumn(1).setPreferredWidth(80);
        table1.getColumnModel().getColumn(2).setPreferredWidth(100);
        table1.getColumnModel().getColumn(3).setPreferredWidth(10);
        JScrollPane scrollPane1 = new JScrollPane(table1);
        panel.add(scrollPane1,gbc);

        JButton button1 = new JButton("refresh");
        button1.addActionListener(e -> {
            String s = e.getActionCommand();
            if (s.equals("refresh")) {

                ArrayList<Integer> tables=new ArrayList<>();

                ArrayList<Reservation> reservations=new ArrayList<>();
                for(Reservation reservation:GetReservationsFromDatabase()){
                    Date date = dateChooser.getDate();
                    if(date.getYear()+1900!=reservation.date.getYear() || date.getMonth()+1!=reservation.date.getMonthValue() || date.getDate()!=reservation.date.getDayOfMonth())continue;

                    reservations.add(reservation);
                }

                reservations.sort((d1,d2) -> (d1.date.getHour()*60+d1.date.getMinute())>(d2.date.getHour()*60+d2.date.getMinute())?1:-1);

                int currentRow=0;
                DefaultTableModel resetModel = (DefaultTableModel) table1.getModel();
                resetModel.setRowCount(0);
                for(Reservation reservation:reservations){

                    if(table1.getRowCount()<currentRow+1) {
                        TableModel model = table1.getModel();
                        DefaultTableModel defaultTableModel = (DefaultTableModel) model;
                        defaultTableModel.addRow(new Object[]{"-", "-", "-", "-"});
                    }

                    String currentTime = new SimpleDateFormat("HH-mm").format(Calendar.getInstance().getTime());
                    String reservationTime = String.valueOf(reservation.date).split("T")[1];
                    int currentTimeInt=Integer.valueOf(currentTime.split("-")[0])*60+Integer.valueOf(currentTime.split("-")[1]);
                    int reservationTimeInt=Integer.valueOf(reservationTime.split(":")[0])*60+Integer.valueOf(reservationTime.split(":")[1]);
                    if(currentTimeInt>reservationTimeInt && currentTimeInt<=reservationTimeInt+OCUPATIONTIME)tables.add(reservation.table);

                    table1.setValueAt(reservationTime,currentRow,0);
                    table1.setValueAt(reservation.name,currentRow,1);
                    table1.setValueAt(reservation.notes,currentRow,2);
                    table1.setValueAt(String.valueOf(reservation.table),currentRow,3);
                    currentRow++;
                }

                for(int i=0;i<TABLEAMOUNT;i++){
                    table2.setValueAt("Free",i,1);
                }
                for(int table:tables){
                    table2.setValueAt("Occupied",table-1,1);
                }
            }
        });
        panel.add(button1,gbc);

        JLabel label2 = new JLabel("Current table status:");
        panel.add(label2,gbc);

        String[][] data=new String[TABLEAMOUNT][2];
        for(int i=0;i<data.length;i++){
            data[i][0]=Integer.toString(i+1);
            data[i][1]="Free";
        }
        DefaultTableModel model2=new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        model2.setDataVector(data,new String[]{"Table","Status"});
        table2 = new JTable(model2);
        JScrollPane scrollPane2 = new JScrollPane(table2);
        scrollPane2.setPreferredSize(new Dimension(200,240));
        panel.add(scrollPane2,gbc);

        JButton button2 = new JButton("Add a reservation");
        JFrame tempFrame=this;
        button2.addActionListener(e -> {
            new AddReservationPage(true);
            tempFrame.setVisible(false);
            tempFrame.dispose();
        });
        panel.add(button2,gbc);


        this.add(panel);
        this.setSize(600,1200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
