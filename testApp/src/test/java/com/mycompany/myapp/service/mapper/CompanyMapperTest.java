package com.mycompany.myapp.service.mapper;


import org.junit.jupiter.api.BeforeEach;

class CompanyMapperTest {

    private CompanyMapper companyMapper;

    @BeforeEach
    public void setUp() {
        companyMapper = new CompanyMapperImpl();
    }
}
