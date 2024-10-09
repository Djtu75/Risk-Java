package util;

import java.util.logging.LogRecord;
import java.util.logging.Level;
import java.util.ArrayList;

public class GameLoggerTest {
    public static void main(String args[]){
        GameLogger GL = GameLogger.getGameLogger();
        ArrayList<LogRecord> records = new ArrayList<LogRecord>(20);
        ArrayList<LogRecord> records2 = new ArrayList<LogRecord>(20);
        LogRecord record = new LogRecord(Level.INFO, "TESTING TESTING 123 123 123");
        int reps = 100_000;
        int tests = 10;
        long end;
        for(int j = 1; j < tests + 1; j++){
            long start = System.nanoTime();
            int curReps = reps - ((j-1)*(reps / tests)) ;
            for(int i = 0; i < curReps; i++){
                GL.LogMessageWithoutClassSearch(record);
            }
            end = System.nanoTime();
            long end2;
            long start2 = System.nanoTime();
            for(int i = 0; i < curReps; i++){
                GL.LogMessage(record);
            }
            end2 = System.nanoTime();
            String resultsString1 = "First  Log benchmark resutls for "+ Integer.toString(curReps)+ " reps took: "+ Long.toString(end - start) + " (ns) or " + Double.toString(((double)(end-start))/1_000_000_000)+ " (s)";
            String resultsString2 = "Second Log benchmark resutls for "+ Integer.toString(curReps)+ " reps took: "+ Long.toString(end2 - start2) + " (ns) or " + Double.toString(((double)(end2-start2))/1_000_000_000)+ " (s)";
            records.add(new LogRecord(Level.SEVERE, resultsString1));
            records2.add(new LogRecord(Level.SEVERE, resultsString2));
        }
        for(LogRecord r: records){
            GL.LogMessage(r);
        }
        GL.LogMessage(new LogRecord(Level.SEVERE, "*****************************************************************************************************"));
        for(LogRecord r: records2){
            GL.LogMessage(r);
        }
    }
    
}
