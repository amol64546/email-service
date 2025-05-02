package com.test.emailtest.repo;

import com.test.emailtest.entity.EmailCount;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCountRepository extends JpaRepository<EmailCount, Long> {

  Optional<EmailCount> findByDate(LocalDate date);  // Find the count for a specific date

}
