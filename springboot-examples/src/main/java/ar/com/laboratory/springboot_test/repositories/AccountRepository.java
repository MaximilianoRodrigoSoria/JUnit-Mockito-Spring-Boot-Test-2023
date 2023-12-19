package ar.com.laboratory.springboot_test.repositories;

import ar.com.laboratory.springboot_test.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
//    List<Account> findAll();
//
//    Account findById(Long id);
//
//    void save(Account cuenta);

    @Query("select a from Account a where a.person=?1")
    Optional<Account> findByPerson(String persona);

}
