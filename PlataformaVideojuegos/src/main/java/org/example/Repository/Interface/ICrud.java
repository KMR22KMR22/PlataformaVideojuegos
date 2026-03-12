package org.example.Repository.Interface;

import java.util.List;
import java.util.Optional;


//Definición de una interfaz genérica para operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
//E representa la entidad, F el DTO (Data Transfer Object) y ID el tipo del identificador de la entidad.

//Usar esta interfaz para definir contratos claros para los repositorios que manejarán diferentes tipos de entidades y sus correspondientes DTOs.

    public interface ICrud<E, F, O, ID> {
        /**
         * Crea una nueva entidad a partir de un DTO.
         *
         * @param dto Objeto de transferencia de datos con la información a persistir.
         * @return La entidad creada.
         */
        Optional<E> create(F dto);

        /**
         * Obtiene una entidad por su identificador único.
         * @param id Identificador de la entidad.
         * @return Un Optional con la entidad encontrada, o vacío si no existe.
         */
        Optional<E> getById(ID id);

        /**
         * Obtiene todas las entidades existentes.
         * @return Lista de todas las entidades.
         */
        List<E> getAll();

        /**
         * Actualiza una entidad existente a partir de su identificador y un DTO.
         * @param id Identificador de la entidad a actualizar.
         * @param form Objeto de transferencia de datos con la información actualizada.
         * @return La entidad actualizada.
         */
        Optional<E> update(ID id, O form);


        /**
         * Elimina una entidad por su identificador único.
         * @param id Identificador de la entidad a eliminar.
         * @return true si la entidad fue eliminada, false en caso contrario.
         */
        boolean delete(ID id);
    }



