package in.pulkitjain.expensetrackerapi.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.naming.java.javaURLContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import in.pulkitjain.expensetrackerapi.entity.Expense;
import in.pulkitjain.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.pulkitjain.expensetrackerapi.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	@Autowired
	private ExpenseRepository expenseRepo;
	
	@Autowired
	private UserService userService;

	@Override
	public Page<Expense> getAllExpenses(Pageable page) {
		// TODO Auto-generated method stub
		return expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page);
	}

	@Override
	public Expense getExpenseById(Long id) {
		// TODO Auto-generated method stub
		Optional<Expense> expense = expenseRepo.findByUserIdAndId(userService.getLoggedInUser().getId(), id);
		if (expense.isPresent()) {
			return expense.get();
		}
		throw new ResourceNotFoundException("Expense is not found for the id " + id);
	}

	@Override
	public void deleteExpenseById(Long id) {
		Expense expense=getExpenseById(id);
		expenseRepo.delete(expense);

	}

	@Override
	public Expense saveExpenseDetails(Expense expense) {
		expense.setUser(userService.getLoggedInUser());
		return expenseRepo.save(expense);
	}

	@Override
	public Expense updateExpenseDetails(Long id, Expense expense) {

		Expense existingExpense = getExpenseById(id);
		existingExpense.setName(expense.getName() != null ? expense.getName() : existingExpense.getName());
		existingExpense.setDescription(
				expense.getDescription() != null ? expense.getDescription() : existingExpense.getDescription());
		existingExpense
				.setCategory(expense.getCategory() != null ? expense.getCategory() : existingExpense.getCategory());
		existingExpense.setDate(expense.getDate() != null ? expense.getDate() : existingExpense.getDate());
		existingExpense.setAmount(expense.getAmount() != null ? expense.getAmount() : existingExpense.getAmount());
		return expenseRepo.save(existingExpense);
	}

	@Override
	public List<Expense> readByCategory(String category, Pageable page) {
		// TODO Auto-generated method stub
		return expenseRepo.findByUserIdAndCategory(userService.getLoggedInUser().getId(),category, page).toList();
	}

	@Override
	public List<Expense> readByName(String keyword, Pageable page) {

		return expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(),keyword, page).toList();
	}

	@Override
	public List<Expense> readByDate(java.sql.Date startDate, java.sql.Date endDate, Pageable page) {
		if (startDate == null) {
			startDate = new java.sql.Date(0);
		}
		if (endDate == null) {
			endDate = new java.sql.Date(System.currentTimeMillis());
		}
		return expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(),startDate, endDate, page).toList();
	}

}
