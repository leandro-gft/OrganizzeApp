package br.com.gft.organizze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.gft.organizze.R;
import br.com.gft.organizze.model.Movimentacao;

public class DespesaActivity extends AppCompatActivity {

    private EditText etDataHoje, etDespesaValor, etDespesaCategoria, etDespesaDesricao;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Date date = new Date();
    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        etDataHoje = findViewById(R.id.etDespesaData);
        etDespesaCategoria = findViewById(R.id.etDespesaCategoria);
        etDespesaDesricao = findViewById(R.id.etDespesaDescricao);
        etDespesaValor = findViewById(R.id.etDespesaValor);

        etDataHoje.setText(sdf.format(date.getTime()));

    }

    public void addDespesa(View view){
        String campoData = etDataHoje.getText().toString();
        String campoCategoria = etDespesaCategoria.getText().toString();
        String campoDescricao = etDespesaDesricao.getText().toString();
        String campoValor = etDespesaValor.getText().toString();

        if (validarDespesa()) {
            movimentacao = new Movimentacao(campoData, campoCategoria, campoDescricao, "d", Double.parseDouble(campoValor));
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


}
