package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        Inicijuojam klases
        Scanner in = new Scanner(System.in);
        Channel ch = new Channel();
        Matrix mx = new Matrix();
        Validator val = new Validator();

//        Gauname reikalingus parametrus
        System.out.println("Iveskite m reiksme");
        int m = in.nextInt();
        val.isValidM(m);

        System.out.println("Iveskite p reiksme (nuo 0 iki 1)");
        float p = in.nextFloat();
        val.isValidProbability(p);

        System.out.println("Pasirinkite scenariju");
        System.out.println("1. Koduoti informacijos vektoriu");
        System.out.println("2. Koduoti teksta");
        int option = in.nextInt();

//        Inicijuojame klases su parametrais
        ReedMuller rm = new ReedMuller(m, true);
        Text tx = new Text(m);

//        Vykdom scenarijų
        switch (option) {
            case 1:
                System.out.println("Iveskite zinute (m+1 ilgio)");
                String initialMessage = in.next();
                val.isValidVector(initialMessage, m+1);

                int[][] encoded = rm.encode(initialMessage);
                System.out.println("Uzkoduota informacija:");
                mx.printMatrix(encoded);

                System.out.println("Issiuntus kanalu gauname:");
                int[][] afterChannel = ch.sendThroughChannel(encoded, p, true);
                mx.printMatrix(afterChannel);

                System.out.println("Ar norite keisti uzkoduota ir per kanala issiusta vektoriu? (1/0)");
                int changeVector = in.nextInt();

                if(changeVector == 1){
                    System.out.println("Iveskite nauja vektoriu");
                    String newVector = in.next();
                    val.isValidVector(newVector, (int) Math.pow(2, m));
                    afterChannel = mx.getMatrixFromWord(newVector);
                }

                int[][] decoded = rm.decode(afterChannel, true);
                System.out.println("Dekoduota informacija:");
                mx.printMatrix(decoded);
                break;

            case 2:
                Scanner sc = new Scanner(System.in);
                System.out.println("Iveskite teksta (baige 2 kart spauskite enter)");
                String text = "";

//                Gauname kelių eilučių vartotojo įvestį
                while (sc.hasNextLine()) {
                    String input = sc.nextLine();
                    if (input.isEmpty()) {
                        break;
                    }

                    text += text != "" ? "\n\r": "";
                    text += input;
                }
                sc.close();

                System.out.println("Persiunciant kanalu nekodavus gauname:");
                System.out.println(tx.sendThroughChannel(text, p, m+1, false));
                System.out.println("Persiunciant kanalu kodavus gauname:");
                System.out.println(tx.sendThroughChannel(text, p, m+1, true));
        }

        in.close();
    }
}
