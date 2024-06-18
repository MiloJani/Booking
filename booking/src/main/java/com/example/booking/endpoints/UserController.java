//package com.example.booking.endpoints;
//
//import com.example.booking.dataproviders.dto.userDTOs.RequestAdminDTO;
//import com.example.booking.dataproviders.dto.userDTOs.RequestUserDTO;
//import com.example.booking.dataproviders.dto.userDTOs.ResponseAdminDTO;
//import com.example.booking.dataproviders.dto.userDTOs.ResponseUserDTO;
//import com.example.booking.dataproviders.services.UserService;
//import io.swagger.v3.oas.annotations.security.SecurityRequirement;
//import jakarta.validation.Valid;
//import lombok.AllArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@AllArgsConstructor
//@RestController
//@SecurityRequirement(name = "Bearer authentication")
//@RequestMapping("/api/users")
////@Tag(
////        name = "CRUD REST APIs for User Resource"
////)
//public class UserController {
//
//    private final UserService userService;
//
//    @GetMapping("/findAll")
//    public ResponseEntity<List<ResponseUserDTO>> findAll() {
//
//        return ResponseEntity.ok(userService.findAllUsers());
//    }
//
//    @GetMapping("/findById/{id}")
//    public ResponseEntity<ResponseUserDTO> findById(@PathVariable Long id) {
//        return ResponseEntity.ok(userService.findUserById(id));
//    }
//
//    @PostMapping("/save")
//    public ResponseEntity<ResponseUserDTO> saveUser(@Valid @RequestBody RequestUserDTO requestUserDTO) {
//        return new ResponseEntity<>(userService.saveUser(requestUserDTO), HttpStatus.CREATED);
//    }
//
//    @PostMapping("/saveAdmin")
//    public ResponseEntity<ResponseAdminDTO> saveAdmin(@Valid @RequestBody RequestAdminDTO requestAdminDTO) {
//        return new ResponseEntity<>(userService.saveAdmin(requestAdminDTO), HttpStatus.CREATED);
//    }
//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<ResponseUserDTO> updateUser(@Valid @RequestBody RequestUserDTO requestUserDTO, @PathVariable Long id) {
//        return ResponseEntity.ok(userService.updateUser(requestUserDTO,id));
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return ResponseEntity.ok().build();
//    }
//}
