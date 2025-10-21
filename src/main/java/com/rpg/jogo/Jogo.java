package com.rpg.jogo;

import com.rpg.dados.RolagemDeDados;
import com.rpg.inventario.Item;
import com.rpg.personagens.*;

import java.util.*;

public class Jogo {
    private Scanner sc = new Scanner(System.in);
    private Personagem jogador;
    private Inimigo inimigo;
    private final RolagemDeDados rolagem = new RolagemDeDados();
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
            try {
                System.out.println("\nO que deseja fazer?");
                System.out.println("[1] - Explorar");
                System.out.println("[2] - Usar item");
                System.out.println("[3] - Ver inventário");
                System.out.println("[4] - Sair do jogo");

                int escolha = sc.nextInt();
                sc.nextLine();

                switch (escolha) {
                    case 1 -> {
                        explorar(progressao);
                        progressao++;
                        // subir nivel do usuario
                    }
                    case 2 -> usarItem();
                    case 3 -> jogador.inventario.listarItens(); // criar metodo do inventario
                    case 4 -> { System.out.println("Saindo..."); return; }
                    default -> {
                        System.out.println("Número inválido! Digite novamente.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Ocorreu um erro: " + e.getMessage());
                e.printStackTrace();
            }
        }
        jogador.estaMorto(jogador, inimigo);
    }

    private void explorar(int progressao) throws Exception {
        switch (progressao) {
            case 0 -> {
                //  ----------- CONTEXTO DA PROGRESSÃO ----------
                System.out.print(jogador.getNome() +
                        " abre os olhos... Não se lembra de nada. \n" +
                        "Ao olhar ao redor, tudo está escuro, mas por sorte acha uma tocha e uma pederneira.\n" +
                        "Acende a tocha e sua luz lhe trás memórias, algo urge em sua mente...\n" +
                        "Gritos, sangue, seu propósito... Mas nada está claro ainda.\n");
                if(opcao == 1){
                    System.out.print("Em seu lado, há uma espada, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item espadaDeFerro = new Item("Espada de Ferro", "FISICO", 5, 1);
                    jogador.inventario.adicionarItem(espadaDeFerro);
                } else if (opcao == 2) {
                    System.out.print("Em seu lado, há um cajado, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item cajado = new Item("Cajado", "CAJADO", 10, 1);
                    jogador.inventario.adicionarItem(cajado);
                } else if (opcao == 3) {
                    System.out.print("Em seu lado, há algumas flechas e um arco, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item arco = new Item("Arco", "ARCO", 5, 1);
                    Item flecha = new Item("Fecha", "FLECHA", 0, 7);
                    jogador.inventario.adicionarItem(arco);
                    jogador.inventario.adicionarItem(flecha);
                }

                System.out.println("Leva consigo, pois algo lhe diz que vai precisar.\n" +
                        "Continua andando, buscando uma saída, até que...\n");

                //  ----------- ROLAGEM DE DADO ----------

                rolagem.simulacao(jogador);
                int evento = rolagem.rolar();
                System.out.println("RESULTADO DO D20: " + evento);

                // //  ----------- EVENTOS ----------

                if (evento >= 15) {
                    System.out.println("Você encontrou uma poção!");
                    jogador.inventario.adicionarItem(new Item("Poção de Cura", "CURA", 10, 1));
                } else if (evento >= 8) {
                    System.out.println("Você encontrou um inimigo!");
                    batalhar(jogador, new Inimigo("Goblin", 40, 8, 5));
                } else {
                    System.out.println("Você caiu numa armadilha! Perdeu 10 de HP!");
                    jogador.sofrerDano(10);
                }
            }
            case 1 -> {
                boolean repetir = true;
                while (repetir) {
                    System.out.print("Segue seu caminho, e, de repente " +
                            jogador.getNome() +
                            " enxerga uma luz no final daquele lugar escuro em que estava, e se aproxima dela.\n" +
                            "Esfrega os olhos, estica as pernas e finalmente sai daquela caverna.\n" +
                            "Porém, ao sair, ouve um barulho diferente ao fundo.\n" +
                            "Correr em direção ao barulho?\n" +
                            "   [1] - Sim\n" +
                            "   [2] - Não\n");

                    int escolha = sc.nextInt();
                    sc.nextLine();

                    if (escolha == 1) {
                        System.out.print(jogador.getNome() + " corre em direção ao barulho.\n" +
                                "Quando consegue alcançar, percebe que são gritos de socorro!\n" +
                                "Uma pessoa está sendo atacada por um lobo-cinzento, o lobo mais forte entre os lobos.\n" +
                                "Você sente que precisa ajudar ela, mas está muito longe... O que você tenta fazer?\n");
                        System.out.println("   [1] - Distrair o lobo com alguma coisa\n" +
                                "   [2] - Aproximar-se furtivamente para tentar atacar o lobo.\n");
                        int opc = sc.nextInt();
                        sc.nextLine();

                        //  ----------- ROLAGEM DE DADO ----------

                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);

                        // //  ----------- EVENTOS ----------

                        if (opc == 1) {
                            if (evento >= 15) {
                                System.out.println("Você encontra uma pedra relativamente grande no chão.\n" +
                                        "Atira ela para longe, o lobo escuta e vai em direção ao som.\n" +
                                        "Você conseguiu destrair o lobo!");
                            } else if (evento >= 8) {
                                System.out.println("Você só encontra uma pequena pedra no chão.\n" +
                                        "Atira ela para longe, o lobo olha, mas volta a atacar a pessoa.\n" +
                                        "Você não conseguiu destrair o lobo.\n" +
                                        "Sua única opção é atacar.");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));
                            } else {
                                System.out.println("Você não encontra nada ao seu redor.\n" +
                                        "Mas enquanto procurava, sem prestar atenção pisou em um galho seco.\n" +
                                        "O lobo escutou... e agora está em sua direção...\n" +
                                        "Sua única opção é atacar.");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));
                            }
                        }
                        else if (opc == 2) {
                            if (evento >= 15) {
                                System.out.println("Você anda furtivamente para trás do lobo.\n" +
                                        "Ele está muito destraído atacando a pessoa, então não sente sua presença ali.\n" +
                                        "Você consegue chegar atrás do lobo.\n" +
                                        "Sua única opção é atacar.");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));

                            } else if (evento >= 8) {
                                System.out.println("Você tenta andar furtivamente para trás do lobo.\n" +
                                        "Enquanto anda, sem querer, pisa em um galho seco.\n" +
                                        "Mas o lobo está tão focado em atacar a pessoa, que não escuta.\n" +
                                        "Você chega atrás do lobo.\n" +
                                        "Sua única opção é atacar.");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));

                            } else {
                                System.out.println("Você tenta andar furtivamente para trás do lobo.\n" +
                                        "Mas você não prestou atenção em um galho seco que estava bem na sua frente.\n" +
                                        "Pisa, o lobo escuta, e agora ele está indo em sua direção...\n" +
                                        "Sua única opção é atacar.");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));
                            }
                        }
                        else {
                            System.out.println("Número inválido! Retornando...\n");
                        }

                        System.out.println("Você vai em direção a pessoa, ajuda ela a se levantar e vocês fogem daquele lugar.");
                        jogador.temAliado();

                        repetir = false;
                    }
                    else if (escolha == 2) {
                        System.out.println("Você finge que não ouviu, pensa que era coisa da sua cabeça,\n" +
                                "podia ser até mesmo o canto de um pássaro...\n" +
                                "Então, você olha ao redor e...");
                        repetir = false;
                    }
                    else {
                        System.out.println("Número inválido! Retornando...\n");
                    }
                }
            }

            default -> {
                System.err.println("Algo deu errado na progressão!");
            }
        }
    }

    private void usarItem() {
        jogador.inventario.listarItens();
        System.out.print("Digite o nome do item para usar: ");
        String nome = sc.nextLine();
        jogador.inventario.getItem(nome);
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
