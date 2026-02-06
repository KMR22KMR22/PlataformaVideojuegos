package Repository.Interface;

import Model.Entidad.LibraryEntity;
import Model.Form.LibraryForm;

public interface ILibraryRepo<P, P1, L extends Number> extends ICrud <LibraryEntity, LibraryForm, Long>{

}
