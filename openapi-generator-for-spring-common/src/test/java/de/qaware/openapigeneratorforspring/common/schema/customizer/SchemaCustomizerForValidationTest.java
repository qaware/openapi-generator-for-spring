package de.qaware.openapigeneratorforspring.common.schema.customizer;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.qaware.openapigeneratorforspring.common.annotation.AnnotationsSupplier;
import de.qaware.openapigeneratorforspring.model.media.Schema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Negative;
import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchemaCustomizerForValidationTest {

    private final SchemaCustomizerForValidation sut = new SchemaCustomizerForValidation();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private Schema schema;
    @Mock
    private AnnotationsSupplier annotationsSupplier;
    @Mock
    private JavaType javaType;
    @Mock
    private SchemaCustomizer.RecursiveResolver recursiveResolver;

    @BeforeEach
    void setUp() {
        when(annotationsSupplier.findAnnotations(any())).thenAnswer(in -> Stream.empty());
    }

    @AfterEach
    void tearDown() {
        verifyNoInteractions(recursiveResolver);
    }

    @Test
    void validationAnnotation(@Mock Min annotation) {
        long minValue = 3;
        when(annotation.value()).thenReturn(minValue);
        when(annotationsSupplier.findAnnotations(Min.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMinimum(new BigDecimal(minValue));
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock Max annotation) {
        long maxValue = 3;
        when(annotation.value()).thenReturn(maxValue);
        when(annotationsSupplier.findAnnotations(Max.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMaximum(new BigDecimal(maxValue));
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock DecimalMin annotation) {
        String minValue = "3";
        when(annotation.value()).thenReturn(minValue);
        when(annotationsSupplier.findAnnotations(DecimalMin.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMinimum(new BigDecimal(minValue));
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock DecimalMax annotation) {
        String maxValue = "3";
        when(annotation.value()).thenReturn(maxValue);
        when(annotationsSupplier.findAnnotations(DecimalMax.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMaximum(new BigDecimal(maxValue));
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation_onCharSequenceType(@Mock NotEmpty annotation) {
        when(annotationsSupplier.findAnnotations(NotEmpty.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, objectMapper.constructType(CharSequence.class), annotationsSupplier, recursiveResolver);

        verify(schema).setMinLength(1);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation_onCollectionType(@Mock NotEmpty annotation) {
        when(annotationsSupplier.findAnnotations(NotEmpty.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, objectMapper.constructType(Collection.class), annotationsSupplier, recursiveResolver);

        verify(schema).setMinItems(1);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock NotBlank annotation) {
        when(annotationsSupplier.findAnnotations(NotBlank.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMinLength(1);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock Negative annotation) {
        when(annotationsSupplier.findAnnotations(Negative.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMaximum(BigDecimal.ZERO);
        verify(schema).setExclusiveMaximum(true);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock NegativeOrZero annotation) {
        when(annotationsSupplier.findAnnotations(NegativeOrZero.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMaximum(BigDecimal.ZERO);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock Positive annotation) {
        when(annotationsSupplier.findAnnotations(Positive.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMinimum(BigDecimal.ZERO);
        verify(schema).setExclusiveMinimum(true);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock PositiveOrZero annotation) {
        when(annotationsSupplier.findAnnotations(PositiveOrZero.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setMinimum(BigDecimal.ZERO);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation(@Mock Pattern annotation) {
        String pattern = ".*";
        when(annotation.regexp()).thenReturn(pattern);
        when(annotationsSupplier.findAnnotations(Pattern.class)).thenReturn(Stream.of(annotation));
        sut.customize(schema, javaType, annotationsSupplier, recursiveResolver);

        verify(schema).setPattern(pattern);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation_onCharSequenceType(@Mock Size annotation) {
        //min/max defaults
        when(annotation.min()).thenReturn(0);
        when(annotation.max()).thenReturn(Integer.MAX_VALUE);
        when(annotationsSupplier.findAnnotations(Size.class)).thenAnswer(invocation -> Stream.of(annotation));
        sut.customize(schema, objectMapper.constructType(CharSequence.class), annotationsSupplier, recursiveResolver);

        verifyNoInteractions(schema);

        int min = 3;
        int max = 5;
        when(annotation.min()).thenReturn(min);
        when(annotation.max()).thenReturn(max);
        sut.customize(schema, objectMapper.constructType(CharSequence.class), annotationsSupplier, recursiveResolver);

        verify(schema).setMinLength(min);
        verify(schema).setMaxLength(max);
        verifyNoMoreInteractions(schema);
    }

    @Test
    void validationAnnotation_onCollectionType(@Mock Size annotation) {
        //min/max defaults
        when(annotation.min()).thenReturn(0);
        when(annotation.max()).thenReturn(Integer.MAX_VALUE);
        when(annotationsSupplier.findAnnotations(Size.class)).thenAnswer(invocation -> Stream.of(annotation));
        sut.customize(schema, objectMapper.constructType(Collection.class), annotationsSupplier, recursiveResolver);

        verifyNoInteractions(schema);

        int min = 3;
        int max = 5;
        when(annotation.min()).thenReturn(min);
        when(annotation.max()).thenReturn(max);
        sut.customize(schema, objectMapper.constructType(Collection.class), annotationsSupplier, recursiveResolver);

        verify(schema).setMinItems(min);
        verify(schema).setMaxItems(max);
        verifyNoMoreInteractions(schema);
    }


}
