package main.java.qmegamax.maxi.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static main.java.qmegamax.maxi.Main.PATH;

public class EditReservationPage extends JFrame implements ActionListener {

    private JTable table;
    private JLabel label;

    public EditReservationPage(){
        this.setTitle("Edit Reservations");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        label = new JLabel("Edit Reservations");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));

        String[][] data = {
                { "Kundan Kumar Jha", "4031", "CSE" },
                { "Anand Jha", "6014", "IT" }
        };

        String[] columnNames = { "Name", "Roll Number", "Department" };

        table = new JTable(data, columnNames);
        table.setBounds(30, 40, 200, 300);

        JScrollPane scrollPane = new JScrollPane(table);

        JButton button = new JButton("Edit entry");
        button.addActionListener(this);

        panel.add(label,gbc);
        panel.add(scrollPane,gbc);
        panel.add(button,gbc);
        this.add(panel);
        this.setSize(400,400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("submit")) {
            //label.setText(textField1.getText());
           // textField1.setText("        ");
        }
    }
}
