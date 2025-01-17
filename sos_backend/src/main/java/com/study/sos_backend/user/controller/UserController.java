package com.study.sos_backend.user.controller;

import com.study.sos_backend.user.dto.UserInfoResponseDto;
import com.study.sos_backend.user.dto.UserUpdateRequestDto;
import com.study.sos_backend.user.dto.UserUpdateResponseDto;
import com.study.sos_backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;



    @PutMapping
    @Operation(summary = "사용자 정보 변경", description = "엑세스 토큰을 통해 유저 정보를 확인 후 정보를 변경합니다. 중요) 이메일 변경으로 인해 dto 의 엑세스 토큰과 리프레쉬 토큰을 헤더에 다시 고정시켜줘야함")
    public ResponseEntity<UserUpdateResponseDto> updateUser(UserUpdateRequestDto requestDto){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        try{
            UserUpdateResponseDto responseDto = userService.updateUser(email, requestDto);
            return ResponseEntity.ok(responseDto);
        }catch (IllegalStateException e){
            return ResponseEntity.badRequest().build();
        }
    }


    @Operation(summary = "사용자 정보 반환", description = "사용자의 유저 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 반환", content = @Content(schema = @Schema(implementation = UserInfoResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "유저가 로그인하지 않았거나 정보가 없을 시 반환")
    })
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponseDto> getMyProfile() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            UserInfoResponseDto userDto = userService.getUser(email);
            return ResponseEntity.ok(userDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    // TODO 나중에 삭제해야 됨
    @GetMapping("/{id}")
    @Operation(summary = "ID 사용자 정보 반환(테스트용, 삭제하거나 비즈니스 이상만)", description = "ID 파라미터를 통해 사용자의 유저 정보를 반환합니다.")
    public ResponseEntity<UserInfoResponseDto> getUserInfo(@PathVariable Long id) {
        try {
            UserInfoResponseDto user = userService.getUser(id);
            return ResponseEntity.ok(user);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "이메일 미사용 확인", description = "사용하지 않는 이메일인지 확인합니다.")
    @GetMapping("/email")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam String email){
        if (userService.verifyEmail(email)){
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.badRequest().build();
    }




}
