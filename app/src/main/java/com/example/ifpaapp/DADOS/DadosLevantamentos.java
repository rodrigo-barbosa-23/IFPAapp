package com.example.ifpaapp.DADOS;

public class DadosLevantamentos {
    private int id_levantamento;
    private String setor;
    private String responsavel;
    private String data_referente;
    private String descricao;

    public int getId_levantamento() {
        return id_levantamento;
    }

    public void setId_levantamento(int id_levantamento) {
        this.id_levantamento = id_levantamento;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
