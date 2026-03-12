package org.example.Repository.InMemory;

import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.LibraryForm;
import org.example.Model.Form.Updates.LibraryUpdate;
import org.example.Repository.Interface.ILibraryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryRepoInMemory implements ILibraryRepo {

    private static final List<LibraryEntity> LIBRARIES = new ArrayList<>();
    private static Long NEXT_ID = 1L;

    @Override
    public Optional<LibraryEntity> create(LibraryForm form) {
        var library = new LibraryEntity(NEXT_ID++, form.idUser(), form.idGame(), form.adquisitionDate());
        LIBRARIES.add(library);
        return Optional.of(library);
    }

    @Override
    public Optional<LibraryEntity> getById(Long id) {
        return LIBRARIES.stream().filter(library -> library.getId().equals(id)).findFirst();
    }

    @Override
    public Optional<LibraryEntity> getByUserGameId(Long idUser, Long idGame) {
        return getAll().stream()
                .filter(l -> l.getIdUser() == idUser && l.getIdGame() == idGame)
                .findFirst();
    }

    @Override
    public List<LibraryEntity> getAll() {return new ArrayList<>(LIBRARIES);
    }

    @Override
    public Optional<LibraryEntity> update(Long id, LibraryUpdate form) {
        getById(id).orElseThrow(()-> new IllegalArgumentException("Libreria no encontrada"));

        var libraryUpdated = new LibraryEntity(form.id(), form.idUser(), form.idGame(), form.adquisitionDate(), form.timePlaying(), form.lastPlayed(), form.instalationState());
        LIBRARIES.removeIf(p -> p.getId().equals(id));
        LIBRARIES.add(libraryUpdated);
        return Optional.of(libraryUpdated);
    }

    @Override
    public boolean delete(Long id) {
        return LIBRARIES.removeIf(p -> p.getId().equals(id));
    }
}
