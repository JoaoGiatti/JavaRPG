package com.rpg.personagens;

import com.rpg.dados.RolagemDeDados;

public class Dragao extends Inimigo {
    private int danoFogo;
    RolagemDeDados rolagem;
    Personagem personagem;

    public Dragao(String nome,  int pontosVida, int ataque, int defesa, int danoFogo) {
        super("Dragão", 300, 70, 80);
        this.danoFogo = danoFogo;
    }

    public void soltarFogo() {

    }
}
