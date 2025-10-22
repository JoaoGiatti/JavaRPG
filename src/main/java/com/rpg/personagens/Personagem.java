package com.rpg.personagens;

import com.rpg.inventario.Inventario;
import com.rpg.dados.RolagemDeDados;
import com.rpg.inventario.Item;
import com.rpg.jogo.Jogo;

import java.util.Scanner;

public abstract class Personagem implements Cloneable {
    private final String nome;
    private int pontosVida;
    private int ataque;
    private int defesa;
    private final int nivel;
    private final Inventario inventario;
    private Item armaFisica;
    private Item armaDistancia;
    private Item armadura;
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
        System.out.println("""
                ========== VOCÊ MORREU ==========
                    [1] - Tentar novamente
                    [2] - Sair
                """);
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
        int dano = inimigo.getAtaque();

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
            danoRecebido = inimigo.getAtaque() + 5;
            System.out.println(this.nome + " falhou completamente na defesa e foi atingido em cheio! Dano: " + danoRecebido);
        }

        this.pontosVida -= danoRecebido;

        if (this.pontosVida < 0) this.pontosVida = 0;
    }

    public boolean temAliado() { return true; }


    public String getNome() { return this.nome; }
    public int getPontosVida() { return this.pontosVida; }
    public int getAtaque() { return ataque; }
    public int getNivel() { return nivel; }
    public int getDefesa() { return defesa; }
    public Inventario getInventario() { return inventario; }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void curar(int valor) {
        if(this.pontosVida + valor > 100){
            this.pontosVida = 100;
        } else {
            this.pontosVida += valor;
        }
    }

    public void equipar(Item item) throws Exception {
        switch (item.getTipo()) {
            case FISICO:
                if (this.armaFisica != null)
                    throw new Exception("Você já tem uma arma física equipada!");
                this.armaFisica = item;
                this.ataque += (int) item.getValor();
                break;

            case DISTANCIA:
                if (this.armaDistancia != null)
                    throw new Exception("Você já tem uma arma de distância equipada!");
                this.armaDistancia = item;
                this.ataque += (int) item.getValor();
                break;

            case EQUIPAVEL:
                if (this.armadura != null)
                    throw new Exception("Você já está usando uma armadura!");
                this.armadura = item;
                this.defesa += (int) item.getValor();
                break;

            default:
                throw new Exception("Esse item não é equipável!");
        }
    }

    @Override
    public String toString() {
        return nome + " (HP: " + pontosVida + ", Atk: " + ataque + ", Def: " + defesa + ")";
    }
}