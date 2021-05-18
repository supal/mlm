package com.supal.mlm.repository;

import com.supal.mlm.domain.MlmUser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MlmUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MlmUserRepository extends JpaRepository<MlmUser, Long> {
    @Query("select mlmUser from MlmUser mlmUser where mlmUser.userId.login = ?#{principal.username}")
    List<MlmUser> findByUserIdIsCurrentUser();
}
