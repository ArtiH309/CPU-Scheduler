import java.util.*;
import java.text.DecimalFormat;


public class CPU {
    //queues for Processes, ready, waiting, and finished
    private PriorityQueue<Process> processQueue;
    private Queue<Process> readyQueue = new LinkedList<>();
    private Queue<Process> waitingQueue = new LinkedList<>();
    private Queue<Process> finished = new LinkedList<>();
    //boolean to track completion status
    private boolean complete;
    private int numOfProcesses;
    private static final int contextSwitchTime = 2; //time taken to perform a context switch is 1ms
    private int contextSwitches = 0;
    private int timer = 0;

    public CPU(ArrayList <Process> processes){ //Constructor
        Comparator<Process> priorityComparator = Comparator.comparingInt(Process::getPriority); //making sure lower number is higher priority
        this.processQueue = new PriorityQueue<Process>(priorityComparator);
        this.processQueue.addAll(processes);
        numOfProcesses = processes.size();
    }
    public void run(int timeQuantum){ //method to run the simulation
        System.out.println("\nStarting:");
        while(!complete){ //while it is not complete
            if(!processQueue.isEmpty()){ //if main queue is not empty add them to ready queue
                readyQueue.add(processQueue.poll());
            }
            else if (!waitingQueue.isEmpty()){ //if waiting queue is empty, add processes in waiting to ready
                readyQueue.add(waitingQueue.poll());
            }
            if (readyQueue.isEmpty()){ //wait for new process to arrive
                timer++;
                continue;
            }
            Process current = readyQueue.peek(); //creates Process object to represent current process in front of queue
            
            //first part of round robin, process coming in for execution
            System.out.println("Process " + current.getProcessID() + " Coming In.");
            printCurrentDetails(); //print current process details
            System.out.println();//new line

            //if burst time is less than quantum, makes sure that timer is updated correctly
            int runTime = Math.min(timeQuantum, current.getBurstTime()); //chooses least of the two
            current.runProcess(runTime); //run the first process in the queue
            timer += runTime; //adds time quantum or remaining burst time to the timer

            //second part of rr, process finishing it's alloted execution time and moving to waiting
            System.out.println("Process " + current.getProcessID() + " Switching Out."); 
            printCurrentDetails(); //prints details of the process after its alloted time
            System.out.println();

            if(current.getBurstTime()<=0){ //if current process burst less than or 0, it is completed
                current.setCompletionTime(timer); //sets the processes completion time to the current time on the timer
                System.out.println("Process " + current.getProcessID() + " completed at " + current.getCompletionTime() + "ms.");
                System.out.println();
                finished.add(readyQueue.poll()); //adds done process to finished queue
            }
            else{ //if it isn't done, process added back to waiting from ready
                waitingQueue.add(readyQueue.poll());
                for (Process p : waitingQueue){
                    p.idlingProcess(contextSwitchTime);
                }
            }
            if(!complete){ //if the processes are not complete, a context switch is performed and kept track of
                contextSwitches++;
                timer += contextSwitchTime;
            }
            if(finished.size() == numOfProcesses){ //if the size of finished queue is the same size as the number of processes, they are all done
                complete = true; //sets complete boolean to true and ends the while loop
            }
        }
        printStatistics(timeQuantum); //after all execution finishes print final stats
    }
    public void printCurrentDetails(){ //method to print details of current process
        System.out.println("\nCPU Timer: " + timer + "ms.");
        System.out.println("Process ID currently running: " + readyQueue.peek().getProcessID());
        System.out.println("Burst Time: " + readyQueue.peek().getBurstTime());
    }
    public String getThroughput(int size, int time){ //function to get throughput in decimal form
        DecimalFormat df = new DecimalFormat("00.##");
        double throughput = 1.0*size/time;
        return df.format(throughput);
    }
    public String getUtilization(){ //function to get utilization in dec form
        DecimalFormat df = new DecimalFormat("00.##");
        double utilization = (1-(1.0*(contextSwitchTime*contextSwitches)/timer))*100;
        return df.format(utilization);
    }
    public void printStatistics(int timeQuantum){ //method to print final stats after completion
        double waitTime = 0;
        double turnaroundTime = 0;
        //loop iterates through finished queue to get each process's details
        for(Process p : finished){
            waitTime+=p.getWaitTime();
            turnaroundTime+=p.getTurnaroundTime();
        }
        System.out.println("\nCPU Statistics using time quantum: " + timeQuantum);
        System.out.println("Total CPU Time: " + timer + "ms");
        System.out.println("Throughput: " + getThroughput(finished.size(), timer));
        System.out.println("CPU Utilization: " + getUtilization() + "%");
        System.out.println("Context Switches Performed: " + contextSwitches);
        System.out.println("Average Turnaround Time: " + (turnaroundTime/finished.size()));
        System.out.println("Average Waiting Time: " + (waitTime/finished.size()));
    }
}
