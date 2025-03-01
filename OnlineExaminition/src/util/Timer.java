package util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Timer extends Thread {
    
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m"; 
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m"; 
    
    private int timeLimit;  
    private final AtomicBoolean isRunning;
    private final int initialTime;
    private final Runnable timeUpCallback;
    
    public Timer(int minutes, Runnable timeUpCallback) {
        this.timeLimit = minutes * 60;
        this.initialTime = timeLimit;
        this.isRunning = new AtomicBoolean(true);
        this.timeUpCallback = timeUpCallback;
    }
    
    @Override
    public void run() {
        System.out.println(); // Initial blank line for timer
        while (timeLimit > 0 && isRunning.get()) {
            String timeDisplay = formatTime(timeLimit);
            
            String color = GREEN; 
            if (timeLimit <= initialTime * 0.1) {
                color = RED; 
            } else if (timeLimit <= initialTime * 0.5) {
                color = YELLOW;
            }
            
            ConsolePrinter.printTimer(color + "Time Remaining: " + timeDisplay + RESET);
            
            try {
                TimeUnit.SECONDS.sleep(1);
                timeLimit--;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        if (timeLimit == 0) {
            System.out.println("\n" + RED + "TIME'S UP!" + RESET);
            if (timeUpCallback != null) {
                timeUpCallback.run();
            }
        }
    }
    
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }
    
    public void stopTimer() {
        isRunning.set(false);
    }
    
    public boolean isTimeRemaining() {
        return timeLimit > 0;
    }
}
