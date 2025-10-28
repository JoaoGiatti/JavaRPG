package com.rpg.personagens;

import com.rpg.inventario.Inventario;

public class Guerreiro extends Personagem implements Cloneable {
    public Guerreiro(String nome) {
        super(nome, 100, 15, 10, 100);
    }

    public Guerreiro(Guerreiro modelo) throws Exception {
        super(modelo); // chama o construtor de cópia da classe Personagem
    }

    @Override
    public Object clone() {
        Guerreiro ret = null;
        try {
            ret = new Guerreiro(this);
        } catch (Exception e) {}
        return ret;
    }
}