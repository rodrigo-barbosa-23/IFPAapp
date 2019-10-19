package com.example.ifpaapp.DADOS;

public class DadosCategorias {
    private String codigo;
    private String definicao;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public DadosCategorias() {

    }

    public DadosCategorias(String codigo, String definicao) {
        this.codigo = codigo;
        this.definicao = definicao;
    }
}
