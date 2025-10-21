package com.rpg.inventario;

import java.util.*;

public class Inventario implements Cloneable {

    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }

    public Inventario(List<Item> itens) {
        this.itens = itens;
    }

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

    public void removeItem(String nome, int qtd) throws Exception {
        if (this.itens == null)
            throw new Exception("Inventário vazio");

        for (Item item : itens) {
            if (item.getNome().equalsIgnoreCase(nome)) {
                if (qtd > item.getQuantidade()) throw new Exception("Quantidade a remover é maior que a existente");
                item.setQuantidade(item.getQuantidade() - qtd);
                if (item.getQuantidade() == 0) itens.remove(item);
                return;
            }
        }
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
