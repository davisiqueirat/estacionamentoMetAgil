package br.com.davi.estacionamento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vagas")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false)
    private Boolean ocupada = false;

    public Vaga() {
    }

    public Vaga(Integer numero) {
        this.numero = numero;
        this.ocupada = false;
    }

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public Boolean getOcupada() {
        return ocupada;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public void setOcupada(Boolean ocupada) {
        this.ocupada = ocupada;
    }
}