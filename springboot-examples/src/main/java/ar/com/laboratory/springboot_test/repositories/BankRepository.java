package ar.com.laboratory.springboot_test.repositories;

import ar.com.laboratory.springboot_test.models.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank,Long> {
//    List<Bank> findAll();
//
//    Bank findById(Long id);
//
//    void update(Bank banco);

}
