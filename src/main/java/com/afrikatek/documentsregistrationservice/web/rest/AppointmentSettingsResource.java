package com.afrikatek.documentsregistrationservice.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.repository.AppointmentSettingsRepository;
import com.afrikatek.documentsregistrationservice.service.AppointmentSettingsService;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSettingsDTO;
import com.afrikatek.documentsregistrationservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.afrikatek.documentsregistrationservice.domain.AppointmentSettings}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentSettingsResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentSettingsResource.class);

    private static final String ENTITY_NAME = "appointmentSettings";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentSettingsService appointmentSettingsService;

    private final AppointmentSettingsRepository appointmentSettingsRepository;

    public AppointmentSettingsResource(
        AppointmentSettingsService appointmentSettingsService,
        AppointmentSettingsRepository appointmentSettingsRepository
    ) {
        this.appointmentSettingsService = appointmentSettingsService;
        this.appointmentSettingsRepository = appointmentSettingsRepository;
    }

    /**
     * {@code POST  /appointment-settings} : Create a new appointmentSettings.
     *
     * @param appointmentSettingsDTO the appointmentSettingsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentSettingsDTO, or with status {@code 400 (Bad Request)} if the appointmentSettings has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointment-settings")
    public ResponseEntity<AppointmentSettingsDTO> createAppointmentSettings(
        @Valid @RequestBody AppointmentSettingsDTO appointmentSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AppointmentSettings : {}", appointmentSettingsDTO);
        if (appointmentSettingsDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointmentSettings cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentSettingsDTO result = appointmentSettingsService.save(appointmentSettingsDTO);
        return ResponseEntity
            .created(new URI("/api/appointment-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appointment-settings/:id} : Updates an existing appointmentSettings.
     *
     * @param id the id of the appointmentSettingsDTO to save.
     * @param appointmentSettingsDTO the appointmentSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentSettingsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointment-settings/{id}")
    public ResponseEntity<AppointmentSettingsDTO> updateAppointmentSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppointmentSettingsDTO appointmentSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppointmentSettings : {}, {}", id, appointmentSettingsDTO);
        if (appointmentSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentSettingsDTO result = appointmentSettingsService.save(appointmentSettingsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appointmentSettingsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointment-settings/:id} : Partial updates given fields of an existing appointmentSettings, field will ignore if it is null
     *
     * @param id the id of the appointmentSettingsDTO to save.
     * @param appointmentSettingsDTO the appointmentSettingsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentSettingsDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentSettingsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentSettingsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentSettingsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointment-settings/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppointmentSettingsDTO> partialUpdateAppointmentSettings(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppointmentSettingsDTO appointmentSettingsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppointmentSettings partially : {}, {}", id, appointmentSettingsDTO);
        if (appointmentSettingsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentSettingsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentSettingsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentSettingsDTO> result = appointmentSettingsService.partialUpdate(appointmentSettingsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appointmentSettingsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appointment-settings} : get all the appointmentSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentSettings in body.
     */
    @GetMapping("/appointment-settings")
    public ResponseEntity<List<AppointmentSettingsDTO>> getAllAppointmentSettings(Pageable pageable) {
        log.debug("REST request to get a page of AppointmentSettings");
        Page<AppointmentSettingsDTO> page = appointmentSettingsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appointment-settings/:id} : get the "id" appointmentSettings.
     *
     * @param id the id of the appointmentSettingsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentSettingsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointment-settings/{id}")
    public ResponseEntity<AppointmentSettingsDTO> getAppointmentSettings(@PathVariable Long id) {
        log.debug("REST request to get AppointmentSettings : {}", id);
        Optional<AppointmentSettingsDTO> appointmentSettingsDTO = appointmentSettingsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentSettingsDTO);
    }

    /**
     * {@code DELETE  /appointment-settings/:id} : delete the "id" appointmentSettings.
     *
     * @param id the id of the appointmentSettingsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointment-settings/{id}")
    public ResponseEntity<Void> deleteAppointmentSettings(@PathVariable Long id) {
        log.debug("REST request to delete AppointmentSettings : {}", id);
        appointmentSettingsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/appointment-settings?query=:query} : search for the appointmentSettings corresponding
     * to the query.
     *
     * @param query the query of the appointmentSettings search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/appointment-settings")
    public ResponseEntity<List<AppointmentSettingsDTO>> searchAppointmentSettings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AppointmentSettings for query {}", query);
        Page<AppointmentSettingsDTO> page = appointmentSettingsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
