package org.example.w1;

public class StatisticsResult {
    private int sum;
    private int min;
    private int max;
    private double average;

    public StatisticsResult(int sum, int min, int max, double average) {
        this.sum = sum;
        this.min = min;
        this.max = max;
        this.average = average;
    }
}