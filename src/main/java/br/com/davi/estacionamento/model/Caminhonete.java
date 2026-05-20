package br.com.davi.estacionamento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
@DiscriminatorValue("caminhonete")
public class Caminhonete extends Veiculo {

    public Caminhonete() {
    }

    public Caminhonete(String placa, String modelo, String cor) {
        super(placa, modelo, cor);
    }

    @Override
    public BigDecimal calcularValor(BigDecimal valorBase) {
        return valorBase.multiply(new BigDecimal("1.50"));
    }

    @Override
    public String getTipo() {
        return "Caminhonete";
    }
}