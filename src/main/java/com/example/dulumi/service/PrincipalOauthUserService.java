package com.example.dulumi.service;

import com.example.dulumi.Repository.UserRepository;
import com.example.dulumi.domain.PrincipalDetails;
import com.example.dulumi.domain.Role;
import com.example.dulumi.domain.User;
import com.example.dulumi.info.GoogleUserInfo;
import com.example.dulumi.info.KakaoUserInfo;
import com.example.dulumi.info.OAuth2UserInfo;

import com.example.dulumi.info.emailUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;



import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PrincipalOauthUserService extends DefaultOAuth2UserService {
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PrincipalOauthUserService.class);

    public PrincipalOauthUserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //"registraionId" 로 어떤 OAuth 로 로그인 했는지 확인 가능(google,naver등)
        System.out.println("getClientRegistration: "+userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes: "+ super.loadUser(userRequest).getAttributes());
        //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Clien라이브러리가 받아줌) -> code를 통해서 AcssToken요청(access토큰 받음)
        // => "userRequest"가 감고 있는 정보
        //회원 프로필을 받아야하는데 여기서 사용되는것이 "loadUser" 함수이다 -> 구글 로 부터 회원 프로필을 받을수 있다.


        /**
         *  OAuth 로그인 회원 가입
         */
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo =null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
//        else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
//            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
//        }
//
        else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo((Map)oAuth2User.getAttributes().get("kakao_account"),
                    String.valueOf(oAuth2User.getAttributes().get("id")));
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("email")) {
            oAuth2UserInfo = new emailUserInfo(oAuth2User.getAttributes());
        }
        else {
            System.out.println("지원하지 않는 로그인입니다.");
        }

        assert oAuth2UserInfo != null;
        String provider = oAuth2UserInfo.getProvider(); //google , naver, facebook etc
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password =  bCryptPasswordEncoder.encode("테스트"); //중요하지 않음 그냥 패스워드 암호화 하
        String email = oAuth2UserInfo.getEmail();
        Role role = Role.USER;

        Optional<User> user = userRepository.findByUsername(username);

        Optional<User> optionalUser = userRepository.findByUsername(username);
        User userEntity = optionalUser.orElse(null);
        //처음 서비스를 이용한 회원일 경우
        if(userEntity == null) {
            LocalDateTime createTime = LocalDateTime.now();
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .provider_id(providerId)
                    .create_date(createTime)
                    .build();

            userRepository.save(userEntity);
            return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
        }
        else {
            return new PrincipalDetails(user.get(), oAuth2User.getAttributes());
        }
    }
}

 