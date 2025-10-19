package com.rpg.personagens;

import com.rpg.inventario.Inventario;

public class Personagem {
    private String nome;
    private int pontosVida;
    private int ataque;
    private int defesa;
    private int nivel;
    private Inventario inventario;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPontosVida() {
        return pontosVida;
    }

    public void setPontosVida(int pontosVida) throws Exception {
        if(pontosVida <= 100 && pontosVida > 0){
            this.pontosVida = pontosVida;
        } else{
            throw new Exception("Valor de PV inválido");
        }
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) throws Exception {
        if(ataque >= 0){
            this.ataque = ataque;
        } else{
            throw new Exception("Valor de ATK negativo");
        }
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) throws Exception{
        if(defesa >= 0){
            this.defesa = defesa;
        } else{
            throw new Exception("Valor de DEF negativo");
        }
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) throws Exception {
        if(nivel >= 0){
            this.nivel = nivel;
        } else{
            throw new Exception("Valor de NVL negativo");
        }
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }
}
