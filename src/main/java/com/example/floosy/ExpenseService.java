package com.example.floosy;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.LinkedHashMap;

import java.math.BigDecimal;
import java.util.List;
import java.math.RoundingMode;

public class ExpenseService {
    static final List<String> CATEGORIES= List.of("اخرى","تسوق","فواتير","مواصلات","اكل");
    private final ExpenseRepository repository;
    private final ExpenseValidator validator;

    public ExpenseService (ExpenseRepository repository, ExpenseValidator validator){
        this.repository =  repository;
        this.validator = validator;
    }


    public void add(String description, BigDecimal amount, String category) {
        validator.validate(description, amount, category);
        repository.save(description.trim(), amount, category);
    }

    public List<Expense> all() {
        return repository.findAll();
    }

    public BigDecimal total() {
        return all().stream().map(Expense::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Map<String, BigDecimal> totalsByCategory() {
        var totals = new LinkedHashMap<String, BigDecimal>();
        categories().forEach(category -> totals.put(category, BigDecimal.ZERO));
        all().forEach(expense -> totals.merge(expense.category(), expense.amount(), BigDecimal::add));
        return totals;
    }

    public int spentPercent(BigDecimal budget) {
        return budget.signum() == 0 ? 0 : total().multiply(BigDecimal.valueOf(100)).divide(budget, 0, RoundingMode.HALF_UP).min(BigDecimal.valueOf(100)).intValue();
    }

    public List<String> categories() {
        return CATEGORIES;
    }

    @PostConstruct
    void start() {
        repository.save("فاتورة الإنترنت", BigDecimal.valueOf(450), "فواتير");
        repository.save("غداء", BigDecimal.valueOf(220), "أكل");
        repository.save("مواصلات العمل", BigDecimal.valueOf(150), "مواصلات");
        System.out.println("ExpenseService initialized");
    }

    @PreDestroy
    void stop() {
        System.out.println("ExpenseService destroyed");
    }
}






@Component
class ExpenseValidator {
    void validate(String description, BigDecimal amount, String category) {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("اكتب وصف المصروف");
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("المبلغ يجب أن يكون أكبر من صفر");
        if (!ExpenseService.CATEGORIES.contains(category)) throw new IllegalArgumentException("اختر التصنيف");
    }
}