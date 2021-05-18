package com.supal.mlm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.supal.mlm.IntegrationTest;
import com.supal.mlm.domain.BoardMlmUser;
import com.supal.mlm.repository.BoardMlmUserRepository;
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
 * Integration tests for the {@link BoardMlmUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardMlmUserResourceIT {

    private static final Long DEFAULT_BOARD_ID = 1L;
    private static final Long UPDATED_BOARD_ID = 2L;

    private static final Long DEFAULT_MLM_USER_ID = 1L;
    private static final Long UPDATED_MLM_USER_ID = 2L;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String ENTITY_API_URL = "/api/board-mlm-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoardMlmUserRepository boardMlmUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardMlmUserMockMvc;

    private BoardMlmUser boardMlmUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardMlmUser createEntity(EntityManager em) {
        BoardMlmUser boardMlmUser = new BoardMlmUser().boardId(DEFAULT_BOARD_ID).mlmUserId(DEFAULT_MLM_USER_ID).level(DEFAULT_LEVEL);
        return boardMlmUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardMlmUser createUpdatedEntity(EntityManager em) {
        BoardMlmUser boardMlmUser = new BoardMlmUser().boardId(UPDATED_BOARD_ID).mlmUserId(UPDATED_MLM_USER_ID).level(UPDATED_LEVEL);
        return boardMlmUser;
    }

    @BeforeEach
    public void initTest() {
        boardMlmUser = createEntity(em);
    }

    @Test
    @Transactional
    void createBoardMlmUser() throws Exception {
        int databaseSizeBeforeCreate = boardMlmUserRepository.findAll().size();
        // Create the BoardMlmUser
        restBoardMlmUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardMlmUser)))
            .andExpect(status().isCreated());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeCreate + 1);
        BoardMlmUser testBoardMlmUser = boardMlmUserList.get(boardMlmUserList.size() - 1);
        assertThat(testBoardMlmUser.getBoardId()).isEqualTo(DEFAULT_BOARD_ID);
        assertThat(testBoardMlmUser.getMlmUserId()).isEqualTo(DEFAULT_MLM_USER_ID);
        assertThat(testBoardMlmUser.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    void createBoardMlmUserWithExistingId() throws Exception {
        // Create the BoardMlmUser with an existing ID
        boardMlmUser.setId(1L);

        int databaseSizeBeforeCreate = boardMlmUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardMlmUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardMlmUser)))
            .andExpect(status().isBadRequest());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoardMlmUsers() throws Exception {
        // Initialize the database
        boardMlmUserRepository.saveAndFlush(boardMlmUser);

        // Get all the boardMlmUserList
        restBoardMlmUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardMlmUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].boardId").value(hasItem(DEFAULT_BOARD_ID.intValue())))
            .andExpect(jsonPath("$.[*].mlmUserId").value(hasItem(DEFAULT_MLM_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }

    @Test
    @Transactional
    void getBoardMlmUser() throws Exception {
        // Initialize the database
        boardMlmUserRepository.saveAndFlush(boardMlmUser);

        // Get the boardMlmUser
        restBoardMlmUserMockMvc
            .perform(get(ENTITY_API_URL_ID, boardMlmUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boardMlmUser.getId().intValue()))
            .andExpect(jsonPath("$.boardId").value(DEFAULT_BOARD_ID.intValue()))
            .andExpect(jsonPath("$.mlmUserId").value(DEFAULT_MLM_USER_ID.intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }

    @Test
    @Transactional
    void getNonExistingBoardMlmUser() throws Exception {
        // Get the boardMlmUser
        restBoardMlmUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBoardMlmUser() throws Exception {
        // Initialize the database
        boardMlmUserRepository.saveAndFlush(boardMlmUser);

        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();

        // Update the boardMlmUser
        BoardMlmUser updatedBoardMlmUser = boardMlmUserRepository.findById(boardMlmUser.getId()).get();
        // Disconnect from session so that the updates on updatedBoardMlmUser are not directly saved in db
        em.detach(updatedBoardMlmUser);
        updatedBoardMlmUser.boardId(UPDATED_BOARD_ID).mlmUserId(UPDATED_MLM_USER_ID).level(UPDATED_LEVEL);

        restBoardMlmUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoardMlmUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoardMlmUser))
            )
            .andExpect(status().isOk());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
        BoardMlmUser testBoardMlmUser = boardMlmUserList.get(boardMlmUserList.size() - 1);
        assertThat(testBoardMlmUser.getBoardId()).isEqualTo(UPDATED_BOARD_ID);
        assertThat(testBoardMlmUser.getMlmUserId()).isEqualTo(UPDATED_MLM_USER_ID);
        assertThat(testBoardMlmUser.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void putNonExistingBoardMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();
        boardMlmUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMlmUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardMlmUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardMlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoardMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();
        boardMlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMlmUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardMlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoardMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();
        boardMlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMlmUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardMlmUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardMlmUserWithPatch() throws Exception {
        // Initialize the database
        boardMlmUserRepository.saveAndFlush(boardMlmUser);

        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();

        // Update the boardMlmUser using partial update
        BoardMlmUser partialUpdatedBoardMlmUser = new BoardMlmUser();
        partialUpdatedBoardMlmUser.setId(boardMlmUser.getId());

        partialUpdatedBoardMlmUser.boardId(UPDATED_BOARD_ID).mlmUserId(UPDATED_MLM_USER_ID).level(UPDATED_LEVEL);

        restBoardMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardMlmUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardMlmUser))
            )
            .andExpect(status().isOk());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
        BoardMlmUser testBoardMlmUser = boardMlmUserList.get(boardMlmUserList.size() - 1);
        assertThat(testBoardMlmUser.getBoardId()).isEqualTo(UPDATED_BOARD_ID);
        assertThat(testBoardMlmUser.getMlmUserId()).isEqualTo(UPDATED_MLM_USER_ID);
        assertThat(testBoardMlmUser.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void fullUpdateBoardMlmUserWithPatch() throws Exception {
        // Initialize the database
        boardMlmUserRepository.saveAndFlush(boardMlmUser);

        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();

        // Update the boardMlmUser using partial update
        BoardMlmUser partialUpdatedBoardMlmUser = new BoardMlmUser();
        partialUpdatedBoardMlmUser.setId(boardMlmUser.getId());

        partialUpdatedBoardMlmUser.boardId(UPDATED_BOARD_ID).mlmUserId(UPDATED_MLM_USER_ID).level(UPDATED_LEVEL);

        restBoardMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardMlmUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardMlmUser))
            )
            .andExpect(status().isOk());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
        BoardMlmUser testBoardMlmUser = boardMlmUserList.get(boardMlmUserList.size() - 1);
        assertThat(testBoardMlmUser.getBoardId()).isEqualTo(UPDATED_BOARD_ID);
        assertThat(testBoardMlmUser.getMlmUserId()).isEqualTo(UPDATED_MLM_USER_ID);
        assertThat(testBoardMlmUser.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    void patchNonExistingBoardMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();
        boardMlmUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boardMlmUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardMlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoardMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();
        boardMlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardMlmUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoardMlmUser() throws Exception {
        int databaseSizeBeforeUpdate = boardMlmUserRepository.findAll().size();
        boardMlmUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardMlmUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(boardMlmUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardMlmUser in the database
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoardMlmUser() throws Exception {
        // Initialize the database
        boardMlmUserRepository.saveAndFlush(boardMlmUser);

        int databaseSizeBeforeDelete = boardMlmUserRepository.findAll().size();

        // Delete the boardMlmUser
        restBoardMlmUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, boardMlmUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoardMlmUser> boardMlmUserList = boardMlmUserRepository.findAll();
        assertThat(boardMlmUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
