package com.rpg.inventario;

import com.rpg.personagens.Inimigo;
import com.rpg.personagens.Personagem;

import java.util.*;

public class Inventario implements Cloneable {

    // ATRIBUTOS

    private List<Item> itens;

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

    public void listarItens() {
        if (itens.isEmpty()) {
            System.out.println("Inventário vazio");
            return;
        }

        System.out.println("      Inventário:");
        for (Item item : itens) {
            System.out.println("        - " + item.getNome() + " (" + item.getQuantidade() + ")");
        }
    }

    public void usarItemFora(Personagem jogador, Scanner sc) {
        if (itens.isEmpty()) {
            System.out.println("Você não possui itens para usar.");
            return;
        }

        listarItens();
        System.out.print("Digite o nome do item que deseja usar: ");
        String nome = sc.nextLine();

        Item item = getItem(nome);
        if (item == null) {
            System.out.println("Item não encontrado!");
            return;
        }

        item.usar(jogador);
        try {
            item.setQuantidade(item.getQuantidade() - 1);
            if (item.getQuantidade() <= 0) {
                itens.remove(item);
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar quantidade do item: " + e.getMessage());
        }
    }

    public void usarItemEmBatalha(Personagem jogador, Inimigo inimigo, Scanner sc) {
        if (itens.isEmpty()) {
            System.out.println("Você não possui itens para usar.");
            return;
        }

        listarItens();
        System.out.print("Digite o nome do item que deseja usar: ");
        String nome = sc.nextLine();

        Item item = getItem(nome);
        if (item == null) {
            System.out.println("Item não encontrado!");
            return;
        }

        item.usarBatalha(jogador, inimigo);
        try {
            item.setQuantidade(item.getQuantidade() - 1);
            if (item.getQuantidade() <= 0) {
                itens.remove(item);
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar quantidade do item: " + e.getMessage());
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
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        Inventario i = (Inventario) obj;

        if (this.itens == null && i.itens == null)
            return true;
        if (this.itens == null || i.itens == null)
            return false;

        return this.itens.equals(i.itens);
    }

    @Override
    public int hashCode() {
        return (this.itens == null) ? 0 : this.itens.hashCode();
    }
}
