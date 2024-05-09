package main.java.qmegamax.maxi.pages;


import main.java.qmegamax.maxi.Main;
import main.java.qmegamax.maxi.util.Credential;
import main.java.qmegamax.maxi.util.DocumentSizeFilter;
import main.java.qmegamax.maxi.pages.errors.AddingReservationErrorPage;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import static main.java.qmegamax.maxi.Main.*;

public class AddUserPage extends JFrame{

    public AddUserPage(){
        this.setTitle("Add a user");
        Main.setImage(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(panel);

        JLabel label = new JLabel("Add a user");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));
        panel.add(label,gbc);

        JPanel box = new JPanel();
        box.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),new EmptyBorder(14, 10, 14, 10)));
        box.setLayout(new GridBagLayout());
        panel.add(box,gbc);

        JTextField textField1 = new JTextField("username",12);
        AbstractDocument abstractDocument1=(AbstractDocument)textField1.getDocument();
        abstractDocument1.setDocumentFilter(new DocumentSizeFilter(30));
        textField1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {if(textField1.getText().equals("username")) textField1.setText("");}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        box.add(textField1,gbc);

        JTextField textField2 = new JTextField("email",12);
        abstractDocument1.setDocumentFilter(new DocumentSizeFilter(20));
        textField2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {if(textField2.getText().equals("email")) textField2.setText("");}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        box.add(textField2,gbc);

        JTextField textField3 = new JTextField("password",12);
        AbstractDocument abstractDocument3=(AbstractDocument)textField3.getDocument();

        abstractDocument3.setDocumentFilter(new DocumentSizeFilter(40));
        textField3.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {if(textField3.getText().equals("password")) textField3.setText("");}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        box.add(textField3,gbc);

        JCheckBox checkBox = new JCheckBox("Aministrator");
        box.add(checkBox,gbc);

        JFrame frame=this;
        JButton button1 = new JButton("Add");
        button1.addActionListener(e -> {

            String name=textField1.getText();
            if(!name.matches("^[a-zA-Z\\s]+")){
                new AddingReservationErrorPage("Invalid name!");
                return;
            }

            String email=textField2.getText();
            if(!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                new AddingReservationErrorPage("Invalid email!");
                return;
            }

            String password=textField3.getText();

            try {
                String sql = " insert into credentials (userId, name, email, password, type)"
                        + " values (?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = CONNECTION.prepareStatement(sql);
                ArrayList<Credential> credentials=GetDataFromDatabase("credentials",Credential.getEmpty());
                preparedStmt.setInt (1, credentials.isEmpty()?1:credentials.get(credentials.size()-1).id+1);
                preparedStmt.setString (2, name);
                preparedStmt.setString (3, email);
                preparedStmt.setString (4,USEENCRYPTION?encryptPassword(password,ENCRYPTIONKEY):password);
                preparedStmt.setString (5, String.valueOf(checkBox.isSelected()? Credential.AccountType.ADMIN:Credential.AccountType.USER));

                preparedStmt.execute();

            }catch (Exception ex) {System.out.println("uhoh");}

            EditCredentialsPage.current.refreshButton.doClick();
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button1,gbc);

        JButton button2 = new JButton("Back");
        button2.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button2,gbc);

        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
