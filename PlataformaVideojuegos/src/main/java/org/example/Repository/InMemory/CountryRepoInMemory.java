package org.example.Repository.InMemory;

import java.util.ArrayList;
import java.util.List;

public class CountryRepoInMemory{

    private List<String> countries = new ArrayList<>();

    public List<String>  getCountries() {
        return countries;
    }

    public void addCountries(String country) {
        countries.add(country);
    }
}
