package main.java.qmegamax.maxi.pages;

import main.java.qmegamax.maxi.Credential;
import main.java.qmegamax.maxi.Main;
import main.java.qmegamax.maxi.pages.errors.LognErrorPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

import static main.java.qmegamax.maxi.Main.*;

public class LoginPage extends JFrame {

    public boolean capchaCompeted;

    private int incorrectGuesses = 0;

    public LoginPage() {
        this.setTitle("Login");
        ImageIcon img = new ImageIcon(PATH + "icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(panel);

        JLabel label = new JLabel("Login");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));
        panel.add(label, gbc);

        JTextField textField1 = new JTextField("username", 16);
        textField1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (textField1.getText().equals("username")) textField1.setText("");
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        panel.add(textField1, gbc);

        JTextField textField2 = new JTextField("password", 16);
        textField2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (textField2.getText().equals("password")) textField2.setText("");
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        panel.add(textField2, gbc);

        JButton button1 = new JButton("Submit");
        button1.addActionListener(e -> {
            if (incorrectGuesses >= CAPCHAAMOUNT && !capchaCompeted) {
                new CapchaPage(this);
                return;
            }

            capchaCompeted = false;

            String username = textField1.getText();
            String password = textField2.getText();

            boolean foundMatch = false;
            for (Credential credential : Main.GetCredentialsFromDatabase()) {
                if (Objects.equals(credential.name, username) && Objects.equals(credential.password, password)) {

                    if (credential.type == Credential.AccountType.USER) {
                        new PendingReservationsPage();
                    }
                    if (credential.type == Credential.AccountType.ADMIN) {
                        new EditReservationPage();
                    }

                    foundMatch = true;
                    this.setVisible(false);
                    this.dispose();
                    break;
                }
            }
            if (!foundMatch) {
                new LognErrorPage();
                incorrectGuesses++;
            }
        });
        panel.add(button1, gbc);

        JButton button2 = new JButton("Log in as a guest");
        button2.addActionListener(e -> {
            new GuestAddReservationPage();
            this.setVisible(false);
            this.dispose();
        });
        panel.add(button2, gbc);

        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
