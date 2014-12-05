package p2pbay.testing;

import p2pbay.P2PBay;
import p2pbay.client.user.SignUp;
import p2pbay.core.DHTObjectType;
import p2pbay.core.User;
import p2pbay.server.TomP2PHandler;

import java.util.Random;

public class UserAccess extends Thread {
    private final int numberOfUsers;
    private final TomP2PHandler handler;
    private int numberOfReads;
    private CountThread count;

    public UserAccess(int numberOfReads, int numberOfUsers, CountThread count, TomP2PHandler tomP2PHandler) {
        this.numberOfReads = numberOfReads;
        this.count = count;
        this.numberOfUsers = numberOfReads;
        this.handler = tomP2PHandler;
    }

    @Override
    public void run() {
        long[] time = new long[numberOfReads];

        long max = 0;
        long min = Long.MAX_VALUE;
        long mean = 0;
        Random r = new Random();
        for (int i = 0; i < numberOfReads; i++) {
            String user = String.valueOf(r.nextInt(numberOfUsers));
            long start = System.currentTimeMillis();
            Object o = handler.get(user, DHTObjectType.USER);
            time[i] = System.currentTimeMillis() - start;
            if (o == null)
                System.out.println("ISSO É ESTUPIDO");
            if (max < time[i])
                max = time[i];
            if (min > time[i])
                min = time[i];
            mean += time[i];
        }
        mean = mean/numberOfUsers;
        count.add(min, max, mean);
        System.out.println("ACCESS" + "min:" + count.getMin() + " max:" + count.getMax() + " mean:" + count.getMean());
    }
}

