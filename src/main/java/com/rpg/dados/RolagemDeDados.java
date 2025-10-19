package com.rpg.dados;

import java.util.Random;

public class RolagemDeDados {

    private final Random rand = new Random();

    // metodo que retorna um D20
    public int rolar() {
        return rand.nextInt(20) + 1;
    }
}
