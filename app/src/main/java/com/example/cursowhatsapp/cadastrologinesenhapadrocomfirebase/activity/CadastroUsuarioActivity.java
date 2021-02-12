package com.example.cursowhatsapp.cadastrologinesenhapadrocomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private Button botaoCadastrar;
    private Usuario usuario;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        nome = (EditText) findViewById(R.id.edtCadastroNome);
        email = (EditText)findViewById(R.id.editCadastroEmail);
        senha = (EditText)findViewById(R.id.editCadastroSenha);
        botaoCadastrar = (Button) findViewById(R.id.btnCadastrar);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setNome( nome.getText().toString() );
                usuario.setEmail( email.getText().toString());
                usuario.setSenha( senha.getText().toString());
                cadastrarUsuario();

            }
        });
    }

    private void cadastrarUsuario(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuário ", Toast.LENGTH_LONG).show();

                    FirebaseUser usuarioFirebase =  task.getResult().getUser();
                    usuario.setId( usuarioFirebase.getUid() );
                    usuario.salvar();

                    autenticacao.signOut();
                    finish();

                } else {
                    String erroExcecao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        erroExcecao = "Digite uma senha mais forte, contendo mais caracteres e com letras e números! ";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "O e-mail digitado é inválido, digite um novo e-mail";

                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esse e-mail já está em uso no App!";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao efetuar o cadastro";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro :  " + erroExcecao, Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}