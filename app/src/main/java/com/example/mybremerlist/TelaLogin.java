package com.example.mybremerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

public class TelaLogin extends AppCompatActivity {

    private TextView text_tela_cadastro;
    private EditText editTextIdLogin;
    private Button button_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        getSupportActionBar().hide();
        startComponents();
        text_tela_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
                startActivity(intent);
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextIdLogin.getText().toString();

                if(id.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else{
                    autenticar(view);
                }
            }
        });
    }

    private void autenticar(View view) {
        String id = editTextIdLogin.getText().toString();
        String senha = "12345678";

        FirebaseAuth.getInstance().signInWithEmailAndPassword(id+"@mail.com",senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(TelaLogin.this, PerfilUsuario.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    try {
                        throw task.getException();
                    } catch(Exception e){
                        Snackbar snackbar = Snackbar.make(view,"erro: "+ e.toString(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(TelaLogin.this, PerfilUsuario.class);
            startActivity(intent);
            finish();

        }
    }

    private void startComponents() {
        text_tela_cadastro = findViewById(R.id.text_tela_cadastro);
        editTextIdLogin = findViewById(R.id.editTextIdLogin);
        button_login = findViewById(R.id.button_login);


    }
}