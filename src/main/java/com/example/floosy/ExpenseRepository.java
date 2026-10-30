package com.example.floosy;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ExpenseRepository
{
    private final List<Expense> expenses= new ArrayList<>();
    private long ids = 0;

    public Expense save(String description, long amount, String category){

        long id = nextId();
        var expense = new Expense(id, description,amount,category, LocalDate.now());
        expenses.add(expense);
        return expense;
    }

    private synchronized long nextId() {
        return ++ids;
    }
    public List<Expense> findAll(){
        return List.copyOf(expenses);
    }
}
