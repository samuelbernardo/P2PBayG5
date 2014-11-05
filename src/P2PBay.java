import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.Random;
import java.util.Scanner;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
public class P2PBay {

    final private Peer peer;
    static Scanner in = new Scanner(System.in);
    static String option;
    static String id;
    static String password;

    public P2PBay(String ip) throws Exception {
        /* Creation of a peer. */
        peer = new PeerMaker(Number160.createHash(Inet4Address.getLocalHost().getHostAddress())).setPorts(4000).makeAndListen();

        /* Connects the new peer to an existing peer. */
        InetAddress address = Inet4Address.getByName(ip);
        FutureDiscover futureDiscover = peer.discover().setInetAddress( address ).setPorts(4000).start();
        futureDiscover.awaitUninterruptibly();

        FutureBootstrap fb = peer.bootstrap().setInetAddress(address).setPorts(4000).start();

        fb.awaitUninterruptibly();
        if (fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public static void main(String[] args) throws NumberFormatException, Exception {
        System.out.println("Insira o IP de um peer:");
        String ip = in.nextLine();
        P2PBay p2pbay = new P2PBay(ip);
        showMenu();
        while (true) {
            switch (option) {
                case "1":
                    String storedPassword = p2pbay.get(id);
                    if(storedPassword.equals(password)){
                        System.out.println("\nSucesso! É aqui que chamamos o menu da aplicação.");
                    }
                    else
                        System.out.println("\nO login falhou! A sair do menu de login...");
                    break;

                case "2":
                    p2pbay.store(id, password);
                    System.out.println("\nA conta foi criada com sucesso!");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
            showMenu();
        }
    }

    private static void showMenu() {
        System.out.println("\n-----P2PBay-----\n\n1 - Login\n2 - Criar uma conta\n\n'exit' para sair\n");
        option = in.nextLine();
        if (option.equals("exit")) {
            in.close();
            System.exit(0);
        }
        System.out.println("Introduza o id:");
        id = in.nextLine();
        System.out.println("Introduza a password:");
        password = in.nextLine();
    }

    private String get(String name) throws ClassNotFoundException, IOException {
        FutureDHT futureDHT = peer.get(Number160.createHash(name)).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
            return futureDHT.getData().getObject().toString();
        }
        return "Not found! :(";
    }

    private void store(String name, String ip) throws IOException {
        peer.put(Number160.createHash(name)).setData(new Data(ip)).start().awaitUninterruptibly();
    }
}
