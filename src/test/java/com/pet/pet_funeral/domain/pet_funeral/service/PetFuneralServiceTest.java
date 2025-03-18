package com.pet.pet_funeral.domain.pet_funeral.service;

import static org.junit.jupiter.api.Assertions.*;

import com.pet.pet_funeral.domain.address.model.Address;
import com.pet.pet_funeral.domain.address.service.AddressService;
import com.pet.pet_funeral.domain.pet_funeral.dto.PetFuneralRequest;
import com.pet.pet_funeral.domain.pet_funeral.model.PetFuneral;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PetFuneralServiceTest {

    @Autowired PetFuneralService petFuneralService;
    @Autowired AddressService addressService;

    private PetFuneralRequest petFuneral;

    @BeforeEach
    void mock객체생성() {
        Address address = Address.builder()
                .street("강남대로 123")
                .city("서울시")
                .state("강남구")
                .zipCode("12345")
                .build();
        addressService.save(address);
        // given
        petFuneral = PetFuneralRequest.builder()
                .name("강남 펫 장례식장")
                .openAt(LocalTime.of(9, 0))
                .closeAt(LocalTime.of(18, 0))
                .price(500000)
                .phoneNumber("123456789")
                .homepage("https://petfuneral.com")
                .legal(true)
                .address(address)
                .build();
    }

    @Test
    void 장례식장_저장() throws Exception {
        // when
        UUID saveId = petFuneralService.save(petFuneral);

        // then
        PetFuneral funeral = petFuneralService.findById(saveId);
        assertEquals(petFuneral.name(), funeral.getName());
    }

    @Test
    void 주소로_장례식장_조회() throws Exception {
        // given
        UUID saveId = petFuneralService.save(petFuneral);

        // when
        Page<PetFuneral> petFuneralPage = petFuneralService.findByCity("서울시", 0, 1);
        List<PetFuneral> content = petFuneralPage.getContent();

        // then
        assertEquals(petFuneral.name(), content.get(0).getName());
    }
}