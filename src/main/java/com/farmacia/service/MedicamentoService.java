package com.farmacia.service;

import com.farmacia.dao.MedicamentoDAO;
import com.farmacia.exception.PersistenciaException;
import com.farmacia.exception.ValidacaoException;
import com.farmacia.model.Medicamento;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MedicamentoService {

    private MedicamentoDAO dao;

    public MedicamentoService() {
        this.dao = new MedicamentoDAO();
    }

    /**
     * Cadastra um novo medicamento após validação
     */
    public void cadastrar(Medicamento medicamento) throws ValidacaoException, PersistenciaException {
        validarMedicamento(medicamento);

        // Verificar se código já existe
        if (dao.existeCodigo(medicamento.getCodigo())) {
            throw new ValidacaoException("Já existe um medicamento cadastrado com o código: " + medicamento.getCodigo());
        }

        dao.adicionar(medicamento);
    }

    /**
     * Atualiza um medicamento existente
     */
    public void atualizar(Medicamento medicamento) throws ValidacaoException, PersistenciaException {
        validarMedicamento(medicamento);
        dao.atualizar(medicamento);
    }

    /**
     * Exclui um medicamento
     */
    public void excluir(String codigo) throws ValidacaoException, PersistenciaException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ValidacaoException("Código não pode ser vazio");
        }

        boolean removido = dao.remover(codigo);
        if (!removido) {
            throw new ValidacaoException("Medicamento não encontrado");
        }
    }

    /**
     * Consulta um medicamento por código
     */
    public Medicamento consultar(String codigo) throws ValidacaoException, PersistenciaException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ValidacaoException("Código não pode ser vazio");
        }

        return dao.buscarPorCodigo(codigo);
    }

    /**
     * Lista todos os medicamentos
     */
    public List<Medicamento> listarTodos() throws PersistenciaException {
        return dao.carregarTodos();
    }

    /**
     * Valida todos os dados do medicamento
     */
    private void validarMedicamento(Medicamento med) throws ValidacaoException {
        // Validar código
        if (med.getCodigo() == null || med.getCodigo().trim().isEmpty()) {
            throw new ValidacaoException("Código é obrigatório");
        }

        if (med.getCodigo().length() != 7) {
            throw new ValidacaoException("Código deve ter exatamente 7 caracteres");
        }

        if (!med.getCodigo().matches("[A-Z0-9]{7}")) {
            throw new ValidacaoException("Código deve conter apenas letras maiúsculas e números");
        }

        // Validar nome
        if (med.getNome() == null || med.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome é obrigatório");
        }

        if (med.getNome().length() < 3) {
            throw new ValidacaoException("Nome deve ter no mínimo 3 caracteres");
        }

        // Validar descrição
        if (med.getDescricao() == null || med.getDescricao().trim().isEmpty()) {
            throw new ValidacaoException("Descrição é obrigatória");
        }

        // Validar princípio ativo
        if (med.getPrincipioAtivo() == null || med.getPrincipioAtivo().trim().isEmpty()) {
            throw new ValidacaoException("Princípio ativo é obrigatório");
        }

        // Validar data de validade
        if (med.getDataValidade() == null) {
            throw new ValidacaoException("Data de validade é obrigatória");
        }

        if (med.getDataValidade().isBefore(LocalDate.now())) {
            throw new ValidacaoException("Data de validade não pode ser no passado");
        }

        // Validar quantidade
        if (med.getQuantidadeEstoque() < 0) {
            throw new ValidacaoException("Quantidade em estoque não pode ser negativa");
        }

        // Validar preço
        if (med.getPreco() == null || med.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Preço deve ser maior que zero");
        }

        // Validar fornecedor
        if (med.getFornecedor() == null) {
            throw new ValidacaoException("Fornecedor é obrigatório");
        }

        validarFornecedor(med.getFornecedor());
    }

    /**
     * Valida dados do fornecedor
     */
    private void validarFornecedor(com.farmacia.model.Fornecedor forn) throws ValidacaoException {
        // Validar CNPJ
        if (forn.getCnpj() == null || forn.getCnpj().trim().isEmpty()) {
            throw new ValidacaoException("CNPJ do fornecedor é obrigatório");
        }

        String cnpj = forn.getCnpj().replaceAll("[^0-9]", "");

        if (cnpj.length() != 14) {
            throw new ValidacaoException("CNPJ deve ter 14 dígitos");
        }

        if (!validarCNPJ(cnpj)) {
            throw new ValidacaoException("CNPJ inválido");
        }

        // Validar razão social
        if (forn.getRazaoSocial() == null || forn.getRazaoSocial().trim().isEmpty()) {
            throw new ValidacaoException("Razão social do fornecedor é obrigatória");
        }

        // Validar telefone
        if (forn.getTelefone() == null || forn.getTelefone().trim().isEmpty()) {
            throw new ValidacaoException("Telefone do fornecedor é obrigatório");
        }

        // Validar email
        if (forn.getEmail() == null || forn.getEmail().trim().isEmpty()) {
            throw new ValidacaoException("Email do fornecedor é obrigatório");
        }

        if (!forn.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidacaoException("Email inválido");
        }

        // Validar cidade
        if (forn.getCidade() == null || forn.getCidade().trim().isEmpty()) {
            throw new ValidacaoException("Cidade do fornecedor é obrigatória");
        }

        // Validar estado
        if (forn.getEstado() == null || forn.getEstado().trim().isEmpty()) {
            throw new ValidacaoException("Estado do fornecedor é obrigatório");
        }

        if (forn.getEstado().length() != 2) {
            throw new ValidacaoException("Estado deve ter 2 caracteres (sigla)");
        }
    }

    /**
     * Valida CNPJ com verificação de dígitos
     */
    private boolean validarCNPJ(String cnpj) {
        // Verificar se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        try {
            // Calcular primeiro dígito verificador
            int soma = 0;
            int peso = 5;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            int digito1 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            // Calcular segundo dígito verificador
            soma = 0;
            peso = 6;
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
                peso = (peso == 2) ? 9 : peso - 1;
            }
            int digito2 = (soma % 11 < 2) ? 0 : 11 - (soma % 11);

            // Verificar dígitos
            return (Character.getNumericValue(cnpj.charAt(12)) == digito1) &&
                    (Character.getNumericValue(cnpj.charAt(13)) == digito2);

        } catch (Exception e) {
            return false;
        }
    }

    // ========== RELATÓRIOS COM STREAM API ==========

    /**
     * Relatório: Medicamentos próximos ao vencimento (30 dias)
     */
    public List<Medicamento> relatorioProximosVencimento() throws PersistenciaException {
        LocalDate dataLimite = LocalDate.now().plusDays(30);

        return dao.carregarTodos().stream()
                .filter(m -> m.getDataValidade().isBefore(dataLimite))
                .sorted((m1, m2) -> m1.getDataValidade().compareTo(m2.getDataValidade()))
                .collect(Collectors.toList());
    }

    /**
     * Relatório: Medicamentos com estoque baixo (menos de 5)
     */
    public List<Medicamento> relatorioEstoqueBaixo() throws PersistenciaException {
        return dao.carregarTodos().stream()
                .filter(m -> m.getQuantidadeEstoque() < 5)
                .sorted((m1, m2) -> Integer.compare(m1.getQuantidadeEstoque(), m2.getQuantidadeEstoque()))
                .collect(Collectors.toList());
    }

    /**
     * Relatório: Valor total do estoque por fornecedor
     */
    public Map<String, BigDecimal> relatorioValorTotalPorFornecedor() throws PersistenciaException {
        return dao.carregarTodos().stream()
                .collect(Collectors.groupingBy(
                        m -> m.getFornecedor().getRazaoSocial(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                m -> m.getPreco().multiply(new BigDecimal(m.getQuantidadeEstoque())),
                                BigDecimal::add
                        )
                ));
    }

    /**
     * Relatório: Medicamentos controlados vs não controlados
     */
    public Map<String, Long> relatorioControladosVsNaoControlados() throws PersistenciaException {
        return dao.carregarTodos().stream()
                .collect(Collectors.groupingBy(
                        m -> m.isControlado() ? "Controlados" : "Não Controlados",
                        Collectors.counting()
                ));
    }

    /**
     * Relatório: Estatísticas gerais
     */
    public Map<String, Object> relatorioEstatisticas() throws PersistenciaException {
        List<Medicamento> medicamentos = dao.carregarTodos();

        long totalMedicamentos = medicamentos.size();

        int quantidadeTotal = medicamentos.stream()
                .mapToInt(Medicamento::getQuantidadeEstoque)
                .sum();

        BigDecimal valorTotal = medicamentos.stream()
                .map(m -> m.getPreco().multiply(new BigDecimal(m.getQuantidadeEstoque())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal precoMedio = medicamentos.isEmpty() ? BigDecimal.ZERO :
                medicamentos.stream()
                        .map(Medicamento::getPreco)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(new BigDecimal(medicamentos.size()), 2, BigDecimal.ROUND_HALF_UP);

        return Map.of(
                "totalMedicamentos", totalMedicamentos,
                "quantidadeTotalEstoque", quantidadeTotal,
                "valorTotalEstoque", valorTotal,
                "precoMedio", precoMedio
        );
    }
}