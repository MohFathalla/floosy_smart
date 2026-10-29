package com.example.floosy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ExpenseRepository
{
    private final List<Expense> expenses= new CopyOnWriteArrayList<>();
    private final AtomicLong ids = new AtomicLong();

    public Expense save(String description, BigDecimal amount, String category){
        var expense = new Expense(ids.incrementAndGet(), description,amount,category, LocalDate.now());

        return expense;
    }

    public List<Expense> findAll(){
        return List.copyOf(expenses);
    }
}
