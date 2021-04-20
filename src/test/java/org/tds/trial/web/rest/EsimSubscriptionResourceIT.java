package org.tds.trial.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.tds.trial.IntegrationTest;
import org.tds.trial.domain.Esim;
import org.tds.trial.domain.EsimSubscription;
import org.tds.trial.repository.EsimSubscriptionRepository;
import org.tds.trial.service.dto.EsimSubscriptionDTO;
import org.tds.trial.service.mapper.EsimSubscriptionMapper;

/**
 * Integration tests for the {@link EsimSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EsimSubscriptionResourceIT {

    private static final String DEFAULT_INSTALL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_INSTALL_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIVATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ACTIVATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIRMATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIRMATION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ENCODED_ACTIVATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_ENCODED_ACTIVATION_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/esim-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EsimSubscriptionRepository esimSubscriptionRepository;

    @Autowired
    private EsimSubscriptionMapper esimSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEsimSubscriptionMockMvc;

    private EsimSubscription esimSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EsimSubscription createEntity(EntityManager em) {
        EsimSubscription esimSubscription = new EsimSubscription()
            .installAddress(DEFAULT_INSTALL_ADDRESS)
            .activationCode(DEFAULT_ACTIVATION_CODE)
            .confirmationCode(DEFAULT_CONFIRMATION_CODE)
            .encodedActivationCode(DEFAULT_ENCODED_ACTIVATION_CODE);
        // Add required entity
        Esim esim;
        if (TestUtil.findAll(em, Esim.class).isEmpty()) {
            esim = EsimResourceIT.createEntity(em);
            em.persist(esim);
            em.flush();
        } else {
            esim = TestUtil.findAll(em, Esim.class).get(0);
        }
        esimSubscription.setEsim(esim);
        return esimSubscription;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EsimSubscription createUpdatedEntity(EntityManager em) {
        EsimSubscription esimSubscription = new EsimSubscription()
            .installAddress(UPDATED_INSTALL_ADDRESS)
            .activationCode(UPDATED_ACTIVATION_CODE)
            .confirmationCode(UPDATED_CONFIRMATION_CODE)
            .encodedActivationCode(UPDATED_ENCODED_ACTIVATION_CODE);
        // Add required entity
        Esim esim;
        if (TestUtil.findAll(em, Esim.class).isEmpty()) {
            esim = EsimResourceIT.createUpdatedEntity(em);
            em.persist(esim);
            em.flush();
        } else {
            esim = TestUtil.findAll(em, Esim.class).get(0);
        }
        esimSubscription.setEsim(esim);
        return esimSubscription;
    }

    @BeforeEach
    public void initTest() {
        esimSubscription = createEntity(em);
    }

    @Test
    @Transactional
    void createEsimSubscription() throws Exception {
        int databaseSizeBeforeCreate = esimSubscriptionRepository.findAll().size();
        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);
        restEsimSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeCreate + 1);
        EsimSubscription testEsimSubscription = esimSubscriptionList.get(esimSubscriptionList.size() - 1);
        assertThat(testEsimSubscription.getInstallAddress()).isEqualTo(DEFAULT_INSTALL_ADDRESS);
        assertThat(testEsimSubscription.getActivationCode()).isEqualTo(DEFAULT_ACTIVATION_CODE);
        assertThat(testEsimSubscription.getConfirmationCode()).isEqualTo(DEFAULT_CONFIRMATION_CODE);
        assertThat(testEsimSubscription.getEncodedActivationCode()).isEqualTo(DEFAULT_ENCODED_ACTIVATION_CODE);
    }

    @Test
    @Transactional
    void createEsimSubscriptionWithExistingId() throws Exception {
        // Create the EsimSubscription with an existing ID
        esimSubscription.setId(1L);
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        int databaseSizeBeforeCreate = esimSubscriptionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEsimSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInstallAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimSubscriptionRepository.findAll().size();
        // set the field null
        esimSubscription.setInstallAddress(null);

        // Create the EsimSubscription, which fails.
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        restEsimSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimSubscriptionRepository.findAll().size();
        // set the field null
        esimSubscription.setActivationCode(null);

        // Create the EsimSubscription, which fails.
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        restEsimSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEncodedActivationCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimSubscriptionRepository.findAll().size();
        // set the field null
        esimSubscription.setEncodedActivationCode(null);

        // Create the EsimSubscription, which fails.
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        restEsimSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEsimSubscriptions() throws Exception {
        // Initialize the database
        esimSubscriptionRepository.saveAndFlush(esimSubscription);

        // Get all the esimSubscriptionList
        restEsimSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(esimSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].installAddress").value(hasItem(DEFAULT_INSTALL_ADDRESS)))
            .andExpect(jsonPath("$.[*].activationCode").value(hasItem(DEFAULT_ACTIVATION_CODE)))
            .andExpect(jsonPath("$.[*].confirmationCode").value(hasItem(DEFAULT_CONFIRMATION_CODE)))
            .andExpect(jsonPath("$.[*].encodedActivationCode").value(hasItem(DEFAULT_ENCODED_ACTIVATION_CODE)));
    }

    @Test
    @Transactional
    void getEsimSubscription() throws Exception {
        // Initialize the database
        esimSubscriptionRepository.saveAndFlush(esimSubscription);

        // Get the esimSubscription
        restEsimSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, esimSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(esimSubscription.getId().intValue()))
            .andExpect(jsonPath("$.installAddress").value(DEFAULT_INSTALL_ADDRESS))
            .andExpect(jsonPath("$.activationCode").value(DEFAULT_ACTIVATION_CODE))
            .andExpect(jsonPath("$.confirmationCode").value(DEFAULT_CONFIRMATION_CODE))
            .andExpect(jsonPath("$.encodedActivationCode").value(DEFAULT_ENCODED_ACTIVATION_CODE));
    }

    @Test
    @Transactional
    void getNonExistingEsimSubscription() throws Exception {
        // Get the esimSubscription
        restEsimSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEsimSubscription() throws Exception {
        // Initialize the database
        esimSubscriptionRepository.saveAndFlush(esimSubscription);

        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();

        // Update the esimSubscription
        EsimSubscription updatedEsimSubscription = esimSubscriptionRepository.findById(esimSubscription.getId()).get();
        // Disconnect from session so that the updates on updatedEsimSubscription are not directly saved in db
        em.detach(updatedEsimSubscription);
        updatedEsimSubscription
            .installAddress(UPDATED_INSTALL_ADDRESS)
            .activationCode(UPDATED_ACTIVATION_CODE)
            .confirmationCode(UPDATED_CONFIRMATION_CODE)
            .encodedActivationCode(UPDATED_ENCODED_ACTIVATION_CODE);
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(updatedEsimSubscription);

        restEsimSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esimSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        EsimSubscription testEsimSubscription = esimSubscriptionList.get(esimSubscriptionList.size() - 1);
        assertThat(testEsimSubscription.getInstallAddress()).isEqualTo(UPDATED_INSTALL_ADDRESS);
        assertThat(testEsimSubscription.getActivationCode()).isEqualTo(UPDATED_ACTIVATION_CODE);
        assertThat(testEsimSubscription.getConfirmationCode()).isEqualTo(UPDATED_CONFIRMATION_CODE);
        assertThat(testEsimSubscription.getEncodedActivationCode()).isEqualTo(UPDATED_ENCODED_ACTIVATION_CODE);
    }

    @Test
    @Transactional
    void putNonExistingEsimSubscription() throws Exception {
        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();
        esimSubscription.setId(count.incrementAndGet());

        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsimSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esimSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEsimSubscription() throws Exception {
        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();
        esimSubscription.setId(count.incrementAndGet());

        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEsimSubscription() throws Exception {
        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();
        esimSubscription.setId(count.incrementAndGet());

        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEsimSubscriptionWithPatch() throws Exception {
        // Initialize the database
        esimSubscriptionRepository.saveAndFlush(esimSubscription);

        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();

        // Update the esimSubscription using partial update
        EsimSubscription partialUpdatedEsimSubscription = new EsimSubscription();
        partialUpdatedEsimSubscription.setId(esimSubscription.getId());

        partialUpdatedEsimSubscription.installAddress(UPDATED_INSTALL_ADDRESS);

        restEsimSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEsimSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEsimSubscription))
            )
            .andExpect(status().isOk());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        EsimSubscription testEsimSubscription = esimSubscriptionList.get(esimSubscriptionList.size() - 1);
        assertThat(testEsimSubscription.getInstallAddress()).isEqualTo(UPDATED_INSTALL_ADDRESS);
        assertThat(testEsimSubscription.getActivationCode()).isEqualTo(DEFAULT_ACTIVATION_CODE);
        assertThat(testEsimSubscription.getConfirmationCode()).isEqualTo(DEFAULT_CONFIRMATION_CODE);
        assertThat(testEsimSubscription.getEncodedActivationCode()).isEqualTo(DEFAULT_ENCODED_ACTIVATION_CODE);
    }

    @Test
    @Transactional
    void fullUpdateEsimSubscriptionWithPatch() throws Exception {
        // Initialize the database
        esimSubscriptionRepository.saveAndFlush(esimSubscription);

        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();

        // Update the esimSubscription using partial update
        EsimSubscription partialUpdatedEsimSubscription = new EsimSubscription();
        partialUpdatedEsimSubscription.setId(esimSubscription.getId());

        partialUpdatedEsimSubscription
            .installAddress(UPDATED_INSTALL_ADDRESS)
            .activationCode(UPDATED_ACTIVATION_CODE)
            .confirmationCode(UPDATED_CONFIRMATION_CODE)
            .encodedActivationCode(UPDATED_ENCODED_ACTIVATION_CODE);

        restEsimSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEsimSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEsimSubscription))
            )
            .andExpect(status().isOk());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
        EsimSubscription testEsimSubscription = esimSubscriptionList.get(esimSubscriptionList.size() - 1);
        assertThat(testEsimSubscription.getInstallAddress()).isEqualTo(UPDATED_INSTALL_ADDRESS);
        assertThat(testEsimSubscription.getActivationCode()).isEqualTo(UPDATED_ACTIVATION_CODE);
        assertThat(testEsimSubscription.getConfirmationCode()).isEqualTo(UPDATED_CONFIRMATION_CODE);
        assertThat(testEsimSubscription.getEncodedActivationCode()).isEqualTo(UPDATED_ENCODED_ACTIVATION_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingEsimSubscription() throws Exception {
        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();
        esimSubscription.setId(count.incrementAndGet());

        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsimSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, esimSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEsimSubscription() throws Exception {
        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();
        esimSubscription.setId(count.incrementAndGet());

        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEsimSubscription() throws Exception {
        int databaseSizeBeforeUpdate = esimSubscriptionRepository.findAll().size();
        esimSubscription.setId(count.incrementAndGet());

        // Create the EsimSubscription
        EsimSubscriptionDTO esimSubscriptionDTO = esimSubscriptionMapper.toDto(esimSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(esimSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EsimSubscription in the database
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEsimSubscription() throws Exception {
        // Initialize the database
        esimSubscriptionRepository.saveAndFlush(esimSubscription);

        int databaseSizeBeforeDelete = esimSubscriptionRepository.findAll().size();

        // Delete the esimSubscription
        restEsimSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, esimSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EsimSubscription> esimSubscriptionList = esimSubscriptionRepository.findAll();
        assertThat(esimSubscriptionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
