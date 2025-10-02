package com.farmacia.controller;

import com.farmacia.exception.PersistenciaException;
import com.farmacia.exception.ValidacaoException;
import com.farmacia.model.Fornecedor;
import com.farmacia.model.Medicamento;
import com.farmacia.service.MedicamentoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MedicamentoController {

    // ========== COMPONENTES DA ABA CADASTRO ==========
    @FXML private TextField txtCodigo;
    @FXML private TextField txtNome;
    @FXML private TextField txtDescricao;
    @FXML private TextField txtPrincipioAtivo;
    @FXML private DatePicker dpDataValidade;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtPreco;
    @FXML private CheckBox chkControlado;

    @FXML private TextField txtCnpj;
    @FXML private TextField txtRazaoSocial;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtCidade;
    @FXML private TextField txtEstado;

    @FXML private Label lblMensagem;

    // ========== COMPONENTES DA ABA LISTAGEM ==========
    @FXML private TextField txtBusca;
    @FXML private TableView<Medicamento> tabelaMedicamentos;
    @FXML private TableColumn<Medicamento, String> colCodigo;
    @FXML private TableColumn<Medicamento, String> colNome;
    @FXML private TableColumn<Medicamento, String> colPrincipioAtivo;
    @FXML private TableColumn<Medicamento, LocalDate> colValidade;
    @FXML private TableColumn<Medicamento, Integer> colQuantidade;
    @FXML private TableColumn<Medicamento, BigDecimal> colPreco;
    @FXML private TableColumn<Medicamento, Boolean> colControlado;
    @FXML private TableColumn<Medicamento, String> colFornecedor;
    @FXML private Label lblTotalRegistros;

    // ========== COMPONENTES DA ABA RELATÓRIOS ==========
    @FXML private ComboBox<String> cmbRelatorios;
    @FXML private TextArea txtRelatorio;

    // Service
    private MedicamentoService service;
    private ObservableList<Medicamento> listaMedicamentos;
    private Medicamento medicamentoSelecionado;

    /**
     * Método chamado automaticamente após carregar o FXML
     */
    @FXML
    public void initialize() {
        System.out.println("=== INICIALIZANDO CONTROLLER ===");

        service = new MedicamentoService();
        listaMedicamentos = FXCollections.observableArrayList();

        configurarTabela();
        configurarComboRelatorios();
        carregarDados();
        configurarEventos();

        lblMensagem.setText("✓ Sistema pronto para uso!");
        lblMensagem.setStyle("-fx-text-fill: green;");

        System.out.println("=== CONTROLLER INICIALIZADO COM SUCESSO ===");
    }

    /**
     * Configura as colunas da tabela
     */
    private void configurarTabela() {
        System.out.println("Configurando tabela...");

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPrincipioAtivo.setCellValueFactory(new PropertyValueFactory<>("principioAtivo"));
        colValidade.setCellValueFactory(new PropertyValueFactory<>("dataValidade"));
        colQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidadeEstoque"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colControlado.setCellValueFactory(new PropertyValueFactory<>("controlado"));

        // Configurar coluna fornecedor para exibir razão social
        colFornecedor.setCellValueFactory(cellData -> {
            Fornecedor fornecedor = cellData.getValue().getFornecedor();
            return new javafx.beans.property.SimpleStringProperty(
                    fornecedor != null ? fornecedor.getRazaoSocial() : ""
            );
        });

        // Formatar coluna de preço
        colPreco.setCellFactory(col -> new TableCell<Medicamento, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", preco));
                }
            }
        });

        // Formatar coluna de controlado
        colControlado.setCellFactory(col -> new TableCell<Medicamento, Boolean>() {
            @Override
            protected void updateItem(Boolean controlado, boolean empty) {
                super.updateItem(controlado, empty);
                if (empty || controlado == null) {
                    setText(null);
                } else {
                    setText(controlado ? "Sim" : "Não");
                }
            }
        });

        tabelaMedicamentos.setItems(listaMedicamentos);
    }

    /**
     * Configura o ComboBox de relatórios
     */
    private void configurarComboRelatorios() {
        cmbRelatorios.getItems().addAll(
                "Medicamentos Próximos ao Vencimento (30 dias)",
                "Medicamentos com Estoque Baixo (menos de 5)",
                "Valor Total do Estoque por Fornecedor",
                "Medicamentos Controlados vs Não Controlados",
                "Estatísticas Gerais"
        );
    }

    /**
     * Configura eventos da interface
     */
    private void configurarEventos() {
        // Ao clicar em uma linha da tabela, preencher formulário
        tabelaMedicamentos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        medicamentoSelecionado = newSelection;
                        preencherFormulario(newSelection);
                    }
                }
        );
    }

    /**
     * Carrega todos os medicamentos do banco
     */
    private void carregarDados() {
        System.out.println("Carregando dados do CSV...");
        try {
            List<Medicamento> medicamentos = service.listarTodos();
            listaMedicamentos.clear();
            listaMedicamentos.addAll(medicamentos);
            lblTotalRegistros.setText("Total de registros: " + medicamentos.size());
            System.out.println("Dados carregados: " + medicamentos.size() + " medicamentos");
        } catch (PersistenciaException e) {
            System.err.println("Erro ao carregar: " + e.getMessage());
            mostrarErro("Erro ao carregar medicamentos: " + e.getMessage());
        }
    }

    /**
     * Preenche o formulário com dados de um medicamento
     */
    private void preencherFormulario(Medicamento med) {
        txtCodigo.setText(med.getCodigo());
        txtNome.setText(med.getNome());
        txtDescricao.setText(med.getDescricao());
        txtPrincipioAtivo.setText(med.getPrincipioAtivo());
        dpDataValidade.setValue(med.getDataValidade());
        txtQuantidade.setText(String.valueOf(med.getQuantidadeEstoque()));
        txtPreco.setText(med.getPreco().toString());
        chkControlado.setSelected(med.isControlado());

        Fornecedor forn = med.getFornecedor();
        txtCnpj.setText(forn.getCnpj());
        txtRazaoSocial.setText(forn.getRazaoSocial());
        txtTelefone.setText(forn.getTelefone());
        txtEmail.setText(forn.getEmail());
        txtCidade.setText(forn.getCidade());
        txtEstado.setText(forn.getEstado());
    }

    // ========== HANDLERS DOS BOTÕES ==========

    @FXML
    private void handleSalvar() {
        System.out.println("=== INÍCIO handleSalvar ===");

        try {
            System.out.println("Obtendo medicamento do formulário...");
            Medicamento medicamento = obterMedicamentoDoFormulario();
            System.out.println("Medicamento obtido: " + medicamento.getCodigo());

            // Verificar se é atualização ou novo cadastro
            if (medicamentoSelecionado != null &&
                    medicamentoSelecionado.getCodigo().equals(medicamento.getCodigo())) {
                System.out.println("Atualizando medicamento...");
                service.atualizar(medicamento);
                mostrarSucesso("Medicamento atualizado com sucesso!");
            } else {
                System.out.println("Cadastrando novo medicamento...");
                service.cadastrar(medicamento);
                mostrarSucesso("Medicamento cadastrado com sucesso!");
            }

            limparFormulario();
            carregarDados();
            medicamentoSelecionado = null;

            System.out.println("=== FIM handleSalvar - SUCESSO ===");

        } catch (ValidacaoException e) {
            System.err.println("ERRO DE VALIDAÇÃO: " + e.getMessage());
            mostrarErro("Erro de validação: " + e.getMessage());
        } catch (PersistenciaException e) {
            System.err.println("ERRO DE PERSISTÊNCIA: " + e.getMessage());
            e.printStackTrace();
            mostrarErro("Erro ao salvar: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERRO INESPERADO: " + e.getMessage());
            e.printStackTrace();
            mostrarErro("Erro inesperado: " + e.getMessage());
        }
    }

    @FXML
    private void handleLimpar() {
        limparFormulario();
        medicamentoSelecionado = null;
        tabelaMedicamentos.getSelectionModel().clearSelection();
        mostrarSucesso("Formulário limpo!");
    }

    @FXML
    private void handleExcluir() {
        String codigo = txtCodigo.getText().trim();

        if (codigo.isEmpty()) {
            mostrarErro("Informe o código do medicamento a ser excluído");
            return;
        }

        // Confirmar exclusão
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Deseja realmente excluir este medicamento?");
        confirmacao.setContentText("Código: " + codigo);

        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                service.excluir(codigo);
                mostrarSucesso("Medicamento excluído com sucesso!");
                limparFormulario();
                carregarDados();
                medicamentoSelecionado = null;
            } catch (ValidacaoException | PersistenciaException e) {
                mostrarErro("Erro ao excluir: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleBuscar() {
        String codigo = txtBusca.getText().trim();

        if (codigo.isEmpty()) {
            mostrarErro("Digite um código para buscar");
            return;
        }

        try {
            Medicamento medicamento = service.consultar(codigo);

            if (medicamento != null) {
                listaMedicamentos.clear();
                listaMedicamentos.add(medicamento);
                lblTotalRegistros.setText("Total de registros: 1");
                tabelaMedicamentos.getSelectionModel().select(medicamento);
                mostrarSucesso("Medicamento encontrado!");
            } else {
                mostrarErro("Medicamento não encontrado");
            }
        } catch (ValidacaoException | PersistenciaException e) {
            mostrarErro("Erro ao buscar: " + e.getMessage());
        }
    }

    @FXML
    private void handleAtualizarListagem() {
        txtBusca.clear();
        carregarDados();
        mostrarSucesso("Listagem atualizada!");
    }

    @FXML
    private void handleGerarRelatorio() {
        String relatorioSelecionado = cmbRelatorios.getValue();

        if (relatorioSelecionado == null) {
            mostrarErro("Selecione um relatório");
            return;
        }

        try {
            StringBuilder relatorio = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            switch (relatorioSelecionado) {
                case "Medicamentos Próximos ao Vencimento (30 dias)":
                    List<Medicamento> proximosVencimento = service.relatorioProximosVencimento();

                    relatorio.append("=".repeat(80)).append("\n");
                    relatorio.append("RELATÓRIO: MEDICAMENTOS PRÓXIMOS AO VENCIMENTO\n");
                    relatorio.append("Período: Próximos 30 dias\n");
                    relatorio.append("Data de geração: ").append(LocalDate.now().format(formatter)).append("\n");
                    relatorio.append("=".repeat(80)).append("\n\n");

                    if (proximosVencimento.isEmpty()) {
                        relatorio.append("Nenhum medicamento próximo ao vencimento.\n");
                    } else {
                        relatorio.append(String.format("Total de medicamentos: %d\n\n", proximosVencimento.size()));
                        relatorio.append(String.format("%-10s %-30s %-15s %-12s\n",
                                "CÓDIGO", "NOME", "VALIDADE", "ESTOQUE"));
                        relatorio.append("-".repeat(80)).append("\n");

                        for (Medicamento med : proximosVencimento) {
                            relatorio.append(String.format("%-10s %-30s %-15s %-12d\n",
                                    med.getCodigo(),
                                    med.getNome().substring(0, Math.min(30, med.getNome().length())),
                                    med.getDataValidade().format(formatter),
                                    med.getQuantidadeEstoque()));
                        }
                    }
                    break;

                case "Medicamentos com Estoque Baixo (menos de 5)":
                    List<Medicamento> estoqueBaixo = service.relatorioEstoqueBaixo();

                    relatorio.append("=".repeat(80)).append("\n");
                    relatorio.append("RELATÓRIO: MEDICAMENTOS COM ESTOQUE BAIXO\n");
                    relatorio.append("Critério: Quantidade < 5 unidades\n");
                    relatorio.append("Data de geração: ").append(LocalDate.now().format(formatter)).append("\n");
                    relatorio.append("=".repeat(80)).append("\n\n");

                    if (estoqueBaixo.isEmpty()) {
                        relatorio.append("Nenhum medicamento com estoque baixo.\n");
                    } else {
                        relatorio.append(String.format("Total de medicamentos: %d\n\n", estoqueBaixo.size()));
                        relatorio.append(String.format("%-10s %-30s %-12s %-15s\n",
                                "CÓDIGO", "NOME", "ESTOQUE", "PREÇO"));
                        relatorio.append("-".repeat(80)).append("\n");

                        for (Medicamento med : estoqueBaixo) {
                            relatorio.append(String.format("%-10s %-30s %-12d R$ %10.2f\n",
                                    med.getCodigo(),
                                    med.getNome().substring(0, Math.min(30, med.getNome().length())),
                                    med.getQuantidadeEstoque(),
                                    med.getPreco()));
                        }
                    }
                    break;

                case "Valor Total do Estoque por Fornecedor":
                    Map<String, BigDecimal> valorPorFornecedor = service.relatorioValorTotalPorFornecedor();

                    relatorio.append("=".repeat(80)).append("\n");
                    relatorio.append("RELATÓRIO: VALOR TOTAL DO ESTOQUE POR FORNECEDOR\n");
                    relatorio.append("Data de geração: ").append(LocalDate.now().format(formatter)).append("\n");
                    relatorio.append("=".repeat(80)).append("\n\n");

                    if (valorPorFornecedor.isEmpty()) {
                        relatorio.append("Nenhum dado disponível.\n");
                    } else {
                        relatorio.append(String.format("%-40s %20s\n", "FORNECEDOR", "VALOR TOTAL"));
                        relatorio.append("-".repeat(80)).append("\n");

                        BigDecimal total = BigDecimal.ZERO;
                        for (Map.Entry<String, BigDecimal> entry : valorPorFornecedor.entrySet()) {
                            relatorio.append(String.format("%-40s R$ %15.2f\n",
                                    entry.getKey().substring(0, Math.min(40, entry.getKey().length())),
                                    entry.getValue()));
                            total = total.add(entry.getValue());
                        }

                        relatorio.append("-".repeat(80)).append("\n");
                        relatorio.append(String.format("%-40s R$ %15.2f\n", "TOTAL GERAL:", total));
                    }
                    break;

                case "Medicamentos Controlados vs Não Controlados":
                    Map<String, Long> controlados = service.relatorioControladosVsNaoControlados();

                    relatorio.append("=".repeat(80)).append("\n");
                    relatorio.append("RELATÓRIO: MEDICAMENTOS CONTROLADOS VS NÃO CONTROLADOS\n");
                    relatorio.append("Data de geração: ").append(LocalDate.now().format(formatter)).append("\n");
                    relatorio.append("=".repeat(80)).append("\n\n");

                    long totalControlados = controlados.getOrDefault("Controlados", 0L);
                    long totalNaoControlados = controlados.getOrDefault("Não Controlados", 0L);
                    long totalGeral = totalControlados + totalNaoControlados;

                    relatorio.append(String.format("%-30s %10s %15s\n", "CATEGORIA", "QUANTIDADE", "PERCENTUAL"));
                    relatorio.append("-".repeat(80)).append("\n");
                    relatorio.append(String.format("%-30s %10d %14.1f%%\n",
                            "Controlados",
                            totalControlados,
                            totalGeral > 0 ? (totalControlados * 100.0 / totalGeral) : 0));
                    relatorio.append(String.format("%-30s %10d %14.1f%%\n",
                            "Não Controlados",
                            totalNaoControlados,
                            totalGeral > 0 ? (totalNaoControlados * 100.0 / totalGeral) : 0));
                    relatorio.append("-".repeat(80)).append("\n");
                    relatorio.append(String.format("%-30s %10d %14s\n", "TOTAL:", totalGeral, "100.0%"));
                    break;

                case "Estatísticas Gerais":
                    Map<String, Object> estatisticas = service.relatorioEstatisticas();

                    relatorio.append("=".repeat(80)).append("\n");
                    relatorio.append("RELATÓRIO: ESTATÍSTICAS GERAIS DO ESTOQUE\n");
                    relatorio.append("Data de geração: ").append(LocalDate.now().format(formatter)).append("\n");
                    relatorio.append("=".repeat(80)).append("\n\n");

                    relatorio.append(String.format("Total de Medicamentos Cadastrados: %d\n",
                            estatisticas.get("totalMedicamentos")));
                    relatorio.append(String.format("Quantidade Total em Estoque: %d unidades\n",
                            estatisticas.get("quantidadeTotalEstoque")));
                    relatorio.append(String.format("Valor Total do Estoque: R$ %.2f\n",
                            estatisticas.get("valorTotalEstoque")));
                    relatorio.append(String.format("Preço Médio dos Medicamentos: R$ %.2f\n",
                            estatisticas.get("precoMedio")));
                    break;
            }

            txtRelatorio.setText(relatorio.toString());
            mostrarSucesso("Relatório gerado com sucesso!");

        } catch (PersistenciaException e) {
            mostrarErro("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Obtém medicamento a partir dos campos do formulário
     */
    private Medicamento obterMedicamentoDoFormulario() throws Exception {
        System.out.println("Criando fornecedor...");

        Fornecedor fornecedor = new Fornecedor(
                txtCnpj.getText().trim(),
                txtRazaoSocial.getText().trim(),
                txtTelefone.getText().trim(),
                txtEmail.getText().trim(),
                txtCidade.getText().trim(),
                txtEstado.getText().trim()
        );

        System.out.println("Criando medicamento...");

        return new Medicamento(
                txtCodigo.getText().trim().toUpperCase(),
                txtNome.getText().trim(),
                txtDescricao.getText().trim(),
                txtPrincipioAtivo.getText().trim(),
                dpDataValidade.getValue(),
                Integer.parseInt(txtQuantidade.getText().trim()),
                new BigDecimal(txtPreco.getText().trim()),
                chkControlado.isSelected(),
                fornecedor
        );
    }

    /**
     * Limpa todos os campos do formulário
     */
    private void limparFormulario() {
        txtCodigo.clear();
        txtNome.clear();
        txtDescricao.clear();
        txtPrincipioAtivo.clear();
        dpDataValidade.setValue(null);
        txtQuantidade.clear();
        txtPreco.clear();
        chkControlado.setSelected(false);

        txtCnpj.clear();
        txtRazaoSocial.clear();
        txtTelefone.clear();
        txtEmail.clear();
        txtCidade.clear();
        txtEstado.clear();

        lblMensagem.setText("");
    }

    /**
     * Exibe mensagem de sucesso
     */
    private void mostrarSucesso(String mensagem) {
        lblMensagem.setText("✓ " + mensagem);
        lblMensagem.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
    }

    /**
     * Exibe mensagem de erro
     */
    private void mostrarErro(String mensagem) {
        lblMensagem.setText("✗ " + mensagem);
        lblMensagem.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}