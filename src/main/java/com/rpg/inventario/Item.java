package com.rpg.inventario;

import com.rpg.personagens.Inimigo;
import com.rpg.personagens.Personagem;

public class Item implements Comparable<Item> {

    public enum TipoItem {
        FISICO,
        DISTANCIA,
        CURA,
        ATIRAVEL,
        EQUIPAVEL
    }

    // ATRIBUTOS

    private String nome;
    private TipoItem tipo;
    private float valor;
    private int quantidade;

    // CONSTRUTOR

    public Item(String nome, TipoItem tipo, float valor, int quantidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.valor = valor;
        this.quantidade = quantidade;
    }

    // SETTERS

    public void setNome(String nome) { this.nome = nome; }
    public void setTipo(TipoItem tipo) { this.tipo = tipo; }
    public void setValor(float valor) { this.valor = valor; }
    public void setQuantidade(int quantidade) throws Exception {
        if (quantidade < 0) throw new Exception("Quantidade inválida");
        this.quantidade = quantidade;
    }

    // GETTERS

    public String getNome() { return nome; }
    public TipoItem getTipo() { return tipo; }
    public float getValor() { return valor; }
    public int getQuantidade() { return quantidade; }

    // MÉTODOS

    public boolean usarBatalha(Personagem jogador, Inimigo inimigo) {
        switch (tipo) {
            case CURA:
                return jogador.curar((int) valor);
            case ATIRAVEL:
                inimigo.sofrerDano((int) valor);
                return true;
            default:
                System.out.println("Esse item precisa ser equipado.");
                return false;
        }
    }

    public boolean usar(Personagem jogador) {
        switch (tipo) {
            case CURA:
                return jogador.curar((int) valor);
            case ATIRAVEL:
                System.out.println("Esse item só pode ser usado em batalha!");
                return false;
            default:
                System.out.println("Esse item precisa ser equipado.");
                return false;
        }
    }

    // REIMPLEMENTAÇÕES

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

    @Override
    public int compareTo(Item i){
        if(this.nome.compareTo(i.nome) > 0) return 1;
        if(this.nome.compareTo(i.nome) < 0) return -1;
        return 0;
    }
}
