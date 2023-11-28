package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {
    private Connection conexao;

    public DAO(String tipoBanco) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        this.conexao = connectionFactory.conectar(tipoBanco);
    }

    public void inserirContato(JavaBeans membro) {
        String create = "insert into membro (nome, cargo ,dizimista) values (? , ? ,?)";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(create);
            pst.setString(1, membro.getNome());
            pst.setString(2, membro.getCargo());
            pst.setString(3, membro.getDizimista());
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public ArrayList<JavaBeans> listarContatos(String cargo) {
        ArrayList<JavaBeans> cadastros = new ArrayList<>();
        String read = "SELECT * FROM membro WHERE cargo = ? ORDER BY nome;";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(read);
            pst.setString(1, cargo);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String id_membro = rs.getString(1);
                String nome = rs.getString(2);
                String cargoResult = rs.getString(3);
                String dizimista = rs.getString(4);
                cadastros.add(new JavaBeans(id_membro, nome, cargoResult, dizimista));
            }
            con.close();
            return cadastros;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void selecionarContato(JavaBeans membro) {
        String read2 = "select * from membro where id_membro = ?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(read2);
            pst.setString(1, membro.getId_membro());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                membro.setId_membro(rs.getString(1));
                membro.setNome(rs.getString(2));
                membro.setCargo(rs.getString(3));
                membro.setDizimista(rs.getString(4));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void alterarCadastro(JavaBeans membro) {
        String create = "UPDATE membro SET nome=?, cargo=?, dizimista=? WHERE id_membro=?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(create);
            pst.setString(1, membro.getNome());
            pst.setString(2, membro.getCargo());
            pst.setString(3, membro.getDizimista());
            pst.setString(4, membro.getId_membro());
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void deletarCadastro(JavaBeans membro) {
        String delete = "delete from membro where id_membro = ?";
        try {
            Connection con = conectar();
            PreparedStatement pst = con.prepareStatement(delete);
            pst.setString(1, membro.getId_membro());
            pst.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
