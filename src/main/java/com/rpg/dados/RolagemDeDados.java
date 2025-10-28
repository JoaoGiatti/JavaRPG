package com.rpg.dados;

import java.util.Random;
import java.util.Scanner;

import com.rpg.personagens.Personagem;

public class RolagemDeDados {

    private final Random rand = new Random();
    private Scanner sc = new Scanner(System.in);

    // metodo que retorna um D20
    public int rolar() {
        return rand.nextInt(20) + 1;
    }

    public void simulacao(Personagem jogador){

        System.out.print(jogador.getNome() + " ROLE O DADO. [ENTER]");
        sc.nextLine();
        System.out.print("Rolando");
        for (int i = 0; i < 3; i++) { // animação simples de pontos
            try {
                Thread.sleep(500); // espera 0,5 segundo
                System.out.print(".");
            } catch (Exception erro) {}
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return "Resultado: " + rand;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        RolagemDeDados r = (RolagemDeDados) obj;
        if(!this.rand.equals(r.rand)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + this.rand.hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }
}
