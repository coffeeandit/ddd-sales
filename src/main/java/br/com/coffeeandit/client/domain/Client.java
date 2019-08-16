package br.com.coffeeandit.client.domain;

import java.util.Objects;

public class Client {
    public Client(String nome, Long CPF) {
        this.nome = nome;
        this.CPF = CPF;
    }

    @Override
    public String toString() {
        return "Client{" +
                "nome='" + nome + '\'' +
                ", CPF=" + CPF +
                '}';
    }

    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(CPF, client.CPF);
    }

    @Override
    public int hashCode() {
        return Objects.hash(CPF);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCPF(Long CPF) {
        this.CPF = CPF;
    }

    public Long getCPF() {
        return CPF;
    }

    private String nome;
    private Long CPF;
}
