package com.farmacia.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Medicamento {
    private String codigo;
    private String nome;
    private String descricao;
    private String principioAtivo;
    private LocalDate dataValidade;
    private int quantidadeEstoque;
    private BigDecimal preco;
    private boolean controlado;
    private Fornecedor fornecedor;

    public Medicamento() {}

    public Medicamento(String codigo, String nome, String descricao, String principioAtivo,
                       LocalDate dataValidade, int quantidadeEstoque, BigDecimal preco,
                       boolean controlado, Fornecedor fornecedor) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.principioAtivo = principioAtivo;
        this.dataValidade = dataValidade;
        this.quantidadeEstoque = quantidadeEstoque;
        this.preco = preco;
        this.controlado = controlado;
        this.fornecedor = fornecedor;
    }

    // Getters e Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getPrincipioAtivo() { return principioAtivo; }
    public void setPrincipioAtivo(String principioAtivo) { this.principioAtivo = principioAtivo; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public int getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(int quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public boolean isControlado() { return controlado; }
    public void setControlado(boolean controlado) { this.controlado = controlado; }

    public Fornecedor getFornecedor() { return fornecedor; }
    public void setFornecedor(Fornecedor fornecedor) { this.fornecedor = fornecedor; }

    /**
     * Converte o medicamento para formato CSV
     * Formato: codigo;nome;descricao;principioAtivo;dataValidade;quantidadeEstoque;preco;controlado;cnpj;razaoSocial;telefone;email;cidade;estado
     */
    public String toCSV() {
        StringBuilder sb = new StringBuilder();

        sb.append(codigo).append(";");
        sb.append(nome).append(";");
        sb.append(descricao).append(";");
        sb.append(principioAtivo).append(";");
        sb.append(dataValidade).append(";");
        sb.append(quantidadeEstoque).append(";");
        sb.append(preco).append(";");
        sb.append(controlado).append(";");

        // Dados do fornecedor
        if (fornecedor != null) {
            sb.append(fornecedor.getCnpj()).append(";");
            sb.append(fornecedor.getRazaoSocial()).append(";");
            sb.append(fornecedor.getTelefone()).append(";");
            sb.append(fornecedor.getEmail()).append(";");
            sb.append(fornecedor.getCidade()).append(";");
            sb.append(fornecedor.getEstado());
        } else {
            // Se não houver fornecedor, preencher com campos vazios
            sb.append(";;;;");
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return nome + " (" + codigo + ")";
    }
}