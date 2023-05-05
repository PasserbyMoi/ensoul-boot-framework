package com.shimao.iot.core.utils;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.stream.Collectors.toList;

/**
 * @author striver.cradle
 */
public abstract class ReflectUtil {

    private static LoadingCache<Class<?>, List<Method>> methodsCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, DAYS)
            .build(ReflectUtil::_getMethods);

    private static LoadingCache<Class<?>, List<Method>> declareMethodsCache = Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(1, DAYS)
            .build(ReflectUtil::_getDeclareMethods);

    /**
     * 获取父类/父接口中首个泛型的类型，找不到则抛异常{@link RuntimeException}
     *
     * @param clazz                 当前类的类型
     * @param superClassOrInterface 父类/父接口的类型
     */
    public static <T> Class<T> getSuperGenericType(final Class<?> clazz, final Class<?> superClassOrInterface) {
        return getSuperGenericType(clazz, superClassOrInterface, 0);
    }

    /**
     * 获取父类/父接口中首个泛型的类型，找不到则抛异常{@link RuntimeException}
     *
     * @param clazz                 当前类的类型
     * @param superClassOrInterface 父类/父接口的类型
     * @param index                 指定第几个泛型参数
     */
    public static <T> Class<T> getSuperGenericType(final Class<?> clazz,
                                                   final Class<?> superClassOrInterface,
                                                   final int index) {
        return ReflectUtil.<T>findSuperGenericType(clazz, superClassOrInterface, index)
                .orElseThrow(() -> new RuntimeException(clazz + "must have a generic type"));
    }

    /**
     * 获取父类/父接口中首个泛型的类型
     *
     * @param clazz                 当前类的类型
     * @param superClassOrInterface 父类/父接口的类型
     */
    public static <T> Optional<Class<T>> findSuperGenericType(final Class<?> clazz,
                                                              final Class<?> superClassOrInterface) {
        return findSuperGenericType(clazz, superClassOrInterface, 0);
    }

    /**
     * 获取父类/父接口中泛型的类型
     *
     * @param clazz                 当前类的类型
     * @param superClassOrInterface 父类/父接口的类型
     * @param index                 指定第几个泛型参数
     */
    @SuppressWarnings("unchecked")
    public static <T> Optional<Class<T>> findSuperGenericType(final Class<?> clazz,
                                                              final Class<?> superClassOrInterface,
                                                              final int index) {
        if (clazz == null || superClassOrInterface == null || index < 0) {
            return Optional.empty();
        }
        Stream<Type> genericInterfaces = Stream.of(clazz.getGenericInterfaces());
        Stream<Type> genericSuperclass = Stream.of(clazz.getGenericSuperclass());
        List<Class<T>> genericSupertypes = Stream.concat(genericInterfaces, genericSuperclass)
                .filter(type -> type instanceof ParameterizedType)
                .map(type -> (ParameterizedType) type)
                .filter(type -> type.getRawType().getTypeName().equals(superClassOrInterface.getName()))
                .flatMap(type -> Stream.of(type.getActualTypeArguments()))
                .filter(type -> type instanceof Class<?>)
                .map(type -> (Class<T>) type)
                .collect(toList());
        if (index > genericSupertypes.size() - 1) {
            return Optional.empty();
        }
        return Optional.of(genericSupertypes.get(index));
    }

    public static List<Method> findMethods(@NonNull Class<?> c, String methodName) {
        return findMethods(c, methodName, true);
    }

    public static List<Method> findMethods(@NonNull Class<?> c, String methodName, boolean includeDefault) {
        return _findMethods(c, methodName, includeDefault, true);
    }

    public static List<Method> findDeclaredMethod(@NonNull Class<?> c, String methodName) {
        return findDeclaredMethods(c, methodName, true);
    }

    public static List<Method> findDeclaredMethods(@NonNull Class<?> c, String methodName, boolean includeDefault) {
        return _findMethods(c, methodName, includeDefault, false);
    }

    /**
     * Returns method from an object, matched by name. This may be considered as
     * a slow operation, since methods are matched one by one.
     * Returns only accessible methods.
     * Only first method is matched.
     *
     * @param c              class to examine
     * @param methodName     name of the method
     * @param includeDefault 是否要包含Java8的默认方法
     * @param publicOnly     是否只支持public方法
     */
    private static List<Method> _findMethods(@NonNull Class<?> c, String methodName, boolean includeDefault,
                                             boolean publicOnly) {
        List<Method> methods = publicOnly ? methodsCache.get(c) : declareMethodsCache.get(c);
        Objects.requireNonNull(methods);
        return methods.stream()
                .filter(method -> includeDefault || !method.isDefault())
                .filter(method -> Objects.equals(methodName, method.getName()))
                .collect(toList());
    }

    private static List<Method> _getMethods(@NonNull Class<?> c) {
        return Stream.of(c.getMethods()).collect(toList());
    }

    private static List<Method> _getDeclareMethods(@NonNull Class<?> c) {
        return Stream.of(c.getDeclaredMethods()).collect(toList());
    }
}
