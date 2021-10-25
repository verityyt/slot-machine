package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    private static final boolean enableDebug = true;
    private static final boolean enableTrace = true;
    private static final boolean enableAnimation = true;
    private static final boolean enableLogic = true;
    private static final boolean enableUI = true;

    private static final boolean enableColors = true;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREY = "\u001B[90m";

    public static void animation(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            if (enableAnimation) {
                System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_YELLOW + "ANIMATION" + ANSI_GREY + "] " + ANSI_RESET + text);
            }
        } else {
            System.out.println("[" + date + "]: [ANIMATION] " + text);
        }
    }

    public static void logic(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            if (enableLogic) {
                System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_BLUE + "LOGIC" + ANSI_GREY + "] " + ANSI_RESET + text);
            }
        } else {
            System.out.println("[" + date + "]: [LOGIC] " + text);
        }
    }

    public static void ui(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            if (enableUI) {
                System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_GREEN + "UI" + ANSI_GREY + "] " + ANSI_RESET + text);
            }
        } else {
            System.out.println("[" + date + "]: [UI] " + text);
        }
    }

    public static void debug(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            if (enableDebug) {
                System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_CYAN + "DEBUG" + ANSI_GREY + "] " + ANSI_RESET + text);
            }
        } else {
            System.out.println("[" + date + "]: [DEBUG] " + text);
        }
    }

    public static void trace(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            if (enableTrace) {
                System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_PURPLE + "TRACE" + ANSI_GREY + "] " + ANSI_RESET + text);
            }
        } else {
            System.out.println("[" + date + "]: [TRACE] " + text);
        }
    }

    public static void warn(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_YELLOW + "WARN" + ANSI_GREY + "] " + ANSI_RESET + text);
        } else {
            System.out.println("[" + date + "]: [WARN] " + text);
        }
    }

    public static void error(String text) {
        String date = new SimpleDateFormat("HH:mm:ss").format(new Date());
        if (enableColors) {
            System.out.println(ANSI_GREY + "[" + date + "]: [" + ANSI_RED + "ERROR" + ANSI_GREY + "] " + ANSI_RESET + text);
            System.exit(-1);
        } else {
            System.out.println("[" + date + "]: [ERROR] " + text);
        }
    }

}
