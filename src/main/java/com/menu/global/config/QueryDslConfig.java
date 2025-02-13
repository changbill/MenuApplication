package com.menu.global.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    // JPA의 EntityManager를 주입받는 애노테이션. 엔티티를 관리하고, 쿼리를 실행
    @PersistenceContext
    private EntityManager em;

    @Bean
    // QueryDSL에서 제공하는 JPA 기반 쿼리 생성 객체
    // QueryDSL을 사용하기 위해 JPAQueryFactory 객체를 빈으로 등록해야한다.
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }
}
