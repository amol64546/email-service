package com.test.emailtest.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Data
public class EmailRequest {

  @Id
  private String id;

  @ElementCollection
  @CollectionTable(name = "email_request_to", joinColumns = @JoinColumn(name = "email_request_id"))
  @Column(name = "to_address")
  private Set<String> to = new HashSet<>();

  private String subject;

  private String body;

  @JsonIgnore
  private String fromEmail;

  private int total;

  private int success;

  private int failed;

  @JsonIgnore
  @Transient
  private String apiKey;

  @Transient
  private MultipartFile attachment;

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
