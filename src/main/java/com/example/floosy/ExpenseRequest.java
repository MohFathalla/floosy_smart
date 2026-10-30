package com.example.floosy;

public record ExpenseRequest(String description, long amount, String category) {
}
