package org.financetracker.repository;

import org.financetracker.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Query("SELECT e FROM Expense e WHERE YEAR(e.date) = :year AND MONTH(e.date) = :month AND DAY(e.date) = :day")
    List<Expense> findByYearMonthDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);
}

