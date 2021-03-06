package br.com.gft.organizze.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String senha;
    private String email;
    private double despesaTotal = 0;
    private double receitaTotal = 0;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    public Usuario() {
    }

    public Usuario(String nome, String senha, String email) {
        this.nome = nome;
        this.senha = senha;
        this.email = email;
    }

    public Usuario(String senha, String email) {
        this.senha = senha;
        this.email = email;
    }

    public double getDespesaTotal() {
        return despesaTotal;
    }

    public void setDespesaTotal(double despesaTotal) {
        this.despesaTotal = despesaTotal;
    }

    public double getReceitaTotal() {
        return receitaTotal;
    }

    public void setReceitaTotal(double receitaTotal) {
        this.receitaTotal = receitaTotal;
    }

    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void salvar() {
        referencia.child("usuarios")
                .child(this.idUsuario)
                .setValue(this); //objeto usuario
    }
}
