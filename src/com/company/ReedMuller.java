package com.company;

public class ReedMuller {
    private int m;
    private Matrix matrixActions;
    private int [][] gMatrix;
    private int [][][] allH;

    /**
     * @param m Rydo Miulerio kodo m parametras
     * @param printMatrix ar norime išspausdinti sugeneruotas matricas
     * Konstruktorius sugeneruoja g ir visas h matricas, reikalingas kodavimui (g) ir dekodavimui (visos h)
     */
    public ReedMuller(int m, boolean printMatrix) {
        this.matrixActions= new Matrix();
        this.m = m;
        this.gMatrix = this.matrixActions.getGMatrix(m);
        this.allH = this.matrixActions.getAllH(m);

        if(printMatrix) {
            System.out.println("Gauta G matrica:");
            matrixActions.printMatrix(this.gMatrix);

            for (int i = 0; i < m; i++) {
                String message = "H" + (i + 1) + " matrica:";
                System.out.println(message);
                matrixActions.printMatrix(this.allH[i]);
            }
        }
    }

    /**
     * @param vector vektorius String formatu, kurį norime koduoti
     * @return funkcija paverčia vektorių į matricą, sudaugina vektorių su generuojančią matrica,
     * kiekvienam elementui pritaiko modulį iš 2 ir grąžina matricą
     */
    int [][] encode(String vector) {
        int [][] wordMatrix = this.matrixActions.getMatrixFromWord(vector);

        int [][] encodedMessage = this.matrixActions.multiplyMatrices(wordMatrix, gMatrix);
        int [][] encodedMessageMod2 = this.matrixActions.getMatrixElementsMod2(encodedMessage);

        return encodedMessageMod2;
    }

    /**
     * @param encodedMatrix užkoduotas vektorius matricos pavidalu
     * @param printMatrix ar spausdinti W matricas
     * @returnfunkcija grąžina dekoduotą vektorių
     */
    int [][] decode(int [][] encodedMatrix, boolean printMatrix){
//        Pagal dekodavimo algoritmą pakeičiam nulius į -1
        int [][] w = this.matrixActions.replace0(encodedMatrix);
        int [][][] results = new int [this.m][][];

//        Pagal dekodavimo algoritmą w dauginam iš h1, taip gaunam w2. w2 dauginam iš h2 ir t.t.
        for(int i = 0; i<this.m;i++){
            if(i==0){
                results[i] = this.matrixActions.multiplyMatrices(w, this.allH[i]);
            }else {
                results[i] = this.matrixActions.multiplyMatrices(results[i-1], this.allH[i]);
            }

            if(printMatrix) {
                System.out.println("W" + (i + 1) + ":");
                this.matrixActions.printMatrix(results[i]);
            }
        }

//        Ieškom didžiausio teigiamo arba neigiamo skaičiaus tarp w1, w1... vektorių ir jo pozicijos, jei skaičius neigiamas pridėsime 0 prie dekoduotos žinutės,
//         jei teigimas, pridėsim 1. Dekoduotas vektorius bus apvesta dvejetaine pozicija ir priekyje prirašytas 0 arba 1
        int numberAndPosition[] = this.matrixActions.findBiggestWithPosition(results, this.m);
        boolean isPositive = numberAndPosition[0] >= 0;
        String decodedMessage = isPositive ? "1" : "0";
        String binaryNumber = Integer.toBinaryString(numberAndPosition[1]);
        String reversedBinaryNumber = "";


//        Apverčiam dvejetainį skaičių
        for (int i=0; i<binaryNumber.length(); i++)
        {
            char ch = binaryNumber.charAt(i);
            reversedBinaryNumber= ch + reversedBinaryNumber;
        }

        decodedMessage += reversedBinaryNumber;

//       Pridedam 0, kol gausim reikiamo ilgio vektorių
        while(decodedMessage.length() < this.m +1){
            decodedMessage += "0";
        }

        return this.matrixActions.getMatrixFromWord(decodedMessage);
    }
}
