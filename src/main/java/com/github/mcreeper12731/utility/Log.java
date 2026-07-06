package com.github.mcreeper12731.utility;

import java.util.Collection;
import java.util.Iterator;

public class Log {

    private static final boolean ENABLED = true;

    private static final String RESET = "\u001B[0m";
    private static final String GRAY = "\u001B[37m";
    private static final String CYAN = "\u001B[36m";

    public static void debug(String prefix, String message) {
        if (ENABLED) {
            String[] messageLines = message.split("\n");
            for (String line : messageLines) {
                System.out.printf("%s%s> %s%s%n", GRAY, prefix, RESET, line);
            }
        }
    }

    public static void debug(String prefix, Object message) {
        debug(prefix, message.toString());
    }

    public static <T> void debug(String prefix, Iterable<T> message) {
        String[] className = message.getClass().getName().split("\\.");
        if (message instanceof Collection<?> collection && collection.size() <= 2) {
            debug(prefix, collection.toString());
            return;
        }
        debug(prefix,  className[className.length - 1] + "[");
        for (T row : message) {
            debug(prefix, "  " + row);
        }
        debug(prefix, "]");
    }

    public static <T> void debug(String prefix, Iterator<T> message) {
        String[] className = message.getClass().getName().split("\\.");
        debug(prefix,  className[className.length - 1] + "[");
        while (message.hasNext()) {
            debug(prefix, "  " + message.next());
        }
        debug(prefix, "]");
    }

    public static void debug(String prefix, Object... messages) {
        if (messages.length == 0) {
            debug(prefix, "");
            return;
        }
        StringBuilder messageBuilder = new StringBuilder();
        for (Object message : messages) {
            messageBuilder.append(message).append(" ");
        }
        messageBuilder.deleteCharAt(messageBuilder.length() - 1);
        debug(prefix, messageBuilder.toString());
    }

    public static void print(String prefix, String message) {
        System.out.printf("%s%s> %s%s%n", CYAN, prefix, RESET, message);
    }

    public static void print(String prefix, Object object) {
        print(prefix, object.toString());
    }

    public static void print(String prefix, Object... messages) {
        StringBuilder messageBuilder = new StringBuilder();
        for (Object message : messages) {
            messageBuilder.append(message).append(" ");
        }
        messageBuilder.deleteCharAt(messageBuilder.length() - 1);
        print(prefix, messageBuilder.toString());
    }

}
