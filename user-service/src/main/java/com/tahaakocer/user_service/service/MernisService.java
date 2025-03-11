package com.tahaakocer.user_service.service;

import com.tahaakocer.user_service.Soap.MNHKPSPublicSoap;
import com.tahaakocer.user_service.dto.UserDto;
import com.tahaakocer.user_service.dto.ValidMernisResponse;
import com.tahaakocer.user_service.exception.GeneralException;
import org.springframework.stereotype.Service;

@Service
public class MernisService {
    private final MNHKPSPublicSoap mnhKPSPublicSoap;

    public MernisService(MNHKPSPublicSoap mnhKPSPublicSoap) {
        this.mnhKPSPublicSoap = mnhKPSPublicSoap;
    }

    public ValidMernisResponse checkIfRealPerson(UserDto userDto) {
        try {
            return ValidMernisResponse.builder()
                    .isMernisValid( mnhKPSPublicSoap.TCKimlikNoDogrula(
                            Long.parseLong(userDto.getTcNo()),
                            userDto.getFirstName(),
                            userDto.getLastName(),
                            Integer.valueOf(userDto.getBirthYear()))
                    )
                    .build();

        } catch (Exception e) {
            throw new GeneralException("Kimlik dogrulama hatasi", e);
        }
    }
}
