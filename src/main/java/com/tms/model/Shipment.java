package com.tms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments", indexes = {
    @Index(name = "idx_shipment_number", columnList = "shipmentNumber"),
    @Index(name = "idx_status", columnList = "status"),
    @Index(name = "idx_shipper_name", columnList = "shipperName"),
    @Index(name = "idx_carrier_name", columnList = "carrierName")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shipment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Shipment number is required")
    @Column(nullable = false, unique = true)
    private String shipmentNumber;
    
    @NotBlank(message = "Shipper name is required")
    @Column(nullable = false)
    private String shipperName;
    
    @Email(message = "Invalid email format")
    private String shipperEmail;
    
    private String shipperPhone;
    
    @NotBlank(message = "Carrier name is required")
    @Column(nullable = false)
    private String carrierName;
    
    private String carrierContact;
    
    @NotBlank(message = "Pickup location is required")
    @Column(nullable = false)
    private String pickupLocation;
    
    private String pickupDate;
    
    @NotBlank(message = "Delivery location is required")
    @Column(nullable = false)
    private String deliveryLocation;
    
    private String deliveryDate;
    
    private String trackingNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShipmentStatus status = ShipmentStatus.PENDING;
    
    @PositiveOrZero(message = "Weight must be positive or zero")
    private Double weight;
    
    private String dimensions;
    
    @PositiveOrZero(message = "Rate must be positive or zero")
    private Double rate;
    
    private String currency = "USD";
    
    @Column(length = 1000)
    private String specialInstructions;
    
    @Column(nullable = false)
    private Boolean flagged = false;
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
