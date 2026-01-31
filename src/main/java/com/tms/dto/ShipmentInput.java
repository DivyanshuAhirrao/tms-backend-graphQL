package com.tms.dto;

import com.tms.model.ShipmentStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentInput {
    
    @NotBlank(message = "Shipment number is required")
    private String shipmentNumber;
    
    @NotBlank(message = "Shipper name is required")
    private String shipperName;
    
    @Email(message = "Invalid email format")
    private String shipperEmail;
    
    private String shipperPhone;
    
    @NotBlank(message = "Carrier name is required")
    private String carrierName;
    
    private String carrierContact;
    
    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;
    
    private String pickupDate;
    
    @NotBlank(message = "Delivery location is required")
    private String deliveryLocation;
    
    private String deliveryDate;
    
    private String trackingNumber;
    
    private ShipmentStatus status;
    
    @PositiveOrZero(message = "Weight must be positive or zero")
    private Double weight;
    
    private String dimensions;
    
    @PositiveOrZero(message = "Rate must be positive or zero")
    private Double rate;
    
    private String currency;
    
    private String specialInstructions;
}
