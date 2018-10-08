package CS_3700;

public class Main {

    public static void main(String[] args) {
        int size = 8;
        float[][] A = new float[size][size];
        float[][] B = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = (float) Math.random();
                B[i][j] = (float) Math.random();
            }
        }
        float[][] C = new float[A.length][B[0].length];
        long startTime, elapsedNanos;

        System.out.println("\n8x8 MATRICES\n------------");
        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 1);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 1 thread:  " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 2);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 2 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 4);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 4 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 8);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with size threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");


        size = 64;
        A = new float[size][size];
        B = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = (float) Math.random();
                B[i][j] = (float) Math.random();
            }
        }

        C = new float[A.length][B[0].length];

        System.out.println("\n64x64 MATRICES\n------------");
        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 1);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 1 thread:  " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 2);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 2 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 4);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 4 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 8);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 8 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        size = 256;
        A = new float[size][size];
        B = new float[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                A[i][j] = (float) Math.random();
                B[i][j] = (float) Math.random();
            }
        }

        C = new float[A.length][B[0].length];

        System.out.println("\n256x256 MATRICES\n------------");
        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 1);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 1 thread:  " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 2);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 2 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 4);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 4 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

        startTime = System.nanoTime();
        matmult(A, B, C, size, size, size, 8);
        elapsedNanos = System.nanoTime() - startTime;
        System.out.println("Time with 8 threads: " + ((double) elapsedNanos ) / Math.pow(10, 9) + " seconds");

    }

    public static void matmult(float[][] A, float[][] B, float[][] C, int m, int n, int p, int numThreads) {
        Partial_Matrix_Mult[] array = new Partial_Matrix_Mult[numThreads];
        for (int i = 0; i < numThreads; i++) {
            array[i] = new Partial_Matrix_Mult(A, B, C, m, n, p, i * m / numThreads, m / numThreads);
        }
        for (int i = 0; i < numThreads; i++) {
            array[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            try {
                array[i].join();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}

class Partial_Matrix_Mult extends Thread {
    float[][] A, B, C;
    int m, n, p, first_row, num_rows;

    Partial_Matrix_Mult(float[][] A, float[][] B, float[][] C, int m, int n, int p, int first_row, int num_rows) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.m = m;
        this.n = n;
        this.p = p;
        this.first_row = first_row;
        this.num_rows = num_rows;
    }

    @Override
    public void run() {
        for (int i = first_row; i < first_row + num_rows; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
    }
}
