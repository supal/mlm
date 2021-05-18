package com.supal.mlm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.supal.mlm.IntegrationTest;
import com.supal.mlm.domain.MlmUser;
import com.supal.mlm.repository.MlmUserRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MlmUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MlmUserResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Boolean DEFAULT_PAID = false;
    private static final Boolean UPDATED_PAID = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Long DEFAULT_REF_MLM_USER_ID = 1L;
    private static final Long UPDATED_REF_MLM_USER_ID = 2L;

    private static final String ENTITY_API_URL = "/api/mlm-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MlmUserRepository mlmUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMlmUserMockMvc;

    private MlmUser mlmUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MlmUser createEntity(EntityManager em) {
        MlmUser mlmUser = new MlmUser()
            .userId(DEFAULT_USER_ID)
            .paid(DEFAULT_PAID)
            .active(DEFAULT_ACTIVE)
            .refMlmUserId(DEFAULT_REF_MLM_USER_ID);
        return mlmUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MlmUser createUpdatedEntity(EntityManager em) {
        MlmUser mlmUser = new MlmUser()
            .userId(UPDATED_USER_ID)
            .paid(UPDATED_PAID)
            .active(UPDATED_ACTIVE)
            .refMlmUserId(UPDATED_REF_MLM_USER_ID);
        return mlmUser;
    }

    @BeforeEach
    public void initTest() {
        mlmUser = createEntity(em);
    }

    @Test
    @Transactional
    void createMlmUser() throws Exception {
        int databaseSizeBeforeCreate = mlmUserRepository.findAll().size();
        // Create the MlmUser
        restMlmUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mlmUser)))
            .andExpect(status().isCreated());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeCreate + 1);
        MlmUser testMlmUser = mlmUserList.get(mlmUserList.size() - 1);
        assertThat(testMlmUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testMlmUser.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testMlmUser.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testMlmUser.getRefMlmUserId()).isEqualTo(DEFAULT_REF_MLM_USER_ID);
    }

    @Test
    @Transactional
    void createMlmUserWithExistingId() throws Exception {
        // Create the MlmUser with an existing ID
        mlmUser.setId(1L);

        int databaseSizeBeforeCreate = mlmUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMlmUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mlmUser)))
            .andExpect(status().isBadRequest());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllMlmUsers() throws Exception {
        // Initialize the database
        mlmUserRepository.saveAndFlush(mlmUser);

        // Get all the mlmUserList
        restMlmUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mlmUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].paid").value(hasItem(DEFAULT_PAID.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].refMlmUserId").value(hasItem(DEFAULT_REF_MLM_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getMlmUser() throws Exception {
        // Initialize the database
        mlmUserRepository.saveAndFlush(mlmUser);

        // Get the mlmUser
        restMlmUserMockMvc
            .perform(get(ENTITY_API_URL_ID, mlmUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mlmUser.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.paid").value(DEFAULT_PAID.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.refMlmUserId").value(DEFAULT_REF_MLM_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingMlmUser() throws Exception {
        // Get the mlmUser
        restMlmUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMlmUser() throws Exception {
        // Initialize the database
        mlmUserRepository.saveAndFlush(mlmUser);

        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();

        // Update the mlmUser
        MlmUser updatedMlmUser = mlmUserRepository.findById(mlmUser.getId()).get();
        // Disconnect from session so that the updates on updatedMlmUser are not directly saved in db
        em.detach(updatedMlmUser);
        updatedMlmUser.userId(UPDATED_USER_ID).paid(UPDATED_PAID).active(UPDATED_ACTIVE).refMlmUserId(UPDATED_REF_MLM_USER_ID);

        restMlmUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMlmUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMlmUser))
            )
            .andExpect(status().isOk());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
        MlmUser testMlmUser = mlmUserList.get(mlmUserList.size() - 1);
        assertThat(testMlmUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMlmUser.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testMlmUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMlmUser.getRefMlmUserId()).isEqualTo(UPDATED_REF_MLM_USER_ID);
    }

    @Test
    @Transactional
    void putNonExistingMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();
        mlmUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMlmUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mlmUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();
        mlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMlmUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(mlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();
        mlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMlmUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(mlmUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMlmUserWithPatch() throws Exception {
        // Initialize the database
        mlmUserRepository.saveAndFlush(mlmUser);

        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();

        // Update the mlmUser using partial update
        MlmUser partialUpdatedMlmUser = new MlmUser();
        partialUpdatedMlmUser.setId(mlmUser.getId());

        partialUpdatedMlmUser.userId(UPDATED_USER_ID).active(UPDATED_ACTIVE).refMlmUserId(UPDATED_REF_MLM_USER_ID);

        restMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMlmUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMlmUser))
            )
            .andExpect(status().isOk());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
        MlmUser testMlmUser = mlmUserList.get(mlmUserList.size() - 1);
        assertThat(testMlmUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMlmUser.getPaid()).isEqualTo(DEFAULT_PAID);
        assertThat(testMlmUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMlmUser.getRefMlmUserId()).isEqualTo(UPDATED_REF_MLM_USER_ID);
    }

    @Test
    @Transactional
    void fullUpdateMlmUserWithPatch() throws Exception {
        // Initialize the database
        mlmUserRepository.saveAndFlush(mlmUser);

        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();

        // Update the mlmUser using partial update
        MlmUser partialUpdatedMlmUser = new MlmUser();
        partialUpdatedMlmUser.setId(mlmUser.getId());

        partialUpdatedMlmUser.userId(UPDATED_USER_ID).paid(UPDATED_PAID).active(UPDATED_ACTIVE).refMlmUserId(UPDATED_REF_MLM_USER_ID);

        restMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMlmUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMlmUser))
            )
            .andExpect(status().isOk());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
        MlmUser testMlmUser = mlmUserList.get(mlmUserList.size() - 1);
        assertThat(testMlmUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testMlmUser.getPaid()).isEqualTo(UPDATED_PAID);
        assertThat(testMlmUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testMlmUser.getRefMlmUserId()).isEqualTo(UPDATED_REF_MLM_USER_ID);
    }

    @Test
    @Transactional
    void patchNonExistingMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();
        mlmUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mlmUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();
        mlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(mlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = mlmUserRepository.findAll().size();
        mlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMlmUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(mlmUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MlmUser in the database
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMlmUser() throws Exception {
        // Initialize the database
        mlmUserRepository.saveAndFlush(mlmUser);

        int databaseSizeBeforeDelete = mlmUserRepository.findAll().size();

        // Delete the mlmUser
        restMlmUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, mlmUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MlmUser> mlmUserList = mlmUserRepository.findAll();
        assertThat(mlmUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
