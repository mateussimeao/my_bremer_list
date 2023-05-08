package com.example.mybremerlist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PerfilUsuario extends AppCompatActivity {

    private TextView text_name, text_id;
    private Button button_sair;
    private Button button_list;
    private Button button_explore;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        getSupportActionBar().hide();
        startComponents();

        button_sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PerfilUsuario.this, TelaLogin.class);
                startActivity(intent);
                finish();
            }
        });

        button_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilUsuario.this, ListaFilmesUser.class);
                startActivity(intent);
            }
        });

        button_explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PerfilUsuario.this, TelaExplorar.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        int index = id.indexOf('@');
        String showId = id.substring(0, index);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference docRef = db.collection("Alunos").document(userId);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    text_name.setText(value.getString("nome"));
                    text_id.setText(showId);
                }
            }
        });


    }

    private void startComponents() {
        text_name = findViewById(R.id.text_name);
        text_id = findViewById(R.id.text_id);
        button_sair = findViewById(R.id.button_sair);
        button_list = findViewById(R.id.button_list);
        button_explore = findViewById(R.id.button_explore);

    }
}