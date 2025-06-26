package com.parintorn0.app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Link
{
    //------------------Object_in_Linked_list------------------//
    public String nameData;
    public String reasonData;
    public Boolean emergencyData;
    public LocalDateTime timeData;         // Using local time to sort the patients
    public Link next;               // a next linked list of this linked list

    //------------------Linked_list_Methods------------------//
    public Link(String name, String reason, Boolean emergency, LocalDateTime time)     // Linked_list_constructor
    {
        nameData = name;
        reasonData = reason;
        emergencyData = emergency;
        timeData = time;
    }
    public String displayLink()             // To return all information in each patient
    {
        DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        if(emergencyData) {          // check whether emergency
            return """
                Name: %s, Reason: %s, Arrival Time: %s, Emergency case
            """.formatted(nameData, reasonData, timeData.format(fmt));
        }
        else {
            return """
                Name: %s, Reason: %s, Arrival Time: %s
            """.formatted(nameData, reasonData, timeData.format(fmt));
        }
    }
}