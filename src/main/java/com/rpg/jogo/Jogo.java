package com.rpg.jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.rpg.dados.RolagemDeDados;
import com.rpg.inventario.Item;
import com.rpg.personagens.Aliado;
import com.rpg.personagens.Arqueiro;
import com.rpg.personagens.Guerreiro;
import com.rpg.personagens.Inimigo;
import com.rpg.personagens.Mago;
import com.rpg.personagens.Maligno;
import com.rpg.personagens.Personagem;

public class Jogo {
    private final Scanner sc = new Scanner(System.in);
    private Personagem jogador;
    private Inimigo inimigo;
    private final RolagemDeDados rolagem = new RolagemDeDados();
    private int progressao = 0;
    public int opcao = 0;
    private Aliado aliado;

    public void iniciar() throws Exception {
        System.out.println("Bem-vindo ao RPG de Texto!");
        String nome = "";
        boolean voltar = true;
        while(voltar){
            System.out.print("Escolha seu nome: ");
            nome = sc.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println("Nome inválido! Digite novamente.");
            }
            else if(nome.equals("André") || nome.equals("Andre") || nome.equals("andré") ||
                    nome.equals("andre") || nome.equals("Maligno") || nome.equals("maligno")){
                System.out.println("Não use esse nome para o bem de nossa história :)");
            }
            else{
                voltar = false;
            }
        }
         
        
        System.out.println("\nEscolha sua classe:");
        System.out.println("[1] - Guerreiro\n[2] - Mago\n[3] - Arqueiro");
        opcao = sc.nextInt();
        sc.nextLine();

        switch (opcao) {
            case 1 -> jogador = new Guerreiro(nome);
            case 2 -> jogador = new Mago(nome);
            case 3 -> jogador = new Arqueiro(nome);
            default -> System.out.println("Opção inválida! Digite novamente.");
        }
        aliado = new Aliado("André", jogador.getPontosVida(), jogador.getAtaque(), jogador.getDefesa(), jogador.getVidaMaxima());

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
                    case 2 -> jogador.getInventario().usarItemFora(jogador, sc);
                    case 3 -> jogador.getInventario().listarItens(jogador); // criar metodo do inventario
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
        jogador.tratarMorte(jogador, aliado, inimigo, this, progressao);
    }

    public void explorar(int progressao) throws Exception {
        boolean explorando = true;

        while (explorando) {
            switch (progressao) {
                case 0 -> {
                    //  ----------- CONTEXTO DA PROGRESSÃO ----------
                    System.out.print(jogador.getNome() +
                            " abre os olhos... Não se lembra de nada. \n" +
                            "Ao olhar ao redor, tudo está escuro, mas por sorte acha uma tocha e uma pederneira.\n" +
                            "Acende a tocha e sua luz lhe trás memórias, algo urge em sua mente...\n" +
                            "Gritos, sangue, seu propósito... Mas nada está claro ainda.\n");
                    if (opcao == 1) {
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
                    Item pocaoDeCura = new Item("Poção de Cura", Item.TipoItem.CURA, 20, 1);
                    jogador.getInventario().adicionarItem(pocaoDeCura);

                    System.out.println("Leva consigo, pois algo lhe diz que vai precisar.\n" +
                            "Continua andando, buscando uma saída, até que...\n");

                    //  ----------- ROLAGEM DE DADO ----------

                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    // //  ----------- EVENTOS ----------

                    if (evento >= 15) {
                        System.out.println("Você encontrou uma poção de cura no chão!");
                        jogador.getInventario().adicionarItem(new Item("Poção de Cura", Item.TipoItem.CURA, 20, 1));
                    } else if (evento >= 8) {
                        System.out.println("Você encontrou um inimigo!");
                        batalhar(jogador, new Inimigo("Aranha Gigante", 20, 8, 5, 20));
                    } else {
                        System.out.println("Você andou com tanta confiança, que caiu numa armadilha de urso óbvia no chão! Perdeu 10 de HP!");
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
                                            Você conseguiu destrair o lobo, que deixou cair algo no chão!""");
                                    getDropItem();
                                } else if (evento >= 8) {
                                    System.out.println("""
                                            Você só encontra uma pequena pedra no chão.
                                            Atira ela para longe, o lobo olha, mas volta a atacar a pessoa.
                                            Você não conseguiu destrair o lobo.
                                            Sua única opção é atacar.""");
                                    batalhar(jogador, new Inimigo("Lobo", 40, 16, 8, 40));
                                } else {
                                    System.out.println("""
                                            Você não encontra nada ao seu redor.
                                            Mas enquanto procurava, sem prestar atenção pisou em um galho seco.
                                            O lobo escutou... e agora está em sua direção...
                                            Sua única opção é atacar.""");
                                    batalhar(jogador, new Inimigo("Lobo", 40, 16, 8, 40));
                                }
                            } else if (opc == 2) {
                                if (evento >= 15) {
                                    System.out.println("""
                                            Você anda furtivamente para trás do lobo.
                                            Ele está muito destraído atacando a pessoa, então não sente sua presença ali.
                                            Você consegue chegar atrás do lobo.
                                            Sua única opção é atacar.""");
                                    batalhar(jogador, new Inimigo("Lobo", 55, 16, 8, 55));

                                } else if (evento >= 8) {
                                    System.out.println("""
                                            Você tenta andar furtivamente para trás do lobo.
                                            Enquanto anda, sem querer, pisa em um galho seco.
                                            Mas o lobo está tão focado em atacar a pessoa, que não escuta.
                                            Você chega atrás do lobo.
                                            Sua única opção é atacar.""");
                                    batalhar(jogador, new Inimigo("Lobo", 55, 16, 8, 55));

                                } else {
                                    System.out.println("Você tenta andar furtivamente para trás do lobo.\n" +
                                            "Mas você não prestou atenção em um galho seco que estava bem na sua frente.\n" +
                                            "Pisa, o lobo escuta, e agora ele está indo em sua direção...\n" +
                                            "Sua única opção é atacar.");
                                    batalhar(jogador, new Inimigo("Lobo", 55, 16, 8, 55));
                                }
                            } else {
                                System.out.println("Número inválido! Retornando...\n");
                            }

                            System.out.println("Você vai em direção a pessoa, ajuda ela a se levantar e vocês fogem daquele lugar.");
                            jogador.setTemAliado(true);

                            repetir = false;
                        } else if (escolha == 2) {
                            System.out.println("""
                                    Você finge que não ouviu, pensa que era coisa da sua cabeça,
                                    podia ser até mesmo o canto de um pássaro...
                                    Então, você continua andando e...\n""");

                            //  ----------- ROLAGEM DE DADO ----------

                            rolagem.simulacao(jogador);
                            int evento = rolagem.rolar();
                            System.out.println("RESULTADO DO D20: " + evento);

                            // //  ----------- EVENTOS ----------

                            if (evento >= 15) {
                                System.out.println("Você encontrou uma poção!");
                                jogador.getInventario().adicionarItem(new Item("Poção de Cura", Item.TipoItem.CURA, 25, 1));
                            } else if (evento >= 8) {
                                System.out.println("Você encontrou um inimigo!");
                                batalhar(jogador, new Inimigo("Lobo", 55, 15, 8, 55));
                            } else {
                                System.out.println("Você caiu em um buraco! Perdeu 10 de HP!");
                                jogador.sofrerDano(10);
                            }
                            repetir = false;
                        } else {
                            System.out.println("Número inválido! Retornando...\n");
                        }
                    }
                }

                case 2 -> {
                    if (jogador.temAliado()) {
                        System.out.println("Quando percebem que estão longe dos perigos, param para descansar.\n" +
                                jogador.getNome() + " pergunta ao homem se ele estava bem.\n" +
                                "Ele diz que estava com o braço ferido, mas que um torniquete o ajudaria.\n");
                        Thread.sleep(3000);
                        System.out.println(jogador.getNome() + " rasga um pedaço de sua roupa e o entrega.\n" +
                                "O homem, com muita dificuldade, consegue fazer o torniquete e para o sangramento.\n" +
                                jogador.getNome() + " pergunta ao homem como ele sabia fazer aquilo\n" +
                                "e o homem responde que já tinha passado por essa situação antes.\n");
                        Thread.sleep(3000);
                        System.out.println(jogador.getNome() + " pergunta seu nome e o que faz ali.\n" +
                                "O homem se chama André e estava ali a dois dias, mas não sabia como chegou naquela ilha.\n" +
                                "Você percebe que André está perdido igualmente a você.\n" +
                                jogador.getNome() + " fala para seguirem juntos, serem aliados.\n" +
                                "André concorda.\n");
                        Thread.sleep(3000);
                        System.out.println("Está escurecendo, André fala para procurarem folhas e galhos secos e fazerem uma fogueira.\n" +
                                "Vocês fazem a fogueira e se esquentam do frio da noite,\n" +
                                "até que escutam um barulho vindo da floresta...");


                        //  ----------- ROLAGEM DE DADO ----------

                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);

                        // //  ----------- EVENTOS ----------

                        if (evento >= 14) {
                            System.out.println("""
                                    Era uma criatura baixa de meio metro, que passou correndo sem dizer nada.
                                    Ela deixou uma caixa cintilante, embrulhada as pressas, parece um presente. Você abre...""");
                                    getDropItem();
                        } else if (evento >= 10) {
                            System.out.println("Era uma rajada de vento forte que apagou o fogo. Perdeu 3 de HP pelo frio.");
                            jogador.sofrerDano(3);
                        } else {
                            System.out.println("Vocês encontraram um inimigo!");
                            aliado = new Aliado("André", 100, jogador.getAtaque(), jogador.getDefesa(), 100);
                            batalharComAliado(jogador, aliado, new Inimigo("Ogro", 60, 18, 10, 60));
                        }
                    } else {
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

                        if (evento >= 10) {
                            System.out.println("""
                                    Era uma criatura baixa de meio metro, que passou correndo sem dizer nada.
                                    Ela deixou uma caixa cintilante, embrulhada as pressas, parece um presente. Você abre...""");
                            getDropItem();
                        } else {
                            System.out.println("Era uma... pessoa! Um homem vindo em sua direção.\n" +
                                    jogador.getNome() + " se assusta, mas o homem chega o acalmando,\n" +
                                    "diz que seu nome é André, que viu a claridade do fogo\n" +
                                    "e sabia que era uma pessoa também.\n");
                            Thread.sleep(2500);
                            System.out.println(jogador.getNome() + " fica desconfiado e pergunta como ele chegou naquele lugar\n" +
                                    "e André responde que não sabia, simplesmente acordou naquela ilha\n" +
                                    "a dois dias atrás, sem se lembrar de nada.\n" +
                                    "Você percebe que André está perdido igualmente a você.\n" +
                                    jogador.getNome() + " fala para seguirem juntos, serem aliados.\n" +
                                    "André concorda e fica junto com " + jogador.getNome() + " naquela fogueira.\n");
                            jogador.setTemAliado(true);
                        }
                    }
                }

                case 3 -> {
                    //  ----------- CONTEXTO DA PROGRESSÃO ----------

                    if (jogador.temAliado()) {
                        System.out.println("""
                                    A noite passou, e você acorda
                                    André já estava ao seu lado afiando sua adaga na brasa da fogueira
                                    [ANDRÉ] >> Já acordou? Estive sem sono esta noite, então dei uma vasculhada ao redor...
                                    [ANDRÉ] >> Devemos seguir ao norte, há uma escuridão assolando a praia, e me parece que está vindo até nós
                                    Vocês empacotam suas coisas e seguem uma trilha marcada por desgaste.""");

                        //  ----------- ROLAGEM DE DADO ----------

                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);

                        // //  ----------- EVENTOS ----------

                        System.out.println("""
                                A trilha ao norte os-leva a uma montanha cercada por uma floresta densa
                                Onde acham um acampamento abandonado, com caixas quebradas ao chão
                                Vocês vasculham e...""");

                        if (evento >= 15) {
                            if (opcao == 1) {
                                System.out.println("Encontram um Escudo!");
                                jogador.getInventario().adicionarItem(new Item("Escudo", Item.TipoItem.EQUIPAVEL, 10, 1));
                            }
                            if (opcao == 2) {
                                System.out.println("Encontram um Amuleto de Defesa");
                                jogador.getInventario().adicionarItem(new Item("Amuleto de defesa", Item.TipoItem.EQUIPAVEL, 10, 1));
                            }
                            if (opcao == 3) {
                                System.out.println("Encontram uma Flecha Incendiária!");
                                jogador.getInventario().adicionarItem(new Item("Flecha Incendiária", Item.TipoItem.ATIRAVEL, 15, 1));
                            }
                        } else if (evento >= 5) {
                            System.out.println("""
                                tum... tum... TUM... TUM...
                                Passos de algo imenso se aproxima com muita raiva""");
                            aliado = new Aliado("André", 100, jogador.getAtaque(), jogador.getDefesa(), 100);
                            batalharComAliado(jogador, aliado, new Inimigo("Golem", 65, 20, 15, 65));
                        } else {
                            System.out.println(jogador.getNome() + " Se aproxima de umas caixas e... É PEGO EM UMA ARMADILHA, (-10 de HP)");
                            jogador.sofrerDano(10);
                            System.out.println("Por sorte André estava lá para solta-lo");
                        }
                    } else {
                        System.out.println("A noite passou...\n" +
                                jogador.getNome() + " acorda e segue seu caminho,\n" +
                                "até que percebe algo estranho...\n");
                        Thread.sleep(3000);
                        System.out.println("Fumaça! Uma pessoa talvez. Uma esperança de ajuda.\n" +
                                jogador.getNome() + " segue o rastro e, chegando mais perto, enxerga sua esperança.\n" +
                                "É mesmo uma pessoa, que tinha acabado de apagar sua fogueira.\n" +
                                jogador.getNome() + " acena, a pessoa se assusta, mas você o acalma,\n" +
                                "diz que está perdido e que seguiu o rastro de fumaça.\n");
                        Thread.sleep(3000);
                        System.out.println("O homem, fica aliviado, diz que achava que estava sozinho naquela ilha.\n" +
                                jogador.getNome() + " pergunta seu nome e o homem diz que se chamava André\n" +
                                "e acordou naquela ilha a dois dias atrás sem se lembrar de nada.\n" +
                                "André parecia desesperado, fugindo de algo desconhecido, assim como você\n");
                        Thread.sleep(3000);
                        System.out.println(jogador.getNome() + " fala para seguirem juntos, serem aliados.\n" +
                                "André concorda, e diz para se apressarem ao norte, algo de ruim os assolaria ao sul da ilha.\n" +
                                "Vocês seguem juntos em direção a uma montanha, por uma trilha desgastada.\n");
                        jogador.setTemAliado(true);
                        System.out.println("Vocês continuam caminhando, até que...");

                        //  ----------- ROLAGEM DE DADO ----------

                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);

                        // //  ----------- EVENTOS ----------

                        System.out.println("""
                                A trilha ao norte os-leva a uma montanha cercada por uma floresta densa
                                Onde acham um acampamento abandonado, com caixas quebradas ao chão
                                Vocês vasculham e...""");

                        if (evento >= 15) {
                            if (opcao == 1) {
                                System.out.println("Encontram um Escudo!");
                                jogador.getInventario().adicionarItem(new Item("Escudo", Item.TipoItem.EQUIPAVEL, 10, 1));
                            }
                            if (opcao == 2) {
                                System.out.println("Encontram um Amuleto de Defesa");
                                jogador.getInventario().adicionarItem(new Item("Amuleto de defesa", Item.TipoItem.EQUIPAVEL, 10, 1));
                            }
                            if (opcao == 3) {
                                System.out.println("Encontram uma Flecha Incendiária!");
                                jogador.getInventario().adicionarItem(new Item("Flecha Incendiária", Item.TipoItem.ATIRAVEL, 15, 1));
                            }
                        } else if (evento >= 5) {
                            System.out.println("""
                                tum... tum... TUM... TUM...
                                Passos de algo imenso se aproxima com muita raiva""");
                            aliado = new Aliado("André", 100, jogador.getAtaque(), jogador.getDefesa(), 100);
                            batalharComAliado(jogador, aliado, new Inimigo("Golem", 65, 20, 15, 65));
                        } else {
                            System.out.println(jogador.getNome() + " Se aproxima de umas caixas e... É PEGO EM UMA ARMADILHA, (-10 de HP)");
                            jogador.sofrerDano(10);
                            System.out.println("Por sorte André estava lá para solta-lo");
                        }
                    }
                }

                case 4 -> {
                    // ----------- CONTEXTO DA PROGRESSÃO ----------
                    System.out.println("""
                            A manhã nasce fria e enevoada. André caminha à frente, calado.
                            Vocês sobem uma trilha estreita que serpenteia a montanha, até que do alto,
                            avistam um vilarejo coberto por neblina, escondido entre vales e árvores antigas.
                            [ANDRÉ] >> Um vilarejo... talvez encontremos respostas lá.
                            """);
                    Thread.sleep(3000);
                    System.out.println("""
                            Ao chegarem ao vilarejo, há sons de martelos, vozes, e cheiro de pão fresco.
                            Pessoas sorriem, há crianças correndo e comerciantes chamando pelos produtos.
                            """);
                    Thread.sleep(2500);
                    System.out.print("   [1] - Explorar o comércio local\n" +
                            "   [2] - Interagir com as crianças\n" +
                            "   [3] - Procurar alguma taverna\n" +
                            "Escolha: ");

                    int escolha = sc.nextInt();
                    sc.nextLine();

                    switch (escolha) {
                        case 1 -> {
                            System.out.println("""
                                Você caminha até um ferreiro de barba longa.
                                [FERREIRO] >> Estrangeiro, hein? Você tem cara de quem precisa de algo forte.
                                Ele te mostra uma caixa de itens antigos...
                                """);
                            rolagem.simulacao(jogador);
                            int evento = rolagem.rolar();
                            System.out.println("RESULTADO DO D20: " + evento);
                            if (evento >= 15) {
                                System.out.println("Um ferreiro te deu 2 facas de arremesso como presente de boas-vindas!");
                                jogador.getInventario().adicionarItem(new Item("Faca de Arremesso", Item.TipoItem.ATIRAVEL, 12, 1));
                            } else if (evento >= 8) {
                                System.out.println("Você consegue barganhar uns pãezinhos de amostra grátis.");
                                jogador.getInventario().adicionarItem(new Item("Pãezinhos", Item.TipoItem.CURA, 5, 3));
                            } else {
                                System.out.println("Você tropeça numa pilha de ferro velho e o ferreiro ri de você. Nada encontrado.");
                            }
                        }
                        case 2 -> {
                            System.out.println("""
                                    Você se aproxima de um grupo de crianças brincando com pedras coloridas.
                                    Uma delas te desafia a acertar o alvo mais distante.
                                    """);
                            rolagem.simulacao(jogador);
                            int evento = rolagem.rolar();
                            System.out.println("RESULTADO DO D20: " + evento);
                            if (evento >= 15) {
                                System.out.println("""
                                        Você acerta o alvo com precisão!
                                        As crianças gritam empolgadas e te dão uma pedrinha brilhante.
                                        """);
                                jogador.getInventario().adicionarItem(new Item("Pedrinha Brilhante", Item.TipoItem.ATIRAVEL, 8, 1));
                            } else if (evento >= 8) {
                                System.out.println("Quase acerta! As crianças riem, mas te entregam uma flor como prêmio de consolação.");
                            } else {
                                System.out.println("Você erra feio e uma criança debocha: 'Aposto que nem sabe segurar uma pedra!'");
                            }
                        }
                        case 3 -> {
                            System.out.println("""
                                    Você entra em uma taverna lotada, o cheiro de bebida forte e lenha queimada preenche o ar.
                                    O taverneiro te encara e diz:
                                    [TAVERNEIRO] >> Forasteiros... há tempos não vemos nenhum. Cuidado por onde andam à noite.
                                    """);
                            rolagem.simulacao(jogador);
                            int evento = rolagem.rolar();
                            System.out.println("RESULTADO DO D20: " + evento);
                            if (evento >= 10) {
                                System.out.println("Você conversa com um velho bêbado que fala sobre uma 'bruxa da cabana negra' nas colinas da aldeia...");
                            } else {
                                System.out.println("Você ouve murmúrios, mas nada de útil.");
                            }
                        }
                        default -> System.out.println("Você prefere apenas observar o vilarejo e seguir André.");
                    }
                    Thread.sleep(2500);
                    System.out.println("""
                            Após algum tempo explorando, algo chama sua atenção.
                            Um sussurro distante, quase inaudível, ecoa de uma cabana isolada no alto do vilarejo.
                            André te olha e franze a testa.
                            [ANDRÉ] >> Eu... vou esperar aqui fora. Algo nesse lugar me incomoda
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            Você entra na cabana. O ar é pesado, cheio de fumaça e cheiro de ervas queimadas.
                            Uma mulher encapuzada surge das sombras.
                            [BRUXA] >> Você... não deveria estar vivo.
                            Ela parece surpresa.""");
                    Thread.sleep(2500);
                    System.out.println("""
                           [BRUXA] >> Eu senti quando você caiu na caverna. E também... quando enfrentou o MALIGNO.
                           [BRUXA] >> Sua missão ainda não acabou. Há algo obscuro em seu caminho... algo próximo.
                           """);
                    Thread.sleep(2500);
                    System.out.println("""
                            De repente, o chão treme. Do lado de fora, ouvem-se gritos e o som de asas batendo no ar.
                            A bruxa olha pela janela — chamas azuis tomam o vilarejo.
                            [BRUXA] >> Um dragão espectral! Corra! Não... lute!
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            A bruxa empunha um cajado antigo e se coloca ao seu lado.
                            [BRUXA] >> Primeira fase. Que os ventos antigos nos protejam!
                            """);
                    Thread.sleep(3000);
                    aliado = new Aliado("Bruxa", 85, 20, 20, 85);
                    batalharComAliado(jogador, aliado, new Inimigo("Dragão Espectral", 130, 28, 20, 130));

                    System.out.println("""
                        As chamas se apagam lentamente, restando apenas cinzas e gritos de desespero.
                        A bruxa, ofegante, olha para você.
                        [BRUXA] >> Ele está próximo... o mal que carrega... está entre vocês.
                        Você olha para fora — André não está mais lá.
                        """);
                    Thread.sleep(3000);
                }

                case 5 -> {
                    // ----------- CONTEXTO DA PROGRESSÃO ----------
                    System.out.println("""
                        Vocês seguem procurando por sobreviventes e André.
                        Nenhum rastro. Nenhum sinal.
                        André simplesmente... desapareceu.
                        """);
                    Thread.sleep(3000);
                    System.out.println("""
                        Pelas ruas do vilarejo, vocês o procuram em meio ao caos.
                        As casas estão destruídas, e uma multidão tenta conter o fogo
                        e salvar o que restou dos destroços deixados pelo Dragão.
                        """);
                    Thread.sleep(3000);
                    System.out.println("""
                        De repente, uma criança corre até vocês, ofegante.
                        [CRIANÇA] >> Eu... eu vi ele! O homem que vocês procuram! Ele entrou lá...
                        Ela aponta para a cabana da bruxa.
                        """);
                    Thread.sleep(3000);
                    System.out.println(jogador.getNome() + """ 
                         olha para a Bruxa, que permanece imóvel por um instante.
                        [BRUXA] >> MALIGNO está a caminho...
                        O silêncio que se segue é pesado.
                        Mas vão em direção a cabana.
                        """);
                    Thread.sleep(3000);
                    System.out.println(""" 
                        Ao chegarem, entram e André está lá, sentado em uma cadeira de madeira,
                        com as mãos cobrindo as orelhas e os olhos arregalados de medo.
                        [ANDRÉ] >> Me desculpem! Eu estava com muito medo!
                        Você o acalma, diz que já passou.
                        A Bruxa está prestando atenção em outra coisa, parece estar sentindo
                        que tem algo lá fora ainda...
                        """);
                    Thread.sleep(3000);
                    System.out.println("""
                           Ela segue em direção a porta e você vai atrás. De repente...
                           Surge, de trás da cabana, uma criatura colossal.
                           Um ser com cabeça de touro e chifres afiados como lâminas.
                           Era um Minotauro em extrema fúria.
                           """);
                    Thread.sleep(3000);
                    System.out.println("""
                           Você olha pra Bruxa.
                           [BRUXA] >> Fase dois...
                           Você volta seu olhar à criatura e empunha, com mãos trêmulas, sua arma.
                           """);
                    aliado = new Aliado("Bruxa", aliado.getPontosVida(), 20, 20, 85);
                    batalharComAliado(jogador, aliado, new Inimigo("Minotauro", 135, 30, 25, 135));

                    System.out.println("""
                        No meio da batalha, o Minotauro dá alguns passos para trás e, de repente, desvia o olhar para a cabana.
                        Vocês se entreolham, confusos.
                        Sem aviso, ele dispara em direção a ela, finca o machado no chão e, com uma força brutal, ergue a cabana inteira.
                        Sob os destroços, ele encontra André — imóvel, exatamente como o deixaram.
                        """);
                    Thread.sleep(3000);
                    System.out.println("""
                        O Minotauro o agarra com uma das mãos, como se fosse nada diante de seu tamanho,
                        e, sem olhar para trás, desaparece entre as árvores, engolido pela escuridão.
                        [BRUXA] >> Minha cabana!!!
                        Ela caminha até a cabana destruída e pega algumas coisas que sobreviveram à destruição e te encara.
                        [BRUXA] >> Olha rapaz, você não sabe o que acontece aqui...
                        Ela estende a mão e te entrega um pequeno amuleto, frio ao toque.
                        """);
                    jogador.getInventario().adicionarItem(new Item("Amuleto de Proteção", Item.TipoItem.EQUIPAVEL, 15, 1));
                    Thread.sleep(3000);
                    System.out.println("""                       
                        Antes que ela possa dizer mais, gritos estridentes ecoam pela floresta.
                        [BRUXA] >> Não vá.
                        Ela diz com um olhar cortante.
                        Você a ignora e corre em direção aos gritos que rasgam a escuridão da floresta.
                        """);

                }
                case 6 -> {
                    // ----------- CONTEXTO DA PROGRESSÃO ----------
                    System.out.println("""
                            A floresta está fria e silenciosa.
                            O amuleto da bruxa brilha fraco, guiando você até vozes distantes.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            Você se aproxima e vê um acampamento da Guilda dos Magos Negros.
                            No centro do círculo mágico... André, preso e gritando.
                            """);
                    Thread.sleep(2000);
                    String[] falasCorrompidas = {
                            "[MAGOS NEGROS] >> ʍɨʐʐɨɴɢ ȶɦɛ ɛռɛཞɠʏ...",
                            "[MAGOS NEGROS] >> ᚹᛁᛋᛋᛁᚾᚷ ᚨᚾᛞ ᛗᚨᚷᛁᚲ...",
                            "[MAGOS NEGROS] >> ✶ ☍ ☌ Ϟ ϟ Ϯ ᚾᚨᛗᛖ ᚣᚩᚢ..."
                    };
                    for (String f : falasCorrompidas) {
                        System.out.print("\r" + f);
                        Thread.sleep(600);
                    }
                    System.out.println("\n");

                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    System.out.println("Um vento sombrio e corvos começam a rodiar " + jogador.getNome());
                    System.out.println("De repente tudo em sua volta escurece, " + jogador.getNome() + " olha em sua volta, até que sente um sussuro em sua retaguarda...");
                    System.out.println("[MAGO NEGRO] >> Espiando, forasteiro?");
                    Thread.sleep(1500);

                    // ----------- BATALHA ----------
                    batalhar(jogador, new Inimigo("Mago Negro", 85, 24, 15, 85));

                    System.out.println("O mago cai em fumaça escura. Você corre até André.");
                    Thread.sleep(2000);
                    System.out.println("[ANDRÉ] >> Saia daqui... AGORA!");
                    Thread.sleep(2000);
                    System.out.println("Você tenta atravessar a barreira mágica...");
                    rolagem.simulacao(jogador);
                    int resultado = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + resultado);

                    if (resultado >= 15) {
                        System.out.println("Você atravessa parte da barreira, mas é atingido por uma magia e cai inconsciente.");
                    } else if (resultado >= 8) {
                        System.out.println("A energia te repele com força. Antes de reagir, é imobilizado por trás.");
                    } else {
                        System.out.println("A barreira te lança longe. Sua visão escurece.");
                    }

                    Thread.sleep(2000);
                    System.out.println("[ANDRÉ] >> Que... tolisse...");
                }
                case 7 -> {
                    System.out.println("""
                            Você desperta... suspenso em meio a um vazio pulsante.
                            As cores não fazem sentido. O chão... se move como água.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            Ecos de vozes se repetem ao seu redor — algumas familiares, outras completamente distorcidas.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            [VOZ DISTORCIDA] >> ...o poder adormecido... desperte... desperte...
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            A fenda entre mundos... foi aberta.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            Você vê, à distância, uma figura cercada por magos ajoelhados.
                            O corpo de André... agora tomado por energia negra.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            [MAGOS CORROMPIDOS] >> MΛLIGNO... MΛLIGNO... MΛLIGNO...
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            Antes que possa se aproximar, algo rasteja das sombras da fenda.
                            [???] >> ̷S̶i̶l̴ê̶n̶c̸i̴o̵.̶.̴.̸
                            """);
                    Thread.sleep(1500);

                    System.out.println("Um inimigo surge do reflexo distorcido da fenda!");
                    batalhar(jogador, new Inimigo("Sombra do Eco", 50, 18, 10, 60));

                    System.out.println("""
                            A sombra se desfaz em fragmentos luminosos.
                            O amuleto em seu pescoço começa a flutuar e pulsar uma luz forte.
                            Ao seu lado, uma presença surge em forma de Eco — a Bruxa.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            [BRUXA DO ECO] >> Você não devia estar aqui, tolo... a fenda consome os vivos.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            [VOCÊ] >> André... ele... abriu isso?
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            [BRUXA DO ECO] >> André não existe mais. O que você viu é o Arqui-Mago Maligno...
                            O criador deste e outros feitiços proibidos. E se ele notar sua presença, será tarde demais.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            O solo vibra. Do horizonte partido surge um dragão negro com olhos ocos.
                            [BRUXA DO ECO] >> Nidhogg... o guardião da passagem. Prepare-se!
                            """);
                    Thread.sleep(2000);

                    aliado = new Aliado("Bruxa Do Eco", 85, 40, 20, 85);
                    batalharComAliado(jogador, aliado, new Inimigo("Nidhogg, Guardião da Fenda", 200, 30, 0, 100));

                    System.out.println("""
                            Nidhogg ruge e se desfaz em névoa escura, deixando uma pedra pulsante no chão.
                            [BRUXA DO ECO] >> Pegue isso. É a Essência da Fenda... talvez possamos usá-la contra ele.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            Você sente o poder da fenda pulsar dentro de si. A bruxa o encara.
                            [BRUXA DO ECO] >> Há uma saída... mas também um caminho direto ao coração da fenda.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            [VOCÊ] >> E se eu quiser acabar com tudo isso?
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            [BRUXA DO ECO] >> Então siga a luz invertida... ela o levará até Maligno.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            A fenda se abre mais uma vez. Você dá um passo à frente... e tudo se dissolve em branco.
                            """);
                    Thread.sleep(2000);

                    System.out.println("Algo em seu bolso é absorvido no clarão: Essência da Fenda (+10 de Ataque / +10 de Defesa)");
                    jogador.setAtaque(jogador.getAtaque() + 10);
                    jogador.setDefesa(jogador.getDefesa() + 10);
                }
                case 8 -> {
                    System.out.println("""
                            Sua visão se apaga... e flashes surgem.
                            Um campo em chamas. Ecos de uma antiga batalha.
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            Uma figura surge — capa rasgada, olhos vermelhos.
                            [???] >> Você... ousou me desafiar.
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            [VOCÊ] >> Eu... te conheço?
                            """);
                    Thread.sleep(1200);
                    System.out.println("""
                            [???] >> Já conheceu. Você me derrotou uma vez... e pagou o preço.
                            """);
                    Thread.sleep(1800);

                    System.out.println("""
                            As memórias voltam. Era ele. O mesmo inimigo do início.
                            O verdadeiro Maligno.
                            """);
                    Thread.sleep(1800);

                    System.out.println("""
                            [MALIGNO] >> Apaguei quem você era. Tomei sua magia... e seu nome.
                            """);
                    Thread.sleep(1800);

                    System.out.println("""
                            A imagem muda.
                            Maligno invade uma cabana — a da Bruxa.
                            Frascos quebrados, sombras gritam.
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            [MALIGNO] >> Feitiço de renascimento precisa de sangue e esquecimento...
                            E ela tem ambos.
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            O sorriso dele é calmo demais pra quem destrói tudo ao redor.
                            """);
                    Thread.sleep(1200);

                    System.out.println("""
                            Um símbolo surge na memória, brilhando no chão — o mesmo da Essência da Fenda.
                            O chão começa a tremer...
                            """);
                    Thread.sleep(1500);

                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);
                    Thread.sleep(1000);

                    if (evento >= 8) {
                        System.out.println("""
                                O símbolo reage à sua presença!
                                Uma energia estranha te envolve...
                                """);
                        Thread.sleep(1200);
                        getDropItem();
                        jogador.setAtaque(jogador.getAtaque() + 5);
                        System.out.println("Você sente um poder obscuro te fortalecendo (+5 Ataque).");
                        jogador.restaurarVidaTotal();
                    } else {
                        System.out.println("""
                                A energia explode — você sente uma queimação interna.
                                Sua vida foi totalmente restaurada
                                """);
                        Thread.sleep(1200);
                        jogador.restaurarVidaTotal();
                    }

                    System.out.println("""
                            Tudo começa a girar. O chão se desfaz... a memória colapsa.
                            """);
                    Thread.sleep(1500);

                    System.out.println("""
                            Vozes sussurram ao redor...
                            [BRUXA] >> Acorde... rápido...
                            """);
                    Thread.sleep(1500);

                    System.out.println("""
                            Sua visão volta.
                            Você está sentado em uma mesa de jantar de pedra negra.
                            Correntes prendem seus braços.
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            À sua frente, taças, cheias de um líquido vermelho, brilham.
                            A Bruxa está ao seu lado — também amarrada.
                            """);
                    Thread.sleep(1500);
                    System.out.println("""
                            [MALIGNO] >> Bem-vindos ao banquete da verdade.
                            Vocês lutam, sangram, amam... e acham que isso tem sentido?
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            Ele levanta uma taça, sorrindo.
                            [MALIGNO] >> Comam. É o último jantar antes do fim.
                            """);
                    Thread.sleep(2000);

                    System.out.println("""
                            Você sente o ar ficar pesado.
                            As correntes vibram.
                            A Bruxa te olha.
                            [BRUXA] >> Fase três... E última.
                            """);
                }

                case 9 -> {
                    System.out.println("""
                            VUOSH! Um vendaval ensandecido arrebenta as janelas,
                            trazendo uma nuvem negra com raios fulminantes
                            e trovões ensurdecedores que os cobrem por completo.
                            """);
                    Thread.sleep(3000);
                    System.out.println("""
                            Vocês escutam ecoando pela escuridão...
                            [MALIGNO] >> HA HA HA! QUE COMEÇE A DIVERSÃO!!!
                            """);
                    Thread.sleep(2500);
                    System.out.println("""     
                            Vocês sentem que estão flutuando, como se não existisse gravidade.
                            As correntes começam a esquentar como lava fervente.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""  
                            Até que aquele manto sombrio se dispersa violentamente,
                            formando um vórtice.
                            Vocês estão bem no centro, presos, sem saída.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""  
                            As correntes se quebram.
                            Vocês olham aquela muralha de vento densa, ameaçadora.
                            Vocês se sentem pequenos, perdidos, cercados por aquela força.
                            Então, algo atravessa a barreira... e se aproxima.
                            """);
                    Thread.sleep(3000);
                    System.out.println("[MALIGNO] >> Sejam muito bem-vindos ao meu lugar favorito...");
                    Thread.sleep(1500);
                    System.out.println("[MALIGNO] >> ARENA DO CAOS!");
                    Thread.sleep(1000);
                    System.out.println("[MALIGNO] >> HA HA HA!");
                    Thread.sleep(2000);
                    System.out.println(jogador.getNome() + """
                             olha para a Bruxa.
                            Vocês trocam um aceno curto, carregado de confiança, e avançam juntos contra MALIGNO.
                            """);
                    Thread.sleep(1500);

                    // CHAMADA DA BATALHA FINAL

                    Item pocaoDeCuraReforcada = new Item("Poção de Cura Reforçada", Item.TipoItem.CURA, 35, 3);
                    jogador.getInventario().adicionarItem(pocaoDeCuraReforcada);
                    
                    aliado = new Aliado("Bruxa", 85, 40, 20, 85);
                    Maligno maligno = new Maligno("Maligno");
                    batalhaFinal(jogador, aliado, maligno);

                    // ENCERRAMENTO
                    System.out.println("""
                            A arena silencia.
                            O chão rachado começa a se desfazer em poeira brilhante, subindo lentamente ao céu.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            A Bruxa se aproxima, ofegante, apoiando-se no cajado.
                            [BRUXA] >> Acabou... por enquanto.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            O corpo de Maligno começa a se dissolver, deixando apenas um fragmento negro pulsante.
                            Você o encara, e sente algo... vivo... dentro dele.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            [BRUXA] >> Não toque nisso! Esse poder... é o mesmo que ele te tirou.
                            """);
                    Thread.sleep(2000);
                    System.out.println("""
                            A luz da fenda consome tudo ao redor.
                            Você sente seu corpo flutuar, o som se distorce, e a voz da Bruxa ecoa distante...
                            [BRUXA] >> ...lembre-se, o esquecimento também é uma prisão...
                            """);
                    Thread.sleep(3000);
                    System.out.println("""
                            Tudo escurece.
                            Quando abre os olhos, está sozinho, em uma floresta calma.
                            O fragmento negro repousa em sua mão... ainda pulsando.
                            """);
                    Thread.sleep(2500);
                    System.out.println("""
                            [VOZ DISTANTE] >> ...não acabou... ele ainda vive... nas sombras...
                            """);
                    Thread.sleep(2500);
                    System.out.print("FIM");
                    for (int i = 0; i < 3; i++) {
                        try {
                            Thread.sleep(500);
                            System.out.print(".");
                        } catch (Exception erro) {}
                    }
                    Thread.sleep(2000);
                    
                }

                default -> System.err.println("Algo deu errado na progressão!");

            }
            explorando = false;
        }
    }

    public void batalhar(Personagem jogador, Inimigo inimigo) throws Exception {
        boolean desistiu = false;
        jogador.setEmBatalha(true);

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

            boolean jogadorAtacou = false;

            switch (escolha) {
                case 1 -> {
                    jogadorAtacou = true;
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
                    jogadorAtacou = true;
                    System.out.println("Você tenta um ataque forte...");
                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    if (evento >= 10) {
                        int dano = jogador.getAtaque() + (int) (Math.random() * 10) + 5;
                        System.out.println("Golpe devastador! Dano causado: " + dano);
                        inimigo.sofrerDano(dano);
                    } else {
                        System.out.println("O ataque passou longe...");
                    }
                }

                case 3 -> jogador.getInventario().usarItemEmBatalha(jogador, inimigo, sc);
                case 4 -> jogador.getInventario().listarItens(jogador);

                case 5 -> {
                    while (!desistiu) {
                        System.out.println("Você tenta sair da luta...");
                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);
                        if (evento >= 17) {
                            System.out.println("Você foge, deixando " + inimigo.getNome() + " para trás.");
                        }
                        else{
                            System.out.println("Você não consegue fugir e toma um golpe forte.");
                            jogador.sofrerDano(20);
                        }
                        desistiu = true;
                    }
                }

                default -> System.out.println("Opção inválida!");
            }

            // só roda o turno do inimigo se o jogador realmente atacou
            if (jogadorAtacou) {
                if (!inimigo.estaVivo()) {
                    System.out.println("\n💀 " + inimigo.getNome() + " foi derrotado!");
                    getDropItem();
                    System.out.println("\n-------------- ⚔️ Fim da Batalha ⚔️--------------\n");
                    return;
                }

                System.out.println("\nTurno do inimigo...");
                int danoBase = inimigo.getAtaque() + (int) (Math.random() * 4);
                System.out.println(inimigo.getNome() + " tenta te atacar com " + danoBase + " de dano base!");

                System.out.println("\nRole o D20 para se defender!");
                rolagem.simulacao(jogador);
                int rolagemDefesa = rolagem.rolar();
                System.out.println("RESULTADO DO D20 (defesa): " + rolagemDefesa);

                double percentualBloqueio;
                if (rolagemDefesa >= 15) {
                    percentualBloqueio = 0.15;
                } else if (rolagemDefesa >= 8) {
                    percentualBloqueio = 0.10;
                } else {
                    percentualBloqueio = 0.05;
                }

                // defesa aumenta o percentual de bloqueio, mas de forma controlada
                double bloqueioEfetivo = percentualBloqueio + (jogador.getDefesa() / 100.0);

                // nunca pode bloquear mais de 90%
                bloqueioEfetivo = Math.min(bloqueioEfetivo, 0.9);

                int danoFinal = (int) Math.max(0, danoBase * (1 - bloqueioEfetivo));
                int danoBloqueado = danoBase - danoFinal;

                jogador.sofrerDano(danoFinal);
                System.out.println("Você bloqueou " + danoBloqueado + " de dano!");
                System.out.println("Você sofreu " + danoFinal + " de dano!");

                if (!jogador.estaVivo()) {
                    jogador.tratarMorte(jogador, aliado, inimigo, this, progressao);
                    return;
                }
            }
        }
        jogador.setEmBatalha(false);
    }

    public void batalharComAliado(Personagem jogador, Aliado aliado, Inimigo inimigo) throws Exception {
        boolean desistiu = false;
        jogador.setEmBatalha(true);

        System.out.println("\n⚔️ Batalhando contra " + inimigo.getNome() + "! ⚔️");
        System.out.println("---------------------------------------------");

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            if(inimigo.getNome().equals("Minotauro") && inimigo.getPontosVida() <= 50) {
                jogador.setEmBatalha(false);
            }
            System.out.println("\n======= STATUS =======");
            System.out.println(jogador.getNome() + " (HP: " + jogador.getPontosVida() + ")");
            System.out.println(aliado.getNome() + " (HP: " + aliado.getPontosVida() + ")");
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

            boolean jogadorAtacou = false;

            switch (escolha) {
                case 1 -> {
                    jogadorAtacou = true;
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
                    jogadorAtacou = true;
                    System.out.println("Você tenta um ataque forte...");
                    rolagem.simulacao(jogador);
                    int evento = rolagem.rolar();
                    System.out.println("RESULTADO DO D20: " + evento);

                    if (evento >= 10) {
                        int dano = jogador.getAtaque() + (int) (Math.random() * 10) + 5;
                        System.out.println("Golpe devastador! Dano causado: " + dano);
                        inimigo.sofrerDano(dano);
                    } else {
                        System.out.println("O ataque passou longe...");
                    }
                }

                case 3 -> jogador.getInventario().usarItemEmBatalha(jogador, inimigo, sc);
                case 4 -> jogador.getInventario().listarItens(jogador);

                case 5 -> {
                    while (!desistiu) {
                        System.out.println("Você tenta sair da luta...");
                        rolagem.simulacao(jogador);
                        int evento = rolagem.rolar();
                        System.out.println("RESULTADO DO D20: " + evento);
                        if (evento >= 17) {
                            System.out.println("Você foge, deixando " + inimigo.getNome() + " para trás.");
                        }
                        else{
                            System.out.println("Você não consegue fugir e toma um golpe forte.");
                            jogador.sofrerDano(20);
                        }
                        desistiu = true;
                    }
                }

                default -> System.out.println("Opção inválida!");
            }

            // só roda o turno do inimigo e do aliado se o jogador realmente atacou
            if (jogadorAtacou) {
                // --- TURNO DO ALIADO ---

                if (aliado.estaVivo() && inimigo.estaVivo()) {
                    System.out.println("\nTurno de " + aliado.getNome() + "!");
                    int eventoAliado = rolagem.rolar();
                    System.out.println("RESULTADO DO D20 (aliado): " + eventoAliado);

                    if (eventoAliado >= 6) { // chance razoável de acertar
                        int danoAliado = aliado.getAtaque() + (int) (Math.random() * 6);
                        System.out.println(aliado.getNome() + " ataca causando " + danoAliado + " de dano!");
                        inimigo.sofrerDano(danoAliado);
                    } else {
                        System.out.println(aliado.getNome() + " errou o ataque!");
                    }
                }

                if (!inimigo.estaVivo()) {
                    System.out.println("\n💀 " + inimigo.getNome() + " foi derrotado!");
                    getDropItem();
                    System.out.println("\n-------------- ⚔️ Fim da Batalha ⚔️--------------\n");
                    return;
                }

                System.out.println("\nTurno do inimigo...");
                int danoBase = inimigo.getAtaque() + (int) (Math.random() * 4);

                // inimigo escolhe aleatoriamente quem atacar
                boolean atacarJogador = Math.random() < 0.6; // 60% chance de atacar o jogador, 40% o aliado
                Personagem alvo = atacarJogador ? jogador : aliado;

                System.out.println(inimigo.getNome() + " tenta atacar " + alvo.getNome() + " com " + danoBase + " de dano base!");


                if (alvo == jogador) {
                    // --- defesa do jogador ---
                    System.out.println("\nRole o D20 para se defender!");
                    rolagem.simulacao(jogador);
                    int rolagemDefesa = rolagem.rolar();
                    System.out.println("RESULTADO DO D20 (defesa): " + rolagemDefesa);

                    double percentualBloqueio;
                    if (rolagemDefesa >= 15) percentualBloqueio = 0.15;
                    else if (rolagemDefesa >= 8) percentualBloqueio = 0.10;
                    else percentualBloqueio = 0.05;

                    double bloqueioEfetivo = percentualBloqueio + (jogador.getDefesa() / 100.0);
                    bloqueioEfetivo = Math.min(bloqueioEfetivo, 0.9);

                    int danoFinal = (int) Math.max(0, danoBase * (1 - bloqueioEfetivo));
                    int danoBloqueado = danoBase - danoFinal;

                    jogador.sofrerDano(danoFinal);
                    System.out.println("Você bloqueou " + danoBloqueado + " de dano!");
                    System.out.println("Você sofreu " + danoFinal + " de dano!");

                    if (!jogador.estaVivo()) {
                        jogador.tratarMorte(jogador, aliado, inimigo, this, progressao);
                        return;
                    }

                } else {
                    // --- aliado não tem defesa ---
                    double variacao = 0.9 + Math.random() * 0.2; // varia entre 90% e 110% do dano base
                    int danoFinal = (int) (danoBase * variacao * 0.4); // sofre só 60% do dano

                    if (Math.random() < 0.3) { // 20% de chance de esquivar
                        System.out.println(aliado.getNome() + " esquivou do ataque com agilidade!");
                    } else {
                        aliado.sofrerDano(danoFinal);
                        System.out.println(alvo.getNome() + " não conseguiu se defender e sofreu " + danoFinal + " de dano!");
                    }

                    if (!aliado.estaVivo()) {
                        System.out.println(aliado.getNome() + " caiu em batalha!");
                    }
                }
            }
        }
        jogador.setEmBatalha(false);
    }

    public void batalhaFinal(Personagem jogador, Aliado aliado, Maligno maligno) throws Exception {
    boolean desistiu = false;
    jogador.setEmBatalha(true);
    System.out.println("\n🔥 BATALHA FINAL CONTRA " + maligno.getNome().toUpperCase() + " 🔥");
    System.out.println("--------------------------------------------------");

    while (jogador.estaVivo() && maligno.estaVivo()) {
        System.out.println("\n======= STATUS =======");
        System.out.println(jogador.getNome() + " (HP: " + jogador.getPontosVida() + ")");
        System.out.println(aliado.getNome() + " (HP: " + aliado.getPontosVida() + ")");
        System.out.println(maligno.getNome() + " (HP: " + maligno.getPontosVida() + ")");
        System.out.println("======================");

        System.out.println("""
                Escolha sua ação:
                [1] - Ataque leve
                [2] - Ataque forte
                [3] - Usar item
                [4] - Ver inventário
                [5] - Desistir (não recomendado)
                """);
        System.out.print("Escolha: ");
        int escolha = sc.nextInt();
        sc.nextLine();
        System.out.println();

        boolean jogadorAtacou = false;

        switch (escolha) {
            case 1 -> {
                jogadorAtacou = true;
                System.out.println("Você tenta um ataque leve...");
                rolagem.simulacao(jogador);
                int evento = rolagem.rolar();
                System.out.println("RESULTADO DO D20: " + evento);
                if (evento >= 6) {
                    int dano = jogador.getAtaque() + (int)(Math.random() * 5);
                    System.out.println("Você acerta " + dano + " de dano!");
                    maligno.sofrerDano(dano);
                } else System.out.println("Você errou!");
            }

            case 2 -> {
                jogadorAtacou = true;
                System.out.println("Você carrega um ataque forte...");
                rolagem.simulacao(jogador);
                int evento = rolagem.rolar();
                System.out.println("RESULTADO DO D20: " + evento);
                if (evento >= 10) {
                    int dano = jogador.getAtaque() + (int)(Math.random() * 10) + 10;
                    System.out.println("💥 GOLPE CRÍTICO! Dano causado: " + dano);
                    maligno.sofrerDano(dano);
                } else System.out.println("Maligno desviou!");
            }

            case 3 -> jogador.getInventario().usarItemEmBatalha(jogador, maligno, sc);
            case 4 -> jogador.getInventario().listarItens(jogador);

            case 5 -> {
                System.out.println("Você tenta fugir...");
                rolagem.simulacao(jogador);
                int evento = rolagem.rolar();
                System.out.println("RESULTADO DO D20: " + evento);
                if (evento >= 19) {
                    System.out.println("Você escapa por um portal instável... mas algo te puxa de volta!");
                    Thread.sleep(1000);
                    System.out.println("[MALIGNO] >> \"A fuga... não existe aqui.\"");
                } else {
                    System.out.println("As correntes da arena o prendem. Não há saída.");
                }
            }

            default -> System.out.println("Opção inválida!");
        }

        // turno da bruxa (aliada)
        if (jogadorAtacou && aliado.estaVivo() && maligno.estaVivo()) {
            System.out.println("\nTurno de " + aliado.getNome() + "!");
            int eventoAliado = rolagem.rolar();
            if (eventoAliado >= 8) {
                int danoAliado = aliado.getAtaque() + (int)(Math.random() * 10);
                System.out.println(aliado.getNome() + " conjura um feitiço e causa " + danoAliado + " de dano!");
                maligno.sofrerDano(danoAliado);
            } else {
                System.out.println(aliado.getNome() + " erra o feitiço — a fenda distorce sua magia!");
            }
        }

        if (!maligno.estaVivo()) {
            System.out.println("\n💀 " + maligno.getNome() + " foi derrotado!");
            System.out.println("[BRUXA] >> \"Finalmente... a fenda se fecha.\"");
            System.out.println("✨ A luz consome tudo. Você sente o peso da magia desaparecer.");
            getDropItem();
            System.out.println("\n-------------- ⚔️ Fim da História ⚔️--------------\n");
            return;
        }

        // turno do maligno
        System.out.println("\nTurno do Maligno...");
        if (Math.random() < 0.35) {
            maligno.ataqueEspecial(jogador, aliado);
        } else {
            int danoBase = maligno.getAtaque() + (int)(Math.random() * 8);
            boolean atacarJogador = Math.random() < 0.6;
            Personagem alvo = atacarJogador ? jogador : aliado;
            System.out.println("Maligno ataca " + alvo.getNome() + "!");
            alvo.sofrerDano(danoBase);
            System.out.println(alvo.getNome() + " sofreu " + danoBase + " de dano!");
        }

        if (!jogador.estaVivo()) {
            jogador.tratarMorte(jogador, aliado, maligno, this, progressao);
            return;
        }
    }

    jogador.setEmBatalha(false);
}

    public Item droparItem(int opcao, int progressao) {
        List<Item> possiveisDrops = new ArrayList<>();

        if (progressao < 0) progressao = 0;
        if (progressao > 10) progressao = 10;

        // DROPS GUERREIRO
        if (opcao == 1) {
            switch (progressao) {
                case 0 -> {
                    possiveisDrops.add(new Item("Espada Enferrujada", Item.TipoItem.FISICO, 5, 1));
                    possiveisDrops.add(new Item("Escudo de Madeira", Item.TipoItem.EQUIPAVEL, 5, 1));
                    possiveisDrops.add(new Item("Frasco de Cura", Item.TipoItem.CURA, 15, 1));
                }
                case 1 -> {
                    possiveisDrops.add(new Item("Machado Simples", Item.TipoItem.FISICO, 8, 1));
                    possiveisDrops.add(new Item("Cota de Couro", Item.TipoItem.EQUIPAVEL, 10, 1));
                    possiveisDrops.add(new Item("Bomba Improvisada", Item.TipoItem.ATIRAVEL, 10, 1));
                }
                case 2 -> {
                    possiveisDrops.add(new Item("Espada Curta", Item.TipoItem.FISICO, 12, 1));
                    possiveisDrops.add(new Item("Escudo de Ferro", Item.TipoItem.EQUIPAVEL, 15, 1));
                }
                case 3 -> {
                    possiveisDrops.add(new Item("Espada Longa", Item.TipoItem.FISICO, 18, 1));
                    possiveisDrops.add(new Item("Poção de Cura", Item.TipoItem.CURA, 25, 1));
                    possiveisDrops.add(new Item("Bomba Improvisada", Item.TipoItem.ATIRAVEL, 10, 1));
                }
                case 4 -> {
                    possiveisDrops.add(new Item("Machado Duplo", Item.TipoItem.FISICO, 22, 1));
                    possiveisDrops.add(new Item("Armadura de Ferro", Item.TipoItem.EQUIPAVEL, 20, 1));
                }
                case 5 -> {
                    possiveisDrops.add(new Item("Espada Flamejante", Item.TipoItem.FISICO, 28, 1));
                    possiveisDrops.add(new Item("Escudo Dourado", Item.TipoItem.EQUIPAVEL, 25, 1));
                    possiveisDrops.add(new Item("Bomba Incendiadora", Item.TipoItem.ATIRAVEL, 20, 1));
                }
                case 6 -> {
                    possiveisDrops.add(new Item("Lâmina Real", Item.TipoItem.FISICO, 34, 1));
                    possiveisDrops.add(new Item("Poção Reforçada", Item.TipoItem.CURA, 40, 1));
                }
                case 7 -> {
                    possiveisDrops.add(new Item("Espada do Destino", Item.TipoItem.FISICO, 40, 1));
                    possiveisDrops.add(new Item("Armadura de Aço Negro", Item.TipoItem.EQUIPAVEL, 35, 1));
                    possiveisDrops.add(new Item("Runa de Sangue", Item.TipoItem.ATIRAVEL, 28, 1));
                }
                case 8 -> {
                    possiveisDrops.add(new Item("Martelo dos Titãs", Item.TipoItem.FISICO, 50, 1));
                    possiveisDrops.add(new Item("Escudo Mítico", Item.TipoItem.EQUIPAVEL, 45, 1));
                }
                case 9 -> {
                    possiveisDrops.add(new Item("Lâmina Divina", Item.TipoItem.FISICO, 60, 1));
                    possiveisDrops.add(new Item("Poção Suprema", Item.TipoItem.CURA, 60, 1));
                }
                case 10 -> {
                    possiveisDrops.add(new Item("Espada dos Deuses", Item.TipoItem.FISICO, 75, 1));
                    possiveisDrops.add(new Item("Armadura Eterna", Item.TipoItem.EQUIPAVEL, 70, 1));
                }
            }
        }

        // DROPS MAGO
        if (opcao == 2) {
            switch (progressao) {
                case 0 -> {
                    possiveisDrops.add(new Item("Cajado de Galho", Item.TipoItem.DISTANCIA, 5, 1));
                    possiveisDrops.add(new Item("Livro de Aprendiz", Item.TipoItem.EQUIPAVEL, 5, 1));
                    possiveisDrops.add(new Item("Essência Bruta", Item.TipoItem.ATIRAVEL, 10, 1));
                }
                case 1 -> {
                    possiveisDrops.add(new Item("Cajado Simples", Item.TipoItem.DISTANCIA, 8, 1));
                    possiveisDrops.add(new Item("Poção de Mana", Item.TipoItem.CURA, 30, 1));
                    possiveisDrops.add(new Item("Pergaminho de fogo", Item.TipoItem.ATIRAVEL, 12, 1));
                }
                case 2 -> {
                    possiveisDrops.add(new Item("Cajado Elemental", Item.TipoItem.DISTANCIA, 14, 1));
                    possiveisDrops.add(new Item("Livro das Sombras", Item.TipoItem.EQUIPAVEL, 12, 1));
                }
                case 3 -> {
                    possiveisDrops.add(new Item("Cajado das Chamas", Item.TipoItem.DISTANCIA, 20, 1));
                    possiveisDrops.add(new Item("Poção de Cura Arcana", Item.TipoItem.CURA, 25, 2));
                    possiveisDrops.add(new Item("Pergaminho do Tornado", Item.TipoItem.ATIRAVEL, 21, 1));
                }
                case 4 -> {
                    possiveisDrops.add(new Item("Cajado do Gelo", Item.TipoItem.DISTANCIA, 25, 1));
                    possiveisDrops.add(new Item("Manto Místico", Item.TipoItem.EQUIPAVEL, 18, 1));
                }
                case 5 -> {
                    possiveisDrops.add(new Item("Cajado Tempestuoso", Item.TipoItem.DISTANCIA, 30, 1));
                    possiveisDrops.add(new Item("Capa de Defesa Arcana", Item.TipoItem.EQUIPAVEL, 25, 1));
                    possiveisDrops.add(new Item("Pergaminho da Cura Sagrada ", Item.TipoItem.CURA, 100, 1));
                }
                case 6 -> {
                    possiveisDrops.add(new Item("Cajado de Runas", Item.TipoItem.DISTANCIA, 36, 1));
                    possiveisDrops.add(new Item("Essência Flamejante", Item.TipoItem.ATIRAVEL, 30, 1));
                    possiveisDrops.add(new Item("Runa da Maldião Proibida", Item.TipoItem.ATIRAVEL, 30, 2));
                }
                case 7 -> {
                    possiveisDrops.add(new Item("Cajado das Estrelas", Item.TipoItem.DISTANCIA, 45, 1));
                    possiveisDrops.add(new Item("Manto Etéreo", Item.TipoItem.EQUIPAVEL, 40, 1));
                }
                case 8 -> {
                    possiveisDrops.add(new Item("Cajado Astral", Item.TipoItem.DISTANCIA, 55, 1));
                    possiveisDrops.add(new Item("Poção Mística", Item.TipoItem.CURA, 50, 1));
                    possiveisDrops.add(new Item("Feitiço Proibido da Devastação", Item.TipoItem.ATIRAVEL, 50, 1));
                }
                case 9 -> {
                    possiveisDrops.add(new Item("Cajado Cósmico", Item.TipoItem.DISTANCIA, 65, 1));
                    possiveisDrops.add(new Item("Manto da Eternidade", Item.TipoItem.EQUIPAVEL, 60, 1));
                }
                case 10 -> {
                    possiveisDrops.add(new Item("Cajado do Caos", Item.TipoItem.DISTANCIA, 80, 1));
                    possiveisDrops.add(new Item("Livro Supremo", Item.TipoItem.EQUIPAVEL, 70, 1));
                }
            }
        }

        // DROPS ARQUEIRO
        if (opcao == 3) {
            switch (progressao) {
                case 0 -> {
                    possiveisDrops.add(new Item("Arco Velho", Item.TipoItem.DISTANCIA, 5, 1));
                    possiveisDrops.add(new Item("Luva Rústica", Item.TipoItem.EQUIPAVEL, 5, 1));
                    possiveisDrops.add(new Item("Frasco Congelante", Item.TipoItem.ATIRAVEL, 10, 1));
                }
                case 1 -> {
                    possiveisDrops.add(new Item("Arco de Caça", Item.TipoItem.DISTANCIA, 8, 1));
                    possiveisDrops.add(new Item("Poção de Cura", Item.TipoItem.CURA, 20, 1));
                    possiveisDrops.add(new Item("Bomba Improvisada", Item.TipoItem.ATIRAVEL, 10, 1));
                }
                case 2 -> {
                    possiveisDrops.add(new Item("Arco Leve", Item.TipoItem.DISTANCIA, 12, 1));
                    possiveisDrops.add(new Item("Luva de Couro", Item.TipoItem.EQUIPAVEL, 10, 1));
                }
                case 3 -> {
                    possiveisDrops.add(new Item("Arco Longo", Item.TipoItem.DISTANCIA, 18, 1));
                    possiveisDrops.add(new Item("Flecha Explosiva", Item.TipoItem.ATIRAVEL, 20, 2));
                    possiveisDrops.add(new Item("Bomba Improvisada", Item.TipoItem.ATIRAVEL, 10, 1));
                }
                case 4 -> {
                    possiveisDrops.add(new Item("Arco Incendiário", Item.TipoItem.DISTANCIA, 24, 1));
                    possiveisDrops.add(new Item("Luvas de Precisão", Item.TipoItem.EQUIPAVEL, 20, 1));
                }
                case 5 -> {
                    possiveisDrops.add(new Item("Arco das Sombras", Item.TipoItem.DISTANCIA, 30, 1));
                    possiveisDrops.add(new Item("Flecha Envenenada", Item.TipoItem.ATIRAVEL, 35, 1));
                    possiveisDrops.add(new Item("Konai de Sangue", Item.TipoItem.ATIRAVEL, 35, 1));
                }
                case 6 -> {
                    possiveisDrops.add(new Item("Arco Rúnico", Item.TipoItem.DISTANCIA, 38, 1));
                    possiveisDrops.add(new Item("Manto do Caçador", Item.TipoItem.EQUIPAVEL, 25, 1));
                    possiveisDrops.add(new Item("Bomba de Micro-Flechas", Item.TipoItem.ATIRAVEL, 20, 1));
                }
                case 7 -> {
                    possiveisDrops.add(new Item("Arco do Vento", Item.TipoItem.DISTANCIA, 45, 1));
                    possiveisDrops.add(new Item("Flecha de Gelo", Item.TipoItem.ATIRAVEL, 40, 3));
                }
                case 8 -> {
                    possiveisDrops.add(new Item("Arco da Tempestade", Item.TipoItem.DISTANCIA, 55, 1));
                    possiveisDrops.add(new Item("Luvas do Caçador Supremo", Item.TipoItem.EQUIPAVEL, 45, 1));
                    possiveisDrops.add(new Item("Konai de Sangue", Item.TipoItem.ATIRAVEL, 35, 1));
                }
                case 9 -> {
                    possiveisDrops.add(new Item("Arco Sagrado", Item.TipoItem.DISTANCIA, 65, 1));
                    possiveisDrops.add(new Item("Poção Suprema", Item.TipoItem.CURA, 55, 1));
                }
                case 10 -> {
                    possiveisDrops.add(new Item("Arco Celestial", Item.TipoItem.DISTANCIA, 80, 1));
                    possiveisDrops.add(new Item("Traje do Guardião", Item.TipoItem.EQUIPAVEL, 70, 1));
                }
            }
        }

        // Seleciona aleatoriamente um dos possíveis drops
        if (possiveisDrops.isEmpty()) return null;
        Random r = new Random();
        return possiveisDrops.get(r.nextInt(possiveisDrops.size()));
    }

    public void getDropItem() throws Exception {
        Item itemDropado = droparItem(opcao, progressao);
        jogador.getInventario().adicionarItem(itemDropado);
        System.out.println("⭐ Item dropado: " + itemDropado.getNome() + "!");
    }

    @Override
    public String toString() {
        return "Jogador: " + jogador +
                "Aliado: " + aliado +
                "Inimigo: " + inimigo +
                "Progressão: " + progressao +
                "Opção: " + opcao;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Jogo j = (Jogo) obj;
        if(!this.jogador.equals(j.jogador) ||
        !this.aliado.equals(j.aliado) ||
        !this.inimigo.equals(j.inimigo) ||
        this.progressao != j.progressao ||
        this.opcao != j.opcao) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + this.jogador.hashCode();
        ret = ret * 2 + this.aliado.hashCode();
        ret = ret * 2 + this.inimigo.hashCode();
        ret = ret * 2 + ((Integer)this.progressao).hashCode();
        ret = ret * 2 + ((Integer)this.opcao).hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }
}
