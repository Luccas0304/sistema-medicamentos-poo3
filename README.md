# Sistema de Gerenciamento de Medicamentos

Sistema desenvolvido em Java com interface gráfica JavaFX para gerenciar o estoque de medicamentos de uma farmácia.

## 📋 Requisitos

- **Java JDK**: versão 11 ou superior
- **JavaFX**: versão 11 ou superior
- **IDE**: IntelliJ IDEA, Eclipse ou NetBeans (recomendado: IntelliJ IDEA)
- **Scene Builder**: (opcional) para editar arquivos FXML

## 🚀 Como Executar o Programa

### Opção 1: Executar via IntelliJ IDEA

1. **Clone ou descompacte o projeto**
   ```bash
   git clone https://github.com/Luccas0304/sistema-medicamentos-poo3
   cd SistemaMedicamentos
   ```

2. **Abra o projeto no IntelliJ IDEA**
    - File → Open → Selecione a pasta do projeto

3. **Configure o JavaFX** (se necessário)
    - File → Project Structure → Libraries
    - Adicione o SDK do JavaFX se não estiver configurado

4. **Execute a aplicação**
    - Localize a classe `App.java` em `src/main/java/com/farmacia/service/`
    - Clique com botão direito → Run 'App.main()'

### Opção 2: Executar via linha de comando

```bash
# Compilar o projeto
javac --module-path /caminho/para/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -d out src/main/java/com/farmacia/**/*.java

# Executar
java --module-path /caminho/para/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml -cp out com.farmacia.service.App
```

### Opção 3: Executar o JAR (se disponível)

```bash
java -jar SistemaMedicamentos.jar
```

## 📁 Estrutura do Projeto

```
SistemaMedicamentos/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── farmacia/
│                   ├── controller/
│                   │   └── MedicamentoController.java
│                   ├── dao/
│                   │   └── MedicamentoDAO.java
│                   ├── exception/
│                   │   ├── PersistenciaException.java
│                   │   └── ValidacaoException.java
│                   ├── model/
│                   │   ├── Fornecedor.java
│                   │   └── Medicamento.java
│                   └── service/
│                       ├── App.java
│                       └── MedicamentoService.java
├── resources/
│   ├── css/
│   │   └── styles.css
│   └── fxml/
│       └── main-view.fxml
├── data/
│   └── medicamentos.csv
└── README.md
```

## 💻 Funcionalidades

### 1. Cadastro de Medicamentos
- Cadastrar novos medicamentos com todos os dados necessários
- Dados do medicamento: código, nome, descrição, princípio ativo, data de validade, quantidade, preço, controlado
- Dados do fornecedor: CNPJ, razão social, telefone, email, cidade, estado

### 2. Consulta e Listagem
- Buscar medicamento por código específico
- Listar todos os medicamentos cadastrados em tabela
- Visualização organizada com todas as informações

### 3. Atualização
- Editar dados de medicamentos existentes
- Seleção via tabela para preenchimento automático do formulário

### 4. Exclusão
- Remover medicamentos do sistema
- Confirmação antes da exclusão para evitar remoções acidentais

### 5. Relatórios com Stream API
- **Medicamentos Próximos ao Vencimento**: Lista medicamentos que vencem nos próximos 30 dias
- **Estoque Baixo**: Identifica medicamentos com menos de 5 unidades
- **Valor Total por Fornecedor**: Calcula o valor total em estoque agrupado por fornecedor
- **Controlados vs Não Controlados**: Estatísticas sobre medicamentos controlados
- **Estatísticas Gerais**: Visão geral do estoque (total, valor, médias)

## 🔒 Validações Implementadas

### Medicamento
- Código: 7 caracteres alfanuméricos obrigatório
- Nome: mínimo 3 caracteres
- Data de validade: não pode ser data passada
- Quantidade: não pode ser negativa
- Preço: deve ser maior que zero

### Fornecedor
- CNPJ: 14 dígitos com validação de dígitos verificadores
- Email: formato válido
- Estado: sigla com 2 caracteres
- Todos os campos obrigatórios

## 💾 Persistência de Dados

Os dados são armazenados em arquivo CSV:
- **Local**: `data/medicamentos.csv`
- **Formato**: Campos separados por ponto-e-vírgula (;)
- **Codificação**: UTF-8
- **Cabeçalho**: Primeira linha com nomes das colunas

Exemplo:
```csv
codigo;nome;descricao;principioAtivo;dataValidade;quantidadeEstoque;preco;controlado;cnpj;razaoSocial;telefone;email;cidade;estado
MED001;Dipirona;Analgésico;Dipirona Sódica;2026-06-15;50;8.90;false;12345678000190;FarmaDistribuidora;1133334444;contato@farma.com;São Paulo;SP
```

## 🎨 Interface Gráfica

O sistema possui três abas principais:

1. **Cadastro**: Formulário completo para cadastro e edição
2. **Listagem**: Tabela com todos os medicamentos e busca
3. **Relatórios**: Geração de relatórios diversos com Stream API

## ⚠️ Tratamento de Erros

- Validações de entrada com mensagens claras
- Exceções personalizadas (ValidacaoException, PersistenciaException)
- Alertas visuais para o usuário
- Logs de erro no console para debug

## 🛠️ Tecnologias Utilizadas

- **Java 11+**
- **JavaFX**: Interface gráfica
- **Stream API**: Processamento de dados e relatórios
- **CSV**: Persistência de dados
- **MVC Pattern**: Organização do código

## 👨‍💻 Autor

Projeto desenvolvido para a disciplina POO3

## 📝 Licença

Projeto acadêmico - Uso educacional