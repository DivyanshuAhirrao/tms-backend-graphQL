package com.tms.dto;

import com.tms.model.ShipmentStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentFilter {
    private String shipperName;
    private String carrierName;
    private ShipmentStatus status;
    private String pickupLocation;
    private String deliveryLocation;
    private Boolean flagged;
}
