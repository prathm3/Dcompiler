package com.service.compiler.CodeExecutionService.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookGetByIdResponse implements Serializable {

    @JsonProperty(value = "id")
    private int id;

    @JsonProperty(value = "title")
    private String title;


    public BookGetByIdResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
    }

}
