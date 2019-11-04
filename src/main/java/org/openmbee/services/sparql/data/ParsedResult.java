package org.openmbee.services.sparql.data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

public class ParsedResult {

    private Set<Pair<String, String>> toRead = new HashSet<>();
    private Set<Pair<String, String>> toModify = new HashSet<>();
    private Set<Pair<String, String>> named = new HashSet<>(); //subset of toRead (from named, using named)

    private Set<Pair<String, String>> defaults = new HashSet<>(); //subset of toRead (from, using, with)
    private Set<String> errors = new HashSet<>(); //uri that can't be parsed
    private boolean unsupported; //unsupported operation

    public boolean isUnsupported() {
        return unsupported;
    }

    public void setUnsupported(boolean unsupported) {
        this.unsupported = unsupported;
    }

    public Set<Pair<String, String>> getToRead() {
        return toRead;
    }

    public void setToRead(
        Set<Pair<String, String>> toRead) {
        this.toRead = toRead;
    }

    public Set<Pair<String, String>> getToModify() {
        return toModify;
    }

    public void setToModify(
        Set<Pair<String, String>> toModify) {
        this.toModify = toModify;
    }

    public Set<String> getErrors() {
        return errors;
    }

    public void setErrors(Set<String> errors) {
        this.errors = errors;
    }

    public Set<Pair<String, String>> getNamed() {
        return named;
    }

    public void setNamed(Set<Pair<String, String>> named) {
        this.named = named;
    }

    public Set<Pair<String, String>> getDefaults() {
        return defaults;
    }

    public void setDefaults(
        Set<Pair<String, String>> defaults) {
        this.defaults = defaults;
    }

    public void addToRead(Pair<String, String> add) {
        toRead.add(add);
    }

    public void addToDefaults(Pair<String, String> add) {
        defaults.add(add);
    }

    public void addToModify(Pair<String, String> mod) {
        toModify.add(mod);
    }

    public void addToError(String error) {
        errors.add(error);
    }

    public void addToNamed(Pair<String, String> n) {
        named.add(n);
    }

    public boolean hasNamed() {
        return !named.isEmpty();
    }

    public boolean hasDefaults() {
        return !defaults.isEmpty();
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public void addAllToNamed(Collection<Pair<String, String>> n) {
        named.addAll(n);
    }

    public void addAllToRead(Collection<Pair<String, String>> r) {
        toRead.addAll(r);
    }

    public void addAllToModify(Collection<Pair<String, String>> m) {
        toModify.addAll(m);
    }

    public void addAllToError(Collection<String> e) {
        errors.addAll(e);
    }

    public void addAllToDefaults(Collection<Pair<String, String>> m) {
        defaults.addAll(m);
    }
}
