package main.java.Repository.Interface;

import main.java.Model.Entidad.LibraryEntity;
import main.java.Model.Form.LibraryForm;

public interface ILibraryRepo<P, P1, L extends Number> extends ICrud <LibraryEntity, LibraryForm, Long>{

}
