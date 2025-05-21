package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;

@Controller
public class ExpenseController {

    @Autowired
    private TransactionService service;

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        return "add";
    }

    @PostMapping("/add")
    public String addTransaction(@ModelAttribute Transaction transaction) {
        transaction.setDate(LocalDate.now());
        service.addTransaction(transaction);
        return "redirect:/summary";
    }

    @GetMapping("/summary")
    public String monthlySummary(@RequestParam(defaultValue = "5") int month,
                                 @RequestParam(defaultValue = "2025") int year,
                                 Model model) {
        model.addAttribute("transactions", service.getMonthlySummary(month, year));
        return "summary";
    }

    @PostMapping("/import")
    public String importFile(@RequestParam("file") MultipartFile file) throws IOException {
        service.importFromFile(file);
        return "redirect:/summary";
    }
}
