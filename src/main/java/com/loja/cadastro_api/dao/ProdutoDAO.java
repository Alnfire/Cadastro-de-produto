package com.loja.cadastro_api.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/produtos_db";
    private String jdbcUsername = "root";
    private String jdbcPassword = "senha";
    
    private static final String INSERT_PRODUTO_SQL = "INSERT INTO produtos (nome, descricao, preco) VALUES (?, ?, ?);";
    private static final String SELECT_PRODUTO_BY_ID = "SELECT * FROM produtos WHERE id = ?;";
    private static final String SELECT_ALL_PRODUTOS = "SELECT * FROM produtos;";
    private static final String DELETE_PRODUTO_SQL = "DELETE FROM produtos WHERE id = ?;";
    private static final String UPDATE_PRODUTO_SQL = "UPDATE produtos SET nome = ?, descricao = ?, preco = ? WHERE id = ?;";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void inserirProduto(Produto produto) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_PRODUTO_SQL)) {
            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            statement.setDouble(3, produto.getPreco());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Produto buscarProdutoPorId(int id) {
        Produto produto = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PRODUTO_BY_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                produto = new Produto(id, nome, descricao, preco);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produto;
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PRODUTOS);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String descricao = rs.getString("descricao");
                double preco = rs.getDouble("preco");
                produtos.add(new Produto(id, nome, descricao, preco));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public boolean atualizarProduto(Produto produto) {
        boolean linhaAtualizada = false;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUTO_SQL)) {
            statement.setString(1, produto.getNome());
            statement.setString(2, produto.getDescricao());
            statement.setDouble(3, produto.getPreco());
            statement.setInt(4, produto.getId());
            linhaAtualizada = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return linhaAtualizada;
    }

    public boolean deletarProduto(int id) {
        boolean linhaDeletada = false;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUTO_SQL)) {
            statement.setInt(1, id);
            linhaDeletada = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return linhaDeletada;
    }
    
}
