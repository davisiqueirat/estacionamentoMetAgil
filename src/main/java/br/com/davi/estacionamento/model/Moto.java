package br.com.davi.estacionamento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("moto")
public class Moto extends Veiculo {

    public Moto() {
    }

    public Moto(String placa, String modelo, String cor) {
        super(placa, modelo, cor);
    }

    @Override
    public BigDecimal calcularValor(BigDecimal valorBase) {
        return valorBase.multiply(new BigDecimal("0.50"));
    }

    @Override
    public String getTipo() {
        return "Moto";
    }
}