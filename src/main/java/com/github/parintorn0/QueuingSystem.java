package com.github.parintorn0;

import java.time.LocalDateTime;

class QueuingSystem
{
    //------------------Object_in_Queueing_system------------------//
    public int numQPatients;     // Number of non-emergency patients
    public int numPPatients;     // Number of emergency patients
    private static Link QFirst;         // Pointer of non-emergency linked list
    private static Link PFirst;         // Pointer of emergency linked list
    private static Link QLast;          // Pointer of last non-emergency linked list
    private static Link PLast;          // Pointer of last emergency linked list

    //------------------Queueing_system_Methods------------------//
    public QueuingSystem()              // Queueing_system_constructor
    {
        QFirst =null;
        PFirst =null;
        PLast =null;
        QLast =null;
        numQPatients=0;
        numPPatients=0;
    }
    public static boolean QisEmpty() { return QFirst ==null; } // check whether non-emergency linked list empty
    public static boolean PisEmpty() { return PFirst ==null; } // check whether emergency linked list empty
    public void insertLast(String name, String reason, Boolean emergency, LocalDateTime time) // Enqueue
    {
        Link newLink=new Link(name, reason, emergency, time); // create new linked list for inserting new patient queue
        if(emergency)       // check whether emergency
        {
            if( PisEmpty() )                // if empty list,
                PFirst = newLink;           // Pfirst --> newLink
            else
                PLast.next = newLink;       // old Plast --> newLink
            PLast = newLink;                // set old Plast to be updated
            numPPatients++;
        }
        else {
            if (QisEmpty())                 // if empty list,
                QFirst = newLink;           // Qfirst --> newLink
            else
                QLast.next = newLink;       // old Qlast --> newLink
            QLast = newLink;                // set old Qlast to be updated
            numQPatients++;
        }
    }
    public void deleteFirst() // Dequeue
    {
        if(PisEmpty() & QisEmpty())         // check whether both linked lists are empty
        {
            System.out.println("There is nothing to delete.");
        }
        else if(!PisEmpty())                // check whether emergency are not empty
        {
            if(PFirst.next == null)         // check whether it has only one emergency patient
                PLast = null;               // pointer of last of emergency linked list is null (No patient)
            PFirst = PFirst.next;           // pointer of emergency linked list point to the next linked list
            numPPatients--;
        }
        else                                // non-emergency are not empty then
        {
            if(QFirst.next == null)         // check whether it has only one non-emergency patient
                QLast = null;               // pointer of last of non-emergency linked list is null (No patient)
            QFirst = QFirst.next;           // pointer of non-emergency linked list point to the next linked list
            numQPatients--;
        }
    }
    public Link getQFirst() // get a pointer of non-emergency linked list (for tracking)
    {
        return QFirst;
    }
    public Link getPFirst() // get a pointer of non-emergency linked list (for tracking)
    {
        return PFirst;
    }
    public String[] displayPatients() // get an array of patients data (for display in UI)
    {
        String[] arrayData=new String[6];           // create an array size of 6 (for display in 6 labels of UI) for development use vector for unlimited size
        int i=0;
        Link current = PFirst;                      // create current for tracking emergency linked list
        while(current != null)                      // loop until it end
        {
            if(i==6){ return arrayData; }           // check whether arrayData is full
            arrayData[i]= current.displayLink();    // append to array
            i++;
            current = current.next;                 // move to the next linked list
        }
        current= QFirst;                             // track non-emergency linked list
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

