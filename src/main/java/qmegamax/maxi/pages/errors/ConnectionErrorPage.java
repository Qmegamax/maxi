package main.java.qmegamax.maxi.pages.errors;

import main.java.qmegamax.maxi.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static main.java.qmegamax.maxi.Main.PATH;

public class ConnectionErrorPage extends JFrame{

    public ConnectionErrorPage(){
        this.setTitle("Error");
        Main.setImage(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        JFrame frame = this;
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel label = new JLabel("Cannot establish connection with the database, check credentials/ip!");
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 22f));
        panel.add(label, gbc);

        this.add(panel);
        this.setSize(690,140);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
