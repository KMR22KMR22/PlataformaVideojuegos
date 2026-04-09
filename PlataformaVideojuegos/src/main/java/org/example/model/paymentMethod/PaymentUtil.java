package org.example.model.paymentMethod;

import org.example.controller.Util;
import org.example.exeptions.ValidationException;
import org.example.model.form.errors.ErrorDto;
import org.example.model.form.errors.ErrorType;

import java.util.List;

public class PaymentUtil {

    public static void chackBalance(float gameCost, float balance) throws ValidationException {
        if (gameCost > balance) {
            Util.thowException(List.of(new ErrorDto("Priece", ErrorType.VALOR_DEMASIADO_ALTO)));
        }
    }
}
