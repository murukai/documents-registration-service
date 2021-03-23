package com.afrikatek.documentsregistrationservice.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.repository.MarriageDetailsRepository;
import com.afrikatek.documentsregistrationservice.service.ApplicantService;
import com.afrikatek.documentsregistrationservice.service.MarriageDetailsService;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.dto.MarriageDetailsDTO;
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
 * REST controller for managing {@link com.afrikatek.documentsregistrationservice.domain.MarriageDetails}.
 */
@RestController
@RequestMapping("/api")
public class MarriageDetailsResource {

    private final Logger log = LoggerFactory.getLogger(MarriageDetailsResource.class);

    private static final String ENTITY_NAME = "marriageDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarriageDetailsService marriageDetailsService;

    private final ApplicantService applicantService;

    private final MarriageDetailsRepository marriageDetailsRepository;

    public MarriageDetailsResource(
        MarriageDetailsService marriageDetailsService,
        ApplicantService applicantService,
        MarriageDetailsRepository marriageDetailsRepository
    ) {
        this.marriageDetailsService = marriageDetailsService;
        this.applicantService = applicantService;
        this.marriageDetailsRepository = marriageDetailsRepository;
    }

    /**
     * {@code POST  /marriage-details} : Create a new marriageDetails.
     *
     * @param marriageDetailsDTO the marriageDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marriageDetailsDTO, or with status {@code 400 (Bad Request)} if the marriageDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marriage-details")
    public ResponseEntity<MarriageDetailsDTO> createMarriageDetails(@Valid @RequestBody MarriageDetailsDTO marriageDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save MarriageDetails : {}", marriageDetailsDTO);
        if (marriageDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new marriageDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarriageDetailsDTO result = marriageDetailsService.save(marriageDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/marriage-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /marriage-details/:id} : Updates an existing marriageDetails.
     *
     * @param id the id of the marriageDetailsDTO to save.
     * @param marriageDetailsDTO the marriageDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marriageDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the marriageDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marriageDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marriage-details/{id}")
    public ResponseEntity<MarriageDetailsDTO> updateMarriageDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MarriageDetailsDTO marriageDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MarriageDetails : {}, {}", id, marriageDetailsDTO);
        if (marriageDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marriageDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marriageDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MarriageDetailsDTO result = marriageDetailsService.save(marriageDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marriageDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /marriage-details/:id} : Partial updates given fields of an existing marriageDetails, field will ignore if it is null
     *
     * @param id the id of the marriageDetailsDTO to save.
     * @param marriageDetailsDTO the marriageDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marriageDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the marriageDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the marriageDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the marriageDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marriage-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MarriageDetailsDTO> partialUpdateMarriageDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MarriageDetailsDTO marriageDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MarriageDetails partially : {}, {}", id, marriageDetailsDTO);
        if (marriageDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marriageDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marriageDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MarriageDetailsDTO> result = marriageDetailsService.partialUpdate(marriageDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marriageDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /marriage-details} : get all the marriageDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marriageDetails in body.
     */
    @GetMapping("/marriage-details")
    public ResponseEntity<List<MarriageDetailsDTO>> getAllMarriageDetails(Pageable pageable) {
        log.debug("REST request to get a page of MarriageDetails");
        Page<MarriageDetailsDTO> page = marriageDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /marriage-details/:id} : get the "id" marriageDetails.
     *
     * @param id the id of the marriageDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marriageDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marriage-details/{id}")
    public ResponseEntity<MarriageDetailsDTO> getMarriageDetails(@PathVariable Long id) {
        log.debug("REST request to get MarriageDetails : {}", id);
        Optional<MarriageDetailsDTO> marriageDetailsDTO = marriageDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marriageDetailsDTO);
    }

    @GetMapping("/marriage-details/{applicantId}/applicant")
    public ResponseEntity<MarriageDetailsDTO> getMarriageDetailsByApplicant(@PathVariable Long applicantId) {
        log.debug("REST request to get MarriageDetails for an applicant : {}", applicantId);
        ApplicantDTO applicantDTO = applicantService.findOne(applicantId).orElseThrow(EntityNotFoundException::new);
        return ResponseEntity.ok().body(marriageDetailsService.findByApplicant(applicantDTO).get());
    }

    /**
     * {@code DELETE  /marriage-details/:id} : delete the "id" marriageDetails.
     *
     * @param id the id of the marriageDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marriage-details/{id}")
    public ResponseEntity<Void> deleteMarriageDetails(@PathVariable Long id) {
        log.debug("REST request to delete MarriageDetails : {}", id);
        marriageDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/marriage-details?query=:query} : search for the marriageDetails corresponding
     * to the query.
     *
     * @param query the query of the marriageDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/marriage-details")
    public ResponseEntity<List<MarriageDetailsDTO>> searchMarriageDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MarriageDetails for query {}", query);
        Page<MarriageDetailsDTO> page = marriageDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
