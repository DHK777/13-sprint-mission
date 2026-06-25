package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.stream()
        .filter(converter -> converter instanceof MappingJackson2HttpMessageConverter)
        .forEach(converter -> {
          MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;

          List<MediaType> mediaTypes = new ArrayList<>(jacksonConverter.getSupportedMediaTypes());
          mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);

          jacksonConverter.setSupportedMediaTypes(mediaTypes);
        });
  }
}