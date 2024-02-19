package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import static org.example.App.finalSum;

public class AnalyticsAgent {
    // Periodically print info about how far we have gotten.
    // COUNT  %DONE  COMPUTE  CURSUM
    // 3467   0.06%  5305     350002
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private long currentCount;
    private float percentDone;
    private float calcsPerSecond;
    private float etaHours;

    public void measure() {
        currentCount = App.computedNumbers.get();
        percentDone = ((float) currentCount / (float) App.endIndex) * 100.0f;
    }

    public void display() {
        System.out.println("Computed Numbers: "+currentCount);
        System.out.println("Completed Percentage "+decimalFormat.format(percentDone)+" %");
        System.out.println("ETC in Hours: "+decimalFormat.format(etaHours));
        System.out.println(decimalFormat.format(calcsPerSecond)+" / s");
    }

    public void runAnalyticsAgent(int analyticsThrottle) {
        new Thread(() -> {
            while (App.computedNumbers.get() < App.endIndex || calcsPerSecond != 0) {
                measure();
                display();
                try {
                    Thread.sleep(analyticsThrottle);
                } catch (Exception e) {
                    System.out.println("Analytics Agent could not sleep!");
                }
                System.out.println();
            }
        }).start();
    }

    public void runTimingAgent(int timingThrottle, int initialDelay) {
        new Thread(() -> {
            while (App.computedNumbers.get() < App.endIndex || calcsPerSecond != 0) {
                try {
                    Thread.sleep(initialDelay);
                } catch (Exception e) {
                    System.out.println("Timing Agent initial delay failed!");
                }
                long thenCount = App.computedNumbers.get();
                long thenTime = System.currentTimeMillis();
                try {
                    Thread.sleep(timingThrottle);
                } catch (Exception e) {
                    System.out.println("Timing Agent could not sleep!");
                }
                long deltaTime = System.currentTimeMillis() - thenTime;
                long deltaCount = App.computedNumbers.get() - thenCount;
                float calcsPerMs = (float) deltaCount / (float) deltaTime;
                calcsPerSecond = calcsPerMs * 1000.0f;
                etaHours = (App.endIndex / calcsPerSecond) / 60 / 60;
            }
            System.out.println("#######################");
            System.out.println("####### D O N E #######");
            System.out.println("#######################");
            double now = (double) System.currentTimeMillis();
            double then = (double) App.masterTimerStart;
            double millisInHours = 3600000.0f;
            double hoursPassed = (now - then) / millisInHours;
            System.out.println(decimalFormat.format(hoursPassed)+" hours elapsed.");
            System.out.println(finalSum.get());

            String filePath = "q4.result";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(String.valueOf(finalSum.get()));
                System.out.println("Writing result...");
                System.out.println(filePath);
            } catch (IOException e) {
                System.out.println("Couldn't write file to disk!");
            }
        }).start();
    }
}
