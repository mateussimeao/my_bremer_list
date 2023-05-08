package com.example.mybremerlist;

public class Usuario {
    private String username;
    private String likes;
    private String id;

    public Usuario(String username, String likes, String id) {
        this.username = username;
        this.likes = likes;
        this.id = id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
