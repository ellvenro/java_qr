package com.company;
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

    public static void coding (String str) {
        int lenMas = (int)Math.ceil((float)str.length() / 3);
        int lenStr = str.length();
        char[][] code = new char[lenMas][3];

        for (int i = 0, j = 0; i < lenStr; i+=3, j++) {
            str.getChars(i, Math.min(i + 3, lenStr), code[j], 0);
        }
        for (int i = 0; i < lenMas; i++)
            System.out.println(code[i]);
    }
}