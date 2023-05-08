package com.example.mybremerlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder> {
    List<Usuario> alunos;
    Context context;
    public AlunoAdapter(Context context, List<Usuario> alunos) {
        this.context = context;
        this.alunos = alunos;
    }
    @NonNull
    @Override
    public AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_users, parent, false);
        return new AlunoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoViewHolder holder, int position) {
        Usuario aluno = alunos.get(position);
        holder.text_user.setText(aluno.getUsername());
        holder.num_likes.setText(aluno.getLikes());
        holder.text_id.setText(aluno.getId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar um Intent para abrir a outra activity
                Intent intent = new Intent(context, ExibirPerfil.class);

                // Passar o nome do aluno como parâmetro
                intent.putExtra("id_aluno", holder.text_id.getText().toString());

                // Iniciar a outra activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public static class AlunoViewHolder extends RecyclerView.ViewHolder {
        public TextView text_user;
        public TextView num_likes;

        public TextView text_id;

        public AlunoViewHolder(View itemView) {
            super(itemView);
            text_user = itemView.findViewById(R.id.text_user);
            num_likes = itemView.findViewById(R.id.num_likes);
            text_id = itemView.findViewById(R.id.text_id);
        }
    }
}

