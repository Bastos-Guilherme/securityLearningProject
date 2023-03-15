package com.securityLearningProject.securityLearningProject.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

// controller responsible for application, simple crud for testing
@RestController
@RequestMapping("api/v1/data")
class DataController {

    var adminCar : String = "Shelby Cobra 427"
    var userCar : String = "VolksWagen Beetle 1600"

    // API endpoint intended for read and accessible only for ADMINS and MANAGERS
    @GetMapping("adimCar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER') or hasAuthority('ADMIN_CAR_READ')")
    fun getAdminCar() : ResponseEntity<String> {
        return ResponseEntity.ok(adminCar)
    }

    // API endpoint intended for read and accessible only for ADMINS, MANAGERS and USERS
    @GetMapping("userCar")
    fun getUserCar() : ResponseEntity<String> {
        return ResponseEntity.ok(userCar)
    }

    // API endpoint intended for write and accessible only for ADMINS and MANAGERS
    @PostMapping("adimCar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER') or hasAuthority('ADMIN_CAR_WRITE')")
    fun setAdminCar(
        @RequestBody(required = true) body: String
    ) : ResponseEntity<String> {
        adminCar = body
        return ResponseEntity.ok(adminCar)
    }

    // API endpoint intended for write and accessible only for ADMINS and MANAGERS with this authorization
    @PostMapping("userCar")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MANAGER') and hasAuthority('USER_CAR_WRITE')")
    fun setUserCar(
        @RequestBody(required = true) body: String
    ) : ResponseEntity<String> {
        userCar = body
        return ResponseEntity.ok(userCar)
    }


    // API endpoint intended for deletion and accessible only for MANAGERS with this authorization
    @DeleteMapping("adimCar")
    @PreAuthorize("hasRole('ROLE_MANAGER') and hasAuthority('ADMIN_CAR_DELETE')")
    fun deleteAdminCar() : ResponseEntity<String> {
        adminCar = ""
        return ResponseEntity(HttpStatus.OK)
    }


    // API endpoint intended for deletion and accessible only for ADMINS, MANAGERS with this authorization
    @DeleteMapping("userCar")
    @PreAuthorize("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN') and hasAuthority('USER_CAR_DELETE')")
    fun deleteUserCar() : ResponseEntity<String> {
        userCar = ""
        return ResponseEntity(HttpStatus.OK)
    }
}