package com.rpg.personagens;

import com.rpg.dados.RolagemDeDados;

public class Dragao extends Inimigo {
    private int danoFogo;

    public Dragao(int danoFogo) {
        super("Dragão", 300, 70, 80, 300);
        this.danoFogo = danoFogo;
    }

    public void soltarFogo() {

    }
}
