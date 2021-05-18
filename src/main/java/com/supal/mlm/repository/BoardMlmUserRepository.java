package com.supal.mlm.repository;

import com.supal.mlm.domain.BoardMlmUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardMlmUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardMlmUserRepository extends JpaRepository<BoardMlmUser, Long> {}
