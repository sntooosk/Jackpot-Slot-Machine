/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apostas.DAO;

import com.apostas.factory.ModuloConexao;
import com.apostas.model.Partida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PartidaDAO {

    private final Connection conexao;
    private PreparedStatement pst;

    public PartidaDAO() {
        conexao = ModuloConexao.conectar();
    }

    public void salvaPartida(Partida jogo) {

        String sql = "INSERT INTO tb01_apostas (tb01_nome, tb01_data, tb01_hora, tb01_vl_aposta, tb01_ganho, tb01_status) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, jogo.getNomeJogador());
            pst.setString(2, jogo.getData());
            pst.setString(3, jogo.getHora());
            pst.setString(4, jogo.getValorApostado());
            pst.setString(5, jogo.getValorGanho());
            pst.setString(6, jogo.getStatus());

            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
