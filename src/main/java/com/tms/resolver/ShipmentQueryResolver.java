package com.tms.resolver;

import com.tms.dto.PageInput;
import com.tms.dto.ShipmentFilter;
import com.tms.dto.ShipmentPage;
import com.tms.model.Shipment;
import com.tms.model.User;
import com.tms.service.ShipmentService;
import com.tms.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShipmentQueryResolver {
    
    private final ShipmentService shipmentService;
    private final UserService userService;
    
    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ShipmentPage shipments(
            @Argument ShipmentFilter filter,
            @Argument PageInput page,
            @Argument String sortBy,
            @Argument String sortDirection) {
        
        log.info("Query: shipments with filter: {}", filter);
        return ShipmentPage.from(
            shipmentService.findShipments(filter, page, sortBy, sortDirection)
        );
    }
    
    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Shipment shipment(@Argument Long id) {
        log.info("Query: shipment with id: {}", id);
        return shipmentService.findById(id);
    }
    
    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public User me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        log.info("Query: me for user: {}", username);
        return userService.findByUsername(username);
    }
}
