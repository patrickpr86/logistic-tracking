# Documentação do Projeto - Sistema de Rastreamento de Pacotes

## 1. Visão Geral
Este projeto consiste em uma aplicação **Spring Boot 3.0.1** em **Java 17** que gerencia o rastreamento de pacotes em um sistema de logística de alta escala. O sistema fornece **APIs RESTful** seguindo os princípios **HATEOAS** e integra diversas tecnologias para garantir escalabilidade, performance e resiliência.

## 2. Tecnologias Utilizadas
- **Spring Boot 3.0.1**
- **Java 17**
- **MySQL**
- **Redis (Cache)**
- **Kafka (Mensageria)**
- **Docker (Containers)**
- **HATEOAS (Hypermedia API)**
- **HikariCP (Gerenciamento de Conexões)**

---

## 3. Modelagem do Banco de Dados
### Decisões de Design:
- **Normalização**: O banco foi estruturado para evitar redundâncias e otimizar buscas.
- **Particionamento**: Aplicado **partitioning** para distribuir os dados e melhorar o desempenho das queries.
- **Indexação**: Índices criados para consultas frequentes.
- **Expurgo de Dados**: Arquivamento e remoção automática de registros antigos via eventos do MySQL.

---

## 4. Estratégias de Escalabilidade e Otimização
### Banco de Dados:
- Uso de **índices** para otimizar buscas.
- **Particionamento por ano** para grandes tabelas como `packages` e `tracking_events`.
- **Expurgo automático** de dados antigos via eventos SQL.

### Processamento Assíncrono:
- Utilização do **Kafka** para eventos de rastreamento, permitindo um fluxo escalável e resiliente.
- Uso de **ExecutorService** para operações assíncronas de forma eficiente.

### Cache:
- **Redis** utilizado para melhorar a performance nas consultas repetitivas de pacotes.

---

## 5. Gestão de Conexões do MySQL
- **HikariCP** configurado com:
    - Máximo de 50 conexões simultâneas.
    - Validação automática das conexões.
    - Tempo de inatividade e reaproveitamento ajustados para alta carga.

---

## 6. Gestão de Logs e Métricas
- **Kafka** é utilizado para processar eventos de rastreamento.
- **Spring Boot Actuator** integrado para expor métricas.
- **Splunk** deveria ser configurado para agregar logs e métricas.
- **Grafana** deveria ser configurado para alertas e monitoramento.

### 🔴 **Pontos que Faltaram**
- Testes unitários e E2E (falta de tempo).
- Configuração do **Splunk** para logs e métricas.
- Configuração do **Grafana** para alertas.

---

## 7. Como Executar a Aplicação
### **Pré-requisitos**
- Docker e Docker Compose instalados.
- Java 17 e Maven instalados.

### **Passos**
1. Clone o repositório:


2. Inicie os containers:
   ```sh
   docker-compose up -d
   ```

3. Compile e rode a aplicação:
   ```sh
   mvn clean package
   java -jar target/app.jar
   ```

4. Acesse a API em:
   ```
   http://localhost:8080/
   ```

---
