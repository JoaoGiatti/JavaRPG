package com.rpg.personagem;

import com.rpg.inventario.Inventario;

public class Guerreiro extends Personagem{

    public void Guerreiro(String nome) throws Exception {
         this.setNome(nome);
         this.setPontosVida(100);
         this.setAtaque(25);
         this.setDefesa(30);
         this.setNivel(1);
         this.setInventario(new Inventario);
    }
}
