package com.parintorn0.app;

class QueuingSystem
{
    //------------------Object_in_Queueing_system------------------//
    public int numQPatients;     // Number of non-emergency patients
    public int numPPatients;     // Number of emergency patients
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
    public void insertLast(String name, String reason, Boolean emergency, String time) // Enqueue
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
    public void deleteFirst() // Dequeue
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
    public String[] displayPatients() // get an array of patients data (for display in UI)
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

