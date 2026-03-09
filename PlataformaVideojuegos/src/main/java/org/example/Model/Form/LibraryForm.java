package org.example.Model.Form;

import org.example.Model.DTO.Game.GameDTO;
import org.example.Model.DTO.Library.InstalationState;
import org.example.Model.DTO.User.UserDTO;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

public record LibraryForm (Long idUser,
                           Long idGame,
                           LocalDate adquisitionDate){

}
