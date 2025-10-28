package com.rpg.personagens;

import com.rpg.inventario.Inventario;
import com.rpg.inventario.Item;
import com.rpg.jogo.Jogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Personagem implements Cloneable {

    // ATRIBUTOS

    private final String nome;
    private int pontosVida;
    private int ataque;
    private int defesa;
    private final int nivel;
    private final Inventario inventario;
    private Item armaFisica;
    private Item armaDistancia;
    private Item armadura;
    private boolean temAliado = false;
    private boolean emBatalha = false;
    private final int vidaMaxima;

    // CONSTRUTOR

    public Personagem(String nome, int pontosVida, int ataque, int defesa, int vidaMaxima) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = 1;
        this.inventario = new Inventario();
        this.vidaMaxima = vidaMaxima;
    }

    //GETTERS

    public String getNome() { return this.nome; }
    public int getPontosVida() { return this.pontosVida; }
    public int getAtaque() { return ataque; }
    public int getNivel() { return nivel; }
    public int getDefesa() { return defesa; }
    public Inventario getInventario() { return inventario; }
    public int getVidaMaxima() { return vidaMaxima; }

    // SETTERS -> TO-DO

    public void setTemAliado(boolean temAliado) { this.temAliado = temAliado; }
    public void setEmBatalha(boolean emBatalha) { this.emBatalha = emBatalha; }
    public void setPontosVida(int pontosVida) { this.pontosVida = pontosVida; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public void setDefesa(int defesa) { this.defesa = defesa; }

    // MÉTODOS

    public boolean estaVivo() { return pontosVida > 0; }

    public void tratarMorte(Personagem jogador, Aliado aliado, Inimigo inimigo, Jogo jogo, int progressao) throws Exception {
        Scanner sc = new Scanner(System.in);
        if (!jogador.estaVivo()) {
            System.out.println("\n======= 💀 VOCÊ MORREU 💀 =======\n" +
                    "O que deseja fazer?\n" +
                    "   [1] - Tentar novamente\n" +
                    "   [2] - Sair do jogo");
            System.out.print("Escolha: ");

            int escolha = sc.nextInt();
            sc.nextLine();

            switch (escolha) {
                case 1 -> {
                    System.out.println("\nRetomando...");
                    jogador.restaurarVidaTotal();
                    inimigo.restaurarVidaTotal();
                    System.out.println();
                    if (jogador.emBatalha()) {
                        if (jogador.temAliado()) {
                            aliado.restaurarVidaTotal();
                            jogo.batalharComAliado(jogador, aliado, inimigo);
                        } else {
                            jogo.batalhar(jogador, inimigo);
                        }
                    } else {
                        jogo.explorar(progressao - 1);
                    }
                }

                case 2 -> {
                    System.out.println("\nVocê decide encerrar sua jornada. Até a próxima!");
                    System.exit(0); // encerra o jogo
                }

                default -> {
                    System.out.println("Opção inválida! Escolha novamente.");
                    tratarMorte(jogador, aliado, inimigo, jogo, progressao);
                }
            }
        }
    }

    public void restaurarVidaTotal() {
        this.pontosVida = this.vidaMaxima;
    }

    public void sofrerDano(int dano) {
        this.pontosVida -= dano;
        if (this.pontosVida < 0) this.pontosVida = 0;
    }

    public boolean temAliado() {
        return temAliado;
    }

    public boolean emBatalha() {
        return emBatalha;
    }

    public boolean curar(int valor) {
        Scanner sc = new Scanner(System.in);

        if (this.pontosVida + valor > vidaMaxima) {
            System.out.println("⚠️ Você passará dos seus pontos máximos de vida e perderá parte da cura! ⚠️\n" +
                    "Deseja usar mesmo assim?\n" +
                    "   [1] - Sim\n" +
                    "   [2] - Não\n" +
                    "Escolha: ");
            int escolha = sc.nextInt();
            sc.nextLine();

            if (escolha == 1) {
                this.pontosVida = vidaMaxima;
                System.out.println(getNome() + " recuperou até o máximo de HP.\n" +
                        "Pontos de vida: " + getPontosVida());
                return true;
            } else {
                System.out.println("Item não usado.");
                return false;
            }
        } else {
            this.pontosVida += valor;
            System.out.println(getNome() + " recuperou " + valor + " de HP.\n" +
                    "Pontos de vida: " + getPontosVida());
            return true;
        }
    }

    public void equipar(Item item) throws Exception {
        switch (item.getTipo()) {
            case FISICO:
                if (this.armaFisica != null)
                    throw new Exception("Você já tem uma arma física equipada!");
                this.armaFisica = item;
                this.ataque += (int) item.getValor();
                break;

            case DISTANCIA:
                if (this.armaDistancia != null)
                    throw new Exception("Você já tem uma arma de distância equipada!");
                this.armaDistancia = item;
                this.ataque += (int) item.getValor();
                break;

            case EQUIPAVEL:
                if (this.armadura != null)
                    throw new Exception("Você já está usando uma armadura!");
                this.armadura = item;
                this.defesa += (int) item.getValor();
                break;

            default:
                throw new Exception("Esse item não é equipável!");
        }
    }

    public void desequipar(Item item) throws Exception {
        switch (item.getTipo()) {
            case FISICO:
                if (this.armaFisica == null)
                    throw new Exception("Você não tem uma arma física equipada!");
                this.ataque -= (int) this.armaFisica.getValor();
                this.armaFisica = null;
                break;

            case DISTANCIA:
                if (this.armaDistancia == null)
                    throw new Exception("Você não tem uma arma de distância equipada!");
                this.ataque -= (int) this.armaDistancia.getValor();
                this.armaDistancia = null;
                break;

            case EQUIPAVEL:
                if (this.armadura == null)
                    throw new Exception("Você não tem uma armadura equipada!");
                this.defesa -= (int) this.armadura.getValor();
                this.armadura = null;
                break;

            default:
                throw new Exception("Esse tipo de item não pode ser desequipado!");
        }
    }

    public Item getItemEquipadoDoTipo(Item.TipoItem tipo) {
        switch (tipo) {
            case FISICO: return armaFisica;
            case DISTANCIA: return armaDistancia;
            case EQUIPAVEL: return armadura;
            default: return null;
        }
    }

    public List<Item> getItensEquipados() {
        List<Item> equipados = new ArrayList<>();
        if (armaFisica != null) equipados.add(armaFisica);
        if (armaDistancia != null) equipados.add(armaDistancia);
        if (armadura != null) equipados.add(armadura);
        return equipados;
    }

    @Override
    public String toString() {
        return nome + " (HP: " + pontosVida + ", Atk: " + ataque + ", Def: " + defesa + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Personagem p = (Personagem) obj;
        if(!this.nome.equals(p.nome)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;
        ret = ret * 2 + this.nome.hashCode();
        if(ret < 0) ret=-ret;
        return ret;
    }
}