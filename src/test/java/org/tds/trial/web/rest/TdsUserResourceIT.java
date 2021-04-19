package org.tds.trial.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.tds.trial.IntegrationTest;
import org.tds.trial.domain.TdsUser;
import org.tds.trial.repository.TdsUserRepository;
import org.tds.trial.service.dto.TdsUserDTO;
import org.tds.trial.service.mapper.TdsUserMapper;

/**
 * Integration tests for the {@link TdsUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TdsUserResourceIT {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tds-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private TdsUserRepository tdsUserRepository;

    @Autowired
    private TdsUserMapper tdsUserMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTdsUserMockMvc;

    private TdsUser tdsUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TdsUser createEntity(EntityManager em) {
        TdsUser tdsUser = new TdsUser()
            .firstname(DEFAULT_FIRSTNAME)
            .surname(DEFAULT_SURNAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD);
        return tdsUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TdsUser createUpdatedEntity(EntityManager em) {
        TdsUser tdsUser = new TdsUser()
            .firstname(UPDATED_FIRSTNAME)
            .surname(UPDATED_SURNAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        return tdsUser;
    }

    @BeforeEach
    public void initTest() {
        tdsUser = createEntity(em);
    }

    @Test
    @Transactional
    void createTdsUser() throws Exception {
        int databaseSizeBeforeCreate = tdsUserRepository.findAll().size();
        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);
        restTdsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isCreated());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeCreate + 1);
        TdsUser testTdsUser = tdsUserList.get(tdsUserList.size() - 1);
        assertThat(testTdsUser.getFirstname()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(testTdsUser.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testTdsUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTdsUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createTdsUserWithExistingId() throws Exception {
        // Create the TdsUser with an existing ID
        tdsUser.setId("existing_id");
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        int databaseSizeBeforeCreate = tdsUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTdsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tdsUserRepository.findAll().size();
        // set the field null
        tdsUser.setFirstname(null);

        // Create the TdsUser, which fails.
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        restTdsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isBadRequest());

        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tdsUserRepository.findAll().size();
        // set the field null
        tdsUser.setSurname(null);

        // Create the TdsUser, which fails.
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        restTdsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isBadRequest());

        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = tdsUserRepository.findAll().size();
        // set the field null
        tdsUser.setEmail(null);

        // Create the TdsUser, which fails.
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        restTdsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isBadRequest());

        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = tdsUserRepository.findAll().size();
        // set the field null
        tdsUser.setPassword(null);

        // Create the TdsUser, which fails.
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        restTdsUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isBadRequest());

        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTdsUsers() throws Exception {
        // Initialize the database
        tdsUserRepository.saveAndFlush(tdsUser);

        // Get all the tdsUserList
        restTdsUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tdsUser.getId())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getTdsUser() throws Exception {
        // Initialize the database
        tdsUserRepository.saveAndFlush(tdsUser);

        // Get the tdsUser
        restTdsUserMockMvc
            .perform(get(ENTITY_API_URL_ID, tdsUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tdsUser.getId()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getNonExistingTdsUser() throws Exception {
        // Get the tdsUser
        restTdsUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTdsUser() throws Exception {
        // Initialize the database
        tdsUserRepository.saveAndFlush(tdsUser);

        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();

        // Update the tdsUser
        TdsUser updatedTdsUser = tdsUserRepository.findById(tdsUser.getId()).get();
        // Disconnect from session so that the updates on updatedTdsUser are not directly saved in db
        em.detach(updatedTdsUser);
        updatedTdsUser.firstname(UPDATED_FIRSTNAME).surname(UPDATED_SURNAME).email(UPDATED_EMAIL).password(UPDATED_PASSWORD);
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(updatedTdsUser);

        restTdsUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tdsUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tdsUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
        TdsUser testTdsUser = tdsUserList.get(tdsUserList.size() - 1);
        assertThat(testTdsUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTdsUser.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testTdsUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTdsUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingTdsUser() throws Exception {
        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();
        tdsUser.setId(UUID.randomUUID().toString());

        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTdsUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tdsUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tdsUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTdsUser() throws Exception {
        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();
        tdsUser.setId(UUID.randomUUID().toString());

        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTdsUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tdsUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTdsUser() throws Exception {
        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();
        tdsUser.setId(UUID.randomUUID().toString());

        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTdsUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tdsUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTdsUserWithPatch() throws Exception {
        // Initialize the database
        tdsUserRepository.saveAndFlush(tdsUser);

        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();

        // Update the tdsUser using partial update
        TdsUser partialUpdatedTdsUser = new TdsUser();
        partialUpdatedTdsUser.setId(tdsUser.getId());

        partialUpdatedTdsUser.firstname(UPDATED_FIRSTNAME).surname(UPDATED_SURNAME).password(UPDATED_PASSWORD);

        restTdsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTdsUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTdsUser))
            )
            .andExpect(status().isOk());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
        TdsUser testTdsUser = tdsUserList.get(tdsUserList.size() - 1);
        assertThat(testTdsUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTdsUser.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testTdsUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testTdsUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateTdsUserWithPatch() throws Exception {
        // Initialize the database
        tdsUserRepository.saveAndFlush(tdsUser);

        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();

        // Update the tdsUser using partial update
        TdsUser partialUpdatedTdsUser = new TdsUser();
        partialUpdatedTdsUser.setId(tdsUser.getId());

        partialUpdatedTdsUser.firstname(UPDATED_FIRSTNAME).surname(UPDATED_SURNAME).email(UPDATED_EMAIL).password(UPDATED_PASSWORD);

        restTdsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTdsUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTdsUser))
            )
            .andExpect(status().isOk());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
        TdsUser testTdsUser = tdsUserList.get(tdsUserList.size() - 1);
        assertThat(testTdsUser.getFirstname()).isEqualTo(UPDATED_FIRSTNAME);
        assertThat(testTdsUser.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testTdsUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testTdsUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingTdsUser() throws Exception {
        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();
        tdsUser.setId(UUID.randomUUID().toString());

        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTdsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tdsUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tdsUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTdsUser() throws Exception {
        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();
        tdsUser.setId(UUID.randomUUID().toString());

        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTdsUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tdsUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTdsUser() throws Exception {
        int databaseSizeBeforeUpdate = tdsUserRepository.findAll().size();
        tdsUser.setId(UUID.randomUUID().toString());

        // Create the TdsUser
        TdsUserDTO tdsUserDTO = tdsUserMapper.toDto(tdsUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTdsUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(tdsUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TdsUser in the database
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTdsUser() throws Exception {
        // Initialize the database
        tdsUserRepository.saveAndFlush(tdsUser);

        int databaseSizeBeforeDelete = tdsUserRepository.findAll().size();

        // Delete the tdsUser
        restTdsUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, tdsUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TdsUser> tdsUserList = tdsUserRepository.findAll();
        assertThat(tdsUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
