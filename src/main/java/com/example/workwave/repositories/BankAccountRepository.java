package com.example.workwave.repositories;
import com.example.workwave.entities.BankAccount;
import com.example.workwave.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
public interface BankAccountRepository extends JpaRepository<BankAccount,Long> {
    BankAccount findByUserUserName(String user);
    List<BankAccount> findByStatusIsTrue();

    //***********
    //@Query("SELECT b.balance FROM BankAccount b WHERE b.id = :accountId AND b.updatedAt BETWEEN :startDate AND :endDate ORDER BY b.updatedAt ASC")
    //List<Double> findDailyBalancesForLast30Days(@Param("accountId") Long accountId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    //default List<Double> findDailyBalancePercentagesForLast30Days(Long accountId) {
    //    List<Double> dailyBalances = findDailyBalancesForLast30Days(accountId, LocalDate.now().minusDays(29), LocalDate.now());
    //    List<Double> dailyBalancePercentages = new ArrayList<>();
    //    double initialBalance = dailyBalances.get(0);
    //    for (double balance : dailyBalances) {
    //        dailyBalancePercentages.add((balance - initialBalance) / initialBalance * 100);
    //    }
    //    return dailyBalancePercentages;
    //}

    @Query(value = "SELECT DATE_SUB(CURDATE(), INTERVAL t.n DAY) AS date, SUM(tr.amount) AS total FROM (SELECT 0 n UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10 UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15 UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20 UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25 UNION SELECT 26 UNION SELECT 27 UNION SELECT 28 UNION SELECT 29) t LEFT JOIN transactions tr ON DATE(tr.transaction_date) = DATE_SUB(CURDATE(), INTERVAL t.n DAY) AND tr.bank_account_id = :accountId GROUP BY t.n ORDER BY t.n ASC", nativeQuery = true)
    List<Object[]> getBalanceHistory(@Param("accountId") Long accountId);

}

