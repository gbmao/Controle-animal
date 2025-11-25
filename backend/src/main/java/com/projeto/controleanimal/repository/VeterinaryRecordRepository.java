package com.projeto.controleanimal.repository;

import com.projeto.controleanimal.model.vetRecord.VeterinaryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinaryRecordRepository extends JpaRepository<VeterinaryRecord, Long> {
}
