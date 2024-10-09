package util;

import java.io.IOException;
import java.util.logging.*;
import java.util.ArrayDeque;
import java.util.Queue;


public class GameLogger {
    private static final Logger LOGGER = Logger.getLogger("Game-Logger");
    private static GameLogger instance = null;
    private static final Queue<LogRecord> Q =  new ArrayDeque<LogRecord>();
    
    protected static Thread trd = new Thread(){
        public void run(){
            LogRecord record;
            try{
                if(GameLogger.glInit()){
                    for(;;){
                        record = Q.poll();
                        if(record != null){
                        LOGGER.log(record);
                        }
                    }
                }
            }catch(Exception e){
                System.out.println(e);
            }
        }
    };

    public static GameLogger getGameLogger(){
        if(instance == null){//singleton patterns
            makeSingleGameLogger();
            instance = new GameLogger();
        }
        return instance;
    }
    public static boolean glInit(){//returns true if logger is initialized
        return instance != null;
    }

    private static void makeSingleGameLogger(){
        FileHandler fileHandler;
        ConsoleHandler consoleHandler;
        System.setProperty("java.util.logging.SimpleFormatter.format", 
        "%4$s %2$s %5$s%6$s%n");
        try{
        //consoleHandler = new ConsoleHandler();
        //consoleHandler.setLevel(Level.ALL);
        fileHandler = new FileHandler("./logfile.log", 0, 1);//filename goes here
        fileHandler.setLevel(Level.ALL);
        SimpleFormatter simple = new SimpleFormatter();
        fileHandler.setFormatter(simple);
        //consoleHandler.setFormatter(simple);
        LOGGER.addHandler(fileHandler);
        //LOGGER.addHandler(consoleHandler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.log(Level.CONFIG, "Initialized GameLogger!");
        }catch(IOException e){
            System.out.println("ERROR CREATING LOG FILE :(");
        }
        trd.start();
    }

    private String getCallerFunctionName(){
        String[] r;
        StackTraceElement[] stack = new Exception().getStackTrace();
        for(int i = 0; i < stack.length-1; i++){
            if(!stack[i].getClassName().equals(GameLogger.class.getName()) && !stack[i - 1].getClassName().equals(GameLogger.class.getName())){
                r = stack[i].getClassName().split("\\.");
                return r[r.length - 1];
            }
        }
        return "nothing";
    }
    private String getCallerClassName(){
        String[] r;
        StackTraceElement[] stack = new Exception().getStackTrace();
        for(int i = 0; i < stack.length-1; i++){
            if(!stack[i].getClassName().equals(GameLogger.class.getName())){
                r = stack[i].getClassName().split("\\.");
                return r[r.length - 1];
            }
        }
        return "nothing";
    }
    public void LogMessage(LogRecord record){
        System.out.println(record.getMessage());
        Q.add(record);
    }
    public void LogMessage(String s){
        System.out.println(s);
        LogRecord record = new LogRecord(Level.FINER, s);
        Q.add(record);
    }
    public void LogMessage(Object obj){
        String message = obj.toString();
        LogRecord record = new LogRecord(Level.FINER, message);
        System.out.println(record.getMessage());
        Q.add(record);
    }

    public void LogMessageWithoutClassSearch(LogRecord record){
        LOGGER.log(record);
    }
    
}
