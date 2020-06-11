package br.com.gft.organizze.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import br.com.gft.organizze.R;
import br.com.gft.organizze.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etSenha;
    private Button btnEntrar;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        btnEntrar = findViewById(R.id.btEntrar);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = etEmail.getText().toString();
                String textSenha = etSenha.getText().toString();
                usuario = new Usuario(textSenha, textEmail);

                if (textEmail.isEmpty() || textSenha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Preencha os campos corretamente", Toast.LENGTH_LONG).show();
                } else {
                    validarLogin();
                }
            }
        });
    }

    private void validarLogin() {
        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String excecao = "";
                try {
                    throw e;
                } catch (FirebaseAuthInvalidUserException ex) {
                    excecao = "E-mail não está cadastrado";
                } catch (FirebaseAuthInvalidCredentialsException ex) {
                    excecao = "E-mail e/ou senha incorretos";
                } catch (Exception ex) {
                    excecao = "Erro ao fazer o login do usuário: " + ex.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(LoginActivity.this,
                        excecao,
                        Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this,
                        "Login realizado com sucesso",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
                finish();
            }
        });
    }
}


