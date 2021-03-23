package com.wemakeprice.tour.bo.service.logic.code;

import com.wemakeprice.tour.bo.common.annotation.CodeIgnore;
import com.wemakeprice.tour.bo.common.entity.TitledEnum;
import com.wemakeprice.tour.bo.common.exception.ApplicationException;
import com.wemakeprice.tour.bo.domain.service.code.CodeService;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.Getter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CodeServiceLogic implements CodeService {
    @Override
    public List<CodeEnumType> findCodeEnums() {
        TypeFilter typeFilter = new AssignableTypeFilter(TitledEnum.class);
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(typeFilter);
        Set<BeanDefinition> components = provider.findCandidateComponents("com.wemakeprice.tour.bo");
        List<CodeEnumType> codeEnumTypes = new ArrayList<>();

        try {
            for (BeanDefinition component : components) {
                Class<?> type = Class.forName(component.getBeanClassName());
                if (!type.isEnum() || type.getAnnotation(CodeIgnore.class) != null) {
                    continue;
                }
                Info annotation = type.getAnnotation(Info.class);

                String description = (annotation != null) ? annotation.title() : type.getSimpleName();
                CodeEnumType codeEnumType = new CodeEnumType(type.getSimpleName(), description);
                List<EnumField> enumFields = codeEnumType.getValues();
                Enum<?>[] enums = type.asSubclass(Enum.class).getEnumConstants();

                for (Enum<?> anEnum : enums) {
                    Field field = type.getField(anEnum.name());
                    CodeIgnore codeIgnore = field.getAnnotation(CodeIgnore.class);
                    if (codeIgnore != null) {
                        continue;
                    }
                    enumFields.add(new EnumField(anEnum.name(), ((TitledEnum) anEnum).getTitle()));
                }
                codeEnumTypes.add(codeEnumType);
            }

            return codeEnumTypes.stream()
                    .sorted(Comparator.comparing(CodeEnumType::getTypeName))
                    .collect(Collectors.toList());

        } catch (NoSuchFieldException | ClassNotFoundException e) {
            throw new ApplicationException("코드 목록 생성에 실패하였습니다.", e);
        }
    }

    @Getter
    public static class CodeEnumType {
        private final String typeName;
        private final String description;
        private final List<EnumField> values;

        CodeEnumType(String typeName, String description) {
            this.typeName = typeName;
            this.description = description;
            this.values = new ArrayList<>();
        }
    }

    @Getter
    public static class EnumField {
        private final String name;
        private final String description;

        EnumField(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
