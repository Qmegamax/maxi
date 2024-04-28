package main.java.qmegamax.maxi.pages;

import main.java.qmegamax.maxi.Credential;
import main.java.qmegamax.maxi.DocumentSizeFilter;
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
import java.sql.SQLException;
import java.util.ArrayList;

import static main.java.qmegamax.maxi.Main.*;

public class EditCurrentUserPage extends JFrame
{
    public EditCurrentUserPage(Credential credential){
        this.setTitle("Add a user");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(panel);

        JLabel label = new JLabel("Edit a user");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));
        panel.add(label,gbc);

        JPanel box = new JPanel();
        box.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),new EmptyBorder(14, 10, 14, 10)));
        box.setLayout(new GridBagLayout());
        panel.add(box,gbc);

        JTextField textField1 = new JTextField(credential.name,12);
        AbstractDocument abstractDocument1=(AbstractDocument)textField1.getDocument();

        abstractDocument1.setDocumentFilter(new DocumentSizeFilter(30));
        box.add(textField1,gbc);

        JTextField textField2 = new JTextField(credential.email,12);
        AbstractDocument abstractDocument2=(AbstractDocument)textField2.getDocument();

        abstractDocument1.setDocumentFilter(new DocumentSizeFilter(20));
        box.add(textField2,gbc);

        JTextField textField3 = new JTextField(USEENCRYPTION?decryptPassword(credential.password,ENCRYPTIONKEY):credential.password,12);
        AbstractDocument abstractDocument3=(AbstractDocument)textField3.getDocument();

        abstractDocument3.setDocumentFilter(new DocumentSizeFilter(40));
        box.add(textField3,gbc);

        JCheckBox checkBox = new JCheckBox("Aministrator");
        checkBox.setSelected(credential.type== Credential.AccountType.ADMIN);
        box.add(checkBox,gbc);

        JFrame frame=this;
        JButton button1 = new JButton("Save");
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
                String sql = "update credentials set name  = ?, email  = ?, password  = ?, type  = ? where userId = ? ";

                PreparedStatement preparedStmt = CONNECTION.prepareStatement(sql);
                preparedStmt.setString (1, name);
                preparedStmt.setString (2, email);
                preparedStmt.setString (3, USEENCRYPTION?encryptPassword(password,ENCRYPTIONKEY):password);
                preparedStmt.setString (4, String.valueOf(checkBox.isSelected()? Credential.AccountType.ADMIN:Credential.AccountType.USER));
                preparedStmt.setInt (5, credential.id);

                preparedStmt.execute();

            }catch (Exception ex) {System.out.println(ex);}

            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button1,gbc);

        JButton button2 = new JButton("Delete");
        button2.addActionListener(e -> {
            try {
                String SQL = "DELETE FROM credentials WHERE userId = ? ";
                PreparedStatement pstmt = CONNECTION.prepareStatement(SQL);
                pstmt.setInt(1, credential.id);
                pstmt.executeUpdate();

            } catch (SQLException ex) {System.out.println("uhoh");}

            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button2,gbc);

        JButton button3 = new JButton("Back");
        button3.addActionListener(e -> {
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button3,gbc);

        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
