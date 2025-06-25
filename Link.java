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