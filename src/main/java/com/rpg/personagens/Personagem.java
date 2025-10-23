package com.rpg.personagens;

import com.rpg.inventario.Inventario;
import com.rpg.inventario.Item;
import com.rpg.jogo.Jogo;

import java.util.Scanner;

public abstract class Personagem implements Cloneable {

    // ATRIBUTOS

    private final String nome;
    private int pontosVida;
    private int ataque;
    private int defesa;
    private final int nivel;
    private final Inventario inventario;
    private Item armaFisica;
    private Item armaDistancia;
    private Item armadura;
    private boolean temAliado = false;
    private boolean emBatalha = false;
    Jogo jogo;

    // CONSTRUTOR

    public Personagem(String nome, int pontosVida, int ataque, int defesa) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = 1;
        this.inventario = new Inventario();
    }

    //GETTERS

    public String getNome() { return this.nome; }
    public int getPontosVida() { return this.pontosVida; }
    public int getAtaque() { return ataque; }
    public int getNivel() { return nivel; }
    public int getDefesa() { return defesa; }
    public Inventario getInventario() { return inventario; }

    // SETTERS -> TO-DO

    public void setTemAliado(boolean temAliado) {
        this.temAliado = temAliado;
    }
    public void setEmBatalha(boolean emBatalha) {
        this.emBatalha = emBatalha;
    }
    public void setPontosVida(int pontosVida) {
        this.pontosVida = pontosVida;
    }

    // MÉTODOS

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public boolean estaVivo() { return pontosVida > 0; }

    public void tratarMorte(Personagem jogador, Inimigo inimigo, Jogo jogo, int progressao) throws Exception {
        Scanner sc = new Scanner(System.in);
        if (!jogador.estaVivo()) {
            System.out.println("\n======= ❌ VOCÊ MORREU ❌ =======" +
                    "O que deseja fazer?\n" +
                    "   [1] - Tentar novamente\n" +
                    "   [2] - Sair do jogo");
            System.out.print("Escolha: ");

            int escolha = sc.nextInt();

            switch (escolha) {
                case 1 -> {
                    System.out.println("\nRetomando...\n");
                    if(jogo.opcao == 1) jogador.setPontosVida(100);
                    if(jogo.opcao == 2) jogador.setPontosVida(70);
                    if(jogo.opcao == 3) jogador.setPontosVida(80);
                    System.out.println();
                    if(jogador.emBatalha()) jogo.batalhar(jogador, inimigo);
                    else jogo.explorar(progressao);
                }

                case 2 -> {
                    System.out.println("\nVocê decide encerrar sua jornada. Até a próxima!");
                    System.exit(0); // encerra o jogo
                }

                default -> {
                    System.out.println("Opção inválida! Escolha novamente.");
                    tratarMorte(jogador, inimigo, jogo, progressao);
                }
            }
        }
    }

    public void sofrerDano(int dano) {
        this.pontosVida -= dano;
        if (this.pontosVida < 0) this.pontosVida = 0;
    }

    public boolean temAliado() {
        return temAliado;
    }

    public boolean emBatalha() {
        return emBatalha;
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