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
        addServiceInformation(str);
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

    //Первая версия qr, побайтовое кодирование
    public static void addServiceInformation (String str) {
        String inf;
        inf = String.join("", "0100", String.format("%8s", Integer.toBinaryString(str.length())).replace(' ', '0'));
        System.out.println(inf);
    }

    //Определение максимального количества информации и уровня коррекции
    public static int determLevel (String str) {
        int len = str.length();
        if (len <= 72) return 72;        // H
        else if (len <= 104) return 104; // Q
        else if (len <= 128) return 128; // M
        else if (len <= 152) return 152; // L
        else return -1;                  //Не подходит по условиям, ошибка ???
    }
}