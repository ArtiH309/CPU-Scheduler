public class Process {
    //instance variables representing process attributes
    private int processID;
    private int arrivalTime;
    private int burstTime;
    private int initialBurstTime;
    private int priority;
    private int completionTime;
    private int processTime;

    //Constructor
    public Process(int processID, int arrivalTime, int burstTime, int priority){
        this.processID = processID;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.initialBurstTime = burstTime;
        this.priority = priority;
    }
    //helper methods
    public int getProcessID(){
        return processID;
    }
    public int getArrivalTime(){
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime){
        this.arrivalTime = arrivalTime;
    }
    public int getBurstTime(){
        return burstTime;
    }
    public void setBurstTime(int burstTime){
        this.burstTime = burstTime;
    }
    public int getPriority(){
        return priority;
    }
    public int getCompletionTime(){
        return completionTime;
    }
    public void setCompletionTime(int timer){
        completionTime = timer;
    }
    public int getProcessTime(){
        return processTime;
    }
    public int getTurnaroundTime(){
        return completionTime - arrivalTime;
    }
    public int getWaitTime(){
        return getTurnaroundTime() - initialBurstTime;
    }
    public int getResponseTime(int timer){
        return timer - getArrivalTime();
    }
    //various necessary methods and a tostring
    public void runProcess(int quantum){
        burstTime -= quantum;
        processTime += quantum;
    }
    public void idlingProcess(int contextSwitchTime){
        processTime += contextSwitchTime;
    }   
}
