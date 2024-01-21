package com.company;

import java.util.Arrays;

public class Matrix {
    /**
     * @param firstMatrix pirma matrica, kuria norime dauginti
     * @param secondMatrix antra matrica, kuria norime dauginti
     * @return funkcija grąžina sudaugintų matricų rezultatą
     */
    int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        int[][] result = new int[firstMatrix.length][secondMatrix[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                result[row][col] = multiplyMatricesCell(firstMatrix, secondMatrix, row, col);
            }
        }

        return result;
    }

    /**
     * @param word vektorius, kurį norime paversti į matricą
     * @return funkcija grąžina vektorių paverstą į matricą (naudojamas dvimatis masyvas, kad vėliau galėtume taikyti veiksmus, tokius, kaip matricų daugybą)
     */
    int[][] getMatrixFromWord (String word){
        int [][] matrix = new int[1][word.length()];

        for(int i=0;i<word.length();i++){
            matrix[0][i] = Integer.parseInt(String.valueOf(word.charAt(i)));
        }

        return matrix;
    }

    /**
     * @param matrix matrica, kurią norime išspausdinti į konsolę
     * funkcija eina per kiekvieną matricos elementą ir jį išveda į konsolę
     */
    void printMatrix (int [][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * @param matrix matrica, kurios elementus norime pakeisti liekana iš 2
     * @return funkcija grąžina matricą, kurios elementai yra liekana iš 2
     */
    int [][] getMatrixElementsMod2(int [][] matrix){
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                matrix[row][col] = matrix[row][col] % 2;
            }
        }

        return matrix;
    }


    /**
     * @param matrix matrica, kurios elementus lygius 0 norime pakeisti į -1
     * @return funkcija grąžina matricą, kurios elementai lygūs 0 pakeisti į -1
     */
    int [][] replace0(int [][] matrix){
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if(matrix[row][col] == 0){
                    matrix[row][col] = -1;
                }
            }
        }

        return matrix;
    }

    /**
     * @param array trimatis masyvas, kurio didžiausio teigiamo arba neigiamo skaičiaus ieškome
     * @param size skaičius kiek dvimačių masyvų sudaro trimatį (m)
     * @return suranda didžiausią teigiamą arba neigiamą skaičiių ir grąžina jį bei jo poziciją masyve
     */
    int [] findBiggestWithPosition(int [][][] array, int size){
        int biggest = Math.abs(array[0][0][0]);
        int position = 0;
        int actualNumber = array[0][0][0];

        for(int i = 0;i<size;i++){
            for(int j = 0; j<(int) Math.pow(2, size); j++){
                int number = Math.abs(array[i][0][j]);
                if(number > biggest){
                    biggest = number;
                    position = j;
                    actualNumber = array[i][0][j];
                }
            }
        }

        return new int []{actualNumber, position};
    }

    /**
     * @param vector vektorius, kurį norime paversti į String formatą
     * @return funkcija grąžina vektorių paverstą į String formatą
     */
    String vectorMatrixToString(int [][] vector){
        String string = "";

        for(int i = 0;i<vector[0].length;i++){
            string+=vector[0][i];
        }

        return string;
    }

    /**
     * @param m Rydo Miulerio kodo m parametras
     * @return funkcija grąžina sugeneruotas h matricas
     */
    int[][][] getAllH(int m){
        int size = (int) Math.pow(2, m);
        int [][] hMatrix = {{1, 1}, {1, -1}};
        int [][][] h = new int [m][size][size];

//        Pagal dekodavimo algoritmo formulę gauname h matricas (I(2^(m-1)) X H X I(2^(i-1)))
        for(int i = 0; i<m; i++){
            int firstISize = (int) Math.pow(2, m-i-1);
            int secondISize = (int) Math.pow(2, i);

            int [][] firstIMatrix = this.getIMatrix(firstISize);
            int [][] secondIMatrix = this.getIMatrix(secondISize);

            h[i] = this.multiplyByEachElement(this.multiplyByEachElement(firstIMatrix, hMatrix), secondIMatrix);
        }

        return h;
    }

    /**
     * @param m Rydo Miulerio kodo m parametras
     * @return funkcija grąžina generuojančią matricą
     */
    int[][] getGMatrix (int m){
        int colSize = (int) Math.pow(2, m);
        int rowSize = m+1;

        int [][] matrix = new int[rowSize][colSize];

//        Pirmą matricos eilutę užpildome vienetais, o kitas 2^(i-1) kartų 0, po to tiek kartų 1, kol užpildome eilutę.
//        T.y. antra eilutė užpildoma 0101...01, trečia 00110011...0011
        for(int i = 0; i<rowSize; i++){
            if( i == 0){
                Arrays.fill(matrix[i], 0, colSize, 1);
            }
            else {
                int howManyEach = (int) Math.pow(2, i-1);
                int idx = 0;
                while(idx<colSize){
                    for(int j = 0; j<howManyEach;j++){
                        matrix[i][idx] = 0;
                        idx++;
                    }

                    for(int k = 0; k<howManyEach;k++){
                        matrix[i][idx] = 1;
                        idx++;
                    }
                }
            }
        }

        return matrix;
    }

    /**
     * @param firstMatrix pirma matrica, kurios elementus dauginame
     * @param secondMatrix antra matrica, kurios elementus dauginame
     * @param row eilutė, kurios stulpelių elementus dauginame
     * @param col stulpelis, kurio eilučių elementus dauginame
     * @return funkcija sudaugina pirmos matricos stulpelio elementus iš antros matricos eilučių elementų, rezultatus sudeda ir grąžina
     */
    private int multiplyMatricesCell(int[][] firstMatrix, int[][] secondMatrix, int row, int col) {
        int cell = 0;
        for (int i = 0; i < secondMatrix.length; i++) {
            cell += firstMatrix[row][i] * secondMatrix[i][col];
        }

        return cell;
    }

    /**
     * @param size vienetinės matricos dydis
     * @return grąžina vienetinę matricą pagal duotą dydį
     */
    private int [][] getIMatrix (int size){
        int [][] iMatrix = new int [size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == col){
                    iMatrix[row][col] = 1;
                }
                else{
                    iMatrix[row][col] = 0;
                }
            }
        }

        return iMatrix;
    }

    /**
     * @param A matrica, kurios elementus dauginsim iš kitos matricos
     * @param B matrica, kurią dauginsime iš kitos matricos elementų
     * @return grąžina matricą, kur kiekvienas A matricos elementas yra padaugintas iš B matricos
     */
    private int [][] multiplyByEachElement(int A[][], int B[][])
    {
        int elementNumber = 0;
        int[][] C= new int[A.length * B.length][A[0].length * B[0].length];

        for (int i = 0; i < A.length; i++)
        {
            for (int k = 0; k < B.length; k++)
            {
                for (int j = 0; j < A[0].length; j++)
                {
                    for (int l = 0; l < B[0].length; l++)
                    {
                        int newRow = elementNumber / (A[0].length * B[0].length);
                        int newCol = elementNumber % (A[0].length * B[0].length);

                        C[newRow][newCol] = A[i][j] * B[k][l];
                        elementNumber++;

                    }
                }
            }
        }

        return C;
    }
}
