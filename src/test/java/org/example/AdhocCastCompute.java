package org.example;

class AdhocCastCompute {

    private static long castCompute(long range) {
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

    public static void main(String[] args) {
        System.out.println(castCompute(100));
    }
}
