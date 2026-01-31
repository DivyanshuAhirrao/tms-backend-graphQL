package com.tms.resolver;

import com.tms.dto.AuthPayload;
import com.tms.dto.ShipmentInput;
import com.tms.model.Shipment;
import com.tms.model.User;
import com.tms.repository.UserRepository;
import com.tms.security.JwtUtil;
import com.tms.service.ShipmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ShipmentMutationResolver {
    
    private final ShipmentService shipmentService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    
    @MutationMapping
    public AuthPayload login(@Argument String username, @Argument String password) {
        log.info("Mutation: login for user: {}", username);

        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(username, user.getRole().name());

        return AuthPayload.builder()
            .token(token)
            .user(user)
            .build();
    }
    
    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Shipment createShipment(@Argument @Valid ShipmentInput input) {
        log.info("Mutation: createShipment with number: {}", input.getShipmentNumber());
        return shipmentService.createShipment(input);
    }
    
    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Shipment updateShipment(@Argument Long id, @Argument @Valid ShipmentInput input) {
        log.info("Mutation: updateShipment with id: {}", id);
        return shipmentService.updateShipment(id, input);
    }
    
    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteShipment(@Argument Long id) {
        log.info("Mutation: deleteShipment with id: {}", id);
        return shipmentService.deleteShipment(id);
    }
    
    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public Shipment flagShipment(@Argument Long id, @Argument Boolean flagged) {
        log.info("Mutation: flagShipment with id: {} as {}", id, flagged);
        return shipmentService.flagShipment(id, flagged);
    }
}
