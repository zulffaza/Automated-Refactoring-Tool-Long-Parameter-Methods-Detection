package com.finalproject.automated.refactoring.tool.longg.parameter.methods.detection.service;

import com.finalproject.automated.refactoring.tool.model.MethodModel;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author fazazulfikapp
 * @version 1.0.0
 * @since 7 November 2018
 */

public interface LongParameterMethodsDetection {

    MethodModel detect(@NonNull MethodModel methodModel, @NonNull Long threshold);

    List<MethodModel> detect(@NonNull List<MethodModel> methodModels, @NonNull Long threshold);
}
