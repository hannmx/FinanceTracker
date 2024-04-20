package org.financetracker.controller;

import org.financetracker.model.Expense;
import org.financetracker.model.Income;
import org.financetracker.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Income> incomes = transactionService.getAllIncomes();
        List<Expense> expenses = transactionService.getAllExpenses();
        model.addAttribute("incomes", incomes);
        model.addAttribute("expenses", expenses);
        return "index";
    }

    @GetMapping("/addTransaction")
    public String showAddTransactionForm(Model model) {
        model.addAttribute("income", new Income());
        model.addAttribute("expense", new Expense());
        return "add_transaction_form";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(@RequestParam("type") String type,
                                 @RequestParam("category") String category, // Добавление параметра category
                                 @RequestParam("amount") double amount,
                                 @RequestParam("description") String description,
                                 Model model) {
        if (type.equals("income")) {
            Income income = new Income();
            income.setAmount(amount);
            income.setDescription(description);
            income.setCategory(category); // Сохранение категории
            income.setDate(new Date());
            transactionService.addIncome(income);
        } else if (type.equals("expense")) {
            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setDescription(description);
            expense.setCategory(category); // Сохранение категории
            expense.setDate(new Date());
            transactionService.addExpense(expense);
        }
        return "redirect:/";
    }

    @GetMapping("/editTransaction")
    public String showEditTransactionForm(@RequestParam Long id, Model model) {
        // Get the transaction by id
        Income income = transactionService.getIncomeById(id);
        Expense expense = transactionService.getExpenseById(id);

        // Check if it's an income or an expense
        if (income != null) {
            model.addAttribute("transaction", income);
        } else if (expense != null) {
            model.addAttribute("transaction", expense);
        } else {
            return "error";
        }

        return "edit_transaction_form";
    }

    @GetMapping("/analysis")
    public String showAnalysisPage(Model model) {

        Date currentDate = new Date();
        List<Income> incomes = transactionService.getAllIncomes();
        List<Expense> expenses = transactionService.getAllExpenses();

        List<Income> incomesToday = transactionService.getIncomesByDate(currentDate);
        List<Expense> expensesToday = transactionService.getExpensesByDate(currentDate);

        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalIncome - totalExpense;

        Map<String, Double> incomeByCategory = transactionService.getCategoryWiseIncome();
        Map<String, Double> expenseByCategory = transactionService.getCategoryWiseExpense();

        // Добавляем данные в модель
        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("totalExpense", totalExpense);
        model.addAttribute("balance", balance);
        model.addAttribute("incomeByCategory", incomeByCategory);
        model.addAttribute("expenseByCategory", expenseByCategory);
        model.addAttribute("incomesToday", incomesToday);
        model.addAttribute("expensesToday", expensesToday);

        return "analysis";
    }

    @GetMapping("/analysis/filter")
    @ResponseBody
    public Map<String, Object> filterAnalysisByDate(@RequestParam(required = false) String selectedDate) {
        if (selectedDate == null || selectedDate.trim().isEmpty()) {
            // Если дата не выбрана, используйте методы для получения данных за все время
            return getDataForAllTime();
        } else {
            // Парсинг даты и получение данных для выбранной даты
            return getDataForSelectedDate(selectedDate);
        }
    }

    private Map<String, Object> getDataForAllTime() {
        // Получение данных за все время
        double totalIncome = transactionService.getTotalIncome();
        double totalExpense = transactionService.getTotalExpense();
        double balance = totalIncome - totalExpense;
        Map<String, Double> incomeByCategory = transactionService.getCategoryWiseIncome();
        Map<String, Double> expenseByCategory = transactionService.getCategoryWiseExpense();

        // Формируем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("totalIncome", totalIncome);
        response.put("totalExpense", totalExpense);
        response.put("balance", balance);
        response.put("incomeByCategory", incomeByCategory);
        response.put("expenseByCategory", expenseByCategory);

        return response;
    }

    private Map<String, Object> getDataForSelectedDate(String selectedDate) {
        // Парсинг даты
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = dateFormat.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return Collections.emptyMap(); // Возвращаем пустой результат в случае ошибки парсинга
        }

        // Получаем данные для выбранной даты
        List<Income> incomes = transactionService.getIncomesByDate(date);
        List<Expense> expenses = transactionService.getExpensesByDate(date);

        // Вычисляем общий доход и расход за выбранную дату
        double totalIncome = incomes.stream().mapToDouble(Income::getAmount).sum();
        double totalExpense = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalIncome - totalExpense;

        // Получаем категории доходов и расходов за выбранную дату
        Map<String, Double> incomeByCategory = transactionService.getCategoryWiseIncomeByDate(date);
        Map<String, Double> expenseByCategory = transactionService.getCategoryWiseExpenseByDate(date);

        // Формируем ответ
        Map<String, Object> response = new HashMap<>();
        response.put("totalIncome", totalIncome);
        response.put("totalExpense", totalExpense);
        response.put("balance", balance);
        response.put("incomeByCategory", incomeByCategory);
        response.put("expenseByCategory", expenseByCategory);

        return response;
    }



    @PostMapping("/editTransaction")
    public String editTransaction(@ModelAttribute Income income, @ModelAttribute Expense expense, Model model) {
        if (income != null) {
            transactionService.updateIncome(income);
        } else if (expense != null) {
            transactionService.updateExpense(expense);
        } else {
            return "error";
        }

        return "redirect:/";
    }

    @PostMapping("/deleteTransaction")
    public String deleteTransaction(@RequestParam Long id, Model model) {
        Income income = transactionService.getIncomeById(id);
        Expense expense = transactionService.getExpenseById(id);

        if (income != null) {
            transactionService.deleteIncome(id);
        } else if (expense != null) {
            transactionService.deleteExpense(id);
        } else {
            return "error";
        }

        return "redirect:/";
    }
}
