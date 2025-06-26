package com.parintorn0.app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

// Start with 2 double ended linked list:  Pfirst=>null, Qfirst=>null, Plast=>null, Qlast=>null
// ->If non-emergency patient come to the hospital (name: A): Pfirst=>null, Qfirst=>A, Plast=>null, Qlast=>A
// ->If non-emergency patient come to the hospital (name: B): Pfirst=>null, Qfirst=>A=>B, Plast=>null, Qlast=>B
// ->If emergency patient come to the hospital (name: C): Pfirst=>C, Qfirst=>A=>B, Plast=>C, Qlast=>B
// ->If emergency patient come to the hospital (name: D): Pfirst=>C=>D, Qfirst=>A=>B, Plast=>D, Qlast=>B

public class HospitalQueue extends JFrame
{
    private JTextField nameTextfield;
    private JTextField reasonTextfield;
    private JButton submitButton;
    private JButton clearButton;
    private JButton exitButton;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JLabel nameLabel;
    private JLabel reasonLabel;
    private JLabel emergencyLabel;
    private JPanel panelMain;
    private JPanel buttonPanel;
    private JPanel panelData;
    private JPanel userInputPanel;
    //-------Data Label--------------------------
    private final JLabel[] dataArray =new JLabel[6];        // create an array to contain all data labels
    private JButton dequeueButton;

    private static QueuingSystem hospital = new QueuingSystem();
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HospitalQueue() throws IOException // UI_constructor
    {
        super("Hospital Queue Simulator");
        
        nameLabel = new JLabel("Name");
        nameLabel.setPreferredSize(new Dimension(64, 16));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        reasonLabel = new JLabel("Reason");
        reasonLabel.setPreferredSize(new Dimension(64, 16));
        reasonLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        nameTextfield = new JTextField(1);
        nameTextfield.setFont(new Font("Arial", Font.PLAIN, 14));
        // nameTextfield.setPreferredSize(new Dimension(550, 24));

        reasonTextfield = new JTextField(1);
        reasonTextfield.setFont(new Font("Arial", Font.PLAIN, 14));
        // reasonTextfield.setPreferredSize(new Dimension(550, 24));

        emergencyLabel = new JLabel("Emergency");
        emergencyLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        yesRadioButton = new JRadioButton("Emergency");
        yesRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));

        noRadioButton = new JRadioButton("Non-Emergency");
        noRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(1,2));
        radioPanel.add(yesRadioButton);
        radioPanel.add(noRadioButton);


        userInputPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5, 5, 5, 5);

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        userInputPanel.add(nameLabel, c);

        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        userInputPanel.add(nameTextfield, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        userInputPanel.add(reasonLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        userInputPanel.add(reasonTextfield, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        userInputPanel.add(emergencyLabel, c);

        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 2;
        userInputPanel.add(radioPanel, c);
        userInputPanel.setPreferredSize(new Dimension(500, 150));

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.PLAIN, 14));

        clearButton = new JButton("Clear Input");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));

        exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));

        dequeueButton = new JButton("Dequeue");
        dequeueButton.setFont(new Font("Arial", Font.PLAIN, 14));


        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(dequeueButton);

        panelData = new JPanel();
        panelData.setLayout(new GridLayout(6, 1, 5, 5));
        panelData.setBorder(new javax.swing.border.EmptyBorder(new Insets(10,20,20,10))); // set border for data panel

        for(int i=0;i<6;i++){               // create 6 labels for displaying patients data
            dataArray[i]=new JLabel("");
            dataArray[i].setFont(new Font("Arial", Font.PLAIN, 14));
            dataArray[i].setPreferredSize(new Dimension(450,16));
            panelData.add(dataArray[i]);
        }

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // create a top panel with FlowLayout
        topPanel.setPreferredSize(new Dimension(600, 100)); // set preferred size for top panel
        topPanel.setBorder(new javax.swing.border.EmptyBorder(new Insets(20,20,20,20))); // set border for button panel
        topPanel.add(userInputPanel);
        topPanel.add(buttonPanel);

        panelMain = new JPanel();
        panelMain.setPreferredSize(new Dimension(800, 450));
        panelMain.setLayout(new GridLayout(2, 1)); // set layout for main panel to display 2 rows with 10px gap
        panelMain.add(topPanel);
        panelMain.add(panelData);
        panelMain.setBorder(new javax.swing.border.LineBorder(new Color(1),1)); // set border for user input panel
        

        setContentPane(panelMain);
        pack();                        // display with decent window dimension
        read();

        for(int i=0;i<6;i++){               // display all patients data from array into UI labels
            if(hospital.displayPatients()[i]==null){
                break;
            }
            dataArray[i].setText(String.valueOf(i+1)+" "+hospital.displayPatients()[i]);
        }

        submitButton.addActionListener(new ButtonListener());
        clearButton.addActionListener(new ButtonListener());
        exitButton.addActionListener(new ButtonListener());
        yesRadioButton.addActionListener(new ButtonListener());
        noRadioButton.addActionListener(new ButtonListener());
        dequeueButton.addActionListener(new ButtonListener());
        noRadioButton.setSelected(true);        // set default to non-emergency
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void write()  // method to save all patients data in txt file
    {
        try (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:app.db");
            Statement statement = connection.createStatement();
        ) {
            statement.setQueryTimeout(30);
            connection.setAutoCommit(false);
            statement.executeUpdate("""
                DELETE FROM link;
            """);
            Link current=hospital.getPfirst();
            String query = """
                INSERT INTO link (name, reason, emergency, datetime) VALUES (?, ?, ?, ?);
            """;
            PreparedStatement pstmt = connection.prepareStatement(query);
            for(int i=0;i<hospital.numPPatients;i++){
                pstmt.setString(1, current.nameData);
                pstmt.setString(2, current.reasonData);
                pstmt.setBoolean(3, current.emergencyData);
                pstmt.setString(4, dtf.format(current.timeData).toString());
                pstmt.executeUpdate();
                current=current.next;
            }
            current=hospital.getQfirst();
            for(int i=0;i<hospital.numQPatients;i++){
                pstmt.setString(1, current.nameData);
                pstmt.setString(2, current.reasonData);
                pstmt.setBoolean(3, current.emergencyData);
                pstmt.setString(4, dtf.format(current.timeData).toString());
                pstmt.executeUpdate();
                current=current.next;
            }
            connection.commit();

            statement.close();
            connection.close();
            System.out.println("Successfully wrote to the file.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public static void read() throws IOException // method to recall patients data from txt file
    {
        try (
            Connection connection = DriverManager.getConnection("jdbc:sqlite:app.db");
            Statement statement = connection.createStatement();
        ) {
            ResultSet rs = statement.executeQuery("""
                SELECT * FROM link ORDER BY datetime ASC;
            """);
            while(rs.next()) {
                String name = rs.getString("name");
                String reason = rs.getString("reason");
                boolean emergency = rs.getBoolean("emergency");
                LocalDateTime time = LocalDateTime.parse(rs.getString("datetime"), dtf);
                hospital.insertLast(name, reason, emergency, time);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("Successfully read the file.");
    }


    public class ButtonListener implements ActionListener{ // class ButtonListener

        public void actionPerformed (ActionEvent e){ // method to receive data inputed from users
            String name;
            String reason;
            Boolean emergency = false;
            if(e.getSource() == submitButton) {
                if(nameTextfield.getText().equals("Please fill the name") | reasonTextfield.getText().equals("Please fill a reason"))
                {
                    return;
                }
                if(nameTextfield.getText().isEmpty() & reasonTextfield.getText().isEmpty())
                {
                    nameTextfield.setText("Please fill the name");
                    reasonTextfield.setText("Please fill a reason");
                    return;
                }
                else if(reasonTextfield.getText().isEmpty())
                {
                    reasonTextfield.setText("Please fill a reason");
                    return;
                }
                else if(nameTextfield.getText().isEmpty())
                {
                    nameTextfield.setText("Please fill a name");
                    return;
                }
                if((!yesRadioButton.isSelected() & !noRadioButton.isSelected())){ return; }
                name = nameTextfield.getText();
                reason = reasonTextfield.getText();
                if (yesRadioButton.isSelected())
                    emergency = true;
                else
                    emergency = false;
                //---------call QueueSystem Method----------------------------
                hospital.insertLast(name, reason, emergency, LocalDateTime.now());
                for(int i=0;i<6;i++){
                    if(hospital.displayPatients()[i]==null){
                        break;
                    }
                    dataArray[i].setText(String.valueOf(i+1)+" "+hospital.displayPatients()[i]);
                }
                HospitalQueue.write();
                //---------Clear text in all button---------------------------
                nameTextfield.setText("");
                reasonTextfield.setText("");
                yesRadioButton.setSelected(false);
                noRadioButton.setSelected(true);
            }
            else if(e.getSource() == clearButton){ //Clear text in all button
                nameTextfield.setText("");
                reasonTextfield.setText("");
                yesRadioButton.setSelected(false);
                noRadioButton.setSelected(true);
            }
            else if(e.getSource() == yesRadioButton){
                yesRadioButton.setSelected(true);
                noRadioButton.setSelected(false);
            }
            else if(e.getSource() == noRadioButton){
                noRadioButton.setSelected(true);
                yesRadioButton.setSelected(false);
            }
            else if(e.getSource() == dequeueButton){
                hospital.deleteFirst();
                for(int i=0;i<6;i++){
                    if(hospital.displayPatients()[i]==null){
                        for(int j=i;j<6;j++){
                            dataArray[i].setText("");
                        }
                        break;
                    }
                    dataArray[i].setText(String.valueOf(i+1)+" "+hospital.displayPatients()[i]);
                }
                HospitalQueue.write();
            }
            else {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        HospitalQueue frame = new HospitalQueue();
        frame.setVisible(true);
    }
}
