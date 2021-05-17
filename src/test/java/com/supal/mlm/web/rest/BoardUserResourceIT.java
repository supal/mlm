package com.supal.mlm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.supal.mlm.IntegrationTest;
import com.supal.mlm.domain.BoardUser;
import com.supal.mlm.repository.BoardUserRepository;
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
 * Integration tests for the {@link BoardUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BoardUserResourceIT {

    private static final Long DEFAULT_BOARD_ID = 1L;
    private static final Long UPDATED_BOARD_ID = 2L;

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/board-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BoardUserRepository boardUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBoardUserMockMvc;

    private BoardUser boardUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardUser createEntity(EntityManager em) {
        BoardUser boardUser = new BoardUser().boardId(DEFAULT_BOARD_ID).userId(DEFAULT_USER_ID).level(DEFAULT_LEVEL).status(DEFAULT_STATUS);
        return boardUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BoardUser createUpdatedEntity(EntityManager em) {
        BoardUser boardUser = new BoardUser().boardId(UPDATED_BOARD_ID).userId(UPDATED_USER_ID).level(UPDATED_LEVEL).status(UPDATED_STATUS);
        return boardUser;
    }

    @BeforeEach
    public void initTest() {
        boardUser = createEntity(em);
    }

    @Test
    @Transactional
    void createBoardUser() throws Exception {
        int databaseSizeBeforeCreate = boardUserRepository.findAll().size();
        // Create the BoardUser
        restBoardUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardUser)))
            .andExpect(status().isCreated());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeCreate + 1);
        BoardUser testBoardUser = boardUserList.get(boardUserList.size() - 1);
        assertThat(testBoardUser.getBoardId()).isEqualTo(DEFAULT_BOARD_ID);
        assertThat(testBoardUser.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testBoardUser.getLevel()).isEqualTo(DEFAULT_LEVEL);
        assertThat(testBoardUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createBoardUserWithExistingId() throws Exception {
        // Create the BoardUser with an existing ID
        boardUser.setId(1L);

        int databaseSizeBeforeCreate = boardUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBoardUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardUser)))
            .andExpect(status().isBadRequest());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBoardUsers() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        // Get all the boardUserList
        restBoardUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(boardUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].boardId").value(hasItem(DEFAULT_BOARD_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    void getBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        // Get the boardUser
        restBoardUserMockMvc
            .perform(get(ENTITY_API_URL_ID, boardUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(boardUser.getId().intValue()))
            .andExpect(jsonPath("$.boardId").value(DEFAULT_BOARD_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingBoardUser() throws Exception {
        // Get the boardUser
        restBoardUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();

        // Update the boardUser
        BoardUser updatedBoardUser = boardUserRepository.findById(boardUser.getId()).get();
        // Disconnect from session so that the updates on updatedBoardUser are not directly saved in db
        em.detach(updatedBoardUser);
        updatedBoardUser.boardId(UPDATED_BOARD_ID).userId(UPDATED_USER_ID).level(UPDATED_LEVEL).status(UPDATED_STATUS);

        restBoardUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBoardUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBoardUser))
            )
            .andExpect(status().isOk());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
        BoardUser testBoardUser = boardUserList.get(boardUserList.size() - 1);
        assertThat(testBoardUser.getBoardId()).isEqualTo(UPDATED_BOARD_ID);
        assertThat(testBoardUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBoardUser.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testBoardUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();
        boardUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, boardUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();
        boardUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(boardUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();
        boardUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(boardUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBoardUserWithPatch() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();

        // Update the boardUser using partial update
        BoardUser partialUpdatedBoardUser = new BoardUser();
        partialUpdatedBoardUser.setId(boardUser.getId());

        partialUpdatedBoardUser.userId(UPDATED_USER_ID).level(UPDATED_LEVEL).status(UPDATED_STATUS);

        restBoardUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardUser))
            )
            .andExpect(status().isOk());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
        BoardUser testBoardUser = boardUserList.get(boardUserList.size() - 1);
        assertThat(testBoardUser.getBoardId()).isEqualTo(DEFAULT_BOARD_ID);
        assertThat(testBoardUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBoardUser.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testBoardUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateBoardUserWithPatch() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();

        // Update the boardUser using partial update
        BoardUser partialUpdatedBoardUser = new BoardUser();
        partialUpdatedBoardUser.setId(boardUser.getId());

        partialUpdatedBoardUser.boardId(UPDATED_BOARD_ID).userId(UPDATED_USER_ID).level(UPDATED_LEVEL).status(UPDATED_STATUS);

        restBoardUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBoardUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBoardUser))
            )
            .andExpect(status().isOk());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
        BoardUser testBoardUser = boardUserList.get(boardUserList.size() - 1);
        assertThat(testBoardUser.getBoardId()).isEqualTo(UPDATED_BOARD_ID);
        assertThat(testBoardUser.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testBoardUser.getLevel()).isEqualTo(UPDATED_LEVEL);
        assertThat(testBoardUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();
        boardUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBoardUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, boardUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();
        boardUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(boardUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBoardUser() throws Exception {
        int databaseSizeBeforeUpdate = boardUserRepository.findAll().size();
        boardUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBoardUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(boardUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BoardUser in the database
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBoardUser() throws Exception {
        // Initialize the database
        boardUserRepository.saveAndFlush(boardUser);

        int databaseSizeBeforeDelete = boardUserRepository.findAll().size();

        // Delete the boardUser
        restBoardUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, boardUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BoardUser> boardUserList = boardUserRepository.findAll();
        assertThat(boardUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
