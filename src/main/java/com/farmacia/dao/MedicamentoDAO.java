package com.farmacia.dao;

import com.farmacia.exception.PersistenciaException;
import com.farmacia.model.Fornecedor;
import com.farmacia.model.Medicamento;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoDAO {
    private static final String ARQUIVO = "data/medicamentos.csv";
    private static final String SEPARADOR = ";";
    private static final String CABECALHO = "codigo;nome;descricao;principioAtivo;dataValidade;" +
            "quantidadeEstoque;preco;controlado;cnpj;razaoSocial;telefone;email;cidade;estado";

    public MedicamentoDAO() {
        criarDiretorioSeNaoExistir();
        criarArquivoSeNaoExistir();
    }

    private void criarDiretorioSeNaoExistir() {
        File diretorio = new File("data");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
    }

    private void criarArquivoSeNaoExistir() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            try (BufferedWriter writer = new BufferedWriter(
                    new FileWriter(arquivo, StandardCharsets.UTF_8))) {
                writer.write(CABECALHO);
                writer.newLine();
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo CSV: " + e.getMessage());
            }
        }
    }

    public List<Medicamento> carregarTodos() throws PersistenciaException {
        List<Medicamento> medicamentos = new ArrayList<>();
        File arquivo = new File(ARQUIVO);

        if (!arquivo.exists()) {
            return medicamentos;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(arquivo, StandardCharsets.UTF_8))) {

            String linha;
            boolean primeiraLinha = true;
            int numeroLinha = 0;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;

                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Pular cabeçalho
                }

                if (linha.trim().isEmpty()) {
                    continue; // Pular linhas vazias
                }

                try {
                    Medicamento medicamento = parseLinha(linha);
                    medicamentos.add(medicamento);
                } catch (Exception e) {
                    System.err.println("Erro ao processar linha " + numeroLinha +
                            ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao carregar medicamentos do arquivo", e);
        }

        return medicamentos;
    }

    private Medicamento parseLinha(String linha) throws Exception {
        String[] dados = linha.split(SEPARADOR, -1); // -1 mantém campos vazios

        if (dados.length < 14) {
            throw new Exception("Linha com formato inválido - esperados 14 campos, encontrados " + dados.length);
        }

        // Criar fornecedor
        Fornecedor fornecedor = new Fornecedor(
                dados[8].trim(),  // cnpj
                dados[9].trim(),  // razaoSocial
                dados[10].trim(), // telefone
                dados[11].trim(), // email
                dados[12].trim(), // cidade
                dados[13].trim()  // estado
        );

        // Criar medicamento
        Medicamento medicamento = new Medicamento(
                dados[0].trim(),                              // codigo
                dados[1].trim(),                              // nome
                dados[2].trim(),                              // descricao
                dados[3].trim(),                              // principioAtivo
                LocalDate.parse(dados[4].trim()),             // dataValidade
                Integer.parseInt(dados[5].trim()),            // quantidadeEstoque
                new BigDecimal(dados[6].trim()),              // preco
                Boolean.parseBoolean(dados[7].trim()),        // controlado
                fornecedor
        );

        return medicamento;
    }

    public void salvarTodos(List<Medicamento> medicamentos) throws PersistenciaException {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(ARQUIVO, StandardCharsets.UTF_8))) {

            // Escrever cabeçalho
            writer.write(CABECALHO);
            writer.newLine();

            // Escrever dados
            for (Medicamento medicamento : medicamentos) {
                writer.write(medicamento.toCSV());
                writer.newLine();
            }

        } catch (IOException e) {
            throw new PersistenciaException("Erro ao salvar medicamentos no arquivo", e);
        }
    }

    public void adicionar(Medicamento medicamento) throws PersistenciaException {
        List<Medicamento> medicamentos = carregarTodos();
        medicamentos.add(medicamento);
        salvarTodos(medicamentos);
    }

    public void atualizar(Medicamento medicamentoAtualizado) throws PersistenciaException {
        List<Medicamento> medicamentos = carregarTodos();

        for (int i = 0; i < medicamentos.size(); i++) {
            if (medicamentos.get(i).getCodigo().equals(medicamentoAtualizado.getCodigo())) {
                medicamentos.set(i, medicamentoAtualizado);
                salvarTodos(medicamentos);
                return;
            }
        }

        throw new PersistenciaException("Medicamento não encontrado para atualização");
    }

    public boolean remover(String codigo) throws PersistenciaException {
        List<Medicamento> medicamentos = carregarTodos();
        boolean removido = medicamentos.removeIf(m -> m.getCodigo().equals(codigo));

        if (removido) {
            salvarTodos(medicamentos);
        }

        return removido;
    }

    public Medicamento buscarPorCodigo(String codigo) throws PersistenciaException {
        return carregarTodos().stream()
                .filter(m -> m.getCodigo().equalsIgnoreCase(codigo))
                .findFirst()
                .orElse(null);
    }

    public boolean existeCodigo(String codigo) throws PersistenciaException {
        return buscarPorCodigo(codigo) != null;
    }
}