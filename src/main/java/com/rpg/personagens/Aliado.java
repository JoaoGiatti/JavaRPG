package com.rpg.personagens;

public class Aliado extends Personagem{
    public Aliado(String nome, int pontosVida, int ataque, int defesa, int vidaMaxima) {
        super(nome, pontosVida, ataque, defesa, vidaMaxima);
    }

    public Aliado(Aliado modelo) throws Exception {
        super(modelo); // chama o construtor de cópia da classe Personagem
    }

    @Override
    public Object clone() {
        Aliado ret = null;
        try {
            ret = new Aliado(this);
        } catch (Exception e) {}
        return ret;
    }
}
