package br.com.gft.organizze.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;

import br.com.gft.organizze.R;

public class MainIntroActivity extends IntroActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove os botoes de voltar e de avançar
        setButtonBackVisible(false);
        setButtonNextVisible(false);

        //Adiciona slides
        //MÉTODO 2
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_3)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_4)
                .build()
        );

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build()
        );


        //MÉTODO 1
//        addSlide(new SimpleSlide.Builder()
//                                .title("Título")
//                                .description("Descrição")
//                                .image(R.drawable.um)
//                                .background(android.R.color.holo_orange_light)
//                                .build()
//        );
//
//        addSlide(new SimpleSlide.Builder()
//                .title("Título 2")
//                .description("Descrição 2")
//                .image(R.drawable.dois)
//                .background(android.R.color.holo_orange_light)
//                .build()
//        );
//
//        addSlide(new SimpleSlide.Builder()
//                .title("Título 3")
//                .description("Descrição 3")
//                .image(R.drawable.tres)
//                .background(android.R.color.holo_orange_light)
//                .build()
//        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        vericarUsuarioLogado();
    }

    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastreSe(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void vericarUsuarioLogado() {
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(MainIntroActivity.this, PrincipalActivity.class));
        }
    }

}
