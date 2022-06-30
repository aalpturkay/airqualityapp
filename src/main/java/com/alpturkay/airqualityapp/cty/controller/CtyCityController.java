package com.alpturkay.airqualityapp.cty.controller;

import com.alpturkay.airqualityapp.cty.dto.CtyCitySaveRequestDto;
import com.alpturkay.airqualityapp.cty.entity.CtyCity;
import com.alpturkay.airqualityapp.cty.service.CtyCityService;
import com.alpturkay.airqualityapp.gen.response.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/allowedCities")
public class CtyCityController {
    private final CtyCityService ctyCityService;

    @PostMapping
    public RestResponse<ResponseEntity<CtyCity>> save(@RequestBody CtyCitySaveRequestDto ctyCitySaveRequestDto){
        return RestResponse.of(ResponseEntity.status(HttpStatus.CREATED).body(ctyCityService.save(ctyCitySaveRequestDto)));
    }
}
