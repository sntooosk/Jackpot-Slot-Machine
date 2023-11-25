/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apostas.model;

/**
 *
 * @author Aluno
 */
public class Partida {

    String nomeJogador;
    String data;
    String hora;
    String valorApostado;
    String valorGanho;
    String status;

    public Partida(String nomeJogador, String data, String hora, String valorApostado, String valorGanho, String status) {
        this.nomeJogador = nomeJogador;
        this.data = data;
        this.hora = hora;
        this.valorApostado = valorApostado;
        this.valorGanho = valorGanho;
        this.status = status;
    }

    public String getNomeJogador() {
        return nomeJogador;
    }

    public void setNomeJogador(String nomeJogador) {
        this.nomeJogador = nomeJogador;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getValorApostado() {
        return valorApostado;
    }

    public void setValorApostado(String valorApostado) {
        this.valorApostado = valorApostado;
    }

    public String getValorGanho() {
        return valorGanho;
    }

    public void setValorGanho(String valorGanho) {
        this.valorGanho = valorGanho;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}