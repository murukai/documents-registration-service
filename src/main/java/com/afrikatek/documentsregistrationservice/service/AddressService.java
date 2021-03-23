package com.afrikatek.documentsregistrationservice.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.afrikatek.documentsregistrationservice.domain.Address;
import com.afrikatek.documentsregistrationservice.domain.Applicant;
import com.afrikatek.documentsregistrationservice.repository.AddressRepository;
import com.afrikatek.documentsregistrationservice.repository.search.AddressSearchRepository;
import com.afrikatek.documentsregistrationservice.service.dto.AddressDTO;
import com.afrikatek.documentsregistrationservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsregistrationservice.service.mapper.AddressMapper;
import com.afrikatek.documentsregistrationservice.service.mapper.ApplicantMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Address}.
 */
@Service
@Transactional
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    private final AddressMapper addressMapper;

    private final ApplicantMapper applicantMapper;

    private final AddressSearchRepository addressSearchRepository;

    public AddressService(
        AddressRepository addressRepository,
        AddressMapper addressMapper,
        ApplicantMapper applicantMapper,
        AddressSearchRepository addressSearchRepository
    ) {
        this.addressRepository = addressRepository;
        this.addressMapper = addressMapper;
        this.applicantMapper = applicantMapper;
        this.addressSearchRepository = addressSearchRepository;
    }

    /**
     * Save a address.
     *
     * @param addressDTO the entity to save.
     * @return the persisted entity.
     */
    public AddressDTO save(AddressDTO addressDTO) {
        log.debug("Request to save Address : {}", addressDTO);
        Address address = addressMapper.toEntity(addressDTO);
        address = addressRepository.save(address);
        AddressDTO result = addressMapper.toDto(address);
        addressSearchRepository.save(address);
        return result;
    }

    /**
     * Partially update a address.
     *
     * @param addressDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AddressDTO> partialUpdate(AddressDTO addressDTO) {
        log.debug("Request to partially update Address : {}", addressDTO);

        return addressRepository
            .findById(addressDTO.getId())
            .map(
                existingAddress -> {
                    addressMapper.partialUpdate(existingAddress, addressDTO);
                    return existingAddress;
                }
            )
            .map(addressRepository::save)
            .map(
                savedAddress -> {
                    addressSearchRepository.save(savedAddress);

                    return savedAddress;
                }
            )
            .map(addressMapper::toDto);
    }

    /**
     * Get all the addresses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Addresses");
        return addressRepository.findAll(pageable).map(addressMapper::toDto);
    }

    /**
     *
     * @param applicantDTO
     * @return the list of addresses for a given applicant
     */
    @Transactional(readOnly = true)
    public Optional<List<AddressDTO>> findAllByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request to get all Addresses by Applicant");
        Applicant applicant = applicantMapper.toEntity(applicantDTO);
        return addressRepository.findByApplicant(applicant).map(addressMapper::toDto);
    }

    /**
     * Get one address by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AddressDTO> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id).map(addressMapper::toDto);
    }

    /**
     * Delete the address by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        addressRepository.deleteById(id);
        addressSearchRepository.deleteById(id);
    }

    /**
     * Search for the address corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AddressDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Addresses for query {}", query);
        return addressSearchRepository.search(queryStringQuery(query), pageable).map(addressMapper::toDto);
    }
}
