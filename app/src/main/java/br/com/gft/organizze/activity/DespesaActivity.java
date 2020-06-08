package br.com.gft.organizze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.gft.organizze.R;

public class DespesaActivity extends AppCompatActivity {

    private EditText etDataHoje;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    private Date date = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesa);

        etDataHoje = findViewById(R.id.etDespesaData);

        etDataHoje.setText(sdf.format(date.getTime()));
    }

    public void addDespesa(View view){

    }
}
