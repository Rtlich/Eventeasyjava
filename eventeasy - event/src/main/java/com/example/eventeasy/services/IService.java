package com.example.eventeasy.services;

import java.util.List;

public interface IService<T> {

    public boolean add(T t);

    public boolean delete(T t);

    public boolean edit(T t);

    public List<T> getAll();

}