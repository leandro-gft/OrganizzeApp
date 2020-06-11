package br.com.gft.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import br.com.gft.organizze.R;
import br.com.gft.organizze.helper.Base64Custom;

public class PrincipalActivity extends AppCompatActivity {

    private TextView txtWelcome, txtSaldo;
    private Button btnSignOut;
    private FloatingActionButton fabD, fabR;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private MaterialCalendarView mcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mcv = findViewById(R.id.calendarView);
        txtSaldo = findViewById(R.id.textSaldoTotal);
        txtWelcome = findViewById(R.id.textWelcome);
        btnSignOut = findViewById(R.id.btnSignOut);

        configuraCalendarView();


        String emailCodificado = auth.getCurrentUser().getEmail();
        referencia.child("usuarios")
                .child(Base64Custom.codificarBase64(emailCodificado))
                .child("nome")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nome = dataSnapshot.getValue().toString();
                        txtWelcome.setText("Olá, " + nome + " !");
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(PrincipalActivity.this, "Erro ao recuperar nome do usuário: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
            }
        });
    }

    private void configuraCalendarView() {
        String meses[] = {"Janeiro","Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        mcv.setTitleMonths(meses);
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(PrincipalActivity.this, ReceitaActivity.class));
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(PrincipalActivity.this, DespesaActivity.class));
    }
}
