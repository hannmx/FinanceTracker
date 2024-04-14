package org.financetracker.repository;

import org.financetracker.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {
    @Query("SELECT i FROM Income i WHERE YEAR(i.date) = :year AND MONTH(i.date) = :month AND DAY(i.date) = :day")
    List<Income> findByYearMonthDay(@Param("year") int year, @Param("month") int month, @Param("day") int day);
}
