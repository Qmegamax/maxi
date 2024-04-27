package main.java.qmegamax.maxi.pages;

import com.mysql.cj.protocol.InternalTime;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

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

        JLabel label1 = new JLabel("Reservations for today:");

        model=new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        model.addColumn("Time");
        model.addColumn("Name");
        model.addColumn("Notes");
        model.addColumn("Table");
        table1 = new JTable(model);
        table1.setBounds(100, 100, 200, 300);

        JScrollPane scrollPane1 = new JScrollPane(table1);

        JButton button1 = new JButton("refresh");
        button1.addActionListener(e -> {
            String s = e.getActionCommand();
            if (s.equals("refresh")) {

                ArrayList<String[]> data=getData();
                ArrayList<Integer> tables=new ArrayList<>();

                for(int i=0;i<data.size();i++){
                    String[] info=data.get(i);
                    if(table1.getRowCount()<i+1) {
                        TableModel model = table1.getModel();
                        DefaultTableModel defaultTableModel = (DefaultTableModel) model;
                        defaultTableModel.addRow(new Object[]{"-", "-", "-", "-"});
                    }

                    String currentTime = new SimpleDateFormat("HH-mm").format(Calendar.getInstance().getTime());
                    int currentTimeInt=Integer.valueOf(currentTime.split("-")[0])*60+Integer.valueOf(currentTime.split("-")[1]);
                    int reservationTimeInt=Integer.valueOf(info[0].split(":")[0])*60+Integer.valueOf(info[0].split(":")[1]);
                    if(currentTimeInt>reservationTimeInt && currentTimeInt<=reservationTimeInt+OCUPATIONTIME)tables.add(Integer.valueOf(info[3]));

                    for(int j=0;j<4;j++){
                        table1.setValueAt(info[j],i,j);
                    }
                }

                System.out.println(tables.size());
                for(int i=0;i<TABLEAMOUNT;i++){
                    table2.setValueAt("Free",i,1);
                }
                for(int i:tables){
                    table2.setValueAt("Occupied",i,1);
                }
            }
        });

        JLabel label2 = new JLabel("Table status:");

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
        table2.setBounds(30, 40, 200, 300);

        JScrollPane scrollPane2 = new JScrollPane(table2);

        JButton button2 = new JButton("refresh");
        JFrame tempFrame=this;
        button2.addActionListener(e -> {
            AddReservationPage addReservationPage=new AddReservationPage();
            tempFrame.setVisible(false);
            tempFrame.dispose();
        });

        panel.add(label1,gbc);
        panel.add(scrollPane1,gbc);
        panel.add(button1,gbc);
        panel.add(label2,gbc);
        panel.add(scrollPane2,gbc);
        panel.add(button2,gbc);
        this.add(panel);
        this.setSize(600,1200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public ArrayList<String[]> getData() {
        ArrayList<String[]> data = new ArrayList<>();

        try {
            Statement statement = CONNECTION.createStatement();
            ResultSet resultSet = statement.executeQuery("select time, name, notes, seatingTable from reservations");

            while (resultSet.next()) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

                if(resultSet.getString("time").split(" ")[0].equals(currentDate)){
                    data.add(new String[]{resultSet.getString("time").split(" ")[1], resultSet.getString("name"),resultSet.getString("notes"),Integer.toString(resultSet.getInt("seatingTable"))});
                }
                }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
