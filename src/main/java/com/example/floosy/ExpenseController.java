package com.example.floosy;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public class ExpenseController {
    private final ExpenseService service;
    private final ObjectProvider<FloosyApplication.ExpenseDraft> drafts;
    private final FloosyApplication.RequestInfo request;
    private final Environment environment;
    private final BigDecimal budget;
    private final String currency;

    public ExpenseController(ExpenseService service, ObjectProvider<FloosyApplication.ExpenseDraft> draft, FloosyApplication.RequestInfo request, Environment environment, @Value("${app.budget}") BigDecimal budget, @Value("${app.currency}") String curreny)
    {
        this.service = service;
        this.drafts = draft;
        this.request = request;
        this.environment = environment;
        this.budget = budget ;
        this.currency = curreny;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(required = false) String saved, @RequestParam(required = false) String error) {
        model.addAttribute("expenses", service.all());
        model.addAttribute("total", service.total());
        model.addAttribute("categories", service.categories());
        model.addAttribute("categoryTotals", service.totalsByCategory());
        model.addAttribute("budget", budget);
        model.addAttribute("currency", currency);
        model.addAttribute("spentPercent", service.spentPercent(budget));
        model.addAttribute("profile", String.join(", ", environment.getActiveProfiles()));
        model.addAttribute("requestId", request.id());
        model.addAttribute("draftId", drafts.getObject().id());
        model.addAttribute("message", saved == null ? null : "تم حفظ المصروف");
        model.addAttribute("error", error == null ? null : "راجع بيانات المصروف");
        return "index";
    }

    @PostMapping("/expenses")
    public String add(@RequestParam String description, @RequestParam BigDecimal amount, @RequestParam String category) {
        try {
            service.add(description, amount, category);
            return "redirect:/?saved=1";
        } catch (IllegalArgumentException exception) {
            return "redirect:/?error=1";
        }
    }

}
