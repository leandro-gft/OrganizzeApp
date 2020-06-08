package br.com.gft.organizze.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.com.gft.organizze.R;
import br.com.gft.organizze.helper.Base64Custom;
import br.com.gft.organizze.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText editNome, editEmail, editSenha;
    private Button btCadastrar;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);


        editEmail = findViewById(R.id.etCadastroEmail);
        editNome = findViewById(R.id.etCadastroNome);
        editSenha = findViewById(R.id.etCadastroSenha);
        btCadastrar = findViewById(R.id.btnCadastrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textEmail = editEmail.getText().toString();
                String textNome = editNome.getText().toString();
                String textSenha = editSenha.getText().toString();

                if (textEmail.isEmpty() || textNome.isEmpty() || textSenha.isEmpty()) {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha todos os campos corretamente",
                            Toast.LENGTH_LONG).show();
                } else {
                    usuario = new Usuario(textNome, textSenha, textEmail);
                    cadastrarUsuario();
                }

            }
        });

    }

    private void cadastrarUsuario() {
        mAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String excecao = "";
                try {
                    throw e;
                } catch (FirebaseAuthWeakPasswordException ex) {
                    excecao = "Digite uma senha mais forte!";
                } catch (FirebaseAuthInvalidCredentialsException ex) {
                    excecao = "Digite um e-mail válido";
                } catch (FirebaseAuthUserCollisionException ex) {
                    excecao = "Essa conta já existe";
                } catch (Exception ex) {
                    excecao = "Erro ao cadastrar usuário: " + ex.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(CadastroActivity.this,
                        excecao,
                        Toast.LENGTH_LONG).show();

            }
        }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(CadastroActivity.this,
                        "Usuário cadastrado com sucesso",
                        Toast.LENGTH_LONG).show();
                String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                usuario.setIdUsuario(idUsuario);
                usuario.salvar();
                finish();
            }
        });
    }

}
