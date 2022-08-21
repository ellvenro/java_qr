package com.company;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        /*
        Scanner in = new Scanner(System.in);
        System.out.print("Input: ");
        String str = in.nextLine();
        System.out.printf("Output: %s\n", str);
        in.close();
        coding(str);
        */
        int[] splitBufInt = {64, 196, 132, 84, 196, 196, 242, 194, 4, 132, 20, 37, 34, 16, 236, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int cntCor = 28;
        int[] corMas = {168, 223, 200, 104, 224, 234, 108, 180, 110, 190, 195, 147, 205, 27, 232, 201, 21, 43, 245, 87, 42, 195, 212, 119, 242, 37, 9, 123};
        Algorithm.algorithm(splitBufInt, cntCor, corMas, 16);
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

        //Добавление байтов коррекции
        bin = String.join("", bin, correct(bin));
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
        if (len <= 72) { Algorithm.corLev = 'H'; return 72; }        // H
        else if (len <= 104) { Algorithm.corLev = 'Q'; return 104; } // Q
        else if (len <= 128) { Algorithm.corLev = 'M'; return 128; } // M
        else if (len <= 152) { Algorithm.corLev = 'L'; return 152; } // L
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
        if (sub % 8 == 0 && sub != 0)
            bin = String.join("", bin, "11101100");
        return bin;
    }

    //Создание байтов коррекции
    public static String correct(String bin){
        //Колличество байтов коррекции
        int cntCor;
        int[] corMas = null;
        cntCor = switch (Algorithm.corLev)
        {
            case 'H':
                corMas = Algorithm.genMasH.clone();
                yield Algorithm.genMasH.length;
            case 'Q':
                corMas = Algorithm.genMasQ.clone();
                yield Algorithm.genMasQ.length;
            case 'M':
                corMas = Algorithm.genMasM.clone();
                yield Algorithm.genMasM.length;
            case 'L':
                corMas = Algorithm.genMasL.clone();
                yield Algorithm.genMasL.length;
            default:
                yield -1;
        };

        //Создание десятичного массива из бинарной строки
        StringBuilder binBuf = new StringBuilder(bin);
        for (int i = 8; i < bin.length(); i+=9)
            binBuf.insert(i, " "); //Добавление пробела
        String buf = binBuf + ""; //Изменение типа
        String[] splitBuf = buf.split(" "); //Разделение по пробелу
        int[] splitBufInt = new int[cntCor]; //Создание десятичного массива
        int cnt = splitBuf.length;
        for (int i = 0; i < cnt; i++)
            splitBufInt[i] = Integer.parseInt(splitBuf[i], 2); //Перевод из двоичной в десятичную
        for (int i = cnt; i < cntCor; i++)
            splitBufInt[i] = 0;

        //Применение алгоритма рида-Соломона
        int[] splitDecCor = Algorithm.algorithm(splitBufInt, cntCor, corMas, cnt);

        //Преобразование в строку бит
        String binCor = "";
        for (int elem : splitDecCor) {
            binCor = String.join("", bin, String.format("%8s", Integer.toBinaryString(elem)).replace(' ', '0'));
        }

        return binCor;
    }


}