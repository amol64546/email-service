package com.test.emailtest.service;

import com.test.emailtest.entity.EmailCount;
import com.test.emailtest.exception.EmailLimitExceededException;
import com.test.emailtest.repo.EmailCountRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailTrackingService {

  private final EmailCountRepository emailCountRepository;

  @Transactional
  public void incrementEmailCount() {
    LocalDate today = LocalDate.now();  // Get the current date

    // Find if an entry for today already exists
    EmailCount emailCount = emailCountRepository.findByDate(today)
        .orElseGet(() -> {
          // If not, create a new entry for today
          EmailCount newEmailCount = new EmailCount();
          newEmailCount.setDate(today);
          newEmailCount.setCount(0);  // Start with 0 emails sent
          return newEmailCount;
        });

    if (emailCount.getCount() >= 100) {
      throw new EmailLimitExceededException("The daily email limit of 100 has been reached.");
    }

    // Increment the email count
    emailCount.setCount(emailCount.getCount() + 1);

    // Save the updated entry
    emailCountRepository.save(emailCount);
  }

  public int getEmailCountForToday() {
    LocalDate today = LocalDate.now();
    return emailCountRepository.findByDate(today)
        .map(EmailCount::getCount)
        .orElse(0);  // If no records found, return 0
  }
}
