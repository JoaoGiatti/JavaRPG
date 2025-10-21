package com.rpg.inventario;

public class Item {

    private String nome;
    private String tipo;
    private float valor;
    private int quantidade;

    public Item(String nome, String tipo, float valor, int quantidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setTipo(String descricao) { this.tipo = descricao; }
    public void setValor(float valor) { this.valor = valor; }
    public void setQuantidade(int quantidade) throws Exception {
        if (quantidade < 0) throw new Exception("Quantidade inválida");
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public String getTipo() { return tipo; }
    public float getValor() { return valor; }
    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() {
        return "Item: " + this.nome +
                "\nTipo: " + this.tipo +
                "\nValor: " + this.valor +
                "\nQuantidade: " + this.quantidade;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) { return true; }
        if (obj == null || obj.getClass() != this.getClass()) { return false; }
        Item item = (Item)obj;
        if(this.nome.compareTo(item.nome) != 0) { return false; }
        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + this.nome.hashCode();
        ret = ret * 2 + this.tipo.hashCode();
        ret = ret * 2 + ((Float)this.valor).hashCode();
        ret = ret * 2 + ((Integer)this.quantidade).hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }
}
