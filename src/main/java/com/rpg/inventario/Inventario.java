package com.rpg.inventario;

public class Inventario implements Cloneable {

    private Item item;

    public Inventario() {
        this.item = null;
    }

    public Inventario(Item item) {
        this.item = item;
    }

    public Inventario(Inventario modelo) throws Exception {
        if (modelo == null)
            throw new Exception("Modelo ausente");

        if (modelo.item != null)
            this.item = new Item(
                    modelo.item.getNome(),
                    modelo.item.getDescricao(),
                    modelo.item.getEfeito(),
                    modelo.item.getQuantidade()
            );
    }

    public Item getItem() {
        return this.item;
    }

    public void adicionarItem(Item novoItem) throws Exception {
        if (novoItem == null)
            throw new Exception("Item inválido");

        if (this.item == null) {
            this.item = novoItem;
            return;
        }

        if (this.item.equals(novoItem)) {
            int novaQtd = this.item.getQuantidade() + novoItem.getQuantidade();
            this.item.setQuantidade(novaQtd);
        } else {
            throw new Exception("O inventário suporta apenas um tipo de item por enquanto");
        }
    }

    public void removeItem(int qtd) throws Exception {
        if (this.item == null)
            throw new Exception("Inventário vazio");

        int qtdAtual = this.item.getQuantidade();

        if (qtd > qtdAtual)
            throw new Exception("Quantidade a remover é maior que a existente");

        this.item.setQuantidade(qtdAtual - qtd);

        if (this.item.getQuantidade() == 0)
            this.item = null;
    }

    public void listarInventario() {
        if (this.item == null)
            System.out.println("Inventário vazio");
        else
            System.out.println(this.item.getNome() + " (" + this.item.getQuantidade() + ")");
    }

    @Override
    public String toString() {
        return (this.item == null)
                ? "Inventário vazio"
                : this.item.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        Inventario i = (Inventario) obj;

        if (this.item == null && i.item == null)
            return true;
        if (this.item == null || i.item == null)
            return false;

        return this.item.equals(i.item);
    }

    @Override
    public int hashCode() {
        return (this.item == null) ? 0 : this.item.hashCode();
    }
}
