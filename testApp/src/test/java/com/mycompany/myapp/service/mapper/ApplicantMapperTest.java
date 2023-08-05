package com.mycompany.myapp.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class ApplicantMapperTest {

    private ApplicantMapper applicantMapper;

    @BeforeEach
    public void setUp() {
        applicantMapper = new ApplicantMapperImpl();
    }
}
