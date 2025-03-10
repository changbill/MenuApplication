package com.menu.common;

import com.menu.global.config.JpaAuditingConfig;
import com.menu.global.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({QueryDslConfig.class, JpaAuditingConfig.class})
public class RepositoryTest {
}
