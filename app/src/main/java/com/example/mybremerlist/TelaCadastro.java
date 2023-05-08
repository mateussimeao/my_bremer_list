package com.example.mybremerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelaCadastro extends AppCompatActivity {

    private EditText editTextNomeCadastro, editTextIdCadastro;
    private Button button_cadastro;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        getSupportActionBar().hide();
        startComponents();

        button_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNomeCadastro.getText().toString();
                String id = editTextIdCadastro.getText().toString();

                if(nome.isEmpty() || id.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    cadastrar(view);
                }
            }
        });
    }

    private void cadastrar(View view) {
        String nome = editTextNomeCadastro.getText().toString();
        String id = editTextIdCadastro.getText().toString();
        String senha = "12345678";

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(id+"@mail.com",senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    salvarNome();

                    Snackbar snackbar = Snackbar.make(view,"Cadastro realizado com sucesso!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    String error;
                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthUserCollisionException e) {
                        error = "Usuario ja cadastrado";
                    }
                    catch (Exception e){
                        error = "Erro no cadastro";
                    }
                    Snackbar snackbar = Snackbar.make(view,error, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
    }

    private void salvarNome() {
        String nome = editTextNomeCadastro.getText().toString();
        List<Filme> listaFilmes = new ArrayList<>();
        String likes = "0";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> alunos = new HashMap<>();
        String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int index = id.indexOf('@');
        String showId = id.substring(0, index);
        alunos.put("nome",nome);
        alunos.put("filmes", listaFilmes);
        alunos.put("likes", likes);
        alunos.put("matricula", showId);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Alunos").document(userId);
        docRef.set(alunos).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","sucesso");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error","erro: " + e.toString());
            }
        });
    }

    private void startComponents() {
        editTextNomeCadastro = findViewById(R.id.editTextNomeCadastro);
        editTextIdCadastro = findViewById(R.id.editTextIdCadastro);
        button_cadastro = findViewById(R.id.button_cadastro);

    }
}