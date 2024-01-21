package com.company;

import java.util.Random;

public class Channel {
    private Validator validator;

    public Channel(){
        this.validator = new Validator();
    }

    /**
     * @param code siunčiamas per kanalą vektorius paverstas į matricą
     * @param errorProbability tikimybė nuo 0 iki 1, kad bite bus klaida
     * @param logErrors ar norime, kad į konsolę išvestų kanale atsiradusias klaidas
     * @return išsiunčia vektorių kanalu ir grąžina gautą vektorių
     */
    public int [][] sendThroughChannel (int [][] code, float errorProbability, boolean logErrors) {
        Random random = new Random();

        for (int i = 0; i < code[0].length; i++) {
            if (random.nextFloat() < errorProbability) {
                if(logErrors) {
                    this.validator.displayChannelError(i);
                }

                code[0][i] = code[0][i] == 1 ? 0: 1;
            }
        }

        return code;
    }
}
