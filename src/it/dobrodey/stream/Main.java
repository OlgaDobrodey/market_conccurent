package it.dobrodey.stream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.ImageIcon;

public class Main extends Thread {

    public static void main(String[] args) throws InterruptedException {
        String str = Math.random() > 0.5 ? "I am fill lucky" : null;
        Stream.ofNullable(str).forEach(System.out::println);
        Main m = new Main();
        m.start();
        sleep(75);
        m.interrupt();
        System.out.println("FINISH MAIN");
        ArrayList<Integer> integers = new ArrayList<Integer>();
        final LinkedList<Integer> integers1 = new LinkedList<>();
        final LinkedList<ImageIcon> imageIcons = new LinkedList<>();




    }

    @Override
    public void run() {
        System.out.println("start " + getName());
//        while (true) {
////            if (interrupted()) {
////                System.out.println("                              hello exception");
////                System.out.println("                              interrupted");
////                try {
////                    sleep(1000);
////                } catch (InterruptedException e) {
////
////                throw new RuntimeException(e);
////                }
//            }
////            System.out.println("finish " + getName());
        for (int i = 0; i < 1000; i++) {
//            try {
//                sleep(10);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            System.out.println(i +"-write "+ Math.random()*1000*i);
        }
        System.out.println("finish " + getName());
        }

}

