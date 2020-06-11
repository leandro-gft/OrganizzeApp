package br.com.gft.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

import br.com.gft.organizze.R;
import br.com.gft.organizze.adapter.AdapterMovimentacao;
import br.com.gft.organizze.helper.Base64Custom;
import br.com.gft.organizze.model.Movimentacao;

public class PrincipalActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView txtWelcome, txtSaldo;
    private ValueEventListener valueEventListenerUsuario;
    private ValueEventListener valueEventListenerMovimentacao;
    private FloatingActionButton fabD, fabR;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference usuarioRef;
    private DatabaseReference movimentacaoRef;
    private MaterialCalendarView mcv;
    private Movimentacao movimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private String data;
    private AdapterMovimentacao adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Configurações da toolbar
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recyclerMovimentos);
        mcv = findViewById(R.id.calendarView);
        txtSaldo = findViewById(R.id.textSaldoTotal);
        txtWelcome = findViewById(R.id.textWelcome);
        configuraCalendarView();

        //Configurando adapter
        adapter = new AdapterMovimentacao(movimentacoes, this);

        //Cofigurando RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void recuperarMovimentacao() {
        String emailCodificado = auth.getCurrentUser().getEmail();

        movimentacaoRef = referencia.child("movimentacao")
                .child(Base64Custom.codificarBase64(emailCodificado))
                .child(data);
        valueEventListenerMovimentacao = movimentacaoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                movimentacoes.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacoes.add(movimentacao);

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperaResumo() {
        String emailCodificado = auth.getCurrentUser().getEmail();
        usuarioRef = referencia.child("usuarios")
                .child(Base64Custom.codificarBase64(emailCodificado));

        valueEventListenerUsuario = usuarioRef.addValueEventListener(new ValueEventListener() {
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
                    txtSaldo.setText(String.format("R$ %.2f", total));
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
        recuperarMovimentacao();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListenerUsuario);
        movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
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

        String mes = String.format("%02d", mcv.getCurrentDate().getMonth() + 1);
        String ano = String.valueOf(mcv.getCurrentDate().getYear());
        data = mes + ano;

        mcv.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String mes = String.format("%02d", date.getMonth() + 1);
                String ano = String.valueOf(date.getYear());
                data = mes + ano;

                movimentacaoRef.removeEventListener(valueEventListenerMovimentacao);
                recuperarMovimentacao();
            }
        });
    }

    public void adicionarReceita(View view) {
        startActivity(new Intent(PrincipalActivity.this, ReceitaActivity.class));
    }

    public void adicionarDespesa(View view) {
        startActivity(new Intent(PrincipalActivity.this, DespesaActivity.class));
    }
}
