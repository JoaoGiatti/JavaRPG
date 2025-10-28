package com.rpg.personagens;

public class Inimigo extends Personagem{
    public Inimigo(String nome, int pontosVida, int ataque, int defesa, int vidaMaxima) {
        super(nome, pontosVida, ataque, defesa, vidaMaxima);
    }

    public Inimigo(Inimigo modelo) throws Exception {
        super(modelo); // chama o construtor de cópia da classe Personagem
    }

    @Override
    public Object clone() {
        Inimigo ret = null;
        try {
            ret = new Inimigo(this);
        } catch (Exception e) {}
        return ret;
    }
}