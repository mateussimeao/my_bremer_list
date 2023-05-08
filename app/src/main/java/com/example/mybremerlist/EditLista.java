package com.example.mybremerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EditLista extends AppCompatActivity {

    private EditText editTextNomeFilme;
    private EditText editTextAnoFilme;
    private Button button_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lista);
        getSupportActionBar().hide();
        startComponents();

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_filme = editTextNomeFilme.getText().toString();
                String ano_filme = editTextAnoFilme.getText().toString();
                Filme filme = new Filme(nome_filme, ano_filme);
                if(nome_filme.isEmpty() || ano_filme.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view,"Preencha todos os campos", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DocumentReference docRef = db.collection("Alunos").document(userId);
                    docRef.update("filmes", FieldValue.arrayUnion(filme)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                    Intent intent = new Intent(EditLista.this, ListaFilmesUser.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void startComponents() {
        editTextNomeFilme = findViewById(R.id.editTextNomeFilme);
        editTextAnoFilme = findViewById(R.id.editTextAnoFilme);
        button_add = findViewById(R.id.button_add);
    }
}