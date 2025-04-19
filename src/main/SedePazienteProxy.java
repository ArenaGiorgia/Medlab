package main;

import java.util.*;
import java.util.stream.Collectors;

public class SedePazienteProxy implements PazienteProvider {
    private final Medlab medlab;

    public SedePazienteProxy(Medlab medlab) {
        this.medlab = medlab;
    }

    @Override
    public Paziente getPazienteByCF(String cf) {
        Paziente p = medlab.getPazienteByCF(cf);
        PersonaleLaboratorio personale = medlab.getPersonaleLaboratorioCorrente();
        if (p != null && p.getSedi() != null) {
            for (Sede sede : p.getSedi()) {
                if (sede.equals(personale.getSede())) {
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public List<Paziente> getAllPazienti() {
        PersonaleLaboratorio personale = medlab.getPersonaleLaboratorioCorrente();
        return medlab.getPazienti().values().stream()
                .filter(p -> p.getSedi() != null && p.getSedi().stream().anyMatch(s -> s.equals(personale.getSede())))
                .collect(Collectors.toList());
    }



}

