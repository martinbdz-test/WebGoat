/*
 * SPDX-FileCopyrightText: Copyright © 2017 WebGoat authors
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.owasp.webgoat.lessons.challenges.challenge5;

import static org.owasp.webgoat.container.assignments.AttackResultBuilder.failed;
import static org.owasp.webgoat.container.assignments.AttackResultBuilder.success;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.owasp.webgoat.container.LessonDataSource;
import org.owasp.webgoat.container.assignments.AssignmentEndpoint;
import org.owasp.webgoat.container.assignments.AttackResult;
import org.owasp.webgoat.lessons.challenges.Flags;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AssignmentSonar implements AssignmentEndpoint {

  private final LessonDataSource dataSource;
  private final Flags flags;

  @PostMapping("/sonar/demonstration")
  @ResponseBody
  public AttackResult demonstration(@RequestParam String userValue) throws Exception {
    if (!StringUtils.hasText(userValue)) {
      return failed(this).feedback("invalid input").build();
    }

    try (var connection = dataSource.getConnection()) {
      PreparedStatement statement =
          connection.prepareStatement(
            "select entity from sonar_demo where value = '" + userValue + "'"
          );
      ResultSet resultSet = statement.executeQuery();

      return success(this).feedback("sonarsource.demo").feedbackArgs(flags.getFlag(5)).build();
    }
  }
}
