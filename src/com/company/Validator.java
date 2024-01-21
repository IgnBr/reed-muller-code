package com.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    /**
     * @param vector teksto eilutė, kuri tikrinama, ar yra validus vektorius
     * @param length vektoriaus ilgis, kurio tikimasi
     * Funkcija patikrina, ar eilutė yra sudaryta iš 0 ir 1 ir atitinka duotą ilgį
     */
    void isValidVector (String vector, int length){
        Pattern p = Pattern.compile("[0-1]+");
        Matcher m = p.matcher(vector);

        if(!m.matches() || vector.length() != length){
            throw new IllegalArgumentException("Vektorius sudarytas is klaidingos abeceles arba yra klaidingo ilgio");
        }
    }

    /**
     * @param posibility realus skaičius nuo 0 iki 1
     * Funkcija patikrina, ar duotas skaičius yra tarp nulio ir vieno
     */
    void isValidProbability (float posibility){
        if(posibility > 1 || posibility < 0){
            throw new IllegalArgumentException("Klaidos tikimybe p turi buti realusis skaicius tarp 0 ir 1");
        }
    }

    /**
     * @param m Rydo Miulerio kodo m parametras
     * Funkcija patikrina, ar paduotas skaičius yra didesnis už nulį
     */
    void isValidM (int m){
        if(m<=0){
            throw new IllegalArgumentException("m turi buti daugiau uz 0");
        }
    }

    /**
     * @param i kanale esančio vektoriaus klaidos pozicija
     * Į konsolę išveda, jog kanale atsitiko klaida ir nurodo jos poziciją
     */
    void displayChannelError(int i){
        String errorMessage = "Klaida " + i + " pozicijoje";
        System.out.println(errorMessage);
    }
}
