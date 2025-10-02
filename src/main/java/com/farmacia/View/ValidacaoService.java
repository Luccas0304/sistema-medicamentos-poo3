package com.farmacia.View;

import com.farmacia.exception.ValidacaoException;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidacaoService {

    public static void validarCodigo(String codigo) throws ValidacaoException {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new ValidacaoException("Código não pode ser vazio");
        }

        String codigoLimpo = codigo.trim();

        if (codigoLimpo.length() != 7) {
            throw new ValidacaoException("Código deve ter exatamente 7 caracteres");
        }

        if (!codigoLimpo.matches("[A-Za-z0-9]{7}")) {
            throw new ValidacaoException("Código deve conter apenas letras e números");
        }
    }

    public static void validarNome(String nome) throws ValidacaoException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome não pode ser vazio");
        }

        if (nome.trim().length() < 3) {
            throw new ValidacaoException("Nome deve ter no mínimo 3 caracteres");
        }
    }

    public static void validarDataValidade(LocalDate data) throws ValidacaoException {
        if (data == null) {
            throw new ValidacaoException("Data de validade não pode ser vazia");
        }

        if (data.isBefore(LocalDate.now())) {
            throw new ValidacaoException("Data de validade não pode ser uma data passada");
        }
    }

    public static void validarQuantidade(int quantidade) throws ValidacaoException {
        if (quantidade < 0) {
            throw new ValidacaoException("Quantidade não pode ser negativa");
        }
    }

    public static void validarPreco(BigDecimal preco) throws ValidacaoException {
        if (preco == null) {
            throw new ValidacaoException("Preço não pode ser vazio");
        }

        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacaoException("Preço deve ser maior que zero");
        }
    }

    public static void validarCNPJ(String cnpj) throws ValidacaoException {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new ValidacaoException("CNPJ não pode ser vazio");
        }

        // Remove caracteres não numéricos
        String cnpjLimpo = cnpj.replaceAll("[^0-9]", "");

        if (cnpjLimpo.length() != 14) {
            throw new ValidacaoException("CNPJ deve ter 14 dígitos");
        }

        // Verifica se todos os dígitos são iguais (CNPJ inválido)
        if (cnpjLimpo.matches("(\\d)\\1{13}")) {
            throw new ValidacaoException("CNPJ inválido");
        }

        // Validação dos dígitos verificadores
        if (!validarDigitosCNPJ(cnpjLimpo)) {
            throw new ValidacaoException("CNPJ inválido - dígitos verificadores incorretos");
        }
    }

    private static boolean validarDigitosCNPJ(String cnpj) {
        try {
            // Pesos para o primeiro dígito verificador
            int[] pesosPrimeiroDigito = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            // Pesos para o segundo dígito verificador
            int[] pesosSegundoDigito = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            // Calcular primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * pesosPrimeiroDigito[i];
            }
            int resto = soma % 11;
            int digito1 = resto < 2 ? 0 : 11 - resto;

            // Calcular segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * pesosSegundoDigito[i];
            }
            resto = soma % 11;
            int digito2 = resto < 2 ? 0 : 11 - resto;

            // Verificar se os dígitos calculados são iguais aos informados
            return digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
                    digito2 == Character.getNumericValue(cnpj.charAt(13));
        } catch (Exception e) {
            return false;
        }
    }

    public static void validarEmail(String email) throws ValidacaoException {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidacaoException("Email não pode ser vazio");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ValidacaoException("Email inválido");
        }
    }

    public static void validarTelefone(String telefone) throws ValidacaoException {
        if (telefone == null || telefone.trim().isEmpty()) {
            throw new ValidacaoException("Telefone não pode ser vazio");
        }

        String telefoneLimpo = telefone.replaceAll("[^0-9]", "");

        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            throw new ValidacaoException("Telefone deve ter 10 ou 11 dígitos");
        }
    }
}