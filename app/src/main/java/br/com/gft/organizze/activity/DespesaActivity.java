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

public class DespesaActivity extends AppCompatActivity {

    private EditText etDataHoje, etDespesaValor, etDespesaCategoria, etDespesaDesricao;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Date date = new Date();
    private Movimentacao movimentacao;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Double despesaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        etDataHoje = findViewById(R.id.etDespesaData);
        etDespesaCategoria = findViewById(R.id.etDespesaCategoria);
        etDespesaDesricao = findViewById(R.id.etDespesaDescricao);
        etDespesaValor = findViewById(R.id.etDespesaValor);

        etDataHoje.setText(sdf.format(date.getTime()));

        recuperarDespesaTotal();

    }

    public void addDespesa(View view){
        String campoData = etDataHoje.getText().toString();
        String campoCategoria = etDespesaCategoria.getText().toString();
        String campoDescricao = etDespesaDesricao.getText().toString();
        String campoValor = etDespesaValor.getText().toString();

        if (validarDespesa()) {
            Double valorDespesa = Double.parseDouble(campoValor);
            movimentacao = new Movimentacao(campoData, campoCategoria, campoDescricao, "d", valorDespesa);
            despesaTotal += valorDespesa;
            atualizarDespesaTotal(despesaTotal);
            movimentacao.salvar();
            finish();
        }
    }


    public Boolean validarDespesa(){
        String campoData = etDataHoje.getText().toString();
        String campoCategoria = etDespesaCategoria.getText().toString();
        String campoDescricao = etDespesaDesricao.getText().toString();
        String campoValor = etDespesaValor.getText().toString();

        if (campoCategoria.isEmpty()||
                campoData.isEmpty() || campoDescricao.isEmpty()|| campoValor.isEmpty()) {
            Toast.makeText(DespesaActivity.this, "Preencha todos os campos corretamente", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void atualizarDespesaTotal(Double despesa){
        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = referencia.child("usuarios").child(idUsuario);

        usuarioRef.child("despesaTotal").setValue(despesa);
    }

    public void recuperarDespesaTotal(){
        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        DatabaseReference usuarioRef = referencia.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                despesaTotal = usuario.getDespesaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
