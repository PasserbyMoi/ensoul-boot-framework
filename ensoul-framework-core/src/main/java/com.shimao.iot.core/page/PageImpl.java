package com.shimao.iot.core.page;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author striver.cradle
 */
@Data
public class PageImpl<T> implements Page<T> {

    private final List<T>  content = new ArrayList<>();
    private final Long     total;
    private final Pageable current;

    public PageImpl(List<T> content, Pageable pageable) {
        this(content, pageable, null);
    }

    public PageImpl(List<T> content, Pageable pageable, Long total) {
        if (content == null) {
            throw new IllegalArgumentException("Content must not be null!");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable must not be null!");
        }
        this.content.addAll(content);
        this.current = pageable;
        this.total = total;
    }

    @NonNull
    @Override
    public Pageable current() {
        return current;
    }

    @NonNull
    @Override
    public <S> Page<S> map(Function<? super T, ? extends S> mapper) {
        List<S> result = new ArrayList<>(content.size());
        for (T element : this) {
            result.add(mapper.apply(element));
        }
        return new PageImpl<>(result, current, total);
    }

    @NonNull
    @Override
    public <S> Page<S> mapAll(Function<List<T>, List<S>> mapper) {
        List<S> result = mapper.apply(content);
        return new PageImpl<>(result, current, total);
    }
}
