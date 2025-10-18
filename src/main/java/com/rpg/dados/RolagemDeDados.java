package com.rpg.dados;

import com.rpg.jogo.Jogadores;

import java.util.*;

public class RolagemDeDados {
    private Random rand = new Random();
    private Scanner scanner = new Scanner(System.in);

    public void dadoInicial(Jogadores qtdJogadores) {
        Map<Integer, Integer> jogadorValor = new HashMap<>();
        List<Integer> jogadores = new ArrayList<>();

        for (int i = 1; i <= qtdJogadores.getQtdJogadores(); i++) {
            jogadores.add(i);
        }

        rodadasDadoInicial(jogadores, jogadorValor);

        // Ordenar do maior para o menor
        List<Map.Entry<Integer, Integer>> ordenados = new ArrayList<>(jogadorValor.entrySet());
        ordenados.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        System.out.println("\nOrdem final para jogar:");
        for (int i = 0; i < ordenados.size(); i++) {
            Map.Entry<Integer, Integer> entry = ordenados.get(i);
            System.out.println("Jogador " + entry.getKey() + " será o " + (i + 1) + "° a jogar (Rolou " + entry.getValue() + ")");
        }
    }

    private void rodadasDadoInicial(List<Integer> jogadores, Map<Integer, Integer> jogadorValor) {
        List<Integer> empate = new ArrayList<>(jogadores);
        int rodada = 1;

        while (!empate.isEmpty()) {
            System.out.println("\nRodada de rolagem " + rodada + ":");
            Map<Integer, List<Integer>> valorJogadores = new HashMap<>();

            for (int jogador : empate) {
                System.out.print("Jogador " + jogador + ", pressione [ENTER] para rolar o dado...");
                scanner.nextLine();

                System.out.print("Rolando");
                for (int i = 0; i < 3; i++) { // animação simples de pontos
                    try {
                        Thread.sleep(700); // espera 0,5 segundo
                        System.out.print(".");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println();

                int d20 = rand.nextInt(20) + 1;
                System.out.println("Jogador " + jogador + " rolou: " + d20);
                jogadorValor.put(jogador, d20);
                System.out.println();

                valorJogadores.putIfAbsent(d20, new ArrayList<>());
                valorJogadores.get(d20).add(jogador);
            }

            // Rodada apenas para empates
            empate.clear();
            for (List<Integer> lista : valorJogadores.values()) {
                if (lista.size() > 1) {
                    System.out.println("Empate entre jogadores: " + lista + ". Rolar novamente!");
                    empate.addAll(lista);
                }
            }

            rodada++;
        }
    }
}
