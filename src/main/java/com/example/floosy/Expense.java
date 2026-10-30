package com.example.floosy;

import java.time.LocalDate;

public record Expense(long id, String description, long amount, String category, LocalDate date) {
}
