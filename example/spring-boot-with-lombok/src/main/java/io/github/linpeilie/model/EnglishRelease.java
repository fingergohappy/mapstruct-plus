package io.github.linpeilie.model;

import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import io.github.linpeilie.mapper.Titles;
import lombok.Data;

@Data
@AutoMapper(target = FrenchRelease.class, uses = Titles.class)
public class EnglishRelease {

    @AutoMapping(qualifiedByName = "EnglishToFrench")
    private String title;

}
