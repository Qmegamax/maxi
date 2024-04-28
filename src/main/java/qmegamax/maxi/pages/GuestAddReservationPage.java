package main.java.qmegamax.maxi.pages;

import com.toedter.calendar.JDateChooser;
import main.java.qmegamax.maxi.util.DocumentSizeFilter;
import main.java.qmegamax.maxi.util.Reservation;
import main.java.qmegamax.maxi.pages.errors.AddingReservationErrorPage;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static main.java.qmegamax.maxi.Main.*;
import static main.java.qmegamax.maxi.Main.GetPendingReservationsFromDatabase;

public class GuestAddReservationPage extends JFrame{
    public boolean capchaCompeted;

    private int submittedReservations =0;

    public GuestAddReservationPage(){
        this.setTitle("Add a reservation");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(panel);

        JLabel label = new JLabel("Add a reservation");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));
        panel.add(label,gbc);

        JPanel box = new JPanel();
        box.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),new EmptyBorder(14, 10, 14, 10)));
        box.setLayout(new GridBagLayout());
        panel.add(box,gbc);

        JDateChooser dateChooser=new JDateChooser();
        dateChooser.setMinSelectableDate(new Date());
        dateChooser.setMaxSelectableDate(new Date(new Date().getTime()+ 604800000L));
        dateChooser.setPreferredSize(new Dimension(140,20));
        box.add(dateChooser,gbc);

        JPanel contentPanel1 = new JPanel(){@Override
        public Dimension getPreferredSize() {
            return new Dimension(140, 30);
        }};
        box.add(contentPanel1,gbc);

        JLabel label1 = new JLabel("Time:   ");
        contentPanel1.add(label1);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());
        JSpinner spinner1 = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner1, "HH:mm");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        spinner1.setEditor(editor);
        contentPanel1.add(spinner1,gbc);

        JTextField textField = new JTextField("name",12);
        AbstractDocument abstractDocument1=(AbstractDocument)textField.getDocument();

        abstractDocument1.setDocumentFilter(new DocumentSizeFilter(30));
        textField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {if(textField.getText().equals("name")) textField.setText("");}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        box.add(textField,gbc);

        JTextArea textArea = new JTextArea("notes",4,12);
        textArea.setPreferredSize(new Dimension(140,60));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        AbstractDocument abstractDocument2=(AbstractDocument)textArea.getDocument();

        abstractDocument2.setDocumentFilter(new DocumentSizeFilter(100));
        textArea.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {if(textArea.getText().equals("notes")) textArea.setText("");}
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });
        box.add(textArea,gbc);

        JPanel contentPanel2 = new JPanel(){@Override
        public Dimension getPreferredSize() {
            return new Dimension(140, 30);
        }};
        box.add(contentPanel2,gbc);

        JLabel label2 = new JLabel("Table:   ");
        contentPanel2.add(label2);

        JSpinner spinner2 = new JSpinner(new SpinnerNumberModel(1,1,TABLEAMOUNT,1));
        spinner2.setEditor(new JSpinner.DefaultEditor(spinner2));
        spinner2.setPreferredSize(new Dimension(36,20));
        contentPanel2.add(spinner2,gbc);

        JButton button1 = new JButton("Add");
        button1.addActionListener(e -> {
            if(submittedReservations >= CAPCHAAMOUNT && !capchaCompeted){
                new CapchaPage(this);
                return;
            }

            Date time=(Date) spinner1.getValue();
            if(time.getHours()<OPENINGHOUR || time.getHours()>CLOSINGHOUR-OCUPATIONTIME/60){
                new AddingReservationErrorPage("Time outside of working hours!");
                return;
            }
            if(dateChooser.getDate()==null){
                new AddingReservationErrorPage("No date chosen!");
                return;
            }
            LocalDateTime date=LocalDateTime.of(dateChooser.getDate().getYear()+1900,dateChooser.getDate().getMonth()+1,dateChooser.getDate().getDate(),time.getHours(),time.getMinutes());
            String name=textField.getText();
            String expression = "^[a-zA-Z\\s]+";
            if(!name.matches(expression)){
                new AddingReservationErrorPage("Invalid name!");
                return;
            }
            String notes=textArea.getText();
            int tableId=(Integer) spinner2.getValue();

            ArrayList<Reservation> currentReservations=GetPendingReservationsFromDatabase();
            currentReservations.addAll(GetReservationsFromDatabase());
            for(Reservation reservation:currentReservations){
                LocalDateTime date1 = reservation.date;
                Date date2 = dateChooser.getDate();
                if(reservation.table==tableId && date1.getYear()==date2.getYear()+1900&& date1.getMonthValue()==date2.getMonth()+1 && date1.getDayOfMonth()==date2.getDate() && time.getHours()*60+time.getMinutes()>date1.getHour()*60+date1.getMinute()-OCUPATIONTIME && time.getHours()*60+time.getMinutes()<date1.getHour()*60+date1.getMinute()+OCUPATIONTIME){
                    new AddingReservationErrorPage("Table is not free at this time!");
                    return;
                }
            }

            try {
                String sql = " insert into pendingReservations (reservationId, time, name, notes, tableId)"
                        + " values (?, ?, ?, ?, ?)";

                PreparedStatement preparedStmt = CONNECTION.prepareStatement(sql);
                ArrayList<Reservation> reservations=GetPendingReservationsFromDatabase();
                preparedStmt.setInt (1, reservations.isEmpty()?1:reservations.get(reservations.size()-1).id+1);
                preparedStmt.setString (2, date.toString().split("T")[0]+" "+date.toString().split("T")[1]);
                preparedStmt.setString   (3, name);
                preparedStmt.setString(4, notes);
                preparedStmt.setInt    (5, tableId);

                preparedStmt.execute();

                capchaCompeted=false;
                submittedReservations++;
                new ReservationSuccsesfullPage();
            }catch (Exception ex) {System.out.println("uhoh");}
        });
        panel.add(button1,gbc);

        this.setSize(400,400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
