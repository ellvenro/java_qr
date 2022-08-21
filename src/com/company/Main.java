package com.company;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    //Генерирующие многочлены
    public static int[] genMasL = {87, 229, 146, 149, 238, 102, 21};
    public static int[] genMasM = {251, 67, 46, 61, 118, 70, 64, 94, 32, 45};
    public static int[] genMasQ = {74, 152, 176, 100, 86, 100, 106, 104, 130, 218, 206, 140, 78};
    public static int[] genMasH = {43, 139, 206, 78, 43, 239, 123, 206, 214, 147, 24, 99, 150, 39, 243, 163, 136};
    public static char corLev;

    //Поле Галуа
    public static int[] field = {
            1, 2, 4, 8, 16, 32, 64, 128, 29, 58, 116, 232, 205, 135, 19, 38,
            76, 152, 45, 90, 180, 117, 234, 201, 143, 3, 6, 12, 24, 48, 96, 192,
            157, 39, 78, 156, 37, 74, 148, 53, 106, 212, 181, 119, 238, 193, 159, 35,
            70, 140, 5, 10, 20, 40, 80, 160, 93, 186, 105, 210, 185, 111, 222, 161,
            95, 190, 97, 194, 153, 47, 94, 188, 101, 202, 137, 15, 30, 60, 120, 240,
            253, 231, 211, 187, 107, 214, 177, 127, 254, 225, 223, 163, 91, 182, 113, 226,
            217, 175, 67, 134, 17, 34, 68, 136, 13, 26, 52, 104, 208, 189, 103, 206,
            129, 31, 62, 124, 248, 237, 199, 147, 59, 118, 236, 197, 151, 51, 102, 204,
            133, 23, 46, 92, 184, 109, 218, 169, 79, 158, 33, 66, 132, 21, 42, 84,
            168, 77, 154, 41, 82, 164, 85, 170, 73, 146, 57, 114, 228, 213, 183, 115,
            230, 209, 191, 99, 198, 145, 63, 126, 252, 229, 215, 179, 123, 246, 241, 255,
            227, 219, 171, 75, 150, 49, 98, 196, 149, 55, 110, 220, 165, 87, 174, 65,
            130, 25, 50, 100, 200, 141, 7, 14, 28, 56, 112, 224, 221, 167, 83, 166,
            81, 162, 89, 178, 121, 242, 249, 239, 195, 155, 43, 86, 172, 69, 138, 9,
            18, 36, 72, 144, 61, 122, 244, 245, 247, 243, 251, 235, 203, 139, 11, 22,
            44, 88, 176, 125, 250, 233, 207, 131, 27, 54, 108, 216, 173, 71, 142, 1
    };
    //Обратное поле Галуа
    public static int[] reverseField = {
            -1, 0, 1, 25, 2, 50, 26, 198, 3, 223, 51, 238, 27, 104, 199, 75,
            4, 100, 224, 14, 52, 141, 239, 129, 28, 193, 105, 248, 200, 8, 76, 113,
            5, 138, 101, 47, 225, 36, 15, 33, 53, 147, 142, 218, 240, 18, 130, 69,
            29, 181, 194, 125, 106, 39, 249, 185, 201, 154, 9, 120, 77, 228, 114, 166,
            6, 191, 139, 98, 102, 221, 48, 253, 226, 152, 37, 179, 16, 145, 34, 136,
            54, 208, 148, 206, 143, 150, 219, 189, 241, 210, 19, 92, 131, 56, 70, 64,
            30, 66, 182, 163, 195, 72, 126, 110, 107, 58, 40, 84, 250, 133, 186, 61,
            202, 94, 155, 159, 10, 21, 121, 43, 78, 212, 229, 172, 115, 243, 167, 87,
            7, 112, 192, 247, 140, 128, 99, 13, 103, 74, 222, 237, 49, 197, 254, 24,
            227, 165, 153, 119, 38, 184, 180, 124, 17, 68, 146, 217, 35, 32, 137, 46,
            55, 63, 209, 91, 149, 188, 207, 205, 144, 135, 151, 178, 220, 252, 190, 97,
            242, 86, 211, 171, 20, 42, 93, 158, 132, 60, 57, 83, 71, 109, 65, 162,
            31, 45, 67, 216, 183, 123, 164, 118, 196, 23, 73, 236, 127, 12, 111, 246,
            108, 161, 59, 82, 41, 157, 85, 170, 251, 96, 134, 177, 187, 204, 62, 90,
            203, 89, 95, 176, 156, 169, 160, 81, 11, 245, 22, 235, 122, 117, 44, 215,
            79, 174, 213, 233, 230, 231, 173, 232, 116, 214, 244, 234, 168, 80, 88, 175 
    };

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
        algorithm(splitBufInt, cntCor, corMas, 16);
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

        System.out.println(bin);
        correct(bin);
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
        if (len <= 72) { corLev = 'H'; return 72; }        // H
        else if (len <= 104) { corLev = 'Q'; return 104; } // Q
        else if (len <= 128) { corLev = 'M'; return 128; } // M
        else if (len <= 152) { corLev = 'L'; return 152; } // L
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
    public static void correct(String bin){
        //Колличество байтов коррекции
        int cntCor = 0;
        int[] corMas = null;
        switch (corLev)
        {
            case 'H':
                cntCor = genMasH.length;
                corMas = new int[cntCor];
                for (int i = 0; i < cntCor; i++)
                    corMas[i] = genMasH[i];
            case 'Q':
                cntCor = genMasQ.length;
                corMas = new int[cntCor];
                for (int i = 0; i < cntCor; i++)
                    corMas[i] = genMasQ[i];
            case 'M':
                cntCor = genMasM.length;
                corMas = new int[cntCor];
                for (int i = 0; i < cntCor; i++)
                    corMas[i] = genMasM[i];
            case 'L':
                cntCor = genMasL.length;
                corMas = new int[cntCor];
                for (int i = 0; i < cntCor; i++)
                    corMas[i] = genMasL[i];
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

        algorithm(splitBufInt, cntCor, corMas, cnt);

    }

    //Применение алгоритма Рида-Соломона
    public static void algorithm(int[] splitBufInt, int cntCor, int[] corMas, int cnt){

        for (int i = 0; i < cnt; i++) {
            int A = splitBufInt[0];
            for (int j = 0; j < splitBufInt.length; j++) {
                if (j == splitBufInt.length - 1)
                    splitBufInt[j] = 0;
                else
                    splitBufInt[j] = splitBufInt[j + 1];
                System.out.printf("%d ", splitBufInt[j]);
            }
            System.out.println();
            if (A == 0)
                continue;
            int C = 0;
            for (int j = 0; j < cntCor; j++)
            {
                C = reverseField[A] + corMas[j];
                if (C > 254)
                    C = C % 255;
                splitBufInt[j] = field[C] ^ splitBufInt[j];
                System.out.printf("%d ", splitBufInt[j]);
            }
            System.out.println();
            System.out.println();
        }
    }
}