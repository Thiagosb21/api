# Desafio Senior

Este projeto foi desenvolvido em Java 17 utilizando o framework Spring Boot 3.0.0. Para facilitar o desenvolvimento, foi utilizada a biblioteca Lombok.

Os dados da aplicação são armazenados de forma persistente no banco de dados PostgreSQL.
Além disso, foram realizados testes unitários com as bibliotecas JUnit e Mockito. Esses testes garantem que as diferentes partes do sistema funcionem corretamente e possibilitam a identificação de possíveis falhas durante o desenvolvimento.


## Funcionalidades

- Busca por hóspedes sem CheckIn
- Busca por hóspedes sem CheckOut
- Busca por hóspede pelo telefone
- Busca por hóspede pelo nome
- Busca por hóspede pelo CPF
- Cadastro de hóspedes
- Criação de reservas
- CheckIn
- CheckOut

## Instalação
Esta disponibilizado dentro do projeto na pasta collection um arquivo para realizar as chamadas das funcionalidades.


Instruções sobre como instalar o projeto.

```bash
# Comando para clonar o repositório
git clone https://github.com/Thiagosb21/api.git

# Ajuste no application.properties para seu servidor com porta, usuario e senha
spring.datasource.url=jdbc:postgresql://localhost:5433/hotel_db
spring.datasource.username=postgres
spring.datasource.password=admin

# Comando para baixar as depedencias
mvn clean install