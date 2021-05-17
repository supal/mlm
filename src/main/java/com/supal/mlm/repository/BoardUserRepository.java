package com.supal.mlm.repository;

import com.supal.mlm.domain.BoardUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the BoardUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {}
