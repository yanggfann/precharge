package com.example.precharge.repository;

import com.example.precharge.repository.entity.PreCharge;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public interface PrechargeRepository
    extends org.springframework.data.repository.Repository<PreCharge, UUID>,
        CustomizedOperations<PreCharge> {

  List<PreCharge> findByContractId(UUID contractId);
}
