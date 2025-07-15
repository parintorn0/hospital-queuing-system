package com.github.parintorn0;

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

// Start with 2 double ended linked list:  PFirst=>null, QFirst=>null, PFast=>null, QLast=>null
// ->If non-emergency patient come to the hospital (name: A): PFirst=>null, QFirst=>A, PLast=>null, QLast=>A
// ->If non-emergency patient come to the hospital (name: B): PFirst=>null, QFirst=>A=>B, PLast=>null, QLast=>B
// ->If emergency patient come to the hospital (name: C): PFirst=>C, QFirst=>A=>B, PLast=>C, QLast=>B
// ->If emergency patient come to the hospital (name: D): PFirst=>C=>D, QFirst=>A=>B, PLast=>D, QLast=>B

public class HospitalQueue extends JFrame
{
    private final JTextField nameTextField;
    private final JTextField reasonTextField;
    private final JButton submitButton;
    private final JButton clearButton;
    private final JRadioButton yesRadioButton;
    private final JRadioButton noRadioButton;
    //-------Data Label--------------------------
    private final JLabel[] dataArray =new JLabel[6];        // create an array to contain all data labels
    private final JButton dequeueButton;

    private final static QueuingSystem hospital = new QueuingSystem();
    private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HospitalQueue() // UI_constructor
    {
        super("Hospital Queue Simulator");

        JLabel nameLabel = new JLabel("Name");
        nameLabel.setPreferredSize(new Dimension(64, 16));
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel reasonLabel = new JLabel("Reason");
        reasonLabel.setPreferredSize(new Dimension(64, 16));
        reasonLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        nameTextField = new JTextField(1);
        nameTextField.setFont(new Font("Arial", Font.PLAIN, 14));

        reasonTextField = new JTextField(1);
        reasonTextField.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel emergencyLabel = new JLabel("Emergency");
        emergencyLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        yesRadioButton = new JRadioButton("Emergency");
        yesRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));

        noRadioButton = new JRadioButton("Non-Emergency");
        noRadioButton.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(1,2));
        radioPanel.add(yesRadioButton);
        radioPanel.add(noRadioButton);


        JPanel userInputPanel = new JPanel(new GridBagLayout());

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
        userInputPanel.add(nameTextField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        userInputPanel.add(reasonLabel, c);

        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        userInputPanel.add(reasonTextField, c);

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

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 14));

        dequeueButton = new JButton("Dequeue");
        dequeueButton.setFont(new Font("Arial", Font.PLAIN, 14));


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        buttonPanel.add(dequeueButton);

        JPanel panelData = new JPanel();
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

        JPanel panelMain = new JPanel();
        panelMain.setPreferredSize(new Dimension(800, 450));
        panelMain.setLayout(new GridLayout(2, 1)); // set layout for main panel to display 2 rows with 10px gap
        panelMain.add(topPanel);
        panelMain.add(panelData);
        panelMain.setBorder(new javax.swing.border.LineBorder(new Color(1),1)); // set border for user input panel
        

        setContentPane(panelMain);
        pack();                        // display with decent window dimension
        try {
            read();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i=0;i<6;i++){               // display all patients data from array into UI labels
            if(hospital.displayPatients()[i]==null){
                break;
            }
            dataArray[i].setText("%s %s".formatted(i+1,hospital.displayPatients()[i]));
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

    public static void write() throws SQLException // method to save all patient's data in txt file
    {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:app.db");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);
        connection.setAutoCommit(false);
        statement.executeUpdate("""
            DELETE FROM link;
        """);
        Link current=hospital.getPFirst();
        String query = """
            INSERT INTO link (name, reason, emergency, datetime) VALUES (?, ?, ?, ?);
        """;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for(int i=0;i<hospital.numPPatients;i++){
            setStatement(current, preparedStatement);
            current=current.next;
        }
        current=hospital.getQFirst();
        for(int i=0;i<hospital.numQPatients;i++){
            setStatement(current, preparedStatement);
            current=current.next;
        }
        connection.commit();

        statement.close();
        connection.close();
        System.out.println("Successfully wrote to the file.");
    }

    private static void setStatement(Link current, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, current.nameData);
        preparedStatement.setString(2, current.reasonData);
        preparedStatement.setBoolean(3, current.emergencyData);
        preparedStatement.setString(4, dtf.format(current.timeData));
        preparedStatement.executeUpdate();
    }

    public static void read() throws SQLException // method to recall patients' data from txt file
    {
        Connection connection = DriverManager.getConnection("jdbc:sqlite:app.db");
        Statement statement = connection.createStatement();
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
        System.out.println("Successfully read the file.");
    }


    public class ButtonListener implements ActionListener{ // class ButtonListener

        public void actionPerformed (ActionEvent e) { // method to receive data inputted from users
            String name;
            String reason;
            boolean emergency;
            if(e.getSource() == submitButton) {
                if(nameTextField.getText().equals("Please fill the name") | reasonTextField.getText().equals("Please fill a reason"))
                {
                    return;
                }
                if(nameTextField.getText().isEmpty() & reasonTextField.getText().isEmpty())
                {
                    nameTextField.setText("Please fill the name");
                    reasonTextField.setText("Please fill a reason");
                    return;
                }
                else if(reasonTextField.getText().isEmpty())
                {
                    reasonTextField.setText("Please fill a reason");
                    return;
                }
                else if(nameTextField.getText().isEmpty())
                {
                    nameTextField.setText("Please fill a name");
                    return;
                }
                if((!yesRadioButton.isSelected() & !noRadioButton.isSelected())){ return; }
                name = nameTextField.getText();
                reason = reasonTextField.getText();
                emergency = yesRadioButton.isSelected();
                //---------call QueueSystem Method----------------------------
                hospital.insertLast(name, reason, emergency, LocalDateTime.now());
                for(int i=0;i<6;i++){
                    if(hospital.displayPatients()[i]==null){
                        break;
                    }
                    dataArray[i].setText("%s %s".formatted(i+1, hospital.displayPatients()[i]));
                }
                try {
                    HospitalQueue.write();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                //---------Clear text in all button---------------------------
                nameTextField.setText("");
                reasonTextField.setText("");
                yesRadioButton.setSelected(false);
                noRadioButton.setSelected(true);
            }
            else if(e.getSource() == clearButton){ //Clear text in all button
                nameTextField.setText("");
                reasonTextField.setText("");
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
                    dataArray[i].setText("%s %s".formatted(i+1, hospital.displayPatients()[i]));
                }
                try {
                    HospitalQueue.write();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        HospitalQueue frame = new HospitalQueue();
        frame.setVisible(true);
    }
}
