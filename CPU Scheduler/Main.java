import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
//necessary packages
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filepath = args[0]; //gets filepath and time quantum from user
        int timeQuantum = Integer.parseInt(args[1]);
        ArrayList <Process> processes = new ArrayList<Process>(); //new arraylist of processes
        //necessary try catch block
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))){ 
            String line; //reads the csv and gets relevant info
            bufferedReader.readLine();
            while((line = bufferedReader.readLine())!=null){ //loop to read csv
                String[] process = line.split(",");
                int processID = Integer.parseInt(process[0].trim());
                int arrivalTime = Integer.parseInt(process[1].trim());
                int burstTime = Integer.parseInt(process[2].trim());
                int priority = Integer.parseInt(process[3].trim());
                //adds info from each row of csv as a process
                processes.add(new Process(processID, arrivalTime, burstTime, priority));
            }
            bufferedReader.close();
        }
        catch(IOException e){
            System.out.println("Error reading file: " + e.getMessage());
            scanner.close();
            return;
        }
        //new CPU object to run the scheduler simulation
        CPU scheduler = new CPU(processes);
        scheduler.run(timeQuantum);
        scanner.close();
    }
}
