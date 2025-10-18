package com.rpg.inventario;

public class Inventario {

    private Item item;

    public Inventario(Item item) {
        this.item = item;
    }

    public Item getItem() { return this.item; }

    public void adicionarItem(Item novoItem) throws Exception {
        if (this.item == null) this.item = novoItem;
        int novaQtd = this.item.getQuantidade() + novoItem.getQuantidade();
        this.item.setQuantidade(novaQtd);
    }

    public void removeItem(int qtd) throws Exception {
        if (this.item == null) throw new Exception("Inventário vazio");

        int qtdAtual = this.item.getQuantidade();

        if (qtd > qtdAtual)  throw new Exception("Quantidade a remover é maior que a existente");

        this.item.setQuantidade(qtdAtual - qtd);

        if (this.item.getQuantidade() == 0)
            this.item = null;
    }

    public void listarInventario() {
        if (this.item == null) {
            System.out.println("Inventário vazio");
        } else {
            System.out.println(this.item.getNome() + " (" + this.item.getQuantidade() + ")");
        }
    }

    public String toString() {
        return (this.item == null)
                ? "Inventário vazio"
                : this.item.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Inventario i = (Inventario)obj;
        if (this.item == i.item) { return true; }
        return false;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + this.item.hashCode();
        if(ret < 0) ret = -ret;
        return ret;
    }

    //contrutor de cópia
    public Inventario (Inventario modelo) throws Exception {
        if (modelo == null) throw new Exception("Modelo ausente");
        if (modelo.item != null)
            this.item = new Item(
                    modelo.item.getNome(),
                    modelo.item.getDescricao(),
                    modelo.item.getEfeito(),
                    modelo.item.getQuantidade()
            );
    }

    @Override
    public Object clone(){
        Inventario ret = null;
        try {
            ret = new Inventario(this);
        } catch (Exception e) {}
        return ret;
    }
}
