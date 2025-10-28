package com.rpg.personagens;

public class Arqueiro extends Personagem {
    public Arqueiro(String nome) {
        super(nome, 80, 12, 8, 80);
    }

    public Arqueiro(Arqueiro modelo) throws Exception {
        super(modelo); // chama o construtor de cópia da classe Personagem
    }

    @Override
    public Object clone() {
        Arqueiro ret = null;
        try {
            ret = new Arqueiro(this);
        } catch (Exception e) {}
        return ret;
    }
}