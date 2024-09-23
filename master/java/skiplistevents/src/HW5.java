/*
        This file uses SkipList.java and Event.java to manage a schedule of events using a Skip List
  implemented using doubly linked lists. It can handle errors regarding clashing event times in the skip
  list, or the non-existence of one in the case of deleting an event.

        The file is currently using FakeRandHeight. If you would like to see its functionality with
  real randomization, uncomment the do while in insert in SkipList.java, and comment anything with
  a ******* above it.
  
		FakeRandHeight was not written by me, and was not able to be redistributed. As such, it is not 
  included in this repository of this project.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HW5 {
    // Initialize the skip list as a schedule of events.
    public static SkipList schedule = new SkipList();
    // Initialize FakeRandHeight *******
    public static FakeRandHeight randomHeight = new FakeRandHeight();

    public static void main(String[] args) {
        // Initialize the file.
        File file = new File(args[0]);
        try {
            // Initialize the scanner to read through the file.
            Scanner scanner = new Scanner(file);
            // While the scanner has something to scan...
            while (scanner.hasNext()) {
                String temp = scanner.next();
                // Switch to the correct method based on what the scanner has read.
                if (temp.equals("AddEvent")) {
                    // Then, read the rest of the line if the method needs to.
                    AddEvent(scanner.nextInt(), scanner.nextLine().replaceAll("\\s",""));
                } else if (temp.equals("PrintSkipList")) {
                    PrintSkipList();
                } else if (temp.equals("CancelEvent")) {
                    CancelEvent(scanner.nextInt());
                } else if (temp.equals("GetEvent")) {
                    GetEvent(scanner.nextInt());
                } else if (temp.equals("GetEventsBetweenTimes")) {
                    GetEventBetweenTimes(scanner.nextInt(), scanner.nextInt());
                } else if (temp.equals("GetEventsForOneDay")) {
                    GetEventsForOneDay(scanner.nextInt());
                } else if (temp.equals("GetEventsForTheRestOfTheDay")) {
                    GetEventsForTheRestOfTheDay(scanner.nextInt());
                } else if (temp.equals("GetEventsFromEarlierInTheDay")) {
                    GetEventsFromEarlierInTheDay(scanner.nextInt());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    // Insert into skip list.
    public static void AddEvent (int time, String name) {
        // Test to see if the time to be added is already in the skip list.
        Event test = schedule.search(time);
        // If it is, don't add anything and print an error.
        if (test.time == time) {
            System.out.println("AddEvent " + time + " " + name + " ExistingEventError:" + test.name);
            return;
        }
        // ******* Just the randomHeight being passed into insert.
        Event inserted = schedule.insert(time, name, randomHeight);
        System.out.println("AddEvent " + inserted.time + " " + inserted.name);
    }


    // Print the skip list.
    public static void PrintSkipList () {
        System.out.println("PrintSkipList");
        schedule.printSkipList();
    }


    // Remove from skip list.
    public static void CancelEvent (int time) {
        // Test to see if the time to be removed is not in the skip list.
        Event test = schedule.search(time);
        // If it isn't, don't remove anything and print an error.
        if (test.time != time) {
            System.out.println("CancelEvent " + time + " NoEventError");
            return;
        }
        Event deleted = schedule.delete(time);
        System.out.println("CancelEvent " + deleted.time + " " + deleted.name);
    }


    // Print event corresponding to time.
    public static void GetEvent (int time) {
        Event gotten = schedule.search(time);
        // If the event found is not actually of the time we're looking for
        if (gotten.time != time) {
            // Print an error instead.
            System.out.println("GetEvent " + time + " none");
            return;
        }
        System.out.println("GetEvent " + gotten.time + " " + gotten.name);
    }


    // Print every event between the times.
    public static void GetEventBetweenTimes (int beforeEvent, int afterEvent) {
        System.out.print("GetEventsBetweenTimes " + beforeEvent + " " + afterEvent + " ");
        // Initialize an event at the beginning of the time frame.
        Event between = schedule.search(beforeEvent);
        // If the search ended up behind the starting line, move it.
        if (between.time < beforeEvent) {
            between = between.next;
        }
        // While the event pointer hasn't reached the end of the time frame
        while (between.time <= afterEvent) {
            // Print each event.
            System.out.print(between.time + ":" + between.name + " ");
            between = between.next;
        }
        System.out.println();
    }


    // Print every event whose times start with the given day.
    public static void GetEventsForOneDay (int day) {
        System.out.print("GetEventsForOneDay " + day + " ");
        // Initialize an event at the beginning of the time frame.
        day = day * 100;
        Event forTheDay = schedule.search(day);
        if (forTheDay.time < day) {
            forTheDay = forTheDay.next;
        }
        // While the event pointer hasn't reached the end of the time frame
        while (forTheDay.time <= (day+100)) {
            // Print each event.
            System.out.print(forTheDay.time + ":" + forTheDay.name + " ");
            forTheDay = forTheDay.next;
        }
        System.out.println();
    }


    // Starting at given time, print every event still within its day.
    public static void GetEventsForTheRestOfTheDay (int time) {
        System.out.print("GetEventsForTheRestOfTheDay " + time + " ");
        // Initialize an event at the beginning of the time frame.
        int day = (time%100000) - (time % 100);
        Event rest = schedule.search(time);
        // If the search ended up behind the starting line, move it.
        if (rest.time < time) {
            rest = rest.next;
        }
        // While the event pointer hasn't reached the end of the time frame
        while (rest.time <= (day+100)) {
            // Print each event.
            System.out.print(rest.time + ":" + rest.name + " ");
            rest = rest.next;
        }
        System.out.println();
    }


    // Starting at beginning of given time's day, print every event up to that time.
    public static void GetEventsFromEarlierInTheDay (int time) {
        System.out.print("GetEventsFromEarlierInTheDay " + time + " ");
        // Initialize an event at the beginning of the time frame.
        int day = (time%100000) - (time % 100);
        Event earlier = schedule.search(day);
        if (earlier.time < day) {
            earlier = earlier.next;
        }
        // While the event pointer hasn't reached the end of the time frame
        while (earlier.time <= time) {
            // Print each event.
            System.out.print(earlier.time + ":" + earlier.name + " ");
            earlier = earlier.next;
        }
        System.out.println();
    }
}