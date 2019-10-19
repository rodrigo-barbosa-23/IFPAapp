package com.example.ifpaapp.DADOS;

public class DadosRelatorios {
    private int id_relatorio;
    private String categoria;
    private String responsavel;
    private String data_referente;

    public int getId_relatorio() {
        return id_relatorio;
    }

    public void setId_relatorio(int id_relatorio) {
        this.id_relatorio = id_relatorio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getData_referente() {
        return data_referente;
    }

    public void setData_referente(String data_referente) {
        this.data_referente = data_referente;
    }
}
