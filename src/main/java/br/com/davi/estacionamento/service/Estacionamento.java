package br.com.davi.estacionamento.service;

import br.com.davi.estacionamento.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Estacionamento {

    private EntityManagerFactory emf;
    private DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public Estacionamento() {
        emf = Persistence.createEntityManagerFactory("estacionamentoPU");
    }

    public void criarVagasIniciais() {
        EntityManager em = emf.createEntityManager();

        try {
            Long quantidade = em.createQuery("SELECT COUNT(v) FROM Vaga v", Long.class)
                    .getSingleResult();

            if (quantidade == 0) {
                em.getTransaction().begin();

                for (int i = 1; i <= 10; i++) {
                    em.persist(new Vaga(i));
                }

                em.getTransaction().commit();

                System.out.println("10 vagas criadas com sucesso.");
            }

        } finally {
            em.close();
        }
    }

    public void cadastrarVeiculo(String placa, String modelo, String cor, String tipo) {
        EntityManager em = emf.createEntityManager();

        try {
            placa = placa.toUpperCase();

            Long existe = em.createQuery(
                            "SELECT COUNT(v) FROM Veiculo v WHERE v.placa = :placa", Long.class)
                    .setParameter("placa", placa)
                    .getSingleResult();

            if (existe > 0) {
                System.out.println("Erro: já existe um veículo cadastrado com essa placa.");
                return;
            }

            Veiculo veiculo;

            if (tipo.equalsIgnoreCase("carro")) {
                veiculo = new Carro(placa, modelo, cor);
            } else if (tipo.equalsIgnoreCase("moto")) {
                veiculo = new Moto(placa, modelo, cor);
            } else if (tipo.equalsIgnoreCase("caminhonete")) {
                veiculo = new Caminhonete(placa, modelo, cor);
            } else {
                System.out.println("Erro: tipo inválido.");
                return;
            }

            em.getTransaction().begin();
            em.persist(veiculo);
            em.getTransaction().commit();

            System.out.println("Veículo cadastrado com sucesso.");

        } finally {
            em.close();
        }
    }

    public void registrarEntrada(String placa, Integer numeroVaga) {
        EntityManager em = emf.createEntityManager();

        try {
            placa = placa.toUpperCase();

            Veiculo veiculo = buscarVeiculoPorPlaca(em, placa);

            if (veiculo == null) {
                System.out.println("Erro: veículo não cadastrado.");
                return;
            }

            Movimentacao movimentacaoAberta = buscarMovimentacaoAberta(em, placa);

            if (movimentacaoAberta != null) {
                System.out.println("Erro: esse veículo já está estacionado.");
                return;
            }

            Vaga vaga = buscarVagaPorNumero(em, numeroVaga);

            if (vaga == null) {
                System.out.println("Erro: vaga não encontrada.");
                return;
            }

            if (vaga.getOcupada()) {
                System.out.println("Erro: essa vaga já está ocupada.");
                return;
            }

            Movimentacao movimentacao = new Movimentacao(veiculo, vaga, LocalDateTime.now());

            em.getTransaction().begin();

            vaga.setOcupada(true);
            em.persist(movimentacao);

            em.getTransaction().commit();

            System.out.println("Entrada registrada com sucesso.");

        } finally {
            em.close();
        }
    }

    public void registrarSaida(String placa) {
        EntityManager em = emf.createEntityManager();

        try {
            placa = placa.toUpperCase();

            Movimentacao movimentacao = buscarMovimentacaoAberta(em, placa);

            if (movimentacao == null) {
                System.out.println("Erro: esse veículo não está estacionado.");
                return;
            }

            if (movimentacao.getDataEntrada() == null) {
                System.out.println("Erro: movimentação sem data de entrada.");
                return;
            }

            LocalDateTime dataSaida = LocalDateTime.now();
            BigDecimal valor = calcularValor(movimentacao, dataSaida);

            em.getTransaction().begin();

            movimentacao.setDataSaida(dataSaida);
            movimentacao.setValorPago(valor);
            movimentacao.getVaga().setOcupada(false);

            em.getTransaction().commit();

            System.out.println("Saída registrada com sucesso.");
            System.out.println("Valor a pagar: R$ " + valor);

        } finally {
            em.close();
        }
    }

    private BigDecimal calcularValor(Movimentacao movimentacao, LocalDateTime dataSaida) {
        LocalDateTime dataEntrada = movimentacao.getDataEntrada();

        long minutos = Duration.between(dataEntrada, dataSaida).toMinutes();

        if (minutos <= 0) {
            minutos = 1;
        }

        BigDecimal valorBase;

        if (minutos <= 60) {
            valorBase = new BigDecimal("5.00");
        } else {
            long minutosAdicionais = minutos - 60;
            long horasAdicionais = (long) Math.ceil(minutosAdicionais / 60.0);

            valorBase = new BigDecimal("5.00")
                    .add(new BigDecimal("3.00").multiply(new BigDecimal(horasAdicionais)));
        }

        BigDecimal valorFinal = movimentacao.getVeiculo().calcularValor(valorBase);

        return valorFinal.setScale(2, RoundingMode.HALF_UP);
    }

    public void listarVeiculosEstacionados() {
        EntityManager em = emf.createEntityManager();

        try {
            List<Movimentacao> movimentacoes = em.createQuery(
                            "SELECT m FROM Movimentacao m WHERE m.dataSaida IS NULL",
                            Movimentacao.class)
                    .getResultList();

            if (movimentacoes.isEmpty()) {
                System.out.println("Nenhum veículo estacionado no momento.");
                return;
            }

            System.out.println("\nVeículos estacionados:");

            for (Movimentacao m : movimentacoes) {
                System.out.println("-----------------------------");
                System.out.println("Placa: " + m.getVeiculo().getPlaca());
                System.out.println("Modelo: " + m.getVeiculo().getModelo());
                System.out.println("Cor: " + m.getVeiculo().getCor());
                System.out.println("Tipo: " + m.getVeiculo().getTipo());
                System.out.println("Vaga: " + m.getVaga().getNumero());
                System.out.println("Entrada: " + m.getDataEntrada().format(formatador));
            }

        } finally {
            em.close();
        }
    }

    public void mostrarHistorico() {
        EntityManager em = emf.createEntityManager();

        try {
            List<Movimentacao> movimentacoes = em.createQuery(
                            "SELECT m FROM Movimentacao m ORDER BY m.id",
                            Movimentacao.class)
                    .getResultList();

            if (movimentacoes.isEmpty()) {
                System.out.println("Nenhuma movimentação registrada.");
                return;
            }

            System.out.println("\nHistórico de movimentações:");

            for (Movimentacao m : movimentacoes) {
                System.out.println("-----------------------------");
                System.out.println("Placa: " + m.getVeiculo().getPlaca());
                System.out.println("Tipo: " + m.getVeiculo().getTipo());
                System.out.println("Vaga: " + m.getVaga().getNumero());
                System.out.println("Entrada: " + m.getDataEntrada().format(formatador));

                if (m.getDataSaida() != null) {
                    System.out.println("Saída: " + m.getDataSaida().format(formatador));
                } else {
                    System.out.println("Saída: ainda estacionado");
                }
                System.out.println("Valor pago: R$ " + m.getValorPago());
            }

        } finally {
            em.close();
        }
    }

    private Veiculo buscarVeiculoPorPlaca(EntityManager em, String placa) {
        List<Veiculo> veiculos = em.createQuery(
                        "SELECT v FROM Veiculo v WHERE v.placa = :placa",
                        Veiculo.class)
                .setParameter("placa", placa)
                .getResultList();

        if (veiculos.isEmpty()) {
            return null;
        }

        return veiculos.get(0);
    }

    private Vaga buscarVagaPorNumero(EntityManager em, Integer numero) {
        List<Vaga> vagas = em.createQuery(
                        "SELECT v FROM Vaga v WHERE v.numero = :numero",
                        Vaga.class)
                .setParameter("numero", numero)
                .getResultList();

        if (vagas.isEmpty()) {
            return null;
        }

        return vagas.get(0);
    }

    private Movimentacao buscarMovimentacaoAberta(EntityManager em, String placa) {
        List<Movimentacao> movimentacoes = em.createQuery(
                        "SELECT m FROM Movimentacao m WHERE m.veiculo.placa = :placa AND m.dataSaida IS NULL",
                        Movimentacao.class)
                .setParameter("placa", placa)
                .getResultList();

        if (movimentacoes.isEmpty()) {
            return null;
        }

        return movimentacoes.get(0);
    }
}