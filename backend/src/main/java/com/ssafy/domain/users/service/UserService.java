package com.ssafy.domain.users.service;

import com.ssafy.domain.company.dto.CompanyDtoMapper;
import com.ssafy.domain.company.dto.response.CompanyResponse;
import com.ssafy.domain.company.entity.Company;
import com.ssafy.domain.company.entity.CompanyScrap;
import com.ssafy.domain.company.repository.CompanyScrapRepository;
import com.ssafy.domain.users.dto.TendencyDtoMapper;
import com.ssafy.domain.users.dto.request.RegisterRequest;
import com.ssafy.domain.users.dto.request.TendencyRequest;
import com.ssafy.domain.users.dto.response.TendencyResponse;
import com.ssafy.domain.users.entity.Tendency;
import com.ssafy.domain.users.entity.Users;
import com.ssafy.domain.users.exception.UserNotFoundException;
import com.ssafy.domain.users.exception.DuplicatedEmailException;
import com.ssafy.domain.users.repository.TendencyRepository;
import com.ssafy.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TendencyRepository tendencyRepository;
    private final CompanyScrapRepository companyScrapRepository;

    @Transactional
    public void register(RegisterRequest request) {
        String email = normalizeEmail(request.getEmail());

        if(userRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException();
        }

        Users user = Users.createUser(email, passwordEncoder.encode(request.getPassword()));
        try {
            userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicatedEmailException();
        }
    }

    @Transactional
    public Users socialRegister(Users user) {
        try {
            return userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicatedEmailException();
        }
    }

//    private void validateDuplicateMember(UserDto user) {
//        Users findUser = findUser(user.getId());
//        if (findUser != null) {
//            throw new IllegalStateException("이미 가입된 회원입니다.");
//        }
//    }

    public Users findUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }


    /**
     * 회원 성향 입력
     * @param id
     * @param tendencyRequest
     */
    public void applyUsersTendency(Integer id, TendencyRequest tendencyRequest){
        Users user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        tendencyRepository.save(Tendency.createUserTendency(user, tendencyRequest));
    }


    /**
     * 회원 성향 조회
     * @param user
     * @return
     */
    public List<TendencyResponse> findTendency(Users user) {
        List<Tendency> tendencies = tendencyRepository.findAllByUsers(user);
        return TendencyDtoMapper.tendencyToDtoList(tendencies);
    }


    /**
     * 회원 관심기업 조회
     * @param userId
     * @return
     */

    public List<CompanyResponse> findUserScrap(Integer userId){
        Users user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        List<CompanyScrap> companyScrap = companyScrapRepository.findAllByUser(user);

        List<Company> companyList = companyScrap.stream()
                .map(CompanyScrap::getCompany)
                .toList();

        return CompanyDtoMapper.companyEntityToDtoList(companyList);


    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
