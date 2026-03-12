package org.example.Repository.Interface;

import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.LibraryForm;
import org.example.Model.Form.Updates.LibraryUpdate;

import java.util.Optional;

public interface ILibraryRepo extends ICrud <LibraryEntity, LibraryForm, LibraryUpdate, Long>{

    public Optional<LibraryEntity> getByUserGameId(Long idUser, Long idGame);
}
