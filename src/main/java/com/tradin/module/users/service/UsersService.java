package com.tradin.module.users.service;

import com.tradin.common.exception.TradinException;
import com.tradin.common.utils.SecurityUtils;
import com.tradin.module.auth.service.dto.UserDataDto;
import com.tradin.module.feign.service.BinanceFeignService;
import com.tradin.module.users.controller.dto.response.FindUserInfoResponseDto;
import com.tradin.module.users.domain.UserSocialType;
import com.tradin.module.users.domain.Users;
import com.tradin.module.users.domain.repository.UsersRepository;
import com.tradin.module.users.service.dto.ChangeMetadataDto;
import com.tradin.module.users.service.dto.PingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.tradin.common.exception.ExceptionMessage.NOT_FOUND_USER_EXCEPTION;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService implements UserDetailsService {
    private final BinanceFeignService binanceFeignService;
    private final UsersRepository usersRepository;

    public void saveUser(UserDataDto userDataDto, UserSocialType socialType) {
        if (!isUserExist(userDataDto.getEmail())) {
            Users users = userDataDto.toEntity(socialType);
            usersRepository.save(users);
        }
    }

    //TODO - FeignClient 실패 처리하기
    public String ping(PingDto request) {
        binanceFeignService.getBtcusdtPositionQuantity(request.getBinanceApiKey(), request.getBinanceSecretKey());
        return "pong";
    }

    public String changeMetaData(ChangeMetadataDto request) {
        Users user = getUserFromSecurityContext();
        int changedLeverage = getChangedLeverage(request, user);

        user.changeLeverage(changedLeverage);
        user.changeQuantityRate(request.getQuantityRate());
        user.changeTradingType(request.getTradingTypes());

        return "success";
    }

    public FindUserInfoResponseDto findUserInfo() {
        Users user = getUserFromSecurityContext();
        return new FindUserInfoResponseDto(user.getName(), user.getEmail());
    }

    private int getChangedLeverage(ChangeMetadataDto request, Users user) {
        return binanceFeignService.changeLeverage(user.getBinanceApiKey(), user.getBinanceSecretKey(), request.getLeverage());
    }

    public Users findById(Long id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    public List<Users> findAutoTradingSubscriberByStrategyName(String name) {
        return usersRepository.findByAutoTradingSubscriber(name);
    }

    public Users getUserFromSecurityContext() {
        Long userId = SecurityUtils.getUserId();
        return findById(userId);
    }

    private boolean isUserExist(String email) {
        return findByEmail(email).isPresent();
    }

    private Optional<Users> findByEmail(String email) {
        return usersRepository.findByEmail(email);
    }

    private Users findBySub(String sub) {
        return usersRepository.findBySub(sub)
                .orElseThrow(() -> new TradinException(NOT_FOUND_USER_EXCEPTION));
    }

    @Override
    public Users loadUserByUsername(String sub) {
        return findBySub(sub);
    }
}
