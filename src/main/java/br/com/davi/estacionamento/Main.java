package br.com.davi.estacionamento;

import br.com.davi.estacionamento.service.Estacionamento;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Estacionamento estacionamento = new Estacionamento();

        estacionamento.criarVagasIniciais();

        int opcao;

        do {
            System.out.println("\n===== SISTEMA DE ESTACIONAMENTO =====");
            System.out.println("1 - Cadastrar veículo");
            System.out.println("2 - Registrar entrada");
            System.out.println("3 - Registrar saída");
            System.out.println("4 - Listar veículos estacionados");
            System.out.println("5 - Mostrar histórico");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    System.out.print("Placa: ");
                    String placa = scanner.nextLine();

                    System.out.print("Modelo: ");
                    String modelo = scanner.nextLine();

                    System.out.print("Cor: ");
                    String cor = scanner.nextLine();

                    System.out.print("Tipo (carro, moto ou caminhonete): ");
                    String tipo = scanner.nextLine();

                    estacionamento.cadastrarVeiculo(placa, modelo, cor, tipo);
                    break;

                case 2:
                    System.out.print("Placa do veículo: ");
                    String placaEntrada = scanner.nextLine();

                    System.out.print("Número da vaga: ");
                    Integer numeroVaga = Integer.parseInt(scanner.nextLine());

                    estacionamento.registrarEntrada(placaEntrada, numeroVaga);
                    break;

                case 3:
                    System.out.print("Placa do veículo: ");
                    String placaSaida = scanner.nextLine();

                    estacionamento.registrarSaida(placaSaida);
                    break;

                case 4:
                    estacionamento.listarVeiculosEstacionados();
                    break;

                case 5:
                    estacionamento.mostrarHistorico();
                    break;

                case 0:
                    System.out.println("Sistema encerrado.");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);

        scanner.close();
    }
}