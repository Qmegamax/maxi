package main.java.qmegamax.maxi.pages;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static main.java.qmegamax.maxi.Main.PATH;

public class AddReservationPage extends JFrame implements ActionListener {

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JLabel label;

    public AddReservationPage(){
        this.setTitle("Add Reservation");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        label = new JLabel("Add a reservation:");

        textField1 = new JTextField("time(xx/xx/xxxx yy/yy)",16);

        textField2 = new JTextField("name",16);
        textField3 = new JTextField("notes",16);
        textField4 = new JTextField("table",16);

        JButton button = new JButton("submit");
        button.addActionListener(this);

        panel.add(label,gbc);
        panel.add(textField1,gbc);
        panel.add(textField2,gbc);
        panel.add(textField3,gbc);
        panel.add(textField4,gbc);
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
