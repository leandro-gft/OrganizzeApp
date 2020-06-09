package br.com.gft.organizze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.gft.organizze.R;
import br.com.gft.organizze.helper.Base64Custom;
import br.com.gft.organizze.model.Movimentacao;
import br.com.gft.organizze.model.Usuario;

public class ReceitaActivity extends AppCompatActivity {

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Date date = new Date();
    private EditText etDataHoje, etReceitaValor, etReceitaCategoria, etReceitaDesricao;
    private Movimentacao movimentacao;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receita);

        etDataHoje = findViewById(R.id.etReceitaData);
        etReceitaCategoria = findViewById(R.id.etReceitaCategoria);
        etReceitaDesricao = findViewById(R.id.etReceitaDescricao);
        etReceitaValor = findViewById(R.id.etReceitaValor);

        etDataHoje.setText(sdf.format(date.getTime()));

        recuperarReceitaTotal();
    }


    public void addReceita(View view) {
        String campoData = etDataHoje.getText().toString();
        String campoCategoria = etReceitaCategoria.getText().toString();
        String campoDescricao = etReceitaDesricao.getText().toString();
        String campoValor = etReceitaValor.getText().toString();

        if (validarReceita()) {
            Double valorReceita = Double.parseDouble(campoValor);
            movimentacao = new Movimentacao(campoData, campoCategoria, campoDescricao, "r", valorReceita);
            receitaTotal += valorReceita;
            atualizarReceitaTotal(receitaTotal);
            movimentacao.salvar();
            finish();
        }
    }

    public Boolean validarReceita(){
        String campoData = etDataHoje.getText().toString();
        String campoCategoria = etReceitaCategoria.getText().toString();
        String campoDescricao = etReceitaDesricao.getText().toString();
        String campoValor = etReceitaValor.getText().toString();

        if (campoCategoria.isEmpty()||
                campoData.isEmpty() || campoDescricao.isEmpty()|| campoValor.isEmpty()) {
            Toast.makeText(ReceitaActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void atualizarReceitaTotal(Double receita){
        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = referencia.child("usuarios").child(idUsuario);

        usuarioRef.child("receitaTotal").setValue(receita);
    }

    public void recuperarReceitaTotal(){
        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = referencia.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
