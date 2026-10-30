package com.example.floosy;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
public class ExpenseService {
    static final List<String> CATEGORIES= List.of("اخرى","تسوق","فواتير","مواصلات","اكل");
    private final ExpenseRepository repository;
    private final ExpenseValidator validator;

    public ExpenseService (ExpenseRepository Repo, ExpenseValidator Vali){
        this.repository =  Repo;
        this.validator = Vali;
    }


    public Expense add(String description, long amountV, String catV) {
        validator.validate(description, amountV, catV);
        return repository.save(description.trim(), amountV, catV);
    }

    public List<Expense> all() {
        return repository.findAll();
    }

    public long total() {
        long sum = 0;
        for (Expense expense : all()) {
            sum = sum + expense.amount();
                                        }
        return sum;    }


    public Map<String, Long> totalsByCategory() {
        Map<String, Long> totals = new HashMap<>();

        for (String category : categories()) {
            totals.put(category, 0L);        }

        for (Expense expense : all()) {
            String category = expense.category();
            long amount = expense.amount();
            long oldTotal = totals.getOrDefault(category, 0L);
            totals.put(category, oldTotal + amount);
        }
        return totals;    }

    public int spentPercent(long budget) {
        return budget == 0 ? 0 : (int) Math.min(100, total() * 100L / budget);
    }

    public List<String> categories() {
        return CATEGORIES;
    }

    @PostConstruct
    void start() {
        repository.save("فاتورة الإنترنت", 450L, "فواتير");
        repository.save("غداء", 220L, "أكل");
        repository.save("مواصلات العمل", 150L, "مواصلات");
        System.out.println("ExpenseService initialized");
    }

    @PreDestroy
    void stop() {
        System.out.println("ExpenseService destroyed");
    }
}






@Component
class ExpenseValidator {
    void validate(String description, long amount, String category) {
        if (description == null || description.isBlank()) throw new IllegalArgumentException("اكتب وصف المصروف");
        if (amount <= 0)
            throw new IllegalArgumentException("المبلغ يجب أن يكون أكبر من صفر");
        if (!ExpenseService.CATEGORIES.contains(category)) throw new IllegalArgumentException("اختر التصنيف");
    }
}
