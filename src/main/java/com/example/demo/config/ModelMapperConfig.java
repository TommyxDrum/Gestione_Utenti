package com.example.demo.config;

import com.example.demo.dto.DocumentDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.models.DocumentModel;
import com.example.demo.models.UserModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        // Mappatura UserModel
        modelMapper.addMappings(utentiMapping);
        modelMapper.addConverter(userConverter);

        // Mappatura Document
        modelMapper.addMappings(documentMapping);
        modelMapper.addConverter(documentConverter);

        return modelMapper;
    }

    PropertyMap<UserModel, UserDTO> utentiMapping = new PropertyMap<UserModel, UserDTO>() {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setNome(source.getNome());
            map().setCognome(source.getCognome());
            map().setEmail(source.getEmail());
        }
    };

    PropertyMap<DocumentModel, DocumentDTO> documentMapping = new PropertyMap<DocumentModel, DocumentDTO>() {
        @Override
        protected void configure() {
            map().setId(source.getId());
            map().setNomeFile(source.getNomeFile());
            map().setTipoFile(source.getTipoFile());
            map().setDimensione(source.getDimensione());
        }
    };

    Converter<String, String> userConverter = new Converter<String, String>() {
        @Override
        public String convert(MappingContext<String, String> mappingContext) {
            return mappingContext.getSource() == null ? "" : mappingContext.getSource().trim();
        }
    };

    Converter<String, String> documentConverter = new Converter<String, String>() {
        @Override
        public String convert(MappingContext<String, String> mappingContext) {
            return mappingContext.getSource() == null ? "" : mappingContext.getSource().trim();
        }
    };
}
