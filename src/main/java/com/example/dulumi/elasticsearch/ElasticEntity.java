package com.example.dulumi.elasticsearch;

import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
@Document(indexName = "elastic_entity")
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
    private String createdDate;

    @Field(type = FieldType.Keyword)
    @Nullable
    private String category;
}
