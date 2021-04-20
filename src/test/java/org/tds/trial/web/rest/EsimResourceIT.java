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
import org.tds.trial.domain.Device;
import org.tds.trial.domain.Esim;
import org.tds.trial.domain.EsimSubscription;
import org.tds.trial.domain.enumeration.EsimState;
import org.tds.trial.repository.EsimRepository;
import org.tds.trial.service.dto.EsimDTO;
import org.tds.trial.service.mapper.EsimMapper;

/**
 * Integration tests for the {@link EsimResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EsimResourceIT {

    private static final String DEFAULT_ICCID = "AAAAAAAAAA";
    private static final String UPDATED_ICCID = "BBBBBBBBBB";

    private static final String DEFAULT_IMSI = "AAAAAAAAAA";
    private static final String UPDATED_IMSI = "BBBBBBBBBB";

    private static final String DEFAULT_EID = "AAAAAAAAAA";
    private static final String UPDATED_EID = "BBBBBBBBBB";

    private static final EsimState DEFAULT_STATE = EsimState.RELEASED;
    private static final EsimState UPDATED_STATE = EsimState.INSTALLED;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/esims";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EsimRepository esimRepository;

    @Autowired
    private EsimMapper esimMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEsimMockMvc;

    private Esim esim;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Esim createEntity(EntityManager em) {
        Esim esim = new Esim().iccid(DEFAULT_ICCID).imsi(DEFAULT_IMSI).eid(DEFAULT_EID).state(DEFAULT_STATE).active(DEFAULT_ACTIVE);
        // Add required entity
        EsimSubscription esimSubscription;
        if (TestUtil.findAll(em, EsimSubscription.class).isEmpty()) {
            esimSubscription = EsimSubscriptionResourceIT.createEntity(em);
            em.persist(esimSubscription);
            em.flush();
        } else {
            esimSubscription = TestUtil.findAll(em, EsimSubscription.class).get(0);
        }
        esim.setSubscription(esimSubscription);
        // Add required entity
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            device = DeviceResourceIT.createEntity(em);
            em.persist(device);
            em.flush();
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        esim.setDevice(device);
        return esim;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Esim createUpdatedEntity(EntityManager em) {
        Esim esim = new Esim().iccid(UPDATED_ICCID).imsi(UPDATED_IMSI).eid(UPDATED_EID).state(UPDATED_STATE).active(UPDATED_ACTIVE);
        // Add required entity
        EsimSubscription esimSubscription;
        if (TestUtil.findAll(em, EsimSubscription.class).isEmpty()) {
            esimSubscription = EsimSubscriptionResourceIT.createUpdatedEntity(em);
            em.persist(esimSubscription);
            em.flush();
        } else {
            esimSubscription = TestUtil.findAll(em, EsimSubscription.class).get(0);
        }
        esim.setSubscription(esimSubscription);
        // Add required entity
        Device device;
        if (TestUtil.findAll(em, Device.class).isEmpty()) {
            device = DeviceResourceIT.createUpdatedEntity(em);
            em.persist(device);
            em.flush();
        } else {
            device = TestUtil.findAll(em, Device.class).get(0);
        }
        esim.setDevice(device);
        return esim;
    }

    @BeforeEach
    public void initTest() {
        esim = createEntity(em);
    }

    @Test
    @Transactional
    void createEsim() throws Exception {
        int databaseSizeBeforeCreate = esimRepository.findAll().size();
        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);
        restEsimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isCreated());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeCreate + 1);
        Esim testEsim = esimList.get(esimList.size() - 1);
        assertThat(testEsim.getIccid()).isEqualTo(DEFAULT_ICCID);
        assertThat(testEsim.getImsi()).isEqualTo(DEFAULT_IMSI);
        assertThat(testEsim.getEid()).isEqualTo(DEFAULT_EID);
        assertThat(testEsim.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testEsim.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createEsimWithExistingId() throws Exception {
        // Create the Esim with an existing ID
        esim.setId(1L);
        EsimDTO esimDTO = esimMapper.toDto(esim);

        int databaseSizeBeforeCreate = esimRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEsimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIccidIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimRepository.findAll().size();
        // set the field null
        esim.setIccid(null);

        // Create the Esim, which fails.
        EsimDTO esimDTO = esimMapper.toDto(esim);

        restEsimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isBadRequest());

        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkImsiIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimRepository.findAll().size();
        // set the field null
        esim.setImsi(null);

        // Create the Esim, which fails.
        EsimDTO esimDTO = esimMapper.toDto(esim);

        restEsimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isBadRequest());

        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimRepository.findAll().size();
        // set the field null
        esim.setState(null);

        // Create the Esim, which fails.
        EsimDTO esimDTO = esimMapper.toDto(esim);

        restEsimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isBadRequest());

        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = esimRepository.findAll().size();
        // set the field null
        esim.setActive(null);

        // Create the Esim, which fails.
        EsimDTO esimDTO = esimMapper.toDto(esim);

        restEsimMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isBadRequest());

        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEsims() throws Exception {
        // Initialize the database
        esimRepository.saveAndFlush(esim);

        // Get all the esimList
        restEsimMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(esim.getId().intValue())))
            .andExpect(jsonPath("$.[*].iccid").value(hasItem(DEFAULT_ICCID)))
            .andExpect(jsonPath("$.[*].imsi").value(hasItem(DEFAULT_IMSI)))
            .andExpect(jsonPath("$.[*].eid").value(hasItem(DEFAULT_EID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getEsim() throws Exception {
        // Initialize the database
        esimRepository.saveAndFlush(esim);

        // Get the esim
        restEsimMockMvc
            .perform(get(ENTITY_API_URL_ID, esim.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(esim.getId().intValue()))
            .andExpect(jsonPath("$.iccid").value(DEFAULT_ICCID))
            .andExpect(jsonPath("$.imsi").value(DEFAULT_IMSI))
            .andExpect(jsonPath("$.eid").value(DEFAULT_EID))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingEsim() throws Exception {
        // Get the esim
        restEsimMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEsim() throws Exception {
        // Initialize the database
        esimRepository.saveAndFlush(esim);

        int databaseSizeBeforeUpdate = esimRepository.findAll().size();

        // Update the esim
        Esim updatedEsim = esimRepository.findById(esim.getId()).get();
        // Disconnect from session so that the updates on updatedEsim are not directly saved in db
        em.detach(updatedEsim);
        updatedEsim.iccid(UPDATED_ICCID).imsi(UPDATED_IMSI).eid(UPDATED_EID).state(UPDATED_STATE).active(UPDATED_ACTIVE);
        EsimDTO esimDTO = esimMapper.toDto(updatedEsim);

        restEsimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(esimDTO))
            )
            .andExpect(status().isOk());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
        Esim testEsim = esimList.get(esimList.size() - 1);
        assertThat(testEsim.getIccid()).isEqualTo(UPDATED_ICCID);
        assertThat(testEsim.getImsi()).isEqualTo(UPDATED_IMSI);
        assertThat(testEsim.getEid()).isEqualTo(UPDATED_EID);
        assertThat(testEsim.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEsim.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingEsim() throws Exception {
        int databaseSizeBeforeUpdate = esimRepository.findAll().size();
        esim.setId(count.incrementAndGet());

        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, esimDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(esimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEsim() throws Exception {
        int databaseSizeBeforeUpdate = esimRepository.findAll().size();
        esim.setId(count.incrementAndGet());

        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(esimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEsim() throws Exception {
        int databaseSizeBeforeUpdate = esimRepository.findAll().size();
        esim.setId(count.incrementAndGet());

        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEsimWithPatch() throws Exception {
        // Initialize the database
        esimRepository.saveAndFlush(esim);

        int databaseSizeBeforeUpdate = esimRepository.findAll().size();

        // Update the esim using partial update
        Esim partialUpdatedEsim = new Esim();
        partialUpdatedEsim.setId(esim.getId());

        partialUpdatedEsim.iccid(UPDATED_ICCID).imsi(UPDATED_IMSI).eid(UPDATED_EID).active(UPDATED_ACTIVE);

        restEsimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEsim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEsim))
            )
            .andExpect(status().isOk());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
        Esim testEsim = esimList.get(esimList.size() - 1);
        assertThat(testEsim.getIccid()).isEqualTo(UPDATED_ICCID);
        assertThat(testEsim.getImsi()).isEqualTo(UPDATED_IMSI);
        assertThat(testEsim.getEid()).isEqualTo(UPDATED_EID);
        assertThat(testEsim.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testEsim.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateEsimWithPatch() throws Exception {
        // Initialize the database
        esimRepository.saveAndFlush(esim);

        int databaseSizeBeforeUpdate = esimRepository.findAll().size();

        // Update the esim using partial update
        Esim partialUpdatedEsim = new Esim();
        partialUpdatedEsim.setId(esim.getId());

        partialUpdatedEsim.iccid(UPDATED_ICCID).imsi(UPDATED_IMSI).eid(UPDATED_EID).state(UPDATED_STATE).active(UPDATED_ACTIVE);

        restEsimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEsim.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEsim))
            )
            .andExpect(status().isOk());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
        Esim testEsim = esimList.get(esimList.size() - 1);
        assertThat(testEsim.getIccid()).isEqualTo(UPDATED_ICCID);
        assertThat(testEsim.getImsi()).isEqualTo(UPDATED_IMSI);
        assertThat(testEsim.getEid()).isEqualTo(UPDATED_EID);
        assertThat(testEsim.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testEsim.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingEsim() throws Exception {
        int databaseSizeBeforeUpdate = esimRepository.findAll().size();
        esim.setId(count.incrementAndGet());

        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, esimDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(esimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEsim() throws Exception {
        int databaseSizeBeforeUpdate = esimRepository.findAll().size();
        esim.setId(count.incrementAndGet());

        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(esimDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEsim() throws Exception {
        int databaseSizeBeforeUpdate = esimRepository.findAll().size();
        esim.setId(count.incrementAndGet());

        // Create the Esim
        EsimDTO esimDTO = esimMapper.toDto(esim);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEsimMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(esimDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Esim in the database
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEsim() throws Exception {
        // Initialize the database
        esimRepository.saveAndFlush(esim);

        int databaseSizeBeforeDelete = esimRepository.findAll().size();

        // Delete the esim
        restEsimMockMvc
            .perform(delete(ENTITY_API_URL_ID, esim.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Esim> esimList = esimRepository.findAll();
        assertThat(esimList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
