package com.wemakeprice.tour.bo.common.util;

import com.wemakeprice.tour.bo.common.annotation.Secured;
import com.wemakeprice.tour.bo.common.annotation.SecuredBean;
import com.wemakeprice.tour.bo.common.exception.DecodingException;
import com.wemakeprice.tour.bo.common.exception.EncodingException;
import com.wemakeprice.tour.bo.common.exception.MaskingException;
import com.wemakeprice.tour.bo.common.exception.SecuredException;
import com.wemakeprice.tour.bo.common.request.SecuredBeanHolder;
import com.wemakeprice.tour.bo.common.sharedtype.SecuredTargets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

@Slf4j
public enum SecuredProcessor {

    MASKING((content, annotation) -> {
        try {
            return MaskingUtils.masking(content, annotation.value());
        } catch (MaskingException e) {
            log.warn(e.getMessage(), e);
            return "";
        }
    }),
    ENCRYPTION((content, annotation) -> {
        try {
            return AES256.getInstance().encode(content, annotation.value());
        } catch (EncodingException e) {
            log.warn(e.getMessage(), e);
            return "";
        }
    }),
    DECRYPTION((content, annotation) -> {
        try {
            return AES256.getInstance().decode(content, annotation.value());
        } catch (DecodingException e) {
            log.warn(e.getMessage(), e);
            return "";
        }
    }),
    CLEAR((content, annotation) -> "");

    private final Map<String, SecuredTargets> cachedSecuredTypeMap = new ConcurrentHashMap<>();
    private final BiFunction<String, Secured, String> function;

    SecuredProcessor(BiFunction<String, Secured, String> function) {
        this.function = function;
    }

    public void process(Object bean) {
        process(bean, null);
    }

    public void process(Object bean, MaskingMode maskingMode) {
        if (bean == null) {
            return;
        }
        if (contains(bean, maskingMode)) {
            return;
        }

        SecuredTargets securedFields = buildSecuredTargetsFromObject(bean);
        if (securedFields == null) {
            return;
        }

        for (SecuredTargets.SecuredTarget securedTarget : securedFields.getSecuredTargetList()) {
            if (securedFields.isIterable()) {
                Iterable<?> list = (Iterable<?>) bean;
                for (Object obj : list) {
                    applySecuredProcess(obj, securedTarget, maskingMode, true);
                }
            } else {
                applySecuredProcess(bean, securedTarget, maskingMode, false);
            }
        }
    }

    private boolean contains(Object obj, MaskingMode maskingMode) {
        Set<BeanStatus> securedBeans = SecuredBeanHolder.getSecuredBeans();

        for (BeanStatus beanStatus : securedBeans) {
            if (beanStatus.obj == obj
                    && beanStatus.lastProcessed == this
                    && beanStatus.maskingMode == maskingMode) {
                return true;
            }
        }

        securedBeans.add(new BeanStatus(obj, this, maskingMode));
        return false;
    }

    private void applySecuredProcess(Object bean, SecuredTargets.SecuredTarget securedTarget, MaskingMode maskingMode,
                                     boolean iterableFlag) {
        String[] pathArray = StringUtils.split(securedTarget.getPath(), "[{}]");
        if (pathArray == null) {
            return;
        }

        if (pathArray.length > 2) {
            throw new SecuredException("Not supported object array depth more than one");

        } else if (pathArray.length == 2) {
            try {
                Object obj = PropertyUtils.getProperty(bean, pathArray[0]);
                if (obj != null) {
                    int i = 0;
                    for (Object ignored : ((Iterable<?>) obj)) {
                        String property = (String) PropertyUtils.getProperty(bean, securedTarget.getPath(Integer.toString(i)));
                        if (!StringUtils.isEmpty(property)) {
                            String converted = function.apply(property, securedTarget.getSecuredInfo());
                            PropertyUtils.setProperty(bean, securedTarget.getPath(Integer.toString(i)), converted);
                        }
                        i++;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new SecuredException(e.getMessage(), e);
            }
        } else {
            try {
                String property = (String) PropertyUtils.getProperty(bean, securedTarget.getPath());
                if (!StringUtils.isEmpty(property) && !disableMaskingOnCertainCondition(maskingMode, iterableFlag)) {
                    String converted = function.apply(property, securedTarget.getSecuredInfo());
                    PropertyUtils.setProperty(bean, securedTarget.getPath(), converted);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new SecuredException(e.getMessage(), e);
            }
        }
    }

    private boolean disableMaskingOnCertainCondition(MaskingMode maskingMode, boolean iterableFlag) {
        if (this == MASKING && maskingMode == MaskingMode.LIST_ONLY) {
            return !iterableFlag;
        }
        return false;
    }

    SecuredTargets buildSecuredTargetsFromObject(Object object) {
        String key;
        Class<?> iterableClass = null;
        Class<?> targetClass = object.getClass();
        boolean collection = false;

        if (Map.class.isAssignableFrom(targetClass)) { // Map class will be ignored
            return null;

        } else if (Iterable.class.isAssignableFrom(targetClass)) {
            Iterable<?> iterable = (Iterable<?>) object;
            if (!iterable.iterator().hasNext()) { // empty list will be ignored
                return null;
            }

            iterableClass = iterable.iterator().next().getClass();
            if (!iterableClass.isAnnotationPresent(SecuredBean.class)) {
                return null;
            }

            key = targetClass.getName() + "|" + iterableClass.getName();
            collection = true;

        } else {
            if (!(targetClass.isAnnotationPresent(SecuredBean.class))) {
                return null;
            }
            key = targetClass.getName();
        }

        SecuredTargets securedTargets = cachedSecuredTypeMap.get(key);

        if (securedTargets == null) {
            securedTargets = new SecuredTargets();
            if (collection) {
                securedTargets.setIterable(true);
                researchSecuredFields(null, iterableClass, securedTargets);
            } else {
                researchSecuredFields(null, object.getClass(), securedTargets);
            }
            cachedSecuredTypeMap.put(key, securedTargets);
        }

        return securedTargets;
    }

    private void researchSecuredFields(String parentPath, Class<?> targetClass, SecuredTargets securedTargets) {
        for (Field field : targetClass.getDeclaredFields()) {
            Secured annotation = AnnotationUtils.getAnnotation(field, Secured.class);

            if (annotation != null) { // found Secured annotation.
                securedTargets.addTarget(new SecuredTargets.SecuredTarget(field.getType(),
                        parentPath, field, annotation));

            } else {
                Class<?> type = field.getType();
                if (type.isPrimitive() || type.isEnum() || type.getPackage() == null) {
                    continue;
                }

                if (Iterable.class.isAssignableFrom(type)) { // Find collection's field, limit one depth.
                    if (ParameterizedType.class.isAssignableFrom(field.getGenericType().getClass())) {
                        ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                        type = (Class<?>) genericType.getActualTypeArguments()[0];

                        if (type.getPackage().getName().startsWith("com.wonders.admin")) {
                            if (checkSameNestedClassField(parentPath, field)) {
                                continue;
                            }

                            researchSecuredFields(buildFieldPath(parentPath, field.getName(), true), type,
                                    securedTargets);
                        }
                    }
                } else { // or user defined type
                    if (type.getPackage().getName().startsWith("com.wonders.admin")) {
                        if (checkSameNestedClassField(parentPath, field)) {
                            continue;
                        }

                        researchSecuredFields(buildFieldPath(parentPath, field.getName(), false), type,
                                securedTargets);
                    }
                }
            }
        }
    }

    private boolean checkSameNestedClassField(String parentPath, Field field) {
        return !StringUtils.isEmpty(parentPath) && Arrays.asList(parentPath.replace("[{}]", "")
                .split("\\.")).contains(field.getName());
    }

    private String buildFieldPath(String parent, String current, boolean collection) {
        String path = !StringUtils.isEmpty(parent) ? parent + "." + current : current;
        return collection ? path + "[{}]" : path;
    }

    public enum MaskingMode {
        LIST_ONLY
    }

    @AllArgsConstructor
    public static class BeanStatus {
        private final Object obj;
        private final SecuredProcessor lastProcessed;
        private final MaskingMode maskingMode;
    }
}
