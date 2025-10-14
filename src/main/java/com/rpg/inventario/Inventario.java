package com.rpg.inventario;

public class Inventario {

    private Item item;
    private int quantidade;

    public Inventario(Item item, int quantidade) {
        this.item = item;
        this.quantidade = quantidade;
    }
    public Item getItem() { return this.item; }
    public int getQuantidade() { return this.quantidade; }

    public void adicionarItem(Item item, int quantidade) {
        if (this.item == null) {
            this.item = item;
            this.quantidade = 1;
        }
        this.quantidade += 1;
    }

    public void removeItem(Item item, int quantidade) throws Exception {
        if(this.quantidade == 0) throw new Exception("Item não encontrado");
        this.quantidade -= 1;
    }

    public void listarInventario(Item item, int quantidade) {
        item = this.item;
        quantidade = this.quantidade;
        System.out.println(item + " (" + quantidade + ")");
    }

    @Override
    public String toString() {
        return "Item: " + this.item.toString() +
                "\nQuantidade: " + this.quantidade;
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
        ret = ret * 2 + ((Integer)this.quantidade).hashCode();
        if(ret < 0) ret = -ret;
        return ret;
    }

    //contrutor de cópia
    public Inventario (Inventario modelo) throws Exception {
        if (modelo == null) throw new Exception("Modelo ausente");
        this.item = modelo.item;
        this.quantidade = modelo.quantidade;
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
