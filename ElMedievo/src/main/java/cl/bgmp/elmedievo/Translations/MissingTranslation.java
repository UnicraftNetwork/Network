package cl.bgmp.elmedievo.Translations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Indicates that a String or {@link ChatConstant} has not been translated yet. */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MissingTranslation {}
