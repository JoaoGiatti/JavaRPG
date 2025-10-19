package com.rpg.personagens;

import com.rpg.inventario.Inventario;
import com.rpg.dados.RolagemDeDados;

public abstract class Personagem implements Cloneable {
    protected String nome;
    protected int pontosVida;
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected Inventario inventario;

    public Personagem(String nome, int pontosVida, int ataque, int defesa) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = 1;
        this.inventario = new Inventario();
    }

    public boolean estaVivo() { return pontosVida > 0; }

    public void sofrerDano(int dano) {
        this.pontosVida -= dano;
        if (this.pontosVida < 0) this.pontosVida = 0;
    }

    public void atacar(Personagem alvo) {
        RolagemDeDados dado = new RolagemDeDados();
        int rolagem = dado.rolar();
        int dano = this.ataque + rolagem - alvo.defesa;
        if (dano > 0) {
            alvo.sofrerDano(dano);
            System.out.println(this.nome + " causou " + dano + " em " + alvo.nome + " (rolou " + rolagem + ").");
        } else {
            System.out.println(this.nome + " não penetrou a defesa de " + alvo.nome + " (rolou " + rolagem + ").");
        }
    }

    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome + " (HP: " + pontosVida + ", Atk: " + ataque + ", Def: " + defesa + ")";
    }
}