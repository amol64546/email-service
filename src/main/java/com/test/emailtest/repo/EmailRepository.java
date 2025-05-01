package com.test.emailtest.repo;

import com.test.emailtest.entity.EmailRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailRequest, String> {

}
