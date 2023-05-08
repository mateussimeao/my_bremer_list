package com.example.mybremerlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExibirPerfil extends AppCompatActivity {
    private TextView nome_profile, likes_profile;
    private Button button_like;
    RecyclerView recyclerView;
    ArrayList<Filme> listaFilmes;
    MyAdapter myAdapter;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exibir_perfil);
        getSupportActionBar().hide();
        startComponents();

        String id_pass = getIntent().getStringExtra("id_aluno");
        Query query = db.collection("Alunos").whereEqualTo("matricula", id_pass);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                if (!task.getResult().isEmpty()) {
                    DocumentSnapshot document = task.getResult().getDocuments().get(0);

                    String nome = document.getString("nome");
                    String likes = document.getString("likes") + " likes";
                    nome_profile.setText(nome);
                    likes_profile.setText(likes);
                    String userId = document.getId();
                    DocumentReference docRef = db.collection("Alunos").document(userId);
                    recyclerView = findViewById(R.id.recyclerview);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));
                    listaFilmes = new ArrayList<>();
                    myAdapter = new MyAdapter(this,listaFilmes);
                    recyclerView.setAdapter(myAdapter);

                    docRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        List<Map<String, Object>> filmes = (List<Map<String, Object>>) documentSnapshot.get("filmes");
                                        if (filmes != null) {
                                            for (Map<String, Object> filme : filmes) {
                                                String titulo = (String) filme.get("titulo");
                                                String ano = (String) filme.get("ano"); // precisa fazer cast de long para int
                                                Filme f = new Filme(titulo, ano);
                                                listaFilmes.add(f);
                                                Log.d("FILME", "teste"+listaFilmes);

                                            }
                                            myAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                }
            } else {
                Log.d("error", "Erro ao buscar o documento: ", task.getException());
            }
        });

        button_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = db.collection("Alunos").whereEqualTo("matricula", id_pass);
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        if (!task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String userId = document.getId();
                            DocumentReference docRef = db.collection("Alunos").document(userId);

                            docRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Snackbar snackbar = Snackbar.make(view,"Pefil curtido!", Snackbar.LENGTH_SHORT);
                                            snackbar.show();
                                            if (documentSnapshot.exists()) {
                                                String likesStr = documentSnapshot.getString("likes");
                                                int likes = Integer.parseInt(likesStr);
                                                likes++;
                                                docRef.update("likes", Integer.toString(likes));
                                            }
                                        }
                                    });
                        }
                    } else {
                        Log.d("error", "Erro ao buscar o documento: ", task.getException());
                    }
                });
            }
        });



    }

    private void startComponents () {
        nome_profile = findViewById(R.id.nome_profile);
        likes_profile = findViewById(R.id.likes_profile);
        button_like = findViewById(R.id.button_like);
    }
}