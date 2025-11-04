package com.hamzabnr.ClientModelCrud.Repositories;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<ClientModel, Long> {

}
