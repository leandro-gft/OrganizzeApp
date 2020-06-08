package br.com.gft.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import br.com.gft.organizze.R;

public class PrincipalActivity extends AppCompatActivity {

    private TextView txtWelcome;
    private Button btnSignOut;
    private FloatingActionButton fabD, fabR;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
//    private DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtWelcome = findViewById(R.id.textWelcome);
        btnSignOut = findViewById(R.id.btnSignOut);

//        txtWelcome.setText("Olá " + db.child("usuarios").child(
//                Base64Custom.codificarBase64(auth.getCurrentUser().getEmail())));

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
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
