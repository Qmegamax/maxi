package main.java.qmegamax.maxi.pages;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static main.java.qmegamax.maxi.Main.PATH;

public class CapchaPage extends JFrame{

    public CapchaPage(LoginPage loginPage){
        this.setTitle("Capcha");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel label = new JLabel("Rotate the image, so it is straight");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(label, gbc);

        AtomicInteger rotation= new AtomicInteger((int) (Math.random() * 16));
        BufferedImage bi=null;
        try {
            bi = ImageIO.read(new File(PATH+"ci\\a"+(int)(Math.random() * 11)+".png"));
        } catch (IOException ignored) {System.out.println("aaaa");}
        BufferedImage finalBi = bi;
        JPanel image=new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.rotate(Math.toRadians(rotation.get() * 22.5), 50, 50);
                g2.drawImage(finalBi, 0, 0,100,100, null);
            }
        };
        panel.add(image,gbc);

        JPanel contentPanel = new JPanel(){@Override
        public Dimension getPreferredSize() {
            return new Dimension(150, 100);
        }};
        panel.add(contentPanel);
        contentPanel.setLayout(new GridBagLayout());

        JButton btnNewButton = new JButton("<-");
        btnNewButton.addActionListener(e -> {
            rotation.getAndDecrement();
            image.repaint();
        });
        contentPanel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("->");
        btnNewButton_1.addActionListener(e -> {
            rotation.getAndIncrement();
            image.repaint();
        });
        contentPanel.add(btnNewButton_1,gbc);

        JButton btnNewButton_2 = new JButton("Submit");
        btnNewButton_2.addActionListener(e -> {
            loginPage.capchaCompeted=(rotation.get()%16==0);
            this.setVisible(false);
            this.dispose();
        });
        contentPanel.add(btnNewButton_2,gbc);

        this.add(panel);
        this.setSize(360,300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public CapchaPage(GuestAddReservationPage guestAddReservationPage){
        this.setTitle("Capcha");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JLabel label = new JLabel("Rotate the image, so it is straight");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 22f));
        panel.add(label, gbc);

        AtomicInteger rotation= new AtomicInteger((int) (Math.random() * 16));
        BufferedImage bi=null;
        try {
            bi = ImageIO.read(new File(PATH+"ci\\a"+(int)(Math.random() * 11)+".png"));
        } catch (IOException ignored) {System.out.println("aaaa");}
        BufferedImage finalBi = bi;
        JPanel image=new JPanel() {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.rotate(Math.toRadians(rotation.get() * 22.5), 50, 50);
                g2.drawImage(finalBi, 0, 0,100,100, null);
            }
        };
        panel.add(image,gbc);

        JPanel contentPanel = new JPanel(){@Override
        public Dimension getPreferredSize() {
            return new Dimension(150, 100);
        }};
        panel.add(contentPanel);
        contentPanel.setLayout(new GridBagLayout());

        JButton btnNewButton = new JButton("<-");
        btnNewButton.addActionListener(e -> {
            rotation.getAndDecrement();
            image.repaint();
        });
        contentPanel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("->");
        btnNewButton_1.addActionListener(e -> {
            rotation.getAndIncrement();
            image.repaint();
        });
        contentPanel.add(btnNewButton_1,gbc);

        JButton btnNewButton_2 = new JButton("Submit");
        btnNewButton_2.addActionListener(e -> {
            guestAddReservationPage.capchaCompeted=(rotation.get()%16==0);
            this.setVisible(false);
            this.dispose();
        });
        contentPanel.add(btnNewButton_2,gbc);

        this.add(panel);
        this.setSize(360,300);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
