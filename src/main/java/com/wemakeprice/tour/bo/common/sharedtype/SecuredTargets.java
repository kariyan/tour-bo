package com.wemakeprice.tour.bo.common.sharedtype;

import com.wemakeprice.tour.bo.common.annotation.Secured;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class SecuredTargets {
    private List<SecuredTarget> securedTargetList;
    private String className;
    private boolean iterable;

    public SecuredTargets() {
        this.securedTargetList = new ArrayList<>();
    }

    public void addTarget(SecuredTarget securedTarget) {
        securedTargetList.add(securedTarget);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SecuredTarget {
        private Class<?> clazz;
        private String path;
        private Field field;
        private Secured securedInfo;

        public String getPath(String... args) {
            String result = StringUtils.isEmpty(path) ? field.getName() : path + "." + field.getName();
            if (args != null) {
                for (String arg: args) {
                    result = StringUtils.replace(result, "{}", arg);
                }
            }
            return result;
        }
    }
}
