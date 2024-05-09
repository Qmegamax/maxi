package main.java.qmegamax.maxi.pages.errors;

import main.java.qmegamax.maxi.Main;
import main.java.qmegamax.maxi.pages.CapchaPage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static main.java.qmegamax.maxi.Main.PATH;

public class AddingReservationErrorPage extends JFrame{

    public AddingReservationErrorPage(String error){
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

        JLabel label = new JLabel(error);
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 22f));
        panel.add(label, gbc);

        this.add(panel);
        this.setSize(360,140);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
