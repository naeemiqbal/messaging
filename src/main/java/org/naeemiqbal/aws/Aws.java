/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package org.naeemiqbal.aws;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author niqbal
 */
public class Aws {

    public static void main(String[] args) {
        Producer p = new Producer();
        String ts ="NMI" +  Long.toString(new Date().getTime());
                System.out.println(ts);
        p.createQ(ts);
        System.out.println(p.list());
        p.send();
        System.out.println("Sent");
        System.out.println(p.receive());
        System.out.println("Hello World!");
        p.deleteQ(ts);
    }

}
