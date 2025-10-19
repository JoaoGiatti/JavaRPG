package com.rpg.personagens;


public abstract class Inimigo extends Personagem{
    public Inimigo(String nome, int pontosVida, int ataque, int defesa) {
        super(nome, pontosVida, ataque, defesa);
    }
}