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

    //Создание кода
    public static void coding (String str) {
        //Кодировка исходной строки в UTF-8
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        String bin = "";

        for (byte aByte : bytes) {
            bin = String.join("", bin, String.format("%8s", Integer.toBinaryString(aByte)).replace(' ', '0'));
        }

        //Дополнение строки нулями для кратности восьми и сервисной информацией
        bin = String.join("", addServiceInformation(str), bin, "0000");

        //Добавление чередующихся байтов
        bin = Filling(str, bin);

        System.out.print(bin);
    }

    //Первая версия qr, побайтовое кодирование
    public static String addServiceInformation (String str) {
        String inf;
        inf = String.join("", "0100", String.format("%8s", Integer.toBinaryString(str.length())).replace(' ', '0'));
        return inf;
    }

    //Определение максимального количества информации и уровня коррекции
    public static int determLevel (String str) {
        int len = str.length() * 8 + 4 + 8;
        if (len <= 72) return 72;        // H
        else if (len <= 104) return 104; // Q
        else if (len <= 128) return 128; // M
        else if (len <= 152) return 152; // L
        else return -1;                  //Не подходит по условиям, ошибка
    }

    //Заполнение информационного блока пустыми байтами
    public static String Filling (String str, String bin) {
        int cntInf = determLevel(str);
        int sub = cntInf - bin.length();
        while (sub % 16 == 0 && sub != 0)
        {
            bin = String.join("", bin, "11101100", "00010001");
            sub -= 16;
        }
        if (sub % 8 == 0)
            bin = String.join("", bin, "11101100");
        return bin;
    }
}