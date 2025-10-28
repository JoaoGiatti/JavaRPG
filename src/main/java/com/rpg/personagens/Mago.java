package com.rpg.personagens;

public class Mago extends Personagem {
    public Mago(String nome) {
        super(nome, 70, 20, 5, 70);
    }

    public Mago(Mago modelo) throws Exception {
        super(modelo); // chama o construtor de cópia da classe Personagem
    }

    @Override
    public Object clone() {
        Mago ret = null;
        try {
            ret = new Mago(this);
        } catch (Exception e) {}
        return ret;
    }
}