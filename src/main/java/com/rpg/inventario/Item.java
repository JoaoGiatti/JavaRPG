package com.rpg.inventario;

public class Item {

    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;

    public Item(String nome, String descricao, String efeito, int quantidade) {
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }

    public void setNome(String nome) { this.nome = nome; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setEfeito(String efeito) { this.efeito = efeito; }
    public void setQuantidade(int quantidade) throws Exception {
        if (quantidade < 0) throw new Exception("Quantidade inválida");
        this.quantidade = quantidade;
    }

    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getEfeito() { return efeito; }
    public int getQuantidade() { return quantidade; }

    @Override
    public String toString() {
        return "Item: " + this.nome +
                "\nDescrição: " + this.descricao +
                "\nEfeito: " + this.efeito +
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
        ret = ret * 2 + this.descricao.hashCode();
        ret = ret * 2 + this.efeito.hashCode();
        ret = ret * 2 + ((Integer)this.quantidade).hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }
}
