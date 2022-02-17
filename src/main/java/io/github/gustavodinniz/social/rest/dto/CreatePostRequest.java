package io.github.gustavodinniz.social.rest.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreatePostRequest {

    private String text;
}
