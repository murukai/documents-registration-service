package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.Holiday;
import com.afrikatek.documentsregistrationservice.repository.HolidayRepository;
import com.afrikatek.documentsregistrationservice.repository.search.HolidaySearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.HolidayDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.HolidayMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Holiday}.
 */
@Service
@Transactional
public class HolidayService {

    private final Logger log = LoggerFactory.getLogger(HolidayService.class);

    private final HolidayRepository holidayRepository;

    private final HolidayMapper holidayMapper;

    private final HolidaySearchRepository holidaySearchRepository;

    public HolidayService(
        HolidayRepository holidayRepository,
        HolidayMapper holidayMapper,
        HolidaySearchRepository holidaySearchRepository
    ) {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
        this.holidaySearchRepository = holidaySearchRepository;
    }

    /**
     * Save a holiday.
     *
     * @param holidayDTO the entity to save.
     * @return the persisted entity.
     */
    public HolidayDTO save(HolidayDTO holidayDTO) {
        log.debug("Request to save Holiday : {}", holidayDTO);
        Holiday holiday = holidayMapper.toEntity(holidayDTO);
        holiday = holidayRepository.save(holiday);
        HolidayDTO result = holidayMapper.toDto(holiday);
        holidaySearchRepository.save(holiday);
        return result;
    }

    /**
     * Partially update a holiday.
     *
     * @param holidayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HolidayDTO> partialUpdate(HolidayDTO holidayDTO) {
        log.debug("Request to partially update Holiday : {}", holidayDTO);

        return holidayRepository
            .findById(holidayDTO.getId())
            .map(
                existingHoliday -> {
                    holidayMapper.partialUpdate(existingHoliday, holidayDTO);
                    return existingHoliday;
                }
            )
            .map(holidayRepository::save)
            .map(
                savedHoliday -> {
                    holidaySearchRepository.save(savedHoliday);

                    return savedHoliday;
                }
            )
            .map(holidayMapper::toDto);
    }

    /**
     * Get all the holidays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HolidayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Holidays");
        return holidayRepository.findAll(pageable).map(holidayMapper::toDto);
    }

    /**
     * Get one holiday by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HolidayDTO> findOne(Long id) {
        log.debug("Request to get Holiday : {}", id);
        return holidayRepository.findById(id).map(holidayMapper::toDto);
    }

    /**
     * Delete the holiday by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Holiday : {}", id);
        holidayRepository.deleteById(id);
        holidaySearchRepository.deleteById(id);
    }

    /**
     * Search for the holiday corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HolidayDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Holidays for query {}", query);
        return holidaySearchRepository.search(queryStringQuery(query), pageable).map(holidayMapper::toDto);
    }
}
