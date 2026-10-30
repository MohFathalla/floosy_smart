package com.example.floosy;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
public class ExpenseController {
    private final ExpenseService Serv;

    public ExpenseController(ExpenseService service) {
        this.Serv = service;
    }

    @GetMapping("/api/expenses")
    public List<Expense> apiAll() {
        return Serv.all();
    }

    @PostMapping("/api/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    public Expense apiAdd(@RequestBody ExpenseRequest request) {
        return Serv.add(request.description(), request.amount(), request.category());
    }

}
