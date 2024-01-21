package com.company;

import java.util.ArrayList;
import java.util.List;

public class Text {
    private Matrix matrixActions;
    private Channel channel;
    private ReedMuller reedMuller;

    public Text(int p){
        this.matrixActions = new Matrix();
        this.channel = new Channel();
        this.reedMuller = new ReedMuller(p, false);
    }

    /**
     * @param string tekstas, kurį norime siųsti kanalu
     * @param p kanalo klaidos tikimybė
     * @param size norimas vektoriaus dydis
     * @param encode ar vektoriai koduojami
     * @return grąžina tekstą persiųstą kanalu
     */
    String sendThroughChannel(String string, float p, int size, boolean encode){
//        Paverčiam tekstą į dvejetainį ir išskaidom į vektorius ir, jei reikia, užkoduojame
        String channelResult = "";
        String binaryString = this.textToBinaryString(string);
        int extraInfo = binaryString.length() % size;
        List<int[][]> vectors = this.splitBinaryStringToVectors(binaryString, size, encode);
        List<int[][]> results = new ArrayList<>();
        List<int[][]> decodedResults = new ArrayList<>();

//        Jei yra pridėtų 0, pirmą vektorių siunčiame saugiu kanalu, jei nėra, kanalu su klaidos tikimybe p
        for(int i = 0; i<vectors.size(); i++){
            int [][] vectorSentThroughChannel = this.channel.sendThroughChannel(vectors.get(i), i == 0 && extraInfo > 0 ? 0: p, false);
            results.add(vectorSentThroughChannel);
        }

//        Jei reikia, atkoduojame vektorius gautus iš kanalo
        if(encode){
            for(int i = 0; i<results.size(); i++){
                int [][] decodedVector = this.reedMuller.decode(results.get(i), false);
                decodedResults.add(decodedVector);
            }
        }

//        Gautus atkoduotus, arba be kodavimo persiųstų vektorių matricas verčiame į eilutę
        for(int i = 0; i<results.size();i++){
            String receivedVector = this.matrixActions.vectorMatrixToString(encode? decodedResults.get(i): results.get(i));
            channelResult += receivedVector;
        }

//        Pašaliname pridėtus 0, ir dvejetainę eilutę pakeičiame į eilutę iš ASCII simbolių
        String receivedBinary = this.removeZeros(channelResult, binaryString.length());
        return this.binaryStringToText(receivedBinary);
    }

    /**
     * @param string eilutė, kurios ilgį norime pakeisti
     * @param length norimas eilutės ilgis
     * @return kol eilutė nėra reikiamo ilgio, nuima po vieną raidę
     */
    private String removeZeros(String string, int length){
        while(string.length()>length){
            string = string.substring(1);
        }

        return string;
    }

    /**
     * @param string dvejetainė eilutė, kurią verčiame į vektorius
     * @param size vektorių ilgis
     * @param encode ar norime koduoti vektorius
     * @return funkcija grąžina nurodyto ilgio ir, jei nurodyta, užkoduotus vektorius
     */
    private List<int[][]> splitBinaryStringToVectors(String string, int size, boolean encode){
        List<int[][]> vectors = new ArrayList<>();

//        Pridedame 0, kol galėsime išskaidyti į norimo ilgio vektorius
        while(string.length() % size != 0){
            string = "0" + string;
        }

        for(int i=0;i<string.length();i+=size){
            String substring = string.substring(i, i+size);
            vectors.add(encode ? this.reedMuller.encode(substring) : matrixActions.getMatrixFromWord(substring));
        }

        return vectors;
    }

    /**
     * @param string tekstas, kurį norime paversti į dvejetainį formatą
     * @return funkcija paverčia kiekvieną teksto simbolį į 7 ilgio dvejetainių skaičių seką
     */
    private String textToBinaryString(String string){
        String binaryString = "";

        for (int i = 0; i < string.length(); i++)
        {
            int asciiVal = Integer.valueOf(string.charAt(i));
            String binary = Integer.toBinaryString(asciiVal);

            while(binary.length() < 7){
                binary = "0" + binary;
            }

            binaryString += binary;
        }

        return binaryString;
    }

    /**
     * @param string dvejetainė eilutė, kurią norime paversti į tekstą
     * @return funkcija grąžina eilutę dvejetainio formato ASCII reikšmes paverčiant į simbolius
     * (kadangi didžiausia ASCII simbolio vertė yra 1111111, kodėl visus simbolius turime 7 simbolių ilgio)
     */
    private String binaryStringToText(String string){
        int idx = 0;
        String text = "";

        while (idx < string.length()){
            String binaryChar = string.substring(idx, idx + 7);
            int asciiChar = Integer.parseInt(binaryChar, 2);
            char c =(char)asciiChar;
            text += c;
            idx += 7;
        }

        return text;
    }
}
