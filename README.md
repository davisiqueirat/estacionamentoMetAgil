
O projeto desenvolvido é um sistema de estacionamento em Java, executado pelo terminal, utilizando JPA/Hibernate para persistência dos dados e banco H2 como banco de dados.

O sistema permite cadastrar veículos, registrar entrada, registrar saída, calcular o valor a pagar, listar veículos estacionados no momento e exibir o histórico de movimentações.

1. CLASSES UTILIZADAS

Classe Veiculo

A classe Veiculo é uma classe abstrata que representa as informações comuns de todos os veículos do sistema. Ela possui os atributos id, placa, modelo e cor.

Essa classe também possui o método abstrato calcularValor, que é implementado pelas classes filhas. Dessa forma, o sistema utiliza polimorfismo para calcular o valor de acordo com o tipo do veículo.

Classe Carro

A classe Carro herda da classe Veiculo. Ela representa veículos do tipo carro.

No cálculo do valor, o carro paga o valor normal da cobrança, sem desconto ou acréscimo.

Classe Moto

A classe Moto também herda da classe Veiculo. Ela representa veículos do tipo moto.

No cálculo do valor, a moto paga 50% do valor da cobrança, conforme a regra definida no projeto.

Classe Caminhonete

A classe Caminhonete herda da classe Veiculo. Ela representa veículos do tipo caminhonete.

No cálculo do valor, a caminhonete paga 150% do valor da cobrança, ou seja, o valor normal com acréscimo de 50%.

Classe Vaga

A classe Vaga representa uma vaga do estacionamento. Ela possui os atributos id, numero e ocupada.

O atributo numero identifica a vaga e o atributo ocupada informa se a vaga está livre ou ocupada.

Classe Movimentacao

A classe Movimentacao representa a entrada e saída de um veículo no estacionamento.

Ela possui os atributos id, veiculo, vaga, dataEntrada, dataSaida e valorPago.

Quando um veículo entra no estacionamento, é criada uma movimentação com a data de entrada. Quando o veículo sai, a movimentação é atualizada com a data de saída e o valor pago.

Classe Estacionamento

A classe Estacionamento contém as principais regras do sistema.

Ela é responsável por cadastrar veículos, registrar entradas, registrar saídas, calcular valores, listar veículos estacionados e mostrar o histórico de movimentações.

Nessa classe também são feitas as validações principais, como impedir placa duplicada, impedir entrada de veículo já estacionado, impedir uso de vaga ocupada e impedir saída de veículo que não está estacionado.

Classe Main

A classe Main é a classe principal do sistema. Ela exibe o menu no terminal e recebe as opções digitadas pelo usuário.

Através dela, o usuário consegue acessar as funcionalidades do sistema.

2. HERANÇA E POLIMORFISMO

O projeto utiliza herança através da classe Veiculo.

As classes Carro, Moto e Caminhonete herdam de Veiculo, reaproveitando os atributos placa, modelo e cor.

O polimorfismo é aplicado no método calcularValor. Cada classe filha implementa esse método de uma forma diferente:

- Carro: paga o valor normal.
- Moto: paga 50% do valor.
- Caminhonete: paga 150% do valor.

Dessa forma, o sistema consegue calcular o valor corretamente dependendo do tipo do veículo.

3. TABELAS UTILIZADAS

Tabela veiculos

A tabela veiculos armazena os veículos cadastrados no sistema.

Campos principais:

- id: identificador do veículo.
- placa: placa do veículo.
- modelo: modelo do veículo.
- cor: cor do veículo.
- tipo: tipo do veículo, podendo ser carro, moto ou caminhonete.

Essa tabela é usada para armazenar os dados das classes Veiculo, Carro, Moto e Caminhonete.

Tabela vagas

A tabela vagas armazena as vagas do estacionamento.

Campos principais:

- id: identificador da vaga.
- numero: número da vaga.
- ocupada: informa se a vaga está ocupada ou livre.

Essa tabela é usada para controlar quais vagas podem ser utilizadas no momento da entrada do veículo.

Tabela movimentacoes

A tabela movimentacoes armazena o histórico de entradas e saídas dos veículos.

Campos principais:

- id: identificador da movimentação.
- veiculo_id: referência ao veículo.
- vaga_id: referência à vaga utilizada.
- dataEntrada: data e hora da entrada do veículo.
- dataSaida: data e hora da saída do veículo.
- valorPago: valor pago ao sair do estacionamento.

Essa tabela permite saber quais veículos ainda estão estacionados e também consultar o histórico de movimentações já realizadas.

4. REGRAS DE NEGÓCIO IMPLEMENTADAS

O sistema implementa as seguintes regras:

- Não permite cadastrar duas vezes a mesma placa.
- Não permite registrar entrada de um veículo que já está estacionado.
- Não permite usar uma vaga que já está ocupada.
- Não permite registrar saída de um veículo que não está estacionado.
- Não permite registrar saída sem que exista uma data de entrada.
- Calcula o valor a pagar conforme o tempo estacionado e o tipo do veículo.

5. REGRAS DE COBRANÇA

A cobrança do estacionamento funciona da seguinte forma:

- Até 1 hora: R$ 5,00.
- Cada hora adicional: R$ 3,00.
- Moto paga 50% do valor.
- Caminhonete paga 150% do valor.
- Carro paga o valor normal.

6. CONCLUSÃO

O sistema atende aos requisitos propostos, utilizando conceitos de programação orientada a objetos, como atributos, métodos, encapsulamento, herança e polimorfismo.

Além disso, utiliza JPA/Hibernate para persistência dos dados no banco H2, mantendo as informações de veículos, vagas e movimentações armazenadas em tabelas.
