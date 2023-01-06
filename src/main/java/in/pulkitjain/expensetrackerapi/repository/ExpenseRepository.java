package in.pulkitjain.expensetrackerapi.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.pulkitjain.expensetrackerapi.entity.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>{
//SELECT * FROM tbl_expenses WHERE category=?	
Page<Expense> findByUserIdAndCategory(Long userId, String category,Pageable page);
//SELECT * FROM tbl_expenses WHERE name LIKE '%keyword%'
Page<Expense> findByUserIdAndNameContaining(Long userId,String keyword,Pageable page);
//SELECT * FROM tbl_expenses WHERE date BETWEEN 'startDate' AND 'endDate'
Page<Expense> findByUserIdAndDateBetween(Long userId,Date startDate,Date endDate,Pageable page);

Page<Expense> findByUserId(Long userId,Pageable page);

Optional<Expense> findByUserIdAndId(Long userId,Long expenseId);
}
