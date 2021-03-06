package kr.todoit.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import kr.todoit.exception.CustomException;
import kr.todoit.exception.DefaultExceptionType;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KakaoLoginService {

    @Value("${custom.kakao.id}")
    private String kakaoClientId;

    @Value("${custom.kakao.redirect-url}")
    private String kakaoRedirectUrl;

    @Autowired
    private ResourceLoader resourceLoader;

    public void loginProcess(String code) throws IOException {
        String accessToken = getAccessTokenByCode(code);
        String kakaoEmail = getKakaoEmailByAccessToken(accessToken);
        System.out.println(kakaoEmail);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
//                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
                .build();

        FirebaseApp.initializeApp(options);

    }

    private String getAccessTokenByCode(String authorizeCode) {
        final String REQ_URL = "https://kauth.kakao.com/oauth/token";

        String access_Token = "";

        try {
            URL url = new URL(REQ_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //  URL????????? ???????????? ?????? ??? ??? ??????, POST ?????? PUT ????????? ????????? setDoOutput??? true??? ???????????????.
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //	POST ????????? ????????? ???????????? ???????????? ???????????? ?????? ??????
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoClientId);  //????????? ???????????? key
            sb.append("&redirect_uri=" + kakaoRedirectUrl);  // ????????? ????????? ?????? ??????
            sb.append("&code=" + authorizeCode);
            bw.write(sb.toString());
            bw.flush();

            //    ?????? ????????? 200????????? ??????
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            //    ????????? ?????? ?????? JSON????????? Response ????????? ????????????
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // Gson ?????????????????? ????????? ???????????? JSON?????? ?????? ??????
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();

            System.out.println("access_token : " + access_Token);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return access_Token;
    }

    private String getKakaoEmailByAccessToken(String accessToken) throws CustomException {
        final String KAKAO_VERIFY_URL = "https://kapi.kakao.com/v2/user/me";

        // KAKAO REQUEST
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity entity = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> resultMap;
        try {
            resultMap = restTemplate.exchange(KAKAO_VERIFY_URL, HttpMethod.GET, entity, Map.class);
        } catch (HttpClientErrorException e) {
            throw new CustomException(DefaultExceptionType.LOGIN_FAILS);
        }

        HashMap<String, Object> kakaoAccount = (HashMap<String, Object>) resultMap.getBody().get("kakao_account");
        String email = kakaoAccount.getOrDefault("email", null).toString();

        if (email == null) {
            throw new CustomException(DefaultExceptionType.LOGIN_FAILS);
        }
        // END KAKAO REQUEST

        log.info("????????? ????????? ?????? ??????.");
        return kakaoAccount.get("email").toString();
    }
}
