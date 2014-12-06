package p2pbay.client.user;

import net.tomp2p.peers.Number160;
import p2pbay.P2PBay;
import p2pbay.client.Client;
import p2pbay.client.SysStrings;
import p2pbay.core.DHTObjectType;
import p2pbay.core.User;
import p2pbay.testing.CountThread;
import p2pbay.testing.IDCount;

import java.util.List;
import java.util.Map;

/**
 * Comando para registar um utilizador
 */
public class SignUp extends UserInteraction implements Runnable {
    private User user = null;
    private String username;
    private String password;

    public SignUp(Client client) {
        super(client);
    }

    @Override
    public void getInfo() {
        System.out.println(SysStrings.SIGNUP);

        username = getUsername();
        password = getPassword();

        //Check if user exists
        user = getClient().getUser(username);
    }

    @Override
    public void doOperation() {
        if(user == null) {
            user = new User(username, password);
            getClient().store(user);
            System.out.println(SysStrings.USER_REGISTERED);
        }
        else
            System.out.println(SysStrings.USER_NOT_REGISTERED);
    }

    public void signUpMultipleUsers(int first, int numberOfUsers, CountThread count, IDCount idCount) {
        long[] time = new long[numberOfUsers];

        long max = 0;
        long min = Long.MAX_VALUE;
        long mean = 0;

        for (int i = 0; i < numberOfUsers; i++) {
            String user = String.valueOf(first + i);
            long start = System.currentTimeMillis();
            P2PBay.P2PBAY.store(new User(user, user));
            time[i] = System.currentTimeMillis() - start;
            if (max < time[i])
                max = time[i];
            if (min > time[i])
                min = time[i];
            mean += time[i];
        }
        mean = mean/numberOfUsers;
        count.add(min, max, mean);
        System.out.println("min:" + count.getMin() + " max:" + count.getMax() + " mean:" + count.getMean());
        for (int i = 0; i < numberOfUsers; i++) {
            Number160 id = P2PBay.P2PBAY.getPeerId(String.valueOf(i), DHTObjectType.USER);
            idCount.add(id);
        }
         max = 0;
         min = Long.MAX_VALUE;
         mean = 0;
        for (Integer integer : idCount.getHashMap().values()) {
            if (max < integer)
                max = integer;
            if (min > integer)
                min = integer;
            mean += integer;
        }
        mean = mean / idCount.getHashMap().size();
        System.out.println("Nr de users " + "min:" + min + " max:" + max + " mean:" + mean);

    }
}
