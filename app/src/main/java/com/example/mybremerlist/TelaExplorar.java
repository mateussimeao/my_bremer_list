package com.example.mybremerlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;

public class TelaExplorar extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    CollectionReference colRef = db.collection("Alunos");
    RecyclerView recyclerView;
    AlunoAdapter alunoAdapter;
    ArrayList<Usuario> listaUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_explorar);
        getSupportActionBar().hide();


        recyclerView = findViewById(R.id.lista_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaUsers = new ArrayList<>();
        alunoAdapter = new AlunoAdapter(this,listaUsers);
        recyclerView.setAdapter(alunoAdapter);
        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String nome = documentSnapshot.getString("nome");
                    String likes = documentSnapshot.getString("likes");
                    String id = documentSnapshot.getString("matricula");
                    String uid = documentSnapshot.getId();

                    Usuario user = new Usuario(nome, likes, id);

                    if(!userId.equals(uid)) {
                        listaUsers.add(user);
                    }
                }
                alunoAdapter.notifyDataSetChanged();
            }
        });

    }
}