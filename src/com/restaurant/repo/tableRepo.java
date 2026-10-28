package com.restaurant.repo;

import java.util.ArrayList;
import java.util.List;
import com.restaurant.model.table;
public class tableRepo{
    private final List<table> tables=new ArrayList<>();

    public tableRepo(){
        tables.add(new table(1, 5));
        tables.add(new table(2, 2));
        tables.add(new table(3, 7));
    }

    public List<table> show(){
        return tables;
    }
}