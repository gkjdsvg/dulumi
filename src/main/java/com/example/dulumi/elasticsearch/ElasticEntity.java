package com.example.dulumi.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Document(indexName = "my_nori_index")
@JsonIgnoreProperties(ignoreUnknown = true)
//@Mapping(mappingPath = "elastic-mapping.json")
//@Setting(settingPath = "elastic-token.json")
public class ElasticEntity {
    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String author;

    @Field(type = FieldType.Date)
    private LocalDate createdDate;

    @Field(type = FieldType.Keyword)
    @Nullable
    private String category;
}
