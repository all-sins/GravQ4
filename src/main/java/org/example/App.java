package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class App 
{
    static long masterTimerStart = System.currentTimeMillis();
    // static long endIndex = (long) (Math.pow( 10, 12));
    static long endIndex = 99;
    static Map<Long, String> threadRanges = new HashMap<>();
    static AtomicLong computedNumbers = new AtomicLong(0);
    static int coreCount = 8;
    static AtomicLong finalSum = new AtomicLong();

    public static void main( String[] args ) {


        // Modulus digit sum compute.
        // Time differences.
        AnalyticsAgent analyticsAgent = new AnalyticsAgent();
        analyticsAgent.runAnalyticsAgent(1000);
        analyticsAgent.runTimingAgent(1000, 0);

        // Statically targeting systems with 8-threads.
        // Needs manual tweaking for different systems.
        // TODO: Dynamically calculate thread ranges for x-cores.
        // Above defined -> static long endIndex = (long) (Math.pow((double) 10, (double) 12));
        for (long i = 0; i < coreCount; i++) {
            // Iteration 0 startRange = 1 not 0, but this is OK,
            // since the compute value of 0 is 0, ergo no-impact.
            long startRange = (i * (endIndex / coreCount) + 1);
            long endRange = (i + 1) * (endIndex / coreCount);
            // Each thread has its own stack, so no need to worry
            // about variable scope.
            threadRanges.put(i, "["+startRange+" - "+endRange+"]");
            new Thread(() -> {
                long tmpSum = 0;
                for (long j = startRange; j <= endRange; j++) {
                    char[] chars = String.valueOf(j).toCharArray();
                    int sum = 0;
                    for (char eachChar : chars) {
                        sum += (int) eachChar - '0';
                    }
                    tmpSum += sum;
                    computedNumbers.addAndGet(1);
                }
                finalSum.addAndGet(tmpSum);
            }).start();
        }

        // Print out ranges.
        for (long x = 0; x < threadRanges.size(); x++) {
            System.out.println("Thread-"+x+" "+threadRanges.get(x));
        }
    }
}
