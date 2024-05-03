package main.java.qmegamax.maxi.pages;

import com.toedter.calendar.JDateChooser;
import main.java.qmegamax.maxi.util.DocumentSizeFilter;
import main.java.qmegamax.maxi.util.Reservation;
import main.java.qmegamax.maxi.pages.errors.AddingReservationErrorPage;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static main.java.qmegamax.maxi.Main.*;

public class EditCurrentReservationPage extends JFrame{
    public EditCurrentReservationPage(Reservation reservation){
        this.setTitle("Edit current reservation");
        ImageIcon img = new ImageIcon(PATH+"icon.png");
        this.setIconImage(img.getImage());

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        this.add(panel);

        JLabel label = new JLabel("Edit a reservation");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 27f));
        panel.add(label,gbc);

        JPanel box = new JPanel();
        box.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),new EmptyBorder(14, 10, 14, 10)));
        box.setLayout(new GridBagLayout());
        panel.add(box,gbc);

        JDateChooser dateChooser=new JDateChooser(new Date(reservation.date.getYear()-1900,reservation.date.getMonthValue()-1,reservation.date.getDayOfMonth()));
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
        calendar.set(Calendar.HOUR_OF_DAY, reservation.date.getHour());
        calendar.set(Calendar.MINUTE, reservation.date.getMinute());
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());
        JSpinner spinner1 = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner1, "HH:mm");
        DateFormatter formatter = (DateFormatter)editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false);
        formatter.setOverwriteMode(true);
        spinner1.setEditor(editor);
        contentPanel1.add(spinner1,gbc);

        JTextField textField = new JTextField(reservation.name,12);
        AbstractDocument abstractDocument1=(AbstractDocument)textField.getDocument();

        abstractDocument1.setDocumentFilter(new DocumentSizeFilter(30));
        box.add(textField,gbc);

        JTextArea textArea = new JTextArea(reservation.notes,4,12);
        textArea.setPreferredSize(new Dimension(140,60));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        AbstractDocument abstractDocument2=(AbstractDocument)textArea.getDocument();

        abstractDocument2.setDocumentFilter(new DocumentSizeFilter(100));
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
        spinner2.setValue(reservation.table);
        contentPanel2.add(spinner2,gbc);

        JFrame frame=this;
        JButton button1 = new JButton("Apply");
        button1.addActionListener(e -> {

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

            ArrayList<Reservation> currentReservations=GetDataFromDatabase("reservation",Reservation.getEmpty());
            currentReservations.addAll(GetDataFromDatabase("pendingReservations",Reservation.getEmpty()));
            for(Reservation everyReservation:currentReservations){
                if(everyReservation.id==reservation.id)continue;

                LocalDateTime date1 = everyReservation.date;
                Date date2 = dateChooser.getDate();
                if(everyReservation.table==tableId && date1.getYear()==date2.getYear()+1900 && date1.getMonthValue()==date2.getMonth()+1 && date1.getDayOfMonth()==date2.getDate() && time.getHours()*60+time.getMinutes()>date1.getHour()*60+date1.getMinute()-OCUPATIONTIME && time.getHours()*60+time.getMinutes()<date1.getHour()*60+date1.getMinute()+OCUPATIONTIME){
                    new AddingReservationErrorPage("Table is not free at this time!");
                    return;
                }
            }

            try {
                String sql = "update reservations set time  = ?, name  = ?, notes  = ?, tableId  = ? where reservationId = ? ";

                PreparedStatement preparedStmt = CONNECTION.prepareStatement(sql);
                preparedStmt.setString (1, date.toString().split("T")[0]+" "+date.toString().split("T")[1]);
                preparedStmt.setString (2, name);
                preparedStmt.setString   (3, notes);
                preparedStmt.setInt(4, tableId);
                preparedStmt.setInt    (5, reservation.id);

                preparedStmt.execute();

            }catch (Exception ex) {System.out.println("uhoh");}

            EditReservationsPage.current.refreshButton.doClick();
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button1,gbc);

        JButton button2 = new JButton("Delete");
        button2.addActionListener(e -> {
            try {
                String SQL = "DELETE FROM reservations WHERE reservationId = ? ";
                PreparedStatement pstmt = CONNECTION.prepareStatement(SQL);
                pstmt.setString(1, String.valueOf(reservation.id));
                pstmt.executeUpdate();

            } catch (SQLException ex) {System.out.println("uhoh");}

            EditReservationsPage.current.refreshButton.doClick();
            frame.setVisible(false);
            frame.dispose();
        });
        panel.add(button2,gbc);

        JButton button3 = new JButton("Cancel");
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
