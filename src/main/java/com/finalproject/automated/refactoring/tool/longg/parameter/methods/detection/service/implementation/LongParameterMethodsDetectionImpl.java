package com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.LongParameterMethodsDetection;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fazazulfikapp
 * @version 1.0.0
 * @since 7 November 201888
 */

@Service
public class LongParameterMethodsDetectionImpl implements LongParameterMethodsDetection {

    private static final Integer FIRST_INDEX = 0;

    @Override
    public MethodModel detect(MethodModel methodModel, Long threshold) {
        try {
            return detect(Collections.singletonList(methodModel), threshold)
                    .get(FIRST_INDEX);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public List<MethodModel> detect(List<MethodModel> methodModels, Long threshold) {
        return methodModels.stream()
                .filter(methodModel -> isLongParameterMethod(methodModel, threshold))
                .collect(Collectors.toList());
    }

    private Boolean isLongParameterMethod(MethodModel methodModel, Long threshold) {
        return methodModel.getParameters().size() > threshold;
    }
}
