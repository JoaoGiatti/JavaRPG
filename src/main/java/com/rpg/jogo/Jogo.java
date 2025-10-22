package com.rpg.jogo;

import com.rpg.dados.RolagemDeDados;
import com.rpg.inventario.Item;
import com.rpg.personagens.*;

import java.util.*;

public class Jogo {
    private final Scanner sc = new Scanner(System.in);
    private Personagem jogador;
    private Inimigo inimigo;
    private final RolagemDeDados rolagem = new RolagemDeDados();
    private int progressao = 0;
    private int opcao = 0;

    public void iniciar() throws Exception {
        System.out.println("Bem-vindo ao RPG de Texto!");
        System.out.print("Escolha seu nome: ");
        String nome = sc.nextLine();

        System.out.println("Escolha sua classe:");
        System.out.println("[1] - Guerreiro\n[2] - Mago\n[3] - Arqueiro");
        opcao = sc.nextInt();
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
                    case 3 -> jogador.getInventario().listarItens(); // criar metodo do inventario
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
        jogador.tratarMorte(jogador, inimigo);
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
                    Item espadaDeFerro = new Item("Espada de Ferro", Item.TipoItem.FISICO, 5, 1);
                    jogador.getInventario().adicionarItem(espadaDeFerro);
                } else if (opcao == 2) {
                    System.out.print("Em seu lado, há um cajado, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item cajado = new Item("Cajado", Item.TipoItem.DISTANCIA, 10, 1);
                    jogador.getInventario().adicionarItem(cajado);
                } else if (opcao == 3) {
                    System.out.print("Em seu lado, há algumas flechas e um arco, com seu nome cravado na empunhadura: " + jogador.getNome() + ".\n");
                    Item arco = new Item("Arco", Item.TipoItem.DISTANCIA, 5, 1);
                    jogador.getInventario().adicionarItem(arco);
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
                    jogador.getInventario().adicionarItem(new Item("Poção de Cura", Item.TipoItem.CURA, 10, 1));
                } else if (evento >= 8) {
                    System.out.println("Você encontrou um inimigo!");
                    batalhar(jogador, new Inimigo("Goblin", 40, 8, 5));
                } else {
                    System.out.println("Você caiu numa armadilha! Perdeu 10 de HP!");
                    jogador.sofrerDano(10);
                }
            }
            case 1 -> {
                //  ----------- CONTEXTO DA PROGRESSÃO ----------

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
                                System.out.println("""
                                        Você encontra uma pedra relativamente grande no chão.
                                        Atira ela para longe, o lobo escuta e vai em direção ao som.
                                        Você conseguiu destrair o lobo!""");
                            } else if (evento >= 8) {
                                System.out.println("""
                                        Você só encontra uma pequena pedra no chão.
                                        Atira ela para longe, o lobo olha, mas volta a atacar a pessoa.
                                        Você não conseguiu destrair o lobo.
                                        Sua única opção é atacar.""");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));
                            } else {
                                System.out.println("""
                                        Você não encontra nada ao seu redor.
                                        Mas enquanto procurava, sem prestar atenção pisou em um galho seco.
                                        O lobo escutou... e agora está em sua direção...
                                        Sua única opção é atacar.""");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));
                            }
                        }
                        else if (opc == 2) {
                            if (evento >= 15) {
                                System.out.println("""
                                        Você anda furtivamente para trás do lobo.
                                        Ele está muito destraído atacando a pessoa, então não sente sua presença ali.
                                        Você consegue chegar atrás do lobo.
                                        Sua única opção é atacar.""");
                                batalhar(jogador, new Inimigo("Lobo", 55, 16, 8));

                            } else if (evento >= 8) {
                                System.out.println("""
                                        Você tenta andar furtivamente para trás do lobo.
                                        Enquanto anda, sem querer, pisa em um galho seco.
                                        Mas o lobo está tão focado em atacar a pessoa, que não escuta.
                                        Você chega atrás do lobo.
                                        Sua única opção é atacar.""");
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
                        System.out.println("""
                                Você finge que não ouviu, pensa que era coisa da sua cabeça,
                                podia ser até mesmo o canto de um pássaro...
                                Então, você continua andando e...""");

                        //  ----------- ROLAGEM DE DADO ----------

                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);

                        // //  ----------- EVENTOS ----------

                        if (evento >= 15) {
                            System.out.println("Você encontrou uma poção!");
                            jogador.getInventario().adicionarItem(new Item("Poção de Cura (25)", Item.TipoItem.CURA, 25, 1));
                        } else if (evento >= 8) {
                            System.out.println("Você encontrou um inimigo!");
                            batalhar(jogador, new Inimigo("Lobo", 55, 15, 8));
                        } else {
                            System.out.println("Você caiu em um buraco! Perdeu 10 de HP!");
                            jogador.sofrerDano(10);
                        }
                        repetir = false;
                    }
                    else {
                        System.out.println("Número inválido! Retornando...\n");
                    }
                }
            }

            case 2 -> {
                if(jogador.temAliado()) {
                    System.out.println("Quando percebem que estão longe dos perigos, param para descansar.\n" +
                            jogador.getNome() + " pergunta ao homem se ele estava bem.\n" +
                            "Ele diz que estava com o braço ferido, mas que um torniquete o ajudaria.\n" +
                            jogador.getNome() + " rasga um pedaço de sua roupa e o entrega.\n" +
                            "O homem, com muita dificuldade, consegue fazer o torniquete e para o sangramento.\n" +
                            jogador.getNome() + " pergunta ao homem como ele sabia fazer aquilo\n" +
                            "e o homem responde que já tinha passado por essa situação antes.\n" +
                            jogador.getNome() + " pergunta seu nome e o que faz ali.\n" +
                            "O homem se chama André e estava ali a dois dias, mas que não sabia como chegou naquela ilha.\n" +
                            "Você percebe que André está perdido igualmente a você.\n" +
                            jogador.getNome() + " fala para seguirem juntos, serem aliados.\n" +
                            "André concorda.\n" +
                            "Está escurecendo, André fala para procurarem folhas e galhos secos e fazerem uma fogueira.\n" +
                            "Vocês fazem a fogueira e se esquentam do frio da noite,\n" +
                            "até que escutam um barulho vindo da floresta...");

                    //  ----------- ROLAGEM DE DADO ----------

                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    // //  ----------- EVENTOS ----------

                    if (evento >= 15) {
                        System.out.println("Era apenas um animal pequeno correndo entre as folhas dos arbustos.");
                    } else if (evento >= 8) {
                        System.out.println("Era uma rajada de vento forte que apagou o fogo. Perdeu 3 de HP pelo frio.");
                        jogador.sofrerDano(3);
                    } else {
                        System.out.println("Você encontrou um inimigo!");
                        batalhar(jogador, new Inimigo("Ogro", 60, 18, 10));
                    }
                }
                else{
                    System.out.println("Continua andando e encontra um lugar calmo.\n" +
                            jogador.getNome() + " fica ali para descansar e percebe que está anoitecendo.\n" +
                            "Começa a procurar folhas e galhos secos para fazer uma fogueira\n" +
                            "para se esquentar do frio da noite.\n" +
                            "Você faz a fogueira e depois de alguns minutos escuta\n" +
                            "um som vindo da floresta...\n");

                    //  ----------- ROLAGEM DE DADO ----------

                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    // //  ----------- EVENTOS ----------

                    if (evento >= 15) {
                        System.out.println("Era apenas um animal pequeno correndo entre as folhas.");
                    } else if (evento >= 8) {
                        System.out.println("Era uma... pessoa! Um homem vindo em sua direção.\n" +
                                jogador.getNome() + " se assusta, mas o homem chega o acalmando,\n" +
                                "diz que seu nome é André, que viu a claridade do fogo\n" +
                                "e sabia que era uma pessoa também.\n" +
                                jogador.getNome() + " fica desconfiado e pergunta como ele chegou naquele lugar\n" +
                                "e André responde que não sabia, simplesmente acordou naquela ilha\n" +
                                "a dois dias atrás, sem se lembrar de nada.\n" +
                                "Você percebe que André está perdido igualmente a você.\n" +
                                jogador.getNome() + " fala para seguirem juntos, serem aliados.\n" +
                                "André concorda e fica junto com " + jogador.getNome() + " naquela fogueira.\n");
                        jogador.temAliado();
                    } else {
                        System.out.println("Era uma rajada de vento forte que apagou o fogo. Perdeu 5 de HP pelo frio.");
                        jogador.sofrerDano(5);
                    }
                }
            }

            default -> {
                System.err.println("Algo deu errado na progressão!");
            }
        }
    }

    private void usarItem() {
        jogador.getInventario().listarItens();
        System.out.print("Digite o nome do item para usar: ");
        String nome = sc.nextLine();
        jogador.getInventario().getItem(nome);
    }

    public void batalhar(Personagem jogador, Inimigo inimigo) throws Exception {
        System.out.println("\n⚔️ Batalhando contra " + inimigo.getNome() + "! ⚔️");
        System.out.println("---------------------------------------------");

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            System.out.println("\n======= STATUS =======");
            System.out.println(jogador.getNome() + " (HP: " + jogador.getPontosVida() + ")");
            System.out.println(inimigo.getNome() + " (HP: " + inimigo.getPontosVida() + ")");
            System.out.println("======================");

            System.out.println("\nO que deseja fazer?");
            System.out.println("[1] - Ataque leve (alta chance, baixo dano)");
            System.out.println("[2] - Ataque forte (baixa chance, alto dano)");
            System.out.println("[3] - Usar item");
            System.out.println("[4] - Ver inventário");
            System.out.println("[5] - Desistir da luta");
            System.out.print("Escolha: ");

            int escolha = sc.nextInt();
            sc.nextLine();
            System.out.println();

            if (escolha == 5) {
                System.out.println("Você decidiu recuar da batalha!");
                return;
            }

            switch (escolha) {
                case 1 -> {
                    System.out.println("Você tenta um ataque leve...");
                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    if (evento >= 5) {
                        int dano = jogador.getAtaque() + (int) (Math.random() * 5);
                        System.out.println("Acertou! Dano causado: " + dano);
                        inimigo.sofrerDano(dano);
                    } else {
                        System.out.println("Você errou o ataque!");
                    }
                }

                case 2 -> {
                    System.out.println("Você tenta um ataque forte...");
                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    if (evento >= 12) {
                        int dano = jogador.getAtaque() + (int) (Math.random() * 15) + 5;
                        System.out.println("Golpe devastador! Dano causado: " + dano);
                        inimigo.sofrerDano(dano);
                    } else {
                        System.out.println("O ataque passou longe...");
                    }
                }

                case 3 -> usarItem();

                case 4 -> jogador.getInventario().listarItens();

                default -> System.out.println("Opção inválida!");
            }

            if (!inimigo.estaVivo()) {
                System.out.println("\n💀 " + inimigo.getNome() + " foi derrotado!");
                return;
            }

            // ======= TURNO DO INIMIGO =======
            System.out.println("\nTurno do inimigo...");
            int danoBase = inimigo.getAtaque() + (int) (Math.random() * 4); // dano variável do inimigo
            System.out.println(inimigo.getNome() + " tenta te atacar com " + danoBase + " de dano base!");

            // defesa do jogador com D20
            System.out.println("\nRole o D20 para se defender!");
            rolagem.simulacao(jogador);
            int rolagemDefesa = rolagem.rolar();
            System.out.println("RESULTADO DO D20 (defesa): " + rolagemDefesa);

            // multiplicador de bloqueio baseado na rolagem
            double percentualBloqueio;
            if (rolagemDefesa >= 15) {
                percentualBloqueio = 0.15;
            } else if (rolagemDefesa >= 8) {
                percentualBloqueio = 0.10;
            } else {
                percentualBloqueio = 0.05;
            }

            // bloqueio proporcional à defesa do jogador
            double bloqueioEfetivo = jogador.getDefesa() * percentualBloqueio;
            int danoFinal = (int) Math.max(0, danoBase - (danoBase * bloqueioEfetivo));

            jogador.sofrerDano(danoFinal);
            System.out.println("Você bloqueou " + (int) (danoBase * bloqueioEfetivo) + " de dano!");
            System.out.println("Você sofreu " + danoFinal + " de dano!");

            if (!jogador.estaVivo()) {
                jogador.tratarMorte(jogador, inimigo);
                return;
            }
        }
    }

}
