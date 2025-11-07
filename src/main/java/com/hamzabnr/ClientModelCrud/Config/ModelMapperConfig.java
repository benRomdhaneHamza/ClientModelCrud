package com.hamzabnr.ClientModelCrud.Config;

import com.hamzabnr.ClientModelCrud.Models.ClientModel;
import com.hamzabnr.ClientModelCrud.dto.ClientDTO;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

  // This makes ModelMapper available for dependency injection in your whole app.
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper mapper = new ModelMapper();
    mapper.typeMap(ClientModel.class, ClientDTO.class)
        .addMappings(m -> {
          m.map(ClientModel::getName, ClientDTO::setName);
          m.map(ClientModel::getEmail, ClientDTO::setEmail);
        });
    mapper.typeMap(ClientDTO.class, ClientModel.class)
        .addMappings(m -> {
          m.map(ClientDTO::getName, ClientModel::setName);
          m.map(ClientDTO::getEmail, ClientModel::setEmail);
        });

    return mapper;
  }


}
