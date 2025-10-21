package com.rpg.jogo;

import com.rpg.dados.RolagemDeDados;
import com.rpg.inventario.Item;
import com.rpg.personagens.*;

import java.util.*;

public class Jogo {
    private Scanner sc = new Scanner(System.in);
    private Personagem jogador;
    private Inimigo inimigo;
    private RolagemDeDados rolagem;
    private int progressao = 0;
    public int opcao = 0;

    public void iniciar() throws Exception {
        System.out.println("Bem-vindo ao RPG de Texto!");
        System.out.print("Escolha seu nome: ");
        String nome = sc.nextLine();

        System.out.println("Escolha sua classe:");
        System.out.println("[1] - Guerreiro\n[2] - Mago\n[3] - Arqueiro");
        opcao = sc.nextInt(); // adicionar exception para opções que não sejam 1, 2 e 3
        sc.nextLine();

        switch (opcao) {
            case 1 -> jogador = new Guerreiro(nome);
            case 2 -> jogador = new Mago(nome);
            case 3 -> jogador = new Arqueiro(nome);
            default -> jogador = new Guerreiro(nome);
        }

        loopPrincipal();
    }

    private void loopPrincipal() throws Exception {
        while (jogador.estaVivo()) {
            System.out.println("\nO que deseja fazer?");
            System.out.println("[1] - Explorar");
            System.out.println("[2] - Usar item");
            System.out.println("[3] - Ver inventário");
            System.out.println("[4] - Sair do jogo");

            int escolha = sc.nextInt();
            sc.nextLine();

            switch (escolha) {
                case 1 -> explorar(progressao);
                case 2 -> usarItem();
                case 3 -> jogador.inventario.listarItens(); // criar metodo do inventario
                case 4 -> { System.out.println("Saindo..."); return; }
                default -> {
                    System.out.println("Número inválido! Digite novamente.");
                    loopPrincipal();
                }
            }

            progressao++;
            // subir nivel do usuario
        }
        System.out.println("Você morreu... Fim de jogo.");
    }

    private void explorar(int progressao) throws Exception {

        switch (progressao) {
            case 0 -> {
                System.out.print(jogador.getNome() +
                        " abre os olhos... Não se lembra de nada. \n" +
                        "Ao olhar ao redor, tudo está escuro, mas por sorte acha uma tocha e uma pederneira.\n" +
                        "Acende a tocha e sua luz lhe trás memórias, algo urge em sua mente...\n" +
                        "Gritos, sangue, seu propósito... Mas nada está claro ainda.\n");
                if(opcao == 1){
                    System.out.print("Em seu lado, há uma espada, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item espadaDeFerro = new Item("Espada de Ferro", "Uma Espada enferrujada e velha", "Causa 5 de dano físico", 1);
                    jogador.inventario.adicionarItem(espadaDeFerro);
                } else if (opcao == 2) {
                    System.out.print("Em seu lado, há algumas flechas e um arco, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item arco = new Item("Arco", "Arco de madeira envelhecida", "Causa 5 de dano físico", 1);
                    Item flecha = new Item("Fecha", "Flecha de madeira com ponta de ferro", "Causa 5 de dano físico", 7);
                    jogador.inventario.adicionarItem(arco);
                    jogador.inventario.adicionarItem(flecha);
                } else if (opcao == 3) {
                    System.out.print("Em seu lado, há um cajado, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item cajado = new Item("Cajado", "Um cajado de madeira envelhecida", "Aumenta em 10% o dano de todas as magias", 1);
                    jogador.inventario.adicionarItem(cajado);
                }

                int evento = rolagem.rolar();
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
                loopPrincipal();
            }
            case 1 -> {
                // progressão 1 - inimigos mais fortes, itens mais raros... AQUI É ONDE A PROGRESSÃO DA HISTORIA É FEITA
            }
        }
    }

    private void usarItem() {
        jogador.inventario.listarItens();
        System.out.print("Digite o nome do item para usar: ");
        String nome = sc.nextLine();
        // procura e usa o item
    }

    public void batalhar(Personagem jogador, Inimigo inimigo) {
        System.out.println("Um " + inimigo.getNome() + " aparece!");
        while (jogador.estaVivo() && inimigo.estaVivo()) {
            jogador.atacar(inimigo);
            if (!inimigo.estaVivo()) break;
            inimigo.atacar(jogador);
            jogador.defender(inimigo);
            if(!jogador.estaVivo()) jogador.estaMorto(jogador, inimigo);
        }
    }
}
