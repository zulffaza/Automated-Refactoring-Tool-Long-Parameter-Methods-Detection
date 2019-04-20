package com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.model.CodeSmellName;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import com.finalproject.automated.refactoring.tool.model.PropertyModel;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author fazazulfikapp
 * @version 1.0.0
 * @since 7 November 2018
 */

public class LongParameterMethodsDetectionImplTest {

    private LongParameterMethodsDetectionImpl longParameterMethodsDetection;

    private static final Integer FIRST_INDEX = 0;
    private static final Integer SECOND_INDEX = 1;
    private static final Integer LONG_PARAMETER_METHOD_COUNT = 1;
    private static final Integer NORMAL_METHOD_COUNT = 1;
    private static final Integer EMPTY_COUNT = 0;

    private static final Long THRESHOLD = 3L;

    private List<MethodModel> methodModels;

    @Before
    public void setUp() {
        longParameterMethodsDetection = new LongParameterMethodsDetectionImpl();
        methodModels = createMethodModels();
    }

    @Test
    public void detect_singleMethod_success() {
        longParameterMethodsDetection.detect(methodModels.get(FIRST_INDEX), THRESHOLD);

        assertEquals(LONG_PARAMETER_METHOD_COUNT.intValue(), methodModels.get(FIRST_INDEX).getCodeSmells().size());
        assertEquals(CodeSmellName.LONG_PARAMETER_METHOD, methodModels.get(FIRST_INDEX).getCodeSmells().get(FIRST_INDEX));
    }

    @Test
    public void detect_singleMethod_success_notLongParameterMethod() {
        longParameterMethodsDetection.detect(methodModels.get(SECOND_INDEX), THRESHOLD);
        assertTrue(methodModels.get(SECOND_INDEX).getCodeSmells().isEmpty());
    }

    @Test
    public void detect_multiMethods_success() {
        longParameterMethodsDetection.detect(methodModels, THRESHOLD);

        assertEquals(LONG_PARAMETER_METHOD_COUNT.longValue(), getLongParameterMethodsCount().longValue());
        assertEquals(NORMAL_METHOD_COUNT.longValue(), getNormalMethodsCount().longValue());
    }

    @Test
    public void detect_multiMethods_success_notLongParameterMethod() {
        methodModels.remove(FIRST_INDEX.intValue());
        longParameterMethodsDetection.detect(methodModels, THRESHOLD);

        assertEquals(EMPTY_COUNT.longValue(), getLongParameterMethodsCount().longValue());
        assertEquals(NORMAL_METHOD_COUNT.longValue(), getNormalMethodsCount().longValue());
    }

    @Test(expected = NullPointerException.class)
    public void detect_singleMethod_failed_emptyMethod() {
        MethodModel methodModel = null;
        longParameterMethodsDetection.detect(methodModel, THRESHOLD);
    }

    @Test(expected = NullPointerException.class)
    public void detect_singleMethod_failed_emptyThreshold() {
        longParameterMethodsDetection.detect(methodModels.get(FIRST_INDEX), null);
    }

    @Test(expected = NullPointerException.class)
    public void detect_multiMethods_failed_emptyMethods() {
        methodModels = null;
        longParameterMethodsDetection.detect(methodModels, THRESHOLD);
    }

    @Test(expected = NullPointerException.class)
    public void detect_multiMethods_failed_emptyThreshold() {
        longParameterMethodsDetection.detect(methodModels, null);
    }

    private List<MethodModel> createMethodModels() {
        List<MethodModel> methodModels = new ArrayList<>();

        methodModels.add(MethodModel.builder()
                .keywords(Collections.singletonList("public"))
                .name("EmailHelp")
                .parameters(Arrays.asList(
                        PropertyModel.builder()
                                .type("String")
                                .name("emailDestination")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailCc")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailBcc")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailSubject")
                                .build(),
                        PropertyModel.builder()
                                .type("String")
                                .name("emailContent")
                                .build()))
                .exceptions(Arrays.asList("Exception", "IOException"))
                .body("\n" +
                        "       mEmailSubject = emailDestination;\n" +
                        "       mEmailSubject = emailCc;\n" +
                        "       mEmailSubject = emailBcc;\n" +
                        "       mEmailSubject = emailSubject;\n" +
                        "       mEmailContent = emailContent;\n" +
                        "\n")
                .build());

        methodModels.add(MethodModel.builder()
                .keywords(Collections.singletonList("public"))
                .returnType("MyResponse<Integer>")
                .name("addGiftInfoCategory")
                .parameters(Collections.singletonList(
                        PropertyModel.builder()
                                .type("GiftInfoCategory")
                                .name("giftInfoCategory")
                                .build()))
                .body("\n" +
                        "        String message;\n" +
                        "        int response;\n" +
                        "\n" +
                        "        try {\n" +
                        "            giftInfoCategory = mGiftInfoCategoryService.addGiftInfoCategory(giftInfoCategory);\n" +
                        "\n" +
                        "            boolean isSuccess = giftInfoCategory != null;\n" +
                        "            message = isSuccess ? \"Gift info category add success\" : \"Gift info category add failed\";\n" +
                        "            response = isSuccess ? 1 : 0;\n" +
                        "        } catch (DataIntegrityViolationException e) {\n" +
                        "            message = \"Gift info category add failed - Gift info category already exists\";\n" +
                        "            response = 0;\n" +
                        "        } catch (Exception e) {\n" +
                        "            message = \"Gift info category add failed - Internal Server Error\";\n" +
                        "            response = 0;\n" +
                        "        }\n" +
                        "\n" +
                        "        return new MyResponse<>(message, response);\n" +
                        "\n")
                .build());

        return methodModels;
    }

    private Long getLongParameterMethodsCount() {
        return methodModels.stream()
                .filter(this::isLongParameterMethod)
                .count();
    }

    private Long getNormalMethodsCount() {
        return methodModels.stream()
                .filter(methodModel -> !isLongParameterMethod(methodModel))
                .count();
    }

    private Boolean isLongParameterMethod(MethodModel methodModel) {
        return methodModel.getCodeSmells()
                .contains(CodeSmellName.LONG_PARAMETER_METHOD);
    }
}