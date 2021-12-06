package main.java;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MyLogger {
    public static Logger logger = Logger.getLogger("ConsoleLogger");
    
    public static void logWarningMessage(String message){
        logMessage(Level.WARNING, message);
    }

    public static void logWarningMessage(String message, Exception e){
        logMessage(Level.WARNING, message, e);
    }

    public static void logErrorMessage(String message){
        logMessage(Level.SEVERE, message);
    }

    public static void logErrorMessage(String message, Exception e){
        logMessage(Level.SEVERE, message, e);
    }

    public static void logInfoMessage(String message){
        logMessage(Level.INFO, message);
    }

    public static void logInfoMessage(String message, Exception e){
        logMessage(Level.INFO, message, e);
    }

    private static void logMessage(Level level, String message){
        logger.log(level, message);
    }

    private static void logMessage(Level level, String message, Exception e){
        logger.log(level, message, e);
    }


}
