package com.joceano.cambioservice.controller;

import com.joceano.cambioservice.model.Cambio;
import com.joceano.cambioservice.repository.CambioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@RestController
@RequestMapping("cambio-service")
public class CambioController {

    @Autowired
    private Environment environment;

    @Autowired
    private CambioRepository repository;

    @GetMapping("/{amount}/{from}/{to}")
    public Cambio getCambio(
            @PathVariable("amount") BigDecimal amount,
            @PathVariable("from") String from,
            @PathVariable("to") String to) {
        var cambio = repository.findByFromAndTo(from, to);
        if (Objects.isNull(cambio))
            throw new RuntimeException("Currency Unsupported");
        var port = environment.getProperty("local.server.port");
        BigDecimal convertionFactor = cambio.getConversionFactor();
        BigDecimal convertedValue = convertionFactor.multiply(amount);
        cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
        cambio.setEnvironment(port);
        return cambio;
    }
}
