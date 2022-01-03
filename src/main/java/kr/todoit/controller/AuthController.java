package kr.todoit.controller;

import kr.todoit.service.KakaoLoginService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/login")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private KakaoLoginService kakaoLoginService;

    @GetMapping("/oauth/kakao")
    public ResponseEntity<HashMap<String, Object>> kakaoLogin(@RequestParam(value = "code", required = false) String code) throws IOException {
        System.out.println(code);

        kakaoLoginService.loginProcess(code);
        return null;
    }
}