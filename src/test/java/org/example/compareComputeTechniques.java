package org.example;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Comparator;

public class compareComputeTechniques extends TestCase {
    public class ResultTiming {
        String name;
        Long result;
        Long timing;

        public ResultTiming(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getResult() {
            return result;
        }

        public void setResult(Long result) {
            this.result = result;
        }

        public Long getTiming() {
            return timing;
        }

        public void setTiming(Long timing) {
            this.timing = timing;
        }
    }
    ArrayList<String> computeRanges = new ArrayList<>();
    long castCompute(long range) {
        if (range == 1) {return 1;}
        long sum = 0;
        for (int i = 0; i < range; i++) {
            char[] chars = String.valueOf(i).toCharArray();
            for (char eachChar : chars) {
                sum += (int) eachChar - '0';
            }
        }
        return sum;
    }

    long castComputeNoCharSubtract(long range) {
        if (range == 1) {return 1;}
        long sum = 0;
        for (int i = 0; i < range; i++) {
            char[] chars = String.valueOf(i).toCharArray();
            for (char eachChar : chars) {
                sum += (int) eachChar - 48;
            }
        }
        return sum;
    }

    long castComputeNoCast(long range) {
        if (range == 1) {return 1;}
        long sum = 0;
        for (int i = 0; i < range; i++) {
            char[] chars = String.valueOf(i).toCharArray();
            for (char eachChar : chars) {
                sum += eachChar;
                sum -= 48;
            }
        }
        return sum;
    }

    long modulusCompute(double range) {
        if (range == 1) {return 1;}
        long digitSum = 0;
        for (long i = 0; i < range; i++) {
            double tmp = i;
            while (tmp > 0) {
                digitSum += tmp % 10;
                tmp = Math.floor(tmp / 10);
            }
        }
        return digitSum;
    }

    public void testCastIsFasterThanModulus() {
        long masterRange = (long) Math.pow(10,9);
        long currentRange = 1;

        while (currentRange != masterRange) {
            long then = 0L;

            ResultTiming cast = new ResultTiming("CAST");
            then = System.nanoTime();
            cast.setResult(castCompute(currentRange));
            cast.setTiming(System.nanoTime() - then);

            ResultTiming modulus = new ResultTiming("MODULUS");
            then = System.nanoTime();
            modulus.setResult(modulusCompute(currentRange));
            modulus.setTiming(System.nanoTime() - then);

            ResultTiming castNoCharSubtract = new ResultTiming("NOCHARSUB");
            then = System.nanoTime();
            castNoCharSubtract.setResult(castComputeNoCharSubtract(currentRange));
            castNoCharSubtract.setTiming(System.nanoTime() - then);

            ResultTiming castNoCast = new ResultTiming("CASTNOCAST");
            then = System.nanoTime();
            castNoCast.setResult(castComputeNoCast(currentRange));
            castNoCast.setTiming(System.nanoTime() - then);

            // assertEquals(cast.getTiming(), modulus.getTiming());
            // assertTrue(castDeltaNano < modulusDeltaNano);

            ArrayList<ResultTiming> results = new ArrayList<>();
            results.add(cast);
            results.add(castNoCast);
            results.add(castNoCharSubtract);
            results.add(modulus);
            results.sort(Comparator.comparing(ResultTiming::getTiming, Comparator.naturalOrder()));
            ResultTiming min = results.get(0);
            ResultTiming secondMin = results.get(1);

            computeRanges.add(currentRange+" "+
                    min.getName()+" "+
                    (secondMin.getTiming()-min.getTiming())+" "+
                    (((double) secondMin.getTiming() - (double) min.getTiming())  / (double) secondMin.getTiming()) * 100.0f+" "+
                    cast.getTiming()+" "+
                    modulus.getTiming()+" "+
                    castNoCharSubtract.getTiming()+" "+
                    castNoCast.getTiming());
            currentRange *= 10;
        }
        System.out.println("RANGE FASTER %CHANGE DELTA-TOP2 CAST MODULUS CASTNOCHAR CASTNOCAST");
        for (String computeRange : computeRanges) {
            System.out.println(computeRange);
        }
    }
}
