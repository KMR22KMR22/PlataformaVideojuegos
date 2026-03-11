package org.example.Repository.Interface;

import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.LibraryForm;
import org.example.Model.Form.Updates.LibraryUpdateForm;

public interface ILibraryRepo extends ICrud <LibraryEntity, LibraryForm, LibraryUpdateForm, Long>{

}
