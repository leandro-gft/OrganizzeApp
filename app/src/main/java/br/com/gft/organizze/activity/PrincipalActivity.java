package br.com.gft.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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
    private ValueEventListener valueEventListener;
    private FloatingActionButton fabD, fabR;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioRef;
    private MaterialCalendarView mcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Configurações da toolbar
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        mcv = findViewById(R.id.calendarView);
        txtSaldo = findViewById(R.id.textSaldoTotal);
        txtWelcome = findViewById(R.id.textWelcome);

        configuraCalendarView();
    }

    private void recuperaResumo() {
        String emailCodificado = auth.getCurrentUser().getEmail();
        usuarioRef = referencia.child("usuarios")
                .child(Base64Custom.codificarBase64(emailCodificado));

        valueEventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String nome = dataSnapshot.child("nome").getValue().toString();
                String despesaTotal = dataSnapshot.child("despesaTotal").getValue().toString();
                String receitaTotal = dataSnapshot.child("receitaTotal").getValue().toString();
                double total = Double.parseDouble(receitaTotal) - Double.parseDouble(despesaTotal);
                txtWelcome.setText("Olá, " + nome + " !");

                if (total < 0) {
                    txtSaldo.setText(String.format("R$ %.2f", total));
                    txtSaldo.setTextColor(getResources().getColor(R.color.vermelho));
                } else {
                    txtSaldo.setText(String.valueOf(String.format("R$ %.2f", total)));
                    txtSaldo.setTextColor(getResources().getColor(R.color.branco));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PrincipalActivity.this, "Erro ao recuperar nome do usuário: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaResumo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSair:
                auth.signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configuraCalendarView() {
        String meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        mcv.setTitleMonths(meses);
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(PrincipalActivity.this, ReceitaActivity.class));
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(PrincipalActivity.this, DespesaActivity.class));
    }
}
