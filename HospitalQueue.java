import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Start with 2 double ended linked list:  Pfirst=>null, Qfirst=>null, Plast=>null, Qlast=>null
// ->If non-emergency patient come to the hospital (name: A): Pfirst=>null, Qfirst=>A, Plast=>null, Qlast=>A
// ->If non-emergency patient come to the hospital (name: B): Pfirst=>null, Qfirst=>A=>B, Plast=>null, Qlast=>B
// ->If emergency patient come to the hospital (name: C): Pfirst=>C, Qfirst=>A=>B, Plast=>C, Qlast=>B
// ->If emergency patient come to the hospital (name: D): Pfirst=>C=>D, Qfirst=>A=>B, Plast=>D, Qlast=>B


class Link
{
    //------------------Object_in_Linked_list------------------//
    public String nameData;
    public String reasonData;
    public Boolean emergencyData;
    public String timeData;         // Using local time to sort the patients
    public Link next;               // a next linked list of this linked list

    //------------------Linked_list_Methods------------------//
    public Link(String name, String reason, Boolean emergency, String time)     // Linked_list_constructor
    {
        nameData = name;
        reasonData = reason;
        emergencyData = emergency;
        timeData = time;
    }
    public String displayLink()             // To return all information in each patient
    {
        if(emergencyData)           // check whether emergency
        {
            return ("Name: " + nameData + ", Reason: " + reasonData + ", Arrival Time: " + timeData + ", Emergency case");
        }
        return ("Name: " + nameData + ", Reason: " + reasonData + ", Arrival Time: " + timeData);
    }
}
class QueuingSystem
{
    //------------------Object_in_Queueing_system------------------//
    public static int numQPatients;     // Number of non-emergency patients
    public static int numPPatients;     // Number of emergency patients
    private static Link Qfirst;         // Pointer of non-emergency linked list
    private static Link Pfirst;         // Pointer of emergency linked list
    private static Link Qlast;          // Pointer of last non-emergency linked list
    private static Link Plast;          // Pointer of last emergency linked list

    //------------------Queueing_system_Methods------------------//
    public QueuingSystem()              // Queueing_system_constructor
    {
        Qfirst=null;
        Pfirst=null;
        Plast=null;
        Qlast=null;
        numQPatients=0;
        numPPatients=0;
    }
    public static boolean QisEmpty() { return Qfirst==null; } // check whether non-emergency linked list empty
    public static boolean PisEmpty() { return Pfirst==null; } // check whether emergency linked list empty
    public static void insertLast(String name, String reason, Boolean emergency, String time) // Enqueue
    {
        Link newLink=new Link(name, reason, emergency, time); // create new linked list for inserting new patient queue
        if(emergency)       // check whether emergency
        {
            if( PisEmpty() )                // if empty list,
                Pfirst = newLink;           // Pfirst --> newLink
            else
                Plast.next = newLink;       // old Plast --> newLink
            Plast = newLink;                // set old Plast to be updated
            numPPatients++;
        }
        else {
            if (QisEmpty())                 // if empty list,
                Qfirst = newLink;           // Qfirst --> newLink
            else
                Qlast.next = newLink;       // old Qlast --> newLink
            Qlast = newLink;                // set old Qlast to be updated
            numQPatients++;
        }
    }
    public static void deleteFirst() // Dequeue
    {
        if(PisEmpty() & QisEmpty())         // check whether both linked lists are empty
        {
            System.out.println("There is nothing to delete.");
        }
        else if(!QisEmpty())                // check whether non-emergency are not empty
        {
            if(Qfirst.next == null)         // check whether it has only one non-emergency patient
                Qlast = null;               // pointer of last of non-emergency linked list is null (No patient)
            Qfirst = Qfirst.next;           // pointer of non-emergency linked list point to the next linked list
            numQPatients--;
        }
        else                                // emergency are not empty then
        {
            if(Pfirst.next == null)         // check whether it has only one emergency patient
                Plast = null;               // pointer of last of emergency linked list is null (No patient)
            Pfirst = Pfirst.next;           // pointer of emergency linked list point to the next linked list
            numPPatients--;
        }
    }
    public Link getQfirst() // get a pointer of non-emergency linked list (for tracking)
    {
        return Qfirst;
    }
    public Link getPfirst() // get a pointer of non-emergency linked list (for tracking)
    {
        return Pfirst;
    }
    public static String[] displayPatients() // get an array of patients data (for display in UI)
    {
        String[] arrayData=new String[6];           // create an array size of 6 (for display in 6 labels of UI) for development use vector for unlimited size
        int i=0;
        Link current = Pfirst;                      // create current for tracking emergency linked list
        while(current != null)                      // loop until it end
        {
            if(i==6){ return arrayData; }           // check whether arrayData is full
            arrayData[i]= current.displayLink();    // append to array
            i++;
            current = current.next;                 // move to the next linked list
        }
        current=Qfirst;                             // track non-emergency linked list
        while(current != null)                      // loop until it end
        {
            if(i==6){ return arrayData; }           // check whether arrayData is full
            arrayData[i]=current.displayLink();     // append to array
            i++;
            current = current.next;                 // move to the next linked list
        }
        return arrayData;
    }
}
public class HospitalQueue extends JFrame
{
    private JTextField NameTextfield;
    private JTextField ReasonTextfield;
    private JButton submitButton;
    private JButton clearButton;
    private JButton exitButton;
    private JRadioButton yesRadioButton;
    private JRadioButton noRadioButton;
    private JLabel NameLabel;
    private JLabel ReasonLabel;
    private JLabel EmergencyLabel;
    private JPanel panelMain;
    private JPanel buttonPanel;
    private JPanel PanelData;
    //-------Data Label--------------------------
    private final JLabel[] dataArray =new JLabel[6];        // create an array to contain all data labels
    private JLabel data1label;
    private JLabel data2label;
    private JLabel data3label;
    private JLabel data4label;
    private JLabel data5label;
    private JLabel data6label;
    private JButton dequeueButton;

    private static QueuingSystem hospital = new QueuingSystem();;

    public HospitalQueue() throws IOException // UI_constructor
    {
        super("Hospital Queue Simulator");
        dataArray[0]=data1label;        // set all label to data in array (by index) (for easier in develop)
        dataArray[1]=data2label;
        dataArray[2]=data3label;
        dataArray[3]=data4label;
        dataArray[4]=data5label;
        dataArray[5]=data6label;
        for(int i=0;i<6;i++){
            dataArray[i].setPreferredSize(new Dimension(450,16));
        }
        this.panelMain.setPreferredSize(new Dimension(600,400));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(this.panelMain);
        this.pack();                        // display with decent window dimension
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
    }

    public static void write()  // method to save all patients data in txt file
    {
        try {
            FileWriter myWriter = new FileWriter("DataBackup.txt");
            Link current=hospital.getPfirst();
            for(int i=0;i<hospital.numPPatients;i++){
                myWriter.write(current.nameData+"\n");
                myWriter.write(current.reasonData+"\n");
                myWriter.write(current.timeData+"\n");
                current=current.next;

            }
            myWriter.write("0\n");
            current=hospital.getQfirst();
            for(int i=0;i<hospital.numQPatients;i++){
                myWriter.write(current.nameData+"\n");
                myWriter.write(current.reasonData+"\n");
                myWriter.write(current.timeData+"\n");
                current=current.next;
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void read() throws IOException // method to recall patients data from txt file
    {
        File file = new File("DataBackup.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        String name;
        String reason;
        String time;
        boolean emergency = true;
        st = br.readLine();
        while (!st.equals("0")) {
            name = st;
            st = br.readLine();
            reason = st;
            st = br.readLine();
            time = st;
            st = br.readLine();
            hospital.insertLast(name,reason,emergency,time);
        }
        st= br.readLine();
        emergency=false;
        while(st!=null) {
            name = st;
            st = br.readLine();
            reason = st;
            st = br.readLine();
            time = st;
            st = br.readLine();
            hospital.insertLast(name,reason,emergency,time);
        }
        System.out.println("Successfully read the file.");
    }


    public class ButtonListener implements ActionListener{ // class ButtonListener

        public void actionPerformed (ActionEvent e){ // method to receive data inputed from users
            String name;
            String reason;
            Boolean emergency = false;
            if(e.getSource() == submitButton) {
                if(NameTextfield.getText().equals("Please fill the name") | ReasonTextfield.getText().equals("Please fill a reason"))
                {
                    return;
                }
                if(NameTextfield.getText().isEmpty() & ReasonTextfield.getText().isEmpty())
                {
                    NameTextfield.setText("Please fill the name");
                    ReasonTextfield.setText("Please fill a reason");
                    return;
                }
                else if(ReasonTextfield.getText().isEmpty())
                {
                    ReasonTextfield.setText("Please fill a reason");
                    return;
                }
                else if(NameTextfield.getText().isEmpty())
                {
                    NameTextfield.setText("Please fill a reason");
                    return;
                }
                if((!yesRadioButton.isSelected() & !noRadioButton.isSelected())){ return; }
                name = NameTextfield.getText();
                reason = ReasonTextfield.getText();
                if (yesRadioButton.isSelected())
                    emergency = true;
                else
                    emergency = false;
                //---------call QueueSystem Method----------------------------
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
                hospital.insertLast(name,reason,emergency,(dtf.format(LocalDateTime.now()).toString()));
                for(int i=0;i<6;i++){
                    if(hospital.displayPatients()[i]==null){
                        break;
                    }
                    dataArray[i].setText(String.valueOf(i+1)+" "+hospital.displayPatients()[i]);
                }
                HospitalQueue.write();
                //---------Clear text in all button---------------------------
                NameTextfield.setText("");
                ReasonTextfield.setText("");
                yesRadioButton.setSelected(false);
                noRadioButton.setSelected(true);
            }
            else if(e.getSource() == clearButton){ //Clear text in all button
                NameTextfield.setText("");
                ReasonTextfield.setText("");
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
