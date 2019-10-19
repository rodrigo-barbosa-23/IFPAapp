package com.example.ifpaapp.DADOS;

public class DadosStatus {
    private String sigla;
    private String definicao;

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getDefinicao() {
        return definicao;
    }

    public void setDefinicao(String definicao) {
        this.definicao = definicao;
    }

    @Override
    public String toString() {
        return this.definicao;
    }

    public DadosStatus() {

    }

    public DadosStatus(String sigla, String definicao) {
        this.sigla = sigla;
        this.definicao = definicao;
    }
}
