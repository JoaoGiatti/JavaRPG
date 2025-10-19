package com.rpg.personagens;

import com.rpg.inventario.Inventario;

public class Arqueiro extends Personagem{
    public void Arqueiro(String nome) throws Exception {
        this.setNome(nome);
        this.setPontosVida(100);
        this.setAtaque(28);
        this.setDefesa(25);
        this.setNivel(1);
        this.setInventario(new Inventario);
    }
}
