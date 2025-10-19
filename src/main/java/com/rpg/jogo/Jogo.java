package com.rpg.jogo;

import com.rpg.dados.RolagemDeDados;
import com.rpg.inventario.Inventario;
import com.rpg.personagens.Arqueiro;
import com.rpg.personagens.Guerreiro;
import com.rpg.personagens.Mago;
import com.rpg.personagens.Personagem;
import java.util.*;

public class Jogo {
    private Scanner sc = new Scanner(System.in);
    private Personagem jogador;
    private int progressao = 0;

    public void iniciar() {
        System.out.println("Bem-vindo ao RPG de Texto!");
        System.out.print("Escolha seu nome: ");
        String nome = sc.nextLine();

        System.out.println("Escolha sua classe:");
        System.out.println("1. Guerreiro\n2. Mago\n3. Arqueiro");
        int opcao = sc.nextInt();
        sc.nextLine();

        switch (opcao) {
            case 1 -> jogador = new Guerreiro(nome);
            case 2 -> jogador = new Mago(nome);
            case 3 -> jogador = new Arqueiro(nome);
            default -> jogador = new Guerreiro(nome);
        }

        loopPrincipal();
    }

    private void loopPrincipal() {
        while (jogador.estaVivo()) {
            System.out.println("\nO que deseja fazer?");
            System.out.println("1. Explorar");
            System.out.println("2. Usar item");
            System.out.println("3. Ver inventário");
            System.out.println("4. Sair do jogo");

            int escolha = sc.nextInt();
            sc.nextLine();

            switch (escolha) {
                case 1 -> explorar(progressao);
                case 2 -> usarItem();
                case 3 -> jogador.inventario.listarItens();
                case 4 -> { System.out.println("Saindo..."); return; }
            }

            progressao++;
        }
        System.out.println("Você morreu... Fim de jogo.");
    }

    private void explorar(int progressao) {

        switch (progressao) {
            case 0 -> {
                System.out.print(jogador.getNome() + " ");
                int evento = new Random().nextInt(3);
                if (evento == 0) {
                    System.out.println("Você encontrou um inimigo!");
                    batalhar(jogador, new Inimigo("Goblin", 40, 8, 5));
                } else if (evento == 1) {
                    System.out.println("Você encontrou uma poção!");
                    jogador.inventario.adicionarItem(new Item("Poção de Cura", "Recupera HP", "cura", 1));
                } else {
                    System.out.println("Você caiu numa armadilha! Perdeu 10 de HP!");
                    jogador.sofrerDano(10);
                }
            }
            case 1 -> {

            }
        }
    }

    private void usarItem() {
        jogador.inventario.listarItens();
        System.out.print("Digite o nome do item para usar: ");
        String nome = sc.nextLine();
        // procura e usa o item
    }

    private void batalhar(Personagem jogador, Inimigo inimigo) {
        System.out.println("Um " + inimigo.getNome() + " aparece!");
        while (jogador.estaVivo() && inimigo.estaVivo()) {
            jogador.atacar(inimigo);
            if (!inimigo.estaVivo()) break;
            inimigo.atacar(jogador);
        }
    }
}
