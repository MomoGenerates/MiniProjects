package util;

public class ConsolePrinter {
    static AnimatedText animate = new AnimatedText();

    public static synchronized void print(String text) {
        System.out.println(text);
    }
    
    public static synchronized void println(String text) {
        animate.animatedText(text, 25);
    }

    public static synchronized void printTimer(String text) {
        System.out.print("\r" + text);
    }
}

// Then in your timer thread
// ConsolePrinter.println("Time Remaining: " + String.format("%02d:%02d", seconds / 60, seconds % 60));

// // And in your main thread
// ConsolePrinter.println("Question 1 of 2");