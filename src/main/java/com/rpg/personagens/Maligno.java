package com.rpg.personagens;

import java.util.Random;

public class Maligno extends Inimigo {
    private Random random = new Random();
    private int turnos = 0;

    public Maligno(String nome) {
        super(nome, 400, 42, 25, 300);
    }

    public void ataqueEspecial(Personagem jogador, Personagem aliado) {
        turnos++;
        int escolha = random.nextInt(3);

        switch (escolha) {
            case 0 -> {
                System.out.println("\n[MALIGNO] >> \"Vocês acham que podem me vencer com espadas e fé?\"");
                System.out.println("Maligno invoca *Chamas do Caos*! Labaredas negras tomam o campo!");
                jogador.sofrerDano(25 + random.nextInt(10));
                aliado.sofrerDano(15 + random.nextInt(5));
                System.out.println("🔥 Ambos sofrem dano com o calor infernal!");
            }
            case 1 -> {
                System.out.println("\n[MALIGNO] >> \"O tempo... pertence a mim!\"");
                System.out.println("Maligno distorce o tempo! Ele ataca duas vezes seguidas!");
                int dano1 = getAtaque() + random.nextInt(10);
                int dano2 = getAtaque() + random.nextInt(10);
                jogador.sofrerDano(dano1);
                jogador.sofrerDano(dano2);
                System.out.println("💫 Você é atingido duas vezes seguidas! (" + (dano1 + dano2) + " de dano total)");
            }
            case 2 -> {
                System.out.println("\n[MALIGNO] >> \"Vocês esquecem que sou eterno.\"");
                System.out.println("Maligno absorve energia da fenda e recupera vitalidade!");
                int cura = 40 + random.nextInt(15);
                this.setPontosVida(this.getPontosVida() + cura);
                System.out.println("🩸 Maligno recupera " + cura + " de vida!");
            }
        }

        // Modo desespero quando a vida cai abaixo de 30%
        if (this.getPontosVida() <= 100 && turnos % 2 == 0) {
            System.out.println("\n[MALIGNO] >> \"CHEGA DE JOGOS!\"");
            System.out.println("Maligno lança *Desintegração Sombria*! Um raio direto contra o jogador!");
            jogador.sofrerDano(40 + random.nextInt(20));
        }
    }

    @Override
    public String toString() {
        return "Turno: " + turnos;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Maligno m = (Maligno) obj;
        if(this.turnos != m.turnos) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + ((Integer)this.turnos).hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }

}
