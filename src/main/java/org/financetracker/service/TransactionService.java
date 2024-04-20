package org.financetracker.service;

import org.financetracker.model.Expense;
import org.financetracker.model.Income;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    double getTotalIncome();
    double getTotalExpense();

    List<Income> getAllIncomes();

    List<Expense> getAllExpenses();

    Income addIncome(Income income);

    Expense addExpense(Expense expense);

    Income getIncomeById(Long id);

    Expense getExpenseById(Long id);

    void updateIncome(Income income);

    void updateExpense(Expense expense);

    void deleteIncome(Long id);

    void deleteExpense(Long id);

    List<Income> getIncomesByDate(Date date);
    List<Expense> getExpensesByDate(Date date);

    Map<String, Double> getCategoryWiseIncome();

    Map<String, Double> getCategoryWiseExpense();

    Map<String, Double> getCategoryWiseIncomeByDate(Date date);
    Map<String, Double> getCategoryWiseExpenseByDate(Date date);

}
