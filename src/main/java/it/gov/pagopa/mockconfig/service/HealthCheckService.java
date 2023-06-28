package it.gov.pagopa.mockconfig.service;

import it.gov.pagopa.mockconfig.repository.HealthCheckRepository;
import it.gov.pagopa.mockconfig.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HealthCheckService {

    @Autowired
    private HealthCheckRepository dao;

    public String checkDBConnection() {
        try {
            return dao.health().isPresent() ?  Constants.HEALTHCHECK_DBCONNECTION_UP : Constants.HEALTHCHECK_DBCONNECTION_DOWN;
        } catch (DataAccessResourceFailureException e) {
            return Constants.HEALTHCHECK_DBCONNECTION_DOWN;
        }
    }
}
