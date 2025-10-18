package com.rpg.jogo;

import com.rpg.dados.RolagemDeDados;
import java.util.*;

public class Jogo {
    Scanner scanner = new Scanner(System.in);
    private Jogadores qtdJogadores = new Jogadores();
    RolagemDeDados rolagem = new RolagemDeDados();

    public void iniciarJogo() {
        int qtd = 0;
        boolean valido = false;

        while (!valido) {
            try {
                System.out.print("\nPrimeiro, digite a quantidade de jogadores (Máx: 5 jogadores): ");
                qtd = scanner.nextInt();

                if (qtd < 1 || qtd > 5) {
                    System.out.println("Quantidade de jogadores inválida! Somente números entre 1 à 5.\nTente novamente...");
                } else {
                    valido = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Quantidade de jogadores inválida! Somente números entre 1 à 5.\nTente novamente...");
                scanner.nextLine();
            }
        }
        System.out.print("\nAgora vamos decidir a ordem de jogada:\n");
        qtdJogadores.setQtdJogadores(qtd);
        rolagem.dadoInicial(qtdJogadores);

    }
}
