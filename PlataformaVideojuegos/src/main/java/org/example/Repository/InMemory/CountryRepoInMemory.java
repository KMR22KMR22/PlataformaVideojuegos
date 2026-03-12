package org.example.Repository.InMemory;

import org.example.Model.Entidad.Country;
import org.example.Repository.Interface.ICountryRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CountryRepoInMemory implements ICountryRepo {

    public static final List<Country> COUNTRIES = new ArrayList<>();
    public static Long next_id = 1L;


    @Override
    public Optional<Country> create(String name) {
        Country country = new Country(next_id++, name);
        COUNTRIES.add(country);
        return Optional.of(country);
    }

    @Override
    public Optional<Country> getById(Long id) {
        return COUNTRIES.stream().filter(c -> c.id() == id).findFirst();
    }

    @Override
    public List<Country> getAll() {
        return new ArrayList<>(COUNTRIES);
    }

    @Override
    public Optional<Country> update(Long aLong, Country form) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Long id) {
        return COUNTRIES.removeIf(u -> u.id().equals(id));
    }
}
