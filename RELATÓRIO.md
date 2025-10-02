---

# Relatório Técnico - Sistema de Medicamentos

## 1. Introdução

Este documento descreve as decisões técnicas e funcionalidades implementadas no Sistema de Gerenciamento de Medicamentos.

## 2. Funcionalidades Implementadas

### 2.1 Cadastro de Medicamentos
- Formulário completo com validação de todos os campos
- Composição com a classe Fornecedor
- Validação de CNPJ com algoritmo de dígitos verificadores
- Validação de email, telefone e demais campos

### 2.2 Persistência em CSV
- Arquivo delimitado por ponto-e-vírgula (;)
- Encoding UTF-8 para suportar caracteres especiais
- Leitura e escrita automática
- Tratamento de erros de IO

### 2.3 Interface Gráfica
- 3 abas: Cadastro, Listagem e Relatórios
- TableView com 8 colunas
- Formatação customizada para preço e booleanos
- Seleção de linha preenche formulário
- Feedback visual para o usuário

# Decisões de Design - Sistema de Gerenciamento de Medicamentos

## 1. Estrutura e Organização Visual

### **Hierarquia de Informações**
- **Cabeçalho principal** em destaque para identificação rápida do sistema
- **Menu lateral** com opções principais (Cadastro, Listagem, Relatórios)
- **Divisão por seções** claramente demarcadas (Dados do Medicamento × Dados do Fornecedor)
- **Rodapé** com informações de versão e desenvolvimento

### **Layout em Tabelas**
- Utilização de tabelas para organização dos campos de formulário
- Alinhamento consistente em colunas para melhor legibilidade
- Separação visual entre diferentes grupos de informações

## 2. Design de Formulário e Campos

### **Campos Obrigatórios vs Opcionais**
- Indicação clara de campos obrigatórios (*) seguindo convenções de UX
- Organização lógica dos campos por categoria (identificação, dados técnicos, fornecedor)

### **Agrupamento Semântico**
- **Dados do Medicamento**: informações básicas, princípio ativo, validade, estoque
- **Dados do Fornecedor**: informações corporativas e de contato
- Campos relacionados mantidos próximos visualmente

## 3. Elementos de Interface Específicos

### **Medicamento Controlado**
- Checkbox destacado para identificação rápida de medicamentos especiais
- Posicionamento estratégico próximo a dados críticos

### **Formatação de Dados**
- Campos com máscaras específicas (CNPJ, Data, Telefone)
- Formatação monetária clara (R$)
- Validação visual de dados (error message visível)

## 4. Feedback e Estados do Sistema

### **Tratamento de Erros**
- Mensagem de erro destacada em localização central
- Indicação específica do problema ("For input string: 'TEST001'")
- Manutenção da interface funcional mesmo com erros

### **Consistência Visual**
- Paleta de cores sóbria e profissional adequada para saúde
- Tipografia legível e hierarquia clara
- Espaçamento consistente entre elementos

## 5. Considerações de Usabilidade

### **Fluxo de Trabalho**
- Design orientado para tarefas repetitivas de cadastro
- Campos organizados na sequência lógica de preenchimento
- Informações críticas (validade, quantidade) em destaque

### **Acessibilidade**
- Rótulos claros e descritivos
- Agrupamento visual relacionado
- Diferenciação entre seções com linhas separadoras

