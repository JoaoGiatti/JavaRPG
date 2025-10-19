package com.rpg;

import com.rpg.jogo.Jogo;

public class Main {
    public static void main(String[] args) {
        Jogo jogar = new Jogo();

        System.out.println("\nVAMOS COMEÇAR!");
        try{
            jogar.iniciar();
        } catch (Exception e){}
    }
}