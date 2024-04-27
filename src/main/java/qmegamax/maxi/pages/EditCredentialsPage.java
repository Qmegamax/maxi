package main.java.qmegamax.maxi.pages;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static main.java.qmegamax.maxi.Main.PATH;

public class EditCredentialsPage extends JFrame implements ActionListener {

    private JTextField textField1;
    private JTextField textField2;
    private JLabel label;

    public EditCredentialsPage(){
        this.setTitle("Login");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(50, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        label = new JLabel("Login");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));

        String[][] data = {
                { "Kundan Kumar Jha", "4031", "CSE" },
                { "Anand Jha", "6014", "IT" }
        };

        String[] columnNames = { "Name", "Roll Number", "Department" };

        //table = new JTable(data, columnNames);
        //table.setBounds(30, 40, 200, 300);

        //JScrollPane scrollPane = new JScrollPane(table);

        JButton button = new JButton("submit");
        button.addActionListener(this);

        panel.add(label,gbc);
        panel.add(textField1,gbc);
        panel.add(textField2,gbc);
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
            label.setText(textField1.getText());
            textField1.setText("        ");
        }
    }
}
