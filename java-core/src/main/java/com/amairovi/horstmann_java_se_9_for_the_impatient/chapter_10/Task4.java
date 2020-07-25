package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_10;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import static java.util.Arrays.parallelPrefix;
import static java.util.Arrays.parallelSetAll;

public class Task4 {

    @NoArgsConstructor
    @AllArgsConstructor
    public static class FMatrix {

        private long[][] m = new long[][]{
                {1, 1},
                {1, 0}
        };

        public FMatrix mult(FMatrix o) {
            long[][] result = new long[2][2];

            result[0][0] = m[0][0] * o.m[0][0] + m[0][1] * o.m[1][0];
            result[0][1] = m[0][0] * o.m[0][1] + m[0][1] * o.m[1][1];
            result[1][0] = m[1][0] * o.m[0][0] + m[1][1] * o.m[1][0];
            result[1][1] = m[1][0] * o.m[1][0] + m[1][1] * o.m[1][1];

            return new FMatrix(result);
        }

        public static FMatrix multiply(FMatrix m1, FMatrix m2) {
            return m1.mult(m2);
        }

        public long getF() {
            return m[0][0];
        }

    }

    public static void main(String[] args) {
        FMatrix[] fMatrices = new FMatrix[50];
        parallelSetAll(fMatrices, i -> new FMatrix());
        parallelPrefix(fMatrices, FMatrix::multiply);

        for (int i = 0; i < fMatrices.length; i++) {
            System.out.println("#" + (i + 2) + " : " + fMatrices[i].getF());
        }
    }

}
