package com.tms.repository;

import com.tms.model.Shipment;
import com.tms.model.ShipmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    
    Optional<Shipment> findByShipmentNumber(String shipmentNumber);
    
    Page<Shipment> findByShipperNameContainingIgnoreCase(String shipperName, Pageable pageable);
    
    Page<Shipment> findByCarrierNameContainingIgnoreCase(String carrierName, Pageable pageable);
    
    Page<Shipment> findByStatus(ShipmentStatus status, Pageable pageable);
    
    Page<Shipment> findByFlagged(Boolean flagged, Pageable pageable);
    
    @Query("SELECT s FROM Shipment s WHERE " +
           "(:shipperName IS NULL OR LOWER(s.shipperName) LIKE LOWER(CONCAT('%', :shipperName, '%'))) AND " +
           "(:carrierName IS NULL OR LOWER(s.carrierName) LIKE LOWER(CONCAT('%', :carrierName, '%'))) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:pickupLocation IS NULL OR LOWER(s.pickupLocation) LIKE LOWER(CONCAT('%', :pickupLocation, '%'))) AND " +
           "(:deliveryLocation IS NULL OR LOWER(s.deliveryLocation) LIKE LOWER(CONCAT('%', :deliveryLocation, '%'))) AND " +
           "(:flagged IS NULL OR s.flagged = :flagged)")
    Page<Shipment> findByFilters(
        @Param("shipperName") String shipperName,
        @Param("carrierName") String carrierName,
        @Param("status") ShipmentStatus status,
        @Param("pickupLocation") String pickupLocation,
        @Param("deliveryLocation") String deliveryLocation,
        @Param("flagged") Boolean flagged,
        Pageable pageable
    );
}
