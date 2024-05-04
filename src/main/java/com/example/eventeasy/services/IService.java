package com.example.eventeasy.services;

import java.util.List;

public interface IService<T> {

    public boolean ajouter(T t);

    public boolean supprimer(T t);

    public boolean modifier(T t);

    public List<T> afficher();

}