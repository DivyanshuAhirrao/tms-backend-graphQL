package com.tms.service;

import com.tms.dto.PageInput;
import com.tms.dto.ShipmentFilter;
import com.tms.dto.ShipmentInput;
import com.tms.exception.ResourceNotFoundException;
import com.tms.model.Shipment;
import com.tms.model.ShipmentStatus;
import com.tms.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipmentService {
    
    private final ShipmentRepository shipmentRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "shipments")
    public Page<Shipment> findShipments(ShipmentFilter filter, PageInput pageInput, 
                                        String sortBy, String sortDirection) {
        log.info("Fetching shipments with filter: {}, page: {}, sortBy: {}, direction: {}", 
                 filter, pageInput, sortBy, sortDirection);
        
        Pageable pageable = createPageable(pageInput, sortBy, sortDirection);
        
        if (filter == null || isEmptyFilter(filter)) {
            return shipmentRepository.findAll(pageable);
        }
        
        return shipmentRepository.findByFilters(
            filter.getShipperName(),
            filter.getCarrierName(),
            filter.getStatus(),
            filter.getPickupLocation(),
            filter.getDeliveryLocation(),
            filter.getFlagged(),
            pageable
        );
    }
    
    @Transactional(readOnly = true)
    public Shipment findById(Long id) {
        log.info("Fetching shipment by id: {}", id);
        return shipmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Shipment not found with id: " + id));
    }
    
    @Transactional
    @CacheEvict(value = "shipments", allEntries = true)
    public Shipment createShipment(ShipmentInput input) {
        log.info("Creating shipment: {}", input.getShipmentNumber());
        
        Shipment shipment = Shipment.builder()
            .shipmentNumber(input.getShipmentNumber())
            .shipperName(input.getShipperName())
            .shipperEmail(input.getShipperEmail())
            .shipperPhone(input.getShipperPhone())
            .carrierName(input.getCarrierName())
            .carrierContact(input.getCarrierContact())
            .pickupLocation(input.getPickupLocation())
            .pickupDate(input.getPickupDate())
            .deliveryLocation(input.getDeliveryLocation())
            .deliveryDate(input.getDeliveryDate())
            .trackingNumber(input.getTrackingNumber())
            .status(input.getStatus() != null ? input.getStatus() : ShipmentStatus.PENDING)
            .weight(input.getWeight())
            .dimensions(input.getDimensions())
            .rate(input.getRate())
            .currency(input.getCurrency() != null ? input.getCurrency() : "USD")
            .specialInstructions(input.getSpecialInstructions())
            .flagged(false)
            .build();
        
        return shipmentRepository.save(shipment);
    }
    
    @Transactional
    @CacheEvict(value = "shipments", allEntries = true)
    public Shipment updateShipment(Long id, ShipmentInput input) {
        log.info("Updating shipment id: {}", id);
        
        Shipment shipment = findById(id);
        
        shipment.setShipmentNumber(input.getShipmentNumber());
        shipment.setShipperName(input.getShipperName());
        shipment.setShipperEmail(input.getShipperEmail());
        shipment.setShipperPhone(input.getShipperPhone());
        shipment.setCarrierName(input.getCarrierName());
        shipment.setCarrierContact(input.getCarrierContact());
        shipment.setPickupLocation(input.getPickupLocation());
        shipment.setPickupDate(input.getPickupDate());
        shipment.setDeliveryLocation(input.getDeliveryLocation());
        shipment.setDeliveryDate(input.getDeliveryDate());
        shipment.setTrackingNumber(input.getTrackingNumber());
        if (input.getStatus() != null) {
            shipment.setStatus(input.getStatus());
        }
        shipment.setWeight(input.getWeight());
        shipment.setDimensions(input.getDimensions());
        shipment.setRate(input.getRate());
        if (input.getCurrency() != null) {
            shipment.setCurrency(input.getCurrency());
        }
        shipment.setSpecialInstructions(input.getSpecialInstructions());
        
        return shipmentRepository.save(shipment);
    }
    
    @Transactional
    @CacheEvict(value = "shipments", allEntries = true)
    public boolean deleteShipment(Long id) {
        log.info("Deleting shipment id: {}", id);
        
        Shipment shipment = findById(id);
        shipmentRepository.delete(shipment);
        return true;
    }
    
    @Transactional
    @CacheEvict(value = "shipments", allEntries = true)
    public Shipment flagShipment(Long id, Boolean flagged) {
        log.info("Flagging shipment id: {} as {}", id, flagged);
        
        Shipment shipment = findById(id);
        shipment.setFlagged(flagged);
        return shipmentRepository.save(shipment);
    }
    
    private Pageable createPageable(PageInput pageInput, String sortBy, String sortDirection) {
        int page = pageInput != null && pageInput.getPage() != null ? pageInput.getPage() : 0;
        int size = pageInput != null && pageInput.getSize() != null ? pageInput.getSize() : 10;
        
        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "DESC".equalsIgnoreCase(sortDirection) 
                ? Sort.Direction.DESC 
                : Sort.Direction.ASC;
            return PageRequest.of(page, size, Sort.by(direction, sortBy));
        }
        
        return PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    }
    
    private boolean isEmptyFilter(ShipmentFilter filter) {
        return filter.getShipperName() == null &&
               filter.getCarrierName() == null &&
               filter.getStatus() == null &&
               filter.getPickupLocation() == null &&
               filter.getDeliveryLocation() == null &&
               filter.getFlagged() == null;
    }
}
