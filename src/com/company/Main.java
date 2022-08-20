package com.company;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Input: ");
        String str = in.nextLine();
        System.out.printf("Output: %s\n", str);
        in.close();
        coding(str);
    }

    //Кодировка исходной строки в UTF-8
    public static void coding (String str) {
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        String[] bin = new String[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            bin[i] = String.format("%8s", Integer.toBinaryString(bytes[i])).replace(' ', '0');
            System.out.println(bin[i]);
        }
    }
}