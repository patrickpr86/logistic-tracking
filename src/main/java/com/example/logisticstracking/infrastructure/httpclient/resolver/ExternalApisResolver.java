package com.example.logisticstracking.infrastructure.httpclient.resolver;

import com.example.logisticstracking.infrastructure.httpclient.ExternalApisGateway;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class ExternalApisResolver {

    private final ExternalApisGateway externalApisGateway;

    public ExternalApisResolver(ExternalApisGateway externalApisGateway) {
        this.externalApisGateway = externalApisGateway;
    }

    public CompletableFuture<Boolean> resolveHoliday(LocalDate date, String country) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return externalApisGateway.isHoliday(date, country);
            } catch (Exception e) {
                log.error("Erro ao verificar feriado: {}", e.getMessage(), e);
                return false;
            }
        });
    }

    public CompletableFuture<String> resolveDogFact() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return externalApisGateway.getRandomDogFunFact();
            } catch (Exception e) {
                log.error("Erro ao buscar fato sobre cachorros: {}", e.getMessage(), e);
                return "Nenhum fato disponível";
            }
        });
    }
}
