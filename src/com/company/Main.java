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

        //Добавление байтов коррекции
        bin = String.join("", bin, correct(bin));

        System.out.println(bin);

        //Расположение кода в матрице
        matrixLocation(bin);
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
    public static String correct (String bin){
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
            binCor = String.join("", binCor, String.format("%8s", Integer.toBinaryString(elem)).replace(' ', '0'));
          }

        return binCor;
    }

    //Расположение закодированной строки в матрице qr-кода
    public static void matrixLocation (String bin) {
        int n = 21;
        byte[][] matrix = new byte[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                //Добавление поисковых узоров
                if (i <= 7 && j <= 7) {
                    if ((i == 0 || i == 6 ) && j != 7 ||
                            (j == 0 || j == 6 ) && i != 7 ||
                            (i >= 2 && i <= 4 && j >= 2 && j <= 4))
                        matrix[i][j] = 1;
                    else
                        matrix[i][j] = 0;
                }
                else
                    matrix[i][j] = -1;
            }

        //Добавление поисковых узоров
        for (int i = 0; i <= 7; i++) {
            System.arraycopy(matrix[i], 0, matrix[n - i - 1], 0, 8);
            for (int j = 0; j <= 7; j++)
                matrix[i][n-j-1] = matrix[i][j];
        }

        //Добавление полос синхронизации
        for (int j = 8; j < n-8; j+=2) {
            matrix[6][j] = matrix[j][6] = 1;
            matrix[6][j+1] = matrix[j+1][6] = 0;
        }

        matrix[n-8][8] = 1;

        //Определение кода для маски
        String mask = switch (Algorithm.corLev) {
            case 'H':
                yield "001011010001001";
            case 'Q':
                yield "011010101011111";
            case 'M':
                yield "101010000010010";
            case 'L':
                yield "111011111000100";
            default:
                yield "";
        };

        //Запись маски и уровная коррекции
        int j1 = 0, j2 = 8, j3 = 0, j4 = 14;
        for (int i = 0; i <= 8; i++)
        {
            if (i != 6 ) {
                matrix[8][i] = (mask.charAt(j1) == '1') ? (byte) 1 : 0;
                j1++;
            }
            if (i != 7 && j3 <= 6) {
                matrix[n-i-1][8] = (mask.charAt(j3) == '1') ? (byte) 1 : 0;
                j3++;
            }
            if (8-i-1 != 6 && j2 < 15) {
                matrix[8 - i - 1][8] = (mask.charAt(j2) == '1') ? (byte) 1 : 0;
                j2++;
            }
            if (j4 > 6)
            matrix[8][n-i-1] = (mask.charAt(j4) == '1') ? (byte) 1 : 0;
            j4--;
        }

        System.out.println(bin);
        //Добавление данных
        int i = n-1, j= n-1;
        int ibin = 0;
        while(i > 0 && j > 0 && ibin < bin.length()) {
            while (i >= 0) {
                if (matrix[i][j] != -1) {
                    i--;
                    continue;
                }
                matrix[i][j] = (bin.charAt(ibin) == '1') ? (byte) 1 : 0;
                j--;
                ibin++;
                matrix[i][j] = (bin.charAt(ibin) == '1') ? (byte) 1 : 0;
                j++;
                i--;
                ibin++;
            }
            i++;
            j -= 2;
            if (j == 6)
                j--;
            while (i < n) {
                if(matrix[i][j] != -1)
                {
                    i++;
                    continue;
                }
                matrix[i][j] = (bin.charAt(ibin) == '1') ? (byte) 1 : 0;
                j--;
                ibin++;
                matrix[i][j] = (bin.charAt(ibin) == '1') ? (byte) 1 : 0;
                j++;
                i++;
                ibin++;
            }
            i--;
            j -= 2;
        }

        //Наложение маски
        for (i = 0; i < n; i++)
            for(j = 0; j < n; j++)
            {
                if ((i > 8 || j > 8) && (i > 8 || j < n-8) && (i < n-8 || j > 8) && i != 6 && j != 6)
                    matrix[i][j] = (byte) (matrix[i][j] ^ (((i+j)%2 == 1) ? (byte)1 : 0));
            }

        System.out.println();
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                System.out.print((matrix[i][j] != -1) ? matrix[i][j] : " ");
                System.out.print(" ");
            }
            System.out.println();
        }

    }

}