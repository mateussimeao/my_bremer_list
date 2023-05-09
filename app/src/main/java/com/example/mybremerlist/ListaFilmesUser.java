package com.example.mybremerlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaFilmesUser extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    ArrayList<Filme> listaFilmes;
    private Button button_edit_list;
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DocumentReference docRef = db.collection("Alunos").document(userId);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_filmes_user);
        getSupportActionBar().hide();
        startComponents();

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
        button_edit_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listaFilmes.size()<4){
                    Intent intent = new Intent(ListaFilmesUser.this, EditLista.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Snackbar snackbar = Snackbar.make(view,"Limite de filmes atingido", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

    }

    private void startComponents() {
        button_edit_list = findViewById(R.id.button_edit_list);

    }
}