package p2pbay.testing;

import p2pbay.P2PBay;
import p2pbay.client.user.SignUp;

public class UserCreation extends Thread {

    private int first;
    private int numberOfUsers;
    private CountThread count;
    private IDCount idCount;

    public UserCreation(int first, int numberOfUsers, CountThread count, IDCount idCount) {
        this.first = first;
        this.numberOfUsers = numberOfUsers;
        this.count = count;
        this.idCount = idCount;
    }

    @Override
    public void run() {
        new SignUp(P2PBay.client).signUpMultipleUsers(first*numberOfUsers, numberOfUsers, count,idCount);
    }
}

