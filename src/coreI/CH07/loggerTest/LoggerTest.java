package coreI.CH07.loggerTest;


import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerTest {

    public static void main(String[] args) {
        Logger myLogger = Logger.getLogger("myLogger");
        myLogger.setLevel(Level.FINE);
        myLogger.setUseParentHandlers(false);
        var handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        myLogger.addHandler(handler);
        myLogger.fine("fine");

        System.err.println("sss");
        System.out.println("sss");

    }
}
