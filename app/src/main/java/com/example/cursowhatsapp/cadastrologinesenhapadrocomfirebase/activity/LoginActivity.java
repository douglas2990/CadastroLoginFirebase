package com.example.cursowhatsapp.cadastrologinesenhapadrocomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cursowhatsapp.cadastrologinesenhapadrocomfirebase.R;
import com.example.cursowhatsapp.cadastrologinesenhapadrocomfirebase.config.ConfiguracaoFirebase;
import com.example.cursowhatsapp.cadastrologinesenhapadrocomfirebase.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, senha;
    private Button botaoLogar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.edtLoginEmail);
        senha = (EditText) findViewById(R.id.edtLoginSenha);
        botaoLogar = (Button)findViewById(R.id.btnLogar);



        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail( email.getText().toString() );
                usuario.setSenha( senha.getText().toString());
                validarLogin();


            }
        });
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ) {
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(LoginActivity.this, "Erro ao fazer login! ", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abrirCadastroUsuario(View view) {

        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity( intent );

    }
}