package org.example.Repository.Interface;

import org.example.Model.Entidad.LibraryEntity;
import org.example.Model.Form.LibraryForm;

public interface ILibraryRepo<P, P1, L extends Number> extends ICrud <LibraryEntity, LibraryForm, Long>{

}
