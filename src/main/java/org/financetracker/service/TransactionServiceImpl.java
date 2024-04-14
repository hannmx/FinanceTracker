package org.financetracker.service;

import org.financetracker.model.Expense;
import org.financetracker.model.Income;
import org.financetracker.repository.ExpenseRepository;
import org.financetracker.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;

    @Autowired
    public TransactionServiceImpl(IncomeRepository incomeRepository, ExpenseRepository expenseRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    @Override
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Override
    public Income addIncome(Income income) {
        return incomeRepository.save(income);
    }

    @Override
    public Expense addExpense(Expense expense) {
        return expenseRepository.save(expense);
    }

    @Override
    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    @Override
    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    @Override
    public void updateIncome(Income income) {
        incomeRepository.save(income);
    }

    @Override
    public void updateExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    @Override
    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }

    @Override
    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    @Override
    public List<Income> getIncomesByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return incomeRepository.findByYearMonthDay(year, month, day);
    }

    @Override
    public List<Expense> getExpensesByDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return expenseRepository.findByYearMonthDay(year, month, day);
    }

    @Override
    public double getTotalIncome() {
        List<Income> incomes = getAllIncomes();
        return incomes.stream().mapToDouble(Income::getAmount).sum();
    }

    @Override
    public double getTotalExpense() {
        List<Expense> expenses = getAllExpenses();
        return expenses.stream().mapToDouble(Expense::getAmount).sum();
    }

    @Override
    public Map<String, Double> getCategoryWiseIncome() {
        List<Income> incomes = getAllIncomes();
        Map<String, Double> categoryWiseIncome = new HashMap<>();
        for (Income income : incomes) {
            String category = income.getCategory();
            double amount = income.getAmount();
            categoryWiseIncome.put(category, categoryWiseIncome.getOrDefault(category, 0.0) + amount);
        }
        return categoryWiseIncome;
    }

    @Override
    public Map<String, Double> getCategoryWiseExpense() {
        List<Expense> expenses = getAllExpenses();
        Map<String, Double> categoryWiseExpense = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categoryWiseExpense.put(category, categoryWiseExpense.getOrDefault(category, 0.0) + amount);
        }
        return categoryWiseExpense;
    }
}

