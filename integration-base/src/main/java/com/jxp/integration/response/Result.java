package com.jxp.integration.response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * Created on 2024-11-19 14:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private I18nVo i18n;
    private LocalDateTime timestamp;

    @JsonIgnore
    public static final Map<Integer, I18nVo> I18N_MSG_MAP = new HashMap();

    private static <T> Result<T> get(int code, String message, T data, I18nVo i18n,
            LocalDateTime timestamp) {
        return new Result<>(code, message, data, i18n, timestamp);
    }

    public static <T> Result<T> ok() {
        final Locale locale = LocaleContextHolder.getLocale();
        final I18nVo i18 = I18N_MSG_MAP.getOrDefault(0, new I18nVo());
        return get(0,
                Locale.ENGLISH.getLanguage().equals(LocaleContextHolder.getLocale().getLanguage()) ? i18.getEnUS() : i18.getZhCN(), null
                , i18, LocalDateTime.now());
    }


}
