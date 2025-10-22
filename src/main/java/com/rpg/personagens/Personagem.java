package com.rpg.personagens;

import com.rpg.inventario.Inventario;
import com.rpg.dados.RolagemDeDados;
import com.rpg.jogo.Jogo;

import java.util.Scanner;

public abstract class Personagem implements Cloneable {
    public String nome;
    public int pontosVida;
    public int ataque;
    public int defesa;
    public int nivel;
    public Inventario inventario;
    RolagemDeDados rolagem = new RolagemDeDados();
    Jogo jogo;
    Scanner sc = new Scanner(System.in);

    public Personagem(String nome, int pontosVida, int ataque, int defesa) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = 1;
        this.inventario = new Inventario();
    }

    public boolean estaVivo() { return pontosVida > 0; }

    public void tratarMorte(Personagem jogador, Inimigo inimigo) throws Exception{
        System.out.println("========== VOCÊ MORREU ==========\n" +
                "    [1] - Tentar novamente\n" +
                "    [2] - Sair\n");
        System.out.print("Escolha sua opção: ");
        int resp = sc.nextInt();
        sc.nextLine();
        if(resp == 1) {
            jogador.pontosVida = 100;
            jogador.estaVivo();
            jogo.batalhar(jogador, inimigo);
        }
        else if(resp == 2) System.out.println("Saindo...");
        else {
            System.out.println("Número inválido! Digite novamente.");
            tratarMorte(jogador, inimigo);
        }
    }

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

    public void defender(Inimigo inimigo) {
        int resultado = rolagem.rolar();
        int danoRecebido = 0;
        int dano = inimigo.ataque;

        if (resultado >= 18) {
            int defesa = (int) (this.defesa * 1.5);
            danoRecebido = dano - defesa;
            System.out.println(this.nome + " conseguiu se defender! Sofreu " + danoRecebido + " de dano.");
        }
        else if (resultado >= 14) {
            int defesa = (int) (this.defesa * 1.2);
            danoRecebido = dano - defesa;
            System.out.println(this.nome + " defendeu parcialmente. Sofreu " + danoRecebido + " de dano.");
        }
        else if (resultado >= 10) {
            int defesa = (int) (this.defesa * 0.5);
            danoRecebido = dano - defesa;
            System.out.println(this.nome + " se defendeu, mas ainda levou um bom dano! Dano: " + danoRecebido);
        }
        else if (resultado >= 5) {
            int defesa = (int) (this.defesa * 0.2);
            danoRecebido = dano - defesa;
            System.out.println(this.nome + " falhou na defesa! Sofreu dano total de " + danoRecebido);
        }
        else {
            danoRecebido = inimigo.ataque + 5;
            System.out.println(this.nome + " falhou completamente na defesa e foi atingido em cheio! Dano: " + danoRecebido);
        }

        this.pontosVida -= danoRecebido;

        if (this.pontosVida < 0) this.pontosVida = 0;
    }

    public boolean temAliado() { return true; }


    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome + " (HP: " + pontosVida + ", Atk: " + ataque + ", Def: " + defesa + ")";
    }
}