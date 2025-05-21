package com.example.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionType;
import com.example.demo.repository.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository repo;

    public void addTransaction(Transaction transaction) {
        repo.save(transaction);
    }

    public List<Transaction> getMonthlySummary(int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return repo.findByDateBetween(start, end);
    }

    public void importFromFile(MultipartFile file) throws IOException {
        List<String> lines = new BufferedReader(new InputStreamReader(file.getInputStream()))
        		.lines().skip(1).toList(); // skip header

        for (String line : lines) {
            String[] parts = line.split(",");
            Transaction tx = new Transaction();
            tx.setSubCategory(parts[0]);
            tx.setAmount(Double.parseDouble(parts[1]));
            tx.setCategory(TransactionType.valueOf(parts[2].toUpperCase()));
            tx.setDate(LocalDate.parse(parts[3].trim()));
            repo.save(tx);
        }
    }
}
