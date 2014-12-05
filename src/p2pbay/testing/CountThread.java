package p2pbay.testing;

public class CountThread extends Thread{
    long min = Long.MAX_VALUE;
    long max = 0;
    long mean = 0;

    long times = 0;

    public CountThread() {
    }

    public void add(long min, long max, long mean) {
        synchronized (this) {
            if (this.min > min)
                this.min = min;
            if(this.max < max)
                this.max = max;
            this.mean = (this.mean*times + mean)/(times+1);
            times++;
        }
        System.out.println();
    }

    public long getMax() {
        return max;
    }

    public long getMin() {
        return min;
    }

    public long getMean() {
        return mean;
    }
}
