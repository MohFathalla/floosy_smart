package com.example.floosy;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Expense(long id, String description, BigDecimal amount, String category, LocalDate date) {
}
