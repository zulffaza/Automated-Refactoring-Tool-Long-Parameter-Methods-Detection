package com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service.LongParameterMethodsDetection;
import com.finalproject.automated.refactoring.tool.model.CodeSmellName;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author fazazulfikapp
 * @version 1.0.0
 * @since 7 November 201888
 */

@Service
public class LongParameterMethodsDetectionImpl implements LongParameterMethodsDetection {

    @Override
    public void detect(MethodModel methodModel, Long threshold) {
        detect(Collections.singletonList(methodModel), threshold);
    }

    @Override
    public void detect(List<MethodModel> methodModels, Long threshold) {
        methodModels.parallelStream()
                .filter(methodModel -> isLongParameterMethod(methodModel, threshold))
                .forEach(this::checkMethod);
    }

    private Boolean isLongParameterMethod(MethodModel methodModel, Long threshold) {
        return methodModel.getParameters().size() > threshold;
    }

    private void checkMethod(MethodModel methodModel) {
        methodModel.getCodeSmells()
                .add(CodeSmellName.LONG_PARAMETER_METHOD);
    }
}
