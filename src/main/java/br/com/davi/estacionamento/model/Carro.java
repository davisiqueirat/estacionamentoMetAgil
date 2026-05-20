package br.com.davi.estacionamento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("carro")
public class Carro extends Veiculo {

    public Carro() {
    }

    public Carro(String placa, String modelo, String cor) {
        super(placa, modelo, cor);
    }

    @Override
    public BigDecimal calcularValor(BigDecimal valorBase) {
        return valorBase;
    }

    @Override
    public String getTipo() {
        return "Carro";
    }
}