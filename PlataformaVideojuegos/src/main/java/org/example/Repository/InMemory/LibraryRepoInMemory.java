package org.example.Repository.InMemory;

import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.LibraryForm;
import org.example.Repository.Interface.ILibraryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryRepoInMemory implements ILibraryRepo<LibraryEntity, LibraryForm, Long> {

    private final List<LibraryEntity> libraries = new ArrayList<>();
    private Long idCounter = 1L;

    @Override
    public Optional<LibraryEntity> crear(LibraryForm form) {
        var library = new LibraryEntity(idCounter++, form.getIdUser(), form.getIdGame(), form.getAdquisitionDate(), form.getLastPlayed());
        libraries.add(library);
        return Optional.of(library);
    }

    @Override
    public Optional<LibraryEntity> obtenerPorId(Long id) {
        return libraries.stream().filter(library -> library.getId().equals(id)).findFirst();
    }

    @Override
    public List<LibraryEntity> obtenerTodos() {return new ArrayList<>(libraries);
    }

    @Override
    public boolean eliminar(Long aLong) {
        return false;
    }
}
