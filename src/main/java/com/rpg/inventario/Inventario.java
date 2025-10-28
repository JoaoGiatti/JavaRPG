package com.rpg.inventario;

import com.rpg.jogo.Jogo;
import com.rpg.personagens.Inimigo;
import com.rpg.personagens.Personagem;

import java.util.*;

public class Inventario implements Cloneable {

    // ATRIBUTOS

    private final List<Item> itens;

    // CONSTRUTOR

    public Inventario() {
        this.itens = new ArrayList<>();
    }

    // MÉTODOS

    public Item getItem(String nome) {
        for (Item item : itens) {
            if (item.getNome().equalsIgnoreCase(nome)) {
                return item;
            }
        }
        return null;
    }

    public void adicionarItem(Item novoItem) throws Exception {
        if (novoItem == null)
            throw new Exception("Item inválido");

        // Se o item já existe no inventário, soma a quantidade
        for (Item item : itens) {
            if (item.getNome().equalsIgnoreCase(novoItem.getNome())) {
                item.setQuantidade(item.getQuantidade() + novoItem.getQuantidade());
                return;
            }
        }

        // Caso contrário, adiciona como novo
        itens.add(novoItem);
    }

    public void listarItens(Personagem jogador) {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio");
            return;
        }

        System.out.println("      Inventário:");
        for (Item item : itens) {
            System.out.println("        - " + item.getNome() + " (" + item.getQuantidade() + ")");
        }
        listarItensEquipados(jogador);
    }

    public void listarItensComIndices(Personagem jogador) {
        System.out.println("      Inventário:");
        for (int i = 0; i < itens.size(); i++) {
            Item item = itens.get(i);
            System.out.println("        [" + (i + 1) + "] - " + item.getNome() + " (" + item.getQuantidade() + ")");
        }

        // Passa o tamanho do inventário para começar a numeração corretamente
        listarItensEquipadosComIndices(jogador, itens.size());
    }

    public void usarItemFora(Personagem jogador, Scanner sc) throws Exception {
        if (itens.isEmpty() && jogador.getItensEquipados().isEmpty()) {
            System.out.println("Você não possui itens para usar.");
            return;
        }

        listarItensComIndices(jogador);
        System.out.print("Digite o número do item que deseja usar: ");
        int escolha;
        try {
            escolha = Integer.parseInt(sc.nextLine());

            // VALIDAÇÃO USANDO totalItens
            int totalItens = itens.size() + jogador.getItensEquipados().size();
            if (escolha < 1 || escolha > totalItens) {
                System.out.println("Escolha inválida!");
                return;
            }

            // SELECIONANDO O ITEM CORRETO
            Item item;
            boolean jaEquipado = false;
            if (escolha <= itens.size()) {
                item = itens.get(escolha - 1); // vem do inventário
            } else {
                item = jogador.getItensEquipados().get(escolha - itens.size() - 1); // vem dos equipados
                jaEquipado = true;
            }

            // Lógica de equipar itens, só para itens do inventário
            if ((item.getTipo() == Item.TipoItem.FISICO ||
                    item.getTipo() == Item.TipoItem.DISTANCIA ||
                    item.getTipo() == Item.TipoItem.EQUIPAVEL)) {

                if(jaEquipado) {
                    System.out.print("Item já equipado! Deseja desequipar" + item.getNome() + "?\n" +
                            "   [1] - Sim\n" +
                            "   [2] - Não\n" +
                            "Escolha: ");
                    int trocar = sc.nextInt();
                    sc.nextLine();

                    if (trocar == 1) {
                        jogador.desequipar(item);
                        itens.add(item);
                        System.out.println("Você desequipou " + item.getNome() + "!");
                    } else if (trocar == 2) {
                        System.out.println("Você manteve " + item.getNome() + " equipado.");
                    } else{
                        System.out.println("Número inválido! Digite novamente.");
                    }
                } else {
                    System.out.print("Deseja equipar " + item.getNome() + "?\n" +
                            "   [1] - Sim\n" +
                            "   [2] - Não\n" +
                            "Escolha: ");
                    int resp = sc.nextInt();
                    sc.nextLine();

                    if (resp == 1) {
                        Item jaEquipadoDoTipo = jogador.getItemEquipadoDoTipo(item.getTipo());
                        if (jaEquipadoDoTipo != null) {
                            System.out.print("Você já possui " + jaEquipadoDoTipo.getNome() + " equipado. Deseja trocar?\n" +
                                    "   [1] - Sim\n" +
                                    "   [2] - Não\n" +
                                    "Escolha: ");
                            int trocar = sc.nextInt();
                            sc.nextLine();

                            if (trocar == 1) {
                                jogador.desequipar(jaEquipadoDoTipo);
                                itens.add(jaEquipadoDoTipo); // volta para inventário
                                jogador.equipar(item);
                                itens.remove(item);
                                System.out.println("Você equipou " + item.getNome() + "!");
                            } else {
                                System.out.println("Você manteve o equipamento atual.");
                            }
                        } else {
                            jogador.equipar(item);
                            itens.remove(item);
                            System.out.println("Você equipou " + item.getNome() + "!");
                        }
                    } else if (resp == 2) {
                        System.out.println("Você decidiu não equipar o item.");
                    } else{
                        System.out.println("Número inválido! Digite novamente.");
                    }
                }

                return;
            } else{
                boolean usado = item.usar(jogador);
                if (usado) {
                    item.setQuantidade(item.getQuantidade() - 1);
                    if (item.getQuantidade() <= 0) itens.remove(item);
                }
            }

        } catch (Exception e) {
            System.out.println("Entrada inválida!");
        }
    }

    public void usarItemEmBatalha(Personagem jogador, Inimigo inimigo, Scanner sc) throws Exception {
        if (itens.isEmpty() && jogador.getItensEquipados().isEmpty()) {
            System.out.println("Você não possui itens para usar.");
            return;
        }

        listarItensComIndices(jogador);
        System.out.print("Digite o número do item que deseja usar: ");
        int escolha;
        try {
            escolha = Integer.parseInt(sc.nextLine());

            // VALIDAÇÃO USANDO totalItens
            int totalItens = itens.size() + jogador.getItensEquipados().size();
            if (escolha < 1 || escolha > totalItens) {
                System.out.println("Escolha inválida!");
                return;
            }

            // SELECIONANDO O ITEM CORRETO
            Item item;
            boolean jaEquipado = false;
            if (escolha <= itens.size()) {
                item = itens.get(escolha - 1); // vem do inventário
            } else {
                item = jogador.getItensEquipados().get(escolha - itens.size() - 1); // vem dos equipados
                jaEquipado = true;
            }

            // Lógica de equipar itens, só para itens do inventário
            if ((item.getTipo() == Item.TipoItem.FISICO ||
                    item.getTipo() == Item.TipoItem.DISTANCIA ||
                    item.getTipo() == Item.TipoItem.EQUIPAVEL)) {

                if(jaEquipado) {
                    System.out.print("Item já equipado! Deseja desequipar" + item.getNome() + "?\n" +
                            "   [1] - Sim\n" +
                            "   [2] - Não\n" +
                            "Escolha: ");
                    int trocar = sc.nextInt();
                    sc.nextLine();

                    if (trocar == 1) {
                        jogador.desequipar(item);
                        itens.add(item);
                        System.out.println("Você desequipou " + item.getNome() + "!");
                    } else if (trocar == 2) {
                        System.out.println("Você manteve " + item.getNome() + " equipado.");
                    } else{
                        System.out.println("Número inválido! Digite novamente.");
                    }
                } else {
                    System.out.print("Deseja equipar " + item.getNome() + "?\n" +
                            "   [1] - Sim\n" +
                            "   [2] - Não\n" +
                            "Escolha: ");
                    int resp = sc.nextInt();
                    sc.nextLine();

                    if (resp == 1) {
                        Item jaEquipadoDoTipo = jogador.getItemEquipadoDoTipo(item.getTipo());
                        if (jaEquipadoDoTipo != null) {
                            System.out.print("Você já possui " + jaEquipadoDoTipo.getNome() + " equipado. Deseja trocar?\n" +
                                    "   [1] - Sim\n" +
                                    "   [2] - Não\n" +
                                    "Escolha: ");
                            int trocar = sc.nextInt();
                            sc.nextLine();

                            if (trocar == 1) {
                                jogador.desequipar(jaEquipadoDoTipo);
                                itens.add(jaEquipadoDoTipo); // volta para inventário
                                jogador.equipar(item);
                                itens.remove(item);
                                System.out.println("Você equipou " + item.getNome() + "!");
                            } else {
                                System.out.println("Você manteve o equipamento atual.");
                            }
                        } else {
                            jogador.equipar(item);
                            itens.remove(item);
                            System.out.println("Você equipou " + item.getNome() + "!");
                        }
                    } else if (resp == 2) {
                        System.out.println("Você decidiu não equipar o item.");
                    } else{
                        System.out.println("Número inválido! Digite novamente.");
                    }
                }

                return;
            } else{
                boolean usado = item.usarBatalha(jogador, inimigo);
                if (usado) {
                    item.setQuantidade(item.getQuantidade() - 1);
                    if (item.getQuantidade() <= 0) itens.remove(item);
                }
            }

        } catch (Exception e) {
            System.out.println("Entrada inválida!");
        }
    }

    public void listarItensEquipados(Personagem jogador) {
        List<Item> equipados = jogador.getItensEquipados();

        if (equipados.isEmpty()) {
            return;
        }

        System.out.println("------------------------------");
        System.out.println("      Itens Equipados:");
        for (int i = 0; i < equipados.size(); i++) {
            Item item = equipados.get(i);
            System.out.println("        - " + item.getNome() + " (" + item.getQuantidade() + ")");
        }

    }

    public void listarItensEquipadosComIndices(Personagem jogador, int startIndex) {
        List<Item> equipados = jogador.getItensEquipados();

        if (equipados.isEmpty()) {
            return;
        }

        System.out.println("------------------------------");
        System.out.println("      Itens Equipados:");
        for (int i = 0; i < equipados.size(); i++) {
            Item item = equipados.get(i);
            System.out.println("        [" + (startIndex + i + 1) + "] - " + item.getNome());
        }
    }


    // REIMPLEMENTAÇÕES

    @Override
    public String toString() {
        return (this.itens == null)
                ? "Inventário vazio"
                : this.itens.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Inventario i = (Inventario) obj;
        if(!this.itens.equals(i.itens)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + this.itens.hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }

    //contrutor de cópia
    public Inventario (Inventario modelo) throws Exception {
        if (modelo == null) throw new Exception("Modelo ausente");

        this.itens = new ArrayList<>();
        if (modelo.itens != null) {
            for (Item item : modelo.itens) {
                this.itens.add(new Item(
                        item.getNome(),
                        item.getTipo(),
                        item.getValor(),
                        item.getQuantidade()
                ));
            }
        }
    }

    @Override
    public Object clone() {
        Inventario ret = null;
        try {
            ret = new Inventario(this);
        } catch (Exception e) {}
        return ret;
    }
}
