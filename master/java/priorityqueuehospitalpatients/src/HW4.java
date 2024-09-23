/*
        This program will insert, organize, and manage a priority queue of patients based on the severity of their
  injury and the time they arrived at the emergency room. Keeps track of the doctors available and when they finish
  treating their patients.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HW4 {


    // Initialize the HPQs for each of the patients, doctors, and emergency rooms.
    public static HeapPriorityQueue Patients = new HeapPriorityQueue();
    public static HeapPriorityQueue Doctors = new HeapPriorityQueue();
    public static HeapPriorityQueue ERs = new HeapPriorityQueue();


    public static void main(String[] args) {
        // Initialize the file.
        File file = new File(args[0]);
        // The hospital begins with two doctors.
        Doctor Alice = new Doctor(800, "Alice");
        Doctor Bob = new Doctor(800, "Bob");
        Doctors.insert(Alice.time, Alice);
        Doctors.insert(Bob.time, Bob);
        ER tempER;
        try {
            // Initialize the scanner to read through the file.
            Scanner scanner = new Scanner(file);
            // While the scanner has something to scan.
            while (scanner.hasNext()) {
                String temp = scanner.next();
                // If the scanner reads "patientArrives"
                if (temp.equals("patientArrives")) {
                    // Read the rest of the line into a new patient.
                    Patient temPatient = new Patient(scanner.nextInt(), scanner.next(), scanner.nextInt());
                    // If there is an emergency room currently in progress.
                    if (!ERs.isEmpty()) {
                        tempER = (ER) ERs.min().getValue();
                        // Check to see if the highest priority ER should be done before the next patient arrives.
                        if (temPatient.time >= tempER.eta) {
                            finishTreating();
                        }
                    }
                    // After the ER is (or isn't) done treating, add the new patient.
                    patientArrives(temPatient);
                // Otherwise, the only other input is when a doctor arrives.
                } else if (temp.equals("doctorArrives")) {
                    // Same situation as the patients.
                    Doctor temDoctor = new Doctor(scanner.nextInt(), scanner.next());
                    if (!ERs.isEmpty()) {
                        tempER = (ER) ERs.min().getValue();
                        if (temDoctor.time >= tempER.eta) {
                            finishTreating();
                        }
                    }
                    System.out.println("doctorArrives " + temDoctor.time + " " + temDoctor.name);
                    doctorArrives(temDoctor);
                }
            }
            // After the file is done being read, the rest of the ERs need to be finished in order.
            while (!ERs.isEmpty()) {
                finishTreating();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void patientArrives(Patient patient) {
        // Using the patient's ESI as the key, insert the patient into the Patients HPQ.
        Patients.insert(patient.esi, patient);
        System.out.println("patientArrives " + patient.time + " " + patient.name + " " + patient.esi);
        // If there is a doctor available
        if (!Doctors.isEmpty()) {
            // Start treating that patient immediately.
            startTreating(patient.time);
        }
    }


    public static void doctorArrives(Doctor doctor) {
        // Same situation as the patients above.
        Doctors.insert(doctor.time, doctor);
        if (!Patients.isEmpty()) {
            startTreating(doctor.time);
        }
    }


    public static void startTreating(int time) {
        // Create a new doctor and patient using the min of each
        Doctor newDoctor = (Doctor) Doctors.removeMin().getValue();
        Patient newPatient = (Patient) Patients.removeMin().getValue();
        System.out.println("doctorStartsTreatingPatient " + time + " " + newDoctor.name + " " + newPatient.name);
        // Calculate the estimated time it will take to finish treating the patient, 2^(6-esi).
        int eta = time + (int) Math.pow(2, (6-newPatient.esi));
        // Fix the eta if it goes out of time.
        if ((eta % 100) >= 60) {
            eta = eta - 60;
            eta = eta + 100;
        }
        if ((eta - 2400) > 0) {
            eta = eta - 2400;
        }
        // Create a new emergency room to fit the patient, doctor, and their eta.
        ER newER = new ER(newPatient, newDoctor, eta);
        ERs.insert(newER.eta, newER);
    }


    public static void finishTreating() {
        // Create a new ER using the min of its HPQ. The patient is effectively freed from the program.
        ER newER = (ER) ERs.removeMin().getValue();
        System.out.println("doctorFinishesTreatmentAndPatientDeparts " + newER.eta + " " + newER.doctor.name + " " + newER.patient.name);
        // The doctor has more work to do, and will be put back into the doctors HPQ.
        newER.doctor.time = newER.eta;
        doctorArrives(newER.doctor);
    }
}