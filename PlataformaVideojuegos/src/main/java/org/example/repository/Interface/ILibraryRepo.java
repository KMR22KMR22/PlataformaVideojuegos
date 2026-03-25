package org.example.repository.Interface;

import org.example.model.entidad.LibraryEntity;
import org.example.model.form.LibraryForm;
import org.example.model.form.updates.LibraryUpdate;

import java.util.Optional;

public interface ILibraryRepo extends ICrud <LibraryEntity, LibraryForm, LibraryUpdate, Long>{

    Optional<LibraryEntity> getByUserGameId(Long idUser, Long idGame);
}
