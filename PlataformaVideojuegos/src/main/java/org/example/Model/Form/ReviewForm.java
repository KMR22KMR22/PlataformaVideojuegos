package org.example.Model.Form;

import org.example.Controller.Util;
import org.example.Model.Form.Errors.ErrorDto;
import org.example.Model.Form.Errors.ErrorType;

import java.util.ArrayList;
import java.util.List;

public record ReviewForm (Long idUser,
                          Long idGame,
                          boolean recommended,
                          String reviwText,
                          Long hoursPlayed){



    public List<ErrorDto> validate(){
        List<ErrorDto> errors = new ArrayList<>();

        if(idUser() == null){
            errors.add(new ErrorDto("IdUser", ErrorType.REQUERIDO));
        }
        if(idGame() == null){
            errors.add(new ErrorDto("IdGame", ErrorType.REQUERIDO));
        }
        if(Util.checkCadenaBlankOrEmpty(reviwText)){
            errors.add(new ErrorDto("ReviewText", ErrorType.REQUERIDO));
        }
        if(reviwText.length() < 50){
            errors.add(new ErrorDto("ReviewText", ErrorType.VALOR_DEMASIADO_BAJO));
        }
        if(reviwText.length() > 8000){
            errors.add(new ErrorDto("ReviewText", ErrorType.VALOR_DEMASIADO_ALTO));
        }
        if(hoursPlayed == null){
            errors.add(new ErrorDto("HoursPlayed", ErrorType.REQUERIDO));
        }
        if(hoursPlayed < 0){
            errors.add(new ErrorDto("HoursPlayed", ErrorType.VALOR_DEMASIADO_BAJO));
        }

        return errors;
    }

}
