package com.rpg.personagem;

public class Mago extends Personagem{
    public void Mago(String nome) throws Exception {
        this.setNome(nome);
        this.setPontosVida(100);
        this.setAtaque(35);
        this.setDefesa(5);
        this.setNivel(1);
        this.setInventario(new Inventario);
    }
}
