package main.java.qmegamax.maxi.pages.errors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static main.java.qmegamax.maxi.Main.PATH;

public class LognErrorPage extends JFrame{

    public LognErrorPage(){
        this.setTitle("Error");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

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

        JLabel label = new JLabel("Incorrect username/password!");
        label.setFont(label.getFont().deriveFont(Font.ITALIC, 22f));
        panel.add(label, gbc);

        this.add(panel);
        this.setSize(360,140);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
