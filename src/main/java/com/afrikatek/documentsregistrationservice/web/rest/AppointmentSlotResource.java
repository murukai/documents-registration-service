package com.afrikatek.documentsregistrationservice.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.Appointment;
import com.afrikatek.documentsregistrationservice.repository.AppointmentSlotRepository;
import com.afrikatek.documentsregistrationservice.service.AppointmentService;
import com.afrikatek.documentsregistrationservice.service.AppointmentSlotService;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentDTO;
import com.afrikatek.documentsregistrationservice.service.dto.AppointmentSlotDTO;
import com.afrikatek.documentsregistrationservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.persistence.EntityNotFoundException;
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
 * REST controller for managing {@link com.afrikatek.documentsregistrationservice.domain.AppointmentSlot}.
 */
@RestController
@RequestMapping("/api")
public class AppointmentSlotResource {

    private final Logger log = LoggerFactory.getLogger(AppointmentSlotResource.class);

    private static final String ENTITY_NAME = "appointmentSlot";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointmentSlotService appointmentSlotService;

    private final AppointmentService appointmentService;

    private final AppointmentSlotRepository appointmentSlotRepository;

    public AppointmentSlotResource(
        AppointmentSlotService appointmentSlotService,
        AppointmentService appointmentService,
        AppointmentSlotRepository appointmentSlotRepository
    ) {
        this.appointmentSlotService = appointmentSlotService;
        this.appointmentService = appointmentService;
        this.appointmentSlotRepository = appointmentSlotRepository;
    }

    /**
     * {@code POST  /appointment-slots} : Create a new appointmentSlot.
     *
     * @param appointmentSlotDTO the appointmentSlotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointmentSlotDTO, or with status {@code 400 (Bad Request)} if the appointmentSlot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointment-slots")
    public ResponseEntity<AppointmentSlotDTO> createAppointmentSlot(@Valid @RequestBody AppointmentSlotDTO appointmentSlotDTO)
        throws URISyntaxException {
        log.debug("REST request to save AppointmentSlot : {}", appointmentSlotDTO);
        if (appointmentSlotDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointmentSlot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointmentSlotDTO result = appointmentSlotService.save(appointmentSlotDTO);
        return ResponseEntity
            .created(new URI("/api/appointment-slots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appointment-slots/:id} : Updates an existing appointmentSlot.
     *
     * @param id the id of the appointmentSlotDTO to save.
     * @param appointmentSlotDTO the appointmentSlotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentSlotDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentSlotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointmentSlotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointment-slots/{id}")
    public ResponseEntity<AppointmentSlotDTO> updateAppointmentSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppointmentSlotDTO appointmentSlotDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AppointmentSlot : {}, {}", id, appointmentSlotDTO);
        if (appointmentSlotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentSlotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentSlotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointmentSlotDTO result = appointmentSlotService.save(appointmentSlotDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appointmentSlotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointment-slots/:id} : Partial updates given fields of an existing appointmentSlot, field will ignore if it is null
     *
     * @param id the id of the appointmentSlotDTO to save.
     * @param appointmentSlotDTO the appointmentSlotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointmentSlotDTO,
     * or with status {@code 400 (Bad Request)} if the appointmentSlotDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appointmentSlotDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointmentSlotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointment-slots/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AppointmentSlotDTO> partialUpdateAppointmentSlot(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppointmentSlotDTO appointmentSlotDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppointmentSlot partially : {}, {}", id, appointmentSlotDTO);
        if (appointmentSlotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointmentSlotDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointmentSlotRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointmentSlotDTO> result = appointmentSlotService.partialUpdate(appointmentSlotDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appointmentSlotDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appointment-slots} : get all the appointmentSlots.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointmentSlots in body.
     */
    @GetMapping("/appointment-slots")
    public ResponseEntity<List<AppointmentSlotDTO>> getAllAppointmentSlots(Pageable pageable) {
        log.debug("REST request to get a page of AppointmentSlots");
        Page<AppointmentSlotDTO> page = appointmentSlotService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appointment-slots/:id} : get the "id" appointmentSlot.
     *
     * @param id the id of the appointmentSlotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointmentSlotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointment-slots/{id}")
    public ResponseEntity<AppointmentSlotDTO> getAppointmentSlot(@PathVariable Long id) {
        log.debug("REST request to get AppointmentSlot : {}", id);
        Optional<AppointmentSlotDTO> appointmentSlotDTO = appointmentSlotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointmentSlotDTO);
    }

    @GetMapping("/appointments-slots/{appointmentId}/appointment")
    public ResponseEntity<List<AppointmentSlotDTO>> findAllByAppointment(@PathVariable Long appointmentId) {
        log.debug("REST request to get AppointmentSlot for appointment with id : {}", appointmentId);
        AppointmentDTO appointmentDTO = appointmentService.findOne(appointmentId).orElseThrow(EntityNotFoundException::new);
        List<AppointmentSlotDTO> appointmentSlotDTOList = appointmentSlotService.findByAppointment(appointmentDTO).get();
        return ResponseEntity.ok().body(appointmentSlotDTOList);
    }

    /**
     * {@code DELETE  /appointment-slots/:id} : delete the "id" appointmentSlot.
     *
     * @param id the id of the appointmentSlotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointment-slots/{id}")
    public ResponseEntity<Void> deleteAppointmentSlot(@PathVariable Long id) {
        log.debug("REST request to delete AppointmentSlot : {}", id);
        appointmentSlotService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/appointment-slots?query=:query} : search for the appointmentSlot corresponding
     * to the query.
     *
     * @param query the query of the appointmentSlot search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/appointment-slots")
    public ResponseEntity<List<AppointmentSlotDTO>> searchAppointmentSlots(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AppointmentSlots for query {}", query);
        Page<AppointmentSlotDTO> page = appointmentSlotService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
