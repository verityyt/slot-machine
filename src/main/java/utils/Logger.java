package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final boolean enableDebug = true;
    private static final boolean enableTrace = true;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static final String ANSI_GREY = "\u001B[90m";

    public static void info(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_GREEN + "INFO" + ANSI_GREY + "] " + ANSI_RESET + text);
    }

    public static void debug(String text) {
        if(enableDebug) {
            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_CYAN + "DEBUG" + ANSI_GREY + "] " + ANSI_RESET + text);
        }
    }

    public static void trace(String text) {
        if(enableTrace) {
            String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
            System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_PURPLE + "TRACE" + ANSI_GREY + "] " + ANSI_RESET + text);
        }
    }

    public static void warn(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_YELLOW + "WARN" + ANSI_GREY + "] " + ANSI_RESET + text);
    }

    public static void error(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_RED + "ERROR" + ANSI_GREY + "] " + ANSI_RESET + text);
    }

}
